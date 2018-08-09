package com.ways.app.policy.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.policy.model.TerminalOriginalEntity;

public interface ITerminalAnalysisDao {

	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap);
	
	/**
	 * 加载终端支持分析图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List loadTerminalChartAndTable(Map<String, Object> paramsMap);
	
	/**
	 * 通过型号加载终端支持分析图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public List loadTerminalChartAndTableByVersion(Map<String, Object> paramsMap);
	
	/**
	 * 导出终端支持分析数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<TerminalOriginalEntity> exportTerminalData(Map<String, Object> paramsMap);
	
	/**
	 * 通过型号导出终端支持分析数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<TerminalOriginalEntity> exportTerminalDataByVersion(Map<String, Object> paramsMap);
}
