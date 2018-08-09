package com.ways.app.price.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.InitialDate;
import com.ways.app.price.model.PriceIndexEntity;
import com.ways.app.price.model.VersionPriceIndexInfo;

/**
 * 价格降幅据Dao层接口
 * @author yinlue
 *
 */
public interface IPriceIndexDao {
	
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
	public List<PriceIndexEntity> getPriceIndexAnalyseData(Map<String, Object> paramsMap);
	
	/**
	 * 导出价格降幅原始数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<VersionPriceIndexInfo> exportPriceIndexOriginalData(Map<String, Object> paramsMap);
	
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
	 * 2017年价格降幅新算法
	 */
	public List<PriceIndexEntity> getPriceIndexAnalyseNewData(Map<String, Object> paramsMap);

	/**
	 * 2017年价格降幅新算法导出
	 * @param paramsMap
	 * @return
	 */
	public List<VersionPriceIndexInfo> exportPriceIndexOriginalNewData(Map<String, Object> paramsMap);
}

	
