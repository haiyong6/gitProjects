package com.ways.app.pricesale.model;

public class CompetingProductEntity {

	private String versionId;//型号id
	private String versionName;//型号全称
	private String versionNameEn;//型号全称英文
	private String versionCode;//型号编码
	private String objId;//对象id，可以是车型，厂商品牌，品牌，级别，系别等
	private String objName;//对象名称
	private String objNameEn;//对象名称英文
	private String yearmonth;//年月
	private String versionsale;//销量
	private String msrp;//指导价
	private String tp;//成交价
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getVersionNameEn() {
		return versionNameEn;
	}
	public void setVersionNameEn(String versionNameEn) {
		this.versionNameEn = versionNameEn;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public String getObjName() {
		return objName;
	}
	public void setObjName(String objName) {
		this.objName = objName;
	}
	public String getObjNameEn() {
		return objNameEn;
	}
	public void setObjNameEn(String objNameEn) {
		this.objNameEn = objNameEn;
	}
	public String getYearmonth() {
		return yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}
	public String getVersionsale() {
		return versionsale;
	}
	public void setVersionsale(String versionsale) {
		this.versionsale = versionsale;
	}
	public String getMsrp() {
		return msrp;
	}
	public void setMsrp(String msrp) {
		this.msrp = msrp;
	}
	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
	}
	
	
}
