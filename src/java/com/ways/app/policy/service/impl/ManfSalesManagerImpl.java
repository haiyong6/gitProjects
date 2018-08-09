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
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.framework.utils.Constant;
import com.ways.app.framework.utils.ExportExcelUtil;
import com.ways.app.policy.dao.ManfSalesDao;
import com.ways.app.policy.model.TerminalObjectEntity;
import com.ways.app.policy.model.VersionPromotionInfoEntity;
import com.ways.app.policy.service.ManfSalesManager;
import com.ways.app.price.model.EchartLineDataEntity;

@Service("ManfSalesManager")
public class ManfSalesManagerImpl implements ManfSalesManager {

	@Autowired
	private ManfSalesDao manfSalesDao;

	private static String CXZE = "促销总额";
	private static String TCZC = "提车支持(STD支持)";
	private static String LSZC = "零售支持(AaK支持)";
	private static String RYJL = "人员奖励";
	private static String JRDK = "金融贷款";
	private static String ZHZC = "置换支持";
	private static String ZSBX = "赠送保险";
	private static String ZSLP = "赠送礼品(油卡、保养)";
//	/private static String XZCX = "选择促销";
	
	private static String[] STARRY = new String[]{TCZC, LSZC, RYJL, JRDK, ZHZC, ZSBX, ZSLP};
	 
