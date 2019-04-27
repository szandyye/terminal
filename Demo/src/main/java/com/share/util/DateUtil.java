/**  
* @Title: ConfigUtil.java
* @Package com.share.util
* @Description: TODO(用一句话描述该文件做什么)
* @author johnny
* @date 2019年4月24日 下午7:25:48
* @version V1.0  
*/ 
package com.share.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
   /**
    * 获取时间戳
    * @param date
    * @return
    */
   public static String getTimestamp(Date date) {
	   if(null == date) {
		   return "";
	   }
	   SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	   return format.format(date);
   }
}