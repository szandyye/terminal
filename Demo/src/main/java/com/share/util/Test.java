package com.share.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Test {
	 private static final String fileName = "config.properties";
public static void main(String[] args) throws IOException {
	Test t = new Test();
	t.getPro();
}
public static void getPro() throws IOException {
      InputStream in = null;
      Properties prop = new Properties();
      in = Test.class.getClassLoader().getResourceAsStream(fileName);
      System.out.println("in:"+in+"fileName:"+fileName);
      if (null == in) throw new RuntimeException("没有找到配置文件"+fileName);
      prop.load(in);
      in.close();

}
}
