package com.ways.app.pricesale.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.Segment;
import com.ways.app.pricesale.model.ScaleBySubModelSale;

/**
 * 车型销售比例分析Dao接口类
 * @author songguobiao
 *
 */
public interface IScaleBySubModelSaleDao {
	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 */
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap);
	
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
	 * 获取车身形式
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<BodyType> getBodyType(Map<String, Object> paramsMap);
	
	/**
	 * 获取分析数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<ScaleBySubModelSale> getAnalysisData(Map<String, Object> paramsMap);
}
