package com.ways.app.policy.model;

import java.io.Serializable;
import java.util.List;

import flexjson.JSON;

/**
 * 政策子车型信息
 * @author yinlue
 *
 */
public class PolicySubModelInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//子车型ID
	private String subModelId;
	//子车型名称
	private String subModelName;
	//车型政策集合
	private List<PolicyInfo> policyList;
	
	public String getSubModelId() {
		return subModelId;
	}
	public void setSubModelId(String subModelId) {
		this.subModelId = subModelId;
	}
	public String getSubModelName() {
		return subModelName;
	}
	public void setSubModelName(String subModelName) {
		this.subModelName = subModelName;
	}
	
	@JSON(include=true)
	public List<PolicyInfo> getPolicyList() {
		return policyList;
	}
	public void setPolicyList(List<PolicyInfo> policyList) {
		this.policyList = policyList;
	}
	
	
}
