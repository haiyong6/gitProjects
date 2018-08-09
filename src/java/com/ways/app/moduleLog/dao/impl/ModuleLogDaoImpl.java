package com.ways.app.moduleLog.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.moduleLog.dao.IModuleLogDao;
import com.ways.framework.base.IBatisBaseDao;

/**
 * 模块日志记录DAO层接口
 * @author yinlue
 *
 */

@Repository("ModuleLogDaoImpl")
public class ModuleLogDaoImpl extends IBatisBaseDao implements IModuleLogDao {

	/**
	 * 添加模块日志记录
	 */
	@Override
	public void addModuleLog(Map<String, Object> paramsMap) 
	{
		getSqlMapClientTemplate().insert("moduleLog.addModuleLog", paramsMap);
	}

	/**
	 * 更新模块日志记录
	 */
	@Override
	public void updateModuleLog(Map<String, Object> paramsMap)
	{
		getSqlMapClientTemplate().update("moduleLog.updateModuleLog", paramsMap);
	}

}
