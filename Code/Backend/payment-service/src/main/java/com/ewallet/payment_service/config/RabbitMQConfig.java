package com.ewallet.payment_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.startPayment.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.startPayment.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.startPayment.key}")
    private String startProjectRoutingKey;

    @Value("${rabbitmq.queue.refund.name}")
    private String refundQueueName;

    @Value("${rabbitmq.routing.refund.key}")
    private String refundRoutingKey;

    @Bean
    public Queue queue(){
        return new Queue(queueName);
    }

    @Bean
    public Queue refundQueue(){
        return new Queue(refundQueueName);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchangeName);
    }
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(exchange()).with(startProjectRoutingKey);
    }

    @Bean
    public Binding refundBinding(){
        return BindingBuilder.bind(refundQueue()).to(exchange()).with(refundRoutingKey);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
