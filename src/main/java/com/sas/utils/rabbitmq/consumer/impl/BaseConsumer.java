package com.sas.utils.rabbitmq.consumer.impl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.sas.utils.rabbitmq.BaseConsumerable;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author liuyongping
 * @version 1.0
 * @created at 2019/7/24 20:04
 */
public class BaseConsumer implements BaseConsumerable {
	private DefaultConsumer consumer;
	private Channel channel;
	private String queueName;
	private String exchangeName;
	private String[] routingKeys;
	private boolean autoAck;


	@Override
	public void initConsumer(String queueName, boolean autoAck) {
		this.setQueueName(queueName);
		this.setAutoAck(autoAck);
	}

	@Override
	public void initConsumer(String queueName, String exchangeName, String[] routingKeys, boolean autoAck) {
		this.setQueueName(queueName);
		this.setAutoAck(autoAck);
		this.setExchangeName(exchangeName);
		this.setRoutingKeys(routingKeys);
	}

	@Override
	public void initHandleDelivery(Channel channel) {
		this.setChannel(channel);
		DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				Boolean isok = DoHandle(consumerTag, envelope, properties, body);
				if (isok) {
					DoHandleSuccess(consumerTag, envelope, properties, body);
				} else {
					DoHandleFaild(consumerTag, envelope, properties, body);
				}
				if (!isAutoAck()) {
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		this.setConsumer(defaultConsumer);
	}

	@Override
	public void DoHandleSuccess(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
		System.out.println("消息执行成功");
	}

	@Override
	public void DoHandleFaild(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
		System.out.println("消息执行失败");
	}

	@Override
	public Boolean DoHandle(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
		String message = null;
		message = new String(body, Charset.defaultCharset());
		System.out.println(message);
		return true;
	}

	@Override
	public DefaultConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(DefaultConsumer consumer) {
		this.consumer = consumer;
	}

	@Override
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	@Override
	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	@Override
	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	@Override
	public String[] getRoutingKeys() {
		return routingKeys;
	}

	public void setRoutingKeys(String[] routingKeys) {
		this.routingKeys = routingKeys;
	}

	@Override
	public boolean isAutoAck() {
		return autoAck;
	}

	public void setAutoAck(boolean autoAck) {
		this.autoAck = autoAck;
	}
}
