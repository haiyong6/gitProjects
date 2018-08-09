package com.ways.app.moduleLog.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 模块日志记录Service层接口
 * @author yinlue
 *
 */
public interface IModuleLogManager {

	
	/**
	 * 添加模块日志记录
	 * @param paramsMap
	 */
	public void addModuleLog(HttpServletRequest request,Map<String, Object> paramsMap);
	
	/**
	 * 更新日志信息
	 * @param paramsMap
	 */
	public void updateModuleLog(Map<String, Object> paramsMap);
}
