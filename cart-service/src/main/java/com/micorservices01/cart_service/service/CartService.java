package com.micorservices01.cart_service.service;


import com.micorservices01.cart_service.client.ProductClient;
import com.micorservices01.cart_service.dto.CartItemRequest;
import com.micorservices01.cart_service.dto.CartItemResponse;
import com.micorservices01.cart_service.dto.CartResponse;
import com.micorservices01.cart_service.dto.ProductDto;
import com.micorservices01.cart_service.entity.CartItem;
import com.micorservices01.cart_service.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartRepo;
    private final ProductClient productClient;

    public CartService(CartItemRepository cartRepo, ProductClient productClient) {
        this.cartRepo = cartRepo;
        this.productClient = productClient;
    }

    // Get cart for a user (with product details)
    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) {
        List<CartItem> items = cartRepo.findByUserId(userId);
        List<CartItemResponse> itemResponses = items.stream()
                .map(this::toItemResponse)
                .toList();

        long totalAmount = itemResponses.stream()
                .mapToLong(CartItemResponse::getLineTotal)
                .sum();

        long totalQty = itemResponses.stream()
                .mapToLong(CartItemResponse::getQuantity)
                .sum();

        CartResponse res = new CartResponse();
        res.setUserId(userId);
        res.setItems(itemResponses);
        res.setTotalAmount(totalAmount);
        res.setTotalQuantity(totalQty);
        return res;
    }

    // Add item or increase quantity if already in cart
    @Transactional
    public CartResponse addItem(Long userId, CartItemRequest req) {
        if (req.getQuantity() == null || req.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        CartItem item = cartRepo.findByUserIdAndProductId(userId, req.getProductId())
                .orElseGet(() -> CartItem.builder()
                        .userId(userId)
                        .productId(req.getProductId())
                        .quantity(0L)
                        .build());

        long newQty = item.getQuantity() + req.getQuantity();
        item.setQuantity(newQty);
        cartRepo.save(item);

        return getCart(userId);
    }

    // Replace the quantity for a given product in cart
    @Transactional
    public CartResponse updateItemQuantity(Long userId, Long productId, Long quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        CartItem item = cartRepo.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        if (quantity == 0) {
            cartRepo.delete(item);
        } else {
            item.setQuantity(quantity);
            cartRepo.save(item);
        }

        return getCart(userId);
    }

    // Remove a single product from cart
    @Transactional
    public CartResponse removeItem(Long userId, Long productId) {
        CartItem item = cartRepo.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        cartRepo.delete(item);
        return getCart(userId);
    }

    // Clear the entire cart
    @Transactional
    public void clearCart(Long userId) {
        cartRepo.deleteByUserId(userId);
    }

    // ---- helper to convert CartItem to CartItemResponse with product data ----

    private CartItemResponse toItemResponse(CartItem item) {
        ProductDto product = productClient.getProductById(item.getProductId());

        CartItemResponse res = new CartItemResponse();
        res.setProductId(product.getId());
        res.setProductName(product.getName());
        res.setProductType(product.getType());
        res.setPrice(product.getPrice());
        res.setQuantity(item.getQuantity());
        res.setLineTotal(item.getQuantity() * product.getPrice());
        return res;
    }
}

