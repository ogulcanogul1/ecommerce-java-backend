package com.ecommerce.project.controllers;

import com.ecommerce.project.dtos.orderDto.OrderDto;
import com.ecommerce.project.dtos.orderDto.OrderRequestDto;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.services.orderService.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<ServiceResult<OrderDto>> createOrder(
            @PathVariable String paymentMethod,
            @RequestBody OrderRequestDto orderRequestDTO){
       ServiceResult<OrderDto> orderDto = orderService.createOrder(
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );

       return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }
}
