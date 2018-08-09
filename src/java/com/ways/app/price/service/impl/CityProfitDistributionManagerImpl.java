package com.ways.app.price.service.impl;

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
import com.ways.app.price.dao.CityProfitDistributionDao;
import com.ways.app.price.model.EchartMapEntity;
import com.ways.app.price.model.VersionInfoEntity;
import com.ways.app.price.service.CityProfitDistributionManager;


/**
 * 车型利润分析Service层接口实现类
 * @author yinlue
 *
 */
@Service("cityProfitDistributionManager")
public class CityProfitDistributionManagerImpl implements CityProfitDistributionManager {

	@Autowired
	private CityProfitDistributionDao cityProfitDistributionDao;

	/**
	 * 初始时间
	 */
	@Override
	public String initDate(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		String json = "";//返回默认开始时间和结束时间
		List<Map<String, String>> list = cityProfitDistributionDao.initDate(paramsMap);
		if(null != list && 0 != list.size())
		{
			Map<String, String> resultMap = list.get(0);
			String endDate = resultMap.get("ENDDATE");
			request.setAttribute("beginDate", resultMap.get("BEGINDATE"));
			request.setAttribute("endDate", endDate);
			request.setAttribute("defaultBeginDate",endDate);
			json = resultMap.get("BEGINDATE");
		}
		return json;
	}

	/**
	 * 加载车型利润图形和表格
	 */
	@Override
	public String loadModelProfitChartAndTable(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		String json = "";
		List<VersionInfoEntity> list = cityProfitDistributionDao.loadModelProfitChartAndTable(paramsMap);
		if(null != list && 0 != list.size())
		{
			Map<String,Object> dataMap = getMapData(list);
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			request.getSession().setAttribute(Constant.getExportExcelDataKey, json);
		}
		return json;
	}
	
