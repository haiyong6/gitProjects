package com.ways.app.price.model;

import java.io.Serializable;

/**
 * 车身形式实体类
 * @author yinlue
 *
 */
public class BodyType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//车身形式ID
	private String bodyTypeId;
	//车身形式名称
	private String bodyTypeName;

	public String getBodyTypeId() {
		return bodyTypeId;
	}

	public void setBodyTypeId(String bodyTypeId) {
		this.bodyTypeId = bodyTypeId;
	}

	public String getBodyTypeName() {
		return bodyTypeName;
	}

	public void setBodyTypeName(String bodyTypeName) {
		this.bodyTypeName = bodyTypeName;
	}
	
}
