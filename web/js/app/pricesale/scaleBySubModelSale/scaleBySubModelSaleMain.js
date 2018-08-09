/**定义子车型LI全局变量*/
var currSubModelLI = null;
/**定义车身形式LI全局变量*/
var currBodyTypeLI = null;
var bodyType = null;
var dateControl = "none";
var inputType = "1";
var selectData = null;
var defaultBeginDate = endDate;
var subModelOrderId = null;
var subModelOrderName = null;
var orderTitles = null;
var exportSeries = null;
/**获取时间模板*/
function getDateTp(type) {
	if("add" == type) {
		endDate = getPreviousMonth(endDate);
	}
	var htmlStr = "";	
	htmlStr += '<li style="list-style: none;margin-bottom:10px;" class="dateLIContainer">';
    htmlStr += '	<div class="form-inline">';
    htmlStr += '  		<span class="startDate-container input-append date">';
	htmlStr += '	  		<input type="text" value="' + endDate + '"  readonly="readonly" class="input-mini white" placeholder="开始日期" />';
	htmlStr += '	  		<span class="add-on"><i class="icon-th"></i></span>';
	htmlStr += '		</span>';
	htmlStr += '		<span class="2">至</span>';
    htmlStr += '  		<span class="endDate-container input-append date">';
	htmlStr += '	  		<input type="text" value="' + endDate + '" readonly="readonly" class="input-mini white"  placeholder="结束日期" />';
	htmlStr += '	  		<span class="add-on"><i class="icon-th"></i></span>';
	htmlStr += '		</span>';
	htmlStr += '		<span style="margin-left:20px;display:' + dateControl + ';" class="dateControl">';
	htmlStr += '			<i class="icon-plus addDateBtn" style="cursor:pointer;" title="增加"></i>';
	htmlStr += '			<i class="icon-minus removeDateBtn" style="margin-left: 20px;cursor:pointer;" title="删除"></i>';
	htmlStr += '		</span>';
	htmlStr += '	</div>';
 	htmlStr += '</li>';
 	return htmlStr;
}

/**获取车身形式和车型模板*/
//function getSubModelTp() {
//	var htmlStr = "";
//	htmlStr += '<li style="list-style: none;margin-bottom:10px;" class="subModelLIContainer">';
//	htmlStr += '	<table style="width:530px;">';
//	htmlStr += '		<tr>';
//	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:8%">';
//	htmlStr += '				<div>';
//	htmlStr += '					<a href="#" role="button" class="btn bodyTypeSelector" data-toggle="modal">车身形式</a>';
// 	htmlStr += '				</div>';
//	htmlStr += '			</td>';
//	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:20%">';
//	htmlStr += '				<div  style="margin-left:0px" class="bodyTypeModalResultContainer">';
//	htmlStr += '				</div>';
//	htmlStr += '			</td>';
//	htmlStr += '			<td valign="top" style="width:20%">	';
//	htmlStr += '				<div>';
//	htmlStr += '					<a href="#" role="button" class="btn subModelSelector" data-toggle="modal">选择车型</a>';
// 	htmlStr += '				</div>';
// 	htmlStr += '			</td>';
//	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:52%">';
//	htmlStr += '				<div style="margin-left:0px" class="subModelModalResultContainer">';
//	htmlStr += '				</div>';
//	htmlStr += '			</td>';
//	htmlStr += '		</tr>';
//	htmlStr += '	</table>';
//	htmlStr += '</li>';
//	return htmlStr;
//};
/** 时间控件初始化*/
function initDate() {
	$(".startDate-container.input-append.date").datetimepicker({
		format: "yyyy-mm",
        language: "zh-CN",        
        todayBtn: 0,
		autoclose: 1,				
		startView: 3,		
		maxView: 3,
		minView: 3,
		startDate: beginDate,
		endDate: defaultBeginDate,
        showMeridian: 1
    });
    $(".endDate-container.input-append.date").datetimepicker({
		format: "yyyy-mm",
        language: "zh-CN",        
        todayBtn: 0,
		autoclose: 1,				
		startView: 3,		
		maxView: 3,
		minView: 3,
		startDate: beginDate,
		endDate: defaultBeginDate,
        showMeridian: 1
    });
}

//日期-增加按扭
$(".addDateBtn").live("click", function() {
	//将拆分勾选去除
	$("#objectDiv").find("input[name='obj_Split']").removeAttr("checked");
	if(6 <= $("#dateYear .dateLIContainer").length) {
		alert("最多只能添加6个时间段");
		return;
	}
	var oUl = $(this).parents(".dateULContainer");
	oUl.append(getDateTp("add"));
	//重新初始化日期控制
	initDate();
});
$(".addDateBtn").click();

