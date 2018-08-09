package com.ways.app.price.model;

import java.io.Serializable;
import java.util.List;

/***********************************************************************************************
 * <br />一汽大众常用型号组
 * <br />Class name: AutoCustomGroup.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />May 11, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
public class AutoCustomGroup implements Serializable{
	/**
	* @Fields serialVersionUID : TODO
	*/
	
	private static final long serialVersionUID = 1L;
	/**
	* @Fields modelID : 常用组车型ID
	*/
	private String modelID;
	/**
	* @Fields modelName : 常用组车型名称
	*/
	private String modelName;
	/**
	* @Fields modelEName : 常用组车型英文名称
	*/
	private String modelEName;
	/**
	* @Fields objectGroupList : 常用对象组
	*/
	private List<ObjectGroup> objectGroupList;
	
	public String getModelID() {
		return modelID;
	}
	public void setModelID(String modelID) {
		this.modelID = modelID;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getModelEName() {
		return modelEName;
	}
	public void setModelEName(String modelEName) {
		this.modelEName = modelEName;
	}
	public List<ObjectGroup> getObjectGroupList() {
		return objectGroupList;
	}
	public void setObjectGroupList(List<ObjectGroup> objectGroupList) {
		this.objectGroupList = objectGroupList;
	}
}
