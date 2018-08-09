package com.ways.app.policy.service.impl;

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
import com.ways.app.policy.dao.IPromotionDao;
import com.ways.app.price.model.EchartLineDataEntity;
import com.ways.app.price.model.EchartMarkPointEntity;
import com.ways.app.price.model.Manf;
import com.ways.app.price.model.SubModel;
import com.ways.app.policy.model.ObjectInfoEntity;
import com.ways.app.policy.service.IPromotionManager;
import com.ways.app.policy.model.VersionPromotionInfoEntity;

/**
 * 促销走势分析Service层接口实现类
 * @author huangwenmei
 *
 */
@Service("promotionManager")
public class PromotionManagerImpl implements IPromotionManager {

	@Autowired
	private IPromotionDao promotionDao;
	/*@Autowired
	private SaleIncentiveQueryDao saleIncentiveQueryDao;*/
//	private String [] titles1  = new String[]{"Date","Code","Manufacture","Origin","Segment","Trim","Model","Transmission","Engine Capacity","Bodytype","Launch Date","Model Year"};
//	private String [] titles2 = new String[]{"年月","型号编码","生产厂","系别","级别","型号标识","车型","排档方式","排量","车身形式","上市时间","年式"};
//	private String [] columns1 = new String[]{"Version incentive","Delivery support","Retail support","Staff bonus","Financial loan","Trade-in support","Free insurance","Free present","Optional incentive","Volume by Version"};
//	private String [] columns2  = new String[]{"型号促销","提车支持","零售支持","人员奖励","金融贷款","置换支持","赠送保险","赠送礼品","选择促销","型号销量"};

