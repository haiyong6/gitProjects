package com.ways.app.policy.webapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.policy.service.GlobalModelProfitPriceManager;
import com.ways.framework.base.BaseController;

/***********************************************************************************************
 * <br />车型利润分析 查车型
 * <br />Class name: GlobaModelProfitPriceController.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Aug 19, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
@Controller
public class GlobalModelProfitPriceController extends BaseController{
	
	protected final Log log = LogFactory.getLog(GlobalModelProfitPriceController.class);
	@Autowired
	private GlobalModelProfitPriceManager globalModelProfitPriceManager;
	/**
     * 获取初始化子车型控件值
     * @return
     * @throws Exception
     */
    @RequestMapping("/policy/global/modelProfitPrice/getSubmodelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	globalModelProfitPriceManager.getSubmodelModal(request,map);
    	//返回细分市场页
    	if("2".equals(map.get("subModelShowType"))) return new ModelAndView("/global/subModelModaBySegmentl",new ExtendedModelMap().asMap());
    	//返回品牌页
    	else if("3".equals(map.get("subModelShowType"))) return new ModelAndView("/global/subModelModaByBrandtl",new ExtendedModelMap().asMap());
    	//返回厂商页
    	else if("4".equals(map.get("subModelShowType"))) return new ModelAndView("/global/subModelModaByManftl",new ExtendedModelMap().asMap());
    	
    	return new ModelAndView("/global/subModelModal",new ExtendedModelMap().asMap());
        
    }
    
    /**
     * 获取子车型下型号数据
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/policy/global/modelProfitPrice/getVersionModalByCommon")
    public ModelAndView getVersionModalByCommon(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	globalModelProfitPriceManager.getVersionModalByCommon(request,getPageParams(request));
    	return new ModelAndView("/global/versionModal",model.asMap());
    }
    
    /**
     * 校验成交价下有效车型和型号
     * @param request
     * @param response
     */
//    @RequestMapping({"/checkPopBoxTpData"})
//    public void checkPopBoxTpData(HttpServletRequest request, HttpServletResponse response)
//    {
//	      Map<String,Object> paramsMap = new HashMap<String,Object>();
//	      paramsMap.put("beginDate", request.getParameter("beginDate"));
//	      paramsMap.put("vid", request.getParameter("vid"));
//	      //如果型号为空，则添加车型
//	      if (AppFrameworkUtil.isEmpty((String)paramsMap.get("vid"))) paramsMap.put("mid", request.getParameter("mid"));
//	
//	      String json = null;//globaModelProfitPriceManager.checkPopBoxTpData(request, paramsMap);
//	      try {
//	        print(response, json);
//	      } catch (IOException e) {
//	        e.printStackTrace();
//	      }
//    }
	
	/**
     * 获取页面参数
     * @param request
     * @return
     */
    public Map<String, Object> getPageParams(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("userId",AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("beginDate",request.getParameter("beginDate"));//开始时间
		map.put("endDate",request.getParameter("endDate"));//结束时间
		map.put("modelIds",request.getParameter("modelIds"));//车型ID
		map.put("moduleName",request.getParameter("moduleName"));
		map.put("subModelShowType",request.getParameter("subModelShowType"));//子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
		
		String timeType = request.getParameter("timeType");//时间类型1：时间点;2：时间段默认为2
		if(AppFrameworkUtil.isEmpty(timeType)) timeType = "2";
		map.put("timeType", timeType);
		
		String frequency = request.getParameter("frequency");//频次
		if(AppFrameworkUtil.isEmpty(frequency)) frequency = "3";
		map.put("frequency", frequency);
		
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
