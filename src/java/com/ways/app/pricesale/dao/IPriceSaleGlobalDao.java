package com.ways.app.pricesale.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.Segment;

/***********************************************************************************************
 * <br />价格销量组件数据访问层接口
 * <br />Class name: IPriceSaleGlobalDao.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Apr 16, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
public interface IPriceSaleGlobalDao {
	/**
	 * 获取细分市场以及所属子细分市场
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<Segment> getSegmentAndChildren(Map<String, Object> paramsMap);
	/**
	 * 按首字母获取厂商
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<LetterObj> getManf(Map<String, Object> paramsMap);
	/**
	 * 按首字母获取品牌
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<LetterObj> getBrand(Map<String, Object> paramsMap);
	/**
	 * 获取车身形式
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<BodyType> getBodyType(Map<String, Object> paramsMap);
	/**
	 * 获取车系
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<BodyType> getOrig(Map<String, Object> paramsMap);
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
}
