package com.sas.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.sas.RabbitmqApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitmqApplication.class)
public class SenderHelperTest {

	@Autowired
	SenderHelper senderHelper;

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
		String[] s = new String[10000];
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