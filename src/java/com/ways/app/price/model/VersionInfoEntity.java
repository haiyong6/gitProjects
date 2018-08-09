package com.ways.app.price.model;

import java.io.Serializable;

import com.ways.app.framework.utils.AppFrameworkUtil;

/**
 * 型号价格信息实体类
 * @author yinlue
 *
 */
public class VersionInfoEntity extends Version implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//年月
	private String yearMonth;
	//周
	private String week;
	//型号编码
	private String versionCode;
	//品牌名称
	private String brandName;
	//品牌英文名称
	private String brandNameEn;
	//成交价
	private String tp;
	//指导价
	private String msrp;
	//经销商成本
	private String grossCost;
	//提车支持
	private String std;
	//零售支持
	private String aak;
	//经销商支持
	private String grossSupport;
	//全款购车促销
	private String fullyPaidPromotion;
	//开票价
	private String invoicePrice;
	//返利金额
	private String rebatePrice;
	//考核奖励
	private String rewardAssessment;
	//促销补贴
	private String promotionalAllowance;
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
	//车型利润
	private String modelProfit;
	//型号销量
	private String versionSale;
	//型号图形展示名称
	private String versionChartName;
	//型号全称英文
	private String versionNameEn;
	//型号上市日期
	private String versionLaunchDate;
	//城市ID
	private String cityId;
	//城市名称
	private String cityName;
	//城市英文名称
	private String cityNameEn;
	//型号上下代变化名称
	private String changName;
	//折扣
	private String discount;
	//上月折扣变化
	private String vsDiscount;
	//折扣率
	private String discountRate;
	//折扣率变化
	private String vsDiscountRate;
	//区域ID
	private String areaId;
	//区或名称
	private String areaName;
	//区域英文名称
	private String areaNameEn;
	
	public String getAreaNameEn() {
		return areaNameEn;
	}

	public void setAreaNameEn(String areaNameEn) {
		this.areaNameEn = areaNameEn;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getVsDiscount() {
		return vsDiscount;
	}

	public void setVsDiscount(String vsDiscount) {
		this.vsDiscount = vsDiscount;
	}

	public String getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

	public String getVsDiscountRate() {
		return vsDiscountRate;
	}

	public void setVsDiscountRate(String vsDiscountRate) {
		this.vsDiscountRate = vsDiscountRate;
	}

	public String getChangName() {
		return changName;
	}

	public void setChangName(String changName) {
		this.changName = changName;
	}

	public String getCityNameEn() {
		return cityNameEn;
	}
	

	public String getVersionNameEn() {
		return versionNameEn;
	}

	public void setVersionNameEn(String versionNameEn) {
		this.versionNameEn = versionNameEn;
	}

	public void setCityNameEn(String cityNameEn) {
		this.cityNameEn = cityNameEn;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	
	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getVersionLaunchDate() {
		return versionLaunchDate;
	}

	public void setVersionLaunchDate(String versionLaunchDate) {
		this.versionLaunchDate = versionLaunchDate;
	}

	public String getVersionChartName() {
		return versionChartName;
	}

	public void setVersionChartName(String versionChartName) {
		this.versionChartName = versionChartName;
	}

	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	
	public String getMsrp() {
		return msrp;
	}

	public void setMsrp(String msrp) {
		this.msrp = msrp;
		if(AppFrameworkUtil.isEmpty(msrp)) this.msrp = "-";
	}

	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
		if(AppFrameworkUtil.isEmpty(tp)) this.tp = "-";
	}
	public String getGrossCost() {
		return grossCost;
	}
	public void setGrossCost(String grossCost) {
		this.grossCost = grossCost;
		if(AppFrameworkUtil.isEmpty(grossCost)) this.grossCost = "-";
	}
	public String getFullyPaidPromotion() {
		return fullyPaidPromotion;
	}
	public void setFullyPaidPromotion(String fullyPaidPromotion) {
		this.fullyPaidPromotion = fullyPaidPromotion;
	}
	public String getInvoicePrice() {
		return invoicePrice;
	}
	public void setInvoicePrice(String invoicePrice) {
		this.invoicePrice = invoicePrice;
		if(AppFrameworkUtil.isEmpty(invoicePrice)) this.invoicePrice = "-";
	}
	public String getRebatePrice() {
		return rebatePrice;
	}
	public void setRebatePrice(String rebatePrice) {
		this.rebatePrice = rebatePrice;
		if(AppFrameworkUtil.isEmpty(rebatePrice)) this.rebatePrice = "-";
	}
	public String getRewardAssessment() {
		return rewardAssessment;
	}
	public void setRewardAssessment(String rewardAssessment) {
		this.rewardAssessment = rewardAssessment;
		if(AppFrameworkUtil.isEmpty(rewardAssessment)) this.rewardAssessment = "-";
	}
	public String getPromotionalAllowance() {
		return promotionalAllowance;
	}
	public void setPromotionalAllowance(String promotionalAllowance) {
		this.promotionalAllowance = promotionalAllowance;
		if(AppFrameworkUtil.isEmpty(promotionalAllowance)) this.promotionalAllowance = "-";
	}
	public String getModelProfit() {
		return modelProfit;
	}
	public void setModelProfit(String modelProfit) {
		this.modelProfit = modelProfit;
		if(AppFrameworkUtil.isEmpty(modelProfit)) this.modelProfit = "-";
	}

	public String getVersionSale() {
		return versionSale;
	}

	public void setVersionSale(String versionSale) {
		this.versionSale = versionSale;
		if(AppFrameworkUtil.isEmpty(versionSale)) this.versionSale = "-";
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		int tWeek = Integer.parseInt(week);
		switch (tWeek) {
		case 1:
			this.week = "W1";
			break;
		case 2:
			this.week = "W2";
			break;
		case 3:
			this.week = "W3";
			break;
		case 4: 
			this.week = "W4";
			break;
		default:
			this.week = "M";
		}
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

	public String getGrossSupport() {
		return grossSupport;
	}

	public void setGrossSupport(String grossSupport) {
		this.grossSupport = grossSupport;
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
}
