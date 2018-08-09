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
import com.ways.app.framework.utils.EchartsUtil;
import com.ways.app.framework.utils.ExportExcelUtil;
import com.ways.app.price.dao.IVersionDiscountRatioDao;
import com.ways.app.price.model.EchartLineDataEntity;
import com.ways.app.price.model.EchartsMarkLineEntity;
import com.ways.app.price.model.VersionInfoEntity;
import com.ways.app.price.service.IVersionDiscountRatioManager;

/**
 * 型号折扣对比Service实现类
 * @author yinlue
 *
 */
@Service("VersionDiscountRatioManagerImpl")
public class VersionDiscountRatioManagerImpl implements IVersionDiscountRatioManager
{

	  @Autowired
	  private IVersionDiscountRatioDao dao;
	
	  /**
	   * 实始化时间 
	   */
	  public String initDate(HttpServletRequest request, Map<String, Object> paramsMap)
	  {
		    List<Map<String, String>> list = dao.initDate(paramsMap);
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
	   * 加载图形和表格
	   */
	  public String loadVersionDiscountRatioChartAndTable(HttpServletRequest request, Map<String, Object> paramsMap)
	  {
		    String json = "";
		    List<VersionInfoEntity> list = dao.loadVersionDiscountRatioChartAndTable(paramsMap);
		    if (list != null && list.size() != 0)
		    {
			      Map<String,Object> dataMap = new HashMap<String,Object>();
			      dataMap.put("grid", list);
			      dataMap.put("chart", getLineChartData(list));
			      json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			      request.getSession().setAttribute(Constant.getExportExcelDataKey, json);
		    }
		    return json;
	  }
	
	  /**
	   * 获取线图数据
	   * @param list
	   * @return
	   */
	  public Map<String, Object> getLineChartData(List<VersionInfoEntity> list)
	  {
		    Map<String,Object> map = new HashMap<String,Object>();
		    String tp = ""; String msrp = ""; String versionName = "";
		    //辅助线集合
		    List<List<EchartsMarkLineEntity>> markList = new ArrayList<List<EchartsMarkLineEntity>>();
		    for (int i = 0; i < list.size(); i++)
		    {
		      VersionInfoEntity info = (VersionInfoEntity)list.get(i);
		      String stp = "-"; String smsrp = "-";
		      if (!"-".equals(info.getTp())) stp = AppFrameworkUtil.getNum(Float.parseFloat(info.getTp()) / 1000, 1);
		      if (!"-".equals(info.getMsrp())) smsrp = AppFrameworkUtil.getNum(Float.parseFloat(info.getMsrp()) / 1000, 1);
		
		      if (i == 0)
		      {
		    	  versionName = info.getVersionName().replace("{0}", "\n");
		    	  tp = stp;
		    	  msrp = smsrp;
		      }
		      else
		      {
		    	  versionName = versionName + "," + info.getVersionName().replace("{0}", "\n");
		    	  tp = tp + "," + stp;
		    	  msrp = msrp + "," + smsrp;
		      }
		
		      List<EchartsMarkLineEntity> mark = new ArrayList<EchartsMarkLineEntity>();
		      mark.add(new EchartsMarkLineEntity(i, smsrp));
		      mark.add(new EchartsMarkLineEntity(i, stp));
		      markList.add(mark);
		    }
		    //线图数据结构
		    map.put("xTitle", versionName.split(","));
		    List<EchartLineDataEntity> seriesList = new ArrayList<EchartLineDataEntity>();
		    seriesList.add(new EchartLineDataEntity("line", "MSRP", "circle", msrp.split(",")));
		    seriesList.add(new EchartLineDataEntity("line", "TP", "circle", tp.split(",")));
		    map.put("series", seriesList);
		    map.put("markList", markList);
		    map.put("boundarys", EchartsUtil.setLineScaleDivisionToThousand(seriesList));
		    return map;
	  }

	  /**
	   * 导出
	   */
	@Override
	public Workbook exportExcel(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		Workbook wb = null;
		String json = (String) request.getSession().getAttribute(Constant.getExportExcelDataKey);
		String languageType = (String) paramsMap.get("languageType");//导出语言
		if(!AppFrameworkUtil.isEmpty(json))
		{
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			JSONArray gridObj = (JSONArray) obj.getJSONArray("grid");
			
			try {
				//读取模块路径
				String path = request.getSession().getServletContext().getRealPath("/"); 
				wb = new HSSFWorkbook(new FileInputStream(new File(path+"excelExample/discountRatio.xls")));
				//导图
				exportLineChartExcel(wb,wb.getSheet("DATA"),gridObj,languageType);
				//导表格
				exportOriginalData(wb,wb.getSheet("原数据"),gridObj,languageType,paramsMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	public void exportOriginalData(Workbook wb,Sheet s,JSONArray gridObj,String languageType,Map<String, Object> paramsMap)
	{
		String[] titles = null;//标题数组
		if("EN".equals(languageType)) 
		{
			titles = new String[]{"Date","Code","Segment","Manufacture","Model","Engine Capacity","Transmission","Trim","Bodytype","Launch Date","Model Year","MSRP(RMB)"
					,"TP（RMB）","Discount（RMB）","Discount Rate","VS Month","VS Change"};
		}
		else 
		{
			titles = new String[]{"日期","车型编码","级别","厂商","车型","排量","排档方式","型号标识","车身形式","上市日期","年式","厂商指导价"
					,"最低参考价加权值","折扣(元)","折扣率","VS上月","VS变化"};
		}
		int rowIndex = 0;//行号锁引
		int cellIndex = 0;//列锁引
		Row row = ExportExcelUtil.createRow(s, rowIndex, 400);
		
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		CellStyle percentageStyle = ExportExcelUtil.getExcelFillPercentageStyle(wb);//格式化百分号样式
		
		//保存线图刻度最大值与最小值，供数据部使用
		Cell cell = row.createCell(40);
		ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(paramsMap.get("ymax").toString(), 0)+".0~", textStyle);
		cell = row.createCell(41);
		ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(paramsMap.get("ymin").toString(), 0)+".0~", textStyle);
		cell = row.createCell(42);
		ExportExcelUtil.setCellValueAndStyle(cell, languageType, textStyle);
		
		//写标题
		for(String title : titles)
		{
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, title, titleStyle);
			s.setColumnWidth(cell.getColumnIndex(), 3000);
		}
		//写数据
		for(int i = 0; i < gridObj.size(); i++)
		{
			JSONObject obj = gridObj.getJSONObject(i);
			row = ExportExcelUtil.createRow(s, ++rowIndex, 400);
			cellIndex = 0;
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("yearMonth")+"~", textStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionCode")+"~", textStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"gradeName"), textStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"manfName"), textStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"subModelName"), textStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("discharge"), textStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"gearMode"), textStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"typeId"), textStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"bodyType"), textStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionLaunchDate"), textStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("modelYear"), textStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("msrp"), thousandthStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("tp"),7), thousandthStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("discount"),7), thousandthStyle);
			
