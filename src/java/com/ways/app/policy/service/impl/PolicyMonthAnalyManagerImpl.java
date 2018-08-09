package com.ways.app.policy.service.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.framework.utils.Constant;
import com.ways.app.framework.utils.ExportExcelUtil;
import com.ways.app.policy.dao.PolicyMonthAnalyDao;
import com.ways.app.policy.model.PolicyInfo;
import com.ways.app.policy.model.PolicySubModelInfo;
import com.ways.app.policy.model.PolicySubmodelEntity;
import com.ways.app.policy.service.PolicyMonthAnalyManager;
import com.ways.app.price.model.SubModel;


/**
 * 促销查询Service层接口实现类
 * @author yuml
 *
 */
@Service("policyMonthAnalyManager")
public class PolicyMonthAnalyManagerImpl implements PolicyMonthAnalyManager {

	@Autowired
	private PolicyMonthAnalyDao policyMonthAnalyDao;

	/**
	 * 初始时间
	 */
	@Override
	public String initDate(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		String json = "";//返回默认开始时间和结束时间
		List<Map<String, String>> list = policyMonthAnalyDao.initDate(paramsMap);
		if(null != list && 0 != list.size())
		{
			Map<String, String> resultMap = list.get(0);
			String endDate = resultMap.get("ENDDATE");
			request.setAttribute("beginDate", resultMap.get("BEGINDATE"));
			request.setAttribute("endDate", endDate);

			String defaultBeginDate = resultMap.get("DEFAULTBEGINDATE").toString().replace("-", "");
			//如果结束时间减一年小于开始时间，则默认为开始时间
			if(Integer.parseInt(defaultBeginDate) < Integer.parseInt(resultMap.get("BEGINDATE").replace("-", "")))
			{
				request.setAttribute("defaultBeginDate", resultMap.get("BEGINDATE"));
				json = resultMap.get("BEGINDATE");
			}
			else 
			{
				defaultBeginDate = defaultBeginDate.substring(0, 4) + "-" + defaultBeginDate.substring(4);
				request.setAttribute("defaultBeginDate", defaultBeginDate );
				json = defaultBeginDate;
			}
			json += "," +resultMap.get("BEGINDATE") + "," + endDate;
		}
		return json;
	}
	
	/**
	 * 加载车型政策内容
	 */
	public String loadModelPolicy(HttpServletRequest request,Map<String, Object> paramsMap)
	{
		String json = "";
			Map<String, Object> dataMap = new HashMap<String, Object>();
			List<PolicySubmodelEntity> submodelList = policyMonthAnalyDao.getSubmodel(paramsMap);//获得车型信息
			List<?> yearMonthList = policyMonthAnalyDao.getYearMonth(paramsMap);//获得年月信息
			dataMap.put("detail", policyMonthAnalyDao.getSubModelPolicyToDetailed(paramsMap));//详细
			dataMap.put("yearMonth", yearMonthList);//年月信息
			dataMap.put("submodelList", submodelList);//车型名称
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(dataMap);
			request.getSession().setAttribute(Constant.getExportExcelDataKey, json);
		return json;
	}
	
	/**
	 * 排序政策数据，将政策内容为无至底
	 * @param simpleList
	 */
	public List<PolicySubModelInfo> orderBySimpleList(List<PolicySubModelInfo> simpleList)
	{
		if(null != simpleList && 0 != simpleList.size())
		{
			for(int i = 0; i < simpleList.size(); i++)
			{
				PolicySubModelInfo subModel = simpleList.get(i);
				List<PolicyInfo> policyList = subModel.getPolicyList();
				List<PolicyInfo> demoList = new ArrayList<PolicyInfo>();
				
				for(int j = 0; j < policyList.size(); j++)
				{
					PolicyInfo policyObj = policyList.get(j);
					
					String lastMonthContent = policyObj.getPolicyLastMonthContent();
					String content = policyObj.getPolicyContent();
					
					lastMonthContent = lastMonthContent.replace("\r", "").replace("\n", "");
					content = content.replace("\r", "").replace("\n", "");
					
					policyObj.setPolicyLastMonthContent(lastMonthContent);
					policyObj.setPolicyContent(content);
					
					if("无".equals(lastMonthContent))
					{
						demoList.add(policyObj);
						policyList.remove(j);
						j--;
					}
				}
				if(0 != demoList.size()) policyList.addAll(demoList);
			}
		}
		return simpleList;
	}

