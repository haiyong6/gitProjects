/** 定义车身形式LI全局变量 */
var currBodyTypeLI = null;
/** 已查询数据的全局变量 */
var selectData = null;

function initDate() {
	$("#startDate-container.input-append.date").datetimepicker({
		format : "yyyy-mm",
		language : "zh-CN",
		todayBtn : 0,
		autoclose : 1,
		startView : 3,
		maxView : 3,
		minView : 3,
		startDate : beginDate,
		endDate : defaultBeginDate,
		showMeridian : 1
	});
}

$(document).ready(function() {
	initDate();
	$("#chartType").val("1");
	refreshSortTable();
	positionStyleReset();
});

/**展示车型弹出框-开始*/
/** 展示车型弹出框*/
$("#subModelModal").on("show", function (e) {
	if(e.relatedTarget) {
		return; //修复bootstrap的modal引入tabpane时，触发事件问题。
	}
	//加载子车型数据
	showLoading("subModelModalBody");
	$("#subModelModalBody").load(ctx + "/price/subModelPriceAnalysis/getSubModelModal", getParams(), function() {
		//弹出框设置默认选中项结果集		
		var strHTML = '<ul class="inline" >';
		$(".subModelModalResultContainer input").each(function() {
			var subModelId = $(this).val();
			var subModelName = $(this).attr("subModelName");
			var pooAttributeId = $(this).attr("pooAttributeId");
			var letter = $(this).attr("letter");
			strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
		  	strHTML += '<div class="removeBtnByResult label label-info" subModelId="' + subModelId + '"  pooAttributeId="' + pooAttributeId;
		  	strHTML += '" letter="' + letter + '" subModelName="' + subModelName + '" style="cursor:pointer" title="删除：' + subModelName + '">';
			strHTML += '<i class="icon-remove icon-white"></i>' + subModelName;
		  	strHTML += '</div>';
		 	strHTML += '</li>';
	 		$(".subModelModalContainer .subModelIdInput").each(function() {
				if($(this).val() == subModelId) {
					$(this).attr("checked","true");//行全选
				}
			});
		});
		strHTML += '</ul>';
		$("#selectorResultContainerBySubModel").html(strHTML);
	});
});
/**展示车型弹出框-结束*/

/**展示车身形式弹出框*/
/** 弹出车身形式对话框*/
$(".bodyTypeSelector").live("click", function() {
	currBodyTypeLI = $(this).parents("table").find(".bodyTypeModalResultContainer");
	$("#bodyTypeModal").modal("show");
});
$("#bodyTypeModal").on("show", function (e) {
	if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
	//去掉默认选中的效果
	$(".bodyTypeModalByAll").each(function(){
		$(this).removeAttr("checked");//取消行全选
	});
	/**打开车身形式选择框时，清空车型选择*/
	$("#subModelModalResultContainer").html("");
	//加载子车型数据
	showLoading("bodyTypeModalBody");
	$("#bodyTypeModalBody").load(ctx + "/price/subModelPriceAnalysis/getBodyType", getParams(), function() {
		//弹出框设置默认选中项结果集		
		$(currBodyTypeLI).find("input").each(function() {
			var bodyTypeId = $(this).val();
			if(bodyTypeId == "0") {
				$(".bodyTypeModalByAll").each(function() {
					$(this).attr("checked","true");//全选
				});
				$(".bodyTypeModalContainer .bodyTypeModal").each(function() {
					$(this).attr("checked","true");//行全选
				});
			} else {
				$(".bodyTypeModalContainer .bodyTypeModal").each(function() {
					if($(this).val() == bodyTypeId) {
						$(this).attr("checked","true");//行全选
					}
				});
			}
		});
	});
});

/**点击确定生成内容*/
$(".bodyTypeModalContainer").find(".confirm").live("click", function() {
	var containerId = $(this).parents(".bodyTypeModalContainer").attr("id");
	var relInputName = $(this).attr("relInputName");
	var strHTML = "";
	strHTML += '<ul class="selectorResultContainer">';
	//如果全部选中，则生成全部
	if($(".bodyTypeModalByAll").attr("checked")) {
		strHTML += '<li>';
		strHTML += '<div class="removeBtn" relContainer="bodyTypeModal" value="0" style="cursor:pointer;" title="全部">';
		strHTML += '<input type="hidden" value="0" name="selectedBodyType" />';
		strHTML += '全部<i class="icon-remove" style="visibility: hidden;"></i>';
		strHTML += '</div>';
		strHTML += '</li>';
	} else {							
		$(this).parents(".bodyTypeModalContainer").find(".bodyTypeModal:checked").each(function() {
			strHTML += '<li>';
			strHTML += '<div class="removeBtn" relContainer="' + containerId + '" value="' + $(this).val() + '" style="cursor:pointer" title="删除：' 
			              + $.trim($(this).parent().text()) + '">';
			strHTML += '<input type="hidden" value="' + $(this).val() + '" name="' + relInputName + '" levelType="1" />';
			strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
			strHTML += '</div>';
			strHTML += '</li>';
		});
	}
	strHTML += '</ul>';
	$("#bodyTypeModalResultContainer").html(strHTML);
});

//本竞品
$("#tabs-competingProducts .subModelIdInput").live("click", function() {
	//把其他维度选中的取消
//	$("#tabs-segment").find(".subModelIdInput:checked").each(function() {
//		$(this).removeAttr("checked");
//	});
//	$("#tabs-brand").find(".subModelIdInput:checked").each(function() {
//		$(this).removeAttr("checked");
//	});
//	$("#tabs-manf").find(".subModelIdInput:checked").each(function() {
//		$(this).removeAttr("checked");
//	});
	
	//显示选中的值	——开始
	//去掉重复选项
	$("#selectorResultContainerBySubModel").html();
	var allSubModelArr = [];
	$("#tabs-competingProducts").find(".subModelIdInput:checked").each(function() {
		var obj = {};
		obj.subModelId = $(this).val();
		obj.subModelName = $.trim($(this).parent().text());
		obj.letter = $(this).attr("letter");
		obj.pooAttributeId = $(this).attr("pooAttributeId");
		allSubModelArr[allSubModelArr.length] = obj;
	});
	allSubModelArr = uniqueSubModel(allSubModelArr); 
	var strHTML = '<ul class="inline" >';
	for(var i = 0; i < allSubModelArr.length; i++) {
		strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
	  	strHTML += '<div class="removeBtnByResult label label-info" subModelId="' + allSubModelArr[i].subModelId + '"  pooAttributeId="';
	  	strHTML += allSubModelArr[i].pooAttributeId + '" letter="' + allSubModelArr[i].letter + '" subModelName="' + allSubModelArr[i].subModelName;
	  	strHTML += '" style="cursor:pointer" title="删除：' + allSubModelArr[i].subModelName + '">';
		strHTML += '<i class="icon-remove icon-white"></i>'+allSubModelArr[i].subModelName;
	  	strHTML += '</div>';
	 	strHTML += '</li>';
	 }
	 strHTML += '</ul>';
	$("#selectorResultContainerBySubModel").html(strHTML);
	//显示选中的值	——结束
});

