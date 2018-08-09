package com.ways.app.policy.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.policy.model.VersionPromotionInfoEntity;
import com.ways.app.price.model.Manf;
import com.ways.app.price.model.SubModel;

/**
 * 促销分类分析Dao层接口
 * @author zhaohaiyong
 *
 */

public interface IPromotionGroupDao {

	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap);
	
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
	 * 导出促销走势分析图形和表格(厂商/品牌/系别/级别)
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
	 * 检查生产商、品牌、系别、级别弹出框数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Manf> checkManfPopBoxData(Map<String, Object> paramsMap);
	
	/**
	 * 加载促销分类分析图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List loadPromotionGroupDataChartAndTable(Map<String, Object> paramsMap);

	/**
	 * 加载促销分类分析图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List loadPromotionGroupChartAndTableByVersion(Map<String, Object> paramsMap);
	
}