	/**
	 * 导出
	 */
	@Override
	public Workbook exportExcel(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		Workbook wb = new HSSFWorkbook();
		String json = (String) request.getSession().getAttribute(Constant.getExportExcelDataKey);
		String analysisDimensionType = (String) paramsMap.get("analysisDimensionType");
		if(null != json)
		{
			String time = (String) paramsMap.get("beginDate");
			
			JSONObject obj = (JSONObject) JSONObject.parse(json);
			JSONArray detail = obj.getJSONArray("detail");
			JSONArray modelNames = obj.getJSONArray("submodelList");
			JSONArray yearMonth = obj.getJSONArray("yearMonth");
			//车型对比
			if("1".equals(analysisDimensionType))
			{
				//获取当前查询条件的时间
				if(!AppFrameworkUtil.isEmpty(time))
				{
					time = time.substring(5);
				}
				
				
				
				createSimpleSheet(wb,detail,modelNames,yearMonth,time);
				createDetailedSheet(wb,detail,"明细",analysisDimensionType,time,modelNames,yearMonth);
			}
			//时间对比
			else
			{
				createDetailedTimeSheet(wb,detail,"时间对比",paramsMap,modelNames,yearMonth);
			}
		}
		return wb;
	}
	
	/**
	 * 创建简单表格Sheet
	 * @param wb
	 * @param array
	 */
	public void createSimpleSheet(Workbook wb,JSONArray detail,JSONArray modelNames,JSONArray yearMonth,String time)
	{
		Sheet sheet = wb.createSheet("简单");
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle textStyle1 = getExcelFillTextStyle(wb);//单车促销时居右显示
		CellStyle textStyle2 = getExcelFillTextStyle1(wb);//促销政策内容居左显示
		CellStyle textRedStyle = getExcelFillTextRedStyle(wb);//促销政策内容居左显示字体红色
		CellStyle textBlueStyle = getExcelFillTextBlueStyle(wb);//促销政策内容居左显示字体蓝色
		CellStyle textGreenStyle = getExcelFillTextGreenStyle(wb);//促销政策内容居左显示字体绿色
		CellStyle textGreenNoBoldStyle = getExcelFillTextGreenNoBoldStyle(wb);//促销政策内容居左显示字体绿色不加粗


		int rowIndex = 0;//行号锁引
		//定义合并行范围
		int mergeBegin = 1;
		int mergeEnd = 0;
		
		//写标题行
		Row row = ExportExcelUtil.createRow(sheet, rowIndex, 1000);
		
		Cell cell = row.createCell(0);
		ExportExcelUtil.setCellValueAndStyle(cell, "车型", titleStyle);
		sheet.setColumnWidth(0, 4000);
		
		cell = row.createCell(1);
		ExportExcelUtil.setCellValueAndStyle(cell, "上月政策", titleStyle);
		sheet.setColumnWidth(1, 15000);
		
		cell = row.createCell(2);
		ExportExcelUtil.setCellValueAndStyle(cell, time+"月政策", titleStyle);
		sheet.setColumnWidth(2, 15000);
		
		//拿取每个车型每个月的条数
		int[][] policyListSizePerSubmodelPerMonth = new int[modelNames.size()][yearMonth.size()];
		for(int i = 0;i < modelNames.size();i++){
			JSONObject submodel=(JSONObject) modelNames.get(i);
			//policyListSizePerSubmodelPerMonth[i] = new int[][];
			for(int t = 0; t < yearMonth.size();t++){
				JSONObject yearMonth1=(JSONObject) yearMonth.get(t);
				for(int k = 0; k < detail.size();k++){
					JSONObject detail1=(JSONObject) detail.get(k);
					String submodelId = detail1.getString("submodelId");
					String ym = detail1.getString("ym");
					
						if(submodelId.equals(submodel.getString("submodelId")) && yearMonth1.getString("YM").equals(ym) ){
								int size = ((JSONArray) detail1.get("policyList")).size();
								policyListSizePerSubmodelPerMonth[i][t]=size;
						}
				}
			}
		}
		
		//计算每个车型里的在所有月的最大条数
				int[] maxPolicyNum = new int[modelNames.size()];
				for(int i = 0; i < policyListSizePerSubmodelPerMonth.length; i++){
					int size = 0;
					for(int k = 0; k < policyListSizePerSubmodelPerMonth[i].length; k++){
						if(size >= policyListSizePerSubmodelPerMonth[i][k]){
							size = size;
						} else{
							size = policyListSizePerSubmodelPerMonth[i][k];
						}
					}
					maxPolicyNum[i]=size;
				}
		
		//写数据
		for(int i = 0; i < modelNames.size(); i++)
		{
			JSONObject submodel = modelNames.getJSONObject(i);
			int mergeCount = maxPolicyNum[i];//合并行数
			
			for(int k = 0; k < mergeCount;k++){
				//写车型名称
				row = ExportExcelUtil.createRow(sheet, ++rowIndex, 600);
				cell = row.createCell(0);
				ExportExcelUtil.setCellValueAndStyle(cell, submodel.getString("submodelName"), textStyle);
				
				String lastMonthPolicyContent = "";
				for(int t = 0; t < yearMonth.size(); t++){
					JSONObject yearMonth1=(JSONObject) yearMonth.get(t);
					
					String fontColor = "";
					String monthPolicyContent ="";
					
					String policyName = "";
					String policyContent = "";
					String subsidyTypeId = "";
					
					String sort = Integer.toString(k+1) + ". ";
					
					
					for(int y = 0; y < detail.size(); y++){
						JSONObject detail1=(JSONObject) detail.get(y);
						String submodelId = detail1.getString("submodelId");
						String ym = detail1.getString("ym");
						if(submodel.getString("submodelId").equals(submodelId) && ym.equals(yearMonth1.getString("YM"))){
							JSONObject policyList1 = (JSONObject) detail1.getJSONArray("policyList").get(k);
							if(k >= ((JSONArray) detail1.get("policyList")).size()){
								policyName="";
								policyContent = "";
								subsidyTypeId="";
								
							} else{
								policyName = policyList1.getString("policyName");//政策名称
								policyContent = policyList1.getString("policyContent");//政策内容
								subsidyTypeId = policyList1.getString("subsidyTypeId");//政策类型
								if(subsidyTypeId.equals("15")){
									sort = "◆  ";
								} 
							}
							if(t == 0){
									lastMonthPolicyContent = policyContent;
							} else{
								if(!lastMonthPolicyContent.equals("") && lastMonthPolicyContent.equals(policyContent)){
									monthPolicyContent =  sort +"延续";
									//fontColor = "blue";//为了跟页面保持一致，延续时显示蓝色
								} else if(!lastMonthPolicyContent.equals("")  && !policyContent.equals("")&&!lastMonthPolicyContent.equals(policyContent)){
									monthPolicyContent =  sort+""+ policyName+": " +policyContent ;
									fontColor = "green";//改动显示绿色
								} else if(lastMonthPolicyContent.equals("") && policyContent.equals("")){
									monthPolicyContent = sort +"";
								} else if(!lastMonthPolicyContent.equals("") && policyContent.equals("")){
									monthPolicyContent =  sort +"取消";
									fontColor = "red";//取消显示红色
								} else if(lastMonthPolicyContent.equals("")  && !lastMonthPolicyContent.equals(policyContent)){
									monthPolicyContent = sort+"新增"+ policyName +": "+ policyContent ;
									fontColor = "greenNoBold";//改动显示绿色
								}
							}
						}
					}
					if(t==1){
						cell = row.createCell(2);
						if("red".equals(fontColor)){
							ExportExcelUtil.setCellValueAndStyle(cell, monthPolicyContent, textRedStyle);//政策内容列
						} else if("blue".equals(fontColor)){
							ExportExcelUtil.setCellValueAndStyle(cell, monthPolicyContent, textBlueStyle);//政策内容列
						} else if("green".equals(fontColor)){
							ExportExcelUtil.setCellValueAndStyle(cell, monthPolicyContent, textGreenStyle);//政策内容列
						} else if("greenNoBold".equals(fontColor)){
							//20170216改为新增加粗显示
							ExportExcelUtil.setCellValueAndStyle(cell, monthPolicyContent, textGreenStyle);//政策内容列
						}else{
							ExportExcelUtil.setCellValueAndStyle(cell, monthPolicyContent, textStyle2);//政策内容列
						}
						cell = row.createCell(3);
						cell.setCellValue("");//防止溢出
					} else{
						if(policyContent.equals("")){
							cell = row.createCell(1);
							ExportExcelUtil.setCellValueAndStyle(cell,  sort+"" , textStyle2);//政策内容列
						} else{
							cell = row.createCell(1);
							ExportExcelUtil.setCellValueAndStyle(cell,  sort+""+policyName +": " + policyContent, textStyle2);//政策内容列
						}
					}
					
				}
			}
			
			//平均单车列
			row = ExportExcelUtil.createRow(sheet, ++rowIndex, 600);
			cell = row.createCell(0);
			ExportExcelUtil.setCellValueAndStyle(cell, submodel.getString("submodelName"), textStyle);
			
			for(int t = 0; t < yearMonth.size(); t++){
				JSONObject yearMonth1=(JSONObject) yearMonth.get(t);
				String value = "";
				
				for(int y = 0; y < detail.size(); y++){
					JSONObject detail1=(JSONObject) detail.get(y);
					String submodelId = detail1.getString("submodelId");
					String ym = detail1.getString("ym");
					if(submodel.getString("submodelId").equals(submodelId) && ym.equals(yearMonth1.getString("YM"))){
						JSONArray policyList1 = (JSONArray) detail1.getJSONArray("policyList");
						for(int L = 0;L < policyList1.size(); L++){
							JSONObject policyList2 = policyList1.getJSONObject(L);
							if(!policyList2.getString("rewardTotal").equals("")){
								value = policyList2.getString("rewardTotal");//单车促销
								break;
							}
						}
					
					}
				}
				cell = row.createCell(t+1);
				if("".equals(value) || "-".equals(value)){
					ExportExcelUtil.setCellValueAndStyle(cell, "平均单车：" + value, textStyle1);//上月平均单车列
				} else{
					ExportExcelUtil.setCellValueAndStyle(cell, "平均单车：" + formatNum(Integer.toString((int)((Math.rint((double)Integer.parseInt(value)/100))*100))) + "元", textStyle1);//上月平均单车列
				}
			}
		
			//合并第一列车型
			mergeEnd += mergeCount+1;
			if(mergeEnd != mergeBegin) sheet.addMergedRegion(new CellRangeAddress(mergeBegin,mergeEnd,0,0));
			mergeBegin = mergeEnd + 1;
		}
		sheet.setDisplayGridlines(false);//设置表格是否显示网格
	}
	
