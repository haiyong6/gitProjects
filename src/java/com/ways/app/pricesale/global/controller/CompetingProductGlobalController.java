package com.ways.app.pricesale.global.controller;

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
import com.ways.app.pricesale.global.service.CompetingProductGlobalManager;
import com.ways.framework.base.BaseController;

/**
 * 初始化控件
 * @author yinlue
 *
 */
@Controller
public class CompetingProductGlobalController extends BaseController{
	
	protected final Log log = LogFactory.getLog(CompetingProductGlobalController.class);
	@Autowired
	private CompetingProductGlobalManager competingProductGlobalManager;
	 
    
    /**
     * 通过返利成交价数据有效日期过滤查询生产商信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/pricesale/competingProductGlobal/getManfModal")
    public ModelAndView getManfModal(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	competingProductGlobalManager.getManfModal(request,map);
    	return new ModelAndView("/global/manfModal",new ExtendedModelMap().asMap());
    }
    
    /**
     * 获取子车型下型号数据
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/pricesale/competingProductGlobal/getVersionModalByCommon")
    public ModelAndView getVersionModalByCommon(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	competingProductGlobalManager.getVersionModalByCommon(request,getPageParams(request));
    	return new ModelAndView("/global/versionModal",model.asMap());
    }
    
    /**
     * 获取细分市场以及所属子细分市场
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/pricesale/competingProductGlobal/getSegmentAndChildren")
    public ModelAndView getSegmentAndChildren(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	competingProductGlobalManager.getSegmentAndChildren(request,getPageParams(request));
    	return new ModelAndView("/global/segmentModal",model.asMap());
    }
    
    /**
     * 获取系别
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/pricesale/competingProductGlobal/getOrig")
    public ModelAndView getOrig(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	competingProductGlobalManager.getOrig(request,getPageParams(request));
    	return new ModelAndView("/policy/global/origModal",model.asMap());
    }
    
    /**
     * 按首字母排序获取厂商
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/pricesale/competingProductGlobal/getManf")
    public ModelAndView getManf(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	competingProductGlobalManager.getManf(request,getPageParams(request));
    	return new ModelAndView("/global/manfModal",model.asMap());
    }
    
    /**
     * 按首字母排序获取品牌
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/pricesale/competingProductGlobal/getBrand")
    public ModelAndView getBrand(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	competingProductGlobalManager.getBrand(request,getPageParams(request));
    	return new ModelAndView("/global/brandModal",model.asMap());
    }
    
    /**
     * 获取车身形式
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/pricesale/competingProductGlobal/getBodyType")
    public ModelAndView getBodyType(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	competingProductGlobalManager.getBodyType(request,getGlobalParams(request));
    	return new ModelAndView("/global/bodyTypeModal",model.asMap());
    }
    
	/**
     * 获取初始化常用对象
     * @return
     * @throws Exception
     */
    @RequestMapping("/pricesale/competingProductGlobal/getAutoCustomGroup")
    public ModelAndView getAutoCustomGroup(HttpServletRequest request,HttpServletResponse response) throws Exception
    {	
    	Map<String, Object> paraMap = getPageParams(request);
    	competingProductGlobalManager.getAutoCustomGroup(request,paraMap);
        return new ModelAndView("/global/autoVersionModal",new ExtendedModelMap().asMap());
    }
    
    /**
     * 校验成交价下有效车型和型号
     * @param request
     * @param response
     */
    @RequestMapping({"/pricesale/checkPopBoxTpData"})
    public void checkPopBoxTpData(HttpServletRequest request, HttpServletResponse response)
    {
	      Map<String,Object> paramsMap = new HashMap<String,Object>();
	      paramsMap.put("beginDate", request.getParameter("beginDate"));
	      paramsMap.put("vid", request.getParameter("vid"));
	      //如果型号为空，则添加车型
	      if (AppFrameworkUtil.isEmpty((String)paramsMap.get("vid"))) paramsMap.put("mid", request.getParameter("mid"));
	
	      String json = competingProductGlobalManager.checkPopBoxTpData(request, paramsMap);
	      try {
	        print(response, json);
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
    }
    
    /**
     * 获取价格降幅弹出框公共参数
     * @param request
     * @return
     */
    public Map<String, Object> getGlobalParams(HttpServletRequest request)
    {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
		map.put("endDate", request.getParameter("endDate"));//结束时间
		String segmentTypes = request.getParameter("segmentType");
		String[] segmentType = segmentTypes.split(",");
		String str ="";
		for(int i = 0;i < segmentType.length;i++){
			str += "'" + segmentType[i] +"'" + ",";
		}
		System.out.println(str.substring(0, str.length()));
		map.put("segmentType", str.substring(0, str.length()-1));////细分市场类别
		request.setAttribute("inputType","1");//1：复选，2：单选;默认为1
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
		String segmentTypes = request.getParameter("segmentType");
		String[] segmentType = segmentTypes.split(",");
		String str ="";
		for(int i = 0;i < segmentType.length;i++){
			str += "'" + segmentType[i] +"'" + ",";
		}
		//System.out.println(str.substring(0, str.length()));
		map.put("segmentType", str.substring(0, str.length()-1));////细分市场类别
		String analysisDimensionType = request.getParameter("analysisDimensionType");
		map.put("analysisDimensionType", analysisDimensionType);//分析维度（1.对象对比 2.城市对比 3.时间对比）
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
		if("3".equals(analysisDimensionType)){
			map.put("endDate", request.getParameter("endDate"));//结束时间
		} else{
			map.put("endDate", request.getParameter("beginDate"));//结束时间
		}
		map.put("modelIds",request.getParameter("modelIds"));//车型ID
		map.put("subModelShowType",request.getParameter("subModelShowType"));//子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
		//map.put("bodyTypeId",request.getParameter("hatchbackId"));//价格降幅获取车型弹出框所对应的车身形式
		map.put("objectBodyTypeIds",request.getParameter("objectBodyTypeIds"));//对象对比下的车身形式获取
		String analysisContentType = request.getParameter("analysisContentType");//分析数据指标类型1:利润；2：折扣:3：成交价
		if(AppFrameworkUtil.isEmpty(analysisContentType)) analysisContentType = "1";
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