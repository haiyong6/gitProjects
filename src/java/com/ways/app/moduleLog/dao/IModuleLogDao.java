package com.ways.app.moduleLog.dao;

import java.util.Map;

/**
 * 模块日志记录DAO层接口
 * @author yinlue
 *
 */
public interface IModuleLogDao {

	
	/**
	 * 添加模块日志记录
	 * @param paramsMap
	 */
	public void addModuleLog(Map<String, Object> paramsMap);
	
	/**
	 * 更新模块日志记录
	 * @param paramsMap
	 */
	public void updateModuleLog(Map<String, Object> paramsMap);
}
