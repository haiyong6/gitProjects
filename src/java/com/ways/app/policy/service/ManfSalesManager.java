package com.ways.app.policy.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

public interface ManfSalesManager {
	 
	/**
	 * 初始时间
	 * @param request
	 * @param paramsMap
	 */
	public String initDate(HttpServletRequest request,Map<String, Object> paramsMap);

	/**
	 * 加载促销走势分析图形和表格
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String loadChartAndTable(HttpServletRequest request,Map<String, Object> paramsMap);
	
	/**
	 * 导出
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public Workbook exportExcel(HttpServletRequest request,Map<String, Object> paramsMap);
}
