$(document).ready(function(){ 
	$('#startDate-container.input-append.date').datetimepicker({
		format: 'yyyy-mm',
        language:  'zh-CN',        
        todayBtn:  0,
		autoclose: 1,				
		startView: 3,		
		maxView:3,
		minView:3,
		startDate:beginDate,
		endDate:endDate,
        showMeridian: 1
    });
    
	//时间改变事件
    $('#startDate-container.input-append.date').on('changeDate',function(){
    	checkPopBoxData();
    });
    
    /**图形切换事件*/
    $("#chartType").change(function(){
    	//雷达图
    	if("1" == $("#chartType").val())
    	{
    		$(".row-fluid table td:eq(1)").find("img").attr("src",ctx+"/img/outGive/tpRatio_Radar.png");
    	}
    	//折线图
    	else
    	{
    		$(".row-fluid table td:eq(1)").find("img").attr("src",ctx+"/img/outGive/tpRatio_line.png");
    	}
    });
	
    /**
     * 展示城市弹出框
     */
	$('#cityModal').on('show', function () {
		showLoading("cityModalBody");
		$('#cityModalBody').load(ctx+"/global/getCityModal",getParams(),function(){
			//设置默认选中项
			$('#cityModalResultContainer input').each(function(){
				var selectedId = $(this).val();
				$("#cityModalBody").find('.cityModalByCity').each(function(){
					if($(this).val() == selectedId){
						$(this).attr("checked",'true');//行全选
					}
				});
			});
			
			//行全选
			$("#cityModal").find(".cityModalByArea").each(function(){
				var allRowCityArr = $(this).parent().parent().parent().find(".cityModalByCity");
				var allRowSelectedCityArr = $(this).parent().parent().parent().find(".cityModalByCity:checked");
				if(allRowCityArr.length == allRowSelectedCityArr.length){
					$(this).attr("checked",'true');//全部全选		
				}else{
					$(this).removeAttr("checked");//全部取消
				}	
			});	
			//全选
			var allCityArr = $("#cityModal").find(".cityModalByCity");
			var allSelectedCityArr = $("#cityModal").find(".cityModalByCity:checked");
			//如果是全国均价则全选城市
			if(0 == $("#cityModalResultContainer div :input").val())
			{
				if($("#myModalLabel :checkbox").attr("checked")) 
				{
					$(".cityModalContainer").find('.cityModalByAll').click();
					$(".cityModalContainer").find('.cityModalByAll').click();
				}
				else
				{
					$(".cityModalContainer").find('.cityModalByAll').click();
				}
			}
			else if(allCityArr.length == allSelectedCityArr.length){
				$(this).parents('#cityModal').find('.cityModalByAll').attr("checked",'true');//全部全选		
			}
			else{
				$(this).parents('#cityModal').find('.cityModalByAll').removeAttr("checked");//全部取消
			}
		});
	});
	
	$('#subModelModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		
		//加载子车型数据
		showLoading("subModelModalBody");
		$('#subModelModalBody').load(ctx+"/global/getSubmodelModal",getParams(),function(){
			//弹出框设置默认选中项结果集		
			var strHTML = '<ul class="inline" >';
			$('#subModelModalResultContainer input').each(function(){
				var subModelId = $(this).val();
				var subModelName = $(this).attr("subModelName");
				var pooAttributeId = $(this).attr("pooAttributeId");
				var letter = $(this).attr("letter");
				strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
			  		strHTML += '<div class="removeBtnByResult label label-info" subModelId="'+subModelId+'"  pooAttributeId="'+pooAttributeId+'" letter="'+letter+'" subModelName="'+subModelName+'" style="cursor:pointer" title="删除：'+subModelName+'">';
				  		strHTML += '<i class="icon-remove icon-white"></i>'+subModelName;
			  		strHTML += '</div>';
			 		strHTML += '</li>';
			 		
		 		$(".subModelModalContainer .subModelIdInput").each(function(){
					if($(this).val() == subModelId){
						$(this).attr("checked",'true');//行全选
					}
				});
			});
			strHTML += '</ul>';
			$("#selectorResultContainerBySubModel").html(strHTML);
			
			//addResultContainerBySubModel();
			/*
			//产地属性默认选中设置
			var selectedPooAttributeIds = $('.selectedPooAttributeIds').val();
			if(selectedPooAttributeIds != ""){
				$('#subModelModalBody').find('.pooAttributeIdInput').each(function(){
					var arr = selectedPooAttributeIds.split(",");
					for(var i=0;i<arr.length;i++){
						if(arr[i] == $(this).val()){
							$(this).attr("checked",'true');//选中
						}
					}					
				});
			}
			*/
		});
	});
	
	$('#versionModal').on('show', function (e) {		
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
		if(e.relatedTarget || 0 == $("#subModelModalResultContainer input[name='selectedSubModel']").length)  return; 
		//加载子车型下型号数据
		showLoading("versionModalBody");
		$('#versionModalBody').load(ctx+"/global/getVersionModalByCommon",getParams(),function(){
			//设置默认选中项
			$('#versionModalResultContainer input').each(function(){
				var selectedId = $(this).val();
				$(".versionModalContainer .versionIdInput").each(function(){
					if($(this).val() == selectedId){
						$(this).attr("checked",'true');//行全选
					}
				});
			});
			
			//设置全选效果---开始
			$(".versionModalContainer").find('.selectVersionAll').each(function(){
				var selectedCount = 0;
				var totalCount = 0;
				var subModelId = $(this).val();
				$(".versionModalContainer .versionIdInput").each(function(){
					if(	subModelId == $(this).attr("subModelId")){
						totalCount++;
						if($(this).attr("checked")){
							selectedCount++;
						}
					}
				});
				if(selectedCount == totalCount){
					$(this).attr("checked",'true');
				}else{
					$(this).removeAttr("checked");//取消选中
				}
			});
			//设置全选效果---结束
			
		});
	});
	
	/**
	 * 校验弹出框有效数据
	 */
	function checkPopBoxData()
	{
		var paramsObj = getParams();
		if(!paramsObj.vid && !paramsObj.mid) return;
		$('.queryConditionContainer').showLoading();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/checkPopBoxTpData",
			   data: paramsObj,
			   dataType:'json',
			   success: function(data){
				   if(data)
				   {
					  var modelObj = $("#subModelModalResultContainer ul li");
					  var vidObj = $("#versionModalResultContainer ul li");
					  for(var i = 0; i < data.length; i++)
					  {
						  var mid = data[i].subModelId;
						  var vids = data[i].versionList;
						  //遍历车型
						  if(modelObj)
						  {
							  $.each(modelObj,function(i,n){
								  if(mid == $(n).find("input").val()) $(n).remove();
							  });
						  }
						  //遍历型号
						  if(vidObj && vids)
						  {
							  for(var j = 0; j < vids.length; j++)
							  {
								  var vid = vids[j].versionId;
								  $.each(vidObj,function(i,n){
									  if(vid == $(n).find("input").val()) $(n).remove();
								  });
							  }
						  }
					  }
				   }
				   $('.queryConditionContainer').hideLoading();
			   },
			   error:function(){
				   $('.queryConditionContainer').hideLoading();
			   }
			});
	}
	
	/**
	 * 获取页面请求参数
	 */
	function getParams()
	{
		var beginDate = $("#startDate").val();
		var inputType = "2";//控件默认单选
		var chartType = $("#chartType").val();//图形类型
		
		//城市ID
		var citys = $("#cityModalResultContainer input[name='selectedCity']").map(function(){return $(this).val();}).get().join(",");
		//如果是全国均价，则取弹出枉里的城市ID
		var beginDate_TPMix=beginDate.substr(0,4)-1;
		if(0 == citys){
			if(beginDate_TPMix>2013){
				citys = "8,17,28,3,18,7,12,15,31,2,5,11,23,1,14,16,20,32,4,13,21,22,29,19,30,41,42,48,60,61,47";
			}else{
				citys = "8,17,28,3,18,7,12,15,31,2,5,11,23,1,14,16,20,32,4,13,21,22,29,47";
			}
		}
		var vid = $("#versionModalResultContainer input[name='selectedVersion']").map(function(){return $(this).val();}).get().join(",");
		var mid = $("#subModelModalResultContainer input[name='selectedSubModel']").map(function(){return $(this).val();}).get().join(",");
		
		var paramObj = {};
		paramObj.beginDate = beginDate;
		paramObj.endDate = beginDate;
		paramObj.inputType = inputType;
		paramObj.citys = citys;
		paramObj.vid = vid;
		paramObj.mid = mid;
		paramObj.modelIds = mid;//车型ID，用于弹出框公共控制校验
		paramObj.timeType = "1";//时间点
		paramObj.analysisContentType = "2";//成交价分析,用于弹出框公共控制校验
		paramObj.chartType = chartType;
		
		getQueryConditionAndBrowser(paramObj);
		return paramObj;
	}
	
	/**
	 * 获取页头查询条件，以及浏览器
	 */
	function getQueryConditionAndBrowser(paramObj)
	{
		paramObj.browser = navigator.appVersion;
		var queryCondition = "";
		
		queryCondition += "图形类型 = ";
		if("1" == $("#chartType").val()) queryCondition += "雷达图";
		if("2" == $("#chartType").val()) queryCondition += "折线图";
		
		queryCondition += "\n时间= " + $("#startDate").val();
		
		queryCondition += "\n城市 = " + $("#cityModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n车型 = " + $("#subModelModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n型号 = " + $("#versionModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		
		paramObj.queryCondition = queryCondition;
	}
	
	
	/**
	 * 画线图
	 */
	function showLineChart(json)
	{
		//线图颜色数组
		var colos = ['#009C0E','#00235A','#E63110'];
		if(!json) return;
		var series = json.series;
		//循环添加数据标签展示内容
		for(var i = 0; i < series.length; i++)
		{
			var obj = series[i];
			var data = obj.data;
			if(1 != i) obj.itemStyle = {normal:{color:colos[i]}};
			else obj.itemStyle = {normal:{color:colos[i],label:{show:true}}};
			//设置最后一个数据展示标签
			var lastDataValue = data[data.length - 1];
			data[data.length - 1] = {value:lastDataValue,symbol:'emptyCircle',itemStyle:{normal:{label:{show:true,position:'right'}}}};
		}
		var option = {
 			    tooltip : {
 			        trigger: 'item',
 			        show:true
			    },
			    legend: {
			        data:['指导价(千元)','成交价(千元)','开票价(千元)']
			    },
			    toolbox: {
			        show : true,
			        feature : {
			            mark : false,
			            dataView : {readOnly: false},
			            magicType:['line'],
			            restore : true,
			            saveAsImage : true
			        }
			    },
			    xAxis : [
			        {
			            type : 'category',
			            axisTick:{lineStyle:{color:'#BBBBBB'}},
			            splitLine : {show : false,lineStyle:{color:'#F0F0F0'}},
			            axisLine:{lineStyle:{color:'#BBBBBB'}},
			            axisLabel:{interval:0},
			            data : json.xTitle
			        }
			    ],
			    yAxis : [
			        {
			        	nameTextStyle:{color:"#53A3F3"},
			            type : 'value',
			            max:json.boundarys[0],
			            min:json.boundarys[1],
			            splitNumber:json.boundarys[2],
			            splitArea : {show : false},
			            axisLine:{lineStyle:{color:'#BBBBBB'}},
			            splitLine : {show : false,lineStyle:{color:'#F0F0F0'}},
			            axisLabel:{
			            	formatter: function(v1)
				            {
			            		if(-1 == v1.toString().indexOf(".")) v1 = v1 + ".0";
				            	return v1;
				            }
			            }
			        }
			    ],
			    series : series,
			    grid:{y:30}
			};
		   myChart.clear();
 		   myChart.setOption(option);
 		   createLineChartTable(series);
	}
	
	/**
	 * 创建线图表格
	 */
	function createLineChartTable(gridJson)
	{
		var echartsWidth = $("#chartId").width() - 87;//echarts图形宽度
		var cityLength = $("#cityModalResultContainer li").length;//城市个数
		if('0' == $("#cityModalResultContainer input[name='selectedCity']").val()) cityLength = 23;
		
		var tdWidth = parseInt(echartsWidth / cityLength);
		var tableHtml = "<table style='margin-left:10px;border=1;width:"+echartsWidth+"px;'>";
		for(var i = 0; i < gridJson.length; i++)
		{
			var datas = gridJson[i].data;
			tableHtml += "<tr><td style='width:90px;text-align:left;'>"+gridJson[i].name+"</td>";
			for(var j = 0; j < datas.length; j++)
			{
				var value = datas[j];
				if(j == datas.length - 1) value = datas[j].value;
				tableHtml += "<td style='width:"+tdWidth+"px;text-align:center'>" + value + "</td>";
			}
			tableHtml += "</tr>";
		}
		tableHtml += "</table>";
		$("#lineChartTable").show();
		$("#lineChartTable").html(tableHtml);
		$("#lineChartTable").width(echartsWidth + 15);
		
		
	}
	
	/**
	 * 画雷达图
	 */
	function showRadarChart(json)
	{
		$("#lineChartTable").hide();
		if(!json) return;
		//线图颜色数组
		var colos = ['#009C0E','#00235A','#E63110','#8994A0'];
		var series = json.seriesList;
		for(var i = 0; i < series.length; i++)
		{
			var obj = series[i];
			//加权均价设置为虚线
			if(i == series.length - 1) 
			{
				obj.itemStyle = {normal:{color:colos[i],lineStyle:{type:'dotted'}}};
				obj.symbol = 'none';
			}
			//成交价为空心圈
			else if(1 == i) 
			{
				obj.itemStyle = {normal:{color:colos[i]}};
				obj.symbol = 'emptyCircle';
			}
			else 
			{
				obj.itemStyle = {normal:{color:colos[i]}};
				obj.symbol = 'circle';
			}
			//设置第一个点展示数值，成交价不展示
//			if(1 != i)
//			{s
//				var values = obj.value;
//				var firstValue = values[0];
//				values[0] = {value:firstValue,itemStyle:{normal:{label:{show:true}}}};
//			}
		}
		var option = {
			    tooltip : {
			        trigger: 'axis',
			        formatter: function(params) {
			        	//返回数据标识提示框内容
			        	var titles = "";
			        	var cityName = (params[0][3]).substr(0,(params[0][3]).indexOf("\n"));
			        	for(var i = 0; i < params.length; i++)
			        	{
			        		if(0 == i) titles +=  cityName + "<br/>" + params[i][1] + ":" + window.formatData(params[i][2]) + "<br/>";
			        		else titles += params[i][1] + ":" + window.formatData(params[i][2]) + "<br/>";
			        	}
			        	return titles;
 	                }
			    },
			    legend: {
			        data:['指导价','成交价','开票价','加权均价']
			    },
			    calculable : true,
			    polar : [
			        {
			           radius:'130',
			           scale : true,
			           splitArea : {show : false},
			           splitLine : {show : false},
			           indicator : json.poloarList
			        }
			    ],
			    series : [
			        {
			            type: 'radar',
			            data : series
			        }
			    ]
		   };
		   myChart.clear();
		   myChart.setOption(option);
 		   myChart.component.legend.setColor("指导价","#009C0E"); 
 		   myChart.component.legend.setColor("成交价","#00235A"); 
 		   myChart.component.legend.setColor("开票价","#E63110"); 
 		   myChart.component.legend.setColor("加权均价","#8994A0"); 
 		   myChart.refresh();
	}
	
	/**
	 * 创建表格
	 */
	function createTable(gridJson)
	{
		var gridTbodyHtml = "";
		var length = gridJson.length; 
		var areaName = "";
		var rowSpanArray = new Array();//区域栏合并行数组
		var rowSpanNum = 0;
		//计算各大区合并行数
		for(var n = 0; n < length; n++)
		{
			var obj = gridJson[n];
			if(areaName != obj.areaName) 
			{
				areaName = obj.areaName;
				if(0 != n) rowSpanArray.push(rowSpanNum);
				rowSpanNum = 1;
			}
			else
			{
				rowSpanNum ++;
			}
			if(n == (length - 1)) rowSpanArray.push(++rowSpanNum);
		}
		
		areaName = "";
		var index = 0;//区域栏合并行数组锁引
		for(var i = 0; i < length; i++)
		{
			var obj = gridJson[i];
			gridTbodyHtml += "<tr>";
			
			if(areaName != obj.areaName)
			{
				gridTbodyHtml += "<td class='tbodytext' rowspan='"+rowSpanArray[index]+"'>" + obj.areaName + "</td>";
				areaName = obj.areaName;
				index ++;
			}
			
			gridTbodyHtml += "<td class='tbodytext' style='height:20px;'>" + obj.cityName + "</td>";
			gridTbodyHtml += "<td class='tbodytext' style='height:20px;'>" + window.formatData(obj.tp) + "</td>";
			gridTbodyHtml += "<td class='tbodytext' style='height:20px;'>" + window.addUnit(obj.variation,false,true) + "</td>";
			gridTbodyHtml += "<td class='tbodytext' style='height:20px;'>" + window.formatData(obj.modelProfit) + "</td>";
			gridTbodyHtml += "<td class='tbodytext' style='height:20px;'>" + window.addUnit(obj.vsTp,true,false) + "</td>";
			gridTbodyHtml += "<td class='tbodytext' style='height:20px;'>" + window.addUnit(obj.vsTpRate,true,true) + "</td>";
			gridTbodyHtml += "<td class='tbodytext' style='height:20px;'>" + window.addUnit(obj.vsProfit,true,false) + "</td>";
			gridTbodyHtml += "<td class='tbodytext' style='height:20px;'>" + window.addUnit(obj.vsProfitRate,true,true) + "</td>";
			if(0 == i)
			{
				gridTbodyHtml += "<td class='tbodytext' rowspan='"+length+"' >" + window.formatData(obj.msrp) + "</td>";
				gridTbodyHtml += "<td class='tbodytext' rowspan='"+length+"' >" + window.formatData(obj.rebatePrice) + "</td>";
				gridTbodyHtml += "<td class='tbodytext' rowspan='"+length+"' >" + window.formatData(obj.rewardAssessment) + "</td>";
				gridTbodyHtml += "<td class='tbodytext' rowspan='"+length+"' >" + window.formatData(obj.promotionalAllowance) + "</td>";
			}
			gridTbodyHtml += "</tr>";
			
		}
		$("#gridTbody").html(gridTbodyHtml);
	}
	
	/**
	 * 参数验证
	 */
	function paramsValidate()
	{
		//城市ID
		var cityLength = $("#cityModalResultContainer input[name='selectedCity']").length;
		var vidLength = $("#versionModalResultContainer input[name='selectedVersion']").length;
		var modelLength = $("#subModelModalResultContainer :input[name='selectedSubModel']").length;
		
		if(0 == cityLength)
		{
			alert("请选择城市");
			return true;
		}
		if(0 == modelLength)
		{
			alert("请选择车型");
			return true;
		}
		if(0 == vidLength)
		{
			alert("请选择型号");
			return true;
		}
		
		return false;
	}
	/**
	 * 确定查询
	 */
	$('#queryBtn').on('click', function (e) {
		if(paramsValidate()) return;
		
		$('body').showLoading();
		//默认展示图表
		$("#showChart").click();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/loadCityTpRatioChartAndTable",
			   data: getParams(),
			   success: function(data){
				   if(data)
				   {
				     //点击面板折叠
				     $(".queryConditionContainer .buttons .toggle a").click();
				     
					 //展示标题
					 $("#tdTitle").show();
					 $("#tdTitle font").html($("#subModelModalResultContainer").text() + "各城市成交价格及利润状态(" + $("#versionModalResultContainer").text() + ")");
					 //是雷达图时才画表格
					 if("1" == $("#chartType").val()) 
					 {
						 createTable(data.grid);
						 $("#tSortable").show();
						 $("#radarTd").show();
					 }
					 else 
					 {
						 $("#radarTd").hide();
						 $("#tSortable").hide();
					 }
					 //画图
					 $("#chartId").height(400);
					 if("1" == $("#chartType").val()) 
					 {
						 $("#lineChartTable").hide();
						 myChart.resize();
						 showRadarChart(data.chart);
					 }
					 else
					 {
						 myChart.resize();
						 showLineChart(data.chart);
					 } 
					 
				   }
				   $('body').hideLoading();
			   },
			   error:function(){
				   $('body').hideLoading();
			   }
			});
	});
	
	$('#resetBtn').on('click', function (e) {
		//城市容器
		$('#cityModalResultContainer').html("");
		//车型容器
		$('#subModelModalResultContainer').html("");
		//型号容器
		$('#versionModalResultContainer').html("");
		
		$('#formId :reset');	
	});
});