//细分市场
$("#tabs-segment .subModelIdInput").live("click", function() {
	//把其他维度选中的取消
	$("#tabs-competingProducts").find('.subModelIdInput:checked').each(function() {
		$(this).removeAttr("checked");
	});
	$("#tabs-brand").find(".subModelIdInput:checked").each(function() {
		$(this).removeAttr("checked");
	});
	$("#tabs-manf").find(".subModelIdInput:checked").each(function() {
		$(this).removeAttr("checked");
	});
	//显示选中的值	——开始
	//去掉重复选项
	$("#selectorResultContainerBySubModel").html();
	var allSubModelArr = [];
	$("#tabs-segment").find(".subModelIdInput:checked").each(function() {
	    var obj = {};
		obj.subModelId = $(this).val();
		obj.subModelName = $.trim($(this).parent().text());
		obj.letter = $(this).attr("letter");
		obj.pooAttributeId = $(this).attr("pooAttributeId");
		allSubModelArr[allSubModelArr.length] = obj;
	});
	allSubModelArr = uniqueSubModel(allSubModelArr); 
	var strHTML = '<ul class="inline" >';
	for(var i=0;i<allSubModelArr.length;i++) {
		strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
	  	strHTML += '<div class="removeBtnByResult label label-info" subModelId="' + allSubModelArr[i].subModelId + '"  pooAttributeId="'; 
	  	strHTML += allSubModelArr[i].pooAttributeId + '" letter="' + allSubModelArr[i].letter + '" subModelName="' + allSubModelArr[i].subModelName;
	  	strHTML += '" style="cursor:pointer" title="删除：' + allSubModelArr[i].subModelName + '">';
		strHTML += '<i class="icon-remove icon-white"></i>' + allSubModelArr[i].subModelName;
	  	strHTML += '</div>';
	 	strHTML += '</li>';
	 }
	 strHTML += '</ul>';
	$("#selectorResultContainerBySubModel").html(strHTML);
	//显示选中的值	——结束
});

//品牌
$("#tabs-brand .subModelIdInput").live("click", function() {
	//把其他维度选中的取消
	$("#tabs-competingProducts").find(".subModelIdInput:checked").each(function() {
		$(this).removeAttr("checked");
	});
	$("#tabs-segment").find(".subModelIdInput:checked").each(function() {
		$(this).removeAttr("checked");
	});
	$("#tabs-manf").find(".subModelIdInput:checked").each(function() {
		$(this).removeAttr("checked");
	});
	//显示选中的值	——开始
	//去掉重复选项
	$("#selectorResultContainerBySubModel").html();
	var allSubModelArr = [];
	$("#tabs-brand").find(".subModelIdInput:checked").each(function() {
		var obj = {};
		obj.subModelId = $(this).val();
		obj.subModelName = $.trim($(this).parent().text());
		obj.letter = $(this).attr("letter");
		obj.pooAttributeId = $(this).attr("pooAttributeId");
		allSubModelArr[allSubModelArr.length] = obj;
	});
	allSubModelArr = uniqueSubModel(allSubModelArr); 
	var strHTML = '<ul class="inline" >';
	for(var i = 0; i < allSubModelArr.length; i++) {
		strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
	  	strHTML += '<div class="removeBtnByResult label label-info" subModelId="' + allSubModelArr[i].subModelId + '"  pooAttributeId="';
	  	strHTML += allSubModelArr[i].pooAttributeId + '" letter="' + allSubModelArr[i].letter + '" subModelName="' + allSubModelArr[i].subModelName;
	  	strHTML += '" style="cursor:pointer" title="删除：' + allSubModelArr[i].subModelName + '">';
		strHTML += '<i class="icon-remove icon-white"></i>' + allSubModelArr[i].subModelName;
	  	strHTML += '</div>';
	 	strHTML += '</li>';
	 }
	 strHTML += '</ul>';
	$("#selectorResultContainerBySubModel").html(strHTML);
	//显示选中的值	——结束
});

//厂商
$("#tabs-manf .subModelIdInput").live("click", function() {
	//把其他维度选中的取消
	$("#tabs-competingProducts").find(".subModelIdInput:checked").each(function() {
		$(this).removeAttr("checked");
	});
	$("#tabs-segment").find(".subModelIdInput:checked").each(function() {
		$(this).removeAttr("checked");
	});
	$("#tabs-brand").find(".subModelIdInput:checked").each(function() {
		$(this).removeAttr("checked");
	});
	//显示选中的值	——开始
	//去掉重复选项
	$("#selectorResultContainerBySubModel").html();
	var allSubModelArr = [];
	$("#tabs-manf").find(".subModelIdInput:checked").each(function() {
		var obj = {};
		obj.subModelId = $(this).val();
		obj.subModelName = $.trim($(this).parent().text());
		obj.letter = $(this).attr("letter");
		obj.pooAttributeId = $(this).attr("pooAttributeId");
		allSubModelArr[allSubModelArr.length] = obj;
	});
	allSubModelArr = uniqueSubModel(allSubModelArr); 
	var strHTML = '<ul class="inline" >';
	for(var i = 0; i < allSubModelArr.length; i++) {
		strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
	  	strHTML += '<div class="removeBtnByResult label label-info" subModelId="' + allSubModelArr[i].subModelId + '"  pooAttributeId="';
	  	strHTML += allSubModelArr[i].pooAttributeId + '" letter="' + allSubModelArr[i].letter + '" subModelName="' + allSubModelArr[i].subModelName;
	  	strHTML += '" style="cursor:pointer" title="删除：' + allSubModelArr[i].subModelName + '">';
		strHTML += '<i class="icon-remove icon-white"></i>'+allSubModelArr[i].subModelName;
	  	strHTML += '</div>';
	 	strHTML += '</li>';
	 }
	 strHTML += '</ul>';
	$("#selectorResultContainerBySubModel").html(strHTML);
	//显示选中的值	——结束
});
	
