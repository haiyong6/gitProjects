package com.ways.app.price.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * 价格降幅Service层接口
 * @author yinlue
 *
 */
public interface ICityPriceIndexManager {
	 
	/**
	 * 实始化时间
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void initDate(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 获取区域价格降幅分析数据
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public String getCityPriceIndexAnalyseData(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 导出
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public Workbook exportExcel(HttpServletRequest request, Map<String, Object> paramsMap);
	
/**
 * 指导价成交价改变事件
 * @param request
 * @param paramsMap
 * @return
 */
	public String tpDate(HttpServletRequest request,Map<String, Object> paramsMap);

	/**
	 * 获取初始化城市控件值
	 * @param request
	 * @param paramsMap
	 */
	public void getCityModal(HttpServletRequest request,Map<String, Object> paramsMap);
	
}
