package com.ways.app.policy.model;

import java.io.Serializable;

import com.ways.app.framework.utils.AppFrameworkUtil;

/**
 * 销量促销分析实体bean
 * 
 * @author songguobiao
 *
 */
public class SalePromotionAnalysisEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//年月
	private String ym;
	//型号Id 唯一标识
	private String vid;
	//厂商
	private String manfName;
	//厂商英文
	private String manfNameEn;
	//级别类型
	private String segmentName;
	//级别
	private String gradeName;
	//级别英文
	private String gradeNameEn;
	//系别
	private String origName;
	//系别英文
	private String origNameEn;
	//车身形式
	private String bodyType;
	//车身形式英文
	private String bodyTypeEn;
	//品牌
	private String brandName;
	//品牌英文
	private String brandNameEn;
	//车型
	private String subModelName;
	//车型英文
	private String subModelNameEn;
	//型号全称
	private String versionName;
	//型号全称英文
	private String versionNameEn;
	//型号编码
	private String versionCode;
	//型号上市日期
	private String versionLaunchDate;
	//指导价
	private String msrp;
	//返利
	private String rebateCash;
	//考核奖励
	private String ckReward;
	//总促销
	private String totalPromotion;
	//全款购车促销
	private String fullyPaid;
	//经销商支持
	private String grossSupport;
	//提车支持(std)
	private String std;
	//零售支持(aak)
	private String aak;
	//用户激励
	private String customerIncentive;
	//礼品
	private String present;
	//保险
	private String insurance;
	//保养
	private String maintenance;
	//人员奖励
	private String personReward;
	//金融贷款
    private String financeLoan;
    //置换支持
    private String displacesSupport;
	//开票价
	private String invoicePrice;
	//经销商成本
	private String grossCost;
	//成交价
	private String tp;
	//利润
	private String profit;
	//利润率
	private Double profitRate;
	//型号上月销量
	private String lastVersionSale;
	//型号当月销量
	private String versionSale;
	//对象ID
	private String objId;
	//对象名称
	private String objName;
	//对象英文名称
	private String objNameEn;
	//是否本品
	private String isBase;
	
	public String getYm() {
		return ym;
	}
	public void setYm(String ym) {
		this.ym = ym;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
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
	public String getSegmentName() {
		return segmentName;
	}
	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getGradeNameEn() {
		return gradeNameEn;
	}
	public void setGradeNameEn(String gradeNameEn) {
		this.gradeNameEn = gradeNameEn;
	}
	public String getOrigName() {
		return origName;
	}
	public void setOrigName(String origName) {
		this.origName = origName;
	}
	public String getOrigNameEn() {
		return origNameEn;
	}
	public void setOrigNameEn(String origNameEn) {
		this.origNameEn = origNameEn;
	}
	public String getBodyType() {
		return bodyType;
	}
	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
	}
	public String getBodyTypeEn() {
		return bodyTypeEn;
	}
	public void setBodyTypeEn(String bodyTypeEn) {
		this.bodyTypeEn = bodyTypeEn;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandNameEn() {
		return brandNameEn;
	}
	public void setBrandNameEn(String brandNameEn) {
		this.brandNameEn = brandNameEn;
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
	public String getVersionLaunchDate() {
		return versionLaunchDate;
	}
	public void setVersionLaunchDate(String versionLaunchDate) {
		this.versionLaunchDate = versionLaunchDate;
	}
	public String getMsrp() {
		return msrp;
	}
	public void setMsrp(String msrp) {
		this.msrp = msrp;
		if(AppFrameworkUtil.isEmpty(msrp)) {
			this.msrp = "-";
		}
	}
	public String getRebateCash() {
		return rebateCash;
	}
	public void setRebateCash(String rebateCash) {
		this.rebateCash = rebateCash;
	}
	public String getCkReward() {
		return ckReward;
	}
	public void setCkReward(String ckReward) {
		this.ckReward = ckReward;
	}
	public String getTotalPromotion() {
		return totalPromotion;
	}
	public void setTotalPromotion(String totalPromotion) {
		this.totalPromotion = totalPromotion;
		if(AppFrameworkUtil.isEmpty(totalPromotion)) {
			this.totalPromotion = "-";
		}
	}
	public String getFullyPaid() {
		return fullyPaid;
	}
	public void setFullyPaid(String fullyPaid) {
		this.fullyPaid = fullyPaid;
	}
	public String getGrossSupport() {
		return grossSupport;
	}
	public void setGrossSupport(String grossSupport) {
		this.grossSupport = grossSupport;
	}
	public String getStd() {
		return std;
	}
	public void setStd(String std) {
		this.std = std;
	}
	public String getAak() {
		return aak;
	}
	public void setAak(String aak) {
		this.aak = aak;
	}
	public String getCustomerIncentive() {
		return customerIncentive;
	}
	public void setCustomerIncentive(String customerIncentive) {
		this.customerIncentive = customerIncentive;
	}
	public String getPresent() {
		return present;
	}
	public void setPresent(String present) {
		this.present = present;
	}
	public String getInsurance() {
		return insurance;
	}
	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}
	public String getMaintenance() {
		return maintenance;
	}
	public void setMaintenance(String maintenance) {
		this.maintenance = maintenance;
	}
	public String getPersonReward() {
		return personReward;
	}
	public void setPersonReward(String personReward) {
		this.personReward = personReward;
	}
	public String getFinanceLoan() {
		return financeLoan;
	}
	public void setFinanceLoan(String financeLoan) {
		this.financeLoan = financeLoan;
	}
	public String getDisplacesSupport() {
		return displacesSupport;
	}
	public void setDisplacesSupport(String displacesSupport) {
		this.displacesSupport = displacesSupport;
	}
	public String getInvoicePrice() {
		return invoicePrice;
	}
	public void setInvoicePrice(String invoicePrice) {
		this.invoicePrice = invoicePrice;
	}
	public String getGrossCost() {
		return grossCost;
	}
	public void setGrossCost(String grossCost) {
		this.grossCost = grossCost;
		if(AppFrameworkUtil.isEmpty(grossCost)) {
			this.grossCost = "-";
		}
	}
	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
		if(AppFrameworkUtil.isEmpty(tp)) {
			this.tp = "-";
		}
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public Double getProfitRate() {
		return profitRate;
	}
	public void setProfitRate(Double profitRate) {
		this.profitRate = profitRate;
	}
	public String getLastVersionSale() {
		return lastVersionSale;
	}
	public void setLastVersionSale(String lastVersionSale) {
		this.lastVersionSale = lastVersionSale;
	}
	public String getVersionSale() {
		return versionSale;
	}
	public void setVersionSale(String versionSale) {
		this.versionSale = versionSale;
		if(AppFrameworkUtil.isEmpty(versionSale)) {
			this.versionSale = "-";
		}
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
	public String getIsBase() {
		return isBase;
	}
	public void setIsBase(String isBase) {
		this.isBase = isBase;
	}
}