package com.ways.app.price.model;

import java.io.Serializable;

import com.ways.app.framework.utils.AppFrameworkUtil;

/**
 * 型号区域信息实体类
 * @author yinlue
 *
 */
public class VersionAreaInfoEntity extends VersionInfoEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//大区ID
	private String areaId;
	//大区名称
	private String areaName;
	//大区名称英文
	private String areaNameEn;
	//城市ID
	private String cityId;
	//城市名称
	private String cityName;
	//离散度
	private String variation;
	//环比上月TP
	private String vsTp;
	//上月指导价
	private String vsMsrp;
	//环比上月TP增长率
	private String vsTpRate;
	//环比上月利润
	private String vsProfit;
	//环比上月利润增长率
	private String vsProfitRate;
	//加权TP
	private String weightTp;
	
	public String getAreaNameEn() {
		return areaNameEn;
	}
	public void setAreaNameEn(String areaNameEn) {
		this.areaNameEn = areaNameEn;
	}
	public String getWeightTp() {
		return weightTp;
	}
	public void setWeightTp(String weightTp) {
		this.weightTp = weightTp;
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
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getVariation() {
		return variation;
	}
	public void setVariation(String variation) {
		this.variation = variation;
	}
	public String getVsTp() {
		return vsTp;
	}
	public void setVsTp(String vsTp) {
		this.vsTp = vsTp;
	}
	public String getVsTpRate() {
		return vsTpRate;
	}
	public void setVsTpRate(String vsTpRate) {
		this.vsTpRate = vsTpRate;
	}
	public String getVsProfit() {
		return vsProfit;
	}
	public void setVsProfit(String vsProfit) {
		this.vsProfit = vsProfit;
		if(AppFrameworkUtil.isEmpty(vsProfit)) this.vsProfit = "-";
	}
	public String getVsProfitRate() {
		return vsProfitRate;
	}
	public void setVsProfitRate(String vsProfitRate) {
		this.vsProfitRate = vsProfitRate;
		if(AppFrameworkUtil.isEmpty(vsProfitRate)) this.vsProfitRate = "-";
	}
	public String getVsMsrp() {
		return vsMsrp;
	}
	public void setVsMsrp(String vsMsrp) {
		this.vsMsrp = vsMsrp;
	}
	
}
