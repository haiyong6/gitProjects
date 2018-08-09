package com.ways.app.policy.model;

public enum PolicyModule {
	CHEXINLIRUNFENXI("车型利润分析","brandprofitanaly");
	
	private String moduleName = null;
	private String moduleCode = null;
	
	private PolicyModule(String moduleName,String moduleCode) {
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
