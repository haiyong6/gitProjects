package com.ways.app.price.webapp.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.framework.utils.DateUtil;
import com.ways.app.moduleLog.service.IModuleLogManager;
import com.ways.app.price.service.ISubModelPriceAnalysisManager;
import com.ways.framework.base.BaseController;

/**
 * 车型价格段分析controller
 * 
 * @author songguobiao
 * @date 20161107
 *
 */
@Controller
public class SubModelPriceAnalysisController extends BaseController {
	
	protected final Log log = LogFactory.getLog(SubModelPriceAnalysisController.class);
	@Autowired
	private ISubModelPriceAnalysisManager subModelPriceAnalysisManager;
	@Autowired
	private IModuleLogManager logManager;
	 
	/**
	 * 进入主页方法
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/price/subModelPriceAnalysisMain")
    public ModelAndView subModelPriceAnalysisMain(HttpServletRequest request) throws Exception
    {
        Model model = new ExtendedModelMap();
        Map<String, Object> map = getPageParams(request);
        subModelPriceAnalysisManager.initDate(request, map);
        request.setAttribute("menuId", "020");//设置模块ID，用来填充菜单栏选中样式
        return new ModelAndView("/price/subModelPriceAnalysis/subModelPriceAnalysisMain", model.asMap());
    }
    
    /**
   	 * 时间改变事件
   	 * 
   	 * @param request
   	 * @param response
   	 * @throws Exception
   	 */
    @RequestMapping("/price/subModelPriceAnalysis/changeDate")
    public void changeDate(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	Map<String, Object> paramsMap = new HashMap<String, Object>();
       	paramsMap.put("changeDate", "1");
       	paramsMap.put("priceType", request.getParameter("priceType") == null ? "1" : request.getParameter("priceType"));
       	paramsMap.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
       	paramsMap.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
       	String json = subModelPriceAnalysisManager.initDate(request, paramsMap);
       	try {
   			print(response,json);
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
    }
    
    /**
     * 获取初始化子车型控件值
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/price/subModelPriceAnalysis/getSubModelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	subModelPriceAnalysisManager.getSubmodelModal(request, map);
    	//返回细分市场页
    	if("2".equals(map.get("subModelShowType"))) {
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaBySegmentl", new ExtendedModelMap().asMap());
    	//返回品牌页
    	} else if("3".equals(map.get("subModelShowType"))) {
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByBrandtl", new ExtendedModelMap().asMap());
    	//返回厂商页
    	} else if("4".equals(map.get("subModelShowType"))) {
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByManftl", new ExtendedModelMap().asMap());
    	//返回本竞品页
    	} else {
    		return new ModelAndView("/policy/global/subModelModal", new ExtendedModelMap().asMap());
    	}
    }
    
    /**
     * 获取车身形式
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/price/subModelPriceAnalysis/getBodyType")
    public ModelAndView getBodyType(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	subModelPriceAnalysisManager.getBodyType(request, getPageParams(request));
    	return new ModelAndView("/policy/global/bodyTypeModal", model.asMap());
    }
    
    /**
     * 获取分析数据
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/price/subModelPriceAnalysis/getAnalysisData")
    public void getAnalysisData(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	Map<String, Object> paramsMap = getPageParams(request);
        String json = subModelPriceAnalysisManager.getAnalysisData(request, paramsMap);
        logManager.addModuleLog(request, paramsMap);
        print(response, json);
    }
    
    /**
     * 输出JSON
     * 
     * @param response
     * @param result
     * @throws IOException
     */
    private void print(HttpServletResponse response, String result) throws IOException
	{
	    response.setCharacterEncoding("UTF-8");
	    response.setContentType("text/json;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    out.print(result);
	    out.flush();
	    out.close();
	 }
    
    /**
     * 导出Excel
     * 
     * @param reuqest
     * @param response
     */
    @RequestMapping("/price/subModelPriceAnalysis/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) 
    {
    	Map<String, Object> paramsMap = getPageParams(request);
    	String languageType = paramsMap.get("languageType").toString();
		try {
			Workbook wb = subModelPriceAnalysisManager.exportExcel(request, paramsMap);
	        String excelName = "";
	        if("EN".equals(languageType)) {
			    excelName = java.net.URLEncoder.encode("PriceDetection-SubModelPriceAnalysis" + DateUtil.getCurrentTime(), "UTF-8");	
			} else {
				excelName = java.net.URLEncoder.encode("价格监测-车型价格段分析" + DateUtil.getCurrentTime(), "UTF-8");
			}
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
		    response.setHeader("Content-Disposition", "attachment;filename=" + excelName + ".xls");  
			ServletOutputStream out = response.getOutputStream(); 
			wb.write(out);
			//先把EXCEL写到临时目录，用来获取文件大小，最后删除
			File f = new File(request.getSession().getServletContext().getRealPath("/") + "/demoExcel/demo.xls");
			if(!f.exists()) {
				f.createNewFile();
			}
			BufferedOutputStream s = new BufferedOutputStream(new FileOutputStream(f));
			wb.write(s);
			
			//关闭流
			s.flush();
			s.close();
			out.flush();
			out.close();
			
			//记录导出日志，并删除临时文件
			paramsMap.put("exportSize", AppFrameworkUtil.getNum(f.length() / 1024, 0));
			logManager.updateModuleLog(paramsMap);
			f.delete();
		} catch (Exception e) {
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
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
		map.put("endDate", request.getParameter("beginDate"));//结束时间(跟开始时间一样)
		map.put("subModelIds",request.getParameter("subModelIds"));//车型ID
		map.put("bodyTypeIds", request.getParameter("bodyTypeIds"));//选择框车身形式ID以|隔开
		map.put("priceType", request.getParameter("priceType") == null ? "1" : request.getParameter("priceType"));//价格类型
		map.put("chartType", request.getParameter("chartType"));//图表类型
		map.put("languageType", request.getParameter("languageType"));//导出语言类型
		map.put("sortLabel", request.getParameter("sortLabel"));//标签显示内容
		map.put("showType", request.getParameter("showType"));//手动排挡显示方式
		map.put("refreshType", request.getParameter("refreshType"));//是否刷新图表:true为是，false为否;
		map.put("subModelShowType", request.getParameter("subModelShowType"));//子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
		String inputType = request.getParameter("inputType") == null ? "1" : request.getParameter("inputType");
		map.put("inputType", inputType);//单选/复选
		request.setAttribute("inputType", inputType);
		//添加记录日志属性
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", "车型价格段分析");//模块名称
		map.put("browser", request.getParameter("browser"));//浏览器
		map.put("ipAddress", AppFrameworkUtil.getRemoteHost(request));//IP地址
		map.put("queryCondition", request.getParameter("queryCondition"));//查询条件
		map.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		return map;
	}
}