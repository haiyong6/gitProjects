package com.ways.app.product.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * 配置型号查询Service层接口
 * @author yinlue
 *
 */
public interface ConfigVersionQueryService {

	/**
	 * 加载配置型号结果集
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public String getConfigVersionResult(HttpServletRequest request,Map<String, Object> paramsMap);
	
	
	/**
	 * 导出
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public Workbook exportExcel(HttpServletRequest request,Map<String, Object> paramsMap);
}