//日期-删除按扭
$(".removeDateBtn").live("click", function() {
	var oCurrLi = $(this).parents(".dateLIContainer");
	var liList = $(this).parents(".dateULContainer").find(".dateLIContainer");
	if(liList.length == 1) {
		alert("不能删除，至少保留一行！");
		return;
	}
	$(oCurrLi).remove();
});

/**初始化时，加载对象模板*/
function initPage() {
	$(".dateULContainer").each(function() { 
		$(this).append(getDateTp());
	});
//	$(".subModelULContainer").each(function() { 
//		$(this).append(getSubModelTp());
//	});
	initDate();
};

/**获取上月**/
function getPreviousMonth(endDate) {
	var year = parseInt(endDate.substring(0, 4));
	var month = endDate.substring(5, 7);
	//0开头的月份做特殊处理,算出上一个月
	if(month.substring(0, 1) == "0") {
		month = parseInt(month.substring(1, 2)) - 1;
	} else {
		month = parseInt(month) - 1;
	}
	//如果本月是1,上个月取上一年12月
	if(month == 0) {
		month = 12;
		year = year - 1;
	}
	//如果小于10的月份,则在前面加0
	if(month < 10) {
		month = "0" + month;
	}
	return (year + "-" + month);
}

$(document).ready(function() { 
	//初始化页面
	initPage();
    //默认选择车型对比
    $("#analysisDimensionType").val("1");
    refreshSortTable();
    positionStyleReset();
//	/** 弹出车身形式对话框*/
//	$(".bodyTypeSelector").live("click", function() {
//		currBodyTypeLI = $(this).parents("table").find(".bodyTypeModalResultContainer");
//		$("#bodyTypeModal").modal("show");
//	});
});	

