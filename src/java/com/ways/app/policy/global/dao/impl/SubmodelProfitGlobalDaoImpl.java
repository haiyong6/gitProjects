package com.ways.app.policy.global.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.ways.app.policy.global.dao.SubmodelProfitGlobalDao;
import com.ways.app.price.model.Area;
import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.ObjectGroup;
import com.ways.app.price.model.Segment;
import com.ways.app.price.model.SubModel;
import com.ways.framework.base.IBatisBaseFawvwDao;

/**
 * 初始化控件
 * @author yinlue
 *
 */
@Repository("SubmodelProfitGlobalImpl")
public class SubmodelProfitGlobalDaoImpl extends IBatisBaseFawvwDao implements SubmodelProfitGlobalDao {
	
	protected final Log log = LogFactory.getLog(SubmodelProfitGlobalDaoImpl.class);
	/**
	 * 获取初始化城市控件值
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Area> getCityModal(Map<String, Object> paramsMap)
	{
		return getSqlMapClientTemplate().queryForList("submodelProfitGlobal.getCityModal",paramsMap);
	}

	/**
	 * 获取子车型通过本品ID查找其竟品车型
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BPSubModel> getSubmodelByBp(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfitGlobal.getSubmodelByBp",paramsMap);
	}

	/**
	 * 获取子车型通过细分市场
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSubmodelBySegment(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfitGlobal.getSubmodelBySegment",paramsMap);
	}

	/**
	 * 获取子车型通过品牌
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByBrand(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfitGlobal.getSubmodelByBrand",paramsMap);
	}

	/**
	 * 获取子车型通过厂商
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByManf(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfitGlobal.getSubmodelByManf",paramsMap);
	}
	
	/**
	 * 通过返利成交价数据有效日期过滤查询生产商信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getManfModal(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfitGlobal.getManfModal", paramsMap);
	}

	/**
	 * 获取子车型下型号数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SubModel> getVersionModalByCommon(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfitGlobal.getVersionModalByCommon",paramsMap);
	}

	/**
	 *  校验成交价有效车型和型号
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SubModel> checkPopBoxTpData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfitGlobal.checkPopBoxTpData", paramsMap);
	}

	/**
	 * 获取细分市场以及所属子细分市场
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSegmentAndChildren(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfitGlobal.getSegmentAndChildren", paramsMap);
	}

	/**
	 * 按首字母获取厂商
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getManf(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfitGlobal.getManf", paramsMap);
	}

	/**
	 * 按首字母获取品牌
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getBrand(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfitGlobal.getBrand", paramsMap);
	}

	/**
	 * 按首车身形式
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BodyType> getBodyType(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfitGlobal.getBodyType", paramsMap);
	}
	
	/**
	 * 获取一汽大众常用型号组
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectGroup> getAutoCustomGroup(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("submodelProfitGlobal.getAutoCustomGroup", paramsMap);
	}
	
}
