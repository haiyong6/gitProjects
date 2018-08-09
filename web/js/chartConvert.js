/**
 * 切换折线图指定线条的数据是否显示。
 * @return
 */
function setShowValue(json,years){
	if(undefined == json) return '{}';
	var series = json.series;
	for(var i=0, serie; (serie= series[i])!= null; i++) {
		var name = serie.name; //图例名称
		if(isExistByArray(years,name)){
			var datas = serie.data
			for (var j = 0, data; (data = datas[j]) != null; j++) {
				data.itemStyle.normal.label.show = true; //显示数值
			}
		}else{
			var datas = serie.data
			for (var j = 0, data; (data = datas[j]) != null; j++) {
				data.itemStyle.normal.label.show = false; //隐藏数值
			}
		}
	}
	return json;
}

function isExistByArray(years,seriesname){
	for(var k=0,year; (year= years.split(",")[k])!= null; k++){
		if(year == seriesname){
			return true;
		}
	}
	return false;
}


/**
 * 自定义Y轴
 * 
 * 实现逻辑如下：
 *   1.遍历每个dataset,再遍历每个value值,拿到每个在最小最大值范围内的value值
 *     判断value值的前一个值是否在最小最大值范围内,如果不在即把前一个value值设置为虚线(dashed=1),如果在即暂时不处理
 *     判断value值的后一个值是否在最小最大值范围内,如果不在即把当前value和后一个value值设置为虚线(dashed=1),如果在即暂时不处理
 *   2.重新遍历每个value值处理最小最大值范围外的value
 *     如果value值大于最大值即把最大值替换当前值,如果当前值大于最大值而且又没被设置为虚线(dashed=1)的把当前value设置为null
 *     如果value值小于最小值即把最小值替换当前值,如果当前值小于最小值而且又没被设置为虚线(dashed=1)的把当前value设置为null
 *   3.最后把最小值设置为图表Y轴最小值,把最大值设置为图表Y轴的最大值
 * 
 * @param divId 弹框div的ID
 * @param min   最小值
 * @param max   最大值
 */
function renderPopupLineChart(divId, min, max){
	
	//FusionCharts js对象副本,clone()是自定方法
	var fusionChartsClone = FusionCharts.items["line"].clone();
	//alert(fusionChartsClone);
	//JSON数据对象副本
	var chartObj = clone.call(fusionChartsClone.getJSONData());
	//flash文件地址
	var swfUrl = fusionChartsClone.src;
	
	//alert(JSON.stringify(chartObj));
	//alert(swfUrl);
		
	var startRange = min
	var endRange = max
	
	//页面原有的fusioncharts对象
	//var chartObj = $('#sales-chart').data('json');
	//页面原有的flash文件地址
	//var swfUrl = "assets/javascripts/libs/FusionCharts_Evaluation_3.2.2/Charts/MSCombi2D.swf";
	
	//如果任意值为空则不往下执行
	if(startRange=="" || startRange==undefined || endRange=="" || endRange==undefined) return false;
	
	//处理开始
	for(var i=0;i<chartObj.dataset.length;i++){
		//对范围内点的相邻点的设置
		for(var j=0;j<chartObj.dataset[i].data.length;j++){
			var value = chartObj.dataset[i].data[j].value;
			if(!isNaN(value)){
				//刻度选择范围内
				if(Number(startRange) <= Number(value) && Number(value) <= Number(endRange)){
					//如果当前点前方有值
					if(j-1>=0){
						var previousValue = chartObj.dataset[i].data[j-1].value;
						//如果值在选择范围外，赋边界值并设为虚线
						if(Number(startRange)>Number(previousValue) || Number(previousValue) > Number(endRange)){
							chartObj.dataset[i].data[j-1].dashed = 1;
							chartObj.dataset[i].data[j-1].showValue = 0;
							chartObj.dataset[i].data[j-1].anchorAlpha = 0;
							chartObj.dataset[i].data[j-1].toolText = " ";
						}
					}
					
					//如果当前点后方有值
					if(j+1<chartObj.dataset[i].data.length){
						var nextValue = chartObj.dataset[i].data[j+1].value;
						//如果值在选择范围外，赋边界值并设为虚线
						if(Number(startRange)>Number(nextValue) || Number(nextValue) > Number(endRange)){
							chartObj.dataset[i].data[j].dashed = 1;
							chartObj.dataset[i].data[j+1].dashed = 1;
							chartObj.dataset[i].data[j+1].showValue = 0;
							chartObj.dataset[i].data[j+1].anchorAlpha = 0;
							chartObj.dataset[i].data[j+1].toolText = " ";
						}
					}
				}
			}
		}
		
		//对范围外的点值设置为空（设置了虚线的除外）
		for(var j=0;j<chartObj.dataset[i].data.length;j++){
			var value = chartObj.dataset[i].data[j].value;
			if(!isNaN(value)){
				//刻度选择范围外
				if(Number(startRange) > Number(value)){
					chartObj.dataset[i].data[j].value = startRange;
					if(chartObj.dataset[i].data[j].dashed != 1){
						chartObj.dataset[i].data[j].value = null;
					}	
				}else if(Number(value) > Number(endRange)){
					chartObj.dataset[i].data[j].value = endRange;
					if(chartObj.dataset[i].data[j].dashed != 1){
						chartObj.dataset[i].data[j].value = null;
					}	
				}
			}
		}
	}
	
	//刻度最大值
	if(chartObj.chart.yAxisMaxValue == undefined){
		chartObj.chart.yaxismaxvalue = endRange;
	}else{
		chartObj.chart.yAxisMaxValue = endRange;
	}
	
	//刻度最小值
	if(chartObj.chart.yAxisMinValue == undefined){
		chartObj.chart.yaxisminvalue = startRange;
	}else{
		chartObj.chart.yAxisMinValue = startRange;
	}
	
	//去掉图表设置菜单
	chartObj.chart.showaboutmenuitem = 0;
	
	//放大图表
	zoomInChart(swfUrl,chartObj,900,400,divId);
}


/**
 * 放大图表
 * @param swf  		flash文件
 * @param chartObj  fusioncharts对象
 * @param width 	fusioncharts宽度
 * @param height  	fusioncharts高度
 * @param divId  	渲染到哪个div的ID
 */
function zoomInChart(swf, chartObj, width, height, divId){

	//alert(JSON.stringify(chartObj));
	
	var oldChart = FusionCharts.items["zoomChartId"];
	
	if (oldChart) {
		oldChart.setJSONData(chartObj);
		oldChart.render(divId);
		oldChart.configure("ChartNoDataText", " ");
	}
	else {
		var myChart = new FusionCharts(swf, "zoomChartId", width, height,"0");
		myChart.setJSONData(chartObj);
		myChart.render(divId);
		myChart.configure("ChartNoDataText", " ");
	}
}

/**
 * @Description 克隆对象 
 */
function clone() {
    var objClone;
    if (this.constructor == Object){
        objClone = new this.constructor(); 
    }else{
        objClone = new this.constructor(this.valueOf()); 
    }
    for(var key in this){
        if ( objClone[key] != this[key] ){ 
            if ( typeof(this[key]) == 'object' ){ 
                objClone[key] = clone.call(this[key]);
            }else{
                objClone[key] = this[key];
            }
        }
    }
    objClone.toString = this.toString;
    objClone.valueOf = this.valueOf;
    return objClone; 
};