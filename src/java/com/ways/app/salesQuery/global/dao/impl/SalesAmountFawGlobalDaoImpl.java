package com.ways.app.salesQuery.global.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.policy.global.model.ObjectEntity;
import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.ObjectGroup;
import com.ways.app.price.model.Segment;
import com.ways.app.price.model.SubModel;
import com.ways.app.salesQuery.global.dao.SalesAmountFawGlobalDao;
import com.ways.framework.base.IBatisBaseFawvwDao;
/**厂商销售支持Dao实现类
 * ClassName: TerminalGlobalDaoImpl 
 * @Description: TODO
 * @author ruanrf
 * @date Jan 6, 2016
 */
@Repository("salesAmountFawGlobalDao")
public class SalesAmountFawGlobalDaoImpl extends IBatisBaseFawvwDao implements SalesAmountFawGlobalDao{
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Segment> getSegmentAndChildren(Map<String, Object> paramsMap) {
		List<Segment> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.getSegmentAndChildren", paramsMap);
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
			list = getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.getManf", paramsMap);
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
			list = getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.getBrand",paramsMap);
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
			list = getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.getBodyType", paramsMap);
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
			list = getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.getOrig", paramsMap);
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
			list = getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.getSubmodelByBp", paramsMap);
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
			list = getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.getSubmodelBySegment", paramsMap);
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
			list = getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.getSubmodelByBrand", paramsMap);
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
			list = getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.getSubmodelByManf", paramsMap);
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
			list = getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.getManfModal", paramsMap);
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
			list = getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.getAutoCustomGroup", paramsMap);
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
			list = getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.getVersionModalByCommon", paramsMap);
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
			return getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.checkPopBoxData", paramsMap);
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
			list = getSqlMapClientTemplate().queryForList("SalesAmountFawGlobal.checkObjPopBoxData", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
