package com.ways.app.price.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;


/**
 * 车型利润分析Service层接口
 * @author yinlue
 *
 */
public interface CityProfitDistributionManager {
	 
	/**
	 * 初始时间
	 * @param request
	 * @param paramsMap
	 */
	public String initDate(HttpServletRequest request,Map<String, Object> paramsMap);
	
	/**
	 * 加载车型利润图形和表格
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String loadModelProfitChartAndTable(HttpServletRequest request,Map<String, Object> paramsMap);
	
	/**
	 * 导出
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public Workbook exportExcel(HttpServletRequest request,Map<String, Object> paramsMap) ;
	
}
