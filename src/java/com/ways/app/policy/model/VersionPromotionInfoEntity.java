package com.ways.app.policy.model;

import java.io.Serializable;
import com.ways.app.framework.utils.AppFrameworkUtil;
import com.ways.app.price.model.Version;

/**
 * 型号价格信息实体类
 * @author huangwenmei
 *
 */
public class VersionPromotionInfoEntity extends Version implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//年月
	public String yearMonth;
	//型号Id 唯一标识
	public String vid;
	//型号编码
	public String versionCode;
	//成交价
	public String tp;
	//指导价
	public String msrp;
	//车身级别
	public String segment;
	//考核激励
	public String bonus;
	//返利
	public String margin;
	
	//经销商支持
	public String grossSupports;
	//用户激励
	public String customerIncentive;
	//礼品
	public String presents;
	//保险
	public String insurance;
	//保养
	public String maintenance;
	//开票价
	public String invoicePrice;
	//经销商开票价
	public String grossInvoicePrice;
	//利润
	public String profit;
	//利润率
	public Double profitRate;
	//型号上月销量
	public String versionLastMonthSales;
	
	//型号当月销量
	public String versionSale;
	//型号图形展示名称
	public String versionChartName;
	//型号全称中文
	public String versionName;
	//型号全称英文
	public String versionNameEn;
	//型号上市日期
	public String versionLaunchDate;
	//城市ID
	public String cityId;
	//城市名称
	public String cityName;
	//城市英文名称
	public String cityNameEn;
	//型号上下代变化名称
	public String changName;
	
	//区域ID
	public String areaId;
	//区或名称
	public String areaName;
	//区域英文名称
	public String areaNameEn;
	
	//促销总额
	private String c1;
	//提车支持
	private String c2;
    
    //零售支持
	private String c3;
    
    //人员奖励
	private String c4;
    
    //金融贷款
	private String c5;
    
    //置换支持 
	private String c6;
     
    //赠送保险 
	private String c7;
     
    //赠送礼品（油卡、保养）
	private String c8;
     
    //选择促销 	
	private String c9;
	//
	private String subsidyType;
	//型号mix
	private String versionMix;

	//系别
	private String origId;
	private String origName;
	private String origNameEn;
	//品牌
	private String brandId;
	private String brandName;
	private String brandNameEn;
	
	private String submodelName;//车型利润分析型号维度子车型名字
	private String submodelNameEn;
	
	private String objName;
	private String objNameEn;
	//型号名称简写
	private String  versionShortName;
	
	//型号英文名称简写
	private String  versionShortNameEn;
	//全款购车促销支持
	private String fullyPaid;
	
	
	public String getSubmodelName() {
		return submodelName;
	}

	public void setSubmodelName(String submodelName) {
		this.submodelName = submodelName;
	}

	public String getSubmodelNameEn() {
		return submodelNameEn;
	}

	public void setSubmodelNameEn(String submodelNameEn) {
		this.submodelNameEn = submodelNameEn;
	}

	public String getFullyPaid() {
		return fullyPaid;
	}

	public void setFullyPaid(String fullyPaid) {
		this.fullyPaid = fullyPaid;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getSubsidyType() {
		return subsidyType;
	}

	public void setSubsidyType(String subsidyType) {
		this.subsidyType = subsidyType;
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

	public String getOrigId() {
		return origId;
	}

	public void setOrigId(String origId) {
		this.origId = origId;
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

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
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

	
	public String getAreaNameEn() {
		return areaNameEn;
	}

	public void setAreaNameEn(String areaNameEn) {
		this.areaNameEn = areaNameEn;
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

	public String getVersionSale() {
		return versionSale;
	}

	public void setVersionSale(String versionSale) {
		this.versionSale = versionSale;
		if(AppFrameworkUtil.isEmpty(versionSale)) this.versionSale = "-";
	}
	
	//促销总额
	public String getC1() {
		return c1;
	}
	//促销总额
	public void setC1(String c1) {
		this.c1 = c1;
	}

	//提车支持
	public String getC2() {
		return c2;
	}
	//提车支持
	public void setC2(String c2) {
		this.c2 = c2;
	}

	//零售支持
	public String getC3() {
		return c3;
	}
	//零售支持
	public void setC3(String c3) {
		this.c3 = c3;
	}

	//人员奖励
	public String getC4() {
		return c4;
	}

	//人员奖励
	public void setC4(String c4) {
		this.c4 = c4;
	}

	//金融贷款
	public String getC5() {
		return c5;
	}

	//金融贷款
	public void setC5(String c5) {
		this.c5 = c5;
	}

	//置换支持 
	public String getC6() {
		return c6;
	}

	//置换支持 
	public void setC6(String c6) {
		this.c6 = c6;
	}

	//赠送保险 
	public String getC7() {
		return c7;
	}

	//赠送保险 
	public void setC7(String c7) {
		this.c7 = c7;
	}

	//赠送礼品（油卡、保养）
	public String getC8() {
		return c8;
	}

	//赠送礼品（油卡、保养）
	public void setC8(String c8) {
		this.c8 = c8;
	}

	//选择促销 	
	public String getC9() {
		return c9;
	}

	//选择促销 	
	public void setC9(String c9) {
		this.c9 = c9;
	}
	
	//型号mix
	public String getVersionMix() {
		return versionMix;
	}

	//型号mix
	public void setVersionMix(String versionMix) {
		this.versionMix = versionMix;
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

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String getBonus() {
		return bonus;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public String getGrossSupports() {
		return grossSupports;
	}

	public void setGrossSupports(String grossSupports) {
		this.grossSupports = grossSupports;
	}

	public String getCustomerIncentive() {
		return customerIncentive;
	}

	public void setCustomerIncentive(String customerIncentive) {
		this.customerIncentive = customerIncentive;
	}

	public String getPresents() {
		return presents;
	}

	public void setPresents(String presents) {
		this.presents = presents;
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

	public String getInvoicePrice() {
		return invoicePrice;
	}

	public void setInvoicePrice(String invoicePrice) {
		this.invoicePrice = invoicePrice;
	}

	public String getGrossInvoicePrice() {
		return grossInvoicePrice;
	}

	public void setGrossInvoicePrice(String grossInvoicePrice) {
		this.grossInvoicePrice = grossInvoicePrice;
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

	public String getVersionLastMonthSales() {
		return versionLastMonthSales;
	}

	public void setVersionLastMonthSales(String versionLastMonthSales) {
		this.versionLastMonthSales = versionLastMonthSales;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
}