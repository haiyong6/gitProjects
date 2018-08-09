package com.ways.app.policy.global.service.impl;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.policy.global.dao.IPromotionGlobalDao;
import com.ways.app.policy.global.service.IPromotionGlobalManager;

/**
 * 初始化控件
 * @author huangwenmei
 *
 */
@Service("promotionGlobalManager")
public class PromotionGlobalManagerImpl implements IPromotionGlobalManager {

	@Autowired
	private IPromotionGlobalDao promotionGlobalDao;

	/**
	 * 获取初始化子车型弹出框控件值
	 */
	@Override
	public void getSubmodelModal(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			handleOneMonthParams(paramsMap);
			//返回细分市场页
	    	if("2".equals(paramsMap.get("subModelShowType"))) request.setAttribute("segmentList", promotionGlobalDao.getSubmodelBySegment(paramsMap));
	    	//返回品牌页
	    	else if("3".equals(paramsMap.get("subModelShowType"))) request.setAttribute("brandLetterList", promotionGlobalDao.getSubmodelByBrand(paramsMap));
	    	//返回厂商页
	    	else if("4".equals(paramsMap.get("subModelShowType"))) request.setAttribute("manfLetterList", promotionGlobalDao.getSubmodelByManf(paramsMap));
	    	//本品页竟品页
	    	else request.setAttribute("bpSubModelList", promotionGlobalDao.getSubmodelByBp(paramsMap));
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询生产商信息
	 */
	@Override
	public void getManfModal(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			handleOneMonthParams(paramsMap);
			request.setAttribute("manfLetterList", promotionGlobalDao.getManfModal(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取车型下的型号数据
	 */
	@Override
	public void getVersionModalByCommon(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			//保存子车型下型号数据
			request.setAttribute("versionList", promotionGlobalDao.getVersionModalByCommon(paramsMap));
		} catch (Exception e) {
			
		}
	}

	
	/**
	 * 获取细分市场以及所属子细分市场
	 */
	@Override
	public void getSegmentAndChildren(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			handleOneMonthParams(paramsMap);
			request.setAttribute("segmentList", promotionGlobalDao.getSegmentAndChildren(paramsMap));
		} catch (Exception e) {

		}
	}

	/**
	 * 按首字母获取厂商
	 */
	@Override
	public void getManf(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			handleOneMonthParams(paramsMap);
			request.setAttribute("manfLetterList", promotionGlobalDao.getManf(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 按首字母获取品牌
	 */
	@Override
	public void getBrand(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			handleOneMonthParams(paramsMap);
			request.setAttribute("brandLetterList", promotionGlobalDao.getBrand(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取车身形式
	 */
	@Override
	public void getBodyType(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("bodyTypeList", promotionGlobalDao.getBodyType(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理1月份特殊参数
	 * @param paramsMap
	 */
	public void handleOneMonthParams(Map<String, Object> paramsMap)
	{
		
		String beginDate = (String)paramsMap.get("beginDate");
		String endDate = (String)paramsMap.get("endDate");
		boolean f = beginDate.contains("-");
		
		if(!AppFrameworkUtil.isEmpty(beginDate)) beginDate = beginDate.replace("-", "");
		if(!AppFrameworkUtil.isEmpty(endDate)) endDate = endDate.replace("-", "");
		
		//如果开始月等于结束月，且是1月份时
		if(beginDate.equals(endDate) && "01".equals(beginDate.substring(4)))
		{
			int year = Integer.parseInt(beginDate.substring(0,4)) - 1;
			if(f) beginDate = year + "-12";
			else beginDate = year + "12";
			
			paramsMap.put("beginDate", beginDate);
		}
	}
	
	/**
	 * 获取常用组
	 */
	@Override
	public void getAutoCustomGroup(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("autoCustomGroupList", promotionGlobalDao.getAutoCustomGroup(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void getOrig(HttpServletRequest request,Map<String, Object> paramsMap) {
		try {
			request.setAttribute("origList", promotionGlobalDao.getOrig(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
}
