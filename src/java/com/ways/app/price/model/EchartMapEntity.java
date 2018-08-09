package com.ways.app.price.model;

import java.io.Serializable;

/**
 * Echart地图实体
 * @author yinlue
 *
 */
public class EchartMapEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//标注名称
	private String name;
	//标注值
	private String value;
	
	public EchartMapEntity() 
	{
		
	}
	
	public EchartMapEntity(String name,String value) 
	{
		this.name = name; 
		this.value = value; 
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
