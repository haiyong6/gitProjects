package com.ways.app.policy.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.policy.model.PolicyMonthInfoEntity;
import com.ways.app.policy.model.PolicySubModelInfo;
import com.ways.app.policy.model.PolicySubmodelEntity;
import com.ways.app.price.model.SubModel;



/**
 * 促销查询Dao层接口
 * @author yuml
 *
 */
public interface PolicyMonthAnalyDao {

	/**
	 * 初始化时间
	 * @param paramsMap
	 * @return
	 */
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型简单表格政策数据查询
	 * @param paramsMap
	 * @return
	 */
	public List<PolicySubModelInfo> getSubModelPolicyToSimple(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型明细表格政策数据查询
	 * @param paramsMap
	 * @return
	 */
	public List<PolicyMonthInfoEntity> getSubModelPolicyToDetailed(Map<String, Object> paramsMap);
	
	/**
	 * 按月获取子车型表格政策数据查询
	 * @param paramsMap
	 * @return
	 */
	
	
	public List<PolicySubModelInfo> getSubModelPolicyByMonth(Map<String, Object> paramsMap);
	/**
	 * 校验框
	 * */
	public List<SubModel> checkPopBoxData(Map<String, Object> paramsMap);

	/**
	 * 获得车型信息
	 * @param paramsMap
	 * @return
	 */
	public List<PolicySubmodelEntity> getSubmodel(Map<String, Object> paramsMap);

	/**
	 * 获得年月信息
	 * @param paramsMap
	 * @return
	 */

	public List<?> getYearMonth(Map<String, Object> paramsMap);
	
}
