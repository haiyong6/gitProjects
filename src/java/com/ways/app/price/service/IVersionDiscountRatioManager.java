package com.ways.app.price.service;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * 型号折扣对比Service层接口
 * @author yinlue
 *
 */
public interface IVersionDiscountRatioManager
{
	
 /**
 * 初始化时间
 * @param paramHttpServletRequest
 * @param paramMap
 * @return
 */
  public String initDate(HttpServletRequest paramHttpServletRequest, Map<String, Object> paramMap);

  /**
   * 加载图形和表格
   * @param paramHttpServletRequest
   * @param paramMap
   * @return
   */
  public String loadVersionDiscountRatioChartAndTable(HttpServletRequest paramHttpServletRequest, Map<String, Object> paramMap);
  
  /**
	 * 导出
	 * @param request
	 * @param paramsMap
	 * @return
	 */
	public Workbook exportExcel(HttpServletRequest request,Map<String, Object> paramsMap);
  
  
}