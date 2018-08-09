package com.ways.app.pricesale.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

/***********************************************************************************************
 * <br />价格段销量分析接口
 * <br />Class name: VolumeByPriceRangeManager.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Apr 13, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
public interface ICompetingProductManager {
	/**
	 * 实始化时间
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void initDate(HttpServletRequest request,Map<String, Object> paramsMap);
	
	/**
	 * 获取价格段销量分析数据
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String getCompetingProductData(HttpServletRequest request,Map<String, Object> paramsMap);
	
	/**
	 * 获取车型数据
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void getSubmodelModal(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 价格段销量分析数据导出
	 * 
	* @param paramsMap
	* @return    
	*/
	public Workbook exportExcel(HttpServletRequest request,Map<String, Object> paramsMap);
}
