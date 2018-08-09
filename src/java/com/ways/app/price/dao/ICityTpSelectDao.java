package com.ways.app.price.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.ObjectGroup;
import com.ways.app.price.model.Segment;

/**
 * 成交价查询Dao层接口
 * 
 * @author songguobiao
 *
 */
public interface ICityTpSelectDao {
	
	/**
	 * 初始化时间
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Map<String, String>> initDate(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过本品ID查找其竟品车型
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<BPSubModel> getSubmodelByBp(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过细分市场
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Segment> getSubmodelBySegment(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过品牌
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getSubmodelByBrand(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型通过厂商
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getSubmodelByManf(Map<String, Object> paramsMap);
	
	/**
	 * 获取车身形式
	 * 
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<BodyType> getBodyType(Map<String, Object> paramsMap);
	
	/**
	 * 获取一汽大众常用型号组
	 * 
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<ObjectGroup> getAutoCustomGroup(Map<String, Object> paramsMap);
	
	/**
	 * 获取城市的名称和排序
	 * 
	 * @param paramsMap
	 * @return
	 */
	public  List<Map<String, String>> getCityNameAndSort(Map<String, Object> paramsMap);
	
	/**
	 * 获取型号成交价数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Map<String, String>> getVersionTpData(Map<String, Object> paramsMap);
	
	/**
	 * 获取型号全国加权平均成交价数据
	 * 
	 * @param paramsMap
	 * @return
	 */
	public List<Map<String, String>> getVersionTpDataByCountryAvg(Map<String, Object> paramsMap);
}
