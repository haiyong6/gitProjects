package com.ways.app.pricesale.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.framework.utils.Constant;
import com.ways.app.framework.utils.ExportExcelUtil;
import com.ways.app.pricesale.dao.ICompetingProductDao;
import com.ways.app.pricesale.model.CompetingProductEntity;
import com.ways.app.pricesale.service.ICompetingProductManager;

/***********************************************************************************************
 * <br />价格段销量分析实现层
 * <br />Class name: VolumeByPriceRangeManagerImpl.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Apr 13, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
@SuppressWarnings("deprecation")
@Service("CompetingProductManagerImpl")
public class CompetingProductManagerImpl implements ICompetingProductManager {
	
	@Autowired
	private ICompetingProductDao competingProductDao;
	
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
		List<Map<String, String>> list = (List<Map<String, String>>) competingProductDao.initDate(paramsMap);
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
	public String getCompetingProductData(HttpServletRequest request, Map<String, Object> paramsMap)
	{
		
		//拼接sql
		String sql = "";

		for(int i = 0; i < paramsMap.get("modelIds").toString().split(",").length;i++){
			sql +=" select "+paramsMap.get("modelIds").toString().split(",")[i]+" objid,"+i+" objsn from dual ";
			if(i != paramsMap.get("modelIds").toString().split(",").length-1){
				sql +=" union all ";
			}
		}
		
		paramsMap.put("sql", sql);
		String json = "";
		Map<String, Object> saveMap = new HashMap<String, Object>();
		saveMap.putAll(paramsMap);
	
		//保存查询条件
		request.getSession().setAttribute(Constant.getCompetingProductExportMapKey, saveMap);
		
		List<CompetingProductEntity> list = competingProductDao.getCompetingProductData(paramsMap);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("objectData", list);//对比对象list
		List<CompetingProductEntity> list2 = competingProductDao.getCompetingProductSubmodelData(paramsMap);
		resultMap.put("submodelData", list2);//车型list
		
		List<CompetingProductEntity> listOrder = competingProductDao.getSubmodelListOrder(paramsMap);//排序后的车型
		resultMap.put("listOrder", listOrder);//车型list
		List<CompetingProductEntity> listObjectNames = competingProductDao.getListObjectNames(paramsMap);
		resultMap.put("listObjectNames", listObjectNames);//级别名字list
		int submodelSales = 0;
		for(CompetingProductEntity cp:list2){
			int versionSales = 0;
			if(cp.getVersionsale() != null && !"".equals(cp.getVersionsale())){
				 versionSales = Integer.parseInt(cp.getVersionsale());
			}
			
			submodelSales = submodelSales + versionSales;
		}
		resultMap.put("submodelSalesSum", submodelSales);//所有车型型号的销量和
		
		int objectSales = 0;
		for(CompetingProductEntity cp:list){
			int versionSales = 0;
			if(cp.getVersionsale() != null&&!"".equals(cp.getVersionsale())){
				 versionSales = Integer.parseInt(cp.getVersionsale());
			}
			
			objectSales = objectSales + versionSales;
		}
		resultMap.put("objectSales", objectSales);//所有对比对象型号的销量和
		
		String priceType = paramsMap.get("priceType").toString();
		
		//所有车型和对比对象在价格段内的销量和
		//指导价
		if("0".equals(priceType)){
			int temp = 0;//价格段个数
			int sprice = Integer.parseInt(paramsMap.get("sprice").toString());//起始价格
			int eprice = Integer.parseInt(paramsMap.get("eprice").toString());//结束价格
			int priceScale = Integer.parseInt(paramsMap.get("priceScale").toString());
				sprice = sprice - priceScale;
				eprice = eprice + priceScale;
			temp = (eprice-sprice)/priceScale;
			int[] arrySales = new int[temp];
			int[] arryObjectSales = new int[temp];//对比对象销量
			String[] arryScale = new String[temp];
			for(int i = 0; i < temp ; i++){
				int submodelPriceScaleVersionSales =0;
				int objectPriceScaleVersionSales =0;
				boolean flag1 = false;//判断有没有销量
				boolean flag2 = false;
				for(CompetingProductEntity cp:list){
					int msrp = 0;
					int versionsales = 0;
					if(cp.getMsrp() !=null && !"".equals(cp.getMsrp())){
						msrp = Integer.parseInt(cp.getMsrp());
					}
					//在价格段内
					if(msrp >= sprice && msrp <= sprice + priceScale){
						if(cp.getVersionsale() != null && !"".equals(cp.getVersionsale())){
							flag2 = true;
							versionsales = Integer.parseInt(cp.getVersionsale());
						}
						objectPriceScaleVersionSales = objectPriceScaleVersionSales + versionsales;
					}
				}
				for(CompetingProductEntity cp:list2){
					int msrp = 0;
					int versionsales = 0;
					if(cp.getMsrp() !=null && !"".equals(cp.getMsrp())){
						msrp = Integer.parseInt(cp.getMsrp());
					}
					//在价格段内
					if(msrp >= sprice && msrp <= sprice + priceScale){
						if(cp.getVersionsale() != null && !"".equals(cp.getVersionsale())){
							flag1 = true;
							versionsales = Integer.parseInt(cp.getVersionsale());
						}
						submodelPriceScaleVersionSales = submodelPriceScaleVersionSales +versionsales;
					}
				}
				if(!flag1){
					arrySales[i] = -1;//凡是没有销量都标识为-1
				} else{
					arrySales[i] = submodelPriceScaleVersionSales;
				}
				if(!flag2){
					arryObjectSales[i] = -1;//凡是没有销量都标识为-1
				} else{
					arryObjectSales[i] = objectPriceScaleVersionSales;
				}
				String str = Integer.toString(sprice/1000) + "k≤P" + "<" + Integer.toString((sprice + priceScale)/1000) + "k";
				arryScale[i] = str ;
				sprice = sprice + priceScale;
			}
			resultMap.put("arrySales", arrySales);//所有车型型号在价格段内的销量和数组
			resultMap.put("arryObjectSales", arryObjectSales);//所有对比对象在价格段内的销量和数组
			resultMap.put("arryScale", arryScale);//价格段数组
		} else{
			int temp = 0;//价格段个数
			int sprice = Integer.parseInt(paramsMap.get("sprice").toString());//起始价格
			int eprice = Integer.parseInt(paramsMap.get("eprice").toString());//结束价格
			int priceScale = Integer.parseInt(paramsMap.get("priceScale").toString());
				sprice = sprice - priceScale;
				eprice = eprice + priceScale;
			temp = (eprice-sprice)/priceScale;
			int[] arrySales = new int[temp];
			String[] arryScale = new String[temp];
			int[] arryObjectSales = new int[temp];//对比对象销量
			for(int i = 0; i < temp ; i++){
				int submodelPriceScaleVersionSales = 0;
				int objectPriceScaleVersionSales = 0;
				boolean flag1 = false;//判断有没有销量
				boolean flag2 = false;
				for(CompetingProductEntity cp:list){
					int tp = 0;
					int versionsales = 0;
					if(cp.getTp() !=null && !"".equals(cp.getTp())){
						tp = Integer.parseInt(cp.getTp());
					}
					//在价格段内
					if(tp >= sprice && tp <= sprice + priceScale){
						if(cp.getVersionsale() != null && !"".equals(cp.getVersionsale())){
							flag2 = true;
							versionsales = Integer.parseInt(cp.getVersionsale());
						}
						objectPriceScaleVersionSales = objectPriceScaleVersionSales +versionsales;
					}
				}
				for(CompetingProductEntity cp:list2){
					int tp = 0;
					int versionsales = 0;
					if(cp.getTp() !=null && !"".equals(cp.getTp())){
						tp = Integer.parseInt(cp.getTp());
					}
					//在价格段内
					if(tp >= sprice && tp <= sprice + priceScale){
						if(cp.getVersionsale() != null && !"".equals(cp.getVersionsale())){
							flag1 = true;
							versionsales = Integer.parseInt(cp.getVersionsale());
						}
						submodelPriceScaleVersionSales = submodelPriceScaleVersionSales +versionsales;
					}
				}
				if(!flag1){
					arrySales[i] = -1;//凡是没有销量都标识为-1
				} else{
					arrySales[i] = submodelPriceScaleVersionSales;
				}
				if(!flag2){
					arryObjectSales[i] = -1;//凡是没有销量都标识为-1
				} else{
					arryObjectSales[i] = objectPriceScaleVersionSales;
				}
				String str = Integer.toString(sprice/1000) + "k≤P" + "<" + Integer.toString((sprice + priceScale)/1000) + "k";
				arryScale[i] = str ;
				sprice = sprice + priceScale;
			}
			resultMap.put("arrySales", arrySales);//所有车型型号在价格段内的销量和数组
			resultMap.put("arryObjectSales", arryObjectSales);//所有对比对象在价格段内的销量和数组
			resultMap.put("arryScale", arryScale);//价格段数组
		}
		
		//单个车型型号的销量和
		String[] submodelNames = new String[ listOrder.size()];
		//String[] submodelIds = new String[listOrder.size()];
		int[] submodelSingleVersionSalest = new int[ listOrder.size()];
		for(int i = 0; i < listOrder.size(); i++){
			String modelId = listOrder.get(i).getObjId();
			//String submodelName = null;
			boolean flag = false;//判断销量是不是为空
			int submodelSingleVersionSales = 0;
			for(CompetingProductEntity cp:list2){
				int versionSales = 0;
				if(modelId.equals(cp.getObjId())){
					if(cp.getVersionsale() !=null && !"".equals(cp.getVersionsale())){
						flag = true;
						versionSales = Integer.parseInt(cp.getVersionsale());
						//submodelName = cp.getObjNameEn();
					}
					submodelSingleVersionSales = submodelSingleVersionSales + versionSales;
				}
			}
			if(!flag){
				submodelSingleVersionSalest[i] = -1;//凡是没有销量都标识为-1
			} else{
				submodelSingleVersionSalest[i] = submodelSingleVersionSales;
			}
			submodelNames[i] =  listOrder.get(i).getObjNameEn();
		}
		resultMap.put("submodelNames", submodelNames);//车型英文名字
		resultMap.put("submodelSingleVersionSalest", submodelSingleVersionSalest);//单个车型型号的销量和
		
		//单个车型型号在价格段内的销量和
		//指导价
				if("0".equals(priceType)){
					
					int[][] arrySalesSinglePriceScales = new int[listOrder.size()][];//单个车型型号在价格段内的销量和
					for(int k = 0; k <  listOrder.size(); k++){
						int temp = 0;//价格段个数
						int sprice = Integer.parseInt(paramsMap.get("sprice").toString());//起始价格
						int eprice = Integer.parseInt(paramsMap.get("eprice").toString());//结束价格
						int priceScale = Integer.parseInt(paramsMap.get("priceScale").toString());
							sprice = sprice - priceScale;
							eprice = eprice + priceScale;
						temp = (eprice-sprice)/priceScale;
						int[] arrySales = new int[temp];
						String modelId =  listOrder.get(k).getObjId();
						for(int i = 0; i < temp ; i++){
							boolean flag = false;//判断是不是为空
							Integer submodelPriceScaleVersionSales = 0;
							for(CompetingProductEntity cp:list2){
								int msrp = 0;
								Integer versionsales = 0;
								if(cp.getMsrp() !=null && !"".equals(cp.getMsrp())){
									msrp = Integer.parseInt(cp.getMsrp());
								}
								
								if(modelId.equals(cp.getObjId())){
									//在价格段内
									if(msrp >= sprice && msrp <= sprice + priceScale){
										if(cp.getVersionsale() != null && !"".equals(cp.getVersionsale())){
											flag=true;
											versionsales = Integer.parseInt(cp.getVersionsale());
										}
										submodelPriceScaleVersionSales = submodelPriceScaleVersionSales +versionsales;
									}
								}
								
							}
							if(!flag){
								arrySales[i] =-1;//凡是没有销量都标识为-1
							} else{
								arrySales[i] = submodelPriceScaleVersionSales;
							}
							sprice = sprice + priceScale;
						}
						
						arrySalesSinglePriceScales[k] = arrySales;
						
					}
					resultMap.put("arrySalesSinglePriceScales", arrySalesSinglePriceScales);//单个车型型号在价格段内的销量和
				} else{
					
					int[][] arrySalesSinglePriceScales = new int[listOrder.size()][];//单个车型型号在价格段内的销量和
					for(int k = 0; k < listOrder.size(); k++){
						int temp = 0;//价格段个数
						int sprice = Integer.parseInt(paramsMap.get("sprice").toString());//起始价格
						int eprice = Integer.parseInt(paramsMap.get("eprice").toString());//结束价格
						int priceScale = Integer.parseInt(paramsMap.get("priceScale").toString());
							sprice = sprice - priceScale;
							eprice = eprice + priceScale;
						temp = (eprice-sprice)/priceScale;
						int[] arrySales = new int[temp];
						String modelId =  listOrder.get(k).getObjId();
						for(int i = 0; i < temp ; i++){
							boolean flag = false;//判断是不是为空
							Integer submodelPriceScaleVersionSales = 0;
							for(CompetingProductEntity cp:list2){
								int tp = 0;
								Integer versionsales = 0;
								if(cp.getTp() !=null && !"".equals(cp.getTp())){
									tp = Integer.parseInt(cp.getTp());
								}
								if(modelId.equals(cp.getObjId())){
									//在价格段内
									if(tp >= sprice && tp <= sprice + priceScale){
										if(cp.getVersionsale() != null && !"".equals(cp.getVersionsale())){
											flag = true;
											versionsales = Integer.parseInt(cp.getVersionsale());
										}
										submodelPriceScaleVersionSales = submodelPriceScaleVersionSales +versionsales;
									}
								}
								
							}
							if(!flag){
								arrySales[i] = -1;//凡是没有销量都标识为-1
							} else{
								arrySales[i] = submodelPriceScaleVersionSales;
							}
							sprice = sprice + priceScale;
						}
						arrySalesSinglePriceScales[k] = arrySales;
					}
					resultMap.put("arrySalesSinglePriceScales", arrySalesSinglePriceScales);//单个车型型号在价格段内的销量和
				}
		resultMap.put("beginDate", paramsMap.get("beginDate").toString());//开始查询时间
		resultMap.put("endDate", paramsMap.get("endDate").toString());//结束查询时间
		json = AppFrameworkUtil.structureConfigParamsGroupJSONData(resultMap);
		/*}*/
		//保存图形JSON数据
		request.getSession().setAttribute(Constant.getCompetingProductExportExcelDataKey, json);
		return json;
	}
	
	/**
	 * 查询级别对应名称
	 * 
	 * @param gradeId
	 * @return
	 */
