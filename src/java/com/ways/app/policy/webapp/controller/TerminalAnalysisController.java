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
import com.ways.app.policy.service.ITerminalAnalysisManager;
import com.ways.framework.base.BaseController;

@Controller
public class TerminalAnalysisController extends BaseController {
	
	protected final Log log = LogFactory.getLog(TerminalAnalysisController.class);
	@Autowired
	private ITerminalAnalysisManager terminalAnalysisManager;
	@Autowired
	private IModuleLogManager logManager;
	
	private String mName = "终端支持研究";
	private String moduleName = "终端支持分析";
	 
	/**
	 * 进入主页方法
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/terminal/terminalAnalysisMain")
    public ModelAndView terminalAnalysisMain(HttpServletRequest request) throws Exception
    {
        Model model = new ExtendedModelMap();
        Map<String, Object> map = getPageParams(request);
        terminalAnalysisManager.initDate(request, map);
        request.setAttribute("menuId", "011");//设置模块ID，用来填充菜单栏选中样式
        return new ModelAndView("/policy/terminalAnalysis/terminalAnalysisMain", model.asMap());
    }
    
	/**
	 * 加载终端支持图形和表格
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/terminal/loadChartAndTable")
	public void loadTerminalAnalysisChartAndTable(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		DateUtil.getTPMix_Date(paramsMap);
		String json = terminalAnalysisManager.loadChartAndTable(request, paramsMap);
		logManager.addModuleLog(request, paramsMap);
		try {
			print(response, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/terminal/exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getExportParams(request);
		try {
			Workbook wb = terminalAnalysisManager.exportExcel(request, paramsMap);
	        String excelName = java.net.URLEncoder.encode(mName + "-" + moduleName + DateUtil.getCurrentTime(), "UTF-8");
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
     * 输出JSON
     * 
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
		beginDate = getTerminalDate(beginDate, frequencyType, "begin");
		endDate = getTerminalDate(endDate, frequencyType, "end");
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		if(!AppFrameworkUtil.isEmpty(beginDate) && !AppFrameworkUtil.isEmpty(endDate)) {
			map.put("sort", getSortColumn(beginDate, endDate, frequencyType));//SQL排序所需字段
		}
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("frequencyType", frequencyType);//日期频次
		map.put("modelIds", request.getParameter("mids"));//车型ID
		map.put("vids", request.getParameter("vids"));
		map.put("manfIds", request.getParameter("manfIds"));
		map.put("brandIds", request.getParameter("brandIds"));
		map.put("origIds", request.getParameter("origIds"));
		map.put("segmentIds", request.getParameter("segmentIds"));
		map.put("bodyTypeIds", request.getParameter("bodyTypeIds"));
		map.put("objectType", request.getParameter("objectType"));
		map.put("priceType", request.getParameter("priceType"));
		map.put("isPromotion", request.getParameter("isPromotion"));
		map.put("submitType", request.getParameter("submitType"));
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", mName + "-" + moduleName);//模块名称
		map.put("browser", request.getParameter("browser"));//浏览器
		map.put("ipAddress", AppFrameworkUtil.getRemoteHost(request));//IP地址
		map.put("queryCondition", request.getParameter("queryCondition"));//查询条件
		map.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		return map;
	}
    
    /**
     * 获取页面参数
     * 
     * @param request
     * @return
     */
    public Map<String, Object> getExportParams(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		String beginDate = request.getParameter("ex_beginDate");//开始时间
		String endDate = request.getParameter("ex_endDate");//结束时间
		String frequencyType = request.getParameter("ex_frequencyType");//日期频次
		//beginDate = getTerminalDate(beginDate,frequencyType, "begin");
		//endDate = getTerminalDate(endDate,frequencyType, "end");
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		if(!AppFrameworkUtil.isEmpty(beginDate) && !AppFrameworkUtil.isEmpty(endDate)) {
			map.put("sort", getSortColumn(beginDate, endDate, frequencyType));//SQL排序所需字段
		}
		map.put("frequencyType", frequencyType);//日期频次
		map.put("modelIds",request.getParameter("ex_mids"));//车型ID
		map.put("vids", request.getParameter("ex_vids"));
		map.put("manfIds", request.getParameter("ex_manfIds"));
		map.put("brandIds", request.getParameter("ex_brandIds"));
		map.put("origIds", request.getParameter("ex_origIds"));
		map.put("segmentIds", request.getParameter("ex_segmentIds"));
		map.put("bodyTypeIds", request.getParameter("ex_bodyTypeIds"));
		map.put("objectType", request.getParameter("ex_objectType"));
		map.put("priceType", request.getParameter("ex_priceType"));
		map.put("languageType", request.getParameter("languageType"));
		return map;
	}
    
    /** 
     * 补全日期
     * 
     * @param  strDate
     * @param  fType
     * @param  dType
     * @return  
     */
    private static String getTerminalDate(String strDate, String fType, String dType)
    {
		String date = null;
		if(strDate != null) {
			//月
			if("1".equals(fType)) {
				date = strDate;
			//季度
			} else if("2".equals(fType)) {
				date = DateUtil.getQuarterMonth(strDate, dType);
			} else {
				//年
				if("begin".equals(dType)) {
					date = strDate.substring(0, 4) + "-01";
				} else {
					date = strDate.substring(0, 4) + "-12";
				}
			}
		}
		return date;
	}
    
    /** 
     * SQL排序字段判断条件
     * 
     * @param  sDate
     * @param  eDate
     * @param  ftype
     * @return  
     */
    private static String getSortColumn(String sDate,String eDate,String fType)
    {
    	int monthCount = DateUtil.calculateMonthIn(sDate, eDate);
    	String sort = "";
    	if("1".equals(fType)) {
    		if(monthCount <= 4) {
    			sort = "dateTime";
    		} else {
    			sort = "object";
    		}
    	} else if("2".equals(fType)) {
    		if(monthCount / 3 <= 4) {
    			sort = "dateTime";
    		} else {
    			sort = "object";
    		}
    	} else {
    		if(monthCount / 12 <= 4) {
    			sort = "dateTime";
    		} else {
    			sort = "object";
    		}
    	}
		return sort;
	}
}
