var currSubModelLI = null;
$(document).ready(function() { 
	$("#startDate-container.input-append.date").datetimepicker({
		format: "yyyy-mm",
        language:  "zh-CN",        
		autoclose: true,				
		startView: 3,		
		maxView:3,
		minView:3,
		startDate:beginDate,		
		endDate:endDate
    });
    $("#endDate-container.input-append.date").datetimepicker({
		format: "yyyy-mm",
        language:  "zh-CN",        
		autoclose: true,				
		startView: 3,		
		maxView:3,
		minView:3,
		startDate:beginDate,
		endDate:endDate
    });
    
	/**时间改变事件*/
	$("#endDate-container.input-append.date").on("changeDate",function() {
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		if(!beginDate || !endDate) {
			alert("请选择时间");
			return false;
		}
		if(parseInt(beginDate.replace("-", "")) > parseInt(endDate.replace("-", ""))) {
			alert("开始时间不能大于结束时间");
			return false;
		}
	});
	
    $("#startDate-container.input-append.date").on("changeDate",function() {
    	var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		if(!beginDate || !endDate) {
			alert("请选择时间");
			return;
		}
		if(parseInt(beginDate.replace("-", "")) > parseInt(endDate.replace("-", ""))) {
			alert("开始时间不能大于结束时间");
			return;
		}
    });
    // 隐藏数据页
    $(".tab-content").hide();
  //常用对象确认时清空车型结果集
    $(".confirm[relcontainer='autoVersionModalResultContainer']").click(function(){
    	$(".subModelModalResultContainer .selectorResultContainer").html("");
    });
    
    /**城市选择器点击确定生成内容*/
    $(".cityModalContainer").find(".confirm").off().on("click", function() {
    	var containerId = $(this).parents(".cityModalContainer").attr("id");
    	var relContainer = $(this).attr("relContainer");
    	var relInputName = $(this).attr("relInputName");
    	$("#" + relContainer).html("");
    	var strHTML = "";
    	strHTML += '<ul class="selectorResultContainer">';
    	//如果城市全部选中，则生成全部城市
    	if($("#myModalLabel :checkbox").attr("checked")) {
    		strHTML += '<li>';
    		strHTML += '<div class="removeBtn" relContainer="cityModal" value="0" style="cursor:pointer;margin-top: 5px;" title="全部城市">';
    		strHTML += '<input type="hidden" value="0" name="selectedCity" />';
    		strHTML += '全部城市<i class="icon-remove" style="visibility: hidden;"></i>';
    		strHTML += '</div>';
    		strHTML += '</li>';
    		if($("#countryAverage").prop("checked")) {
    			strHTML += '<li>';
        		strHTML += '<div class="removeBtn" relContainer="cityModal" value="-1" style="cursor:pointer;margin-top: 5px;" title="全国均价">';
        		strHTML += '<input type="hidden" value="-1" name="selectedCity" />';
        		strHTML += '全国均价<i class="icon-remove" style="visibility: hidden;"></i>';
        		strHTML += '</div>';
        		strHTML += '</li>';
    		}
    	} else {
    		$(this).parents(".cityModalContainer").find(".cityModalByCity:checked").each(function() {
    			strHTML += '<li>';
    			strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+$(this).val()+'" style="cursor:pointer" title="删除：'+$.trim($(this).parent().text())+'">';
    			strHTML += '<input type="hidden" value="'+$(this).val()+'" name="'+relInputName+'" />';
    			strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
    			strHTML += '</div>';
    			strHTML += '</li>';
    		});
    	}
    	strHTML += '</ul>';
    	$("#"+relContainer).html(strHTML);
    });
});

