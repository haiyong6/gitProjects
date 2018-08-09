package com.ways.app.policy.model;

import java.io.Serializable;
import java.util.List;

import flexjson.JSON;

/**
 * 政策层级实体
 * @author yinlue
 *
 */
public class PolicyHierarchy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//ID
	private String id;
	//名称
	private String name;
	//合并行
	private String count;
	//子政策集合
	private List<PolicyHierarchy> childrenPolicyList;
	//车型名称集合
	private List<PolicyValue> modelNameList;
	//政策值集合
	private List<PolicyValue> policyValueList;
	//车型MSRP范围
	private List<PolicyValue> policyMsrp;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	@JSON(include=true)
	public List<PolicyHierarchy> getChildrenPolicyList() {
		return childrenPolicyList;
	}
	public void setChildrenPolicyList(List<PolicyHierarchy> childrenPolicyList) {
		this.childrenPolicyList = childrenPolicyList;
	}
	@JSON(include=true)
	public List<PolicyValue> getModelNameList() {
		return modelNameList;
	}
	public void setModelNameList(List<PolicyValue> modelNameList) {
		this.modelNameList = modelNameList;
	}
	public List<PolicyValue> getPolicyValueList() {
		return policyValueList;
	}
	public void setPolicyValueList(List<PolicyValue> policyValueList) {
		this.policyValueList = policyValueList;
	}
	@JSON(include=true)
	public List<PolicyValue> getPolicyMsrp() {
		return policyMsrp;
	}
	public void setPolicyMsrp(List<PolicyValue> policyMsrp) {
		this.policyMsrp = policyMsrp;
	}
}
