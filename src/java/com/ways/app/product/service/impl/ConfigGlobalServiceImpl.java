package com.ways.app.product.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.product.dao.ConfigGlobalDao;
import com.ways.app.product.model.ConfigInfoEntity;
import com.ways.app.product.service.ConfigGlobalService;

/**
 * 配置公共Service实现类
 * @author yinlue
 *
 */
@Service("ConfigGlobalService")
public class ConfigGlobalServiceImpl implements ConfigGlobalService {

	@Autowired
	private ConfigGlobalDao dao;

	/**
	 * 获取配置大类
	 */
	@Override
	public void getConfigClassify(HttpServletRequest request,Map<String, String> paramsMap) 
	{
		request.setAttribute("configClassifyList", dao.getConfigClassify(paramsMap));		
	}

	/**
	 * 获取配置信息集合
	 */
	@Override
	public String getConfigInfoList(HttpServletRequest request,Map<String, String> paramsMap) 
	{
		String json = "";
		List<ConfigInfoEntity> list = dao.getConfigInfoList(paramsMap);
		if(null != list && 0 != list.size())
		{
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(list);
		}
		return json;
	}

	/**
	 *  获取配置值
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getConfigValue(HttpServletRequest request,Map<String, String> paramsMap) 
	{
		String json = "";
		List<Map<String, String>> list = (List<Map<String, String>>) dao.getConfigValue(paramsMap);
		if(null != list && 0 != list.size())
		{
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(list);
		}
		return json;
	}

	/**
	 * 获取车身形式
	 */
	@Override
	public void getSubModelBodyType(HttpServletRequest request,Map<String, String> paramsMap) 
	{
		try {
			request.setAttribute("subModelBodyType", dao.getSubModelBodyType(paramsMap));
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 获取初始化子车型弹出框控件值
	 */
	@Override
	public void getProductSubmodelModal(HttpServletRequest request,Map<String, String> paramsMap) 
	{
		try {
			//返回细分市场页
	    	if("2".equals(paramsMap.get("subModelShowType"))) request.setAttribute("segmentList", dao.getProductSubmodelBySegment(paramsMap));
	    	//返回品牌页
	    	else if("3".equals(paramsMap.get("subModelShowType"))) request.setAttribute("brandLetterList", dao.getProductSubmodelByBrand(paramsMap));
	    	//返回厂商页
	    	else if("4".equals(paramsMap.get("subModelShowType"))) request.setAttribute("manfLetterList", dao.getProductSubmodelByManf(paramsMap));
	    	//本品页竟品页
	    	else request.setAttribute("bpSubModelList", dao.getProductSubmodelByBp(paramsMap));
	    	
		} catch (Exception e) {
			
		}
	}
	
}
