package com.ways.app.price.model;

import java.io.Serializable;
import java.util.List;

/***********************************************************************************************
 * <br />一汽大众常用组类
 * <br />Class name: ObjectGroup.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />May 11, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
public class ObjectGroup implements Serializable {
	/**
	* @Fields serialVersionUID : TODO
	*/
	
	private static final long serialVersionUID = 1L;
	/**
	* @Fields objectGroupID : 常用组ID
	*/
	private String objectGroupID;
	/**
	* @Fields objectGroup : 常用组名称
	*/
	private String objectGroup;
	/**
	* @Fields versionList : 常用对象组下所有型号信息
	*/
	private List<Version> versionList;
	
	public String getObjectGroupID() {
		return objectGroupID;
	}
	public void setObjectGroupID(String objectGroupID) {
		this.objectGroupID = objectGroupID;
	}
	public String getObjectGroup() {
		return objectGroup;
	}
	public void setObjectGroup(String objectGroup) {
		this.objectGroup = objectGroup;
	}
	public List<Version> getVersionList() {
		return versionList;
	}
	public void setVersionList(List<Version> versionList) {
		this.versionList = versionList;
	}

}
