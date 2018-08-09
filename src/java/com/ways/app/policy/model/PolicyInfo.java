package com.ways.app.policy.model;

import java.io.Serializable;

/**
 * 政策信息
 * @author yinlue
 *
 */
public class PolicyInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//政策名称
	private String policyName;
	//政策内容
	private String policyContent;
	//上月政策内容
	private String policyLastMonthContent;
	
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	public String getPolicyContent() {
		return policyContent;
	}
	public void setPolicyContent(String policyContent) {
		this.policyContent = policyContent;
	}
	public String getPolicyLastMonthContent() {
		return policyLastMonthContent;
	}
	public void setPolicyLastMonthContent(String policyLastMonthContent) {
		this.policyLastMonthContent = policyLastMonthContent;
	}
}
