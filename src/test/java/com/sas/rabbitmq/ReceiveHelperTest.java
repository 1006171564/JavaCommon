package com.sas.rabbitmq;

import com.sas.RabbitmqApplication;
import com.sas.rabbitmq.consumer.impl.MyConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitmqApplication.class)//如果是mvc工程需要添加参数：webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
public class ReceiveHelperTest {

	@Autowired
	ReceiveHelper receiveHelper;

	@Test
	public void receiveBySimple() {

		MyConsumer myConsumer = new MyConsumer();
		myConsumer.initConsumer("Test_Simple", false);
		receiveHelper.receiveBySimple(myConsumer);
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
		MyConsumer myConsumer = new MyConsumer();
		myConsumer.initConsumer("sendByWork", false);
		receiveHelper.receiveByWork(myConsumer);
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void receiveByWork1() {
		MyConsumer myConsumer = new MyConsumer();
		myConsumer.initConsumer("sendByWork", false);
		receiveHelper.receiveByWork(myConsumer);
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void receiveByRouting() {
	}

	@Test
	public void receiveByTopic() {
	}
}