	/**
	 * 获取地图数据
	 * @param list
	 * @return
	 */
	public Map<String,Object> getMapData(List<VersionInfoEntity> list)
	{
		Map<String,Object> dataMap = new HashMap<String,Object>();
		
		List<EchartMapEntity> mapChatList1 = new ArrayList<EchartMapEntity>();
		List<EchartMapEntity> mapChatList2 = new ArrayList<EchartMapEntity>();
		List<EchartMapEntity> mapChatList3 = new ArrayList<EchartMapEntity>();
		
		
		for(VersionInfoEntity vie :list){
			if(vie.getCityName().equals("上海")){
			}
			double modelProfit = 0;
			try {
				//车型利润
				modelProfit = Double.parseDouble(vie.getModelProfit());
			} catch (Exception e) {
			} 
			double rewardAssessment = 0;
			try {
				//考核奖励
			   rewardAssessment = Double.parseDouble(vie.getRewardAssessment());
			} catch (Exception e) {
			}
			if(modelProfit>0){//利润>0
				EchartMapEntity eme = new EchartMapEntity();
				eme.setName(vie.getCityName());
				eme.setValue(Math.round(modelProfit)+"");
				mapChatList1.add(eme);
			}
			if(modelProfit<=0){//利润<=0
				EchartMapEntity eme = new EchartMapEntity();
				eme.setName(vie.getCityName());
				eme.setValue(Math.round(modelProfit)+"");
				mapChatList2.add(eme);
			}
			if((modelProfit+rewardAssessment)<=0){//利润+奖励<=0
				EchartMapEntity eme = new EchartMapEntity();
				eme.setName(vie.getCityName());
				eme.setValue(Math.round(modelProfit+rewardAssessment)+"");
				mapChatList3.add(eme);
			}
		}
		
		dataMap.put("chart1", mapChatList1);
		dataMap.put("chart2", mapChatList2);
		dataMap.put("chart3", mapChatList3);
		dataMap.put("grid", list);
		
		if(list.size()>0&&list.get(0)!=null&&list.get(0).getVersionChartName()!=null)
		dataMap.put("versionName", list.get(0).getVersionChartName());
		
		return dataMap;
	}
	
	
	public Workbook exportExcel(HttpServletRequest request,
			Map<String, Object> paramsMap) {
		Workbook wb = null;
		String json = (String) request.getSession().getAttribute(Constant.getExportExcelDataKey);
		if(!AppFrameworkUtil.isEmpty(json))
		{
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			JSONArray gridObj = (JSONArray) obj.getJSONArray("grid");
			
			try {
				String path = request.getSession().getServletContext().getRealPath("/"); 
				wb = new HSSFWorkbook(new FileInputStream(new File(path+"excelExample/cityProfitDistribution.xls")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			paramsMap.put("languageType", request.getParameter("languageType"));
			exportOriginalData(wb,wb.getSheet("数据显示"),gridObj,paramsMap);
//			exportOriginalData(wb,wb.getSheet("data"),gridObj,paramsMap);
		}
		return wb;
	}
	
	/**
	 * 导出原始数据
	 * @param wb
	 * @param s
	 * @param gridObj
	 * @param paramsMap
	 */
	public void exportOriginalData(Workbook wb,Sheet s,JSONArray gridObj,Map<String, Object> paramsMap)
	{
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		String[] titles = null;//标题数组
		if("EN".equals(languageType)) 
		{
			titles = new String[]{"Date","Code","Segment","Manufacture","Model","Engine Capacity","Transmission","Trim","Bodytype","Launch Date","MSRP(RMB)","City"
					,"TP（RMB）","Seller Cost","Invoice Price","Margin","Bonus","Tactical","Model Profit"};
		}
		else 
		{
			titles = new String[]{"日期","车型编码","级别","生产厂","车型","排量","排档方式","型号标识","车身形式","上市日期","指导价","城市"
					,"成交价","经销商成本","开票价","激励额","考核奖励","促销补贴","车型利润"};
		}
		int rowIndex = 0;//行号索引
		Row row = ExportExcelUtil.createRow(s, rowIndex, 600);
		Cell cell = null;
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		
		
		//写表格标题
		for(int i = 0; i < titles.length; i++)
		{
			cell = row.createCell(i);
			ExportExcelUtil.setCellValueAndStyle(cell, titles[i], titleStyle);
			if(i==1)s.setColumnWidth(i, 5500);else if(i==7)s.setColumnWidth(i, 5500);else s.setColumnWidth(i, 3000);
		}
		//写表格数据内容
		for(int j = 0; j < gridObj.size(); j++)
		{
			JSONObject obj = gridObj.getJSONObject(j);
			rowIndex++;
			row = ExportExcelUtil.createRow(s, rowIndex, 400);
			
			cell = row.createCell(0);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("yearMonth")+" ", textStyle);
			
			cell = row.createCell(1);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionCode")+" ", textStyle);
			
			cell = row.createCell(2);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"gradeName"), textStyle);
			
			cell = row.createCell(3);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"manfName"), textStyle);
			
			cell = row.createCell(4);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"subModelName"), textStyle);
			
			cell = row.createCell(5);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("discharge"), textStyle);
			
			cell = row.createCell(6);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"gearMode"), textStyle);
			
			cell = row.createCell(7);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"typeId"), textStyle);
			
			cell = row.createCell(8);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"bodyType"), textStyle);
			
//			cell = row.createCell(9);
//			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"versionName"), textStyle);
			
			cell = row.createCell(9);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionLaunchDate"), textStyle);
			
			cell = row.createCell(10);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("msrp"), thousandthStyle);
			
			cell = row.createCell(11);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"cityName"), textStyle);
			
			cell = row.createCell(12);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("tp"),7), thousandthStyle);
			
			cell = row.createCell(13);
			ExportExcelUtil.setCellValueAndStyle(cell,  obj.getString("sellerCost"), thousandthStyle);
			
			cell = row.createCell(14);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("invoicePrice"), thousandthStyle);
			
			cell = row.createCell(15);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("rebatePrice"), thousandthStyle);
			
			cell = row.createCell(16);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("rewardAssessment"), thousandthStyle);
			
			cell = row.createCell(17);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("promotionalAllowance"), thousandthStyle);
			
			cell = row.createCell(18);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("modelProfit"),7), thousandthStyle);
		}
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
}
