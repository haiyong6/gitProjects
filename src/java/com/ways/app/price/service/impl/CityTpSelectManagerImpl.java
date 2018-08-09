package com.ways.app.price.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
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
import com.ways.app.price.dao.ICityTpSelectDao;
import com.ways.app.price.service.ICityTpSelectManager;

/**
 * 成交价查询Service层接口实现类
 * @author songguobiao
 *
 */
@Service("CityTpSelectManagerImpl")
public class CityTpSelectManagerImpl implements ICityTpSelectManager {

	@Autowired
	private ICityTpSelectDao cityTpSelectDao;
	
	/**
	 * 初始化时间
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@Override
	public void initDate(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		List<Map<String, String>> list = cityTpSelectDao.initDate(paramsMap);
		if(null != list && 0 != list.size()) {
			Map<String, String> resultMap = list.get(0);
			request.setAttribute("beginDate", resultMap.get("BEGINDATE"));
			request.setAttribute("endDate", resultMap.get("ENDDATE"));
			String defaultBeginDate = resultMap.get("ENDDATE").split("-")[0] + "-01";
			request.setAttribute("defaultBeginDate", defaultBeginDate);
		}
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
	    		request.setAttribute("segmentList", cityTpSelectDao.getSubmodelBySegment(paramsMap));
	    	} else if("3".equals(subModelShowType)) {
	    		//返回品牌页
		    	request.setAttribute("brandLetterList", cityTpSelectDao.getSubmodelByBrand(paramsMap));
	    	} else if("4".equals(subModelShowType)) {
	    		//返回厂商页
		    	request.setAttribute("manfLetterList", cityTpSelectDao.getSubmodelByManf(paramsMap));
	    	} else {
	    		//本品页竟品页
		    	request.setAttribute("bpSubModelList", cityTpSelectDao.getSubmodelByBp(paramsMap));
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
			request.setAttribute("bodyTypeList", cityTpSelectDao.getBodyType(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取一汽大众常用型号组
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@Override
	public void getAutoCustomGroup(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("autoCustomGroupList", cityTpSelectDao.getAutoCustomGroup(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取型号成交价数据
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public String getVersionTpData(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		List<Map<String, String>> list = null;
		Integer cityNum = (Integer)paramsMap.get("cityNum");
		String countryAvg = (String)paramsMap.get("countryAvg");
		paramsMap.put("multiple", "0");
		String citys = paramsMap.get("citys").toString();
		paramsMap.put("citys", citys.replace("-1,", "").replace(",-1", ""));
		List<Map<String, String>> cityNames = cityTpSelectDao.getCityNameAndSort(paramsMap);
		StringBuilder cNameEns = new StringBuilder();
		StringBuilder cNames = new StringBuilder();
		StringBuilder sumCitys = new StringBuilder();
		if(cityNames.size() > 0) {
			for(int i = 0; i < cityNames.size(); i++) {
				Map<String, String> map = cityNames.get(i);
				cNames.append("'" + map.get("CITYNAME") + "'").append(" as ").append(map.get("CITYNAME")).append(",");
				cNameEns.append("'" + map.get("CITYNAMEEN") + "'").append(" as ").append(map.get("CITYNAMEEN")).append(",");
				sumCitys.append("sum(" + map.get("CITYNAME") + ") ").append(map.get("CITYNAMEEN")).append(",");
			}
			cNames.delete(cNames.length() - 1, cNames.length());
			cNameEns.delete(cNameEns.length() - 1, cNameEns.length());
			sumCitys.delete(sumCitys.length() - 1, sumCitys.length());
		}
		paramsMap.put("cityNames", cNames);
		paramsMap.put("sumCitys", sumCitys);
		
		//只选了全国均价
		if(cityNum == 1 && "1".equals(countryAvg)) {
			try {
				list = cityTpSelectDao.getVersionTpDataByCountryAvg(paramsMap);
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			//选择多个城市时
			if(cityNum >= 2 && "0".equals(countryAvg) || cityNum >= 3) {
				paramsMap.put("multiple", "1");
			}
			try {
				list = cityTpSelectDao.getVersionTpData(paramsMap);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		if(null != list && list.size() > 0) {
	        json = AppFrameworkUtil.structureConfigParamsGroupJSONData(list);
		}
		request.getSession().setAttribute(Constant.getCityTpSelectExportExcelDataKey, json);
		request.getSession().setAttribute("cityNames", cityNames);
		return json;
	}
	
	/**
	 * 获取城市的名称和排序
	 * 
	 * @param paramsMap
	 * @return
	 */
	@Override
    public String getCityNameAndSort(Map<String, Object> paramsMap) {
    	String json = "";
    	List<Map<String, String>> list = cityTpSelectDao.getCityNameAndSort(paramsMap);
        if(null != list && list.size() > 0) {
        	json = AppFrameworkUtil.structureConfigParamsGroupJSONData(list);
        }
        return json;
    }
	