/**图表类型改变事件**/
$("#chartType").on("change", function (e) {
	var obj = $(this);
	if(obj.val() == "1") {
		// 选择散点图时,不显示指导价&成交价
		$("#priceType option[value='3']").remove();
		$("#setDiv").css("display", "block");
		$(".showTd").css("display","");
		$(".showTd1").css("display","none");//空白占位置
		$("#priceType").val("1");
		$("#priceType").trigger("change");
	} else {
		$("#priceType").append("<option value='3' >指导价&成交价</option>");
		$("#setDiv").css("display", "block");
		$(".showTd").css("display","none");
		$(".showTd1").css("display","block");
		$("#priceType").val("1");
		$("#priceType").trigger("change");
	}
});

/** 价格信息下拉框改变事件*/
$("#priceType").on("change", function() {
	var priceType = $(this).val();
	$("body").showLoading();
	//去后台查询成交价的时间范围
	$.ajax({
		   type: "POST",
		   url: ctx + "/price/subModelPriceAnalysis/changeDate",
		   data: getParams(),
		   success: function(data) {
			   if(data) {
				   $.each(data, function(i, k) {
				       if("defaultBeginDate" == i) {
				    	   defaultBeginDate = k;
				    	   endDate = k;
				    	   //先清除日期控件参数
				    	   $("#startDate-container.input-append.date").datetimepicker("remove");
				    	   //重新设置日期控件参数
				    	   initDate();
				    	   //赋值显示日期为最新日期
				    	   $("#startDate").val(k);
				       }
			       });   
			   }
			   $("body").hideLoading();
		   },
		   error: function() {
			   $("body").hideLoading();
		   }
	});
});

/** 重置按钮事件-开始 */
$("#resetBtn").on("click", function (e) {
	//车型容器
	$("#subModelModalResultContainer").html("");
	//清空车身形式
	$("#bodyTypeModalResultContainer").html(""); 
	//去除指导价&成交价选项
	$("#priceType option[value='3']").remove();
	$("#formId :reset");	
	var chart = $("#chartTitleDiv");
	chart.html("");
	$("#setDivName").css("display","none");//标题隐藏
	$("#chartTitleDivNoData").hide();
});
/** 重置按钮事件-结束 */
/** 车型控件值鼠标经过事件*/
$(".removeBtn").live("mouseover", function() {
	$(this).find(".icon-remove").css({visibility:"visible"});
});

/**车型控件值鼠标离开事件*/
$(".removeBtn").live("mouseout", function() {
	$(this).find(".icon-remove").css({visibility:"hidden"});
});

/** 查询按钮事件-开始 */
$("#queryBtn").on("click", function (e) { 
	var params = getParams(); 
    if(paramsValidate(params)) {
    	return;
    }
    params.refreshType = "false";//不刷新图表
    $("#setDivName").css("display","none");
    $("#chartTitleDivNoData").hide();
	$(".showConditionContainer .buttons .toggle a").click();
    $("body").showLoading(); 
    //默认展示图表
    $("#chartTitleDiv").height(500);
    //发送请求 
    $.ajax({ type: "POST",
             url: ctx + "/price/subModelPriceAnalysis/getAnalysisData", 
             data: params,
             success: function(data) {
  			     if(data) {
  			    $(".queryConditionContainer .buttons .toggle a").click();
  			  	$("#setDivName").css("display","block");//标题显示
  			  	if($("#chartType").val() == 2){
  			  	$("#setDivName font").html("Price Range");
  			  	}
				     $("#chartTitleDivNoData").hide();
				     $("#tSortableNoData").hide();
				     $("#chartTitleDiv").show();
				     $("#tSortable").show();
		        	 selectData = data;
		        	 showAllSubModel(data);
		        	 if($("#chartType").val() == "1") {
		        		 showScatterChart(data);
		        	 } else {
		        		 showBarChart(data);
		        	 }
  				 } else {
  					 $("#chartTitleDivNoData").show();
  					 $("#tSortableNoData").show();
  					 $("#chartTitleDiv").hide();
  					 $("#tSortable").hide();
  				 }
  				 $("body").hideLoading();
  			 },
  			 error:function() {
  				 $("#chartTitleDivNoData").show();
  				 $("#tSortableNoData").show();
  				 $("#chartTitleDiv").hide();
  				 $("#tSortable").hide();
  				 $("body").hideLoading();
  			 }
  	});
});
/** 查询按钮事件-结束 */

/**展示散点图**/
function showScatterChart(data) {
	var chart = $("#chartTitleDiv");
	chart.html("");
	var series = data.series;
	var xTitleEns = data.xTitleEns;
	var xTitleIds = data.xTitleIds;//车型id
	var yName = "MSRP(RMB)";
	var myChart = echarts.init(document.getElementById("chartTitleDiv"));
	var showType = $(".showType:checked").val();
	var chartType = $("#chartType").val();
	var priceType = $("#priceType").val();
	if(priceType == "2") {
		yName = "TP(RMB)";
	}
	var yAxis = data.yAxis;
	var allData = getScatterData(series, showType, chartType);
	option = {
	    "grid": { y: 40, y2: 80, x: 60, x2: 80 },
	    "xAxis": [
	        {
	            "type": "category",
	            "splitLine": {
	                "show": false
	            },
	            "data": xTitleEns,
	            "axisTick": {
	                "show": false
	            },
	            "axisLabel": {
	                "interval": 0
	            },
	            "splitLine": {
	                "show": true,//网格线显示
	                "lineStyle":{
	                	"type":"dotted"//点状
	                }
	            }
	        }
	    ],
	    "yAxis": [
	        {
	            "type": "value",
	            "splitNumber": yAxis[3],
	            "interval": yAxis[2],
	            "min": yAxis[0],
	            "max": yAxis[1],
	            "splitLine": {
	                "show": true,//网格线显示
	                "lineStyle":{
	                	"type":"dotted"//点状
	                }
	            },
	            "name": yName,
	            "nameGap": 18,
	            "nameTextStyle": {
	                "color": "#000",
	                "fontFamily": "微软雅黑",
	                "position": "left"
	            },
	            "axisLabel": {
	                "margin": 10
	            }
	        }
	    ],
	    "series": allData,
	    "animation": true,
	    "backgroundColor": "#ffffff"
	} 
	myChart.clear();
//	console.log(JSON.stringify(option));
	myChart.setOption(option);
}


