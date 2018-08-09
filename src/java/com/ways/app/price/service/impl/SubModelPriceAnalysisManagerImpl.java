package com.ways.app.price.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
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
import com.ways.app.price.dao.ISubModelPriceAnalysisDao;
import com.ways.app.price.model.NumberTickUnitFormat;
import com.ways.app.price.model.SubModelPriceAnalysis;
import com.ways.app.price.service.ISubModelPriceAnalysisManager;

/**
 * 车型价格段分析Service层接口实现类
 * 
 * @author songguobiao
 * @date 20161107
 *
 */
@Service("SubModelPriceAnalysisManagerImpl")
public class SubModelPriceAnalysisManagerImpl implements ISubModelPriceAnalysisManager {

	@Autowired
	private ISubModelPriceAnalysisDao subModelPriceAnalysisDao;
	/**
	 * 初始化时间
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public String initDate(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String beginDate = "";
		String endDate = "";
		String defaultBeginDate = "";
		List<Map<String, String>> list = (List<Map<String, String>>) subModelPriceAnalysisDao.initDate(paramsMap);
		if(null != list && list.size() > 0) {
			beginDate = list.get(0).get("BEGINDATE");
			endDate = list.get(0).get("ENDDATE");
			defaultBeginDate = endDate;
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("defaultBeginDate", defaultBeginDate);
		}
		if("1".equals(paramsMap.get("changeDate"))) {
			String json = "";
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("beginDate", beginDate);
			dataMap.put("endDate", endDate);
			dataMap.put("defaultBeginDate", defaultBeginDate);
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			return json;
		} else {
			return null;
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
			//返回细分市场页
	    	if("2".equals(paramsMap.get("subModelShowType"))) {
	    		request.setAttribute("segmentList", subModelPriceAnalysisDao.getSubmodelBySegment(paramsMap));
	    		//返回品牌页
	    	} else if("3".equals(paramsMap.get("subModelShowType"))) {
	    		request.setAttribute("brandLetterList", subModelPriceAnalysisDao.getSubmodelByBrand(paramsMap));
	    		//返回厂商页
	    	} else if("4".equals(paramsMap.get("subModelShowType"))) {
	    		request.setAttribute("manfLetterList", subModelPriceAnalysisDao.getSubmodelByManf(paramsMap));
	    		//本品页竟品页
	    	} else {
	    		request.setAttribute("bpSubModelList", subModelPriceAnalysisDao.getSubmodelByBp(paramsMap));
	    	}
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
			request.setAttribute("bodyTypeList", subModelPriceAnalysisDao.getBodyType(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取分析数据
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public String getAnalysisData(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		List<SubModelPriceAnalysis> list = null;
		//拼接sql
				String sql = "";

				for(int i = 0; i < paramsMap.get("subModelIds").toString().split(",").length;i++){
					sql +=" select "+paramsMap.get("subModelIds").toString().split(",")[i]+" objid,"+i+" objsn from dual ";
					if(i != paramsMap.get("subModelIds").toString().split(",").length-1){
						sql +=" union all ";
					}
				}
		paramsMap.put("sql", sql);
		list = subModelPriceAnalysisDao.getAnalysisData(paramsMap);
		if(null != list && list.size() > 0) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("series", list);
			List<List<String>> titles = getXTitles(list);
			dataMap.put("xTitles", titles.get(0));
			dataMap.put("xTitleEns", titles.get(1));
			dataMap.put("xTitleIds", titles.get(2));
			Integer minPrice = getMinAndMaxPrice(list)[0];
			Integer maxPrice = getMinAndMaxPrice(list)[1];
			//为保持跟3.0一致，当选择指导价成交价同时选择时坐标最小值为0；
			if(paramsMap.get("priceType").equals("3")){
				minPrice = 0;
			}
			dataMap.put("yAxis", getInterval(minPrice, maxPrice));
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			request.getSession().setAttribute(Constant.getSubModelPriceAnalysisDataKey, json);
		}
		return json;
	}
	
	/**
	 * 获取X轴标题
	 * 
	 * @param list
	 * @return
	 */
	private List<List<String>> getXTitles(List<SubModelPriceAnalysis> list) 
	{
		List<List<String>> titles = new ArrayList<List<String>>();
		List<String> xTitles = new ArrayList<String>();
		List<String> xTitlesEn = new ArrayList<String>();
		List<String> xTitlesID = new ArrayList<String>();
		String subModelName = "";
		String subModelNameEn = "";
		String subModelId = "";
		for(int i = 0; i < list.size(); i++) {
			subModelName = list.get(i).getSubModelName();
			subModelNameEn = list.get(i).getSubModelNameEn();
			subModelId = list.get(i).getSubModelId();
			xTitles.add(subModelName);
			xTitlesEn.add(subModelNameEn);
			xTitlesID.add(subModelId);
		}
		titles.add(xTitles);
		titles.add(xTitlesEn);
		titles.add(xTitlesID);
		return titles;
	}
	
