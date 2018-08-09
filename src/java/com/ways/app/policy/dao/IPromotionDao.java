package com.ways.app.policy.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.policy.model.ObjectInfoEntity;
import com.ways.app.policy.model.VersionPromotionInfoEntity;
import com.ways.app.price.model.Manf;
import com.ways.app.price.model.SubModel;

/**
 * 促销走势分析Dao层接口
 * @author huangwenmei
 *
 */
public interface IPromotionDao {

	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap);
	
	/**
	 * 加载促销走势分析图形和表格(型号)
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<VersionPromotionInfoEntity> loadVersionPromotionChartAndTable(Map<String, Object> paramsMap);
	
	/**
	 * 加载促销走势分析图形和表格(车型)
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<ObjectInfoEntity> loadModelPromotionChartAndTable(Map<String, Object> paramsMap);
	
	/**
	 * 加载促销走势分析图形和表格(厂商品牌,品牌,系别,级别)
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<ObjectInfoEntity> loadModelPromotionChartAndTable2(Map<String, Object> paramsMap);
	
	/**
	 * 导出促销走势分析图形和表格(型号)
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<VersionPromotionInfoEntity> exportVersionPromotionChartAndTable(Map<String, Object> paramsMap);
	
	/**
	 * 导出促销走势分析图形和表格(车型)
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<VersionPromotionInfoEntity> exportPromotionData(Map<String, Object> paramsMap);
	
	/**
	 * 导出促销走势分析图形和表格(厂商品牌,品牌,系别,级别)
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<VersionPromotionInfoEntity> exportPromotionData2(Map<String, Object> paramsMap);
	
	/**
	 * 检查车型,型号弹出框数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<SubModel> checkPopBoxData(Map<String, Object> paramsMap);
	
	/**
	 * 检查厂商品牌、品牌、系别、级别弹出框数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Manf> checkManfPopBoxData(Map<String, Object> paramsMap);
}
