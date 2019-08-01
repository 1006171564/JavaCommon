package com.sas.rabbitmq;


import com.rabbitmq.client.AMQP;
import com.sas.RabbitmqApplication;
import com.sas.utils.rabbitmq.ReceiveHelper;
import com.sas.utils.rabbitmq.SenderHelper;
import com.sas.utils.rabbitmq.utils.RabbitmqUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitmqApplication.class)//如果是mvc工程需要添加参数：webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT

public class SenderHelperTest {

	SenderHelper senderHelper;
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
		senderHelper = new SenderHelper(host,port,virtualhost,username,password);
	}
	@Test
	public void sendBySimple() {

		String[] s = new String[100];
		for (int i = 0; i < s.length; i++) {
			if (i % 2 == 0) {
				s[i] = "zhongguo" + i;
			} else {
				s[i] = "qinghai" + i;
			}
		}
		senderHelper.sendBySimple("Test_Simple", s);
	}

	@Test
	public void sendBySimple1() {
	}

	@Test
	public void sendByFanout() {

		String[] s = new String[2];
		s[0] = "zhongguo";
		s[1] = "qinghai";
		senderHelper.sendByFanout("sendByFanout", s);
	}

	@Test
	public void sendByFanout1() {
	}

	@Test
	public void sendByWork() {
		String[] s = new String[100];
		for (int i = 0; i < s.length; i++) {
			if (i % 2 == 0) {
				s[i] = "zhongguo" + i;
			} else {
				s[i] = "qinghai" + i;
			}
		}
		senderHelper.sendByWork("sendByWork", s);
	}

	@Test
	public void sendByWork1() {
		String[] s = new String[2];
		s[0] = "zhongguo";
		s[1] = "qinghai";
		//测试队列消息持久化
		AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties().builder();
		properties.deliveryMode(2);
		senderHelper.sendByWork("sendByWork", s, null, properties.build());
	}

	@Test
	public void sendByRouting() {
	}

	@Test
	public void sendByTopic() {
	}
}