	/**
	 * 获取最小和最大价格
	 * 
	 * @param list
	 * @return
	 */
	private Integer[] getMinAndMaxPrice(List<SubModelPriceAnalysis> list) 
	{
		Integer[] resultList = new Integer[2];
		Integer max = 0;
		Integer min = 10000000;
		for(int i = 0; i < list.size(); i++) {
			SubModelPriceAnalysis spa = list.get(i);
			if(null != spa.getMaxMsrp() && !"".equals(spa.getMaxMsrp())) {
				if(Integer.parseInt(spa.getMaxMsrp()) > max) {
					max = Integer.parseInt(spa.getMaxMsrp());
				}
				if(Integer.parseInt(spa.getMinMsrp()) < min) {
					min = Integer.parseInt(spa.getMinMsrp());
				}
			}
			if(null != spa.getMaxTp() && !"".equals(spa.getMaxTp())) {
				if(Integer.parseInt(spa.getMaxTp()) > max) {
					max = Integer.parseInt(spa.getMaxTp());
				}
				if(Integer.parseInt(spa.getMinTp()) < min) {
					min = Integer.parseInt(spa.getMinTp());
				}
			}
			if(null != spa.getMaxPrice() && !"".equals(spa.getMaxPrice())) {
				if(Integer.parseInt(spa.getMaxPrice()) > max) {
					max = Integer.parseInt(spa.getMaxPrice());
				}
				if(Integer.parseInt(spa.getMinPrice()) < min) {
					min = Integer.parseInt(spa.getMinPrice());
				}
			}
		}
		resultList[0] = min;
		resultList[1] = max;
		return resultList;
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
		HSSFWorkbook wb = null;
		String json = (String) request.getSession().getAttribute(Constant.getSubModelPriceAnalysisDataKey);
		if(!AppFrameworkUtil.isEmpty(json)) {
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			try {
				String path = request.getSession().getServletContext().getRealPath("/"); 
				String templateFileName = "";
				if("1".equals(paramsMap.get("chartType").toString())) {
					templateFileName = "subModelPriceAnalysisScatterTemplate.xls";
				} else {
					if("3".equals(paramsMap.get("priceType").toString())) {
						templateFileName = "subModelPriceAnalysisSpanTemplate.xls";
					} else {
						templateFileName = "subModelPriceAnalysisPriceSpanTemplate.xls";
					}
				}
				wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/" + templateFileName)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			exportObjectOriginalData(wb, wb.getSheet("数据显示"), obj, paramsMap);
			exportObjectChartData(wb, wb.getSheet("data"), obj, paramsMap);
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
	private void exportObjectChartData(HSSFWorkbook wb, Sheet sheet, JSONObject chartObj, Map<String, Object> paramsMap)
	{
		String languageType = paramsMap.get("languageType").toString();//语言类型ZH,EN
		String chartType = paramsMap.get("chartType").toString();//图表类型
		String priceType = paramsMap.get("priceType").toString();//价格类型
		String[] titlesCn = null;
		String[] titlesEn = null;
		if("1".equals(chartType)) {
			titlesCn = new String[] {"车型", "型号标识", "序号", ""};
			titlesEn =  new String[] {"Model", "TrimName", "Index", ""};
			if("1".equals(priceType)) {
				titlesCn[3] = "指导价";
				titlesEn[3] = "MSRP";
			} else {
				titlesCn[3] = "成交价";
				titlesEn[3] = "TP";
			}
		} else {
			titlesCn = new String[] {"品牌", "厂商", "级别", "车型", "", "MIN", "MAX", "跨度"};
			titlesEn = new String[] {"Brand", "OEM", "Segment", "Model", "", "MIN", "MAX", "Variant"};
			if("1".equals(priceType)) {
				titlesCn[4] = "指导价";
				titlesEn[4] = "MSRP";
			} else if("2".equals(priceType)) {
				titlesCn[4] = "成交价";
				titlesEn[4] = "TP";
			} else {
				titlesCn = new String[] {"级别", "品牌", "厂商", "车型", "指导价", "MIN", "MAX", "跨度", "成交价", "MIN", "MAX", "跨度"};
				titlesEn = new String[] {"Brand", "OEM", "Segment", "Model", "MSRP", "MIN", "MAX", "Variant", "TP", "MIN", "MAX", "Variant"};
			}
		}
		JSONArray resultList =  chartObj.getJSONArray("series");
		int rowIndex = 0;//行号锁引
		Row row = sheet.getRow(0);
		
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle textStyle1 = ExportExcelUtil.getExcelFillTextStyle1(wb);//内容文本样式
		CellStyle titleStyle1 = getExcelTitleBackgroundStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		
		//写表格标题
		String[] titles = null;
		if(!"EN".equals(languageType)) {
			titles = titlesCn;
		} else {
			titles = titlesEn;
		}
		Cell cell = null;
		if("1".equals(chartType)) {
			Cell chartCell = row.createCell(40);
			//写入手动排挡显示类型
			ExportExcelUtil.setCellValueAndStyle(chartCell, paramsMap.get("showType").toString(), textStyle);
			for(int i = 0; i < titles.length; i++) {
				cell = row.getCell(i);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[i], titleStyle);
				sheet.setColumnWidth(i, 5000);
			}
		} else {
			//先写品牌,厂商,级别,车型
			for(int i = 0; i < 4; i++) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
				cell = row.createCell(i);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[i], titleStyle);
			}
			if(!"3".equals(priceType)) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));
				cell = row.createCell(4);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[4], titleStyle1);
				
				row = sheet.getRow(1);
				
				cell = row.createCell(4);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[5], titleStyle);
				
				cell = row.createCell(5);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[6], titleStyle);
				
				cell = row.createCell(6);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[7], titleStyle);
			} else {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));
				cell = row.createCell(4);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[4], titleStyle1);
				
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 9));
				cell = row.createCell(7);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[8], titleStyle1);
				
				row = sheet.getRow(1);
				
				cell = row.createCell(4);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[5], titleStyle);
				
				cell = row.createCell(5);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[6], titleStyle);
				
				cell = row.createCell(6);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[7], titleStyle);
				
				cell = row.createCell(7);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[9], titleStyle1);
				
				cell = row.createCell(8);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[10], titleStyle);
				
				cell = row.createCell(9);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[11], titleStyle);
			}
			//跨度图时需要从第三行开始写数据
			rowIndex++;
		}
		
		//写数据
		for(int j = 0; j < resultList.size(); j++) {
			if("1".equals(chartType)) {
				JSONArray list = resultList.getJSONObject(j).getJSONArray("list");
				String name = "";
				for(int i = 0; i < list.size(); i++) {
					JSONObject obj = list.getJSONObject(i);
					String subModelName = getValueByLanguageType(obj, languageType, "subModelName");
					if("".equals(name) || (!"".equals(name) && !subModelName.equals(name))) {
						name = subModelName;
					} else {
						subModelName = "";
					}
					rowIndex++;
					row = ExportExcelUtil.createRow(sheet, rowIndex, 400);
					int count = 0;
					cell = row.createCell(count++);
					if(!"".equals(subModelName)) {
						ExportExcelUtil.setCellValueAndStyle(cell, subModelName, textStyle1);
					}
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, getLabelNameByLanguageType(obj, paramsMap), textStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("sn") , textStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("price"), thousandthStyle);
					
				}
			} else {
				JSONObject subModelObj = resultList.getJSONObject(j);
				changeChartSheet(wb, paramsMap, resultList);
				if("3".equals(priceType)) {
					rowIndex++;
					row = ExportExcelUtil.createRow(sheet, rowIndex, 400);
					int count = 0;
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, subModelObj.getString("gradeNameEn"), textStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(subModelObj, languageType, "brandName"), textStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(subModelObj, languageType, "manfName"), textStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(subModelObj, languageType, "subModelName"), textStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, subModelObj.getString("minMsrp"), thousandthStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, subModelObj.getString("maxMsrp"), thousandthStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, String.valueOf(Integer.parseInt(subModelObj.getString("maxMsrp")) - Integer.parseInt(subModelObj.getString("minMsrp"))), thousandthStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, subModelObj.getString("minTp"), thousandthStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, subModelObj.getString("maxTp"), thousandthStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, String.valueOf(Integer.parseInt(subModelObj.getString("maxTp")) - Integer.parseInt(subModelObj.getString("minTp"))), thousandthStyle);
				} else {
					rowIndex++;
					row = ExportExcelUtil.createRow(sheet, rowIndex, 400);
					int count = 0;
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(subModelObj, languageType, "brandName"), textStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(subModelObj, languageType, "manfName"), textStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, subModelObj.getString("gradeNameEn"), textStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(subModelObj, languageType, "subModelName"), textStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, subModelObj.getString("minPrice"), thousandthStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, subModelObj.getString("maxPrice"), thousandthStyle);
					
					cell = row.createCell(count++);
					ExportExcelUtil.setCellValueAndStyle(cell, String.valueOf(Integer.parseInt(subModelObj.getString("maxPrice")) - Integer.parseInt(subModelObj.getString("minPrice"))), thousandthStyle);
				}
			}
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
	public void exportObjectOriginalData(HSSFWorkbook wb, Sheet sheet, JSONObject jsonObj, Map<String, Object> paramsMap)
	{
		//语言类型ZH,EN
		String languageType = paramsMap.get("languageType").toString();
		JSONArray gridObj = jsonObj.getJSONArray("series");
		//图表类型
		String chartType = paramsMap.get("chartType").toString();
		String priceType = paramsMap.get("priceType").toString();
		String[] titlesCn = null;
		String[] titlesEn = null;
		if("1".equals(chartType)) {
			titlesCn = new String[] {"型号编码", "厂商", "车型", "排量", "排挡方式", "型号标识", "车身形式", "上市日期", "指导价"};
			titlesEn = new String[] {"VersionCode", "OEM", "Model", "Transmission", "EngineCapacity", "TrimName", "BodyType", "LaunchDate", "MSRP"};
			if("2".equals(priceType)) {
				titlesCn[8] = "成交价";
				titlesEn[8] = "TP";
			}
		} else {
			if("1".equals(priceType)) {
				titlesCn = new String[] {"型号编码", "厂商", "车型",  "排量", "排挡方式", "型号标识", "车身形式", "上市日期", "指导价"};
				titlesEn = new String[] {"VersionCode", "OEM", "Model", "Transmission", "EngineCapacity", "TrimName", "BodyType", "LaunchDate", "MSRP"};
			} else if("2".equals(priceType)) {
				titlesCn = new String[] {"型号编码", "厂商", "车型", "排量", "排挡方式", "型号标识", "车身形式", "上市日期", "成交价"};
				titlesEn = new String[] {"VersionCode", "OEM", "Model", "Transmission", "EngineCapacity", "TrimName", "BodyType", "LaunchDate", "TP"};
			} else {
				titlesCn = new String[] {"型号编码", "厂商", "车型", "排量", "排挡方式", "型号标识", "车身形式", "上市日期", "指导价", "成交价"};
				titlesEn = new String[] {"VersionCode", "OEM", "Model", "Transmission", "EngineCapacity", "TrimName", "BodyType", "LaunchDate", "MSRP", "TP"};
			}
		}
		int rowIndex = 0;//行号锁引
		Row row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
		Cell cell = null;
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
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
			sheet.setColumnWidth(i, 5800);
		}
		int index = 0;
		//写表格数据内容
		for(int j = 0; j < gridObj.size(); j++) {
			JSONArray list = gridObj.getJSONObject(j).getJSONArray("list");
			for(int i = 0; i < list.size(); i++) {
				JSONObject obj = list.getJSONObject(i);
				//如果无记录则不导出
				if(AppFrameworkUtil.isEmpty(obj.getString("versionCode"))) {
					continue;
				}
				//重置锁引，用于创建单元格列锁引位置
				index = 0;
				rowIndex++;
				row = ExportExcelUtil.createRow(sheet, rowIndex, 400);
				
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionCode") + "~", textStyle);
				
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "manfName"), textStyle);
				
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "subModelName"), textStyle);
				
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("emissionsName"), textStyle);
				
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("transMission"), textStyle);
				
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "versionShortName"), textStyle);
				
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("bodyTypeNameEn"), textStyle);
				
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionLaunchDate"), textStyle);
				
				cell = row.createCell(index++);
				if(!"3".equals(priceType)) {
					ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("price"), thousandthStyle);
				} else {
					ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("msrp"), thousandthStyle);
					cell = row.createCell(index++);
					ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("tp"), thousandthStyle);
				}
			}
		}
		sheet.setDisplayGridlines(false);
	}
	
	/**
	 * 修改图表参数
	 * 
	 * @param workbook
	 * @param paramsMap
	 * @param gridObj
	 */
	private void changeChartSheet(HSSFWorkbook workbook, Map<String, Object> paramsMap, JSONArray resultList) 
	{
		HSSFSheet sheet = workbook.getSheet("图表显示");
		String priceType = paramsMap.get("priceType").toString();
		double minPrice = Double.MAX_VALUE;
		double maxPrice = Double.MIN_VALUE;
		for (int i = 0; i < resultList.size(); i++) {
			JSONObject subModel = resultList.getJSONObject(i);
			if(i == 0) {
				//选择双价格时
				if("3".equals(priceType)) {
					/*minPrice = Double.parseDouble(subModel.getString("minTp"));*/
					minPrice = 0;
					maxPrice = Double.parseDouble(subModel.getString("maxMsrp"));
				} else {
					minPrice = Double.parseDouble(subModel.getString("minPrice"));
					maxPrice = Double.parseDouble(subModel.getString("maxPrice"));
				}
			} else {
				//选择双价格时
				if("3".equals(priceType)) {
				/*	minPrice = Math.min(minPrice, Double.parseDouble(subModel.getString("minTp")));*/
					minPrice = 0;
					maxPrice = Math.max(maxPrice, Double.parseDouble(subModel.getString("maxMsrp")));
				} else {
					minPrice = Math.min(minPrice, Double.parseDouble(subModel.getString("minPrice")));
					maxPrice = Math.max(maxPrice, Double.parseDouble(subModel.getString("maxPrice")));
				}
			}
		}
		List<Integer> intervalList = getInterval((int)minPrice, (int)maxPrice);
		Row row = sheet.getRow(0);
		Cell cell = row.getCell(27);
		cell.setCellValue(intervalList.get(0));
		row = sheet.getRow(1);
		cell = row.getCell(27);
		cell.setCellValue(intervalList.get(1));
		row = sheet.getRow(2);
		cell = row.getCell(27);
		cell.setCellValue(intervalList.get(2));
		row = sheet.getRow(4);
		cell = row.getCell(27);
		cell.setCellValue(resultList.size());
	}
	
	/**
	 * 计算间隔
	 * 
	 * @param minPrice
	 * @param maxPrice
	 * @return
	 */
	private List<Integer> getInterval(Integer minPrice, Integer maxPrice) 
	{
		List<Integer> resultList = new ArrayList<Integer>();
		NumberTickUnitFormat tickFormat = NumberTickUnitFormat.getInstance(minPrice, maxPrice);

		/*//间隔值
		Integer interval = 0;
		//最小值
		Integer min = 0;
		//最大值
		Integer max = 0;
		minPrice = minPrice / 10000 * 10000;
		maxPrice = maxPrice / 10000 * 10000 + 10000;
		interval = (maxPrice - minPrice);
		if(interval <= 20000) {
			interval = 10000;
			min = minPrice - interval * 2;
			max = min + interval * 6;
		} else if(interval > 20000 && interval <= 50000) {
			interval = 20000;
			min = minPrice - 20000;
			max = min + interval * 6;
		} else if(interval > 50000 && interval <= 80000) {
			interval = 30000;
			min = minPrice - 20000;
			max = min + interval * 6;
		} else if(interval > 80000 && interval <= 120000) {
			interval = 40000;
			min = minPrice - 20000;
			max = min + interval * 6;
		} else {
			interval = interval / 5 / 10000 * 10000 + 10000;
			min = minPrice - 40000;
			max = min + interval * 6;
		}*/
		/*resultList.add(min);
		resultList.add(max);
		resultList.add(interval);*/
		//System.out.println("tickFormat.getMinTick()=="+tickFormat.getMinTick());
		//System.out.println("tickFormat.getMaxTick()=="+tickFormat.getMaxTick());
		//为保持和3.0一样，得到的最小值除以单元递增值然后向上取整*单元递增值
		int minValue =(int)Math.ceil(tickFormat.getMinTick()/tickFormat.getTick())*tickFormat.getTick().intValue();
		resultList.add(minValue);//最小刻度值
		resultList.add(tickFormat.getMaxTick().intValue());//最大刻度值
		resultList.add(tickFormat.getTick().intValue());//单元递增值
		resultList.add(tickFormat.getFenTick());//分段间隔数
		
		return resultList;
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
	
	/**
	 * 根据语言类型获取标签值
	 * @param obj
	 * @param paramsMap
	 * @return
	 */
	private String getLabelNameByLanguageType(JSONObject obj, Map<String, Object> paramsMap) 
	{
		String labelName = "";
		//标签显示内容
		String[] sortLabels = paramsMap.get("sortLabel").toString().split(",");
		String languageType = paramsMap.get("languageType").toString();
			for(String label : sortLabels) {
				//型号名称
				if("1".equals(label)) {
					labelName += getValueByLanguageType(obj, languageType, "versionShortName");
				} else if("2".equals(label)) {
					labelName += obj.getString("price");
				} else {
					labelName += obj.getString("bodyTypeNameEn");
				}
				labelName += "  ";
			}
		return labelName.substring(0, labelName.length() - 1);
	}
	
	/**
	 * 获取Excel标题背景样式
	 * @param wb
	 * @return
	 */
	private CellStyle getExcelTitleBackgroundStyle(Workbook wb)
	{
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();   
		font.setColor(IndexedColors.WHITE.index);//设置字体颜色
		
		style.setBorderLeft(CellStyle.SOLID_FOREGROUND); 
		style.setBorderBottom(CellStyle.SOLID_FOREGROUND);  
		style.setLeftBorderColor(IndexedColors.WHITE.getIndex());//设置左边框颜色
		style.setBottomBorderColor(IndexedColors.WHITE.getIndex());//设置下边框颜色
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//设置单元格文本左对齐
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中  

		//设置背景颜色
		style.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style.setFont(font);//设置字体
		return style;
	}
	
}
