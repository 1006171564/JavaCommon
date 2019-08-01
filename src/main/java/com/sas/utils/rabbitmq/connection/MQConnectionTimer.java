package com.sas.utils.rabbitmq.connection;
import java.util.Timer;

/**
 * mq定时自动销毁连接任务机制
 */
public class MQConnectionTimer extends MQConnection {
	private long activeTime;
	private Timer timer;

	public MQConnectionTimer(String driver, String url, String username,
							 String password,String port,String virtual_host, long activeTime) {
		super(url, username, password,port,virtual_host);
		this.activeTime = activeTime;
		timer = new Timer();
	}

	public void tick(MQConTimerTask task) {
		try{
			this.timer.schedule(task, activeTime);
			System.out.println("定时开始");
		} catch(IllegalStateException e) {
			System.err.println("已经存在task了");
		}

	}

	public void cacel(){
		this.timer.cancel();
		System.out.println("取消定时");
	}

}
