package com.sas.utils.ftp.config;

/**
 * @author ：lyp
 * @description：ftp服务器相关配置信息
 */
public class FtpConfig {
	public FtpConfig() {
	}

	private String url;

	private int port;

	private String username;

	private String password;

	private String remoteFullFileName;

	private String localFullFileName;

	private String downDir;

	/**
	 * ftp服务器地址
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * ftp服务器端口
	 */
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * ftp服务器用户名
	 */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * ftp服务器密码
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * ftp服务器存放文件的路径
	 */
	public String getRemoteFullFileName() {
		return remoteFullFileName;
	}

	public void setRemoteFullFileName(String remoteFullFileName) {
		this.remoteFullFileName = remoteFullFileName;
	}


	/**
	 * 下载文件时，存放在本地的路径
	 */
	public String getDownDir() {
		return downDir;
	}

	public void setDownDir(String downDir) {
		this.downDir = downDir;
	}

	/**
	 * 本地文件的全路径路径
	 */
	public String getLocalFullFileName() {
		return localFullFileName;
	}

	public void setLocalFullFileName(String localFullFileName) {
		this.localFullFileName = localFullFileName;
	}
}
