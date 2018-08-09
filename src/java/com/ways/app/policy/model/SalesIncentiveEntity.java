package com.ways.app.policy.model;

import java.io.Serializable;

/**
 * 销售激励信息实体类
 * @author huangwenmei
 *
 */
public class SalesIncentiveEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String vid;
	private String ym;   //日期
	private String manfname;  //厂商
	private String manfnameEn;
	private String segment;   //车型级别
	private String subsegment;  //细分市场
	private String bodytype;   //车身形式
	private String bodytypeEn;
	private String brand;  //品牌
	private String brandEn;
	private String model;    //车型
	private String modelEn;
	private String versionname;  //型号全称
	private String versionnameEn;
	private String code;        //型号编码
	private String MSRP;        //指导价
	private String margin;      //激励额
	private String bonus;       //考核奖励
	private String totaltactical;  //总促销
	private String grosssupports; //内促 经销商支持
	private String STD;    //提车支持
	private String AAK;    //零售支持
	private String customerincentive;//用户激励
	private String presents;   //礼品
	private String insurance;  //保险
	private String maintenance;  //保养
	private String staffreward;   //人员奖励
	private String financialloan;   //金融贷款
	private String tradeinsupport;  //置换支持
	private String invoiceprice; //开票价
	private String grossinvoiceprice;//经销商开票价 
	private String grosscost;//经销商成本
	private String TP;   //成交价
	private String profit;  // 利润
	private Double profitrate;  // 利润率
	private String versionlastmonthsales;//上个月的销量
	private String versionmonthsales;//当月销量
	private String launchdate;//上市日期
	private String fullyPaid;//全款购车促销支持
	
	
	
	public String getFullyPaid() {
		return fullyPaid;
	}
	public void setFullyPaid(String fullyPaid) {
		this.fullyPaid = fullyPaid;
	}
	public String getGrosscost() {
		return grosscost;
	}
	public void setGrosscost(String grosscost) {
		this.grosscost = grosscost;
	}
	public String getLaunchdate() {
		return launchdate;
	}
	public void setLaunchdate(String launchdate) {
		this.launchdate = launchdate;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public String getYm() {
		return ym;
	}
	public void setYm(String ym) {
		this.ym = ym;
	}
	public String getManfname() {
		return manfname;
	}
	public void setManfname(String manfname) {
		this.manfname = manfname;
	}
	public String getManfnameEn() {
		return manfnameEn;
	}
	public void setManfnameEn(String manfnameEn) {
		this.manfnameEn = manfnameEn;
	}
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public String getSubsegment() {
		return subsegment;
	}
	public void setSubsegment(String subsegment) {
		this.subsegment = subsegment;
	}
	public String getBodytype() {
		return bodytype;
	}
	public void setBodytype(String bodytype) {
		this.bodytype = bodytype;
	}
	public String getBodytypeEn() {
		return bodytypeEn;
	}
	public void setBodytypeEn(String bodytypeEn) {
		this.bodytypeEn = bodytypeEn;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getBrandEn() {
		return brandEn;
	}
	public void setBrandEn(String brandEn) {
		this.brandEn = brandEn;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getModelEn() {
		return modelEn;
	}
	public void setModelEn(String modelEn) {
		this.modelEn = modelEn;
	}
	public String getVersionname() {
		return versionname;
	}
	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}
	public String getVersionnameEn() {
		return versionnameEn;
	}
	public void setVersionnameEn(String versionnameEn) {
		this.versionnameEn = versionnameEn;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMSRP() {
		return MSRP;
	}
	public void setMSRP(String mSRP) {
		MSRP = mSRP;
	}
	public String getMargin() {
		return margin;
	}
	public void setMargin(String margin) {
		this.margin = margin;
	}
	public String getBonus() {
		return bonus;
	}
	public void setBonus(String bonus) {
		this.bonus = bonus;
	}
	public String getTotaltactical() {
		return totaltactical;
	}
	public void setTotaltactical(String totaltactical) {
		this.totaltactical = totaltactical;
	}
	public String getGrosssupports() {
		return grosssupports;
	}
	public void setGrosssupports(String grosssupports) {
		this.grosssupports = grosssupports;
	}
	public String getSTD() {
		return STD;
	}
	public void setSTD(String sTD) {
		STD = sTD;
	}
	public String getAAK() {
		return AAK;
	}
	public void setAAK(String aAK) {
		AAK = aAK;
	}
	public String getCustomerincentive() {
		return customerincentive;
	}
	public void setCustomerincentive(String customerincentive) {
		this.customerincentive = customerincentive;
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
	public String getStaffreward() {
		return staffreward;
	}
	public void setStaffreward(String staffreward) {
		this.staffreward = staffreward;
	}
	public String getFinancialloan() {
		return financialloan;
	}
	public void setFinancialloan(String financialloan) {
		this.financialloan = financialloan;
	}
	public String getTradeinsupport() {
		return tradeinsupport;
	}
	public void setTradeinsupport(String tradeinsupport) {
		this.tradeinsupport = tradeinsupport;
	}
	public String getInvoiceprice() {
		return invoiceprice;
	}
	public void setInvoiceprice(String invoiceprice) {
		this.invoiceprice = invoiceprice;
	}
	public String getGrossinvoiceprice() {
		return grossinvoiceprice;
	}
	public void setGrossinvoiceprice(String grossinvoiceprice) {
		this.grossinvoiceprice = grossinvoiceprice;
	}
	public String getTP() {
		return TP;
	}
	public void setTP(String tP) {
		TP = tP;
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public Double getProfitrate() {
		return profitrate;
	}
	public void setProfitrate(Double profitrate) {
		this.profitrate = profitrate;
	}
	public String getVersionlastmonthsales() {
		return versionlastmonthsales;
	}
	public void setVersionlastmonthsales(String versionlastmonthsales) {
		this.versionlastmonthsales = versionlastmonthsales;
	}
	public String getVersionmonthsales() {
		return versionmonthsales;
	}
	public void setVersionmonthsales(String versionmonthsales) {
		this.versionmonthsales = versionmonthsales;
	}
	
	
}