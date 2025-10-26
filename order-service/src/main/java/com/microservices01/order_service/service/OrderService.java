package com.microservices01.order_service.service;

import com.microservices01.order_service.client.ProductClient;
import com.microservices01.order_service.client.ProductClientRestTemplate;
import com.microservices01.order_service.client.UserClient;
import com.microservices01.order_service.client.UserClientRestTemplate;
import com.microservices01.order_service.dto.OrderDto;
import com.microservices01.order_service.dto.ProductDto;
import com.microservices01.order_service.dto.UserDto;
import com.microservices01.order_service.entity.Orderr;
import com.microservices01.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private UserClientRestTemplate userClient;

    @Autowired
    private ProductClientRestTemplate productClient;

    public void createOrder(OrderDto orderDto){
        UserDto userDto = userClient.getUserById(orderDto.getUserId());
        ProductDto productDto = productClient.getProductById(orderDto.getProductId());
        Orderr order = new Orderr();
        order.setUserId(userDto.getId());
        order.setUserName(userDto.getName());
        order.setProductId(productDto.getId());
        order.setProductName(productDto.getName());
        order.setType(productDto.getType());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice(orderDto.getQuantity()*productDto.getPrice());
        order.setAdress(userDto.getAdress());
        order.setPin(userDto.getPin());

        orderRepo.save(order);
    }

    public List<Orderr> getAllOrders(){
        return orderRepo.findAll();
    }

    public Page<Orderr> getAllOrders(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepo.findAll(pageable);
    }

    public Orderr getOrderById(Long id){
        Optional<Orderr> o = orderRepo.findById(id);
        return o.get();
    }

    public List<Orderr> getAllSortedOrders(String field){
        return orderRepo.findAll(Sort.by(field));
    }

}
