package com.ways.app.policy.model;
/***
 * 促销查询政策内容List实体类
 * @author zhaohaiyong
 *
 */
public class PolicyListEntity {

	private String subsidyTypeId;//subsidytypeId
	private String policyName;//政策名称
	private String policyContent;//政策内容
	private String reward;//促销
	private String rewardTotal;//单车总促销
	
	public String getSubsidyTypeId() {
		return subsidyTypeId;
	}
	public void setSubsidyTypeId(String subsidyTypeId) {
		this.subsidyTypeId = subsidyTypeId;
	}
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
	public String getReward() {
		return reward;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getRewardTotal() {
		return rewardTotal;
	}
	public void setRewardTotal(String rewardTotal) {
		this.rewardTotal = rewardTotal;
	}
	
	
}
