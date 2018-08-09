package com.ways.app.pricesale.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.hp.hpl.sparta.ParseException;
import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.framework.utils.Constant;
import com.ways.app.framework.utils.ExportExcelUtil;
import com.ways.app.pricesale.dao.IScaleBySubModelSaleDao;
import com.ways.app.pricesale.model.PriceNumberTickUnitFormat;
import com.ways.app.pricesale.model.ScaleBySubModelSale;
import com.ways.app.pricesale.model.ScaleBySubModelSaleEntity;
import com.ways.app.pricesale.service.IScaleBySubModelSaleManager;

/**
 * 车型销售比例分析Service实现类
 * @author songguobiao
 *
 */
@Service("ScaleBySubModelSaleManagerImpl")
public class ScaleBySubModelSaleManagerImpl implements IScaleBySubModelSaleManager {
	@Autowired
	private IScaleBySubModelSaleDao scaleBySubModelSaleDao;
	
	/**
	 * 初始化时间
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@Override
	public String initDate(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String beginDate = "";
		String endDate = "";
		List<Map<String, String>> list = (List<Map<String, String>>) scaleBySubModelSaleDao.initDate(paramsMap);
		if(null != list && list.size() > 0) {
			beginDate = list.get(0).get("BEGINDATE");
			endDate = list.get(0).get("ENDDATE");
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("defaultBeginDate", endDate);
		}
		if("1".equals(paramsMap.get("changeDate"))) {
			String json = "";
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("beginDate", beginDate);
			dataMap.put("endDate", endDate);
			dataMap.put("defaultBeginDate", endDate);
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
			handleOneMonthParams(paramsMap);
			//返回细分市场页
	    	if("2".equals(paramsMap.get("subModelShowType"))) {
	    		request.setAttribute("segmentList", scaleBySubModelSaleDao.getSubmodelBySegment(paramsMap));
	    		//返回品牌页
	    	} else if("3".equals(paramsMap.get("subModelShowType"))) {
	    		request.setAttribute("brandLetterList", scaleBySubModelSaleDao.getSubmodelByBrand(paramsMap));
	    		//返回厂商页
	    	} else if("4".equals(paramsMap.get("subModelShowType"))) {
	    		request.setAttribute("manfLetterList", scaleBySubModelSaleDao.getSubmodelByManf(paramsMap));
	    		//本品页竟品页
	    	} else {
	    		request.setAttribute("bpSubModelList", scaleBySubModelSaleDao.getSubmodelByBp(paramsMap));
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
			request.setAttribute("bodyTypeList", scaleBySubModelSaleDao.getBodyType(paramsMap));
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
		List<ScaleBySubModelSale> list = null;
		if("2".equals(paramsMap.get("analysisDimensionType").toString())) {
			String[] dateGroup = paramsMap.get("dateGroup").toString().split("\\|");
			if(dateGroup.length == 1){
				//获取时间段内的月数
				//计算月数
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
				Date begin = null;
				Date end = null;
				try {
					begin = sdf.parse(dateGroup[0].split(",")[0]);
					end=sdf.parse(dateGroup[0].split(",")[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				@SuppressWarnings("deprecation")
				int months=(end.getYear()-begin.getYear())*12+(end.getMonth()-begin.getMonth())+1;
				String beginTime = dateGroup[0].split(",")[0].replace("-", "");
			     String[] arryTime = new String[months];
			      for(int i = 0 ; i < months; i++){
			    	  if(beginTime.substring(4,6).equals("12")){
			    		  arryTime[i] = beginTime;
			    		 beginTime = (Integer.toString(Integer.parseInt(beginTime.substring(0,4))+1))+"01";
			    	  } else{
			    		  arryTime[i] = beginTime;
			    		  beginTime = Integer.toString(Integer.parseInt(beginTime)+1);
			    	  }
			      }
					list = new ArrayList<ScaleBySubModelSale>(months);
					for(int i = 0; i < months; i++) {
						paramsMap.put("beginDate", arryTime[i]);
						paramsMap.put("endDate", arryTime[i]);
						paramsMap.put("dateGroupLength","single");//单对象
						List<ScaleBySubModelSale> sbssList = scaleBySubModelSaleDao.getAnalysisData(paramsMap);
						if(sbssList.size() > 0) {
							//时间对比时,需要把序号修改
							for(ScaleBySubModelSaleEntity sse : sbssList.get(0).getList()) {
								sse.setSn(String.valueOf(i + 1));
							}
							list.add(sbssList.get(0));
						}
					}
			      
			} else{
				list = new ArrayList<ScaleBySubModelSale>(dateGroup.length);
				for(int i = 0; i < dateGroup.length; i++) {
					paramsMap.put("beginDate", dateGroup[i].split(",")[0].replace("-", ""));
					paramsMap.put("endDate", dateGroup[i].split(",")[1].replace("-", ""));
					paramsMap.put("dateGroupLength","many");//多对象
					List<ScaleBySubModelSale> sbssList = scaleBySubModelSaleDao.getAnalysisData(paramsMap);
					if(sbssList.size() > 0) {
						//时间对比时,需要把序号修改
						for(ScaleBySubModelSaleEntity sse : sbssList.get(0).getList()) {
							sse.setSn(String.valueOf(i + 1));
						}
						list.add(sbssList.get(0));
					}
				}
			}
			
		} else {
			list = scaleBySubModelSaleDao.getAnalysisData(paramsMap);
		}
		if(null != list && list.size() > 0) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("series", list);
			dataMap.put("xTitles", getXTitles(list).get(0));
			dataMap.put("xTitlesEn", getXTitles(list).get(1));
			Integer[] resultList = getMinAndMaxPrice(list);
			dataMap.put("yAxis", getInterval(resultList[0], resultList[1]));
			
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			request.getSession().setAttribute(Constant.getScaleBySubModelSaleDataKey, json);
		}
		return json;
	}
	
	/**
	 * 获取X轴标题
	 * 
	 * @param list
	 * @return
	 */
	private List<List<String>> getXTitles(List<ScaleBySubModelSale> list) 
	{
		List<List<String>> xTotalTitles = new ArrayList<List<String>>();
		List<String> xTitles = new ArrayList<String>();
		List<String> xTitlesEn = new ArrayList<String>();
		for(int i = 0; i < list.size(); i++) {
			ScaleBySubModelSale sse = list.get(i);
			xTitles.add(sse.getSubModelName());
			xTitlesEn.add(sse.getSubModelNameEn());
		}
		xTotalTitles.add(xTitles);
		xTotalTitles.add(xTitlesEn);
		return xTotalTitles;
	}
	
