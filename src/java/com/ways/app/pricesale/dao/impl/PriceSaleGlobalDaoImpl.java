package com.ways.app.pricesale.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.Segment;
import com.ways.app.pricesale.dao.IPriceSaleGlobalDao;
import com.ways.framework.base.BaseDaoImpl;

/***********************************************************************************************
 * <br />价格销量组件数据访问层实现类
 * <br />Class name: PriceSaleGlobalDaoImpl.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Apr 16, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
@Repository("PriceSaleGlobalDaoImpl")
public class PriceSaleGlobalDaoImpl extends BaseDaoImpl implements IPriceSaleGlobalDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSegmentAndChildren(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("PriceSaleGlobal.getSegmentAndChildren", paramsMap);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getManf(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("PriceSaleGlobal.getManf", paramsMap);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getBrand(Map<String, Object> paramsMap)
	{
		return getSqlMapClientTemplate().queryForList("PriceSaleGlobal.getBrand", paramsMap);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BodyType> getBodyType(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("PriceSaleGlobal.getBodyType", paramsMap);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BodyType> getOrig(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("PriceSaleGlobal.getOrig", paramsMap);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BPSubModel> getSubmodelByBp(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("PriceSaleGlobal.getSubmodelByBp",paramsMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSubmodelBySegment(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("PriceSaleGlobal.getSubmodelBySegment",paramsMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByBrand(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("PriceSaleGlobal.getSubmodelByBrand",paramsMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByManf(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("PriceSaleGlobal.getSubmodelByManf",paramsMap);
	}

}
