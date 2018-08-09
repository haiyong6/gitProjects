package com.ways.app.policy.dao.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.ways.app.policy.dao.IPromotionDao;
import com.ways.app.price.model.Manf;
import com.ways.app.price.model.SubModel;
import com.ways.app.policy.model.ObjectInfoEntity;
import com.ways.app.policy.model.VersionPromotionInfoEntity;
import com.ways.framework.base.IBatisBaseFawvwDao;

/**
 * 促销走势分析DAO层实现类
 * @author huangwenmei
 *
 */
@Repository("promotionDao")
public class PromotionDaoImpl extends IBatisBaseFawvwDao implements IPromotionDao {

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
		return getSqlMapClientTemplate().queryForList("promotion.initDate", paramsMap);
	}

	/**
	 * 加载促销走势分析图形和表格(型号)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionPromotionInfoEntity> loadVersionPromotionChartAndTable(Map<String, Object> paramsMap) {
		return (List<VersionPromotionInfoEntity>)getSqlMapClientTemplate().queryForList("promotion.loadVersionPromotionChartAndTable", paramsMap);
	}
	
	/**
	 * 加载促销走势分析图形和表格(车型)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectInfoEntity> loadModelPromotionChartAndTable(Map<String, Object> paramsMap) {
		return (List<ObjectInfoEntity>)getSqlMapClientTemplate().queryForList("promotion.loadModelPromotionChartAndTable", paramsMap);
	}
	
	/**
	 * 加载促销走势分析图形和表格(厂商品牌,品牌,系别,级别)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectInfoEntity> loadModelPromotionChartAndTable2(Map<String, Object> paramsMap) {
		return (List<ObjectInfoEntity>)getSqlMapClientTemplate().queryForList("promotion.loadModelPromotionChartAndTable2", paramsMap);
	}
	
	/**
	 * 导出促销走势分析图形和表格(型号)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionPromotionInfoEntity> exportVersionPromotionChartAndTable(Map<String, Object> paramsMap) {
		return (List<VersionPromotionInfoEntity>)getSqlMapClientTemplate().queryForList("promotion.exportVersionPromotionChartAndTable", paramsMap);
	}
	/**
	 * 导出促销走势分析图形和表格(车型)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionPromotionInfoEntity> exportPromotionData(Map<String, Object> paramsMap) {
		return (List<VersionPromotionInfoEntity>)getSqlMapClientTemplate().queryForList("promotion.exportPromotionData", paramsMap);
	}
	
	/**
	 * 导出促销走势分析图形和表格(厂商品牌,品牌,系别,级别)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionPromotionInfoEntity> exportPromotionData2(Map<String, Object> paramsMap) {
		return (List<VersionPromotionInfoEntity>)getSqlMapClientTemplate().queryForList("promotion.exportPromotionData2", paramsMap);
	}
	
	/**
	 * 校验弹出框有效数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SubModel> checkPopBoxData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("promotion.checkPopBoxData", paramsMap);
	}
	
	/**
	 * 校验检查生产商、品牌、系别、级别弹出框有效数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Manf> checkManfPopBoxData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("promotion.checkManfPopBoxData", paramsMap);
	}
}