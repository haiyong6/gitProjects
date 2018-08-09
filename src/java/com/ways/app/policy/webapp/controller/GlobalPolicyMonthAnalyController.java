package com.ways.app.policy.webapp.controller;

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
import com.ways.app.policy.service.GlobalPolicyMonthAnalyManager;
import com.ways.framework.base.BaseController;

/**
 * 促销查询-查车型controller
 * @author yuml
 *
 */
@Controller
public class GlobalPolicyMonthAnalyController extends BaseController{
	
	protected final Log log = LogFactory.getLog(GlobalPolicyMonthAnalyController.class);
	
	@Autowired
	private GlobalPolicyMonthAnalyManager globalPolicyMonthAnalyManager;
	 
	
	   /**
     * 获取初始化子车型控件值
     * @return
     * @throws Exception
     */
    @RequestMapping("/policy/global/getSubmodelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	globalPolicyMonthAnalyManager.getSubmodelModal(request,map);
    	//返回细分市场页
    	if("2".equals(map.get("subModelShowType"))) return new ModelAndView("/policy/saleIncentiveQuery/subModelModaBySegmentl",new ExtendedModelMap().asMap());
    	//返回品牌页
    	else if("3".equals(map.get("subModelShowType"))) return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByBrandtl",new ExtendedModelMap().asMap());
    	//返回厂商页
    	else if("4".equals(map.get("subModelShowType"))) return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByManftl",new ExtendedModelMap().asMap());
    	
    	return new ModelAndView("/policy/saleIncentiveQuery/subModelModal",new ExtendedModelMap().asMap());
        
    }
    
    /**
     * 获取页面参数
     * @param request
     * @return
     */
    public Map<String, Object> getPageParams(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		String analysisDimensionType = request.getParameter("analysisDimensionType");//分析维度：1，车型对比；2，时间对比
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
		map.put("endDate", request.getParameter("endDate"));//结束时间
		map.put("modelIds",request.getParameter("modelIds"));//车型ID
		map.put("analysisDimensionType", analysisDimensionType);
		map.put("subModelShowType", request.getParameter("subModelShowType"));
		//根据分析维度类型，设置值
		if("1".equals(analysisDimensionType))
		{
			request.setAttribute("inputType","1");//1：复选，2：单选;默认为1
			map.put("timeType", "1");//时间点
		}
		else
		{
			request.setAttribute("inputType","2");//1：复选，2：单选;默认为1
			map.put("timeType", "2");//时间段
		}
		return map;
	}

}