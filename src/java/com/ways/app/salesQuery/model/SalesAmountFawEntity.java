package com.ways.app.salesQuery.model;

public class SalesAmountFawEntity {
	
	private String gradeName;//级别
	private String gradeNameEn;//级别英文
	private String manfName;//厂商
	private String manfNameEn;//厂商英文
	private String objId;//对象ID
	private String objName;//对象
	private String objNameEn;//对象英文
	private String yearMonth;//年月
	private String sales;//销量
	private String percent;//占比
	
	
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
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
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	
	
}
