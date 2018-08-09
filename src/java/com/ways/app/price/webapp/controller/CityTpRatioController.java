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
import com.ways.app.price.service.ICityTpRatioManager;
import com.ways.framework.base.BaseController;

/**
 * 城市成交价对比
 */
@Controller
public class CityTpRatioController extends BaseController{
	
	protected final Log log = LogFactory.getLog(CityTpRatioController.class);
	@Autowired
	private ICityTpRatioManager cityTpRatioManager;
//	@Autowired
//	private IGlobalManager globalManager;
	@Autowired
	private IModuleLogManager logManager;
	 
	/**
	 * 进入主页方法
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/cityTpRatio/cityTpRatioMain")
    public ModelAndView profitOfMain(HttpServletRequest request) throws Exception
    {
        Model model = new ExtendedModelMap();
        Map<String, Object> map = getPageParams(request);
        cityTpRatioManager.initDate(request, map);
        request.setAttribute("menuId", "002");//设置模块ID，用来填充菜单栏选中样式
        return new ModelAndView("/price/cityTpRatio/cityTpRatio",model.asMap());
    }
    
    
	/**
	 * 加载城市成交价对比图形和表格
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/loadCityTpRatioChartAndTable")
	public void loadCityTpRatioChartAndTable(HttpServletRequest request,HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		DateUtil.getTPMix_Date(paramsMap);
		String json = cityTpRatioManager.loadCityTpRatioChartAndTable(request, paramsMap);
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
	@RequestMapping("/exportCityTpExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		
		try {
			Workbook wb = cityTpRatioManager.exportExcel(request, paramsMap);
	        String excelName = java.net.URLEncoder.encode("价格监测-成交价城市对比", "UTF-8");
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
    	String citys =request.getParameter("citys");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
		map.put("endDate", request.getParameter("endDate"));//结束时间
		map.put("mid",request.getParameterValues("mid"));//车型ID
		map.put("vid", request.getParameter("vid"));//型号ID
		map.put("citys", citys);//城市ID
		map.put("chartType", request.getParameter("chartType"));//1:雷达图；2：折线图
		map.put("languageType", request.getParameter("languageType"));//导出语言类型
		
		//添加记录日志属性
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", "成交价城市对比");//模块名称
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