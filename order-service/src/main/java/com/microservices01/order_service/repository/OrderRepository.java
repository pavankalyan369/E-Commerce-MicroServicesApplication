package com.microservices01.order_service.repository;

import com.microservices01.order_service.entity.Orderr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orderr,Long> {

}
