package com.ways.app.policy.global.webapp.controller;

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
import com.ways.app.policy.global.service.ManfSalesGlobalManager;
import com.ways.framework.base.BaseController;

@Controller
public class ManfSalesGlobalController extends BaseController{
	
	protected final Log log = LogFactory.getLog(ManfSalesGlobalController.class);
	@Autowired
	private ManfSalesGlobalManager manfSalesGlobalManager;
	
    /**
     * 获取细分市场以及所属子细分市场
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/manfSales/global/getSegmentAndChildren")
    public ModelAndView getSegmentAndChildren(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	manfSalesGlobalManager.getSegmentAndChildren(request, getPageParams(request));
    	return new ModelAndView("/policy/manfSales/segmentModal", model.asMap());
    }
    
    /**
     * 按首字母排序获取厂商
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/manfSales/global/getManf")
    public ModelAndView getManf(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	manfSalesGlobalManager.getManf(request, getPageParams(request));
    	return new ModelAndView("/global/manfModal", model.asMap());
    }
    
    /**
     * 获取初始化常用对象
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/manfSales/global/getAutoCustomGroup")
    public ModelAndView getAutoCustomGroup(HttpServletRequest request, HttpServletResponse response) throws Exception
    {	
    	Map<String, Object> paraMap = getPageParams(request);
    	manfSalesGlobalManager.getAutoCustomGroup(request, paraMap);
        return new ModelAndView("/global/autoVersionModal", new ExtendedModelMap().asMap());
    }
    
    /**
     * 获取子车型下型号数据
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/manfSales/global/getVersionModalByCommon")
    public ModelAndView getVersionModalByCommon(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	manfSalesGlobalManager.getVersionModalByCommon(request, getPageParams(request));
    	return new ModelAndView("/global/versionModal", model.asMap());
    }
    
    /**
     * 按首字母排序获取厂商品牌
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/manfSales/global/getManfModal")
    public ModelAndView getManfModal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	manfSalesGlobalManager.getManfModal(request, map);
    	return new ModelAndView("/global/manfModal", new ExtendedModelMap().asMap());
    }
    
    /**
     * 按首字母排序获取品牌
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/manfSales/global/getBrand")
    public ModelAndView getBrand(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	manfSalesGlobalManager.getBrand(request,getPageParams(request));
    	return new ModelAndView("/global/brandModal", model.asMap());
    }
    
    /**
     * 获取车身形式
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/manfSales/global/getBodyType")
    public ModelAndView getBodyType(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	manfSalesGlobalManager.getBodyType(request,getPageParams(request));
    	return new ModelAndView("/policy/global/bodyTypeModal", model.asMap());
    }
    
    /**
     * 获取车系
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/manfSales/global/getOrig")
    public ModelAndView getOrig(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	manfSalesGlobalManager.getOrig(request, getPageParams(request));
    	return new ModelAndView("/global/origModal", model.asMap());
    }
    
    /**
     * 获取初始化子车型控件值
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/manfSales/global/getSubmodelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	Object subModelShowType = map.get("subModelShowType");
    	manfSalesGlobalManager.getSubmodelModal(request, map);
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
    @RequestMapping({"/manfSales/checkPopBoxData"})
    public void checkPopBoxTpData(HttpServletRequest request, HttpServletResponse response)
    {
	      Map<String,Object> paramsMap = getPageParams(request);
	      String json = manfSalesGlobalManager.checkPopBoxData(request, paramsMap);
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
		//String endDate = request.getParameter("endDate");//结束时间
		String frequencyType = request.getParameter("frequencyType");//日期频次
		String seasonType=request.getParameter("seasonType");//季度频次
		map.put("beginDate", getManfSalesDate(beginDate,frequencyType,"begin",request, seasonType));
		map.put("endDate", getManfSalesDate(beginDate,frequencyType,"end",request,seasonType));
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
		map.put("bodyTypeIds", request.getParameter("bodyTypeIds"));
		map.put("objectType", request.getParameter("objectType"));
		
		String inputType = request.getParameter("inputType");//1：复选，2：单选;默认为1
    	if(inputType == null) inputType ="2";  
    	request.setAttribute("inputType","2");
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
	
	private static  String getManfSalesDate(String strDate,String ftype,String dtype,HttpServletRequest request,String seasonType){
		String date = null;
		if("1".equals(ftype)){//月
			date = strDate;
		}else if("2".equals(ftype)){//季度
			date = getQuarterMonth(strDate, dtype,seasonType);
		}else{
			if("begin".equals(dtype)){//年
				date = strDate.substring(0, 4)+"-01";
			}else{
				date = strDate.substring(0, 4)+"-12";
			}
		}
		return date;
	}

	private static String getQuarterMonth(String strDate, String dtype,String seasonType) {
		StringBuffer date = new StringBuffer();
		//Q1
		if("1".equals(seasonType)){
			date.append(strDate);
			if("begin".equals(dtype)){
				return date.append("-01").toString();
			}else{
				return date.append("-03").toString();
			}
		//Q2
		} else if("2".equals(seasonType)){
			date.append(strDate);
			if("begin".equals(dtype)){
				return date.append("-04").toString();
			}else{
				return date.append("-06").toString();
			}
		//Q3	
		} else if("3".equals(seasonType)){
			date.append(strDate);
			if("begin".equals(dtype)){
				return date.append("-07").toString();
			}else{
				return date.append("-09").toString();
			}
		//Q4	
		}else{
			date.append(strDate);
			if("begin".equals(dtype)){
				return date.append("-10").toString();
			}else{
				return date.append("-12").toString();
			}
		}
	}
}
