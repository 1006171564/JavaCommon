package com.sas.utils.rabbitmq.connection;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * mq普通连接
 */
public class MQConnection {
    private String url;
    private String username;
    private String password;
    private String virtual_host;
    private String port;

    private boolean isUsed;
    private Connection conn;

    public MQConnection(String url,String username,String password,String port,String virtual_host){
        this.url = url;
        this.username = username;
        this.password = password;
        this.isUsed = false;
        this.port=port;
        this.virtual_host=virtual_host;
        //创建数据库连接
        this.createConnection();
    }

    private void createConnection() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(url);// MQ的IP
            factory.setPort(Integer.parseInt(port));// MQ端口
            factory.setUsername(username);// MQ用户名
            factory.setPassword(password);// MQ密码
            factory.setVirtualHost(virtual_host);// MQ virtual_host
            conn= factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public String getVirtual_host() {
        return virtual_host;
    }

    public void setVirtual_host(String virtual_host) {
        this.virtual_host = virtual_host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}