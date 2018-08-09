package com.ways.app.salesQuery.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.framework.utils.Constant;
import com.ways.app.framework.utils.ExportExcelUtil;
import com.ways.app.price.model.Segment;
import com.ways.app.salesQuery.dao.SalesAmountFawDao;
import com.ways.app.salesQuery.model.SalesAmountFawEntity;
import com.ways.app.salesQuery.service.SalesAmountFawManager;

@SuppressWarnings("deprecation")
@Service("salesAmountFawManager")
public class SalesAmountFawManagerImpl implements SalesAmountFawManager {

	@Autowired
	private  SalesAmountFawDao salesAmountFawDao;
	
	/**
	 * 实始化时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initDate(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		List<Map<String, String>> list = (List<Map<String, String>>) salesAmountFawDao.initDate(paramsMap);
		if(null != list && 0 != list.size())
		{
			String beginDate = list.get(0).get("BEGINDATE");
			String endDate = list.get(0).get("ENDDATE");
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("defaultBeginDate", endDate);
		}
	}
	
	/**
	 * 获取销售比例查询数据
	 */
	@SuppressWarnings("unused")
	@Override
	public String getSalesAmountFawData(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		int salesType = Integer.parseInt(paramsMap.get("salesType").toString());//0为频数，1为占比
		int frequencyType = Integer.parseInt(paramsMap.get("frequencyType").toString());//1,月，2，季度，3，年
		int objectType = Integer.parseInt(paramsMap.get("objectType").toString());//1,车型，2，厂商，3，级别
		String json = "";
		//保存查询条件
		Map<String, Object> saveMap = new HashMap<String, Object>();
		saveMap.putAll(paramsMap);
		List<SalesAmountFawEntity> list = null;
		List<SalesAmountFawEntity> list2 = null;
		List<SalesAmountFawEntity> list3 = null;
		List<SalesAmountFawEntity> list4 = null;
		List<SalesAmountFawEntity> list5 = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			 list2 = salesAmountFawDao.getSalesAmountFawGroupByObjectData(paramsMap);//对象详细信息以及对象总计销量数据
			
			 if(salesType == 1){
				 list4 = salesAmountFawDao.getSalesAmountFawGroupByMonthObjectPercentData(paramsMap);//年月占比数据
			 } else{
				 	list3 = salesAmountFawDao.getSalesAmountFawGroupByMonthData(paramsMap);//以年月分组总计销量数据
					list = salesAmountFawDao.getSalesAmountFawGroupByMonthObjectData(paramsMap);//年月销量数据
					list5 = salesAmountFawDao.getSalesAmountFawTotalData(paramsMap);//销量总计数据
			 }
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(null != list2 && 0 != list2.size())
		{
		  try{
			  resultMap.put("monthObjectData", list);
			  resultMap.put("objectData", list2);
			  resultMap.put("monthData", list3);
			  resultMap.put("monthObjectPercentData", list4);
			  resultMap.put("totalData", list5);
			  json = AppFrameworkUtil.structureConfigParamsGroupJSONData(resultMap);
		     } catch (Exception e) {
				e.printStackTrace();
			 }
		  	request.getSession().removeAttribute(Constant.getSalesAmountFawDataExportKey);
		  	request.getSession().removeAttribute(Constant.getSalesAmountFawDataParamsMapKey);
			request.getSession().setAttribute(Constant.getSalesAmountFawDataExportKey, json);
			request.getSession().setAttribute(Constant.getSalesAmountFawDataParamsMapKey, saveMap);
		}
		return json;
	}
	
