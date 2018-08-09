package com.ways.app.price.model;

/**
 * 型号价格降幅实体类，作用于导出
 * @author yinlue
 *
 */
public class VersionPriceIndexInfo extends VersionInfoEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//销量
	private String sales;
	//总销量
	private String totalSales;
	//价格
	private String price;
	//上月价格
	private String lastMonthPrice;
	//基期价格
	private String datumPrice;
	//CV差
	private String cvDifference;
	//价格降幅
	private String priceIndex;
	
	
	
	public String getLastMonthPrice() {
		return lastMonthPrice;
	}
	public void setLastMonthPrice(String lastMonthPrice) {
		this.lastMonthPrice = lastMonthPrice;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	public String getTotalSales() {
		return totalSales;
	}
	public void setTotalSales(String totalSales) {
		this.totalSales = totalSales;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDatumPrice() {
		return datumPrice;
	}
	public void setDatumPrice(String datumPrice) {
		this.datumPrice = datumPrice;
	}
	public String getCvDifference() {
		return cvDifference;
	}
	public void setCvDifference(String cvDifference) {
		this.cvDifference = cvDifference;
	}
	public String getPriceIndex() {
		return priceIndex;
	}
	public void setPriceIndex(String priceIndex) {
		this.priceIndex = priceIndex;
	}

}
