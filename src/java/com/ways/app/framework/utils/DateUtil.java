package com.ways.app.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class DateUtil {
	/**
	* 函数功能说明 
	* 获取成交价城市Mix日期
	* 小于等于2013年查询城市时只查询2013年含城市Mix的城市
	* 大于2013年的取年份减一城市Mix数据
	* 查询出图结果数据也根据此逻辑获取城市Mix进行计算
	* rrf  Mar 6, 2015
	* 修改者名字 修改日期
	* 修改内容
	* @param paraMaps
	* @return    
	* Map<String,Object>
	*/
	public static Map<String, Object> getTPMix_Date(Map<String, Object> paraMaps){
		int beginDate_TPMix = 2013;
    	int endDate_TPMix = 2013;
    	SimpleDateFormat df= new SimpleDateFormat("yyyy");
    	Calendar beginDate = Calendar.getInstance();
    	Calendar endDate = Calendar.getInstance();
    	Map<String, Object> paraMap = paraMaps;
    	if(paraMap.containsKey("beginDate")){
    	   try {
			beginDate.setTime(df.parse(paraMap.get("beginDate").toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(beginDate != null){
    		   beginDate_TPMix = beginDate.get(Calendar.YEAR)-1<=beginDate_TPMix?beginDate_TPMix:beginDate.get(Calendar.YEAR)-1;
    	   }
    	   paraMap.put("beginDate_TPMix", beginDate_TPMix);
    	}
    	if(paraMap.containsKey("endDate")){
    	   try {
			endDate.setTime(df.parse(paraMap.get("endDate").toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(beginDate != null){
     		   endDate_TPMix = endDate.get(Calendar.YEAR)-1<=endDate_TPMix?endDate_TPMix:endDate.get(Calendar.YEAR)-1;  
     	   }
     	   paraMap.put("endDate_TPMix", endDate_TPMix);
     	}
    	return paraMap;
	}
	
	
	public static String getCurrentQuarterStartDate(String date){
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd");; 
		Calendar c = Calendar.getInstance(); 
        int currentMonth = 1; 
        String now = null; 
        try { 
        	c.setTime(longSdf.parse(date));
        	currentMonth = c.get(Calendar.MONTH) + 1;
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0); 
            else if (currentMonth >= 4 && currentMonth <= 6) 
                c.set(Calendar.MONTH, 3); 
            else if (currentMonth >= 7 && currentMonth <= 9) 
                c.set(Calendar.MONTH, 4); 
            else if (currentMonth >= 10 && currentMonth <= 12) 
                c.set(Calendar.MONTH, 9); 
            c.set(Calendar.DATE, 1);
            now = longSdf.format(c.getTime()); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return now; 
	}
	
	/**
	 * 获取当前时间
	 * @date Dec 29, 2015
	 */
	public static String getCurrentTime(){
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HHmmss");; 
        return longSdf.format(System.currentTimeMillis()).replace(" ", "-"); 
	}
	
	public static String getQuarterMonth(String date,String dType){
		int month = 1;
		String now = null;
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM");; 
		Calendar c = Calendar.getInstance();
        try { 
        	  c.setTime(longSdf.parse(date));
        	  month = c.get(Calendar.MONTH) + 1;
	          if (month >= 1 && month <= 3){
	        	   if("begin".equals(dType)){
	        		   c.set(Calendar.MONTH, 0); 
	        	   }else if("end".equals(dType)){
	        		   c.set(Calendar.MONTH, 2);
	        	   }
	           }else if (month >= 4 && month <= 6){
	        	   if("begin".equals(dType)){
	        		   c.set(Calendar.MONTH, 3);
	        	   }else if("end".equals(dType)){
	        		   c.set(Calendar.MONTH, 5);
	        	   }
	           }else if (month >= 7 && month <= 9){
	        	   if("begin".equals(dType)){
	        		   c.set(Calendar.MONTH, 6);
	        	   }else if("end".equals(dType)){
	        		   c.set(Calendar.MONTH, 8);
	        	   }
	           }else if (month >= 10 && month <= 12){
	        	   if("begin".equals(dType)){
	        		   c.set(Calendar.MONTH, 9);
	        	   }else if("end".equals(dType)){
	        		   c.set(Calendar.MONTH, 11);
	        	   }
	           }
	        now = longSdf.format(c.getTime()); 
        } catch (Exception e) {
            e.printStackTrace(); 
        } 
        return now; 
	}
	
	public static int calculateMonthIn(String sDate,String eDate){
		int c = 0;
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM");; 
		Calendar cal1 = new GregorianCalendar();
		Calendar cal2 = new GregorianCalendar();
		try {
			cal1.setTime(longSdf.parse(eDate));
			cal2.setTime(longSdf.parse(sDate));
			c = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12
					+ cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH) + 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public static void main(String[] args) {
		System.out.println(calculateMonthIn("2015-01","2015-03"));
	}
}
