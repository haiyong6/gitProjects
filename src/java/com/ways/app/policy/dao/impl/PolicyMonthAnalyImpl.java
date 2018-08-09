package com.ways.app.policy.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.policy.dao.PolicyMonthAnalyDao;
import com.ways.app.policy.model.PolicyMonthInfoEntity;
import com.ways.app.policy.model.PolicySubModelInfo;
import com.ways.app.policy.model.PolicySubmodelEntity;
import com.ways.app.price.model.SubModel;
import com.ways.framework.base.IBatisBaseFawvwDao;

/**
 * 促销查询DAO层实现类
 * @author yuml
 *
 */
@Repository("policyMonthAnalyImpl")
public class PolicyMonthAnalyImpl extends IBatisBaseFawvwDao implements PolicyMonthAnalyDao {

	/**
	 * 初始化时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("ploicyMonthAnaly.initDate",paramsMap);
	}

	/**
	 * 获取子车型简单表格政策数据查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PolicySubModelInfo> getSubModelPolicyToSimple(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("ploicyMonthAnaly.detail",paramsMap);
	}

	/**
	 * 获取子车型明细表格政策数据查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PolicyMonthInfoEntity> getSubModelPolicyToDetailed(Map<String, Object> paramsMap) 
	{
		//return getSqlMapClientTemplate().queryForList("ploicyMonthAnaly.getSubModelPolicyToDetailed",paramsMap);
		return getSqlMapClientTemplate().queryForList("ploicyMonthAnaly.detail",paramsMap);
	}

	/**
	 * 按月获取子车型表格政策数据查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PolicySubModelInfo> getSubModelPolicyByMonth(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("ploicyMonthAnaly.getSubModelPolicyByMonth",paramsMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubModel> checkPopBoxData(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("ploicyMonthAnaly.checkPopBoxData", paramsMap);
	}

	/**
	 * 获得车型信息
	 */
	@Override
	public List<PolicySubmodelEntity> getSubmodel(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("ploicyMonthAnaly.submodelQuery", paramsMap);
	}

	@Override
	public List<?> getYearMonth(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("ploicyMonthAnaly.yearMonthQuery", paramsMap);
	}
}
