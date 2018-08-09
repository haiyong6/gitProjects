package com.ways.app.price.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.dao.ICityTpSelectDao;
import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.ObjectGroup;
import com.ways.app.price.model.Segment;
import com.ways.framework.base.IBatisBaseFawvwDao;

/**
 * 成交价查询DAO层实现类
 * 
 * @author songguobiao
 *
 */
@Repository("CityTpSelectDaoImpl")
public class CityTpSelectDaoImpl extends IBatisBaseFawvwDao implements ICityTpSelectDao {
	
	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("cityTpSelect.initDate", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("cityTpSelect.getSubmodelByBp", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("cityTpSelect.getSubmodelBySegment", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("cityTpSelect.getSubmodelByBrand", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("cityTpSelect.getSubmodelByManf", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("cityTpSelect.getBodyType", paramsMap);
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
		return getSqlMapClientTemplate().queryForList("cityTpSelect.getAutoCustomGroup", paramsMap);
	}
	
	/**
	 * 获取城市的名称和排序
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getCityNameAndSort(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("cityTpSelect.getCityNameAndSort", paramsMap);
	}
	
	/**
	 * 获取型号成交价数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getVersionTpData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("cityTpSelect.getVersionTpData", paramsMap);
	}
	
	/**
	 * 获取型号全国加权平均成交价数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getVersionTpDataByCountryAvg(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("cityTpSelect.getVersionTpDataByCountryAvg", paramsMap);
	}
}
