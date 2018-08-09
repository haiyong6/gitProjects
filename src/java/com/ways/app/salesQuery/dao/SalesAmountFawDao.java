package com.ways.app.salesQuery.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.ObjectGroup;
import com.ways.app.price.model.Segment;
import com.ways.app.salesQuery.model.SalesAmountFawEntity;

public interface SalesAmountFawDao {
	/**
	 * 初始化时间
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public List<?> initDate(Map<String, Object> paramsMap);
	
	/**
	 * 获取年月销量数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<SalesAmountFawEntity> getSalesAmountFawGroupByMonthObjectData(Map<String, Object> paramsMap);
	
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
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<BodyType> getBodyType(Map<String, Object> paramsMap);
	
	/**
	 * 获取一汽大众常用型号组
	 * 
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<ObjectGroup> getAutoCustomGroup(Map<String, Object> paramsMap);

	/**
	 * 获取对象详细信息以及对象总计销量数据
	 * @param paramsMap
	 * @return
	 */
	public List<SalesAmountFawEntity> getSalesAmountFawGroupByObjectData(Map<String, Object> paramsMap);
	
	/**
	 * 获取以年月分组总计销量数据
	 * @param paramsMap
	 * @return
	 */
	public List<SalesAmountFawEntity> getSalesAmountFawGroupByMonthData(Map<String, Object> paramsMap);

	/**
	 * 年月占比数据
	 * @param paramsMap
	 * @return
	 */
	public List<SalesAmountFawEntity> getSalesAmountFawGroupByMonthObjectPercentData(Map<String, Object> paramsMap);

	/**
	 * 销量总计数据
	 * @param paramsMap
	 * @return
	 */
	public List<SalesAmountFawEntity> getSalesAmountFawTotalData(Map<String, Object> paramsMap);


}
