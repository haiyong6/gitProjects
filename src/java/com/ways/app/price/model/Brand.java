package com.ways.app.price.model;

import java.io.Serializable;
import java.util.List;

/**
 * 品牌实体类
 * @author yinlue
 *
 */
public class Brand implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//品牌ID
	private String brandId;
	//品牌Name
	private String brandName;
	//品牌首字母
	private String letter;
	//品牌下子车型
	private List<SubModel> subModelList;
	
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
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
