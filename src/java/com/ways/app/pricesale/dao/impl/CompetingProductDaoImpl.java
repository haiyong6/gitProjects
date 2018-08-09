package com.ways.app.pricesale.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.Segment;
import com.ways.app.pricesale.dao.ICompetingProductDao;
import com.ways.app.pricesale.model.CompetingProductEntity;
import com.ways.framework.base.IBatisBaseFawvwDao;

/***********************************************************************************************
 * <br />价格段销量分析Dao层实现类
 * <br />Class name: VolumeByPriceRangeDaoImpl.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Apr 13, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
@Repository("CompetingProductDaoImpl")
public class CompetingProductDaoImpl extends IBatisBaseFawvwDao implements ICompetingProductDao {

	@Override
	public List<?> initDate(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("CompetingProduct.initDate",paramsMap);
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
		return getSqlMapClientTemplate().queryForList("CompetingProduct.getSubmodelByBp", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("CompetingProduct.getSubmodelBySegment", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("CompetingProduct.getSubmodelByBrand", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("CompetingProduct.getSubmodelByManf", paramsMap);
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
	    return (String) getSqlMapClientTemplate().queryForObject("CompetingProduct.getGradeName", gradeId);	
	}
	
	/**
	 * 获取竞品价量分析数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CompetingProductEntity> getCompetingProductData(Map<String, Object> paramsMap) 
	{
		List<CompetingProductEntity> dataList = null;
		try {
			dataList = getSqlMapClientTemplate().queryForList("CompetingProduct.getCompetingProductObjectData", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 获取竞品价量分析车型数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CompetingProductEntity> getCompetingProductSubmodelData(Map<String, Object> paramsMap) {
		List<CompetingProductEntity> dataList = null;
		try {
			dataList = getSqlMapClientTemplate().queryForList("CompetingProduct.getCompetingProductSubmodelData", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 按页面要求的车型排序
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CompetingProductEntity> getSubmodelListOrder(Map<String, Object> paramsMap) {
		List<CompetingProductEntity> dataList = null;
		try {
			dataList = getSqlMapClientTemplate().queryForList("CompetingProduct.getSubmodelListOrder", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 对比对象list
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CompetingProductEntity> getListObjectNames(Map<String, Object> paramsMap) {
		List<CompetingProductEntity> dataList = null;
		try {
			dataList = getSqlMapClientTemplate().queryForList("CompetingProduct.getListObjectNames", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 导出对比对象
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CompetingProductEntity> exportCompetingProductObjectData(Map<String, Object> paramsMap) {
		List<CompetingProductEntity> dataList = null;
		try {
			dataList = getSqlMapClientTemplate().queryForList("CompetingProduct.exportCompetingProductObjectData", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 导出车型
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CompetingProductEntity> exportCompetingProductSubmodelData(Map<String, Object> paramsMap) {
		List<CompetingProductEntity> dataList = null;
		try {
			dataList = getSqlMapClientTemplate().queryForList("CompetingProduct.exportCompetingProductSubmodelData", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}

}
