package com.ways.app.policy.model;

import java.io.Serializable;

/**
 * 政策值实体
 * @author yinlue
 *
 */
public class PolicyValue implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	

	
}
