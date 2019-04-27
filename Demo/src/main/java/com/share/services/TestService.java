package com.share.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.share.util.ConfigUtil;
import com.share.util.DateUtil;
import com.share.util.Main;

public final class TestService {
	private static Log logger = LogFactory.getLog(TestService.class);
    /**
     * http调用均可参照此方法
     * @param reqMap
     * @param rft_key
     * @param rft_secret
     * @return
     */
	public static String getMessageCode(Map<String, Object> reqMap, String rft_key, String rft_secret) {

		return Main.sign(reqMap, rft_key, rft_secret);
	}

	public static Log getLogger() {
		return logger;
	}
	
	public static void main(String[] args){
		Map<String, Object> reqMap = new HashMap<String, Object>();
		
		String timestamp = DateUtil.getTimestamp(new Date());
		String rft_key = ConfigUtil.getPro("share.terminal.key");
		String rft_secret = ConfigUtil.getPro("share.terminal.secret");
		
		reqMap.put("encode", "UTF-8");
		reqMap.put("merchantNo", "a20190424");
		reqMap.put("phoneNo", "15802061725");
		reqMap.put("timestamp", timestamp);
		reqMap.put("version", "1.0");
		
		Main.sign(reqMap, rft_key, rft_secret);
	}


}