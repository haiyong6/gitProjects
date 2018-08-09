package com.ways.app.policy.webapp.controller;

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
import com.ways.app.policy.service.ISalePromotionAnalysisManager;
import com.ways.framework.base.BaseController;

@Controller
public class SalePromotionAnalysisController extends BaseController {
    @Autowired
    private ISalePromotionAnalysisManager salePromotionAnalysisManager;
    protected final Log log = LogFactory.getLog(PromotionController.class);
	@Autowired
	private IModuleLogManager logManager;
	
	/**
	 * 进入主页方法
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/policy/salePromotionAnalysisMain")
    public ModelAndView terminalAnalysisMain(HttpServletRequest request) throws Exception
    {
        Model model = new ExtendedModelMap();
        Map<String, Object> map = getPageParams(request);
        salePromotionAnalysisManager.initDate(request, map);
        request.setAttribute("menuId", "015");//设置模块ID，用来填充菜单栏选中样式
        return new ModelAndView("/policy/salePromotionAnalysis/salePromotionAnalysisMain", model.asMap());
    }
    
    /**
     * 获取子车型下型号数据
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("policy/salePromotionAnalysis/getVersionModalByCommon")
    public ModelAndView getVersionModalByCommon(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	salePromotionAnalysisManager.getVersionModalByCommon(request, getPageParams(request));
    	return new ModelAndView("/policy/global/versionModal", model.asMap());
    }
    
    /**
     * 获取初始化常用对象
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/policy/salePromotionAnalysis/getAutoCustomGroup")
    public ModelAndView getAutoCustomGroup(HttpServletRequest request, HttpServletResponse response) throws Exception
    {	
    	Map<String, Object> paraMap = getPageParams(request);
    	salePromotionAnalysisManager.getAutoCustomGroup(request, paraMap);
        return new ModelAndView("/policy/global/autoVersionModal", new ExtendedModelMap().asMap());
    }
    
    /**
     * 获取初始化子车型控件值
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/policy/salePromotionAnalysis/getSubmodelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	salePromotionAnalysisManager.getSubmodelModal(request, map);
    	//返回细分市场页
    	if("2".equals(map.get("subModelShowType"))) {
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaBySegmentl", new ExtendedModelMap().asMap());
    	//返回品牌页
    	} else if("3".equals(map.get("subModelShowType"))) {
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByBrandtl", new ExtendedModelMap().asMap());
    	//返回厂商页
    	} else if("4".equals(map.get("subModelShowType"))) {
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByManftl", new ExtendedModelMap().asMap());
    	} else {
    		return new ModelAndView("/policy/global/subModelModal", new ExtendedModelMap().asMap());
    	}
    }
    
    /**
     * 获取厂商
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/policy/salePromotionAnalysis/getManf")
    public ModelAndView getManf(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	salePromotionAnalysisManager.getManf(request,getPageParams(request));
    	return new ModelAndView("/policy/global/manfModal", model.asMap());
    }
    
    /**
     * 获取品牌
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/policy/salePromotionAnalysis/getBrand")
    public ModelAndView getBrand(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	salePromotionAnalysisManager.getBrand(request, getPageParams(request));
    	return new ModelAndView("/policy/global/brandModal", model.asMap());
    }
    
    /**
     * 获取车系
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/policy/salePromotionAnalysis/getOrig")
    public ModelAndView getOrig(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	salePromotionAnalysisManager.getOrig(request,getPageParams(request));
    	return new ModelAndView("/policy/global/origModal", model.asMap());
    }
    
    /**
     * 获取车身形式
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/policy/salePromotionAnalysis/getBodyType")
    public ModelAndView getBodyType(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	salePromotionAnalysisManager.getBodyType(request, getPageParams(request));
    	return new ModelAndView("/policy/global/bodyTypeModal", model.asMap());
    }
    
    /**
     * 获取细分市场以及所属子细分市场
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/policy/salePromotionAnalysis/getSegmentAndChildren")
    public ModelAndView getSegmentAndChildren(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	salePromotionAnalysisManager.getSegmentAndChildren(request, getPageParams(request));
    	return new ModelAndView("/policy/global/segmentModal", model.asMap());
    }
    
    /**
     * 加载销量促销数据和图表
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/policy/salePromotionAnalysis/loadChartAndTable")
    public void loadChartAndTable(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	Map<String, Object> paramsMap = getPageParams(request);
    	String json = salePromotionAnalysisManager.loadChartAndTable(request, paramsMap);
    	logManager.addModuleLog(request, paramsMap);
		try {
			print(response, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
     */
    @RequestMapping("/policy/salePromotionAnalysis/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) 
    {
    	Map<String, Object> paramsMap = getPageParams(request);
    	String languageType = paramsMap.get("languageType").toString();
		try {
			Workbook wb = salePromotionAnalysisManager.exportExcel(request, paramsMap);
	        String excelName = "";
	        if("EN".equals(languageType)) {
			    excelName = java.net.URLEncoder.encode("TerminalSupportResearch-SalePromotionAnalysis" + DateUtil.getCurrentTime(), "UTF-8");	
			} else {
				excelName = java.net.URLEncoder.encode("终端支持研究-销量促销分析" + DateUtil.getCurrentTime(), "UTF-8");
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
    private Map<String, Object> getPageParams(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(getObjectData(request));
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
		map.put("endDate", request.getParameter("endDate"));//结束时间
		map.put("subModelShowType", request.getParameter("subModelShowType"));//子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
		map.put("languageType", request.getParameter("languageType"));//语言类型
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", "终端支持研究" + "-" + "销量促销分析");//模块名称
		map.put("browser", request.getParameter("browser"));//浏览器
		map.put("ipAddress", AppFrameworkUtil.getRemoteHost(request));//IP地址
		map.put("queryCondition", request.getParameter("queryCondition"));//查询条件
		map.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		String inputType = request.getParameter("inputType");//单选或复选
		if(null != inputType) {
			request.setAttribute("inputType", inputType);
		}
		map.put("paramModuleCode", "salePromotionAnalysis"); //模块名称
		if(null != request.getParameter("languageType") && !"".equals(request.getParameter("languageType").toString())) {
			map.putAll(getTickValues(request));
		}
		return map;
	}
    
    /**
     * 获取对象维度数据
     * 
     * @param request
     * @return
     */
    private Map<String, Object> getObjectData(HttpServletRequest request) 
    {
        String objectType = request.getParameter("objectType");
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("objectType", objectType);
    	if("1".equals(objectType)) {
    		paramsMap.put("subModelIds", request.getParameter("subModelIds"));
    		paramsMap.put("versionIds", request.getParameter("versionIds"));
    		paramsMap.put("versionNames", request.getParameter("versionNames"));
        } else if("2".equals(objectType)) {
        	paramsMap.put("subModelIds", request.getParameter("subModelIds"));
        } else if("3".equals(objectType)) {
        	paramsMap.put("manfIds", request.getParameter("manfIds"));
        } else if("4".equals(objectType)) {
        	paramsMap.put("brandIds", request.getParameter("brandIds"));
        } else if("5".equals(objectType)) {
        	paramsMap.put("origIds", request.getParameter("origIds"));
        	paramsMap.put("bodyTypeIds", request.getParameter("bodyTypeIds"));
        } else {
        	paramsMap.put("gradeIds", request.getParameter("gradeIds"));
        	String segmentType = request.getParameter("segmentType");//细分市场类别
    		if(null != segmentType) {
    			segmentType = segmentType.replace(",", "','");
    		}
    		paramsMap.put("segmentType", segmentType);
        	paramsMap.put("bodyTypeIds", request.getParameter("bodyTypeIds"));
        }
    	return paramsMap;
    }
    
    /**
     * 获取刻度间距值
     * @param request
     * @return
     */
    private Map<String, Object> getTickValues(HttpServletRequest request) 
    {
    	Map<String, Object> paramsMap = new HashMap<String, Object>();
    	paramsMap.put("priceMax", request.getParameter("priceMax"));//价格刻度最大值
		paramsMap.put("priceMin", request.getParameter("priceMin"));//价格刻度最小值
		paramsMap.put("priceTick", request.getParameter("priceTick"));//价格刻度间距
		paramsMap.put("saleMax", request.getParameter("saleMax"));//销量刻度最大值
		paramsMap.put("saleMin", request.getParameter("saleMin"));//销量刻度最小值
		paramsMap.put("saleTick", request.getParameter("saleTick"));//销量刻度间距
		paramsMap.put("promotionMax", request.getParameter("promotionMax"));//促销刻度最大值
		paramsMap.put("promotionMin", request.getParameter("promotionMin"));//促销刻度最小值
		paramsMap.put("promotionTick", request.getParameter("promotionTick"));//促销刻度间距
		return paramsMap;
    }
}
