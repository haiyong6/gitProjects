package com.ways.app.product.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.product.model.ProductVersionEntity;

/**
 * 配置型号查询DAO
 * @author yinlue
 *
 */
public interface ConfigVersionQueryDao {

	/**
	 * 加载配置型号结果集
	 * @param paramsMap
	 * @return
	 */
	public List<ProductVersionEntity> getConfigVersionResult(Map<String, Object> paramsMap);
}
