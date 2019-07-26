package com.sas.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.sas.rabbitmq.utils.RabbitmqUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * mq消息发送助手
 * @author liuyongping
 * @version 1.0
 * @created at 2019/7/22 10:56
 */
@Component
public class SenderHelper {


	private ExecutorService service = Executors.newFixedThreadPool(10);
	private Logger logger = Logger.getLogger(SenderHelper.class);

	@Autowired
	RabbitmqUtil rabbitmqUtil;

	//持久队列(队列将在服务器重启后继续存在)
	boolean durable = true;
	//声明一个独占队列(仅限于此连接)
	boolean exclusive = false;
	//声明一个autoDelete队列(服务器将在不再使用时删除它)
	boolean autoDelete = false;

	public SenderHelper() {
	}

	/**
	 * 向MQ发送JSON数据 (点对点模式)
	 *
	 * @param queueName 队列名称
	 * @param messages  消息簇
	 */
	public void sendBySimple(String queueName, String[] messages) {

		try (Connection connection = rabbitmqUtil.getConnection()) {
			try (
					Channel channel = rabbitmqUtil.createChannel(connection, queueName, durable,
							exclusive, autoDelete, null);
			) {
				rabbitmqUtil.sendMessage(channel, "", queueName, null, messages);
				logger.info("生产者启动,当前模式为[[点对点模式]]");
				logger.info("生产者向队列 [" + queueName + "] 发送消息成功");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("生产者向队列 [" + queueName + "] 发送消息失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("生产者向队列 [" + queueName + "] 发送消息失败");
		}
	}

	/**
	 * 向MQ发送JSON数据 (点对点模式)
	 *
	 * @param queueName 队列名称
	 * @param messages  消息簇
	 * @param arguments 队列的其他属性(构造参数)
	 * @param props     消息路由头等的其他属性
	 */
	public void sendBySimple(String queueName, String[] messages, Map<String, Object> arguments, AMQP.BasicProperties props) {

		try (Connection connection = rabbitmqUtil.getConnection()) {

			try (
					Channel channel = rabbitmqUtil.createChannel(connection, queueName, durable,
							exclusive, autoDelete, arguments);
			) {

				rabbitmqUtil.sendMessage(channel, "", queueName, props, messages);
				logger.info("生产者启动,当前模式为[[点对点模式]]");
				logger.info("生产者向队列 [" + queueName + "] 发送消息成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向MQ发送message数据 (广播模式)
	 * <p>
	 * 交换机的广播类型fanout，广播类型不需要routingKey，交换机会将所有的消息都发送到每个绑定的队列中去。
	 * <p>
	 * 在发布消息时可以只先指定交换机的名称，交换机的声明的代码可以放到消费者端进行声明，队列的声明也放在消费者端来声明。
	 * <p>
	 * 发布订阅类似观察者模式设计模式，一般适用于当接收到某条消息时同时做多种类似的任务的处理，如一个发短信，另一个一个发邮件；
	 * 一个插入数据库，另一个保存在文件等类似操作，扇形交换机将消息传送给不同的队列，不同的队列对同一种消息采取不同的行为。
	 * <p>
	 * 扇形交换机是最基本的交换机类型，它所能做的事情非常简单———广播消息。扇形交换机会把能接收到的消息全部发送给绑定在自己身上
	 * 的队列。因为广播不需要“思考”，所以扇形交换机处理消息的速度也是所有的交换机类型里面最快的。
	 *
	 * @param messages 消息簇
	 */
	public void sendByFanout(String exchangeName, String[] messages) {

		try (Connection connection = rabbitmqUtil.getConnection()) {

			try (
					Channel channel = rabbitmqUtil.createChannel(connection, exchangeName, BuiltinExchangeType.FANOUT,
							exchangeName, null, durable,
							exclusive, autoDelete, null);
			) {
				rabbitmqUtil.sendMessage(channel, exchangeName, "", null, messages);
				logger.info("生产者启动,当前模式为[[广播模式]]");
				logger.info("生产者向交换机 [" + exchangeName + "] 发送消息成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 向MQ发送message数据 (广播模式)
	 * <p>
	 * 交换机的广播类型fanout，广播类型不需要routingKey，交换机会将所有的消息都发送到每个绑定的队列中去。
	 * <p>
	 * 在发布消息时可以只先指定交换机的名称，交换机的声明的代码可以放到消费者端进行声明，队列的声明也放在消费者端来声明。
	 * <p>
	 * 发布订阅类似观察者模式设计模式，一般适用于当接收到某条消息时同时做多种类似的任务的处理，如一个发短信，另一个一个发邮件；
	 * 一个插入数据库，另一个保存在文件等类似操作，扇形交换机将消息传送给不同的队列，不同的队列对同一种消息采取不同的行为。
	 * <p>
	 * 扇形交换机是最基本的交换机类型，它所能做的事情非常简单———广播消息。扇形交换机会把能接收到的消息全部发送给绑定在自己身上
	 * 的队列。因为广播不需要“思考”，所以扇形交换机处理消息的速度也是所有的交换机类型里面最快的。
	 *
	 * @param exchangeName 交换机名称
	 * @param messages     消息
	 * @param arguments    队列的其他属性(构造参数)
	 * @param props        消息路由头等的其他属性
	 */
	public void sendByFanout(String exchangeName, String[] messages, Map<String, Object> arguments,
							 AMQP.BasicProperties props) {

		try (Connection connection = rabbitmqUtil.getConnection()) {

			try (
					Channel channel = rabbitmqUtil.createChannel(connection, exchangeName, BuiltinExchangeType.FANOUT,
							exchangeName, null, durable,
							exclusive, autoDelete, arguments);
			) {
				rabbitmqUtil.sendMessage(channel, exchangeName, "", props, messages);
				logger.info("生产者启动,当前模式为[[广播模式]]");
				logger.info("生产者向交换机 [" + exchangeName + "] 发送消息成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 向MQ发送message数据 (work模式)
	 * <p>
	 * 多个生产者，多个消费者模式
	 *
	 * @param queueName 队列名称
	 * @param messages  消息簇
	 */
	public void sendByWork(String queueName, String[] messages) {

		try (Connection connection = rabbitmqUtil.getConnection()) {

			try (
					Channel channel = rabbitmqUtil.createChannel(connection, queueName, durable,
							exclusive, autoDelete, null);
			) {
				rabbitmqUtil.sendMessage(channel, "", queueName, null, messages);
				logger.info("生产者启动,当前模式为[[work模式]]");
				logger.info("生产者向交换机 [" + queueName + "] 发送消息成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向MQ发送message数据 (work模式)
	 * 多个个生产者，多个消费者模式
	 *
	 * @param queueName 队列名称
	 * @param messages  消息簇
	 * @param arguments 队列的其他属性(构造参数)
	 * @param props     消息路由头等的其他属性
	 */
	public void sendByWork(String queueName, String[] messages, Map<String, Object> arguments,
						   AMQP.BasicProperties props) {

		try (Connection connection = rabbitmqUtil.getConnection()) {

			try (
					Channel channel = rabbitmqUtil.createChannel(connection, queueName, durable,
							exclusive, autoDelete, arguments);
			) {
				rabbitmqUtil.sendMessage(channel, "", queueName, props, messages);
				logger.info("生产者启动,当前模式为[[work模式]]");
				logger.info("生产者向交换机 [" + queueName + "] 发送消息成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 向MQ发送message数据 (routing模式)
	 * <p>
	 * 使用直连接类型，将多个路由键绑定到同一个队列上。也可以将同一个键绑定到多个队列上	 * (多重绑定multiple bindings)，
	 * 此时满足键的队列都能收到消息，不满足的直接被丢弃。
	 * direct
	 *
	 * @param exchangeName 队列名称
	 * @param messages     消息簇
	 */
	public void sendByRouting(String exchangeName, String[] routingKeys, String[] messages) {

		if (routingKeys == null || routingKeys.length <= 0) {
			return;
		}
		if ("".equals(exchangeName.trim())) {
			return;
		}
		try (Connection connection = rabbitmqUtil.getConnection()) {
			try (
					Channel channel = rabbitmqUtil.createChannel(connection, exchangeName, BuiltinExchangeType.DIRECT);
			) {
				for (int i = 0; i < routingKeys.length; i++) {

					rabbitmqUtil.sendMessage(channel, exchangeName, routingKeys[i], null, messages);
				}
				logger.info("生产者启动,当前模式为[[routing模式]]");
				logger.info("生产者向交换机 [" + exchangeName + "] 发送消息成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向MQ发送message数据 (Topic模式)
	 * <p>
	 * 主题类型topic，直连接类型direct必须是生产者发布消息指定的routingKey和消费者在队列绑定
	 * 时指定的routingKey完全相等时才能匹配到队列上，与direct不同,topic可以进行模糊匹配，可以使用星号*和井号#这两个通配符来进
	 * 行模糊匹配，其中星号可以代替一个单词；主题类型的转发器的消息不能随意的设置选择键（routing_key），必须是由点隔开的一系列
	 * 的标识符组成。标识符可以是任何东西，但是一般都与消息的某些特性相关。一些合法的选择键的例子：”quick.orange.rabbit”,你可
	 * 以定义任何数量的标识符，上限为255个字节。 #井号可以替代零个或更多的单词，只要能模糊匹配上就能将消息映射到队列中。当一个
	 * 队列的绑定键为#的时候，这个队列将会无视消息的路由键，接收所有的消息
	 *
	 * @param exchangeName 交换机名称
	 * @param messages     消息簇
	 */
	public void sendByTopic(String exchangeName, String[] routingKeys, String[] messages) {

		if (routingKeys == null || routingKeys.length <= 0) {
			return;
		}
		if ("".equals(exchangeName.trim())) {
			return;
		}
		try (Connection connection = rabbitmqUtil.getConnection()) {

			try (
					Channel channel = rabbitmqUtil.createChannel(connection, exchangeName, BuiltinExchangeType.TOPIC);
			) {
				for (int i = 0; i < routingKeys.length; i++) {
					rabbitmqUtil.sendMessage(channel, exchangeName, routingKeys[i], null, messages);
				}
				logger.info("生产者启动,当前模式为[[Topic模式]]");
				logger.info("生产者向交换机 [" + exchangeName + "] 发送消息成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
