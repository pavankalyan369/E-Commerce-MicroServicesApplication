package com.micorservices01.cart_service.controller;


import com.micorservices01.cart_service.dto.CartItemRequest;
import com.micorservices01.cart_service.dto.CartResponse;
import com.micorservices01.cart_service.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cart;

    public CartController(CartService cart) {
        this.cart = cart;
    }

    // Get cart for a user
    @GetMapping("/{userId}")
    public CartResponse getCart(@PathVariable Long userId) {
        return cart.getCart(userId);
    }

    // Add item (or increase quantity)
    @PostMapping("/{userId}/items")
    public CartResponse addItem(@PathVariable Long userId,
                                @RequestBody CartItemRequest req) {
        return cart.addItem(userId, req);
    }

    // Update quantity (absolute)
    @PatchMapping("/{userId}/items/{productId}")
    public CartResponse updateQuantity(@PathVariable Long userId,
                                       @PathVariable Long productId,
                                       @RequestParam Long quantity) {
        return cart.updateItemQuantity(userId, productId, quantity);
    }

    // Remove one item
    @DeleteMapping("/{userId}/items/{productId}")
    public CartResponse removeItem(@PathVariable Long userId,
                                   @PathVariable Long productId) {
        return cart.removeItem(userId, productId);
    }

    // Clear entire cart
    @DeleteMapping("/{userId}")
    public void clearCart(@PathVariable Long userId) {
        cart.clearCart(userId);
    }
}

