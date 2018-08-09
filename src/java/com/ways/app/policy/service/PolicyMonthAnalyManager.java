package com.ways.app.policy.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;


/**
 * 促销走势分析Service层接口
 * @author huangwenmei
 *
 */
public interface PolicyMonthAnalyManager {
	 
	/**
	 * 初始时间
	 * @param request
	 * @param paramsMap
	 */
	public String initDate(HttpServletRequest request,Map<String, Object> paramsMap);
	
	
	/**
	 * 促销信息
	 * @param paramsMap
	 * @return
	 */
	public String loadModelPolicy(HttpServletRequest request,Map<String, Object> paramsMap);
	
	/**
	 * 导出
	 * @param request
	 * @param paramsMap
	 */
	public Workbook exportExcel(HttpServletRequest request,Map<String, Object> paramsMap);


	

	/**
	 * 校验框
	 * */
public String checkPopBoxData(HttpServletRequest request,
		Map<String, Object> paramsMap);
	
}
