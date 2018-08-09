package com.ways.app.policy.webapp.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
import com.ways.app.policy.service.PolicyMonthAnalyManager;
import com.ways.framework.base.BaseController;

/**
 * 促销查询controller
 * @author yuml
 *
 */
@Controller
public class PolicyMonthAnalyController extends BaseController{
	
	protected final Log log = LogFactory.getLog(PolicyMonthAnalyController.class);
	
	@Autowired
	private PolicyMonthAnalyManager policyMonthAnalyManager;
	@Autowired
	private IModuleLogManager logManager;
	
	 
	/**
	 * 进入主页方法
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/policy/policyMonthAnalyMain")
    public ModelAndView policyMonthAnalyMain(HttpServletRequest request) throws Exception
    {
        Model model = new ExtendedModelMap();
        Map<String, Object> map = getPageParams(request);
        policyMonthAnalyManager.initDate(request, map);
        request.setAttribute("menuId", "007");//设置模块ID，用来填充菜单栏选中样式
        return new ModelAndView("/policy/policyMonthAnaly/policyMonthAnalyMain",model.asMap());
    }
    
    /**
	 * 重置时间
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/resetDatePolicyMonthAnaly")
	public void resetDate(HttpServletRequest request,HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		String json = policyMonthAnalyManager.initDate(request, paramsMap);
		try {
			print(response, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    
	/**
	 * 加载数据
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/loadPolicyMonthData")
	public void loadModelProfitChartAndTable(HttpServletRequest request,HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		String json = policyMonthAnalyManager.loadModelPolicy(request,paramsMap);
		logManager.addModuleLog(request, paramsMap);
		try {
			print(response, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/policy/checkPopBoxData")
	public void checkPopBoxData(HttpServletRequest request, HttpServletResponse response)
	{
		String json = "";
		Map<String, Object> paramsMap = getPageParams(request);
			json = policyMonthAnalyManager.checkPopBoxData(request, paramsMap);
		try {
			print(response, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 导出
	 * @param request
	 * @param response
	 */
	@RequestMapping("/exportPolicyExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		
		try {
			Workbook wb = policyMonthAnalyManager.exportExcel(request, paramsMap);
	        String excelName = java.net.URLEncoder.encode("终端支持研究-促销查询", "UTF-8");
	        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date=new Date();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
		    response.setHeader("Content-Disposition", "attachment;filename=" + excelName + dateFormater.format(date) +".xls" );  
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
		map.put("modelIds",request.getParameter("modelIds"));//车型ID
		map.put("analysisDimensionType", request.getParameter("analysisDimensionType"));//分析维度：1，车型对比；2，时间对比
		
		String modelName = request.getParameter("modelName");//车型名称,用于导出
		if(!AppFrameworkUtil.isEmpty(modelName))
		{
			try {
				modelName = new String(modelName.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		map.put("modelName",modelName);
		
		//添加记录日志属性
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", "促销查询");//模块名称
		map.put("browser", request.getParameter("browser"));//浏览器
		map.put("ipAddress", AppFrameworkUtil.getRemoteHost(request));//IP地址
		map.put("queryCondition", request.getParameter("queryCondition"));//查询条件
		map.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		return map;
	}

}