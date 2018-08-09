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
import com.ways.app.policy.global.service.IPromotionGlobalManager;
import com.ways.framework.base.BaseController;

/**
 * 初始化控件
 * @author huangwenmei
 *
 */
@Controller
public class PromotionGlobalController extends BaseController{
	
	protected final Log log = LogFactory.getLog(PromotionGlobalController.class);
	@Autowired
	private IPromotionGlobalManager promotionGlobalManager;
	
    
    /**
     * 获取初始化子车型控件值
     * @return
     * @throws Exception
     */
    @RequestMapping("/promotion/global/getSubmodelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	promotionGlobalManager.getSubmodelModal(request,map);
    	//返回细分市场页
    	if("2".equals(map.get("subModelShowType"))) return new ModelAndView("/policy/global/subModelModaBySegmentl",new ExtendedModelMap().asMap());
    	//返回品牌页
    	else if("3".equals(map.get("subModelShowType"))) return new ModelAndView("/policy/global/subModelModaByBrandtl",new ExtendedModelMap().asMap());
    	//返回厂商页
    	else if("4".equals(map.get("subModelShowType"))) return new ModelAndView("/policy/global/subModelModaByManftl",new ExtendedModelMap().asMap());
    	
    	return new ModelAndView("/policy/global/subModelModal",new ExtendedModelMap().asMap());
        
    }
    
    /**
     * 查询生产商信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/promotion/global/getManfModal")
    public ModelAndView getManfModal(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	promotionGlobalManager.getManfModal(request,map);
    	return new ModelAndView("/policy/global/manfModal",new ExtendedModelMap().asMap());
    }
    
    /**
     * 获取子车型下型号数据
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/promotion/global/getVersionModalByCommon")
    public ModelAndView getVersionModalByCommon(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	promotionGlobalManager.getVersionModalByCommon(request,getPageParams(request));
    	return new ModelAndView("/policy/global/versionModal",model.asMap());
    }
    
    /**
     * 获取细分市场以及所属子细分市场
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/promotion/global/getSegmentAndChildren")
    public ModelAndView getSegmentAndChildren(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	promotionGlobalManager.getSegmentAndChildren(request,getPageParams(request));
    	return new ModelAndView("/policy/global/segmentModal",model.asMap());
    }
    
    /**
     * 按首字母排序获取厂商
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/promotion/global/getManf")
    public ModelAndView getManf(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	promotionGlobalManager.getManf(request,getPageParams(request));
    	return new ModelAndView("/policy/global/manfModal",model.asMap());
    }
    
    /**
     * 按首字母排序获取品牌
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/promotion/global/getBrand")
    public ModelAndView getBrand(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	promotionGlobalManager.getBrand(request,getPageParams(request));
    	return new ModelAndView("/policy/global/brandModal",model.asMap());
    }
    
    /**
     * 获取车身形式
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/promotion/global/getBodyType")
    public ModelAndView getBodyType(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	promotionGlobalManager.getBodyType(request,getPageParams(request));
    	return new ModelAndView("/policy/global/bodyTypeModal",model.asMap());
    }
    
	/**
     * 获取初始化常用对象
     * @return
     * @throws Exception
     */
    @RequestMapping("/promotion/global/getAutoCustomGroup")
    public ModelAndView getAutoCustomGroup(HttpServletRequest request,HttpServletResponse response) throws Exception
    {	
    	Map<String, Object> paraMap = getPageParams(request);
    	promotionGlobalManager.getAutoCustomGroup(request,paraMap);
        return new ModelAndView("/policy/global/autoVersionModal",new ExtendedModelMap().asMap());
    }
    
    /**
     * 获取车系
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/promotion/global/getOrig")
    public ModelAndView getOrig(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	promotionGlobalManager.getOrig(request,getPageParams(request));
    	return new ModelAndView("/policy/global/origModal",model.asMap());
    }
    
 
    
    /**
     * 获取弹出框公共参数
     * @param request
     * @return
     */
    public Map<String, Object> getPageParams(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
		map.put("endDate", request.getParameter("endDate"));//结束时间
		map.put("modelIds",request.getParameter("modelIds"));//车型ID
		map.put("subModelShowType",request.getParameter("subModelShowType"));//子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
		map.put("bodyTypeId",request.getParameter("hatchbackId"));//车身形式
		String segmentType = request.getParameter("segmentType");//细分市场类别
		if(null != segmentType) {
			segmentType = segmentType.replace(",", "','");
		}
		map.put("segmentType", segmentType);
		String analysisContentType = request.getParameter("analysisContentType");//分析数据指标类型1:促销；2：内促:3：外促
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