	/**
	 * 设置促销数据的分类信息
	 * 
	 * @param paramsMap
	 */
	private void putParam(Map<String, Object> paramsMap) 
	{
		String  dataUserName = "";
		String  dataUserNameKey = "dataUserName";
		// 促销总额
		String promotionType1 = "5";
		// 提车支持
		String promotionType2 = "6";
		// 零售支持
		String promotionType3 = "7";
		// 人员奖励
		String promotionType4 = "8";
		// 金融贷款
		String promotionType5 = "9";
		// 置换支持
		String promotionType6 = "10";
		// 赠送保险
		String promotionType7 = "11";
		// 赠送礼品（油卡、保养）
		String promotionType8 = "12";
		// 选择促销
		String promotionType9 = "13";
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
		List<Map<String, String>> list = promotionDao.initDate(paramsMap);
		if(null != list && list.size() > 0) {
			Map<String, String> resultMap = list.get(0);
			String endDate = resultMap.get("ENDDATE");
			String beginDate = resultMap.get("BEGINDATE");
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			//默认开始时间，时间规则是相差12个点
			String defaultBeginDate = (Integer.parseInt(endDate.replace("-", "")) - 99) + "";
			//如果结束时间减一年小于开始时间，则默认为开始时间
			if( Integer.parseInt(defaultBeginDate) < Integer.parseInt(beginDate.replace("-", ""))) {
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
	 * 加载促销走势图形和表格
	 * 
	 * @param request
	 * @param paramsMap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String loadChartAndTable(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		putParam(paramsMap);
		String objectType = paramsMap.get("objectType").toString();
		List list = null;
		//型号
		if("1".equals(objectType)) {
			list = promotionDao.loadVersionPromotionChartAndTable(paramsMap);
		} else if("2".equals(objectType)) {
			//车型
			list = promotionDao.loadModelPromotionChartAndTable(paramsMap);
		} else {
			//其他维度
			list = promotionDao.loadModelPromotionChartAndTable2(paramsMap);
		}
		if(null != list && list.size() > 0) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			if("1".equals(objectType)) {
				dataMap.put("chart", getChartDataToVersion(list, paramsMap));
			} else {
				dataMap.put("chart", getChartDataToObject(list, paramsMap));
			}			
			dataMap.put("grid", list);
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			request.getSession().setAttribute(Constant.getPromotionExcelDataKey, json);
		}
		return json;
	}
	
	/**
	 * 获取型号促销走势图形数据
	 * 
	 * @param list
	 * @param paramsMap
	 * @return
	 */
	public Map<String,Object> getChartDataToVersion(List<VersionPromotionInfoEntity> list, Map<String, Object> paramsMap)
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
				VersionPromotionInfoEntity vInfo = list.get(i);
				String vname = vInfo.getVersionChartName();
				String yearMonth = vInfo.getYearMonth();
				if(!xTitle.contains(yearMonth)) {
					xTitle += yearMonth + ",";
				}
				if(!versionId.contains(vInfo.getVid())) {
					versionId += vInfo.getVid() + ",";
					isNewVersion = true;
					if(i != 0) {
						isAddVersion = true;
					}
				}
				
				//添加对象
				if(isAddVersion) {
					lineList.add(new EchartLineDataEntity("line", versionName, symbol, lineData.split(","), 
							new ArrayList<EchartMarkPointEntity>(markPointList), isBase, symbolSize));
					isAddVersion = false;
					markPointList.clear();
				}
				
				String summaryValue = vInfo != null && vInfo.getC1() != null ? vInfo.getC1().trim() : "null";
				if(summaryValue.trim().equalsIgnoreCase("null")) {
					summaryValue = "-";
				}
				if(isNewVersion) {
					lineData = "";
					String yaxis = "";//汽泡Y轴位置
					lineData += summaryValue;  //促销总额
					yaxis = vInfo.getC1();  //促销总额
					
					isNewVersion = false;
					//如果标记名称不为空,则添加标记对象
					if(!AppFrameworkUtil.isEmpty(vInfo.getChangName()) && !AppFrameworkUtil.isEmpty(yaxis) && !"-".equals(yaxis)) {
						markPointList.add(new EchartMarkPointEntity(vInfo.getChangName(), "", vInfo.getYearMonth(), 
								AppFrameworkUtil.getNum(Double.parseDouble(yaxis), 0), "5"));
					}
				} else {
					String yaxis = "";//汽泡Y轴位置
					lineData += "," + summaryValue;  //促销总额
					yaxis = vInfo.getC1();  //促销总额
					//如果标记名称不为空,则添加标记对象
					if(!AppFrameworkUtil.isEmpty(vInfo.getChangName()) && !AppFrameworkUtil.isEmpty(yaxis) && !"-".equals(yaxis)) {
						markPointList.add(new EchartMarkPointEntity(vInfo.getChangName(), "", vInfo.getYearMonth(),
								AppFrameworkUtil.getNum(Double.parseDouble(yaxis), 0), "5"));
					}
				}
				versionName = vname;
				//线轴点形状,默认为实心,如果是本品则为空心
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
	public Map<String,Object> getChartDataToObject(List<ObjectInfoEntity> list, Map<String, Object> paramsMap)
	{
		Map<String,Object> dataMap = new HashMap<String,Object>();
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
				if("2".equals(objectType)) {
					oName = mInfo.getSubmodelNameEn();
				} else {
					oName = mInfo.getManfNameEn();
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
						if(i != 0) {
							isAddObject = true;
						}
					}
				}
				
				//添加对象
				if(isAddObject) {
					lineList.add(new EchartLineDataEntity("line", objectName, symbol, lineData.split(","), 
							new ArrayList<EchartMarkPointEntity>(markPointList), isBase, symbolSize));
					isAddObject = false;
					markPointList.clear();
				}
				String summaryValue = mInfo != null && mInfo.getC1() != null ? mInfo.getC1().trim() : "null";
				if(summaryValue.trim().equalsIgnoreCase("null")) {
					summaryValue = "-";
				}
				if(isNewObject) {
					lineData = "";
					//String yaxis = "";//汽泡Y轴位置				
				    lineData += summaryValue;//默认为利润
					//yaxis = mInfo.getC1();					
					isNewObject = false;
				} else {
					//String yaxis = "";//汽泡Y轴位置					
					lineData += "," + summaryValue;//默认为利润
					//yaxis = mInfo.getC1();					
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
			
			xTitle = xTitle.substring(0,xTitle.length() - 1);
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
		for(int i = 0; i < list.size(); i++) {
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
		        	   		if (Integer.parseInt(v1.getIsBase()) < Integer.parseInt(v2.getIsBase())) {
		        	   			return 1;
		        	   		} else if (Integer.parseInt(v1.getIsBase()) > Integer.parseInt(v2.getIsBase())) {
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
		putParam(paramsMap);
		List<SubModel> list = promotionDao.checkPopBoxData(paramsMap);
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
	public String checkManfPopBoxData(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		String json = "";
		putParam(paramsMap);
		List<Manf> list = promotionDao.checkManfPopBoxData(paramsMap);
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
		String json = (String) request.getSession().getAttribute(Constant.getPromotionExcelDataKey);
		if(!AppFrameworkUtil.isEmpty(json)) {
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			JSONObject chartObj = (JSONObject) obj.get("chart");
			JSONArray  gridObj = (JSONArray) obj.getJSONArray("grid");
			try {
				String path = request.getSession().getServletContext().getRealPath("/"); 
				wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/promotionTemplate.xls")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			exportObjectOriginalData(wb, wb.getSheet("原数据"), gridObj, paramsMap);
			exportObjectChartData(wb, wb.getSheet("DATA"), chartObj, paramsMap);
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
	public void exportObjectOriginalData(Workbook wb,Sheet s,JSONArray gridObj,Map<String, Object> paramsMap)
	{
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		//String[] titles = null;//标题数组
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
		//CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
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
		putParam(paramsMap);
		List list = null;
		String objectType = paramsMap.get("objectType").toString();
		if("1".equals(objectType)) {
			// 型号
			list = promotionDao.exportVersionPromotionChartAndTable(paramsMap);
		} else if("2".equals(objectType)) {
			// 车型
			list = promotionDao.exportPromotionData(paramsMap);
		} else {
			// 厂商品牌,品牌,系别,级别
			list = promotionDao.exportPromotionData2(paramsMap);
		}
		Row row;
		int rowIndex =0;//行号锁引
		//写表格数据内容
		for(int j = 0; j < list.size(); j++) {
			VersionPromotionInfoEntity  obj = (VersionPromotionInfoEntity)list.get(j);
			//如果无记录则不导出
			if(AppFrameworkUtil.isEmpty(obj.getVersionCode())) {
				continue;
			}
			String yearMonth = obj.getYearMonth();
			String versionCode = obj.getVersionCode();
			String versionLaunchDate = obj.getVersionLaunchDate();
			String versionSale = obj.getVersionSale();
			String c1 = null;
			if(obj.getC1() == null) {
				c1="-";
			} else {
				c1 = obj.getC1();
			}
			
			String c2 = null;
			if(obj.getC2() == null) {
				c2 = "-";
			} else {
				c2 = obj.getC2();
			}
			String c3 = null;
			if(obj.getC3() == null) {
				c3 = "-";
			} else {
				c3 = obj.getC3();
			}
			String c4 = null;
			if(obj.getC4() == null) {
				c4 = "-";
			} else {
				c4 = obj.getC4();
			}
			String c5 = null;
			if(obj.getC5() == null) {
				c5 = "-";
			} else {
				c5 = obj.getC5();
			}
			String c6 = null;
			if(obj.getC6() == null) {
				c6 = "-";
			} else {
				c6 = obj.getC6();
			}
			String c7 = null;
			if(obj.getC7() == null){
				c7 = "-";
			} else {
				c7 = obj.getC7();
			}
			String c8 = null;
			if(obj.getC8() == null) {
				c8 = "-";
			} else {
				c8 = obj.getC8();
			}
//			String c9 = null;
//			if(obj.getC9() == null) {
//				c9 = "-";
//			} else { 
//				c9 = obj.getC9();
//			}
			//新增字段
			String segment = obj.getSegment();//车身级别
			String msrp = obj.getMsrp();//指导价
			String bonus = null;
			if(obj.getBonus() == null) {
				bonus = "-";
			} else {
				bonus = obj.getBonus();//考核奖励
			}
			String margin = null;
			if(obj.getMargin() == null) {
				margin = "-";
			} else {
				margin = obj.getMargin();//返利
			}	
			
			String fullyPaid=null;
			if(obj.getFullyPaid() == null){
				fullyPaid="-";
			} else{
				fullyPaid=obj.getFullyPaid().toString();//全款购车促销支持
			}
			
			String grossSupports = null;
			if(c2 == "-" && c3 == "-") {
				grossSupports = "-";
			} else {
				grossSupports = obj.getGrossSupports();//经销商支持
			}
			String customerIncentive = obj.getCustomerIncentive();//用户激励
//			String presents = null;
//			if(obj.getPresents() == null) {
//				presents = "-";
//			} else {
//				presents = obj.getPresents();//礼品
//			}
//					
//			String insurance = null;
//			if(obj.getInsurance() == null) {
//				insurance = "-";
//			} else {
//				insurance = obj.getInsurance();//保险
//			}
			String maintenance = null;
			if(obj.getMaintenance() == null) {
				maintenance = "-";
			} else {
				maintenance = obj.getMaintenance();//保养
			}
			String invoicePrice = null;
			if(obj.getInvoicePrice() == null) {
				invoicePrice = "-";
			} else {
				invoicePrice = obj.getInvoicePrice();//开票价
			}
			String grossInvoicePrice = null;
			if(obj.getGrossInvoicePrice() == null ) {
				grossInvoicePrice = "-";
			} else {
				grossInvoicePrice = obj.getGrossInvoicePrice();//经销商开票价
			}
			String tp = obj.getTp();//成交价tp
			String profit = null;
			if(obj.getProfit() == null) {
				profit = "-";
			} else {
				profit = obj.getProfit();//利润
			}
			String profitRate = null;
			if(obj.getProfitRate() == null) {
				profitRate = "-";
			} else {
				profitRate = obj.getProfitRate().toString();//利润率
			}
			String versionLastMonthSales = null;
			if(obj.getVersionLastMonthSales() == null) {
				versionLastMonthSales = "-";
			} else {
				versionLastMonthSales=obj.getVersionLastMonthSales();//型号上月销量
			}
			
			String brandName = obj.getBrandName();//品牌
			String versionName = obj.getVersionName();//型号全称中文
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
				versionName = obj.getVersionNameEn();//型号全称英文
			} 
			//重置锁引，用于创建单元格列锁引位置
			Cell cell = null;
			index = 0;
			//rowIndex++;
			++rowIndex;
			row  = ExportExcelUtil.createRow(s, rowIndex, 400);
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, yearMonth + "~", textStyle);
				
			//"年月","型号编码","生产厂","系别","级别","型号标识","车型","拍档方式","排量","车身形式","上市时间","年式"			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  manfName + "~", textStyle);
				
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
			ExportExcelUtil.setCellValueAndStyle(cell,  c1 , thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  fullyPaid, thousandthStyle);
			
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  grossSupports, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell, c2, thousandthStyle);
				
			cell = row.createCell(index++);
			ExportExcelUtil.setCellValueAndStyle(cell,  c3, thousandthStyle);
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
			ExportExcelUtil.setCellValueAndStyles(cell, profit,thousandthStyle, thousandthREDStyle);
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
	 * @param chartObj
	 * @param paramsMap
	 */
	public void exportObjectChartData(Workbook wb, Sheet s, JSONObject chartObj, Map<String, Object> paramsMap)
	{
		String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
		JSONArray titles = chartObj.getJSONArray("xTitle");
		JSONArray series =  chartObj.getJSONArray("series");
		String firstTitle = "促销";
		if("EN".equals(languageType)) {
			firstTitle = "Promotion";
		}
		if("2".equals(paramsMap.get("analysisContentType"))) {
			firstTitle = "内促";
			if("EN".equals(languageType)) {
				firstTitle = "Promotion";
			}
		}
		if("3".equals(paramsMap.get("analysisContentType"))) {
			firstTitle = "外促";
			if("EN".equals(languageType)) {
				firstTitle = "Promotion";
			}
		}
		int rowIndex = 0;//行号锁引
		Row row = ExportExcelUtil.createRow(s, rowIndex, 600);
		
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
			cell = row.createCell(i + 1);
			ExportExcelUtil.setCellValueAndStyle(cell, titles.getString(i), titleStyle);
			if(0 == i) {
				s.setColumnWidth(i, 6000);
			} else {
				s.setColumnWidth(i, 2500);
			}
		}
		
		//写数据
		for(int j = 0; j < series.size(); j++) {
			JSONObject obj = series.getJSONObject(j);
			JSONArray datas = obj.getJSONArray("data");
			
			rowIndex++;
			row = ExportExcelUtil.createRow(s, rowIndex, 400);
			cell = row.createCell(0);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("name"), textStyle);
			
			for(int n = 0; n < datas.size(); n++) {
				cell = row.createCell(n + 1);
				if(datas.get(n) != null && datas.getString(n).trim().length() > 0 && !datas.getString(n).trim().equalsIgnoreCase("null")) {
					ExportExcelUtil.setCellValueAndStyle(cell, AppFrameworkUtil.getNum(datas.getString(n), 7), thousandthStyle);
				} else {
					ExportExcelUtil.setCellValueAndStyle(cell, "-", thousandthStyle);
				}
			}
			//写入本品标识
			cell = row.createCell(130);
			ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("isBase"), thousandthStyle);
		}
		s.setDisplayGridlines(false);
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
}