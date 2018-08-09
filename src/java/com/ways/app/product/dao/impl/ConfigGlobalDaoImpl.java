package com.ways.app.product.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.Segment;
import com.ways.app.product.dao.ConfigGlobalDao;
import com.ways.app.product.model.ConfigClassifyEntity;
import com.ways.app.product.model.ConfigInfoEntity;
import com.ways.app.product.model.GlobalTextEntity;
import com.ways.framework.base.IBatisBaseDao;

/**
 * 配置公共DAO实现类
 * @author yinlue
 *
 */
@Repository("ConfigGlobalDaoImpl")
public class ConfigGlobalDaoImpl extends IBatisBaseDao implements ConfigGlobalDao {

	/**
	 * 获取配置大类
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigClassifyEntity> getConfigClassify(Map<String, String> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("configGlobal.getConfigClassify",paramsMap);
	}

	
	/**
	 * 获取配置信息集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigInfoEntity> getConfigInfoList(Map<String, String> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("configGlobal.getConfigInfoList",paramsMap);
	}


	/**
	 *  获取配置值
	 */
	@Override
	public List<?> getConfigValue(Map<String, String> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("configGlobal.getConfigValue",paramsMap);
	}


	/**
	 * 获取车身形式
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GlobalTextEntity> getSubModelBodyType(Map<String, String> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("configGlobal.getSubModelBodyType",paramsMap);
	}

	/**
	 * 获取子车型通过本品ID查找其竟品车型
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BPSubModel> getProductSubmodelByBp(Map<String, String> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("configGlobal.getProductSubmodelByBp",paramsMap);
	}


	/**
	 * 获取子车型通过细分市场
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getProductSubmodelBySegment(Map<String, String> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("configGlobal.getProductSubmodelBySegment",paramsMap);
	}

	/**
	 * 获取子车型通过品牌
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getProductSubmodelByBrand(Map<String, String> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("configGlobal.getProductSubmodelByBrand",paramsMap);
	}

	/**
	 * 获取子车型通过厂商
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getProductSubmodelByManf(Map<String, String> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("configGlobal.getProductSubmodelByManf",paramsMap);
	}

}
