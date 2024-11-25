package com.goodfood.orders.service;

import com.goodfood.orders.dto.OrderDto;
import com.goodfood.orders.dto.StatusDto;
import com.goodfood.orders.model.Order;
import com.goodfood.orders.model.Status;
import com.goodfood.orders.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private final ModelMapper modelMapper;

    public List<OrderDto> findAll() {
        return repository.findAll().stream()
                .map(p -> modelMapper.map(p, OrderDto.class))
                .collect(Collectors.toList());
    }

    public OrderDto getOrderById(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(order, OrderDto.class);
    }

    public OrderDto createOrder(OrderDto dto) {
        Order order = modelMapper.map(dto, Order.class);

        order.setDateHour(LocalDateTime.now());
        order.setStatus(Status.DONE);
        order.getItens().forEach(item -> item.setOrder(order));
        repository.save(order);

        return modelMapper.map(order, OrderDto.class);
    }

    public OrderDto updateOrderStatus(Long id, StatusDto dto) {

        Order order = repository.searchForIdWithItems(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(dto.getStatus());
        repository.updateStatus(dto.getStatus(), order);
        return modelMapper.map(order, OrderDto.class);
    }

    public void approvePaymentOrder(Long id) {

        Order order = repository.searchForIdWithItems(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(Status.PAID);
        repository.updateStatus(Status.PAID, order);
    }
}
