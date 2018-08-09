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
import com.ways.app.price.service.ICityTpSelectManager;
import com.ways.framework.base.BaseController;

/**
 * 成交价查询Controller
 * 
 * @author songguobiao
 * 20160426
 */
@Controller
public class CityTpSelectController extends BaseController {

	protected final Log log = LogFactory.getLog(CityTpSelectController.class);
	@Autowired
	private ICityTpSelectManager cityTpSelectManager;
	@Autowired
	private IModuleLogManager logManager;
	 
	/**
	 * 进入主页方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/price/cityTpSelectMain")
    public ModelAndView cityTpSelectMain(HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
    	Model model = new ExtendedModelMap();
        Map<String, Object> map = getPageParams(request);
        cityTpSelectManager.initDate(request, map);
        request.setAttribute("menuId", "012");//设置模块ID，用来填充菜单栏选中样式
        return new ModelAndView("/price/cityTpSelect/cityTpSelectMain", model.asMap());
    }
    
    /**
     * 获取车身形式
     * 
     * @param request
     * @return
     * @throws Exception
     */
	@RequestMapping("/price/cityTpSelect/getBodyType")
    public ModelAndView getBodyType(HttpServletRequest request) throws Exception 
    {
    	Model model = new ExtendedModelMap();
    	cityTpSelectManager.getBodyType(request, getPageParams(request));
    	return new ModelAndView("/global/bodyTypeModal", model.asMap());
    }
	 
    /**
     * 获取初始化子车型控件值
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/price/cityTpSelect/getSubmodelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getPageParams(request);
    	Object subModelShowType = map.get("subModelShowType");
    	cityTpSelectManager.getSubmodelModal(request, map);
    	if("2".equals(subModelShowType)){
    		//返回细分市场页
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaBySegmentl", new ExtendedModelMap().asMap());
    	}else if("3".equals(subModelShowType)){
    		//返回品牌页
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByBrandtl", new ExtendedModelMap().asMap());
    	}else if("4".equals(subModelShowType)){
    		//返回厂商页
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByManftl", new ExtendedModelMap().asMap());
    	}else{
    		// 返回本品及竞品
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModal", new ExtendedModelMap().asMap());
    	}
    }
    
    /**
     * 获取初始化常用对象
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/price/cityTpSelect/getAutoCustomGroup")
    public ModelAndView getAutoCustomGroup(HttpServletRequest request, HttpServletResponse response) throws Exception
    {	
    	Map<String, Object> paramsMap = getPageParams(request);
    	cityTpSelectManager.getAutoCustomGroup(request, paramsMap);
        return new ModelAndView("/global/autoVersionModal", new ExtendedModelMap().asMap());
    }
    
    /**
     * 获取型号成交价数据
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/price/cityTpSelect/getVersionTpData")
    public void getVersionTpData(HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
    	Map<String, Object> paramsMap = getPageParams(request);
		DateUtil.getTPMix_Date(paramsMap);
		String json = cityTpSelectManager.getVersionTpData(request, paramsMap);
		logManager.addModuleLog(request, paramsMap);
		try {
			print(response, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 获取城市的名称和排序
     * 
     * @param request
     * @param response
     * @throw Exception
     */
    @RequestMapping("/price/cityTpSelect/getCityNameAndSort")
    public void getAllCityNames(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	Map<String, Object> paramsMap = getPageParams(request);
    	String json = cityTpSelectManager.getCityNameAndSort(paramsMap);
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
	@RequestMapping("/price/cityTpSelect/exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		try {
			String languageType = paramsMap.get("languageType").toString();
			Workbook wb = cityTpSelectManager.exportExcel(request, paramsMap);
			String excelName  = "";
			if("EN".equals(languageType)) {
			    excelName = java.net.URLEncoder.encode("PriceDetection-TPSelect" + DateUtil.getCurrentTime(), "UTF-8");	
			} else {
				excelName = java.net.URLEncoder.encode("价格监测-成交价查询" + DateUtil.getCurrentTime(), "UTF-8");
			}
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
		    response.setHeader("Content-Disposition", "attachment;filename=" + excelName + ".xls");  
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
    private Map<String, Object> getPageParams(HttpServletRequest request) 
    {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		if(null != beginDate && beginDate.length() <= 4) {
			beginDate += "-01";
			endDate += "-12";
		}
		map.put("beginDate", beginDate);//开始时间
		map.put("endDate", endDate);//结束时间
		String citys =  request.getParameter("citys");
		map.put("citys", citys);//城市ID
		map.put("countryAvg", request.getParameter("countryAvg"));//全国均价
		map.put("languageType", request.getParameter("languageType"));//导出语言类型
		map.put("bodyTypeId", request.getParameter("bodyTypeId"));//车身形式ID
		map.put("subModelShowType",request.getParameter("subModelShowType"));//子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
		String inputType = request.getParameter("inputType");//1：复选，2：单选;默认为1
    	if(inputType == null) {
    		inputType = "1";  
    	}
    	request.setAttribute("inputType", inputType);
    	map.put("versionIds", request.getParameter("versionIds"));//常用对象组型号ID
    	map.put("subModelIds", request.getParameter("subModelIds"));//车型ID
    	map.put("frequencyType", request.getParameter("frequencyType"));//频次类型
    	map.put("tpType", request.getParameter("tpType"));//成交价类型
		//添加记录日志属性
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", "成交价查询");//模块名称
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
