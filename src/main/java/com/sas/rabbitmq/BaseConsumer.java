package com.sas.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;

/**
 * @Auther: liuyongping
 * @Date: 2019/7/19 10:33
 * @Description: 消费者接口
 */
public interface BaseConsumer {

	/**
	 * 初始化消费者参数
	 * @param queueName 队列名称
	 * @param autoAck 自动应答
	 */
	void initConsumer(String queueName, boolean autoAck);

	/**
	 * 初始化消费者参数
	 * @param queueName 队列名称
	 * @param autoAck 交换机名称
	 * @param routingKeys 路由键
	 * @param autoAck 自动应答
	 */
	void initConsumer(String queueName, String exchangeName, String[] routingKeys, boolean autoAck);


	/**
	 * 初始化消息处理助手
	 * @param channel 连接通道
	 */
	void initHandleDelivery(Channel channel);

	DefaultConsumer getConsumer();
	Channel getChannel();
	String getQueueName();
	String getExchangeName();
	String[] getRoutingKeys();
	boolean isAutoAck();

}
