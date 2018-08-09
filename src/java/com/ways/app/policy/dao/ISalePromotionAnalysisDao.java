package com.ways.app.policy.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.policy.model.SalePromotionAnalysisEntity;
import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.ObjectGroup;
import com.ways.app.price.model.Segment;
import com.ways.app.price.model.SubModel;

/**
 * 销量促销分析Dao接口类
 * @author songguobiao
 *
 */
public interface ISalePromotionAnalysisDao {
	
	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap); 
	
	/**
	 * 获取子车型下型号数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<SubModel> getVersionModalByCommon(Map<String, Object> paramsMap);
	
	/**
	 * 获取一汽大众常用型号组
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<ObjectGroup> getAutoCustomGroup(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过本品ID查找其竟品车型
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<BPSubModel> getSubmodelByBp(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过细分市场
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Segment> getSubmodelBySegment(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过品牌
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getSubmodelByBrand(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过厂商
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getSubmodelByManf(Map<String, Object> paramsMap);
	
	/**
	 * 获取厂商
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getManf(Map<String, Object> paramsMap);
	
	/**
	 * 获取品牌
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getBrand(Map<String, Object> paramsMap);
	
	/**
	 * 获取系别
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<BodyType> getOrig(Map<String, Object> paramsMap);
	
	/**
	 * 获取车身形式
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<BodyType> getBodyType(Map<String, Object> paramsMap);
	
	/**
	 * 获取细分市场以及所属子细分市场
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Segment> getSegmentAndChildren(Map<String, Object> paramsMap);
	
	/**
	 * 加载销量促销数据(型号维度)
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<SalePromotionAnalysisEntity> loadSalePromotionAnalysisByVersion(Map<String, Object> paramsMap);
	
	/**
	 * 加载销量促销数据(其他维度-车型,厂商品牌,品牌,系别,级别)
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<SalePromotionAnalysisEntity> loadSalePromotionAnalysisByOther(Map<String, Object> paramsMap);
	
	/**
	 * 导出(其他维度-车型,厂商品牌,品牌,系别,级别)
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<SalePromotionAnalysisEntity> exportExcelByOther(Map<String, Object> paramsMap);
	
	/**
	 * 导出(型号维度)
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<SalePromotionAnalysisEntity> exportExcelByVersion(Map<String, Object> paramsMap);
}
