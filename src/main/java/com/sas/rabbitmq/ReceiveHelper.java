package com.sas.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.sas.rabbitmq.utils.RabbitmqUtil;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * mq消息接收助手
 *
 * @author liuyongping
 * @version 1.0
 * @created at 2019/7/22 10:56
 */
public class ReceiveHelper {

	RabbitmqUtil rabbitmqUtil;

	public ReceiveHelper(String host, String port, String virtualHost, String username, String password){
		rabbitmqUtil=new RabbitmqUtil(host,port,virtualHost,username,password);
	}
	//持久队列(队列将在服务器重启后继续存在)
	boolean durable = true;
	//声明一个独占队列(仅限于此连接)
	boolean exclusive = false;
	//声明一个autoDelete队列(服务器将在不再使用时删除它)
	boolean autoDelete = false;
	//消费消息自动应答
	boolean autoAck = false;

	private ExecutorService service = Executors.newFixedThreadPool(10);
	private Logger logger = Logger.getLogger(ReceiveHelper.class);

	/**
	 * 向MQ接收JSON数据 (点对点模式)
	 */
	public void receiveBySimple(BaseConsumerable consumer) {
		String queueName = consumer.getQueueName();
		try {

			Connection connection = rabbitmqUtil.getConnection();
			Channel channel = rabbitmqUtil.createChannel(connection, queueName, durable,
					exclusive, autoDelete, null);
			consumer.initHandleDelivery(channel);
			rabbitmqUtil.reveiveMessage(consumer);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("消费者向队列 [" + queueName + "] 接收消息失败");
		}

	}

	/**
	 * 向MQ接收JSON数据 (点对点模式)
	 *
	 * @param arguments 队列的其他属性(构造参数)
	 */
	public void receiveBySimple(BaseConsumerable consumer, Map<String, Object> arguments) {

		try {
			Connection connection = rabbitmqUtil.getConnection();
			Channel channel = rabbitmqUtil.createChannel(connection, consumer.getQueueName(), durable,
					exclusive, autoDelete, arguments);
			consumer.initHandleDelivery(channel);
			rabbitmqUtil.reveiveMessage(consumer);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("消费者向队列 [" + consumer.getQueueName() + "] 接收消息失败");

		}
	}

	/**
	 * 向MQ接收message数据 (广播模式)
	 * <p>
	 * 交换机的广播类型fanout，广播类型不需要routingKey，交换机会将所有的消息都接收到每个绑定的队列中去。
	 * <p>
	 * 在发布消息时可以只先指定交换机的名称，交换机的声明的代码可以放到消费者端进行声明，队列的声明也放在消费者端来声明。
	 * <p>
	 * 发布订阅类似观察者模式设计模式，一般适用于当接收到某条消息时同时做多种类似的任务的处理，如一个发短信，另一个一个发邮件；
	 * 一个插入数据库，另一个保存在文件等类似操作，扇形交换机将消息传送给不同的队列，不同的队列对同一种消息采取不同的行为。
	 * <p>
	 * 扇形交换机是最基本的交换机类型，它所能做的事情非常简单———广播消息。扇形交换机会把能接收到的消息全部接收给绑定在自己身上
	 * 的队列。因为广播不需要“思考”，所以扇形交换机处理消息的速度也是所有的交换机类型里面最快的。
	 */
	public void receiveByFanout(BaseConsumerable consumer) {

		String exchangeName = consumer.getExchangeName();
		try {
			Connection connection = rabbitmqUtil.getConnection();
			Channel channel = rabbitmqUtil.createChannel(connection, exchangeName, BuiltinExchangeType.FANOUT,
					exchangeName, null, durable,
					exclusive, autoDelete, null);
			consumer.initHandleDelivery(channel);
			rabbitmqUtil.reveiveMessage(consumer);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Fanout消费者向交换机 [" + exchangeName + "] 接收消息失败");
		}

	}


	/**
	 * 向MQ接收message数据 (广播模式)
	 * <p>
	 * 交换机的广播类型fanout，广播类型不需要routingKey，交换机会将所有的消息都接收到每个绑定的队列中去。
	 * <p>
	 * 在发布消息时可以只先指定交换机的名称，交换机的声明的代码可以放到消费者端进行声明，队列的声明也放在消费者端来声明。
	 * <p>
	 * 发布订阅类似观察者模式设计模式，一般适用于当接收到某条消息时同时做多种类似的任务的处理，如一个发短信，另一个一个发邮件；
	 * 一个插入数据库，另一个保存在文件等类似操作，扇形交换机将消息传送给不同的队列，不同的队列对同一种消息采取不同的行为。
	 * <p>
	 * 扇形交换机是最基本的交换机类型，它所能做的事情非常简单———广播消息。扇形交换机会把能接收到的消息全部接收给绑定在自己身上
	 * 的队列。因为广播不需要“思考”，所以扇形交换机处理消息的速度也是所有的交换机类型里面最快的。
	 *
	 * @param arguments 队列的其他属性(构造参数)
	 */
	public void receiveByFanout(BaseConsumerable consumer, Map<String, Object> arguments) {
		String exchangeName = consumer.getExchangeName();
		try {
			Connection connection = rabbitmqUtil.getConnection();
			Channel channel = rabbitmqUtil.createChannel(connection, exchangeName, BuiltinExchangeType.FANOUT,
					exchangeName, null, durable,
					exclusive, autoDelete, arguments);
			consumer.initHandleDelivery(channel);
			rabbitmqUtil.reveiveMessage(consumer);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Fanout消费者向交换机 [" + exchangeName + "] 接收消息失败");
		}
	}


