package com.ways.app.pricesale.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.Segment;
import com.ways.app.pricesale.dao.IVolumeByPriceRangeDao;
import com.ways.app.pricesale.model.VolumeByPriceRangeEntity;
import com.ways.framework.base.IBatisBaseDao;

/***********************************************************************************************
 * <br />价格段销量分析Dao层实现类
 * <br />Class name: VolumeByPriceRangeDaoImpl.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Apr 13, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
@Repository("VolumeByPriceRangeDaoImpl")
public class VolumeByPriceRangeDaoImpl extends IBatisBaseDao implements IVolumeByPriceRangeDao {

	@Override
	public List<?> initDate(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("VolumeByPriceRange.initDate",paramsMap);
	}
	
	/**
	 * 获取车型数据通过本竞品
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BPSubModel> getSubmodelByBp(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("VolumeByPriceRange.getSubmodelByBp", paramsMap);
	}

	/**
	 * 获取车型数据通过细分市场
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSubmodelBySegment(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("VolumeByPriceRange.getSubmodelBySegment", paramsMap);
	}

	/**
	 * 获取车型数据通过品牌
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByBrand(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("VolumeByPriceRange.getSubmodelByBrand", paramsMap);
	}

	/**
	 * 获取车型数据通过厂商
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByManf(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("VolumeByPriceRange.getSubmodelByManf", paramsMap);
	}

	/**
	 * 获取级别对应名称
	 * 
	 * @param gradeId
	 * @return
	 */
	@Override
	public String getGradeName(String gradeId) 
	{
	    return (String) getSqlMapClientTemplate().queryForObject("VolumeByPriceRange.getGradeName", gradeId);	
	}
	
	/**
	 * 获取价格段销量分析数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VolumeByPriceRangeEntity> getVolumeByPriceRangeData(Map<String, Object> paramsMap) 
	{
		List<VolumeByPriceRangeEntity> dataList = null;
		try {
			dataList = getSqlMapClientTemplate().queryForList("VolumeByPriceRange.getVolumeByPriceRangeData", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}

}
