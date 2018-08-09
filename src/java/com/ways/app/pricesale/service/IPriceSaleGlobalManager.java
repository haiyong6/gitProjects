package com.ways.app.pricesale.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/***********************************************************************************************
 * <br />价格销量公共类接口
 * <br />Class name: IPriceSaleGlobalManager.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Apr 16, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
public interface IPriceSaleGlobalManager {
	/**
	 * 获取细分市场以及所属子细分市场
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public void getSegmentAndChildren(HttpServletRequest request, Map<String, Object> paramsMap);
	/**
	 * 按首字母获取厂商
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public void getManf(HttpServletRequest request, Map<String, Object> paramsMap);
	/**
	 * 按首字母获取品牌
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public void getBrand(HttpServletRequest request, Map<String, Object> paramsMap);

	/**
	 * 获取车身形式
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public void getBodyType(HttpServletRequest request, Map<String, Object> paramsMap);
	/**
	 * 获取车系
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public void getOrig(HttpServletRequest request, Map<String, Object> paramsMap);
	/**
	 * 获取初始化子车型弹出框控件值
	 * @param request
	 * @param paramsMap
	 */
	public void getSubmodelModal(HttpServletRequest request,Map<String, Object> paramsMap);
}
