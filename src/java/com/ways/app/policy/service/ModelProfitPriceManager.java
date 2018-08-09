package com.ways.app.policy.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/***********************************************************************************************
 * <br />车型利润分析接口
 * <br />Class name: ModelProfitPriceManager.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Aug 11, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
public interface ModelProfitPriceManager {

	/**
	 * 初始时间
	 * @param request
	 * @param paramsMap
	 */
	public void initDate(HttpServletRequest request,Map<String, Object> paramsMap);
	
	/**
	 * 获取周,半月,季度
	 * @param request
	 * @param paramsMap
	 */
	public String getDateUnit(HttpServletRequest request,Map<String, Object> paramsMap);
}
