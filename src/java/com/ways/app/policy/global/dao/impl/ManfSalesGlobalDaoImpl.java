package com.ways.app.policy.global.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.policy.global.dao.ManfSalesGlobalDao;
import com.ways.app.policy.global.model.ObjectEntity;
import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.ObjectGroup;
import com.ways.app.price.model.Segment;
import com.ways.app.price.model.SubModel;
import com.ways.framework.base.IBatisBaseFawvwDao;
/**厂商销售支持Dao实现类
 * ClassName: TerminalGlobalDaoImpl 
 * @Description: TODO
 * @author ruanrf
 * @date Jan 6, 2016
 */
@Repository("manfSalesGlobalDao")
public class ManfSalesGlobalDaoImpl extends IBatisBaseFawvwDao implements ManfSalesGlobalDao{
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSegmentAndChildren(Map<String, Object> paramsMap) {
		List<Segment> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSalesGlobal.getSegmentAndChildren", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getManf(Map<String, Object> paramsMap) {
		List<LetterObj> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSalesGlobal.getManf", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getBrand(Map<String, Object> paramsMap)
	{
		List<LetterObj> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSalesGlobal.getBrand",paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BodyType> getBodyType(Map<String, Object> paramsMap) {
		List<BodyType> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSalesGlobal.getBodyType", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BodyType> getOrig(Map<String, Object> paramsMap) {
		List<BodyType> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSalesGlobal.getOrig", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BPSubModel> getSubmodelByBp(Map<String, Object> paramsMap) 
	{
		List<BPSubModel> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSalesGlobal.getSubmodelByBp", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSubmodelBySegment(Map<String, Object> paramsMap) 
	{
		List<Segment> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSalesGlobal.getSubmodelBySegment", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByBrand(Map<String, Object> paramsMap) 
	{
		List<LetterObj> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSalesGlobal.getSubmodelByBrand", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getSubmodelByManf(Map<String, Object> paramsMap) 
	{
		List<LetterObj> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSalesGlobal.getSubmodelByManf", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LetterObj> getManfModal(Map<String, Object> paramsMap) 
	{
		List<LetterObj> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSalesGlobal.getManfModal", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectGroup> getAutoCustomGroup(Map<String, Object> paramsMap) {
		List<ObjectGroup> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSalesGlobal.getAutoCustomGroup", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubModel> getVersionModalByCommon(Map<String, Object> paramsMap) {
		List<SubModel> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSalesGlobal.getVersionModalByCommon", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubModel> checkPopBoxData(Map<String, Object> paramsMap) {
		List<SubModel> list = null;
		try {
			return getSqlMapClientTemplate().queryForList("ManfSalesGlobal.checkPopBoxData", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectEntity> checkObjPopBoxData(Map<String, Object> paramsMap) {
		List<ObjectEntity> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("ManfSalesGlobal.checkObjPopBoxData", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
