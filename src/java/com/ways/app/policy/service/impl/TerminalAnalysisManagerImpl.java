package com.ways.app.policy.service.impl;

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
import org.apache.poi.ss.util.CellRangeAddress;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.framework.utils.Constant;
import com.ways.app.framework.utils.ExportExcelUtil;
import com.ways.app.policy.dao.ITerminalAnalysisDao;
import com.ways.app.policy.model.PromotionInfo;
import com.ways.app.policy.model.TerminalObjectEntity;
import com.ways.app.policy.model.TerminalOriginalEntity;
import com.ways.app.policy.service.ITerminalAnalysisManager;
import com.ways.app.price.model.EchartLineDataEntity;

@Service("TerminalAnalysisManager")
public class TerminalAnalysisManagerImpl implements ITerminalAnalysisManager {

	@Autowired
	private ITerminalAnalysisDao terminalAnalysisDao;

	private static String CXZE = "促销总额";
	private static String TCZC = "提车支持(STD支持)";
	private static String LSZC = "零售支持(AaK支持)";
	private static String RYJL = "人员奖励";
	private static String JRDK = "金融贷款";
	private static String ZHZC = "置换支持";
	private static String ZSBX = "赠送保险";
	private static String ZSLP = "赠送礼品(油卡、保养)";
//	/private static String XZCX = "选择促销";
	
	private static String[] STARRY = new String[]{TCZC, LSZC, RYJL, JRDK, ZHZC, ZSBX, ZSLP};
	 
