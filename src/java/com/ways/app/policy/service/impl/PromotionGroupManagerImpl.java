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
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.framework.utils.Constant;
import com.ways.app.framework.utils.ExportExcelUtil;
import com.ways.app.policy.dao.IPromotionGroupDao;
import com.ways.app.policy.model.TerminalObjectEntity;
import com.ways.app.policy.model.VersionPromotionInfoEntity;
import com.ways.app.policy.service.IPromotionGroupManager;
import com.ways.app.price.model.EchartLineDataEntity;
import com.ways.app.price.model.Manf;
import com.ways.app.price.model.SubModel;

@Service("promotionGroupManager")
public class PromotionGroupManagerImpl  implements IPromotionGroupManager{

	@Autowired
	private IPromotionGroupDao promotionGroupDao;
//	private String [] titles1  = new String[]{"Date","Code","Manufacture","Origin","Segment","Trim","Model","Transmission","Engine Capacity","Bodytype","Launch Date","Model Year"};
//	private String [] titles2 = new String[]{"年月","型号编码","生产厂","系别","级别","型号标识","车型","排档方式","排量","车身形式","上市时间","年式"};
//	private String [] columns1 = new String[]{"Version incentive","Delivery support","Retail support","Staff bonus","Financial loan","Trade-in support","Free insurance","Free present","Optional incentive","Volume by Version"};
//	private String [] columns2  = new String[]{"型号促销","提车支持","零售支持","人员奖励","金融贷款","置换支持","赠送保险","赠送礼品","选择促销","型号销量"};
	
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
		String json = "";//返回默认开始时间和结束时间
		putParam(paramsMap);
		List<Map<String, String>> list = promotionGroupDao.initDate(paramsMap);
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
				request.setAttribute("defaultBeginDate", defaultBeginDate );
				json = defaultBeginDate;
			}
			json += "," +resultMap.get("BEGINDATE") + "," + endDate;
		}
		return json;
	}

	/**
	 * 加载促销分类分析图形和表格
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
		//String submitType = paramsMap.get("submitType").toString();
		String modelId = paramsMap.get("modelIds").toString();
		int modelIdSize = modelId.split(",").length;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List list2 = null;
		//型号查询需要查询上下代
		if("1".equals(objectType)) {
			list2 = promotionGroupDao.loadPromotionGroupChartAndTableByVersion(paramsMap);
		} else {
			list2 = promotionGroupDao.loadPromotionGroupDataChartAndTable(paramsMap);
		}
		//车型维度补空数据
		List<TerminalObjectEntity> list = list2;
		List<TerminalObjectEntity> listnew = new ArrayList<TerminalObjectEntity>();//组建新的list
		List listnew2 = null;//判断相同时间内元素的个数
		if("2".equals(objectType) && null != list && list.size() > 0) {
			String time = "";
			for (int l = 0; l < list.size(); l++) {
				boolean s = false;
				if (!time.contains(list.get(l).getDateTime())) {
					time += "," + list.get(l).getDateTime();
					s = true;
					if (l != 0) {
						if ((listnew2.size() + 1) / 9 < modelIdSize) {
							// 加少了的list
							// ,每个车型加9条,加modelIdSize-(listnew2.size()+1)/9个车型
							for (int p = 0; p < modelIdSize - (listnew2.size() + 1) / 9; p++) {
								for (int o = 0; o < 9; o++) {
									TerminalObjectEntity t = new TerminalObjectEntity();
									// 特殊情况处理当201600时取201512
									int tm = (Integer.parseInt(list.get(l).getDateTime()) - 1);
									String tmm = String.valueOf((Integer.parseInt(list.get(l).getDateTime()) - 1));
									if (tmm.substring(4, 6).equals("00")) {
										tm = tm - 100 + 12;
									}
									t.setDateTime(String.valueOf(tm));
									StringBuffer dx = new StringBuffer();
									dx.append(" ");
									// 确保每加一个车型的objectId,objectName,objectNameEn都不一样
									for (int u = 0; u < p; u++) {
										dx.append(" ");
									}
									t.setObjectId(dx.toString());
									t.setObjectName(dx.toString());
									t.setObjectNameEn(dx.toString());
									t.setSubsidyTypeId(" ");
									switch(o) {
									    case 0: t.setSubsidyType("提车支持(STD支持)");
									    break;
									    case 1: t.setSubsidyType("零售支持(AaK支持)");
							            break;
									    case 2: t.setSubsidyType("人员奖励");
							            break;
									    case 3: t.setSubsidyType("金融贷款");
							            break;
									    case 4: t.setSubsidyType("置换支持");
							            break;
									    case 5: t.setSubsidyType("赠送保险");
							            break;
									    case 6: t.setSubsidyType("交车礼品支援");
							            break;
									    case 7: t.setSubsidyType("保养赠送支援");
							            break;
									    case 8: t.setSubsidyType("选择促销");
							            break;
									}
//									if (o == 0) {
//										t.setSubsidyType("提车支持(STD支持)");
//									}
//									if (o == 1) {
//										t.setSubsidyType("零售支持(AaK支持)");
//									}
//									if (o == 2) {
//										t.setSubsidyType("人员奖励");
//									}
//									if (o == 3) {
//										t.setSubsidyType("金融贷款");
//									}
//									if (o == 4) {
//										t.setSubsidyType("置换支持");
//									}
//									if (o == 5) {
//										t.setSubsidyType("赠送保险");
//									}
//									if (o == 6) {
//										t.setSubsidyType("交车礼品支援");
//									}
//									if (o == 7) {
//										t.setSubsidyType("保养赠送支援");
//									}
//									if (o == 8) {
//										t.setSubsidyType("选择促销");
//									}
									t.setSubsidy(" ");
									listnew.add(t);
								}
							}
						}
					}
					listnew2 = new ArrayList();
				}

				if (s == false) {
					listnew2.add(" ");
				}

				// 进来一个加一个
				TerminalObjectEntity t = new TerminalObjectEntity();
				t.setDateTime(list.get(l).getDateTime());
				t.setObjectId(list.get(l).getObjectId());
				t.setObjectName(list.get(l).getObjectName());
				t.setObjectNameEn(list.get(l).getObjectNameEn());
				t.setSubsidyTypeId(list.get(l).getSubsidyTypeId());
				t.setSubsidyType(list.get(l).getSubsidyType());
				t.setSubsidy(list.get(l).getSubsidy());
				listnew.add(t);
				// 最后一个时间段补数据
				if (l == list.size() - 1) {
					if ((listnew2.size() + 1) / 9 < modelIdSize) {
						// 加少了的list
						// ,每个车型加9条,加modelIdSize-(listnew2.size()+1)/9个车型
						for (int p = 0; p < modelIdSize - (listnew2.size() + 1) / 9; p++) {
							for (int o = 0; o < 9; o++) {
								TerminalObjectEntity tt = new TerminalObjectEntity();
								// 特殊情况处理当201600时取201512
								int tm = (Integer.parseInt(list.get(l).getDateTime()));
								String tmm = String.valueOf((Integer.parseInt(list.get(l).getDateTime())));
								if (tmm.substring(4, 6).equals("00")) {
									tm = tm - 100 + 12;
								}
								tt.setDateTime(String.valueOf(tm));
								StringBuffer dx = new StringBuffer();
								dx.append(" ");
								// 确保每加一个车型的objectId,objectName,objectNameEn都不一样
								for (int u = 0; u < p; u++) {
									dx.append(" ");
								}
								tt.setObjectId(dx.toString());
								tt.setObjectName(dx.toString());
								tt.setObjectNameEn(dx.toString());
								tt.setSubsidyTypeId(" ");
								switch(o) {
							    case 0: tt.setSubsidyType("提车支持(STD支持)");
							    break;
							    case 1: tt.setSubsidyType("零售支持(AaK支持)");
					            break;
							    case 2: tt.setSubsidyType("人员奖励");
					            break;
							    case 3: tt.setSubsidyType("金融贷款");
					            break;
							    case 4: tt.setSubsidyType("置换支持");
					            break;
							    case 5: tt.setSubsidyType("赠送保险");
					            break;
							    case 6: tt.setSubsidyType("交车礼品支援");
					            break;
							    case 7: tt.setSubsidyType("保养赠送支援");
					            break;
							    case 8: tt.setSubsidyType("选择促销");
					            break;
							}
//								if (o == 0) {
//									tt.setSubsidyType("提车支持(STD支持)");
//								}
//								if (o == 1) {
//									tt.setSubsidyType("零售支持(AaK支持)");
//								}
//								if (o == 2) {
//									tt.setSubsidyType("人员奖励");
//								}
//								if (o == 3) {
//									tt.setSubsidyType("金融贷款");
//								}
//								if (o == 4) {
//									tt.setSubsidyType("置换支持");
//								}
//								if (o == 5) {
//									tt.setSubsidyType("赠送保险");
//								}
//								if (o == 6) {
//									tt.setSubsidyType("交车礼品支援");
//								}
//								if (o == 7) {
//									tt.setSubsidyType("保养赠送支援");
//								}
//								if (o == 8) {
//									tt.setSubsidyType("选择促销");
//								}
								tt.setSubsidy(" ");
								listnew.add(tt);
							}
						}
					}
				}
			}

		}
		if("2".equals(objectType)) {
			if (null != listnew && listnew.size() > 0) {
				dataMap.put("chart", getChartDataByPromotion(listnew));
				dataMap.put("grid", listnew);
				json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
				request.getSession().setAttribute(Constant.getPromotionGroupExcelDataKey, json);
			}
		} else {
			if (null != list && list.size() > 0) {
				dataMap.put("chart", getChartDataByPromotion(list));
				dataMap.put("grid", list);
				json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
				request.getSession().setAttribute(Constant.getPromotionGroupExcelDataKey, json);
			}
		}
		return json;
	}
	
	/**
	 * 函数功能说明 获取对象促销分类分析图形数据 
	 * 
	 * @param list
	 * @param paramsMap
	 * @return
	 */
	public Map<String, Object> getChartDataByPromotion(List<TerminalObjectEntity> tList) 
	{
		String oStr = "";
		String dStr = "";
		String showType = "";
		List<String> oList = new ArrayList<String>();// 对象
		List<String> dList = new ArrayList<String>();// 时间
		Map<String, Object> dataMap = new HashMap<String, Object>();
		for (int i = 0; i < tList.size(); i++) {
			String dtime = tList.get(i).getDateTime();
			String oName = tList.get(i).getObjectNameEn();
			if (!dStr.contains(dtime)) {
				dStr += dtime + ",";
				dList.add(dtime);
			}
			
			if (!oStr.contains(oName)) {
				oStr += oName + ",";
				oList.add(oName);
			}
		}
		// 对象显示最大数量
		if (dList.size() <= 8) {
			showType = "1";
			dataMap = getChartDataByPromotion(tList, showType, oList.size());
			dataMap.put("showType", showType);
		}
		if (oList.size() <= 8 && dList.size() > 4) {
			showType = "1";
			dataMap = getChartDataByPromotion(tList, showType, dList.size());
			dataMap.put("showType", showType);
		}
		return dataMap;
	}
	
	/**
	 * 获取促销图表数据
	 * 
	 * @param tList
	 * @param showType
	 * @param size
	 * @return
	 */
	public Map<String, Object> getChartDataByPromotion(List<TerminalObjectEntity> tList, String showType, int size) {
		//String CXZE = "促销总额";
		String TCZC = "提车支持(STD支持)";
		String LSZC = "零售支持(AaK支持)";
		String RYJL = "人员奖励";
		String JRDK = "金融贷款";
		String ZHZC = "置换支持";
		String ZSBX = "赠送保险";
		String ZSLP = "交车礼品支援";
		String XZCX = "保养赠送支援";
		
		String[] STARRY = new String[]{TCZC, LSZC, RYJL, JRDK, ZHZC, ZSBX, ZSLP, XZCX};
		String xTitle = "";
		String subxTitle = "";
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<EchartLineDataEntity> lineList = new ArrayList<EchartLineDataEntity>();
		for (int i = 0; i < STARRY.length; i++) {
			String subsidy ="";
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
			lineList.add(new EchartLineDataEntity("bar", subsidyType, "emptyCircle", subsidy.substring(0, subsidy.length() - 1).split(",")));
		}
		dataMap.put("xTitle", xTitle.substring(0, xTitle.length() - 1).split(","));
		dataMap.put("subxTitle", subxTitle.substring(0, subxTitle.length() - 1).split(","));
		dataMap.put("series", lineList);
		dataMap.put("titles", STARRY);
		return dataMap;
	}
	
	/**
	 * 获取型号促销走势图形数据
	 * 
	 * @param list
	 * @return
	 */
	/*public Map<String,Object> getChartDataToVersion(List<VersionPromotionInfoEntity> list,Map<String, Object> paramsMap)
	{
		Map<String,Object> dataMap = new HashMap<String,Object>();
		if(null != list && 0 != list.size())
		{
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
			for(int i = 0; i < list.size(); i++)
			{
				VersionPromotionInfoEntity vInfo = list.get(i);
				String vname = vInfo.getVersionChartName();
				String yearMonth = vInfo.getYearMonth();
				
				
				if(!xTitle.contains(yearMonth)) xTitle += yearMonth + ",";
				if(!versionId.contains(vInfo.getVersionChartName())) 
				{
					versionId += vInfo.getVersionChartName() + ",";
					isNewVersion = true;
					if(i != 0) isAddVersion = true;
				}
				
				//添加对象
				if(isAddVersion)
				{
					lineList.add(new EchartLineDataEntity("line",versionName,symbol,lineData.split(","),new ArrayList<EchartMarkPointEntity>(markPointList),isBase,symbolSize));
					isAddVersion = false;
					markPointList.clear();
				}
				
				String summaryValue = vInfo!=null&&vInfo.getC1()!=null?vInfo.getC1().trim():"null";
				if(summaryValue.trim().equalsIgnoreCase("null")){
					summaryValue = "-";
				}
				if(isNewVersion)
				{
					lineData = "";
					String yaxis = "";//汽泡Y轴位置
					lineData += summaryValue;  //促销总额
					yaxis = vInfo.getC1();  //促销总额
					
					isNewVersion = false;
					//如果标记名称不为空,则添加标记对象
					if(!AppFrameworkUtil.isEmpty(vInfo.getChangName()) && !AppFrameworkUtil.isEmpty(yaxis) && !"-".equals(yaxis))
					{
						markPointList.add(new EchartMarkPointEntity(vInfo.getChangName(),"",
								vInfo.getYearMonth(),AppFrameworkUtil.getNum(Double.parseDouble(yaxis), 0),"5"));
					}
				}
				else
				{
					String yaxis = "";//汽泡Y轴位置
					lineData += "," + summaryValue;  //促销总额
				
					yaxis = vInfo.getC1();  //促销总额
					
					
					//如果标记名称不为空,则添加标记对象
					if(!AppFrameworkUtil.isEmpty(vInfo.getChangName()) && !AppFrameworkUtil.isEmpty(yaxis) && !"-".equals(yaxis))
					{
						markPointList.add(new EchartMarkPointEntity(vInfo.getChangName(),"",vInfo.getYearMonth(),
								AppFrameworkUtil.getNum(Double.parseDouble(yaxis), 0),"5"));
					}
				}
				versionName = vname;
				//线轴点形状,默认为实心,如果是本品则为空心
				symbol = "circle";
				symbolSize = 3;
				if("1".equals(vInfo.getIsBase()))
				{
					 symbol = "emptyCircle";
					 symbolSize = 5;
				}
				isBase = vInfo.getIsBase();
				
				//添加最后一个对象
				if(i == list.size() - 1)
				{
					lineList.add(new EchartLineDataEntity("line",versionName,symbol,lineData.split(","),markPointList,isBase,symbolSize));
					isAddVersion = false;
				}
			}
			
			xTitle = xTitle.substring(0,xTitle.length()-1);
			dataMap.put("xTitle", xTitle.split(","));
			dataMap.put("series", orderByLineList(lineList));
			dataMap.put("titles", getLineLegend(lineList));
			dataMap.put("boundarys", EchartsUtil.setLineScaleDivision(lineList));
		}
		
		return dataMap;
	}*/
	
	/**
	* 函数功能说明 获取对象促销走势分析图形数据
	* 
	* huangwenmei  Dec 11, 2015
	* 修改者名字 修改日期
	* 修改内容
	* @param list
	* @param paramsMap
	* @return    
	* Map<String,Object>   
	*/
	/*public Map<String,Object> getChartDataToObject(List<ObjectInfoEntity> list,Map<String, Object> paramsMap)
	{
		Map<String,Object> dataMap = new HashMap<String,Object>();
		String objectType = paramsMap.get("objectType").toString();
		if(null != list && 0 != list.size())
		{
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
			for(int i = 0; i < list.size(); i++)
			{
				String oName = "";
				ObjectInfoEntity mInfo = list.get(i);
				String yearMonth = mInfo.getYearMonth();
				if("2".equals(objectType)){
					oName = mInfo.getSubmodelNameEn();
				}else{
					oName = mInfo.getManfNameEn();
				}
				
				
				if(!xTitle.contains(yearMonth)) xTitle += yearMonth + ",";
				if("2".equals(objectType)){
					if(!objectId.contains(mInfo.getSubModelId())) 
					{
						objectId += mInfo.getSubModelId() + ",";
						isNewObject = true;
						if(i != 0) isAddObject = true;
					}
				}else{
					if(!objectId.contains(mInfo.getManfId())) 
					{
						objectId += mInfo.getManfId() + ",";
						isNewObject = true;
						if(i != 0) 
							isAddObject = true;
					}
				}
				
				//添加对象
				if(isAddObject)
				{
					lineList.add(new EchartLineDataEntity("line",objectName,symbol,lineData.split(","),new ArrayList<EchartMarkPointEntity>(markPointList),isBase,symbolSize));
					isAddObject = false;
					markPointList.clear();
				}
				String summaryValue = mInfo != null && mInfo.getC1() != null ? mInfo.getC1().trim():"null";
				if(summaryValue.trim().equalsIgnoreCase("null")){
					summaryValue = "-";
				}
				if(isNewObject)
				{
					lineData = "";
					String yaxis = "";//汽泡Y轴位置				
				    lineData += summaryValue;//默认为利润
					yaxis = mInfo.getC1();					
					isNewObject = false;
				}
				else
				{
					String yaxis = "";//汽泡Y轴位置					
					lineData += "," + summaryValue;//默认为利润
					yaxis = mInfo.getC1();					
				}
				
				objectName = oName;
				//线轴点形状，默认为实心，如果是本品则为空心
				symbol = "circle";
				symbolSize = 3;
				if("1".equals(mInfo.getIsBase()))
				{
					 symbol = "emptyCircle";
					 symbolSize = 5;
				}
				isBase = mInfo.getIsBase();
				
				//添加最后一个对象
				if(i == list.size() - 1)
				{
					lineList.add(new EchartLineDataEntity("line",objectName,symbol,lineData.split(","),markPointList,isBase,symbolSize));
					isAddObject = false;
				}
			}
			
			xTitle = xTitle.substring(0,xTitle.length()-1);
			dataMap.put("xTitle", xTitle.split(","));
			dataMap.put("series", orderByLineList(lineList));
			dataMap.put("titles", getLineLegend(lineList));
			dataMap.put("boundarys", EchartsUtil.setLineScaleDivision(lineList));
		}
		
		return dataMap;
	}*/

	
	/**
	 * 获取图例
	 * @param list
	 * @return
	 */
	/*public String[] getLineLegend(List<EchartLineDataEntity> list)
	{
		String[] legned = new String[list.size()];
		for(int i = 0; i < list.size(); i++)
		{
			legned[i] = list.get(i).getName();
		}
		return legned;
	}*/
	
	/**
	 * 排序线图数据集，
	 * @param list
	 * @return
	 */
