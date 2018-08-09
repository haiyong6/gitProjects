package com.ways.app.framework.utils;

import java.util.ArrayList;
import java.util.List;

import com.ways.app.framework.utils.EchartsLineGraduateScope;
import com.ways.app.price.model.EchartLineDataEntity;

/**
 * Echarts工具类
 * @author yinlue
 *
 */
public class EchartsUtil2 {
	
	
	private static List<EchartsLineGraduateScope> list;
	
	public static void initList()
	{
		if(null == list || 0 == list.size())
		{
			list = new ArrayList<EchartsLineGraduateScope>();
			list.add(new EchartsLineGraduateScope("-9500","-4500","-1000"));
			list.add(new EchartsLineGraduateScope("-4500","-2500","-500"));
			list.add(new EchartsLineGraduateScope("-2500","-1800","-300"));
			list.add(new EchartsLineGraduateScope("-1800","-800","-200"));
			list.add(new EchartsLineGraduateScope("-800","-500","-100"));
			list.add(new EchartsLineGraduateScope("-500","-100","-50"));
			list.add(new EchartsLineGraduateScope("-100","-50","-25"));
			list.add(new EchartsLineGraduateScope("-50","-10","-10"));
			list.add(new EchartsLineGraduateScope("-10","-5","1"));
			list.add(new EchartsLineGraduateScope("0","5","1"));
			list.add(new EchartsLineGraduateScope("5","10","2"));
			list.add(new EchartsLineGraduateScope("10","50","10"));
			list.add(new EchartsLineGraduateScope("50","100","25"));
			list.add(new EchartsLineGraduateScope("100","120","25"));
			list.add(new EchartsLineGraduateScope("120","500","50"));
			list.add(new EchartsLineGraduateScope("500","800","100"));
			list.add(new EchartsLineGraduateScope("800","1800","200"));
			list.add(new EchartsLineGraduateScope("1800","2500","300"));
			list.add(new EchartsLineGraduateScope("2500","4500","500"));
			list.add(new EchartsLineGraduateScope("4500","9000","1000"));
			list.add(new EchartsLineGraduateScope("9000","18000","2000"));
			list.add(new EchartsLineGraduateScope("18000","25000","3000"));
			list.add(new EchartsLineGraduateScope("25000","30000","3000"));
			list.add(new EchartsLineGraduateScope("30000","40000","5000"));
			list.add(new EchartsLineGraduateScope("40000","90000","10000"));
			list.add(new EchartsLineGraduateScope("90000","150000","20000"));
			list.add(new EchartsLineGraduateScope("150000","270000","30000"));
			list.add(new EchartsLineGraduateScope("150000","270000","30000"));
			list.add(new EchartsLineGraduateScope("270000","400000","50000"));
			list.add(new EchartsLineGraduateScope("400000","900000","100000"));
			list.add(new EchartsLineGraduateScope("900000","2000000","250000"));
			list.add(new EchartsLineGraduateScope("2000000","3000000","600000"));
			list.add(new EchartsLineGraduateScope("3000000","4000000","800000"));
			list.add(new EchartsLineGraduateScope("4000000","5000000","1000000"));
			list.add(new EchartsLineGraduateScope("5000000","8000000","1500000"));
			list.add(new EchartsLineGraduateScope("8000000","12000000","2000000"));
			list.add(new EchartsLineGraduateScope("12000000","15000000","3000000"));
			list.add(new EchartsLineGraduateScope("15000000","20000000","5000000"));
			list.add(new EchartsLineGraduateScope("20000000","30000000","6000000"));
		}
	}
	