	/**
	 * 初始时间
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public String initDate(HttpServletRequest request, Map<String, Object> paramsMap)
	{
		String json = "";// 返回默认开始时间和结束时间
		List<Map<String, String>> list = terminalAnalysisDao.initDate(paramsMap);
		if (null != list && list.size() > 0) {
			Map<String, String> resultMap = list.get(0);
			String endDate = resultMap.get("ENDDATE");
			String beginDate = resultMap.get("BEGINDATE");
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			// 默认开始时间，时间规则是相差12个点
			String defaultBeginDate = (Integer.parseInt(endDate.replace("-", "")) - 99) + "";
			// 如果结束时间减一年小于开始时间，则默认为开始时间
			if (Integer.parseInt(defaultBeginDate) < Integer.parseInt(beginDate.replace("-", ""))) {
				request.setAttribute("defaultBeginDate", beginDate);
				json = resultMap.get("BEGINDATE");
			} else {
				if (Integer.parseInt(defaultBeginDate.substring(4)) > 12) {
					defaultBeginDate = (Integer.parseInt(defaultBeginDate.substring(0, 4)) + 1) + "-01";
				} else {
					defaultBeginDate = defaultBeginDate.substring(0, 4) + "-" + defaultBeginDate.substring(4);
				}
				request.setAttribute("defaultBeginDate", defaultBeginDate);
				json = defaultBeginDate;
			}
			json += "," + resultMap.get("BEGINDATE") + "," + endDate;
		}
		return json;
	}

	/**
	 * 加载终端支持图形和表格
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String loadChartAndTable(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		String objectType = paramsMap.get("objectType").toString();
		String submitType = paramsMap.get("submitType").toString();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List list = null;
		//型号查询需要查询上下代
		if("0".equals(objectType)) {
			list = terminalAnalysisDao.loadTerminalChartAndTableByVersion(paramsMap);
		} else {
			list = terminalAnalysisDao.loadTerminalChartAndTable(paramsMap);
		}
		if(null != list && list.size() > 0) {
			//促销细分
			if("changPromotion".equals(submitType)) {
				dataMap.put("chart", getChartDataByPromotion(list));
			} else {
				dataMap.put("chart", getChartData(list, paramsMap));
			}
			dataMap.put("grid", list);
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			request.getSession().setAttribute(Constant.getExportExcelTerminalDataKey, json);
		}
		return json;
	}

	/**
	 * 函数功能说明 获取对象终端支持分析图形数据 
	 * 
	 * @param tList
	 * @param paramsMap
	 * @return 
	 */
	public Map<String, Object> getChartData(List<TerminalObjectEntity> tList, Map<String, Object> paramsMap) 
	{
		String oStr = "";
		String dStr = "";
		String objectName = "";
		String dateTime = "";
		boolean isNewDate = false;
		boolean isAddDate = false;
		
		boolean isNewObj = false;
		boolean isAddObj = false;
		String lineDateTimeData = "";
		String lineObjectData = "";
		List<String> oList = new ArrayList<String>();// 对象
		List<String> dList = new ArrayList<String>();// 时间
		List<TerminalObjectEntity> list = new ArrayList<TerminalObjectEntity>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		for(int i = 0; i < tList.size(); i++) {
			if(CXZE.equals(tList.get(i).getSubsidyType())) {
				list.add(tList.get(i));
			}
		}
		if (null != list && list.size() > 0) {
			// 线图数据
			List<EchartLineDataEntity> lineDateTimeList = new ArrayList<EchartLineDataEntity>();
			List<EchartLineDataEntity> lineObjectList = new ArrayList<EchartLineDataEntity>();
			// 数据
			for(int i = 0; i < list.size(); i++) {
				String dtime = list.get(i).getDateTime();
				String oName = list.get(i).getObjectNameEn();
				String sValue = list.get(i).getSubsidy();
				if (!dStr.contains(dtime)) {
					dStr += dtime + ",";
					dList.add(dtime);
					isNewDate = true;
					if(i != 0) {
						isAddDate = true;
					}
				}
				if(isAddDate) {
					lineDateTimeList.add(new EchartLineDataEntity("bar", dateTime, "emptyCircle", lineDateTimeData.split(",")));
					isAddDate = false;
				}
				if(isNewDate) {
					lineDateTimeData = sValue;
					isNewDate = false;
				} else {
					lineDateTimeData += "," + sValue;
				}
				
				if (!oStr.contains(oName)) {
					oStr += oName + ",";
					oList.add(oName);
					isNewObj = true;
					if(i != 0) {
						isAddObj = true;
					}
				}
				
				if(isAddObj) {
					lineObjectList.add(new EchartLineDataEntity("bar", objectName, "emptyCircle", lineObjectData.split(",")));
					isAddObj = false;
				}
				
				if(isNewObj) {
					lineObjectData = sValue;
					isNewObj = false;
				} else {
					lineObjectData += "," + sValue;
				}
				
				dateTime = dtime;
				objectName = oName;
				
				//添加最后一个对象
				if(i == list.size() - 1) {
					lineDateTimeList.add(new EchartLineDataEntity("bar", dateTime, "emptyCircle", lineDateTimeData.split(",")));
					lineObjectList.add(new EchartLineDataEntity("bar", objectName, "emptyCircle", lineObjectData.split(",")));
					isAddDate = false;
					isAddObj = false;
				}
			}
			if (dList.size() <= 4) {
				dataMap.put("xTitle", oStr.substring(0,oStr.length() - 1).split(","));
				dataMap.put("series", lineDateTimeList);
				dataMap.put("titles", dStr.substring(0,dStr.length() - 1).split(","));
				dataMap.put("showType", "0");
			}

			if (oList.size() <= 4 && dList.size() > 4) {
				dataMap.put("xTitle", dStr.substring(0, dStr.length() - 1).split(","));
				dataMap.put("series", lineObjectList);
				dataMap.put("titles", oStr.substring(0, oStr.length() - 1).split(","));
				dataMap.put("showType", "1");
			}
		}
		return dataMap;
	}
	
	
	/**
	 * 函数功能说明 获取对象终端支持分析图形数据 
	 * 
	 * @param tList
	 * @return 
	 */
	public Map<String, Object> getChartDataByPromotion(List<TerminalObjectEntity> tList) 
	{
		String oStr = "";
		String dStr = "";
		String showType = "";
		List<String> oList = new ArrayList<String>();// 对象
		List<String> dList = new ArrayList<String>();// 时间
		Map<String, Object> dataMap = null;
		for(int i = 0; i < tList.size(); i++) {
			String dtime = tList.get(i).getDateTime();
			String oName = tList.get(i).getObjectNameEn();
			if(!dStr.contains(dtime)) {
				dStr += dtime + ",";
				dList.add(dtime);
			}
			
			if(!oStr.contains(oName)) {
				oStr += oName + ",";
				oList.add(oName);
			}
		}
		
		if (dList.size() <= 4) {
			showType = "0";
			dataMap = getChartDataByPromotion(tList, showType, oList.size());
			dataMap.put("showType", showType); 
		}

		if (oList.size() <= 4 && dList.size() > 4) {
			showType = "1";
			dataMap = getChartDataByPromotion(tList, showType, dList.size());
			dataMap.put("showType", showType);
		}
		return dataMap;
	}
	
