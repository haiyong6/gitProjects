package com.ways.app.policy.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.Segment;
import com.ways.app.price.model.SubModel;

/***********************************************************************************************
 * <br />车型利润分析 Dao层接口类
 * <br />Class name: GlobaModelProfitPriceDao.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Aug 19, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
public interface GlobalModelProfitPriceDao {
	/**
	 * 获取子车型通过本品ID查找其竟品车型
	 * @param paramsMap
	 * @return
	 */
	public List<BPSubModel> getSubmodelByBp(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过细分市场
	 * @param paramsMap
	 * @return
	 */
	public List<Segment> getSubmodelBySegment(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过品牌
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getSubmodelByBrand(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过厂商
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getSubmodelByManf(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型下型号数据
	 * @param paramsMap
	 * @return
	 */
	public List<SubModel> getVersionModalByCommon(Map<String, Object> paramsMap);
}
