package com.ways.app.price.model;

import java.io.Serializable;
import java.util.List;

/**
 * 大区实体类
 * @author yinlue
 *
 */
public class Area implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//大区ID
	private String areaId;
	//大区名称
	private String areaName;
	//城市集合
	private List<City> citys;
	
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public List<City> getCitys() {
		return citys;
	}
	public void setCitys(List<City> citys) {
		this.citys = citys;
	}
	
}
