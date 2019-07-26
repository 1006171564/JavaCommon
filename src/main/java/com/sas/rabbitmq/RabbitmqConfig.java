package com.sas.rabbitmq;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * mq自动扫描配置
 * @author liuyongping
 * @version 1.0
 * @created at 2019/7/19 13:33
 */
@Component
@ComponentScan("com.sas.utils.tools.rabbitmq")
public class RabbitmqConfig {

}
