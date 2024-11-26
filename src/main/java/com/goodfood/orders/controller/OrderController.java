package com.goodfood.orders.controller;

import com.goodfood.orders.dto.OrderDto;
import com.goodfood.orders.dto.StatusDto;
import com.goodfood.orders.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService service;


    @GetMapping()
    public List<OrderDto> listAllOrders() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> listOrderById(@PathVariable @NotNull Long id) {
        OrderDto dto = service.getOrderById(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping()
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderDto dto, UriComponentsBuilder uriBuilder) {
        OrderDto orderCreated = service.createOrder(dto);

        URI uri = uriBuilder.path("/orders/{id}").buildAndExpand(orderCreated.getId()).toUri();

        return ResponseEntity.created(uri).body(orderCreated);

    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateStatus(@PathVariable Long id, @RequestBody StatusDto status) {
        OrderDto dto = service.updateOrderStatus(id, status);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/paid")
    public ResponseEntity<Void> approvePayment(@PathVariable @NotNull Long id) {
        service.approvePaymentOrder(id);

        return ResponseEntity.ok().build();

    }
}