	/**
	 * 创建明细表格Sheet
	 * @param wb
	 * @param array
	 * @param sheetName
	 * @param analysisDimensionType
	 * @param tdTitle
	 */
	public void createDetailedSheet(Workbook wb,JSONArray detail,String sheetName,String analysisDimensionType,String time,JSONArray modelNames,JSONArray yearMonth)
	{

		Sheet sheet = wb.createSheet(sheetName);
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle textStyle1 = getExcelFillTextStyle(wb);//单车促销时居右显示
		CellStyle textStyle2 = getExcelFillTextStyle1(wb);//促销政策内容居左显示
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//内容文本样式
		CellStyle textRedStyle = getExcelFillTextRedStyle(wb);//促销政策内容居左显示字体红色
		CellStyle textGreenNoBoldStyle = getExcelFillTextGreenNoBoldStyle(wb);//促销政策内容居左显示字体绿色不加粗
		CellStyle textGreenStyle = getExcelFillTextGreenStyle(wb);//促销政策内容居左显示字体绿色

		int rowIndex = 0;//行号锁引
		//定义合并行范围
		int mergeBegin = 1;
		int mergeEnd = 0;
		
		//写标题行
		Row row = ExportExcelUtil.createRow(sheet, rowIndex, 1000);
		
		Cell cell = row.createCell(0);
		ExportExcelUtil.setCellValueAndStyle(cell, "车型", titleStyle);
		sheet.setColumnWidth(0, 4000);
		
		int titleIndex = 0;
		for(int i = 0; i < yearMonth.size();i++){
			JSONObject yearMonth1 = yearMonth.getJSONObject(i);
			cell = row.createCell(++titleIndex);
			ExportExcelUtil.setCellValueAndStyle(cell,yearMonth1.getString("YM"), titleStyle);
			sheet.setColumnWidth(titleIndex, 2000);
			
			cell = row.createCell(++titleIndex);
			ExportExcelUtil.setCellValueAndStyle(cell, "", titleStyle);
			sheet.setColumnWidth(titleIndex, 15000);
			
			cell = row.createCell(++titleIndex);
			ExportExcelUtil.setCellValueAndStyle(cell, "", titleStyle);
			sheet.setColumnWidth(titleIndex, 3000);
		}
		
		//合并标题行
		sheet.addMergedRegion(new CellRangeAddress(0,0,1,3));
		sheet.addMergedRegion(new CellRangeAddress(0,0,4,6));
		
		//拿取每个车型每个月的条数
		int[][] policyListSizePerSubmodelPerMonth = new int[modelNames.size()][yearMonth.size()];
		for(int i = 0;i < modelNames.size();i++){
			JSONObject submodel=(JSONObject) modelNames.get(i);
			//policyListSizePerSubmodelPerMonth[i] = new int[][];
			for(int t = 0; t < yearMonth.size();t++){
				JSONObject yearMonth1=(JSONObject) yearMonth.get(t);
				for(int k = 0; k < detail.size();k++){
					JSONObject detail1=(JSONObject) detail.get(k);
					String submodelId = detail1.getString("submodelId");
					String ym = detail1.getString("ym");
					
						if(submodelId.equals(submodel.getString("submodelId")) && yearMonth1.getString("YM").equals(ym) ){
								int size = ((JSONArray) detail1.get("policyList")).size();
								policyListSizePerSubmodelPerMonth[i][t]=size;
						}
				}
			}
		}
		
		//计算每个车型里的在所有月的最大条数
				int[] maxPolicyNum = new int[modelNames.size()];
				for(int i = 0; i < policyListSizePerSubmodelPerMonth.length; i++){
					int size = 0;
					for(int k = 0; k < policyListSizePerSubmodelPerMonth[i].length; k++){
						if(size >= policyListSizePerSubmodelPerMonth[i][k]){
							size = size;
						} else{
							size = policyListSizePerSubmodelPerMonth[i][k];
						}
					}
					maxPolicyNum[i]=size;
				}
		
				//车型行
				for(int i = 0; i < modelNames.size(); i++){

					JSONObject submodel = modelNames.getJSONObject(i);
					int mergeCount = maxPolicyNum[i];//合并行数
					int indexNum = 0;
					for(int k = 0; k < mergeCount;k++){
						//写车型名称
						row = ExportExcelUtil.createRow(sheet, ++rowIndex, 600);
						cell = row.createCell(0);
						ExportExcelUtil.setCellValueAndStyle(cell, submodel.getString("submodelName"), textStyle);
						
						String lastMonthPolicyContent = "";
						 indexNum = 0;
						for(int t = 0; t < yearMonth.size(); t++){
							JSONObject yearMonth1=(JSONObject) yearMonth.get(t);
							String fontColor = "";
							String monthPolicyContent ="";
							
							String policyName = "";
							String policyContent = "";
							String subsidyTypeId = "";
							String reward = "";
							
							String sort = Integer.toString(k+1);
							
							for(int y = 0; y < detail.size(); y++){
								JSONObject detail1=(JSONObject) detail.get(y);
								String submodelId = detail1.getString("submodelId");
								String ym = detail1.getString("ym");
								if(submodel.getString("submodelId").equals(submodelId) && ym.equals(yearMonth1.getString("YM"))){
									JSONObject policyList1 = (JSONObject) detail1.getJSONArray("policyList").get(k);
									if(k >= ((JSONArray) detail1.get("policyList")).size()){
										policyName="";
										policyContent = "";
										subsidyTypeId="";
										reward = "";
									} else{
										policyName = policyList1.getString("policyName");//政策名称
										policyContent = policyList1.getString("policyContent");//政策内容
										subsidyTypeId = policyList1.getString("subsidyTypeId");//政策类型
										reward = policyList1.getString("reward");
										
										if(subsidyTypeId.equals("15")){
											sort = "◆";
											reward = "-";
										} 
									}
								
									if(t == 0){
										if(policyContent.equals("")){
											lastMonthPolicyContent ="";
										} else{
											lastMonthPolicyContent = policyName+": " + policyContent;
										}
									} else{
										if(!lastMonthPolicyContent.equals("") && policyContent.equals("")){
											monthPolicyContent = "取消";
											fontColor = "red";//取消为红色加粗
										} else if(lastMonthPolicyContent.equals("") && policyContent.equals("")){
											monthPolicyContent = "";
										} else if(lastMonthPolicyContent.equals("") && !policyContent.equals("")){
											monthPolicyContent = "新增" + policyName+": "+policyContent;
											fontColor = "greenNoBold";//新增为绿色不加粗
										} else if(!lastMonthPolicyContent.equals("") && !policyContent.equals("") && !lastMonthPolicyContent.equals( policyName+": " + policyContent)){
											monthPolicyContent = policyName+": "+policyContent;
											fontColor = "green";//改动为绿色加粗
										}else {
											monthPolicyContent = policyName+": "+policyContent;
										}
									}
								}
							}
							indexNum++;
							cell = row.createCell(indexNum);
							ExportExcelUtil.setCellValueAndStyle(cell,  sort , textStyle);//排序列
							if(t == 1){
								indexNum++;
								cell = row.createCell(indexNum);
								if("red".equals(fontColor)){
									ExportExcelUtil.setCellValueAndStyle(cell,  monthPolicyContent , textRedStyle);//政策内容列
								} else if("greenNoBold".equals(fontColor)){
									//20170216改为新增加粗
									ExportExcelUtil.setCellValueAndStyle(cell,  monthPolicyContent , textGreenStyle);//政策内容列
								}else if("green".equals(fontColor)){
									ExportExcelUtil.setCellValueAndStyle(cell,  monthPolicyContent , textGreenStyle);//政策内容列
								}else{
									ExportExcelUtil.setCellValueAndStyle(cell,  monthPolicyContent , textStyle2);//政策内容列
								}
							} else{
								indexNum++;
								cell = row.createCell(indexNum);
								ExportExcelUtil.setCellValueAndStyle(cell,  lastMonthPolicyContent , textStyle2);//政策内容列
							}
							
							indexNum++;
							cell = row.createCell(indexNum);
							if("".equals(reward) || "-".equals(reward)){
								ExportExcelUtil.setCellValueAndStyle(cell,  reward , textStyle);//reward促销列
							} else{
								ExportExcelUtil.setCellValueAndStyle(cell,  reward , thousandthStyle);//reward促销列
							}
						}
					}
					
					//平均单车列
					indexNum = 0;
					row = ExportExcelUtil.createRow(sheet, ++rowIndex, 600);
					cell = row.createCell(indexNum);
					ExportExcelUtil.setCellValueAndStyle(cell, submodel.getString("submodelName"), textStyle);
					
					
					
					for(int t = 0; t < yearMonth.size(); t++){
						JSONObject yearMonth1=(JSONObject) yearMonth.get(t);
						String value = "";
						
						for(int y = 0; y < detail.size(); y++){
							JSONObject detail1=(JSONObject) detail.get(y);
							String submodelId = detail1.getString("submodelId");
							String ym = detail1.getString("ym");
							if(submodel.getString("submodelId").equals(submodelId) && ym.equals(yearMonth1.getString("YM"))){
								JSONArray policyList1 = (JSONArray) detail1.getJSONArray("policyList");
								for(int L = 0;L < policyList1.size(); L++){
									JSONObject policyList2 = policyList1.getJSONObject(L);
									if(!policyList2.getString("rewardTotal").equals("")){
										value = policyList2.getString("rewardTotal");//单车促销
										break;
									}
								}
							
							}
						}
						
						cell = row.createCell(++indexNum);
						ExportExcelUtil.setCellValueAndStyle(cell, "", textStyle);
						
						cell = row.createCell(++indexNum);
						ExportExcelUtil.setCellValueAndStyle(cell, "单车促销", textStyle1);
						
						cell = row.createCell(++indexNum);
						if("".equals(value) || "-".equals(value)){
							ExportExcelUtil.setCellValueAndStyle(cell,  value, textStyle);//上月平均单车列
						} else{
							ExportExcelUtil.setCellValueAndStyle(cell,  Integer.toString((int)((Math.rint((double)Integer.parseInt(value)/100))*100)), thousandthStyle);//上月平均单车列
						}
					}
				
					//合并第一列车型
					mergeEnd += mergeCount+1;
					if(mergeEnd != mergeBegin) sheet.addMergedRegion(new CellRangeAddress(mergeBegin,mergeEnd,0,0));
					mergeBegin = mergeEnd + 1;
				}
	}

