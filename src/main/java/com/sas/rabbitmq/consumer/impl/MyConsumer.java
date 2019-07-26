package com.sas.rabbitmq.consumer.impl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.sas.rabbitmq.BaseConsumer;

import java.io.IOException;

/**
 * @author liuyongping
 * @version 1.0
 * @created at 2019/7/24 20:04
 */
public class MyConsumer implements BaseConsumer {
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
				String message = new String(body, "UTF-8");
				System.out.println(message);
				channel.basicAck(envelope.getDeliveryTag(), false);
				//				if (message.contains(":3")){
				//					// requeue：重新入队列，true: 重新放入队列
				//					// channel.basicReject(envelope.getDeliveryTag(), true);
				//					//重新投递 指定投递的消息是否允许当前消费者消费。
				//					channel.basicRecover(true);
				//
				//				} else {
				//				channel.basicAck(envelope.getDeliveryTag(), false);
				//				}
			}
		};
		this.setConsumer(defaultConsumer);
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
