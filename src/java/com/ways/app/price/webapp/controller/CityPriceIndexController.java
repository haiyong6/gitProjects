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
import com.ways.app.price.service.ICityPriceIndexManager;
import com.ways.app.price.service.IGlobalManager;
import com.ways.framework.base.BaseController;

/**
 * 区域价格降幅分析controller
 * @author yinlue
 *
 */
@Controller
public class CityPriceIndexController extends BaseController {
	
	protected final Log log = LogFactory.getLog(CityPriceIndexController.class);
	@Autowired
	private ICityPriceIndexManager manager;
	@Autowired
	private IModuleLogManager logManager;
	@Autowired
	private IGlobalManager globalManager;
	
	private String moduleName = "区域价格降幅";
	private String moduleNameEn = "CityPriceIndex";
	/**
	 * 进入主页方法
	 * 
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/cityPriceIndex/cityPriceIndexMain")
    public ModelAndView priceIndexOfMain(HttpServletRequest request) throws Exception
    {
        Model model = new ExtendedModelMap();
        manager.initDate(request, getPageParams(request));
        request.setAttribute("menuId", "023");//设置模块ID，用来填充菜单栏选中样式
        return new ModelAndView("/price/cityPriceIndex/cityPriceIndex", model.asMap());
    }
    
    /**
	 * 指导价成交价改变事件
	 * 
	 * @throws Exception
	 */
    @RequestMapping("/cityPriceIndex/tpDate")
    public void tpDate(HttpServletRequest request , HttpServletResponse response) throws Exception
    {
    	Map<String, Object> paramsMap = getPageParams(request);
    	String json = manager.tpDate(request, paramsMap);
    	//logManager.addModuleLog(request, paramsMap);
    	try {
			print(response,json);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 获取价格降幅分析数据
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/price/cityPriceIndex/getCityPriceIndexAnalyseData")
    public void getCityPriceIndexAnalyseData(HttpServletRequest request, HttpServletResponse response)
    {
    	Map<String, Object> paramsMap = getPageParams(request);
    	String json = manager.getCityPriceIndexAnalyseData(request, paramsMap);
    	logManager.addModuleLog(request, paramsMap);
    	try {
			print(response,json);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * 获取初始化城市控件值
     * @return
     * @throws Exception
     */
    @RequestMapping("/price/cityPriceIndex/getCityModal")
    public ModelAndView getCityModal(HttpServletRequest request,HttpServletResponse response) throws Exception
    {	
    	
    	Map<String, Object> paraMap = getPageParams(request);
    	//加上页面选择的时间
    	String dateGroup = paraMap.get("dateGroup").toString();
    	String endDate = dateGroup.split("\\|")[0].split(",")[0];
    	String beginDate = dateGroup.split("\\|")[dateGroup.split("\\|").length-1].split(",")[1];
    	String beginDate_Mix = beginDate;
    	String endDate_Mix = endDate;
    	if(Integer.parseInt(endDate_Mix.substring(0,4)) < Integer.parseInt(beginDate_Mix.substring(0,4))){
    		beginDate_Mix = Integer.toString(Integer.parseInt(endDate.substring(0,4))-1);
    		endDate_Mix = Integer.toString(Integer.parseInt(beginDate.substring(0,4))-1);
    	} else {
    		beginDate_Mix = Integer.toString(Integer.parseInt(beginDate.substring(0,4))-1);
    		endDate_Mix = Integer.toString(Integer.parseInt(endDate.substring(0,4))-1);
    	}
    	//低于2013年取13年mix时间
    	if(Integer.parseInt(beginDate_Mix) <= 2013){
    		beginDate_Mix = "2013";
    	} 
    	if(Integer.parseInt(endDate_Mix) <= 2013){
    		endDate_Mix = "2013";
    	}
    	
    	paraMap.put("beginDate_Mix", beginDate_Mix);
    	paraMap.put("endDate_Mix", endDate_Mix);
    	paraMap.put("beginDate", beginDate);
    	paraMap.put("endDate", endDate);
    	
    	DateUtil.getTPMix_Date(paraMap);
    	manager.getCityModal(request,paraMap);
        return new ModelAndView("/global/cityModal",new ExtendedModelMap().asMap());
    }
    
    /**
     * 获取初始化子车型控件值
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
   /* @RequestMapping("/price/cityPriceIndex/getSubmodelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getSubModelPageParams(request);
    	globalManager.getSubmodelModal(request, map);
    	//返回细分市场页
    	if("2".equals(map.get("subModelShowType"))) {
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaBySegmentl",new ExtendedModelMap().asMap());
    		//返回品牌页
    	} else if("3".equals(map.get("subModelShowType"))) {
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByBrandtl",new ExtendedModelMap().asMap());
    		//返回厂商页
    	} else if("4".equals(map.get("subModelShowType"))) {
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByManftl",new ExtendedModelMap().asMap());
    	} else {
    		return new ModelAndView("/global/subModelModal",new ExtendedModelMap().asMap());
    	}
        
    }*/
	/**
	 * 导出
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/exportCityPriceIndexOriginalData")
	public void exportCityPriceIndexOriginalData(HttpServletRequest request ,HttpServletResponse response)
	{
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("languageType", request.getParameter("languageType"));
		paramsMap.put("yMax", request.getParameter("yMax"));
		paramsMap.put("yMin", request.getParameter("yMin"));
		paramsMap.put("splitNumber", request.getParameter("splitNumber"));
		paramsMap.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		paramsMap.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		try {
			Workbook wb = manager.exportExcel(request, paramsMap);
			
	        String excelName = null;
	        if(request.getParameter("languageType").equals("EN")){
		        excelName = java.net.URLEncoder.encode("price-" + moduleNameEn + DateUtil.getCurrentTime(), "UTF-8");
	        } else{
		        excelName = java.net.URLEncoder.encode("价格监测-" + moduleName + DateUtil.getCurrentTime(), "UTF-8");
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
    public Map<String, Object> getPageParams(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("objectType", request.getParameter("objectType"));//页面分析维度
		map.put("priceType", request.getParameter("priceType"));//页面价格类型
		map.put("equipageType", request.getParameter("equipageType"));//页面装备信息
		map.put("dateGroup", request.getParameter("dateGroup"));//时间组以|隔开
		map.put("objId", request.getParameter("objId"));//选择框对象ID以|隔开
		map.put("objName", request.getParameter("objName"));//选择框对象名称以|隔开
		map.put("bodyTypeId", request.getParameter("bodyTypeId"));//选择框车身形式ID以|隔开
		map.put("bodyTypeName", request.getParameter("bodyTypeName"));//选择框车身形式名称以|隔开
		map.put("multiType", request.getParameter("multiType"));//时间多选为1,否则是对象对象
		map.put("brandAll", request.getParameter("brandAll"));//价格降幅中品牌为全选
		map.put("manfAll", request.getParameter("manfAll"));//价格降幅中厂商为全选
		map.put("cityIds", request.getParameter("cityIds"));//城市ID
		map.put("citySplit", request.getParameter("citySplit"));//城市是否拆分，true为拆分,false为不拆分
		//添加记录日志属性
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", moduleName);//模块名称
		map.put("browser", request.getParameter("browser"));//浏览器
		map.put("ipAddress", AppFrameworkUtil.getRemoteHost(request));//IP地址
		map.put("queryCondition", request.getParameter("queryCondition"));//查询条件
		map.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		return map;
	}
    
    public Map<String, Object> getSubModelPageParams(HttpServletRequest request)
  	{
  		Map<String, Object> map = new HashMap<String, Object>();
  		
  		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
  		map.put("beginDate", request.getParameter("beginDate"));//开始时间
  		map.put("endDate", request.getParameter("endDate"));//结束时间
  		map.put("modelIds", request.getParameter("modelIds"));//车型ID
  		map.put("subModelShowType", request.getParameter("subModelShowType"));//子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
  		map.put("bodyTypeId", request.getParameter("hatchbackId"));//价格降幅获取车型弹出框所对应的车身形式
  		String analysisContentType = request.getParameter("analysisContentType");//分析数据指标类型1:利润；2：折扣:3：成交价
  		if(AppFrameworkUtil.isEmpty(analysisContentType)) {
  			analysisContentType = "1";
  		}
  		map.put("analysisContentType", analysisContentType);
  		String timeType = request.getParameter("timeType");//时间类型1：时间点;2：时间段默认为2
  		if(AppFrameworkUtil.isEmpty(timeType)) {
  			timeType = "2";
  		}
  		map.put("timeType", timeType);
  		
  		String inputType = request.getParameter("inputType");//1：复选，2：单选;默认为1
      	if(inputType == null) {
      		inputType ="1";  
      	}
      	request.setAttribute("inputType", inputType);
  		return map;
  	}
}