			cell = row.createCell(cellIndex++);
			if("".equals(obj.getString("discountRate")) || "-".equals(obj.getString("discountRate"))){
				ExportExcelUtil.setCellValueToPercentage(cell, "-", percentageStyle);
			} else{
				ExportExcelUtil.setCellValueToPercentage(cell, obj.getString("discountRate"), percentageStyle);
			}
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("vsDiscount"),7), thousandthStyle);
			
			cell = row.createCell(cellIndex++);
			if("".equals(obj.getString("vsDiscountRate")) || "-".equals(obj.getString("vsDiscountRate"))){
				ExportExcelUtil.setCellValueToPercentage(cell, "-", percentageStyle);
			} else{
				ExportExcelUtil.setCellValueToPercentage(cell, obj.getString("vsDiscountRate"), percentageStyle);
			}
		}
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
	 * 导出线图图形数据
	 * @param wb
	 * @param s
	 * @param chartObj
	 */
	public void exportLineChartExcel(Workbook wb,Sheet s,JSONArray gridObj,String languageType)
	{
		String[] titles = null;//标题数组
		if("EN".equals(languageType)) 
		{
			titles = new String[]{"Version","MSRP","TP","TP","Discount","Discount Rate","VS Month","VS Change"};
		}
		else 
		{
			titles = new String[]{"型号","指导价","成交价","成交价","折扣","折扣率","VS上月","VS变化"};
		}
		int rowIndex = 0;//行号锁引
		int cellIndex = 0;//列锁引
		Row row = ExportExcelUtil.createRow(s, rowIndex, 400);
		Cell cell = null;
		
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle percentageStyle = ExportExcelUtil.getExcelFillPercentageStyle(wb);//格式化百分号样式
		//写标题
		for(String title : titles)
		{
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, title, titleStyle);
			s.setColumnWidth(cell.getColumnIndex(), 3000);
		}
		//写数据
		for(int i = 0; i < gridObj.size(); i++)
		{
			JSONObject obj = gridObj.getJSONObject(i);
			row = ExportExcelUtil.createRow(s, ++rowIndex, 400);
			cellIndex = 0;
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"versionName").replace("{0}", ""), textStyle);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell,AppFrameworkUtil.getNum(Float.parseFloat(obj.getString("msrp")) / 1000, 1), textStyle);
			
			cell = row.createCell(cellIndex++);
			if("-".equals(obj.getString("tp"))){
				ExportExcelUtil.setCellValueAndStyle(cell,"-", textStyle);
			} else{
				ExportExcelUtil.setCellValueAndStyle(cell,AppFrameworkUtil.getNum(Float.parseFloat(obj.getString("tp")) / 1000, 1), textStyle);
			}
			
			cell = row.createCell(cellIndex++);
			if("-".equals(obj.getString("tp"))){
				ExportExcelUtil.setCellValueAndStyle(cell,"-", textStyle);
			} else{
				ExportExcelUtil.setCellValueAndStyle(cell,AppFrameworkUtil.getNum(Float.parseFloat(obj.getString("tp")) / 1000, 1), textStyle);
			}
			
			cell = row.createCell(cellIndex++);
			if("-".equals(obj.getString("discount")) || "".equals(obj.getString("discount"))){
				ExportExcelUtil.setCellValueAndStyle(cell,"-", textStyle);
			} else{
				ExportExcelUtil.setCellValueAndStyle(cell,AppFrameworkUtil.getNum(Float.parseFloat(obj.getString("discount")) / 1000, 1), textStyle);
			}
			
			cell = row.createCell(cellIndex++);
			if("-".equals(obj.getString("discountRate")) || "".equals(obj.getString("discountRate"))){
				ExportExcelUtil.setCellValueToPercentage(cell,"-", percentageStyle);
			} else{
				ExportExcelUtil.setCellValueToPercentage(cell,obj.getString("discountRate"), percentageStyle);
			}
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValueAndStyle(cell,AppFrameworkUtil.getNum(obj.getString("vsDiscount"), 1), textStyle);
			
			cell = row.createCell(cellIndex++);
			if("-".equals(obj.getString("vsDiscountRate")) || "".equals(obj.getString("vsDiscountRate"))){
				ExportExcelUtil.setCellValueToPercentage(cell,"-", percentageStyle);
			} else{
				ExportExcelUtil.setCellValueToPercentage(cell,obj.getString("vsDiscountRate"), percentageStyle);
			}
			
		}
		s.setColumnWidth(0, 5500);
		s.setDisplayGridlines(false);
	}
}