	/**
	 * 导出Excel
	 * 
	 * @param request 
	 * @param paramsMap
	 * @return
	 */
	@Override
	public Workbook exportExcel(HttpServletRequest request, Map<String, Object> paramsMap)
	{
		Workbook wb = null;
		String json = (String) request.getSession().getAttribute(Constant.getCityTpSelectExportExcelDataKey);
		String path = request.getSession().getServletContext().getRealPath("/"); 
		if(!AppFrameworkUtil.isEmpty(json)) {
			JSONArray obj = (JSONArray) JSONObject.parse(json);
			try {
				if(!"EN".equals(paramsMap.get("languageType").toString())) {
					wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/cityTpSelect.xls")));
					exportOriginalData(wb, wb.getSheet("数据显示"), obj, request,paramsMap);
				} else {
					wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/cityTpSelectEn.xls")));
					exportOriginalData(wb, wb.getSheet("data"), obj, request, paramsMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return wb;
    }

	/**
	 * 导出原始数据
	 * 
	 * @param wb
	 * @param sheet
	 * @param gridObj
	 * @param paramsMap
	 */
	@SuppressWarnings("unchecked")
	public void exportOriginalData(Workbook wb, Sheet sheet, JSONArray gridObj, HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		int rowIndex = 0;
		Row row = null;
		Cell cell = null;
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		CellStyle thousandthREDStyle =ExportExcelUtil.getExcelFormatThousandthREDStyle(wb);
		//城市名称
		List<Map<String, String>> cityNameList = (List<Map<String, String>>)request.getSession().getAttribute("cityNames");
		String languageType = paramsMap.get("languageType").toString();
		String countryAvg = paramsMap.get("countryAvg").toString();
		Integer cityNum = Integer.parseInt(paramsMap.get("cityNum").toString());
		String citys = paramsMap.get("citys").toString();
		row = sheet.getRow(rowIndex);
		//当前表头的列数
		int cellNum = row.getPhysicalNumberOfCells();
		//至少选了一个城市时
		if((cityNum == 1 && "0".equals(countryAvg)) || cityNum >= 2) {
			for(int i = 0; i < cityNameList.size(); i++) {
				Map<String, String> map = cityNameList.get(i);
				Cell c = row.createCell(cellNum);
				c.setCellStyle(row.getCell(8).getCellStyle());
				sheet.setColumnWidth(cellNum, 20 * 256);
				cellNum++;
				if("EN".equals(languageType)) {
					c.setCellValue(map.get("CITYNAMEEN"));
				} else {
					c.setCellValue(new HSSFRichTextString(map.get("CITYNAME") + "\r\n" + map.get("CITYNAMEEN")));
				}
			}
		}
		//选择多个城市时
		if((cityNum >= 2 && "0".equals(countryAvg)) || cityNum >= 3) {
			Cell c1 = row.createCell(cellNum);
			c1.setCellStyle(row.getCell(8).getCellStyle());
			sheet.setColumnWidth(cellNum, 20 * 256);
			cellNum++;
			Cell c2 = row.createCell(cellNum);
			c2.setCellStyle(row.getCell(8).getCellStyle());
			sheet.setColumnWidth(cellNum, 20 * 256);
			cellNum++;
			if("EN".equals(languageType)) {
				c1.setCellValue("Citys Avg");
				c2.setCellValue("Citys MixAvg");
			} else {
				c1.setCellValue(new HSSFRichTextString("所选城市算术平均价\r\nCitys Avg"));
				c2.setCellValue(new HSSFRichTextString("所选城市加权平均价\r\nCitys MixAvg"));
			}
		}
		//选了全国均价时
		if("1".equals(countryAvg)) {
			Cell c = row.createCell(cellNum);
			c.setCellStyle(row.getCell(8).getCellStyle());
			sheet.setColumnWidth(cellNum, 20 * 256);
			if("EN".equals(languageType)) {
				c.setCellValue("Country Avg");
			} else {
				c.setCellValue(new HSSFRichTextString("全国均价\r\nCountry Avg"));
			}
			cellNum++;
		}
		if((cityNum >= 2 && "0".equals(countryAvg)) || cityNum >= 3) {
			Cell c3 = row.createCell(cellNum);
			c3.setCellStyle(row.getCell(2).getCellStyle());
			sheet.setColumnWidth(cellNum, 20 * 256);
			if("EN".equals(languageType)) {
				c3.setCellValue("Promotions");
			} else {
				c3.setCellValue(new HSSFRichTextString("内部促销信息\r\nPromotions"));
			}
		}
		//写表格数据内容
		for(int j = 0; j < gridObj.size(); j++) {
			JSONObject obj = gridObj.getJSONObject(j);
			rowIndex++;
			row = ExportExcelUtil.createRow(sheet, rowIndex, 400);
			int i = 0;
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("PRICETIME") + " ", textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "BATCH"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("VERSIONCODE") + "~", textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("SEGMENTNAME"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "BRANDNAME"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "MANFNAME"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell,getValueByLanguageType(obj, languageType, "SUBMODELNAME"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("ENGINECAPACITY"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "TRANSMISSIONTYPE"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "VERSIONMARK"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "BODYTYPENAME"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("LAUNCHDATE"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("YEAR"), textStyle);
			
			cell = row.createCell(i++);
			ExportExcelUtil.setCellValueAndStyles(cell, obj.getString("MSRP"), thousandthStyle, thousandthREDStyle);
			
			//不是只选全国均价时要显示各城市的成交价
			if((cityNum == 1 && "0".equals(countryAvg)) || cityNum >= 2) {
				for(int k = 0; k < cityNameList.size(); k++) {
					Map<String, String> map = cityNameList.get(k);
					cell = row.createCell(i++);
					ExportExcelUtil.setCellValueAndStyleEx(cell, obj.getString(map.get("CITYNAMEEN")), thousandthStyle, thousandthREDStyle);
				}
			}
			// 如果是城市多选
			if(citys.indexOf("0") != -1 || (cityNum >=2 && "0".equals(countryAvg)) || cityNum >= 3) {
				cell = row.createCell(i++);
				ExportExcelUtil.setCellValueAndStyleEx(cell, obj.getString("AMTAVG"), thousandthStyle, thousandthREDStyle);
				cell = row.createCell(i++);
				ExportExcelUtil.setCellValueAndStyleEx(cell, obj.getString("MIXAVG"), thousandthStyle, thousandthREDStyle);
			}
			// 如果有选全国均价
			if("1".equals(countryAvg)) {
				cell = row.createCell(i++);
				ExportExcelUtil.setCellValueAndStyleEx(cell, obj.getString("COUNTRYAVG"), thousandthStyle, thousandthREDStyle);
			}
			// 如果是城市多选
			if(citys.indexOf("0") != -1 || (cityNum >= 2 && "0".equals(countryAvg)) || cityNum >= 3) {
				cell = row.createCell(i++);
				CellStyle cs = cell.getCellStyle();
				cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				ExportExcelUtil.setCellValueAndStyleEx(cell, obj.getString("PROMOTIONS"), cs, cs);
			}
		}
	}

	/**
	 * 根据语言类型获取值
	 * 
	 * @param chartObj
	 * @param languageType
	 * @param property
	 * @return
	 */
	private String getValueByLanguageType(JSONObject obj, String languageType, String property) 
	{
		String value = "";
		if("EN".equals(languageType)) {
			value = obj.getString(property + "EN");
		} else {
			value = obj.getString(property);
		}
		return value;
	}
}
