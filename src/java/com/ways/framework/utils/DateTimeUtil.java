package com.ways.framework.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * 日期处理类
 */

public class DateTimeUtil {

	public static String SHORT_FORM = "yyyy-MM-dd";

	public static String LONG_FORM = "yyyy-MM-dd HH:mm:ss";

	// ------------------------------------------------------------------------------
	/**
	 * 检查日期是否满足"yyyy-MM-dd"的格式，且toDate不小于fromDate
	 * 
	 * @param fromDate
	 *            开始日期
	 * @param toDate
	 *            结束日期
	 * @param dates
	 *            用于返回处理后的开始，结束日期
	 * @return 日期格式正确，返回true，否则false
	 */
	public static boolean isValidDates(String fromDate, String toDate,
			String[] dates) {
		if (fromDate == null || fromDate.trim().length() == 0)
			return false;
		if (toDate == null || toDate.trim().length() == 0)
			return false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date begin = null;
		Date end = null;
		try {
			begin = format.parse(fromDate);
			end = format.parse(toDate);
			if (begin.after(end))
				return false;
			dates[0] = format.format(begin);
			dates[1] = format.format(end);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * @author zgk
	 * @Title: format
	 * @Description: Date 转 String
	 * @param time
	 * @param formaterKey
	 * @return
	 * @throws
	 */
	public static String format(Date time, String formaterKey) {
		String date = "";
		if (formaterKey != null) {
			if (time == null) {
				date = "";
			} else {
				SimpleDateFormat formater = new SimpleDateFormat(formaterKey);
				date = formater.format(time);
			}

			return date;
		}
		throw new IllegalArgumentException(new StringBuilder("格式化串存在问题")
				.append(formaterKey).toString());
	}
	
	
	/**
	 * @Title: getMonthEnDesc 
	 * @Description: 获取月份英文简写描述
	 * @param month
	 * @return      
	 * @throws
	 */
	public static String getMonthEnSortDesc(Date month) {
		if(month == null) return "";
		Calendar c = Calendar.getInstance();
		c.setTime(month);
		int m = c.get(Calendar.MONTH) + 1;
		switch (m) {
			case 1: return "Jan";
			case 2: return "Feb";
			case 3: return "Mar";
			case 4: return "Apr";
			case 5: return "May";
			case 6: return "Jun";
			case 7: return "Jul";
			case 8: return "Aug";
			case 9: return "Sep";
			case 10: return "Oct";
			case 11: return "Nov";
			case 12: return "Dec";
		}
		return "";
	}
	
	/**
	 * @Title: getBeginOfHour 
	 * @Description: 获取某小时开始时间 如：2013-05-09 06:00:00
	 * @param time
	 * @return      
	 * @throws
	 */
	public static Date getBeginOfHour(Date time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * @Title: getEndOfHour 
	 * @Description: 获取小时结束时间 如：2013-05-09 06:59:59
	 * @param time
	 * @return      
	 * @throws
	 */
	public static Date getEndOfHour(Date time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @Title: getBeginOfDay
	 * @Description: 获取某日开始时间 如：2013-05-09 00:00:00
	 * @param time
	 * @return
	 * @throws
	 */
	public static Date getBeginOfDay(Date time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @Title: getEndOfDay
	 * @Description: 获取某日结束时间 如：2013-05-09 59:59:59
	 * @param time
	 * @return
	 * @throws
	 */
	public static Date getEndOfDay(Date time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @Title: getBeginOfMonth
	 * @Description: 获取月开始时间
	 * @param time
	 * @return yyyy-MM-dd 00:00:00 000
	 * @throws
	 */
	public static Date getBeginOfMonth(Date time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @Title: getEndOfMonth
	 * @Description: 获取月结束时间
	 * @param time
	 * @return yyyy-MM-dd 23:59:59 999
	 */
	public static Date getEndOfMonth(Date time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}

	/**
	 * @Title: getBeginOfYear
	 * @Description: 获取开始结束时间
	 * @param date
	 * @return yyyy-MM-dd 00:00:00 000
	 */
	public static Date getBeginOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
     * @Title: getBeginOfQuarter 
     * @Description: 获取季度的开始时间，即2012-01-1 00:00:00
     * @param date
     * @return      
     * @throws
     */
    public static  Date getBeginOfQuarter(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
    		c.set(Calendar.MINUTE, 0);
    		c.set(Calendar.SECOND, 0);
    		c.set(Calendar.MILLISECOND, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * @Title: getEndOfQuarter 
     * @Description: 获取季度的结束时间，即2012-03-31 23:59:59
     * @param date
     * @return      
     * @throws
     */
    public static  Date getEndOfQuarter(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            c.set(Calendar.HOUR_OF_DAY, 23);
    		c.set(Calendar.MINUTE, 59);
    		c.set(Calendar.SECOND, 59);
    		c.set(Calendar.MILLISECOND, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }
    
    /**
     * @Title: getQuarterStrDesc 
     * @Description: 获取季度的中文描述 如：2013-01-01  13Q1 
     * @param date
     * @return      
     * @throws
     */
    public static String getQuarterStrDesc(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        String desc = "";
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
            	desc = format(date, "yy") + "Q1";
            else if (currentMonth >= 4 && currentMonth <= 6)
            	desc = format(date, "yy") + "Q2";
            else if (currentMonth >= 7 && currentMonth <= 9)
            	desc = format(date, "yy") + "Q3";
            else if (currentMonth >= 10 && currentMonth <= 12)
                desc = format(date, "yy") + "Q4";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return desc;
    }

	/**
	 * @Title: getEndOfYear
	 * @Description: 获取年结束时间
	 * @param time
	 * @return yyyy-MM-dd 23:59:59 999
	 */
	public static Date getEndOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, 12);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 取得指定日期所在周的第一天
	 * @param time
	 * @return yyyy-MM-dd 00:00:00 000
	 */
	public static Date getBeginOfWeek(Date time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		return calendar.getTime();
	}
	
	/**
	 * 取得指定日期所在周的最后一天
	 * @param time
	 * @return yyyy-MM-dd 23:59:59 999
	 */
	public static Date getEndOfWeek(Date time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()+6);
		return calendar.getTime();
	}
	
	/**
	 * 判断指定日期，是否为周末
	 * @param time
	 * @return boolean
	 */
	public static boolean isSunday(Date time){
		boolean isWeather = false;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		int w = calendar.get(Calendar.DAY_OF_WEEK);
		if(w == 1 || w == 7){
			isWeather = true;
		}
		return isWeather;
	}
	
	/**
	 * 对<code>Date</code>型的数据进行加减操作
	 * @param date 日期
	 * @param amount  步长
	 * @param field 针对的字段
	 * @return
	 */
	public static Date add(Date date, int amount, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * @Title: getNow
	 * @Description:
	 * @return
	 * @throws
	 */
	public static Date getNow() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 判断时间大于或者等于当天
	 * 
	 * @param day
	 *            时间
	 * @return
	 */
	public static boolean isGreaterOrEquToday(Date day) {
		java.util.Calendar tc = Calendar.getInstance();
		tc.setTime(day);
		Calendar td = Calendar.getInstance();
		td.set(Calendar.HOUR_OF_DAY, 0);
		td.set(Calendar.MINUTE, 0);
		td.set(Calendar.SECOND, 0);
		td.set(Calendar.MILLISECOND, 0);
		return tc.after(td) || tc.equals(td);
	}
	
	/**
	 * @Title: isSameDay 
	 * @Description: 判断两个日期是否相等 
	 * @param day1
	 * @param day2
	 * @return      
	 * @throws
	 */
	public static boolean isSameDay(Date day1, Date day2) {
		java.util.Calendar tc = Calendar.getInstance();
		tc.setTime(day1);
		tc.set(Calendar.HOUR_OF_DAY, 0);
		tc.set(Calendar.MINUTE, 0);
		tc.set(Calendar.SECOND, 0);
		tc.set(Calendar.MILLISECOND, 0);
		
		Calendar td = Calendar.getInstance();
		td.setTime(day2);
		td.set(Calendar.HOUR_OF_DAY, 0);
		td.set(Calendar.MINUTE, 0);
		td.set(Calendar.SECOND, 0);
		td.set(Calendar.MILLISECOND, 0);
		return tc.equals(td);
	}
	
	/**
	 * 判断时间小于或者等于当天
	 * 
	 * @param day
	 *            时间
	 * @return
	 */
	public static boolean isLessOrEquToday(Date day) {
		java.util.Calendar tc = Calendar.getInstance();
		tc.setTime(day);
		Calendar td = Calendar.getInstance();
		td.set(Calendar.HOUR_OF_DAY, 0);
		td.set(Calendar.MINUTE, 0);
		td.set(Calendar.SECOND, 0);
		td.set(Calendar.MILLISECOND, 0);
		return tc.before(td) || tc.equals(td);
	}

	/**
	 * 判断时间大于或者等于当月
	 * 
	 * @param day
	 *            时间
	 * @return
	 */
	public static boolean isGreaterOrEquThisMonth(Date day) {
		
		java.util.Calendar tc = Calendar.getInstance();
		tc.setTime(day);
		tc.set(Calendar.DAY_OF_MONTH, 1);
		tc.set(Calendar.HOUR_OF_DAY, 0);
		tc.set(Calendar.MINUTE, 0);
		tc.set(Calendar.SECOND, 0);
		tc.set(Calendar.MILLISECOND, 0);
		Calendar td = Calendar.getInstance();
		td.set(Calendar.DAY_OF_MONTH, 1);
		td.set(Calendar.HOUR_OF_DAY, 0);
		td.set(Calendar.MINUTE, 0);
		td.set(Calendar.SECOND, 0);
		td.set(Calendar.MILLISECOND, 0);
		
		return tc.after(td) || tc.equals(td);
	}
	
	/**
	 * 判断时间小于或者等于当月
	 * 
	 * @param day
	 *            时间
	 * @return
	 */
	public static boolean isLessOrEquThisMonth(Date day) {
		
		java.util.Calendar tc = Calendar.getInstance();
		tc.setTime(day);
		tc.set(Calendar.DAY_OF_MONTH, 1);
		tc.set(Calendar.HOUR_OF_DAY, 0);
		tc.set(Calendar.MINUTE, 0);
		tc.set(Calendar.SECOND, 0);
		tc.set(Calendar.MILLISECOND, 0);
		Calendar td = Calendar.getInstance();
		td.set(Calendar.DAY_OF_MONTH, 1);
		td.set(Calendar.HOUR_OF_DAY, 0);
		td.set(Calendar.MINUTE, 0);
		td.set(Calendar.SECOND, 0);
		td.set(Calendar.MILLISECOND, 0);
		
		return tc.before(td) || tc.equals(td);
	}
	
	/**
	 * @Title: isThisYear
	 * @Description: 判断day日期的年份是否大于今年
	 * @param day
	 * @return
	 * @throws
	 */
	public static boolean isGreaterThisYear(Date day) {
		java.util.Calendar tc = Calendar.getInstance();
		tc.setTime(day);
		Calendar td = Calendar.getInstance();
		return tc.get(Calendar.YEAR) > td.get(Calendar.YEAR);
	}

	/**
	 * @Title: isToday
	 * @Description: 判断day日期是否是今天
	 * @param day
	 * @return
	 * @throws
	 */
	public static boolean isToday(Date day) {
		java.util.Calendar tc = Calendar.getInstance();
		tc.setTime(day);
		Calendar td = Calendar.getInstance();
		return tc.get(Calendar.YEAR) == td.get(Calendar.YEAR)
				&& tc.get(Calendar.MONTH) == td.get(Calendar.MONTH)
				&& tc.get(Calendar.DAY_OF_MONTH) == td
						.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @Title: getBetweenYearDate
	 * @Description: 获取两个年跨度的所有年份 如：2011-2013 返回：2011、2012、2013
	 * @param start
	 * @param end
	 * @return
	 * @throws
	 */
	public static List<Date> getBetweenYearDate(Date start, Date end) {
		List<Date> result = new ArrayList<Date>();
		if (start.after(end))
			return result;
		Date startMonth = getBeginOfMonth(start);
		while (!startMonth.after(end)) {
			result.add(startMonth);
			startMonth = changeDate(startMonth, 1, Calendar.YEAR);
		}
		return result;
	}
	
	/**
	 * @Title: getBetweenMonthDate 
	 * @Description: 获取两个月跨度的所有月份 如：2013/05-2013/07 返回：2013/05、2013/06、2013/07
	 * @param start
	 * @param end
	 * @return      
	 * @throws
	 */
	public static List<Date> getBetweenMonthDate(Date start, Date end) {
		List<Date> result = new ArrayList<Date>();
		if (start.after(end))
			return result;
		Date startMonth = getBeginOfMonth(start);
		while (!startMonth.after(end)) {
			result.add(startMonth);
			startMonth = changeDate(startMonth, 1, Calendar.MONTH);
		}
		return result;
	}
	
	/**
	 * @Title: getBetweenWeekDate 
	 * @Description: 获取两个日跨度的所有周日期
	 * @param start
	 * @param end
	 * @return      
	 * @throws
	 */
	public static List<Date> getBetweenWeekDate(Date start, Date end) {
		List<Date> result = new ArrayList<Date>();
		if (start.after(end))
			return result;
		Date startMonth = getBeginOfWeek(start);
		while (!startMonth.after(end)) {
			result.add(startMonth);
			startMonth = changeDate(startMonth, 1, Calendar.WEEK_OF_YEAR);
		}
		return result;
	}
	
	/**
	 * @Title: getBetweenDayDate 
	 * @Description: 获取两个日跨度的所有日期 如：2013/05/06-2013/05/08 返回：2013/05/06、2013/05/07、2013/05/08
	 * @param start
	 * @param end
	 * @return      
	 * @throws
	 */
	public static List<Date> getBetweenDayDate(Date start, Date end) {
		List<Date> result = new ArrayList<Date>();
		if (start.after(end))
			return result;
		Date startMonth = getBeginOfDay(start);
		while (!startMonth.after(end)) {
			result.add(startMonth);
			startMonth = changeDate(startMonth, 1, Calendar.DATE);
		}
		return result;
	}
	
	/**
	 * @Title: _getBetweenDayDate 
	 * @Description: 获取两个日跨度的所有日期 如：2013/05/06-2013/05/08 返回：2013/05/07 PS:不包含起始日期
	 * @param start
	 * @param end
	 * @return      
	 * @throws
	 */
	public static List<Date> _getBetweenDayDate(Date start, Date end) {
		List<Date> result = new ArrayList<Date>();
		if (start.after(end))
			return result;
		Date startDay = getBeginOfDay(start);
		while (!startDay.after(end)) {
			if(!isSameDay(start, startDay) && !isSameDay(end, startDay)) result.add(startDay);
			startDay = changeDate(startDay, 1, Calendar.DATE);
		}
		return result;
	}
	
	 /**
     * @Title: getHourInDay 
     * @Description: 获取某一天的第几小时时间
     * @param day 某天
     * @param hour 小时 0-24
     * @return      
     * @throws
     */
    public static Date getHourInDay(Date day, int hour) {
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
    }
    

	/**
	 * 修改日期,根据字段和步长
	 * 
	 * @param time
	 *            日期
	 * @param amount
	 *            步长
	 * @param field
	 *            字段 Calendar内常量
	 * @return
	 */
	public static Date changeDate(Date time, int amount, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.add(field, amount);
		return calendar.getTime();
	}
	
	/**
	 * 获取当前时间是星期几
	 * @param date
	 * @param language 语言(en英文显示,否则中文显示)
	 * @return
	 */
    public static String getWeekName(Date date,String language) { 
    	String weekDay = "";
    	String[] weekDays = null;
    	if(language.equals("en")){
    		weekDays = new String[]{ "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" }; 
    		//weekDays = new String[]{ "S", "M", "T", "W", "T", "F", "S" };
    	}else{
    		weekDays = new String[]{ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" }; 
    	}
		Calendar cal = Calendar.getInstance();
		if (date == null) {
			weekDay = "--"; 
		} else {
			cal.setTime(date); 
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;  
			if (w < 0) w = 0; 
			weekDay = weekDays[w];
		}
		return weekDay;  
	}
    
    /**
     * @Title: getWeekOfDate 
     * @Description: 传入一个时间，判断该时间是星期几
     * @param  时间
     * @return  星期几    
     * @throws
     */
    public static String getWeekOfDate(Date date) { 
    	String weekDay = "";
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };  
		Calendar cal = Calendar.getInstance();
		if (date == null) {
			weekDay = "--"; 
		} else {
			cal.setTime(date); 
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;  
			if (w < 0) w = 0; 
			weekDay = weekDays[w];
		}
		return weekDay;  
	}
    
    /**
     * 计算当前时间是当年里的第几周
     * @param date
     * @return
     */
    public static int getWeekCountOfDate(Date date){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    /**
     * 计算两个时间之间有几个周
     * @param sDate
     * @param eDate
     * @return
     */
    public static int getSumWeekDate(Date sDate,Date eDate){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(sDate);
    	int start = cal.get(Calendar.WEEK_OF_YEAR);
    	cal.setTime(eDate);
    	int end = cal.get(Calendar.WEEK_OF_YEAR);
     	return end-start+1;
    }
    
    /**
     * @Title: getMonthChineseDescOfDate 
     * @Description: 传入一个时间，判断该时间是几月
     * @param  时间
     * @return 月   
     * @throws
     */
    public static String getMonthChineseDescOfDate(Date date) { 
    	String month = "";
		String[] days = { "一月", "二月", "三月", "四月", "五月", "六月", "七月","八月","九月","十月","十一月","十二月"};  
		Calendar cal = Calendar.getInstance();
		if (date == null) {
			month = "--"; 
		} else {
			cal.setTime(date); 
			int w = cal.get(Calendar.MONTH) ;  
			if (w < 0) w = 0; 
			month = days[w];
		}
		
		return month;  
	}

	// ------------------------------------------------------------------------------
	public static String getTimeString(Timestamp tsp) {
		if (tsp == null)
			return "";
		return getTimeString(new Date(tsp.getTime()));
	}

	// ------------------------------------------------------------------------------
	public static String getTimeString(java.util.Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(LONG_FORM);
		return formatter.format(date).trim();
	}
	
	public static String getShortTimeString(java.util.Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		return formatter.format(date).trim();
	}

	/*
	 * public static String getTimeString(java.sql.Date date) { SimpleDateFormat
	 * formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); return
	 * formatter.format(date).trim(); }
	 */
	// ------------------------------------------------------------------------------
	public static String getDateString(java.util.Date date) {
		if (date == null)
			return "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date).trim();
	}

	/*
	 * public static String getDateString(java.sql.Date date) { SimpleDateFormat
	 * formatter = new SimpleDateFormat("yyyy-MM-dd"); return
	 * formatter.format(date).trim(); }
	 */
	// ------------------------------------------------------------------------------
	public static String to_char(Timestamp tsp, String format) {
		if (tsp == null)
			return "";
		return to_char(new Date(tsp.getTime()), format);
	}

	// ------------------------------------------------------------------------------
	public static String to_char(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date).trim();
	}

	// ------------------------------------------------------------------------------
	/**
	 * 字符串转换为日期java.util.Date
	 * 
	 * @param dateText
	 *            字符串
	 * @param format
	 *            日期格式
	 * @return
	 */
	public static Date paseDate(String dateText, String format) {
		if (dateText == null) {
			return null;
		}
		DateFormat df = null;
		try {
			if (format == null) {
				df = new SimpleDateFormat();
			} else {
				df = new SimpleDateFormat(format);
			}

			// setLenient avoids allowing dates like 9/32/2001
			// which would otherwise parse to 10/2/2001
			df.setLenient(false);
			return df.parse(dateText);
		} catch (ParseException e) {
			return null;
		}
	}

	// ------------------------------------------------------------------------------
	/**
	 * 返回本周，本月，本年时间范围的开始，结束日期，日期格式：yyyy-MM-dd
	 * 
	 * @param selType
	 *            1:本周 2：本月 3：本年
	 * @return 开始，结束日期字符数组
	 */
	public static final String[] getDateRange(int selType) {
		String startDate = null, endDate = null;

		Calendar cd = Calendar.getInstance();
		int year = cd.get(Calendar.YEAR);
		int month = cd.get(Calendar.MONTH) + 1;
		Calendar cdTmp = Calendar.getInstance();
		int i;
		switch (selType) {
		case 1: // 本周
			i = cd.get(Calendar.DAY_OF_WEEK) - 1;
			cdTmp.setTime(new Date(cd.getTime().getTime() - i * 3600 * 24
					* 1000));
			startDate = cdTmp.get(Calendar.YEAR) + "-"
					+ (cdTmp.get(Calendar.MONTH) + 1) + "-"
					+ cdTmp.get(Calendar.DAY_OF_MONTH);
			i = 7 - cd.get(Calendar.DAY_OF_WEEK);
			cdTmp.setTime(new Date(cd.getTime().getTime() + i * 3600 * 24
					* 1000));
			endDate = cdTmp.get(Calendar.YEAR) + "-"
					+ (cdTmp.get(Calendar.MONTH) + 1) + "-"
					+ cdTmp.get(Calendar.DAY_OF_MONTH);
			break;
		case 2: // 本月
			startDate = year + "-" + month + "-01";
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				endDate = year + "-" + month + "-31";
				break;
			case 2:
				if (isLeapYear(year))
					endDate = year + "-" + month + "-29";
				else
					endDate = year + "-" + month + "-28";
				break;
			default:
				endDate = year + "-" + month + "-30";
			}
			break;
		case 3: // 本年
			startDate = year + "-01-01";
			endDate = year + "-12-31";
			break;
		default:
			startDate = "2000-01-01";
			endDate = "2100-01-01";
		} // switch
		return new String[] { startDate, endDate };
	}

	// ------------------------------------------------------------------------------
	/**
	 * 判断是否闰年
	 * 
	 * @param y
	 *            年份
	 * @return true or false
	 */
	public static final boolean isLeapYear(int y) {
		if (y % 4 == 0) {
			if (y % 100 == 0) {
				if (y % 400 == 0)
					return true;
				else
					return false;
			} else {
				return true;
			} // else
		} // if
		return false;
	}

	/**
	 * 获取两个日期间的月份间隔
	 * 
	 * @param date
	 * @param date
	 * @return int
	 */
	public static final int monthsIndays(Date early, Date late) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(early);
		c2.setTime(late);
		int earlyYear = c1.get(Calendar.YEAR);
		int earlyMonth = c1.get(Calendar.MONTH);
		int lateYear = c2.get(Calendar.YEAR);
		int lateMonth = c2.get(Calendar.MONTH);
		int months = (lateYear - earlyYear) * 12 + lateMonth - earlyMonth + 1;
		return months;
	}


	// ------查看是否闰年----------------
	public static final boolean checkLeapYear(int Year) {
		boolean isLeapYear = false;
		if (Year % 4 == 0 && Year % 100 != 0) {
			isLeapYear = true;
		}
		if (Year % 400 == 0)
			isLeapYear = true;
		else if (Year % 4 != 0) {
			isLeapYear = false;
		}
		return isLeapYear;
	}

	// --------计算当月天数---------------
	// 要输入年份的原因是要判断二月29天还是28天
	public static final int checkMonth(int Month, int Year) {
		int Dates = 0;
		if (Month < 0 || Month > 12) {
			System.out.println("Month Error");
		}
		if (Month == 1 || Month == 3 || Month == 5 || Month == 7 || Month == 8
				|| Month == 10 || Month == 12) {
			Dates = 31;
		}
		if (Month == 2 && checkLeapYear(Year)) {
			Dates = 29;
		}
		if (Month == 2 && !checkLeapYear(Year)) {
			Dates = 28;
		}
		if (Month == 4 || Month == 6 || Month == 9 || Month == 11) {
			Dates = 30;
		}
		return Dates;
	}

	// 返回early/later之间的，所有日期，不包括later
//	public static List<Date> getEachDateList(Date early, Date later) {
//		List<Date> dates = new ArrayList<Date>();
//		int days = DateTimeUtil.daysBetween(early, later);
//
//		for (int i = 0; i < days; i++) {
//			dates.add(DateTimeUtil.dateIncreaseByDay(early, i));
//		}
//		return dates;
//	}

	
	
	public static String parse2WeekDisplay(Date date){
		//13'05W2
		TimeZone tz = new SimpleTimeZone(-28800000,"America/Los_Angeles");
        Locale loc = new Locale("en","us");
        Calendar cal = Calendar.getInstance(tz,loc);
        //TimeZone:Asia/Shanghai  Locale:zh_CN(lan:zh,country:CN)
        //Calendar cal = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
        cal.setTime(getEndOfWeek(date));
        
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        int week = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        //int week = cal.get(Calendar.WEEK_OF_MONTH);
        
		return year.substring(2, 4) + "'" + ((month.length() == 1)?("0" + month):month) + "W" + week;
	}
	
	/**
	 * 将时间转成(12'06)格式
	 * @param date
	 */
	public static String parse2MonthFormat(Date date){
		//2012-06-01 转成 12'06
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        return year.substring(2, 4) + "'" + (month.length() < 2 ? "0"+month : month);
	}
	
	/**
	 * 返回date是本月的第几天
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 返回同比天（如果没有同比天，则返回null）
	 * @param date
	 * @return
	
	public static Date getDateOnDate(Date date){
		
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(date);
		 int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		 
		 date = add(date,-1,Calendar.MONTH); // getLastMonth
		 cal.setTime(date);
		 
		 int lastMonthDay = cal.get(Calendar.DAY_OF_MONTH);
		 return (dayOfMonth == lastMonthDay)?getBeginOfDay(cal.getTime()):null;
	}
	 */
	
	/**
	 * 返回同比天（上一年的这一天）
	 * @param date
	 * @return
	 */
	public static Date getDateOnDate(Date date){
		
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(date);
		 cal.add(Calendar.YEAR, -1);
		 
		 return cal.getTime();
	}
	
	/**
	 * 返回同比周
	 * @param date
	 * @return
	 */
	public static Date getWeekOnWeek(Date date){
		
		TimeZone tz = new SimpleTimeZone(-28800000,"America/Los_Angeles");
        Locale loc = new Locale("en","us");
        Calendar cal = Calendar.getInstance(tz,loc);
        
        cal.setTime(date);
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        
        cal.setTime(add(date,-1,Calendar.YEAR)); // getLastYear
        cal.set(Calendar.WEEK_OF_YEAR, weekOfYear); // 如果当年只有52周，设值为53时，会来到今年的第1周
        int lastYearWeekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        // 如果今年有53周，但上一年只有52周时，返回null
        return (weekOfYear == lastYearWeekOfYear)?getBeginOfWeek(cal.getTime()):null;
	}
	
	/**
	 * 返回年份值
	 * @param date
	 * @return
	 */
	public static String getYear(Date date){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		return String.valueOf(cal.get(Calendar.YEAR));
	}
	
	/**
	 * 返回月份值
	 * @param date
	 * @return
	 */
	public static String getMonth(Date date){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		return String.valueOf(cal.get(Calendar.MONTH)+1);
	}
	
	/**
	 * 根据date返回中文的day显示
	 * @param date
	 * @return
	 */
	public static String getDayChineseDescOfDate(Date date){
		
		if(date != null){
			
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			
			return c.get(Calendar.DAY_OF_MONTH)+"日";
		}else
			return "";
	}
	
	/**
	 * 根据date返回本月的第一周的首日，以周一为开始
	 * @param date
	 * @return
	 */
	public static Date getFirstWeekOfMonth(Date date){
		
		TimeZone tz = new SimpleTimeZone(-28800000,"America/Los_Angeles");
        Locale loc = new Locale("en","us");
        Calendar cal = Calendar.getInstance(tz,loc);
        
        cal.setTime(getEndOfWeek(date));
        int week = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        cal.add(Calendar.WEEK_OF_MONTH,1-week);
        
        return getBeginOfWeek(cal.getTime());
	}
	
	/**
	 * 根据date返回本月所有星期的首日，以周一为开始
	 * @param date
	 * @return
	 */
	public static List<Date> getWeeksOfMonth(Date date){
		
		List<Date> dates = new ArrayList<Date>();
		Date currentMonthBegin = getFirstWeekOfMonth(date);
		Date nextMonthBegin = getFirstWeekOfMonth(add(date,1,Calendar.MONTH));
		
		while(currentMonthBegin.before(nextMonthBegin)){
			
			dates.add(currentMonthBegin);
			currentMonthBegin = add(currentMonthBegin,1,Calendar.WEEK_OF_MONTH);
		}
		return dates;
	}
	
	/**
	 * 根据monthStrs(即一串以逗号分隔的数字)，返回年份+月份信息
	 * 如月份为5,7,则返回Date类型的(year)/05/01,(year)/07/01
	 * @param date
	 * @param monthStrs
	 * @return
	 */
	public static List<Date> getMonthsDateByOption(String year , String monthStrs){
		
		Calendar cal = Calendar.getInstance();
		List<Date> dates = new ArrayList<Date>();
		
		if(monthStrs != null && !"".equals(monthStrs.trim())){
			
			for(String month : monthStrs.split(",")){
				
				cal.set(Calendar.YEAR, Integer.parseInt(year));
				cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
				dates.add(getBeginOfMonth(cal.getTime()));
			}
		}
		return dates;
	}
	
	/**
	 * 根据dayStrs(即一串以逗号分隔的数字)，返回年份月份+day信息
	 * 如月份为5,7,则返回Date类型的(year)/(month)/05,(year)/(month)/07
	 * @param date
	 * @param dayStrs
	 * @return
	 */
	public static List<Date> getDaysDateByOption(String year , String month , String dayStrs){
		
		Calendar cal = Calendar.getInstance();
		List<Date> dates = new ArrayList<Date>();
		
		if(dayStrs != null && !"".equals(dayStrs.trim())){
			
			for(String day : dayStrs.split(",")){
				
				cal.set(Calendar.YEAR, Integer.parseInt(year));
				cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
				cal.set(Calendar.DATE, Integer.parseInt(day));
				dates.add(getBeginOfDay(cal.getTime()));
			}
		}
		return dates;
	}
	
	public static Date toDate(Long millis){
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		
		return cal.getTime();
	}
	
//	public static void main(String[] args) {
//		   1372608000 1367337600
//		 1372521600 1376150400
//		System.out.println(getTimeString(toDate(1367078400*1000l)));
//		System.out.println(getTimeString(toDate(1370707200*1000l)));
//		
//		Date date1 = new Date("2013/08/06");
//		Date date2 = new Date("2013/08/07");
//		List<Date> dates = _getBetweenDayDate(date1, date2);
//		for(Date d : dates) {
//			System.out.println(format(d, LONG_FORM));
//		}
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		System.out.println("");
//		2013-06-30 00:00:00
//		2013-08-11 00:00:00
//	}
}