	/**
	 * 初始时间
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public String initDate(HttpServletRequest request, Map<String, Object> paramsMap)
	{
		String json = "";// 返回默认开始时间和结束时间
		List<Map<String, String>> list = manfSalesDao.initDate(paramsMap);
		if (null != list && list.size() > 0) {
			Map<String, String> resultMap = list.get(0);
			String endDate = resultMap.get("ENDDATE");
			String beginDate = resultMap.get("BEGINDATE");
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			// 默认开始时间，时间规则是相差12个点
			String defaultBeginDate = (Integer.parseInt(endDate.replace("-", "")) - 99) + "";
			// 如果结束时间减一年小于开始时间，则默认为开始时间
			if (Integer.parseInt(defaultBeginDate) < Integer.parseInt(beginDate.replace("-", ""))) {
				request.setAttribute("defaultBeginDate", beginDate);
				json = resultMap.get("BEGINDATE");
			} else {
				if (Integer.parseInt(defaultBeginDate.substring(4)) > 12) {
					defaultBeginDate = (Integer.parseInt(defaultBeginDate.substring(0, 4)) + 1) + "-01";
				} else {
					defaultBeginDate = defaultBeginDate.substring(0, 4) + "-" + defaultBeginDate.substring(4);
				}
				request.setAttribute("defaultBeginDate", defaultBeginDate);
				json = defaultBeginDate;
			}
			json += "," + resultMap.get("BEGINDATE") + "," + endDate;
		}
		return json;
	}

	/**
	 * 加载厂商销售支持图形和表格
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public String loadChartAndTable(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		String objectType = paramsMap.get("objectType").toString();
//		String submitType = "changPromotion";
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<TerminalObjectEntity> list = null;
		//型号查询需要查询上下代
		if("0".equals(objectType)) {
			list = manfSalesDao.loadManfSalesChartAndTableByVersion(paramsMap);
		} else {
			list = manfSalesDao.loadManfSalesChartAndTable(paramsMap);
		}
		if(null != list && list.size() > 0) {
			//促销细分
			/*if("changPromotion".equals(submitType)) {
				dataMap.put("chart", getChartDataByPromotion(list));
			} else {
				dataMap.put("chart", getChartData(list, paramsMap));
			}*/
			dataMap.put("grid", list);
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			request.getSession().setAttribute(Constant.getExportExcelManfSalesDataKey, json);
		}
		
		return json;
	}

	/**
	 * 函数功能说明 获取对象厂商销售支持图形数据 
	 * 
	 * @param tList
	 * @param paramsMap
	 * @return 
	 */
	public Map<String, Object> getChartData(List<TerminalObjectEntity> tList, Map<String, Object> paramsMap) 
	{
		String oStr = "";
		String dStr = "";
		String objectName = "";
		String dateTime = "";
		boolean isNewDate = false;
		boolean isAddDate = false;
		
		boolean isNewObj = false;
		boolean isAddObj = false;
		String lineDateTimeData = "";
		String lineObjectData = "";
		List<String> oList = new ArrayList<String>();// 对象
		List<String> dList = new ArrayList<String>();// 时间
		List<TerminalObjectEntity> list = new ArrayList<TerminalObjectEntity>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		for(int i = 0; i < tList.size(); i++) {
			if(CXZE.equals(tList.get(i).getSubsidyType())) {
				list.add(tList.get(i));
			}
		}
		if (null != list && list.size() > 0) {
			// 线图数据
			List<EchartLineDataEntity> lineDateTimeList = new ArrayList<EchartLineDataEntity>();
			List<EchartLineDataEntity> lineObjectList = new ArrayList<EchartLineDataEntity>();
			// 数据
			for(int i = 0; i < list.size(); i++) {
				String dtime = list.get(i).getDateTime();
				String oName = list.get(i).getObjectNameEn();
				String sValue = list.get(i).getSubsidy();
				if (!dStr.contains(dtime)) {
					dStr += dtime + ",";
					dList.add(dtime);
					isNewDate = true;
					if(i != 0) {
						isAddDate = true;
					}
				}
				if(isAddDate) {
					lineDateTimeList.add(new EchartLineDataEntity("bar", dateTime, "emptyCircle", lineDateTimeData.split(",")));
					isAddDate = false;
				}
				if(isNewDate) {
					lineDateTimeData = sValue;
					isNewDate = false;
				} else {
					lineDateTimeData += "," + sValue;
				}
				
				if (!oStr.contains(oName)) {
					oStr += oName + ",";
					oList.add(oName);
					isNewObj = true;
					if(i != 0) {
						isAddObj = true;
					}
				}
				
				if(isAddObj) {
					lineObjectList.add(new EchartLineDataEntity("bar", objectName, "emptyCircle", lineObjectData.split(",")));
					isAddObj = false;
				}
				
				if(isNewObj) {
					lineObjectData = sValue;
					isNewObj = false;
				} else {
					lineObjectData += "," + sValue;
				}
				
				dateTime = dtime;
				objectName = oName;
				
				//添加最后一个对象
				if(i == list.size() - 1) {
					lineDateTimeList.add(new EchartLineDataEntity("bar", dateTime, "emptyCircle", lineDateTimeData.split(",")));
					lineObjectList.add(new EchartLineDataEntity("bar", objectName, "emptyCircle", lineObjectData.split(",")));
					isAddDate = false;
					isAddObj = false;
				}
			}
			if (dList.size() <= 4) {
				dataMap.put("xTitle", oStr.substring(0,oStr.length() - 1).split(","));
				dataMap.put("series", lineDateTimeList);
				dataMap.put("titles", dStr.substring(0,dStr.length() - 1).split(","));
				dataMap.put("showType", "0");
			}

			if (oList.size() <= 4 && dList.size() > 4) {
				dataMap.put("xTitle", dStr.substring(0, dStr.length() - 1).split(","));
				dataMap.put("series", lineObjectList);
				dataMap.put("titles", oStr.substring(0, oStr.length() - 1).split(","));
				dataMap.put("showType", "1");
			}
		}
		return dataMap;
	}
	
	
	/**
	 * 函数功能说明 获取对象厂商销售支持图形数据 
	 * 
	 * @param tList
	 * @return 
	 */
	public Map<String, Object> getChartDataByPromotion(List<TerminalObjectEntity> tList) 
	{
		String oStr = "";
		String dStr = "";
		String showType = "";
		List<String> oList = new ArrayList<String>();// 对象
		List<String> dList = new ArrayList<String>();// 时间
		Map<String, Object> dataMap = null;
		for(int i = 0; i < tList.size(); i++) {
			String dtime = tList.get(i).getDateTime();
			String oName = tList.get(i).getObjectNameEn();
			if(!dStr.contains(dtime)) {
				dStr += dtime + ",";
				dList.add(dtime);
			}
			
			if(!oStr.contains(oName)) {
				oStr += oName + ",";
				oList.add(oName);
			}
		}
		
		if (dList.size() <= 4) {
			showType = "0";
			dataMap = getChartDataByPromotion(tList, showType, oList.size());
			dataMap.put("showType", showType); 
		}

		if (oList.size() <= 4 && dList.size() > 4) {
			showType = "1";
			dataMap = getChartDataByPromotion(tList, showType, dList.size());
			dataMap.put("showType", showType);
		}
		return dataMap;
	}
	
	/**
	 * 获取促销数据
	 * 
	 * @param tList
	 * @param showType
	 * @param size
	 * @return
	 */
	public Map<String, Object> getChartDataByPromotion(List<TerminalObjectEntity> tList, String showType, int size) 
	{
		String xTitle = "";
		String subxTitle = "";
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<EchartLineDataEntity> lineList = new ArrayList<EchartLineDataEntity>();
		for (int i = 0; i < STARRY.length; i++) {
			String subsidy = "";
			String subsidyType = STARRY[i];
			for (int j = 0; j < tList.size(); j++) {
				if(STARRY[i].equals(tList.get(j).getSubsidyType())) {
					subsidy += tList.get(j).getSubsidy() + ",";
					if("0".equals(showType) && i == 0) {
						xTitle += tList.get(j).getDateTime() + ",";
						if(!subxTitle.contains(tList.get(j).getObjectNameEn())) {
							subxTitle += tList.get(j).getObjectNameEn() + ",";
						}
					} else if("1".equals(showType) && i == 0) {
						xTitle += tList.get(j).getObjectNameEn() + ",";
						if(!subxTitle.contains(tList.get(j).getDateTime())) {
							subxTitle += tList.get(j).getDateTime() + ",";
						}
					}
				}
			}
			lineList.add(new EchartLineDataEntity("bar", subsidyType, "emptyCircle", subsidy.substring(0,subsidy.length() - 1).split(",")));
		}
		
		dataMap.put("xTitle", xTitle.substring(0, xTitle.length() - 1).split(","));
		dataMap.put("subxTitle", subxTitle.substring(0, subxTitle.length() - 1).split(","));
		dataMap.put("series", lineList);
		dataMap.put("titles", STARRY);
		return dataMap;
	}
	
	/**
	 * 获取图例
	 * 
	 * @param list
	 * @return
	 */
	public String[] getLineLegend(List<EchartLineDataEntity> list) {
		String[] legned = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			legned[i] = list.get(i).getName();
		}
		return legned;
	}

	/**
	 * 导出
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public Workbook exportExcel(HttpServletRequest request,Map<String, Object> paramsMap) {

		Workbook wb = null;
		String json = (String) request.getSession().getAttribute(Constant.getExportExcelManfSalesDataKey);
		if (!AppFrameworkUtil.isEmpty(json)) {
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			JSONArray gridObj = (JSONArray) obj.getJSONArray("grid");
			try {
				String path = request.getSession().getServletContext().getRealPath("/");
				wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/manfSalesTemplate.xls")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			exportObjectOriginalData(wb, wb.getSheet("原数据"), paramsMap);
			exportObjectChartData(wb, wb.getSheet("DATA"), gridObj, paramsMap);
			
		}
		return wb;
	}

	private void exportObjectChartData(Workbook wb, Sheet sheet,JSONArray gridObj, Map<String, Object> paramsMap) {
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		String[] title = null;
		if("EN".equals(languageType)){
			 title = new String[]{"STD","Aak","Staff reward","Financial loan","Trade-in support","Insurance","Presents"};
		} else{
			title = new String[]{"提车支持(STD支持)","零售支持(Aak支持)","人员奖励","金融贷款","置换支持","赠送保险","赠送礼品(油卡、保养)"};
		}
		String firstTitle = null;
		if("EN".equals(languageType)){
			 firstTitle = "Index";
		} else{
			firstTitle = "指标";
		}
		String secondTitle=null;
		if("EN".equals(languageType)){
			 secondTitle = "Price/Million";
		} else{
			secondTitle = "金额/百万";
		}
		String thirdTitle=null;
		if("EN".equals(languageType)){
			thirdTitle = "Persent/%";
		} else{
			thirdTitle = "占比/%";
		}
		int rowIndex = 0;//行号锁引
		Row row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
		
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
//		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		Cell cell=null;
		//写表格标题
		cell = row.createCell(0);
		ExportExcelUtil.setCellValueAndStyle(cell, firstTitle, titleStyle);
		
		for(int i = 0; i < title.length; i++) {
			cell = row.createCell(i + 1);
			ExportExcelUtil.setCellValueAndStyle(cell, title[i], titleStyle);
				sheet.setColumnWidth(i, 4000);
				if(i == title.length-1){
					sheet.setColumnWidth(i+1, 4000);
				}
		}
		
		//把时间写在后面，导出图表要用到
		JSONObject obj1=(JSONObject) gridObj.get(0);
		cell=row.createCell(100);
		ExportExcelUtil.setCellValueAndStyle(cell, obj1.get("dateTime").toString(), titleStyle);
		sheet.setColumnWidth(100, 4000);
		
		//写数据
		for(int j = 0; j < 2; j++) {
			rowIndex++;
			row = ExportExcelUtil.createRow(sheet, rowIndex, 400);
			cell = row.createCell(0);
			if(0 == j){
				ExportExcelUtil.setCellValueAndStyle(cell, secondTitle, textStyle);
			}else{
				ExportExcelUtil.setCellValueAndStyle(cell, thirdTitle, textStyle);
			}
			
			for(int n = 0; n < gridObj.size(); n++) {
				JSONObject obj=(JSONObject) gridObj.get(n);
				cell = row.createCell(n + 1);
				if( gridObj.get(n) != null && gridObj.size() > 0 && !gridObj.getString(n).trim().equalsIgnoreCase("null")) {
					if(j==0){
						if("".equals(obj.get("versionSubsidy").toString())){
							ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
						}else{
							ExportExcelUtil.setCellValueAndStyle(cell, obj.get("versionSubsidy").toString()+"~", textStyle);
						}
					}else{
						ExportExcelUtil.setCellValueAndStyle(cell, obj.get("subsidy").toString()+"~", textStyle);
					}
				} else {
					ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
				}
			}
			/*//写入本品标识
			cell = row.createCell(40);
			ExportExcelUtil.setCellValueAndStyle(cell, gridObj.getString("isBase"), thousandthStyle);*/
		}
			
		sheet.setDisplayGridlines(false);
	}

	/**
	 * 导出原始数据
	 * 
	 * @param wb
	 * @param s
	 * @param paramsMap
	 */
	public void exportObjectOriginalData(Workbook wb, Sheet s, Map<String, Object> paramsMap) {
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		CellStyle textPercentStyle = ExportExcelUtil.getExcelFillPercentageStyle(wb);//百分号格式化
		CellStyle textPercentRedStyle = ExportExcelUtil.getExcelFillPercentageRedStyle(wb);//红色
		CellStyle thousandthREDStyle = ExportExcelUtil.getExcelFormatThousandthREDStyle(wb);
		//表格标题数组锁引，如果是城市利润需要添加城市列
		int index  = 0;
		
		putParam(paramsMap);
		List<VersionPromotionInfoEntity> list = null;
		String objectType = paramsMap.get("objectType").toString();
		if("0".equals(objectType)) {
			// 型号
			list = manfSalesDao.exportManfSalesDataByVersion(paramsMap);
		} else {
			// 车型，厂商品牌,品牌,系别,级别
			list = manfSalesDao.exportManfSalesData(paramsMap);
		}
		Row row;
		int rowIndex =0;//行号锁引
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
//			String c9 = null;
//			if(obj.getC9() == null) {
//				c9 = "-";
//			} else { 
//				c9 = obj.getC9();
//			}
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
			//全款购车促销支持
			String fullyPaid =obj.getFullyPaid() == null ? "-" : obj.getFullyPaid();
			
			String grossSupports = null;
			if(c2 == "-" && c3 == "-") {
				grossSupports = "-";
			} else {
				grossSupports = obj.getGrossSupports();//经销商支持
			}
			String customerIncentive = obj.getCustomerIncentive();//用户激励
//			String presents = null;
//			if(obj.getPresents() == null) {
//				presents = "-";
//			} else {
//				presents = obj.getPresents();//礼品
//			}
//					
//			String insurance = null;
//			if(obj.getInsurance() == null) {
//				insurance = "-";
//			} else {
//				insurance = obj.getInsurance();//保险
//			}
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
			String orig=obj.getOrigName();
			if("EN".equals(languageType)) {
				manfName = obj.getManfNameEn();
				gradeName = obj.getGradeNameEn();
				subModelName = obj.getSubmodelNameEn();
				bodyType = obj.getBodyTypeEn();
				brandName = obj.getBrandNameEn();
				versionName = obj.getVersionNameEn();//型号全称英文
				orig=obj.getOrigNameEn();
			} 
			//重置锁引，用于创建单元格列锁引位置
			Cell cell = null;
			index = 0;
			//rowIndex++;
			++rowIndex;
			row  = ExportExcelUtil.createRow(s, rowIndex, 400);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, yearMonth + "~", textStyle);
				
			//"年月","型号编码","生产厂","系别","级别","型号标识","车型","拍档方式","排量","车身形式","上市时间","年式"			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  manfName + "~", textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, orig, textStyle);
				
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
			ExportExcelUtil.setCellValueAndStyle(cell, fullyPaid, thousandthStyle);
			
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
			ExportExcelUtil.setCellValueAndStyles(cell, profitRate , textPercentStyle, textPercentRedStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionLastMonthSales, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionSale, thousandthStyle);
		}
		s.setDisplayGridlines(false);
	}

	/**
	 * 导出型号、车型、厂商、品牌、系别、级别图形数据
	 * 
	 * @param wb
	 * @param s
	 * @param chartObj
	 * @param paramsMap
	 */
	public void exportObject1ChartData(Workbook wb, Sheet s,JSONObject chartObj, Map<String, Object> paramsMap) 
	{
		int rowIndex = 0;// 行号锁引
		String priceType = paramsMap.get("priceType").toString();
		Row row = ExportExcelUtil.createRow(s, rowIndex, 600);
		JSONArray xTitles = chartObj.getJSONArray("xTitle");
		JSONArray subxTitle = chartObj.getJSONArray("subxTitle");
		JSONArray titles =  chartObj.getJSONArray("titles");
		JSONArray series =  chartObj.getJSONArray("series");
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);// 表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);// 内容文本样式
		int index = 0;
		Cell cell = null;
		cell = row.createCell(index++);
		//对象
		ExportExcelUtil.setCellValueAndStyle(cell, "对象", titleStyle);
		for(int i = 0; i < xTitles.size(); i++) {
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, "", textStyle);
		}
		
		if(titles.size() < 7) {
			s.addMergedRegion(new CellRangeAddress((short) 0, (short) 0, (short) 1, (short)xTitles.size()));
		} else {
			for(int j = 0; j < subxTitle.size(); j++) {
				int cosNum = xTitles.size() / subxTitle.size();
				if(j == 0) {
					s.addMergedRegion(new CellRangeAddress((short) 0, (short) 0, (short) 1, (short)cosNum));
					row.getCell(1).setCellValue(subxTitle.getString(0));
				} else {
					s.addMergedRegion(new CellRangeAddress((short) 0, (short) 0, (short) (cosNum * j + 1), (short)(cosNum* (j + 1))));
					row.getCell(cosNum * j + 1).setCellValue(subxTitle.getString(j));
				}
			}
		}
		
		index = 0;
		rowIndex++;
		row = ExportExcelUtil.createRow(s, rowIndex, 600);
		cell = row.createCell(index++);
		ExportExcelUtil.setCellValueAndStyle(cell, "指标", titleStyle);
		
		//横坐标
		for(int i = 0; i < xTitles.size(); i++) {
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, xTitles.getString(i), textStyle);
		}
		for(int i = 0; i < titles.size(); i++) {
			index = 1;
			rowIndex++;
			row = ExportExcelUtil.createRow(s, rowIndex, 600);
			
			//指标
			cell = row.createCell(0);
			ExportExcelUtil.setCellValueAndStyle(cell, titles.getString(i), textStyle);
			
			//指标所对应横坐标值
			JSONObject obj = series.getJSONObject(i);
			JSONArray datas = obj.getJSONArray("data");
			for(int j = 0; j < datas.size(); j++) {
				cell = row.createCell(index++);
				if("-".equals(datas.getString(j)) || "".equals(datas.getString(j))) {
					ExportExcelUtil.setCellValueAndStyle(cell, "", textStyle);
				} else if("2".equals(priceType)) {
					ExportExcelUtil.setCellValueAndStyle(cell, (datas.getString(j) + "%"), textStyle);
				} else {
					ExportExcelUtil.setCellValueAndStyle(cell, datas.getString(j), textStyle);
				}
			}
		}
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
		if ("EN".equals(languageType)) {
			value = obj.getString(property + "En");
		} else {
			value = obj.getString(property);
		}
		return value;
	}
	
	/**
	 * 设置促销数据的分类信息
	 * 
	 * @param paramsMap
	 */
	private void  putParam(Map<String, Object> paramsMap) 
	{
		String  dataUserName = "";
		String  dataUserNameKey = "dataUserName";
		// 促销总额
		String promotionType1 = "5";
		// 提车支持
		String promotionType2 = "6";
		// 零售支持
		String promotionType3 = "7";
		// 人员奖励
		String promotionType4 = "8";
		// 金融贷款
		String promotionType5 = "9";
		// 置换支持
		String promotionType6 = "10";
		// 赠送保险
		String promotionType7 = "11";
		// 赠送礼品（油卡、保养）
		String promotionType8 = "12";
		// 选择促销
		String promotionType9 = "13";
		String  paramModuleCode = "promotionPolicy";
		paramsMap.put(dataUserNameKey, dataUserName);
		paramsMap.put("paramModuleCode", paramModuleCode);
		paramsMap.put("promotionType1", promotionType1);
		paramsMap.put("promotionType2", promotionType2);
		paramsMap.put("promotionType3", promotionType3);
		paramsMap.put("promotionType4", promotionType4);
		paramsMap.put("promotionType5", promotionType5);
		paramsMap.put("promotionType6", promotionType6);
		paramsMap.put("promotionType7", promotionType7);
		paramsMap.put("promotionType8", promotionType8);
		paramsMap.put("promotionType9", promotionType9);
	}
}
