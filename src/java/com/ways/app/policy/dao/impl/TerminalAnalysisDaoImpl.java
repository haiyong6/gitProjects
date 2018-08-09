package com.ways.app.policy.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.policy.dao.ITerminalAnalysisDao;
import com.ways.app.policy.model.TerminalOriginalEntity;
import com.ways.framework.base.IBatisBaseFawvwDao;

@Repository("TerminalAnalysisDao")
public class TerminalAnalysisDaoImpl extends IBatisBaseFawvwDao implements ITerminalAnalysisDao {

	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap) 
	{
		List<Map<String, String>> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("TerminalAnalysis.initDate", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 加载终端支持分析图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List loadTerminalChartAndTable(Map<String, Object> paramsMap) 
	{
		List list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("TerminalAnalysis.loadTerminalData", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 加载终端支持分析图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List loadTerminalChartAndTableByVersion(Map<String, Object> paramsMap) 
	{
		List list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("TerminalAnalysis.loadTerminalDataByVersion", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 导出终端支持分析图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TerminalOriginalEntity> exportTerminalData(Map<String, Object> paramsMap) 
	{
		List<TerminalOriginalEntity> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("TerminalAnalysis.exportOriginalData", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 通过型号导出终端支持分析图形和表格
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TerminalOriginalEntity> exportTerminalDataByVersion(Map<String, Object> paramsMap) 
	{
		List<TerminalOriginalEntity> list = null;
		try {
			list = getSqlMapClientTemplate().queryForList("TerminalAnalysis.exportOriginalDataByVersion", paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