/**展示车型弹出框-开始*/
/** 展示车型弹出框*/
$("#subModelModal").on("show", function (e) {
	if(e.relatedTarget) {
		return; //修复bootstrap的modal引入tabpane时，触发事件问题。
	}
	//加载子车型数据
	showLoading("subModelModalBody");
	$("#subModelModalBody").load(ctx + "/pricesale/scaleBySubModelSale/getSubModelModal", getSubModelParams(bodyType), function() {
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
	$("#bodyTypeModalBody").load(ctx + "/pricesale/scaleBySubModelSale/getBodyType", getParams(), function() {
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
//		$("#tabs-segment").find(".subModelIdInput:checked").each(function() {
//			$(this).removeAttr("checked");
//		});
//		$("#tabs-brand").find(".subModelIdInput:checked").each(function() {
//			$(this).removeAttr("checked");
//		});
//		$("#tabs-manf").find(".subModelIdInput:checked").each(function() {
//			$(this).removeAttr("checked");
//		});
	
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
	
/**点击确定查询*/
$("#queryBtn").on("click", function (e) {
	var params = getParams();
	if(paramsValidate(params)) {
		return;
	}
	$("body").showLoading();
	$(".showConditionContainer .buttons .toggle a").click();
	$("#setDivName").css("display","none");//隐藏标题
//		//默认展示图表
	$("#chartTitleDiv").height(500);
	$("#chartTitleDivNoData").css("display","none");
	//发送请求
	$.ajax({
        type: "POST",
		url: ctx + "/pricesale/scaleBySubModelSale/getAnalysisData",
		data: getParams(),
		success: function(data) {
				     // 先清空图表所在的div (兼容IE8浏览器)
					 var chart = $("#chartTitleDiv");
					 chart.html("");
			         if(data) {
			        	 subModelOrderId = null;
			        	 subModelOrderName = null;
			        	 orderTitles = null;
			        	 selectData = data;
			             //查询面板折叠
			        	 $(".queryConditionContainer .buttons .toggle a").click();
			        	 $("#noData").css("display", "none");
			        	 $("#exportButtons").css("display", "block");
			        	 $("#showChart").click();
			        	$(".queryConditionContainer .buttons .toggle a").click();
			        	$("#setDivName").css("display","block");//显示标题
			        	 showChart(data);
			         } else {
			        	 $("#chartTitleDivNoData").css("display","block");
			        	 selectData = null;
			         }
			         $("body").hideLoading();
		   		  },
		error:function() {
		          $("body").hideLoading();
		      }
	});
});
	
/** 重置按钮**/
$("#resetBtn").on("click", function (e) {
	$("#formId :reset");	
	$("#productConfig").show();
	window.location.reload();
});

/** 车型控件值鼠标经过事件*/
$(".removeBtn").live("mouseover", function() {
	$(this).find(".icon-remove").css({visibility:"visible"});
});

/**车型控件值鼠标离开事件*/
$(".removeBtn").live("mouseout", function() {
	$(this).find(".icon-remove").css({visibility:"hidden"});
});

/**获取要显示的数据**/
function getData(series, showType, labelSum, chartType, bubbleType) {
	var newSeries = new Array();
	var size = 20;
	var sizeScale = 1;
	var position = 0;
	var positionFlag = 0;
	var borderColor = "rgb(174, 212, 248)";
	var isShow = 0;
	var splitType = $(".splitType:checked").val();
	var split = "";
	var labels = new Array();
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
	//型号显示数量  1为6个,2为全部
	if(labelSum == 2) {
		isShow = -1;
	}
	//分隔符
	if(splitType == "1") {
		split = ",";
	} else if(splitType == "2") {
		split = ";";
	} else {
		split = " ";
	}
	//把要展示的标签放在一个数组里
	$("#sortLabel .order li").each(function() {
		var obj = $(this);
		if(obj.attr("on") == "true") {
			labels.push($(this).find("input").val());
		}
	});
	var colors = "rgb(174, 212, 248)";
	
	for(var i = 0; i < series.length; i++) {
		
	    var data = series[i];
	    var newData = new Array(data.list.length);
	    //如果是气泡图,算出各数据范围的图形数值比例
	    sizeScale = getSizeScale(chartType, bubbleType, data.list);
	    if(i%2 == 0){
	    	colors = "rgb(174, 212, 248)";
	    } else{
	    	colors = "rgb(255,155,89)";
	    }
	    if($("#analysisDimensionType").val() == 2){
	    	colors = "rgb(174, 212, 248)";
	    }
	    for(var k = 0; k < data.list.length; k++) {
	      	var versionObj = data.list[k];
	      	if(versionObj.transMission == "MT") {
	       		position = 0 + positionFlag;
	       	} else {
	       		position = 0 - positionFlag;
	       	}
	       	var labelName = changeLabelName(split, labels, versionObj);
	       	size = getSizeValue(bubbleType, versionObj);
	       	var obj = 
                 {
 		   		"value": [
	                   data.subModelName,
	                   versionObj.price
	                   ],
                "symbolSize": size * sizeScale + 10,
                "symbolOffset": [position, 0],
                "label": {
                	"normal": {
                		"show": versionObj.isShow > isShow,
                		"formatter": labelName,
                		"position": "right",
                		"textStyle": {
                			"color": "black"
                		}
                	}
                },
                "itemStyle": {
                	"normal": {
                		"color": colors,
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
	    newSeries[i] = 
	        {
	            "type": "scatter",
	            "data": newData
		    }
	}
    return newSeries;
}

/**展示图表**/
function showChart(data) {
	var chart = $("#chartTitleDiv");
	chart.html("");
	var analysisDimensionType = $("#analysisDimensionType").val();
	var series = data.series;
	var xTitles = data.xTitles;
	if(analysisDimensionType == "1") {
		series = refreshSubModel(series);
		xTitles = rereshTitles(series, xTitles);
		showAllSubModel(series);
	} else {
		exportSeries = series;
	}
	var myChart = echarts.init(document.getElementById("chartTitleDiv"));
	var showType = $(".showType:checked").val();
	var labelSum = $(".labelSum:checked").val();
	var chartType = $(".chartType:checked").val();
	var bubbleType = $(".bubbleType:checked").val();
	var priceType = $("#priceType").val();
	var yName = "TP(RMB)";
	if(priceType == "2") {
		yName = "MSRP(RMB)";
	}
	var yAxis = data.yAxis;
	var allData = getData(series, showType, labelSum, chartType, bubbleType);
	option = {
	    "grid": { 
	    	"top": 40,
	    	"bottom": 40,
	    	"left": 60,
	    	"right": 80
	    },
	    "xAxis": [
	        {
	            "type": "category",
	            "splitLine": {
	                "show": false
	            },
	            "data": xTitles,
	            "axisTick": {
	                "show": false
	            },
	            "axisLabel": {
	                "interval": 0
	            },
	            "splitLine": {
	                "show": false,//网格线显示
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
	            "name": yName,
	            "nameGap": 18,
	            "nameTextStyle": {
	                "color": "#000",
	                "fontFamily": "微软雅黑",
	                "position": "left"
	            },
	            "axisLabel": {
	                "margin": 10
	            },
	            "splitLine": {
	                "show": false,//网格线显示
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
//	console.log(JSON.stringify(option));
	myChart.setOption(option);
}

/**获取需要展示的车型**/
function refreshSubModel(series) {
	if(subModelOrderId != null) {
		var newSeries = new Array();
		var count = 0;
		subModelOrderId = new Array();
		subModelOrderName = new Array();
		$("#sortSubModel .order li").each(function() {
			var obj = $(this);
			//把选中要展示的车型放到全局变量中
			if(obj.attr("on") == "true") {
				subModelOrderName.push(obj.find("span").html());
				subModelOrderId.push($(this).find("input").val());
			}
		});
		for(var k = 0; k < subModelOrderId.length; k++) {
			for(var i = 0; i < series.length; i++) {
				var data = series[i];
				if(data.subModelId == subModelOrderId[k]) {
					newSeries[count] = data;
					count++;
				}
			}
		}
		//保存需要导出的数据
		exportSeries = newSeries;
		return newSeries;
	} else {
		//保存需要导出的数据
		exportSeries = series;
		return series;
	}
}

/**获取需要展示的车型名称**/
function rereshTitles(series, xTitles) {
	var newXTitles = new Array(series.length);
	if(orderTitles != null) {
		for(var i = 0; i < series.length; i++) {
			for(var k = 0; k < orderTitles.length; k++) {
				if(series[i].subModelName == orderTitles[k].split("(")[0]) {
					newXTitles[i] = orderTitles[k];
				}
			}
		}
	} else {
		orderTitles = xTitles;
		newXTitles = xTitles;
	}
	return newXTitles;
}

/**显示需要展示的车型**/
function showAllSubModel(series) {
	var position = $("#sortSubModel");
	if(subModelOrderName == null) {
		subModelOrderName = new Array(series.length);
		subModelOrderId = new Array(series.length);
		var html = "";
		var $pick = position.find(".pick");
		var $order = position.find(".order");
		//创建可以勾选车型的列表
		for(var i = 0; i < series.length; i++) {
			var data = series[i];
			html += '<li><label><input type="checkbox" value="' + data.subModelId + '" checked="checked">' + data.subModelName + '</label></li>';
		}
		$pick.find("ul").html(html);
		html = "";
		//创建可以拖动车型的列表
		for(var i = 0; i < series.length; i++) {
			var data = series[i];
			html += '<li on="true" class rel="' + data.subModelId + '" style="display: list-item;" ><input type="hidden" value="' + data.subModelId + '" >';
			html += '<span>' + data.subModelName + '</span></li>';
			subModelOrderName[i] = data.subModelName;
			subModelOrderId[i] = data.subModelId;
		}
		$order.find("ul").html(html);
	}
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

/**根据气泡大小选择改变标签显示内容**/
$("input[name='bubbleType']").live("click", function() {
	var value = $(this).val();
	if(value == "1") {
		$("#sortLabel .pick input[name = 'sortLabel'][value = 4]").removeAttr("disabled");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 4]").attr("checked", "true");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 5]").removeAttr("checked");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 5]").attr("disabled", "disabled");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 6]").removeAttr("disabled"); 
		$("#sortLabel .pick input[name = 'sortLabel'][value = 6]").removeAttr("checked");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 7]").removeAttr("checked");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 7]").attr("disabled", "disabled");
	} else if(value == "2") {
		$("#sortLabel .pick input[name = 'sortLabel'][value = 4]").removeAttr("checked");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 4]").attr("disabled", "disabled");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 5]").removeAttr("disabled");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 5]").attr("checked", "true");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 6]").removeAttr("checked");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 6]").attr("disabled", "disabled");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 7]").removeAttr("disabled"); 
		$("#sortLabel .pick input[name = 'sortLabel'][value = 7]").removeAttr("checked");
	} else if(value == "3") {
		$("#sortLabel .pick input[name = 'sortLabel'][value = 4]").removeAttr("disabled"); 
		$("#sortLabel .pick input[name = 'sortLabel'][value = 4]").removeAttr("checked");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 5]").removeAttr("checked");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 5]").attr("disabled", "disabled");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 6]").removeAttr("disabled");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 6]").attr("checked", "true");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 7]").removeAttr("checked");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 7]").attr("disabled", "disabled");
    } else {
    	$("#sortLabel .pick input[name = 'sortLabel'][value = 4]").removeAttr("checked");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 4]").attr("disabled", "disabled");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 5]").removeAttr("disabled");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 5]").removeAttr("checked");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 6]").removeAttr("checked");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 6]").attr("disabled", "disabled");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 7]").removeAttr("disabled");
		$("#sortLabel .pick input[name = 'sortLabel'][value = 7]").attr("checked", "true");
	}
	$("#sortLabel .pick .confirm").click();
});

