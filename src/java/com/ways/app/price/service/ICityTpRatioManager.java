package com.ways.app.price.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * 城市成交价Service层接口
 * @author yinlue
 *
 */
public interface ICityTpRatioManager {
	 
	
	/**
	 * 初始时间
	 * @param request
	 * @param paramsMap
	 */
	public String initDate(HttpServletRequest request,Map<String, Object> paramsMap);
	
	/**
	 * 加载城市成交价对比图形和表格
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String loadCityTpRatioChartAndTable(HttpServletRequest request,Map<String, Object> paramsMap);
	
	/**
	 * 导出
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public Workbook exportExcel(HttpServletRequest request,Map<String, Object> paramsMap);
}
