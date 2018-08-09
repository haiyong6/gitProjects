package com.ways.app.price.model;

import java.io.Serializable;
import com.ways.app.framework.utils.AppFrameworkUtil;

/**
 * 型号实体类
 * @author yinlue
 *
 */
public class Version implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//型号ID
	public String versionId;
	//型号名称
	public String versionName;
	//型号指导价
	public String msrp;
	//型号所属级别中文
	public String gradeName;
	//型号所属级别英文
	public String gradeNameEn;
	//厂商中文
	public String manfName;
	//厂商英文
	public String manfNameEn;
	//子车型中文
	public String subModelName;
	//子车型英文
	public String subModelNameEn;
	//排量
	public String discharge;
	//排档方式中文
	public String gearMode;
	//排档方式英文
	public String gearModeEn;
	//车身形式中文
	public String bodyType;
	//车身形式英文
	public String bodyTypeEn;
	//年款
	public String modelYear;
	//型号标识中文
	public String typeId;
	//型号标识英文
	public String typeIdEn;
	//是否一汽大众本品车型1:是 0:否
	public String isBase;
	//型号占全国MIX
	public String mix;
	
	/**
	* @Fields letter : 型号排序(一汽大众常用选项)
	*/
	private String letter;
	
	public String getMix() {
		return mix;
	}
	public void setMix(String mix) {
		this.mix = mix;
		if(AppFrameworkUtil.isEmpty(mix)) this.mix = "-";
	}
	public String getIsBase() {
		return isBase;
	}
	public void setIsBase(String isBase) {
		this.isBase = isBase;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getMsrp() {
		return msrp;
	}
	public void setMsrp(String msrp) {
		this.msrp = msrp;
		if(AppFrameworkUtil.isEmpty(msrp)) this.msrp = "-";
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
	public String getDischarge() {
		return discharge;
	}
	public void setDischarge(String discharge) {
		this.discharge = discharge;
	}
	public String getGearMode() {
		return gearMode;
	}
	public void setGearMode(String gearMode) {
		this.gearMode = gearMode;
	}
	public String getGearModeEn() {
		return gearModeEn;
	}
	public void setGearModeEn(String gearModeEn) {
		this.gearModeEn = gearModeEn;
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
	public String getModelYear() {
		return modelYear;
	}
	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeIdEn() {
		return typeIdEn;
	}
	public void setTypeIdEn(String typeIdEn) {
		this.typeIdEn = typeIdEn;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	
}