	/**
	 * 设置线图刻度动态划分Segment
	 * @param lineList   数据集
	 * @param yAxisIndex Y轴锁引
	 * @return
	 */
	public static String[] setLineScaleDivision(int[] temp,String yAxisIndex)
	{
		initList();
		String[] arrs = new String[3];
		int maxValue = temp[0];
		int minValue = temp[1];
		int demoMaxValue = 0;//原始最大值
		
		//如果没有值，则直接返回
		if(0 == maxValue) {
			arrs[0] = "10";
			arrs[1] = "0";
			arrs[2] = "2";
			return arrs;
		}
		demoMaxValue = (int) ((float)maxValue*1.1)/1000*1000;
		minValue = (int) ((float)minValue*0.8)/1000*1000;
		if(yAxisIndex.equals("0")){
			if(minValue>0&&minValue<5) minValue = 0;
		}else {
			demoMaxValue = maxValue;
			minValue = (int) ((float)minValue*0.2)/1000*1000;

		}
		//最小值与最大值差
		int diffentValue = maxValue - minValue;
		
		int splitNumber = 0;//动态分割段数
		int unitValue = 0;//单元值
		
		//循环匹配刻度值
		for(EchartsLineGraduateScope scopeObj : list)
		{
			if(diffentValue >= Integer.parseInt(scopeObj.getMin()) && diffentValue < Integer.parseInt(scopeObj.getMax()))
			{
				unitValue = Integer.parseInt(scopeObj.getUnitValue());
				if(0 == (diffentValue % unitValue)) splitNumber = diffentValue / unitValue;
				else splitNumber = diffentValue / unitValue + 1;
				break;
			}
		}
		//根据单元值计算最终最小值和最大值
		if(minValue > unitValue || (minValue < 0 && minValue < unitValue)) minValue = (minValue - unitValue) / unitValue * unitValue;
		
		maxValue = splitNumber * unitValue + minValue;
		//如果原始最大值大于划分后的最大值，则分割段数++
		while(demoMaxValue >= maxValue)
		{
			splitNumber += 1;
			maxValue = splitNumber * unitValue + minValue;
		}
		
		arrs[0] = maxValue + "";
		arrs[1] = minValue + "";
		arrs[2] = splitNumber + "";
		return arrs;
	}
	
	/**
	 * 设置区域价格降幅线图刻度动态划分
	 * @param lineList
	 * @return
	 */
	public static String[] setLineScaleDivision(List<EchartLineDataEntity> lineList)
	{
		initList();
		String[] arrs = new String[3];
		int maxValue = 0;
		int minValue = 1000000;
		int demoMaxValue = 0;//原始最大值
		
		//获取数组最大值和最小值
		for(EchartLineDataEntity series : lineList)
		{
			String[] data = series.getData();
			if("bar".equals(series.getType())) continue;
			
			for(String str : data)
			{
				if(!AppFrameworkUtil.isEmpty(str) && !"-".equals(str))
				{
					try {
						int value = (int)(Float.parseFloat(str));
						if(value > maxValue) maxValue = value;
						if(value < minValue) minValue = value;
					} catch (Exception e) {
						
					}
				}
			}
		}
		demoMaxValue = maxValue;
		//最小值与最大值差
		int diffentValue = maxValue - minValue;
		
		int splitNumber = 0;//动态分割段数
		int unitValue = 0;//单元值
		
		//循环匹配刻度值
		for(EchartsLineGraduateScope scopeObj : list)
		{
			//如果最小值 为空，则是无穷大
			if(AppFrameworkUtil.isEmpty(scopeObj.getMin()))
			{
				if(diffentValue < Integer.parseInt(scopeObj.getMax()))
				{
					unitValue = Integer.parseInt(scopeObj.getUnitValue());
					if(0 == (diffentValue % unitValue)) splitNumber = diffentValue / unitValue;
					else splitNumber = diffentValue / unitValue + 1;
					break;
				}
			}
			//如果最大值 为空，则是无穷小
			else if(AppFrameworkUtil.isEmpty(scopeObj.getMax()))
			{
				if(diffentValue >= Integer.parseInt(scopeObj.getMin()))
				{
					unitValue = Integer.parseInt(scopeObj.getUnitValue());
					if(0 == (diffentValue % unitValue)) splitNumber = diffentValue / unitValue;
					else splitNumber = diffentValue / unitValue + 1;
					break;
				}
			}
			//正常范围值
			else
			{
				if(diffentValue >= Integer.parseInt(scopeObj.getMin()) && diffentValue < Integer.parseInt(scopeObj.getMax()))
				{
					unitValue = Integer.parseInt(scopeObj.getUnitValue());
					if(0 == (diffentValue % unitValue)) splitNumber = diffentValue / unitValue;
					else splitNumber = diffentValue / unitValue + 1;
					break;
				}
			}
		}
		//根据单元值计算最终最小值和最大值
		if(unitValue == 0 && splitNumber == 0) {
			minValue = 0;
			maxValue = 3000;
			unitValue = 300;
			splitNumber = 10;
		} else {
			if(minValue >= 0){
				minValue = Integer.parseInt(AppFrameworkUtil.getNum(((float)((minValue - unitValue ))) / unitValue, 0)) * unitValue;
			} else {
				minValue = (minValue - unitValue) / unitValue * unitValue ;
			}
			maxValue = splitNumber * unitValue + minValue;
		}
		//如果原始最大值大于划分后的最大值，则分割段数++
		while(demoMaxValue >= maxValue)
		{
			splitNumber += 1;
			maxValue = splitNumber * unitValue + minValue;
		}
		
		arrs[0] = maxValue + "";
		arrs[1] = minValue + "";
		arrs[2] = splitNumber + "";
		return arrs;
	}
}