package com.ways.app.policy.model;

import java.io.Serializable;

public class PromotionInfo implements Serializable{
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 2266842031900603568L;
	/**
	 * @Fields Versionsubsidy : 型号促销
	 */
	private String Versionsubsidy;
	/**
	 * @Fields SubsidyTypeId : 促销类型ID
	 */
	private String SubsidyTypeId;
	/**
	 * @Fields SubsidyType : 促销类型
	 */
	private String SubsidyType;
	/**
	 * @Fields Subsidy : 促销价格
	 */
	private String Subsidy;
	/**
	 * @Fields VersionSale : 销量
	 */
	private String VersionSale;
	
	public String getVersionsubsidy() {
		return Versionsubsidy;
	}
	public void setVersionsubsidy(String versionsubsidy) {
		Versionsubsidy = versionsubsidy;
	}
	public String getSubsidyTypeId() {
		return SubsidyTypeId;
	}
	public void setSubsidyTypeId(String subsidyTypeId) {
		SubsidyTypeId = subsidyTypeId;
	}
	public String getSubsidyType() {
		return SubsidyType;
	}
	public void setSubsidyType(String subsidyType) {
		SubsidyType = subsidyType;
	}
	public String getSubsidy() {
		return Subsidy;
	}
	public void setSubsidy(String subsidy) {
		Subsidy = subsidy;
	}
	public String getVersionSale() {
		return VersionSale;
	}
	public void setVersionSale(String versionSale) {
		VersionSale = versionSale;
	}
}