/**获取要显示的散点图数据**/
function getScatterData(series, showType, chartType) {
	var scatterData = new Array();
	var position = 0;
	var positionFlag = 0;
	var borderColor = "rgb(174, 212, 248)";
	var priceType = $("#priceType").val();
	var labels = new Array();
	//颜色代码
	var colorGroup = ["255,85,85", "85,85,255", "85,255,85", "255,255,85", "255,85,255", "85,255,255", "255,175,175", "128,128,128", "255,85,85", "85,85,255", "85,255,85", "255,255,85", "255,85,255", "85,255,255", "255,175,175", "128,128,128",
	                  "255,85,85", "85,85,255", "85,255,85", "255,255,85", "255,85,255", "85,255,255", "255,175,175", "128,128,128"];
	
	var symbolGroup = ['rect','circle',  'triangle','diamond','roundRect',   'pin', 'arrow',
	                   'rect','circle',  'triangle','diamond','roundRect',   'pin', 'arrow',
	                   'rect','circle',  'triangle','diamond','roundRect',   'pin', 'arrow',
	                   'rect','circle',  'triangle','diamond','roundRect',   'pin', 'arrow'];//图形标志
	//散点图和二维气泡图不作处理,三维气泡图把图形透明度提高
	if(chartType == 2) {
		borderColor = "rgb(174, 212, 248)";
	} else {
		borderColor = "#fff";
	}
	//手动排挡显示类型 1为同列显示,2为分开显示
	if(showType == 2) {
		positionFlag = 30;
	}
	//把要展示的标签放在一个数组里
   	$("#sortLabel .order li").each(function() {
		var obj = $(this);
		if(obj.attr("on") == "true") {
			labels.push($(this).find("input").val());
		}
	});
    var num = 1;
	for(var i = 0; i < series.length; i++) {
	    var data = series[i];
	    var newData = new Array(data.list.length);
	    var color = "";
	    var symbols = "diamond";
	    if(i < colorGroup.length) {
	    	color = "rgb(" + colorGroup[i] + ")";
	    } else {
	    	color = "rgb(" + colorGroup[colorGroup.length - (i - colorGroup.length + 1)] + ")";
	    }
	    if(i < symbolGroup.length) {
	    	symbols = symbolGroup[i];
	    } 
	   
	    for(var k = 0; k < data.list.length; k++) {
	      	var versionObj = data.list[k];
	       	if(versionObj.transMission == "MT") {
	       		position = 0 + positionFlag;
	       	} else {
	       		position = 0 - positionFlag;
	       	}
	       	var labelName = changeLabelName(priceType, labels, versionObj);
	       	var subModelNameEn = data.subModelNameEn;
	       	var obj = 
                 {
 		   		"value": [
	                   subModelNameEn,
	                   versionObj.price
	                   ],
	            "symbol": symbols,
                "symbolSize": 12,
                "symbolOffset": [position, 0],
                "label": {
                	"normal": {
                		"show": true,
                		"formatter": labelName,
                		"position": "right",
                		"textStyle": {
                			"color": "black"
                		}
                	}
                },
                "itemStyle": {
                	"normal": {
                		"color": color,
                		"borderColor": borderColor
                	},
                	"emphasis": {
                		"color": "#B1CF57",
                		"borderWidth": 1.2,
                		"borderColor": "#fff"
                	}
                }
	        }
	        newData[k] = obj;
	    }
	    scatterData[i] = 
	        {
	            "type": "scatter",
	            "data": newData
		    }
	}
    return scatterData;
}

/**修改散点图标签显示数值**/
function changeLabelName(priceType, labels, versionObj) {
	var labelName = "";
	//获取标签显示内容数组
	for(var i = 0; i < labels.length; i++) {
		var label = labels[i];
		if(label == "1") {
			labelName += versionObj.versionShortNameEn;
		} else if(label == "2") {
			if(priceType != "3") {
				labelName += "[" + changeToThousand(versionObj.price) + "]";
			} else {
				labelName += "[" + changeToThousand(versionObj.msrp) + " " + changeToThousand(versionObj.tp) + "]";
			}
		} else {
			labelName += versionObj.bodyTypeNameEn;
		}
		labelName += "  ";
	}
	return labelName;
}

/**展示柱状图**/
function showBarChart(data) {
	var chart = $("#chartTitleDiv");
	chart.html("");
	var series = data.series;
	var xTitleEns = data.xTitleEns;
	var yName = "";
	var yAxis = data.yAxis;
	var myChart = echarts.init(document.getElementById("chartTitleDiv"));
	var priceType = $("#priceType").val();
	var yShow = true;//y轴网格显示与否
	var xShow = true; //x轴显示与否
	if(priceType == 3){
		yShow = false;
		xShow = false;
	} 
	if(priceType == "1") {
		yName = "MSRP(RMB)";
	} else if(priceType == "2") {
		yName = "TP(RMB)";
	} else {
		yName = "MSRP/TP(.000)";
		yAxis = turnToThousand(yAxis);
		yAxis[0] = parseInt(yAxis[0]);
		yAxis[1] = parseInt(yAxis[1]);
		yAxis[2] = parseInt(yAxis[2]);
	}
	var allData = getBarData(data, priceType);
	option = {
	    "grid": { y: 40, y2: 80, x: 60, x2: 80 },
	    "legend": {
	    	"show": priceType == "3",
	    	"left": 1,
	    	"itemWidth": 60,
	    	"itemHeight": 3,
	    	"data":["MSRP", "TP"],
	    	"selectedMode": false
	    },
	    "xAxis": [
	        {
	            "type": "category",
	            "splitLine": { 
	            	"show": priceType == 3,
	                "lineStyle": { 
	                	"color": "black",
	                    "type": "dashed" 
	                } 
	            },
	            "data": xTitleEns,
	            "splitLine": {
	                "show": xShow,//网格线显示
	                "lineStyle":{
	                	"type":"dotted"//点状
	                }
	            }
	        }
	    ],
	    "yAxis": [
	        {
	            "type": "value",
	            "splitNumber": 6,
	            "interval": yAxis[2],
	            "min": yAxis[0],
	            "max": yAxis[1],
	            "name": yName,
	            "nameGap": 10,
	            "nameTextStyle": {
	                "color": "#000",
	                "fontFamily": "微软雅黑",
	                "position": "left"
	            },
	            "axisLabel": {
	                "margin": 10
	            },
	            "splitLine": {
	                "show": yShow,//网格线显示
	                "lineStyle":{
	                	"type":"dotted"//点状
	                }
	            }
	        }
	    ],
	    "series": allData,
	    "animation": true,
	    "backgroundColor": "#ffffff"
	} 
	myChart.clear();
	//console.log(JSON.stringify(option));
	myChart.setOption(option);
}

