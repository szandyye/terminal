/**  
* @Title: Main.java
* @Package com.share.util
* @Description: TODO(用一句话描述该文件做什么)
* @author johnny
* @date 2019年4月24日 下午7:25:48
* @version V1.0  
*/ 
package com.share.util;

import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Main {

	private static Log logger = LogFactory.getLog(Main.class);
	
	public static String sign(Map<String, Object> reqMap, String rft_key, String rft_secret) {
		String sign = SignUtil.createSign(reqMap, rft_key, rft_secret);
		reqMap.put("sign", sign);
		String result = "";
		try {
			result = sendRequest(reqMap);
			logger.info("返回结果："+result);
			Map<String, Object> resMap = SignUtil.convertJsonToMap(result);
			String signature = (String) resMap.get("sign");
			resMap.remove("sign");
			if (SignUtil.signVerify(rft_key, rft_secret, resMap, signature)) {
				logger.info("验签成功："+signature);
			} else {
				logger.info("验签失败："+signature);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @Title: sendRequest
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param ht
	 * @return
	 * @throws ReqDataException
	 * @throws RetDataException
	 * @throws Exception
	 */
	public static String sendRequest(Map<String, Object> ht)
			throws  Exception {
		String url = ConfigUtil.getPro("share.terminal.api.url");
		//加密
		String param = SignUtil.RSAEncryptRequest(ht);
		// 发送请求,得到响应数据
		HttpClient rr = new HttpClient();
		Object o = rr.sendRequestForPost(url, param);
//		String postData = HttpRequest.postData(url, ht, "UTF-8");
		if (null == o) {
			throw new Exception("请求返回的数据为null");
		}
		String resStr = "";
		try {
			//解密
			resStr = SignUtil.RSADecryptResponse(String.valueOf(o));
			
		} catch (Exception e) {
			throw new Exception("响应解密失败:" + e);
		}
		return resStr;
	}
	
}
