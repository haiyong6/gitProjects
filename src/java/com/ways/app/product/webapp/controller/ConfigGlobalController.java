package com.ways.app.product.webapp.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.product.service.ConfigGlobalService;
import com.ways.framework.base.BaseController;

/**
 * 配置公共controller
 * @author yinlue
 *
 */
@Controller
public class ConfigGlobalController extends BaseController{
	
	protected final Log log = LogFactory.getLog(ConfigGlobalController.class);
	@Autowired
	private ConfigGlobalService service;
	 
	
	/**
     * 获取配置大类
     * @return
     * @throws Exception
     */
    @RequestMapping("/product/global/getConfigClassify")
    public ModelAndView getConfigClassify(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	service.getConfigClassify(request, getPageParams(request));
        return new ModelAndView("/product/configClassify",new ExtendedModelMap().asMap());
    }
    
	/**
     * 获取小类大类
     * @return
     * @throws Exception
     */
    @RequestMapping("/product/global/getConfigInfoList")
    public void getConfigInfoList(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	String json = service.getConfigInfoList(request, getPageParams(request));
    	try {
			print(response, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	/**
     * 获取非B类型配置值
     * @return
     * @throws Exception
     */
    @RequestMapping("/product/global/getConfigValue")
    public void getConfigValue(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	String json = service.getConfigValue(request, getPageParams(request));
    	try {
			print(response, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * 获取初始化子车型控件值
     * @return
     * @throws Exception
     */
    @RequestMapping("/product/global/getProductSubmodelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	Map<String, String> map = getPageParams(request);
    	service.getProductSubmodelModal(request,map);
    	//返回细分市场页
    	if("2".equals(map.get("subModelShowType"))) return new ModelAndView("/global/subModelModaBySegmentl",new ExtendedModelMap().asMap());
    	//返回品牌页
    	else if("3".equals(map.get("subModelShowType"))) return new ModelAndView("/global/subModelModaByBrandtl",new ExtendedModelMap().asMap());
    	//返回厂商页
    	else if("4".equals(map.get("subModelShowType"))) return new ModelAndView("/global/subModelModaByManftl",new ExtendedModelMap().asMap());
    	
    	return new ModelAndView("/global/subModelModal",new ExtendedModelMap().asMap());
        
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
    public Map<String, String> getPageParams(HttpServletRequest request)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("classifyId", request.getParameter("classifyId")); //配置大类ID
		map.put("configId", request.getParameter("configId"));//配置小类ID
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("subModelShowType", request.getParameter("subModelShowType"));//车型弹出框展示类型
		
		map.put("beginPrice", request.getParameter("beginPrice")); //开始价格
		map.put("endPrice", request.getParameter("endPrice"));	//结束价格
		map.put("subModelBodyType", request.getParameter("subModelBodyType")); //车身形式
		
		//如果没有选择价格和车身形式，不参与车型校验
		if(!AppFrameworkUtil.isEmpty(map.get("beginPrice")) || !AppFrameworkUtil.isEmpty(map.get("endPrice")) )
		{
			map.put("checkSubModel", "1");//是否校验车型1:校验
			map.put("priceCheck", "1");//校验类型，价格校验
		}
		if(!AppFrameworkUtil.isEmpty(map.get("subModelBodyType")))
		{
			map.put("checkSubModel", "1");//是否校验车型1:校验
			map.put("bodyTypeCheck", "1");//校验类型，车型形式校验
		}
		request.setAttribute("inputType","1");//1：复选
		
		return map;
	}
   
}