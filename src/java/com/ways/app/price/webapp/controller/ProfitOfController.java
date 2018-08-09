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
import com.ways.app.price.service.IProfitManager;
import com.ways.framework.base.BaseController;

/**
 * 车型利润分析controller
 * @author yinlue
 *
 */
@Controller
public class ProfitOfController extends BaseController {
	
	protected final Log log = LogFactory.getLog(ProfitOfController.class);
	@Autowired
	private IProfitManager profitManager;
	@Autowired
	private IModuleLogManager logManager;
	 
	/**
	 * 进入主页方法
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/profit/profitOfMain")
    public ModelAndView profitOfMain(HttpServletRequest request) throws Exception
    {
        Model model = new ExtendedModelMap();
        Map<String, Object> map = getPageParams(request);
        profitManager.initDate(request, map);
        request.setAttribute("menuId", "001");//设置模块ID，用来填充菜单栏选中样式
        return new ModelAndView("/price/profitOf/profitOfMain",model.asMap());
    }
    
    /**
	 * 重置时间
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/resetDate")
	public void resetDate(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		String json = profitManager.initDate(request, paramsMap);
		try {
			print(response, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
	 * 校验弹出框有效数据
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/checkPopBoxData")
	public void checkPopBoxData(HttpServletRequest request, HttpServletResponse response)
	{
		String json = "";
		Map<String, Object> paramsMap = getPageParams(request);
		//如果没有型号则保存车型
		if(AppFrameworkUtil.isEmpty((String)paramsMap.get("vids"))) {
			paramsMap.put("mids", request.getParameter("mids"));//车型ID
		}
		if("1".equals(paramsMap.get("objectType")) || "2".equals(paramsMap.get("objectType"))) {
			//对象类型为车型或者型号时弹出框校验
			json = profitManager.checkPopBoxData(request, paramsMap);
		} else {
			//对象类型为生产商时弹出框校验
			json = profitManager.checkManfPopBoxData(request, paramsMap);
		}
		try {
			print(response, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    
	/**
	 * 加载车型利润图形和表格
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/loadModelProfitChartAndTable")
	public void loadModelProfitChartAndTable(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		DateUtil.getTPMix_Date(paramsMap);
		String json = profitManager.loadModelProfitChartAndTable(request, paramsMap);
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
	@RequestMapping("/exportProfitExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		try {
			String languageType = paramsMap.get("languageType").toString();
			Workbook wb = profitManager.exportExcel(request, paramsMap);
			String excelName  = "";
			if("EN".equals(languageType)) {
			    excelName = java.net.URLEncoder.encode("PriceDetection-PriceProfitTrend" + DateUtil.getCurrentTime(), "UTF-8");	
			} else {
				excelName = java.net.URLEncoder.encode("价格监测-价格利润走势" + DateUtil.getCurrentTime(), "UTF-8");
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
			paramsMap.put("exportSize", AppFrameworkUtil.getNum(f.length()/1024, 0));
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
     * @param request
     * @return
     */
    public Map<String, Object> getPageParams(HttpServletRequest request)
	{
    	String citys =request.getParameter("citys");
    	String analysisContentType = request.getParameter("analysisContentType");//分析数据指标类型1:利润；2：折扣:3：成交价
		if(AppFrameworkUtil.isEmpty(analysisContentType)) {
			analysisContentType = "1";
		}
		
		String analysisType = request.getParameter("inputType");//分析类型,1:对象对比；2：城市对比
		//对象对比且全国均价
		if("8,17,28,3,18,7,12,15,31,2,5,11,23,1,14,16,20,32,4,13,21,22,29,19,30,41,42,48,60,61,47".equals(citys) && "1".equals(analysisType)){
			citys = "0";
		} 
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
		map.put("endDate", request.getParameter("endDate"));//结束时间
		map.put("modelIds",request.getParameter("mids"));//车型ID
		map.put("vids", request.getParameter("vids"));//型号ID
		map.put("manfs", request.getParameter("manfs"));//生产商ID
		map.put("latestWeek", request.getParameter("latestWeek"));//是否存在最新周数据
		map.put("maxDate", request.getParameter("maxDate"));//获取最大日期
		map.put("objectType", request.getParameter("objectType"));//对象类型
		map.put("citys", citys);//城市ID
		map.put("analysisType", analysisType);
		map.put("languageType", request.getParameter("languageType"));//导出语言类型
		map.put("ymax", request.getParameter("ymax"));//线图刻度最大值
		map.put("ymin", request.getParameter("ymin"));//线图刻度最小值
		map.put("analysisContentType", analysisContentType);
		
		//添加记录日志属性
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", "价格利润走势");//模块名称
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