package com.ways.app.price.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.dao.IPriceIndexDao;
import com.ways.app.price.model.InitialDate;
import com.ways.app.price.model.PriceIndexEntity;
import com.ways.app.price.model.VersionPriceIndexInfo;
import com.ways.framework.base.IBatisBaseDao;

/**
 * 价格降幅DAO层实现类
 * @author yinlue
 *
 */
@Repository("PriceIndexDaoImpl")
public class PriceIndexDaoImpl extends IBatisBaseDao implements IPriceIndexDao {

	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	@Override
	public List<?> initDate(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("priceIndex.initDate", paramsMap);
	}

	/**
	 * 获取价格降幅分析数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PriceIndexEntity> getPriceIndexAnalyseData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("priceIndex.getPriceIndexAnalyseData", paramsMap);
	}
	
	/**
	 * 2017价格降幅新算法
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PriceIndexEntity> getPriceIndexAnalyseNewData(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("priceIndex.getPriceIndexAnalyseNewData", paramsMap);
	}
	
	/**
	 * 导出价格降幅原始数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionPriceIndexInfo> exportPriceIndexOriginalData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("priceIndex.exportPriceIndexOriginalData", paramsMap);
	}

	/**
	 * 导出价格降幅原始数据2017新算法
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionPriceIndexInfo> exportPriceIndexOriginalNewData(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("priceIndex.exportPriceIndexOriginalNewData", paramsMap);
	}
	
	/**
	 * 英文导出excel英文字段
	 * 
	 * @param paramsMap
	 * @return
	 */
	@Override
	public List<?> getExportContentEN(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("priceIndex.getExportContentEN", paramsMap);
	}
	
	/**
	 * 获取对象名称
	 * 
	 * @param paramsMap
	 * @return
	 */
	@Override
	public String getObjName(Map<String, Integer> paramsMap) {
		return (String)getSqlMapClientTemplate().queryForObject("priceIndex.getObjName", paramsMap);
	}

	/**
	 * 指导价成交价改变时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<InitialDate> tpDate(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("priceIndex.tpDate", paramsMap);
	}
}
