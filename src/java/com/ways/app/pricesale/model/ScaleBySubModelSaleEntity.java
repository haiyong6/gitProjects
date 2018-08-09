package com.ways.app.pricesale.model;

/**
 * 车型销售比例分析实体类
 * 
 * @author songguobiao
 *
 */
public class ScaleBySubModelSaleEntity {
	/**
	 * 车型ID
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
	 * 序号
	 */
	private String sn;
	/**
	 * 型号ID
	 */
	private String vid;
	/**
	 * 型号编码
	 */
	private String versionCode;
	/**
	 * 型号简称
	 */
	private String versionShortName;
	/**
	 * 型号英文简称
	 */
	private String versionShortNameEn;
	/**
	 * 型号标识
	 */
	private String versionTrimName;
	/**
	 * 型号英文标识
	 */
	private String versionTrimNameEn;
	/**
	 * 车身形式
	 */
	private String bodyTypeName;
	/**
	 * 车身形式英文
	 */
	private String bodyTypeNameEn;
	/**
	 * 原装版型号编码
	 */
	private String originalVersionCode;
	/**
	 * 原装版标识
	 */
	private String originalTrimName;
	/**
	 * 原装版英文标识
	 */
	private String originalTrimNameEn;
	/**
	 * 是否显示
	 */
	private String isShow;
	/**
	 * 价格
	 */
	private String price;
	/**
	 * 累计Mix
	 */
	private String addMix;
	/**
	 * 月均Mix
	 */
	private String avgMix;
	/**
	 * 累计型号销量
	 */
    private String addSale;
    /**
     * 月均型号销量
     */
    private String avgSale;
    
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
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
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
	public String getVersionTrimName() {
		return versionTrimName;
	}
	public void setVersionTrimName(String versionTrimName) {
		this.versionTrimName = versionTrimName;
	}
	public String getVersionTrimNameEn() {
		return versionTrimNameEn;
	}
	public void setVersionTrimNameEn(String versionTrimNameEn) {
		this.versionTrimNameEn = versionTrimNameEn;
	}
	public String getBodyTypeName() {
		return bodyTypeName;
	}
	public void setBodyTypeName(String bodyTypeName) {
		this.bodyTypeName = bodyTypeName;
	}
	public String getBodyTypeNameEn() {
		return bodyTypeNameEn;
	}
	public void setBodyTypeNameEn(String bodyTypeNameEn) {
		this.bodyTypeNameEn = bodyTypeNameEn;
	}
	public String getOriginalVersionCode() {
		return originalVersionCode;
	}
	public void setOriginalVersionCode(String originalVersionCode) {
		this.originalVersionCode = originalVersionCode;
	}
	public String getOriginalTrimName() {
		return originalTrimName;
	}
	public void setOriginalTrimName(String originalTrimName) {
		this.originalTrimName = originalTrimName;
	}
	public String getOriginalTrimNameEn() {
		return originalTrimNameEn;
	}
	public void setOriginalTrimNameEn(String originalTrimNameEn) {
		this.originalTrimNameEn = originalTrimNameEn;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAddMix() {
		return addMix;
	}
	public void setAddMix(String addMix) {
		this.addMix = addMix;
	}
	public String getAvgMix() {
		return avgMix;
	}
	public void setAvgMix(String avgMix) {
		this.avgMix = avgMix;
	}
	public String getAddSale() {
		return addSale;
	}
	public void setAddSale(String addSale) {
		this.addSale = addSale;
	}
	public String getAvgSale() {
		return avgSale;
	}
	public void setAvgSale(String avgSale) {
		this.avgSale = avgSale;
	}
}
