package com.ways.app.price.model;

/**
 * 型号区域价格降幅实体类，作用于导出
 * @author yinlue
 *
 */
public class VersionCityPriceIndexInfo extends VersionInfoEntity {

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
	//城市名称
	private String cityName;
	//城市英文名称
	private String cityNameEn;
	
	
	
	public String getLastMonthPrice() {
		return lastMonthPrice;
	}
	public void setLastMonthPrice(String lastMonthPrice) {
		this.lastMonthPrice = lastMonthPrice;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityNameEn() {
		return cityNameEn;
	}
	public void setCityNameEn(String cityNameEn) {
		this.cityNameEn = cityNameEn;
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
