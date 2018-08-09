package com.ways.app.salesQuery.global.webapp.controller;

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
import com.ways.app.policy.global.webapp.controller.ManfSalesGlobalController;
import com.ways.app.salesQuery.global.service.SalesAmountFawGlobalManager;
import com.ways.framework.base.BaseController;

@Controller
public class SalesAmountFawGlobalController extends BaseController{
	
	protected final Log log = LogFactory.getLog(ManfSalesGlobalController.class);
	@Autowired
	private SalesAmountFawGlobalManager salesAmountFawGlobalManager;
	
    /**
     * 获取细分市场以及所属子细分市场
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/salesAmountFaw/global/getSegmentAndChildren")
    public ModelAndView getSegmentAndChildren(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	salesAmountFawGlobalManager.getSegmentAndChildren(request, getPageParams(request));
    	return new ModelAndView("/policy/global/segmentModal", model.asMap());
    }
    
    /**
     * 按首字母排序获取厂商
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/salesAmountFaw/global/getManf")
    public ModelAndView getManf(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	salesAmountFawGlobalManager.getManf(request, getPageParams(request));
    	return new ModelAndView("/global/manfModal", model.asMap());
    }
    
    /**
     * 获取初始化常用对象
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/salesAmountFaw/global/getAutoCustomGroup")
    public ModelAndView getAutoCustomGroup(HttpServletRequest request, HttpServletResponse response) throws Exception
    {	
    	Map<String, Object> paraMap = getPageParams(request);
    	salesAmountFawGlobalManager.getAutoCustomGroup(request, paraMap);
        return new ModelAndView("/global/autoVersionModal", new ExtendedModelMap().asMap());
    }
    
    /**
     * 获取子车型下型号数据
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/salesAmountFaw/global/getVersionModalByCommon")
    public ModelAndView getVersionModalByCommon(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	salesAmountFawGlobalManager.getVersionModalByCommon(request, getPageParams(request));
    	return new ModelAndView("/global/versionModal", model.asMap());
    }
    
    /**
     * 按首字母排序获取厂商品牌
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/salesAmountFaw/global/getManfModal")
    public ModelAndView getManfModal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	salesAmountFawGlobalManager.getManfModal(request, map);
    	return new ModelAndView("/global/manfModal", new ExtendedModelMap().asMap());
    }
    
    /**
     * 按首字母排序获取品牌
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/salesAmountFaw/global/getBrand")
    public ModelAndView getBrand(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	salesAmountFawGlobalManager.getBrand(request,getPageParams(request));
    	return new ModelAndView("/global/brandModal", model.asMap());
    }
    
    /**
     * 获取车身形式
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/salesAmountFaw/global/getBodyType")
    public ModelAndView getBodyType(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	salesAmountFawGlobalManager.getBodyType(request,getPageParams(request));
    	return new ModelAndView("/policy/global/bodyTypeModal", model.asMap());
    }
    
    /**
     * 获取车系
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/salesAmountFaw/global/getOrig")
    public ModelAndView getOrig(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	salesAmountFawGlobalManager.getOrig(request, getPageParams(request));
    	return new ModelAndView("/global/origModal", model.asMap());
    }
    
    /**
     * 获取初始化子车型控件值
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/salesAmountFaw/global/getSubmodelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	Object subModelShowType = map.get("subModelShowType");
    	salesAmountFawGlobalManager.getSubmodelModal(request, map);
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
     * 校验有效对象
     * 
     * @param request
     * @param response
     */
    @RequestMapping({"/salesAmountFaw/checkPopBoxData"})
    public void checkPopBoxTpData(HttpServletRequest request, HttpServletResponse response)
    {
	      Map<String,Object> paramsMap = getPageParams(request);
	      String json = salesAmountFawGlobalManager.checkPopBoxData(request, paramsMap);
	      try {
	        print(response, json);
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
    }
    
    /**
     * 获取页面参数
     * 
     * @param request
     * @return
     */
    public Map<String, Object> getPageParams(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		String beginDate = request.getParameter("beginDate");//开始时间
		String endDate = request.getParameter("endDate");//结束时间
		String frequencyType = request.getParameter("frequencyType");//日期频次
		String seasonType=request.getParameter("seasonType");//季度频次
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("frequencyType", frequencyType);//日期频次
		map.put("seasonType", seasonType);//季度频次
		map.put("modelIds",request.getParameter("mids"));//车型ID
		map.put("vids", request.getParameter("vids"));
		map.put("manfIds", request.getParameter("manfIds"));
		map.put("brandIds", request.getParameter("brandIds"));
		map.put("origIds", request.getParameter("origIds"));
		map.put("segmentIds", request.getParameter("segmentIds"));
		String segmentType = request.getParameter("segmentType");//细分市场类别
		if(null != segmentType) {
			segmentType = segmentType.replace(",", "','");
		}
		map.put("segmentType", segmentType);
		map.put("bodyTypeIds", request.getParameter("hatchbackId"));
		map.put("objectType", request.getParameter("objectType"));
		
		String inputType = request.getParameter("inputType");//1：复选，2：单选;默认为1
    	if(inputType == null) inputType ="1";  
    	request.setAttribute("inputType",inputType);
		map.put("subModelShowType", request.getParameter("subModelShowType"));//子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
		return map;
	}
    
	/**
	 * 输出JSON流
	 * 
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

