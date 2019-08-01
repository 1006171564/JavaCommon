package com.sas.utils.rabbitmq.connection;


import java.io.IOException;
import java.util.TimerTask;
import java.util.Vector;

public class MQConTimerTask extends TimerTask {

    private Vector<MQConnection> connectPool = null;
    private MQConnectionTimer MQTimer;

    public MQConTimerTask(Vector<MQConnection> connectPool,
                          MQConnectionTimer MQTimer) {
        super();
        this.connectPool = connectPool;
        this.MQTimer = MQTimer;
    }


    @Override
    public void run() {
        //将过期的数据库连接移除
        try {
            MQTimer.getConn().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectPool.remove(MQTimer);
        System.out.println("移除超出生命周期的数据库连接！");
    }

}  