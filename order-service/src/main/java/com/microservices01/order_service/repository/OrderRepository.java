package com.microservices01.order_service.repository;

import com.microservices01.order_service.entity.Orderr;
import com.microservices01.order_service.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orderr, Long> {

    List<Orderr> findByUserId(Long userId);

    List<Orderr> findByStatus(OrderStatus status);
}
