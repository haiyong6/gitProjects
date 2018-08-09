package com.ways.app.pricesale.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.framework.utils.Constant;
import com.ways.app.framework.utils.EchartsUtil;
import com.ways.app.framework.utils.ExportExcelUtil;
import com.ways.app.price.model.EchartLineDataEntity;
import com.ways.app.pricesale.dao.IVolumeByPriceRangeDao;
import com.ways.app.pricesale.model.VolumeByPriceRangeEntity;
import com.ways.app.pricesale.service.IVolumeByPriceRangeManager;

/***********************************************************************************************
 * <br />价格段销量分析实现层
 * <br />Class name: VolumeByPriceRangeManagerImpl.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Apr 13, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
@Service("VolumeByPriceRangeManagerImpl")
public class VolumeByPriceRangeManagerImpl implements IVolumeByPriceRangeManager {
	
	@Autowired
	private IVolumeByPriceRangeDao volumeByPriceRangeDao;
	
	/**
	 * 实始化时间
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initDate(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		List<Map<String, String>> list = (List<Map<String, String>>) volumeByPriceRangeDao.initDate(paramsMap);
		if(null != list && list.size() > 0) {
			String beginDate = list.get(0).get("BEGINDATE");
			String endDate = list.get(0).get("ENDDATE");
			String defaultBeginDate = endDate.substring(0,4) + "-01";
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("defaultBeginDate", defaultBeginDate);
		}
	}
	
	/**
	 * 获取价格段销量分析数据
	 * 
	 * @param request
	 * @param paramsMap
	 * @return  
	 */
	@Override
	public String getVolumeByPriceRangeData(HttpServletRequest request, Map<String, Object> paramsMap)
	{
		String json = "";
		Map<String, Object> saveMap = new HashMap<String, Object>();
		saveMap.putAll(paramsMap);
		//保存查询条件
		request.getSession().setAttribute(Constant.getVolumeByPriceRangeExportMapKey, saveMap);
		//时间多选
		if("1".equals(paramsMap.get("multiType"))) {
			json = dateMultiSel(request, paramsMap);
		}
		//对象多选
		else {
			json = objectMultiSel(request, paramsMap);
		}
		//保存图形JSON数据
		request.getSession().setAttribute(Constant.getExportExcelDataKey, json);
		return json;
	}
	
	/**
	 * 时间多选查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String dateMultiSel(HttpServletRequest request, Map<String, Object> paramsMap)
	{
		String json = "";
		int sPrice = (Integer) paramsMap.get("sprice");
		int ePrice = (Integer) paramsMap.get("eprice");
		int priceScale = (Integer) paramsMap.get("priceScale");
		double temp = (double)(ePrice - sPrice)/priceScale;
		int length = (int) Math.ceil(temp);
		
		String objId = (String) paramsMap.get("objId");//选择框对象ID以|隔开
		objId = objId.substring(0, objId.length() - 1);
		
		String objName = (String) paramsMap.get("objName");//选择框对象名称以|隔开
		objName = objName.substring(0, objName.length() - 1);		
		
		String objectType = paramsMap.get("objectType").toString().split("\\|")[0];//选择框对象维度以|隔开
		paramsMap.put("objectType", objectType);
		
		String bodyTypeId = (String) paramsMap.get("bodyTypeId");//选择框车身形式ID以|隔开
		if(!"0".equals(bodyTypeId)) {
			bodyTypeId = bodyTypeId.substring(0, bodyTypeId.length() - 1);
		}
		
		String bodyTypeName = (String) paramsMap.get("bodyTypeName");//选择框车身形式名称以|隔开
		bodyTypeName = bodyTypeName.substring(0, bodyTypeName.length() - 1);		
		
		String dateGroup = (String) paramsMap.get("dateGroup");//时间组以|隔开
		dateGroup = dateGroup.substring(0, dateGroup.length() - 1);		
		String[] dates = dateGroup.split("\\|");
		//定义返回结果MAP
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//线图数据集合
		List<EchartLineDataEntity> lineData = new ArrayList<EchartLineDataEntity>();
		//定义线图X轴数组
		String[] xTitles = null;
		//定义图例数组
		String[] legends = new String[dates.length];
		
		String objIdKey = "";
		if("0".equals(objectType)) {
			objIdKey = "gradeId";
		} else if("1".equals(objectType)) {
			objIdKey = "origId";
		} else if("2".equals(objectType)) {
			objIdKey = "brandId";
		} else if("3".equals(objectType)) {
			objIdKey = "manfId";
			StringBuilder ids = new StringBuilder();
			for(int k = 0; k < objId.split(",").length; k++) {
				String id = objId.split(",")[k];
				ids.append("'").append(id).append("'").append(",");
			}
			objId = ids.substring(0, ids.length() - 1).toString();
		} else {
			objIdKey = "subModelId";
		}
		
		for(int i = 0; i < dates.length; i++) {
			String beginDate = dates[i].split(",")[0]; //开始时间
			String endDate = dates[i].split(",")[1];   //结束时间
			paramsMap.put("beginDate", beginDate.replace("-", ""));
			paramsMap.put("endDate", endDate.replace("-", ""));
			paramsMap.put(objIdKey, objId);
			paramsMap.put("bodyTypeId", bodyTypeId);
			
			List<VolumeByPriceRangeEntity> list = volumeByPriceRangeDao.getVolumeByPriceRangeData(paramsMap);
			if(null != list && list.size() > 0) {
				String legend = "";
				if("0".equals(objectType)) {
					Set<String> parentGrade = new HashSet<String>();
					Set<String> grade = new HashSet<String>();
					StringBuilder parentGrades = new StringBuilder(); 
					StringBuilder grades = new StringBuilder();
					for(int j = 0; j < list.size(); j++) {
						String[] gradeName = list.get(j).getObjectType().split(" ");
						parentGrade.add(gradeName[0]);
						grade.add(gradeName[1]);
					}
					for(String s : parentGrade) {
						parentGrades.append(s).append(",");
					}
					for(String s : grade) {
						grades.append(s).append(",");
					}
					legend = parentGrades.substring(0, parentGrades.length() - 1).toString() + " " + grades.substring(0, grades.length() - 1).toString();
				
				} else {
					legend = objName;
				}
				if(!"全部".equals(bodyTypeName)) {
					legend += "&" + bodyTypeName;
				}
				legend += "[" + beginDate + "~" + endDate + "]";
				legends[i] = legend;
				int count = 0;
				int price = 0;
				int tempPrice = sPrice;
				int sales;
				xTitles = new String[length];
				String[] data = new String[length];
				while (tempPrice < ePrice) {
					sales = 0;
					for (int j = 0; j < list.size(); j++) {
						price = list.get(j).getPrice();
						if(price >= tempPrice && price < (tempPrice + priceScale)) {
							sales = sales + list.get(j).getSales();
						}
					}
					xTitles[count] = tempPrice / 1000 + "K≤P<" + (tempPrice + priceScale) / 1000 + "K";
					data[count] = sales + "";
					tempPrice = tempPrice + priceScale;
					count++;
				}
				lineData.add(new EchartLineDataEntity("line", legend, "", data));
			}
		}
		
		resultMap.put("xTitles", xTitles);
		resultMap.put("series", lineData);
		resultMap.put("legends", legends);
		resultMap.put("boundarys", EchartsUtil.setLineScaleDivisionToVolumeByPriceRange(lineData));
		
		json = AppFrameworkUtil.structureConfigParamsGroupJSONData(resultMap);
		return json;
	}
	
	/**
	 * 对象多选查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String objectMultiSel(HttpServletRequest request, Map<String, Object> paramsMap)
	{
		String json = "";
		String [] boundarys = null;
		int sPrice = (Integer) paramsMap.get("sprice");
		int ePrice = (Integer) paramsMap.get("eprice");
		int priceScale = (Integer) paramsMap.get("priceScale");
		double temp = (double)(ePrice - sPrice) / priceScale;
		int length = (int) Math.ceil(temp);
		String dateGroup = (String) paramsMap.get("dateGroup");//时间组以|隔开
		dateGroup = dateGroup.substring(0, dateGroup.length() - 1);		
		String beginDate = dateGroup.split(",")[0]; //开始时间
		String endDate = dateGroup.split(",")[1];   //结束时间
		paramsMap.put("beginDate", beginDate.replace("-", ""));
		paramsMap.put("endDate", endDate.replace("-", ""));
		String objId = (String) paramsMap.get("objId");//选择框对象ID以|隔开
		objId = objId.substring(0, objId.length() - 1);
		String objName = (String) paramsMap.get("objName");//选择框对象名称以|隔开
		objName = objName.substring(0, objName.length() - 1);		
		
		String bodyTypeId = (String) paramsMap.get("bodyTypeId");//选择框车身形式ID以|隔开	
		if(!"0".equals(bodyTypeId)) {
			bodyTypeId = bodyTypeId.substring(0, bodyTypeId.length() - 1);
		}
		
		String bodyTypeName = (String) paramsMap.get("bodyTypeName");//选择框车身形式名称以|隔开
		bodyTypeName = bodyTypeName.substring(0, bodyTypeName.length() - 1);
		if(!AppFrameworkUtil.isEmpty(objId)) {
			String[] objIds = objId.split("\\|");
			String[] objNames = objName.split("\\|");
			String[] bodyTypeIds = bodyTypeId.split("\\|");
			String[] bodyTypeNames = bodyTypeName.split("\\|");			
			
			//定义返回结果MAP
			Map<String, Object> resultMap = new HashMap<String, Object>();
			//线图数据集合
			List<EchartLineDataEntity> lineData = new ArrayList<EchartLineDataEntity>();
			//定义线图X轴数组
			String[] xTitles = null;
			
			String objIdKey = "";
			String[] objTypes = paramsMap.get("objectType").toString().split("\\|");
			List<String> titles = new ArrayList<String>() ;
			for(int i = 0; i < objTypes.length; i++) {
				String objType = objTypes[i];
				paramsMap.put("objectType", objType);
				if("0".equals(objType)) {
					objIdKey = "gradeId";
				} else if("1".equals(objType)) {
					objIdKey = "origId";
				} else if("2".equals(objType)) {
					objIdKey = "brandId";
				} else if("3".equals(objType)) {
					objIdKey = "manfId";
					StringBuilder ids = new StringBuilder();
					for(int k = 0; k < objIds[i].split(",").length; k++) {
						String id = objIds[i].split(",")[k];
						ids.append("'").append(id).append("'").append(",");
					}
					objIds[i] = ids.substring(0, ids.length() - 1).toString();
				} else {
					objIdKey = "subModelId";
				}
				paramsMap.put(objIdKey, objIds[i]);
				paramsMap.put("bodyTypeId", bodyTypeIds[i]);
				List<VolumeByPriceRangeEntity> list = volumeByPriceRangeDao.getVolumeByPriceRangeData(paramsMap);
				String legend = "";
				if(null != list && list.size() > 0) {
					xTitles = new String[length];
					String[] data = new String[length];
					if("0".equals(objType)) {
						if(!"整体市场".equals(objName)) {
							Set<String> parentGrade = new HashSet<String>();
							Set<String> grade = new HashSet<String>();
							StringBuilder parentGrades = new StringBuilder(); 
							StringBuilder grades = new StringBuilder();
							for(int j = 0; j < list.size(); j++) {
								String[] gradeName = list.get(j).getObjectType().split(" ");
								parentGrade.add(gradeName[0]);
								grade.add(gradeName[1]);
							}
							for(String s : parentGrade) {
								parentGrades.append(s).append(",");
							}
							for(String s : grade) {
								grades.append(s).append(",");
							}
							legend = parentGrades.substring(0, parentGrades.length() - 1).toString() + " " + grades.substring(0, grades.length() - 1).toString();
						} else {
							legend = objNames[i];
						}
						if(!"全部".equals(bodyTypeNames[i])) {
							legend += "&" + bodyTypeNames[i];
						}
					} else {
						legend = objNames[i] + "&" + bodyTypeNames[i];
						if("全部".equals(bodyTypeNames[i])) {
							legend = objNames[i];
						}
					}
					titles.add(legend);
					int count = 0;
					int price = 0;
					int tempPrice = sPrice;
					int sales;
					while (tempPrice<ePrice) {
						sales = 0;
						for (int j = 0; j < list.size(); j++) {
							price = list.get(j).getPrice();
							if(price >= tempPrice && price < (tempPrice + priceScale)) {
								sales = sales + list.get(j).getSales();
							}
						}
						xTitles[count] = tempPrice / 1000 + "K≤P<" + (tempPrice + priceScale) / 1000 + "K";
						data[count] = sales + "";
						tempPrice = tempPrice + priceScale;
						count++;
					}
					lineData.add(new EchartLineDataEntity("line", legend, "", data));
				} else {
					if("0".equals(objType)) {
						legend = getGradeName(objIds[i]);
					} else {
						legend = objNames[i];
					}
					titles.add(legend);
				}
			}
			//定义图例数组
			String[] legends = new String[titles.size()];
			for(int i = 0; i < titles.size(); i++) {
				legends[i] = titles.get(i);
			}
			resultMap.put("xTitles", xTitles);
			resultMap.put("series", lineData);
			resultMap.put("legends", legends);
			boundarys = EchartsUtil.setLineScaleDivisionToVolumeByPriceRange(lineData);
			resultMap.put("boundarys", boundarys);
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(resultMap);
		}
		return json;
	}
	
	/**
	 * 查询级别对应名称
	 * 
	 * @param gradeId
	 * @return
	 */
	private String getGradeName(String gradeId) 
	{
	    return volumeByPriceRangeDao.getGradeName(gradeId);
	}
	
	/**
	 * 获取车型数据
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@Override
	public void getSubmodelModal(HttpServletRequest request, Map<String, Object> paramsMap) 
	{	
		Object subModelShowType = paramsMap.get("subModelShowType");
		try {
	    	if("2".equals(subModelShowType)) {
				//返回细分市场页
	    		request.setAttribute("segmentList", volumeByPriceRangeDao.getSubmodelBySegment(paramsMap));
	    	} else if("3".equals(subModelShowType)) {
	    		//返回品牌页
		    	request.setAttribute("brandLetterList", volumeByPriceRangeDao.getSubmodelByBrand(paramsMap));
	    	} else if("4".equals(subModelShowType)) {
	    		//返回厂商页
		    	request.setAttribute("manfLetterList", volumeByPriceRangeDao.getSubmodelByManf(paramsMap));
	    	} else {
	    		//本品页竟品页
		    	request.setAttribute("bpSubModelList", volumeByPriceRangeDao.getSubmodelByBp(paramsMap));
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Workbook exportExcel(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		//获取保存条件
		HashMap<String, Object> conditionMap = (HashMap<String, Object>) request.getSession().getAttribute("getVolumeByPriceRangeExportMapKey");
		conditionMap.putAll(paramsMap);
		int sPrice = Integer.parseInt(conditionMap.get("sprice").toString()) / 10000;
		int ePrice = Integer.parseInt(conditionMap.get("eprice").toString()) / 10000;
		String languageType = conditionMap.get("languageType").toString();
		//获取图形JSON
		String json = (String) request.getSession().getAttribute(Constant.getExportExcelDataKey);
		JSONObject obj = JSONObject.fromObject(json);
		JSONArray series = obj.getJSONArray("series");
		JSONArray legends = obj.getJSONArray("legends");
		JSONArray xTitles = obj.getJSONArray("xTitles");
		Map<String, Object> runMap = new HashMap<String, Object>();
		runMap.putAll(conditionMap);
		Workbook wb = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/"); 
			wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/SalesPrice.xls")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/**
		 * 设置数据显示Sheet页  --start
		 */
		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFSheet Datasheet = (HSSFSheet)wb.getSheet("数据显示");
		Datasheet.setColumnWidth(0, 6000);
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		
		/**
		 * 标题横坐标
		 */
		row = Datasheet.createRow(0);
		createStyleCell(row, titleStyle, "", HSSFCell.ENCODING_UTF_16, 0);
		for (int i = 0; i < xTitles.size(); i++) {
			Datasheet.setColumnWidth(i + 1, 3000);
			createStyleCell(row, titleStyle, xTitles.getString((i)), HSSFCell.ENCODING_UTF_16, i+1);
		}
		/**
		 * 各段数据值
		 */
		for (int i = 0; i < legends.size(); i++) {
			JSONArray data = null;
			row = Datasheet.createRow(i+1);
			for (int j = 0; j < xTitles.size(); j++) {
				if(j == 0) {
					createStyleCell(row, textStyle, legends.getString(i), HSSFCell.ENCODING_UTF_16, 0);
				}
				for (int k = 0; k < series.size(); k++) {
					if(legends.getString(i).equals(series.getJSONObject(k).getString("name"))) {
						data = series.getJSONObject(k).getJSONArray("data");
					}
				}
				cell = row.createCell(j+1);
				cell.setCellStyle(textStyle);
				if(data != null) {
					cell.setCellValue(data.getInt(j));
					if(data.getInt(j) != 0) {
						cell.setCellStyle(thousandthStyle);
					}
				} else {
					cell.setCellValue(0);
				}
			}
		}
		/**
		 * 设置数据显示Sheet页  --end
		 */
		
		/**
		 * 图表显示Sheet页  --start
		 */
		HSSFSheet chartSheet = (HSSFSheet)wb.getSheet("图表显示");
		CellStyle chartCellStyle = wb.createCellStyle();
		chartCellStyle.setLeftBorderColor(IndexedColors.WHITE.getIndex());//设置左边框颜色
		chartCellStyle.setBorderBottom(CellStyle.SOLID_FOREGROUND);   
		chartCellStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());//设置下边框颜色
		chartCellStyle.setBorderRight(CellStyle.SOLID_FOREGROUND);   
		chartCellStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());//设置右边框颜色
		HSSFFont font = (HSSFFont) wb.createFont();
		font.setColor(IndexedColors.WHITE.index);
		chartCellStyle.setFont(font);
		
		HSSFRow chartRow1 = chartSheet.createRow(0);
		HSSFRow chartRow2 = chartSheet.createRow(1);
		HSSFRow chartRow3 = chartSheet.createRow(2);
		
		HSSFCell chartCell1 = chartRow1.createCell(19);
		HSSFCell chartCell2 = chartRow2.createCell(19);
		HSSFCell chartCell3 = chartRow3.createCell(19);
		
		if("ZH".equals(languageType)) {
			chartCell1.setCellValue("[销量]");
			chartCell3.setCellValue("ChartTitle");
			if("0".equals(conditionMap.get("priceType"))) {
				chartCell2.setCellValue("[MSRP价格段:" + sPrice + "~" + ePrice+"万]");
			} else {
				chartCell2.setCellValue("[TP价格段:" + sPrice + "~" + ePrice + "万]");
			}
		} else {
			chartCell1.setCellValue("[Sales]");
			chartCell3.setCellValue("ChartTitle");
			if("0".equals(conditionMap.get("priceType"))) {
				chartCell2.setCellValue("[MSRP:" + sPrice + "~" + ePrice + "W]");
			} else {
				chartCell2.setCellValue("[TP:" + sPrice + "~" + ePrice + "W]");
			}
		}
		chartCell1.setCellStyle(chartCellStyle);
		chartCell2.setCellStyle(chartCellStyle);
		chartCell3.setCellStyle(chartCellStyle);
		/**
		 * 图表显示Sheet页  --end
		 */
		/**
		 * 数据显示Sheet页  --start
		 */
		if("1".equals(conditionMap.get("multiType"))) {	
			//时间多选
			exportMulitDate(wb, request, conditionMap, languageType);
		} else {
			//对象多选
			exportObjectMultiSel(wb, request, conditionMap, languageType);
		}
		/**
		 * 数据显示Sheet页  --end
		 */
		return wb;
	}
	
	/**
	 * 时间多选导出
	 * 
	 * @param wb
	 * @param request
	 * @param conditionMap
	 * @param languageType
	 * @return
	 */
	public void exportMulitDate(Workbook wb, HttpServletRequest request, Map<String, Object> conditionMap, String languageType)
	{
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.putAll(conditionMap);
		HSSFSheet sheet = null;
		HSSFRow row = null;
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		String dateGroup = (String) paramsMap.get("dateGroup");//时间组以|隔开
		dateGroup = dateGroup.substring(0, dateGroup.length() - 1);
		
		String objId = (String) paramsMap.get("objId");//选择框对象ID以|隔开
		objId = objId.substring(0, objId.length() - 1);
		
		String objectType = paramsMap.get("objectType").toString().split("\\|")[0];//选择框对象维度以|隔开
		paramsMap.put("objectType", objectType);
		
		String objName = (String) paramsMap.get("objName");//选择框对象名称以|隔开
		objName = objName.substring(0, objName.length() - 1);		
		
		String bodyTypeId = (String) paramsMap.get("bodyTypeId");//选择框车身形式ID以|隔开
		if(!"0".equals(bodyTypeId)) {
			bodyTypeId = bodyTypeId.substring(0, bodyTypeId.length() - 1);
		}
		
		String bodyTypeName = (String) paramsMap.get("bodyTypeName");//选择框车身形式名称以|隔开
		bodyTypeName = bodyTypeName.substring(0, bodyTypeName.length() - 1);		
		
		String objIdKey = "";
		String priceType = (String) paramsMap.get("priceType");//价格类型
		String[] dates = dateGroup.split("\\|");
		String allFlag = "";
		if(!AppFrameworkUtil.isEmpty(dateGroup)) {
			allFlag = "false";
			paramsMap.put("objectType", objectType);
			if("0".equals(objectType)) {
				objIdKey = "gradeId";
				if(objId.split(",").length > 1) {
					// 选择某些大类进行全选时
					if(objId.split(",")[0].length() == 1) {
						allFlag = "true";
					}
				} else {
					if(!"0".equals(objId) && objId.split(",")[0].length() == 1) {
						allFlag = "true";
					}
				}
				paramsMap.put("allFlag", allFlag);
			} else if("1".equals(objectType)) {
				objIdKey = "origId";
			} else if("2".equals(objectType)) {
				objIdKey = "brandId";
			} else if("3".equals(objectType)) {
				objIdKey = "manfId";
				StringBuilder ids = new StringBuilder();
				for(int k = 0; k < objId.split(",").length; k++) {
					String id = objId.split(",")[k];
					ids.append("'").append(id).append("'").append(",");
				}
				objId = ids.substring(0, ids.length() - 1).toString();
			} else {
				objIdKey = "subModelId";
			}
			for(int i = 0; i < dates.length; i++) {
				String beginDate = dates[i].split(",")[0]; //开始时间
				String endDate = dates[i].split(",")[1];   //结束时间
				paramsMap.put("beginDate", beginDate.replace("-", ""));
				paramsMap.put("endDate", endDate.replace("-", ""));
				
				paramsMap.put(objIdKey, objId);
				paramsMap.put("bodyTypeId", bodyTypeId);
				StringBuilder sb = new StringBuilder();
				sb.append(beginDate.replace("-", "年")).append("月~").append(endDate.replace("-", "年")).append("月原始数据(").append(i + 1).append(")");
				String legend = sb.toString();
				List<VolumeByPriceRangeEntity> dataList = volumeByPriceRangeDao.getVolumeByPriceRangeData(paramsMap);
				/**
				 * 创建相应Sheet页并设置行列属性
				 */
				sheet = (HSSFSheet) wb.createSheet(legend);
				sheet.setDisplayGridlines(false);
				sheet.setDefaultRowHeightInPoints(20);
				sheet.setDefaultColumnWidth(15);
				sheet.setColumnWidth(0, 6000); 
				sheet.setColumnWidth(2, 8000);
				
				/**
				 * 设置标题列
				 */
				row = sheet.createRow(0);
				if("ZH".equals(languageType)) {
					createStyleCell(row, titleStyle, "车型编码", HSSFCell.ENCODING_UTF_16, 0);
					if("0".equals(objectType)) {
						createStyleCell(row, titleStyle, "级别", HSSFCell.ENCODING_UTF_16, 1);
					} else if("1".equals(objectType)) {
						createStyleCell(row, titleStyle, "系别", HSSFCell.ENCODING_UTF_16, 1);
					} else if("2".equals(objectType)) {
						createStyleCell(row, titleStyle, "品牌", HSSFCell.ENCODING_UTF_16, 1);
					} else if("3".equals(objectType)) {
						createStyleCell(row, titleStyle, "厂商品牌", HSSFCell.ENCODING_UTF_16, 1);
					} else {
						createStyleCell(row, titleStyle, "车型", HSSFCell.ENCODING_UTF_16, 1);
					}
					
					createStyleCell(row, titleStyle, "型号名称", HSSFCell.ENCODING_UTF_16, 2);
					createStyleCell(row, titleStyle, "销量", HSSFCell.ENCODING_UTF_16, 3);
					
					if("0".equals(priceType)) {
						createStyleCell(row, titleStyle, "指导价", HSSFCell.ENCODING_UTF_16, 4);
						createStyleCell(row, titleStyle, "成交价", HSSFCell.ENCODING_UTF_16, 5);
					} else {
						createStyleCell(row, titleStyle, "成交价", HSSFCell.ENCODING_UTF_16, 4);
						createStyleCell(row, titleStyle, "指导价", HSSFCell.ENCODING_UTF_16, 5);
					}
					createStyleCell(row, titleStyle, "时间", HSSFCell.ENCODING_UTF_16, 6);
				} else {
					createStyleCell(row, titleStyle, "VersionCode", HSSFCell.ENCODING_UTF_16, 0);
					if("0".equals(objectType)) {
						createStyleCell(row, titleStyle, "Segment", HSSFCell.ENCODING_UTF_16, 1);
					} else if("1".equals(objectType)){
						createStyleCell(row, titleStyle, "Orig", HSSFCell.ENCODING_UTF_16, 1);
					} else if("2".equals(objectType)){
						createStyleCell(row, titleStyle, "Brand", HSSFCell.ENCODING_UTF_16, 1);
					} else if("3".equals(objectType)){
						createStyleCell(row, titleStyle, "Manf", HSSFCell.ENCODING_UTF_16, 1);
					} else {
						createStyleCell(row, titleStyle, "Model", HSSFCell.ENCODING_UTF_16, 1);
					}
					createStyleCell(row, titleStyle, "Version", HSSFCell.ENCODING_UTF_16, 2);
					createStyleCell(row, titleStyle, "Sales", HSSFCell.ENCODING_UTF_16, 3);
					if("0".equals(priceType)) {
						createStyleCell(row, titleStyle, "MSRP", HSSFCell.ENCODING_UTF_16, 4);
						createStyleCell(row, titleStyle, "TP", HSSFCell.ENCODING_UTF_16, 5);
					} else {
						createStyleCell(row, titleStyle, "TP", HSSFCell.ENCODING_UTF_16, 4);
						createStyleCell(row, titleStyle, "MSRP", HSSFCell.ENCODING_UTF_16, 5);
					}
					createStyleCell(row, titleStyle, "Time", HSSFCell.ENCODING_UTF_16, 6);
				}
				
				for (int k = 0; k < dataList.size(); k++) {
					row = sheet.createRow(k + 1);
					createStyleCell(row, textStyle, dataList.get(k).getVersionCode(), HSSFCell.ENCODING_UTF_16, 0);
					createStyleCell(row, textStyle, dataList.get(k).getObjectType(), HSSFCell.ENCODING_UTF_16, 1);
					if("ZH".equals(languageType)) {
						createStyleCell(row, textStyle, dataList.get(k).getVersionName(), HSSFCell.ENCODING_UTF_16, 2);
					} else {
						createStyleCell(row, textStyle, dataList.get(k).getVersionEName(), HSSFCell.ENCODING_UTF_16, 2);
					}
					createStyleCell(row, thousandthStyle, dataList.get(k).getSales(), HSSFCell.ENCODING_UTF_16, 3);
					createStyleCell(row, thousandthStyle, dataList.get(k).getPrice(), HSSFCell.ENCODING_UTF_16, 4);
					createStyleCell(row, thousandthStyle, dataList.get(k).getOtherprice(), HSSFCell.ENCODING_UTF_16, 5);
					createStyleCell(row, textStyle, dataList.get(k).getTime(), HSSFCell.ENCODING_UTF_16, 6);
				}
			}
		}
	}
	
	/**
	* 对象多选导出
	* 
	* @param wb
	* @param request
	* @param conditionMap    
	* @param languageType
	*/
	public void exportObjectMultiSel(Workbook wb, HttpServletRequest request, Map<String, Object> conditionMap, String languageType)
	{
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.putAll(conditionMap);
		HSSFSheet sheet = null;
		HSSFRow row = null;
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		
		String dateGroup = (String) paramsMap.get("dateGroup");//时间组以|隔开
		dateGroup = dateGroup.substring(0, dateGroup.length() - 1);		
		String beginDate = dateGroup.split(",")[0]; //开始时间
		String endDate = dateGroup.split(",")[1];   //结束时间
		paramsMap.put("beginDate", beginDate.replace("-", ""));
		paramsMap.put("endDate", endDate.replace("-", ""));
		String objId = (String) paramsMap.get("objId");//选择框对象ID以|隔开
		objId = objId.substring(0, objId.length() - 1);
		
		String objName = (String) paramsMap.get("objName");//选择框对象名称以|隔开
		objName = objName.substring(0, objName.length() - 1);		
		
		String bodyTypeId = (String) paramsMap.get("bodyTypeId");//选择框车身形式ID以|隔开
		if(bodyTypeId.endsWith("|")) {
			bodyTypeId = bodyTypeId.substring(0, bodyTypeId.length() - 1);
		}
		
		String bodyTypeName = (String) paramsMap.get("bodyTypeName");//选择框车身形式名称以|隔开
		bodyTypeName = bodyTypeName.substring(0, bodyTypeName.length() - 1);
		
		String priceType = (String) paramsMap.get("priceType");//价格类型
		if(!AppFrameworkUtil.isEmpty(objId)) {
			String[] objIds = objId.split("\\|");
			String[] bodyTypeIds = bodyTypeId.split("\\|");
			
			String objIdKey = "";
			String[] objTypes = paramsMap.get("objectType").toString().split("\\|");
			String allFlag;
			for(int i = 0; i < objTypes.length; i++) {
				String objType = objTypes[i];
				paramsMap.put("objectType", objType);
				allFlag = "false";
				if("0".equals(objType)) {
					objIdKey = "gradeId";
					if(objIds[i].split(",").length > 1) {
						// 选择某些大类进行全选时
						if(objIds[i].split(",")[0].length() == 1) {
							allFlag = "true";
						}
					} else {
						if(!"0".equals(objIds[i]) && objIds[i].split(",")[0].length() == 1) {
							allFlag = "true";
						}
					}
					paramsMap.put("allFlag", allFlag);
				} else if("1".equals(objType)) {
					objIdKey = "origId";
				} else if("2".equals(objType)) {
					objIdKey = "brandId";
				} else if("3".equals(objType)) {
					objIdKey = "manfId";
					StringBuilder ids = new StringBuilder();
					for(int k = 0; k < objIds[i].split(",").length; k++) {
						String id = objIds[i].split(",")[k];
						ids.append("'").append(id).append("'").append(",");
					}
					objIds[i] = ids.substring(0, ids.length() - 1).toString();
				} else {
					objIdKey = "subModelId";
				}
				paramsMap.put(objIdKey, objIds[i]);
				paramsMap.put("bodyTypeId", bodyTypeIds[i]);
				StringBuilder sb = new StringBuilder();
				sb.append(beginDate.replace("-", "年")).append("月~").append(endDate.replace("-", "年")).append("月原始数据(").append(i + 1).append(")");
				String legend = sb.toString();
				List<VolumeByPriceRangeEntity> dataList = volumeByPriceRangeDao.getVolumeByPriceRangeData(paramsMap);
				/**
				 * 创建相应Sheet页并设置行列属性
				 */
				sheet = (HSSFSheet) wb.createSheet(legend);
				sheet.setDisplayGridlines(false);
				sheet.setDefaultRowHeightInPoints(20);
				sheet.setDefaultColumnWidth(15);
				sheet.setColumnWidth(0, 6000); 
				sheet.setColumnWidth(2, 8000);
				
				/**
				 * 设置标题列
				 */
				row = sheet.createRow(0);
				if("ZH".equals(languageType)) {
					createStyleCell(row, titleStyle, "车型编码", HSSFCell.ENCODING_UTF_16, 0);
					if("0".equals(objType)) {
						createStyleCell(row, titleStyle, "级别", HSSFCell.ENCODING_UTF_16, 1);
					} else if("1".equals(objType)) {
						createStyleCell(row, titleStyle, "系别", HSSFCell.ENCODING_UTF_16, 1);
					} else if("2".equals(objType)) {
						createStyleCell(row, titleStyle, "品牌", HSSFCell.ENCODING_UTF_16, 1);
					} else if("3".equals(objType)) {
						createStyleCell(row, titleStyle, "厂商品牌", HSSFCell.ENCODING_UTF_16, 1);
					} else {
						createStyleCell(row, titleStyle, "车型", HSSFCell.ENCODING_UTF_16, 1);
					}
					
					createStyleCell(row, titleStyle, "型号名称", HSSFCell.ENCODING_UTF_16, 2);
					createStyleCell(row, titleStyle, "销量", HSSFCell.ENCODING_UTF_16, 3);
					
					if("0".equals(priceType)) {
						createStyleCell(row, titleStyle, "指导价", HSSFCell.ENCODING_UTF_16, 4);
						createStyleCell(row, titleStyle, "成交价", HSSFCell.ENCODING_UTF_16, 5);
					} else {
						createStyleCell(row, titleStyle, "成交价", HSSFCell.ENCODING_UTF_16, 4);
						createStyleCell(row, titleStyle, "指导价", HSSFCell.ENCODING_UTF_16, 5);
					}
					createStyleCell(row, titleStyle, "时间", HSSFCell.ENCODING_UTF_16, 6);
				} else {
					createStyleCell(row, titleStyle, "VersionCode", HSSFCell.ENCODING_UTF_16, 0);
					if("0".equals(objType)) {
						createStyleCell(row, titleStyle, "Segment", HSSFCell.ENCODING_UTF_16, 1);
					} else if("1".equals(objType)) {
						createStyleCell(row, titleStyle, "Orig", HSSFCell.ENCODING_UTF_16, 1);
					} else if("2".equals(objType)) {
						createStyleCell(row, titleStyle, "Brand", HSSFCell.ENCODING_UTF_16, 1);
					} else if("3".equals(objType)) {
						createStyleCell(row, titleStyle, "Manf", HSSFCell.ENCODING_UTF_16, 1);
					} else {
						createStyleCell(row, titleStyle, "Model", HSSFCell.ENCODING_UTF_16, 1);
					}
					createStyleCell(row, titleStyle, "Version", HSSFCell.ENCODING_UTF_16, 2);
					createStyleCell(row, titleStyle, "Sales", HSSFCell.ENCODING_UTF_16, 3);
					if("0".equals(priceType)) {
						createStyleCell(row, titleStyle, "MSRP", HSSFCell.ENCODING_UTF_16, 4);
						createStyleCell(row, titleStyle, "TP", HSSFCell.ENCODING_UTF_16, 5);
					} else {
						createStyleCell(row, titleStyle, "TP", HSSFCell.ENCODING_UTF_16, 4);
						createStyleCell(row, titleStyle, "MSRP", HSSFCell.ENCODING_UTF_16, 5);
					}
					createStyleCell(row, titleStyle, "Time", HSSFCell.ENCODING_UTF_16, 6);
				}
				
				for (int k = 0; k < dataList.size(); k++) {
					row = sheet.createRow(k + 1);
					createStyleCell(row, textStyle, dataList.get(k).getVersionCode(), HSSFCell.ENCODING_UTF_16, 0);
					createStyleCell(row, textStyle, dataList.get(k).getObjectType(), HSSFCell.ENCODING_UTF_16, 1);
					if("ZH".equals(languageType)) {
						createStyleCell(row, textStyle, dataList.get(k).getVersionName(), HSSFCell.ENCODING_UTF_16, 2);
					} else {
						createStyleCell(row, textStyle, dataList.get(k).getVersionEName(), HSSFCell.ENCODING_UTF_16, 2);
					}
					createStyleCell(row, thousandthStyle, dataList.get(k).getSales(), HSSFCell.ENCODING_UTF_16, 3);
					createStyleCell(row, thousandthStyle, dataList.get(k).getPrice(), HSSFCell.ENCODING_UTF_16, 4);
					createStyleCell(row, thousandthStyle, dataList.get(k).getOtherprice(), HSSFCell.ENCODING_UTF_16, 5);
					createStyleCell(row, textStyle, dataList.get(k).getTime(), HSSFCell.ENCODING_UTF_16, 6);
				}
			}
		}
	}
	
	/**
	 * 创建单元格格式并赋值(字符串)
	 * 
	 * @param row
	 * @param style
	 * @param value
	 * @param encoding
	 * @param column
	 */
	private void createStyleCell(HSSFRow row, CellStyle style, String value, short encoding, int column) 
	{
		HSSFCell cell = row.createCell(column);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	} 
	
	/**
	 * 创建单元格格式并赋值(数字)
	 * 
	 * @param row
	 * @param style
	 * @param value
	 * @param encoding
	 * @param column
	 */
	private void createStyleCell(HSSFRow row, CellStyle style, int value, short encoding, int column) 
	{
		HSSFCell cell = row.createCell(column);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	} 
}
