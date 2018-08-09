package com.ways.app.price.model;

/**
 * Echart数据标注实体
 * @author yinlue
 *
 */
public class EchartMarkPointEntity {

	//标注名称
	private String name;
	//标注值
	private String value;
	//X轴索引或值
	private String xAxis;
	//Y轴索引或值
	private String yAxis;
	//标注大小
	private String symbolSize;
	//标注图形
	private String symbol;
	
	public EchartMarkPointEntity() 
	{
		
	}
	
	public EchartMarkPointEntity(String name,String value,String xAxis,String yAxis,String symbolSize) 
	{
		this.name = name; 
		this.value = value; 
		this.xAxis = xAxis; 
		this.yAxis = Integer.parseInt(yAxis) * 0.99 + "";
		this.symbolSize = symbolSize;
		this.symbol = "triangle";
	}
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getxAxis() {
		return xAxis;
	}
	public void setxAxis(String xAxis) {
		this.xAxis = xAxis;
	}
	public String getyAxis() {
		return yAxis;
	}
	public void setyAxis(String yAxis) {
		this.yAxis = yAxis;
	}
	public String getSymbolSize() {
		return symbolSize;
	}
	public void setSymbolSize(String symbolSize) {
		this.symbolSize = symbolSize;
	}
}
