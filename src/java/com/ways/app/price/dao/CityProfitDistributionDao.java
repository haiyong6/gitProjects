package com.ways.app.price.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.SubModel;
import com.ways.app.price.model.VersionInfoEntity;



/**
 * 车型利润分析Dao层接口
 * @author yinlue
 *
 */
public interface CityProfitDistributionDao {

	/**
	 * 初始化时间
	 * @param paramsMap
	 * @return
	 */
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap);
	
	/**
	 * 加载车型利润图形和表格
	 * @param paramsMap
	 * @return
	 */
	public List<VersionInfoEntity> loadModelProfitChartAndTable(Map<String, Object> paramsMap);
	
	/**
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<SubModel> checkPopBoxData(Map<String, Object> paramsMap);
}
