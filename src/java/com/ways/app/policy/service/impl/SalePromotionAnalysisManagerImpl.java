package com.ways.app.policy.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.ways.app.framework.utils.EchartsUtil2;
import com.ways.app.framework.utils.ExportExcelUtil;
import com.ways.app.policy.dao.ISalePromotionAnalysisDao;
import com.ways.app.policy.model.SalePromotionAnalysisEntity;
import com.ways.app.policy.service.ISalePromotionAnalysisManager;

/**
 * 销量促销分析Service层实现类
 * @author songguobiao
 *
 */
@Service("SalePromotionAnalysisManagerImpl")
public class SalePromotionAnalysisManagerImpl implements ISalePromotionAnalysisManager {
	
	@Autowired
	ISalePromotionAnalysisDao salePromotionAnalysisDao;
	
	/**
	 * 初始化时间
	 * 
	 * @param request
	 * @param paramsMap
	 */
	public void initDate(HttpServletRequest request, Map<String, Object> paramsMap) 
    {
		List<Map<String, String>> list = salePromotionAnalysisDao.initDate(paramsMap);
		if(null != list && list.size() > 0) {
			Map<String, String> resultMap = list.get(0);
			String endDate = resultMap.get("ENDDATE");
			String beginDate = resultMap.get("BEGINDATE");
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			//默认开始时间，时间规则是相差12个点
			String defaultBeginDate = (Integer.parseInt(endDate.replace("-", "")) - 99) + "";
			//如果结束时间减一年小于开始时间，则默认为开始时间
			if( Integer.parseInt(defaultBeginDate) < Integer.parseInt(beginDate.replace("-", ""))) {
				request.setAttribute("defaultBeginDate", beginDate);
			} else {
				if(Integer.parseInt(defaultBeginDate.substring(4)) > 12) {
					defaultBeginDate = (Integer.parseInt(defaultBeginDate.substring(0, 4)) + 1) + "-01";
				} else {
					defaultBeginDate = defaultBeginDate.substring(0, 4) + "-" + defaultBeginDate.substring(4);
				}
				request.setAttribute("defaultBeginDate", defaultBeginDate );
			}
		}
    }
	
