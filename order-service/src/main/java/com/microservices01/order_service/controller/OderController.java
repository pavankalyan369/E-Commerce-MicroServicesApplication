package com.microservices01.order_service.controller;

import com.microservices01.order_service.dto.OrderDto;
import com.microservices01.order_service.entity.Orderr;
import com.microservices01.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public void createOrder(@RequestBody OrderDto orderDto){
        orderService.createOrder(orderDto);
    }
    @GetMapping("/all")
    public List<Orderr> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Orderr getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }

    @GetMapping("/pagination/{pageNo}/{pageSize}")
    public Page<Orderr> getAllOrders(
            @PathVariable int pageNo,
            @PathVariable int pageSize) {
        return orderService.getAllOrders(pageNo, pageSize);
    }


    @GetMapping("/sorted/{field}")
    public List<Orderr> getAllSortedOrder(@PathVariable String field){
        return orderService.getAllSortedOrders(field);
    }

}
