package com.ways.app.policy.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.policy.dao.ManfSalesDao;
import com.ways.app.policy.model.VersionPromotionInfoEntity;
import com.ways.framework.base.IBatisBaseFawvwDao;

@Repository("ManfSalesDao")
public class ManfSalesDaoImpl extends IBatisBaseFawvwDao implements ManfSalesDao {

	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap) 
	{
		List<Map<String, String>> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSales.initDate", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 加载厂商销售支持图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List loadManfSalesChartAndTable(Map<String, Object> paramsMap) 
	{
		List list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSales.loadManfSalesData", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 加载厂商销售支持图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List loadManfSalesChartAndTableByVersion(Map<String, Object> paramsMap) 
	{
		List list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSales.loadManfSalesDataByVersion", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 导出厂商销售支持图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionPromotionInfoEntity> exportManfSalesData(Map<String, Object> paramsMap) {
		return (List<VersionPromotionInfoEntity>)getSqlMapClientTemplate().queryForList("ManfSales.exportManfSalesData", paramsMap);
	}
	
	/**
	 * 通过型号导出厂商销售支持图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionPromotionInfoEntity> exportManfSalesDataByVersion(Map<String, Object> paramsMap) 
	{
		List<VersionPromotionInfoEntity> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSales.exportManfSalesDataByVersion", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
}