/**获取要显示的柱状图数据**/
function getBarData(data, priceType) {
	var series = data.series;
	var newSeries = new Array();
	var zLevel = 1000;
	var subModelDatas = new Array();
	var emptyDatas = new Array();
	var msrpDatas = new Array();
	var tpDatas = new Array();
	var msrpEmptyDatas = new Array();
	var tpEmptyDatas = new Array();
	for(var i = 0; i < series.length; i++) {
	    var subModel = series[i];
	    if(priceType == "3") {
	    	msrpDatas[i] = parseInt(subModel.maxMsrp) - parseInt(subModel.minMsrp);
	    	tpDatas[i] = parseInt(subModel.maxTp) - parseInt(subModel.minTp);
	    	msrpEmptyDatas[i] = parseInt(subModel.minMsrp);
	    	tpEmptyDatas[i] = parseInt(subModel.minTp);
	    } else {
	    	subModelDatas[i] = parseInt(subModel.maxPrice) - parseInt(subModel.minPrice)
	    	emptyDatas[i] = parseInt(subModel.minPrice);
	    }
	}
	if(priceType == "3") {
		msrpDatas = turnToThousand(msrpDatas);
		tpDatas = turnToThousand(tpDatas);
		msrpEmptyDatas = turnToThousand(msrpEmptyDatas);
		tpEmptyDatas = turnToThousand(tpEmptyDatas);
		var msrpObj = 
		{
			"name": "MSRP",
		    "type": "bar",
			"stack": "msrp",
//			"barWidth": 30,
			"barMaxWidth": 40,
//			"barGap": 2,
			"z": zLevel--,
			"itemStyle": {
				"normal": {
					"color": "rgba(174, 212, 248, 100)"
				}
			},
			"label": {
				"normal": {
					"show": true,
					"formatter": function(a) {
						var  num = (parseFloat(msrpEmptyDatas[a.dataIndex]) + parseFloat(a.value)).toFixed(1);
						return  num;
					},
					"position": "top",
					"textStyle": {
						"color": "black"
					}
				}
			},
			"data": msrpDatas	
		};
		var msrpEmptyObj = 
		{
			"type": "bar",
			"stack": "msrp",
//			"barWidth": 30,
			"barMaxWidth": 40,
//			"barGap": 2,
			"itemStyle": {
				"normal": {
					"barBorderColor": "rgba(0, 0, 0, 0)",
					"color": "rgba(0, 0, 0, 0)"
				},
				"emphasis": {
					"barBorderColor": "rgba(0, 0, 0, 0)",
					"color": "rgba(0, 0, 0, 0)"
				}
			},
			"label": {
				"normal": {
					"show": true,
					"position": "insideTop",
					"textStyle": {
						"color": "black"
					}
				}
			},
			"data": msrpEmptyDatas
		};
		var tpObj = 
		{
		    "name": "TP",
		    "type": "bar",
			"stack": "tp",
//			"barWidth": 30,
			"barMaxWidth": 40,
//			"barGap": 2,
			"itemStyle": {
				"normal": {
					"color": "rgba(244, 164, 96, 100)"
				}
			},
			"label": {
				"normal": {
					"show": true,
					"position": "top",
					"textStyle": {
						"color": "black"
					},
					"formatter": function(a) {
						var num = (parseFloat(tpEmptyDatas[a.dataIndex]) + parseFloat(a.value)).toFixed(1);
						if(num == 0.0) {
							num = 0;
						}
						return num;
					}
				}
			},
			"data": tpDatas	
		};
		var tpEmptyObj = 
		{
			"type": "bar",
			"stack": "tp",
//			"barWidth": 30,
			"barMaxWidth": 40,
//			"barGap": 2,
			"itemStyle": {
				"normal": {
					"barBorderColor": "rgba(0, 0, 0, 0)",
					"color": "rgba(0, 0, 0, 0)"
				},
				"emphasis": {
					"barBorderColor": "rgba(0, 0, 0, 0)",
					"color": "rgba(0, 0, 0, 0)"
				}
			},
			"label": {
				"normal": {
					"show": true,
					"position": "insideTop",
					"textStyle": {
						"color": "black"
					},
					"formatter": function(a) {
						var num = (parseFloat(tpEmptyDatas[a.dataIndex])).toFixed(1);
						if(num == 0.0) {
							num = "";
						}
						return num;
					}
				}
			},
			"data": tpEmptyDatas
		};
		newSeries[0] = msrpEmptyObj
		newSeries[1] = tpEmptyObj;
		newSeries[2] = msrpObj;
		newSeries[3] = tpObj;
	} else {
		var priceObj = 
		{
			"type": "bar",
			"stack": "price",
//			"barWidth": 30,
			"barMaxWidth": 40,
			"barGap": 2,
			"itemStyle": {
				"normal": {
					"color": "rgba(199, 199, 199, 100)"
				}
			},
			"label": {
				"normal": {
					"show": true,
					"formatter": function(a) {
						return emptyDatas[a.dataIndex] + a.value;
					},
					"position": "top",
					"textStyle": {
						"color": "black"
					}
				}
			},
			"data": subModelDatas
		};
		var emptyObj = 
		{
			"type": "bar",
			"stack": "price",
//			"barWidth": 30,
			"barMaxWidth": 40,
			"barGap": 2,
			"itemStyle": {
				"normal": {
					"barBorderColor": "rgba(0, 0, 0, 0)",
					"color": "rgba(0, 0, 0, 0)"
				},
				"emphasis": {
					"barBorderColor": "rgba(0, 0, 0, 0)",
					"color": "rgba(0, 0, 0, 0)"
				}
			},
			"label": {
				"normal": {
					"show": true,
					"position": "insideTop",
					"textStyle": {
						"color": "black"
					}
				}
			},
			"data": emptyDatas
		};
		newSeries[0] = emptyObj;
		newSeries[1] = priceObj;
	}
    return newSeries;
}

