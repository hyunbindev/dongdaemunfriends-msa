package com.baduk.baduk.constants.rabbitmq;

import lombok.Getter;

/**
 * rabbitmqconstants
 * queue, exchange, routingKey 관리
 */
public enum RabbitMQconstant {
	NOTIFICATION("notification.queue","notification.exchange","notification");
	
	public final String QUEUE;
	public final String EXCHANGE;
	public final String ROUTING_KEY;
	
	RabbitMQconstant(String queue, String exchange, String routingKey){
		this.QUEUE = queue;
		this.EXCHANGE = exchange;
		this.ROUTING_KEY = routingKey;
	}
}