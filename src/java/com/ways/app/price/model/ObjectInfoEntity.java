package com.ways.app.price.model;

import java.io.Serializable;

import com.ways.app.framework.utils.AppFrameworkUtil;

/***********************************************************************************************
 * <br />车型/生产商价格信息实体类
 * <br />Class name: SubModelInfoEntity.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: ruanrf
 * <br />Jun 13, 2015
 * <br />@version 4.0
 * 
 ***********************************************************************************************/
public class ObjectInfoEntity implements Serializable{
	
		private static final long serialVersionUID = 1L;
		
		//子车型ID
		public String subModelId;
		//子车型名称
		public String SubmodelName;
		//子车型英文名称
		public String submodelNameEn;
		//厂商ID
		public String manfId;
		//厂商名称
		public String manfName;
		//厂商英文名称
		public String manfNameEn;
		//级别ID
		public String gradeId;
		//级别名称
		public String gradename;
		//级别英文名称
		public String gradeNameEn;
		//是否一汽大众本品车型1:是 0:否
		public String isBase;
		//年月
		public String yearMonth;
		//批次
		public String week;
		//成交价
		public String tp;
		//指导价
		public String msrp;
		//经销商成本
		public String grossCost;
		//开票价
		public String invoicePrice;
		//返利金额
		public String rebatePrice;
		//考核奖励
		public String rewardAssessment;
		//促销补贴
		public String promotionalAllowance;
		//车型利润
		public String modelProfit;
		//车型图形展示名称
		public String subModelChartName;
		//城市ID
		public String cityId;
		//城市名称
		public String cityName;
		//城市英文名称
		public String cityNameEn;
		//折扣
		private String discount;
		//上月折扣变化
		private String vsDiscount;
		//折扣率
		private String discountRate;
		//折扣率变化
		private String vsDiscountRate;
		//区域ID
		public String areaId;
		//区或名称
		public String areaName;
		//区域英文名称
		public String areaNameEn;
		
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

		public String getCityNameEn() {
			return cityNameEn;
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

		public String getSubModelChartName() {
			return subModelChartName;
		}

		public void setSubModelChartName(String subModelChartName) {
			this.subModelChartName = subModelChartName;
		}

		public String getYearMonth() {
			return yearMonth;
		}
		public void setYearMonth(String yearMonth) {
			this.yearMonth = yearMonth;
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

		public String getSubModelId() {
			return subModelId;
		}

		public void setSubModelId(String subModelId) {
			this.subModelId = subModelId;
		}

		public String getSubmodelName() {
			return SubmodelName;
		}

		public void setSubmodelName(String submodelName) {
			SubmodelName = submodelName;
		}

		public String getSubmodelNameEn() {
			return submodelNameEn;
		}

		public void setSubmodelNameEn(String submodelNameEn) {
			this.submodelNameEn = submodelNameEn;
		}

		public String getManfId() {
			return manfId;
		}

		public void setManfId(String manfId) {
			this.manfId = manfId;
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

		public String getGradeId() {
			return gradeId;
		}

		public void setGradeId(String gradeId) {
			this.gradeId = gradeId;
		}

		public String getGradename() {
			return gradename;
		}

		public void setGradename(String gradename) {
			this.gradename = gradename;
		}

		public String getGradeNameEn() {
			return gradeNameEn;
		}

		public void setGradeNameEn(String gradeNameEn) {
			this.gradeNameEn = gradeNameEn;
		}

		public String getIsBase() {
			return isBase;
		}

		public void setIsBase(String isBase) {
			this.isBase = isBase;
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
			default:
				this.week = "M";;
			}
		}
		
	}
