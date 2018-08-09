package com.ways.app.pricesale.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ways.app.pricesale.dao.IPriceSaleGlobalDao;
import com.ways.app.pricesale.service.IPriceSaleGlobalManager;

/*******************************************************************************
 * <br />
 * 价格销量公共接口实现类 <br />
 * Class name: IPriceSaleGlobalManagerImpl.java <br />
 * Copyright 2015 Ways Company in GuangZhou. All rights reserved. <br />
 * Author: ruanrf <br />
 * Apr 16, 2015 <br />
 * 
 * @version 3.0
 * 
 ******************************************************************************/
@Service("priceSaleGlobalManager")
public class IPriceSaleGlobalManagerImpl implements IPriceSaleGlobalManager {
	@Autowired
	private IPriceSaleGlobalDao priceSaleGlobalDao;

	@Override
	public void getSegmentAndChildren(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("segmentList", priceSaleGlobalDao.getSegmentAndChildren(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void getManf(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("manfLetterList", priceSaleGlobalDao.getManf(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getBrand(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("brandLetterList", priceSaleGlobalDao.getBrand(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getBodyType(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("bodyTypeList", priceSaleGlobalDao.getBodyType(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getOrig(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("origList", priceSaleGlobalDao.getOrig(paramsMap));
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
	    		request.setAttribute("segmentList", priceSaleGlobalDao.getSubmodelBySegment(paramsMap));
	    	}else if("3".equals(subModelShowType)){
	    		//返回品牌页
		    	request.setAttribute("brandLetterList", priceSaleGlobalDao.getSubmodelByBrand(paramsMap));
	    	}else if("4".equals(subModelShowType)){
	    		//返回厂商页
		    	request.setAttribute("manfLetterList", priceSaleGlobalDao.getSubmodelByManf(paramsMap));
	    	}else{
	    		//本品页竟品页
		    	request.setAttribute("bpSubModelList", priceSaleGlobalDao.getSubmodelByBp(paramsMap));
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
