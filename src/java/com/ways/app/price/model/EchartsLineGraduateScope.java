package com.ways.app.price.model;

import java.io.Serializable;

/**
 * Echarts 线图刻度范围实体类
 * @author yinlue
 *
 */
public class EchartsLineGraduateScope implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//最大值范围
	private String max;
	//最小值范围
	private String min;
	//单元值
	private String unitValue;
	
	public EchartsLineGraduateScope() 
	{
		
	}
	
	public EchartsLineGraduateScope(String min,String max,String unitValue) 
	{
		this.max = max;
		this.min = min;
		this.unitValue = unitValue;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getUnitValue() {
		return unitValue;
	}

	public void setUnitValue(String unitValue) {
		this.unitValue = unitValue;
	}

}