	/**
	 * 获取促销数据
	 * 
	 * @param tList
	 * @param showType
	 * @param size
	 * @return
	 */
	public Map<String, Object> getChartDataByPromotion(List<TerminalObjectEntity> tList, String showType, int size) 
	{
		String xTitle = "";
		String subxTitle = "";
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<EchartLineDataEntity> lineList = new ArrayList<EchartLineDataEntity>();
		for (int i = 0; i < STARRY.length; i++) {
			String subsidy = "";
			String subsidyType = STARRY[i];
			for (int j = 0; j < tList.size(); j++) {
				if(STARRY[i].equals(tList.get(j).getSubsidyType())) {
					subsidy += tList.get(j).getSubsidy() + ",";
					if("0".equals(showType) && i == 0) {
						xTitle += tList.get(j).getDateTime() + ",";
						if(!subxTitle.contains(tList.get(j).getObjectNameEn())) {
							subxTitle += tList.get(j).getObjectNameEn() + ",";
						}
					} else if("1".equals(showType) && i == 0) {
						xTitle += tList.get(j).getObjectNameEn() + ",";
						if(!subxTitle.contains(tList.get(j).getDateTime())) {
							subxTitle += tList.get(j).getDateTime() + ",";
						}
					}
				}
			}
			lineList.add(new EchartLineDataEntity("bar", subsidyType, "emptyCircle", subsidy.substring(0,subsidy.length() - 1).split(",")));
		}
		
		dataMap.put("xTitle", xTitle.substring(0, xTitle.length() - 1).split(","));
		dataMap.put("subxTitle", subxTitle.substring(0, subxTitle.length() - 1).split(","));
		dataMap.put("series", lineList);
		dataMap.put("titles", STARRY);
		return dataMap;
	}
	
