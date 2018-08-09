package com.ways.app.policy.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * 促销分类分析Service层接口
 * @author zhaohaiyong
 *
 */
public interface IPromotionGroupManager {

	/**
	 * 初始时间
	 * @param request
	 * @param paramsMap
	 */
	public String initDate(HttpServletRequest request,Map<String, Object> paramsMap);
	
	/**
	 * 校验弹出框有效数据
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String checkPopBoxData(HttpServletRequest request,Map<String, Object> paramsMap);
	
	/**
	 * 校验生产商弹出框有效数据
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String checkManfPopBoxData(HttpServletRequest request,Map<String, Object> paramsMap);
	
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
