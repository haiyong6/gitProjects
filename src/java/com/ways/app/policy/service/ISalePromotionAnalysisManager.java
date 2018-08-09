package com.ways.app.policy.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * 销量促销分析Service层接口类
 * @author songguobiao
 *
 */
public interface ISalePromotionAnalysisManager {
	
	/**
	 * 初始化时间
	 * 
	 * @param request
	 * @param paramsMap
	 */
    public void initDate(HttpServletRequest request, Map<String, Object>paramsMap);
    
    /**
	 * 获取车型下的型号数据
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void getVersionModalByCommon(HttpServletRequest request, Map<String, Object> paramsMap);	
	
	/**
	 * 获取一汽大众常用型号组
	 * 
	 * @param request
	 * @param paramsMap    
	 */
	public void getAutoCustomGroup(HttpServletRequest request, Map<String, Object> paramsMap); 
	
	/**
	 * 获取初始化子车型弹出框控件值
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void getSubmodelModal(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 获取厂商
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void getManf(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 获取品牌
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void getBrand(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 获取系别
	 * @param request
	 * @param paramsMap
	 */
	public void getOrig(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 获取车身形式
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void getBodyType(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 获取细分市场以及所属子细分市场
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void getSegmentAndChildren(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 加载销量促销数据
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String loadChartAndTable(HttpServletRequest request, Map<String, Object> paramsMap);
	
	/**
	 * 导出
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public Workbook exportExcel(HttpServletRequest request, Map<String, Object> paramsMap);
}
