package com.ways.app.price.model;

import java.io.Serializable;

/**
 * 价格降幅实体类
 * @author yinlue
 *
 */
public class PriceIndexEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	//对象ID
	private String id;
	//时间年月
	private String yearmonth;
	//值
	private String value;
	
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