/** 展示城市弹出框*/
$("#cityModal").on("show", function () {
	showLoading("cityModalBody");
	$("#cityModalBody").load(ctx+"/price/global/getCityModal", getParams(), function() {
		// 全国均价特殊处理
		if($("#cityModalResultContainer").html() == "") {
			$("#countryAverage").prop("checked", false);
		}
		var allCityValue;
		var flag = false;
		//设置默认选中项
		$("#cityModalResultContainer input").each(function() {
			var selectedId = $(this).val();
			allCityValue = selectedId;
			$("#cityModalBody").find(".cityModalByCity").each(function() {
				if($(this).val() == selectedId) {
					$(this).attr("checked", "true");//行全选
				}
			});
			if(allCityValue != -1 && !flag) {
				$("#countryAverage").prop("checked", false);
			} else {
				flag = true;
				$("#countryAverage").prop("checked", true);
			}
		});
		
		//行全选
		$("#cityModal").find(".cityModalByArea").each(function() {
			var allRowCityArr = $(this).parent().parent().parent().find(".cityModalByCity");
			var allRowSelectedCityArr = $(this).parent().parent().parent().find(".cityModalByCity:checked");
			if(allRowCityArr.length == allRowSelectedCityArr.length) {
				$(this).attr("checked","true");//全部全选		
			} else {
				$(this).removeAttr("checked");//全部取消
			}	
		});	
		//全选
		var allCityArr = $(".selectorTable").find(".cityModalByCity");
		var allSelectedCityArr = $(".selectorTable").find(".cityModalByCity:checked");
		//如果是全部城市则全选城市
		if(0 == $("#cityModalResultContainer div :input").val()) {
			if($("#myModalLabel :checkbox").attr("checked")) {
				$(".cityModalContainer").find(".cityModalByAll").click();
				$(".cityModalContainer").find(".cityModalByAll").click();
			} else {
				$(".cityModalContainer").find(".cityModalByAll").click();
			}
		} else if(allCityArr.length == allSelectedCityArr.length) {
			$(this).parents("#cityModal").find(".cityModalByAll").attr("checked","true");//全部全选		
		} else {
			$(this).parents("#cityModal").find(".cityModalByAll").removeAttr("checked");//全部取消
		}
	});
});

/** 弹出车身形式对话框 开始*/
$(".bodyTypeSelector").live("click",function(){
	currBodyTypeLI = $(this).parents("table").find(".bodyTypeModalResultContainer");
	$("#bodyTypeModal").modal("show");
});

