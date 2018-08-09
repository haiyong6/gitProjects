package com.ways.app.pricesale.model;

import java.util.List;

public class ScaleBySubModelSale {
	
	/**
	 * 车型ID
	 */
	private String subModelId;
	/**
	 * 车型名称
	 */
	private String subModelName;
	/**
	 * 车型英文名称
	 */
	private String subModelNameEn;
	
	/**
	 * 该车型型号集合
	 */
	private List<ScaleBySubModelSaleEntity> list;
	
	public String getSubModelId() {
		return subModelId;
	}
	public void setSubModelId(String subModelId) {
		this.subModelId = subModelId;
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
	public List<ScaleBySubModelSaleEntity> getList() {
		return list;
	}
	public void setList(List<ScaleBySubModelSaleEntity> list) {
		this.list = list;
	}
}
