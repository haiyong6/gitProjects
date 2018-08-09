package com.ways.app.price.model;

import java.io.Serializable;
import java.util.List;

/**
 * 子车型实体类
 * @author yinlue
 *
 */
public class SubModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//子车型ID
	private String subModelId;
	//子车型名称
	private String subModelName;
	//名称首字母
	private String letter;
	//产地属性
	private String pooAttributeId;
	//型号集合
	private List<Version> versionList;
	
	public List<Version> getVersionList() {
		return versionList;
	}
	public void setVersionList(List<Version> versionList) {
		this.versionList = versionList;
	}
	
	public String getPooAttributeId() {
		return pooAttributeId;
	}
	public void setPooAttributeId(String pooAttributeId) {
		this.pooAttributeId = pooAttributeId;
	}
	public String getSubModelId() {
		return subModelId;
	}
	public void setSubModelId(String subModelId) {
		this.subModelId = subModelId;
	}
	
	public String getSubModelName() {
		return subModelName;
	}
	public void setSubModelName(String subModelName) {
		this.subModelName = subModelName;
	}
	public SubModel() {
	}
	public SubModel(String subModelId, String subModelName,String letter,String pooAttributeId) {
		super();
		this.subModelId = subModelId;
		this.subModelName = subModelName;
		this.letter = letter;
		this.pooAttributeId = pooAttributeId;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
}