/**修改气泡数值比例**/
function getSizeScale(chartType, bubbleType, list) {
	var sizeScale = 0;
	if(chartType == 3) {
		return sizeScale;
	} else {
		var addMixMax = 0;
		var addSaleMax = 0;
		var avgMixMax = 0;
		var avgSaleMax = 0;
		//求出每个类型数值的最大值
		for(var i = 0; i < list.length; i++) {
			var obj = list[i];
			if(parseFloat(obj.addMix) > addMixMax) {
				addMixMax = parseFloat(obj.addMix);
			}
			if(parseFloat(obj.avgMix) > avgMixMax) {
				avgMixMax = parseFloat(obj.avgMix);
			}
			if(parseInt(obj.addSale) > addSaleMax) {
				addSaleMax = parseInt(obj.addSale);
			}
			if(parseInt(obj.avgSale) > avgSaleMax) {
				avgSaleMax = parseInt(obj.avgSale);
			}
		}
		if(bubbleType == 1) {
			if(addMixMax > 0.5) {
				//sizeScale = 80;
				sizeScale = 1;
			} else {
				//sizeScale = 160;
				sizeScale = 2;
			}
		} else if(bubbleType == 2) {
			if(avgMixMax > 0.5) {
				//sizeScale = 80;
				sizeScale = 1;
			} else {
				//sizeScale = 160;
				sizeScale = 2;
			}
		} else if(bubbleType == 3) {
			if(addSaleMax > 50000) {
				//sizeScale = 0.0003;
				sizeScale = 0.000075;
			} else if(addSaleMax > 10000 && addSaleMax < 50000) {
				//sizeScale = 0.0016;
				sizeScale = 0.0004;
			} else if(addSaleMax > 5000 && addSaleMax < 10000) {
				//sizeScale = 0.008;
				sizeScale = 0.002;
			} else if(addSaleMax > 1000 && addSaleMax < 5000) {
				//sizeScale = 0.016;
				sizeScale = 0.004;
			} else {
				//sizeScale = 0.08;
				sizeScale = 0.02;
			}
		} else {
			if(avgSaleMax > 50000) {
				//sizeScale = 0.0003;
				sizeScale = 0.000075;
			} else if(avgSaleMax > 10000 && avgSaleMax < 50000) {
				//sizeScale = 0.0016;
				sizeScale = 0.0004;
			} else if(avgSaleMax > 5000 && avgSaleMax < 10000) {
				//sizeScale = 0.008;
				sizeScale = 0.002;
			} else if(avgSaleMax > 1000 && avgSaleMax < 5000) {
				//sizeScale = 0.016;
				sizeScale = 0.004;
			} else {
				//sizeScale = 0.08;
				sizeScale = 0.02;
			}
		}
		return sizeScale;
	}
}