/**
 * 导出Excel
 * @param languageType EN:英文版;ZH:中文版
 */
function exportExcel(languageType)
{
	//雷达图
	if("1" == $("#chartType").val())
	{
		if(!$.trim($("#gridTbody").html()))
		{
			alert("暂无数据导出");
			return;
		}
	}
	//折线图
	else
	{
		if(!$.trim($("#lineChartTable").html()))
		{
			alert("暂无数据导出");
			return;
		}
	}
	
	$("#languageType").val(languageType);
	$("#exportFormId").submit();
}

/**
 * 车型弹出框
 * @param type
 */
function showSubModel(type)
{
	if(type==1){
		$("#choseType").attr("value","1")//本竞品
	}else if(type==2){
		$("#choseType").attr("value","2")//细分市场
	}else if(type==3){
		$("#choseType").attr("value","3")//品牌
	}else if(type==4){
		$("#choseType").attr("value","4")//厂商
	}
	
	/*根据选择条件隐藏*/
	var pooAttributeIdArr = [];
	$(".subModelModalContainer").find('.pooAttributeIdInput').each(function(){
		
		if($(this).attr("checked")){
			
			pooAttributeIdArr[pooAttributeIdArr.length] = $(this).val();
		}
	});	
	
	$(".subModelModalContainer").find(".subModelIdInput").each(function(){
		var flag = false;
		for(var i=0;i<pooAttributeIdArr.length;i++){
			if(pooAttributeIdArr[i] == $(this).attr("pooAttributeId")){
				flag = true;
				break;
			}
		}
		if(flag){
			$(this).parent().show();
		}else{
			$(this).parent().hide();
		}
	});
	/*根据选择条件隐藏结束*/
	var inputType = "2";//默认弹出框为单选
	
	if('1' == type) getModelPage("tabs-competingProducts",type,inputType,"1");
	else if('2' == type) getModelPage("tabs-segment",type,inputType,"1");
	else if('3' == type) getModelPage("tabs-brand",type,inputType,"1");
	else getModelPage("tabs-manf",type,inputType,"1");
};