	/**
	 * 创建时间段对比下的明细
	 */
	
	public void createDetailedTimeSheet(Workbook wb,JSONArray detail,String sheetName,Map<String, Object> paramsMap,JSONArray modelNames,JSONArray yearMonth)
	{

		Sheet sheet = wb.createSheet(sheetName);
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle textStyle2 = getExcelFillTextStyle1(wb);//政策内容居左显示
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//内容文本样式

		
		int rowIndex = 0;//行号锁引
		//定义合并行范围
		int mergeBegin = 1;
		int mergeEnd = 0;
		int colsIndex = 0;//列索引
		
		//写标题行
		Row row = ExportExcelUtil.createRow(sheet, rowIndex, 1000);
		
		Cell cell = row.createCell(colsIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, "车型", titleStyle);
		sheet.setColumnWidth(colsIndex, 4000);
		
		 cell = row.createCell(++colsIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, "序号", titleStyle);
		sheet.setColumnWidth(colsIndex, 2000);
		
		 cell = row.createCell(++colsIndex);
		ExportExcelUtil.setCellValueAndStyle(cell, "奖励名称", titleStyle);
		sheet.setColumnWidth(colsIndex, 7000);
		
		//年月
		for(int i = 0; i < yearMonth.size();i++){
			JSONObject yearMonth1 = yearMonth.getJSONObject(i);
			
				cell = row.createCell(++colsIndex);
				ExportExcelUtil.setCellValueAndStyle(cell, yearMonth1.getString("YM"), titleStyle);
				sheet.setColumnWidth(colsIndex, 15000);
				
				cell = row.createCell(++colsIndex);
				ExportExcelUtil.setCellValueAndStyle(cell, "", titleStyle);
				sheet.setColumnWidth(colsIndex, 3000);
				//合并标题行
				sheet.addMergedRegion(new CellRangeAddress(0,0,colsIndex-1,colsIndex));
			
			
		}
		
		//拿取每个车型每个月的条数
		int[][] policyListSizePerSubmodelPerMonth = new int[modelNames.size()][yearMonth.size()];
		for(int i = 0;i < modelNames.size();i++){
			JSONObject submodel=(JSONObject) modelNames.get(i);
			//policyListSizePerSubmodelPerMonth[i] = new int[][];
			for(int t = 0; t < yearMonth.size();t++){
				JSONObject yearMonth1=(JSONObject) yearMonth.get(t);
				for(int k = 0; k < detail.size();k++){
					JSONObject detail1=(JSONObject) detail.get(k);
					String submodelId = detail1.getString("submodelId");
					String ym = detail1.getString("ym");
					
						if(submodelId.equals(submodel.getString("submodelId")) && yearMonth1.getString("YM").equals(ym) ){
								int size = ((JSONArray) detail1.get("policyList")).size();
								policyListSizePerSubmodelPerMonth[i][t]=size;
						}
				}
			}
		}
		
		//计算每个车型里的在所有月的最大条数
				int[] maxPolicyNum = new int[modelNames.size()];
				for(int i = 0; i < policyListSizePerSubmodelPerMonth.length; i++){
					int size = 0;
					for(int k = 0; k < policyListSizePerSubmodelPerMonth[i].length; k++){
						if(size >= policyListSizePerSubmodelPerMonth[i][k]){
							size = size;
						} else{
							size = policyListSizePerSubmodelPerMonth[i][k];
						}
					}
					maxPolicyNum[i]=size;
				}
		
				//车型行
				for(int i = 0; i < modelNames.size(); i++){

					JSONObject submodel = modelNames.getJSONObject(i);
					int mergeCount = maxPolicyNum[i];//合并行数
					int indexNum = 0;
					for(int k = 0; k < mergeCount;k++){
						//写车型名称
						row = ExportExcelUtil.createRow(sheet, ++rowIndex, 600);
						cell = row.createCell(0);
						ExportExcelUtil.setCellValueAndStyle(cell, submodel.getString("submodelName"), textStyle);
						
						 indexNum = 0;
						for(int t = 0; t < yearMonth.size(); t++){
							JSONObject yearMonth1=(JSONObject) yearMonth.get(t);
							
							String monthPolicyContent ="";
							
							String policyName = "";
							String policyContent = "";
							String subsidyTypeId = "";
							String reward = "";
							
							String sort = Integer.toString(k+1);
							
							for(int y = 0; y < detail.size(); y++){
								JSONObject detail1=(JSONObject) detail.get(y);
								String submodelId = detail1.getString("submodelId");
								String ym = detail1.getString("ym");
								if(submodel.getString("submodelId").equals(submodelId) && ym.equals(yearMonth1.getString("YM"))){
									JSONObject policyList1 = (JSONObject) detail1.getJSONArray("policyList").get(k);
									if(k >= ((JSONArray) detail1.get("policyList")).size()){
										policyName="";
										policyContent = "";
										subsidyTypeId="";
										reward = "";
									} else{
										policyName = policyList1.getString("policyName");//政策名称
										policyContent = policyList1.getString("policyContent");//政策内容
										subsidyTypeId = policyList1.getString("subsidyTypeId");//政策类型
										reward = policyList1.getString("reward");
										
										if(subsidyTypeId.equals("15")){
											sort = "◆";
											reward = "-";
										} 
									}
								
								}
							}
							
							//第一个月
							if(t==0){
								indexNum++;
								cell = row.createCell(indexNum);
								ExportExcelUtil.setCellValueAndStyle(cell,  sort , textStyle);//排序列
								
								indexNum++;
								cell = row.createCell(indexNum);
								ExportExcelUtil.setCellValueAndStyle(cell,  policyName , textStyle);//政策名称列
								
								indexNum++;
								cell = row.createCell(indexNum);
								ExportExcelUtil.setCellValueAndStyle(cell,  policyContent , textStyle2);//政策内容列
								
								
								indexNum++;
								cell = row.createCell(indexNum);
								if("".equals(reward) || "-".equals(reward)){
									ExportExcelUtil.setCellValueAndStyle(cell,  reward , textStyle);//reward促销列
								} else{
									ExportExcelUtil.setCellValueAndStyle(cell,  reward , thousandthStyle);//reward促销列
								}
							} else{
								indexNum++;
								cell = row.createCell(indexNum);
								ExportExcelUtil.setCellValueAndStyle(cell,  policyContent , textStyle2);//政策内容列
								
								
								indexNum++;
								cell = row.createCell(indexNum);
								if("".equals(reward) || "-".equals(reward)){
									ExportExcelUtil.setCellValueAndStyle(cell,  reward , textStyle);//reward促销列
								} else{
									ExportExcelUtil.setCellValueAndStyle(cell,  reward , thousandthStyle);//reward促销列
								}
							}
							
						}
					}
					
					//平均单车列
					indexNum = 0;
					row = ExportExcelUtil.createRow(sheet, ++rowIndex, 600);
					cell = row.createCell(indexNum);
					ExportExcelUtil.setCellValueAndStyle(cell, submodel.getString("submodelName"), textStyle);
					
					for(int t = 0; t < yearMonth.size(); t++){
						JSONObject yearMonth1=(JSONObject) yearMonth.get(t);
						String value = "";
						
						for(int y = 0; y < detail.size(); y++){
							JSONObject detail1=(JSONObject) detail.get(y);
							String submodelId = detail1.getString("submodelId");
							String ym = detail1.getString("ym");
							if(submodel.getString("submodelId").equals(submodelId) && ym.equals(yearMonth1.getString("YM"))){
								JSONArray policyList1 = (JSONArray) detail1.getJSONArray("policyList");
								for(int L = 0;L < policyList1.size(); L++){
									JSONObject policyList2 = policyList1.getJSONObject(L);
									if(!policyList2.getString("rewardTotal").equals("")){
										value = policyList2.getString("rewardTotal");//单车促销
										break;
									}
								}
							
							}
						}
						
						if(t == 0){
							cell = row.createCell(++indexNum);
							ExportExcelUtil.setCellValueAndStyle(cell, "", textStyle);
							
							cell = row.createCell(++indexNum);
							ExportExcelUtil.setCellValueAndStyle(cell, "单车促销", textStyle);
							
							cell = row.createCell(++indexNum);
							ExportExcelUtil.setCellValueAndStyle(cell,  "", textStyle);
							
							cell = row.createCell(++indexNum);
							if("".equals(value) || "-".equals(value)){
								ExportExcelUtil.setCellValueAndStyle(cell,  value, textStyle);//上月平均单车列
							} else{
								ExportExcelUtil.setCellValueAndStyle(cell,  Integer.toString((int)((Math.rint((double)Integer.parseInt(value)/100))*100)), thousandthStyle);//上月平均单车列
							}
						} else{
							cell = row.createCell(++indexNum);
							ExportExcelUtil.setCellValueAndStyle(cell,  "", textStyle);
							
							cell = row.createCell(++indexNum);
							if("".equals(value) || "-".equals(value)){
								ExportExcelUtil.setCellValueAndStyle(cell,  value, textStyle);//上月平均单车列
							} else{
								ExportExcelUtil.setCellValueAndStyle(cell,  Integer.toString((int)((Math.rint((double)Integer.parseInt(value)/100))*100)), thousandthStyle);//上月平均单车列
							}
						}
					}
				
					//合并第一列车型
					mergeEnd += mergeCount+1;
					if(mergeEnd != mergeBegin) sheet.addMergedRegion(new CellRangeAddress(mergeBegin,mergeEnd,0,0));
					mergeBegin = mergeEnd + 1;
				
				}
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
		List<SubModel> list = policyMonthAnalyDao.checkPopBoxData(paramsMap);
		if(null != list && 0 != list.size()) json = AppFrameworkUtil.structureConfigParamsGroupJSONData(list);
		return json;
	}
	
