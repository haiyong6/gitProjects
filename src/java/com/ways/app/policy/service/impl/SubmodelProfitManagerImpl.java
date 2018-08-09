package com.ways.app.policy.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.framework.utils.Constant;
import com.ways.app.framework.utils.ExportExcelUtil;
import com.ways.app.policy.dao.SubmodelProfitDao;
import com.ways.app.policy.model.VersionPromotionInfoEntity;
import com.ways.app.policy.service.SubmodelProfitManager;
import com.ways.app.price.model.Manf;
import com.ways.app.price.model.SubModel;

/**
 * 车型利润分析Service层接口实现类
 * @author yinlue
 *
 */
@Service("submodelProfitManager")
public class SubmodelProfitManagerImpl implements SubmodelProfitManager {

	@Autowired
	private SubmodelProfitDao submodelProfitDao;

	/**
	 * 初始时间
	 */
	@Override
	public String initDate(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";//返回默认开始时间和结束时间
		List<Map<String, String>> list = submodelProfitDao.initDate(paramsMap);
		if(null != list && list.size() > 0) {
			Map<String, String> resultMap = list.get(0);
			String endDate = resultMap.get("ENDDATE");
			String beginDate = resultMap.get("BEGINDATE");
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			//默认开始时间，时间规则是相差12个点
			String defaultBeginDate = (Integer.parseInt(endDate.replace("-", "")) - 99) + "";
			//如果结束时间减一年小于开始时间，则默认为开始时间
			if(Integer.parseInt(defaultBeginDate) < Integer.parseInt(beginDate.replace("-", ""))) {
				request.setAttribute("defaultBeginDate", beginDate);
				json = resultMap.get("BEGINDATE");
			} else {
				if(Integer.parseInt(defaultBeginDate.substring(4)) > 12) {
					defaultBeginDate = (Integer.parseInt(defaultBeginDate.substring(0, 4)) + 1) + "-01";
				} else {
					defaultBeginDate = defaultBeginDate.substring(0, 4) + "-" + defaultBeginDate.substring(4);
				}
				request.setAttribute("defaultBeginDate", defaultBeginDate);
				json = defaultBeginDate;
			}
			json += "," +resultMap.get("BEGINDATE") + "," + endDate;
		}
		
		//返回是否含最新周数据
		/*String latestWeek = findLatestWeek(request, paramsMap);
		request.setAttribute("latestWeek", latestWeek);
		json += "," + latestWeek;*/
		return json;
	}

