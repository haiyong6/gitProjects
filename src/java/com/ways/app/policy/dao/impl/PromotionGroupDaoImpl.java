package com.ways.app.policy.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.policy.dao.IPromotionGroupDao;
import com.ways.app.policy.model.VersionPromotionInfoEntity;
import com.ways.app.price.model.Manf;
import com.ways.app.price.model.SubModel;
import com.ways.framework.base.IBatisBaseFawvwDao;
/**
 * 促销分类分析DAO层实现类
 * @author zhaohaiyong
 *
 */
@Repository("promotionGroupDao")
public class PromotionGroupDaoImpl extends IBatisBaseFawvwDao implements IPromotionGroupDao {
	
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
		return getSqlMapClientTemplate().queryForList("promotionGroup.initDate", paramsMap);
	}
	
	/**
	 * 加载促销分类支持分析图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List loadPromotionGroupDataChartAndTable(Map<String, Object> paramsMap) 
	{
		List list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("promotionGroup.loadPromotionGroupData", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 加载促销分类分析图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List loadPromotionGroupChartAndTableByVersion(Map<String, Object> paramsMap) 
	{
		List list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("promotionGroup.loadPromotionGroupDataByVersion", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 导出促销走势分析图形和表格(型号)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionPromotionInfoEntity> exportVersionPromotionChartAndTable(Map<String, Object> paramsMap) 
	{	
		return (List<VersionPromotionInfoEntity>)getSqlMapClientTemplate().queryForList("promotionGroup.exportVersionPromotionChartAndTable", paramsMap);
	}
	
	/**
	 * 导出促销走势分析图形和表格(车型)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionPromotionInfoEntity> exportPromotionData(Map<String, Object> paramsMap) 
	{	
		return (List<VersionPromotionInfoEntity>)getSqlMapClientTemplate().queryForList("promotionGroup.exportPromotionData", paramsMap);
	}
	
	/**
	 * 导出促销走势分析图形和表格(厂商/品牌/系别/级别)
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VersionPromotionInfoEntity> exportPromotionData2(Map<String, Object> paramsMap) 
	{	
		return (List<VersionPromotionInfoEntity>)getSqlMapClientTemplate().queryForList("promotionGroup.exportPromotionData2", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("promotionGroup.checkPopBoxData", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("promotionGroup.checkManfPopBoxData", paramsMap);
	}
}
