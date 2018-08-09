package com.ways.app.salesQuery.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.ObjectGroup;
import com.ways.app.price.model.Segment;
import com.ways.app.salesQuery.dao.SalesAmountFawDao;
import com.ways.app.salesQuery.model.SalesAmountFawEntity;
import com.ways.framework.base.IBatisBaseFawvwDao;

@Repository("salesAmountFawDao")
public class SalesAmountFawDaoImpl extends IBatisBaseFawvwDao implements SalesAmountFawDao{
	
	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	@Override
	public List<?> initDate(Map<String, Object> paramsMap)
	{
        return getSqlMapClientTemplate().queryForList("salesAmountFaw.initDate", paramsMap);
	}
	
	/**
	 * 获取年月销量数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SalesAmountFawEntity> getSalesAmountFawGroupByMonthObjectData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("salesAmountFaw.getSalesAmountFawGroupByMonthObjectData", paramsMap);
	}
	
	/**
	 * 获取车型数据通过本竞品
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BPSubModel> getSubmodelByBp(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("salesAmountFaw.getSubmodelByBp", paramsMap);
	}

	/**
	 * 获取车型数据通过细分市场
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSubmodelBySegment(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("salesAmountFaw.getSubmodelBySegment", paramsMap);
	}

	/**
	 * 获取车型数据通过品牌
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByBrand(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("salesAmountFaw.getSubmodelByBrand", paramsMap);
	}

	/**
	 * 获取车型数据通过厂商
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByManf(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("salesAmountFaw.getSubmodelByManf", paramsMap);
	}

	/**
	 * 获取车身形式
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BodyType> getBodyType(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("salesAmountFaw.getBodyType", paramsMap);
	}
	
	/**
	 * 获取一汽大众常用型号组
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectGroup> getAutoCustomGroup(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("salesAmountFaw.getAutoCustomGroup", paramsMap);
	}

	/**
	 * 获取对象详细信息以及对象总计销量数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SalesAmountFawEntity> getSalesAmountFawGroupByObjectData(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("salesAmountFaw.getSalesAmountFawGroupByObjectData", paramsMap);
	}

	/**
	 * 获取以年月分组总计销量数据
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SalesAmountFawEntity> getSalesAmountFawGroupByMonthData(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("salesAmountFaw.getSalesAmountFawGroupByMonthData", paramsMap);
	}

	/**
	 * 年月占比数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SalesAmountFawEntity> getSalesAmountFawGroupByMonthObjectPercentData(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("salesAmountFaw.getSalesAmountFawGroupByMonthObjectPercentData", paramsMap);
	}

	/**
	 * 销量总计数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SalesAmountFawEntity> getSalesAmountFawTotalData(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("salesAmountFaw.getSalesAmountFawTotalData", paramsMap);
	}


}
