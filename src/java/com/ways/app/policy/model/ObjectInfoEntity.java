package com.ways.app.policy.model;

import java.io.Serializable;

/***********************************************************************************************
 * <br />车型/生产商/品牌/系别/级别对象的促销信息实体类
 * <br />Class name: ObjectInfoEntity.java
 * <br />Copyright 2015 Ways Company in GuangZhou. All rights reserved.
 * <br />Author: huangwenmei
 * <br />Dec 13, 2015
 * <br />@version 4.0
 * 
 ***********************************************************************************************/
public class ObjectInfoEntity implements Serializable{
	
		private static final long serialVersionUID = 1L;
		
		//子车型ID
		public String subModelId;
		//子车型名称
		public String submodelName;
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
		
		public String objName;
		public String objNameEn;
		
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
		//保险
		private String maintenance;
		//车型图形展示名称
		public String subModelChartName;
		//城市ID
		public String cityId;
		//城市名称
		public String cityName;
		//城市英文名称
		public String cityNameEn;
		
		//区域ID
		public String areaId;
		//区或名称
		public String areaName;
		//区域英文名称
		public String areaNameEn;
		
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

		public String getMaintenance() {
			return maintenance;
		}

		public void setMaintenance(String maintenance) {
			this.maintenance = maintenance;
		}

		public String getAreaNameEn() {
			return areaNameEn;
		}

		public void setAreaNameEn(String areaNameEn) {
			this.areaNameEn = areaNameEn;
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
		
		

		public String getSubModelId() {
			return subModelId;
		}

		public void setSubModelId(String subModelId) {
			this.subModelId = subModelId;
		}

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
		
	}
