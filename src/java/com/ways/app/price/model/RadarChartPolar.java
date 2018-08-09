package com.ways.app.price.model;

import java.io.Serializable;

/**
 * 雷达图范围边界实体
 * @author yinlue
 *
 */
public class RadarChartPolar implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//雷达点文本
	private String text;
	//边界最大值
	private int max;
	//边界最小值
	private int min;
	
	public RadarChartPolar() {
		
	}
	
	public RadarChartPolar(String text,int max,int min) {
		this.text = text;
		this.max = max;
		this.min = min;
	}
	
	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	
	
	
}
