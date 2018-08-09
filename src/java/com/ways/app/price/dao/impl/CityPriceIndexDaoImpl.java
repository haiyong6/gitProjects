package com.ways.app.price.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.dao.ICityPriceIndexDao;
import com.ways.app.price.model.Area;
import com.ways.app.price.model.CityPriceIndexEntity;
import com.ways.app.price.model.InitialDate;
import com.ways.app.price.model.VersionCityPriceIndexInfo;
import com.ways.framework.base.IBatisBaseFawvwDao;

/**
 * 区域价格降幅DAO层实现类
 * @author yinlue
 *
 */
@Repository("CityPriceIndexDaoImpl")
public class CityPriceIndexDaoImpl extends IBatisBaseFawvwDao implements ICityPriceIndexDao {

	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	@Override
	public List<?> initDate(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("cityPriceIndex.initDate", paramsMap);
	}

	/**
	 * 获取价格降幅分析数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CityPriceIndexEntity> getCityPriceIndexAnalyseData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("cityPriceIndex.getCityPriceIndexAnalyseData", paramsMap);
	}

	/**
	 * 获取价格降幅分析数据2017新算法
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CityPriceIndexEntity> getCityPriceIndexAnalyseNewData(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("cityPriceIndex.getCityPriceIndexAnalyseNewData", paramsMap);
	}
	
	/**
	 * 导出价格降幅原始数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionCityPriceIndexInfo> exportCityPriceIndexOriginalData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("cityPriceIndex.exportCityPriceIndexOriginalData", paramsMap);
	}

	/**
	 * 成交价维度当时间段大于2016年时采用新算法
	 * @param paramsMap
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionCityPriceIndexInfo> exportCityPriceIndexOriginalNewData(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("cityPriceIndex.exportCityPriceIndexOriginalNewData", paramsMap);
	}
	
	/**
	 * 英文导出excel英文字段
	 * 
	 * @param paramsMap
	 * @return
	 */
	@Override
	public List<?> getExportContentEN(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("cityPriceIndex.getExportContentEN", paramsMap);
	}
	
	/**
	 * 获取对象名称
	 * 
	 * @param paramsMap
	 * @return
	 */
	@Override
	public String getObjName(Map<String, Integer> paramsMap) {
		return (String)getSqlMapClientTemplate().queryForObject("cityPriceIndex.getObjName", paramsMap);
	}

	/**
	 * 指导价成交价改变时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<InitialDate> tpDate(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("cityPriceIndex.tpDate", paramsMap);
	}

	/**
	 * 获取初始化城市控件值
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Area> getCityModal(Map<String, Object> paramsMap)
	{
		return getSqlMapClientTemplate().queryForList("cityPriceIndex.getCityModal",paramsMap);
	}

	/**
	 * 获取城市中英文对照
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getExportCityContentEN(Map<String, Object> paramsExportMap) {
		return getSqlMapClientTemplate().queryForList("cityPriceIndex.getExportCityContentEN", paramsExportMap);
	}
}
