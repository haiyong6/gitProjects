package com.ways.app.pricesale.model;

public enum PriceSaleModule {
	JIAGEDUANXIAOLIANGFENGXI("价格段销量分析","salespriceanaly");
	
	private String moduleName = null;
	private String moduleCode = null;
	
	private PriceSaleModule(String moduleName,String moduleCode) {
		this.moduleName = moduleName;
		this.moduleCode = moduleCode;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
}
