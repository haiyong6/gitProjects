package com.ways.app.price.model;

import java.util.List;
/**
 * 车型价格段分析实体类
 * 
 * @author songguobiao
 * @date 20161107
 */
public class SubModelPriceAnalysis {
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
	 * 品牌名称
	 */
	private String brandName;
	/**
	 * 品牌英文名称
	 */
	private String brandNameEn;
	/**
	 * 厂商名称
	 */
	private String manfName;
	/**
	 * 厂商英文名称
	 */
	private String manfNameEn;
	/**
	 * 级别英文名称
	 */
	private String gradeNameEn;
	/**
	 * 本品排序
	 */
	private String modelSort;
	/**
	 * 最小价格
	 */
	private String minPrice;
	/**
	 * 最大价格
	 */
	private String maxPrice;
	/**
	 * 最小指导价
	 */
	private String minMsrp;
	/**
	 * 最大指导价
	 */
	private String maxMsrp;
	/**
	 * 最小成交价
	 */
	private String minTp;
	/**
	 * 最大成交价
	 */
	private String maxTp;
	/**
	 * 该车型型号集合
	 */
	private List<SubModelPriceAnalysisEntity> list;

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

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandNameEn() {
		return brandNameEn;
	}

	public void setBrandNameEn(String brandNameEn) {
		this.brandNameEn = brandNameEn;
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

	public String getGradeNameEn() {
		return gradeNameEn;
	}

	public void setGradeNameEn(String gradeNameEn) {
		this.gradeNameEn = gradeNameEn;
	}
    
	public String getModelSort() {
		return modelSort;
	}
	
	public void setModelSort(String modelSort) {
		this.modelSort = modelSort;
	}
	
	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getMinMsrp() {
		return minMsrp;
	}

	public void setMinMsrp(String minMsrp) {
		this.minMsrp = minMsrp;
	}

	public String getMaxMsrp() {
		return maxMsrp;
	}

	public void setMaxMsrp(String maxMsrp) {
		this.maxMsrp = maxMsrp;
	}

	public String getMinTp() {
		return minTp;
	}

	public void setMinTp(String minTp) {
		this.minTp = minTp;
	}

	public String getMaxTp() {
		return maxTp;
	}

	public void setMaxTp(String maxTp) {
		this.maxTp = maxTp;
	}

	public List<SubModelPriceAnalysisEntity> getList() {
		return list;
	}

	public void setList(List<SubModelPriceAnalysisEntity> list) {
		this.list = list;
	}
}
