package com.microservices01.order_service.controller;

import com.microservices01.order_service.dto.OrderDto;
import com.microservices01.order_service.dto.OrderRequestDto;
import com.microservices01.order_service.entity.OrderStatus;
import com.microservices01.order_service.entity.PaymentStatus;
import com.microservices01.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OderController {

    @Autowired
    private OrderService orderService;

    // ------------ CREATE ------------ //

    @PostMapping("/create")
    public OrderDto createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return orderService.createOrder(orderRequestDto);
    }

    // ------------ READ ------------ //

    @GetMapping("/all")
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/user/{userId}")
    public List<OrderDto> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @GetMapping("/status/{status}")
    public List<OrderDto> getOrdersByStatus(@PathVariable OrderStatus status) {
        return orderService.getOrdersByStatus(status);
    }

    // ------------ DELETE ------------ //

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    // ------------ PATCH: STATUS / CANCEL ------------ //

    @PatchMapping("/{id}/status")
    public OrderDto patchStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return orderService.updateStatus(id, status);
    }

    @PostMapping("/{id}/cancel")
    public OrderDto cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    // ------------ PATCH: SHIPPING (adress, pin, phone) ------------ //

    @PatchMapping("/{id}/shipping")
    public OrderDto patchShipping(
            @PathVariable Long id,
            @RequestParam(required = false) String adress,
            @RequestParam(required = false) Long pin,
            @RequestParam(required = false) String phone
    ) {
        return orderService.patchShipping(id, adress, pin, phone);
    }

    // ------------ PATCH: PAYMENT STATUS ------------ //

    @PatchMapping("/{id}/payment-status")
    public OrderDto patchPaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus paymentStatus
    ) {
        return orderService.patchPaymentStatus(id, paymentStatus);
    }

    // ------------ PATCH: TRACKING NUMBER ------------ //

    @PatchMapping("/{id}/tracking")
    public OrderDto patchTracking(
            @PathVariable Long id,
            @RequestParam String trackingNumber
    ) {
        return orderService.patchTrackingNumber(id, trackingNumber);
    }
}
