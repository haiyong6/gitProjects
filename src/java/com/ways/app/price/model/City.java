package com.ways.app.price.model;

import java.io.Serializable;

/**
 * 城市实体类
 * @author yinlue
 *
 */
public class City implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//城市ID
	private String cityId;
	//城市名称
	private String cityName;
	
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}
