package com.ways.app.policy.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.policy.dao.GlobalPolicyMonthAnalyDao;
import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.Segment;
import com.ways.framework.base.IBatisBaseFawvwDao;

/**
 * 促销查询DAO层实现类
 * @author yuml
 *
 */
@Repository("globalPolicyMonthAnalyImpl")
public class GlobalPolicyMonthAnalyImpl extends IBatisBaseFawvwDao implements GlobalPolicyMonthAnalyDao {

	
	/**
	 * 获取子车型通过本品ID查找其竟品车型
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BPSubModel> getSubmodelByBp(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("policyMonthAnalyGlobal.getSubmodelByBpPolicy",paramsMap);
	}

	/**
	 * 获取子车型通过细分市场
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSubmodelBySegment(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("policyMonthAnalyGlobal.getSubmodelBySegmentPolicy",paramsMap);
	}

	/**
	 * 获取子车型通过品牌
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByBrand(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("policyMonthAnalyGlobal.getSubmodelByBrandPolicy",paramsMap);
	}

	/**
	 * 获取子车型通过厂商
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByManf(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("policyMonthAnalyGlobal.getSubmodelByManfPolicy",paramsMap);
	}


}
