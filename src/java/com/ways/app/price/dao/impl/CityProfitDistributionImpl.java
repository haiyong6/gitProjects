package com.ways.app.price.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.dao.CityProfitDistributionDao;
import com.ways.app.price.model.SubModel;
import com.ways.app.price.model.VersionInfoEntity;
import com.ways.framework.base.IBatisBaseDao;

/**
 * 车型利润分析DAO层实现类
 * @author yinlue
 *
 */
@Repository("cityProfitDistributionImpl")
public class CityProfitDistributionImpl extends IBatisBaseDao implements CityProfitDistributionDao {

	/**
	 * 初始化时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("CityProfitDistribution.initDate",paramsMap);
	}

	/**
	 * 加载车型利润图形和表格
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionInfoEntity> loadModelProfitChartAndTable(Map<String, Object> paramsMap) 
	{
		if("2".equals(paramsMap.get("analysisType")))
		{
			return getSqlMapClientTemplate().queryForList("CityProfitDistribution.loadCityProfitChartAndTable",paramsMap);
		}
		return getSqlMapClientTemplate().queryForList("CityProfitDistribution.loadModelProfitChartAndTable",paramsMap);
	}

	/**
	 * 校验弹出框有效数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SubModel> checkPopBoxData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("CityProfitDistribution.checkPopBoxData",paramsMap);
	}


}
