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
		var inputType = "1";//控件默认多选
		
		//城市ID
		var citys = $("#cityModalResultContainer input[name='selectedCity']").map(function(){return $(this).val();}).get().join(",");
		//如果是全国均价，则取弹出枉里的城市ID
		var beginDate_TPMix=beginDate.substr(0,4)-1;
		if(0 == citys){
			if(beginDate_TPMix>2013){
				citys = "0";
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
		
		queryCondition += "时间 = " + $("#startDate").val();
		
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
		var colos = ['#0090FF','#E63110'];
		if(!json) return;
		var series = json.series;
		//循环添加数据标签展示内容
		for(var i = 0; i < series.length; i++)
		{
			var obj = series[i];
			obj.symbolSize = '5';
			obj.itemStyle = {normal:{color:colos[i],lineStyle:{width:0},label:{show:true,position:'right',textStyle:{color:'#000'}}}};
			//画辅助线
			if(0 == i)
			{
				obj.markLine = {
						tooltip:{show:false}
						,itemStyle:{normal:{lineStyle:{type:'solid',color:'#909090',width:2}}}
						,symbol:'none'
						,data:json.markList
				};
				obj.tooltip = {show:false};
			}
		}
		var option = {
 			    tooltip : {
 			        trigger: 'axis',
 			        show:true,
 			       formatter: function(params)
 			       {
 			    	   var startDate = $("#startDate").val();
 			    	   var month = startDate.split("-")[1];
 			    	   var msrpObj = params[0];
 			    	   var tpObj = params[1];
 			    	   var discount = (parseFloat(msrpObj[2]) - parseFloat(tpObj[2])) * 1000;
 			    	   if(0 > discount) {
 			    		   discount = "<font style='color:#E63110'>" + window.formatData(discount) + "</font>";
 			    	   } else {
 			    		   discount = window.formatData(discount);
 			    	   }
 			    	   
 			    	   var title = month + "月<br/>"+(msrpObj[1]).replace("\n"," ")+"<br/>MSRP:"+window.formatData((parseFloat(msrpObj[2])*1000))
 			    	   			   +"<br/>" + "TP:" + window.formatData((parseFloat(tpObj[2])*1000)) +"<br/>折扣:" + discount;
 			    	   return title;
 			       }
			    },
			    legend: {
			        data:['MSRP','TP']
			    },
			    toolbox: {
			        show : false,
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
			        	name:'price(千元)',
			        	nameTextStyle:{color:"#53A3F3"},
			            type : 'value',
			            max:json.boundarys[0],
			            min:json.boundarys[1],
			            splitNumber:json.boundarys[2],
			            splitArea : {show : false},
			            axisLine:{lineStyle:{color:'#BBBBBB'}},
			            splitLine : {show : true,lineStyle:{color:'#F0F0F0'}},
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
			    grid:{y:35,x:115}
			};
		   myChart.clear();
 		   myChart.setOption(option);
 		   $("#discountLineDiv").show();
 		   //保存线图刻度最大值与最小值
          $("#ymax").val(json.boundarys[0]);
 		  $("#ymin").val(json.boundarys[1]);
	}
	
	
	/**
	 * 创建表格
	 */
	function createTable(gridJson)
	{
		//设置动态表格宽度
		var echartsWidth = $("#chartId").width() - 140;//echarts图形宽度
		var cityLength = $("#versionModalResultContainer li").length;//型号个数
		var tdWidth = parseInt(echartsWidth / cityLength);
		$("#discountLineTable").width(echartsWidth + 62);
		
		var theadHtml = "<thead><tr><th>型号</th>";
		var tbodyHtml = "<tbody>";
		var firstTd = ['MSRP','TP','折扣','折扣率','VS上月','VS变化'];
		
		for(var i = 0; i < firstTd.length; i++)
		{
			var className = "tbodytext";
			if(0 == i % 2) className = "tbodytext odd";
			
			tbodyHtml += "<tr>";
			tbodyHtml += "<td class='"+className.replace('tbodytext','')+"' style='text-align: center;width:130px;'>" + firstTd[i] + "</td>";
			
			for(var j = 0;j < gridJson.length; j++)
			{
				var obj = gridJson[j];
				//画标题和MSRP行
				if(0 == i) 
				{
					theadHtml += "<th>" + obj.versionName.replace("{0}","<br/>") + "</th>";
					tbodyHtml += "<td class='"+className+"' style='width:"+tdWidth+"px;' >" + window.formatData(obj.msrp) + "</td>";
				}
				if(1 == i) tbodyHtml += "<td class='"+className+"' style='width:"+tdWidth+"px;' >" + window.formatData(obj.tp) + "</td>";
				if(2 == i) tbodyHtml += "<td class='"+className+"' style='width:"+tdWidth+"px;' >" + window.addUnit(obj.discount,true,false) + "</td>";
				if(3 == i) tbodyHtml += "<td class='"+className+"' style='width:"+tdWidth+"px;' >" + window.addUnit(obj.discountRate,true,true) + "</td>";
				if(4 == i) tbodyHtml += "<td class='"+className+"' style='width:"+tdWidth+"px;' >" + window.addUnit(obj.vsDiscount,true,false) + "</td>";
				if(5 == i) tbodyHtml += "<td class='"+className+"' style='width:"+tdWidth+"px;' >" + window.addUnit(obj.vsDiscountRate,true,true) + "</td>";
			}
			tbodyHtml += "</tr>";
		}
		theadHtml += "</tr></thead>";
		tbodyHtml += "</tbody>";
		$("#discountLineTable").html(theadHtml + tbodyHtml);
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
			   url: ctx+"/loadVersionDiscountRatioChartAndTable",
			   data: getParams(),
			   success: function(data){
				   if(data)
				   {
				     //点击面板折叠
				     $(".queryConditionContainer .buttons .toggle a").click();
				     $("#chartId").height(400);
					 myChart.resize();
					 //画图
					 showLineChart(data.chart);
					 //画表格
					 createTable(data.grid);
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
	if(!$.trim($("#discountLineTable").html()))
	{
		alert("暂无数据导出");
		return;
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
	var inputType = "1";//默认弹出框为多选
	
	if('1' == type) getModelPage("tabs-competingProducts",type,inputType,"1");
	else if('2' == type) getModelPage("tabs-segment",type,inputType,"1");
	else if('3' == type) getModelPage("tabs-brand",type,inputType,"1");
	else getModelPage("tabs-manf",type,inputType,"1");
};