/*	public List<EchartLineDataEntity> orderByLineList(List<EchartLineDataEntity> list)
	{
		try {
			//按型号ID，时间年月，排序
			Collections.sort(list, new Comparator<EchartLineDataEntity>(){    
		           public int compare(EchartLineDataEntity v1, EchartLineDataEntity v2) { 
		        	   
		        	   		if (Integer.parseInt(v1.getIsBase()) < Integer.parseInt(v2.getIsBase())) return 1;
		        	   		
		        	   		else if (Integer.parseInt(v1.getIsBase()) > Integer.parseInt(v2.getIsBase())) return -1;
		        	   		
		        	   		else  return 0;	
			           }});
		} catch (Exception e) {

		}
		return list;
	}*/
	
	
	/**
	 * 获取线图分割段数分割值
	 * @param paramsMap
	 * @return
	 */
	/*public int getChartSplitValue(Map<String, Object> paramsMap)
	{
		int splitValue = 1000;
		if("3".equals(paramsMap.get("analysisContentType"))) splitValue = 10000;
		return splitValue;
	}*/

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
		putParam(paramsMap);
		List<SubModel> list = promotionGroupDao.checkPopBoxData(paramsMap);
		if(null != list && list.size() > 0) {
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(list);
		}
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
	public String checkManfPopBoxData(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		String json = "";
		putParam(paramsMap);
		List<Manf> list = promotionGroupDao.checkManfPopBoxData(paramsMap);
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
		String json = (String) request.getSession().getAttribute(Constant.getPromotionGroupExcelDataKey);
		if(!AppFrameworkUtil.isEmpty(json)) {
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			JSONObject chartObj = (JSONObject) obj.get("chart");
			JSONArray  gridObj = (JSONArray) obj.getJSONArray("grid");
			String showType = chartObj.getString("showType");
			try {
				String path = request.getSession().getServletContext().getRealPath("/"); 
				wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/promotionGroupTemplate.xls")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			//String objectType = paramsMap.get("objectType").toString();
			exportObjectOriginalData(wb, wb.getSheet("原数据"), gridObj, paramsMap);
			//exportObjectChartData(wb,wb.getSheet("DATA"),chartObj,paramsMap);
			if(!AppFrameworkUtil.isEmpty(showType)) {
				exportObjectChartData(wb, wb.getSheet("DATA"), gridObj, chartObj, paramsMap);
			}
		}
		return wb;
	}
	
	/**
	 * 导出原始数据
	 * 
	 * @param wb
	 * @param s
	 * @param gridObj
	 * @param paramsMap
	 */
	@SuppressWarnings("rawtypes")
	public void exportObjectOriginalData(Workbook wb, Sheet s, JSONArray gridObj, Map<String, Object> paramsMap)
	{
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		
//		String[] titles = null;//标题数组
		//"年月","型号编码","生产厂","系别","级别","型号标识","车型","拍档方式","排量","车身形式","上市时间","年式"
		/*if("EN".equals(languageType)) 
		{
			titles = new String[]{titles1[0],titles1[1],titles1[2],titles1[3],titles1[4],titles1[5],titles1[6],titles1[7],titles1[8],titles1[9],titles1[10],titles1[11],
					columns1[0],columns1[1],columns1[2],columns1[3],columns1[4],columns1[5],columns1[6],columns1[7],columns1[8],columns1[9]};
		}
		else 
		{
			titles = new String[]{titles2[0],titles2[1],titles2[2],titles2[3],titles2[4],titles2[5],titles2[6],titles2[7],titles2[8],titles2[9],titles2[10],titles2[11],
					columns2[0],columns2[1],columns2[2],columns2[3],columns2[4],columns2[5],columns2[6],columns2[7],columns2[8],columns2[9]};
		}*/
//		Cell cell = null;
//		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		CellStyle textPercentStyle = ExportExcelUtil.getExcelFillPercentageStyle(wb);//百分号格式化
		CellStyle textPercentRedStyle = ExportExcelUtil.getExcelFillPercentageRedStyle(wb);//红色
		CellStyle thousandthREDStyle =ExportExcelUtil.getExcelFormatThousandthREDStyle(wb);
		//表格标题数组锁引，如果是城市利润需要添加城市列
		int index  = 0;
		//对象选择不为型号时需要添加销量列
		//int length = titles.length;
		
		//写表格标题
/*		for(int i = index; i < length; i++)
		{
			int cellIndex = i;	
			cell = row.createCell(cellIndex);
		
				ExportExcelUtil.setCellValueAndStyle(cell, titles[i], titleStyle);
			
			s.setColumnWidth(i, 3000);
		}*/
		List list = null;
		String objectType = paramsMap.get("objectType").toString();		
		putParam(paramsMap);
		if("1".equals(objectType)) {
			list = promotionGroupDao.exportVersionPromotionChartAndTable(paramsMap);
		} else if("2".equals(objectType)) {
			list = promotionGroupDao.exportPromotionData(paramsMap);
		} else {
			list = promotionGroupDao.exportPromotionData2(paramsMap);
		}
//		String json = "";
//		Map<String, Object> dataMap = new HashMap<String,Object>();
		Row row;
		int rowIndex =0;//行号锁引
		//写表格数据内容
		for(int j = 0; j < list.size(); j++) {
			VersionPromotionInfoEntity obj = (VersionPromotionInfoEntity)list.get(j);
			//如果无记录则不导出
			if(AppFrameworkUtil.isEmpty(obj.getVersionCode())) {
				continue;
			}
			String yearMonth = obj.getYearMonth();
			String versionCode = obj.getVersionCode();
//			String discharge = obj.getDischarge();
			String versionLaunchDate = obj.getVersionLaunchDate();
//			String modelYear = obj.getModelYear();
//			String versionMix = obj.getVersionMix();
			String versionSale = obj.getVersionSale();
			String c1 = obj.getC1() == null ? "-" : obj.getC1();
			String c2 = obj.getC2() == null ? "-" : obj.getC2();
			String c3 = obj.getC3() == null ? "-" : obj.getC3();
			String c4 = obj.getC4() == null ? "-" : obj.getC4();
			String c5 = obj.getC5() == null ? "-" : obj.getC5();
			String c6 = obj.getC6() == null ? "-" : obj.getC6();
			String c7 = obj.getC7() == null ? "-" : obj.getC7();
			String c8 = obj.getC8() == null ? "-" : obj.getC8();
			//String c9 = obj.getC9() == null ? "-" : obj.getC9();
			
			//新增字段
			String segment = obj.getSegment();//车身级别
			String msrp = obj.getMsrp();//指导价
			//考核奖励
			String bonus = obj.getBonus() == null ? "-" : obj.getBonus();
			//返利
			String margin = obj.getMargin() == null ? "-" : obj.getMargin();
			//全款购车促销支持
			String fullyPaid = obj.getFullyPaid() == null ? "-" : obj.getFullyPaid();
			//经销商支持
			String grossSupports = (c2 == "-" && c3 == "-") ? "-" : obj.getGrossSupports();
			//用户激励
			String customerIncentive=obj.getCustomerIncentive();
			//礼品
//			String presents = obj.getPresents() == null ? "-" : obj.getPresents();
			//保险
//			String insurance = obj.getInsurance() == null ? "-" : obj.getInsurance();
			//保养
			String maintenance = obj.getMaintenance() == null ? "-" : obj.getMaintenance();
			//开票价
			String invoicePrice = obj.getInvoicePrice() == null ? "-" : obj.getInvoicePrice();
			//经销商开票价
			String grossInvoicePrice = obj.getGrossInvoicePrice() == null ? "-" : obj.getGrossInvoicePrice();
			//成交价TP
			String tp = obj.getTp();
			//利润
			String profit = obj.getProfit() == null ? "-" : obj.getProfit();
			//利润率
			String profitRate = obj.getProfitRate() == null ? "-" : obj.getProfitRate().toString();
			//型号上月销量
			String versionLastMonthSales = obj.getVersionLastMonthSales() == null ? "-" : obj.getVersionLastMonthSales();
			//品牌
			String brandName = obj.getBrandName();
			//型号全称中文
			String versionName = obj.getVersionName();
			String manfName = obj.getManfName();
//			String origName = obj.getOrigName();
			String gradeName = obj.getGradeName();
//			String versionShortName = obj.getVersionShortName();
			String subModelName = obj.getSubmodelName();
//			String gearMode = obj.getGradeName();				
			String bodyType = obj.getBodyType();
			if("EN".equals(languageType)) {
				manfName = obj.getManfNameEn();
//				origName = obj.getOrigNameEn();
				gradeName = obj.getGradeNameEn();
//				versionShortName = obj.getVersionShortNameEn();
				subModelName = obj.getSubmodelNameEn();
//				gearMode = obj.getGradeNameEn();				
				bodyType = obj.getBodyTypeEn();
				brandName = obj.getBrandNameEn();
				//型号全称英文
				versionName = obj.getVersionNameEn();
			} 
			//重置锁引，用于创建单元格列锁引位置
			Cell cell = null;
			index = 0;
			++rowIndex;
			row  = ExportExcelUtil.createRow(s, rowIndex, 400);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, yearMonth + "~", textStyle);
				
			//"年月","型号编码","生产厂","系别","级别","型号标识","车型","拍档方式","排量","车身形式","上市时间","年式"			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, manfName + "~", textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, segment, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, gradeName, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, bodyType, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, brandName, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, subModelName, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionName, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionCode + "~", textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionLaunchDate, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, msrp, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, margin, textStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, bonus, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c1, thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, fullyPaid, thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, grossSupports, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c2, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c3, thousandthStyle);
			String customerIncentive2 = null;	
			if(c8 == "-" && c7 == "-" && maintenance == "-" && c4 == "-" && c5 == "-" && c6 == "-") {
				customerIncentive2 = "-";
			} else {
				customerIncentive2 = customerIncentive;
			}
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, customerIncentive2, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c8, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c7, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, maintenance, thousandthStyle);
				
			//"型号销量"					
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c4, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c5, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c6, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, invoicePrice, thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, grossInvoicePrice, thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, tp, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyles(cell, profit, thousandthStyle, thousandthREDStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyles(cell, profitRate, textPercentStyle, textPercentRedStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionLastMonthSales, thousandthStyle);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, versionSale, thousandthStyle);
		}
		s.setDisplayGridlines(false);
	}


	/**
	 * 导出型号、车型、厂商、品牌、系别、级别图形数据
	 * 
	 * @param wb
	 * @param s
	 * @param gridObj
	 * @param chartObj
	 * @param paramsMap
	 */
	public void exportObjectChartData(Workbook wb, Sheet s, JSONArray gridObj, JSONObject chartObj, Map<String, Object> paramsMap)
	{
		//标题英文版
		String titles1 = "STD";
		String titles2 = "Aak";
		String titles3 = "Staff reward";
		String titles4 = "Financial loan";
		String titles5 ="Trade-in support";
		String titles6 = "Insurance";
		String titles7 = "Presents";
		String titles8 = "Maintenance";
		String languageType = (String)paramsMap.get("languageType");
		
		int rowIndex = 0;// 行号锁引
//		String xtitleStr = "";
//		String priceType ="1";//金额
		Row row = ExportExcelUtil.createRow(s, rowIndex, 600);
		
		JSONArray xTitles = chartObj.getJSONArray("xTitle");//时间
		JSONArray subxTitle = chartObj.getJSONArray("subxTitle");//对象数量
		JSONArray titles =  chartObj.getJSONArray("titles");
		JSONArray series =  chartObj.getJSONArray("series");

		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);// 表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);// 内容文本样式
