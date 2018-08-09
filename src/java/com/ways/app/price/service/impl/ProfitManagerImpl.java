package com.ways.app.price.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.ways.app.price.dao.IProfitDao;
import com.ways.app.price.model.EchartLineDataEntity;
import com.ways.app.price.model.EchartMarkPointEntity;
import com.ways.app.price.model.Manf;
import com.ways.app.price.model.SubModel;
import com.ways.app.price.model.ObjectInfoEntity;
import com.ways.app.price.model.VersionInfoEntity;
import com.ways.app.price.service.IProfitManager;

/**
 * 车型利润分析Service层接口实现类
 * @author yinlue
 *
 */
@Service("profitManager")
public class ProfitManagerImpl implements IProfitManager {

	@Autowired
	private IProfitDao profitDao;

	/**
	 * 初始时间
	 */
	@Override
	public String initDate(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";//返回默认开始时间和结束时间
		List<Map<String, String>> list = profitDao.initDate(paramsMap);
		if(null != list && list.size() > 0) {
			Map<String, String> resultMap = list.get(0);
			String endDate = resultMap.get("ENDDATE");
			String beginDate = resultMap.get("BEGINDATE");
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			//默认开始时间，时间规则是相差12个点
			String defaultBeginDate = (Integer.parseInt(endDate.replace("-", "")) - 99) + "";
			//如果结束时间减一年小于开始时间，则默认为开始时间
			if(Integer.parseInt(defaultBeginDate) < Integer.parseInt(beginDate.replace("-", ""))) {
				request.setAttribute("defaultBeginDate", beginDate);
				json = resultMap.get("BEGINDATE");
			} else {
				if(Integer.parseInt(defaultBeginDate.substring(4)) > 12) {
					defaultBeginDate = (Integer.parseInt(defaultBeginDate.substring(0, 4)) + 1) + "-01";
				} else {
					defaultBeginDate = defaultBeginDate.substring(0, 4) + "-" + defaultBeginDate.substring(4);
				}
				request.setAttribute("defaultBeginDate", defaultBeginDate);
				json = defaultBeginDate;
			}
			json += "," +resultMap.get("BEGINDATE") + "," + endDate;
		}
		
		//返回是否含最新周数据
		String latestWeek = findLatestWeek(request, paramsMap);
		request.setAttribute("latestWeek", latestWeek);
		json += "," + latestWeek;
		return json;
	}