/** 展示车身形式弹出框*/
$("#bodyTypeModal").on("show", function (e) {
	if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
	//去掉默认选中的效果
	$(".bodyTypeModalByAll").each(function(){
		$(this).removeAttr("checked");//取消行全选
	});
	/** 打开车身形式选择框时，清空车型和常用对象选择**/
	$(".subModelModalResultContainer").html("");
	$("#autoVersionModalResultContainer").html("");
	//加载子车型数据
	showLoading("bodyTypeModalBody");
	$("#bodyTypeModalBody").load(ctx+"/price/cityTpSelect/getBodyType", getParams(), function(){
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
/** 弹出车身形式对话框 结束*/

/** 展示车型弹出框-开始*/
$(".subModelSelector").live("click",function(){
	currSubModelLI = $(this).parents(".subModelLIContainer");
	//保存获取车形弹出框当前车型下标
	$("#getModelIndexId").val($("#model .subModelULContainer .subModelLIContainer").index(currSubModelLI));
	$("#subModelModal").modal("show");
});

/** 展示车型弹出框*/
$("#subModelModal").on("show", function (e) {
	if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
	//加载子车型数据
	showLoading("subModelModalBody");
	$("#subModelModalBody").load(ctx+"/price/cityTpSelect/getSubmodelModal", getParams(), function() {
		//弹出框设置默认选中项结果集		
		var strHTML = '<ul class="inline" >';
		$(currSubModelLI).find(".subModelModalResultContainer input").each(function() {
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
					$(this).attr("checked", "true");
				}
			});
		});
		strHTML += '</ul>';
		$("#selectorResultContainerBySubModel").html(strHTML);
	});
});
/** 展示车型弹出框-结束*/

/** 车型确认按钮动作*/
$(".subModelModalContainer").find(".confirm").unbind("click").bind("click",function(e) {
	/**选了车型后清空常用对象选项**/
	$('#autoVersionModalResultContainer').html("");
	var containerId = $(this).parents(".subModelModalContainer").attr("id");
	var relInputName = $(this).attr("relInputName");
	var allSubModelArr = [];
	$(this).parents(".subModelModalContainer").find(".resultShowContent").find(".removeBtnByResult").each(function(){
		var obj = {};
		obj.subModelId =  $(this).attr("subModelId");
		obj.subModelName =  $(this).attr("subModelName");
		obj.letter =  $(this).attr("letter");
		obj.pooAttributeId = $(this).attr("pooAttributeId");
		allSubModelArr[allSubModelArr.length] = obj;
	});
	var strHTML = "";
	strHTML += '<ul class="selectorResultContainer">';
	for(var i=0;i<allSubModelArr.length;i++){
		strHTML += '<li>';
	  	strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+allSubModelArr[i].subModelId+'" style="cursor:pointer" title="删除：'+allSubModelArr[i].subModelName+'">';
		strHTML += '<input type="hidden" letter="'+allSubModelArr[i].letter+'" pooAttributeId="'+allSubModelArr[i].pooAttributeId+'" subModelName="'+allSubModelArr[i].subModelName+'" value="'+allSubModelArr[i].subModelId+'" name="'+relInputName+'">';
		strHTML += allSubModelArr[i].subModelName + '<i class="icon-remove" style="visibility: hidden;"></i>';
	  	strHTML += '</div>';
  		strHTML += '</li>';
	 }
	strHTML += '</ul>';
	$(currSubModelLI).find(".subModelModalResultContainer").html(strHTML);
});

/** 弹出常用对象对话框 开始*/
$("#autoVersionModal").on("show", function (e) {	
	//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
	if(e.relatedTarget)  return; 
	//加载子常用对象下型号数据
	showLoading("autoVersionModalBody");
	/**打开常用对象时,清空车型选择**/
	$(".subModelModalResultContainer").html("");
	$("#autoVersionModalBody").load(ctx + "/price/cityTpSelect/getAutoCustomGroup", getParams(), function() {
		//设置默认选中项
		$("#autoVersionModalResultContainer input").each(function() {
			var selectedId = $(this).val();
			$(".autoVersionModalContainer .autoVersionIdInput").each(function() {
				if($(this).val() == selectedId) {
					$(this).attr("checked", "true");
				}
			});
		});
		
		//设置全选效果---开始
		$(".autoVersionModalContainer").find(".selectAutoVersionAll").each(function() {
			var selectedCount = 0;
			var totalCount = 0;
			var objectGroup = $(this).val();
			$(".autoVersionModalContainer .autoVersionIdInput").each(function() {
				if(objectGroup == $(this).attr("objectGroup")) {
					totalCount++;
					if($(this).attr("checked")) {
						selectedCount++;
					}
				}
			});
			if(selectedCount == totalCount) {
				$(this).attr("checked", "true");//同个组别全选
			}else{
				$(this).removeAttr("checked");//取消选中
			}
		});
		//设置全选效果---结束
	});
});
/** 弹出常用对象对话框 结束*/

/** 获取页面请求参数*/
function getParams() {	
	var paramObj = {};
	var beginDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var maxDate = $("#maxDate").val();
	//频次类型
	var frequencyType = $("#frequencyType").val();
	//成交价类型
	var tpType = $("#tpType").val();
	var inputType = 1;//复选
	var bodyTypeId = $("#model .subModelLIContainer").eq($("#getModelIndexId").val()).find("td:eq(1)").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	var bodyTypeName = $("#model .subModelLIContainer").eq($("#getModelIndexId").val()).find("td:eq(1)").find("li div").map(function(){return $(this).text();}).get().join(",");
	//城市ID
	var citys = getCitys();
	//检查是否有选全国均价
	if(checkCountryAvg(citys)) {
		paramObj.countryAvg = "1";
		$("#countryAvg").val("1");
	} else {
		paramObj.countryAvg = "0";
		$("#countryAvg").val("0");
	}
	var versionIds = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").map(function(){return $(this).val();}).get().join(",");
	var subModelIds = $(".subModelModalResultContainer input[name='selectedSubModel']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
	paramObj.beginDate = beginDate;
	paramObj.endDate = endDate;
	paramObj.maxDate = maxDate;
	paramObj.frequencyType = frequencyType;
	paramObj.tpType = tpType;
	paramObj.inputType = inputType;
	paramObj.bodyTypeId = bodyTypeId;
    paramObj.bodyTypeName = bodyTypeName;
    paramObj.citys = citys;
	paramObj.subModelIds = subModelIds;
	paramObj.versionIds = versionIds;
	paramObj.timeType = "2";
	getQueryConditionAndBrowser(paramObj);
	return paramObj;
}

/**检查是否有选全国均价*/
function checkCountryAvg(citys) {
	var flag = false;
    for(var i = 0; i < citys.split(",").length; i++) {
    	if(citys.split(",")[i] == -1) {
    		flag = true;
    	}
    }
	return flag;
}

/**频次选择框改变事件*/
function changeFrequencyType() {
	var frequencyType = $("#frequencyType option:selected").val();
	var beginDateTime = $("#startDate").val();
	var endDateTime = $("#endDate").val();
	var startDateObj = $("#startDate-container.input-append.date");
	var endDateObj = $("#endDate-container.input-append.date");
	if(frequencyType == "5") {
		$(startDateObj).datetimepicker("remove");
		$(endDateObj).datetimepicker("remove");
		if(beginDateTime.indexOf("-") != -1) {
			beginDateTime = beginDateTime.substr(0, 4);
			endDateTime = endDateTime.substr(0, 4);
		}
		$("#startDate-container.input-append.date").datetimepicker({
			format: "yyyy",
			language:  "zh-CN",        
			autoclose: true,				
			startView: 4,		
			maxView:4,
			minView:4,
			startDate:beginDate.substr(0, 4),		
			endDate:endDate.substr(0, 4)
        });
		$("#endDate-container.input-append.date").datetimepicker({
			format: "yyyy",
			language:  "zh-CN",        
			autoclose: true,				
			startView: 4,		
			maxView:4,
			minView:4,
			startDate:beginDate.substr(0, 4),		
			endDate:endDate.substr(0, 4)
        });
		$(startDateObj).datetimepicker("update", beginDateTime);
		$(endDateObj).datetimepicker("update", endDateTime);
	} else {
		$(startDateObj).datetimepicker("remove");
		$(endDateObj).datetimepicker("remove");
		if(beginDateTime.indexOf("-") == -1) {
			beginDateTime += "-01";
			endDateTime += "-01";
		}
		$("#startDate-container.input-append.date").datetimepicker({
			format: "yyyy-mm",
	        language:  "zh-CN",        
			autoclose: true,				
			startView: 3,		
			maxView:3,
			minView:3,
			startDate:beginDate,		
			endDate:endDate
	    });
	    $("#endDate-container.input-append.date").datetimepicker({
			format: "yyyy-mm",
	        language:  "zh-CN",        
			autoclose: true,				
			startView: 3,		
			maxView:3,
			minView:3,
			startDate:beginDate,
			endDate:endDate
	    });
	    $(startDateObj).datetimepicker("update", beginDateTime);
		$(endDateObj).datetimepicker("update", endDateTime);
	}
}

/**获取页头查询条件，以及浏览器*/
function getQueryConditionAndBrowser(paramObj) {
	paramObj.browser = navigator.appVersion;
	var queryCondition = "";
	queryCondition += "频次 = ";
	if("1" == $("#frequencyType").val()) {
		queryCondition += "周";
	} else if("2" == $("#frequencyType").val()) {
		queryCondition += "每半月";
	} else if("3" == $("#frequencyType").val()) {
		queryCondition += "月";
	} else if("4" == $("#frequencyType").val()) {
		queryCondition += "季度";
	} else {
		queryCondition += "年";
	}
	
	queryCondition += "\n时间 = " + $("#startDate").val() + "至" + $("#endDate").val();
	queryCondition += "\n城市 = " + $("#cityModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
	queryCondition += "\n车身形式= " + paramObj.bodyTypeName;
	queryCondition += "\n车型 = " + $(".subModelModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
	queryCondition += "\n常用对象型号组 = " + $("#autoVersionModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
	paramObj.queryCondition = queryCondition;
}

/**弹出框*/
window.showSubModel = function(type) {
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
	var inputType = "1"; //弹出框默认多选
	if("1" == type) {
		getModelPage("tabs-competingProducts", type, inputType);
	} else if("2" == type) {
		getModelPage("tabs-segment", type, inputType);
	} else if("3" == type) {
		getModelPage("tabs-brand", type, inputType);
	} else {
		getModelPage("tabs-manf", type, inputType);
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
function getModelPage(id, type, inputType) {
	//如果内容不为空则触发请求
	if(!$.trim($("#" + id).html())) {
		//获取时间
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var bodyTypeId = "";
		var paramObj = {};
		var dateGroup = null;
		getDateGroup(paramObj)
		bodyTypeId = $("#model .subModelLIContainer").eq($("#getModelIndexId").val()).find("td:eq(1)").find("li div :input").map(function(){return $(this).val();}).get().join(",");
		//传递参数
		var params = {inputType:inputType,subModelShowType:type,beginDate:beginDate,endDate:endDate,bodyTypeId:bodyTypeId,dateGroup:dateGroup};
		//触发请求
		showLoading(id);
		$("#" + id).load(ctx+"/price/cityTpSelect/getSubmodelModal", params, function() {
			$("#selectorResultContainerBySubModel .removeBtnByResult").each(function() {
				var subModelId = $(this).attr("subModelId");
				$(".subModelModalContainer .subModelIdInput").each(function() {
					if($(this).val() == subModelId) $(this).attr("checked","true");//行全选
				});
			});
			checkAll(type);
			
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
		});
	}
}

/**获取时间组*/
window.getDateGroup = function(paramObj) {
	var dateGroup = "";
	dateGroup += $("#dateYear").find("#startDate-container :input").val() + "," +  $("#dateYear").find("#endDate-container :input").val() + "|";
	paramObj.dateGroup = dateGroup;
};

/**把有全选的选项选上*/
function checkAll(type) {
	if("2" == type) {
		var allObj = $("#tabs-segment .selectorTypeTd");
		$(allObj).find(".selectSegmentAll").each(function() {
			var flag = true;
			var modelObj = $(this);
			$(modelObj).closest("td").next().find(".subModelIdInput").each(function() {
				if(!$(this).prop("checked")){
					flag = false;
				}
			});
			//如果一个细分市场下的车型被全选，则细分市场也选上
			if(flag == true){
				modelObj.prop("checked", true);
			}
		});
	} else if("3" == type) {
		var allObj = $("#tabs-brand .selectorTypeTd");
		$(allObj).find(".selectBrandAll").each(function(){
			var flag = true;
			var modelObj = $(this);
			$(modelObj).closest("td").next().find(".subModelIdInput").each(function() {
				if(!$(this).prop("checked")){
					flag = false;
				}
			});
			//如果一个品牌下的车型被全选，则品牌也选上
			if(flag == true){
				modelObj.prop("checked", true);
			}
		});
	} else if("4" == type) {
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

/**点击确定查询*/
$("#queryBtn").on("click", function (e) {
	if(!paramsValidate()) {
		return;
	}
	$("body").showLoading();
	var citys = $("#cityModalResultContainer input[name='selectedCity']").map(function(){return $(this).val();}).get().join(",");
	//如果选择全部城市，则取弹出枉里的城市ID
	citys = getCitys();
	var allCityNames = $("#allCityNames");
	var allCityNameEns = $("#allCityNameEns");
	//只选了全国均价
	if(citys.length == 2 && citys.indexOf("-1") != -1) {
		allCityNames.val("全国均价");
		allCityNameEns.val("CountryAvg");
	} else {
		//获取城市名称和排序
		$.ajax({
			type: "POST",
			async: false,
			url: ctx + "/price/cityTpSelect/getCityNameAndSort",
			data: {citys: citys},
			success: function(data) {
				//页面加载隐藏图表面板
				$(".tab-content").hide();
				var cityName = "";
				var cityNameEn = "";
				for(var i = 0; i < data.length; i++) {
					dataObj = data[i];
					cityName += dataObj.CITYNAME + ",";
					cityNameEn += dataObj.CITYNAMEEN + ",";
				}
				allCityNames.val(cityName.substr(0, cityName.length - 1));
				allCityNameEns.val(cityNameEn.substr(0, cityNameEn.length - 1));
			},
		});	
	}
	//发送请求
	$.ajax({
        type: "POST",
		url: ctx + "/price/cityTpSelect/getVersionTpData",
		data: getParams(),
		success: function(data) {
		   if(data) {
		       //查询面板折叠
		       $(".queryConditionContainer .buttons .toggle a").click();
			   $("#noData").css("display","none");
			   $("#exportButtons").css("display","block");
			   $("#chartId").height(450);
			   showData(data);
		   } else {
			   $("#tableHead").removeClass("hide");
			   $("#gridTbody").html("无数据!");
		   }
		   $("body").hideLoading();
	   },
	   error:function() {
		   $("body").hideLoading();
	   }
	});
});

/**参数验证*/
window.paramsValidate = function() {
	var paramObj = getParams();
	if((paramObj.versionIds == "" || paramObj.versionIds == null) && (paramObj.subModelIds == "" || paramObj.subModelIds == null)) {
		alert("请选择车型或常用对象");
		return false;
	}
	if(paramObj.citys == "" || paramObj.citys == null) {
		alert("请选择城市");
		return false;
	}
	getDateGroup(paramObj);
	var dateGroup = (paramObj.dateGroup).substr(0, paramObj.dateGroup.length - 1).split("|");
	var date = dateGroup[0].split(",");
	var beginDate = parseInt(date[0].replace("-", ""));
	var endDate = parseInt(date[1].replace("-", ""));
	if(beginDate > endDate) {
		alert("开始时间不能大于结束时间");
		return false;
	}
	return true;
};

/**展示数据表格*/
function showData(data) {
	$("#tableHead").removeClass("hide");
	$(".tab-content").show();
	var citys = $("#cityModalResultContainer input[name='selectedCity']").map(function(){return $(this).val();}).get().join(",");
	var cityNum = citys.split(",");
	var tbodyHtml = "";
	var tr = $("#headTr");
	$("#gridTbody").html("");
	if(data) {
		data = getCityNames(data);
		var cityNameTitles = data.cityNameTitle.split(",");
		var cityNameEnTitles = data.cityNameEnTitle.split(",");
		//重新搭建表头
		tr.html(createTableHead());
		var headStr = "";
		for(var i = 0; i < cityNameTitles.length; i++) {
			if("" != cityNameTitles[i] && null != cityNameTitles[i] && undefined != cityNameTitles[i]) {
				headStr += '<th class="tHead">' + cityNameTitles[i] + "</br>" + cityNameEnTitles[i] + '</th>';
			}
		}
		if(cityNum.length >= 3 || (cityNum.length >= 2 && $("#countryAvg").val() == "0") || citys.indexOf("0") != -1) {
			headStr += '<th class="tHead">所选城市算术平均价(元)</br>Citys Avg(RMB)</th>';
			headStr += '<th class="tHead">所选城市加权平均价(元)</br>Citys MixAvg(RMB)</th>';
		}
		if(citys.indexOf("-1") != -1) {
			headStr += '<th class="tHead">全国均价(元)</br>Country Avg(RMB)</th>';
		}
		if(cityNum.length >= 3 || (cityNum.length >= 2 && $("#countryAvg").val() == "0") || citys.indexOf("0") != -1) {
			headStr += '<th class="tHead" id="promotionsTh">内部促销信息</br>Promotions</th>';
		}
		tr.html(tr.html() + headStr);
		for(var i = 0; i < data.length; i++) {
			var dataObj = data[i];
			tbodyHtml += "<tr>";
			tbodyHtml += "<td class='tdSubModel'>" + dataObj.PRICETIME + "</td>";
			tbodyHtml += "<td class='tdSubModel'>" + dataObj.BATCH + "</td>";
			tbodyHtml += "<td class='tdSubModel'>" + dataObj.VERSIONCODE + "</td>";
			tbodyHtml += "<td class='tdSubModel'>" + dataObj.SEGMENTNAME +"</td>";
			tbodyHtml += "<td class='tdSubModel'>" + dataObj.BRANDNAME +"</td>";
			tbodyHtml += "<td class='tdSubModel'>" + dataObj.MANFNAME + "</td>";
			tbodyHtml += "<td class='tdSubModel'>" + dataObj.SUBMODELNAME + "</td>";
			tbodyHtml += "<td class='tdSubModel'>" + dataObj.ENGINECAPACITY + "</td>";
			tbodyHtml += "<td class='tdSubModel'>" + dataObj.TRANSMISSIONTYPE + "</td>";
			tbodyHtml += "<td class='tdSubModel'>" + dataObj.VERSIONMARK + "</td>";
			tbodyHtml += "<td class='tdSubModel'>" + dataObj.BODYTYPENAME + "</td>";
			tbodyHtml += "<td class='tdSubModel'>" + dataObj.LAUNCHDATE + "</td>";
			tbodyHtml += "<td class='tdSubModel'>" + dataObj.YEAR + "</td>";
			tbodyHtml += "<td class='tdSubModel'>" + changeShow(dataObj.MSRP) + "</td>";
			if(cityNum.length == 1 && citys.indexOf("-1") == -1 || cityNum.length >= 2) {
				for(var j = 0; j < cityNameEnTitles.length; j++) {
					tbodyHtml += "<td class='tdSubModel'>" + changeShow(dataObj[cityNameEnTitles[j]]) + "</td>";
				}
			}
			if(cityNum.length >= 3 || (cityNum.length >= 2 && $("#countryAvg").val() == "0") || citys.indexOf("0") != -1) {
				tbodyHtml += "<td class='tdSubModel'>" + changeShow(dataObj.AMTAVG) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + changeShow(dataObj.MIXAVG) + "</td>";
			}
			if(citys.indexOf("-1") != -1) {
				tbodyHtml += "<td class='tdSubModel'>" + changeShow(dataObj.COUNTRYAVG) + "</td>";
			}
			if(cityNum.length >= 3 || (cityNum.length >= 2 && $("#countryAvg").val() == "0") || citys.indexOf("0") != -1) {
				tbodyHtml += "<td class='tdSubModel'>" + changeShow(dataObj.PROMOTIONS) + "</td>";
			}
			tbodyHtml += "</tr>";
		}
		$("#gridTbody").html(tbodyHtml);
    }
}

/**搭建表头*/
function createTableHead() {
	var str = "";
	str += '<th class="tHead">日期</br>Date</th>';
	str += '<th class="tHead">批次</br>Batch</th>';
	str += '<th class="tHead">型号编码</br>Version Code</th>';
	str += '<th class="tHead">级别</br>Segment</th>';
	str += '<th class="tHead">品牌</br>Brand</th>';
	str += '<th class="tHead">生产厂</br>OEM</th>';
	str += '<th class="tHead">车型</br>Model</th>';
	str += '<th class="tHead">排量</br>Engine Capacity</th>';
	str += '<th class="tHead">排挡方式</br>Transmission Type</th>';
	str += '<th class="tHead">型号标识</br>Version Mark</th>';                               
	str += '<th class="tHead">车身形式</br>Body Type</th>';
	str += '<th class="tHead">上市时间</br>Launch Date</th>';
	str += '<th class="tHead">年式</br>Year</th>';
	str += '<th class="tHead">厂商指导价(元)</br>MSRP(RMB)</th>';
	return str;
}
/**获取城市名称作为表头*/
function getCityNames(data) {
	var allCityNames = $("#allCityNames").val().split(",");
	var allCityNameEns = $("#allCityNameEns").val().split(",");
	var cityNameTitle = "";
	var cityNameEnTitle = "";
	var dataObj = data[0];
	for(var j = 0; j < allCityNameEns.length; j++) {
		for (var key in dataObj) { 
   		    if(key == allCityNameEns[j]) {
				cityNameTitle += allCityNames[j] + ",";
				cityNameEnTitle += allCityNameEns[j] + ",";
			}
		} 
	}
	data.cityNameTitle = cityNameTitle.substr(0, cityNameTitle.length - 1);
	data.cityNameEnTitle = cityNameEnTitle.substr(0, cityNameEnTitle.length - 1);
	return data;
}

/**将空值置为-*/
function changeShow(str) {
	if(null == str || "" == str || undefined == str) {
		return "-";
	} else {
		return thousandthStyle(str);
	}
}

/**千分位的处理*/
function thousandthStyle(number) {
    if("" == number || null == number || undefined == number) {
	    return "";
    }
    if(0 == number) {
	    return "0";
    }
    var num = number + "";  
    num = num.replace(new RegExp(",", "g"), "");   
    // 正负号处理   
    var symble = "";   
    if(/^([-+]).*$/.test(num)) {   
    	symble = num.replace(/^([-+]).*$/, "$1");   
        num = num.replace(/^([-+])(.*)$/, "$2");   
    }   
    if(/^[0-9]+(\.[0-9]+)?$/.test(num)) {   
        var num = num.replace(new RegExp("^[0]+", "g"), "");   
        if(/^\./.test(num)) {   
            num = "0" + num;   
        }   
  
        var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1");   
        var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1");   
        var re = /(\d+)(\d{3})/;  
        while(re.test(integer)) {   
            integer = integer.replace(re, "$1,$2");  
        }   
        return symble + integer + decimal;   
    } else {
        return number;   
    }   
}

/**是否全选城市*/
function getCitys() {
	var citys = $("#cityModalResultContainer input[name='selectedCity']").map(function(){return $(this).val();}).get().join(",");
	//如果选择全部城市，则取弹出枉里的城市ID
	if(citys.split(",")[0] == "0") {
		var endDate_TPMix = endDate.substr(0, 4) - 1;
		var flag = false;
		if(citys.indexOf("-1") != -1) {
			flag = true
		}
		if(endDate_TPMix > 2013) {
			citys = "8,17,28,3,18,7,12,15,31,2,5,11,23,1,14,16,20,32,4,13,21,22,29,19,30,41,42,48,60,61,47";
		} else {
			citys = "8,17,28,3,18,7,12,15,31,2,5,11,23,1,14,16,20,32,4,13,21,22,29,47";
		}
		if(flag) {
			citys += ",-1";
		}
	}
	return citys;
}

/**车型控件值鼠标经过事件*/
$(".removeBtn").live("mouseover",function() {
	$(this).find(".icon-remove").css({visibility:"visible"});
});

/**车型控件值鼠标离开事件*/
$(".removeBtn").live("mouseout",function() {
	$(this).find(".icon-remove").css({visibility:"hidden"});
});

/**删除车身形式时，清空车型选择*/
$(".bodyTypeModalResultContainer").find(".icon-remove").live("click",function(){
	$(".subModelModalResultContainer").html("");
});

/**重置*/
$("#resetBtn").on("click", function (e) {
	//清空车型
	$(".subModelModalResultContainer").html("");
	//清空常用对象
	$("#autoVersionModalResultContainer").html("");
	//清空车身形式
	$(".bodyTypeModalResultContainer").html(""); 
	//清空城市
	$("#cityModalResultContainer").html("");
});

/** 导出Excel*/
function exportExcel(languageType) {
	var content = $.trim($("#gridTbody").html());
	var citys = getCitys();
	$("#citys").val(citys);
	if(!content || "无数据!" == content) {
	    alert("暂无数据导出");
		return false;
	}
	$("#languageType").val(languageType);
	$("#exportFormId").submit();
}