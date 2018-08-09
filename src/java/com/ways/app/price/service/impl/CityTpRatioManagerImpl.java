package com.ways.app.price.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
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
import com.ways.app.framework.utils.EchartsUtil;
import com.ways.app.framework.utils.ExportExcelUtil;
import com.ways.app.price.dao.ICityTpRatioDao;
import com.ways.app.price.model.EchartLineDataEntity;
import com.ways.app.price.model.RadarChartPolar;
import com.ways.app.price.model.RadarChartSeriesEntity;
import com.ways.app.price.model.VersionAreaInfoEntity;
import com.ways.app.price.service.ICityTpRatioManager;


/**
 * 城市成交价Service层接口实现类
 * @author yinlue
 *
 */
@Service("CityTpRatioManagerImpl")
public class CityTpRatioManagerImpl implements ICityTpRatioManager {

	@Autowired
	private ICityTpRatioDao cityTpRatioDao;

	/**
	 * 初始时间
	 */
	@Override
	public String initDate(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		List<Map<String, String>> list = cityTpRatioDao.initDate(paramsMap);
		if(null != list && 0 != list.size())
		{
			Map<String, String> resultMap = list.get(0);
			String endDate = resultMap.get("ENDDATE");
			request.setAttribute("beginDate", resultMap.get("BEGINDATE"));
			request.setAttribute("endDate", endDate);
			request.setAttribute("defaultBeginDate", endDate);
		}
		return null;
	}

