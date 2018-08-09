package com.ways.app.price.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.Area;
import com.ways.app.price.model.CityPriceIndexEntity;
import com.ways.app.price.model.InitialDate;
import com.ways.app.price.model.VersionCityPriceIndexInfo;

/**
 * 区域价格降幅据Dao层接口
 * @author yinlue
 *
 */
public interface ICityPriceIndexDao {
	
	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<?> initDate(Map<String, Object> paramsMap);
	
	/**
	 * 获取价格降幅分析数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<CityPriceIndexEntity> getCityPriceIndexAnalyseData(Map<String, Object> paramsMap);
	
	/**
	 * 导出价格降幅原始数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<VersionCityPriceIndexInfo> exportCityPriceIndexOriginalData(Map<String, Object> paramsMap);
	
	/**
	 * 查询英文字节
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<?> getExportContentEN(Map<String, Object> paramsMap);
	
	/**
	 * 获取对象名称
	 * 
	 * @param paramsMap
	 * @return
	 */
	public String getObjName(Map<String, Integer> paramsMap);

	/**
	 * 指导价成交价改变时间
	 * @param paramsMap
	 * @return
	 */
	public List<InitialDate> tpDate(Map<String, Object> paramsMap);

	/**
	 * 获取初始化城市控件值
	 */
	public List<Area> getCityModal(Map<String, Object> paramsMap);

	/**
	 * 获取城市中英文对照
	 * @param paramsExportMap
	 * @return
	 */
	public List<Map<String, String>> getExportCityContentEN(Map<String, Object> paramsExportMap);

	/**
	 * 成交价维度当时间段大于2016年时采用新算法
	 * @param paramsMap
	 * @return
	 */
	public List<CityPriceIndexEntity> getCityPriceIndexAnalyseNewData(Map<String, Object> paramsMap);

	
	/**
	 * 成交价维度当时间段大于2016年时采用新算法
	 * @param paramsMap
	 * @return
	 */
	public List<VersionCityPriceIndexInfo> exportCityPriceIndexOriginalNewData(Map<String, Object> paramsMap);

	
}

	
