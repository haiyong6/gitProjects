package com.ways.app.price.model;

import java.io.Serializable;

/**
 * 雷达图数据装载实体
 * @author yinlue
 *
 */
public class RadarChartSeriesEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//数据
	private String[] value;
	//名称
	private String name;
	
	public RadarChartSeriesEntity() {
		
	}
	
	public RadarChartSeriesEntity(String[] value,String name) 
	{
		this.value = value;
		this.name = name;
	}
	
	public String[] getValue() {
		return value;
	}
	public void setValue(String[] value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
