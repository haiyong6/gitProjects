package com.ways.app.policy.dao;

import java.util.List;
import java.util.Map;

/***********************************************************************************************
 * <br />价格利润走势Dao层接口类
 * <br />Class name: ModelProfitPriceDao.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Aug 11, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
public interface ModelProfitPriceDao {

	/**
	 * 初始化时间
	 * @param paramsMap
	 * @return
	 */
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap);
	
	/**
	 * 获取周,半月,季度
	 * @param request
	 * @param paramsMap
	 */
	public List<Map<String, String>> getDateUnit(Map<String, Object> paramsMap);
}