	/**
	 * 加载车型利润图形和表格
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public String loadModelProfitChartAndTable(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		String objectType = paramsMap.get("objectType").toString();
		List<VersionPromotionInfoEntity> list = new ArrayList<VersionPromotionInfoEntity>();
		//对象类型为型号
	    if("1".equals(objectType)) {
			   list = submodelProfitDao.loadVersionProfitChartAndTable(paramsMap);
		//对象类型为车型/生产商
	    } else{
	    	list = submodelProfitDao.loadSubmodelChart(paramsMap);
	    }
	    if(null != list && list.size() > 0) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("grid", list);
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			request.getSession().setAttribute(Constant.getSubmodelProfitExcelDataKey, json);
		}
		return json;
	}
	
	
	

	/**
	 * 校验弹出框有效数据
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public String checkPopBoxData(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		List<SubModel> list = submodelProfitDao.checkPopBoxData(paramsMap);
		if(null != list && list.size() > 0) json = AppFrameworkUtil.structureConfigParamsGroupJSONData(list);
		return json;
	}
	
	/**
	 * 校验生产商弹出框有效数据
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public String checkManfPopBoxData(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		List<Manf> list = submodelProfitDao.checkManfPopBoxData(paramsMap);
		if(null != list && list.size() > 0) {
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(list);
		}
		return json;
	}

	/**
	 * 导出
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public Workbook exportExcel(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		Workbook wb = null;
		String json = (String) request.getSession().getAttribute(Constant.getSubmodelProfitExcelDataKey);
		if(!AppFrameworkUtil.isEmpty(json)) {
		    JSONObject obj = (JSONObject) JSONObject.parse(json);
			JSONArray gridObj = (JSONArray) obj.getJSONArray("grid");
			String objectType= paramsMap.get("objectType").toString();
			try {
			    String path = request.getSession().getServletContext().getRealPath("/"); 
			    String analysisDimensionType = paramsMap.get("analysisType").toString();
			    String citys = paramsMap.get("citys").toString();
			    if("2".equals(analysisDimensionType) && !"0".equals(citys)){
			    	wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/submodelProfitCityTemplate.xls")));
			    } else{
			    	wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/submodelProfitTemplate.xls")));
			    }
			} catch (Exception e) {
				e.printStackTrace();
			}
			if("1".equals(objectType)){
				exportOriginalData(wb, wb.getSheet("原数据"), gridObj, paramsMap);
			} else{
				exportOriginalData2(wb, wb.getSheet("原数据"), gridObj, paramsMap);

			}
			exportChartData(wb, wb.getSheet("DATA"), gridObj, paramsMap);
		}
		return wb;
	}
	
	/**
	 * 导出原始数据
	 * 
	 * @param wb
	 * @param sheet
	 * @param gridObj
	 * @param paramsMap
	 */
	public void exportOriginalData(Workbook wb, Sheet sheet, JSONArray gridObj, Map<String, Object> paramsMap)
	{
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		String analysisDimensionType = paramsMap.get("analysisType").toString();
		String citys = paramsMap.get("citys").toString();
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		CellStyle textPercentStyle = ExportExcelUtil.getExcelFillPercentageStyle(wb);//百分号格式化
		CellStyle textPercentRedStyle = ExportExcelUtil.getExcelFillPercentageRedStyle(wb);//红色
		CellStyle thousandthREDStyle =ExportExcelUtil.getExcelFormatThousandthREDStyle(wb);
		//表格标题数组锁引，如果是城市利润需要添加城市列
		int index  = 0;
		
//		String objectType = paramsMap.get("objectType").toString();
		Row row;
		int rowIndex =0;//行号锁引
		//写表格数据内容
		for(int j = 0; j < gridObj.size(); j++) {
			JSONObject obj=(JSONObject) gridObj.get(j);
			String versionCode = obj.get("versionCode").toString();
			//如果无记录则不导出
			if(AppFrameworkUtil.isEmpty(versionCode)) {
				continue;
			}
			String yearMonth = obj.get("yearMonth").toString();
			
			String versionLaunchDate = obj.get("versionLaunchDate").toString();
			String versionSale = obj.get("versionSale").toString();
			String c1 = null;
			if("".equals(obj.get("c1").toString())) {
				c1="-";
			} else {
				c1 = obj.get("c1").toString();
			}
			
			String c2 = null;
			if("".equals(obj.get("c2").toString())) {
				c2 = "-";
			} else {
				c2 = obj.get("c2").toString();
			}
			String c3 = null;
			if("".equals(obj.get("c3").toString())) {
				c3 = "-";
			} else {
				c3 = obj.get("c3").toString();
			}
			String c4 = null;
			if("".equals(obj.get("c4").toString())) {
				c4 = "-";
			} else {
				c4 = obj.get("c4").toString();
			}
			String c5 = null;
			if("".equals(obj.get("c5").toString())) {
				c5 = "-";
			} else {
				c5 = obj.get("c5").toString();
			}
			String c6 = null;
			if("".equals(obj.get("c6").toString())) {
				c6 = "-";
			} else {
				c6 = obj.get("c6").toString();
			}
			String c7 = null;
			if("".equals(obj.get("c7").toString())){
				c7 = "-";
			} else {
				c7 = obj.get("c7").toString();
			}
			String c8 = null;
			if("".equals(obj.get("c8").toString())) {
				c8 = "-";
			} else {
				c8 = obj.get("c8").toString();
			}
			//新增字段
			String segment = obj.get("segment").toString();//车身级别
			String msrp = obj.get("msrp").toString();//指导价
			String bonus = null;
			if("".equals(obj.get("bonus").toString())) {
				bonus = "-";
			} else {
				bonus = obj.get("bonus").toString();//考核奖励
			}
			String margin = null;
			if("".equals(obj.get("margin").toString())) {
				margin = "-";
			} else {
				margin = obj.get("margin").toString();//返利
			}	
			
			String fullyPaid=null;
			if("".equals(obj.get("fullyPaid").toString())){
				fullyPaid="-";
			} else{
				fullyPaid=obj.get("fullyPaid").toString();//全款购车促销支持
			}
			
			String grossSupports = null;
			if(c2 == "-" && c3 == "-") {
				grossSupports = "-";
			} else {
				grossSupports = obj.get("grossSupports").toString();//经销商支持
			}
			String customerIncentive = obj.get("customerIncentive").toString();//用户激励
			String maintenance = null;
			if("".equals(obj.get("maintenance").toString())) {
				maintenance = "-";
			} else {
				maintenance = obj.get("maintenance").toString();//保养
			}
			String invoicePrice = null;
			if("".equals(obj.get("invoicePrice").toString())) {
				invoicePrice = "-";
			} else {
				invoicePrice = obj.get("invoicePrice").toString();//开票价
			}
			String grossInvoicePrice = null;
			if("".equals(obj.get("grossInvoicePrice").toString()) ) {
				grossInvoicePrice = "-";
			} else {
				grossInvoicePrice = obj.get("grossInvoicePrice").toString();//经销商开票价
			}
			String tp = obj.get("tp").toString();//成交价tp
			String profit = null;
			if("".equals(obj.get("profit").toString()) ) {
				profit = "-";
			} else {
				profit = obj.get("profit").toString();//利润
			}
			String profitRate = null;
			if("".equals(obj.get("profitRate").toString()) ) {
				profitRate = "-";
			} else {
				profitRate = obj.get("profitRate").toString();//利润率
			}
			String versionLastMonthSales = null;
			if("".equals(obj.get("versionLastMonthSales").toString())) {
				versionLastMonthSales = "-";
			} else {
				versionLastMonthSales=obj.get("versionLastMonthSales").toString();//型号上月销量
			}
			
			String brandName = obj.get("brandName").toString();//品牌
			String versionName = obj.get("versionName").toString();//型号全称中文
			String manfName = obj.get("manfName").toString();
//			String origName = obj.getOrigName();
			String gradeName = obj.get("gradeName").toString();
//			String versionShortName = obj.getVersionShortName();
			String subModelName = obj.get("submodelName").toString();
			String bodyType = obj.get("bodyType").toString();
			String city = obj.get("cityName").toString();
			if("EN".equals(languageType)) {
				manfName = obj.get("manfNameEn").toString();
				city = obj.get("cityNameEn").toString();
				gradeName = obj.get("gradeNameEn").toString();
//				versionShortName = obj.getVersionShortNameEn();
				subModelName = obj.get("submodelNameEn").toString();
//				gearMode = obj.getGradeNameEn();				
				bodyType = obj.get("bodyTypeEn").toString();
				brandName = obj.get("brandNameEn").toString();
				versionName = obj.get("versionNameEn").toString();//型号全称英文
			} 
			//重置锁引，用于创建单元格列锁引位置
			Cell cell = null;
			index = 0;
			//rowIndex++;
			++rowIndex;
			row  = ExportExcelUtil.createRow(sheet, rowIndex, 400);
			//城市维度且城市不是全选
			if("2".equals(analysisDimensionType) && !"0".equals(citys)){
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, city + "~", textStyle);
			}
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, yearMonth + "~", textStyle);
				
