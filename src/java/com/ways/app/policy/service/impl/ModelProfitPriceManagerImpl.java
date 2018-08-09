package com.ways.app.policy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.policy.dao.ModelProfitPriceDao;
import com.ways.app.policy.service.ModelProfitPriceManager;

/***********************************************************************************************
 * <br />车型利润分析Service接口实现层
 * <br />Class name: ModelProfitPriceManagerImpl.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Aug 11, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
@Service("ModelProfitPriceManager")
public class ModelProfitPriceManagerImpl implements ModelProfitPriceManager {

	@Autowired
	private ModelProfitPriceDao modelProfitPriceDao;
	@Override
	public void initDate(HttpServletRequest request,Map<String, Object> paramsMap) {
		List<Map<String, String>> list = (List<Map<String, String>>) modelProfitPriceDao.initDate(paramsMap);
		if(null != list && 0 != list.size())
		{
			String beginDate = list.get(0).get("BEGINDATE");
			String endDate = list.get(0).get("ENDDATE");
			String defaultBeginDate = endDate.substring(0,4) + "-01";
			
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("defaultBeginDate", defaultBeginDate);
		}
	}
	
	@Override
	public String getDateUnit(HttpServletRequest request,Map<String, Object> paramsMap) {
		String json = "{}";
		String timeType = (String)paramsMap.get("timeType");
		
		paramsMap.put("isEndDate", "0");
		Map<String, Object> serialMap = new HashMap<String, Object>();
		
		try {
			
			List<Map<String, String>> slist = (List<Map<String, String>>) modelProfitPriceDao.getDateUnit(paramsMap);
			if(null != slist && 0 != slist.size()) serialMap.put("sDate", slist);
			
			if("2".equals(timeType)){
				paramsMap.put("isEndDate", "1");
				List<Map<String, String>> elist = (List<Map<String, String>>) modelProfitPriceDao.getDateUnit(paramsMap);
				if(null != elist && 0 != elist.size()) serialMap.put("eDate", elist);
			}
			
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(serialMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}

}
