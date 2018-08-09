package com.ways.app.salesQuery.global.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.policy.global.model.ObjectEntity;
import com.ways.app.price.model.SubModel;
import com.ways.app.salesQuery.global.dao.SalesAmountFawGlobalDao;
import com.ways.app.salesQuery.global.service.SalesAmountFawGlobalManager;

/**厂商销售支持实现类
 * 
 * ClassName: TerminalGlobalManagerImpl 
 * @author ruanrf
 * @date Jan 6, 2016
 */
@Service("salesAmountFawGlobalManager")
public class SalesAmountFawGlobalManagerImpl implements SalesAmountFawGlobalManager {
	@Autowired
	private SalesAmountFawGlobalDao salesAmountFawGlobalDao;

	@Override
	public void getSegmentAndChildren(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("segmentList", salesAmountFawGlobalDao.getSegmentAndChildren(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void getManf(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("manfLetterList", salesAmountFawGlobalDao.getManf(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getBrand(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("brandLetterList", salesAmountFawGlobalDao.getBrand(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getBodyType(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("bodyTypeList", salesAmountFawGlobalDao.getBodyType(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getOrig(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("origList", salesAmountFawGlobalDao.getOrig(paramsMap));
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
	    		request.setAttribute("segmentList", salesAmountFawGlobalDao.getSubmodelBySegment(paramsMap));
	    	}else if("3".equals(subModelShowType)){
	    		//返回品牌页
		    	request.setAttribute("brandLetterList", salesAmountFawGlobalDao.getSubmodelByBrand(paramsMap));
	    	}else if("4".equals(subModelShowType)){
	    		//返回厂商页
		    	request.setAttribute("manfLetterList", salesAmountFawGlobalDao.getSubmodelByManf(paramsMap));
	    	}else{
	    		//本品页竟品页
		    	request.setAttribute("bpSubModelList", salesAmountFawGlobalDao.getSubmodelByBp(paramsMap));
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getManfModal(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("manfLetterList", salesAmountFawGlobalDao.getManfModal(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getAutoCustomGroup(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("autoCustomGroupList", salesAmountFawGlobalDao.getAutoCustomGroup(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getVersionModalByCommon(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			//保存子车型下型号数据
			request.setAttribute("versionList", salesAmountFawGlobalDao.getVersionModalByCommon(paramsMap));
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
			slist = salesAmountFawGlobalDao.checkPopBoxData(paramsMap);
		}else{
			olist = salesAmountFawGlobalDao.checkObjPopBoxData(paramsMap);
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
