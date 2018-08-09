package com.ways.app.price.model;

import java.io.Serializable;
import java.util.List;

/**
 * Echarts线图数据实体
 * @author yinlue
 *
 */
public class EchartLineDataEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//图形类型
	private String type;
	//图形名称
	private String name;
	//图形数据
	private String[] data;
	//数据点标记
	private String symbol;
	//堆积标示
	private String stack;
	//数据点标记大小
	private int symbolSize;
	//是否本品标题，供排序使用
	private String isBase;
	//数据标记汽泡对象
	private List<EchartMarkPointEntity> markPointList;
	
	public EchartLineDataEntity() {
		
	}
	
	public EchartLineDataEntity(String type,String name,String symbol,String[] data,List<EchartMarkPointEntity> markPointList,String isBase,int symbolSize) {
		this.type = type;
		this.name = name;
		this.symbol = symbol;
		this.data = data;
		this.markPointList = markPointList;
		this.isBase = isBase;
		this.symbolSize = symbolSize;
	}
	
	public EchartLineDataEntity(String type,String name,String symbol,String[] data) {
		this.type = type;
		this.name = name;
		this.symbol = symbol;
		this.data = data;
		this.symbolSize = 4;
	}
	
	public EchartLineDataEntity(String type,String name,String symbol,String stack,String[] data) {
		this.type = type;
		this.name = name;
		this.symbol = symbol;
		this.stack = stack;
		this.data = data;
		this.symbolSize = 4;
	}
	
	public int getSymbolSize() {
		return symbolSize;
	}

	public void setSymbolSize(int symbolSize) {
		this.symbolSize = symbolSize;
	}

	public String getIsBase() {
		return isBase;
	}

	public void setIsBase(String isBase) {
		this.isBase = isBase;
	}

	public List<EchartMarkPointEntity> getMarkPointList() {
		return markPointList;
	}

	public void setMarkPointList(List<EchartMarkPointEntity> markPointList) {
		this.markPointList = markPointList;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getData() {
		return data;
	}
	public void setData(String[] data) {
		this.data = data;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getStack() {
		return stack;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}
	
}