//	private String getGradeName(String gradeId) 
//	{
//	    return competingProductDao.getGradeName(gradeId);
//	}
//	
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
	    		request.setAttribute("segmentList", competingProductDao.getSubmodelBySegment(paramsMap));
	    	} else if("3".equals(subModelShowType)) {
	    		//返回品牌页
		    	request.setAttribute("brandLetterList", competingProductDao.getSubmodelByBrand(paramsMap));
	    	} else if("4".equals(subModelShowType)) {
	    		//返回厂商页
		    	request.setAttribute("manfLetterList", competingProductDao.getSubmodelByManf(paramsMap));
	    	} else {
	    		//本品页竟品页
		    	request.setAttribute("bpSubModelList", competingProductDao.getSubmodelByBp(paramsMap));
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
	@SuppressWarnings({ "unchecked" })
	@Override
	public Workbook exportExcel(HttpServletRequest request, Map<String, Object> paramsMap) 
	{
		//获取保存条件
		HashMap<String, Object> conditionMap = (HashMap<String, Object>) request.getSession().getAttribute("getCompetingProductExportMapKey");
		conditionMap.putAll(paramsMap);
		//int sPrice = Integer.parseInt(conditionMap.get("sprice").toString()) / 10000;
		//int ePrice = Integer.parseInt(conditionMap.get("eprice").toString()) / 10000;
		String languageType = conditionMap.get("languageType").toString();
		String objectType = conditionMap.get("objectType").toString();
		String isScale = conditionMap.get("isScale").toString();//页面占比选项是否勾选，true表示是，false表示否
		//获取图形JSON
		String json = (String) request.getSession().getAttribute(Constant.getCompetingProductExportExcelDataKey);
		JSONObject obj = JSONObject.fromObject(json);
		JSONArray arryScale = obj.getJSONArray("arryScale");//价格段数组
		JSONArray arryObjectSales = obj.getJSONArray("arryObjectSales");//所有对比对象在价格段内的销量和
		JSONArray arrySales = obj.getJSONArray("arrySales");//所有车型在价格段内的销量和
		JSONArray submodelNames = obj.getJSONArray("submodelNames");//车型的英文名字
		JSONArray arrySalesSinglePriceScales = obj.getJSONArray("arrySalesSinglePriceScales");//单个车型在价格段内的销量和
		
		String objectSales = obj.getString("objectSales");//所有对比对象的销量和
		String beginDate = obj.getString("beginDate");//查询开始时间
		String endDate = obj.getString("endDate");//查询结束时间
		String submodelSalesSum = obj.getString("submodelSalesSum");//所有车型的销量和
		JSONArray submodelSingleVersionSalest = obj.getJSONArray("submodelSingleVersionSalest");//单个车型的销量和
		JSONArray listObjectNames = obj.getJSONArray("listObjectNames");//级别名字list
		
		String name = "";
		for(int i = 0; i < listObjectNames.size(); i++){
			JSONObject obj1=(JSONObject) listObjectNames.get(i);
			name += obj1.getString("objNameEn") +"&" ;
		}
		String objectNames = null;
		if("ZH".equals(languageType)){
			if("0".equals(objectType)){
				objectNames = "对比级别(" + name.substring(0,name.length()-1) +")";
			} else if("1".equals(objectType)){
				objectNames = "对比系别(" + name.substring(0,name.length()-1) +")";
			} else if("2".equals(objectType)){
				objectNames = "对比品牌(" + name.substring(0,name.length()-1) +")";
			} else{
				objectNames = "对比厂商品牌(" + name.substring(0,name.length()-1) +")";
			}
			
		} else{
			if("0".equals(objectType)){
				objectNames = "Grade(" + name.substring(0,name.length()-1) +")";
			} else if("1".equals(objectType)){
				objectNames = "Orig(" + name.substring(0,name.length()-1) +")";
			} else if("2".equals(objectType)){
				objectNames = "Brand(" + name.substring(0,name.length()-1) +")";
			} else{
				objectNames = "Manf(" + name.substring(0,name.length()-1) +")";
			}
		}
				
		Workbook wb = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/"); 
			wb = new HSSFWorkbook(new FileInputStream(new File(path + "excelExample/competingProductTemplate.xls")));
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
		CellStyle titleStyle1 = getExcelTitleBackgroundSpecialStyle(wb);//表格标题样式1，导出excell图的标题
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle textStyle1 = getExcelFillTextStyle(wb);//内容文本样式，边框为白色
		CellStyle persentStyle = getExcelFillPercentageStyle(wb);//格式化百分比，保留一位小数，左边框实线，上下虚线
		CellStyle persentStyle1 = getExcelFillPercentageStyle1(wb);//格式化百分比,保留两位小数
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		CellStyle thousandthStyle1 = getExcelFormatThousandthStyle(wb);//格式化千分位样式不带边框
//		CellStyle textStyle2 = getExcelFillTextStyle2(wb);//内容文本样式2，没有边框
		/**
		 * 标题横坐标
		 */
		row = Datasheet.createRow(0);
		createStyleCell(row, titleStyle, "", HSSFCell.ENCODING_UTF_16, 0);
		for (int i = 0; i < arryScale.size(); i++) {
			Datasheet.setColumnWidth(i + 1, 3000);
			createStyleCell(row, titleStyle, arryScale.getString((i)), HSSFCell.ENCODING_UTF_16, i+1);
		}
		
		/**
		 * 对比对象行
		 */
		row = Datasheet.createRow(1);
		
		createStyleCell(row, textStyle, objectNames, HSSFCell.ENCODING_UTF_16, 0);
		for (int i = 0; i < arryObjectSales.size(); i++) {
			Datasheet.setColumnWidth(i + 1, 3000);
			cell = row.createCell(i+1);
			cell.setCellStyle(textStyle);
			if(Integer.parseInt(arryObjectSales.getString(i)) > 0 ) {
				cell.setCellValue(Integer.parseInt(arryObjectSales.getString(i)));
				if(Integer.parseInt(arryObjectSales.getString(i))!=0) {
					cell.setCellStyle(thousandthStyle);
				}
			} else {
				cell.setCellValue(0);
			}
		}
		
		/**
		 * 车型行
		 */
		int rowSubmodel = 2;//车型行位置
		//if("false".equals(isScale)){
			row = Datasheet.createRow(rowSubmodel);
			
			String sn = null;
			if("ZH".equals(languageType)){
				sn = "车型";
			} else{
				sn = "Submodel";
			}
			createStyleCell(row, textStyle, sn, HSSFCell.ENCODING_UTF_16, 0);
			for (int i = 0; i < arrySales.size(); i++) {
				Datasheet.setColumnWidth(i + 1, 3000);
				cell = row.createCell(i+1);
				cell.setCellStyle(textStyle);
				if(Integer.parseInt(arrySales.getString(i)) > 0 ) {
					cell.setCellValue(Integer.parseInt(arrySales.getString(i)));
					if(Integer.parseInt(arrySales.getString(i))!=0) {
						cell.setCellStyle(thousandthStyle);
					}
				} else {
					cell.setCellValue(0);
				}
			}
			rowSubmodel = 3;
		//} 
		
	
		
		/**
		 * 各段数据值
		 */
		for (int i = 0; i < submodelNames.size(); i++) {
//			JSONArray data = null;
			JSONArray obj1 = (JSONArray) arrySalesSinglePriceScales.get(i);
			row = Datasheet.createRow(i+rowSubmodel);
			for (int j = 0; j < arryScale.size(); j++) {
				if(j == 0) {
					createStyleCell(row, textStyle, submodelNames.getString(i), HSSFCell.ENCODING_UTF_16, 0);
				}
					Datasheet.setColumnWidth(j + 1, 3000);
					cell = row.createCell(j+1);
					cell.setCellStyle(textStyle);
					if(Integer.parseInt(obj1.getString(j)) > 0 ) {
						cell.setCellValue(Integer.parseInt(obj1.getString(j)));
						if(Integer.parseInt(obj1.getString(j)) != 0) {
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
		
		HSSFRow chartRow1 = chartSheet.createRow(arrySales.size()+4);
		HSSFRow chartRow2 = chartSheet.createRow(arrySales.size()+5);
		HSSFRow chartRow3 = chartSheet.createRow(arrySales.size()+6);
		
		HSSFCell chartCell3 = chartRow3.createCell(0);
		chartCell3.setCellValue(beginDate.substring(0,4)+"年"+beginDate.substring(5,7)+"月"+"至"+endDate.substring(0,4)+"年"+endDate.substring(5,7)+"月");
		HSSFCell chartCell1 = chartRow1.createCell(3);
		chartCell1.setCellValue(Integer.parseInt(objectSales));
		chartCell1.setCellStyle(thousandthStyle1);
		chartCell1 = chartRow1.createCell(2);
		chartCell1.setCellStyle(textStyle1);
		if("EN".equals(languageType)){
			if("0".equals(objectType)){
				chartCell1.setCellValue(objectNames.substring(0,5));//grade
			} else if("1".equals(objectType)){
				chartCell1.setCellValue(objectNames.substring(0,4));//orig
			} else if("2".equals(objectType)){
				chartCell1.setCellValue(objectNames.substring(0,5));//brand
			} else if("3".equals(objectType)){
				chartCell1.setCellValue(objectNames.substring(0,4));//manf
			}
		} else{
		 if("3".equals(objectType)){
				chartCell1.setCellValue(objectNames.substring(0,6));//对比厂商品牌
			} else{
				chartCell1.setCellValue(objectNames.substring(0,4));
			}
		}
		int num = 4;
		//100%列补空
		if("true".equals(isScale)){
			chartCell1 = chartRow1.createCell(num);
			
			num = 6;
		}
		//单个车型的销量和写入
		for (int i = 0; i < submodelSingleVersionSalest.size(); i++) {
			if(i==0){
				chartCell1 = chartRow1.createCell(i+num);
			} else{
				chartCell1 = chartRow1.createCell(i+i+num);
			}
			
			if(Integer.parseInt(submodelSingleVersionSalest.getString(i)) > 0 ) {
				chartCell1.setCellValue(Integer.parseInt(submodelSingleVersionSalest.getString(i)));
				if(Integer.parseInt(submodelSingleVersionSalest.getString(i))!=0) {
					chartCell1.setCellStyle(thousandthStyle1);
				}
			} else {
				chartCell1.setCellValue(0);
				chartCell1.setCellStyle(textStyle1);
			}
		}
		//车型行
			HSSFCell chartCell2 = chartRow2.createCell(3);
			if(Integer.parseInt(submodelSalesSum) > 0 ) {
				chartCell2.setCellValue(Integer.parseInt(submodelSalesSum));
				if(Integer.parseInt(submodelSalesSum)!=0) {
					chartCell2.setCellStyle(thousandthStyle1);
				}
			} else {
				chartCell2.setCellValue(0);
				chartCell2.setCellStyle(textStyle1);
			}
			chartCell2 = chartRow2.createCell(2);
			chartCell2.setCellStyle(textStyle1);
			if("EN".equals(languageType)){
				chartCell2.setCellValue("Submodel");
			} else{
				chartCell2.setCellValue("对比车型");
			}
			
			 num = 4;
			//100%列补空
			if("true".equals(isScale)){
				chartCell1 = chartRow1.createCell(num);
				num = 6;
			}
			//单个车型销量和占所有级别销量和的百分比
			for(int k = 0; k < submodelSingleVersionSalest.size(); k++){
				submodelSingleVersionSalest.getInt(k);
				if(k==0){
					chartCell2 = chartRow2.createCell(k+num);
				} else{
					chartCell2 = chartRow2.createCell(k+k+num);
				}
				chartCell2.setCellStyle(persentStyle1);
				if(submodelSingleVersionSalest.getInt(k) > 0 ) {
					if(Integer.parseInt(objectSales)>0){
						//结果保留两位小数
						chartCell2.setCellValue((double)submodelSingleVersionSalest.getInt(k)/(double)Integer.parseInt(objectSales));
					}
				} else {
					chartCell2.setCellValue(0);
				}
			}
			
		//右边图表显示
			HSSFRow row1= chartSheet.createRow(1);

			//车型名字
			HSSFCell submodleCell = null;
					//当占比选项勾选时显示100%列
			int beginNum = 4;
			if("true".equals(isScale)){
				submodleCell = row1.createCell(beginNum);
				submodleCell.setCellValue("100%");
				submodleCell.setCellStyle(titleStyle1);
				//合并单元格,四个参数分别为，行，行，列，列
				chartSheet.addMergedRegion(new CellRangeAddress(1,2,beginNum,beginNum));
				beginNum = 6;
			}
			for(int i =0;i < submodelNames.size(); i++ ){
					submodleCell = row1.createCell(beginNum+i+i);
				submodleCell.setCellValue(submodelNames.getString(i));
				submodleCell.setCellStyle(titleStyle1);
				//合并单元格,四个参数分别为，行，行，列，列
				chartSheet.addMergedRegion(new CellRangeAddress(1,2,beginNum+i+i,beginNum+i+i));
			}
			//下面的百分比
			//单个车型在价格段内的销量和倒叙排列
			int[][] arry = new int[submodelNames.size()][arryScale.size()];
			for(int i =0; i<submodelNames.size();i++){
				JSONArray obj1 = (JSONArray) arrySalesSinglePriceScales.get(i);
				for (int j = 0; j < arryScale.size(); j++) {
					arry[i][j]=obj1.getInt(arryScale.size()-1-j);
				}
			}
				//所有车型在价格段内的销量和倒叙排列
			int[] arryAllSubmodelScaleSalesDao = new int[arrySales.size()]; 
				for(int i = 0; i < arrySales.size();i++){
					//JSONArray obj1 = (JSONArray) arrySales.get(i);
					//int value = 0;
					arryAllSubmodelScaleSalesDao[i] = arrySales.getInt(arrySales.size()-1-i);
				}
				//价格段内级别销量倒叙排列
				int[] arryObjectSalesDao = new int[arryObjectSales.size()];
				int arryObjectSalesTotal = 0;//各级别价格段销量之和
				for(int i = 0; i < arryObjectSales.size(); i++){
					arryObjectSalesDao[i] = arryObjectSales.getInt(arryObjectSales.size()-(i+1));
					int value = 0;
					if(arryObjectSalesDao[i] >=0){
						value = arryObjectSalesDao[i] ;
					} 
					arryObjectSalesTotal = value + arryObjectSalesTotal;
				}
			for(int i = 0;i < arrySales.size(); i++){
				row1= chartSheet.createRow(3+i);
				 submodleCell = null;
				 beginNum = 4;
				 //100%列写数据(价格段内级别销量与所有级别的总销量的百分比)
				 if("true".equals(isScale)){
					 submodleCell = row1.createCell(beginNum);
						submodleCell.setCellStyle(persentStyle);
					double value =	(double)arryObjectSalesDao[i];
					double value1 = (double)arryObjectSalesTotal;
						if(value >=0 && value1 > 0){
							submodleCell.setCellValue(value/value1);
						}
						beginNum = 6;
				 }
				for(int k = 0;k < arry.length; k++){
					submodleCell = row1.createCell(beginNum+k+k);
					submodleCell.setCellStyle(persentStyle);
					double value = (double)arry[k][i];
					double value2 = (double)submodelSingleVersionSalest.getInt(k);
					double value3 = (double)arryAllSubmodelScaleSalesDao[i];//单个车型在价格段内的销量和与所有车型在价格段内的销量和的百分比
					
					if("true".equals(isScale)){
						if(value>=0&&value3>0){
							submodleCell.setCellValue(value/value3);
						} 
					} else{
						if(value>=0&&value2>0){
							submodleCell.setCellValue(value/value2);
						}
					}
				}
			}
			
		/**
		 * 图表显示Sheet页  --end
		 */
		
		/**
		 * 数据显示Sheet页  --start
		 */
		List<CompetingProductEntity> objectList = competingProductDao.exportCompetingProductObjectData(conditionMap);
		List<CompetingProductEntity> submodelList = competingProductDao.exportCompetingProductSubmodelData(conditionMap);
		exportSubmodelMultiSel(wb,request,conditionMap,submodelList);//车型导出
		exportObjectMultiSel(wb, request, conditionMap,objectList);//对比对象导出
		/**
		 * 数据显示Sheet页  --end
		 */
		return wb;
	}
	
	/**
	 * 车型导出
	 * @param wb
	 * @param request
	 * @param conditionMap
	 * @param submodelList
	 */
	public void exportSubmodelMultiSel(Workbook wb,HttpServletRequest request, HashMap<String, Object> conditionMap,List<CompetingProductEntity> submodelList) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.putAll(conditionMap);
		HSSFSheet sheet = null;
		HSSFRow row = null;
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		
		String languageType = paramsMap.get("languageType").toString();
		String priceType = paramsMap.get("priceType").toString();
		String isScale = paramsMap.get("isScale").toString();//占比勾选情况，true为打钩，false为不打勾。
//		String objectType = paramsMap.get("objectType").toString();
				
		String legend = "对比车型销量";
		
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
					createStyleCell(row, titleStyle, "型号编码", HSSFCell.ENCODING_UTF_16, 0);
					createStyleCell(row, titleStyle, "型号名称", HSSFCell.ENCODING_UTF_16, 1);
					createStyleCell(row, titleStyle, "车型名称", HSSFCell.ENCODING_UTF_16, 2);
					createStyleCell(row, titleStyle, "销量", HSSFCell.ENCODING_UTF_16, 3);
					
					if("0".equals(priceType)) {
						createStyleCell(row, titleStyle, "指导价", HSSFCell.ENCODING_UTF_16, 4);
					} else {
						createStyleCell(row, titleStyle, "成交价", HSSFCell.ENCODING_UTF_16, 4);
					}
				} else {
					createStyleCell(row, titleStyle, "VersionCode", HSSFCell.ENCODING_UTF_16, 0);
					createStyleCell(row, titleStyle, "Version", HSSFCell.ENCODING_UTF_16, 1);
					createStyleCell(row, titleStyle, "SubmodelName", HSSFCell.ENCODING_UTF_16, 2);
					createStyleCell(row, titleStyle, "Sales", HSSFCell.ENCODING_UTF_16, 3);
					if("0".equals(priceType)) {
						createStyleCell(row, titleStyle, "MSRP", HSSFCell.ENCODING_UTF_16, 4);
					} else {
						createStyleCell(row, titleStyle, "TP", HSSFCell.ENCODING_UTF_16, 4);
					}
				}
				
				//设置占比标识，当占比打钩时为true，当占比不打勾时为false
				/***
				 * 20170214 ；应客户需求，占比勾不勾选都要显示车型柱子所以这里改为始终为false，为保证导出宏出图时不隐藏车型。
				 */
				
				//createStyleCell(row, textStyle, isScale, HSSFCell.ENCODING_UTF_16, 20);
				createStyleCell(row, textStyle, "false", HSSFCell.ENCODING_UTF_16, 20);
				
				for (int k = 0; k < submodelList.size(); k++) {
					String versionsale = null;
					if(null != submodelList.get(k).getVersionsale() && !"".equals(submodelList.get(k).getVersionsale())){
						versionsale = submodelList.get(k).getVersionsale();
					} else{
						versionsale = "-";
					}
					
					String Msrp = null;
					if(null != submodelList.get(k).getMsrp() && !"".equals(submodelList.get(k).getMsrp())){
						Msrp = submodelList.get(k).getMsrp();
					} else{
						Msrp = "-";
					}
					
					String Tp = null;
					if(null != submodelList.get(k).getTp() && !"".equals(submodelList.get(k).getTp())){
						Tp = submodelList.get(k).getTp();
					} else{
						Tp = "-";
					}
					row = sheet.createRow(k + 1);
					createStyleCell(row, textStyle, submodelList.get(k).getVersionCode(), HSSFCell.ENCODING_UTF_16, 0);
					if("ZH".equals(languageType)) {
						createStyleCell(row, textStyle, submodelList.get(k).getVersionName(), HSSFCell.ENCODING_UTF_16, 1);
						createStyleCell(row, textStyle, submodelList.get(k).getObjName(), HSSFCell.ENCODING_UTF_16, 2);
					} else {
						createStyleCell(row, textStyle, submodelList.get(k).getVersionNameEn(), HSSFCell.ENCODING_UTF_16, 1);
						createStyleCell(row, textStyle, submodelList.get(k).getObjNameEn(), HSSFCell.ENCODING_UTF_16, 2);
					}
					
					if("-".equals(versionsale)){
						createStyleCell(row, textStyle, versionsale, HSSFCell.ENCODING_UTF_16, 3);
					} else if(0 == Integer.parseInt(versionsale)){
						createStyleCell(row, textStyle, Integer.parseInt(versionsale), HSSFCell.ENCODING_UTF_16, 3);
					} else if(Integer.parseInt(versionsale)>0){
						createStyleCell(row, thousandthStyle, Integer.parseInt(versionsale), HSSFCell.ENCODING_UTF_16, 3);
					} else if(Integer.parseInt(versionsale)<0){
						createStyleCell(row, thousandthStyle, Integer.parseInt(versionsale), HSSFCell.ENCODING_UTF_16, 3);
					}

					if("0".equals(priceType)) {
						if("-".equals(Msrp)){
							createStyleCell(row, textStyle, Msrp, HSSFCell.ENCODING_UTF_16, 4);
						} else if(0==Integer.parseInt(Msrp)){
							createStyleCell(row, thousandthStyle,0, HSSFCell.ENCODING_UTF_16, 4);
						} else{
							createStyleCell(row, thousandthStyle,Integer.parseInt(Msrp), HSSFCell.ENCODING_UTF_16, 4);
						}
						
					} else {
						if("-".equals(Tp)){
							createStyleCell(row, textStyle, Tp, HSSFCell.ENCODING_UTF_16, 4);
						} else{
							createStyleCell(row, thousandthStyle, Integer.parseInt(Tp), HSSFCell.ENCODING_UTF_16, 4);
						}
					}
					
				}
		
	}

	/**
	* 对比对象导出
	* 
	* @param wb
	* @param request
	* @param conditionMap    
	* @param languageType
	*/
	public void exportObjectMultiSel(Workbook wb, HttpServletRequest request, Map<String, Object> conditionMap,List<CompetingProductEntity> objectList)
	{
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.putAll(conditionMap);
		HSSFSheet sheet = null;
		HSSFRow row = null;
		CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
		CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
		CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
		
		String languageType = paramsMap.get("languageType").toString();
		String priceType = paramsMap.get("priceType").toString();
		String objectType = paramsMap.get("objectType").toString();
				
		String legend =null;
		if("0".equals(objectType)){
			legend = "对比级别销量";
		} else if("1".equals(objectType)){
			legend = "对比系别销量";
		} else if("2".equals(objectType)){
			legend = "对比品牌销量";
		} else{
			legend = "对比厂商品牌销量";
		}
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
					createStyleCell(row, titleStyle, "型号编码", HSSFCell.ENCODING_UTF_16, 0);
					createStyleCell(row, titleStyle, "型号名称", HSSFCell.ENCODING_UTF_16, 1);
					createStyleCell(row, titleStyle, "销量", HSSFCell.ENCODING_UTF_16, 2);
					
					if("0".equals(priceType)) {
						createStyleCell(row, titleStyle, "指导价", HSSFCell.ENCODING_UTF_16, 3);
					} else {
						createStyleCell(row, titleStyle, "成交价", HSSFCell.ENCODING_UTF_16, 3);
					}
				} else {
					createStyleCell(row, titleStyle, "VersionCode", HSSFCell.ENCODING_UTF_16, 0);
					createStyleCell(row, titleStyle, "Version", HSSFCell.ENCODING_UTF_16, 1);
					createStyleCell(row, titleStyle, "Sales", HSSFCell.ENCODING_UTF_16, 2);
					if("0".equals(priceType)) {
						createStyleCell(row, titleStyle, "MSRP", HSSFCell.ENCODING_UTF_16, 3);
					} else {
						createStyleCell(row, titleStyle, "TP", HSSFCell.ENCODING_UTF_16, 3);
					}
				}
				
				for (int k = 0; k < objectList.size(); k++) {
					String versionsale = null;
					if(null != objectList.get(k).getVersionsale() && !"".equals(objectList.get(k).getVersionsale())){
						versionsale = objectList.get(k).getVersionsale();
					} else{
						versionsale = "-";
					}
					
					String Msrp = null;
					if(null != objectList.get(k).getMsrp() && !"".equals(objectList.get(k).getMsrp())){
						Msrp = objectList.get(k).getMsrp();
					} else{
						Msrp = "-";
					}
					
					String Tp = null;
					if(null != objectList.get(k).getTp() && !"".equals(objectList.get(k).getTp())){
						Tp = objectList.get(k).getTp();
					} else{
						Tp = "-";
					}
					row = sheet.createRow(k + 1);
					
						createStyleCell(row, textStyle, objectList.get(k).getVersionCode(), HSSFCell.ENCODING_UTF_16, 0);
					
					if("ZH".equals(languageType)) {
						createStyleCell(row, textStyle, objectList.get(k).getVersionName(), HSSFCell.ENCODING_UTF_16, 1);
					} else {
						createStyleCell(row, textStyle, objectList.get(k).getVersionNameEn(), HSSFCell.ENCODING_UTF_16, 1);
					}
					
					if("-".equals(versionsale)){
						createStyleCell(row, textStyle, versionsale, HSSFCell.ENCODING_UTF_16, 2);
					} else if(0 == Integer.parseInt(versionsale)){
						createStyleCell(row, textStyle, Integer.parseInt(versionsale), HSSFCell.ENCODING_UTF_16, 2);
					} else if(Integer.parseInt(versionsale)>0){
						createStyleCell(row, thousandthStyle, Integer.parseInt(versionsale), HSSFCell.ENCODING_UTF_16, 2);
					} else if(Integer.parseInt(versionsale)<0){
						createStyleCell(row, thousandthStyle, Integer.parseInt(versionsale), HSSFCell.ENCODING_UTF_16, 2);
					}
					
					if("0".equals(priceType)) {
						if("-".equals(Msrp)){
							createStyleCell(row, textStyle, Msrp, HSSFCell.ENCODING_UTF_16, 3);
						} else{
							createStyleCell(row, thousandthStyle, Integer.parseInt(Msrp), HSSFCell.ENCODING_UTF_16, 3);
						}
					} else {
						if("-".equals(Tp)){
							createStyleCell(row, textStyle, Tp, HSSFCell.ENCODING_UTF_16, 3);
						} else{
							createStyleCell(row, thousandthStyle, Integer.parseInt(Tp), HSSFCell.ENCODING_UTF_16, 3);
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
	
	/**
	 * 获取Excel填充文本样式没有边框，边框为白色
	 * @param wb
	 * @return
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
		
		//style.BORDER_NONE;
		//设置边框颜色
		style.setBorderLeft(CellStyle.BORDER_THIN);   
		style.setLeftBorderColor(IndexedColors.WHITE.getIndex());//设置左边框颜色
		style.setBorderBottom(CellStyle.BORDER_THIN);   
		style.setBottomBorderColor(IndexedColors.WHITE.getIndex());//设置下边框颜色
		style.setBorderRight(CellStyle.BORDER_THIN);   
		style.setRightBorderColor(IndexedColors.WHITE.getIndex());//设置右边框颜色
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.WHITE.getIndex());//设置上边框颜色

		style.setFont(font);//设置字体
		style.setWrapText(true);//设置文本自动换行
		return style;
	}
	
	/**
	 * 获取Excel填充文本样式2 
	 * @param wb
	 * @return
	 */
	public static CellStyle getExcelFillTextStyle2(Workbook wb)
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
		
		//style.BORDER_NONE;
		//设置边框颜色
		style.setBorderTop(HSSFCellStyle.BORDER_NONE);
		style.setBorderLeft(HSSFCellStyle.BORDER_NONE);   
		//style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());//设置左边框颜色
		style.setBorderBottom(HSSFCellStyle.BORDER_NONE);   
		//style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());//设置下边框颜色
		style.setBorderRight(HSSFCellStyle.BORDER_NONE);   
		//style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());//设置右边框颜色

		style.setFont(font);//设置字体
		style.setWrapText(true);//设置文本自动换行
		return style;
	}
	
	/**
	 * 获取Excel格式化千分号样式
	 * @param wb
	 * @return
	 */
	public static CellStyle getExcelFormatThousandthStyle(Workbook wb)
	{
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();   
		font.setColor(IndexedColors.BLACK.index);//设置字体颜色
		style.setFont(font);//设置字体
		
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//设置单元格文本左对齐
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中  
		
		//设置背景颜色
		HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		customPalette.setColorAtIndex(HSSFColor.GREY_25_PERCENT.index, (byte) 214, (byte) 214, (byte) 214); 
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		
		//设置边框颜色
		style.setBorderLeft(CellStyle.BORDER_THIN);   
		style.setLeftBorderColor(IndexedColors.WHITE.getIndex());//设置左边框颜色
		style.setBorderBottom(CellStyle.BORDER_THIN);   
		style.setBottomBorderColor(IndexedColors.WHITE.getIndex());//设置下边框颜色
		style.setBorderRight(CellStyle.BORDER_THIN);   
		style.setRightBorderColor(IndexedColors.WHITE.getIndex());//设置右边框颜色
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.WHITE.getIndex());//设置上边框颜色
		HSSFDataFormat df = (HSSFDataFormat) wb.createDataFormat();      
		style.setDataFormat(df.getFormat("###,#"));//设置单元格数据格式
		return style;
	}
	
	/**
	 * 获取Excel标题背景样式1 excell导出图
	 * @param wb
	 * @return
	 */
	public static CellStyle getExcelTitleBackgroundSpecialStyle(Workbook wb)
	{
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();   
		font.setColor(IndexedColors.WHITE.index);//设置字体颜色
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setCharSet(Font.DEFAULT_CHARSET);
		
		style.setBorderLeft(CellStyle.SOLID_FOREGROUND);   
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());//设置左边框颜色
		
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//设置单元格文本左对齐
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中  
		//#4682B4
		HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		customPalette.setColorAtIndex((short)13, (byte) 59, (byte) 122, (byte) 178); 
		//设置背景颜色
		style.setFillForegroundColor((short)13);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style.setFont(font);//设置字体
		return style;
	}
	
	/**
	 * 获取Excel填充数百分号样式，保留一位小数
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
		style.setBorderBottom(HSSFCellStyle.BORDER_DASHED);   
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());//设置下边框颜色
		style.setBorderRight(HSSFCellStyle.BORDER_NONE);   
		//style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());//设置右边框颜色

		HSSFDataFormat df = (HSSFDataFormat) wb.createDataFormat();      
		style.setDataFormat(df.getFormat("0.0%"));//设置单元格数据格式
		return style;
	}
	
	/**
	 * 获取Excel填充数百分号样式，保留两位小数
	 * @param wb
	 * @return
	 */
	public static CellStyle getExcelFillPercentageStyle1(Workbook wb)
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
		//设置边框颜色
		//设置边框颜色
				style.setBorderLeft(CellStyle.BORDER_THIN);   
				style.setLeftBorderColor(IndexedColors.WHITE.getIndex());//设置左边框颜色
				style.setBorderBottom(CellStyle.BORDER_THIN);   
				style.setBottomBorderColor(IndexedColors.WHITE.getIndex());//设置下边框颜色
				style.setBorderRight(CellStyle.BORDER_THIN);   
				style.setRightBorderColor(IndexedColors.WHITE.getIndex());//设置右边框颜色
				style.setBorderTop(CellStyle.BORDER_THIN);
				style.setTopBorderColor(IndexedColors.WHITE.getIndex());//设置上边框颜色
		HSSFDataFormat df = (HSSFDataFormat) wb.createDataFormat();      
		style.setDataFormat(df.getFormat("0.00%"));//设置单元格数据格式
		return style;
	}
}
