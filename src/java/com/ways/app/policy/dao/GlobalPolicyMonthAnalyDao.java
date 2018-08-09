package com.ways.app.policy.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.Segment;



/**
 * 促销查询Dao层接口
 * @author yuml
 *
 */
public interface GlobalPolicyMonthAnalyDao {

	
	/**
	 * 获取子车型通过本品ID查找其竟品车型
	 * @param paramsMap
	 * @return
	 */
	public List<BPSubModel> getSubmodelByBp(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过细分市场
	 * @param paramsMap
	 * @return
	 */
	public List<Segment> getSubmodelBySegment(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过品牌
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getSubmodelByBrand(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过厂商
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getSubmodelByManf(Map<String, Object> paramsMap);
	
}
