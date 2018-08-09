package com.ways.app.price.model;

import java.io.Serializable;

/**
 * Echarts画辅助线实体类
 * @author yinlue
 *
 */
public class EchartsMarkLineEntity implements Serializable{
	
  private static final long serialVersionUID = 1L;
  //X轴坐标
  private int xAxis;
  //Y轴坐标
  private String yAxis;
  //线名称
  private String name;
  //线值
  private String value;

  public EchartsMarkLineEntity()
  {
	  
  }

  public EchartsMarkLineEntity(int xAxis, String yAxis)
  {
    this.xAxis = xAxis;
    this.yAxis = yAxis;
  }

  public int getxAxis() {
    return this.xAxis;
  }
  public void setxAxis(int xAxis) {
    this.xAxis = xAxis;
  }
  public String getyAxis() {
    return this.yAxis;
  }
  public void setyAxis(String yAxis) {
    this.yAxis = yAxis;
  }
  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getValue() {
    return this.value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  
}