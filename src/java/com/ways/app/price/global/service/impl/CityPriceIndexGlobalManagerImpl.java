package com.ways.app.price.global.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.price.global.dao.ICityPriceIndexGlobalDao;
import com.ways.app.price.global.service.ICityPriceIndexGlobalManager;
import com.ways.app.price.model.SubModel;

/**
 * 初始化控件
 * @author yinlue
 *
 */
@Service("cityPriceIndexGlobalManager")
public class CityPriceIndexGlobalManagerImpl implements ICityPriceIndexGlobalManager {

	@Autowired
	private ICityPriceIndexGlobalDao cityPriceIndexGlobalDao;

	/**
	 * 获取初始化城市控件值
	 */
	@Override
	public void getCityModal(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("areaList", cityPriceIndexGlobalDao.getCityModal(paramsMap));
		} catch (Exception e) {
			
		}
	}

	/**
	 * 获取初始化子车型弹出框控件值
	 */
	@Override
	public void getSubmodelModal(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			handleOneMonthParams(paramsMap);
			//返回细分市场页
	    	if("2".equals(paramsMap.get("subModelShowType"))) request.setAttribute("segmentList", cityPriceIndexGlobalDao.getSubmodelBySegment(paramsMap));
	    	//返回品牌页
	    	else if("3".equals(paramsMap.get("subModelShowType"))) request.setAttribute("brandLetterList", cityPriceIndexGlobalDao.getSubmodelByBrand(paramsMap));
	    	//返回厂商页
	    	else if("4".equals(paramsMap.get("subModelShowType"))) request.setAttribute("manfLetterList", cityPriceIndexGlobalDao.getSubmodelByManf(paramsMap));
	    	//本品页竟品页
	    	else request.setAttribute("bpSubModelList", cityPriceIndexGlobalDao.getSubmodelByBp(paramsMap));
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过返利成交价数据有效日期过滤查询生产商信息
	 */
	@Override
	public void getManfModal(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			handleOneMonthParams(paramsMap);
			request.setAttribute("manfLetterList", cityPriceIndexGlobalDao.getManfModal(paramsMap));
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
			request.setAttribute("versionList", cityPriceIndexGlobalDao.getVersionModalByCommon(paramsMap));
		} catch (Exception e) {
			
		}
	}

	/**
	 * 校验成交价有效车型和型号
	 */
	@Override
	public String checkPopBoxTpData(HttpServletRequest paramHttpServletRequest,Map<String, Object> paramsMap) 
	{
		String json = "";
		List<SubModel> list = cityPriceIndexGlobalDao.checkPopBoxTpData(paramsMap);
	    if (list != null && list.size() != 0 ) 
	    {
	    	json = AppFrameworkUtil.structureConfigParamsGroupJSONData(list);
	    }
	    return json;
	}

	/**
	 * 获取细分市场以及所属子细分市场
	 */
	@Override
	public void getSegmentAndChildren(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			handleOneMonthParams(paramsMap);
			request.setAttribute("segmentList", cityPriceIndexGlobalDao.getSegmentAndChildren(paramsMap));
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
			request.setAttribute("manfLetterList", cityPriceIndexGlobalDao.getManf(paramsMap));
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
			request.setAttribute("brandLetterList", cityPriceIndexGlobalDao.getBrand(paramsMap));
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
			request.setAttribute("bodyTypeList", cityPriceIndexGlobalDao.getBodyType(paramsMap));
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
	 * 获取车身形式
	 */
	@Override
	public void getAutoCustomGroup(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("autoCustomGroupList", cityPriceIndexGlobalDao.getAutoCustomGroup(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
}
