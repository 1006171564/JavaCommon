package com.sas.rabbitmq;

import com.sas.RabbitmqApplication;
import com.sas.utils.rabbitmq.ReceiveHelper;
import com.sas.utils.rabbitmq.consumer.impl.BaseConsumer;
import com.sas.utils.rabbitmq.utils.RabbitmqUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitmqApplication.class)
//如果是mvc工程需要添加参数：webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
public class ReceiveHelperTest {

	ReceiveHelper receiveHelper;

	{
		Properties properties = new Properties();
		// 使用InPutStream流读取properties文件
		InputStream in = RabbitmqUtil.class.getClassLoader().getResourceAsStream("mq-config.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String host = properties.getProperty("spring.rabbitmq.host");
		String port = properties.getProperty("spring.rabbitmq.port");
		String password = properties.getProperty("spring.rabbitmq.password");
		String username = properties.getProperty("spring.rabbitmq.username");
		String virtualhost = properties.getProperty("spring.rabbitmq.virtualHost");
		receiveHelper = new ReceiveHelper(host,port,virtualhost,username,password);
	}

	@Test
	public void receiveBySimple() {

		BaseConsumer myConsumer = new BaseConsumer();
		myConsumer.initConsumer("Test_Simple", false);
		receiveHelper.receiveBySimple(myConsumer);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void receiveBySimple1() {
	}

	@Test
	public void receiveByFanout() {
	}

	@Test
	public void receiveByFanout1() {
	}

	@Test
	public void receiveByWork() {
		BaseConsumer myConsumer = new BaseConsumer();
		myConsumer.initConsumer("sendByWork", false);
		receiveHelper.receiveByWork(myConsumer);
	}

	@Test
	public void receiveByWork1() {
		BaseConsumer myConsumer = new BaseConsumer();
		myConsumer.initConsumer("sendByWork", false);
		receiveHelper.receiveByWork(myConsumer);
	}

	@Test
	public void receiveByRouting() {
	}

	@Test
	public void receiveByTopic() {
	}
}