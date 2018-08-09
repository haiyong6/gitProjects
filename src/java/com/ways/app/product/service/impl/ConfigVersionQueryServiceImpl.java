package com.ways.app.product.service.impl;

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
import com.ways.app.product.dao.ConfigVersionQueryDao;
import com.ways.app.product.model.ProductVersionEntity;
import com.ways.app.product.service.ConfigVersionQueryService;

/**
 * 配置型号查询Service实现类
 * @author yinlue
 *
 */
@Service("ConfigVersionQueryService")
public class ConfigVersionQueryServiceImpl implements ConfigVersionQueryService {

	@Autowired
	private ConfigVersionQueryDao dao;

	/**
	 * 加载配置型号结果集
	 */
	@Override
	public String getConfigVersionResult(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		String json = "";
		//添加SQL Where条件
		analyzeConfigContent(paramsMap);
		List<ProductVersionEntity> list = dao.getConfigVersionResult(paramsMap);
		if(null != list && 0 != list.size())
		{
			json = AppFrameworkUtil.structureConfigParamsGroupJSONData(list);
			request.getSession().setAttribute(Constant.getExportExcelDataKey, json);
		}
		return json;
	}
	
	/**
	 * 解析配置结构
	 * 
	 * 内容：id,type,value|id.....
	 * @param paramsMap
	 */
	public void analyzeConfigContent(Map<String, Object> paramsMap)
	{
		String configs = (String) paramsMap.get("configs");
		String whereSQL = "";
		
		if(!AppFrameworkUtil.isEmpty(configs))
		{
			String[] configArrs = configs.split("\\|");
			for(int i = 0; i < configArrs.length; i++ )
			{
				String[] configContent = configArrs[i].split(",");
				if("B".equals(configContent[1]))
				{
					if(0 != i){
						whereSQL += " inner join";
						whereSQL +="(select p.car_config_id, p.pzdyid from cims_peizhi p where (";
						whereSQL +="p.pzdyid ="+configContent[0]+" and p.ishave = 1 and p.valuenote = 'S'))e"+i;
						whereSQL +=" on e0.car_config_id=e"+i+".car_config_id";
					}else{
						whereSQL +="(select p.car_config_id, p.pzdyid from cims_peizhi p where (";
						whereSQL +="p.pzdyid ="+configContent[0]+" and p.ishave = 1 and p.valuenote = 'S'))e0";
					}
				}
				else
				{
					if(0 != i){
						whereSQL += " inner join";
						whereSQL +="(select p.car_config_id, p.pzdyid from cims_peizhi p where (";
						whereSQL +="p.pzdyid ="+configContent[0]+" and p.valuenote =to_char("+configContent[2]+")))e"+i;
						whereSQL +=" on e0.car_config_id=e"+i+".car_config_id";
					}else{
						whereSQL +="(select p.car_config_id, p.pzdyid from cims_peizhi p where (";
						whereSQL +="p.pzdyid ="+configContent[0]+" and p.valuenote = to_char("+configContent[2]+")))e0";
					}
					
				}
			}
			paramsMap.put("configLength", configArrs.length+"");//选择的配置个数
		}
		paramsMap.put("whereSQL", whereSQL);
	}

	/**
	 * 导出
	 */
	@Override
	public Workbook exportExcel(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		Workbook wb = new HSSFWorkbook();
		Sheet s = wb.createSheet("型号配置查询");
		String json = (String) request.getSession().getAttribute(Constant.getExportExcelDataKey);
		if(!AppFrameworkUtil.isEmpty(json))
		{
			String languageType = (String) paramsMap.get("languageType");//语言类型ZH,EN
			JSONArray dataObj = (JSONArray) JSONArray.parse(json);
			String[] titles = null;//标题数组
			if("EN".equals(languageType)) 
			{
				titles = new String[]{"Code","Segment","Manufacture","Model","Engine Capacity","Transmission","Trim","Bodytype","Launch Date","Model Year","MSRP(RMB)"
						,"Mix"};
			}
			else 
			{
				titles = new String[]{"车型编码","级别","厂商","车型","排量","排档方式","型号标识","车身形式","上市日期","年式","厂商指导价"
						,"Mix"};
			}
			int rowIndex = 0;//行号锁引
			Row row = ExportExcelUtil.createRow(s, rowIndex, 600);
			Cell cell = null;
			CellStyle titleStyle = ExportExcelUtil.getExcelTitleBackgroundStyle(wb);//表格标题样式
			CellStyle textStyle = ExportExcelUtil.getExcelFillTextStyle(wb);//内容文本样式
			CellStyle thousandthStyle = ExportExcelUtil.getExcelFormatThousandthStyle(wb);//格式化千分位样式
			CellStyle percentageStyle = ExportExcelUtil.getExcelFillPercentageStyle(wb);//格式化百分号样式
			//写表格标题
			for(int i = 0; i < titles.length; i++)
			{
				cell = row.createCell(i);
				ExportExcelUtil.setCellValueAndStyle(cell, titles[i], titleStyle);
				s.setColumnWidth(i, 4500);
			}
			//写数据
			for(int j = 0; j < dataObj.size(); j++)
			{
				row = ExportExcelUtil.createRow(s, ++rowIndex, 600);
				int cellIndex = 0;
				JSONObject obj = dataObj.getJSONObject(j);
				
				cell = row.createCell(cellIndex++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionCode")+"~", textStyle);
				
				cell = row.createCell(cellIndex++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"gradeName"), textStyle);
				
				cell = row.createCell(cellIndex++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"manfName"), textStyle);
				
				cell = row.createCell(cellIndex++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"subModelName"), textStyle);
				
				cell = row.createCell(cellIndex++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("discharge"), textStyle);
				
				cell = row.createCell(cellIndex++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"gearMode"), textStyle);
				
				cell = row.createCell(cellIndex++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"typeId"), textStyle);
				
				cell = row.createCell(cellIndex++);
				ExportExcelUtil.setCellValueAndStyle(cell, getValueByLanguageType(obj,languageType,"bodyType"), textStyle);
				
				cell = row.createCell(cellIndex++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("versionLaunchDate"), textStyle);
				
				cell = row.createCell(cellIndex++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("modelYear"), textStyle);
				
				cell = row.createCell(cellIndex++);
				ExportExcelUtil.setCellValueAndStyle(cell, obj.getString("msrp"), thousandthStyle);
				
				cell = row.createCell(cellIndex++);
				ExportExcelUtil.setCellValueToPercentage(cell, obj.getString("mix"), percentageStyle);
			}
		}
		s.setDisplayGridlines(false);
		return wb;
	}
	
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
