package com.ways.app.policy.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/***********************************************************************************************
 * <br />车型利润分析 service接口
 * <br />Class name: GlobaModelProfitPriceManager.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Aug 19, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
public interface GlobalModelProfitPriceManager {
	/**
	 * 获取初始化子车型弹出框控件值
	 * @param request
	 * @param paramsMap
	 */
	public void getSubmodelModal(HttpServletRequest request,Map<String, Object> paramsMap);
	
	/**
	 * 获取车型下的型号数据
	 * @param request
	 * @param paramsMap
	 */
	public void getVersionModalByCommon(HttpServletRequest request,Map<String, Object> paramsMap);

}
