package com.microservices01.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        var key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public Converter<Jwt, Mono<JwtAuthenticationToken>> jwtAuthConverter() {
        return (Jwt jwt) -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles == null) {
                Map<String, Object> realmAccess = jwt.getClaim("realm_access");
                if (realmAccess != null) {
                    Object ra = realmAccess.get("roles");
                    if (ra instanceof Collection<?> c) {
                        roles = c.stream()
                                .filter(String.class::isInstance)
                                .map(String.class::cast)
                                .toList();
                    }
                }
            }
            if (roles == null) roles = List.of();

            var authorities = roles.stream()
                    .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            String name = jwt.getClaimAsString("userId");
            if (name == null || name.isBlank()) name = jwt.getSubject();
            if (name == null || name.isBlank()) name = jwt.getClaimAsString("preferred_username");
            if (name == null || name.isBlank()) name = "user";

            return Mono.just(new JwtAuthenticationToken(jwt, authorities, name));
        };
    }

    @Bean
    public SecurityWebFilterChain security(ServerHttpSecurity http,
                                           Converter<Jwt, Mono<JwtAuthenticationToken>> converter) {

        // ---------- user-service ownership-based matchers ----------
        var getUserById = new PathPatternParserServerWebExchangeMatcher(
                "/user-service/users/{id}", HttpMethod.GET);
        var updateUser = new PathPatternParserServerWebExchangeMatcher(
                "/user-service/users/{id}", HttpMethod.PUT);
        var deleteUser = new PathPatternParserServerWebExchangeMatcher(
                "/user-service/users/{id}", HttpMethod.DELETE);
        var patchUserAnything = new PathPatternParserServerWebExchangeMatcher(
                "/user-service/users/{id}/**", HttpMethod.PATCH);

        // ---------- order-service ownership matcher ----------
        var getOrdersByUser = new PathPatternParserServerWebExchangeMatcher(
                "/order-service/orders/user/{userId}", HttpMethod.GET);

        // ---------- NEW: cart-service ownership matcher ----------
        // any method under /cart-service/cart/{userId}/...
        var cartByUser = new PathPatternParserServerWebExchangeMatcher(
                "/cart-service/cart/{userId}/**");

        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);

        http.authorizeExchange(ex -> ex
                // -------------- auth endpoints --------------
                .pathMatchers("/auth/admin/**").hasRole("ADMIN")  // admin-only endpoints
                .pathMatchers("/auth/**").permitAll()             // register user, login

                // -------------- user-service --------------
                .matchers(getUserById).access(ownsByPathVar(getUserById, "id"))
                .matchers(updateUser).access(ownsByPathVar(updateUser, "id"))
                .matchers(deleteUser).access(ownsByPathVar(deleteUser, "id"))
                .matchers(patchUserAnything).access(ownsByPathVar(patchUserAnything, "id"))

                .pathMatchers(HttpMethod.GET, "/user-service/users/all").hasRole("ADMIN")
                .pathMatchers(HttpMethod.POST, "/user-service/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/user-service/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/user-service/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PATCH, "/user-service/**").authenticated()
                .pathMatchers("/user-service/**").authenticated()

                // -------------- product-service --------------
                .pathMatchers(HttpMethod.GET, "/product-service/products/**")
                .hasAnyRole("USER", "ADMIN")
                .pathMatchers(HttpMethod.POST, "/product-service/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/product-service/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PATCH, "/product-service/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/product-service/**").hasRole("ADMIN")
                .pathMatchers("/product-service/**").authenticated()

                // -------------- order-service --------------
                .pathMatchers(HttpMethod.GET, "/order-service/orders/all").hasRole("ADMIN")
                .matchers(getOrdersByUser).access(ownsByPathVar(getOrdersByUser, "userId"))
                .pathMatchers("/order-service/**").hasAnyRole("USER", "ADMIN")

                // -------------- NEW: cart-service --------------
                // only owner (or ADMIN) can access /cart-service/cart/{userId}/...
                .matchers(cartByUser).access(ownsByPathVar(cartByUser, "userId"))
                // in practice, all our cart endpoints are under /cart/cart/{userId} or /cart/cart/{userId}/items...
                .pathMatchers("/cart-service/**").hasAnyRole("USER", "ADMIN")

                // -------------- everything else --------------
                .anyExchange().authenticated()
        );

        http.oauth2ResourceServer(oauth -> oauth.jwt(j -> j.jwtAuthenticationConverter(converter)));

        return http.build();
    }

    // ---------- ownership helpers ----------

    private ReactiveAuthorizationManager<AuthorizationContext> ownsByPathVar(
            ServerWebExchangeMatcher matcher, String varName) {

        return (authentication, context) ->
                matcher.matches(context.getExchange())
                        .flatMap(match -> {
                            if (!match.isMatch()) return Mono.just(new AuthorizationDecision(false));
                            String requestedId = Objects.toString(match.getVariables().get(varName), null);
                            return authentication
                                    .map(auth -> new AuthorizationDecision(isAdmin(auth) || idsEqual(auth, requestedId)))
                                    .defaultIfEmpty(new AuthorizationDecision(false));
                        });
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    private boolean idsEqual(Authentication auth, String requestedId) {
        if (!(auth instanceof JwtAuthenticationToken token)) return false;
        if (requestedId == null) return false;
        String userId = token.getToken().getClaimAsString("userId");
        if (userId == null || userId.isBlank()) userId = token.getName();
        return requestedId.equals(userId);
    }
}