	/**
	 * 加载城市成交价对比图形和表格
	 */
	@Override
	public String loadCityTpRatioChartAndTable(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		String json = "";
		List<VersionAreaInfoEntity> list = cityTpRatioDao.loadCityTpRatioChartAndTable(paramsMap);
		if(null != list && 0 != list.size())
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			
			if("1".equals(paramsMap.get("chartType"))) dataMap.put("chart", getRadarChartData(list));
			else dataMap.put("chart", getLineChartData(list));
			
			dataMap.put("grid", list);
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			request.getSession().setAttribute(Constant.getExportExcelDataKey, json);
			request.getSession().setAttribute("chartType", paramsMap.get("chartType"));
		}
		return json;
	}

	/**
	 * 获取线图数据
	 * @return
	 */
	public Map<String, Object> getLineChartData(List<VersionAreaInfoEntity> list)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		String tp = "",msrp = "",invoicePrice = "",cityName = "";
		
		for(int i = 0; i < list.size(); i++)
		{
			VersionAreaInfoEntity info = list.get(i);
			String stp = "-",smsrp = "-",sinvoicePrice = "-";
			if(!"-".equals(info.getTp())) stp = AppFrameworkUtil.getNum(Float.parseFloat(info.getTp())/1000, 1); //TP
			if(!"-".equals(info.getMsrp())) smsrp = AppFrameworkUtil.getNum(Float.parseFloat(info.getMsrp())/1000, 1); //MSRP
			if(!"-".equals(info.getInvoicePrice())) sinvoicePrice = AppFrameworkUtil.getNum(Float.parseFloat(info.getInvoicePrice())/1000, 1);//开票价
			
			if(0 == i)
			{
				cityName = info.getCityName();
				tp = stp;
				msrp = smsrp;
				invoicePrice = sinvoicePrice;
			}
			else
			{
				cityName += "," + info.getCityName();
				tp += "," + stp;
				msrp += "," + smsrp;
				invoicePrice += "," + sinvoicePrice;
			}
		}
		map.put("xTitle", cityName.split(","));
		List<EchartLineDataEntity> seriesList = new ArrayList<EchartLineDataEntity>();
		seriesList.add(new EchartLineDataEntity("line","指导价(千元)","none",msrp.split(",")));
		seriesList.add(new EchartLineDataEntity("line","成交价(千元)","emptyCircle",tp.split(",")));
		seriesList.add(new EchartLineDataEntity("line","开票价(千元)","none",invoicePrice.split(",")));
		map.put("series", seriesList);
		map.put("boundarys", EchartsUtil.setLineScaleDivisionToThousand(seriesList));
		return map;
	}
	
	/**
	 * 获取雷达图数据
	 * @return
	 */
	public Map<String, Object> getRadarChartData(List<VersionAreaInfoEntity> list)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		String tp = "",msrp = "",invoicePrice = "",weightTp = "";
		
		list = descList(list);
		//雷达图范围边界集合
		List<RadarChartPolar> poloarList = new ArrayList<RadarChartPolar>();
		//雷达图数据集合
		List<RadarChartSeriesEntity> seriesList = new ArrayList<RadarChartSeriesEntity>();
		
		int max = 1;
		int min = 1;
		for(int i = 0; i < list.size(); i++)
		{
			VersionAreaInfoEntity info = list.get(i);
			
			String stp = AppFrameworkUtil.getNum(info.getTp(), 0); //TP
			String smsrp = AppFrameworkUtil.getNum(info.getMsrp(), 0); //MSRP
			String sinvoicePrice = AppFrameworkUtil.getNum(info.getInvoicePrice(), 0);//开票价
			String sweightTp = AppFrameworkUtil.getNum(info.getWeightTp(), 0); //加权均价
			
			if(0 == i)
			{
				tp = stp;
				msrp = smsrp;
				invoicePrice = sinvoicePrice;
				weightTp = sweightTp;
			}
			else
			{
				tp += "," + stp;
				msrp += "," + smsrp;
				invoicePrice += "," + sinvoicePrice;
				weightTp += "," + sweightTp;
			}
			
			//计算城市边界最大值
			if(1 == max)
			{
				if(!"-".equals(smsrp)){
					max = (int)(Integer.parseInt(smsrp) * 1.02);
				}
				
				if(!"-".equals(sinvoicePrice)){
					min = (int)(Integer.parseInt(sinvoicePrice) * 0.83);
				}else{
						 min = (int)(Integer.parseInt(sweightTp) * 0.83);
				}  
				
			}
			
			poloarList.add(new RadarChartPolar(info.getCityName()+"\n"+AppFrameworkUtil.format(info.getTp()),max,min));
		}
		seriesList.add(new RadarChartSeriesEntity(msrp.split(","),"指导价"));
		seriesList.add(new RadarChartSeriesEntity(tp.split(","),"成交价"));
		seriesList.add(new RadarChartSeriesEntity(invoicePrice.split(","),"开票价"));
		seriesList.add(new RadarChartSeriesEntity(weightTp.split(","),"加权均价"));
		
		map.put("poloarList", poloarList);
		map.put("seriesList", seriesList);
		return map;
	}
	
	/**
	 * 倒序集合，为使雷达图画圈
	 * @param list
	 * @return
	 */
	public List<VersionAreaInfoEntity> descList(List<VersionAreaInfoEntity> list)
	{
		List<VersionAreaInfoEntity> newList = new ArrayList<VersionAreaInfoEntity>();
		newList.add(list.get(0));
		
		for(int i = (list.size()-1); i > 0; i--)
		{
			newList.add(list.get(i));
		}
		
		return newList;
	}
	
	
	/**
	 * 导出
	 */
	@Override
	public Workbook exportExcel(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		Workbook wb = null;
		String json = (String) request.getSession().getAttribute(Constant.getExportExcelDataKey);
		String chartType = (String) request.getSession().getAttribute("chartType");//1:雷达图；2：折线图
		String languageType = (String) paramsMap.get("languageType");//导出语言
		if(!AppFrameworkUtil.isEmpty(json))
		{
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			JSONObject chartObj = (JSONObject) obj.get("chart");
			JSONArray gridObj = (JSONArray) obj.getJSONArray("grid");
			
			String excelName = "cityTpRadar.xls";
			if("2".equals(chartType)) excelName = "cityTpLine.xls";
			try {
				//读取模块路径
				String path = request.getSession().getServletContext().getRealPath("/"); 
				wb = new HSSFWorkbook(new FileInputStream(new File(path+"excelExample/"+excelName)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			//导图
			if("1".equals(chartType)) exportRadarChartExcel(wb,wb.getSheet("DATA"),gridObj,chartType,languageType);
			else exportLineChartExcel(wb,wb.getSheet("DATA"),chartObj,chartType,languageType);
			//导表格
			exportOriginalData(wb,wb.getSheet("原始数据"),gridObj,languageType);
		}
		return wb;
	}
	
	/**
	 * 导出雷达图图形数据
	 * @param wb
	 * @param s
	 * @param gridObj
	 * @param chartType
	 */
	public void exportRadarChartExcel(Workbook wb,Sheet s,JSONArray gridObj,String chartType,String languageType)
	{
		String[] times = getMonth(gridObj.getJSONObject(0).getString("yearMonth"));
		
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		CellStyle percentageStyle = ExportExcelUtil.getExcelFillPercentageStyle(wb);//格式化百分号样式
		
		writeTableTitle(times,languageType,wb,s);
		
		int gridSize = gridObj.size();
		int rowIndex = 2;//行号锁引
		//写表格数据
		for(int i = 0; i < gridSize; i++)
		{
			JSONObject obj = gridObj.getJSONObject(i);
			Row row = ExportExcelUtil.createRow(s, rowIndex++, 400);
			Cell cell = null;
			
			cell = row.createCell(0);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"areaName"), textStyle);
			
			cell = row.createCell(1);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"cityName"), textStyle);
			
			cell = row.createCell(2);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("tp"), thousandthStyle);
			
			cell = row.createCell(3);
			ExportExcelUtil.setCellValueToPercentage(cell, AppFrameworkUtil.getNum(obj.getString("variation"),7), percentageStyle);
			
			cell = row.createCell(4);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("modelProfit"), thousandthStyle);
			
			cell = row.createCell(5);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("vsTp"), thousandthStyle);
			
			cell = row.createCell(6);
			ExportExcelUtil.setCellValueToPercentage(cell, obj.getString("vsTpRate"), percentageStyle);
			
			cell = row.createCell(7);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("vsProfit"), thousandthStyle);
			
			cell = row.createCell(8);
			ExportExcelUtil.setCellValueToPercentage(cell, obj.getString("vsProfitRate"), percentageStyle);
			
			cell = row.createCell(9);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("msrp"), thousandthStyle);
			
			cell = row.createCell(10);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("rebatePrice"), thousandthStyle);
			
			cell = row.createCell(11);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("rewardAssessment"), thousandthStyle);
			
			cell = row.createCell(12);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("promotionalAllowance"), thousandthStyle);
			
			//写第二个表格
			cell = row.createCell(14);
			ExportExcelUtil.setCellValueAndStyle(cell, countValue(obj.getString("tp"),obj.getString("vsTp")), thousandthStyle);
			
			cell = row.createCell(15);
			ExportExcelUtil.setCellValueAndStyle(cell, countValue(obj.getString("modelProfit"),obj.getString("vsProfit")), thousandthStyle);
			
			cell = row.createCell(16);
			ExportExcelUtil.setCellValueToPercentage(cell,countPercentage(countValue(obj.getString("modelProfit"),obj.getString("vsProfit")),obj.getString("vsMsrp")), percentageStyle);
			
			//写第三个表格
			cell = row.createCell(18);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"cityName"), thousandthStyle);
			
			cell = row.createCell(19);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("msrp"), thousandthStyle);
			
			cell = row.createCell(20);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("tp"), thousandthStyle);
			
			cell = row.createCell(21);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("invoicePrice"), thousandthStyle);
			
			cell = row.createCell(22);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("weightTp"),7), thousandthStyle);
			
			cell = row.createCell(23);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("modelProfit"), thousandthStyle);
			
			cell = row.createCell(24);
			ExportExcelUtil.setCellValueToPercentage(cell, countPercentage(obj.getString("modelProfit"),obj.getString("msrp")), percentageStyle);
		}
		//合并
		s.addMergedRegion(new CellRangeAddress(2,gridSize+1,9,9));
		s.addMergedRegion(new CellRangeAddress(2,gridSize+1,10,10));
		s.addMergedRegion(new CellRangeAddress(2,gridSize+1,11,11));
		s.addMergedRegion(new CellRangeAddress(2,gridSize+1,12,12));
		//计算合并区域列
		String areaName = "";
		int rowSpanNum = 0;
		List<Integer> rowSpanList = new ArrayList<Integer>();
		//计算各大区合并行数
		for(int n = 0; n < gridSize; n++)
		{
			JSONObject obj = gridObj.getJSONObject(n);
			if(!areaName.equals(obj.getString("areaName"))) 
			{
				areaName = obj.getString("areaName");
				if(0 != n) rowSpanList.add(rowSpanNum);
				rowSpanNum = 1;
			}
			else
			{
				rowSpanNum ++;
			}
			if(n == (gridSize - 1)) rowSpanList.add(rowSpanNum);
		}
		//从第二号开始合并
		rowSpanNum = 2;
		int endIndex = 0;//大区合并行结束锁引
		for(int k = 0; k < rowSpanList.size(); k++)
		{
			if(0 == k) endIndex += rowSpanList.get(k) + 1;
			else endIndex += rowSpanList.get(k);
			
			s.addMergedRegion(new CellRangeAddress(rowSpanNum,endIndex,0,0));
			rowSpanNum += rowSpanList.get(k);
		}
		s.setDisplayGridlines(false);
	}
	
	
	
	
	/**
	 * 写标题
	 * @param times
	 * @param languageType
	 * @param wb
	 * @param s
	 */
	public void writeTableTitle(String[] times,String languageType,Workbook wb,Sheet s)
	{
		
		String[] titles = null;//标题数组
		String month = "月";
		if("EN".equals(languageType)) 
		{
			titles = new String[]{"Area","City","TP","Last Month","MSRP","TP","Margin","Bonus","Tactical","Gross Profit","Profit Rate","TP","Dispersion","Invoice Price"};
			month = "Month";
		}
		else 
		{
			titles = new String[]{"大区","城市","当前成交价","环比上月","指导价","成交价","激励额","奖励","促销","利润","利润率","加权均价","离散度","开票"};
		}
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle titleBottomStyle = ExportExcelUtil.getExcelTitleBackgroundToBottomStyle(wb);//表格标题下边框样式
		titleBottomStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//设置单元格文本左对齐
		
		int rowIndex = 0;//行号锁引
		Row row = ExportExcelUtil.createRow(s, rowIndex, 400);
		//写标题开始
		Cell cell = row.createCell(0);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[0], titleStyle);
		
		cell = row.createCell(1);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[1], titleStyle);
		
		cell = row.createCell(2);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[2], titleBottomStyle);
		
		cell = row.createCell(3);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
		
		cell = row.createCell(4);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
		s.addMergedRegion(new CellRangeAddress(0,0,2,4));
		
		cell = row.createCell(5);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[3], titleBottomStyle);
		
		cell = row.createCell(6);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
		s.addMergedRegion(new CellRangeAddress(0,0,2,4));
		
		cell = row.createCell(7);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
		s.addMergedRegion(new CellRangeAddress(0,0,2,4));
		
		cell = row.createCell(8);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
		s.addMergedRegion(new CellRangeAddress(0,0,5,8));
		
		cell = row.createCell(9);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[4], titleStyle);
		
		cell = row.createCell(10);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[6], titleStyle);
		
		cell = row.createCell(11);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[7], titleStyle);
		
		cell = row.createCell(12);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[8], titleStyle);
		
		cell = row.createCell(14);
		ExportExcelUtil.setCellValueAndStyle(cell, times[1]+month, titleBottomStyle);
		
		cell = row.createCell(15);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
		
		cell = row.createCell(16);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
		s.addMergedRegion(new CellRangeAddress(0,0,14,16));
		
		cell = row.createCell(18);
		ExportExcelUtil.setCellValueAndStyle(cell, times[0]+month, titleBottomStyle);
		
		cell = row.createCell(19);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
		
		cell = row.createCell(20);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
		
		cell = row.createCell(21);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
		
		cell = row.createCell(22);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
		
		cell = row.createCell(23);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
		
		cell = row.createCell(24);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
		s.addMergedRegion(new CellRangeAddress(0,0,18,24));
		//第二行标题
		row = ExportExcelUtil.createRow(s, ++rowIndex, 400);
		cell = row.createCell(0);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleStyle);
		s.addMergedRegion(new CellRangeAddress(0,1,0,0));
		
		cell = row.createCell(1);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleStyle);
		s.addMergedRegion(new CellRangeAddress(0,1,1,1));
		
		cell = row.createCell(2);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[5], titleStyle);
		
		cell = row.createCell(3);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[12], titleStyle);
		
		cell = row.createCell(4);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[9], titleStyle);
		
		cell = row.createCell(5);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[5]+".±", titleStyle);
		
		cell = row.createCell(6);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[5]+"%", titleStyle);
		
		cell = row.createCell(7);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[9]+".±", titleStyle);
		
		cell = row.createCell(8);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[10]+".±", titleStyle);
		
		cell = row.createCell(9);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleStyle);
		s.addMergedRegion(new CellRangeAddress(0,1,9,9));
		
		cell = row.createCell(10);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleStyle);
		s.addMergedRegion(new CellRangeAddress(0,1,10,10));
		
		cell = row.createCell(11);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleStyle);
		s.addMergedRegion(new CellRangeAddress(0,1,11,11));
		
		cell = row.createCell(12);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleStyle);
		s.addMergedRegion(new CellRangeAddress(0,1,12,12));
		
		cell = row.createCell(14);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[5], titleStyle);
		
		cell = row.createCell(15);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[9], titleStyle);
		
		cell = row.createCell(16);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[10], titleStyle);
		
		cell = row.createCell(18);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[1], titleStyle);
		
		cell = row.createCell(19);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[4], titleStyle);
		
		cell = row.createCell(20);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[5], titleStyle);
		
		cell = row.createCell(21);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[13], titleStyle);
		
		cell = row.createCell(22);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[11], titleStyle);
		
		cell = row.createCell(23);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[9], titleStyle);
		
		cell = row.createCell(24);
		ExportExcelUtil.setCellValueAndStyle(cell, titles[10], titleStyle);
	}
	
	
	/**
	 * 导出线图图形数据
	 * @param wb
	 * @param s
	 * @param chartObj
	 * @param chartType
	 */
	public void exportLineChartExcel(Workbook wb,Sheet s,JSONObject chartObj,String chartType,String languageType)
	{
		JSONArray titles = chartObj.getJSONArray("xTitle");
		JSONArray series =  chartObj.getJSONArray("series");
		int rowIndex = 0;//行号锁引
		Row row = ExportExcelUtil.createRow(s, rowIndex, 600);
		Cell cell = null;
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		//写表格标题
		
		cell = row.createCell(0);
		ExportExcelUtil.setCellValueAndStyle(cell, "", titleStyle);
		s.setColumnWidth(cell.getColumnIndex(), 3500);
		
		for(int i = 0; i < titles.size(); i++)
		{
			cell = row.createCell(i + 1);
			String title = titles.getString(i);
			
			if("EN".equals(languageType)) 
			{
				if("沈阳".equals(title)) title = "Shenyang";
				else if("哈尔滨".equals(title)) title = "Harbin";
				else if("呼和浩特".equals(title)) title = "Huhehaote";
				else if("北京".equals(title)) title = "Beijing";
				else if("石家庄".equals(title)) title = "Shijiazhuang";
				else if("武汉".equals(title)) title = "Wuhan";
				else if("济南".equals(title)) title = "Ji nan";
				else if("郑州".equals(title)) title = "Zhengzhou";
				else if("青岛".equals(title)) title = "Qingdao";
				else if("上海".equals(title)) title = "Shanghai";
				else if("杭州".equals(title)) title = "Hangzhou";
				else if("南京".equals(title)) title = "Nanjing";
				else if("合肥".equals(title)) title = "Hefei";
				else if("广州".equals(title)) title = "Guangzhou";
				else if("深圳".equals(title)) title = "Shenzhen";
				else if("长沙".equals(title)) title = "Changsha";
				else if("南宁".equals(title)) title = "Nanning";
				else if("福州".equals(title)) title = "Fuzhou";
				else if("成都".equals(title)) title = "Chengdu";
				else if("西安".equals(title)) title = "Xi an";
				else if("重庆".equals(title)) title = "Chongqing";
				else if("昆明".equals(title)) title = "Kunming";
				else if("贵阳".equals(title)) title = "Guiyang";
				else if("太原".equals(title)) title = "Taiyuan";
				else if("长春".equals(title)) title = "Changchun";
				else if("大连".equals(title)) title = "Dalian";
				else if("天津".equals(title)) title = "Tianjing";
				else if("乌鲁木齐".equals(title)) title = "Wulumuqi";
				else if("宁波".equals(title)) title = "Ningbo";
				else if("南昌".equals(title)) title = "Nanchang";
				else if("兰州".equals(title)) title = "Lanzhou";
			}
			
			ExportExcelUtil.setCellValueAndStyle(cell, title, titleStyle);
			s.setColumnWidth(cell.getColumnIndex(), 2000);
		}
		//写数据
		for(int j = 0; j < series.size(); j++)
		{
			JSONObject obj = series.getJSONObject(j);
			JSONArray datas = obj.getJSONArray("data");
			
			row = ExportExcelUtil.createRow(s, ++rowIndex, 400);
			cell = row.createCell(0);
			
			String title = obj.getString("name");
			if("EN".equals(languageType)) 
			{
				if("指导价(千元)".equals(title)) title = "MSRP";
				else if("成交价(千元)".equals(title)) title = "TP";
				else title = "Invoice Price";
			}
			
			ExportExcelUtil.setCellValueAndStyle(cell,title, textStyle);
			for(int n = 0; n < datas.size(); n++)
			{
				cell = row.createCell(n+1);
				ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(datas.getString(n),7), textStyle);
			}
		}
		s.setDisplayGridlines(false);
	}
	
	/**
	 * 导出原始数据
	 * @param wb
	 * @param s
	 * @param gridObj
	 */
	public void exportOriginalData(Workbook wb,Sheet s,JSONArray gridObj,String languageType)
	{
		String[] titles = null;//标题数组
		int cityLength = gridObj.size();
		if("EN".equals(languageType)) 
		{
			titles = new String[]{"Date","Code","Segment","Manufacture","Model","Engine Capacity","Transmission","Trim","Bodytype","Launch Date","Model Year","MSRP(RMB)"
					,"TP（RMB）","TP（RMB）","Discount（RMB）","Margin","Bonus","Tactical","Invoice Price","Gross Profit"};
		}
		else 
		{
			titles = new String[]{"日期","车型编码","级别","厂商","车型","排量","排档方式","型号标识","车身形式","上市日期","年式","厂商指导价"
					,"最低参考价（元）","加权成交价","折扣","激励额","考核奖励","促销","开票价","经销商利润"};
		}
		int rowIndex = 0;//行号锁引
		Row row = ExportExcelUtil.createRow(s, rowIndex, 400);
		
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle titleBottomStyle = ExportExcelUtil.getExcelTitleBackgroundToBottomStyle(wb);//表格标题下边框样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		//写标识为是中文还是英文版，供数据部使用
		Cell cell = row.createCell(60);
		ExportExcelUtil.setCellValueAndStyle(cell, languageType, titleBottomStyle);
		//写表格标题
		int cellIndex = 0;
		for(int i = 0; i < titles.length; i++)
		{
			cell = row.createCell(cellIndex++);
			if(12 == i) 
			{
				ExportExcelUtil.setCellValueAndStyle(cell, titles[i], titleBottomStyle);
				//写空的城市列
				for(int j = 0; j < (cityLength - 1); j++)
				{
					cell = row.createCell(cellIndex++);
					ExportExcelUtil.setCellValueAndStyle(cell, "", titleBottomStyle);
				}
			}
			else 
			{
				ExportExcelUtil.setCellValueAndStyle(cell, titles[i], titleStyle);
				s.setColumnWidth(i, 3000);
			}
		}
		//合并市场最低参考价
		s.addMergedRegion(new CellRangeAddress(0,0,12,(12+cityLength-1)));
		
		//写数据
		row = ExportExcelUtil.createRow(s, ++rowIndex, 400);
		cellIndex = 0;
		for(int n = 0; n < titles.length; n++)
		{
			if(12 == n) 
			{
				//写城市列
				for(int k = 0; k < gridObj.size(); k++)
				{
					JSONObject obj = gridObj.getJSONObject(k);
					cell = row.createCell(cellIndex++);
					ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"cityName"), titleBottomStyle);
				}
			}
			else 
			{
				cell = row.createCell(cellIndex++);
				ExportExcelUtil.setCellValueAndStyle(cell,"", titleStyle);
				//合并行
				s.addMergedRegion(new CellRangeAddress(0,1,cell.getColumnIndex(),cell.getColumnIndex()));
			}
		}
		//写数据
		row = ExportExcelUtil.createRow(s, ++rowIndex, 400);
		cellIndex = 0;
		JSONObject versionInfoObj = gridObj.getJSONObject(0);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, versionInfoObj.getString("yearMonth")+"~", textStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, versionInfoObj.getString("versionCode")+"~", textStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(versionInfoObj,languageType,"gradeName"), textStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(versionInfoObj,languageType,"manfName"), textStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(versionInfoObj,languageType,"subModelName"), textStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, versionInfoObj.getString("discharge"), textStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(versionInfoObj,languageType,"gearMode"), textStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(versionInfoObj,languageType,"typeId"), textStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(versionInfoObj,languageType,"bodyType"), textStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, versionInfoObj.getString("versionLaunchDate"), textStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, versionInfoObj.getString("modelYear"), textStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, versionInfoObj.getString("msrp"), thousandthStyle);
		
		//写城市TP
		for(int m = 0; m < gridObj.size(); m++)
		{
			JSONObject obj = gridObj.getJSONObject(m);
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("tp"), thousandthStyle);
		}
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(versionInfoObj.getString("weightTp"),7), thousandthStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, countValue( versionInfoObj.getString("msrp"),versionInfoObj.getString("weightTp")), thousandthStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, versionInfoObj.getString("rebatePrice"), thousandthStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, versionInfoObj.getString("rewardAssessment"), thousandthStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, versionInfoObj.getString("promotionalAllowance"), thousandthStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, versionInfoObj.getString("invoicePrice"), thousandthStyle);
		
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(versionInfoObj.getString("modelProfit"),7), thousandthStyle);
		
		s.setDisplayGridlines(false);
	}
	
	/**
	 * 根据语言类型获取值
	 * @param chartObj
	 * @param languageType
	 * @param property
	 * @return
	 */
	public String getValueByLanguageType(JSONObject obj,String languageType,String property)
	{
		String value = "";
		if("EN".equals(languageType)) value = obj.getString(property + "En");
		else value = obj.getString(property);
		return value;
	}
	
	/**
	 * 计算
	 * @param v1
	 * @param v2
	 * @return
	 */
	public String countValue(String v1,String v2)
	{
		String value = "-";
		try {
			if(!"-".equals(v1) && !"-".equals(v2) && !AppFrameworkUtil.isEmpty(v1) && !AppFrameworkUtil.isEmpty(v2)) 
			{
				value = Float.parseFloat(v1) - Float.parseFloat(v2) + "";
			}
		} catch (Exception e) {
			
		}
		return value;
	}
	
	/**
	 * 计算带百分号
	 * @param v1
	 * @param v2
	 * @return
	 */
	public String countPercentage(String v1,String v2)
	{
		String value = "-";
		if(!"-".equals(v1) && !"-".equals(v2) ) 
		{
			value = Float.parseFloat(v1) / Float.parseFloat(v2) * 100 + "";
		}
		return value;
	}
	
	/**
	 * 获取当前月以及上月时间
	 * @param time
	 * @return
	 */
	public String[] getMonth(String time)
	{
		String[] arrs = new String[2];
		Calendar c = null;
		try {
			c = Calendar.getInstance();
			c.setTime(new SimpleDateFormat("yyyyMM").parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int month = c.get(Calendar.MONTH);
		arrs[0]= month + 1 + "";//当前月
		if(0 == month) arrs[1] = "12";//上一月
		else arrs[1] = month + "";
		
		return arrs;
	}
	
	
}