	/**
	 * 加载车型利润图形和表格
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String loadModelProfitChartAndTable(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		String objectType = paramsMap.get("objectType").toString();
		List list = null;
		if(paramsMap.get("latestWeek").equals(paramsMap.get("endDate"))) {
			paramsMap.put("latestWeek", "true");
		} else {
			paramsMap.put("latestWeek", "false");
		}
		//对象类型为型号
	    if("1".equals(objectType)) {
		   //分析维度为城市对比
		   if("2".equals(paramsMap.get("analysisType"))) {
		       list = profitDao.loadCityProfitChartAndTable(paramsMap);
		   //分析维度为对象对比
		   } else {
			   list = profitDao.loadVersionProfitChartAndTable(paramsMap);
		   }
		//对象类型为车型/生产商
	    } else {
		   //分析维度为城市对比
	       if("2".equals(paramsMap.get("analysisType"))) {
	    	   list = profitDao.loadObjectCityProfitChartAndTable(paramsMap);
	       //分析维度为对象对比
		   } else {
			   list = profitDao.loadModelProfitChartAndTable(paramsMap);
		   }
	    }
	    if(null != list && list.size() > 0) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			if("1".equals(objectType)) {
				if("1".equals(paramsMap.get("analysisType"))) {
					dataMap.put("chart", getChartDataToVersion(list, paramsMap));
				} else if("2".equals(paramsMap.get("analysisType"))) {
					dataMap.put("chart", getChartDataToCity(list, paramsMap));
				}
			} else {
				if("1".equals(paramsMap.get("analysisType"))) {
					dataMap.put("chart", getChartDataToObject(list, paramsMap));
				} else if("2".equals(paramsMap.get("analysisType"))) {
					dataMap.put("chart", getChartDataToObjectCity(list, paramsMap));
				}
			}			
			dataMap.put("grid", list);
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			request.getSession().setAttribute(Constant.getProfitOfExcelDataKey, json);
		}
		return json;
	}
	
	/**
	 * 获取型号利润图形数据
	 * 
	 * @param list
	 * @param paramsMap
	 * @return
	 */
	public Map<String,Object> getChartDataToVersion(List<VersionInfoEntity> list, Map<String, Object> paramsMap)
	{
		Map<String,Object> dataMap = new HashMap<String,Object>();
		if(null != list && list.size() > 0) {
			//线图数据
			List<EchartLineDataEntity> lineList = new ArrayList<EchartLineDataEntity>();
			//X轴
			String xTitle = "";
			//型号名称
			String versionName = "";
			//型号ID，唯一标识，有可能名称重名
			String versionId = "";
			//线图数据
			String lineData = "";
			//线轴点形状，默认为实心，如果是本品则为空心
			String symbol = "";
			//标识点大小
			int symbolSize = 3;
			//是否为本品车型
			String isBase = "";
			//是否是下一个新型号标识
			boolean isNewVersion = false;
			//是否添加新型号
			boolean isAddVersion = false;
			//数据标记型号换汽泡集合
			List<EchartMarkPointEntity> markPointList = new ArrayList<EchartMarkPointEntity>();
			for(int i = 0; i < list.size(); i++) {
				VersionInfoEntity vInfo = list.get(i);
				String vname = vInfo.getVersionChartName();
				String yearMonth = vInfo.getYearMonth();
				String week = vInfo.getWeek();
				
				//如果有最新周数据,横坐标标签处理
				if(!"M".equals(week)) {
					yearMonth = yearMonth+week;
				}
				if(!xTitle.contains(yearMonth)) {
					xTitle += yearMonth + ",";
				}
				if(!versionId.contains(vInfo.getVersionId())) {
					versionId += vInfo.getVersionId() + ",";
					isNewVersion = true;
					if(i != 0) {
						isAddVersion = true;
					}
				}
				
				//添加对象
				if(isAddVersion) {
					lineList.add(new EchartLineDataEntity("line",versionName,symbol,lineData.split(","),new ArrayList<EchartMarkPointEntity>(markPointList),isBase,symbolSize));
					isAddVersion = false;
					markPointList.clear();
				}
				
				if(isNewVersion) {
					lineData = "";
					String yaxis = "";//汽泡Y轴位置
					//折扣
					if("2".equals(paramsMap.get("analysisContentType"))) { 
						lineData += countDiscount(vInfo.getMsrp(),vInfo.getTp());
						yaxis = countDiscount(vInfo.getMsrp(),vInfo.getTp());
					} else if("3".equals(paramsMap.get("analysisContentType"))) {
						lineData += vInfo.getTp();//成交价
						yaxis = vInfo.getTp();
					} else {
						lineData += vInfo.getModelProfit();//默认为利润
						yaxis = vInfo.getModelProfit();
					}
					
					isNewVersion = false;
					//如果标记名称不为空,则添加标记对象
					if(!AppFrameworkUtil.isEmpty(vInfo.getChangName()) && !AppFrameworkUtil.isEmpty(yaxis) && !"-".equals(yaxis)) {
						markPointList.add(new EchartMarkPointEntity(vInfo.getChangName(), "", vInfo.getYearMonth(), 
								AppFrameworkUtil.getNum(Double.parseDouble(yaxis), 0), "5"));
					}
				} else {
					String yaxis = "";//汽泡Y轴位置
					//折扣
					if("2".equals(paramsMap.get("analysisContentType"))) {
						lineData += "," + countDiscount(vInfo.getMsrp(),vInfo.getTp());
						yaxis = countDiscount(vInfo.getMsrp(),vInfo.getTp());
					} else if("3".equals(paramsMap.get("analysisContentType"))) {
						lineData += "," + vInfo.getTp();//成交价
						yaxis = vInfo.getTp();
					} else {
						lineData += "," + vInfo.getModelProfit();//默认为利润
						yaxis = vInfo.getModelProfit();
					}
					
					//如果标记名称不为空,则添加标记对象
					if(!AppFrameworkUtil.isEmpty(vInfo.getChangName()) && !AppFrameworkUtil.isEmpty(yaxis) && !"-".equals(yaxis)) {
						markPointList.add(new EchartMarkPointEntity(vInfo.getChangName(), "", vInfo.getYearMonth(),
	 							AppFrameworkUtil.getNum(Double.parseDouble(yaxis), 0), "5"));
					}
				}
				versionName = vname;
				//线轴点形状，默认为实心，如果是本品则为空心
				symbol = "circle";
				symbolSize = 3;
				if("1".equals(vInfo.getIsBase())) {
					 symbol = "emptyCircle";
					 symbolSize = 5;
				}
				isBase = vInfo.getIsBase();
				
				//添加最后一个对象
				if(i == list.size() - 1) {
					lineList.add(new EchartLineDataEntity("line", versionName, symbol, lineData.split(","), markPointList, isBase, symbolSize));
					isAddVersion = false;
				}
			}
			
			xTitle = xTitle.substring(0, xTitle.length() - 1);
			dataMap.put("xTitle", xTitle.split(","));
			dataMap.put("series", orderByLineList(lineList));
			dataMap.put("titles", getLineLegend(lineList));
			dataMap.put("boundarys", EchartsUtil.setLineScaleDivision(lineList));
		}
		return dataMap;
	}
	