/**获取气泡数值**/
function getSizeValue(bubbleType, versionObj) {
	var bubbleSize = 0;
	if(bubbleType == 1) {
		bubbleSize = versionObj.addMix;
	} else if(bubbleType == 2) {
		bubbleSize = versionObj.avgMix;
	} else if(bubbleType == 3) {
		bubbleSize = versionObj.addSale;
	} else {
		bubbleSize = versionObj.avgSale;
	}
	return bubbleSize;
}

/**修改气泡显示数值**/
function changeLabelName(split, labels, versionObj) {
	var labelName = "";
	//获取标签显示内容数组
	for(var i = 0; i < labels.length; i++) {
		var label = labels[i];
		if(label == "1") {
			labelName += versionObj.versionShortNameEn;
		} else if(label == "2") {
			labelName += versionObj.bodyTypeNameEn;
		} else if(label == "3") {
			labelName += "[" + versionObj.price + "]";
		} else if(label == "4") {
			labelName += "[" + parseFloat(versionObj.addMix * 100, 1).toFixed(2) + "%]";
		} else if(label == "5") {
			labelName += "[" + parseFloat(versionObj.avgMix * 100, 1).toFixed(2) + "%]";
		} else if(label == "6") {
			labelName += "[" + versionObj.addSale + "]";
		} else {
			labelName += "[" + versionObj.avgSale + "]";
		}
		labelName += split;
	}
	return labelName.substring(0, labelName.length - 1);
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

/**刷新图表**/
function refreshChart() {
	if(selectData != null) {
		showChart(selectData);
	} else {
		alert("请先查询数据！");
	}
}

/**分析维度改变事件**/
$("#analysisDimensionType").on("change", function(e) {
	var obj = $(this);
    //时间维度时，时间组件后面的+号显示,车型维度则隐藏
	if(obj.val() == "2") {
		$(".dateControl").show();
		dateControl = "inline";
		inputType = "2";
		$(".subModelModalResultContainer").html("");
		$(".subModelOrderTd").hide();
		$(".subModelOrderTdHide").show();//占位置防止错位
	} else {
		$(".dateControl").hide();
		dateControl = "none";
		$(".subModelModalResultContainer").html("");
		$("#sortSubModel .sortTable").html("");
		$(".subModelOrderTd").show();
		$(".subModelOrderTdHide").hide();//占位置防止错位
		inputType = "1";
		var dateLiList = $(".dateULContainer").find(".dateLIContainer");
		if(dateLiList.length > 1) {
			for(var i = 1; i < dateLiList.length; i++) {
				$(dateLiList[i]).remove();
				endDate = getPreviousMonth(endDate);
			}
		}
	}
});

/** 价格信息下拉框改变事件*/
$(".oppositeType").on("click", function() {
	$("body").showLoading();
	//去后台查询成交价的时间范围
	$.ajax({
		   type: "POST",
		   url: ctx + "/pricesale/ScaleBySubModelSale/changeDate",
		   data: getParams(),
		   success: function(data) {
			   if(data) {
				   $.each(data, function(i, k) {
				       if("endDate" == i) {
				    	   defaultBeginDate = k;
				    	   endDate = k;
				    	   //先清除日期控件参数
				    	   $(".startDate-container.input-append.date").datetimepicker("remove");
				    	   $(".endDate-container.input-append.date").datetimepicker("remove");
				    	   //重新设置日期控件参数
				    	   initDate();
				    	   //赋值显示日期为最新日期
				    	   $("#dateYear .dateLIContainer").eq(0).find(".startDate-container input").val(k);
					       $("#dateYear .dateLIContainer").eq(0).find(".endDate-container input").val(k);
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

/** 参数验证**/
function paramsValidate(params) {
	getDateGroup(params);
	var dateGroup = (params.dateGroup).substr(0,params.dateGroup.length - 1).split("|");
	for(var i = 0; i < dateGroup.length; i++) {
		var date = dateGroup[i].split(",");
		var beginDate = parseInt(date[0].replace("-",""));
		var endDate = parseInt(date[1].replace("-",""));
		
		if(beginDate > endDate) {
			alert("第" + (i+1) + "条时间段，开始时间不能大于结束时间");
			return true;
		}
		if(dateGroup.length > 1) {
			//如果是多条时间段
			if((endDate - beginDate) >= 100) {
				alert("第" + (i+1) + "条时间段，时间不能跨年");
				return true;
			}
		}
	}
	var subModelIds = params.subModelIds;
	if(subModelIds == "" || subModelIds == undefined || subModelIds == null) {
		alert("请选择车型");
		return true;
	}
};

/** 获取页面参数**/
window.getParams = function() {
	var beginDate = $("#dateYear .dateLIContainer").eq(0).find(".startDate-container input").val().replace("-", "");
	var endDate = $("#dateYear .dateLIContainer").eq(0).find(".endDate-container input").val().replace("-", "");
	var subModelIds = $(".subModelModalResultContainer input[name='selectedSubModel']").map(function() {return $(this).val();}).get().join(",");
	var subModelNames = $(".subModelModalResultContainer .removeBtn").map(function() {return $(this).text();}).get().join(",");
	var priceType = $("#priceType").val();//价格类型	
	var analysisDimensionType = $("#analysisDimensionType").val();//分析维度
	var oppositeType = $(".oppositeType:checked").val();
	var showType = $(".showType:checked").val();
	var labelSum = $(".labelSum:checked").val();
	var bubbleType = $(".bubbleType:checked").val();
	var chartType = $(".chartType:checked").val();
	var sortLabel = $(".sortLabel:checked").map(function() {return $(this).val();}).get().join(",");
	var splitType = $(".splitType:checked").val();
	var paramObj = {};
	paramObj.subModelIds = subModelIds;
	paramObj.subModelNames = subModelNames;
	paramObj.priceType = priceType;
	paramObj.analysisDimensionType = analysisDimensionType;
	paramObj.oppositeType = oppositeType;
	paramObj.showType = showType;
	paramObj.labelSum = labelSum;
	paramObj.bubbleType = bubbleType;
	paramObj.chartType = chartType;
	paramObj.sortLabel = sortLabel;
	paramObj.splitType = splitType;
	paramObj.inputType = inputType;
	if(analysisDimensionType == "2") {
		var timeRange = {}; 
		getDateGroup(paramObj);
		timeRange = getTimeRange(paramObj.dateGroup);
		beginDate = timeRange.beginDate;
		endDate = timeRange.endDate;
	}
	paramObj.beginDate = beginDate;
	paramObj.endDate = endDate;
	getQueryConditionAndBrowser(paramObj);
	return paramObj;
};

window.getSubModelParams = function() {
	var bodyTypeId = $("#bodyTypeModalResultContainer li div").map(function() {return $.trim($(this).attr("value"));}).get().join(",");
	var beginDate = $("#dateYear .dateLIContainer").eq(0).find(".startDate-container input").val();
	var endDate = $("#dateYear .dateLIContainer").eq(0).find(".endDate-container input").val();
	var priceType = $("#priceType").val();//页面价格类型	
	var paramObj = {};
	var timeRange = {}; 
	paramObj.bodyTypeId = bodyTypeId;
	paramObj.priceType = priceType;
	getDateGroup(paramObj);
	timeRange = getTimeRange(paramObj.dateGroup);
	paramObj.beginDate = timeRange.beginDate;
	paramObj.endDate = timeRange.endDate;
	paramObj.inputType = inputType;
	return paramObj;	
}

/** 获取页头查询条件，以及浏览器**/
window.getQueryConditionAndBrowser = function(paramObj) {
	paramObj.browser = navigator.appVersion;
	var bodyTypeName = $(".bodyTypeModalResultContainer ul li div").map(function(){return $(this).text();}).get().join(",");
	var queryCondition = "";
	queryCondition += "价格类型 = ";
	if("1" == $("#priceType").val()) {
		queryCondition += "成交价";
	} else {
		queryCondition += "指导价";
	}
	queryCondition += "\n分析维度 = ";
	if(paramObj.analysisDimensionType == "1") {
		queryCondition += "车型对比";
		queryCondition += "\n时间= " + paramObj.beginDate + " - " + paramObj.endDate;
	} else {
		queryCondition += "时间对比";
		queryCondition += "\n时间= " + paramObj.dateGroup;
	}
	queryCondition += "\n对象= " + paramObj.subModelNames
	queryCondition += "\n车身形式= " + bodyTypeName;
	paramObj.queryCondition = queryCondition;
};

/**  获取时间组**/
window.getDateGroup = function(paramObj)
{
	var dateGroup = "";
	var dataLi = $("#dateYear .dateLIContainer");
	$.each(dataLi, function(i, n) {
		dateGroup += $(n).find(".startDate-container :input").val() + "," +  $(n).find(".endDate-container :input").val() + "|";
	});
		
	paramObj.dateGroup = dateGroup;
};

/** 导出Excel**/
function exportExcel(languageType) {
	if(selectData == null) {
		alert("暂无数据导出");
		return false;
	}
	$("#languageType").val(languageType);
	$("#ex_labelType").val($(".labelType:checked").val());
	$("#ex_bubbleType").val($(".bubbleType:checked").val());
	$("#ex_showType").val($(".showType:checked").val());
	$("#ex_labelSum").val($(".labelSum:checked").val());
	$("#ex_chartType").val($(".chartType:checked").val());
	$("#ex_sortLabel").val($(".sortLabel:checked").map(function() {return $(this).val();}).get().join(","));
	$("#ex_splitType").val($(".splitType:checked").val());
	$("#ex_analysisDimensionType").val($("#analysisDimensionType").val());
	$("#ex_priceType").val($("#priceType").val());
	$("#ex_series").val(JSON.stringify(exportSeries));
	$("#exportFormId").submit();
}

/** 车型弹出框**/
window.showSubModel = function(type) {
	if(type == 1) {
		$("#choseType").attr("value", "1")//本竞品
	} else if(type==2) {
		$("#choseType").attr("value", "2")//细分市场
	} else if(type==3) {
		$("#choseType").attr("value", "3")//品牌
	} else {
		$("#choseType").attr("value", "4")//厂商
	}
	/*根据选择条件隐藏*/
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
	/*根据选择条件隐藏结束*/
	if("1" == type) {
		getPriceSaleModelPage("tabs-competingProducts", type, inputType);
	} else if("2" == type) {
		getPriceSaleModelPage("tabs-segment", type, inputType);
	} else if("3" == type) {
		getPriceSaleModelPage("tabs-brand", type, inputType);
	} else {
		getPriceSaleModelPage("tabs-manf", type, inputType);
	}
};

/**
 * 展示页面
 * @param id 展示容器ID
 * @param type 子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
 * @param inputType 控件类型1：复选，2：单选;默认为1
 * @param timeType 时间类型1：时间点;2：时间段默认为2
 * 
 */
function getPriceSaleModelPage(id, type, inputType, timeType) {
	//如果内容不为空则触发请求
	if(!$.trim($("#" + id).html())) {
		//获取时间
		var beginDate = $("#dateYear .dateLIContainer").eq(0).find(".startDate-container input").val().replace("-", "");
		var endDate = $("#dateYear .dateLIContainer").eq(0).find(".endDate-container input").val().replace("-", "");
		var bodyTypeId = "";
		var paramObj = {};
		var priceType = $("#priceType").val();
		var analysisDimensionType = $("#analysisDimensionType").val();
		if(analysisDimensionType == "2") {
			var timeRange ={}; 
			getDateGroup(paramObj);
			timeRange = getTimeRange(paramObj.dateGroup);
			beginDate = timeRange.beginDate;
			endDate = timeRange.endDate;
		}
		//传递参数
		var bodyTypeId = $("#bodyTypeModalResultContainer li div").map(function() {return $.trim($(this).attr("value"));}).get().join(",");
		var params = {inputType: inputType, subModelShowType: type, priceType:priceType, bodyTypeId: bodyTypeId, beginDate: beginDate, endDate: endDate};
		//触发请求
		showLoading(id);
		$("#" + id).load(ctx+"/pricesale/scaleBySubModelSale/getSubModelModal", params, function() {
			$("#selectorResultContainerBySubModel .removeBtnByResult").each(function() {
				var subModelId = $(this).attr("subModelId");
				$(".subModelModalContainer .subModelIdInput").each(function() {
					if($(this).val() == subModelId) {
						$(this).attr("checked", "true");// 行全选
					}
				});
			});
			checkAll(type);
			/*根据选择条件隐藏*/
			var pooAttributeIdArr = [];
			$(".subModelModalContainer").find(".pooAttributeIdInput").each(function(){
				if($(this).attr("checked")) {
					pooAttributeIdArr[pooAttributeIdArr.length] = $(this).val();
				}
			});	
			
			$(".subModelModalContainer").find(".subModelIdInput").each(function() {
				var flag = false;
				for(var i = 0; i < pooAttributeIdArr.length;i++) {
					if(pooAttributeIdArr[i] == $(this).attr("pooAttributeId")){
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
		});
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

/**从时间组里获得最小时间和最大时间**/
function getTimeRange(dateGroup) {
	if(dateGroup.substr(dateGroup.length - 1, dateGroup.length) == "|") {
        dateGroup = dateGroup.substr(0, dateGroup.length - 1);
    }
	var minSDateIndex = 0;
	var maxEDateIndex = 0;
	var timeRange ={};
	var dates = dateGroup.split("|");
	for (var i = 0; i < dates.length;i++) {
		var sDate = parseInt(dates[i].split(",")[0].replace("-", ""));
		var eDate = parseInt(dates[i].split(",")[1].replace("-", ""));
		var minSDate = parseInt(dates[minSDateIndex].split(",")[0].replace("-", ""));
		var maxEDate = parseInt(dates[maxEDateIndex].split(",")[1].replace("-", ""));
		if(sDate < minSDate) {
			minSDateIndex = i;
		}
		if(eDate > maxEDate) {
			maxEDateIndex = i;
		}
	}
	timeRange.beginDate = dates[minSDateIndex].split(",")[0].replace("-", "");
	timeRange.endDate = dates[maxEDateIndex].split(",")[1].replace("-", "");
	return timeRange;
}
