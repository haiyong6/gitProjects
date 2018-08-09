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
import com.ways.app.policy.global.service.IPromotionGlobalManager;
import com.ways.app.policy.service.IPromotionGroupManager;
import com.ways.framework.base.BaseController;
/**
 * 促销分类分析
 */
@Controller
public class PromotionGroupController extends BaseController {
	protected final Log log = LogFactory.getLog(PromotionController.class);
	@Autowired
	private IPromotionGroupManager promotionGroupManager;
//	@Autowired
//	private IGlobalManager globalManager;
	@Autowired
	private IModuleLogManager logManager;
	@Autowired
	private IPromotionGlobalManager promotionGlobalManager;
	private String mName = "终端支持研究";
	private String moduleName = "促销分类分析";
	 
	/**
	 * 进入主页方法
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/promotionGroup/promotionGroupMain")
    public ModelAndView promotionGroupMain(HttpServletRequest request) throws Exception
    {
        Model model = new ExtendedModelMap();
        Map<String, Object> map = getPageParams(request);
        promotionGroupManager.initDate(request, map);
        request.setAttribute("menuId", "013");//设置模块ID，用来填充菜单栏选中样式
        return new ModelAndView("/policy/promotionGroup/promotionGroupMain", model.asMap());
    }
    
    /**
	 * 重置时间
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/promotionGroup/resetDate")
	public void resetDate(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		String json = promotionGroupManager.initDate(request, paramsMap);
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
	@RequestMapping(value="/promotionGroup/checkPopBoxData")
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
			json = promotionGroupManager.checkPopBoxData(request, paramsMap);
		} else {
			//对象类型为生产商时弹出框校验
			json = promotionGroupManager.checkManfPopBoxData(request, paramsMap);
		}
		try {
			print(response, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	 /**
     * 获取初始化子车型控件值
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/policy/promotionGroup/getSubmodelModal")
    public ModelAndView getSubmodelModal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	Map<String, Object> map = getSubModelPageParams(request);
    	promotionGlobalManager.getSubmodelModal(request,map);
    	//返回细分市场页
    	if("2".equals(map.get("subModelShowType"))) {
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaBySegmentl", new ExtendedModelMap().asMap());
   		//返回品牌页
    	} else if("3".equals(map.get("subModelShowType"))) {
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByBrandtl", new ExtendedModelMap().asMap());
   		//返回厂商页
    	} else if("4".equals(map.get("subModelShowType"))) {
    		return new ModelAndView("/policy/saleIncentiveQuery/subModelModaByManftl", new ExtendedModelMap().asMap());
    	} else {
    		return new ModelAndView("/policy/global/subModelModal", new ExtendedModelMap().asMap());
    	}
    }
    
	/**
	 * 加载促销分类图形和表格
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/promotionGroup/loadChartAndTable")
	public void loadPromotionChartAndTable(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams2(request);
		DateUtil.getTPMix_Date(paramsMap);
		String json = promotionGroupManager.loadChartAndTable(request, paramsMap);
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
	@RequestMapping("/promotionGroup/exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> paramsMap = getPageParams(request);
		try {
			Workbook wb = promotionGroupManager.exportExcel(request, paramsMap);
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
    	String citys =request.getParameter("citys");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
		map.put("endDate", request.getParameter("endDate"));//结束时间
		map.put("modelIds", request.getParameter("mids"));//车型ID
		map.put("vids", request.getParameter("vids"));//型号ID
		map.put("manfs", request.getParameter("manfs"));//生产商ID	
		map.put("bodyTypeIds", request.getParameter("bodyTypeIds"));//车身形式
		map.put("brandIds", request.getParameter("brandIds"));//品牌
		map.put("origIds", request.getParameter("origIds"));//系别
		map.put("segmentIds", request.getParameter("segmentIds"));//级别
		map.put("duiying", request.getParameter("duiying"));
//		String level1Ids = "";
//		String level2Ids = "";
//		if( segmentIds != null  &&  segmentIds.trim().length() > 0  && !segmentIds.trim().equalsIgnoreCase("null") ){
//			String [] levelIds = segmentIds.split(",");
//			for(int i = 0 ; i < levelIds.length ; i++ ){
//				String lid = levelIds[i];
//				if( lid != null && lid.split("~").length > 0  &&  lid.split("~")[1].equals("1") ){
//					if( level1Ids.trim().length() > 0 ) level1Ids += ",";
//					level1Ids += lid.split("~")[0];
//				}
//				if( lid != null && lid.split("~").length > 0  &&  lid.split("~")[1].equals("2") ){
//					if( level2Ids.trim().length() > 0 ) level2Ids += ",";
//					level2Ids += lid.split("~")[0];
//				}
//			}
//		}
//		map.put("level1Ids", level1Ids);//级别1	
//		map.put("level2Ids", level2Ids);//级别2	
//		String segmentCnd = "";
//		if( level1Ids.trim().length() > 0 && level2Ids.trim().length() > 0 ){
//			segmentCnd = " ( wg.grade_id in ("+level1Ids+") Or wg.grade_id in ("+level2Ids+") )";
//		}
//		if( level1Ids.trim().length() > 0 && level2Ids.trim().length()== 0 ){
//			segmentCnd = " wg.grade_id in ("+level1Ids+") ";
//		}
//		if( level1Ids.trim().length() == 0 && level2Ids.trim().length() > 0 ){
//			segmentCnd = " wg.grade_id in ("+level2Ids+")";
//		}
//		map.put("segmentCnd", segmentCnd);//级别2	
//		
//		System.out.println(segmentCnd);
		map.put("maxDate", request.getParameter("maxDate"));//获取最大日期
		map.put("objectType", request.getParameter("objectType"));//对象类型
		map.put("citys", citys);//城市ID
		map.put("analysisType", request.getParameter("inputType"));//分析类型,1:车型利润；2：城市利润
		map.put("languageType", request.getParameter("languageType"));//导出语言类型
		map.put("ymax", request.getParameter("ymax"));//线图刻度最大值
		map.put("ymin", request.getParameter("ymin"));//线图刻度最小值
		
		String analysisContentType = request.getParameter("analysisContentType");//分析数据指标类型1:促销；2：内促:3：外促
		if(AppFrameworkUtil.isEmpty(analysisContentType)) analysisContentType = "1";
		map.put("analysisContentType", analysisContentType);
		
		//添加记录日志属性
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", moduleName);//模块名称
		map.put("browser", request.getParameter("browser"));//浏览器
		map.put("ipAddress", AppFrameworkUtil.getRemoteHost(request));//IP地址
		map.put("queryCondition", request.getParameter("queryCondition"));//查询条件
		map.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		
		return map;
	}
    
    /**
     * 获取页面参数2
     * 
     * @param request
     * @return
     */
    public Map<String, Object> getPageParams2(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		String beginDate = request.getParameter("beginDate");//开始时间
		String endDate = request.getParameter("endDate");//结束时间
		String frequencyType = "1";//日期频次为月
		
		/*beginDate = getTerminalDate(beginDate,frequencyType,"begin");
		endDate = getTerminalDate(endDate,frequencyType,"end");
		*/
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		if(!AppFrameworkUtil.isEmpty(beginDate) && !AppFrameworkUtil.isEmpty(endDate)) {
			map.put("sort", getSortColumn(beginDate, endDate, frequencyType));//SQL排序所需字段
		}
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("frequencyType", frequencyType);//日期频次
		map.put("modelIds",request.getParameter("mids"));//车型ID
		map.put("vids", request.getParameter("vids"));
		map.put("manfIds", request.getParameter("manfIds"));
		map.put("brandIds", request.getParameter("brandIds"));
		map.put("origIds", request.getParameter("origIds"));
		map.put("segmentIds", request.getParameter("segmentIds"));
		map.put("bodyTypeIds", request.getParameter("bodyTypeIds"));
		map.put("objectType", request.getParameter("objectType"));
		
		map.put("duiying", request.getParameter("duiying"));
		map.put("priceType", request.getParameter("priceType"));
		map.put("isPromotion", request.getParameter("isPromotion"));
		map.put("submitType", request.getParameter("submitType"));
		
		map.put("userName", AppFrameworkUtil.getUserName(request));//用户名称
		map.put("moduleName", mName+"-"+moduleName);//模块名称
		map.put("browser", request.getParameter("browser"));//浏览器
		map.put("ipAddress", AppFrameworkUtil.getRemoteHost(request));//IP地址
		map.put("queryCondition", request.getParameter("queryCondition"));//查询条件
		map.put("loginId", AppFrameworkUtil.getUserId(request));//登录ID
		return map;
	}
    
