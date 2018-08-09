package com.ways.app.price.model;

import java.io.Serializable;

/**
 * 区域价格降幅实体类
 * @author yinlue
 *
 */
public class CityPriceIndexEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	//对象ID
	private String id;
	//时间年月
	private String yearmonth;
	//值
	private String value;
	//城市ID
	private String cityId;
	//城市名称
	private String cityName;
	//城市名称英文
	private String cityNameEn;
	
	
	public String getCityNameEn() {
		return cityNameEn;
	}
	public void setCityNameEn(String cityNameEn) {
		this.cityNameEn = cityNameEn;
	}
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getYearmonth() {
		return yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
