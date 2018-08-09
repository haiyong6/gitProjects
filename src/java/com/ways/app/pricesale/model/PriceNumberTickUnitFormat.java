package com.ways.app.pricesale.model;


/**
 * 车型销量比例分析数值工具类
 * @author 赵海勇
 *
 */
public class PriceNumberTickUnitFormat {

	private int fenTick = 13;    //纵轴分断数
	private int zhuTick = 10000;	
	private double minNumber;    //最小值
	private double maxNumber;    //最大值
//	private static Double[] startTick = {0d,250d,500d,750d,1000d,2000d,2500d,5000d,7500d,10000d,20000d,25000d,50000d,75000d,100000d,200000d,250000d,500000d,750000d,1000000d,2000000d,2500000d,5000000d,7500000d,10000000d};
//	private static Double[] separatedTick = {10.0,20.0,25.0,50.0,75.0,100.0,200.0,250.0,500.0,750.0,1000.0,2000.0,2500.0,5000.0,7500.0,10000.0,20000.0,25000.0,50000.0,75000.0,100000.0,200000.0,250000.0,500000.0,750000.0,1000000.0,2000000.0,5000000.0,7500000.0,10000000.0};
	
	private static Double[] priceSaleSeparatedTick = {10000.0,20000.0,30000.0,40000.0,50000.0,60000.0,70000.0,80000.0,90000.0,100000.0,110000.0,120000.0,130000.0,140000.0,
		150000.0,160000.0,170000.0,180000.0,190000.0,200000.0,210000.0,220000.0,230000.0,240000.0,250000.0,260000.0,270000.0,280000.0,290000.0,300000.0};
	

	public int getFenTick() {
		return fenTick;
	}

	public void setFenTick(int fenTick) {
		this.fenTick = fenTick;
	}
	
	public int getZhuTick() {
		return zhuTick;
	}

	public void setZhuTick(int zhuTick) {
		this.zhuTick = zhuTick;
	}

	private PriceNumberTickUnitFormat (){
		
	}
	
	public static PriceNumberTickUnitFormat getInstance(double minNum,double maxNum){
		PriceNumberTickUnitFormat instance = new PriceNumberTickUnitFormat();
		instance.minNumber = minNum;
		instance.maxNumber = maxNum;
		return instance;
	}
	
	/**
	 * 获取最小刻度值
	 * @return
	 */
	public Double getMinTick(){
		double minValue = this.minNumber;
		double minNumberTick = 0;
		minNumberTick = minValue - minValue % getZhuTick();
		Loop: for(int i=0;i<priceSaleSeparatedTick.length;i++){
			if(minNumberTick <= priceSaleSeparatedTick[i]){
				if(i>0)	minNumberTick = priceSaleSeparatedTick[i-1];
				break Loop;
			}
		}
		return minNumberTick;
	}
	
	/**
	 * 获取最大刻度值
	 * @return
	 */
	public Double getMaxTick(){
		double maxValue = this.maxNumber;
		double tick = getPriceSaleTick();
		double maxNumberTick = 0;
		maxNumberTick = maxValue - maxValue % tick ;
		while(maxValue + tick > maxNumberTick){
			maxNumberTick += tick;
		}
		return maxNumberTick;
	}
	
	/**
	 * 获取车型销售比例刻度递增值
	 * @return
	 */
	public Double getPriceSaleTick(){
		double maxValue = this.maxNumber;
		//N = (H-(L - L % 10000))/S
		double numberTick = 0;
		numberTick = (maxValue-this.getMinTick())/getFenTick();
		Loop: for(int i=0;i<priceSaleSeparatedTick.length;i++){
			if(numberTick <= priceSaleSeparatedTick[i]){
				numberTick = priceSaleSeparatedTick[i];
				break Loop;
			}
		}
		return numberTick;
	}
	
//	/**
//	 * 获取刻度递增值
//	 * @return
//	 */
//	public Double getTick(){
//		double maxValue = this.maxNumber;
//		//N = (H-(L - L % 10000))/S
//		double numberTick = 0;
//		numberTick = (maxValue-this.getMinTick())/getFenTick();
//		Loop: for(int i=0;i<separatedTick.length;i++){
//			if(numberTick <= separatedTick[i]){
//				numberTick = separatedTick[i];
//				break Loop;
//			}
//		}
//		return numberTick;
//	}
	
//	@Override
//	public String toString() {
//		StringBuffer sb = new StringBuffer();
//		sb.append("fenTick      =").append(fenTick).append("\n");
//		sb.append("zhuTick      =").append(zhuTick).append("\n");
//		sb.append("minNumber    =").append(minNumber).append("\n");
//		sb.append("maxNumber    =").append(maxNumber).append("\n");
//		sb.append("eachTick     =").append(getTick()).append("\n");
//		sb.append("minTick      =").append(getMinTick()).append("\n");
//		sb.append("maxTick      =").append(getMaxTick()).append("\n");
//		return sb.toString();
//	}
//	
	public static void main(String[] args) {
		double a  = 345000.0;
		double b = 689900.0;
		PriceNumberTickUnitFormat format = PriceNumberTickUnitFormat.getInstance(a, b);
		double min = format.getMinTick();
		double max = format.getMaxTick();
		double tick = format.getPriceSaleTick();
		System.out.println(min+"  "+max+"  "+tick);
	}
}
