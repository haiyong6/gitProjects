package com.ways.app.pricesale.webapp.controller;

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
import com.ways.app.pricesale.model.PriceSaleModule;
import com.ways.app.pricesale.service.IVolumeByPriceRangeManager;
import com.ways.framework.base.BaseController;

@Controller
public class VolumeByPriceRangeController extends BaseController {
	protected final Log log = LogFactory.getLog(VolumeByPriceRangeController.class);
	@Autowired
	private IModuleLogManager logManager;
	@Autowired
	private IVolumeByPriceRangeManager volumeByPriceRangeManager;
	
	/**
	 * 进入主页方法
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/pricesale/VolumeByPriceRangeMain")
    public ModelAndView VolumeByPriceRangeMain(HttpServletRequest request) throws Exception
    {
        Model model = new ExtendedModelMap();
        volumeByPriceRangeManager.initDate(request, getPageParams(request));
        request.setAttribute("menuId", "008");//设置模块ID，用来填充菜单栏选中样式
        request.setAttribute("moduleCode", PriceSaleModule.JIAGEDUANXIAOLIANGFENGXI.getModuleCode());
        return new ModelAndView("/pricesale/VolumeByPriceRange/VolumeByPriceRangeMain", model.asMap());
    }
    
    /**
     * 获取初始化子车型控件值
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/pricesale/VolumeByPriceRange/getSubmodelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getParamsBySubModel(request);
    	Object subModelShowType = map.get("subModelShowType");
    	volumeByPriceRangeManager.getSubmodelModal(request, map);
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
     * 获取价格段销量分析数据
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/pricesale/getVolumeByPriceRangeData")
    public void getPriceIndexAnalyseData(HttpServletRequest request, HttpServletResponse response)
    {
    	Map<String, Object> paramsMap = getPageParams(request);
    	try {
	    	String json = volumeByPriceRangeManager.getVolumeByPriceRangeData(request, paramsMap);
	    	logManager.addModuleLog(request, paramsMap);
			print(response,json);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	/**
	 * 导出
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/exportVolumeByPriceRangeData")
	public void exportVolumeByPriceRangeData(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("languageType", request.getParameter("languageType"));
		paramsMap.put("yMax", request.getParameter("yMax"));
		paramsMap.put("yMin", request.getParameter("yMin"));
		paramsMap.put("splitNumber", request.getParameter("splitNumber"));
		paramsMap.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		paramsMap.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		try {
			Workbook wb = volumeByPriceRangeManager.exportExcel(request, paramsMap);
	        String excelName = "";
	        if("EN".equals(paramsMap.get("languageType"))) {
	        	excelName = "Price Analysis-VolumeByPriceRange";
	        } else {
	        	excelName = java.net.URLEncoder.encode("价量分析-价格段销量分析", "UTF-8");
	        }
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
		    response.setHeader("Content-Disposition", "attachment;filename=" + excelName + ".xls" );  
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
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("objectType", request.getParameter("objectType"));//页面分析维度
		map.put("priceType", request.getParameter("priceType"));//页面价格类型
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
  		map.put("endDate", request.getParameter("endDate"));//结束时间
		map.put("equipageType", request.getParameter("equipageType"));//页面装备信息
		map.put("dateGroup", request.getParameter("dateGroup"));//时间组以|隔开
		map.put("objId", request.getParameter("objId"));//选择框对象ID以|隔开
		map.put("objName", request.getParameter("objName"));//选择框对象名称以|隔开
		map.put("bodyTypeId", request.getParameter("bodyTypeId"));//选择框车身形式ID以|隔开
		map.put("bodyTypeName", request.getParameter("bodyTypeName"));//选择框车身形式名称以|隔开
		map.put("multiType", request.getParameter("multiType"));//时间多选为1,否则是对象对象
		String analysisContentType = request.getParameter("analysisContentType");//模块名称
		String inputType = request.getParameter("inputType");//1：复选，2：单选;默认为1
      	if(inputType == null) {
      		inputType = "1";  
      	}
      	request.setAttribute("inputType", inputType);
		map.put("analysisContentType", analysisContentType);
		Object sprice = request.getParameter("sprice");
		Object eprice = request.getParameter("eprice");
		if(sprice != null && !"".equals(sprice) && eprice != null && !"".equals(eprice)) {
			map.put("sprice", Integer.parseInt(sprice.toString()) * 10000);//获取价格起始值
			map.put("eprice", Integer.parseInt(eprice.toString()) * 10000);//获取价格结束值
		}
		Object priceScale = request.getParameter("priceScale");
		if(priceScale != null) {
			map.put("priceScale", Integer.parseInt(priceScale.toString()));
		}
		//添加记录日志属性
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", PriceSaleModule.JIAGEDUANXIAOLIANGFENGXI.getModuleName());//模块名称
		map.put("browser", request.getParameter("browser"));//浏览器
		map.put("ipAddress", AppFrameworkUtil.getRemoteHost(request));//IP地址
		map.put("queryCondition", request.getParameter("queryCondition"));//查询条件
		map.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		map.put("moduleCode", PriceSaleModule.JIAGEDUANXIAOLIANGFENGXI.getModuleCode());
		return map;
	}
    
    /**
     * 获取页面参数(查询车型时)
     * 
     * @param request
     * @return
     */
    public Map<String, Object> getParamsBySubModel(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		String analysisContentType = request.getParameter("analysisContentType");//模块名称
		map.put("analysisContentType", analysisContentType);
		map.put("priceType", request.getParameter("priceType"));//页面价格类型
		map.put("subModelShowType", request.getParameter("subModelShowType"));
		map.put("priceType", request.getParameter("priceType"));//页面价格类型
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
  		map.put("endDate", request.getParameter("endDate"));//结束时间
  		String bodyTypeId = request.getParameter("bodyTypeId");
		map.put("bodyTypeId", bodyTypeId);//选择框车身形式ID以|隔开
		String inputType = request.getParameter("inputType");//1：复选，2：单选;默认为1
      	if(inputType == null) {
      		inputType = "1";  
      	}
      	request.setAttribute("inputType", inputType);
		return map;
	}
}
