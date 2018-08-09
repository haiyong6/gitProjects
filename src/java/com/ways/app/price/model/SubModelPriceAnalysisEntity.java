package com.ways.app.price.model;

import java.io.Serializable;

/**
 * 车型价格段分析实体类
 * 
 * @author songguobiao
 * @date 20161107
 *
 */
public class SubModelPriceAnalysisEntity  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 型号编码
	 */
	private String versionCode;
	/**
	 * 车型编码
	 */
	private String subModelId;
	/**
	 * 车型名称
	 */
	private String subModelName;
	/**
	 * 车型英文名称
	 */
	private String subModelNameEn;
	/**
	 * 厂商名称
	 */
	private String manfName;
	/**
	 * 厂商英文名称
	 */
	private String manfNameEn;
	/**
	 * 排量
	 */
	private String emissionsName;
	/**
	 * 排挡方式
	 */
	private String transMission;
	/**
	 * 型号标识
	 */
	private String versionShortName;
	/**
	 * 型号标识英文
	 */
	private String versionShortNameEn;
	/**
	 * 车身形式英文
	 */
	private String bodyTypeNameEn;
	/**
	 * 型号上市日期
	 */
	private String versionLaunchDate;
	/**
	 * 价格
	 */
	private String price;
	/**
	 * 指导价
	 */
	private String msrp;
	/**
	 * 成交价
	 */
	private String tp;
	/**
	 * 序号
	 */
	private double sn;
	/**
	 * 是否本品
	 */
	private String isBase;
	
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
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
	public String getSubModelNameEn() {
		return subModelNameEn;
	}
	public void setSubModelNameEn(String subModelNameEn) {
		this.subModelNameEn = subModelNameEn;
	}
	public String getManfName() {
		return manfName;
	}
	public void setManfName(String manfName) {
		this.manfName = manfName;
	}
	public String getManfNameEn() {
		return manfNameEn;
	}
	public void setManfNameEn(String manfNameEn) {
		this.manfNameEn = manfNameEn;
	}
	public String getEmissionsName() {
		return emissionsName;
	}
	public void setEmissionsName(String emissionsName) {
		this.emissionsName = emissionsName;
	}
	public String getTransMission() {
		return transMission;
	}
	public void setTransMission(String transMission) {
		this.transMission = transMission;
	}
	public String getVersionShortName() {
		return versionShortName;
	}
	public void setVersionShortName(String versionShortName) {
		this.versionShortName = versionShortName;
	}
	public String getVersionShortNameEn() {
		return versionShortNameEn;
	}
	public void setVersionShortNameEn(String versionShortNameEn) {
		this.versionShortNameEn = versionShortNameEn;
	}
	public String getBodyTypeNameEn() {
		return bodyTypeNameEn;
	}
	public void setBodyTypeNameEn(String bodyTypeNameEn) {
		this.bodyTypeNameEn = bodyTypeNameEn;
	}
	public String getVersionLaunchDate() {
		return versionLaunchDate;
	}
	public void setVersionLaunchDate(String versionLaunchDate) {
		this.versionLaunchDate = versionLaunchDate;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
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
	public double getSn() {
		return sn;
	}
	public void setSn(double sn) {
		this.sn = sn;
	}
	public String getIsBase() {
		return isBase;
	}
	public void setIsBase(String isBase) {
		this.isBase = isBase;
	}
}
