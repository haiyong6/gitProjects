package com.ways.app.price.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.Manf;
import com.ways.app.price.model.SubModel;

/**
 * 车型利润分析Dao层接口
 * @author yinlue
 *
 */
public interface IProfitDao {

	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap);
	
	/**
	 * 获取是否含最新周
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<String> findLatestWeek(Map<String, Object> paramsMap);
	
	/**
	 * 加载车型利润图形和表格(车型,厂商-城市对比)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List loadObjectCityProfitChartAndTable(Map<String, Object> paramsMap);

	/**
	 * 加载车型利润图形和表格(车型,厂商-对象对比)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List loadModelProfitChartAndTable(Map<String, Object> paramsMap);
	
	/**
	 * 加载车型利润图形和表格(型号-城市对比)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List loadCityProfitChartAndTable(Map<String, Object> paramsMap);
	
	/**
	 * 加载车型利润图形和表格(型号-对象对比)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List loadVersionProfitChartAndTable(Map<String, Object> paramsMap);
	
	/**
	 * 导出价格利润走势数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List exportProfitData(Map<String, Object> paramsMap);
	
	/**
	 * 导出价格利润走势数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List exportCityProfitData(Map<String, Object> paramsMap);
	
	/**
	 * 检查车型,型号弹出框数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<SubModel> checkPopBoxData(Map<String, Object> paramsMap);
	
	/**
	 * 检查生产商弹出框数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Manf> checkManfPopBoxData(Map<String, Object> paramsMap);
}