/**把字符串数组转换成数字数组**/
function changeToNumber(array) {
	var newArray = new Array(array.length);
	for(var i = 0; i < array.length; i++) {
		newArray[i] = parseInt(array[i]);
	}
	return newArray;
}

/**将数字改为以千为单位**/
function turnToThousand(array) {
	var newArray = new Array(array.length);
	for(var i = 0; i < array.length; i++) {
		if(array[i] == 0){
			newArray[i] = 0;
		} else{
			newArray[i] = (array[i] / 1000).toFixed(1);
		}
	}
	return newArray;
}

/**数字转千分位显示**/
function changeToThousand(number) {
    var n = parseFloat(number).toFixed(0);
    var re = /(\d{1,3})(?=(\d{3})+)/g;
    return n.replace(re, "$1,");
}

/**初始化拖动事件代码**/
function refreshSortTable() {
    $(".sortTable").sortable({
        cursor: "move",
        items: "li",   
        placeholder: "ex",
        revert: false,        
        update: function(event, ui) {
        	updateStyle();
        }
    });
}

/**更新排序后的样式**/
function updateStyle() {
    var li = $(".position li");
    $(li).each(function() {
		$(this).removeClass("last");
	});
    var lastObj = $(li).last();
    lastObj.addClass("last");
    positionStyleReset();
}

/**增加或删除按钮点击事件**/
$(".order .edit").live("click", function() {
	var position = $(this).parents(".position");
	var $pick = position.find(".pick");
	var $order = position.find(".order");
	$order.fadeOut(0);
	$pick.fadeIn(0);
	$order.find("li").each(function() {
		$pick.find("input[value='"+ $(this).attr('rel') +"']").parents("li").appendTo($pick.find("ul"));
	});
});

/**确认按钮点击事件**/
$(".pick .confirm").live("click", function() {
	var position = $(this).parents(".position");
	var $pick = position.find(".pick");
	var $order = position.find(".order");
	$order.fadeIn(0);
	$pick.fadeOut(0);
	$order.find("li").attr("on", "false");
	$pick.find("input[type='checkbox']").each(function() {
		var obj = $(this);
		if(obj.prop("checked")) {
			$order.find("li[rel='"+ obj.val() +"']").attr("on", "true");
		}
	})
	positionStyleReset();
});

/**面板 标签顺序 头,尾标签样式**/
function positionStyleReset() {
	$(".position").each(function() {
		$(this).find(".order li").fadeOut(0);
		var li = $(this).find(".order li[on='true']");
		li.fadeIn(0);
		li.removeClass().children("a").css("display", "block");
		li.first().children(".turnLeft").css("display", "none");
		li.last().addClass("last").children(".turnRight").css("display", "none");
	});
}

/**刷新图表**/
function refreshChart() {
	if(selectData != null) {
		var params = getParams2(); 
	    if(paramsValidate(params)) {
	    	return;
	    }
	    params.refreshType = "true";//刷新图表
	    $("#setDivName").css("display","none");
	    $("#chartTitleDivNoData").hide();
		$(".showConditionContainer .buttons .toggle a").click();
	    $("body").showLoading(); 
	    //默认展示图表
	    $("#chartTitleDiv").height(500);
	    //发送请求 
	    $.ajax({ type: "POST",
	             url: ctx + "/price/subModelPriceAnalysis/getAnalysisData", 
	             data: params,
	             success: function(data) {
	  			     if(data) {
	  			    $(".queryConditionContainer .buttons .toggle a").click();
	  			  	$("#setDivName").css("display","block");//标题显示
	  			  	if($("#chartType").val() == 2){
	  			  	$("#setDivName font").html("Price Range");
	  			  	}
					     $("#chartTitleDivNoData").hide();
					     $("#tSortableNoData").hide();
					     $("#chartTitleDiv").show();
					     $("#tSortable").show();
			        	 selectData = data;
			        	 showAllSubModel(data);
			        	 if($("#chartType").val() == "1") {
			        		 showScatterChart(data);
			        	 } else {
			        		 showBarChart(data);
			        	 }
	  				 } else {
	  					 $("#chartTitleDivNoData").show();
	  					 $("#tSortableNoData").show();
	  					 $("#chartTitleDiv").hide();
	  					 $("#tSortable").hide();
	  				 }
	  				 $("body").hideLoading();
	  			 },
	  			 error:function() {
	  				 $("#chartTitleDivNoData").show();
	  				 $("#tSortableNoData").show();
	  				 $("#chartTitleDiv").hide();
	  				 $("#tSortable").hide();
	  				 $("body").hideLoading();
	  			 }
	  	});
		/*if($("#chartType").val() == "1") {
			showScatterChart(selectData);
		} else {
			showBarChart(selectData);
		}*/
	} else {
		alert("请先查询数据！");
	}
}

/**
 * 获取页面请求参数
 */
window.getParams = function() {
	var beginDate = $("#startDate").val().replace("-", "");
	var subModelIds = $("#subModelModalResultContainer input[name='selectedSubModel']").map(function() {return $(this).val();}).get().join(",");
	var bodyTypeIds = $("#bodyTypeModalResultContainer li div").map(function() {return $.trim($(this).attr("value"));}).get().join(",");
	var showType = $(".showType:checked").val();
	var chartType = $("#chartType").val();
	var priceType = $("#priceType").val();
	var sortLabel = $(".sortLabel:checked").map(function() {return $(this).val();}).get().join(",");
	var paramObj = {};
	paramObj.chartType = chartType;
	paramObj.priceType = priceType;
	paramObj.beginDate = beginDate;
	paramObj.subModelIds = subModelIds;
	paramObj.bodyTypeIds = bodyTypeIds;
	paramObj.showType = showType;
	paramObj.sortLabel = sortLabel;
	paramObj.inputType = "1";
	getQueryConditionAndBrowser(paramObj);
	return paramObj;
}

