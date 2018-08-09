package com.ways.framework.model;

import java.io.Serializable;
import java.util.List;

/**
 * 分页类
 * @author fzt
 *
 */
@SuppressWarnings("serial")
public class Pagination<T> implements Serializable{
	/**
	 * 默认的页面显示记录条数
	 */
	public static Integer DEFAULT_PAGE_SIZE = 20;
	
	private Integer pageNo;

	/**
	 * 每页的记录数
	 */
	private Integer pageSize;
	
	/**
	 * 当前页第一条数据在List中的位置,默认从0开始
	 */
	//private Long start;
	
	/**
	 * 当前页中存放的记录,类型一般为List
	 */
	private List<T> dataList;
	
	/**
	 * 数据库中记录的总条数
	 */
	private Integer totalSize;
	
//	private Boolean hasNextPage;
	
//	private Boolean hasPreviousPage;
	
	 
	/**
	 * 默认构造方法.
	 *
	 * @param start	 本页数据在数据库中的起始位置
	 * @param totalSize 数据库中总记录条数
	 * @param pageSize  本页容量
	 * @param data	  本页包含的数据
	 */
	public Pagination(Integer pageNo, Integer pageSize, Integer totalSize, List<T> dataList) {
		this.pageNo = pageNo;
		this.totalSize = totalSize;
		this.pageSize = pageSize;
		this.dataList = dataList;
	}
	
	/**
	 * 取总记录数.
	 */
	public Integer getTotalSize() {
		return this.totalSize;
	}
	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	/**
	 * 取总页数
	 */
	public Integer getTotalPageCount() {
		if (totalSize % pageSize == 0)
			return (int)(totalSize / pageSize);
		else
			return (int)(totalSize / pageSize + 1);
	}
	/**
	 * 取每页数据容量.
	 */
	public int getPageSize() {
		return pageSize;
	}
 
	public Boolean getHasNextPage() {
		return this.getPageNo() < this.getTotalPageCount();
	}
	
	
	public Boolean getHasPreviousPage() {
		return this.getPageNo() > 1;
	}

	/**
	 * 获取任一页第一条数据在数据集的位置
	 *
	 * @param pageNo   从1开始的页号
	 * @param pageSize 每页记录条数
	 * @return 该页第一条数据
	 */
	public static Long getStartOfPage(Integer pageNo, Integer pageSize) {
		return Long.parseLong(String.valueOf((pageNo - 1) * pageSize));
	}
	
	
	public Integer getPageNo() {
		if(pageNo == null){
			return 1;
		}
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
//	public static void main(String[] args) {
//		Integer pageNo = 4;
//		Integer pageSize = 20;
//		Integer totalSize = 123 ;
//		
//		
//		Long start = Pagination.getStartOfPage(pageNo, pageSize);
//		Pagination page = new Pagination(pageNo,pageSize,totalSize,new ArrayList());
//		System.out.println(start);
//		System.out.println(page.getTotalPageCount());
//		System.out.println(page.getPageNo());
//		System.out.println(page.getPageSize());
//		System.out.println(page.getHasPreviousPage());
//		System.out.println(page.getHasNextPage());
//		
//		
//	}
}