/**  
* @Title: SignUtil.java
* @Package com.share.util
* @Description: TODO(用一句话描述该文件做什么)
* @author johnny
* @date 2019年4月24日 下午7:25:48
* @version V1.0  
*/ 
package com.share.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SignUtil {
	private static Logger logger = LoggerFactory.getLogger(SignUtil.class);
	
	public static String RSAEncryptRequest(Map reqData) {
		return encrypt(JSONObject.fromObject(reqData).toString());
	}

	public static String RSADecryptResponse(String repData) {
		return decrypt(repData);
	}

	public static String encrypt(String data)
    {
		try {
			String encryptedStr = RSATool.RSAEncode(data);
			logger.info("生成的请求密文为："+encryptedStr);
			return encryptedStr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
	
	public static String decrypt(String data)
	{
		try {
			String decryptedStr = RSATool.RSADecode(data);
			logger.info("响应解密明文为："+decryptedStr);
			return decryptedStr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String createSign(Map<String, Object> requestBody, String signKey, String signSecret) {
		String data = convertMapToJson(requestBody);
		logger.info("签名原始串："+data);
		String key = DigestUtils.md5Hex(signKey+signSecret);
		logger.info("签名密钥串："+key);
		String sign = DigestUtils.md5Hex(data + key);
		logger.info("签名结果串："+sign);
		return sign;
	}
	
	public static Boolean signVerify(String key, String secret, Map<String, Object> data, String sign) {
		String newSign = createSign(data, key, secret);
		return newSign.equals(sign);
	}
	
	public static String convertMapToJson(Map<String, Object> requestBody) {
		requestBody.remove("sign");
		TreeSet<String> sortedKey = new TreeSet<String>(requestBody.keySet());
		StringBuilder builer = new StringBuilder();
		for(String key : sortedKey) {
			builer.append(key).append("=").append(convertObjectToJson(requestBody.get(key))).append("&");
		}
		String result = builer.toString();
		logger.info("result："+result);
		return result.substring(0, result.length() - 1);
	}
	
	public static String convertObjectToJson(Object obj) {
		logger.info("obj："+obj);
		if(obj == null) {
			return "";
		}
		if(obj.getClass().isArray()) {
			return StringUtils.join((Object[])obj, "&");
		} else if(obj instanceof Map) {
			return convertMapToJson((Map<String, Object>)obj);
		} else if(obj.getClass().isPrimitive() || obj.getClass() == String.class) {
			return String.valueOf(obj);
		} else if(obj instanceof Collection) {
			StringBuilder builder = new StringBuilder();
			for(Object _obj : (List<Object>)obj) {
				builder.append(convertObjectToJson(_obj)).append("&");
			}
			String result = builder.toString();
			return result.substring(0, result.length() - 1);
		} else {
			return String.valueOf(obj);
		}
	}

	
	public static Map<String, Object> convertJsonToMap(String jsonStr) throws JSONException {
	    Map<String, Object> retMap = new HashMap<String, Object>();

	    if(jsonStr != null) {
	    	JSONObject json = JSONObject.fromObject(jsonStr);
	        retMap = jsonToMap(json);
	    }
	    return retMap;
	}

	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
	    Map<String, Object> map = new HashMap<String, Object>();

	    Iterator<String> keysItr = json.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = json.get(key);

	        if(value instanceof JSONArray) {
	            value = jsonToList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = jsonToMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}

	public static List<Object> jsonToList(JSONArray array) throws JSONException {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.size(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = jsonToList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = jsonToMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}
}
