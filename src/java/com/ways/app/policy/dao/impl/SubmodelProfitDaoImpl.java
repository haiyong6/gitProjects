package com.ways.app.policy.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.policy.dao.SubmodelProfitDao;
import com.ways.app.policy.model.VersionPromotionInfoEntity;
import com.ways.app.price.model.Manf;
import com.ways.app.price.model.SubModel;
import com.ways.framework.base.IBatisBaseFawvwDao;

/**
 * 车型利润分析DAO层实现类
 * @author yinlue
 *
 */
@Repository("submodelProfitDaoImpl")
public class SubmodelProfitDaoImpl extends IBatisBaseFawvwDao implements SubmodelProfitDao {

	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfit.initDate", paramsMap);
	}
	
	/**
	 * 加载车型利润图形和表格(型号-对象对比)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List loadVersionProfitChartAndTable(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfit.loadVersionChart", paramsMap);
	}
	
	/**
	 * 导出车型利润图形和表格(车型,厂商-对象对比)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List exportProfitData(Map<String, Object> paramsMap) 
	{	
	    return getSqlMapClientTemplate().queryForList("submodelProfit.exportSubmodelProfitData", paramsMap);
	}

	/**
	 * 校验弹出框有效数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SubModel> checkPopBoxData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfit.checkPopBoxData", paramsMap);
	}
	
	/**
	 * 校验生产商弹出框有效数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Manf> checkManfPopBoxData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfit.checkManfPopBoxData", paramsMap);
	} 


	/**
	 * 车型维度查询数据
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionPromotionInfoEntity> loadSubmodelChart(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("submodelProfit.loadSubmodelChart", paramsMap);
	}
}
