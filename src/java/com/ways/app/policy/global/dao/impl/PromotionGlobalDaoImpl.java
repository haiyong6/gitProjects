package com.ways.app.policy.global.dao.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.ways.app.policy.global.dao.IPromotionGlobalDao;
import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.ObjectGroup;
import com.ways.app.price.model.Segment;
import com.ways.app.price.model.SubModel;
import com.ways.framework.base.IBatisBaseFawvwDao;

/**
 * 初始化控件
 * @author huangwenmei
 *
 */
@Repository("promotionGlobalDao")
public class PromotionGlobalDaoImpl extends IBatisBaseFawvwDao implements IPromotionGlobalDao {

	/**
	 * 获取子车型通过本品ID查找其竟品车型
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BPSubModel> getSubmodelByBp(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("promotionglobal.getSubmodelByBp",paramsMap);
	}

	/**
	 * 获取子车型通过细分市场
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSubmodelBySegment(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("promotionglobal.getSubmodelBySegment",paramsMap);
	}

	/**
	 * 获取子车型通过品牌
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByBrand(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("promotionglobal.getSubmodelByBrand",paramsMap);
	}

	/**
	 * 获取子车型通过厂商
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByManf(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("promotionglobal.getSubmodelByManf",paramsMap);
	}
	
	/**
	 * 查询生产商信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getManfModal(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("promotionglobal.getManfModal", paramsMap);
	}

	/**
	 * 获取子车型下型号数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SubModel> getVersionModalByCommon(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("promotionglobal.getVersionModalByCommon",paramsMap);
	}
	

	/**
	 * 获取细分市场以及所属子细分市场
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSegmentAndChildren(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("promotionglobal.getSegmentAndChildren", paramsMap);
	}

	/**
	 * 按首字母获取厂商
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getManf(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("promotionglobal.getManf", paramsMap);
	}

	/**
	 * 按首字母获取品牌
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getBrand(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("promotionglobal.getBrand", paramsMap);
	}

	/**
	 * 按首车身形式
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BodyType> getBodyType(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("promotionglobal.getBodyType", paramsMap);
	}
	
	/**
	 * 获取一汽大众常用型号组
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectGroup> getAutoCustomGroup(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("promotionglobal.getAutoCustomGroup", paramsMap);
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BodyType> getOrig(Map<String, Object> paramsMap) {
		return getSqlMapClientTemplate().queryForList("promotionglobal.getOrig", paramsMap);
	}
}