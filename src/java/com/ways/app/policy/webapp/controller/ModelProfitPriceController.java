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
import com.ways.app.moduleLog.service.IModuleLogManager;
import com.ways.app.policy.model.PolicyModule;
import com.ways.app.policy.service.ModelProfitPriceManager;
import com.ways.framework.base.BaseController;

/***********************************************************************************************
 * <br />车型利润分析Controller
 * <br />Class name: ModelProfitPriceController.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Aug 11, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
@Controller
public class ModelProfitPriceController extends BaseController {
	
	protected final Log log = LogFactory.getLog(ModelProfitPriceController.class);
	
	@Autowired
	private ModelProfitPriceManager modelProfitPriceManager;
	@Autowired
	private IModuleLogManager logManager;
	
	/**
	 * 进入主页方法
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/policy/modelProfitPriceMain")
    public ModelAndView modelProfitPriceMain(HttpServletRequest request) throws Exception
    {
        Model model = new ExtendedModelMap();
        Map<String, Object> map = getPageParams(request);
        modelProfitPriceManager.initDate(request, map);
        request.setAttribute("menuId", "009");//设置模块ID，用来填充菜单栏选中样式
        request.setAttribute("moduleName", PolicyModule.CHEXINLIRUNFENXI.getModuleCode());//设置模块ID，用来填充菜单栏选中样式
        return new ModelAndView("/policy/modelProfitPrice/modelProfitPriceMain",model.asMap());
    }
    
    /**
	 * 获取周,月,季详细
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/policy/getDateUnit")
    public void getDateUnit(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	Map<String, Object> paramsMap = getPageParams(request);
    	String json = modelProfitPriceManager.getDateUnit(request, paramsMap);
    	logManager.addModuleLog(request, paramsMap);
    	try {
			print(response,json);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 输出JSON
     * @param response
     * @param result
     * @throws IOException
     */
    public void print(HttpServletResponse response, String result) throws IOException
	{
	    response.setCharacterEncoding("UTF-8");
	    response.setContentType("text/json;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    out.print(result);
	    out.flush();
	    out.close();
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
		map.put("frequency", request.getParameter("frequency"));//频次
		map.put("timeType",request.getParameter("timeType"));//车型ID
		map.put("modelIds",request.getParameter("modelIds"));//车型ID
		
		//添加记录日志属性
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", PolicyModule.CHEXINLIRUNFENXI.getModuleName());//模块名称
		map.put("browser", request.getParameter("browser"));//浏览器
		map.put("ipAddress", AppFrameworkUtil.getRemoteHost(request));//IP地址
		map.put("queryCondition", request.getParameter("queryCondition"));//查询条件
		map.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		return map;
	}
}
