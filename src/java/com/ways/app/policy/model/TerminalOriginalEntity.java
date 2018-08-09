package com.ways.app.policy.model;

import java.io.Serializable;
import java.util.List;

public class TerminalOriginalEntity implements Serializable{
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 6116264786571995423L;
	/**
	 * @Fields Vid : 型号ID
	 */
	private String Vid;
	/**
	 * @Fields Versionname : 型号名称
	 */
	private String Versionname;
	/**
	 * @Fields Versionnameen : 型号英文名称
	 */
	private String Versionnameen;
	/**
	 * @Fields Versioncode : 型号编码
	 */
	private String Versioncode;
	/**
	 * @Fields Modelyear : 年款
	 */
	private String Modelyear;
	/**
	 * @Fields Versionlaunchdate : 上市日期
	 */
	private String Versionlaunchdate;
	/**
	 * @Fields Versionshortname : 型号标识
	 */
	private String Versionshortname;
	/**
	 * @Fields Versionshortnameen : 型号标识英文
	 */
	private String Versionshortnameen;
	/**
	 * @Fields Manfid : 厂商ID
	 */
	private String Manfid;
	/**
	 * @Fields Manfname : 厂商名称
	 */
	private String Manfname;
	/**
	 * @Fields Manfnameen : 厂商名称英文
	 */
	private String Manfnameen;
	/**
	 * @Fields Brandid : 品牌ID
	 */
	private String Brandid;
	/**
	 * @Fields Brandname : 品牌名称
	 */
	private String Brandname;
	/**
	 * @Fields Brandnameen : 品牌英文名称
	 */
	private String Brandnameen;
	/**
	 * @Fields Origid : 系别ID
	 */
	private String Origid;
	/**
	 * @Fields Origname : 系别名称
	 */
	private String Origname;
	/**
	 * @Fields Orignameen : 系别英文名称
	 */
	private String Orignameen;
	/**
	 * @Fields Submodelid : 子车型ID
	 */
	private String Submodelid;
	/**
	 * @Fields Submodelname : 子车型名称
	 */
	private String Submodelname;
	/**
	 * @Fields Submodelnameen : 子车型英文名称
	 */
	private String Submodelnameen;
	/**
	 * @Fields Discharge : 排量
	 */
	private String Discharge;
	/**
	 * @Fields Gearmode : 排挡方式
	 */
	private String Gearmode;
	/**
	 * @Fields Gearmodeen : 排挡方式英文
	 */
	private String Gearmodeen;
	/**
	 * @Fields Bodytype : 车身形势
	 */
	private String Bodytype;
	/**
	 * @Fields Bodytypeen : 车身形势名称
	 */
	private String Bodytypeen;
	/**
	 * @Fields Gradeid : 细分市场ID
	 */
	private String Gradeid;
	/**
	 * @Fields Gradename : 细分市场名称
	 */
	private String Gradename;
	/**
	 * @Fields Gradenameen : 细分市场英文名称
	 */
	private String Gradenameen;
	/**
	 * @Fields Yearmonth : 年月
	 */
	private String Yearmonth;
	/**
	 * @Fields Year : 年
	 */
	private String Year;
	/**
	 * @Fields Month : 月
	 */
	private String Month;
	/**
	 * @Fields Quarter : 季
	 */
	private String Quarter;
	/**
	 * @Fields pList : 促销价格List
	 */
	private List<PromotionInfo> pList;
	
