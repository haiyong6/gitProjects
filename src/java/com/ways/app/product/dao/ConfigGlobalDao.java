package com.ways.app.product.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.Segment;
import com.ways.app.product.model.ConfigClassifyEntity;
import com.ways.app.product.model.ConfigInfoEntity;
import com.ways.app.product.model.GlobalTextEntity;

/**
 * 配置公共DAO
 * @author yinlue
 *
 */
public interface ConfigGlobalDao {

	/**
	 * 获取配置大类
	 * @param paramsMap
	 * @return
	 */
	public List<ConfigClassifyEntity> getConfigClassify(Map<String, String> paramsMap);
	
	/**
	 * 获取配置信息集合
	 * @param paramsMap
	 * @return
	 */
	public List<ConfigInfoEntity> getConfigInfoList(Map<String, String> paramsMap);
	
	/**
	 *  获取配置值
	 * @param paramsMap
	 * @return
	 */
	public List<?> getConfigValue(Map<String, String> paramsMap);
	
	/**
	 * 获取车身形式
	 * @param paramsMap
	 * @return
	 */
	public List<GlobalTextEntity> getSubModelBodyType(Map<String, String> paramsMap);
	
	/**
	 * 获取子车型通过本品ID查找其竟品车型
	 * @param paramsMap
	 * @return
	 */
	public List<BPSubModel> getProductSubmodelByBp(Map<String, String> paramsMap);
	
	/**
	 * 获取子车型通过细分市场
	 * @param paramsMap
	 * @return
	 */
	public List<Segment> getProductSubmodelBySegment(Map<String, String> paramsMap);
	
	/**
	 * 获取子车型通过品牌
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getProductSubmodelByBrand(Map<String, String> paramsMap);
	
	/**
	 * 获取子车型通过厂商
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getProductSubmodelByManf(Map<String, String> paramsMap);
}
