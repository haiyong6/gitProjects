package com.ways.app.policy.model;

import java.util.List;

/****
 * 促销查询20161221改动实体类
 * @author zhaohaiyong
 *
 */
public class PolicyMonthInfoEntity {

	private String ym;//年月
	private String submodelId;//车型Id
	private String submodelName;//车型名称
	private List<PolicyListEntity> policyList;
	public String getYm() {
		return ym;
	}
	public void setYm(String ym) {
		this.ym = ym;
	}
	public String getSubmodelId() {
		return submodelId;
	}
	public void setSubmodelId(String submodelId) {
		this.submodelId = submodelId;
	}
	public String getSubmodelName() {
		return submodelName;
	}
	public void setSubmodelName(String submodelName) {
		this.submodelName = submodelName;
	}
	public List<PolicyListEntity> getPolicyList() {
		return policyList;
	}
	public void setPolicyList(List<PolicyListEntity> policyList) {
		this.policyList = policyList;
	}
	
}
