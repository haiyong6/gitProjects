package com.ways.app.policy.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * 促销查询Service层接口
 * @author yuml
 *
 */
public interface GlobalPolicyMonthAnalyManager {
	 
	/**
	 * 获取初始化子车型弹出框控件值
	 * @param request
	 * @param paramsMap
	 */
	public void getSubmodelModal(HttpServletRequest request,Map<String, Object> paramsMap);
}
