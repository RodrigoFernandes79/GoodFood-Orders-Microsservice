package com.goodfood.orders.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderAMQPConfiguration {
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue queueDetailsOrder() {
        return QueueBuilder
                .nonDurable("payment.details-order")
                .deadLetterExchange("payments.dlx") //configuring dlx Exchange
                .build();
    }

    @Bean
    public Queue queueDlqDetailsOrder() {
        return QueueBuilder
                .nonDurable("payment.details-order-dlq")
                .build();
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder
                .fanoutExchange("payments.exchange")
                .build();
    }

    @Bean
    public FanoutExchange deadLetterExchange() {
        return ExchangeBuilder
                .fanoutExchange("payments.dlx")
                .build();
    }

    @Bean
    public Binding bindPaymentOrder() {
        return BindingBuilder
                .bind(queueDetailsOrder())
                .to(fanoutExchange());
    }

    @Bean
    public Binding bindDlqPaymentOrder() {
        return BindingBuilder
                .bind(queueDlqDetailsOrder())
                .to(deadLetterExchange());
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

}
