package com.sas.rabbitmq.connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * mq连接配置参数
 * @author liuyongping
 * @version 1.0
 * @created at 2019/7/23 15:01
 */
@Component
public class RabbitmqConnectParams {
	public RabbitmqConnectParams() {

	}
	@Value("${spring.rabbitmq.host}")
	public String url;
	@Value("${spring.rabbitmq.username}")
	public String username;
	@Value("${spring.rabbitmq.password}")
	public String password;
	@Value("${spring.rabbitmq.virtualHost}")
	public String virtualHost;
	@Value("${spring.rabbitmq.port}")
	public String port;

}
