package com.ways.app.policy.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.framework.utils.DateUtil;
import com.ways.app.policy.dao.GlobalModelProfitPriceDao;
import com.ways.app.policy.model.PolicyModule;
import com.ways.app.policy.service.GlobalModelProfitPriceManager;

/***********************************************************************************************
 * <br />车型利润分析 service接口实现层
 * <br />Class name: GlobaModelProfitPriceManagerImpl.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Aug 19, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
@Service("globalModelProfitPriceManagerImpl")
public class GlobalModelProfitPriceManagerImpl implements GlobalModelProfitPriceManager {
	@Autowired
	private GlobalModelProfitPriceDao globalModelProfitPriceDao;
	@Override
	public void getSubmodelModal(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			//特殊模块时间获取
			getDateParams(paramsMap);
			//返回细分市场页
	    	if("2".equals(paramsMap.get("subModelShowType"))) request.setAttribute("segmentList", globalModelProfitPriceDao.getSubmodelBySegment(paramsMap));
	    	//返回品牌页
	    	else if("3".equals(paramsMap.get("subModelShowType"))) request.setAttribute("brandLetterList", globalModelProfitPriceDao.getSubmodelByBrand(paramsMap));
	    	//返回厂商页
	    	else if("4".equals(paramsMap.get("subModelShowType"))) request.setAttribute("manfLetterList", globalModelProfitPriceDao.getSubmodelByManf(paramsMap));
	    	//本品页竟品页
	    	else request.setAttribute("bpSubModelList", globalModelProfitPriceDao.getSubmodelByBp(paramsMap));
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* 函数功能说明 车型利润分析时时间获取
	* ruanrf  Aug 19, 2015
	* 修改者名字 修改日期
	* 修改内容
	* @param paramsMap    
	* void   
	*/
	private void getDateParams(Map<String, Object> paramsMap){
		String moduleName = (String)paramsMap.get("moduleName");
		if(PolicyModule.CHEXINLIRUNFENXI.getModuleCode().equals(moduleName)){
			String r_beginDate = "";
			String r_endDate = "";
			String beginDate = (String)paramsMap.get("beginDate");
			String endDate = (String)paramsMap.get("endDate");
			String frequency = (String)paramsMap.get("frequency");
			String timeType = (String)paramsMap.get("timeType");
			
			if("1".equals(timeType) && !AppFrameworkUtil.isEmpty(beginDate)){
				if("1".equals(frequency)){
					r_beginDate = beginDate.substring(0, 7);
				}else if("2".equals(frequency)){
					r_beginDate = beginDate.substring(0, 7);
				}else if("3".equals(frequency)){
					r_beginDate = beginDate;
				}else if("4".equals(frequency)){
					r_beginDate = DateUtil.getCurrentQuarterStartDate(beginDate).substring(0, 7);
					r_endDate = beginDate.substring(0, 7);
					
					//频次为季度时替换起始日期
					paramsMap.put("beginDate", DateUtil.getCurrentQuarterStartDate(beginDate));
					paramsMap.put("endDate", beginDate);
				}else if("5".equals(frequency)){
					r_beginDate = beginDate+"-01";
					r_endDate = beginDate+"-12";
					
					//频次为年度时替换日期
					paramsMap.put("beginDate", r_beginDate);
					paramsMap.put("endDate", r_endDate);
				}
			}else if("2".equals(timeType) && !AppFrameworkUtil.isEmpty(beginDate) && !AppFrameworkUtil.isEmpty(endDate)){
				if("1".equals(frequency)){
					r_beginDate = beginDate.substring(0, 7);
					r_endDate = endDate.substring(0, 7);
				}else if("2".equals(frequency)){
					r_beginDate = beginDate.substring(0, 7);
					r_endDate = endDate.substring(0, 7);
				}else if("3".equals(frequency)){
					r_beginDate = beginDate;
					r_endDate = endDate;
				}else if("4".equals(frequency)){
					r_beginDate = DateUtil.getCurrentQuarterStartDate(beginDate).substring(0, 7);
					r_endDate = endDate.substring(0, 7);
					
					//替换季度时候起始日期
					paramsMap.put("beginDate", DateUtil.getCurrentQuarterStartDate(beginDate));
					paramsMap.put("endDate", endDate);
				}else if("5".equals(frequency)){
					r_beginDate = beginDate+"-01";
					r_endDate = endDate+"-12";
					
					//频次为年度时替换日期
					paramsMap.put("beginDate", r_beginDate);
					paramsMap.put("endDate", r_endDate);
				}
			}
			paramsMap.put("r_beginDate", r_beginDate);
			paramsMap.put("r_endDate", r_endDate);
		}
	}
	
	/**
	 * 获取车型下的型号数据
	 */
	@Override
	public void getVersionModalByCommon(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			//获取时间
			getDateParams(paramsMap);
			//保存子车型下型号数据
			request.setAttribute("versionList", globalModelProfitPriceDao.getVersionModalByCommon(paramsMap));
		} catch (Exception e) {
			
		}
	}

}
