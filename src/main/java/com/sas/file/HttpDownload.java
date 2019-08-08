package com.sas.file;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;

/**
 * 通用文件下载类
 * @author liuyongping
 * @version 1.0
 * @created at 2019/7/5 11:25
 */
public class HttpDownload {

	/**
	 * 文件下载
	 * @param response 请求头
	 * @param filePath 文件路径
	 * @param fileName 文件名称
	 * @return 传输结果
	 */

	public static boolean downloadFile(HttpServletResponse response, String filePath, String fileName) {
		if (fileName == null) {
			return false;
		}
		String fileFullName= Paths.get(filePath,fileName).toString();
		//设置文件路径
		File file = new File(fileFullName);
		if (!file.exists()) {
			return false;
		}
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		try {
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] buffer = new byte[1024];
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			OutputStream os = response.getOutputStream();
			int i = bis.read(buffer);
			while (i != -1) {
				os.write(buffer, 0, i);
				i = bis.read(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}