    /**
     * SQL排序字段判断条件
     * 
     * @param sDate
     * @param eDate
     * @param fType
     * @return
     */
    private static String getSortColumn(String sDate, String eDate, String fType){
    	int monthCount = DateUtil.calculateMonthIn(sDate, eDate);
    	String sort = "";
    	if("1".equals(fType)) {
    		if(monthCount <= 4) {
    			sort = "dateTime";
    		} else {
    			sort = "object";
    		}
    	}/*else if("2".equals(ftype)){
    		if(monthCount/3 <= 4){
    			sort = "dateTime";
    		}else{
    			sort = "object";
    		}
    	}else{
    		if(monthCount/12 <= 4){
    			sort = "dateTime";
    		}else{
    			sort = "object";
    		}
    	}*/
		return sort;
	}
    
    /**
     * 获取弹出框公共参数
     * 
     * @param request
     * @return
     */
    public Map<String, Object> getSubModelPageParams(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppFrameworkUtil.getUserGroupId(request));//用户组ID
		map.put("beginDate", request.getParameter("beginDate"));//开始时间
		map.put("endDate", request.getParameter("endDate"));//结束时间
		map.put("modelIds", request.getParameter("modelIds"));//车型ID
		map.put("subModelShowType", request.getParameter("subModelShowType"));//子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
		map.put("bodyTypeId", request.getParameter("hatchbackId"));//车身形式
		
		String analysisContentType = request.getParameter("analysisContentType");//分析数据指标类型1:促销；2：内促:3：外促
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
    		inputType = "1";  
    	}
    	request.setAttribute("inputType", inputType);
		return map;
	}
}
