package com.sas.rabbitmq.connection;

import com.rabbitmq.client.Connection;

import java.util.Vector;

/**
 * mq连接池
 *
 * @author liuyongping
 * @version 1.0
 * @created at 2019/7/22 10:52
 */
public class ConnectionWrapper {

	private final int INIT_SIZE = 4; //连接池初始化大小

	private final int MAX_SIZE = 10; //连接池的最大值

	private String driver;

	//mq连接参数信息
	private String host;
	private String port;
	private String password;
	private String username;
	private String virtualhost;

	private long activeTime = 5000;

	private Vector<MQConnection> connectPool = null;//存放数据库连接的向量

	public ConnectionWrapper(String host, String port, String password, String username, String virtualhost) {
		this.host = host;
		this.port = port;
		this.password = password;
		this.username = username;
		this.virtualhost = virtualhost;
	}

	private void initPool() {

		if (null == connectPool) {
			//创建数据库连接池
			connectPool = new Vector<MQConnection>(INIT_SIZE);
			//循环创建数据库连接
			for (int i = 0; i < INIT_SIZE; i++) {
				MQConnection db = new MQConnection(host, username, password, port, virtualhost);
				System.out.println("创建了MQConnection连接");
				connectPool.add(db);
			}
		}
	}

	/**
	 * 创建自动销毁连接
	 *
	 * @return
	 */
	public MQConnection createNewConectionTimer() {
		//初始化连接池
		initPool();
		//此方法的作用是：当获取连接的时候，如果连接不够了，才会执行这个方法创建连接
		synchronized (connectPool) {
			MQConnection db = new MQConnectionTimer(driver, host, username, password, port, virtualhost, activeTime);
			System.out.println("创建了MQConnectionTimer连接");
			connectPool.add(db);
			return db;
		}
	}

	/**
	 * 获取连接
	 *
	 * @return
	 */
	public Connection getConnection() {
		initPool();
		System.out.println("此时连接池中还有的连接数： " + connectPool.size());
		synchronized (connectPool) {
			Connection conn = null;
			MQConnection db = null;
			while (true) {
				//循环查找空闲的连接，直到找到位置
				for (int i = 0; i < connectPool.size(); i++) {
					db = connectPool.get(i);
					if (!db.isUsed()) {
						System.out.println("有空闲的连接");
						//此连接处于空闲状态
						if (db instanceof MQConnectionTimer) {
							//System.out.println("取得的链接是MQConnectionTimer");
							//如果db是MQConnectionTimer对象
							MQConnectionTimer dbTimer = (MQConnectionTimer) db;
							dbTimer.cacel(); //取消定时
							conn = db.getConn();
							db.setUsed(true); //设置此链接繁忙状态
							return conn;
						} else {
							//System.out.println("取得的连接是MQConnection");
							//如果db是MQConnection对象
							conn = db.getConn();
							db.setUsed(true); //设置此链接繁忙状态
							return conn;
						}
					}

				}
				//System.out.println("没有空闲的连接");
				//如果没有找到空闲的连接，则创建连接
				if (null == conn && connectPool.size() < this.MAX_SIZE) {
					//如果连接池的大小小于要求的最大连接数,才可以创建
					db = this.createNewConectionTimer();
					conn = db.getConn();
					db.setUsed(false);//新创建的连接设置为空闲状态
					return conn;
				}

				//如果连接池的大小达到了最大连接数
				if (null == conn && connectPool.size() == this.MAX_SIZE) {
					System.out.println("连接池满了");
					try {
						//进行等待，知道有链接进入空闲状态
						connectPool.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	public void releaseConnection(Connection conn) {
		if (connectPool == null || conn == null) {
			return;
		}
		synchronized (connectPool) {
			for (int i = 0; i < connectPool.size(); i++) {
				MQConnection db = connectPool.get(i);
				if (db instanceof MQConnectionTimer) {
					MQConnectionTimer dbTimer = (MQConnectionTimer) db;
					MQConTimerTask task = new MQConTimerTask(connectPool, dbTimer);
					dbTimer.tick(task);
					//System.out.println("释放了MQConnectionTimer的对象");
				} else {
					//固定的连接，一直存在
					if (conn == db.getConn()) {
						db.setUsed(false);
						connectPool.notify();
						//System.out.println("释放了MQConnection的对象");
						break;
					}
				}
			}
		}
	}
}
