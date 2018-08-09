package com.ways.app.salesQuery.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.ObjectGroup;
import com.ways.app.price.model.Segment;
import com.ways.app.salesQuery.dao.ModelSalesAmountFawDao;
import com.ways.app.salesQuery.model.ModelSalesAmountFawEntity;
import com.ways.framework.base.IBatisBaseFawvwDao;

@Repository("modelSalesAmountFawDao")
public class ModelSalesAmountFawDaoImpl extends IBatisBaseFawvwDao implements ModelSalesAmountFawDao{
	
	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	@Override
	public List<?> initDate(Map<String, Object> paramsMap)
	{
        return getSqlMapClientTemplate().queryForList("modelSalesAmountFaw.initDate", paramsMap);
	}
	
	/**
	 * 获取销售激励查询数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ModelSalesAmountFawEntity> getModelSalesAmountFawData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("modelSalesAmountFaw.getModelSalesAmountFawData", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("modelSalesAmountFaw.getSubmodelByBp", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("modelSalesAmountFaw.getSubmodelBySegment", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("modelSalesAmountFaw.getSubmodelByBrand", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("modelSalesAmountFaw.getSubmodelByManf", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("modelSalesAmountFaw.getBodyType", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("modelSalesAmountFaw.getAutoCustomGroup", paramsMap);
	}

	/**
	 * 获取总计数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ModelSalesAmountFawEntity> getTotalData(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("modelSalesAmountFaw.getTotalData", paramsMap);
	}


}
