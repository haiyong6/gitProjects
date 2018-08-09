package com.ways.app.policy.webapp.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	public static void main(String[] args) {
//		try {
//			Date date1 = new Date("2014-01");
//			Date date2 = new Date("2015-10");
//			System.out.println(calculateMonthIn(date1,date2)+"###");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public static int calculateMonthIn(Date date1, Date date2) {
		Calendar cal1 = new GregorianCalendar();
		cal1.setTime(date1);
		Calendar cal2 = new GregorianCalendar();
		cal2.setTime(date2);
		int c = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12
				+ cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
		return c;
	}
}