	@Override
	public void getSubmodelModal(HttpServletRequest request, Map<String, Object> paramsMap) 
	{	
		Object subModelShowType = paramsMap.get("subModelShowType");
		try {
	    	if("2".equals(subModelShowType)){
	    		List<Segment> list=salesAmountFawDao.getSubmodelBySegment(paramsMap);
				//返回细分市场页
	    		request.setAttribute("segmentList", list);
	    	}else if("3".equals(subModelShowType)){
	    		//返回品牌页
		    	request.setAttribute("brandLetterList", salesAmountFawDao.getSubmodelByBrand(paramsMap));
	    	}else if("4".equals(subModelShowType)){
	    		//返回厂商页
		    	request.setAttribute("manfLetterList", salesAmountFawDao.getSubmodelByManf(paramsMap));
	    	}else{
	    		//本品页竟品页
		    	request.setAttribute("bpSubModelList", salesAmountFawDao.getSubmodelByBp(paramsMap));
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getBodyType(HttpServletRequest request, Map<String, Object> paramsMap) {
		try {
			request.setAttribute("bodyTypeList", salesAmountFawDao.getBodyType(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取一汽大众常用型号组
	 */
	@Override
	public void getAutoCustomGroup(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("autoCustomGroupList", salesAmountFawDao.getAutoCustomGroup(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出Excel
	 */
	@Override
	public Workbook exportExcel(HttpServletRequest request,
		Map<String, Object> paramsMap) {
		Workbook wb = null;
		String json = (String) request.getSession().getAttribute(Constant.getSalesAmountFawDataExportKey);
		
		//获取保存条件
		@SuppressWarnings("unchecked")
		HashMap<String, Object> conditionMap = (HashMap<String, Object>) request.getSession().getAttribute("getSalesAmountFawDataParamsMapKey");
		conditionMap.put("languageType", request.getParameter("languageType"));
		String path = request.getSession().getServletContext().getRealPath("/"); 
		if(!AppFrameworkUtil.isEmpty(json))
		{
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			try {
				
					wb = new HSSFWorkbook(new FileInputStream(new File(path+"excelExample/salesAmountFawTemplate.xls")));
					exportOriginalData(wb,obj,conditionMap);
				
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
	public void exportOriginalData(Workbook wb,JSONObject obj,Map<String, Object> paramsMap)
	{
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		int salesType = Integer.parseInt(paramsMap.get("salesType").toString());//0为显示销量，1为显示占比；
		//int frequencyType = Integer.parseInt(paramsMap.get("frequencyType").toString());//1，月2，季度3，年
		int objectType = Integer.parseInt(paramsMap.get("objectType").toString());//1,车型2，厂商3，级别
		String[] titles = null;//标题数组
			if(!languageType.equals("EN")){
				if(objectType == 1){
					titles = new String[]{"级别","生产厂商","车型"};
				} else if(objectType == 2){
					titles = new String[]{"生产厂商"};
				} else{
					titles = new String[]{"级别"};
				}
			} else{
				if(objectType == 1){
					titles = new String[]{"Grade","Manf","Submodel"};
				} else if(objectType == 2){
					titles = new String[]{"Manf"};
				} else{
					titles = new String[]{"Grade"};
				}	
			}
			JSONArray monthData = null;
			JSONArray monthDataT = null;
			JSONArray totalDataT = null;
			if(salesType == 0){
				monthData = (JSONArray) obj.getJSONArray("monthObjectData");
				monthDataT = (JSONArray) obj.getJSONArray("monthData");
				totalDataT = (JSONArray) obj.getJSONArray("totalData");
			} else{
				monthData =  (JSONArray) obj.getJSONArray("monthObjectPercentData");
			}
			JSONArray objectData = (JSONArray) obj.getJSONArray("objectData");
			
		
		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFSheet sheet = (HSSFSheet)wb.getSheet("数据显示");
		
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
//		CellStyle textRedStyle= ExportExcelUtil.getExcelFillTextStyle(wb,"RED");//内容文本样式
		CellStyle textPercentStyle = getExcelFillPercentageStyle(wb);//
		//CellStyle textPercentRedStyle = ExportExcelUtil.getExcelFillPercentageRedStyle(wb);//红色
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//内容文本样式
		//CellStyle titleStyle1 = getExcelTitleBackgroundStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		//CellStyle thousandthREDStyle =ExportExcelUtil.getExcelFormatThousandthREDStyle(wb);
		sheet.setColumnWidth(0, 7000);
		int width = 3000;
		
		//写标题内容
		row = (HSSFRow) sheet.createRow(0);
		row.setHeightInPoints(46);//行高
		for(int i = 0; i < titles.length; i++){
			cell = row.createCell(i);
			cell.setCellValue(titles[i]);
			cell.setCellStyle(titleStyle);
		}
		
		//月份部分
		
		//首先先创建空的标题样式单元格
		JSONObject monthDataObj = (JSONObject) monthData.get(0);
		String ym =  monthDataObj.getString("yearMonth");
		
		cell = row.createCell(titles.length);
		cell.setCellValue(ym);
		cell.setCellStyle(titleStyle);
		sheet.setColumnWidth(titles.length, width);

		
		int k = titles.length;
		for(int i = 0; i < monthData.size(); i++){
			 monthDataObj=(JSONObject) monthData.get(i);
			if(!ym.equals(monthDataObj.getString("yearMonth"))){
					k++;
					cell = row.createCell(k);
					cell.setCellValue(monthDataObj.getString("yearMonth"));
					cell.setCellStyle(titleStyle);
					sheet.setColumnWidth(k, width);
				ym = monthDataObj.getString("yearMonth");
				
			}
		}
		
		//总计部分
	if(salesType == 0){
		cell = row.createCell(k+1);
		if(!languageType.equals("EN")){
			cell.setCellValue("总计");
		} else{
			cell.setCellValue("Total");
		}
		cell.setCellStyle(titleStyle);
	}
	
		
		
		
		
		
		//写表格数据内容
		for(int i = 0; i < objectData.size(); i++)
		{
			JSONObject objectDataObj = (JSONObject) objectData.get(i);
			
			String manfName = null;
			String objName = null;
			if(!languageType.equals("EN")){
				manfName = objectDataObj.getString("manfName");
				objName = objectDataObj.getString("objName");
			} else{
				manfName = objectDataObj.getString("manfNameEn");
				objName = objectDataObj.getString("objNameEn");
			}
			
			row = (HSSFRow) sheet.createRow(1 + i);
			row.setHeightInPoints(20);//行高
			int j=0;
			
			if(objectType == 1){
				cell = row.createCell(j++);
				cell.setCellValue(objectDataObj.getString("gradeName"));
				cell.setCellStyle(textStyle);
				
				cell = row.createCell(j++);
				cell.setCellValue(manfName);
				cell.setCellStyle(textStyle);
			}
			
			cell = row.createCell(j++);
			cell.setCellValue(objName);
			cell.setCellStyle(textStyle);
			
			monthDataObj=(JSONObject) monthData.get(0);
			 ym =  monthDataObj.getString("yearMonth");
			 for(int t = 0; t < monthData.size(); t++){
				 
				 if(objectDataObj.getString("objId").equals(((JSONObject) monthData.get(t)).getString("objId")) && ((JSONObject) monthData.get(t)).getString("yearMonth").equals(ym)){
					 cell = row.createCell(j++);
					 if(salesType == 0){
						 if(null !=((JSONObject) monthData.get(t)).getString("sales") && !"".equals(((JSONObject) monthData.get(t)).getString("sales"))){
							if(0 == ((JSONObject) monthData.get(t)).getInteger("sales")){
								 cell.setCellValue(0);
								 cell.setCellStyle(textStyle);
							 } else{
								 cell.setCellValue(((JSONObject) monthData.get(t)).getInteger("sales"));
								 cell.setCellStyle(thousandthStyle);
							 }
						 } else{
							 cell.setCellValue("-");
							 cell.setCellStyle(textStyle);
						 }
					 } else{
						 if(null !=((JSONObject) monthData.get(t)).getString("percent") && !"".equals(((JSONObject) monthData.get(t)).getString("percent"))){
							 cell.setCellValue(Double.parseDouble(((JSONObject) monthData.get(t)).getString("percent")));
							 cell.setCellStyle(textPercentStyle);
						 } else{
							 cell.setCellValue("-");
							 cell.setCellStyle(textStyle);
						 }
					 }
				 }
			 }
			 
			 for(int t = 0; t < monthData.size(); t++){
				 if(objectDataObj.getString("objId").equals(((JSONObject) monthData.get(t)).getString("objId")) && !((JSONObject) monthData.get(t)).getString("yearMonth").equals(ym)){
					 cell = row.createCell(j++);
					 if(salesType == 0){
						 if(null !=((JSONObject) monthData.get(t)).getString("sales") && !"".equals(((JSONObject) monthData.get(t)).getString("sales"))){
							 if(0 == ((JSONObject) monthData.get(t)).getInteger("sales")){
								 cell.setCellValue(0);
								 cell.setCellStyle(textStyle);
							 } else{
								 cell.setCellValue(((JSONObject) monthData.get(t)).getInteger("sales"));
								 cell.setCellStyle(thousandthStyle);
							 }
						 } else{
							 cell.setCellValue("-");
							 cell.setCellStyle(textStyle);
						 }
					 } else{
						 if(null !=((JSONObject) monthData.get(t)).getString("percent") && !"".equals(((JSONObject) monthData.get(t)).getString("percent"))){
							 cell.setCellValue(Double.parseDouble(((JSONObject) monthData.get(t)).getString("percent")));
							 cell.setCellStyle(textPercentStyle);
						 } else{
							 cell.setCellValue("-");
							 cell.setCellStyle(textStyle);
						 }
					 }
				 }
			 }
			 
			 //总计部分
			 if(salesType == 0){
						 cell = row.createCell(j++);
						 if(null !=objectDataObj.getString("sales") && !"".equals(objectDataObj.getString("sales"))){
							 if(0 == objectDataObj.getInteger("sales")){
								 cell.setCellValue(0);
								 cell.setCellStyle(textStyle);
							 } else{
								 cell.setCellValue(objectDataObj.getInteger("sales"));
								 cell.setCellStyle(thousandthStyle);
							 }
						 } else{
							 cell.setCellValue("-");
							 cell.setCellStyle(textStyle);
						 }
			 }
			}
		//最后一行总计内容
		if(salesType == 0){
			row = (HSSFRow) sheet.createRow(objectData.size()+1);
			row.setHeightInPoints(20);//行高
			int j=0;
			
			cell = row.createCell(j++);
			if(!languageType.equals("EN")){
				cell.setCellValue("总计");
			} else{
				cell.setCellValue("Total");
			}
			cell.setCellStyle(textStyle);
			
			if(objectType == 1){
				cell = row.createCell(j++);
				cell.setCellValue("");
				cell.setCellStyle(textStyle);
				
				cell = row.createCell(j++);
				cell.setCellValue("");
				cell.setCellStyle(textStyle);
				
				//合并单元格,四个参数分别为，行，行，列，列           合并3个列
				sheet.addMergedRegion(new CellRangeAddress(objectData.size()+1,objectData.size()+1,0,2));
			} 
			
			//月份总计
			for(int i = 0; i < monthDataT.size(); i++){
				cell = row.createCell(j++);
				if(null != ((JSONObject)monthDataT.get(i)).getString("sales") && !"".equals(((JSONObject)monthDataT.get(i)).getString("sales"))){
					if(0 == ((JSONObject)monthDataT.get(i)).getInteger("sales")){
						cell.setCellValue(0);
						cell.setCellStyle(textStyle);
					} else {
						cell.setCellValue(((JSONObject)monthDataT.get(i)).getInteger("sales"));
						cell.setCellStyle(thousandthStyle);
					}
				} else{
					cell.setCellValue("-");
					cell.setCellStyle(textStyle);
				}
				
			}
			//最后总计
			cell = row.createCell(j++);
			if(null != ((JSONObject)totalDataT.get(0)).getString("sales") && !"".equals(((JSONObject)totalDataT.get(0)).getString("sales"))){
				if(0 == ((JSONObject)totalDataT.get(0)).getInteger("sales")){
					cell.setCellValue(0);
					cell.setCellStyle(textStyle);
				} else {
					cell.setCellValue(((JSONObject)totalDataT.get(0)).getInteger("sales"));
					cell.setCellStyle(thousandthStyle);
				}
			} else{
				cell.setCellValue("-");
				cell.setCellStyle(textStyle);
			}
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
	
	/**
	 * 获取Excel标题背景样式
	 * @param wb
	 * @return
	 */
	public static CellStyle getExcelTitleBackgroundStyle(Workbook wb)
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
	
	/**
	 * 获取Excel填充数百分号样式
	 * @param wb
	 * @return
	 */
	public static CellStyle getExcelFillPercentageStyle(Workbook wb)
	{
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();   
		font.setColor(IndexedColors.BLACK.index);//设置字体颜色
		style.setFont(font);//设置字体
		
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//设置单元格文本居中
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中  
		
		//设置背景颜色
		HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		customPalette.setColorAtIndex(HSSFColor.GREY_25_PERCENT.index, (byte) 214, (byte) 214, (byte) 214); 
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		
		//设置边框颜色
		style.setBorderLeft(CellStyle.SOLID_FOREGROUND);   
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());//设置左边框颜色
		style.setBorderBottom(CellStyle.SOLID_FOREGROUND);   
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());//设置下边框颜色
		style.setBorderRight(CellStyle.SOLID_FOREGROUND);   
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());//设置右边框颜色

		HSSFDataFormat df = (HSSFDataFormat) wb.createDataFormat();      
		style.setDataFormat(df.getFormat("0.00%"));//设置单元格数据格式
		return style;
	}
}
