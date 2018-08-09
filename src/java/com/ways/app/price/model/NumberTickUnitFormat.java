package com.ways.app.price.model;
/**
 * 车型价格段分析刻度计算实体类
 * @author zhaohaiyong
 *
 */
public class NumberTickUnitFormat {
	
	private int fenTick = 10;//划分段数
	private int zhuTick = 10000;//单元格数值	
	private double minNumber;
	private double maxNumber;
	private static Double[] startTick = {0d,250d,500d,750d,1000d,2000d,2500d,5000d,7500d,10000d,20000d,25000d,50000d,75000d,100000d,200000d,250000d,500000d,750000d,1000000d,2000000d,2500000d,5000000d,7500000d,10000000d};
	private static Double[] separatedTick = {10.0,20.0,25.0,50.0,75.0,100.0,200.0,250.0,500.0,750.0,1000.0,2000.0,2500.0,5000.0,7500.0,10000.0,20000.0,25000.0,50000.0,75000.0,100000.0,200000.0,250000.0,500000.0,750000.0,1000000.0,2000000.0,5000000.0,7500000.0,10000000.0};
	
	
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

	private NumberTickUnitFormat (){
		
	}
	
	public static NumberTickUnitFormat getInstance(double minNum,double maxNum){
		NumberTickUnitFormat instance = new NumberTickUnitFormat();
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
		Loop: for(int i=0;i<startTick.length;i++){
			if(minNumberTick <= startTick[i]){
				if(i>0)	minNumberTick = startTick[i-1];
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
		double tick = getTick();
		double maxNumberTick = 0;
		maxNumberTick = maxValue - maxValue % tick ;
		while(maxValue + tick > maxNumberTick){
			maxNumberTick += tick;
		}
		return maxNumberTick;
	}
	
	/**
	 * 获取刻度递增值
	 * @return
	 */
	public Double getTick(){
		double maxValue = this.maxNumber;
		//N = (H-(L - L % 10000))/S
		double numberTick = 0;
		numberTick = (maxValue-this.getMinTick())/getFenTick();
		Loop: for(int i=0;i<separatedTick.length;i++){
			if(numberTick <= separatedTick[i]){
				numberTick = separatedTick[i];
				break Loop;
			}
		}
		return numberTick;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("fenTick      =").append(fenTick).append("\n");
		sb.append("zhuTick      =").append(zhuTick).append("\n");
		sb.append("minNumber    =").append(minNumber).append("\n");
		sb.append("maxNumber    =").append(maxNumber).append("\n");
		sb.append("eachTick     =").append(getTick()).append("\n");
		sb.append("minTick      =").append(getMinTick()).append("\n");
		sb.append("maxTick      =").append(getMaxTick()).append("\n");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		//NumberTickUnitFormat test = new NumberTickUnitFormat();
		double a  = 345000.0;
		double b = 689900.0;
		NumberTickUnitFormat format = NumberTickUnitFormat.getInstance(a, b);
		double min = format.getMinTick();
		double max = format.getMaxTick();
		double tick = format.getTick();
		System.out.println(min+"  "+max+"  "+tick);
	}

}
