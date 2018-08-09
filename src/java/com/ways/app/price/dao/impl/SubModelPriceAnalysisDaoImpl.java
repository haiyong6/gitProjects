package com.ways.app.price.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.dao.ISubModelPriceAnalysisDao;
import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.Segment;
import com.ways.app.price.model.SubModelPriceAnalysis;
import com.ways.framework.base.IBatisBaseFawvwDao;

/**
 * 车型价格段查询DAO层实现类
 * 
 * @author songguobiao
 *
 */
@Repository("SubModelPriceAnalysisDaoImpl")
public class SubModelPriceAnalysisDaoImpl extends IBatisBaseFawvwDao implements ISubModelPriceAnalysisDao {
	
	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("subModelPriceAnalysis.initDate", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("subModelPriceAnalysis.getSubmodelByBp", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("subModelPriceAnalysis.getSubmodelBySegment", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("subModelPriceAnalysis.getSubmodelByBrand", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("subModelPriceAnalysis.getSubmodelByManf", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("subModelPriceAnalysis.getBodyType", paramsMap);
	}
	
	/**
	 * 获取分析数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SubModelPriceAnalysis> getAnalysisData(Map<String, Object> paramsMap)
	{
		return getSqlMapClientTemplate().queryForList("subModelPriceAnalysis.getAnalysisData", paramsMap);
	}
}
