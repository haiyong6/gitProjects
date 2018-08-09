package com.ways.app.product.model;

import java.io.Serializable;

/**
 * ID，Value公共实体类
 * @author yinlue
 *
 */
public class GlobalTextEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String text;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
