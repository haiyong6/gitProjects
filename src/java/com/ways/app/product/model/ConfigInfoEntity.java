package com.ways.app.product.model;

import java.io.Serializable;

/**
 * 配置信息实体
 * @author yinlue
 *
 */
public class ConfigInfoEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//配置ID
	private String configId;
	//配置名称
	private String configName;
	//配置英文名称
	private String configNameEn;
	//配置类型
	private String configType;
	
	
	public String getConfigId() {
		return configId;
	}
	public void setConfigId(String configId) {
		this.configId = configId;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public String getConfigNameEn() {
		return configNameEn;
	}
	public void setConfigNameEn(String configNameEn) {
		this.configNameEn = configNameEn;
	}
	public String getConfigType() {
		return configType;
	}
	public void setConfigType(String configType) {
		this.configType = configType;
	}
}
