package com.ways.app.price.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;


/**
 * 车型价格段分析Service层接口
 * 
 * @author songguobiao
 * @date 20161107
 *
 */
public interface ISubModelPriceAnalysisManager {
	 
	/**
	 * 初始时间
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String initDate(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 获取初始化子车型弹出框控件值
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void getSubmodelModal(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 获取车身形式
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void getBodyType(HttpServletRequest request, Map<String, Object> paramsMap); 
	
	/**
	 * 获取分析数据
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String getAnalysisData(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 导出
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public Workbook exportExcel(HttpServletRequest request, Map<String, Object> paramsMap);
}
