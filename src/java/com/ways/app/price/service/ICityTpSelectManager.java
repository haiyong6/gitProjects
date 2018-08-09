package com.ways.app.price.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * 成交价查询Service层接口
 * 
 * @author songguobiao
 *
 */
public interface ICityTpSelectManager {
	
	/**
	 * 初始化时间
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void initDate(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 获取车型数据
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
	 * 获取型号成交价数据
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String getVersionTpData(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 获取城市的名称和排序
	 * 
	 * @return
	 */
    public String getCityNameAndSort(Map<String, Object> paramsMap);
    
	/**
	 * 导出
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public Workbook exportExcel(HttpServletRequest request, Map<String, Object> paramsMap);
}
