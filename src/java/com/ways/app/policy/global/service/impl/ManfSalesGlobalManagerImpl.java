package com.ways.app.policy.global.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.policy.global.dao.ManfSalesGlobalDao;
import com.ways.app.policy.global.model.ObjectEntity;
import com.ways.app.policy.global.service.ManfSalesGlobalManager;
import com.ways.app.price.model.SubModel;

/**厂商销售支持实现类
 * 
 * ClassName: TerminalGlobalManagerImpl 
 * @author ruanrf
 * @date Jan 6, 2016
 */
@Service("manfSalesGlobalManager")
public class ManfSalesGlobalManagerImpl implements ManfSalesGlobalManager {
	@Autowired
	private ManfSalesGlobalDao manfSalesGlobalDao;

	@Override
	public void getSegmentAndChildren(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("segmentList", manfSalesGlobalDao.getSegmentAndChildren(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void getManf(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("manfLetterList", manfSalesGlobalDao.getManf(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getBrand(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("brandLetterList", manfSalesGlobalDao.getBrand(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getBodyType(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("bodyTypeList", manfSalesGlobalDao.getBodyType(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getOrig(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("origList", manfSalesGlobalDao.getOrig(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getSubmodelModal(HttpServletRequest request,Map<String, Object> paramsMap) 
	{	
		Object subModelShowType = paramsMap.get("subModelShowType");
		try {
	    	if("2".equals(subModelShowType)){
				//返回细分市场页
	    		request.setAttribute("segmentList", manfSalesGlobalDao.getSubmodelBySegment(paramsMap));
	    	}else if("3".equals(subModelShowType)){
	    		//返回品牌页
		    	request.setAttribute("brandLetterList", manfSalesGlobalDao.getSubmodelByBrand(paramsMap));
	    	}else if("4".equals(subModelShowType)){
	    		//返回厂商页
		    	request.setAttribute("manfLetterList", manfSalesGlobalDao.getSubmodelByManf(paramsMap));
	    	}else{
	    		//本品页竟品页
		    	request.setAttribute("bpSubModelList", manfSalesGlobalDao.getSubmodelByBp(paramsMap));
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getManfModal(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("manfLetterList", manfSalesGlobalDao.getManfModal(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getAutoCustomGroup(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("autoCustomGroupList", manfSalesGlobalDao.getAutoCustomGroup(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getVersionModalByCommon(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			//保存子车型下型号数据
			request.setAttribute("versionList", manfSalesGlobalDao.getVersionModalByCommon(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String checkPopBoxData(HttpServletRequest request,Map<String, Object> paramsMap) {
		String json = "";
		List<SubModel> slist = null;
		List<ObjectEntity> olist = null;
		int objectType =Integer.parseInt(paramsMap.get("objectType").toString());
		if(objectType < 2){
			slist = manfSalesGlobalDao.checkPopBoxData(paramsMap);
		}else{
			olist = manfSalesGlobalDao.checkObjPopBoxData(paramsMap);
		}
		
	    if (slist != null && slist.size() != 0 ) 
	    {
	    	json = AppFrameworkUtil.structureConfigParamsGroupJSONData(slist);
	    }
	    if (olist != null && olist.size() != 0 ) 
	    {
	    	json = AppFrameworkUtil.structureConfigParamsGroupJSONData(olist);
	    }
	    return json;
	}
}
