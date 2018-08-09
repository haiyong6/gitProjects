package com.ways.app.price.global.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.price.model.Area;
import com.ways.app.price.model.BPSubModel;
import com.ways.app.price.model.BodyType;
import com.ways.app.price.model.LetterObj;
import com.ways.app.price.model.ObjectGroup;
import com.ways.app.price.model.Segment;
import com.ways.app.price.model.SubModel;


/**
 * 初始化控件
 * @author yinlue
 *
 */
public interface IGlobalDao {
	/**
	 * 获取初始化城市控件值
	 */
	public List<Area> getCityModal(Map<String, Object> paramsMap);
	
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
	
	/**
	 * 通过返利成交价数据有效日期过滤查询生产商信息
	 * @param paramsMap
	 * @return
	 */
	public List<LetterObj> getManfModal(Map<String, Object> paramsMap);
	
	/**
	 * 获取子车型下型号数据
	 * @param paramsMap
	 * @return
	 */
	public List<SubModel> getVersionModalByCommon(Map<String, Object> paramsMap);
	
	/**
	 * 校验成交价有效车型和型号
	 * @param paramMap
	 * @return
	 */
	public List<SubModel> checkPopBoxTpData(Map<String, Object> paramsMap);
	
	/**
	 * 获取细分市场以及所属子细分市场
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<Segment> getSegmentAndChildren(Map<String, Object> paramsMap);
	
	/**
	 * 按首字母获取厂商
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<LetterObj> getManf(Map<String, Object> paramsMap);
	
	/**
	 * 按首字母获取品牌
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<LetterObj> getBrand(Map<String, Object> paramsMap);
	
	/**
	 * 获取车身形式
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<BodyType> getBodyType(Map<String, Object> paramsMap);
	
	/**
	 * 获取一汽大众常用型号组
	 * @param paramHttpServletRequest
	 * @param paramsMap
	 */
	public List<ObjectGroup> getAutoCustomGroup(Map<String, Object> paramsMap);
}
