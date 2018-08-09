package com.ways.app.product.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.product.dao.ConfigVersionQueryDao;
import com.ways.app.product.model.ProductVersionEntity;
import com.ways.framework.base.IBatisBaseDao;

/**
 * 配置型号查询DAO实现类
 * @author yinlue
 *
 */
@Repository("ConfigVersionQuerylDaoImpl")
public class ConfigVersionQuerylDaoImpl extends IBatisBaseDao implements ConfigVersionQueryDao {

	/**
	 * 加载配置型号结果集
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductVersionEntity> getConfigVersionResult(Map<String, Object> paramsMap) 
	{
		return getSqlMapClientTemplate().queryForList("configVersion.getConfigVersionResult",paramsMap);
	}

}
