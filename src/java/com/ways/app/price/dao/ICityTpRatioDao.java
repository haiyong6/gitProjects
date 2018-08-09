package com.ways.app.price.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.VersionAreaInfoEntity;




/**
 * 城市成交价Dao层接口
 * @author yinlue
 *
 */
public interface ICityTpRatioDao {

	/**
	 * 初始化时间
	 * @param paramsMap
	 * @return
	 */
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap);
	
	/**
	 * 加载城市成交价对比图形和表格
	 * @param paramsMap
	 */
	public List<VersionAreaInfoEntity> loadCityTpRatioChartAndTable(Map<String, Object> paramsMap);
}
