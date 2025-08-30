package com.baduk.baduk.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baduk.baduk.constants.rabbitmq.RabbitMQconstant;



@Configuration
@EnableRabbit
public class RabbitNotificationConfig {
	
	@Bean
	public TopicExchange notificationExchange() {
		return new TopicExchange(RabbitMQconstant.NOTIFICATION.EXCHANGE);
	}
	
	@Bean
	public Queue notificationQueue() {
		return new Queue(RabbitMQconstant.NOTIFICATION.QUEUE, true);
	}
	
	@Bean
	public Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange) {
		return BindingBuilder.bind(notificationQueue)
				.to(notificationExchange)
				.with(RabbitMQconstant.NOTIFICATION.ROUTING_KEY);
	}
	
	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
	    return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
	    RabbitTemplate template = new RabbitTemplate(connectionFactory);
	    template.setMessageConverter(jackson2JsonMessageConverter());
	    return template;
	}
}