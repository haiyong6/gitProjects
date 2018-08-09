package com.ways.app.price.webapp.controller;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.ways.app.moduleLog.service.IModuleLogManager;
import com.ways.app.price.service.IMsrpQueryManager;
import com.ways.app.pricesale.model.PriceSaleModule;
import com.ways.framework.base.BaseController;

@Controller
public class MsrpQueryController extends BaseController {
	
	protected final Log log = LogFactory.getLog(MsrpQueryController.class);
	@Autowired
	private IMsrpQueryManager msrpQueryManager;
	@Autowired
	private IModuleLogManager logManager;
	
	private String moduleName = "指导价查询";
	private String moduleNameEn = "MsrpQuery";
	 
	/**
	 * 进入主页方法
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/price/msrpQueryMain")
    public ModelAndView terminalAnalysisMain(HttpServletRequest request) throws Exception
    {
        Model model = new ExtendedModelMap();
        msrpQueryManager.initDate(request, getPageParams(request));
        request.setAttribute("menuId", "021");//设置模块ID，用来填充菜单栏选中样式
        request.setAttribute("moduleCode", moduleNameEn);
        return new ModelAndView("/price/msrpQuery/msrpQueryMain", model.asMap());
    }
    
    /**
     * 获取初始化子车型控件值
     * @return
     * @throws Exception
     */
    @RequestMapping("/price/msrpQuery/getSubmodelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	Object subModelShowType = map.get("subModelShowType");
    	msrpQueryManager.getSubmodelModal(request, map);
    	if("2".equals(subModelShowType)) {
    		//返回细分市场页
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaBySegmentl", new ExtendedModelMap().asMap());
    	} else if("3".equals(subModelShowType)) {
    		//返回品牌页
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByBrandtl", new ExtendedModelMap().asMap());
    	} else if("4".equals(subModelShowType)) {
    		//返回厂商页
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByManftl", new ExtendedModelMap().asMap());
    	} else {
    		// 返回本品及竞品
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModal", new ExtendedModelMap().asMap());
    	}
    }
    
    /**
     * 获取车身形式
     * @param request
     * @return
     * @throws Exception
     */
	@RequestMapping("/price/msrpQuery/getBodyType")
    public ModelAndView getBodyType(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	msrpQueryManager.getBodyType(request, getPageParams(request));
    	return new ModelAndView("/global/bodyTypeModal", model.asMap());
    }
    
    /**
     * 获取初始化常用对象
     * @return
     * @throws Exception
     */
    @RequestMapping("/price/msrpQuery/getAutoCustomGroup")
    public ModelAndView getAutoCustomGroup(HttpServletRequest request,HttpServletResponse response) throws Exception
    {	
    	Map<String, Object> paraMap = getPageParams(request);
    	msrpQueryManager.getAutoCustomGroup(request, paraMap);
        return new ModelAndView("/global/autoVersionModal", new ExtendedModelMap().asMap());
    }
    
    /**
	 * 获取销售比例查询数据表格
	 * @param request
	 * @param response
	 */
    @RequestMapping("/price/msrpQuery/getMsrpQueryData")
	public void getModelSalesAmountFawData(HttpServletRequest request,HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		//DateUtil.getTPMix_Date(paramsMap);
		String json = msrpQueryManager.getMsrpQueryData(request, paramsMap);
		logManager.addModuleLog(request, paramsMap);
		try {
			print(response, json);
		} catch (IOException e) {
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
		map.put("priceType", "0");//价格类型（0是指导价）
		map.put("subModelId",request.getParameter("subModelId"));//车型ID
		map.put("bodyTypeId", request.getParameter("hatchbackId"));//车身形式ID
		map.put("queryCondition", request.getParameter("queryCondition"));//查询条件
		map.put("msrpType", request.getParameter("msrpType"));//0为显示历史指导价，1为最新指导价
		if(null != map.get("subModelId") && map.get("subModelId").toString().length()>0){
			map.put("hasMode", "true");
		}else{
			map.put("hasMode", "false");
		}
		
		map.put("subModelShowType",request.getParameter("subModelShowType"));//子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
		map.put("versionId", request.getParameter("aVersionId"));//常用对象组的型号ID
		if(null != map.get("versionId") && map.get("versionId").toString().length() >0){
			map.put("hasVersion", "true");
		}
		
		String analysisContentType = request.getParameter("analysisContentType");//模块名称
		map.put("analysisContentType", analysisContentType);
		
		String timeType = request.getParameter("timeType");//时间类型1：时间点;2：时间段默认为2
		if(AppFrameworkUtil.isEmpty(timeType)) timeType = "2";
		map.put("timeType", timeType);
		
		String inputType = request.getParameter("inputType");//1：复选，2：单选;默认为1
    	if(inputType == null) inputType ="1";  
    	request.setAttribute("inputType",inputType);
		//添加记录日志属性
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", moduleName);//模块名称
		map.put("browser", request.getParameter("browser"));//浏览器
		map.put("ipAddress", AppFrameworkUtil.getRemoteHost(request));//IP地址
		map.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		map.put("moduleCode", PriceSaleModule.JIAGEDUANXIAOLIANGFENGXI.getModuleCode());
		return map;
	}
    
    /**
     * 导出文件
     * @param request
     * @param response
     */
    @RequestMapping("/price/msrpQuery/exportExcelMap")
    public void exportExcel(HttpServletRequest request,HttpServletResponse response){

		Map<String, Object> paramsMap = getPageParams(request);
		try {
			Workbook wb = msrpQueryManager.exportExcel(request, paramsMap);
			String excelName = null;
			String languageType = request.getParameter("languageType");
			//System.out.println("languageType==="+languageType);
			if("EN".equals(languageType)){
				excelName = java.net.URLEncoder.encode(moduleNameEn, "UTF-8");
			} else{
				excelName = java.net.URLEncoder.encode(moduleName, "UTF-8");
			}
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date=new Date();
		    response.setHeader("Content-Disposition", "attachment;filename="+excelName+dateFormater.format(date)+".xls" );  
			ServletOutputStream out = response.getOutputStream();  
			wb.write(out);
			
			//先把EXCEL写到临时目录，用来获取文件大小，最后删除
			File f = new File(request.getSession().getServletContext().getRealPath("/") + "/demoExcel/demo.xls");
			if(!f.exists())
			{
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
			paramsMap.put("exportSize", AppFrameworkUtil.getNum(f.length()/1024, 0));
			logManager.updateModuleLog(paramsMap);
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}


