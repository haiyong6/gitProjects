package com.ways.app.framework.utils;

import java.util.ArrayList;
import java.util.List;

import com.ways.app.price.model.EchartLineDataEntity;
import com.ways.app.price.model.EchartsLineGraduateScope;

/**
 * Echarts工具类
 * @author yinlue
 *
 */
public class EchartsUtil {
	
	
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
			list.add(new EchartsLineGraduateScope("-500","0","-50"));
			list.add(new EchartsLineGraduateScope("0","10","2"));
			list.add(new EchartsLineGraduateScope("10","500","50"));
			list.add(new EchartsLineGraduateScope("500","800","100"));
			list.add(new EchartsLineGraduateScope("800","1800","200"));
			list.add(new EchartsLineGraduateScope("1800","2500","300"));
			list.add(new EchartsLineGraduateScope("2500","4500","500"));
			list.add(new EchartsLineGraduateScope("4500","9000","1000"));
			list.add(new EchartsLineGraduateScope("9000","18000","2000"));
			list.add(new EchartsLineGraduateScope("18000","25000","3000"));
			list.add(new EchartsLineGraduateScope("25000","45000","5000"));
			list.add(new EchartsLineGraduateScope("45000","90000","10000"));
			list.add(new EchartsLineGraduateScope("90000","150000","20000"));
			list.add(new EchartsLineGraduateScope("150000","270000","30000"));
			list.add(new EchartsLineGraduateScope("150000","270000","30000"));
			list.add(new EchartsLineGraduateScope("270000","400000","50000"));
			list.add(new EchartsLineGraduateScope("400000","900000","100000"));
			list.add(new EchartsLineGraduateScope("900000","2000000","250000"));
			list.add(new EchartsLineGraduateScope("2000000","","500000"));
		}
	}
	
	/**
	 * 设置线图刻度动态划分
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
				minValue = Integer.parseInt(AppFrameworkUtil.getNum(((float)((minValue - unitValue))) / unitValue, 0)) * unitValue;
			} else {
				minValue = (minValue - unitValue) / unitValue * unitValue;
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
	
	
	/**
	 * 设置按千元线图刻度动态划分
	 * @param lineList
	 * @return
	 */
	public static String[] setLineScaleDivisionToThousand(List<EchartLineDataEntity> lineList)
	{
		initList();
		String[] arrs = new String[3];
		float maxValue = 0;
		float minValue = 1000;
		float demoMaxValue = 0;//原始最大值
		
		//获取数组最大值和最小值
		for(EchartLineDataEntity series : lineList)
		{
			String[] data = series.getData();
			for(String str : data)
			{
				if(!AppFrameworkUtil.isEmpty(str) && !"-".equals(str))
				{
					try {
						float value = Float.parseFloat(str);
						if(value > maxValue) maxValue = value;
						if(value < minValue) minValue = value;
					} catch (Exception e) {
						
					}
				}
			}
		}
		demoMaxValue = maxValue;
		//最小值与最大值差
		float diffentValue = maxValue - minValue;
		//动态分割段数
		int splitNumber = 0;
		float unitValue = 0;
		
		//循环匹配刻度值
		for(EchartsLineGraduateScope scopeObj : list)
		{
			//如果最小值 为空，则是无穷大
			if(AppFrameworkUtil.isEmpty(scopeObj.getMin()))
			{
				if(diffentValue < Float.parseFloat(scopeObj.getMax()) / 1000 )
				{
					unitValue = Float.parseFloat(scopeObj.getUnitValue()) / 1000;
					if(0 == (diffentValue % unitValue)) splitNumber = (int)(diffentValue / unitValue);
					else splitNumber = (int)(diffentValue / unitValue) + 1;
					break;
				}
			}
			//如果最大值 为空，则是无穷小
			else if(AppFrameworkUtil.isEmpty(scopeObj.getMax()))
			{
				if(diffentValue >= Float.parseFloat(scopeObj.getMin()) / 1000 )
				{
					unitValue = Float.parseFloat(scopeObj.getUnitValue()) / 1000;
					if(0 == (diffentValue % unitValue)) splitNumber = (int)(diffentValue / unitValue);
					else splitNumber = (int)(diffentValue / unitValue) + 1;
					break;
				}
			}
			//正常范围值
			else
			{
				if(diffentValue >= Float.parseFloat(scopeObj.getMin()) / 1000 && diffentValue < Float.parseFloat(scopeObj.getMax()) / 1000)
				{
					unitValue = Float.parseFloat(scopeObj.getUnitValue()) / 1000;
					if(0 == (diffentValue % unitValue)) splitNumber = (int)(diffentValue / unitValue);
					else splitNumber = (int)(diffentValue / unitValue) + 1;
					break;
				}
			}
		}
		//根据单元值计算最终最小值和最大值
		minValue = (minValue - unitValue) / unitValue * unitValue;
		maxValue = splitNumber * unitValue + minValue;
		//如果原始最大值大于划分后的最大值，则分割段数+2
		if(demoMaxValue > maxValue)
		{
			splitNumber ++;
			maxValue = splitNumber * unitValue + minValue;
		}
		
		arrs[0] = maxValue + "";
		arrs[1] = minValue + "";
		arrs[2] = splitNumber + "";
		return arrs;
	}
	
	/**
	 * 设置线图刻度动态划分
	 * @param lineList
	 * @return
	 */
	public static String[] setLineScaleDivisionToPriceIndex(List<EchartLineDataEntity> lineList)
	{
		initList();
		String[] arrs = new String[3];
		int maxValue = 0;
		int minValue = 0;
		int demoMaxValue = 0;//原始最大值
		boolean initValue = true;
		
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
						//初始化第一次最小最大值
						if(initValue)
						{
							maxValue = value;
							minValue = value;
							initValue = false;
						}
						
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
		int unitValue = 1;//单元值
		
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
		if(minValue >= 0) minValue = Integer.parseInt(AppFrameworkUtil.getNum(((float)((minValue - unitValue))) / unitValue, 0)) * unitValue;
		else minValue = (minValue - unitValue) / unitValue * unitValue;
		
		maxValue = splitNumber * unitValue + minValue;
		//如果原始最大值大于划分后的最大值，则分割段数++
		while(demoMaxValue >= maxValue)
		{
			splitNumber += 1;
			maxValue = splitNumber * unitValue + minValue;
		}
		
		arrs[0] = maxValue + "";
		arrs[1] = (minValue - (unitValue * 2)) + "";
		arrs[2] = splitNumber + 2 + "";
		return arrs;
	}
	
	/**
	 * 设置价格段销量分析线图刻度动态划分
	 * @param lineList
	 * @return
	 */
	public static String[] setLineScaleDivisionToVolumeByPriceRange(List<EchartLineDataEntity> lineList)
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
		
		maxValue = splitNumber * unitValue + minValue;
		//如果原始最大值大于划分后的最大值，则分割段数++
		while(demoMaxValue >= maxValue)
		{
			splitNumber += 1;
			maxValue = splitNumber * unitValue + minValue;
		}
		
		while(demoMaxValue < (maxValue-minValue) && minValue > 0)
		{
			maxValue = splitNumber * unitValue;
			minValue = 0;
		}
		arrs[0] = maxValue + "";
		arrs[1] = minValue + "";
		arrs[2] = splitNumber + "";
		return arrs;
	}
	
}
