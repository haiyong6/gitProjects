package com.ways.app.price.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

public interface IMsrpQueryManager {
	/**
	 * 实始化时间
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void initDate(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 获取指导价查询数据表格
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String getMsrpQueryData(HttpServletRequest request, Map<String, Object> paramsMap);
	
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
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public void getBodyType(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	* 获取一汽大众常用型号组
	* 
	* @param request
	* @param paramsMap    
	*/
	public void getAutoCustomGroup(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 导出
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public Workbook exportExcel(HttpServletRequest request, Map<String, Object> paramsMap);
}
