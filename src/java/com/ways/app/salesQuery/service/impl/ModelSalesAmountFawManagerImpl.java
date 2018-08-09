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
import com.ways.app.salesQuery.dao.ModelSalesAmountFawDao;
import com.ways.app.salesQuery.model.ModelSalesAmountFawEntity;
import com.ways.app.salesQuery.service.ModelSalesAmountFawManager;

@SuppressWarnings("deprecation")
@Service("modelSalesAmountFawManager")
public class ModelSalesAmountFawManagerImpl implements ModelSalesAmountFawManager {

	@Autowired
	private  ModelSalesAmountFawDao modelSalesAmountFawDao;
	
	/**
	 * 实始化时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initDate(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		List<Map<String, String>> list = (List<Map<String, String>>) modelSalesAmountFawDao.initDate(paramsMap);
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
	public String getModelSalesAmountFawData(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		//保存查询条件
		Map<String, Object> saveMap = new HashMap<String, Object>();
		saveMap.putAll(paramsMap);
		List<ModelSalesAmountFawEntity> list = null;
		List<ModelSalesAmountFawEntity> list2 = null;
		List<Map<String, Object>> list3 = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			list = modelSalesAmountFawDao.getModelSalesAmountFawData(paramsMap);//年月数据
			 list2 = modelSalesAmountFawDao.getTotalData(paramsMap);//总计数据
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(null != list && 0 != list.size())
		{
		  try{
			  resultMap.put("monthData", list);
			  resultMap.put("totalData", list2);
				json = AppFrameworkUtil.structureConfigParamsGroupJSONData(resultMap);
		     } catch (Exception e) {
				e.printStackTrace();
			 }
		  	request.getSession().removeAttribute(Constant.getModelSalesAmountFawDataExportKey);
		  	request.getSession().removeAttribute(Constant.getModelSalesAmountFawDataParamsMapKey);
			request.getSession().setAttribute(Constant.getModelSalesAmountFawDataExportKey, json);
			request.getSession().setAttribute(Constant.getModelSalesAmountFawDataParamsMapKey, saveMap);
		}
		return json;
	}
	
	@Override
	public void getSubmodelModal(HttpServletRequest request, Map<String, Object> paramsMap) 
	{	
		Object subModelShowType = paramsMap.get("subModelShowType");
		try {
	    	if("2".equals(subModelShowType)){
	    		List<Segment> list=modelSalesAmountFawDao.getSubmodelBySegment(paramsMap);
				//返回细分市场页
	    		request.setAttribute("segmentList", list);
	    	}else if("3".equals(subModelShowType)){
	    		//返回品牌页
		    	request.setAttribute("brandLetterList", modelSalesAmountFawDao.getSubmodelByBrand(paramsMap));
	    	}else if("4".equals(subModelShowType)){
	    		//返回厂商页
		    	request.setAttribute("manfLetterList", modelSalesAmountFawDao.getSubmodelByManf(paramsMap));
	    	}else{
	    		//本品页竟品页
		    	request.setAttribute("bpSubModelList", modelSalesAmountFawDao.getSubmodelByBp(paramsMap));
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getBodyType(HttpServletRequest request, Map<String, Object> paramsMap) {
		try {
			request.setAttribute("bodyTypeList", modelSalesAmountFawDao.getBodyType(paramsMap));
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
			request.setAttribute("autoCustomGroupList", modelSalesAmountFawDao.getAutoCustomGroup(paramsMap));
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
		String json = (String) request.getSession().getAttribute(Constant.getModelSalesAmountFawDataExportKey);
		
		//获取保存条件
		@SuppressWarnings("unchecked")
		HashMap<String, Object> conditionMap = (HashMap<String, Object>) request.getSession().getAttribute("getModelSalesAmountFawDataParamsMapKey");
		conditionMap.put("languageType", request.getParameter("languageType"));
		String path = request.getSession().getServletContext().getRealPath("/"); 
		if(!AppFrameworkUtil.isEmpty(json))
		{
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			try {
				
					wb = new HSSFWorkbook(new FileInputStream(new File(path+"excelExample/modelSalesAmountFawTemplate.xls")));
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
		int salesType = Integer.parseInt(paramsMap.get("salesType").toString());//0为显示销量，1为隐藏销量；
		int totalType = Integer.parseInt(paramsMap.get("totalType").toString());//0为显示累计，1为隐藏累计；
		String[] titles = null;//标题数组
			if(!languageType.equals("EN")){
				titles = new String[]{"型号编码","级别","生产厂商","车型","排量","排挡方式","型号标识","车身形式","上市日期","年款","厂商指导价(元)"};
			} else{
				titles = new String[]{"VersionCode","Grade","Manf","Submodel","Displacement","GearMode","VersionShortName","BodyType","LaunchDate","Year","MSRP"};
			}
			JSONArray monthData = (JSONArray) obj.getJSONArray("monthData");
			JSONArray totalData = (JSONArray) obj.getJSONArray("totalData");
		
		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFSheet sheet = (HSSFSheet)wb.getSheet("数据显示");
		
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
//		CellStyle textRedStyle= ExportExcelUtil.getExcelFillTextStyle(wb,"RED");//内容文本样式
		CellStyle textPercentStyle = getExcelFillPercentageStyle(wb);//
		//CellStyle textPercentRedStyle = ExportExcelUtil.getExcelFillPercentageRedStyle(wb);//红色
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//内容文本样式
		CellStyle titleStyle1 = getExcelTitleBackgroundStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		//CellStyle thousandthREDStyle =ExportExcelUtil.getExcelFormatThousandthREDStyle(wb);
		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 3000);
		int width = 3000;
		sheet.setColumnWidth(11, width);
		sheet.setColumnWidth(12, width);
		sheet.setColumnWidth(13, width);
		
		//第二行
		JSONObject monthDataObj = (JSONObject) monthData.get(0);
		row = (HSSFRow) sheet.createRow(1);
		row.setHeightInPoints(23);//行高
		for(int i=0; i < titles.length;i++){
			cell = row.createCell(i);
			cell.setCellValue("");
			cell.setCellStyle(titleStyle);
		}
		String ym =  monthDataObj.getString("yearMonth");
		
		if(!languageType.equals("EN")){
			if(salesType == 0){
				cell = row.createCell(11);
				cell.setCellValue("车型销量");
				cell.setCellStyle(titleStyle);
				cell = row.createCell(12);
				cell.setCellValue("销售比例");
				cell.setCellStyle(titleStyle);
				cell = row.createCell(13);
				cell.setCellValue("型号销量");
				cell.setCellStyle(titleStyle);
			} else{
				cell = row.createCell(11);
				cell.setCellValue("销售比例");
				cell.setCellStyle(titleStyle);
			}
			
		} else{
			if(salesType == 0){
				cell = row.createCell(11);
				cell.setCellValue("SubModelSale");
				cell.setCellStyle(titleStyle);
				cell = row.createCell(12);
				cell.setCellValue("SalesRatio");
				cell.setCellStyle(titleStyle);
				cell = row.createCell(13);
				cell.setCellValue("VersionSale");
				cell.setCellStyle(titleStyle);
			} else{
				cell = row.createCell(11);
				cell.setCellValue("SalesRatio");
				cell.setCellStyle(titleStyle);
			}
			
		}
		
		
		int k = 0;
		int num = 0;
		if(salesType == 0){
			 num = 14;
		} else{
			 num = 12;
		}
		
		for(int i = 0; i < monthData.size(); i++){
			monthDataObj = (JSONObject) monthData.get(i);
			if(!ym.equals(monthDataObj.getString("yearMonth"))){
				k++;
				if(!languageType.equals("EN")){
					if(salesType == 0){
						cell = row.createCell(num);
						sheet.setColumnWidth(num, width);//设置宽度
						cell.setCellValue("车型销量");
						cell.setCellStyle(titleStyle);
						cell = row.createCell(num + 1);
						sheet.setColumnWidth(num + 1, width);//设置宽度
						cell.setCellValue("销售比例");
						cell.setCellStyle(titleStyle);
						cell = row.createCell(num + 2);
						sheet.setColumnWidth(num + 2, width);//设置宽度
						cell.setCellValue("型号销量");
						cell.setCellStyle(titleStyle);
					} else{
						cell = row.createCell(num);
						sheet.setColumnWidth(num, width);//设置宽度
						cell.setCellValue("销售比例");
						cell.setCellStyle(titleStyle);
					}
					
				} else{
					if(salesType == 0){
						cell = row.createCell(num);
						sheet.setColumnWidth(num, width);//设置宽度
						cell.setCellValue("SubmodelSale");
						cell.setCellStyle(titleStyle);
						cell = row.createCell(num + 1);
						sheet.setColumnWidth(num + 1, width);//设置宽度
						cell.setCellValue("SaleRatio");
						cell.setCellStyle(titleStyle);
						cell = row.createCell(num + 2);
						sheet.setColumnWidth(num + 2, width);//设置宽度
						cell.setCellValue("VersionSale");
						cell.setCellStyle(titleStyle);
					} else{
						cell = row.createCell(num);
						sheet.setColumnWidth(num, width);//设置宽度
						cell.setCellValue("SaleRatio");
						cell.setCellStyle(titleStyle);
					}
					
				}
				if(salesType == 0){
					num = 14 + 3*k;
				} else{
					num++;
				}	
				ym = monthDataObj.getString("yearMonth");
			}
		}
		//总计部分
		if(totalType == 0){
			if(!languageType.equals("EN")){
				if(salesType == 0){
					cell = row.createCell(num);
					sheet.setColumnWidth(num, width);//设置宽度
					cell.setCellValue("车型销量");
					cell.setCellStyle(titleStyle);
					cell = row.createCell(num+1);
					sheet.setColumnWidth(num+1, width);//设置宽度
					cell.setCellValue("销售比例");
					cell.setCellStyle(titleStyle);
					cell = row.createCell(num+2);
					sheet.setColumnWidth(num+2, width);//设置宽度
					cell.setCellValue("型号销量");
					cell.setCellStyle(titleStyle);
				} else{
					cell = row.createCell(num);
					sheet.setColumnWidth(num, width);//设置宽度
					cell.setCellValue("销售比例");
					cell.setCellStyle(titleStyle);
				}
				
			} else{
				if(salesType == 0){
					cell = row.createCell(num);
					sheet.setColumnWidth(num, width);//设置宽度
					cell.setCellValue("SubmodelSale");
					cell.setCellStyle(titleStyle);
					cell = row.createCell(num+1);
					sheet.setColumnWidth(num+1, width);//设置宽度
					cell.setCellValue("SaleRatio");
					cell.setCellStyle(titleStyle);
					cell = row.createCell(num+2);
					sheet.setColumnWidth(num+2, width);//设置宽度
					cell.setCellValue("VersionSale");
					cell.setCellStyle(titleStyle);
				} else{
					cell = row.createCell(num);
					sheet.setColumnWidth(num, width);//设置宽度
					cell.setCellValue("SaleRatio");
					cell.setCellStyle(titleStyle);
				}
				
			}
		}
		
		
		
		//写标题内容
		row = (HSSFRow) sheet.createRow(0);
		row.setHeightInPoints(23);//行高
		for(int i = 0; i < titles.length; i++){
			cell = row.createCell(i);
			cell.setCellValue(titles[i]);
			cell.setCellStyle(titleStyle);
			//合并单元格,四个参数分别为，行，行，列，列
				sheet.addMergedRegion(new CellRangeAddress(0,1,i,i));
		}
		
		//月份部分
		
		//首先先创建空的标题样式单元格
		 monthDataObj=(JSONObject) monthData.get(0);
		 ym =  monthDataObj.getString("yearMonth");
		 k = 0;
		 int L = 0;
		 if(salesType == 0){
			 L = 13;
		 } else{
			 L = 11;
		 }
			for(int i = 0; i < monthData.size(); i++){
				 monthDataObj = (JSONObject) monthData.get(i);
				if(!ym.equals(monthDataObj.getString("yearMonth"))){
					k++;
					if(salesType == 0){
						L = 14+3*k-1;//获得最后一个单元格的位置
					} else{
						L = 10+k;
					}
					ym = monthDataObj.getString("yearMonth");
				}
			}
			
		for(int i = 11; i < L+1; i++){
			cell = row.createCell(i);
			cell.setCellValue("");
			cell.setCellStyle(titleStyle1);
		}
		
		monthDataObj=(JSONObject) monthData.get(0);
		 ym =  monthDataObj.getString("yearMonth");
		cell = row.createCell(11);
		cell.setCellValue(ym);
		cell.setCellStyle(titleStyle1);
		//合并单元格,四个参数分别为，行，行，列，列
		if(salesType == 0){
			sheet.addMergedRegion(new CellRangeAddress(0,0,11,13));
		}
		
		 k = 0;
		for(int i = 0; i < monthData.size(); i++){
			 monthDataObj=(JSONObject) monthData.get(i);
			if(!ym.equals(monthDataObj.getString("yearMonth"))){
				k++;
				if(salesType == 0){
					cell = row.createCell(14 + (k-1)*3);
					cell.setCellValue(monthDataObj.getString("yearMonth"));
					cell.setCellStyle(titleStyle1);
					//合并单元格,四个参数分别为，行，行，列，列
					sheet.addMergedRegion(new CellRangeAddress(0,0,14+3*(k-1),14+3*k-1));
				} else{
					cell = row.createCell(11 + k);
					cell.setCellValue(monthDataObj.getString("yearMonth"));
					cell.setCellStyle(titleStyle1);
					
				}
				ym = monthDataObj.getString("yearMonth");
				if(salesType != 0 && k == 1){
					L = L+1;
				}
			}
		}
		
		//总计部分
	if(totalType == 0){
		cell = row.createCell(L+1);
		if(!languageType.equals("EN")){
			cell.setCellValue("总计");
		} else{
			cell.setCellValue("Total");
		}
		cell.setCellStyle(titleStyle1);
		if(salesType == 0){
			cell = row.createCell(L+2);
			cell.setCellValue("");
			cell.setCellStyle(titleStyle1);
			cell = row.createCell(L+3);
			cell.setCellValue("");
			cell.setCellStyle(titleStyle1);
			//合并单元格,四个参数分别为，行，行，列，列
			sheet.addMergedRegion(new CellRangeAddress(0,0,L+1,L+3));
		} 
	}
	
		
		
		
		
		
		//写表格数据内容
		for(int i = 0; i < totalData.size(); i++)
		{
			JSONObject totalDataObj = (JSONObject) totalData.get(i);
			
			String manfName = null;
			String objName = null;
			String versionName = null;
			String bodyType = null;
			String pd = null;
			if(!languageType.equals("EN")){
				manfName = totalDataObj.getString("manfName");
				objName = totalDataObj.getString("objName");
				versionName = totalDataObj.getString("versionName");
				bodyType = totalDataObj.getString("bodyType");
				pd = totalDataObj.getString("pd");
			} else{
				manfName = totalDataObj.getString("manfNameEn");
				objName = totalDataObj.getString("objNameEn");
				versionName = totalDataObj.getString("versionNameEn");
				bodyType = totalDataObj.getString("bodyTypeEn");
				pd = totalDataObj.getString("pdEn");
			}
			
			row = (HSSFRow) sheet.createRow(2 + i);
			row.setHeightInPoints(20);//行高
			int j=0;
			cell = row.createCell(j++);
			cell.setCellValue(totalDataObj.getString("versionCode"));
			cell.setCellStyle(textStyle);
			
			cell = row.createCell(j++);
			cell.setCellValue(totalDataObj.getString("gradeName"));
			cell.setCellStyle(textStyle);
			
			cell = row.createCell(j++);
			cell.setCellValue(manfName);
			cell.setCellStyle(textStyle);
			
			cell = row.createCell(j++);
			cell.setCellValue(objName);
			cell.setCellStyle(textStyle);
			
			cell = row.createCell(j++);
			cell.setCellValue(totalDataObj.getString("pl"));
			cell.setCellStyle(textStyle);
			
			cell = row.createCell(j++);
			cell.setCellValue(pd);
			cell.setCellStyle(textStyle);
			
			cell = row.createCell(j++);
			cell.setCellValue(versionName);
			cell.setCellStyle(textStyle);
			
			cell = row.createCell(j++);
			cell.setCellValue(bodyType);
			cell.setCellStyle(textStyle);
			
			cell = row.createCell(j++);
			cell.setCellValue((totalDataObj.getString("launchDate")));
			cell.setCellStyle(textStyle);
			
			cell = row.createCell(j++);
			cell.setCellValue(Integer.parseInt(totalDataObj.getString("year")));
			cell.setCellStyle(textStyle);
			
			cell = row.createCell(j++);
			if(null != totalDataObj.getString("msrp") && !"".equals(totalDataObj.getString("msrp"))){
				cell.setCellValue(totalDataObj.getInteger("msrp"));
				cell.setCellStyle(thousandthStyle);
			} else{
				cell.setCellValue("-");
				cell.setCellStyle(textStyle);
			}
			
			
			monthDataObj=(JSONObject) monthData.get(0);
			 ym =  monthDataObj.getString("yearMonth");
			 for(int t = 0; t < monthData.size(); t++){
				 
				 if(totalDataObj.getString("vid").equals(((JSONObject) monthData.get(t)).getString("vid")) && ((JSONObject) monthData.get(t)).getString("yearMonth").equals(ym)){
					 if(salesType == 0){
						//车型销量
						 cell = row.createCell(j++);
						 if(null !=((JSONObject) monthData.get(t)).getString("sales") && !"".equals(((JSONObject) monthData.get(t)).getString("sales"))){
							//如果销售比例和型号销量为空，则车型销量为空
							 if((null==((JSONObject) monthData.get(t)).getString("percent") || "".equals(((JSONObject) monthData.get(t)).getString("percent"))) && (null==((JSONObject) monthData.get(t)).getString("versionSale") || "".equals(((JSONObject) monthData.get(t)).getString("versionSale")))){
								 cell.setCellValue("-");
								 cell.setCellStyle(textStyle);
							} else if(0 == ((JSONObject) monthData.get(t)).getInteger("sales")){
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
					 } 
					 
					 //销售比例
					 cell = row.createCell(j++);
					 if(null !=((JSONObject) monthData.get(t)).getString("percent") && !"".equals(((JSONObject) monthData.get(t)).getString("percent"))){
						 cell.setCellValue(Double.parseDouble(((JSONObject) monthData.get(t)).getString("percent")));
						 cell.setCellStyle(textPercentStyle);
					 } else{
						 cell.setCellValue("-");
						 cell.setCellStyle(textStyle);
					 }
					 
					 if(salesType == 0){
						 //型号销量
						 cell = row.createCell(j++);
						 if(null !=((JSONObject) monthData.get(t)).getString("versionSale") && !"".equals(((JSONObject) monthData.get(t)).getString("versionSale"))){
							if(0 == ((JSONObject) monthData.get(t)).getInteger("versionSale")){
								 cell.setCellValue(0);
								 cell.setCellStyle(textStyle);
							 } else{
								 cell.setCellValue(((JSONObject) monthData.get(t)).getInteger("versionSale"));
								 cell.setCellStyle(thousandthStyle);
							 }
						 } else{
							 cell.setCellValue("-");
							 cell.setCellStyle(textStyle);
						 }
					 }
			
				 }
			 }
			 
			 for(int t = 0; t < monthData.size(); t++){
				 if(totalDataObj.getString("vid").equals(((JSONObject) monthData.get(t)).getString("vid")) && !((JSONObject) monthData.get(t)).getString("yearMonth").equals(ym)){
					
					 if(salesType == 0){
						//车型销量
						 cell = row.createCell(j++);
						 if(null !=((JSONObject) monthData.get(t)).getString("sales") && !"".equals(((JSONObject) monthData.get(t)).getString("sales"))){
							//如果销售比例和型号销量为空，则车型销量为空
							 if((null==((JSONObject) monthData.get(t)).getString("percent") || "".equals(((JSONObject) monthData.get(t)).getString("percent"))) && (null==((JSONObject) monthData.get(t)).getString("versionSale") || "".equals(((JSONObject) monthData.get(t)).getString("versionSale")))){
								 cell.setCellValue("-");
								 cell.setCellStyle(textStyle);
							} else if(0 == ((JSONObject) monthData.get(t)).getInteger("sales")){
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
					 }
					 
					 //销售比例
					 cell = row.createCell(j++);
					 if(null !=((JSONObject) monthData.get(t)).getString("percent") && !"".equals(((JSONObject) monthData.get(t)).getString("percent"))){
						 cell.setCellValue(Double.parseDouble(((JSONObject) monthData.get(t)).getString("percent")));
						 cell.setCellStyle(textPercentStyle);
					 } else{
						 cell.setCellValue("-");
						 cell.setCellStyle(textStyle);
					 }
					 
					 if(salesType == 0){
						 //型号销量
						 cell = row.createCell(j++);
						 if(null !=((JSONObject) monthData.get(t)).getString("versionSale") && !"".equals(((JSONObject) monthData.get(t)).getString("versionSale"))){
							 if(0 == ((JSONObject) monthData.get(t)).getInteger("versionSale")){
								 cell.setCellValue(0);
								 cell.setCellStyle(textStyle);
							 } else{
								 cell.setCellValue(((JSONObject) monthData.get(t)).getInteger("versionSale"));
								 cell.setCellStyle(thousandthStyle);
							 }
						 } else{
							 cell.setCellValue("-");
							 cell.setCellStyle(textStyle);
						 }
					 }
				 }
			 }
			 
			 //总计部分
			 if(totalType == 0){
				 if(salesType == 0){
						//车型销量
						 cell = row.createCell(j++);
						 if(null !=totalDataObj.getString("totalSales") && !"".equals(totalDataObj.getString("totalSales"))){
							 if(0 == totalDataObj.getInteger("totalSales")){
								 cell.setCellValue(0);
								 cell.setCellStyle(textStyle);
							 } else{
								 cell.setCellValue(totalDataObj.getInteger("totalSales"));
								 cell.setCellStyle(thousandthStyle);
							 }
						 } else{
							 cell.setCellValue("-");
							 cell.setCellStyle(textStyle);
						 }
					 }
					 
					 
					 //销售比例
					 cell = row.createCell(j++);
					 if(null !=totalDataObj.getString("totalPercent") && !"".equals(totalDataObj.getString("totalPercent"))){
						 cell.setCellValue(Double.parseDouble(totalDataObj.getString("totalPercent")));
						 cell.setCellStyle(textPercentStyle);
					 } else{
						 cell.setCellValue("-");
						 cell.setCellStyle(textStyle);
					 }
					 
					 if(salesType == 0){
						 //型号销量
						 cell = row.createCell(j++);
						 if(null !=totalDataObj.getString("totalVersionSale") && !"".equals(totalDataObj.getString("totalVersionSale"))){
							 if(0 == totalDataObj.getInteger("totalVersionSale")){
								 cell.setCellValue(0);
								 cell.setCellStyle(textStyle);
							 } else{
								 cell.setCellValue(totalDataObj.getInteger("totalVersionSale"));
								 cell.setCellStyle(thousandthStyle);
							 }
						 } else{
							 cell.setCellValue("-");
							 cell.setCellStyle(textStyle);
						 } 
					 }
				
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
