package com.ways.app.price.webapp.controller;

import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.framework.utils.DateUtil;
import com.ways.app.moduleLog.service.IModuleLogManager;
import com.ways.app.price.service.IVersionDiscountRatioManager;
import com.ways.framework.base.BaseController;

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

/**
 * 型号折扣对比Conroller
 * @author yinlue
 *
 */
@Controller
public class VersionDiscountRatioController extends BaseController {
	protected final Log log = LogFactory.getLog(VersionDiscountRatioController.class);

	@Autowired
	private IVersionDiscountRatioManager manager;

//	@Autowired
//	private IGlobalManager globalManager;
	
	@Autowired
	private IModuleLogManager logManager;

	/**
	 * 进入主页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/versionDiscountRatio/versionDiscountRatio")
	public ModelAndView profitOfMain(HttpServletRequest request)throws Exception 
	{
		Model model = new ExtendedModelMap();
		Map<String, Object> map = getPageParams(request);
		manager.initDate(request, map);
		request.setAttribute("menuId", "003");//设置模块ID，用来填充菜单栏选中样式
		return new ModelAndView("/price/versionDiscountRatio/versionDiscountRatio",model.asMap());
	}

	/**
	 * 加载图形和表格
	 * @param request
	 * @param response
	 */
	@RequestMapping("/loadVersionDiscountRatioChartAndTable")
	public void loadVersionDiscountRatioChartAndTable(HttpServletRequest request, HttpServletResponse response) 
	{
		Map<String,Object> paramsMap = getPageParams(request);
		DateUtil.getTPMix_Date(paramsMap);
		String json = manager.loadVersionDiscountRatioChartAndTable(request, paramsMap);
		logManager.addModuleLog(request, paramsMap);
		
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
	@RequestMapping("/exportDiscountRatioExcel")
	public void exportDiscountRatioExcel(HttpServletRequest request,HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		
		try {
			Workbook wb = manager.exportExcel(request, paramsMap);
	        String excelName = java.net.URLEncoder.encode("价格监测-车型折扣对比", "UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
		    response.setHeader("Content-Disposition", "attachment;filename="+excelName+".xls" );  
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

	/**
	 * 获取前台页面参数
	 * @param request
	 * @return
	 */
	public Map<String, Object> getPageParams(HttpServletRequest request) 
	{
		String citys =request.getParameter("citys");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));
		map.put("beginDate", request.getParameter("beginDate"));
		map.put("endDate", request.getParameter("endDate"));
		map.put("mid", request.getParameterValues("mid"));
		map.put("vid", request.getParameter("vid"));
		map.put("citys", request.getParameter("citys"));
		map.put("languageType", request.getParameter("languageType"));//导出语言类型
		//导出线图刻度最小值和最大值，供数据部使用
		map.put("ymax", request.getParameter("ymax"));
		map.put("ymin", request.getParameter("ymin"));
		
		//添加记录日志属性
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", "车型折扣对比");//模块名称
		map.put("browser", request.getParameter("browser"));//浏览器
		map.put("ipAddress", AppFrameworkUtil.getRemoteHost(request));//IP地址
		map.put("queryCondition", request.getParameter("queryCondition"));//查询条件
		map.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		
		//存储城市个数
		if(citys != null){
			map.put("cityNum", citys.split(",").length);
		}
		
		return map;
	}
}