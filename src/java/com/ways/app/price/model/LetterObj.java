package com.ways.app.price.model;

import java.io.Serializable;
import java.util.List;

/**
 * 品牌或厂商首字母实体类
 * @author yinlue
 *
 */
public class LetterObj implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//首字母
	private String letter;
	//对象集合
	private List<?> objList;
	
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	public List<?> getObjList() {
		return objList;
	}
	public void setObjList(List<?> objList) {
		this.objList = objList;
	}
}
