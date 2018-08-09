package com.ways.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class DateConverter implements Converter<String, Date> {    
	@Override    
	public Date convert(String source) {		
		if(source == null || source.trim().equals("")) return null;
	    SimpleDateFormat dateFormat = null;
	    
	    if(source.length() == 10){
	    	if(source.contains("-")){
	    		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    	}else if(source.contains("/")){
	    		dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    	}
	    }else{
	    	dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    }
	    dateFormat.setLenient(false);    
	    try {
			return dateFormat.parse(source);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}    
	    return null;    
	}  
	
//	public static void main(String[] args) {
//		DateConverter info =  new DateConverter();
//		System.out.println(info.convert("9999/10/10").toLocaleString());;
//	}
}