	/**
	 * 获取Y轴价格刻度
	 * 
	 * @param list
	 * @return
	 */
	private Integer[] getMinAndMaxPrice(List<ScaleBySubModelSale> list) 
	{
		Integer[] resultList = new Integer[2];
		int max = 0;
		int min = Integer.parseInt(list.get(0).getList().get(0).getPrice());
		for(int i = 0; i < list.size(); i++) {
			List<ScaleBySubModelSaleEntity> sseList = list.get(i).getList();
			for(ScaleBySubModelSaleEntity sse : sseList) {
				if(Integer.parseInt(sse.getPrice()) > max) {
					max = Integer.parseInt(sse.getPrice());
				}
				if(Integer.parseInt(sse.getPrice()) < min) {
					min = Integer.parseInt(sse.getPrice());
				}
			}
		}
		
		resultList[0] = min;
		resultList[1] = max;
		return resultList;
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
		PriceNumberTickUnitFormat tickFormat = PriceNumberTickUnitFormat.getInstance(minPrice, maxPrice);
		
		/*//间隔值
		Integer interval = 0;
		//间隔数
		Integer intervalNumber = 6;
		//最小值
		Integer min = 0;
		//最大值
		Integer max = 0;
		minPrice = minPrice / 10000 * 10000;
		maxPrice = maxPrice / 10000 * 10000 + 10000;
		interval = (maxPrice - minPrice);
		if(interval <= 20000) {
			interval = 10000;
			max = min + interval * 6;
		} else if(interval > 20000 && interval <= 50000) {
			interval = 20000;
			min = minPrice - 20000;
			max = min + interval * 6;
		} else if(interval > 50000 && interval <= 80000) {
			interval = 30000;
			min = minPrice - 20000;
			max = min + interval * 6;
		} else if(interval > 80000 && interval <= 200000) {
			interval = 40000;
			min = minPrice - 20000;
			max = min + interval * 6;
		} else if(interval > 200000 && interval <= 400000) {
			interval = 50000;
			min = minPrice - 20000;
			max = min + interval * 9;
			intervalNumber = 9;
		} else if(interval > 400000 && interval <= 600000) {
			interval = 70000;
			min = minPrice - 20000;
			max = min + interval * 10;
			intervalNumber = 10;
		} else if(interval > 600000 && interval <= 800000) {
			interval = 80000;
			min = minPrice - 20000;
			max = min + interval * 11;
			intervalNumber = 11;
		} else if(interval > 800000 && interval <= 1000000) {
			interval = 100000;
			min = minPrice - 20000;
			max = min + interval * 11;
			intervalNumber = 11;
		} else if(interval > 1000000 && interval <= 1200000) {
			interval = 120000;
			min = minPrice - 20000;
			max = min + interval * 11;
			intervalNumber = 11;
		} else {
			interval = interval / 13 / 10000 * 10000 + 10000;
			min = minPrice - 20000;
			max = min + interval * 13;
			intervalNumber = 13;
		}*/
		/*resultList.add(min);
		resultList.add(max);
		resultList.add(interval);
		resultList.add(intervalNumber);*/
		//为保持和3.0一样，得到的最小值除以单元递增值然后向上取整*单元递增值
		int minValue =(int)Math.ceil(tickFormat.getMinTick()/tickFormat.getPriceSaleTick())*tickFormat.getPriceSaleTick().intValue();
		resultList.add(minValue);//最小刻度
		resultList.add(tickFormat.getMaxTick().intValue());//最大刻度
		resultList.add(tickFormat.getPriceSaleTick().intValue());//间隔递增单元值
		resultList.add(tickFormat.getFenTick());//间隔分段个数
		
		return resultList;
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
		//重新设置导出的数据
		changeData(request, paramsMap);
		String json = (String) request.getSession().getAttribute(Constant.getScaleBySubModelSaleDataKey);
		if(!AppFrameworkUtil.isEmpty(json)) {
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			try {
				String path = request.getSession().getServletContext().getRealPath("/"); 
				wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/scaleBySubModelSaleTemplate.xls")));
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
	private void exportObjectChartData(Workbook wb, Sheet sheet, JSONObject chartObj, Map<String, Object> paramsMap)
	{
		String languageType = paramsMap.get("languageType").toString();//语言类型ZH,EN
		String bubbleType = paramsMap.get("bubbleType").toString();//气泡类型
		String[] titlesCn = new String[]{"型号编码", "厂商", "车型", "排量", "排挡方式", "序号", "成交价", "", "标签值"};
		String[] titlesEn = new String[]{"VersionCode", "OEM", "Model", "EngineCapacity", "Transmission", "Index", "TP", "", "Label"};
		if("2".equals(paramsMap.get("analysisDimensionType"))) {
			titlesCn[2] = "日期";
			titlesEn[2] = "Date";
		}
		if("2".equals(paramsMap.get("priceType"))) {
			titlesCn[6] = "指导价";
			titlesEn[6] = "MSRP";
		}
		if("1".equals(bubbleType)) {
			titlesCn[7] = "累计Mix";
			titlesEn[7] = "AddMix";
		} else if("2".equals(bubbleType)) {
			titlesCn[7] = "月均Mix";
			titlesEn[7] = "AvgMix";
		} else if("3".equals(bubbleType)) {
			titlesCn[7] = "累计型号销量";
			titlesEn[7] = "AddVersionSale";
		} else {
			titlesCn[7] = "月均型号销量";
			titlesEn[7] = "AvgVersionSale";
		}
		JSONArray resultList =  chartObj.getJSONArray("series");
		JSONArray xTitles = null;
		if(!"EN".equals(languageType)) {
			xTitles = chartObj.getJSONArray("xTitles");
		} else {
			xTitles = chartObj.getJSONArray("xTitlesEn");
		}
		JSONArray yAxis = chartObj.getJSONArray("yAxis");
		//价格刻度字符串
		StringBuilder priceTick = new StringBuilder();
		//最小值
		priceTick.append(yAxis.get(0)).append(",");
		//间隔数
		Integer intervalNumber = Integer.parseInt(yAxis.get(3).toString());
		for(int i = 1; i < intervalNumber; i++) {
			Integer tick = Integer.parseInt(yAxis.get(0).toString()) + Integer.parseInt(yAxis.get(2).toString()) * i;
			priceTick.append(tick).append(",");
		}
		//最大值
		priceTick.append(yAxis.get(1));
		//X轴名称字符串
		StringBuilder xName = new StringBuilder();
		for(int i = 0; i < xTitles.size(); i++) {
			xName.append(xTitles.get(i)).append(",");
		}
		int rowIndex = 0;//行号锁引
		Row row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
		
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		
		Cell chartCell = row.createCell(40);
		//写入图表类型
		ExportExcelUtil.setCellValueAndStyle(chartCell, paramsMap.get("chartType").toString(), textStyle);
		chartCell = row.createCell(41);
		//写入手动排挡显示类型
		ExportExcelUtil.setCellValueAndStyle(chartCell, paramsMap.get("showType").toString(), textStyle);
		chartCell = row.createCell(42);
		//写入价格刻度
		ExportExcelUtil.setCellValueAndStyle(chartCell, priceTick.toString(), textStyle);
		chartCell = row.createCell(43);
		//写入X轴标题
		ExportExcelUtil.setCellValueAndStyle(chartCell, xName.toString().substring(0, xName.length() - 1), textStyle);
		
		//写表格标题
		String[] titles = new String[7];
		if(!"EN".equals(languageType)) {
			titles = titlesCn;
		} else {
			titles = titlesEn;
		}
		Cell cell = null;
		for(int i = 0; i < titles.length; i++) {
			cell = row.createCell(i);
			ExportExcelUtil.setCellValueAndStyle(cell, titles[i], titleStyle);
			sheet.setColumnWidth(i, 5000);
		}
		
		//写数据
		for(int j = 0; j < resultList.size(); j++) {
			JSONArray list = resultList.getJSONObject(j).getJSONArray("list");
			for(int i = 0; i < list.size(); i++) {
				JSONObject se = list.getJSONObject(i);
				rowIndex++;
				row = ExportExcelUtil.createRow(sheet, rowIndex, 400);
				int count = 0;
				cell = row.createCell(count++);
				ExportExcelUtil.setCellValueAndStyle(cell, se.getString("versionCode") + "~", textStyle);
				
				cell = row.createCell(count++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(se, languageType, "manfName"), textStyle);
				
				cell = row.createCell(count++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(se, languageType, "subModelName"), textStyle);
				
				cell = row.createCell(count++);
				ExportExcelUtil.setCellValueAndStyle(cell, se.getString("emissionsName"), textStyle);
				
				cell = row.createCell(count++);
				ExportExcelUtil.setCellValueAndStyle(cell, se.getString("transMission"), textStyle);
				
				cell = row.createCell(count++);
				ExportExcelUtil.setCellValueAndStyle(cell, se.getString("sn"), textStyle);
				
				cell = row.createCell(count++);
				ExportExcelUtil.setCellValueAndStyle(cell, se.getString("price"), thousandthStyle);
				
				cell = row.createCell(count++);
				if("1".equals(bubbleType)) {
					ExportExcelUtil.setCellValueAndStyle(cell, getPercentValue(se.getString("addMix")), textStyle);
				} else if("2".equals(bubbleType)) {
					ExportExcelUtil.setCellValueAndStyle(cell, getPercentValue(se.getString("avgMix")), textStyle);
				} else if("3".equals(bubbleType)) {
					ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(se.getString("addSale"), 7), thousandthStyle);
				} else {
					ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(se.getString("avgSale"), 7), thousandthStyle);
				}
				
				cell = row.createCell(count++);
				ExportExcelUtil.setCellValueAndStyle(cell, getLabelNameByLanguageType(se, paramsMap), textStyle);
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
	public void exportObjectOriginalData(Workbook wb, Sheet sheet, JSONObject jsonObj, Map<String, Object> paramsMap)
	{
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		String bubbleType = paramsMap.get("bubbleType").toString();//气泡类型
		JSONArray gridObj = jsonObj.getJSONArray("series");
		String[] titlesCn = new String[]{"型号编码", "厂商", "车型", "排量", "排挡方式", "型号标识", "成交价", "", "原装版型号编码", "原装版型号标识"};
		String[] titlesEn = new String[]{"VersionCode", "OEM", "Model", "EngineCapacity", "Transmission", "TrimName", "TP", "", 
				 "Model No. of Original Version", "Model Mark of Original Version"};
		if("2".equals(paramsMap.get("analysisDimensionType"))) {
			titlesCn[2] = "日期";
			titlesEn[2] = "Date";
		}
		if("2".equals(paramsMap.get("priceType"))) {
			titlesCn[6] = "指导价";
			titlesEn[6] = "MSRP";
		}
		if("1".equals(bubbleType)) {
			titlesCn[7] = "累计Mix";
			titlesEn[7] = "AddMix";
		} else if("2".equals(bubbleType)) {
			titlesCn[7] = "月均Mix";
			titlesEn[7] = "AvgMix";
		} else if("3".equals(bubbleType)) {
			titlesCn[7] = "累计型号销量";
			titlesEn[7] = "AddVersionSale";
		} else {
			titlesCn[7] = "月均型号销量";
			titlesEn[7] = "AvgVersionSale";
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
			sheet.setColumnWidth(i, 5000);
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
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "versionTrimName"), textStyle);
				
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("price"), 7), thousandthStyle);
				
				cell = row.createCell(index++);
				if("1".equals(bubbleType)) {
					ExportExcelUtil.setCellValueAndStyle(cell, getPercentValue(obj.getString("addMix")), textStyle);
				} else if("2".equals(bubbleType)) {
					ExportExcelUtil.setCellValueAndStyle(cell, getPercentValue(obj.getString("avgMix")), textStyle);
				} else if("3".equals(bubbleType)) {
					ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("addSale"), 7), thousandthStyle);
				} else {
					ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("avgSale"), 7), thousandthStyle);
				}
				
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("originalVersionCode") + "~", textStyle);
				
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "originalTrimName"), textStyle);
			}
		}
		sheet.setDisplayGridlines(false);
	}
	
	/**
	 * 改变原始数据(只导出页面显示的数据)
	 * 
	 * @param request
	 * @param paramsMap
	 */
	private void changeData(HttpServletRequest request, Map<String, Object> paramsMap) 
    {
    	String json = "";
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<ScaleBySubModelSale> list = null;
		String series = "";
		try {
			series = new String(paramsMap.get("exportSeries").toString().getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 页面JSON字符串转换为JAVA对象
		list = JSONArray.parseArray(series, ScaleBySubModelSale.class);
		//重新排序
		int count = 0;
		for(int i = 0; i < list.size(); i++) {
			count++;
			List<ScaleBySubModelSaleEntity> sseList = list.get(i).getList();
	    	for(ScaleBySubModelSaleEntity sse: sseList) {
	    		sse.setSn(String.valueOf(count));
	    	}
		}
    	dataMap.put("series", list);
		dataMap.put("xTitles", getXTitles(list).get(0));
		dataMap.put("xTitlesEn", getXTitles(list).get(1));
		String sessionJson = (String)request.getSession().getAttribute(Constant.getScaleBySubModelSaleDataKey);
		JSONObject obj = (JSONObject) JSONObject.parse(sessionJson);
		dataMap.put("yAxis", obj.getJSONArray("yAxis"));
		json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
		//重新设定查询JSON数据
		request.getSession().setAttribute(Constant.getScaleBySubModelSaleDataKey, json);
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
	
	/**
	 * 获取百分比数值
	 * @param strValue
	 * @return
	 */
	private String getPercentValue(String strValue) {
		DecimalFormat df = new DecimalFormat("#.##");
		String percentValue = "";
		percentValue = df.format(Double.parseDouble(strValue) * 100) + "%";
		return percentValue;
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
		//分隔符
		String split = "";
		if("1".equals(paramsMap.get("splitType").toString())) {
			split = ",";
		} else if("2".equals(paramsMap.get("splitType").toString())) {
			split = ";";
		} else {
			split = " ";
		}
		String isShow = obj.getString("isShow");
		String labelSum = paramsMap.get("labelSum").toString();
		
		
		DecimalFormat cc = new DecimalFormat("##.##");
		
		if("2".equals(labelSum) || ("1".equals(labelSum) && "1".equals(isShow))) {
			for(String label : sortLabels) {
				//型号名称
				if("1".equals(label)) {
					labelName += obj.getString("versionShortNameEn");
				} else if("2".equals(label)) {
					labelName += obj.getString("bodyTypeNameEn");
				} else if("3".equals(label)) {
					labelName += "[" + obj.getString("price") + "]";
				} else if("4".equals(label)) {
					labelName += "[" + cc.format(Float.parseFloat(obj.getString("addMix")) * 100) + "%]";
				} else if("5".equals(label)) {
					labelName += "[" + cc.format(Float.parseFloat(obj.getString("avgMix")) * 100) + "%]";
				} else if("6".equals(label)) {
					labelName += "[" + obj.getString("addSale") + "]";				
				} else {
					labelName += "[" + obj.getString("avgSale") + "]";
				}
				labelName += split;
			}
		} else {
			labelName = ",";
		}
		return labelName.substring(0, labelName.length() - 1);
	}
}
