package com.ways.app.pricesale.model;

import java.io.Serializable;

/***********************************************************************************************
 * <br />价格段销量分析实体类
 * <br />Class name: VolumeByPriceRangeEntity.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Apr 25, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
public class VolumeByPriceRangeEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	* @Fields versionCode : 编码
	*/
	private String versionCode;
	
	/**
	* @Fields objectType : 所选对象
	*/
	private String objectType;
	
	/**
	* @Fields versionName : 型号名称
	*/
	private String versionName;
	
	/**
	* @Fields versionName : 型号英文名称
	*/
	private String versionEName;
	
	/**
	* @Fields sales : 销量
	*/
	private int sales;
	
	/**
	* @Fields price : 价格
	*/
	private int price;
	/**
	* @Fields otherPrice : 其他价格(条件为指导价时存储为成交价,条件为成交价时存储为指导价)
	*/
	private int otherprice;
	
	/**
	* @Fields time : 时间
	*/
	private String time;
	
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public int getSales() {
		return sales;
	}
	public void setSales(Object sales) {
		if(null == sales){
			this.sales = 0;
		}else{
			this.sales = Integer.parseInt(sales.toString());
		}
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(Object price) {
		if(null == price){
			this.price = 0;
		}else{
			this.price = Integer.parseInt(price.toString());
		}
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getVersionEName() {
		return versionEName;
	}
	public void setVersionEName(String versionEName) {
		this.versionEName = versionEName;
	}
	public int getOtherprice() {
		return otherprice;
	}
	public void setOtherprice(Object otherprice) {
		if(null == otherprice){
			this.otherprice = 0;
		}else{
			this.otherprice = Integer.parseInt(otherprice.toString());
		}
	}
}
