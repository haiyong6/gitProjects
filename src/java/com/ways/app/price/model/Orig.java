package com.ways.app.price.model;

import java.io.Serializable;

/***********************************************************************************************
 * <br />车系
 * <br />Class name: Orig.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Apr 21, 2015
 * <br />@version 3.0
 * 
 ***********************************************************************************************/
public class Orig implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */

	private static final long serialVersionUID = 1L;
	/**
	 * 车系ID
	 */
	private String origId;
	/**
	 * 车系名称
	 */
	private String origName;

	public String getOrigId() {
		return origId;
	}

	public void setOrigId(String origId) {
		this.origId = origId;
	}

	public String getOrigName() {
		return origName;
	}

	public void setOrigName(String origName) {
		this.origName = origName;
	}

}
