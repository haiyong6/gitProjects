package com.ways.app.price.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.dao.ICityTpRatioDao;
import com.ways.app.price.model.VersionAreaInfoEntity;
import com.ways.framework.base.IBatisBaseDao;

/**
 * 城市成交价分析DAO层实现类
 * @author yinlue
 *
 */
@Repository("CityTpRatioDaoImpl")
public class CityTpRatioDaoImpl extends IBatisBaseDao implements ICityTpRatioDao {

	
	/**
	 * 初始化时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("cityTpRatio.initDate",paramsMap);
	}

	/**
	 * 加载城市成交价对比图形和表格
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionAreaInfoEntity> loadCityTpRatioChartAndTable(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("cityTpRatio.loadCityTpRatioChartAndTable",paramsMap);
	}



}
