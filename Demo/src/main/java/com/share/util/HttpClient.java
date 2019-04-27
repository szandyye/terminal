/**  
* @Title: HttpClient.java
* @Package com.jushi.test
* @Description: TODO(用一句话描述该文件做什么)
* @author johnny
* @date 2017年5月15日 下午7:25:48
* @version V1.0  
*/ 
package com.share.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClient {

	private static Logger logger = LoggerFactory.getLogger(HttpClient.class);

/**
 * 
 * @Title: sendRequestForPost
 * @Description: TODO(这里用一句话描述这个方法的作用)
 * @param urlstr
 * @param request
 * @return
 */
	public Object sendRequestForPost(String urlstr, Object request) {
		return sendRequest("POST", urlstr, request);
	}

	/**
	 * ******************************************** method name : sendRequest
	 * description : 发送HTTP同步请求
	 * 
	 * @return : Object
	 * @param : @param method 请求方法 GET和POST
	 * @param : @param urlstr 请求的URl地址
	 * @param : @param request 支持String和Map数据类型
	 * @param : @return 
	 * @see : *******************************************
	 */
	private Object sendRequest(String method, String urlstr, Object request) {
		HttpsURLConnection conn = null;
		OutputStream os = null;
		Object rtn = null;
		String reqCon = null;
		logger.info("请求地址：{}", urlstr);
		try {
			
			TrustManager[] tm = {new X509TrustManager() {  
                public void checkClientTrusted(X509Certificate[] chain,  
                        String authType) throws CertificateException {  
                }  
                public void checkServerTrusted(X509Certificate[] chain,  
                        String authType) throws CertificateException {  
                }  
                public X509Certificate[] getAcceptedIssuers() {  
                    return null;  
                }  
        }  };
			
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			
			URL url = new URL(urlstr);
			conn = (HttpsURLConnection) url.openConnection();
			
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			
			conn.setSSLSocketFactory(ssf);
			
			// TODO 超时时间考虑设置到配置文件中
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(60 * 1000);
			conn.setRequestMethod(method);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Encoding", "UTF-8");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("RFT-KEY", ConfigUtil.getPro("share.key"));
			conn.setRequestProperty("rft-org", "2001");
			os = conn.getOutputStream();
			// 判断传入请求数据的类型，目前只处理String
			if (request instanceof String) {
				// 请求来的数据需要先做url编码
				reqCon = (String) request;
				os.write(reqCon.getBytes("UTF-8"));
			}
			os.flush();
			int rCode = conn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == rCode) {
				if (null != conn.getInputStream()) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int i = -1;
					try {
						while ((i = conn.getInputStream().read()) != -1) {
							baos.write(i);
						}
					} catch (IOException e) {
						logger.error("HttpClient.HttpException", e);
					}
					String retStr = baos.toString("UTF-8");
					logger.info("返回的响应密文为：{}", retStr);
					// 返回的数据不需要做decode解码操作（否则验签会失败）
					rtn = retStr.trim();
				}
			} else {
				logger.info("HttpClient.Http.rCode:{}", rCode);
			}
		} catch (Exception e) {
			logger.error("HttpClient.HttpException", e);
		} finally {
			try {
				os.close();
				conn.disconnect();
			} catch (Exception e) {
				os = null;
				conn = null;
			}
		}
		return rtn;
	}
	
	class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			// 直接返回true
			return true;
		}
	}
	
}

