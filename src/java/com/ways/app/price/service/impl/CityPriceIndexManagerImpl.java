package com.ways.app.price.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.framework.utils.Constant;
import com.ways.app.framework.utils.EchartsUtil2;
import com.ways.app.framework.utils.ExportExcelUtil;
import com.ways.app.price.dao.ICityPriceIndexDao;
import com.ways.app.price.model.CityPriceIndexEntity;
import com.ways.app.price.model.EchartLineDataEntity;
import com.ways.app.price.model.InitialDate;
import com.ways.app.price.model.VersionCityPriceIndexInfo;
import com.ways.app.price.service.ICityPriceIndexManager;

/**
 * 区域价格降幅Service层接口实现类
 * @author yinlue
 *
 */
@Service("CityPriceIndexManagerImpl")
public class CityPriceIndexManagerImpl implements ICityPriceIndexManager {

	@Autowired
	private ICityPriceIndexDao cityPriceIndexDao;

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
		List<Map<String, String>> list = (List<Map<String, String>>) cityPriceIndexDao.initDate(paramsMap);
		if(null != list && list.size() > 0) {
			String beginDate = list.get(0).get("BEGINDATE");
			String endDate = list.get(0).get("ENDDATE");
			String defaultBeginDate = endDate.substring(0, 4) + "-01";
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("defaultBeginDate", defaultBeginDate);
		}
	}

	/**
	 * 获取初始化城市控件值
	 */
	@Override
	public void getCityModal(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			request.setAttribute("areaList", cityPriceIndexDao.getCityModal(paramsMap));
		} catch (Exception e) {
			
		}
	}

	/**
	 * 获取价格降幅分析数据
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public String getCityPriceIndexAnalyseData(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		Map<String, Object> saveMap = new HashMap<String, Object>();
		Map<String, String> eNameMap = new HashMap<String, String>(); 
		eNameMap = getExportContentEN(paramsMap);
		Map<String, String> eNameMapCity = new HashMap<String, String>(); 
		if(paramsMap.get("citySplit").equals("true")){
			eNameMapCity = getExportCityContentEN(paramsMap);
		}

		saveMap.putAll(paramsMap);
		
		//保存查询条件
		request.getSession().setAttribute(Constant.getCityPriceIndexExportMapKey, saveMap);
		//时间多选
		if("1".equals(paramsMap.get("multiType"))) {
			json = dateMultiSel(request, paramsMap);
		//对象多选
		} else {
			json = objectMultiSel(request, paramsMap);
		}
		//保存图形JSON数据
		request.getSession().setAttribute(Constant.getCityPriceIndexExportExcelDataKey, json);
		//保存英文字段
		request.getSession().setAttribute(Constant.getCityPriceIndexExportExcelDataEnKey, eNameMap);
		request.getSession().setAttribute(Constant.getCityPriceIndexExportExcelDataEnCityKey, eNameMapCity);
		
		return json;
	}

	

	/**
	 * 获取查询维度
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String,String> getExportContentEN(Map<String, Object> paramsMap) 
	{
		Map<String, Object> paramsExportMap = new HashMap<String, Object>();
		
		//时间组
		String dateGroup = (String) paramsMap.get("dateGroup");//时间组以|隔开
		dateGroup = dateGroup.substring(0, dateGroup.length() - 1);
		
		String[] date = dateGroup.split("\\|");
		String year = "";
		for(int i = 0 ; i < date.length; i++){
			year += date[i].split(",")[0].substring(0,4) + ",";
		}
		paramsExportMap.put("year", year.substring(0, year.length()-1));
		
		String segmentType = "false";
		String origType = "false";
		String brandType = "false";
		String manfType = "false";
		String modelType = "false";
		
		String segment = "'-100'";
		String orig = "'-100'";
		String brand = "'-100'";
		String manf = "'-100'";
		String model = "'-100'";
		
		String [] objArray = paramsMap.get("objId").toString().split("\\|");
		String [] typeArray = paramsMap.get("objectType").toString().split("\\|");
		
		for (int i = 0; i < typeArray.length; i++) {
			switch (Integer.parseInt(typeArray[i])) {
			case 0:
				segmentType = "true";
				String allFlag = "false";
				String[] segmentId = objArray[i].split(",");
				if(segmentId.length > 1) {
					if(segmentId[0].length() == 1) {
						allFlag = "true";
					}
				} else {
					if(!"0".equals(segmentId[0]) && segmentId[0].length() == 1) {
						allFlag = "true";
					}
				}
				paramsMap.put("allFlag", allFlag);
				if(i < objArray.length) {
					segment += "," + objArray[i];
				}
				break;
			case 1:
				manfType = "true";
				if(i < objArray.length) {
					manf += "," + "'" + objArray[i] + "'";
				}
				break;
			case 2:
				brandType = "true";
				if(i < objArray.length) {
					brand += "," + objArray[i];
				}
				break;
			case 3:
				modelType = "true";
				if(i < objArray.length) {
					model += "," + objArray[i];
				}
				break;
			case 4:
				origType = "true";
				if(i < objArray.length) {
					orig += "," + objArray[i];
				}
				break;	
			}
		}
		paramsExportMap.put("segmentType", segmentType);
		paramsExportMap.put("origType", origType);
		paramsExportMap.put("brandType", brandType);
		paramsExportMap.put("manfType", manfType);
		paramsExportMap.put("modelType", modelType);
		paramsExportMap.put("segment", segment);
		paramsExportMap.put("orig", orig);
		paramsExportMap.put("brand", brand);
		paramsExportMap.put("manf", manf);
		paramsExportMap.put("model", model);
		paramsExportMap.put("citySplit", (String)paramsMap.get("citySplit"));
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		
		list = (List<Map<String, String>>) cityPriceIndexDao.getExportContentEN(paramsExportMap);
		
		Map<String, String> eNameMap = new HashMap<String, String>();
		if(null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				eNameMap.put(list.get(i).get("KEY"), list.get(i).get("VALUE"));
			}
		}
		return eNameMap;
	}
	
	
	/**
	 * 获取城市中英文对照
	 * 
	 * @param paramsMap
	 * @return
	 */
	private Map<String,String> getExportCityContentEN(Map<String, Object> paramsMap) 
	{
		List<Map<String, String>> listCity = new ArrayList<Map<String,String>>();
		listCity = (List<Map<String, String>>) cityPriceIndexDao.getExportCityContentEN(paramsMap);
		
		Map<String, String> eNameMapCity = new HashMap<String, String>();
		if(null != listCity && listCity.size() > 0) {
			for (int i = 0; i < listCity.size(); i++) {
				eNameMapCity.put(listCity.get(i).get("KEY"), listCity.get(i).get("VALUE"));
			}
		}
		return eNameMapCity;
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
		
		String citySplit = (String)paramsMap.get("citySplit");//城市是否拆分：true为拆分，false为不拆分
		
		String cityIds = (String)paramsMap.get("cityIds");//城市ID
		
		String[] cityId = cityIds.split(",");
		
		String json = "";
		
		String dateGroup = (String) paramsMap.get("dateGroup");//时间组以|隔开
		dateGroup = dateGroup.substring(0, dateGroup.length() - 1);
		
		String objId = (String) paramsMap.get("objId");//选择框对象ID以|隔开
		objId = objId.substring(0, objId.length() - 1);
		
		String objectType = (String) paramsMap.get("objectType");//选择框对象类型ID以|隔开
		objectType = objectType.substring(0, objectType.length() - 1);
		paramsMap.put("objectType", objectType);
		
		String objName = (String) paramsMap.get("objName");//选择框对象名称以|隔开
		objName = objName.substring(0, objName.length() - 1);		
		
		String bodyTypeId = (String) paramsMap.get("bodyTypeId");//选择框车身形式ID以|隔开
		bodyTypeId = bodyTypeId.substring(0, bodyTypeId.length() - 1);	
		if("0".equals(bodyTypeId)) {
			paramsMap.remove("bodyTypeId");
		}
		
		String bodyTypeName = (String) paramsMap.get("bodyTypeName");//选择框车身形式名称以|隔开
		bodyTypeName = bodyTypeName.substring(0, bodyTypeName.length() - 1);		
		
		if(!AppFrameworkUtil.isEmpty(dateGroup)) {	
			String[] dates = dateGroup.split("\\|");
			//定义返回结果MAP
			Map<String, Object> resultMap = new HashMap<String, Object>();
			//线图数据集合
			List<EchartLineDataEntity> lineData = new ArrayList<EchartLineDataEntity>();
			//定义线图X轴数组
			String[] xTitles = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
			//定义图例数组
			String[] legends = null;
			if(citySplit.equals("true")){
				legends = new String[dates.length*cityId.length];
			} else{
				legends = new String[dates.length];
			}
			//保存第一条线图数据，用来计算柱状图
			String[] firstChartData = null;
			
			String objIdKey = "gradeId";
			if("1".equals(paramsMap.get("objectType"))) {
				objIdKey = "manfId";
				StringBuilder ids = new StringBuilder();
				for(int k = 0; k < objId.split(",").length; k++) {
					String id = objId.split(",")[k];
					ids.append("'").append(id).append("'").append(",");
				}
				objId = ids.substring(0, ids.length() - 1).toString();
			} else if("2".equals(paramsMap.get("objectType"))) {
				objIdKey = "brandId";
			} else if("3".equals(paramsMap.get("objectType"))) {
				objIdKey = "subModelId";
			} else if("4".equals(paramsMap.get("objectType"))) {
				objIdKey = "origId";
			}
			
			int L = 0;
			for(int i = 0; i < dates.length; i++) {
				String beginDate = dates[i].split(",")[0]; //开始时间
				String endDate = dates[i].split(",")[1];   //结束时间
				/*//计算月数
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
				Date begin = null;
				Date end = null;
				try {
					begin = sdf.parse(beginDate);
					end=sdf.parse(endDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				@SuppressWarnings("deprecation")
				int months=(end.getYear()-begin.getYear())*12+(end.getMonth()-begin.getMonth())+1;*/
				String legend = beginDate.substring(0, 4); //图例
				paramsMap.put("beginDate", beginDate.replace("-", ""));
				paramsMap.put("endDate", endDate.replace("-", ""));
				paramsMap.put(objIdKey, objId);
				if(!"0".equals(bodyTypeId)) {
					paramsMap.put("bodyTypeId", bodyTypeId);
				}
				
				List<CityPriceIndexEntity> list = null;
				//成交价维度当时间段大于2008年时采用新算法
				if(paramsMap.get("priceType").equals("1") && Integer.parseInt(paramsMap.get("beginDate").toString().substring(0,4)) > 2008 && Integer.parseInt(paramsMap.get("endDate").toString().substring(0,4)) > 2008){
					list = cityPriceIndexDao.getCityPriceIndexAnalyseNewData(paramsMap);
				} else{
					list = cityPriceIndexDao.getCityPriceIndexAnalyseData(paramsMap);
				}
				
				if(null != list && list.size() > 0) {
					//定义数据
					String[] data = null; 
					String cityName = null;
					if(citySplit.equals("true")){
						for(int t = 0; t < cityId.length; t++){
							data = new String[12];
							cityName = new String();
							int k = 0;
							/*if(dates.length == 1){
								k = Integer.parseInt(beginDate.substring(5,7))-1;
							}*/
							for(int j = 0; j < list.size(); j++) {
								CityPriceIndexEntity priceIndex = list.get(j);
								if(cityId[t].equals(priceIndex.getCityId())){
									data[k] = priceIndex.getValue();
									cityName = priceIndex.getCityName();
									k++;
								}
							}
							legends[L] = legend + " " + cityName;
							L++;
							iteratorArray(data);
							lineData.add(new EchartLineDataEntity("line", legend + " " + cityName, "", data));
						}
					} else{
						data = new String[12];
						for(int j = 0; j < list.size(); j++) {
							CityPriceIndexEntity priceIndex = list.get(j);
							data[j] = priceIndex.getValue();
						}
						legends[i] = legend;
						iteratorArray(data);
						lineData.add(new EchartLineDataEntity("line", legend, "", data));
					}
					if(null == firstChartData) {
						firstChartData = data;
					}
				}
			}
			//计算柱状图形
			//lineData.add(getBarChartDate(firstChartData));
			/*legends[legends.length - 1] = "月度变化";*/
			resultMap.put("xTitles", xTitles);
			resultMap.put("series", lineData);
			resultMap.put("legends", legends);
			resultMap.put("boundarys", EchartsUtil2.setLineScaleDivision(lineData));
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(resultMap);
		}
		return json;
	}
	
	/**
	 * 迭代数组
	 * 
	 * @param arrs
	 */
	public void iteratorArray(String[] arrs)
	{
		for(int i = 0; i < arrs.length; i++) {
			if(AppFrameworkUtil.isEmpty(arrs[i])) arrs[i] = "-";
		}
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
		
		String dateGroup = (String) paramsMap.get("dateGroup");//时间组以|隔开
		dateGroup = dateGroup.substring(0, dateGroup.length() - 1);		
		String beginDate = dateGroup.split(",")[0]; //开始时间
		String endDate = dateGroup.split(",")[1];   //结束时间
		
		//计算月数
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		Date begin = null;
		Date end = null;
		try {
			begin = sdf.parse(beginDate);
			end=sdf.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		@SuppressWarnings("deprecation")
		int months=(end.getYear()-begin.getYear())*12+(end.getMonth()-begin.getMonth())+1;
		
		String citySplit = (String) paramsMap.get("citySplit");//城市是否拆分，true是拆分，false是不拆分
		String cityIds = (String) paramsMap.get("cityIds");//城市id
		paramsMap.put("beginDate", beginDate.replace("-", ""));
		paramsMap.put("endDate", endDate.replace("-", ""));
		
		String objId = (String) paramsMap.get("objId");//选择框对象ID以|隔开
		objId = objId.substring(0, objId.length() - 1);
		
		String objectType = (String) paramsMap.get("objectType");//选择框对象类型ID以|隔开
		objectType = objectType.substring(0, objectType.length() - 1);
		
		String objName = (String) paramsMap.get("objName");//选择框对象名称以|隔开
		objName = objName.substring(0, objName.length() - 1);		
		
		String bodyTypeId = (String) paramsMap.get("bodyTypeId");//选择框车身形式ID以|隔开
		bodyTypeId = bodyTypeId.substring(0, bodyTypeId.length() - 1);	
		if("0".equals(bodyTypeId)) {
			paramsMap.remove("bodyTypeId");
		}
		
		String bodyTypeName = (String) paramsMap.get("bodyTypeName");//选择框车身形式名称以|隔开
		bodyTypeName = bodyTypeName.substring(0, bodyTypeName.length() - 1);
		
		if(!AppFrameworkUtil.isEmpty(objId)) {
			String[] objIds = objId.split("\\|");
			String[] objNames = objName.split("\\|");
			String[] bodyTypeIds = bodyTypeId.split("\\|");
			String[] bodyTypeNames = bodyTypeName.split("\\|");	
			String[] objectTypes = objectType.split("\\|");
			
			//定义返回结果MAP
			Map<String, Object> resultMap = new HashMap<String, Object>();
			//线图数据集合
			List<EchartLineDataEntity> lineData = new ArrayList<EchartLineDataEntity>();
			//定义线图X轴数组
			String[] xTitles = null;
			//定义图例数组
			String[] legends = null;
			if(citySplit.equals("true")){
				legends =  new String[objIds.length*cityIds.split(",").length];
			} else{
				legends =  new String[objIds.length];
			}
			boolean flag = false;
			String allFlag;
			
			int L = 0;
			for(int i = 0; i < objIds.length; i++) {
				String objIdKey = "";
				allFlag = "false";
				if("1".equals(objectTypes[i])) {
					objIdKey = "manfId";
					StringBuilder ids = new StringBuilder();
					for(int k = 0; k < objIds[i].split(",").length; k++) {
						String id = objIds[i].split(",")[k];
						ids.append("'").append(id).append("'").append(",");
					}
					objIds[i] = ids.substring(0, ids.length() - 1).toString();
				} else if("2".equals(objectTypes[i])) {
					objIdKey = "brandId";
				} else if("3".equals(objectTypes[i])) {
					objIdKey = "subModelId";
				} else if("4".equals(objectTypes[i])) {
					objIdKey = "origId";
				} else {
					objIdKey = "gradeId";
					if(objIds[i].split(",").length > 1) {
						flag = true;
						// 选择某些大类进行全选时
						if(objIds[i].split(",")[0].length() == 1) {
							allFlag = "true";
						}
					} else {
						if(!"0".equals(objIds[i]) && objIds[i].split(",")[0].length() == 1) {
							allFlag = "true";
						}
						flag = false;
					}
					paramsMap.put("allFlag", allFlag);
				}
				paramsMap.put("objectType", objectTypes[i]);
				paramsMap.put(objIdKey, objIds[i]);
				paramsMap.put("bodyTypeId", bodyTypeIds[i]);
				if("0".equals(bodyTypeIds[i])) {
					paramsMap.remove("bodyTypeId");
				}
				List<CityPriceIndexEntity> list = null;
				try {
					//成交价维度当时间段大于2008年时采用新算法
					if(paramsMap.get("priceType").equals("1") && Integer.parseInt(paramsMap.get("beginDate").toString().substring(0,4)) > 2008 && Integer.parseInt(paramsMap.get("endDate").toString().substring(0,4)) > 2008){
						list = cityPriceIndexDao.getCityPriceIndexAnalyseNewData(paramsMap);
					} else{
						list = cityPriceIndexDao.getCityPriceIndexAnalyseData(paramsMap);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(null != list && list.size() > 0) {
					String legend = "";
					if("0".equals(objectTypes[i])) {
						if(!flag && !"0".equals(objIds[i].split(",")[0]) && !"1".equals(objIds[i].split(",")[0]) && 
								!"2".equals(objIds[i].split(",")[0]) && !"3".equals(objIds[i].split(",")[0])) {
							legend = getObjName(objIds[i]);
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
					
					xTitles = new String[months];

					//String[] cityName = null;
					String[] cityId = null;
					if(citySplit.equals("true")){
						// cityName = new String[months];
						 cityId = cityIds.split(",");
						 for(int t = 0; t < cityId.length; t++){
								String[] data = new String[months];
							 int k = 0;
							 String cityName = new String();
							 for(int j = 0; j < list.size(); j++){
								 CityPriceIndexEntity priceIndex = list.get(j);
								 if(cityId[t].equals(priceIndex.getCityId())){
									 xTitles[k] = priceIndex.getYearmonth();
									 if(null == priceIndex.getValue()){
										 data[k] = "-";
									 } else{
											data[k] = priceIndex.getValue();
									 }
										cityName = priceIndex.getCityName();
										k++;
								 }
							 }
							 if(cityName != null && !cityName.equals("")){
								 //如果城市拆分情况下，单对象则只显示城市
								 if(objIds.length == 1){
									 legends[L] =  cityName;
								 } else{
									 legends[L] = legend + " " + cityName;
								 }
								 for(int p = 0;p<data.length;p++){
										if(data[p] == null){
											data[p] = "-";
										} 
									 }
								 if(objIds.length == 1){
									 lineData.add(new EchartLineDataEntity("line", cityName, "", data));
								 } else{
									 lineData.add(new EchartLineDataEntity("line", legend + " " + cityName, "", data));
								 }
									 L++;
									
							 } else{
								 
								 //legends[L] = "-" + " " + cityName;
								  for(int p = 0;p<data.length;p++){
										if(data[p] == null){
											data[p] = "-";
										} 
									 }
									 lineData.add(new EchartLineDataEntity("line", "-", "", data));
									 
							 }
							 
						 }
					} else{
						legends[i] = legend;
						String[] data = new String[months];
						for(int j = 0; j < list.size(); j++) {
							CityPriceIndexEntity priceIndex = list.get(j);
							xTitles[j] = priceIndex.getYearmonth();
							data[j] = priceIndex.getValue();
							//cityName[j] = priceIndex.getCityName();
						}
						lineData.add(new EchartLineDataEntity("line", legend, "", data));
					}
					
					
				}
			}
			resultMap.put("xTitles", xTitles);
			resultMap.put("series", lineData);
			resultMap.put("legends", legends);
			resultMap.put("boundarys", EchartsUtil2.setLineScaleDivision(lineData));
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(resultMap);
		}
		return json;
	}
	
	/**
	 * 获取对象名称
	 * 
	 * @param gradeId
	 * @return
	 */
	private String getObjName(String gradeId) {
		Map<String, Integer> paramsMap = new HashMap<String, Integer>();
		paramsMap.put("gradeId", Integer.parseInt(gradeId));
		return cityPriceIndexDao.getObjName(paramsMap);
	}
	
	/**
	 * 根据第一条线图获取月差柱形图
	 * 
	 * @param data
	 * @return
	 */
	public EchartLineDataEntity getBarChartDate(String[] data)
	{
		EchartLineDataEntity chart = null;
		String[] barData = new String[data.length];
		if(null != data && data.length > 0) {
			boolean countFirstMonth = true;//计算首个月度 差
			for(int i = 0; i < data.length; i++) {
				if(0 == i) {
					barData[i] = "-0";
					if(!"-".equals(data[i])) {
						barData[i] = AppFrameworkUtil.getNum((Float.parseFloat(data[i]) - 100), 2);
						countFirstMonth = false;
					}
					continue;
				}
				
				//上月值
				String lastMonthValue = data[i - 1];
				//本月值
				String monthValue = data[i];
				//处理时间段不是一月份开始的首个月度差
				if(!"-".equals(monthValue)) {
					if(countFirstMonth)	{
						barData[i] = AppFrameworkUtil.getNum((Float.parseFloat(data[i]) - 100), 2);
						countFirstMonth = false;
						continue;
					}
				}
				
				if("-".equals(lastMonthValue) || "-".equals(monthValue)) {
					barData[i] = "-0";
					continue;
				}
				barData[i] = AppFrameworkUtil.getNum((Float.parseFloat(monthValue) - Float.parseFloat(lastMonthValue)), 2);
			}
			chart = new EchartLineDataEntity("bar", "月度变化", "", barData);
		}
		return chart;
	}

	/**
	 * 导出Excel
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
		HashMap<String, Object> conditionMap = (HashMap<String, Object>) request.getSession().getAttribute(Constant.getCityPriceIndexExportMapKey);
		conditionMap.putAll(paramsMap);
		//获取图形JSON
		String json = (String) request.getSession().getAttribute(Constant.getCityPriceIndexExportExcelDataKey);
		
		Map<String, Object> runMap = new HashMap<String, Object>();
		runMap.putAll(conditionMap);
		
		Workbook wb = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/"); 
			wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/cityPriceIndexTemplate.xls")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//是否导出月度差柱状图
		boolean isBar = false;
		//时间多选
		if("1".equals(conditionMap.get("multiType"))) {
			exportMulitDate(wb, runMap);
			isBar = true;
			//对象多选
		} else {
			exportMulitObj(wb, runMap, request);
		}
		
		//导出图形数据
		exportChartData(wb, wb.getSheet("data"), json, isBar, conditionMap, request);
		return wb;
	}
	
	/**
	 * 导出图形数据
	 * 
	 * @param wb
	 * @param s
	 * @param json
	 * @param isBar 是否计算柱状图
	 */
	@SuppressWarnings("unchecked")
	public void exportChartData(Workbook wb, Sheet s, String json, boolean isBar, Map<String, Object> paramsMap, HttpServletRequest request)
	{
		if(!AppFrameworkUtil.isEmpty(json)) {
			Map<String, String> eNameMap = (Map<String, String>) request.getSession().getAttribute(Constant.getCityPriceIndexExportExcelDataEnKey);//获取英文字段
			Map<String, String> eNameMapCity = (Map<String, String>) request.getSession().getAttribute(Constant.getCityPriceIndexExportExcelDataEnCityKey);//获取城市英文字段

			String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
			String citySplit = (String) paramsMap.get("citySplit");//城市是否拆分
			
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			JSONArray xTitles = obj.getJSONArray("xTitles");//标题
			JSONArray series = obj.getJSONArray("series");//数据块
			
			CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
			CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
			CellStyle textStyle2 = getExcelFillTextStyle(wb);//内容文本样式无边框
			
			Font font = wb.createFont();   
			font.setColor(IndexedColors.RED.index);//设置字体颜色
			CellStyle textRedStyle = ExportExcelUtil.getExcelFillTextStyle(wb);
			textRedStyle.setFont(font);
			
			int rowIndex = 0;//行号锁引
			int cellIndex = 0;//列锁引
			Row row = ExportExcelUtil.createRow(s, rowIndex, 600);
			Cell cell = null;
			
			//写标题 
			for(int i = 0; i < xTitles.size(); i++) {
				cell = row.createCell(++cellIndex);
				String title = xTitles.getString(i);
				//后优化英文版
				if("EN".equals(languageType)) {
					if(2 == title.length()) {
						title = title.replace("1月", "Jan");
						title = title.replace("2月", "Feb");
						title = title.replace("3月", "Mar");
						title = title.replace("4月", "Apr");
						title = title.replace("5月", "May");
						title = title.replace("6月", "June");
						title = title.replace("7月", "Jul");
						title = title.replace("8月", "Aug");
						title = title.replace("9月", "Sep");
					} else {
						title = title.replace("10月", "Oct");
						title = title.replace("11月", "Nov");
						title = title.replace("12月", "Dec");
					}
				}
				ExportExcelUtil.setCellValueAndStyle(cell, title, titleStyle);
				s.setColumnWidth(i + 1, 3000);
			}
			
			//对象多选时增加累计变化(累计变化=最新月-100%)
		/*	if(!isBar) {
				String MoM = "环比";
				String YTD = "累计变化";
				if("EN".equals(languageType)) {
					MoM = MoM.replace("环比", "MoM");
					YTD = YTD.replace("累计变化", "YTD Change");
				}
				cell = row.createCell(++cellIndex);
				ExportExcelUtil.setCellValueAndStyle(cell,MoM, titleStyle);
				s.setColumnWidth(xTitles.size() + 1, 3000);
				
				cell = row.createCell(++cellIndex);
				ExportExcelUtil.setCellValueAndStyle(cell, YTD, titleStyle);
				s.setColumnWidth(xTitles.size() + 2, 3000);
			}*/
			//为照顾到宏打两个空单元格
			/*if(!isBar) {*/
				String MoM = "环比";
				String YTD = "累计变化";
				if("EN".equals(languageType)) {
					MoM = MoM.replace("环比", "MoM");
					YTD = YTD.replace("累计变化", "YTD Change");
				}
				cell = row.createCell(++cellIndex);
				ExportExcelUtil.setCellValueAndStyle(cell,"", textStyle2);
				s.setColumnWidth(xTitles.size() + 1, 3000);
				
				cell = row.createCell(++cellIndex);
				ExportExcelUtil.setCellValueAndStyle(cell, "", textStyle2);
				s.setColumnWidth(xTitles.size() + 2, 3000);
			/*}*/
			
			
			//写数据
			for(int j = 0; j < series.size(); j++) {
				cellIndex = 0;
				JSONObject dataObj = series.getJSONObject(j);
				JSONArray datas = dataObj.getJSONArray("data");
				
				//如果是柱状图
				if("bar".equals(dataObj.getString("type"))) {
					if(!isBar) {
						continue;
					}
				}
				String name = dataObj.getString("name");
				//如果没有数据则不显示
				if(name.equals("-")){
					continue;
				}
				row = ExportExcelUtil.createRow(s, ++rowIndex, 400);
				cell = row.createCell(cellIndex);
				
				
				
				
				if("EN".equals(languageType)) {
					Iterator<String> iterator = eNameMap.keySet().iterator();
					while (iterator.hasNext()) {
						String str = iterator.next();
						name = name.replaceAll(str, eNameMap.get(str));
					}
					
					if("true".equals(citySplit)){
						Iterator<String> iteratorCity = eNameMapCity.keySet().iterator();
						while (iteratorCity.hasNext()) {
							String str = iteratorCity.next();
							name = name.replaceAll(str, eNameMapCity.get(str));
						}
					}
					name = name.replaceAll("整体市场", "Hold market");
				}
				
				if("EN".equals(languageType)) {
					name = name.replace("月度变化", "MOM var");
				}
				
				ExportExcelUtil.setCellValueAndStyle(cell, name, titleStyle);
				
				for(int k = 0; k < datas.size(); k++) {
					cell = row.createCell(++cellIndex);
					String value = datas.getString(k);
					if("-0".equals(value) || "-".equals(value)) {
						value = "-";
					} else {
						value += "%";
					}
					ExportExcelUtil.setCellValueAndStyle(cell, value, textStyle);
				}
				
				//对象多选时增加累计变化(累计变化=最新月-100%)
				/*if(!isBar) {
					String YTDValue = "-";
					int YTDIndex = 0;
					for(int g = datas.size() -1; g >= 0; g--) {
						YTDValue = datas.getString(g);
						if(!"-0".equals(YTDValue) && !"-".equals(YTDValue)) {
							YTDIndex = g;
							YTDValue = AppFrameworkUtil.getNum(Double.valueOf(YTDValue) - 100, 2) + "%";
							break;
						}
					}
					
					String MoMValue = "-";
					for(int l = YTDIndex -1; l >= 0; l--) {
						MoMValue = datas.getString(l);
						if(!"-0".equals(MoMValue) && !"-".equals(MoMValue)) {
							MoMValue = AppFrameworkUtil.getNum(Double.valueOf(datas.getString(YTDIndex)) - Double.valueOf(MoMValue), 2) + "%";
							break;
						}
					}
					cell = row.createCell(++cellIndex);
					if(MoMValue.contains("-")) {
						ExportExcelUtil.setCellValueAndStyle(cell, MoMValue, textRedStyle);
					} else {
						ExportExcelUtil.setCellValueAndStyle(cell, MoMValue, textStyle);
					}
					s.setColumnWidth(datas.size() + 1, 3000);
					cell = row.createCell(++cellIndex);
					if(YTDValue.contains("-")) {
						ExportExcelUtil.setCellValueAndStyle(cell, YTDValue, textRedStyle);
					} else {
						ExportExcelUtil.setCellValueAndStyle(cell, YTDValue, textStyle);
					}
					s.setColumnWidth(datas.size() + 2, 3000);
				}*/
			}
			
			//计算累计降幅
			if(isBar) {
				/*Float lastValue = 0f;
				JSONObject dataObj = series.getJSONObject(0);
				JSONArray datas = dataObj.getJSONArray("data");
				for(int i = datas.size() - 1; i >= 0; i--) {
					if(!"-".equals(datas.getString(i))) {
						lastValue = Float.parseFloat(datas.getString(i));
						break;
					}
				}
				row = ExportExcelUtil.createRow(s, ++rowIndex, 400);
				cell = row.createCell(0);
				
				String name = "年累计下降";
				if("EN".equals(languageType)) {
					name = "YTD Decrease";
				}*/
				/*ExportExcelUtil.setCellValueAndStyle(cell, dataObj.getString("name") + name, titleStyle);
				cell = row.createCell(1);
				ExportExcelUtil.setCellValueAndStyle(cell,AppFrameworkUtil.getNum((lastValue - 100), 2) + "%", textStyle);*/
			}
			
			//写图形最大值和最小值
			row = s.getRow(0);
			cell = row.createCell(68);
			ExportExcelUtil.setCellValueAndStyle(cell, paramsMap.get("yMin") + "," + paramsMap.get("yMax"), null);
			
			row = s.getRow(1);
			cell = row.createCell(68);
			ExportExcelUtil.setCellValueAndStyle(cell, "-2,4", null);
			
			row = s.getRow(2);
			cell = row.createCell(68);
			ExportExcelUtil.setCellValueAndStyle(cell, (String)paramsMap.get("splitNumber"), null);
		}
	}
	
	
	/**
	 * 导出多时间段原始数据
	 * 
	 * @param wb
	 * @param paramsMap
	 */
	public void exportMulitDate(Workbook wb, Map<String, Object> paramsMap)
	{
		
		String dateGroup = (String) paramsMap.get("dateGroup");//时间组以|隔开
		dateGroup = dateGroup.substring(0, dateGroup.length() - 1);
		
		String objId = (String) paramsMap.get("objId");//选择框对象ID以|隔开
		objId = objId.substring(0, objId.length() - 1);
		
		String objectType = (String) paramsMap.get("objectType");//选择框对象类型ID以|隔开
		objectType = objectType.substring(0, objectType.length() - 1);
		paramsMap.put("objectType",objectType);
		
		String objName = (String) paramsMap.get("objName");//选择框对象名称以|隔开
		objName = objName.substring(0, objName.length() - 1);		
		
		String bodyTypeId = (String) paramsMap.get("bodyTypeId");//选择框车身形式ID以|隔开
		bodyTypeId = bodyTypeId.substring(0, bodyTypeId.length() - 1);	
		if("0".equals(bodyTypeId)) {
			paramsMap.remove("bodyTypeId");
		}
		
		String bodyTypeName = (String) paramsMap.get("bodyTypeName");//选择框车身形式名称以|隔开
		bodyTypeName = bodyTypeName.substring(0, bodyTypeName.length() - 1);		
		
		if(!AppFrameworkUtil.isEmpty(dateGroup)) {	
			String[] dates = dateGroup.split("\\|");
			String objIdKey = "gradeId";
			if("1".equals(paramsMap.get("objectType"))) {
				objIdKey = "manfId";
				StringBuilder ids = new StringBuilder();
				for(int k = 0; k < objId.split(",").length; k++) {
					String id = objId.split(",")[k];
					ids.append("'").append(id).append("'").append(",");
				}
				objId = ids.substring(0, ids.length() - 1).toString();
			} else if("2".equals(paramsMap.get("objectType"))) {
				objIdKey = "brandId";
			} else if("3".equals(paramsMap.get("objectType"))) {
				objIdKey = "subModelId";
			} else if("4".equals(paramsMap.get("objectType"))) {
				objIdKey = "origId";
			}
			
			CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
			CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
			CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
			CellStyle percentageStyle = ExportExcelUtil.getExcelFillPercentageStyle(wb);//格式化百分号样式
			
			for(int i = 0; i < dates.length; i++) {
				String beginDate = dates[i].split(",")[0]; //开始时间
				String endDate = dates[i].split(",")[1];   //结束时间
				String legend = beginDate.substring(0, 4); //图例
				paramsMap.put("beginDate", beginDate.replace("-", ""));
				paramsMap.put("endDate", endDate.replace("-", ""));
				paramsMap.put(objIdKey, objId);
				if(!"0".equals(bodyTypeId)) {
					paramsMap.put("bodyTypeId", bodyTypeId);
				}
				
				List<VersionCityPriceIndexInfo> list = null;
				//成交价维度当时间段大于2008年时采用新算法
				if(paramsMap.get("priceType").equals("1") && Integer.parseInt(paramsMap.get("beginDate").toString().substring(0,4)) > 2008 && Integer.parseInt(paramsMap.get("endDate").toString().substring(0,4)) > 2008){
					list = cityPriceIndexDao.exportCityPriceIndexOriginalNewData(paramsMap);
				} else{
					list = cityPriceIndexDao.exportCityPriceIndexOriginalData(paramsMap);
				}
				
				if(null != list && list.size() > 0) {
					Sheet s = wb.createSheet(legend + "原始数据" + "(" + (i + 1) + ")");
					//标题
					List<String> titles = getExportTitle(paramsMap);
					
					int rowIndex = 0;//行号锁引
					Row row = ExportExcelUtil.createRow(s, rowIndex, 600);
					Cell cell = null;
					
					//写标题
					for(int j = 0; j < titles.size(); j++) {
						cell = row.createCell(j);
						ExportExcelUtil.setCellValueAndStyle(cell, titles.get(j), titleStyle);
						s.setColumnWidth(j, 4000);
					}
					//写数据
					for(int k = 0; k < list.size(); k++) {
						row = ExportExcelUtil.createRow(s, ++rowIndex, 400);
						VersionCityPriceIndexInfo info = list.get(k);
						try {
							setCellValue(row, info, paramsMap, textStyle, thousandthStyle, percentageStyle);
						} catch (Exception e) {
							
						}
					}
				}
			}
		}
	}
	
	/**
	 * 填充单元值
	 * 
	 * @param row
	 * @param info
	 * @param textStyle  内容文本样式
	 * @param thousandthStyle  格式化千分位样式
	 * @param percentageStyle  格式化百分号样式
	 */
	public void setCellValue(Row row, VersionCityPriceIndexInfo info, Map<String, Object> paramsMap, CellStyle textStyle, CellStyle thousandthStyle,
			CellStyle percentageStyle)
	{
		String languageType = (String) paramsMap.get("languageType");
		String citySplit = (String) paramsMap.get("citySplit");
		int cellIndex = 0;
		Cell cell = row.createCell(cellIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, info.getYearMonth() + "~", textStyle);
		
		cell = row.createCell(++cellIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, info.getVersionCode() + "~", textStyle);
		
		cell = row.createCell(++cellIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, info.getGradeName(), textStyle);
		
		cell = row.createCell(++cellIndex);
		if("EN".equals(languageType)) {
			ExportExcelUtil.setCellValueAndStyle(cell, info.getManfNameEn(), textStyle);
		} else {
			ExportExcelUtil.setCellValueAndStyle(cell, info.getManfName(), textStyle);
		}
		
		cell = row.createCell(++cellIndex);
		if("EN".equals(languageType)) {
			ExportExcelUtil.setCellValueAndStyle(cell, info.getSubModelNameEn(), textStyle);
		} else {
			ExportExcelUtil.setCellValueAndStyle(cell, info.getSubModelName(), textStyle);
		}
		
		cell = row.createCell(++cellIndex);
		if("EN".equals(languageType)) {
			ExportExcelUtil.setCellValueAndStyle(cell, info.getTypeIdEn(), textStyle);
		} else {
			ExportExcelUtil.setCellValueAndStyle(cell, info.getTypeId(), textStyle);
		}
		
		cell = row.createCell(++cellIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, info.getBodyType(), textStyle);
		
		cell = row.createCell(++cellIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, info.getVersionLaunchDate(), textStyle);
		
		cell = row.createCell(++cellIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, info.getModelYear(), textStyle);
		
		if(citySplit.equals("true")){
			cell = row.createCell(++cellIndex);
			if("EN".equals(languageType)){
				ExportExcelUtil.setCellValueAndStyle(cell, info.getCityNameEn(), textStyle);
			} else{
				ExportExcelUtil.setCellValueAndStyle(cell, info.getCityName(), textStyle);
			}
		}
		
		cell = row.createCell(++cellIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, info.getPrice(), thousandthStyle);
		
		//成交价维度当时间段大于2008年时采用新算法增加上月TP字段
		if(paramsMap.get("priceType").equals("1") && Integer.parseInt(paramsMap.get("beginDate").toString().substring(0,4)) > 2008 && Integer.parseInt(paramsMap.get("endDate").toString().substring(0,4)) > 2008){
			cell = row.createCell(++cellIndex);
			ExportExcelUtil.setCellValueAndStyle(cell, info.getLastMonthPrice(), thousandthStyle);
		}
		
		if("0".equals(paramsMap.get("priceType")) && "1".equals(paramsMap.get("equipageType"))) {
			cell = row.createCell(++cellIndex);
			if(AppFrameworkUtil.isEmpty(info.getCvDifference())) {
				ExportExcelUtil.setCellValueAndStyle(cell, "", textStyle);
			} else {
				ExportExcelUtil.setCellValueAndStyle(cell, info.getCvDifference(), thousandthStyle);
			}
		}
		
		cell = row.createCell(++cellIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, info.getDatumPrice(), thousandthStyle);

		cell = row.createCell(++cellIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, info.getSales(), thousandthStyle);
		
		cell = row.createCell(++cellIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, info.getTotalSales(), thousandthStyle);
		
		cell = row.createCell(++cellIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, info.getPriceIndex() + "~", textStyle);
	}
	
	/**
	 * 获取导出标题
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<String> getExportTitle(Map<String, Object> paramsMap)
	{
		List<String> tList = new ArrayList<String>();
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		String citySplit = (String) paramsMap.get("citySplit");//城市是否拆分，true为拆分，false为不拆分
		if("EN".equals(languageType)) {
			tList.add("Date");
			tList.add("Code");
			tList.add("Segment");
			tList.add("Manf");
			tList.add("Model");
			tList.add("Trim");
			tList.add("Bodytype");
			tList.add("Launch Date");
			tList.add("Model Year");
			if(citySplit.equals("true")){
				tList.add("City");
			}
			if("0".equals(paramsMap.get("priceType"))) {
				//指导价标题 
				tList.add("MSRP");
				if("1".equals(paramsMap.get("equipageType"))) {
					tList.add("CV");
				}
				tList.add("Datum MSRP");
				tList.add("Sales");
				tList.add("Total Sales");
				tList.add("MSRP Index");
			} else {
				//成交价标题
				tList.add("TP");
				//成交价维度当时间段大于2008年时采用新算法增加上月TP字段
				if(paramsMap.get("priceType").equals("1") && Integer.parseInt(paramsMap.get("beginDate").toString().substring(0,4)) > 2008 && Integer.parseInt(paramsMap.get("endDate").toString().substring(0,4)) > 2008){
					tList.add("LastMonth TP");
				}
				tList.add("Datum TP");
				tList.add("Sales");
				tList.add("Total Sales");
				tList.add("TP Index");
			}
		} else {
			tList.add("年月");
			tList.add("车型编码");
			tList.add("级别");
			tList.add("生产厂");
			tList.add("车型");
			tList.add("型号标识");
			tList.add("车身形式");
			tList.add("上市日期");
			tList.add("年式");
			if(citySplit.equals("true")){
				tList.add("城市");
			}
			if("0".equals(paramsMap.get("priceType"))) {
				//指导价标题 
				tList.add("MSRP");
				if("1".equals(paramsMap.get("equipageType"))) {
					tList.add("CV差");
				}
				tList.add("基期MSRP");
				tList.add("销量");
				tList.add("总销量");
				tList.add("MSRP指数");
			} else {
				//成交价标题
				tList.add("TP");
				//成交价维度当时间段大于2008年时采用新算法增加上月TP字段
				if(paramsMap.get("priceType").equals("1") && Integer.parseInt(paramsMap.get("beginDate").toString().substring(0,4)) > 2008 && Integer.parseInt(paramsMap.get("endDate").toString().substring(0,4)) > 2008){
					tList.add("上月TP");
				}
				tList.add("基期TP");
				tList.add("销量");
				tList.add("总销量");
				tList.add("TP指数");
			}
		}
		return tList;
	}
	
	/**
	 * 导出多对象原始数据
	 * 
	 * @param wb
	 * @param paramsMap
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public void exportMulitObj(Workbook wb, Map<String, Object> paramsMap, HttpServletRequest request)
	{
		Map<String,String> eNameMap = (Map<String, String>) request.getSession().getAttribute(Constant.getCityPriceIndexExportExcelDataEnKey);//获取英文字段
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		String totalLegend = ""; 
		String dateGroup = (String) paramsMap.get("dateGroup");//时间组以|隔开
		dateGroup = dateGroup.substring(0, dateGroup.length() - 1);		
		String beginDate = dateGroup.split(",")[0]; //开始时间
		String endDate = dateGroup.split(",")[1];   //结束时间
		paramsMap.put("beginDate", beginDate.replace("-", ""));
		paramsMap.put("endDate", endDate.replace("-", ""));
		
		String objId = (String) paramsMap.get("objId");//选择框对象ID以|隔开
		objId = objId.substring(0, objId.length() - 1);
		
		String objectType = (String) paramsMap.get("objectType");//选择框对象类型ID以|隔开
		objectType = objectType.substring(0, objectType.length() - 1);
		
		String objName = (String) paramsMap.get("objName");//选择框对象名称以|隔开
		objName = objName.substring(0, objName.length() - 1);		
		
		String bodyTypeId = (String) paramsMap.get("bodyTypeId");//选择框车身形式ID以|隔开
		bodyTypeId = bodyTypeId.substring(0, bodyTypeId.length() - 1);	
		if("0".equals(bodyTypeId)) {
			paramsMap.remove("bodyTypeId");
		}
		
		String bodyTypeName = (String) paramsMap.get("bodyTypeName");//选择框车身形式名称以|隔开
		bodyTypeName = bodyTypeName.substring(0, bodyTypeName.length() - 1);
		
		if(!AppFrameworkUtil.isEmpty(objId)) {
			String[] objIds = objId.split("\\|");
			String[] objNames = objName.split("\\|");
			String[] bodyTypeIds = bodyTypeId.split("\\|");
			String[] bodyTypeNames = bodyTypeName.split("\\|");
			String[] objectTypes = objectType.split("\\|");
			
			CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
			CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
			CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
			CellStyle percentageStyle = ExportExcelUtil.getExcelFillPercentageStyle(wb);//格式化百分号样式
			for(int i = 0; i < objIds.length; i++) {
				String objIdKey = "gradeId";
				if("1".equals(objectTypes[i])) {
					objIdKey = "manfId";
					StringBuilder ids = new StringBuilder();
					for(int k = 0; k < objIds[i].split(",").length; k++) {
						String id = objIds[i].split(",")[k];
						ids.append("'").append(id).append("'").append(",");
					}
					objIds[i] = ids.substring(0, ids.length() - 1).toString();
				} else if("2".equals(objectTypes[i])) {
					objIdKey = "brandId";
				} else if("3".equals(objectTypes[i])) {
					objIdKey = "subModelId";
				} else if("4".equals(objectTypes[i])) {
					objIdKey = "origId";
				}
				paramsMap.put("objectType", objectTypes[i]);
				paramsMap.put(objIdKey, objIds[i]);
				paramsMap.put("bodyTypeId", bodyTypeIds[i]);
				if("0".equals(bodyTypeIds[i])) {
					paramsMap.remove("bodyTypeId");
				}
				String legend = "";
				if("EN".equals(languageType)) {
					String[] name = objNames[i].split(",");
					for(int j = 0;j < name.length; j++ ) {
						if("整体市场".equals(name[j])) {
							legend = "Hold market";
							break;
						}
						if(j != name.length - 1) {
							legend += eNameMap.get(name[j]) + ",";
						} else {
							legend += eNameMap.get(name[j]);
						}
					}
					if(!"全部".equals(bodyTypeNames[i])) {
						legend = legend +"&"+ bodyTypeNames[i];
					}
				} else {
					legend = objNames[i] + "&" + bodyTypeNames[i];
					if("全部".equals(bodyTypeNames[i])) {
						legend = objNames[i];
					}
				}
				if(legend.contains("/")) {
					legend = legend.replace('/', '|');
				}
				
				if(totalLegend.indexOf(legend) != -1) {
					legend = legend + i;
				}
				totalLegend += legend;
				
				List<VersionCityPriceIndexInfo> list = null;
				//成交价维度当时间段大于2008年时采用新算法
				if(paramsMap.get("priceType").equals("1") && Integer.parseInt(paramsMap.get("beginDate").toString().substring(0,4)) > 2008 && Integer.parseInt(paramsMap.get("endDate").toString().substring(0,4)) > 2008){
					list = cityPriceIndexDao.exportCityPriceIndexOriginalNewData(paramsMap);
				} else{
					list = cityPriceIndexDao.exportCityPriceIndexOriginalData(paramsMap);
				}
				if(null != list && list.size() > 0) {
					Sheet s = null;
					String beginDate1 = (String) paramsMap.get("beginDate");
					String endDate1 = (String) paramsMap.get("endDate");
					/*if("0".equals(objectTypes[i])){*/
					s = wb.createSheet(beginDate1.substring(0, 4) + "年" + beginDate1.substring(4, 6) + "月" + "~" + 
					       endDate1.substring(0, 4) + "年" + endDate1.substring(4, 6) + "月" + "原始数据" + "(" + (i + 1) +")");
					/*} else {
						 s = wb.createSheet(legend);
					}*/
					
					//标题
					List<String> titles = getExportTitle(paramsMap);
					int rowIndex = 0;//行号锁引
					Row row = ExportExcelUtil.createRow(s, rowIndex, 600);
					Cell cell = null;
					
					//写标题
					for(int j = 0; j < titles.size(); j++) {
						cell = row.createCell(j);
						ExportExcelUtil.setCellValueAndStyle(cell, titles.get(j), titleStyle);
						s.setColumnWidth(j, 4000);
					}
					//写数据
					for(int k = 0; k < list.size(); k++) {
						row = ExportExcelUtil.createRow(s, ++rowIndex, 400);
						VersionCityPriceIndexInfo info = list.get(k);
						try {
							setCellValue(row, info, paramsMap, textStyle, thousandthStyle, percentageStyle);
						} catch (Exception e) {
							
						}
					}
				}
			}
		}
	}

	/**
	 * 指导价成交价改变时间
	 */
	public String tpDate(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		String json = "";
		List<InitialDate> list = (List<InitialDate>) cityPriceIndexDao.tpDate(paramsMap);
		if(null != list && list.size() > 0) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("beginDate", list.get(0).getBeginDate());
			dataMap.put("endDate", list.get(0).getEndDate());
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
		}
		return json;
	}
	
	/**
	 * 无边框文本样式
	 */
	public static CellStyle getExcelFillTextStyle(Workbook wb)
	{
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();   
		font.setColor(IndexedColors.BLACK.index);//设置字体颜色
		
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//设置单元格文本居中
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中  
		
		//设置背景颜色
		HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		customPalette.setColorAtIndex(HSSFColor.GREY_25_PERCENT.index, (byte) 214, (byte) 214, (byte) 214); 
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		
		
		//设置边框颜色
		/*style.setBorderLeft(CellStyle.SOLID_FOREGROUND);   
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());//设置左边框颜色
		style.setBorderBottom(CellStyle.SOLID_FOREGROUND);   
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());//设置下边框颜色
		style.setBorderRight(CellStyle.SOLID_FOREGROUND);   
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());//设置右边框颜色
*/
		style.setFont(font);//设置字体
		style.setWrapText(true);//设置文本自动换行
		return style;
	}
}