			//"年月","型号编码","生产厂","系别","级别","型号标识","车型","拍档方式","排量","车身形式","上市时间","年式"			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  manfName + "~", textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, segment, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, gradeName, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, bodyType, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, brandName, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, subModelName, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionName, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionCode + "~", textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionLaunchDate, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, msrp, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, margin, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, bonus, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  c1 , thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  fullyPaid, thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  grossSupports, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c2, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  c3, thousandthStyle);
			String customerIncentive2 = null;	
			if(c8 == "-" && c7 == "-" && maintenance == "-" && c4 == "-" && c5 == "-" && c6 == "-") {
				customerIncentive2 = "-";
			} else {
				customerIncentive2 = customerIncentive;
			}
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, customerIncentive2, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c8, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c7, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, maintenance, thousandthStyle);
				
			//"型号销量"					
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c4, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c5, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c6, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, invoicePrice, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, grossInvoicePrice, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, tp, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyles(cell, profit,thousandthStyle, thousandthREDStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyles(cell, profitRate, textPercentStyle, textPercentRedStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionLastMonthSales, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionSale, thousandthStyle);
		}
		sheet.setDisplayGridlines(false);
	}
	
	@SuppressWarnings("unchecked")
	public void exportOriginalData2(Workbook wb, Sheet sheet, JSONArray gridObj, Map<String, Object> paramsMap)
	{

		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		CellStyle textPercentStyle = ExportExcelUtil.getExcelFillPercentageStyle(wb);//百分号格式化
		CellStyle textPercentRedStyle = ExportExcelUtil.getExcelFillPercentageRedStyle(wb);//红色
		CellStyle thousandthREDStyle =ExportExcelUtil.getExcelFormatThousandthREDStyle(wb);
		//表格标题数组锁引，如果是城市利润需要添加城市列
		int index  = 0;
		//putParam(paramsMap);
		List<VersionPromotionInfoEntity> list = null;
//		String objectType = paramsMap.get("objectType").toString();
		String analysisType = paramsMap.get("analysisType").toString();
		String citys = paramsMap.get("citys").toString();
		
		list = submodelProfitDao.exportProfitData(paramsMap);
		Row row;
		int rowIndex = 0;//行号锁引
		//写表格数据内容
		for(int j = 0; j < list.size(); j++) {
			VersionPromotionInfoEntity  obj = (VersionPromotionInfoEntity)list.get(j);
			//如果无记录则不导出
			if(AppFrameworkUtil.isEmpty(obj.getVersionCode())) {
				continue;
			}
			String yearMonth = obj.getYearMonth();
			String versionCode = obj.getVersionCode();
			String versionLaunchDate = obj.getVersionLaunchDate();
			String versionSale = obj.getVersionSale();
			String c1 = null;
			if(obj.getC1() == null) {
				c1="-";
			} else {
				c1 = obj.getC1();
			}
			
			String c2 = null;
			if(obj.getC2() == null) {
				c2 = "-";
			} else {
				c2 = obj.getC2();
			}
			String c3 = null;
			if(obj.getC3() == null) {
				c3 = "-";
			} else {
				c3 = obj.getC3();
			}
			String c4 = null;
			if(obj.getC4() == null) {
				c4 = "-";
			} else {
				c4 = obj.getC4();
			}
			String c5 = null;
			if(obj.getC5() == null) {
				c5 = "-";
			} else {
				c5 = obj.getC5();
			}
			String c6 = null;
			if(obj.getC6() == null) {
				c6 = "-";
			} else {
				c6 = obj.getC6();
			}
			String c7 = null;
			if(obj.getC7() == null){
				c7 = "-";
			} else {
				c7 = obj.getC7();
			}
			String c8 = null;
			if(obj.getC8() == null) {
				c8 = "-";
			} else {
				c8 = obj.getC8();
			}
			//新增字段
			String segment = obj.getSegment();//车身级别
			String msrp = obj.getMsrp();//指导价
			String bonus = null;
			if(obj.getBonus() == null) {
				bonus = "-";
			} else {
				bonus = obj.getBonus();//考核奖励
			}
			String margin = null;
			if(obj.getMargin() == null) {
				margin = "-";
			} else {
				margin = obj.getMargin();//返利
			}	
			
			String fullyPaid=null;
			if(obj.getFullyPaid() == null){
				fullyPaid="-";
			} else{
				fullyPaid=obj.getFullyPaid().toString();//全款购车促销支持
			}
			
			String grossSupports = null;
			if(c2 == "-" && c3 == "-") {
				grossSupports = "-";
			} else {
				grossSupports = obj.getGrossSupports();//经销商支持
			}
			String customerIncentive = obj.getCustomerIncentive();//用户激励
			String maintenance = null;
			if(obj.getMaintenance() == null) {
				maintenance = "-";
			} else {
				maintenance = obj.getMaintenance();//保养
			}
			String invoicePrice = null;
			if(obj.getInvoicePrice() == null) {
				invoicePrice = "-";
			} else {
				invoicePrice = obj.getInvoicePrice();//开票价
			}
			String grossInvoicePrice = null;
			if(obj.getGrossInvoicePrice() == null ) {
				grossInvoicePrice = "-";
			} else {
				grossInvoicePrice = obj.getGrossInvoicePrice();//经销商开票价
			}
			String tp = obj.getTp();//成交价tp
			String profit = null;
			if(obj.getProfit() == null) {
				profit = "-";
			} else {
				profit = obj.getProfit();//利润
			}
			String profitRate = null;
			if(obj.getProfitRate() == null) {
				profitRate = "-";
			} else {
				profitRate = obj.getProfitRate().toString();//利润率
			}
			String versionLastMonthSales = null;
			if(obj.getVersionLastMonthSales() == null) {
				versionLastMonthSales = "-";
			} else {
				versionLastMonthSales=obj.getVersionLastMonthSales();//型号上月销量
			}
			
			String brandName = obj.getBrandName();//品牌
			String versionName = obj.getVersionName();//型号全称中文
			String manfName = obj.getManfName();
			String gradeName = obj.getGradeName();
			String subModelName = obj.getSubmodelName();
			String bodyType = obj.getBodyType();
			String city = obj.getCityName();
			if("EN".equals(languageType)) {
				manfName = obj.getManfNameEn();
				gradeName = obj.getGradeNameEn();
				subModelName = obj.getSubmodelNameEn();
				bodyType = obj.getBodyTypeEn();
				brandName = obj.getBrandNameEn();
				versionName = obj.getVersionNameEn();//型号全称英文
				city = obj.getCityNameEn();
			} 
			//重置锁引，用于创建单元格列锁引位置
			Cell cell = null;
			index = 0;
			++rowIndex;
			row  = ExportExcelUtil.createRow(sheet, rowIndex, 400);
			
			if("2".equals(analysisType) && !"0".equals(citys)){
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, city + "~", textStyle);
			}
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, yearMonth + "~", textStyle);
				
			//"年月","型号编码","生产厂","系别","级别","型号标识","车型","拍档方式","排量","车身形式","上市时间","年式"			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  manfName + "~", textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, segment, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, gradeName, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, bodyType, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, brandName, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, subModelName, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionName, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionCode + "~", textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionLaunchDate, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, msrp, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, margin, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, bonus, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  c1 , thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  fullyPaid, thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  grossSupports, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c2, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  c3, thousandthStyle);
			String customerIncentive2 = null;	
			if(c8 == "-" && c7 == "-" && maintenance == "-" && c4 == "-" && c5 == "-" && c6 == "-") {
				customerIncentive2 = "-";
			} else {
				customerIncentive2 = customerIncentive;
			}
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, customerIncentive2, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c8, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c7, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, maintenance, thousandthStyle);
				
			//"型号销量"					
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c4, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c5, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c6, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, invoicePrice, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, grossInvoicePrice, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, tp, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyles(cell, profit,thousandthStyle, thousandthREDStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyles(cell, profitRate, textPercentStyle, textPercentRedStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionLastMonthSales, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionSale, thousandthStyle);
		}
		sheet.setDisplayGridlines(false);
	
	}
	
	/**
	 * 导出图形数据
	 * 
	 * @param wb
	 * @param sheet
	 * @param chartObj
	 * @param paramsMap
	 */
	public void exportChartData(Workbook wb, Sheet sheet, JSONArray gridObj, Map<String, Object> paramsMap)
	{
		
		String objectType = paramsMap.get("objectType").toString();
		String analysisType = paramsMap.get("analysisType").toString();
		String citys = paramsMap.get("citys").toString();
		String languageType = paramsMap.get("languageType").toString();
		int rowIndex = 0;// 行号锁引
		Row row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);// 表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);// 内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);// 格式化千分位样式
		int index = 0;
		Cell cell = null;
		cell = row.createCell(index);
		//日期
		if("EN".equals(languageType)){
			ExportExcelUtil.setCellValueAndStyle(cell, "Date", titleStyle);
		} else{
			ExportExcelUtil.setCellValueAndStyle(cell, "日期", titleStyle);
		}
		//写日期	
		for(int i = 0; i < gridObj.size(); i++) {
			JSONObject obj=(JSONObject) gridObj.get(i);
				cell = row.createCell(i+1);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("yearMonth"), textStyle);
		}
		//对象
		rowIndex++;
		row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
		index=0;
		cell = row.createCell(index);
		if("EN".equals(languageType)){
			ExportExcelUtil.setCellValueAndStyle(cell, "Object", titleStyle);
		} else{
			ExportExcelUtil.setCellValueAndStyle(cell, "对象", titleStyle);
		}
		String versionNameNew=null;
		String versionNameNewEn=null;
		//时间对比维度型号名称为空时找到型号名字
		if("1".equals(objectType)){
			for(int k = 0; k < gridObj.size(); k++){
				JSONObject obj1=(JSONObject) gridObj.get(k);
				if(!"".equals(obj1.getString("versionName"))){
					versionNameNew=obj1.getString("versionName");
					versionNameNewEn=obj1.getString("versionNameNewEn");
					break;
				} 
			}
		}
		
		//写对象	
				for(int i = 0; i < gridObj.size(); i++) {
					JSONObject obj=(JSONObject) gridObj.get(i);
						cell = row.createCell(i+1);
						//城市维度且不是全国均价
						if("2".equals(analysisType) && !"0".equals(citys)){
							if("EN".equals(languageType)){
								ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("cityNameEn"), textStyle);
							} else{
								ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("cityName"), textStyle);
							}
						} else{
							if("1".equals(objectType)){
								if("EN".equals(languageType)){
									if("3".equals(analysisType)&&"".equals(obj.getString("versionName"))){
										ExportExcelUtil.setCellValueAndStyle(cell, versionNameNewEn, textStyle);
									} else{
										ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionNameEn"), textStyle);
									}
								} else{
									//时间对比维度型号名是空补齐
									if("3".equals(analysisType)&&"".equals(obj.getString("versionName"))){
										ExportExcelUtil.setCellValueAndStyle(cell, versionNameNew, textStyle);
									} else{
										ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionName"), textStyle);
									}
								}
							} else{
								if("EN".equals(languageType)){
									ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("objNameEn"), textStyle);
								} else{
									ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("objName"), textStyle);
								}
							}
						}
				}
				
				//做宏时根据这个判断维度
				cell = row.createCell(100);
				ExportExcelUtil.setCellValueAndStyle(cell, analysisType, textStyle);
		//指导价
				rowIndex++;
				row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
				index=0;
				cell = row.createCell(index);
				if("EN".equals(languageType)){
					ExportExcelUtil.setCellValueAndStyle(cell, "MSRP", textStyle);
				} else{
					ExportExcelUtil.setCellValueAndStyle(cell, "指导价", textStyle);
				}
		//写指导价
				for(int i = 0; i < gridObj.size(); i++) {
					JSONObject obj=(JSONObject) gridObj.get(i);
						cell = row.createCell(i+1);
						ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("msrp"), thousandthStyle);
				}
		//成交价
				rowIndex++;
				row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
				index=0;
				cell = row.createCell(index);
				if("EN".equals(languageType)){
					ExportExcelUtil.setCellValueAndStyle(cell, "TP", textStyle);
				} else{
					ExportExcelUtil.setCellValueAndStyle(cell, "成交价", textStyle);
				}
		//写成交价
				for(int i = 0; i < gridObj.size(); i++) {
					JSONObject obj=(JSONObject) gridObj.get(i);
						cell = row.createCell(i+1);
						ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("tp"), thousandthStyle);
				}
				
				//经销商成本
				rowIndex++;
				row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
				index=0;
				cell = row.createCell(index);
				if("EN".equals(languageType)){
					ExportExcelUtil.setCellValueAndStyle(cell, "GrossCost", textStyle);
				} else{
					ExportExcelUtil.setCellValueAndStyle(cell, "经销商成本", textStyle);
				}
		//写经销商成本
				for(int i = 0; i < gridObj.size(); i++) {
					JSONObject obj=(JSONObject) gridObj.get(i);
						cell = row.createCell(i+1);
						if("".equals(obj.getString("grossInvoicePrice"))){
							ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
						} else{
							ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("grossInvoicePrice"), thousandthStyle);
						}
						
				}
				
				//利润
				rowIndex++;
				row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
				index=0;
				cell = row.createCell(index);
				if("EN".equals(languageType)){
					ExportExcelUtil.setCellValueAndStyle(cell, "Profit", textStyle);
				} else{
					ExportExcelUtil.setCellValueAndStyle(cell, "利润", textStyle);
				}
		//写利润
				for(int i = 0; i < gridObj.size(); i++) {
					JSONObject obj=(JSONObject) gridObj.get(i);
						cell = row.createCell(i+1);
						if("".equals(obj.getString("profit"))){
							ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
						} else{
							ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("profit"), thousandthStyle);
						}
				}
				
		//开票价
				rowIndex++;
				row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
				index=0;
				cell = row.createCell(index);
				if("EN".equals(languageType)){
					ExportExcelUtil.setCellValueAndStyle(cell, "InvoicePrice", textStyle);
				} else{
					ExportExcelUtil.setCellValueAndStyle(cell, "开票价", textStyle);
				}
		//写开票价
				for(int i = 0; i < gridObj.size(); i++) {
					JSONObject obj=(JSONObject) gridObj.get(i);
						cell = row.createCell(i+1);
						if("".equals(obj.getString("invoicePrice"))){
							ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
						} else{
							ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("invoicePrice"), thousandthStyle);
						}
				}

		//考核奖励
				rowIndex++;
				row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
				index=0;
				cell = row.createCell(index);
				if("EN".equals(languageType)){
					ExportExcelUtil.setCellValueAndStyle(cell, "Bonus", textStyle);
				} else{
					ExportExcelUtil.setCellValueAndStyle(cell, "考核奖励", textStyle);
				}
		//写考核奖励
				for(int i = 0; i < gridObj.size(); i++) {
					JSONObject obj=(JSONObject) gridObj.get(i);
						cell = row.createCell(i+1);
						if("".equals(obj.getString("bonus"))){
							ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
						} else{
							ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("bonus"), thousandthStyle);
						}
				}
		//用户激励
				rowIndex++;
				row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
				index=0;
				cell = row.createCell(index);
				if("EN".equals(languageType)){
					ExportExcelUtil.setCellValueAndStyle(cell, "CustomerIncentive", textStyle);
				} else{
					ExportExcelUtil.setCellValueAndStyle(cell, "用户激励", textStyle);
				}
		//写用户激励
				for(int i = 0; i < gridObj.size(); i++) {
					JSONObject obj=(JSONObject) gridObj.get(i);
						cell = row.createCell(i+1);
						if("".equals(obj.getString("customerIncentive"))){
							ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
						} else{
							if("".equals(obj.getString("c8"))&&"".equals(obj.getString("c7"))&&"".equals(obj.getString("maintenance"))&&"".equals(obj.getString("c4"))&&"".equals(obj.getString("c5"))&&"".equals(obj.getString("c6"))){
								ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
							} else{
								ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("customerIncentive"), thousandthStyle);
							}
						}
				}
		//经销商支持
				rowIndex++;
				row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
				index=0;
				cell = row.createCell(index);
				if("EN".equals(languageType)){
					ExportExcelUtil.setCellValueAndStyle(cell, "GrossSupports", textStyle);
				} else{
					ExportExcelUtil.setCellValueAndStyle(cell, "经销商支持", textStyle);
				}
		//写经销商支持
				for(int i = 0; i < gridObj.size(); i++) {
					JSONObject obj=(JSONObject) gridObj.get(i);
						cell = row.createCell(i+1);
						if(("".equals(obj.getString("grossSupports")))){
							ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
						} else{
							if("".equals(obj.getString("c2"))&&"".equals(obj.getString("c3"))){
								ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
							} else{
								ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("grossSupports"), thousandthStyle);
							}
						}
				}
		//返利
				rowIndex++;
				row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
				index=0;
				cell = row.createCell(index);
				if("EN".equals(languageType)){
					ExportExcelUtil.setCellValueAndStyle(cell, "Rebate", textStyle);
				} else{
					ExportExcelUtil.setCellValueAndStyle(cell, "返利", textStyle);
				}
		//写返利
				for(int i = 0; i < gridObj.size(); i++) {
					JSONObject obj=(JSONObject) gridObj.get(i);
						cell = row.createCell(i+1);
						if("".equals(obj.getString("margin"))){
							ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
						} else{
							ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("margin"), thousandthStyle);
						}
				}
	}
	
	/**
	 * 计算折扣
	 * 
	 * @param msrp
	 * @param tp
	 * @return
	 */
	public String countDiscount(String msrp, String tp)
	{
		String discount = "-";
		if(!"-".equals(msrp) && !"-".equals(tp)) {
			discount = Float.parseFloat(msrp) - Float.parseFloat(tp) + "";
		}
		return discount;
	}
	
	/**
	 * 根据语言类型获取值
	 * 
	 * @param obj
	 * @param languageType
	 * @param property
	 * @return
	 */
	public String getValueByLanguageType(JSONObject obj, String languageType, String property)
	{
		String value = "";
		if("EN".equals(languageType)) {
			value = obj.getString(property + "En");
		} else {
			value = obj.getString(property);
		}
		return value;
	}

}
