package com.ways.app.pricesale.webapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.pricesale.service.IPriceSaleGlobalManager;
import com.ways.framework.base.BaseController;

/***********************************************************************************************
 * <br />价格销量公共Controller
 * <br />Class name: PriceSaleGlobalController.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Apr 16, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
@Controller
public class PriceSaleGlobalController extends BaseController{
	
	@Autowired
	private IPriceSaleGlobalManager priceSaleGlobalManager;
	
    /**
     * 获取细分市场以及所属子细分市场
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("pricesale/global/getSegmentAndChildren")
    public ModelAndView getSegmentAndChildren(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	priceSaleGlobalManager.getSegmentAndChildren(request,getPriceSaleGlobalParams(request));
    	return new ModelAndView("/global/segmentModal",model.asMap());
    }
    
    /**
     * 按首字母排序获取厂商
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/pricesale/global/getManf")
    public ModelAndView getManf(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	priceSaleGlobalManager.getManf(request,getPriceSaleGlobalParams(request));
    	return new ModelAndView("/global/manfModal",model.asMap());
    }
    
    /**
     * 按首字母排序获取品牌
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("pricesale/global/getBrand")
    public ModelAndView getBrand(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	priceSaleGlobalManager.getBrand(request,getPriceSaleGlobalParams(request));
    	return new ModelAndView("/global/brandModal",model.asMap());
    }
    
    /**
     * 获取车身形式
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("pricesale/global/getBodyType")
    public ModelAndView getBodyType(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	priceSaleGlobalManager.getBodyType(request,getPriceSaleGlobalParams(request));
    	return new ModelAndView("/global/bodyTypeModal",model.asMap());
    }
    
    /**
     * 获取车系
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("pricesale/global/getOrig")
    public ModelAndView getOrig(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	priceSaleGlobalManager.getOrig(request,getPriceSaleGlobalParams(request));
    	return new ModelAndView("/global/origModal",model.asMap());
    }
    
    /**
     * 获取初始化子车型控件值
     * @return
     * @throws Exception
     */
    @RequestMapping("/pricesale/global/getPriceSaleSubmodelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	Object subModelShowType = map.get("subModelShowType");
    	priceSaleGlobalManager.getSubmodelModal(request,map);
    	if("2".equals(subModelShowType)){
    		//返回细分市场页
    		return new ModelAndView("/global/subModelModaBySegmentl",new ExtendedModelMap().asMap());
    	}else if("3".equals(subModelShowType)){
    		//返回品牌页
    		return new ModelAndView("/global/subModelModaByBrandtl",new ExtendedModelMap().asMap());
    	}else if("4".equals(subModelShowType)){
    		//返回厂商页
    		return new ModelAndView("/global/subModelModaByManftl",new ExtendedModelMap().asMap());
    	}else{
    		return new ModelAndView("/global/subModelModal",new ExtendedModelMap().asMap());
    	}
    	
    	
        
    }
    
    /**
     * 获取价格段销量分析弹出框公共参数
     * @param request
     * @return
     */
    public Map<String, Object> getPriceSaleGlobalParams(HttpServletRequest request)
    {
    	Map<String, Object> map = new HashMap<String, Object>();
    	//String moduleCode = request.getParameter("analysisContentType");
    	map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
		map.put("endDate", request.getParameter("endDate"));//结束时间
		//map.put("moduleCode", moduleCode);
		request.setAttribute("inputType","1");//1：复选，2：单选;默认为1
		/**
		 * 如果是价格段销量分析模块,添加参数价格类型用来区分查询生产商,品牌等弹出框数据的查询过滤
		 */
		/*if(moduleCode.equals(PriceSaleModule.JIAGEDUANXIAOLIANGFENGXI.getModuleCode())){
			map.put("priceType", request.getParameter("priceType"));
		}*/
    	return map;
    }
    
    /**
     * 获取页面参数
     * @param request
     * @return
     */
    public Map<String, Object> getPageParams(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
		map.put("endDate", request.getParameter("endDate"));//结束时间
		map.put("priceType", request.getParameter("priceType"));//价格类型
		map.put("modelIds",request.getParameter("modelIds"));//车型ID
		map.put("subModelShowType",request.getParameter("subModelShowType"));//子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
		map.put("bodyTypeId",request.getParameter("hatchbackId"));//价格降幅获取车型弹出框所对应的车身形式
		
		String analysisContentType = request.getParameter("analysisContentType");//模块名称
		map.put("analysisContentType", analysisContentType);
		
		String timeType = request.getParameter("timeType");//时间类型1：时间点;2：时间段默认为2
		if(AppFrameworkUtil.isEmpty(timeType)) timeType = "2";
		map.put("timeType", timeType);
		
		String inputType = request.getParameter("inputType");//1：复选，2：单选;默认为1
    	if(inputType == null) inputType ="1";  
    	request.setAttribute("inputType",inputType);
    	
		return map;
	}
    
	/**
	 * 输出JSON流
	 * @param response
	 * @param result
	 * @throws IOException
	 */
	public void print(HttpServletResponse response, String result)throws IOException 
	{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
		out.close();
	}
}
