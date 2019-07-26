package com.sas.rabbitmq.utils;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.sas.rabbitmq.BaseConsumer;
import com.sas.rabbitmq.connection.ConnectionWrapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * mq 工具类
 *
 * @author liuyongping
 * @version 1.0
 * @created at 2019/7/22 10:52
 */
@Component
public class RabbitmqUtil {
	@Autowired
	private ConnectionWrapper factory;//注入连接池对象

	private Logger logger = Logger.getLogger(RabbitmqUtil.class);

	/**
	 * 绑定路由键
	 *
	 * @param channel      传输通道
	 * @param queueName    队列名称
	 * @param exchangeName 交换机名称
	 * @param routingKeys  路由键
	 * @throws IOException
	 */
	public void bindRoutingKeys(Channel channel, String queueName, String exchangeName, String[] routingKeys) throws IOException {
		// 将一个对列绑定多个路由键
		if (routingKeys != null && routingKeys.length > 0) {
			for (int i = 0; i < routingKeys.length; i++) {
				channel.queueBind(queueName, exchangeName, routingKeys[i]);
			}
		}
	}

	/**
	 * 创建通道
	 *
	 * @param connection   连接
	 * @param exchangeName 交换机名称
	 * @param exchangeType 交换类型
	 * @param queueName    队列名称
	 * @param routingKeys  路由键
	 * @param durable      是否将队列持久化
	 * @param exclusive    唯一专属队列
	 * @param autoDelete   当没有消费者连接，是否自动删除队列
	 * @param arguments    队列的其他属性(构造参数)
	 * @return
	 * @throws Exception
	 */
	public Channel createChannel(Connection connection, String exchangeName, BuiltinExchangeType exchangeType,
								 String queueName, String[] routingKeys, boolean durable,
								 boolean exclusive, boolean autoDelete,
								 Map<String, Object> arguments) throws Exception {

		Channel channel = connection.createChannel();

		channel.exchangeDeclare(exchangeName, exchangeType.getType());
		channel.queueDeclare(queueName, durable, exclusive, autoDelete, arguments);
		bindRoutingKeys(channel, queueName, exchangeName, routingKeys);
		logger.info("通道启动初始化成功");
		return channel;
	}

	/**
	 * 创建通道
	 *
	 * @param connection 连接
	 * @param queueName  队列名称
	 * @param durable    是否将队列持久化
	 * @param exclusive  唯一专属队列
	 * @param autoDelete 当没有消费者连接，是否自动删除队列
	 * @param arguments  队列的其他属性(构造参数)
	 * @return
	 * @throws Exception
	 */
	public Channel createChannel(Connection connection, String queueName, boolean durable, boolean exclusive,
								 boolean autoDelete, Map<String, Object> arguments) throws Exception {

		Channel channel = connection.createChannel();
		channel.queueDeclare(queueName, durable, exclusive, autoDelete, arguments);
		logger.info("通道启动初始化成功");
		return channel;
	}

	/**
	 * 创建通道
	 *
	 * @param connection 连接
	 * @return
	 * @throws Exception
	 */
	public Channel createChannel(Connection connection) throws Exception {
		Channel channel = connection.createChannel();
		logger.info("通道启动初始化成功");
		return channel;
	}

	/**
	 * 创建通道
	 *
	 * @param connection   连接
	 * @param exchangeName 交换机名称
	 * @param exchangeType 交换类型
	 * @return
	 * @throws Exception
	 */
	public Channel createChannel(Connection connection, String exchangeName, BuiltinExchangeType exchangeType) throws Exception {
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(exchangeName, exchangeType);
		logger.info("通道启动初始化成功");
		return channel;
	}

	/**
	 * 创建通道
	 *
	 * @param connection   连接
	 * @param exchangeName 交换机名称
	 * @param routingKeys  路由键
	 * @param exchangeType 交换类型
	 * @return
	 * @throws Exception
	 */
	public Channel createChannel(Connection connection, String exchangeName, String[] routingKeys, BuiltinExchangeType exchangeType) throws Exception {
		Channel channel = connection.createChannel();
		String queueName = channel.queueDeclare().getQueue();
		channel.exchangeDeclare(exchangeName, exchangeType);
		// 将一个对列绑定多个路由键
		for (int i = 0; i < routingKeys.length; i++) {
			channel.queueBind(queueName, exchangeName, routingKeys[i]);
		}
		logger.info("通道启动初始化成功");
		return channel;
	}
//	/**
//	 * 发送消息
//	 * 生产者批量确认机制
//	 * @param channel 连接通道
//	 * @param exchangeName 交换机名称
//	 * @param routingKey 路由键
//	 * @param props        消息路由头等的其他属性
//	 * @param messages
//	 * @throws IOException
//	 */
//	public void sendMessage(Channel channel, String exchangeName, String routingKey, AMQP.BasicProperties props,
//							String[] messages) throws IOException {
//		long batchCount = messages.length;
//		long msgCount =0;
//
//		//生产者异步确认机制 （事务机制，普通confirm，批量confirm，异步confirm）可以实现生产者确认  异步效率最高
//		SortedSet<Long> confirmSet = new TreeSet<>();
//		channel.confirmSelect();
//		channel.addConfirmListener(new ConfirmListener() {
//			@Override
//			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
//				System.out.println("Ack,SeqNo：" + deliveryTag + ",multiple：" + multiple);
//				if (multiple) {
//					confirmSet.headSet(deliveryTag - 1).clear();
//				} else {
//					confirmSet.remove(deliveryTag);
//				}
//			}
//
//			@Override
//			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
//				System.out.println("Nack,SeqNo：" + deliveryTag + ",multiple：" + multiple);
//				if (multiple) {
//					confirmSet.headSet(deliveryTag - 1).clear();
//				} else {
//					confirmSet.remove(deliveryTag);
//				}
//				// 注意这里需要添加处理消息重发的场景
//			}
//		});
//		String message;
//		// 发送消息
//		while (msgCount < batchCount) {
//			message = messages[(int) msgCount];
//			long nextSeqNo = channel.getNextPublishSeqNo();
//			channel.basicPublish(exchangeName, routingKey, props, message.getBytes());
//			confirmSet.add(nextSeqNo);
//			msgCount = nextSeqNo;
//		}
//	}

	/**
	 * 发送消息
	 * 生产者单条确认机制
	 *
	 * @param channel      连接通道
	 * @param exchangeName 交换机名称
	 * @param routingKey   路由键
	 * @param props        消息路由头等的其他属性
	 * @param messages
	 * @throws IOException
	 */
	public void sendMessage(Channel channel, String exchangeName, String routingKey, AMQP.BasicProperties props,
							String[] messages) throws IOException {

		// 发送消息
		for (int i = 0; i < messages.length; i++) {
			channel.basicPublish(exchangeName, routingKey, props, messages[i].getBytes());
		}
	}

	/**
	 * 接收消息
	 *
	 * @param consumer 消息接收者
	 * @throws IOException
	 */
	public void reveiveMessage(BaseConsumer consumer) throws IOException {
		consumer.getChannel().basicConsume(consumer.getQueueName(), consumer.isAutoAck(), consumer.getConsumer());
	}

	/**
	 * @return Connection 连接对象
	 * @throws Exception IO异常,连接超时异常
	 */
	public Connection getConnection() throws Exception {

		logger.info("获取ＭＱ服务器连接信息");
		if (factory == null) {
			factory = new ConnectionWrapper();
		}
		return factory.getConnection();
	}

}
