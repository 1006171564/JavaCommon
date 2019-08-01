package com.sas.utils.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * @Auther: liuyongping
 * @Date: 2019/7/19 10:33
 * @Description: 消费者接口
 */
public interface BaseConsumerable {

	/**
	 * 初始化消费者参数
	 *
	 * @param queueName 队列名称
	 * @param autoAck   自动应答
	 */
	void initConsumer(String queueName, boolean autoAck);

	/**
	 * 初始化消费者参数
	 *
	 * @param queueName   队列名称
	 * @param autoAck     交换机名称
	 * @param routingKeys 路由键
	 * @param autoAck     自动应答
	 */
	void initConsumer(String queueName, String exchangeName, String[] routingKeys, boolean autoAck);

	/**
	 * 执行成功后的操作
	 *
	 * @param consumerTag
	 * @param envelope
	 * @param properties
	 * @param body
	 */
	void DoHandleSuccess(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body);

	/**
	 * 执行失败后的操作
	 *
	 * @param consumerTag
	 * @param envelope
	 * @param properties
	 * @param body
	 */
	void DoHandleFaild(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body);

	/**
	 * 执行消息操作
	 * @param consumerTag
	 * @param envelope
	 * @param properties
	 * @param body
	 * @return
	 */
	Boolean DoHandle(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) ;

	/**
	 * 初始化消息处理助手
	 *
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
