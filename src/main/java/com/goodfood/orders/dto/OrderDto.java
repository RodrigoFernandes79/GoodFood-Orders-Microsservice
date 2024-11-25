package com.goodfood.orders.dto;

import com.goodfood.orders.model.OrderItem;
import com.goodfood.orders.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private LocalDateTime dateHour;
    private Status status;
    private List<OrderItem> itens = new ArrayList<>();

}
