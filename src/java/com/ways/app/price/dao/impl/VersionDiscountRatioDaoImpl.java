package com.ways.app.price.dao.impl;
 
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.dao.IVersionDiscountRatioDao;
import com.ways.app.price.model.VersionInfoEntity;
import com.ways.framework.base.IBatisBaseDao;
 
@Repository("VersionDiscountRatioDaoImpl")
public class VersionDiscountRatioDaoImpl extends IBatisBaseDao implements IVersionDiscountRatioDao{
	
	/**
	 * 初始化时间
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap)
	{
	    return getSqlMapClientTemplate().queryForList("discountRatio.initDate", paramsMap);
	}

	/**
	 * 加载图形和表格
	 */
	@SuppressWarnings("unchecked")
	public List<VersionInfoEntity> loadVersionDiscountRatioChartAndTable(Map<String, Object> paramsMap)
	{
	     return getSqlMapClientTemplate().queryForList("discountRatio.loadVersionDiscountRatioChartAndTable", paramsMap);
	}
}