/**
 * 获取页面请求参数(刷新图表时的车型参数)
 */
window.getParams2 = function() {
	var beginDate = $("#startDate").val().replace("-", "");
	var subModelIds = "";
	$(".tab-content #orderSort ul").find("li").each(function(){
		var ths = $(this);
		if($(ths).css("display") != "none"){
			subModelIds += $(ths).attr("rel")+",";
		}
	})

	subModelIds = subModelIds.substr(0,subModelIds.length-1);
	var bodyTypeIds = $("#bodyTypeModalResultContainer li div").map(function() {return $.trim($(this).attr("value"));}).get().join(",");
	var showType = $(".showType:checked").val();
	var chartType = $("#chartType").val();
	var priceType = $("#priceType").val();
	var sortLabel = $(".sortLabel:checked").map(function() {return $(this).val();}).get().join(",");
	var paramObj = {};
	paramObj.chartType = chartType;
	paramObj.priceType = priceType;
	paramObj.beginDate = beginDate;
	paramObj.subModelIds = subModelIds;
	paramObj.bodyTypeIds = bodyTypeIds;
	paramObj.showType = showType;
	paramObj.sortLabel = sortLabel;
	paramObj.inputType = "1";
	getQueryConditionAndBrowser(paramObj);
	return paramObj;
}

/**
 * 获取页头查询条件，以及浏览器
 */
function getQueryConditionAndBrowser(paramObj) {
	paramObj.browser = navigator.appVersion;
	var queryCondition = "";
	queryCondition += "\n价格类型 = ";
	if("1" == $("#priceType").val()) {
		queryCondition += "指导价";
	} else if("2" == $("#priceType").val()) {
		queryCondition += "成交价";
	} else {
		queryCondition += "指导价&成交价";
	}
	queryCondition += "\n时间 = " + $("#startDate").val() + "至" + $("#startDate").val();
	queryCondition += "\n车型 = " + $(".subModelModalResultContainer li div").map(function() {return $.trim($(this).text());}).get().join(",");
	paramObj.queryCondition = queryCondition;
}

/**
 * 车型弹出框
 * 
 * @param type
 */
window.showSubModel = function (type) {
	if(type == 1) {
		$("#choseType").attr("value", "1")// 本竞品
	} else if(type == 2) {
		$("#choseType").attr("value", "2")// 细分市场
	} else if(type == 3) {
		$("#choseType").attr("value", "3")// 品牌
	} else {
		$("#choseType").attr("value", "4")// 厂商
	}

	/* 根据选择条件隐藏 */
	var pooAttributeIdArr = [];
	$(".subModelModalContainer").find(".pooAttributeIdInput").each(function() {
		if($(this).attr("checked")) {
			pooAttributeIdArr[pooAttributeIdArr.length] = $(this).val();
		}
	});

	$(".subModelModalContainer").find(".subModelIdInput").each(function() {
		var flag = false;
		for(var i = 0; i < pooAttributeIdArr.length; i++) {
			if(pooAttributeIdArr[i] == $(this).attr("pooAttributeId")) {
				flag = true;
				break;
			}
		}
		if(flag) {
			$(this).parent().show();
		} else {
			$(this).parent().hide();
		}
	});
	/* 根据选择条件隐藏结束 */
	if("1" == type) {
		getTerminalModelPage("tabs-competingProducts", type, "1", "2");
	} else if("2" == type) {
		getTerminalModelPage("tabs-segment", type, "1", "2");
	} else if("3" == type) {
		getTerminalModelPage("tabs-brand", type, "1", "2");
	} else {
		getTerminalModelPage("tabs-manf", type, "1", "2");
	}
};

/**
 * 展示页面
 * 
 * @param id 展示容器ID
 * @param type 子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
 * @param inputType 控件类型1：复选，2：单选;默认为1
 * @param timeType 时间类型1：时间点;2：时间段默认为2
 * 
 */
function getTerminalModelPage(id, type, inputType, timeType) {
	// 如果内容不为空则触发请求
	if(!$.trim($("#" + id).html())) {
		//获取时间
		var beginDate = $("#startDate").val();
		//获取车身形式
		var bodyTypeIds = $(".bodyTypeModalResultContainer li div").map(function() {return $.trim($(this).attr("value"));}).get().join(",");
		var priceType = $("#priceType").val();
		// 传递参数
		var params = {
			inputType : inputType,
			subModelShowType : type,
			beginDate : beginDate,
			bodyTypeIds : bodyTypeIds,
			priceType : priceType
		};
		// 触发请求
		showLoading(id);
		$("#" + id).load(ctx + "/price/subModelPriceAnalysis/getSubModelModal", params, function() {
			$("#selectorResultContainerBySubModel .removeBtnByResult").each(function() {
				var subModelId = $(this).attr("subModelId");
				$(".subModelModalContainer .subModelIdInput").each(function() {
					if($(this).val() == subModelId) {
						$(this).attr("checked", "true");// 行全选
					}
				});
			});
			checkAll(type);
			/* 根据选择条件隐藏 */
			var pooAttributeIdArr = [];
			$(".subModelModalContainer").find(".pooAttributeIdInput").each(function() {
				if($(this).attr("checked")) {
					pooAttributeIdArr[pooAttributeIdArr.length] = $(this).val();
				}
			});

			$(".subModelModalContainer").find(".subModelIdInput").each(function() {
				var flag = false;
				for(var i = 0; i < pooAttributeIdArr.length; i++) {
					if(pooAttributeIdArr[i] == $(this).attr("pooAttributeId")) {
						flag = true;
						break;
					}
				}
				if(flag) {
					$(this).parent().show();
				} else {
					$(this).parent().hide();
				}
			});
		/* 根据选择条件隐藏结束 */
		});
    //当第二次进入页面时,根据已选车型联动当前TAB下的车型选择框
	} else {
		var tabName = "#tabs-";
		if(type == "1") {
			tabName += "competingProducts";
		} else if(type == "2") {
			tabName += "segment";
		} else if(type == "3") {
			tabName += "brand";
		} else {
			tabName += "manf";
		}
		$("#selectorResultContainerBySubModel .removeBtnByResult").each(function() {
			var subModelId = $(this).attr("subModelId");
			$(tabName + " .subModelIdInput").each(function() {
				if($(this).val() == subModelId) {
					$(this).attr("checked", "true");// 行全选
				}
			});
		});
		checkAll(type);
	}
};

