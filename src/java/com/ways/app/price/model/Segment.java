package com.ways.app.price.model;

import java.io.Serializable;
import java.util.List;

/**
 * 细分市场实体类
 * @author yinlue
 *
 */
public class Segment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//细分市场ID
	private String segmentId;
	//细分市场名称
	private String segmentName;
	//子细分市场集合
	private List<Segment> segmentList;
	//子车型集合
	private List<SubModel> subModelList;
	
	public String getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}
	public String getSegmentName() {
		return segmentName;
	}
	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}
	public List<Segment> getSegmentList() {
		return segmentList;
	}
	public void setSegmentList(List<Segment> segmentList) {
		this.segmentList = segmentList;
	}
	public List<SubModel> getSubModelList() {
		return subModelList;
	}
	public void setSubModelList(List<SubModel> subModelList) {
		this.subModelList = subModelList;
	}
}
