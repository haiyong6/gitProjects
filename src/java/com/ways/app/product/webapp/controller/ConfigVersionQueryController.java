package com.ways.app.product.webapp.controller;

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
import com.ways.app.moduleLog.service.IModuleLogManager;
import com.ways.app.product.service.ConfigGlobalService;
import com.ways.app.product.service.ConfigVersionQueryService;
import com.ways.framework.base.BaseController;

/**
 * 配置型号查询controller
 * @author yinlue
 *
 */
@Controller
public class ConfigVersionQueryController extends BaseController{
	
	protected final Log log = LogFactory.getLog(ConfigVersionQueryController.class);
	@Autowired
	private ConfigVersionQueryService service;
	@Autowired
	private ConfigGlobalService globalService;
	@Autowired
	private IModuleLogManager logManager; 
	
	/**
	 * 进入主页方法
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/product/configVersionQueryMain")
    public ModelAndView configVersionQueryMain(HttpServletRequest request) throws Exception
    {
        Model model = new ExtendedModelMap();
        globalService.getSubModelBodyType(request, null);
        request.setAttribute("menuId", "006");//设置模块ID，用来填充菜单栏选中样式
        return new ModelAndView("/product/configVersionQuery/configVersionQuery",model.asMap());
    }
    
	/**
     * 加载配置型号结果集
     * @return
     * @throws Exception
     */
    @RequestMapping("/product/getConfigVersionResult")
    public void getConfigValue(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	Map<String, Object> paramsMap = getPageParams(request);
    	String json = service.getConfigVersionResult(request, paramsMap);
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
	@RequestMapping("/exportProductConfigVersion")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		try {
			Workbook wb = service.exportExcel(request, paramsMap);
	        String excelName = java.net.URLEncoder.encode("产品配置-型号配置查询", "UTF-8");
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
    	Map<String, Object> map = new HashMap<String, Object>();
		map.put("configs", request.getParameter("configs"));//配置多个配置用|隔开，配置里用，隔开
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		
		map.put("beginPrice", request.getParameter("beginPrice")); //开始价格
		map.put("endPrice", request.getParameter("endPrice"));	//结束价格
		map.put("subModelBodyType", request.getParameter("subModelBodyType")); //车身形式
		map.put("mids", request.getParameter("mids"));//获取车形ID
		map.put("languageType", request.getParameter("languageType"));//导出语言类型
		
		//添加记录日志属性
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", "型号配置查询");//模块名称
		map.put("browser", request.getParameter("browser"));//浏览器
		map.put("ipAddress", AppFrameworkUtil.getRemoteHost(request));//IP地址
		map.put("queryCondition", request.getParameter("queryCondition"));//查询条件
		map.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		
		return map;
	}
   
}