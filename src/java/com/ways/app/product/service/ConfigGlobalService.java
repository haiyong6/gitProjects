package com.ways.app.product.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 配置公共Service层接口
 * @author yinlue
 *
 */
public interface ConfigGlobalService {

	
	/**
	 * 获取配置大类
	 * @param paramsMap
	 * @return
	 */
	public void getConfigClassify(HttpServletRequest request,Map<String, String> paramsMap);
	
	/**
	 * 获取配置信息集合
	 * @param paramsMap
	 * @return
	 */
	public String getConfigInfoList(HttpServletRequest request,Map<String, String> paramsMap);
	
	/**
	 * 获取配置值
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String getConfigValue(HttpServletRequest request,Map<String, String> paramsMap);
	
	/**
	 * 获取车身形式
	 * @param request
	 * @param paramsMap
	 */
	public void getSubModelBodyType(HttpServletRequest request,Map<String, String> paramsMap);
	
	/**
	 * 获取初始化子车型弹出框控件值
	 */
	public void getProductSubmodelModal(HttpServletRequest request,Map<String, String> paramsMap);
}
