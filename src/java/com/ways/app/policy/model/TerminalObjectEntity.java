package com.ways.app.policy.model;

import java.io.Serializable;

import com.ways.app.framework.utils.AppFrameworkUtil;

/**终端支持对象实体类
 * ClassName: TerminalObjectEntity
 * @Description: TODO
 * @author ruanrf
 * @date Jan 14, 2016
 */
public class TerminalObjectEntity implements Serializable {

	private static final long serialVersionUID = 8108512821919134609L;
	/**
	 * @Fields objectId : 对象ID
	 */
	private String objectId;
	/**
	 * @Fields objectName : 对象名称
	 */
	private String objectName;
	/**
	 * @Fields objectNameEn : 对象英文名称
	 */
	private String objectNameEn;
	/**
	 * @Fields dateTime : 时间
	 */
	private String dateTime;
	/**
	 * @Fields subsidyTypeId : 促销类型ID
	 */
	private String subsidyTypeId;
	/**
	 * @Fields subsidyType : 促销类型名称
	 */
	private String subsidyType;
	/**
	 * @Fields subsidy : 促销价格
	 */
	private String subsidy;
	/**
	 * @Fields subsidy : 促销价格
	 */
	private String VersionSubsidy;
	
	
	
	public String getVersionSubsidy() {
		return VersionSubsidy;
	}

	public void setVersionSubsidy(String versionSubsidy) {
		VersionSubsidy = versionSubsidy;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getObjectNameEn() {
		return objectNameEn;
	}

	public void setObjectNameEn(String objectNameEn) {
		this.objectNameEn = objectNameEn;
	}
	
	public String getSubsidyTypeId() {
		return subsidyTypeId;
	}

	public void setSubsidyTypeId(String subsidyTypeId) {
		this.subsidyTypeId = subsidyTypeId;
	}

	public String getSubsidyType() {
		return subsidyType;
	}

	public void setSubsidyType(String subsidyType) {
		this.subsidyType = subsidyType;
	}

	public String getSubsidy() {
		return subsidy;
	}

	public void setSubsidy(String subsidy) {
		this.subsidy = subsidy;
		if(AppFrameworkUtil.isEmpty(subsidy)) this.subsidy = "-";
	}
}
