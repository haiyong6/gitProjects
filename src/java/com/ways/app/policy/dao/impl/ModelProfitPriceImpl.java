package com.ways.app.policy.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.policy.dao.ModelProfitPriceDao;
import com.ways.framework.base.IBatisBaseDao;

/***********************************************************************************************
 * <br />车型利润分析Dao实现类
 * <br />Class name: ModelProfitPriceImpl.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Aug 11, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
@Repository("ModelProfitPriceImpl")
public class ModelProfitPriceImpl extends IBatisBaseDao implements ModelProfitPriceDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("modelProfitPrice.initDate",paramsMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getDateUnit(Map<String, Object> paramsMap) {
		List<Map<String, String>> sList = null;
		try {
			sList =getSqlMapClientTemplate().queryForList("modelProfitPrice.getDateUnit", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sList;
	}

}
