package com.ways.app.price.util;

import java.io.InputStream;
import java.util.Properties;

public class ChartMapUtil {

	public static Properties prop = new Properties();
	
	static{
		 try {
			 InputStream is = ChartMapUtil.class.getClassLoader().getResourceAsStream("mapUtil.properties");
			 prop.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getValue(String key)
	{
		String value = "";
		value = prop.getProperty(key);
		return value;
	}
}
