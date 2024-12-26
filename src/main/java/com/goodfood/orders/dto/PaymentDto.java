package com.goodfood.orders.dto;

import com.goodfood.orders.model.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PaymentDto {

    private Long id;
    private BigDecimal value;
    private String name;
    private String cardNumber;
    private String expiration;
    private String code;
    private StatusPayment status;
    private Long orderId;
    private Long paymentMethodId;
    private List<OrderItem> itens;
}

