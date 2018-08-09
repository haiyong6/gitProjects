package com.ways.app.price.model;

import java.io.Serializable;
import java.util.List;

/**
 * 厂商与子车型实体类
 * @author yinlue
 *
 */
public class Manf implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//厂商ID
	private String manfId;
	//厂商名称
	private String manfName;
	//首字母
	private String letter;
	//子车型集合
	private List<SubModel> subModelList;
	
	public String getManfId() {
		return manfId;
	}
	public void setManfId(String manfId) {
		this.manfId = manfId;
	}
	public String getManfName() {
		return manfName;
	}
	public void setManfName(String manfName) {
		this.manfName = manfName;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	public List<SubModel> getSubModelList() {
		return subModelList;
	}
	public void setSubModelList(List<SubModel> subModelList) {
		this.subModelList = subModelList;
	}
}
