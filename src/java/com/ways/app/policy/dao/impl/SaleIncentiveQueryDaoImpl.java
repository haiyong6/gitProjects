package com.ways.app.policy.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.policy.dao.SaleIncentiveQueryDao;
import com.ways.app.policy.model.SalesIncentiveEntity;
import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.ObjectGroup;
import com.ways.app.price.model.Segment;
import com.ways.framework.base.IBatisBaseFawvwDao;

@Repository("saleIncentiveQueryDao")
public class SaleIncentiveQueryDaoImpl extends IBatisBaseFawvwDao implements SaleIncentiveQueryDao{
	
	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	@Override
	public List<?> initDate(Map<String, Object> paramsMap)
	{
        return getSqlMapClientTemplate().queryForList("saleIncentive.initDate", paramsMap);
	}
	
	/**
	 * 获取销售激励查询数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SalesIncentiveEntity> getSaleIncentiveQueryData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("saleIncentive.getSaleIncentiveQueryData", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("saleIncentive.getSubmodelByBp", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("saleIncentive.getSubmodelBySegment", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("saleIncentive.getSubmodelByBrand", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("saleIncentive.getSubmodelByManf", paramsMap);
	}

	/**
	 * 获取车身形式
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BodyType> getBodyType(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("saleIncentive.getBodyType", paramsMap);
	}
	
	/**
	 * 获取一汽大众常用型号组
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectGroup> getAutoCustomGroup(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("saleIncentive.getAutoCustomGroup", paramsMap);
	}
}