//		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);// 格式化千分位样式
		
		int index = 0;
		Cell cell = null;
		cell = row.createCell(index++);
		//对象
		if(languageType.equals("EN")) {
			ExportExcelUtil.setCellValueAndStyle(cell, "Object", titleStyle);
		} else {
			ExportExcelUtil.setCellValueAndStyle(cell, "对象", titleStyle);
		}
			
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
		if(languageType.equals("EN")) {
			ExportExcelUtil.setCellValueAndStyle(cell, "Index", titleStyle);
		} else {
			ExportExcelUtil.setCellValueAndStyle(cell, "指标", titleStyle);
		}
		//横坐标
		for(int i = 0; i < xTitles.size(); i++) {
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, xTitles.getString(i), textStyle);
		}
		
		//做宏时根据这个判断维度
		String objectType= paramsMap.get("objectType").toString();
		cell = row.createCell(100);
		ExportExcelUtil.setCellValueAndStyle(cell, objectType, textStyle);
		
		for (int i = 0; i < titles.size(); i++) {
			index = 1;
			rowIndex++;
			row = ExportExcelUtil.createRow(s, rowIndex, 600);
			//指标
			cell = row.createCell(0);
			if(languageType.equals("EN")) {
				if(titles.getString(i).equals("提车支持(STD支持)")) {
					ExportExcelUtil.setCellValueAndStyle(cell, titles1, textStyle);
				}
				if(titles.getString(i).endsWith("零售支持(AaK支持)")) {
					ExportExcelUtil.setCellValueAndStyle(cell, titles2, textStyle);
				}
				if(titles.getString(i).equals("人员奖励")) {
					ExportExcelUtil.setCellValueAndStyle(cell, titles3, textStyle);
				}
				if(titles.getString(i).equals("金融贷款")) {
					ExportExcelUtil.setCellValueAndStyle(cell, titles4, textStyle);
				}
				if(titles.getString(i).equals("置换支持")) {
					ExportExcelUtil.setCellValueAndStyle(cell, titles5, textStyle);
				}
				if(titles.getString(i).equals("赠送保险")) {
					ExportExcelUtil.setCellValueAndStyle(cell, titles6, textStyle);
				}
				if(titles.getString(i).equals("交车礼品支援")) {
					ExportExcelUtil.setCellValueAndStyle(cell, titles7, textStyle);
				}
				if(titles.getString(i).equals("保养赠送支援")) {
					ExportExcelUtil.setCellValueAndStyle(cell, titles8, textStyle);
				}
			} else {
				ExportExcelUtil.setCellValueAndStyle(cell, titles.getString(i), textStyle);
			}
			
			//指标所对应横坐标值
			JSONObject obj = series.getJSONObject(i);
			JSONArray datas = obj.getJSONArray("data");
			for(int j = 0; j < datas.size(); j++) {
				cell = row.createCell(index++);
				if("-".equals(datas.getString(j)) || "".equals(datas.getString(j))) {
					ExportExcelUtil.setCellValueAndStyle(cell, "-", textStyle);
				} else {
					ExportExcelUtil.setCellValueAndStyle(cell, datas.getString(j), textStyle);
				}
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
	 * 设置促销数据的分类信息
	 * 
	 * @param paramsMap
	 */
	private void  putParam(Map<String, Object> paramsMap) 
	{
		String  dataUserName = ""; 
		String  dataUserNameKey = "dataUserName";
		//促销总额
		String  promotionType1 = "5";
		//提车支持
		String  promotionType2 = "6";
		//零售支持
		String  promotionType3 = "7";
		//人员奖励
		String  promotionType4 = "8";
		//金融贷款
		String  promotionType5 = "9";
		//置换支持
		String  promotionType6 = "10";
		//赠送保险
		String  promotionType7 = "11";
		//赠送礼品（油卡、保养）
		String  promotionType8 = "12";
		//选择促销
		String  promotionType9 = "13";
		String  paramModuleCode = "promotionPolicy";
		paramsMap.put(dataUserNameKey, dataUserName);
		paramsMap.put("paramModuleCode", paramModuleCode);
		paramsMap.put("promotionType1", promotionType1);
		paramsMap.put("promotionType2", promotionType2);
		paramsMap.put("promotionType3", promotionType3);
		paramsMap.put("promotionType4", promotionType4);
		paramsMap.put("promotionType5", promotionType5);
		paramsMap.put("promotionType6", promotionType6);
		paramsMap.put("promotionType7", promotionType7);
		paramsMap.put("promotionType8", promotionType8);
		paramsMap.put("promotionType9", promotionType9);
	}
}
