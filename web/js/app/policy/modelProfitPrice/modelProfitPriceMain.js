$(document).ready(function(){
	
	$("#analysisDimensionType").on("change",function(){
		initDateByObject();
	});
	
	//频次切换时时间控件根据频次变化
	$("#frequency").on("change",function(){
		initDateByObject();
	});
	
	/**获取时间模板*/
	function getDateTp(analysisDimensionType,frequency){
		//频次为季度,年份时选择时间为年份
		var defaultBeginDate_temp = defaultBeginDate;
		var endDate_temp = endDate;
		if(3 < frequency)
		{
			defaultBeginDate_temp = defaultBeginDate.substr(0,4);
			endDate_temp = endDate.substr(0,4);
		}
		
		var htmlStr = "";
		//分析维度为时间对比时,时间选项为时间段
		if(analysisDimensionType == 2){
			htmlStr += '<li style="list-style: none;margin-bottom:10px;" class="dateLIContainer">';
		    htmlStr += '	<div class="form-inline">';
		    htmlStr += '  		<span class="startDate-container input-append date">';
			htmlStr += '	  		<input id = "startDate" type="text" value="'+defaultBeginDate_temp+'"  readonly="readonly" class="input-mini white" placeholder="开始日期"  />';
			htmlStr += '	  		<span class="add-on"><i class="icon-th"></i></span>';
			htmlStr += '	  		<select name="sDateUnit" onchange = "validateDate()" style ="width:120px;"/>';
			htmlStr += '		</span>';
			htmlStr += '		<span class="2">至</span>';
		    htmlStr += '  		<span class="endDate-container input-append date">';
			htmlStr += '	  		<input id = "endDate" type="text" value="'+endDate_temp+'" readonly="readonly" class="input-mini white"  placeholder="结束日期" />';
			htmlStr += '	  		<span class="add-on"><i class="icon-th"></i></span>';
			htmlStr += '	  		<select name="eDateUnit" onchange = "validateDate()" style ="width:120px;"/>';
			htmlStr += '		</span>';
			htmlStr += '	</div>';
		 	htmlStr += '</li>';
		}else{
			htmlStr += '<li style="list-style: none;margin-bottom:10px;" class="dateLIContainer">';
		    htmlStr += '	<div class="form-inline">';
		    htmlStr += '  		<span class="startDate-container input-append date">';
			htmlStr += '	  		<input id = "startDate" type="text" value="'+endDate_temp+'"  readonly="readonly" class="input-mini white" placeholder="开始日期"  />';
			htmlStr += '	  		<span class="add-on"><i class="icon-th"></i></span>';
			htmlStr += '	  		<select name="sDateUnit" onchange = "validateDate()" style ="width:120px;"/>';
			htmlStr += '		</span>';
			htmlStr += '	</div>';
		 	htmlStr += '</li>';
		}	
	 	return htmlStr;
	}
	
	/** 时间控件初始化*/
	function initDate(frequency){
		var format = 'yyyy-mm';
		var viewType = "year";
		var beginDate_temp = beginDate;
		var endDate_temp = endDate;
		if( 3< frequency){
			format = 'yyyy';
			viewType = 'decade';
			beginDate_temp = beginDate.substr(0,4);
			endDate_temp = endDate.substr(0,4);
		}
		
		$('.startDate-container.input-append.date').datetimepicker({
			format: format,
	        language:  'zh-CN',        
	        todayBtn:  0,
			autoclose: 1,				
			startView: viewType,		
			maxView:viewType,
			minView:viewType,
			startDate:beginDate_temp,
			endDate:endDate_temp,
	        showMeridian: 1
	    });
	    $('.endDate-container.input-append.date').datetimepicker({
			format: format,
	        language:  'zh-CN',        
	        todayBtn:  0,
			autoclose: 1,				
			startView: viewType,		
			maxView:viewType,
			minView:viewType,
			startDate:beginDate_temp,
			endDate:endDate_temp,
	        showMeridian: 1
	    });
	}
	
	function initDateByObject(){
	  var analysisDimensionType = $("#analysisDimensionType").val();
	  var frequency = $("#frequency").val();
	  $(".dateULContainer li").remove();
	  $(".dateULContainer").append(getDateTp(analysisDimensionType,frequency));
	  //初始化时间控件设置
	  initDate(frequency);
	  //频次为月,年时不需要下拉选择框
	  if(3 == frequency || 5 == frequency){
	  	$(".dateULContainer select[name = 'sDateUnit']").addClass("hide");
		$(".dateULContainer select[name = 'eDateUnit']").addClass("hide");
	  }else{
	  	  //填充周,半月,季度下拉框
		  getDateUnit();
	  }
	  
	  //时间改变事件
	$('.endDate-container.input-append.date').on('changeDate',function(){
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		
		var frequency = $("#frequency").val();
		var analysisDimensionType = $("#analysisDimensionType").val();
	  	
		if (2 == analysisDimensionType) {
			if (!beginDate || !endDate) {
				alert("请选择时间");
				return false;
			}
			if (parseInt(beginDate.replace("-", "")) > parseInt(endDate.replace("-", ""))) {
				alert("开始时间不能大于结束时间");
				return false;
			}
			validateDate();
		}else{
			if (!beginDate) {
				alert("请选择时间");
				return;
			}
		}
		
		if(3 != frequency && 5 != frequency){
			//填充周,半月,季度下拉框
		  	getDateUnit();
	  	}
		
		//checkPopBoxData();
	});
	
    $('.startDate-container.input-append.date').on('changeDate',function(){
    	var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		
		var frequency = $("#frequency").val();
		var analysisDimensionType = $("#analysisDimensionType").val();
		//分析维度为时间对比时才拥有两个时间控件
		if (analysisDimensionType == 2) {
			if (!beginDate || !endDate) {
				alert("请选择时间");
				return;
			}
			if (parseInt(beginDate.replace("-", "")) > parseInt(endDate.replace("-", ""))) {
				alert("开始时间不能大于结束时间");
				return;
			}
			validateDate();
		}else{
			if (!beginDate) {
				alert("请选择时间");
				return;
			}
		}
		
		if(3 != frequency && 5 != frequency){
			//填充周,半月,季度下拉框
		  	getDateUnit();
	  	}
		
    	//checkPopBoxData();
    });
	}
	
	//初始化页面
	initDateByObject();
	
    /**
	 * 加载周,半月,季度下拉选择框
	 */
	function getDateUnit(){
		var params = {};
		var analysisDimensionType = $("#analysisDimensionType").val();
		var frequency = $("#frequency").val();
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		
		//清空下拉选项框
		$(".dateULContainer select[name = 'sDateUnit'] option").remove();
		$(".dateULContainer select[name = 'eDateUnit'] option").remove();
		
		if ("2" == analysisDimensionType) {
			params.timeType = 2;
		}
		else {
			params.timeType = 1;
		}
	  	params.frequency = frequency;
		params.beginDate = beginDate;
		params.endDate = endDate;
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/policy/getDateUnit",
			   data: params,
			   dataType:'json',
			   success: function(data){
				   if(data){
				   		var option = "";
				   		for(var i = 0;i < data.sDate.length;i++){
							option +="<option value='"+data.sDate[i].VALUE+"'>"+data.sDate[i].TEXT+"</option>";	
						}
						$(".dateULContainer select[name = 'sDateUnit']").append(option);
				   	 	if(2 == analysisDimensionType){
							option = "";
							for(var i = 0;i < data.eDate.length;i++){
								option +="<option value='"+data.eDate[i].VALUE+"'>"+data.eDate[i].TEXT+"</option>";	
							}
							$(".dateULContainer select[name = 'eDateUnit']").append(option)
						}
				   }
			   }
			});
	}
	
	//判断时间选择先后是否冲突
	window.validateDate =function(){
		var endDate = $("#endDate").val();
		var beginDate = $("#startDate").val();
		var frequency = $("#frequency").val();
		var analysisDimensionType = $("#analysisDimensionType").val();
	  	
		if(2 == analysisDimensionType && 3 != frequency && 5 != frequency){
			if(parseInt(beginDate.replace("-", "")) == parseInt(endDate.replace("-", ""))){
				var sIndex = $(".dateULContainer select[name = 'sDateUnit']").get(0).selectedIndex;
				var eIndex = $(".dateULContainer select[name = 'eDateUnit']").get(0).selectedIndex;
				if(sIndex > eIndex){
					alert("开始时间不能大于结束时间");
					return;
				}
			}
		}
	}
	
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
			if(0 == $("#cityModalResultContainer div :input").val()){
				if($("#myModalLabel :checkbox").attr("checked")){
					$(".cityModalContainer").find('.cityModalByAll').click();
					$(".cityModalContainer").find('.cityModalByAll').click();
				}
				else{
					$(".cityModalContainer").find('.cityModalByAll').click();
				}
			}else if(allCityArr.length == allSelectedCityArr.length){
				$(this).parents('#cityModal').find('.cityModalByAll').attr("checked",'true');//全部全选		
			}else{
				$(this).parents('#cityModal').find('.cityModalByAll').removeAttr("checked");//全部取消
			}
		});
	});
	
	$('#subModelModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		
		//加载子车型数据
		showLoading("subModelModalBody");
//		var paramObj = getParams();
//		var frequency = paramObj.frequency;
//		var timeType = paramObj.timeType;
//		if(4 == frequency){
//			paramObj.b			
//		}
//		if(5 == frequency){
//			
//		}
		$('#subModelModalBody').load(ctx+"/policy/global/modelProfitPrice/getSubmodelModal",getParams(),function(){
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
		});
	});
	
	$('#versionModal').on('show', function (e) {	
		
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
		if(e.relatedTarget || 0 == $("#subModelModalResultContainer input[name='selectedSubModel']").length)  return; 
		//加载子车型下型号数据
		showLoading("versionModalBody");
		$('#versionModalBody').load(ctx+"/policy/global/modelProfitPrice/getVersionModalByCommon",getParams(),function(){
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
	
	$('#autoVersionModal').on('show', function (e) {
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
		if(e.relatedTarget)  return; 
		//加载子常用对象下型号数据
		showLoading("autoVersionModalBody");
		$('#autoVersionModalBody').load(ctx+"/global/getAutoCustomGroup",getParams(),function(){
			//设置默认选中项
			$('#autoVersionModalResultContainer input').each(function(){
				var selectedId = $(this).val();
				$(".autoVersionModalContainer .autoVersionIdInput").each(function(){
					if($(this).val() == selectedId){
						$(this).attr("checked",'true');//行全选
					}
				});
			});
			
			//设置全选效果---开始
			$(".autoVersionModalContainer").find('.selectAutoVersionAll').each(function(){
				var selectedCount = 0;
				var totalCount = 0;
				var objectGroup = $(this).val();
				$(".autoVersionModalContainer .autoVersionIdInput").each(function(){
					if(	objectGroup == $(this).attr("objectGroup")){
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
		var beginDate;
		var endDate;
		var inputType = "1";//控件默认单选
		var timeType = "1";
		var frequency = $("#frequency").val();//频次
		var moduleName = $("#moduleName").val();//模块标志
		var analysisDimensionType = $("#analysisDimensionType").val();//分析维度
		//分析维度为时间对比时弹出框里面按钮为单选
		if ("1" != analysisDimensionType) {
			inputType = "2";
		}
		//分析维度为时间对比时存在结束时间(timeType时间点或时间段标志:1时间点,2时间段)
		if("2" == analysisDimensionType) {
			timeType = "2";
			if(3 == frequency || 5 == frequency){
				beginDate = $("#startDate").val();
				endDate = $("#endDate").val();
			}else{
				beginDate = $(".dateULContainer select[name = 'sDateUnit']").val();
				endDate = $(".dateULContainer select[name = 'eDateUnit']").val();
			}
		}else{
			if(3 == frequency || 5 == frequency){
				beginDate = $("#startDate").val();
			}else{
				beginDate = $(".dateULContainer select[name = 'sDateUnit']").val();
			}
		}
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
		paramObj.endDate = endDate;
		paramObj.inputType = inputType;
		paramObj.frequency = frequency;
		paramObj.citys = citys;
		paramObj.vid = vid;
		paramObj.mid = mid;
		paramObj.modelIds = mid;//车型ID，用于弹出框公共控制校验
		paramObj.timeType = timeType;//时间点
		paramObj.moduleName = moduleName;//用于弹出框公共控制校验
		
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
		
		queryCondition += "\n时间= " + $("#startDate").val();
		
		queryCondition += "\n城市 = " + $("#cityModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n车型 = " + $("#subModelModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n型号 = " + $("#versionModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		
		paramObj.queryCondition = queryCondition;
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
	$("#languageType").val(languageType);
	$("#exportFormId").submit();
}

/**
 * 车型弹出框
 * @param type
 */
function showSubModel(type)
{
	var inputType = "1";//控件默认单选
	var timeType = "1";
	var analysisDimensionType = $("#analysisDimensionType").val();//分析维度
	if("1" != analysisDimensionType)inputType = "2";
	if("2" == analysisDimensionType)timeType = "2";
	
	if('1' == type) getModelPage("tabs-competingProducts",type,inputType,timeType);
	else if('2' == type) getModelPage("tabs-segment",type,inputType,timeType);
	else if('3' == type) getModelPage("tabs-brand",type,inputType,timeType);
	else getModelPage("tabs-manf",type,inputType,timeType);
};