	/**
	 * 单车促销时居右显示
	 * @param wb
	 * @return
	 */
	public static CellStyle getExcelFillTextStyle(Workbook wb)
	{
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();   
		font.setColor(IndexedColors.BLACK.index);//设置字体颜色
		
		style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//设置单元格文本居中
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

		style.setFont(font);//设置字体
		style.setWrapText(true);//设置文本自动换行
		return style;
	}
	
	/**
	 * 获取政策内容居左显示
	 * @param wb
	 * @return
	 */
	public static CellStyle getExcelFillTextStyle1(Workbook wb)
	{
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();   
		font.setColor(IndexedColors.BLACK.index);//设置字体颜色
		
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);//设置单元格文本居中
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

		style.setFont(font);//设置字体
		style.setWrapText(false);//设置文本自动换行
		return style;
	}
	
	/**
	 * 获取政策内容居左显示字体红色
	 * @param wb
	 * @return
	 */
	public static CellStyle getExcelFillTextRedStyle(Workbook wb)
	{
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();   
		font.setColor(IndexedColors.RED.index);//设置字体颜色
		font.setBoldweight(font.BOLDWEIGHT_BOLD);//设置字体加粗
		
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);//设置单元格文本居中
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

		style.setFont(font);//设置字体
		style.setWrapText(false);//设置文本自动换行
		return style;
	}
	
	/**
	 * 获取政策内容居左显示字体蓝色
	 * @param wb
	 * @return
	 */
	public static CellStyle getExcelFillTextBlueStyle(Workbook wb)
	{
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();   
		font.setColor(IndexedColors.BLUE.index);//设置字体颜色
		
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);//设置单元格文本居中
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

		style.setFont(font);//设置字体
		style.setWrapText(false);//设置文本自动换行
		return style;
	}
	
	/**
	 * 获取政策内容居左显示字体绿色
	 * @param wb
	 * @return
	 */
	public static CellStyle getExcelFillTextGreenStyle(Workbook wb)
	{
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();   
		font.setColor(IndexedColors.GREEN.index);//设置字体颜色
		font.setBoldweight(font.BOLDWEIGHT_BOLD);//设置字体加粗
		
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);//设置单元格文本居中
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

		style.setFont(font);//设置字体
		style.setWrapText(false);//设置文本自动换行
		return style;
	}
	
	/**
	 * 获取政策内容居左显示字体绿色
	 * @param wb
	 * @return
	 */
	public static CellStyle getExcelFillTextGreenNoBoldStyle(Workbook wb)
	{
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();   
		font.setColor(IndexedColors.GREEN.index);//设置字体颜色
		
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);//设置单元格文本居中
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

		style.setFont(font);//设置字体
		style.setWrapText(false);//设置文本自动换行
		return style;
	}
	
	
	/**
	 * 格式化千分位
	 * @param strNum
	 * @returns
	 */
	public String formatNum(String strNum) {
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		//System.out.println(11122.33); //结果是11,122.33
		return numberFormat.format(Integer.parseInt(strNum));
		}
}
