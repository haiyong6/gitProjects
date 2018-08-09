package com.ways.app.price.model;

import java.io.Serializable;
import java.util.List;

/**
 * 本品车型实体类
 * @author yinlue
 *
 */
public class BPSubModel implements Serializable{
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
	//竟品车集合
	private List<SubModel> jpSubModelList;
	
	
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
	 
	public List<SubModel> getJpSubModelList() {
		return jpSubModelList;
	}
	public void setJpSubModelList(List<SubModel> jpSubModelList) {
		this.jpSubModelList = jpSubModelList;
	}
	public String getPooAttributeId() {
		return pooAttributeId;
	}
	public void setPooAttributeId(String pooAttributeId) {
		this.pooAttributeId = pooAttributeId;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	 
}
