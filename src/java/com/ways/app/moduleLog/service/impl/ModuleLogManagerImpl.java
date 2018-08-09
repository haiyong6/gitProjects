package com.ways.app.moduleLog.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ways.app.moduleLog.dao.IModuleLogDao;
import com.ways.app.moduleLog.service.IModuleLogManager;

/**
 * 模块日志记录Service层接口实现类
 * @author yinlue
 *
 */
@Service("moduleLogManager")
public class ModuleLogManagerImpl implements IModuleLogManager {

	@Autowired
	private IModuleLogDao moduleLogDao;

	/**
	 * 添加模块日志记录
	 */
	@Override
	public void addModuleLog(HttpServletRequest request,Map<String, Object> paramsMap) 
	{
		try {
			moduleLogDao.addModuleLog(paramsMap);
		} catch (Exception e) {

		}
	}

	/**;
	 * 更新日志信息
	 */
	@Override
	public void updateModuleLog(Map<String, Object> paramsMap) 
	{
		try {
			moduleLogDao.updateModuleLog(paramsMap);
		} catch (Exception e) {

		}
	}
}
