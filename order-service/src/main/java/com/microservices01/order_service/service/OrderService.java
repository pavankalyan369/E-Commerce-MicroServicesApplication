package com.microservices01.order_service.service;

import com.microservices01.order_service.client.ProductClient;
import com.microservices01.order_service.client.UserClient;
import com.microservices01.order_service.dto.OrderDto;
import com.microservices01.order_service.dto.OrderRequestDto;
import com.microservices01.order_service.dto.ProductDto;
import com.microservices01.order_service.dto.UserDto;
import com.microservices01.order_service.entity.OrderStatus;
import com.microservices01.order_service.entity.Orderr;
import com.microservices01.order_service.entity.PaymentStatus;
import com.microservices01.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    // ------------ CREATE ------------ //

    public OrderDto createOrder(OrderRequestDto req) {

        UserDto user = userClient.getUserById(req.getUserId());
        ProductDto product = productClient.getProductById(req.getProductId());

        if (product.getStock() != null && product.getStock() < req.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product id: " + product.getId());
        }

        Long totalAmount = req.getQuantity() * product.getPrice();

        Orderr order = Orderr.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .userPhone(user.getPhone())
                .adress(user.getAdress())
                .pin(user.getPin())

                .productId(product.getId())
                .productName(product.getName())
                .productType(product.getType())

                .quantity(req.getQuantity())
                .price(product.getPrice())
                .totalAmount(totalAmount)

                .status(OrderStatus.CREATED)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMode(req.getPaymentMode())
                .trackingNumber(null)
                .build()
                ;

        Orderr saved = orderRepo.save(order);

        // NOTE: here you *could* call product-service PATCH /products/{id}/stock?delta=-quantity
        // via another Feign client if you want to auto-reduce stock.

        return mapToDto(saved);
    }

    // ------------ READ ------------ //

    public List<OrderDto> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto getOrderById(Long id) {
        Orderr order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return mapToDto(order);
    }

    public List<OrderDto> getOrdersByUserId(Long userId) {
        return orderRepo.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<OrderDto> getOrdersByStatus(OrderStatus status) {
        return orderRepo.findByStatus(status)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ------------ DELETE ------------ //

    public void deleteOrder(Long id) {
        if (!orderRepo.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepo.deleteById(id);
    }

    // ------------ PATCH HELPERS ------------ //

    public OrderDto updateStatus(Long id, OrderStatus status) {
        Orderr order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.setStatus(status);
        return mapToDto(orderRepo.save(order));
    }

    public OrderDto cancelOrder(Long id) {
        Orderr order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel order in status: " + order.getStatus());
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setPaymentStatus(PaymentStatus.REFUNDED);  // or keep as PENDING for COD etc.

        return mapToDto(orderRepo.save(order));
    }

    public OrderDto patchShipping(Long id, String adress, Long pin, String phone) {
        Orderr order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (adress != null) order.setAdress(adress);
        if (pin != null) order.setPin(pin);
        if (phone != null) order.setUserPhone(phone);

        return mapToDto(orderRepo.save(order));
    }

    public OrderDto patchPaymentStatus(Long id, PaymentStatus paymentStatus) {
        Orderr order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setPaymentStatus(paymentStatus);
        return mapToDto(orderRepo.save(order));
    }

    public OrderDto patchTrackingNumber(Long id, String trackingNumber) {
        Orderr order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setTrackingNumber(trackingNumber);
        return mapToDto(orderRepo.save(order));
    }

    // ------------ MAPPER ------------ //

    private OrderDto mapToDto(Orderr order) {
        OrderDto dto = new OrderDto();

        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setUserName(order.getUserName());
        dto.setUserEmail(order.getUserEmail());
        dto.setUserPhone(order.getUserPhone());
        dto.setAdress(order.getAdress());
        dto.setPin(order.getPin());

        dto.setProductId(order.getProductId());
        dto.setProductName(order.getProductName());
        dto.setProductType(order.getProductType());

        dto.setQuantity(order.getQuantity());
        dto.setPrice(order.getPrice());
        dto.setTotalAmount(order.getTotalAmount());

        dto.setStatus(order.getStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setPaymentMode(order.getPaymentMode());
        dto.setTrackingNumber(order.getTrackingNumber());

        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        return dto;
    }
}
