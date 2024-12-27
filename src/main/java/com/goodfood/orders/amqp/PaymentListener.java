package com.goodfood.orders.amqp;

import com.goodfood.orders.dto.PaymentDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {

    @RabbitListener(queues = "payment.details-order")
    public void receiveMessage(@Payload PaymentDto paymentDto) {
        System.out.println(paymentDto.getId());
        System.out.println(paymentDto.getName());
        System.out.println(paymentDto.getCardNumber());
        if (paymentDto.getCardNumber().equals("0000")) {
            throw new RuntimeException("Payment not processed");
        }

        System.out.println("""
                Payment data: %s,
                Customer name: %s,
                Order number: %s,
                Value: R$ %s,
                Payment Status: %s
                """.formatted(paymentDto.getId(),
                paymentDto.getName(),
                paymentDto.getOrderId(),
                paymentDto.getValue(),
                paymentDto.getStatus()));
    }

}