	/**
	* 函数功能说明 获取对象利润图形数据
	* 
	* ruanrf  Jun 26, 2015
	* 修改者名字 修改日期
	* 修改内容
	* @param list
	* @param paramsMap
	* @return    
	* Map<String,Object>   
	*/
	public Map<String, Object> getChartDataToObject(List<ObjectInfoEntity> list, Map<String, Object> paramsMap)
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String objectType = paramsMap.get("objectType").toString();
		if(null != list && list.size() > 0) {
			//线图数据
			List<EchartLineDataEntity> lineList = new ArrayList<EchartLineDataEntity>();
			//X轴
			String xTitle = "";
			//车型名称
			String objectName = "";
			//车型ID，唯一标识，有可能名称重名
			String objectId = "";
			//线图数据
			String lineData = "";
			//线轴点形状，默认为实心，如果是本品则为空心
			String symbol = "";
			//标识点大小
			int symbolSize = 3;
			//是否为本品车型
			String isBase = "";
			//是否是下一个新型号标识
			boolean isNewObject = false;
			//是否添加新型号
			boolean isAddObject = false;
			//数据标记型号换汽泡集合
			List<EchartMarkPointEntity> markPointList = new ArrayList<EchartMarkPointEntity>();
			for(int i = 0; i < list.size(); i++) {
				String oName = "";
				ObjectInfoEntity mInfo = list.get(i);
				String yearMonth = mInfo.getYearMonth();
				String week = mInfo.getWeek();
				if("2".equals(objectType)) {
					oName = mInfo.getSubmodelNameEn();
				} else {
					oName = mInfo.getManfNameEn();
				}
				
				//如果有最新周数据,横坐标标签处理
				if(!"M".equals(week)) {
					yearMonth = yearMonth+week;
				}
				if(!xTitle.contains(yearMonth)) {
					xTitle += yearMonth + ",";
				}
				if("2".equals(objectType)) {
					if(!objectId.contains(mInfo.getSubModelId())) {
						objectId += mInfo.getSubModelId() + ",";
						isNewObject = true;
						if(i != 0) {
							isAddObject = true;
						}
					}
				} else {
					if(!objectId.contains(mInfo.getManfId())) {
						objectId += mInfo.getManfId() + ",";
						isNewObject = true;
						if(i != 0) isAddObject = true;
					}
				}
				
				//添加对象
				if(isAddObject) {
					lineList.add(new EchartLineDataEntity("line", objectName, symbol, lineData.split(","), new ArrayList<EchartMarkPointEntity>(markPointList),
							isBase, symbolSize));
					isAddObject = false;
					markPointList.clear();
				}
				
				//String yaxis = "";//汽泡Y轴位置
				if(isNewObject) {
					lineData = "";
					//折扣
					if("2".equals(paramsMap.get("analysisContentType"))) {
						lineData += countDiscount(mInfo.getMsrp(), mInfo.getTp());
						//yaxis = countDiscount(mInfo.getMsrp(), mInfo.getTp());
					}
					//成交价
					else if("3".equals(paramsMap.get("analysisContentType"))) {
						lineData += mInfo.getTp();
						//yaxis = mInfo.getTp();
					//默认为利润
					} else {
						lineData += mInfo.getModelProfit();
						//yaxis = mInfo.getModelProfit();
					}
					isNewObject = false;
				} else {
					//折扣
					if("2".equals(paramsMap.get("analysisContentType"))) {
						lineData += "," + countDiscount(mInfo.getMsrp(),mInfo.getTp());
						//yaxis = countDiscount(mInfo.getMsrp(),mInfo.getTp());
					//成交价
					} else if("3".equals(paramsMap.get("analysisContentType"))) {
						lineData += "," + mInfo.getTp();
						//yaxis = mInfo.getTp();
						//默认为利润
					} else {
						lineData += "," + mInfo.getModelProfit();
						//yaxis = mInfo.getModelProfit();
					}
				}
				
				objectName = oName;
				//线轴点形状，默认为实心，如果是本品则为空心
				symbol = "circle";
				symbolSize = 3;
				if("1".equals(mInfo.getIsBase())) {
					 symbol = "emptyCircle";
					 symbolSize = 5;
				}
				isBase = mInfo.getIsBase();
				
				//添加最后一个对象
				if(i == list.size() - 1) {
					lineList.add(new EchartLineDataEntity("line", objectName, symbol, lineData.split(","), markPointList, isBase, symbolSize));
					isAddObject = false;
				}
			}
			
			xTitle = xTitle.substring(0, xTitle.length() - 1);
			dataMap.put("xTitle", xTitle.split(","));
			dataMap.put("series", orderByLineList(lineList));
			dataMap.put("titles", getLineLegend(lineList));
			dataMap.put("boundarys", EchartsUtil.setLineScaleDivision(lineList));
		}
		return dataMap;
	}
	
	/**
	 * 获取图例
	 * 
	 * @param list
	 * @return
	 */
	public String[] getLineLegend(List<EchartLineDataEntity> list)
	{
		String[] legned = new String[list.size()];
		for(int i = 0; i < list.size(); i++)
		{
			legned[i] = list.get(i).getName();
		}
		return legned;
	}
	
	/**
	 * 排序线图数据集
	 * 
	 * @param list
	 * @return
	 */
	public List<EchartLineDataEntity> orderByLineList(List<EchartLineDataEntity> list)
	{
		try {
			//按型号ID，时间年月，排序
			Collections.sort(list, new Comparator<EchartLineDataEntity>() {    
		           public int compare(EchartLineDataEntity v1, EchartLineDataEntity v2) { 
		       	       if(Integer.parseInt(v1.getIsBase()) < Integer.parseInt(v2.getIsBase())) {
		       	    	   return 1;
		       	       } else if(Integer.parseInt(v1.getIsBase()) > Integer.parseInt(v2.getIsBase())) {
		       	    	   return -1;
		       	       } else {
		       	    	   return 0;	
		       	       }
			       }
		     });
		} catch (Exception e) {

		}
		return list;
	}
	
	/**
	 * 获取城市利润图形数据
	 * 
	 * @param list
	 * @param paramsMap
	 * @return
	 */
	public Map<String,Object> getChartDataToCity(List<VersionInfoEntity> list, Map<String, Object> paramsMap)
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();
		if(null != list && list.size() > 0) {
			//线图数据
			List<EchartLineDataEntity> lineList = new ArrayList<EchartLineDataEntity>();
			//X轴
			String xTitle = "";
			//图例
			String titles = "";
			//型号名称
			String cityName = "";
			//型号ID，唯一标识，有可能名称重名
			String cityId = "";
			//线图数据
			String lineData = "";
			//是否是下一个新型号标识
			boolean isNewVersion = false;
			//是否添加新型号
			boolean isAddVersion = false;
			for(int i = 0; i < list.size(); i++) {
				VersionInfoEntity vInfo = list.get(i);
				String vname = vInfo.getCityNameEn();
				String yearMonth = vInfo.getYearMonth();
				String week = vInfo.getWeek();
				
				//如果有最新周数据,横坐标标签处理
				if(!"M".equals(week)) {
					yearMonth = yearMonth+week;
				}
				
				if(!xTitle.contains(yearMonth)) {
					xTitle += yearMonth + ",";
				}
				if(!cityId.contains(vInfo.getCityId())) {
					cityId += vInfo.getCityId()+ ",";
					titles += vname + ",";
					isNewVersion = true;
					if(i != 0) {
						isAddVersion = true;
					}
				}
				//添加对象
				if(isAddVersion) {
					lineList.add(new EchartLineDataEntity("line", cityName, "circle", lineData.split(",")));
					isAddVersion = false;
				}
				if(isNewVersion) {
					lineData = "";
					if("2".equals(paramsMap.get("analysisContentType"))) {
						lineData += countDiscount(vInfo.getMsrp(),vInfo.getTp()); //折扣
					} else if("3".equals(paramsMap.get("analysisContentType"))) {
						lineData += vInfo.getTp();//成交价
					} else {
						lineData += vInfo.getModelProfit();//默认为利润
					}
					isNewVersion = false;
				} else {
					if("2".equals(paramsMap.get("analysisContentType"))) {
						lineData += "," + countDiscount(vInfo.getMsrp(),vInfo.getTp()); //折扣
					} else if("3".equals(paramsMap.get("analysisContentType"))) {
						lineData += "," + vInfo.getTp();//成交价
					} else {
						lineData += "," + vInfo.getModelProfit();//默认为利润
					}
				}
				cityName = vname;
				//添加最后一个对象
				if(i == list.size() - 1) {
					lineList.add(new EchartLineDataEntity("line", cityName, "circle", lineData.split(",")));
					isAddVersion = false;
				}
			}
			xTitle = xTitle.substring(0, xTitle.length() - 1);
			titles = titles.substring(0, titles.length() - 1);
			dataMap.put("xTitle", xTitle.split(","));
			dataMap.put("titles", titles.split(","));
			dataMap.put("series", lineList);
			dataMap.put("boundarys", EchartsUtil.setLineScaleDivision(lineList));
		}
		return dataMap;
	}
	
	/**
	 * 获取对象城市利润图形数据
	 * 
	 * @param list
	 * @param paramsMap
	 * @return
	 */
	public Map<String,Object> getChartDataToObjectCity(List<ObjectInfoEntity> list, Map<String, Object> paramsMap)
	{
		Map<String,Object> dataMap = new HashMap<String,Object>();
		if(null != list && list.size() > 0) {
			//线图数据
			List<EchartLineDataEntity> lineList = new ArrayList<EchartLineDataEntity>();
			//X轴
			String xTitle = "";
			//图例
			String titles = "";
			//型号名称
			String cityName = "";
			//型号ID，唯一标识，有可能名称重名
			String cityId = "";
			//线图数据
			String lineData = "";
			//是否是下一个新型号标识
			boolean isNewVersion = false;
			//是否添加新型号
			boolean isAddVersion = false;
			for(int i = 0; i < list.size(); i++) {
				ObjectInfoEntity vInfo = list.get(i);
				String vname = vInfo.getCityNameEn();
				String yearMonth = vInfo.getYearMonth();
				String week = vInfo.getWeek();
				//如果有最新周数据,横坐标标签处理
				if(!"M".equals(week)) {
					yearMonth = yearMonth+week;
				}
				if(!xTitle.contains(yearMonth)) {
					xTitle += yearMonth + ",";
				}
				if(!cityId.contains(vInfo.getCityId())) {
					cityId += vInfo.getCityId()+ ",";
					titles += vname + ",";
					isNewVersion = true;
					if(i != 0) {
						isAddVersion = true;
					}
				}
				
				//添加对象
				if(isAddVersion) {
					lineList.add(new EchartLineDataEntity("line", cityName, "circle", lineData.split(",")));
					isAddVersion = false;
				}
				
				if(isNewVersion) {
					lineData = "";
					if("2".equals(paramsMap.get("analysisContentType"))) {
						lineData += countDiscount(vInfo.getMsrp(),vInfo.getTp()); //折扣
					} else if("3".equals(paramsMap.get("analysisContentType"))) {
						lineData += vInfo.getTp();//成交价
					} else {
						lineData += vInfo.getModelProfit();//默认为利润
					}
					isNewVersion = false;
				} else {
					if("2".equals(paramsMap.get("analysisContentType"))) {
						lineData += "," + countDiscount(vInfo.getMsrp(),vInfo.getTp()); //折扣
					} else if("3".equals(paramsMap.get("analysisContentType"))) {
						lineData += "," + vInfo.getTp();//成交价
					} else {
						lineData += "," + vInfo.getModelProfit();//默认为利润
					}
				}
				cityName = vname;
				//添加最后一个对象
				if(i == list.size() - 1) {
					lineList.add(new EchartLineDataEntity("line", cityName, "circle", lineData.split(",")));
					isAddVersion = false;
				}
			}
			
			xTitle = xTitle.substring(0, xTitle.length() - 1);
			titles = titles.substring(0, titles.length() - 1);
			dataMap.put("xTitle", xTitle.split(","));
			dataMap.put("titles", titles.split(","));
			dataMap.put("series", lineList);
			dataMap.put("boundarys", EchartsUtil.setLineScaleDivision(lineList));
		}
		return dataMap;
	}
	
	/**
	 * 获取线图分割段数分割值
	 * 
	 * @param paramsMap
	 * @return
	 */
	public int getChartSplitValue(Map<String, Object> paramsMap)
	{
		int splitValue = 1000;
		if("3".equals(paramsMap.get("analysisContentType"))) {
			splitValue = 10000;
		}
		return splitValue;
	}

	/**
	 * 校验弹出框有效数据
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public String checkPopBoxData(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		List<SubModel> list = profitDao.checkPopBoxData(paramsMap);
		if(null != list && list.size() > 0) json = AppFrameworkUtil.structureConfigParamsGroupJSONData(list);
		return json;
	}
	
	/**
	 * 校验生产商弹出框有效数据
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public String checkManfPopBoxData(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		List<Manf> list = profitDao.checkManfPopBoxData(paramsMap);
		if(null != list && list.size() > 0) {
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(list);
		}
		return json;
	}

	/**
	 * 导出
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	@Override
	public Workbook exportExcel(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		Workbook wb = null;
		String json = (String) request.getSession().getAttribute(Constant.getProfitOfExcelDataKey);
		if(!AppFrameworkUtil.isEmpty(json)) {
		    JSONObject obj = (JSONObject) JSONObject.parse(json);
			JSONObject chartObj = (JSONObject) obj.get("chart");
			JSONArray gridObj = (JSONArray) obj.getJSONArray("grid");
			try {
			    String path = request.getSession().getServletContext().getRealPath("/"); 
				wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/profitOfTemplate.xls")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			exportOriginalData(wb, wb.getSheet("原数据"), gridObj, paramsMap);
			exportChartData(wb, wb.getSheet("DATA"), chartObj, paramsMap);
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
	@SuppressWarnings({ "rawtypes" })
	public void exportOriginalData(Workbook wb, Sheet sheet, JSONArray gridObj, Map<String, Object> paramsMap)
	{
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		String analysisType = (String) paramsMap.get("analysisType");//分析类型,1:车型利润；2：城市利润
		
		String[] titles = null;//标题数组
		String yearMonth = null;
		if("EN".equals(languageType)) {
			titles = new String[]{"City", "Date", "Code", "Segment", "Brand", "Manufacture", "Model", "Engine Capacity", "Transmission", "Version Name", "Body Type",
					"Launch Date", "Model Year", "MSRP(RMB)", "TP(RMB)", "Discount(RMB)", "Gross Cost", "Fully-paid Car Purchase Promotion", "Gross Supports", "Std", "Aak", "Invoice Price", "Margin",
					"Customer Incentive", "Present", "Insurance", "Maintenance", "Person Reward", "Finance Loan", "Displaces Support", "Bonus", 
					"Gross Profit", "Version Sales"};
		} else {
			titles = new String[]{"城市", "日期", "型号编码", "级别", "品牌", "厂商", "车型", "排量", "排档方式", "型号全称", "车身形式", "上市日期", "年式", "厂商指导价",
					"成交价", "折扣", "经销商成本", "全款购车促销", "经销商支持", "提车支持", "零售支持", "开票价", "返利", "用户激励", "礼品", "保险", "保养", "人员奖励", "金融贷款", "置换支持",
                    "考核奖励", "经销商利润", "型号销量"};
		}
		//对象类型为车型或者厂商时导出需要重新查询原始数据
		if(!"1".equals(paramsMap.get("objectType"))) {
			List list = null;
			String json = "";
			Map<String, Object> dataMap = new HashMap<String, Object>();
			if(paramsMap.get("latestWeek").equals(paramsMap.get("endDate"))) {
		        paramsMap.put("latestWeek", "true");
			} else {
				paramsMap.put("latestWeek", "false");
			}
		    //对象类型为车型/生产商
			//分析维度为对象对比,或者城市对比
			if("2".equals(paramsMap.get("analysisType"))) {
			    list = profitDao.exportCityProfitData(paramsMap);
			} else {
			    list = profitDao.exportProfitData(paramsMap);
			}
			dataMap.put("grid", list);
			json =  AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			JSONObject jsonObj = JSONObject.parseObject(json);
			gridObj = jsonObj.getJSONArray("grid");
		}
		
		int rowIndex = 0;//行号锁引
		Row row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
		Cell cell = null;
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle titleSpecialStyle =  ExportExcelUtil.getExcelTitleBackgroundSpecialStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		CellStyle thousandthREDStyle =ExportExcelUtil.getExcelFormatThousandthREDStyle(wb);		
		
		//表格标题数组锁引，如果是城市对比需要添加城市列
		int index = 1;
		if("2".equals(analysisType)) {
			index = 0;
		}
		int length = titles.length;
		//写表格标题
		for(int i = index; i < length; i++) {
			int cellIndex = i;
			if(!"2".equals(analysisType)) {
				cellIndex = i - 1; //如果是车型利润，锁引需要向前称一位，因为有城市列
			}
			cell = row.createCell(cellIndex);
			String titleName = titles[i];
			if("提车支持".equals(titleName) || "零售支持".equals(titleName) || "礼品".equals(titleName) || "保险".equals(titleName) || "保养".equals(titleName) || 
				  "人员奖励".equals(titleName) || "金融贷款".equals(titleName) || "置换支持".equals(titleName) || "Std".equals(titleName) || "Aak".equals(titleName) || 
				  "Present".equals(titleName) || "Insurance".equals(titleName) || "Maintenance".equals(titleName) || "Person Reward".equals(titleName) || 
				  "Finance Loan".equals(titleName) || "Displaces Support".equals(titleName)) {
				ExportExcelUtil.setCellValueAndStyle(cell, titles[i], titleSpecialStyle);
			} else {
				ExportExcelUtil.setCellValueAndStyle(cell, titles[i], titleStyle);
			}
			sheet.setColumnWidth(i, 3000);
		}
		//写表格数据内容
		for(int j = 0; j < gridObj.size(); j++) {
			JSONObject obj = gridObj.getJSONObject(j);
			//如果无记录则不导出
			if(AppFrameworkUtil.isEmpty(obj.getString("versionCode"))) {
				continue;
			}
			//重置锁引，用于创建单元格列锁引位置
			index = 0;
			rowIndex++;
			row = ExportExcelUtil.createRow(sheet, rowIndex, 400);
			//添加城市列
			if("2".equals(analysisType)) {
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "cityName"), textStyle);
			}
			cell = row.createCell(index++);
			yearMonth = obj.getString("yearMonth");
			if(!"M".equals(obj.getString("week"))) {
				yearMonth = yearMonth + obj.getString("week");
			}
			ExportExcelUtil.setCellValueAndStyle(cell, yearMonth + "~", textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionCode") + "~", textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "gradeName"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "brandName"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "manfName"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "subModelName"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("discharge"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "gearMode"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "versionName"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj, languageType, "bodyType"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionLaunchDate"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("modelYear"), textStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("msrp"), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("tp"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, countDiscount(obj.getString("msrp"), obj.getString("tp")), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("grossCost"), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("fullyPaidPromotion"), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getZeroNum(obj.getString("grossSupport"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getZeroNum(obj.getString("std"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getZeroNum(obj.getString("aak"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getZeroNum(obj.getString("invoicePrice"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("rebatePrice"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getZeroNum(obj.getString("customerIncentive"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getZeroNum(obj.getString("present"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getZeroNum(obj.getString("insurance"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getZeroNum(obj.getString("maintenance"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getZeroNum(obj.getString("personReward"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getZeroNum(obj.getString("financeLoan"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getZeroNum(obj.getString("displacesSupport"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("rewardAssessment"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyles(cell, AppFrameworkUtil.getZeroNum(obj.getString("modelProfit"), 7), thousandthStyle, thousandthREDStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  AppFrameworkUtil.getNum(obj.getString("versionSale"), 7), thousandthStyle);
		}
		sheet.setDisplayGridlines(false);
	}
	
	/**
	 * 导出对象数据(已废弃)
	 * 
	 * @param wb
	 * @param sheet
	 * @param gridObj
	 * @param paramsMap
	 */
	public void exportObjectData(Workbook wb, Sheet sheet, JSONArray gridObj, Map<String, Object> paramsMap)
	{
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		String analysisType = (String) paramsMap.get("analysisType");//分析类型,1:车型利润；2：城市利润
		String objTitle = null;
		String objTitleEn = null;
		String yearMonth = null;
		String objectType = paramsMap.get("objectType").toString();
		if("2".equals(objectType)) {
			objTitle = "车型全称";
			objTitleEn = "ModelName";
		} else {
			objTitle = "生产商|品牌全称";
			objTitleEn = "Manf|Brand";
		}
		
		String[] titles = null;//标题数组
		if("EN".equals(languageType)) {
			titles = new String[]{"City","Date",objTitleEn,"MSRP(RMB)","TP（RMB）","Discount（RMB）","SellerCost","Invoice Price","Margin","Bonus","Tactical","Gross Profit"};
		} else {
			titles = new String[]{"城市","日期",objTitle,"指导价","成交价","折扣","经销商成本","开票价","激励额","考核奖励","促销","经销商利润"};
		}
		int rowIndex = 0;//行号锁引
		Row row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
		Cell cell = null;
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		
		//表格标题数组锁引，如果是城市利润需要添加城市列
		int index = 1;
		if("2".equals(analysisType)) {
			index = 0;
		}
		//写表格标题
		for(int i = index; i < titles.length; i++) {
			int cellIndex = i;
			if(!"2".equals(analysisType)) cellIndex = i - 1; //如果是车型利润，锁引需要向前称一位，因为有城市列
			cell = row.createCell(cellIndex);
			ExportExcelUtil.setCellValueAndStyle(cell, titles[i], titleStyle);
			sheet.setColumnWidth(i, 3000);
		}
		//写表格数据内容
		for(int j = 0; j < gridObj.size(); j++) {
			JSONObject obj = gridObj.getJSONObject(j);
			//如果无记录则不导出
			if(AppFrameworkUtil.isEmpty(obj.getString("submodelName")) && AppFrameworkUtil.isEmpty(obj.getString("manfName"))) {
				continue;
			}
			//重置锁引，用于创建单元格列锁引位置
			index = 0;
			rowIndex++;
			row = ExportExcelUtil.createRow(sheet, rowIndex, 400);
			//添加城市列
			if("2".equals(analysisType)) {
				cell = row.createCell(index++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType, "cityName"), textStyle);
			}
			cell = row.createCell(index++);
			yearMonth  = obj.getString("yearMonth");
			if(!"M".equals(obj.getString("week"))) {
				yearMonth = yearMonth + obj.getString("week");
			}
			ExportExcelUtil.setCellValueAndStyle(cell, yearMonth + "~", textStyle);
			
			cell = row.createCell(index++);
			if("EN".equals(languageType)) {	
				if("2".equals(objectType)) {
					ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("submodelNameEn") + "~", textStyle);
				} else {
					ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("manfNameEn") + "~", textStyle);
				}
			} else {
				if("2".equals(objectType)) {
					ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("submodelName") + "~", textStyle);
				} else {
					ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("manfName") + "~", textStyle);
				}
			}
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("msrp"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("tp"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, countDiscount(obj.getString("msrp"), obj.getString("tp")), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("sellerCost"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("invoicePrice"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("rebatePrice"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("rewardAssessment"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("promotionalAllowance"), 7), thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(obj.getString("modelProfit"), 7), thousandthStyle);
		}
		sheet.setDisplayGridlines(false);
	}
	
	/**
	 * 导出图形数据
	 * 
	 * @param wb
	 * @param sheet
	 * @param chartObj
	 * @param paramsMap
	 */
	public void exportChartData(Workbook wb, Sheet sheet, JSONObject chartObj, Map<String, Object> paramsMap)
	{
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		JSONArray titles = chartObj.getJSONArray("xTitle");
		JSONArray series =  chartObj.getJSONArray("series");
		String firstTitle = "利润";
		if("EN".equals(languageType)) {
			firstTitle = "Gross Profit";
		}
		if("2".equals(paramsMap.get("analysisContentType"))) {
			firstTitle = "折扣";
			if("EN".equals(languageType)) {
				firstTitle = "Discount(RMB)";
			}
		}
		if("3".equals(paramsMap.get("analysisContentType"))) {
			firstTitle = "成交价";
			if("EN".equals(languageType)) {
				firstTitle = "TP(RMB)";
			}
		}
		int rowIndex = 0;//行号锁引
		Row row = ExportExcelUtil.createRow(sheet, rowIndex, 600);
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		
		//写入线图刻度最大值与最小值
		Cell cell = row.createCell(130);
		ExportExcelUtil.setCellValueAndStyle(cell, paramsMap.get("ymax").toString(), textStyle);
		cell = row.createCell(131);
		ExportExcelUtil.setCellValueAndStyle(cell, paramsMap.get("ymin").toString(), textStyle);
		
		//写表格标题
		cell = row.createCell(0);
		ExportExcelUtil.setCellValueAndStyle(cell, firstTitle, titleStyle);
		for(int i = 0; i < titles.size(); i++) {
			cell = row.createCell(i+1);
			ExportExcelUtil.setCellValueAndStyle(cell, titles.getString(i), titleStyle);
			if(0 == i) {
				sheet.setColumnWidth(i, 6000);
			} else {
				sheet.setColumnWidth(i, 2500);
			}
		}
		
		//写数据
		for(int j = 0; j < series.size(); j++) {
			JSONObject obj = series.getJSONObject(j);
			JSONArray datas = obj.getJSONArray("data");
			rowIndex++;
			row = ExportExcelUtil.createRow(sheet, rowIndex, 400);
			cell = row.createCell(0);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("name"), textStyle);
			for(int n = 0; n < titles.size(); n++) {
				cell = row.createCell(n + 1);
				ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(datas.getString(n), 7), thousandthStyle);
			}
			//写入本品标识
			cell = row.createCell(130);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("isBase"), thousandthStyle);
		}
		sheet.setDisplayGridlines(false);
	}
	
	/**
	 * 计算折扣
	 * 
	 * @param msrp
	 * @param tp
	 * @return
	 */
	public String countDiscount(String msrp, String tp)
	{
		String discount = "-";
		if(!"-".equals(msrp) && !"-".equals(tp)) {
			discount = Float.parseFloat(msrp) - Float.parseFloat(tp) + "";
		}
		return discount;
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
		if("EN".equals(languageType)) {
			value = obj.getString(property + "En");
		} else {
			value = obj.getString(property);
		}
		return value;
	}

	/**
	 * 获取是否含最新周
	 * 
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String findLatestWeek(HttpServletRequest request, Map<String, Object> paramsMap) {
		String endDate = request.getAttribute("endDate").toString();
		paramsMap.put("endDate", endDate);
		List<String> list = profitDao.findLatestWeek(paramsMap);
		if(list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
