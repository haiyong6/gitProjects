package com.ways.app.pricesale.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.Segment;
import com.ways.app.pricesale.dao.IScaleBySubModelSaleDao;
import com.ways.app.pricesale.model.ScaleBySubModelSale;
import com.ways.framework.base.IBatisBaseFawvwDao;

/**
 * 车型销售比例分析Dao实现类
 * @author songguobiao
 *
 */
@Repository("ScaleBySubModelSaleDaoImpl")
public class ScaleBySubModelSaleDaoImpl extends IBatisBaseFawvwDao implements IScaleBySubModelSaleDao {
	
	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("scaleBySubModelSale.initDate", paramsMap);
	}
	
	/**
	 * 获取子车型通过本品ID查找其竟品车型
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BPSubModel> getSubmodelByBp(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("scaleBySubModelSale.getSubmodelByBp", paramsMap);
	}

	/**
	 * 获取子车型通过细分市场
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSubmodelBySegment(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("scaleBySubModelSale.getSubmodelBySegment", paramsMap);
	}

	/**
	 * 获取子车型通过品牌
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByBrand(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("scaleBySubModelSale.getSubmodelByBrand", paramsMap);
	}

	/**
	 * 获取子车型通过厂商
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByManf(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("scaleBySubModelSale.getSubmodelByManf", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("scaleBySubModelSale.getBodyType", paramsMap);
	}
	
	/**
	 * 获取分析数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ScaleBySubModelSale> getAnalysisData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("scaleBySubModelSale.getAnalysisData", paramsMap);
	}
}