	/**
	 * 获取图例
	 * 
	 * @param list
	 * @return
	 */
	public String[] getLineLegend(List<EchartLineDataEntity> list) {
		String[] legned = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			legned[i] = list.get(i).getName();
		}
		return legned;
	}

	/**
	 * 导出
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public Workbook exportExcel(HttpServletRequest request,Map<String, Object> paramsMap) {

		Workbook wb = null;
		String json = (String) request.getSession().getAttribute(Constant.getExportExcelTerminalDataKey);
		if (!AppFrameworkUtil.isEmpty(json)) {
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			JSONObject chartObj = (JSONObject) obj.get("chart");
			//JSONArray gridObj = (JSONArray) obj.getJSONArray("grid");
			String showType = chartObj.getString("showType");

			try {
				String path = request.getSession().getServletContext().getRealPath("/");
				wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/TerminaAnalysisTemplate.xls")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			exportObjectOriginalData(wb, wb.getSheet("原数据"), paramsMap);
			if(!AppFrameworkUtil.isEmpty(showType)){
				exportObjectChartData(wb, wb.getSheet("DATA"), chartObj, paramsMap);
			}
		}
		return wb;
	}

	/**
	 * 导出原始数据
	 * 
	 * @param wb
	 * @param s
	 * @param paramsMap
	 */
	public void exportObjectOriginalData(Workbook wb, Sheet s, Map<String, Object> paramsMap) {
		String languageType = (String) paramsMap.get("languageType");// 语言类型ZH,EN
		String objectType = paramsMap.get("objectType").toString();
		String[] baseTitle = new String[] { "年月", "型号编码", "生产厂", "系别", "级别","型号标识", "车型", "排档方式", "排量", "车身形式", "上市时间", "年式" };
		String[] dataTitle = new String[] { "型号促销支持","型号促销","提车支持", "零售支持", "人员奖励","金融贷款", "置换支持", "赠送保险", "赠送礼品", "型号销量" };
		String[] baseTitleEn = new String[] { "Date", "Code", "Manufacture","Origin", "Segment", "Trim", "Model", "Transmission","Engine Capacity", "Bodytype", "Launch Date", "Model Year" };
		String[] dataTitleEn = new String[] { "Version incentive support","Version incentive","Delivery support","Retail support","Staff bonus","Financial loan", "Trade-in support", "Free insurance","Free present", "Volume by Version" };
		String[] titles = null;// 标题数组
		
		// "年月","型号编码","生产厂","系别","级别","型号标识","车型","拍档方式","排量","车身形式","上市时间","年式"
		if ("EN".equals(languageType)) {
			titles = new String[] { baseTitleEn[0], baseTitleEn[1], baseTitleEn[2],
					baseTitleEn[3], baseTitleEn[4], baseTitleEn[5], baseTitleEn[6], baseTitleEn[7],
					baseTitleEn[8], baseTitleEn[9], baseTitleEn[10],baseTitleEn[11],
					dataTitleEn[0], dataTitleEn[1], dataTitleEn[2], dataTitleEn[3],
					dataTitleEn[4], dataTitleEn[5], dataTitleEn[6], dataTitleEn[7],
					dataTitleEn[8], dataTitleEn[9] };
		} else {
			titles = new String[] { baseTitle[0], baseTitle[1], baseTitle[2],
					 baseTitle[3], baseTitle[4], baseTitle[5], baseTitle[6], baseTitle[7],
					 baseTitle[8], baseTitle[9], baseTitle[10],baseTitle[11],
					 dataTitle[0], dataTitle[1], dataTitle[2], dataTitle[3],
					 dataTitle[4], dataTitle[5], dataTitle[6], dataTitle[7],
					 dataTitle[8], dataTitle[9] };
		}

		int rowIndex = 0;// 行号锁引
		Row row = ExportExcelUtil.createRow(s, rowIndex, 600);
		Cell cell = null;
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);// 表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);// 内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);// 格式化千分位样式

		// 写表格标题
		int length = titles.length;
		for(int i = 0; i < length; i++) {
		    int cellIndex = i;
			cell = row.createCell(cellIndex);
			ExportExcelUtil.setCellValueAndStyle(cell, titles[i], titleStyle);
			s.setColumnWidth(i, 3000);
		}

		List<TerminalOriginalEntity> list = null;
		if("0".equals(objectType)) {
			list = terminalAnalysisDao.exportTerminalDataByVersion(paramsMap);
		}else{
			list = terminalAnalysisDao.exportTerminalData(paramsMap);
		}
		//Map<String, Object> dataMap = new HashMap<String, Object>();

		// 写表格数据内容
		for(int j = 0; j < list.size(); j++) {
			TerminalOriginalEntity obj = (TerminalOriginalEntity) list.get(j);
			List<PromotionInfo> pList = list.get(j).getPList();
			// 如果无记录则不导出
			if (AppFrameworkUtil.isEmpty(obj.getVersioncode()))
				continue;
			// 重置锁引，用于创建单元格列锁引位置
			int index = 0;
			rowIndex++;
			row = ExportExcelUtil.createRow(s, rowIndex, 400);

			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getYearmonth() + "~", textStyle);

			// "年月","型号编码","生产厂","系别","级别","型号标识","车型","拍档方式","排量","车身形式","上市时间","年式"
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getVersioncode() + "~", textStyle);
			
			if("EN".equals(languageType)) {
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getManfnameen(), textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getOrignameen(), textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getGradenameen(), textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getVersionshortnameen(),textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getSubmodelnameen(), textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getGearmodeen(), textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getDischarge(), textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getBodytypeen(), textStyle);

			   } else {
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getManfname(), textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getOrigname(), textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getGradename(), textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getVersionshortname(),textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getSubmodelname(), textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getGearmode(), textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getDischarge(), textStyle);

				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getBodytype(), textStyle);
			}
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getVersionlaunchdate(), textStyle);

			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getModelyear(), textStyle);
			
			for(int i = 0; i < pList.size(); i++) {
				PromotionInfo pInfo = pList.get(i);
				String subsidyType = pInfo.getSubsidyType();
				if(CXZE.equals(subsidyType)) {
					cell = row.createCell(index);
					ExportExcelUtil.setCellValueAndStyle(cell, pInfo.getVersionsubsidy(), thousandthStyle);
					cell = row.createCell(index + 1);
					ExportExcelUtil.setCellValueAndStyle(cell, pInfo.getSubsidy(), thousandthStyle);
				} else if(TCZC.equals(subsidyType)) {
					cell = row.createCell(index + 2);
					ExportExcelUtil.setCellValueAndStyle(cell, pInfo.getSubsidy(), thousandthStyle);
				} else if(LSZC.equals(subsidyType)) {
					cell = row.createCell(index + 3);
					ExportExcelUtil.setCellValueAndStyle(cell, pInfo.getSubsidy(), thousandthStyle);
				} else if(RYJL.equals(subsidyType)) {
					cell = row.createCell(index + 4);
					ExportExcelUtil.setCellValueAndStyle(cell, pInfo.getSubsidy(), thousandthStyle);
				} else if(JRDK.equals(subsidyType)) {
					cell = row.createCell(index + 5);
					ExportExcelUtil.setCellValueAndStyle(cell, pInfo.getSubsidy(), thousandthStyle);
				} else if(ZHZC.equals(subsidyType)) {
					cell = row.createCell(index + 6);
					ExportExcelUtil.setCellValueAndStyle(cell, pInfo.getSubsidy(), thousandthStyle);
				} else if(ZSBX.equals(subsidyType)) {
					cell = row.createCell(index + 7);
					ExportExcelUtil.setCellValueAndStyle(cell, pInfo.getSubsidy(), thousandthStyle);
				} else if(ZSLP.equals(subsidyType)) {
					cell = row.createCell(index + 8);
					ExportExcelUtil.setCellValueAndStyle(cell, pInfo.getSubsidy(), thousandthStyle);
					cell = row.createCell(index + 9);
					ExportExcelUtil.setCellValueAndStyle(cell, pInfo.getVersionSale(), thousandthStyle);
				}
			}
		}
		s.setDisplayGridlines(false);
	}

	/**
	 * 导出型号、车型、厂商、品牌、系别、级别图形数据
	 * 
	 * @param wb
	 * @param s
	 * @param chartObj
	 * @param paramsMap
	 */
	public void exportObjectChartData(Workbook wb, Sheet s,JSONObject chartObj, Map<String, Object> paramsMap) 
	{
		int rowIndex = 0;// 行号锁引
		String priceType = paramsMap.get("priceType").toString();
		Row row = ExportExcelUtil.createRow(s, rowIndex, 600);
		JSONArray xTitles = chartObj.getJSONArray("xTitle");
		JSONArray subxTitle = chartObj.getJSONArray("subxTitle");
		JSONArray titles =  chartObj.getJSONArray("titles");
		JSONArray series =  chartObj.getJSONArray("series");
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);// 表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);// 内容文本样式
		int index = 0;
		Cell cell = null;
		cell = row.createCell(index++);
		//对象
		ExportExcelUtil.setCellValueAndStyle(cell, "对象", titleStyle);
		for(int i = 0; i < xTitles.size(); i++) {
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, "", textStyle);
		}
		
		if(titles.size() < 7) {
			s.addMergedRegion(new CellRangeAddress((short) 0, (short) 0, (short) 1, (short)xTitles.size()));
		} else {
			for(int j = 0; j < subxTitle.size(); j++) {
				int cosNum = xTitles.size() / subxTitle.size();
				if(j == 0) {
					s.addMergedRegion(new CellRangeAddress((short) 0, (short) 0, (short) 1, (short)cosNum));
					row.getCell(1).setCellValue(subxTitle.getString(0));
				} else {
					s.addMergedRegion(new CellRangeAddress((short) 0, (short) 0, (short) (cosNum * j + 1), (short)(cosNum* (j + 1))));
					row.getCell(cosNum * j + 1).setCellValue(subxTitle.getString(j));
				}
			}
		}
		
		index = 0;
		rowIndex++;
		row = ExportExcelUtil.createRow(s, rowIndex, 600);
		cell = row.createCell(index++);
		ExportExcelUtil.setCellValueAndStyle(cell, "指标", titleStyle);
		
		//横坐标
		for(int i = 0; i < xTitles.size(); i++) {
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, xTitles.getString(i), textStyle);
		}
		for(int i = 0; i < titles.size(); i++) {
			index = 1;
			rowIndex++;
			row = ExportExcelUtil.createRow(s, rowIndex, 600);
			
			//指标
			cell = row.createCell(0);
			ExportExcelUtil.setCellValueAndStyle(cell, titles.getString(i), textStyle);
			
			//指标所对应横坐标值
			JSONObject obj = series.getJSONObject(i);
			JSONArray datas = obj.getJSONArray("data");
			for(int j = 0; j < datas.size(); j++) {
				cell = row.createCell(index++);
				if("-".equals(datas.getString(j)) || "".equals(datas.getString(j))) {
					ExportExcelUtil.setCellValueAndStyle(cell, "", textStyle);
				} else if("2".equals(priceType)) {
					ExportExcelUtil.setCellValueAndStyle(cell, (datas.getString(j) + "%"), textStyle);
				} else {
					ExportExcelUtil.setCellValueAndStyle(cell, datas.getString(j), textStyle);
				}
			}
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
	public String getValueByLanguageType(JSONObject obj, String languageType, String property)
	{
		String value = "";
		if ("EN".equals(languageType)) {
			value = obj.getString(property + "En");
		} else {
			value = obj.getString(property);
		}
		return value;
	}
}