	public String getVid() {
		return Vid;
	}
	public void setVid(String vid) {
		Vid = vid;
	}
	public String getVersionname() {
		return Versionname;
	}
	public void setVersionname(String versionname) {
		Versionname = versionname;
	}
	public String getVersionnameen() {
		return Versionnameen;
	}
	public void setVersionnameen(String versionnameen) {
		Versionnameen = versionnameen;
	}
	public String getVersioncode() {
		return Versioncode;
	}
	public void setVersioncode(String versioncode) {
		Versioncode = versioncode;
	}
	public String getModelyear() {
		return Modelyear;
	}
	public void setModelyear(String modelyear) {
		Modelyear = modelyear;
	}
	public String getVersionlaunchdate() {
		return Versionlaunchdate;
	}
	public void setVersionlaunchdate(String versionlaunchdate) {
		Versionlaunchdate = versionlaunchdate;
	}
	public String getVersionshortname() {
		return Versionshortname;
	}
	public void setVersionshortname(String versionshortname) {
		Versionshortname = versionshortname;
	}
	public String getVersionshortnameen() {
		return Versionshortnameen;
	}
	public void setVersionshortnameen(String versionshortnameen) {
		Versionshortnameen = versionshortnameen;
	}
	public String getManfid() {
		return Manfid;
	}
	public void setManfid(String manfid) {
		Manfid = manfid;
	}
	public String getManfname() {
		return Manfname;
	}
	public void setManfname(String manfname) {
		Manfname = manfname;
	}
	public String getManfnameen() {
		return Manfnameen;
	}
	public void setManfnameen(String manfnameen) {
		Manfnameen = manfnameen;
	}
	public String getBrandid() {
		return Brandid;
	}
	public void setBrandid(String brandid) {
		Brandid = brandid;
	}
	public String getBrandname() {
		return Brandname;
	}
	public void setBrandname(String brandname) {
		Brandname = brandname;
	}
	public String getBrandnameen() {
		return Brandnameen;
	}
	public void setBrandnameen(String brandnameen) {
		Brandnameen = brandnameen;
	}
	public String getOrigid() {
		return Origid;
	}
	public void setOrigid(String origid) {
		Origid = origid;
	}
	public String getOrigname() {
		return Origname;
	}
	public void setOrigname(String origname) {
		Origname = origname;
	}
	public String getOrignameen() {
		return Orignameen;
	}
	public void setOrignameen(String orignameen) {
		Orignameen = orignameen;
	}
	public String getSubmodelid() {
		return Submodelid;
	}
	public void setSubmodelid(String submodelid) {
		Submodelid = submodelid;
	}
	public String getSubmodelname() {
		return Submodelname;
	}
	public void setSubmodelname(String submodelname) {
		Submodelname = submodelname;
	}
	public String getSubmodelnameen() {
		return Submodelnameen;
	}
	public void setSubmodelnameen(String submodelnameen) {
		Submodelnameen = submodelnameen;
	}
	public String getDischarge() {
		return Discharge;
	}
	public void setDischarge(String discharge) {
		Discharge = discharge;
	}
	public String getGearmode() {
		return Gearmode;
	}
	public void setGearmode(String gearmode) {
		Gearmode = gearmode;
	}
	public String getGearmodeen() {
		return Gearmodeen;
	}
	public void setGearmodeen(String gearmodeen) {
		Gearmodeen = gearmodeen;
	}
	public String getBodytype() {
		return Bodytype;
	}
	public void setBodytype(String bodytype) {
		Bodytype = bodytype;
	}
	public String getBodytypeen() {
		return Bodytypeen;
	}
	public void setBodytypeen(String bodytypeen) {
		Bodytypeen = bodytypeen;
	}
	public String getGradeid() {
		return Gradeid;
	}
	public void setGradeid(String gradeid) {
		Gradeid = gradeid;
	}
	public String getGradename() {
		return Gradename;
	}
	public void setGradename(String gradename) {
		Gradename = gradename;
	}
	public String getGradenameen() {
		return Gradenameen;
	}
	public void setGradenameen(String gradenameen) {
		Gradenameen = gradenameen;
	}
	public String getYearmonth() {
		return Yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		Yearmonth = yearmonth;
	}
	public String getYear() {
		return Year;
	}
	public void setYear(String year) {
		Year = year;
	}
	public String getMonth() {
		return Month;
	}
	public void setMonth(String month) {
		Month = month;
	}
	public String getQuarter() {
		return Quarter;
	}
	public void setQuarter(String quarter) {
		Quarter = quarter;
	}
	public List<PromotionInfo> getPList() {
		return pList;
	}
	public void setPList(List<PromotionInfo> list) {
		pList = list;
	}
}
