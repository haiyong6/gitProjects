package com.ways.app.price.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.dao.IProfitDao;
import com.ways.app.price.model.Manf;
import com.ways.app.price.model.SubModel;
import com.ways.framework.base.IBatisBaseFawvwDao;

/**
 * 车型利润分析DAO层实现类
 * @author yinlue
 *
 */
@Repository("profitDaoImpl")
public class ProfitDaoImpl extends IBatisBaseFawvwDao implements IProfitDao {

	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("profit.initDate", paramsMap);
	}

	/**
	 * 加载车型利润图形和表格(车型,厂商-城市对比)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List loadObjectCityProfitChartAndTable(Map<String, Object> paramsMap)
	{
		return getSqlMapClientTemplate().queryForList("profit.loadObjectCityProfitChartAndTable", paramsMap);
	}
	
	/**
	 * 加载车型利润图形和表格(车型,厂商-对象对比)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List loadModelProfitChartAndTable(Map<String, Object> paramsMap)
	{
		return getSqlMapClientTemplate().queryForList("profit.loadModelProfitChartAndTable", paramsMap);
	}
	
	/**
	 * 加载车型利润图形和表格(型号-城市对比)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List loadCityProfitChartAndTable(Map<String, Object> paramsMap) 
	{
	    return getSqlMapClientTemplate().queryForList("profit.loadCityProfitChartAndTable", paramsMap);	
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
		return getSqlMapClientTemplate().queryForList("profit.loadVersionProfitChartAndTable", paramsMap);
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
	    return getSqlMapClientTemplate().queryForList("profit.exportProfitData", paramsMap);
	}

	/**
	 * 导出车型利润图形和表格(车型,厂商-城市对比)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List exportCityProfitData(Map<String, Object> paramsMap) 
	{	
	    return getSqlMapClientTemplate().queryForList("profit.exportCityProfitData", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("profit.checkPopBoxData", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("profit.checkManfPopBoxData", paramsMap);
	} 

	/**
	 * 查询是否存在最新周数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> findLatestWeek(Map<String, Object> paramsMap)
	{
		return getSqlMapClientTemplate().queryForList("profit.findLatestWeek", paramsMap);
	}
}
