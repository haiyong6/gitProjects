package com.ways.app.pricesale.global.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.ObjectGroup;
import com.ways.app.price.model.Orig;
import com.ways.app.price.model.Segment;
import com.ways.app.price.model.SubModel;
import com.ways.app.pricesale.global.dao.CompetingProductGlobalDao;
import com.ways.framework.base.IBatisBaseFawvwDao;

/**
 * 初始化控件
 * @author yinlue
 *
 */
@Repository("CompetingProductGlobalImpl")
public class CompetingProductGlobalDaoImpl extends IBatisBaseFawvwDao implements CompetingProductGlobalDao {
	
	protected final Log log = LogFactory.getLog(CompetingProductGlobalDaoImpl.class);
	
	/**
	 * 通过返利成交价数据有效日期过滤查询生产商信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getManfModal(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("competingProductGlobal.getManfModal", paramsMap);
	}

	/**
	 * 获取子车型下型号数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SubModel> getVersionModalByCommon(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("competingProductGlobal.getVersionModalByCommon",paramsMap);
	}

	/**
	 *  校验成交价有效车型和型号
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SubModel> checkPopBoxTpData(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("competingProductGlobal.checkPopBoxTpData", paramsMap);
	}

	/**
	 * 获取细分市场以及所属子细分市场
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSegmentAndChildren(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("competingProductGlobal.getSegmentAndChildren", paramsMap);
	}

	/**
	 * 按首字母获取厂商
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getManf(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("competingProductGlobal.getManf", paramsMap);
	}

	/**
	 * 按首字母获取品牌
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getBrand(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("competingProductGlobal.getBrand", paramsMap);
	}

	/**
	 * 按首车身形式
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BodyType> getBodyType(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("competingProductGlobal.getBodyType", paramsMap);
	}
	
	/**
	 * 获取一汽大众常用型号组
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectGroup> getAutoCustomGroup(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("competingProductGlobal.getAutoCustomGroup", paramsMap);
	}

	/**
	 * 获取系别
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Orig> getOrig(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("competingProductGlobal.getOrig", paramsMap);
	}
	
}