/**把有全选的选项选上**/
function checkAll(type)
{
	if("2" == type){
		var allObj = $("#tabs-segment .selectorTypeTd");
		$(allObj).find(".selectSegmentAll").each(function(){
			var flag = true;
			var modelObj = $(this);
			$(modelObj).closest("td").next().find(".subModelIdInput").each(function(){
				if(!$(this).prop("checked")){
					flag = false;
				}
			});
			//如果一个细分市场下的车型被全选，则细分市场也选上
			if(flag == true){
				modelObj.prop("checked", true);
			}
		});
	} else if("3" == type){
		var allObj = $("#tabs-brand .selectorTypeTd");
		$(allObj).find(".selectBrandAll").each(function(){
			var flag = true;
			var modelObj = $(this);
			$(modelObj).closest("td").next().find(".subModelIdInput").each(function(){
				if(!$(this).prop("checked")){
					flag = false;
				}
			});
			//如果一个品牌下的车型被全选，则品牌也选上
			if(flag == true){
				modelObj.prop("checked", true);
			}
		});
	} else if("4" == type){
		var allObj = $("#tabs-manf .selectorTypeTd");
		$(allObj).find(".selectManfAll").each(function(){
			var flag = true;
			var modelObj = $(this);
			$(modelObj).closest("td").next().find(".subModelIdInput").each(function(){
				if(!$(this).prop("checked")){
					flag = false;
				}
			});
			//如果一个厂商下的车型被全选，则厂商也选上
			if(flag == true){
				modelObj.prop("checked", true);
			}
		});
	} 
}

/**校验是否已选择对象**/
function paramsValidate(params) {
	var beginDate = params.beginDate;
	var subModelIds = params.subModelIds;
	var flag = false;
	if("" == subModelIds) {
		alert("请选择车型");
		flag = true;
	}
	return flag;
};

/**
 * 导出Excel
 * 
 * @param languageType EN:英文版;ZH:中文版
 */
function exportExcel(languageType) {
	if(selectData == null) {
		alert("暂无数据导出");
		return false;
	}
	var paramObj = getParams();
	$("#ex_beginDate").val(paramObj.beginDate);
	$("#ex_subModelIds").val(paramObj.subModelIds);
	$("#ex_priceType").val(paramObj.priceType);
	$("#ex_chartType").val(paramObj.chartType);
	$("#ex_showType").val(paramObj.showType);
	$("#ex_sortLabel").val(paramObj.sortLabel);
	$("#languageType").val(languageType);
	$("#exportFormId").submit();
};

/**显示需要展示的车型**/
function showAllSubModel(data) {
	var position = $("#sortSubModel");
	var series = data.series;
	var subModelOrderName = data.xTitleEns;
	var subModelOrderId = data.xTitleIds;//车型id
		//subModelOrderName = new Array(data.listOrder.length);
		//subModelOrderId = new Array(data.listOrder.length);
		var html = "";
		var $pick = position.find(".pick");
		var $order = position.find(".order");
		//创建可以勾选车型的列表
		for(var i = 0; i < subModelOrderId.length; i++) {
			//var daTa = data.listOrder[i];
			html += '<li><label><input type="checkbox" value="' + subModelOrderId[i] + '" checked="checked">' + subModelOrderName[i] + '</label></li>';
		}
		//$pick.find("ul").html("");
		$pick.find("ul").html(html);
		html = "";
		//创建可以拖动车型的列表
		for(var i = 0; i < subModelOrderId.length; i++) {
			//var daTa = data.listOrder[i];
			html += '<li on="true" class rel="' + subModelOrderId[i] + '" style="display: list-item;" ><input type="hidden" value="' + subModelOrderName[i] + '" >';
			html += '<span>' + subModelOrderName[i] + '</span></li>';
			//subModelOrderName[i] = daTa.objNameEn;
			//subModelOrderId[i] = daTa.objId;
		}
		//$order.find("ul").html("");
		$order.find("ul").html(html);
	
	refreshSortTable();
	updateStyle();
}

/**初始化拖动事件代码**/
function refreshSortTable() {
    $(".sortTable").sortable({
        cursor: "move",
        items: "li",   
        placeholder: "ex",
        revert: false,        
        update: function(event, ui) {
        	updateStyle();
        }
    });
}

/**更新排序后的样式**/
function updateStyle() {
    var li = $(".position li");
    $(li).each(function() {
		$(this).removeClass("last");
	});
    var lastObj = $(li).last();
    lastObj.addClass("last");
    positionStyleReset();
}

/**确认按钮点击事件**/
$(".pick .confirm").live("click", function() {
	var position = $(this).parents(".position");
	var $pick = position.find(".pick");
	var $order = position.find(".order");
	$order.fadeIn(0);
	$pick.fadeOut(0);
	$order.find("li").attr("on", "false");
	$pick.find("input[type='checkbox']").each(function() {
		var obj = $(this);
		if(obj.prop("checked")) {
			$order.find("li[rel='"+ obj.val() +"']").attr("on", "true");
		}
	})
	positionStyleReset();
});

/**面板 标签顺序 头,尾标签样式**/
function positionStyleReset() {
	$(".position").each(function() {
		$(this).find(".order li").fadeOut(0);
		var li = $(this).find(".order li[on='true']");
		li.fadeIn(0);
		li.removeClass().children("a").css("display", "block");
		li.first().children(".turnLeft").css("display", "none");
		li.last().addClass("last").children(".turnRight").css("display", "none");
	});
}

/**修改车型显示名称**/
function changeSubModelName(list) {
	var subModelName = list[0].subModelName;
	var count = 0;
	for(var i = 0; i < list.length; i++) {
		count += parseInt(list[i].addSale);
	}
	subModelName += "(" + count + ")";
	return subModelName;
}

