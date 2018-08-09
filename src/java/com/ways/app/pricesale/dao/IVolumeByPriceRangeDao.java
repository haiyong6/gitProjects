package com.ways.app.pricesale.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.Segment;
import com.ways.app.pricesale.model.VolumeByPriceRangeEntity;

/***********************************************************************************************
 * <br />价格段销量分析Dao层接口
 * <br />Class name: VolumeByPriceRangeDao.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Apr 13, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
public interface IVolumeByPriceRangeDao {
	/**
	 * 初始化时间
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public List<?> initDate(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过本品ID查找其竟品车型
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<BPSubModel> getSubmodelByBp(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过细分市场
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Segment> getSubmodelBySegment(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过品牌
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getSubmodelByBrand(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过厂商
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getSubmodelByManf(Map<String, Object> paramsMap);
	
	/**
	 * 获取级别对应名称
	 * 
	 * @param gradeId
	 * @return
	 */
	public String getGradeName(String gradeId);
	
	/**
	* 函数功能说明 获取价格段销量分析查询结果
	* 
	* @param paramsMap
	* @return    
	* List<VolumeByPriceRangeEntity>   
	*/
	public List<VolumeByPriceRangeEntity> getVolumeByPriceRangeData(Map<String, Object> paramsMap);
}
