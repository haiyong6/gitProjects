package com.ways.app.product.model;

import java.io.Serializable;
import java.util.List;

/**
 * 配置分类实体
 * @author yinlue
 *
 */
public class ConfigClassifyEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//分类ID
	private String classifyId;
	//分类名称
	private String classifyName;
	//分类英文名称
	private String classifyNameEn;
	//配置小类集合
	private List<ConfigInfoEntity> infoList;
	
	
	public List<ConfigInfoEntity> getInfoList() {
		return infoList;
	}
	public void setInfoList(List<ConfigInfoEntity> infoList) {
		this.infoList = infoList;
	}
	public String getClassifyId() {
		return classifyId;
	}
	public void setClassifyId(String classifyId) {
		this.classifyId = classifyId;
	}
	public String getClassifyName() {
		return classifyName;
	}
	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}
	public String getClassifyNameEn() {
		return classifyNameEn;
	}
	public void setClassifyNameEn(String classifyNameEn) {
		this.classifyNameEn = classifyNameEn;
	}
	
}
