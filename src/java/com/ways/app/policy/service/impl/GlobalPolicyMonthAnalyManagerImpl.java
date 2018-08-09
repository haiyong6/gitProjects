package com.ways.app.policy.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ways.app.policy.dao.GlobalPolicyMonthAnalyDao;
import com.ways.app.policy.service.GlobalPolicyMonthAnalyManager;


/**
 * 促销查询Service层接口实现类
 * @author yuml
 *
 */
@Service("globalPolicyMonthAnalyManager")
public class GlobalPolicyMonthAnalyManagerImpl implements GlobalPolicyMonthAnalyManager {

	@Autowired
	private GlobalPolicyMonthAnalyDao globalPolicyMonthAnalyDao;

	
	/**
	 * 获取初始化子车型弹出框控件值
	 * @param request
	 * @param paramsMap
	 */
	public void getSubmodelModal(HttpServletRequest request,Map<String, Object> paramsMap){
		
		try {
			//返回细分市场页
	    	if("2".equals(paramsMap.get("subModelShowType"))) request.setAttribute("segmentList", globalPolicyMonthAnalyDao.getSubmodelBySegment(paramsMap));
	    	//返回品牌页
	    	else if("3".equals(paramsMap.get("subModelShowType"))) request.setAttribute("brandLetterList", globalPolicyMonthAnalyDao.getSubmodelByBrand(paramsMap));
	    	//返回厂商页
	    	else if("4".equals(paramsMap.get("subModelShowType"))) request.setAttribute("manfLetterList", globalPolicyMonthAnalyDao.getSubmodelByManf(paramsMap));
	    	//本品页竟品页
	    	else request.setAttribute("bpSubModelList", globalPolicyMonthAnalyDao.getSubmodelByBp(paramsMap));
	    	
		} catch (Exception e) {
			
		}
	}

}
