package com.ways.framework.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 字符转换类
 * @author csx
 *
 */
public class StringUtil {
	private static SimpleDateFormat sdfByTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdfByDate = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdfByTimeAll = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	public static String convertQuot(String orgStr){
		return orgStr.replace("'", "\\'").replace("\"","\\\"");
	}
	
	public static String convertEmptyStr(String str){
		if(str == null){
			return "";
		}else{
			return str;
		}
	}
	
	public static String convertEmptyHTMLStr(String str){
		String tmp = convertEmptyStr(str);
		if(str == null || tmp.equals("")){
			return "&nbsp;";
		}else{
			return tmp;
		}
	}
	
	public static boolean isEmpty(String str ){
		if(null == str || 0 == str.trim().length() || "null".equals(str) || "UNDEFINED".equalsIgnoreCase(str))
		{
			return true;
		}
		return false;
	}
		
	 public static boolean isNotEmpty(String str) {
        return !StringUtil.isEmpty(str);
	 }
	 public static boolean isNotBlank(String str) {
		 return !StringUtil.isBlank(str);
	 }

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static String encodePassword(String password, String algorithm) {
		byte[] unencodedPassword = password.getBytes();

		MessageDigest md = null;

		try {
			// first create an instance, given the provider
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {
			return password;
		}

		md.reset();

		// call the update method one or more times
		// (useful when you don't know the size of your data, eg. stream)
		md.update(unencodedPassword);

		// now calculate the hash
		byte[] encodedPassword = md.digest();

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}

			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}

		return buf.toString();
	}
	
	public static String getParamToString(String param){
		if(param == null){
			return "";
		}else{
			return param.trim();
		}
		
	}
	
	public static Integer getParamToIntger(String param){
		if(param == null || param.trim().equals("")){
			return null;
		}else{
			try{
				return Integer.parseInt(param);
			}catch(Exception e){
				return null;
			}
			
		}
	}
	
	public static Date getParamToTimestamp(String param){
		if(param == null || param.trim().equals("")){
			return null;
		}else{
			try{
				return sdfByTime.parse(param);
			}catch(Exception e){
				return null;
			}
			
		}
	}
	
	public static Date getParamToDate(String param){
		if(param == null || param.trim().equals("")){
			return null;
		}else{
			try{
				return sdfByDate.parse(param);
			}catch(Exception e){
				return null;
			}
			
		}
	}
	public static String getSeqNo(){
		String s = sdfByTimeAll.format(new Date());
		return s;
	}
	
	public static void main(String[]args){
	 
	}
}