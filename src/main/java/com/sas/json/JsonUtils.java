package com.sas.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * json工具类
 *
 * @author liuyongping
 * @version 1.0
 * @created at 2019/7/29 17:29
 */
public class JsonUtils {

	/**
	 * 将对象转换成json字符串。
	 * <p>Title: ObjectToJson</p>
	 * <p>Description: </p>
	 *
	 * @param data
	 * @return
	 */
	public static String objectToJson(Object data) {
		try {
			String jsonStr = JSONObject.toJSON(data).toString();
			return jsonStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将json结果集转化为对象
	 *
	 * @param jsonData json数据
	 * @param beanType 对象中的object类型
	 * @return
	 */
	public static <T> T jsonToObject(String jsonData, Class<T> beanType) {
		try {
			JSON jsonObject = JSONObject.parseObject(jsonData);
			T t = JSONObject.toJavaObject(jsonObject, beanType);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}