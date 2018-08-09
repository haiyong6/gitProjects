package com.ways.app.policy.service.impl;

import java.io.File;
import java.io.FileInputStream;
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
import com.ways.app.policy.dao.SaleIncentiveQueryDao;
import com.ways.app.policy.model.SalesIncentiveEntity;
import com.ways.app.policy.service.SaleIncentiveQueryManager;
import com.ways.app.price.model.Segment;

@Service("saleIncentiveQueryManager")
public class SaleIncentiveQueryManagerImpl implements SaleIncentiveQueryManager {

	@Autowired
	private SaleIncentiveQueryDao saleIncentiveQueryDao;
	
	/**
	 * 实始化时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initDate(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		List<Map<String, String>> list = (List<Map<String, String>>) saleIncentiveQueryDao.initDate(paramsMap);
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
	 * 获取销售激励查询数据
	 */
	@Override
	public String getSaleIncentiveQueryData(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		List<SalesIncentiveEntity> list = null;
		try{
			 list = saleIncentiveQueryDao.getSaleIncentiveQueryData(paramsMap);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(null != list && 0 != list.size())
		{
		  try{
			   json = AppFrameworkUtil.structureConfigParamsGroupJSONData(list);
		     } catch (Exception e) {
				e.printStackTrace();
			 }
			request.getSession().setAttribute(Constant.getSalesExportExcelDataKey, json);
		}
		return json;
	}
	
	@Override
	public void getSubmodelModal(HttpServletRequest request, Map<String, Object> paramsMap) 
	{	
		Object subModelShowType = paramsMap.get("subModelShowType");
		try {
	    	if("2".equals(subModelShowType)){
	    		List<Segment> list=saleIncentiveQueryDao.getSubmodelBySegment(paramsMap);
	    		/*List<Segment> listNew=new ArrayList<Segment>();
	    	String s="";
	    	String r="";
	    	int l=0;
	    	int g=0;
	    		for(int i=0;i<list.size();i++){
	    			Segment segment=new Segment();
	    			//如果级别重复
	    			if(s.contains(list.get(i).getSegmentName())){
	    				
	    				for(int k=0;k<list.get(i).getSegmentList().size();k++){
	    					Segment segment2=new Segment();
	    					if(r.contains(list.get(i).getSegmentList().get(k).getSegmentName())){
	    						segment2.setSegmentId(list.get(g).getSegmentId());
				    			segment2.setSegmentName(list.get(g).getSegmentName());
			    				segment2.setSubModelList(list.get(g).getSubModelList());
			    				
			    				
	    					}else{
	    						r +=","+list.get(i).getSegmentList().get(k).getSegmentName();
	    						segment2.setSegmentId(list.get(k).getSegmentId());
				    			segment2.setSegmentName(list.get(k).getSegmentName());
			    				segment2.setSubModelList(list.get(k).getSubModelList());
			    				g=k;
	    					}
	    					
	    					
		    				//把重复的子级别加到上个级别底下
		    				list.get(l).getSegmentList().add(segment2);
	    				}
	    				
	    				segment.setSegmentId(list.get(l).getSegmentId());
	    				segment.setSegmentName(list.get(l).getSegmentName());
	    				segment.setSegmentList(list.get(l).getSegmentList());
	    			}else{
	    				
	    				s +=","+list.get(i).getSegmentName();
	    				segment.setSegmentId(list.get(i).getSegmentId());
	    				segment.setSegmentName(list.get(i).getSegmentName());
	    				segment.setSegmentList(list.get(i).getSegmentList());
	    				if(i!=0){
	    					listNew.add(segment);
	    				}
	    				l=i;
	    				
	    				
	    			}
	    			listNew.add(segment);
	    			//如果级别重复
	    			if(i!=0&&(list.get(i).getSegmentName().equals(list.get(i-1).getSegmentName()))){
	    				for(int k=0;k<list.get(i).getSegmentList().size();k++){
	    					Segment segment2=new Segment();
		    				segment2.setSegmentId(list.get(k).getSegmentId());
			    			segment2.setSegmentName(list.get(k).getSegmentName());
		    				segment2.setSubModelList(list.get(k).getSubModelList());
		    				//把重复的子级别加到上个级别底下
		    				list.get(i-1).getSegmentList().add(segment2);
	    				}
	    				
	    				segment.setSegmentId(list.get(i-1).getSegmentId());
	    				segment.setSegmentName(list.get(i-1).getSegmentName());
	    				segment.setSegmentList(list.get(i-1).getSegmentList());
	    				
	    			}else{
	    				
	    				segment.setSegmentId(list.get(i).getSegmentId());
	    				segment.setSegmentName(list.get(i).getSegmentName());
	    				segment.setSegmentList(list.get(i).getSegmentList());
	    				
	    				
		    			
	    			}
	    			
	    			
	    		}*/
	    		
				//返回细分市场页
	    		request.setAttribute("segmentList", list);
	    	}else if("3".equals(subModelShowType)){
	    		//返回品牌页
		    	request.setAttribute("brandLetterList", saleIncentiveQueryDao.getSubmodelByBrand(paramsMap));
	    	}else if("4".equals(subModelShowType)){
	    		//返回厂商页
		    	request.setAttribute("manfLetterList", saleIncentiveQueryDao.getSubmodelByManf(paramsMap));
	    	}else{
	    		//本品页竟品页
		    	request.setAttribute("bpSubModelList", saleIncentiveQueryDao.getSubmodelByBp(paramsMap));
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getBodyType(HttpServletRequest request, Map<String, Object> paramsMap) {
		try {
			request.setAttribute("bodyTypeList", saleIncentiveQueryDao.getBodyType(paramsMap));
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
			request.setAttribute("autoCustomGroupList", saleIncentiveQueryDao.getAutoCustomGroup(paramsMap));
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
		String json = (String) request.getSession().getAttribute(Constant.getSalesExportExcelDataKey);
		paramsMap.put("languageType", request.getParameter("languageType"));
		String path = request.getSession().getServletContext().getRealPath("/"); 
		if(!AppFrameworkUtil.isEmpty(json))
		{
			JSONArray obj = (JSONArray) JSONObject.parse(json);
			try {
				if(!"EN".equals(paramsMap.get("languageType"))){
					wb = new HSSFWorkbook(new FileInputStream(new File(path+"excelExample/saleIncentiveQuery.xls")));
					exportOriginalData(wb,wb.getSheet("数据显示"),obj,paramsMap);
				}else{
					wb = new HSSFWorkbook(new FileInputStream(new File(path+"excelExample/saleIncentiveQueryEn.xls")));
					exportOriginalData(wb,wb.getSheet("data"),obj,paramsMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(null == wb){
			try{
				if(!"EN".equals(paramsMap.get("languageType"))){
					wb = new HSSFWorkbook(new FileInputStream(new File(path+"excelExample/saleIncentiveQuery.xls")));
				}else{
					wb = new HSSFWorkbook(new FileInputStream(new File(path+"excelExample/saleIncentiveQueryEn.xls")));
				}
			}catch(Exception e){
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
	public void exportOriginalData(Workbook wb,Sheet s,JSONArray gridObj,Map<String, Object> paramsMap)
	{
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
//		String[] titles = null;//标题数组

		int rowIndex = 0;//行号索引
		
		Row row = null;
		Cell cell = null;
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
//		CellStyle textRedStyle= ExportExcelUtil.getExcelFillTextStyle(wb,"RED");//内容文本样式
		CellStyle textPercentStyle = ExportExcelUtil.getExcelFillPercentageStyle(wb);//
		CellStyle textPercentRedStyle = ExportExcelUtil.getExcelFillPercentageRedStyle(wb);//红色
		
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		CellStyle thousandthREDStyle =ExportExcelUtil.getExcelFormatThousandthREDStyle(wb);
		
		//写表格数据内容
		for(int j = 0; j < gridObj.size(); j++)
		{
			JSONObject obj = gridObj.getJSONObject(j);
			rowIndex++;
			row = ExportExcelUtil.createRow(s, rowIndex, 400);
			int i=0;
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("ym")+" ", textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"manfname"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell,obj.getString("segment"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell,obj.getString("subsegment"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("bodytypeEn"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell,getValueByLanguageType(obj,languageType,"brand"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell,getValueByLanguageType(obj,languageType,"model"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"versionname"), textStyle);
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell,obj.getString("code")+"~", textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell,obj.getString("launchdate"), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell,changeShow(obj.getString("MSRP")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell, changeShow(obj.getString("margin")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell, changeShow(obj.getString("bonus")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell, changeShow(obj.getString("totaltactical")), thousandthStyle,thousandthREDStyle);
			
			String grossSupports=null;
			if(obj.getString("STD").equals("")&&obj.getString("AAK").equals("")){
				grossSupports="-";
			}else{
				grossSupports=obj.getString("grosssupports");
			}
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell,changeShow(obj.getString("fullyPaid")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell,grossSupports, thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell,  changeShow(obj.getString("STD")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell, changeShow(obj.getString("AAK")), thousandthStyle,thousandthREDStyle);
			
			String customerIncentive=null;
			if(obj.getString("presents").equals("")&&obj.getString("insurance").equals("")&&obj.getString("maintenance").equals("")&& obj.getString("staffreward").equals("")&&obj.getString("financialloan").equals("")&&obj.getString("tradeinsupport").equals("")){
				customerIncentive="-";
			}else{
				customerIncentive=obj.getString("customerincentive");
			}
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell, customerIncentive, thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell, changeShow(obj.getString("presents")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell, changeShow(obj.getString("insurance")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell, changeShow(obj.getString("maintenance")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell, changeShow(obj.getString("staffreward")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell, changeShow(obj.getString("financialloan")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell,changeShow(obj.getString("tradeinsupport")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell, changeShow(obj.getString("invoiceprice")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell,  changeShow(obj.getString("grossinvoiceprice")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell,changeShow(obj.getString("TP")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell,changeShow(obj.getString("profit")), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell,changeShow(obj.getString("profitrate")), textPercentStyle,textPercentRedStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyleEx(cell,obj.getString("versionlastmonthsales"), thousandthStyle,thousandthREDStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyleEx(cell,obj.getString("versionmonthsales"), thousandthStyle,thousandthREDStyle);
		}
	}
	
	private String changeShow(String string) {
		if(null==string||"".equals(string)){
			return "-";
		}else{
			return string;
		}
	}

//	private void changeNumberStyle(Cell cell,String rowValue,CellStyle defaultStyle){
//	
//		if(null ==rowValue || "".equals(rowValue)){
//			cell.setCellValue("-");
//		}else{
//			cell.setCellValue(rowValue);
//			try{
//				cell.setCellStyle(Double.parseDouble(rowValue)<0?changeStyle:defaultStyle);
//			}catch(Exception e){
//				cell.setCellStyle(defaultStyle);
//			}
//		}
//		if(null != defaultStyle) cell.setCellStyle(defaultStyle);
//		try{
//			cell.setCellStyle(Double.parseDouble(rowValue)<0?changeStyle:defaultStyle);
//		}catch(Exception e){
//			cell.setCellStyle(defaultStyle);
//		}
	//	return cell;
//	}
	
	
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