	/**
	 * 获取车型下的型号数据
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@Override
	public void getVersionModalByCommon(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		try {
			//保存子车型下型号数据
			request.setAttribute("versionList", salePromotionAnalysisDao.getVersionModalByCommon(paramsMap));
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 获取一汽大众常用对象组
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@Override
	public void getAutoCustomGroup(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("autoCustomGroupList", salePromotionAnalysisDao.getAutoCustomGroup(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取初始化子车型弹出框控件值
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@Override
	public void getSubmodelModal(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		try {
			handleOneMonthParams(paramsMap);
			//返回细分市场页
	    	if("2".equals(paramsMap.get("subModelShowType"))) {
	    		request.setAttribute("segmentList", salePromotionAnalysisDao.getSubmodelBySegment(paramsMap));
	    		//返回品牌页
	    	} else if("3".equals(paramsMap.get("subModelShowType"))) {
	    		request.setAttribute("brandLetterList", salePromotionAnalysisDao.getSubmodelByBrand(paramsMap));
	    		//返回厂商页
	    	} else if("4".equals(paramsMap.get("subModelShowType"))) {
	    		request.setAttribute("manfLetterList", salePromotionAnalysisDao.getSubmodelByManf(paramsMap));
	    		//本品页竟品页
	    	} else {
	    		request.setAttribute("bpSubModelList", salePromotionAnalysisDao.getSubmodelByBp(paramsMap));
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取厂商
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@Override
	public void getManf(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			handleOneMonthParams(paramsMap);
			request.setAttribute("manfLetterList", salePromotionAnalysisDao.getManf(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取品牌
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@Override
	public void getBrand(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			handleOneMonthParams(paramsMap);
			request.setAttribute("brandLetterList", salePromotionAnalysisDao.getBrand(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取系别
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@Override
	public void getOrig(HttpServletRequest request, Map<String, Object> paramsMap) {
		try {
			request.setAttribute("origList", salePromotionAnalysisDao.getOrig(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取车身形式
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@Override
	public void getBodyType(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("bodyTypeList", salePromotionAnalysisDao.getBodyType(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取细分市场以及所属子细分市场
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@Override
	public void getSegmentAndChildren(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		try {
			handleOneMonthParams(paramsMap);
			request.setAttribute("segmentList", salePromotionAnalysisDao.getSegmentAndChildren(paramsMap));
		} catch (Exception e) {

		}
	}
	
    /**
	 * 加载销量促销数据
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public String loadChartAndTable(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		String objectType = paramsMap.get("objectType").toString();
		List<SalePromotionAnalysisEntity> list = null;
		//型号维度
		if("1".equals(objectType)) {
			list = salePromotionAnalysisDao.loadSalePromotionAnalysisByVersion(paramsMap);
		//其他维度
		} else {
			list = salePromotionAnalysisDao.loadSalePromotionAnalysisByOther(paramsMap);
		}
		if(null != list && list.size() > 0) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap = getChartDataToObject(list, paramsMap);
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			request.getSession().setAttribute(Constant.getSalePromotionAnalysisExcelDataKey, json);
		}
		return json;
	}
	
    /**
	 *  将图形数据装入Map中
	 * 
	 * @param list
	 * @param paramsMap
	 * @return    
	 */
	private Map<String, Object> getChartDataToObject(List<SalePromotionAnalysisEntity> list, Map<String, Object> paramsMap)
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> xTitles = new ArrayList<String>();
		String titles = "";
	    //获取X轴标题
		for(SalePromotionAnalysisEntity sp : list) {
			if(!titles.equals(sp.getYm())) {
				xTitles.add(sp.getYm());
				titles = sp.getYm();
			}
		}
		//获取对象名称总数
		Set<String> objSaleNames = new HashSet<String>();
		Set<String> objPromotionNames = new HashSet<String>();
		Set<String> objNames = new HashSet<String>();
		Set<String> objNameEns = new HashSet<String>();
		List<String> saleNames = new ArrayList<String>();
		List<String> promotionNames = new ArrayList<String>();
		List<String> names = new ArrayList<String>();
		List<String> nameEns = new ArrayList<String>();
		int nameSize = 0;
		for(SalePromotionAnalysisEntity sp : list) {
			nameSize = objSaleNames.size();
			objSaleNames.add(sp.getObjName() + "销量");
			objPromotionNames.add(sp.getObjName() + "促销");
			objNames.add(sp.getObjName());
			objNameEns.add(sp.getObjNameEn());
			if(objSaleNames.size() > nameSize) {
				saleNames.add(sp.getObjName() + "销量");
				promotionNames.add(sp.getObjName() + "促销");
				names.add(sp.getObjName());
				nameEns.add(sp.getObjNameEn());
			}
		}
		List<List<String>> saleData = new ArrayList<List<String>>();
		for(String objectName : names) {
			List<String> data = new ArrayList<String>();
			for(SalePromotionAnalysisEntity sp : list) {
				if(objectName.equals(sp.getObjName())) {
					data.add(sp.getVersionSale());
				}
			}
			saleData.add(data);
		}
		List<List<String>> promotionData = new ArrayList<List<String>>();
		for(String objectName : names) {
			List<String> data = new ArrayList<String>();
			for(SalePromotionAnalysisEntity sp : list) {
				if(objectName.equals(sp.getObjName())) {
					data.add(sp.getTotalPromotion());
				}
			}
			promotionData.add(data);
		}
		List<String[]> tickList = new ArrayList<String[]>();
		String[] saleTicks = getSaleTicks(saleData);
		String[] promotionTicks = getPromotionTicks(promotionData);
		tickList.add(saleTicks);
		tickList.add(promotionTicks);
		dataMap.put("xTitles", xTitles);
		dataMap.put("saleData", saleData);
		dataMap.put("promotionData", promotionData);
		dataMap.put("saleNames", saleNames);
		dataMap.put("promotionNames", promotionNames);
		dataMap.put("objNames", names);
		dataMap.put("objNameEns", nameEns);
		dataMap.put("resultList", list);
		dataMap.put("tickList", tickList);
		return dataMap;
	}
	
	/**
	 * 获取促销刻度数组
	 * 
	 * @param promotionData
	 * @return
	 */
	private String[] getPromotionTicks(List<List<String>> promotionData) 
	{
		int[] promotionMaxAndMin = new int[2];
		List<Integer> dataList = new ArrayList<Integer>();
		for(List<String> sd : promotionData) {
			for(String s : sd) {
				if(null != s && !"-".equals(s)) {
					dataList.add(Integer.parseInt(s));
				}
			}
		}
		if(dataList.size() > 0) {
			int[] data = new int[dataList.size()];
			for(int i = 0; i < dataList.size(); i++) {
				data[i] = dataList.get(i);
			}
			//利用冒泡算法得出最大值最小值
			for(int i = 0; i < data.length; i++) {
				for(int j = i; j < data.length; j++) {
					int temp = 0;
					if(data[j] > data[i]) {
						temp = data[i];
						data[i] = data[j];
						data[j] = temp;
					}
				}
			}
//			//最大值和最小值
			promotionMaxAndMin[0] = data[0];
			promotionMaxAndMin[1] = (int)(data[data.length - 1] * -8);
		}
		String[] promotionTicks = EchartsUtil2.setLineScaleDivision(promotionMaxAndMin, "1");
		return promotionTicks;
	}
	
	/**
	 * 获取销量刻度数组
	 * 
	 * @param saleData
	 * @return
	 */
	private String[] getSaleTicks(List<List<String>> saleData) 
	{
		//刻度数组
		int[] saleMaxAndMin = new int[2];
		List<Integer> dataList = new ArrayList<Integer>();
		for(List<String> pd : saleData) {
			for(String p : pd) {
				if(null != p && !"-".equals(p)) {
					dataList.add(Integer.parseInt(p));
				}
			}
		}
		if(dataList.size() > 0) {
			int[] data = new int[dataList.size()];
			for(int i = 0; i < dataList.size(); i++) {
				data[i] = dataList.get(i);
			}
			//利用冒泡算法得出最大值最小值
			for(int i = 0; i < data.length; i++) {
				for(int j = i; j < data.length; j++) {
					int temp = 0;
					if(data[j] > data[i]) {
						temp = data[i];
						data[i] = data[j];
						data[j] = temp;
					}
				}
			}
			//最大值和最小值的差值
			saleMaxAndMin[0] = data[0] * 4;
			saleMaxAndMin[1] = data[data.length - 1];
		}
		String[] saleTicks = EchartsUtil2.setLineScaleDivision(saleMaxAndMin, "0");
		return saleTicks;
	}
	
	/**
	 * 导出
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public Workbook exportExcel(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		Workbook wb = null;
		String json = (String) request.getSession().getAttribute(Constant.getSalePromotionAnalysisExcelDataKey);
		if(!AppFrameworkUtil.isEmpty(json)) {
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			try {
				String path = request.getSession().getServletContext().getRealPath("/"); 
				wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/salePromotionAnalysisTemplate.xls")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			exportObjectOriginalData(wb, wb.getSheet("原数据"), obj, paramsMap);
			exportObjectChartData(wb, wb.getSheet("DATA"), obj, paramsMap);
		}
		return wb;
	}
	
	/**
	 * 导出图形数据
	 * 
	 * @param wb
	 * @param sheet
	 * @param chartObj
	 * @param paramsMap
	 */
	public void exportObjectChartData(Workbook wb, Sheet sheet, JSONObject chartObj, Map<String, Object> paramsMap)
	{
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		String objectType = (String) paramsMap.get("objectType");//对象维度类型
		String[] titlesCn = new String[]{"日期", "对象名称", "促销", "销量"};
		String[] titlesEn = new String[]{"Date", "ObjectName", "Promotion", "Sale"};
		if("1".equals(objectType)) {
			titlesCn[1] = "型号全称";
			titlesEn[1] = "VersionName";
		} else if("2".equals(objectType)) {
			titlesCn[1] = "车型名称";
			titlesEn[1] = "SubmodelName";
		} else if("3".equals(objectType)) {
			titlesCn[1] = "厂商品牌";
			titlesEn[1] = "ManfName";
		} else if("4".equals(objectType)) {
			titlesCn[1] = "品牌";
			titlesEn[1] = "BrandName";
		} else if ("5".equals(objectType)) {
			titlesCn[1] = "系别名称";
			titlesEn[1] = "OrigName";
		} else {
			titlesCn[1] = "级别名称";
			titlesEn[1] = "SegmentName";
		}
		//刻度集合
		JSONArray saleTicks = chartObj.getJSONArray("tickList").getJSONArray(0);
		JSONArray promotionTicks = chartObj.getJSONArray("tickList").getJSONArray(1);
		StringBuilder promotionTick = new StringBuilder();
		StringBuilder saleTick = new StringBuilder();
		//刻度数值间距  ---(最大值-最小值) / 切割数量
		int saleStep = (Integer.parseInt(saleTicks.get(0).toString()) - Integer.parseInt(saleTicks.get(1).toString())) / Integer.parseInt(saleTicks.get(2).toString());
		saleTick.append(saleTicks.get(0)).append(",").append(saleTicks.get(1)).append(",").append(saleStep);
		int promotionStep = (Integer.parseInt(promotionTicks.get(0).toString()) - Integer.parseInt(promotionTicks.get(1).toString())) / Integer.parseInt(promotionTicks.get(2).toString());
		promotionTick.append(promotionTicks.get(0)).append(",").append(promotionTicks.get(1)).append(",").append(promotionStep);
		String[] colors = {"0,35,90", "174,212,248", "187,194,197", "226,103,0", "0,156,14", "137,148,160", "98,197,226", "249,167,0", "74,111,138",
                "187,198,0", "146,10,106", "230,49,16", "0,93,91", "250,206,0", "119,0,29"};
		StringBuilder colorRGB = new StringBuilder();
		for(String color : colors) {
			colorRGB.append(color).append("|");
		}
		int rowIndex = 0;//行号锁引
		Row row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
		
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		
		//写入线图刻度最大值与最小值
		Cell cell = row.createCell(40);
		ExportExcelUtil.setCellValueAndStyle(cell, promotionTick.toString(), textStyle);
		cell = row.createCell(41);
		ExportExcelUtil.setCellValueAndStyle(cell, saleTick.toString(), textStyle);
		//写入颜色代码
		cell = row.createCell(42);
		ExportExcelUtil.setCellValueAndStyle(cell, colorRGB.toString().substring(0, colorRGB.length() - 1), textStyle);
		//写入对象名称
		cell = row.createCell(43);
		if(!"EN".equals(languageType)) {
			ExportExcelUtil.setCellValueAndStyle(cell, chartObj.getString("objNames"), textStyle);
		} else {
			ExportExcelUtil.setCellValueAndStyle(cell, chartObj.getString("objNameEns"), textStyle);
		}
		
		//数据集合
		JSONArray resultList =  chartObj.getJSONArray("resultList");
		//写表格标题
		String[] titles = new String[7];
		if(!"EN".equals(languageType)) {
			titles = titlesCn;
		} else {
			titles = titlesEn;
		}
		for(int i = 0; i < titles.length; i++) {
			cell = row.createCell(i);
			ExportExcelUtil.setCellValueAndStyle(cell, titles[i], titleStyle);
			sheet.setColumnWidth(i, 6000);
		}
		
		//写数据
		for(int j = 0; j < resultList.size(); j++) {
			JSONObject se = (JSONObject)resultList.get(j);
			rowIndex++;
			row = ExportExcelUtil.createRow(sheet, rowIndex, 400);
			int count = 0;
			cell = row.createCell(count++);
			ExportExcelUtil.setCellValueAndStyle(cell, se.getString("ym"), textStyle);
			
			cell = row.createCell(count++);
			if(!"EN".equals(languageType)) {
				ExportExcelUtil.setCellValueAndStyle(cell, se.getString("objName"), textStyle);
			} else {
				ExportExcelUtil.setCellValueAndStyle(cell, se.getString("objNameEn"), textStyle);
			}
			
			cell = row.createCell(count++);
			ExportExcelUtil.setCellValueAndStyle(cell, se.getString("totalPromotion") == null ? "-" : se.getString("totalPromotion"), thousandthStyle);
			
			cell = row.createCell(count++);
			ExportExcelUtil.setCellValueAndStyle(cell, se.getString("versionSale") == null ? "-" : se.getString("versionSale"), thousandthStyle);
		}
		sheet.setDisplayGridlines(false);
	}
	
	/**
	 * 导出原始数据
	 * 
	 * @param wb
	 * @param sheet
	 * @param jsonObj
	 * @param paramsMap
	 */
	public void exportObjectOriginalData(Workbook wb, Sheet sheet, JSONObject jsonObj, Map<String, Object> paramsMap)
	{
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		JSONArray gridObj = null;
		List<SalePromotionAnalysisEntity> list = null;
		//对象维度不是型号时导出需要重新查询原始数据
		if(!"1".equals(paramsMap.get("objectType"))) {
			list = salePromotionAnalysisDao.exportExcelByOther(paramsMap);
		} else {
			list = salePromotionAnalysisDao.exportExcelByVersion(paramsMap);
		}
		String json = "";
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("resultList", list);
		json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
		JSONObject jsonObject = JSONObject.parseObject(json);
		gridObj = jsonObject.getJSONArray("resultList");
		
		int rowIndex = 0;
		Row row = null;
		Cell cell = null;
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		CellStyle textPercentStyle = ExportExcelUtil.getExcelFillPercentageStyle(wb);//百分号格式化
		CellStyle textPercentRedStyle = ExportExcelUtil.getExcelFillPercentageRedStyle(wb);//红色
		CellStyle thousandthREDStyle =ExportExcelUtil.getExcelFormatThousandthREDStyle(wb);//千分位红色
		
		int index = 0;
		//写表格数据内容
		for(int j = 0; j < gridObj.size(); j++) {
			JSONObject obj = gridObj.getJSONObject(j);
			//如果无记录则不导出
			if(AppFrameworkUtil.isEmpty(obj.getString("versionCode"))) {
				continue;
			}
			//重置锁引，用于创建单元格列锁引位置
			index = 0;
			rowIndex++;
			row = ExportExcelUtil.createRow(sheet, rowIndex, 400);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("ym") + "~", textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "manfName"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("segmentName"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "gradeName"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "bodyType"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "brandName"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "subModelName"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "versionName"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionCode") + "~", textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionLaunchDate"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("msrp"), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("rebateCash"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("ckReward"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("totalPromotion"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("fullyPaid"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("grossSupport"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("std"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("aak"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("customerIncentive"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("present"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("insurance"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("maintenance"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("personReward"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("financeLoan"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("displacesSupport"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("invoicePrice"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("grossCost"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("tp"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			if("-".equals(obj.getString("tp")) || "-".equals(obj.getString("grossCost"))) {
				ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
			} else {
				ExportExcelUtil.setCellValueAndStyles(cell, AppFrameworkUtil.getNum(obj.getString("profit"), 7), thousandthStyle, thousandthREDStyle);
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyles(cell, AppFrameworkUtil.getNum(obj.getString("profitRate"), 7), textPercentStyle, textPercentRedStyle);
			}
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("lastVersionSale"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("versionSale"), 7), thousandthStyle);
		}
		sheet.setDisplayGridlines(false);
	}
	
	/**
	 * 处理1月份特殊参数
	 * 
	 * @param paramsMap
	 */
	private void handleOneMonthParams(Map<String, Object> paramsMap)
	{
		String beginDate = (String)paramsMap.get("beginDate");
		String endDate = (String)paramsMap.get("endDate");
		boolean f = beginDate.contains("-");
		if(!AppFrameworkUtil.isEmpty(beginDate)) {
			beginDate = beginDate.replace("-", "");
		}
		if(!AppFrameworkUtil.isEmpty(endDate)) {
			endDate = endDate.replace("-", "");
		}
		//如果开始月等于结束月，且是1月份时
		if(beginDate.equals(endDate) && "01".equals(beginDate.substring(4))) {
			int year = Integer.parseInt(beginDate.substring(0,4)) - 1;
			if(f) {
				beginDate = year + "-12";
			} else {
				beginDate = year + "12";
			}
			paramsMap.put("beginDate", beginDate);
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
	private String getValueByLanguageType(JSONObject obj, String languageType, String property)
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