	/**
	 * 向MQ接收message数据 (work模式)
	 * <p>
	 * 多个生产者，多个消费者模式
	 */
	public void receiveByWork(BaseConsumerable consumer) {
		String queueName = consumer.getQueueName();
		try {
			Connection connection = rabbitmqUtil.getConnection();
			Channel channel = rabbitmqUtil.createChannel(connection, queueName, durable,
					exclusive, autoDelete, null);
			//轮询不指定参数就是轮询分发，指定参数就是公平转发
			//channel.basicQos(1);
			consumer.initHandleDelivery(channel);
			rabbitmqUtil.reveiveMessage(consumer);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Work消费者向交换机 [" + queueName + "] 接收消息失败");
		}

	}

	/**
	 * 向MQ接收message数据 (work模式)
	 * 多个个消费者，多个消费者模式
	 *
	 * @param arguments 队列的其他属性(构造参数)
	 */
	public void receiveByWork(BaseConsumerable consumer, Map<String, Object> arguments) {
		String queueName = consumer.getQueueName();
		try {

			Connection connection = rabbitmqUtil.getConnection();
			Channel channel = rabbitmqUtil.createChannel(connection, queueName, durable,
					exclusive, autoDelete, arguments);
			consumer.initHandleDelivery(channel);
			rabbitmqUtil.reveiveMessage(consumer);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Work消费者向交换机 [" + queueName + "] 接收消息失败");
		}

	}


	/**
	 * 向MQ接收message数据 (routing模式)
	 * <p>
	 * 使用直连接类型，将多个路由键绑定到同一个队列上。也可以将同一个键绑定到多个队列上	 * (多重绑定multiple bindings)，
	 * 此时满足键的队列都能收到消息，不满足的直接被丢弃。
	 * direct
	 */
	public void receiveByRouting(BaseConsumerable consumer) {

		String[] routingKeys = consumer.getRoutingKeys();
		String exchangeName = consumer.getExchangeName();
		if (routingKeys == null || routingKeys.length <= 0) {
			return;
		}
		if ("".equals(exchangeName.trim())) {
			return;
		}
		try {
			Connection connection = rabbitmqUtil.getConnection();
			Channel channel = rabbitmqUtil.createChannel(connection, exchangeName, routingKeys, BuiltinExchangeType.DIRECT);
			consumer.initHandleDelivery(channel);
			rabbitmqUtil.reveiveMessage(consumer);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Routing消费者向交换机 [" + exchangeName + "] 接收消息失败");
		}

	}

	/**
	 * 向MQ接收message数据 (Topic模式)
	 * <p>
	 * 主题类型topic，直连接类型direct必须是消费者发布消息指定的routingKey和消费者在队列绑定
	 * 时指定的routingKey完全相等时才能匹配到队列上，与direct不同,topic可以进行模糊匹配，可以使用星号*和井号#这两个通配符来进
	 * 行模糊匹配，其中星号可以代替一个单词；主题类型的转发器的消息不能随意的设置选择键（routing_key），必须是由点隔开的一系列
	 * 的标识符组成。标识符可以是任何东西，但是一般都与消息的某些特性相关。一些合法的选择键的例子：”quick.orange.rabbit”,你可
	 * 以定义任何数量的标识符，上限为255个字节。 #井号可以替代零个或更多的单词，只要能模糊匹配上就能将消息映射到队列中。当一个
	 * 队列的绑定键为#的时候，这个队列将会无视消息的路由键，接收所有的消息
	 */
	public void receiveByTopic(BaseConsumerable consumer) {
		String[] routingKeys = consumer.getRoutingKeys();
		String exchangeName = consumer.getExchangeName();
		if (routingKeys == null || routingKeys.length <= 0) {
			return;
		}
		if ("".equals(exchangeName.trim())) {
			return;
		}
		try {

			Connection connection = rabbitmqUtil.getConnection();
			Channel channel = rabbitmqUtil.createChannel(connection, exchangeName, routingKeys, BuiltinExchangeType.TOPIC);
			consumer.initHandleDelivery(channel);
			rabbitmqUtil.reveiveMessage(consumer);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Topic消费者向交换机 [" + exchangeName + "] 接收消息失败");
		}
	}

}
