/**定义厂商LI全局变量*/
var currManfLI = null;

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

	/**
	 * 分析维度改变(城市,车型,型号,常用组)清空
	 */
	 $('#analysisDimensionType').on('change', function () {
		 $("#cityModalResultContainer").html("");
		 $("#subModelModalResultContainer").html("");
		 $("#versionModalResultContainer").html("");
		 $("#versionModalBody").html("");
		 $("#autoVersionModalResultContainer").html("");
		 $("#manfModalResultContainer").html("");
		 $("#chartTitleDiv").html("");//清空图表
		 $("#gridTbody").html("");//清空表格
		 //当为时间对比维度时时间变为时间段
		 if($("#analysisDimensionType").val()=="3"){
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
			    $('#endDate-container.input-append.date').datetimepicker({
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
			    $("#startDate-container input").val(defaultBeginDate);
			    $("#startDate-container").next().attr("style","display:in-line;");
			    $("#endDate-container").attr("style","display:in-line;");
		 } else{
			 $("#startDate-container input").val(endDate);
			 $("#startDate-container").next().attr("style","display:none;");
			 $("#endDate-container").attr("style","display:none;");
		 }
		 
		 
	});
	
	//时间改变事件
	$('#endDate-container.input-append.date').on('changeDate',function(){
		if($("#analysisDimensionType").val()=="3"){
			var beginDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			if(!beginDate || !endDate)
			{
				alert("请选择时间");
				return false;
			}
			if(parseInt(beginDate.replace("-","")) > parseInt(endDate.replace("-","")) )
			{
				alert("开始时间不能大于结束时间");
				return false;
			}
		}
		
		checkPopBoxData();
	});
	
    $('#startDate-container.input-append.date').on('changeDate',function(){
    	if($("#analysisDimensionType").val()=="3"){
    		var beginDate = $("#startDate").val();
    		var endDate = $("#endDate").val();
    		if(!beginDate || !endDate)
    		{
    			alert("请选择时间");
    			return;
    		}
    		if(parseInt(beginDate.replace("-","")) > parseInt(endDate.replace("-","")) )
    		{
    			alert("开始时间不能大于结束时间");
    			return;
    		}
    	}
    	checkPopBoxData();
    });
	
    /**
     * 展示城市弹出框
     */
	$('#cityModal').on('show', function () {
		
		showLoading("cityModalBody");
		$('#cityModalBody').load(ctx+"/policy/submodelProfitGlobal/getCityModal",getParams(),function(){
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
	
	/**
	 * 展示车型弹出框
	 */
	$('#subModelModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		
		//加载子车型数据
		showLoading("subModelModalBody");
		$('#subModelModalBody').load(ctx+"/policy/submodelProfitGlobal/getSubmodelModal",getParams(),function(){
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
	
	/*车型弹出框点击事件-开始*/
	//本竞品
	$("#tabs-competingProducts .subModelIdInput").live("click",function(){
		//把其他维度选中的取消
		$("#tabs-segment").find('.subModelIdInput:checked').each(function(){
			$(this).removeAttr('checked');
		});
		$("#tabs-brand").find('.subModelIdInput:checked').each(function(){
			$(this).removeAttr('checked');
		});
		$("#tabs-manf").find('.subModelIdInput:checked').each(function(){
			$(this).removeAttr('checked');
		});
		
		//显示选中的值	——开始
		//去掉重复选项
		$("#selectorResultContainerBySubModel").html();
		var allSubModelArr = [];
		$("#tabs-competingProducts").find('.subModelIdInput:checked').each(function(){
			var obj = {};
			obj.subModelId =  $(this).val();
			obj.subModelName =  $.trim($(this).parent().text());
			obj.letter =  $(this).attr("letter");
			obj.pooAttributeId = $(this).attr("pooAttributeId");
			allSubModelArr[allSubModelArr.length] = obj;
		});
		allSubModelArr = uniqueSubModel(allSubModelArr); 
		var strHTML = '<ul class="inline" >';
		for(var i=0;i<allSubModelArr.length;i++){
			strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
		  		strHTML += '<div class="removeBtnByResult label label-info" subModelId="'+allSubModelArr[i].subModelId+'"  pooAttributeId="'+allSubModelArr[i].pooAttributeId+'" letter="'+allSubModelArr[i].letter+'" subModelName="'+allSubModelArr[i].subModelName+'" style="cursor:pointer" title="删除：'+allSubModelArr[i].subModelName+'">';
			  		strHTML += '<i class="icon-remove icon-white"></i>'+allSubModelArr[i].subModelName;
		  		strHTML += '</div>';
		 		strHTML += '</li>';
		 }
		 strHTML += '</ul>';
		$("#selectorResultContainerBySubModel").html(strHTML);
		//显示选中的值	——结束
	});
	
	//细分市场
	$("#tabs-segment .subModelIdInput").live("click",function(){
		//把其他维度选中的取消
		$("#tabs-competingProducts").find('.subModelIdInput:checked').each(function(){
			$(this).removeAttr('checked');
		});
		$("#tabs-brand").find('.subModelIdInput:checked').each(function(){
			$(this).removeAttr('checked');
		});
		$("#tabs-manf").find('.subModelIdInput:checked').each(function(){
			$(this).removeAttr('checked');
		});
		//显示选中的值	——开始
		//去掉重复选项
		$("#selectorResultContainerBySubModel").html();
		var allSubModelArr = [];
		$("#tabs-segment").find('.subModelIdInput:checked').each(function(){
			var obj = {};
			obj.subModelId =  $(this).val();
			obj.subModelName =  $.trim($(this).parent().text());
			obj.letter =  $(this).attr("letter");
			obj.pooAttributeId = $(this).attr("pooAttributeId");
			allSubModelArr[allSubModelArr.length] = obj;
		});
		allSubModelArr = uniqueSubModel(allSubModelArr); 
		var strHTML = '<ul class="inline" >';
		for(var i=0;i<allSubModelArr.length;i++){
			strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
		  		strHTML += '<div class="removeBtnByResult label label-info" subModelId="'+allSubModelArr[i].subModelId+'"  pooAttributeId="'+allSubModelArr[i].pooAttributeId+'" letter="'+allSubModelArr[i].letter+'" subModelName="'+allSubModelArr[i].subModelName+'" style="cursor:pointer" title="删除：'+allSubModelArr[i].subModelName+'">';
			  		strHTML += '<i class="icon-remove icon-white"></i>'+allSubModelArr[i].subModelName;
		  		strHTML += '</div>';
		 		strHTML += '</li>';
		 }
		 strHTML += '</ul>';
		$("#selectorResultContainerBySubModel").html(strHTML);
		//显示选中的值	——结束
	});
	
	//品牌
	$("#tabs-brand .subModelIdInput").live("click",function(){
		//把其他维度选中的取消
		$("#tabs-competingProducts").find('.subModelIdInput:checked').each(function(){
			$(this).removeAttr('checked');
		});
		$("#tabs-segment").find('.subModelIdInput:checked').each(function(){
			$(this).removeAttr('checked');
		});
		$("#tabs-manf").find('.subModelIdInput:checked').each(function(){
			$(this).removeAttr('checked');
		});
		//显示选中的值	——开始
		//去掉重复选项
		$("#selectorResultContainerBySubModel").html();
		var allSubModelArr = [];
		$("#tabs-brand").find('.subModelIdInput:checked').each(function(){
			var obj = {};
			obj.subModelId =  $(this).val();
			obj.subModelName =  $.trim($(this).parent().text());
			obj.letter =  $(this).attr("letter");
			obj.pooAttributeId = $(this).attr("pooAttributeId");
			allSubModelArr[allSubModelArr.length] = obj;
		});
		allSubModelArr = uniqueSubModel(allSubModelArr); 
		var strHTML = '<ul class="inline" >';
		for(var i=0;i<allSubModelArr.length;i++){
			strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
		  		strHTML += '<div class="removeBtnByResult label label-info" subModelId="'+allSubModelArr[i].subModelId+'"  pooAttributeId="'+allSubModelArr[i].pooAttributeId+'" letter="'+allSubModelArr[i].letter+'" subModelName="'+allSubModelArr[i].subModelName+'" style="cursor:pointer" title="删除：'+allSubModelArr[i].subModelName+'">';
			  		strHTML += '<i class="icon-remove icon-white"></i>'+allSubModelArr[i].subModelName;
		  		strHTML += '</div>';
		 		strHTML += '</li>';
		 }
		 strHTML += '</ul>';
		$("#selectorResultContainerBySubModel").html(strHTML);
		//显示选中的值	——结束
	});
	
	//厂商
	$("#tabs-manf .subModelIdInput").live("click",function(){
		//把其他维度选中的取消
		$("#tabs-competingProducts").find('.subModelIdInput:checked').each(function(){
			$(this).removeAttr('checked');
		});
		$("#tabs-segment").find('.subModelIdInput:checked').each(function(){
			$(this).removeAttr('checked');
		});
		$("#tabs-brand").find('.subModelIdInput:checked').each(function(){
			$(this).removeAttr('checked');
		});
		//显示选中的值	——开始
		//去掉重复选项
		$("#selectorResultContainerBySubModel").html();
		var allSubModelArr = [];
		$("#tabs-manf").find('.subModelIdInput:checked').each(function(){
			var obj = {};
			obj.subModelId =  $(this).val();
			obj.subModelName =  $.trim($(this).parent().text());
			obj.letter =  $(this).attr("letter");
			obj.pooAttributeId = $(this).attr("pooAttributeId");
			allSubModelArr[allSubModelArr.length] = obj;
		});
		allSubModelArr = uniqueSubModel(allSubModelArr); 
		var strHTML = '<ul class="inline" >';
		for(var i=0;i<allSubModelArr.length;i++){
			strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
		  		strHTML += '<div class="removeBtnByResult label label-info" subModelId="'+allSubModelArr[i].subModelId+'"  pooAttributeId="'+allSubModelArr[i].pooAttributeId+'" letter="'+allSubModelArr[i].letter+'" subModelName="'+allSubModelArr[i].subModelName+'" style="cursor:pointer" title="删除：'+allSubModelArr[i].subModelName+'">';
			  		strHTML += '<i class="icon-remove icon-white"></i>'+allSubModelArr[i].subModelName;
		  		strHTML += '</div>';
		 		strHTML += '</li>';
		 }
		 strHTML += '</ul>';
		$("#selectorResultContainerBySubModel").html(strHTML);
		//显示选中的值	——结束
	});
	
	$('#versionModal').on('show', function (e) {	
		
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
		if(e.relatedTarget || 0 == $("#subModelModalResultContainer input[name='selectedSubModel']").length)  return; 
		//加载子车型下型号数据
		showLoading("versionModalBody");
		$('#versionModalBody').load(ctx+"/policy/submodelProfitGlobal/getVersionModalByCommon",getParams(),function(){
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
		$('#autoVersionModalBody').load(ctx+"/policy/submodelProfitGlobal/getAutoCustomGroup",getParams(),function(){
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
	
	/** 展示厂商弹出框-开始*/
	$("a[name ='manfSelector']").live('click',function(){
		currManfLI = $("div[name = 'objectType3']");
	});
	
	$('#manfModal').on('show', function (e) {
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
		if(e.relatedTarget)  return; 
		//加载生产商数据
		showLoading("manfModalBody");
		$('#manfModalBody').load(ctx+"/policy/submodelProfitGlobal/getManfModal",getParams(),function(){
			//弹出框设置默认选中项结果集		
			var strHTML = '<ul class="inline" >';
			$(currManfLI).find('.manfModalResultContainer input').each(function(){
				var id = $(this).val();
				var name = $(this).attr("manfName");
				var letter = $(this).attr("letter");
				strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
			  	strHTML += '<div class="removeBtnByResult label label-info" manfId="'+id+'"  letter="'+letter+'" manfName="'+name+'" style="cursor:pointer" title="删除：'+name+'">';
				strHTML += '<i class="icon-remove icon-white"></i>'+name;
			  	strHTML += '</div>';
			 	strHTML += '</li>';
			 	$(".manfModalContainer .manfIdInput").each(function(){
					$(this).attr("checked");//行全选
				});
		 		$(".manfModalContainer .manfIdInput").each(function(){
					if($(this).val() == id){
						$(this).attr("checked",'true');//行全选
					}
				});
			});
			strHTML += '</ul>';
			$("#selectorResultContainerByManf").html(strHTML);
		});
	});	
	
	$(".manfModalContainer").find('.manfIdInput').live('click',function(){
		//显示选中的值	——开始
		//去掉重复选项
		$("#selectorResultContainerByManf").html();
		var allObjArr = [];
		$(".manfModalContainer").find('.manfIdInput:checked').each(function(){
			var obj = {};
			obj.id =  $(this).val();
			obj.name =  $.trim($(this).parent().text());
			obj.letter =  $(this).attr("letter");
			allObjArr[allObjArr.length] = obj;
		});
		var strHTML = '<ul class="inline" >';
		for(var i=0;i<allObjArr.length;i++){
			strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
		  		strHTML += '<div class="removeBtnByResult label label-info" manfId="'+allObjArr[i].id+'"  letter="'+allObjArr[i].letter+'" manfName="'+allObjArr[i].name+'" style="cursor:pointer" title="删除：'+allObjArr[i].name+'">';
			  		strHTML += '<i class="icon-remove icon-white"></i>'+allObjArr[i].name;
		  		strHTML += '</div>';
		 		strHTML += '</li>';
		 }
		 strHTML += '</ul>';
		$("#selectorResultContainerByManf").html(strHTML);
		//显示选中的值	——结束
	});
	/** 展示厂商弹出框-结束*/
	
	/**
	 * 分析维度改变(城市,车型,型号,常用组)清空,且显示对象改变
	 */
	 $('#objectType').on('change', function () {
		 $("#gridTbody").html("");//清空查询结果
		 $("#chartTitleDiv").html("");//清空图表
	 	 var type =$('#objectType').val();
		 //$("#cityModalResultContainer").html("");
		 $("#subModelModalResultContainer").html("");
		 $("#versionModalResultContainer").html("");
		 $("#versionModalBody").html("");
		 $("#autoVersionModalResultContainer").html("");
		 $("#manfModalResultContainer").html("");
		 
		 //对象类型改变时弹出框跟着改变
		 $(".control-group[name^='objectType']").css("display","none");
		 if(type == 1){
		 	$(".control-group[name='objectType1']").css("display","block");
		 }else if(type == 2){
		 	$(".control-group[name='objectType1']").eq(0).css("display","block");
		 }else if(type == 3){
		 	$(".control-group[name='objectType3']").css("display","block");
		 }
	});
	
	
	
	/**
	 * 分析数据内容改变
	 */
	$('#analysisContentType').on('change', function () {
		 $('.queryConditionContainer').showLoading();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/policy/submodelProfit/resetDate",
			   data: {analysisContentType:$("#analysisContentType").val()},
			   dataType:'text',
			   success: function(data){
				   if(data)
				   {
						 //data = 默认开始时间 ，开始时间，结束时间
						 var defaultBeginDate = data.split(",")[0];
					     var beginDate = data.split(",")[1];
					     var endDate = data.split(",")[2];
						 var latestWeek = data.split(",")[3];
						 $('#latestWeek').val(latestWeek);
					     //喧染时间
					     $('#startDate-container').datetimepicker('setStartDate',beginDate);
					     $('#startDate-container').datetimepicker('setEndDate',endDate);
					     $('#endDate-container').datetimepicker('setStartDate',beginDate);
					     $('#endDate-container').datetimepicker('setEndDate',endDate);
					     //设置默认时间
					     $('#startDate-container').datetimepicker('update',defaultBeginDate);
					     $('#endDate-container').datetimepicker('update',endDate);
				   }
				   checkPopBoxData();
				   $('.queryConditionContainer').hideLoading();
			   },
			   error:function(){
				   $('.queryConditionContainer').hideLoading();
			   }
			});
	});
	
	/**
	 * 校验弹出框有效数据
	 */
	function checkPopBoxData()
	{	
		var paramsObj = getParams();
		if(paramsObj.objectType == 3){
			if(!paramsObj.manfs) return;
		}else{
			if(!paramsObj.vids && !paramsObj.mids) return;
		}
		$('.queryConditionContainer').showLoading();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/policy/submodelProfit/checkPopBoxData",
			   data: paramsObj,
			   dataType:'json',
			   success: function(data){
				   if(data)
				   {
					  var modelObj = $("#subModelModalResultContainer ul li");
					  var vidObj = $("#versionModalResultContainer ul li");
					  var manfObj = $("#manfModalResultContainer ul li");
					  for(var i = 0; i < data.length; i++)
					  {
						  var mid = data[i].subModelId;
						  var vids = data[i].versionList;
						  var manf = data[i].manfId;
						  
						  //遍历生产商
						  if(manfObj)
						  {
							  $.each(manfObj,function(i,n){
								  if(manf == $(n).find("input").val()) $(n).remove();
							  });
						  }
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
		var endDate = $("#endDate").val();
		var latestWeek = $("#latestWeek").val();
		var maxDate = $("#maxDate").val();
		var objectType = $("#objectType").val();
		var inputType = $("#analysisDimensionType").val();//控件是单选还是复选
		var analysisContentType = $("#analysisContentType").val();//数据指标分析类型
		//城市ID
		var citys = $("#cityModalResultContainer input[name='selectedCity']").map(function(){return $(this).val();}).get().join(",");
		//如果是全国均价，则取弹出枉里的城市ID
		var endDate_TPMix=endDate.substr(0,4)-1;
		
		var analysisDimensionType = $("#analysisDimensionType").val();
		
		var autoVidLength = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").length;
		if(autoVidLength > 0){
			var vids = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").map(function(){return $(this).val();}).get().join(",");
		}else{
			var vids = $("#versionModalResultContainer input[name='selectedVersion']").map(function(){return $(this).val();}).get().join(",");
		}
		var mids = $("#subModelModalResultContainer input[name='selectedSubModel']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
		var manfs = $("#manfModalResultContainer input[name='selectedManf']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
		var paramObj = {};
		paramObj.beginDate = beginDate;
		paramObj.endDate = endDate;
		paramObj.modelIds = mids;
		paramObj.inputType = inputType;
		paramObj.citys = citys;
		paramObj.vids = vids;
		paramObj.mids = mids;
		paramObj.manfs = manfs;
		paramObj.maxDate = maxDate;
		paramObj.latestWeek = latestWeek;
		paramObj.objectType = objectType;
		paramObj.analysisContentType = analysisContentType;
		paramObj.timeType = "2";
		paramObj.analysisDimensionType = analysisDimensionType;
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
		
		/*queryCondition += "数据指标 = ";
		if("1" == $("#analysisContentType").val()) queryCondition += "利润";
		if("2" == $("#analysisContentType").val()) queryCondition += "折扣";
		if("3" == $("#analysisContentType").val()) queryCondition += "成交价";*/
		
		queryCondition += "\n分析维度 = ";
		if("1" == $("#analysisDimensionType").val() ) queryCondition += "对象对比";
		if("2" == $("#analysisDimensionType").val() ) queryCondition += "城市对比";
		if("3" == $("#analysisDimensionType").val() ) queryCondition += "时间对比";
		
		queryCondition += "\n对象类型 = ";
		if("1" == $("#objectType").val() ) queryCondition += "型号";
		if("2" == $("#objectType").val() ) queryCondition += "车型";
		if("3" == $("#objectType").val() ) queryCondition += "厂商品牌";
		
		queryCondition += "\n时间 = " + $("#startDate").val() + "至" + $("#endDate").val();
		
		queryCondition += "\n城市 = " + $("#cityModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n车型 = " + $("#subModelModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n型号 = " + $("#versionModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n自定义型号组 = " + $("#autoVersionModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n生产商 = " + $("#manfModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		paramObj.queryCondition = queryCondition;
	}
	

	
	/**
	 * 画型号表格
	 */
	function showGrid(grid,objectType)
	{
		//alert(grid[0].versionLastMonthSales)
		if(objectType != 1){
			$("#gridTheadByVersion").addClass("hide");
			if (objectType == 2) {
				$("#gridTheadByModel").removeClass("hide");
				$("#gridTheadByManf").addClass("hide");
			} else {
				$("#gridTheadByModel").addClass("hide");
				$("#gridTheadByManf").removeClass("hide");
			}
		}else{
			$("#gridTheadByVersion").removeClass("hide");
			$("#gridTheadByModel").addClass("hide");
			$("#gridTheadByManf").addClass("hide");
		}
		var tbodyHtml = "";
		if(grid)
		{
			for(var i = 0; i < grid.length; i++)
			{
				var dataObj = grid[i];
				var className = "";
				if(0 == i % 2) className = "odd";
				
				//如果无数据则不显示
				if(objectType==1){
					if(!dataObj.versionCode) continue;
				} else {
					if(!dataObj.objName) continue;
				}
				tbodyHtml += "<tr>";
				var yearMonth = "";
					yearMonth = dataObj.yearMonth;
				tbodyHtml += "<td style='width:8%' class='"+className+"'>" + yearMonth + "</td>";
				//如果是城市对比并且不是全国均价
				if("2" == $("#analysisDimensionType").val() && $("#cityModalResultContainer input[name='selectedCity']").val() != 0)
				{
					tbodyHtml += "<td style='width:6%' class='"+className+"'>" + dataObj.cityName + "</td>";
					if(objectType==1){
						$("#versionCityTh").removeClass("hide");
					} else if(objectType==2){
						$("#modelCityTh").removeClass("hide");
					} else{
						$("#manfCityTh").removeClass("hide");
					}
					
					//如果是城市利润时保存型号英文名称
				} else{
					if(objectType==1){
						$("#versionCityTh").addClass("hide");
					} else if(objectType==2){
						$("#modelCityTh").addClass("hide");
					} else{
						$("#manfCityTh").addClass("hide");
					}
				}
				if(objectType==1){
					tbodyHtml += "<td style='width:10%' class='"+className+"'>" + dataObj.versionName + "</td>";
				} else{
					tbodyHtml += "<td style='width:10%' class='"+className+"'>" + dataObj.objName + "</td>";
				} 
				
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatDataNew(formatData(dataObj.margin)) + "</td>";
				var grossSupports=null;
				if(!dataObj.c2&&!dataObj.c3){
					grossSupports="-";
				} else{
					grossSupports = formatDataNew(formatData(dataObj.grossSupports));
				}
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + grossSupports + "</td>";
				var customerIncentive=null;
				if(!dataObj.c8&&!dataObj.c7&&!dataObj.c4&&!dataObj.c5&&!dataObj.c6&&!dataObj.maintenance){
					customerIncentive="-";
				} else{
					customerIncentive=formatDataNew(formatData(dataObj.customerIncentive));
				}
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + customerIncentive + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatDataNew(formatData(dataObj.bonus)) + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatDataNew(formatData(dataObj.invoicePrice)) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatDataNew(formatData(dataObj.profit)) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatDataNew(formatData(dataObj.grossInvoicePrice)) + "</td>";
				tbodyHtml += "</tr>";
			}
			$("#gridTbody").html(tbodyHtml);
		}
	}
	
	
	
	/**
	 * 获取折扣
	 */
	function formatDataNew(data)
	{
		if(data)
		{
			if("-" != data )
			{
				return  data ;
			}
		}
		return "-";
	}
	
	
	/**
	 * 参数验证
	 */
	function paramsValidate()
	{
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var objectType = $("#objectType").val();
		//城市ID
		var cityLength = $("#cityModalResultContainer input[name='selectedCity']").length;
		var vidLength = $("#versionModalResultContainer input[name='selectedVersion']").length;
		var modelLength = $("#subModelModalResultContainer :input[name='selectedSubModel']").length;
		var autoVidLength = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").length;
		var manfLength = $("#manfModalResultContainer .selectorResultContainer li").length;
		if(!beginDate || !endDate)
		{
			alert("请选择时间");
			return true;
		}
		
		if($("#analysisDimensionType").val()==3){
			if(parseInt(beginDate.replace("-","")) > parseInt(endDate.replace("-","")) )
			{
				alert("开始时间不能大于结束时间");
				return true;
			}
		}
		
		if(0 == cityLength)
		{
			alert("请选择城市");
			return true;
		}
		//判断对象类型型号时必须选择车型,型号组合或自定义型号组
		if(objectType == 1){
			if(0 == modelLength && 0 == autoVidLength)
			{
				alert("请选择车型");
				return true;
			}
			if(0 == vidLength && 0 == autoVidLength)
			{
				alert("请选择型号");
				return true;
			}	
		}else if(objectType == 2){
			//对象类型为车型时必须选择车型
			if(0 == modelLength)
			{
				alert("请选择车型");
				return true;
			}
		}else{
			//对象类型为厂商时必须选择生产商
			if(0 == manfLength){
				alert("请选择厂商");
				return true;
			}
		}
		
		
		return false;
	}
	
	$('#queryBtn').on('click', function (e) {
		if(paramsValidate()) return;
		var params = getParams();
		
		$('body').showLoading();
		//默认展示图表
		$("#showChart").click();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/policy/submodelProfit/loadModelProfitChartAndTable",
			   data: params,
			   success: function(data){
				   if(data)
				   {
				     //查询面板折叠
				     $(".queryConditionContainer .buttons .toggle a").click();
				     
				     $("#chartTitleDiv").html("");
					 $("#gridTbody").html("");
				    var flag = false;
				     //当全部为空的时候就不显示图
					 for(var i=0; i<data.grid.length;i++){
						if( data.grid[i].msrp != "-"  || data.grid[i].tp != "-" ){
							flag=true;
							break;
						}
					 }
					 if(flag){
						 $("#chartTitleDiv").height(400);
						 showChart(data.grid,params.objectType);//画图表
						 showGrid(data.grid,params.objectType);//画数据表格
						 $("#tSortable th").css("text-align","center");//数据表头居中显示
						 $("#gridTbody td").css("text-align","center");//数据表居中显示
					 } else{
						 //没有数据
						 $("#chartTitleDiv").html("");
						 $("#gridTbody").html("");
						 $("#chartTitleDiv").height(100);
						 $("#chartTitleDiv").html("<h2 Style='color:black'>NO DATA!<h2>")
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
		//常用选项容器
		$('#autoVersionModalResultContainer').html("");
		//生产商容器
		$('#manfModalResultContainer').html("");
		
		$(".control-group[name^='objectType']").css("display","none");
		$(".control-group[name='objectType1']").css("display","block");
		
		$('#formId :reset');	
		//去掉图表
		$("#chartTitleDiv").html("");
		$("#gridTbody").html("");
		 $("#chartTitleDiv").height(100);
		//隐藏结束时间模块
		 $("#startDate-container").next().attr("style","display:none;");
		 $("#endDate-container").attr("style","display:none;");
	});
});

function showChart(json,objectType){
	var myChart = echarts.init(document.getElementById("chartTitleDiv"));
	//对象集
	var arrObject = new Array();
	for(var i = 0;i < json.length;i++){
		var value = null;
		if($("#analysisDimensionType").val() == 1){
			//型号
			if($("#objectType").val() == 1){
				value = "指导价成交价\n"+json[i].submodelName+" "+json[i].versionShortName;
				//车型和厂商品牌
			} else{
				value = "指导价成交价\n"+json[i].objName;
			}
			
		} else if($("#analysisDimensionType").val() == 2 ){
			//全国均价
			if($("#cityModalResultContainer input[name='selectedCity']").val() == 0){
				if($("#objectType").val() == 1){
				value = "指导价成交价\n全国均价";
				} else{
					value = "指导价成交价\n全国均价";
				}
			} else{
				value = "指导价成交价\n"+json[i].cityName;
			}
		} else{
			value = "指导价成交价\n"+json[i].yearMonth;
		}
			arrObject.push(value);
	} 
	
	/*数值开始*/
	//y轴最大值坐标
	var max = 0;
	for(var i = 0;i < json.length;i++){
		var max2 = 0;
		if(Number(formatData2(json[i].msrp)) >= Number(formatData2(json[i].tp))){
			max2 = Number(formatData2(json[i].msrp))
		} else{
			max2 = Number(formatData2(json[i].tp))
		}
		for(var i = 1;i < json.length;i++){
			var max3 = 0;
			if(Number(formatData2(json[i].msrp)) >= Number(formatData2(json[i].tp))){
				    max3 = Number(formatData2(json[i].msrp))
			} else{
				max3 = Number(formatData2(json[i].tp))
			}
			if(max2 >= max3){
				max2 = max2
			} else{
				max2 = max3
			}
		}
		if(max >= max2){
			max = Number(max)/1.5
		} else{
			max = Number(max2)/1.5
		}
	}
	
	//y轴最小值坐标
	/*var min = Number(json[0].profit);
	for(var i = 1; i < json.length; i++){
		if(min >= Number(json[i].profit)){
			min = Number(json[i].profit)
		} else{
			min = min
		}
	}*/
	//指导价
	var arrMsrp = new Array();
	for(var i = 0;i < json.length;i++){
		//if(!json[i].versionCode) continue;
		var value = null;
		if(!json[i].margin&&!json[i].c2&&!json[i].c3&&!json[i].c8&&!json[i].c7&&!json[i].maintenance&&!json[i].c4&&!json[i].c5&&!json[i].c6&&!json[i].bonus&&!json[i].invoicePrice){
			value = null;
		} else{
			value = Number(json[i].msrp)/2;
			if("-" == json[i].msrp){
				value = null;
			}
		}
		arrMsrp.push(value);
	}
	var arrMsrpNew = new Array();
	for(var i=0;i<arrMsrp.length;i++){
		var value = arrMsrp[i];
		var value2 = Number(json[i].msrp);
		if("-" == json[i].msrp){
			value = null;
			value2 = null;
	}
			 value={
						value:value,label:{normal:{show:true,formatter:formatNum(value2),color:'black',textStyle:{color:'black'},position:'insideBottom'}}
					}
		arrMsrpNew.push(value);
	}
	//返利
	var arrMargin = new Array();
	for(var i = 0;i < json.length;i++){
		var value = null;
		/*if(1==objectType&&!json[i].versionCode){
			value = null;
		} else{*/
			value = Number(json[i].margin);
			if(""==json[i].margin){
				value = null;
			}
		/*}*/
		arrMargin.push(value);
	}
	//经销商支持
	var arrGrossSupports = new Array();
	for(var i = 0;i < json.length;i++){
		var value = null;
		if(!json[i].c2&&!json[i].c3){
			value = null;
		} else{
			value = Number(json[i].grossSupports);
			if("" == json[i].grossSupports){
				value=null;
			}
		}
		arrGrossSupports.push(value);
	}
	//用户激励
	var arrCustomerIncentive = new Array();
	for(var i = 0;i < json.length;i++){
		var value = null;
		if(!json[i].c8&&!json[i].c7&&!json[i].maintenance&&!json[i].c4&&!json[i].c5&&!json[i].c6){
			value = null;
		} else{
			value = Number(json[i].customerIncentive);
			if("" == json[i].customerIncentive){
				value = null;
			}
		}
		arrCustomerIncentive.push(value);
	}
	//奖励
	var arrBonus = new Array();
	for(var i = 0;i < json.length;i++){
		var value = null;
		/*if(1==objectType&&!json[i].versionCode){
			value = null;
		} else{*/
			value = Number(json[i].bonus);
			if("" == json[i].bonus){
				value = null;
			}
		/*}*/
		arrBonus.push(value);
	}
	//开票价
	var arrInvoicePrice = new Array();
	for(var i = 0;i < json.length;i++){
		var value = null;
			value = Number(json[i].invoicePrice)/2;
			var value2 = Number(json[i].invoicePrice);
			if("" == json[i].invoicePrice){
				value=null;
				value2=null;
			}
			value={
					value:value,label:{normal:{show:true,formatter:formatNum(value2),textStyle:{color:'white'},position:'inside'}}
			};
		arrInvoicePrice.push(value);
	}
	//成交价
	var arrTp = new Array();
	for(var i = 0;i < json.length;i++){
		var value = null;
		var value2 = Number(json[i].tp);
			value = Number(json[i].tp)/2;
			if("-" == json[i].tp){
				value = null;
				value2 = null;
			}
			if(!json[i].profit&&!json[i].grossInvoicePrice){
				value=null;
			} else{
				value={
						value:value,label:{normal:{show:true,formatter:formatNum(value2),textStyle:{color:'black'},position:'insideBottom'}}
				};
			}
		arrTp.push(value);
	}
	//利润
	var arrProfit = new Array();
	for(var i = 0;i < json.length;i++){
		var value = null;
		/*if(1==objectType&&!json[i].versionCode){
			value = null;
		} else{*/
			value=Number(json[i].profit);
			if("" == json[i].profit){
				value = null;
			}
		/*}*/
		arrProfit.push(value);
	}
	
	var profitColor='green';//利润的颜色，当是正的为绿色，负的为红色
	
	var arrProfitNew = new Array();
	for(var i=0;i<arrProfit.length;i++){
		var value=null;
		var value2=null;
		if(Number(arrProfit[i])<0){
			value=Number(-arrProfit[i]);
			value2=arrProfit[i].toString();
			profitColor='#F00000';
		} else{
			if(null == arrProfit[i]){
				value = null;
			} else{
				value = Number(arrProfit[i])
				value2 = arrProfit[i];
				profitColor='green';
			}
		}
		 value={
				absvalue:arrProfit[i],//临时数据值用来判断tooltip中的利润值
				value:value,itemStyle:{
					normal:{color:profitColor}},label:{normal:{show:true,formatter:formatNum(value2),textStyle:{color:'black'},position:'inside'}}
		};
		arrProfitNew.push(value);
	}
	//经销商成本
	var arrGrossInvoicePrice = new Array();
	for(var i = 0;i < json.length;i++){
		var value = null;
			value = Number(json[i].grossInvoicePrice)/2;
			var value2 = Number(json[i].grossInvoicePrice);
			if("" == json[i].grossInvoicePrice){
				value = null;
				value2 = null;
			}
			value={
					value:value,label:{normal:{show:true,formatter:formatNum(value2),textStyle:{color:'white'},position:'inside'}}
			};
		arrGrossInvoicePrice.push(value);
	}
	/*数值结束*/
	var  barWidth=null;//柱子的宽度
	var barMaxWidth=50;//柱子的最大宽度
	var barMinHeight=5;//柱子的最小高度
	var barGap='0%';//两柱子间的距离
	var profitLegendColor ='green';//利润图例的颜色，全是负时为红色，只要有一个为正时就是绿色,全是空也为绿色
	for(var i=0; i<json.length;i++){
		if(Number(json[i].profit)<0){
			profitLegendColor = '#F00000';
		}
		if(Number(json[i].profit)>0){
			profitLegendColor = 'green';
			break;
		}
	}
	var option = {
		    tooltip : {
		    	 trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'line'        // 默认为直线，可选为：'line' | 'shadow'
		        },
		        textStyle:{
               		 fontSize: 12
                },
		        formatter:function(params){
		        	var str = "<div style=font-family:'sans-serif';>";
		        	var value = null;
		        	str +="<strong>"+params[0].name.substr(6,params[0].name.length)+"</strong>";

		        	value = params[7].data.absvalue;//直接取真正利润值
		        	
		        	var msrp=null;
		        	if(params[5].value==null){
		        		msrp=null;
		        	} else{
		        		msrp=params[5].value*2
		        	}
		        	var invoicePrice=null;
		        	if(params[0].value==null){
		        		invoicePrice=null;
		        	} else{
		        		invoicePrice=params[0].value*2;
		        	}
		        	var tp=null;
		        	if(params[8].value==null){
		        		tp=null;
		        	} else{
		        		tp=params[8].value*2;
		        	}
		        	if(params[6].value==null){
		        		grossInvoicePrice=null;
		        	} else{
		        		grossInvoicePrice=params[6].value*2;
		        	}
		        	str +="<br/>"+params[5].seriesName+":"+formatNum(msrp);
		        	str +="<br/>"+params[4].seriesName+":"+formatNum(params[4].value);
		        	str +="<br/>"+params[3].seriesName+":"+formatNum(params[3].value);
		        	str +="<br/>"+params[2].seriesName+":"+formatNum(params[2].value);
		        	str +="<br/>"+params[1].seriesName+":"+formatNum(params[1].value);
		        	str +="<br/>"+params[0].seriesName+":"+formatNum(invoicePrice);
		        	str +="<br/>"+params[8].seriesName+":"+formatNum(tp);
		        	str +="<br/>"+params[7].seriesName+":"+value;
		        	str +="<br/>"+params[6].seriesName+":"+formatNum(grossInvoicePrice);
		        	str +="</div>";
		        	return str;
		        }
		    },
		    legend: {
		    	 backGroudColor:'#ffff',
		    	 selectedMode:false, //图例点击事件关闭
		        data:['返利','经销商支持','用户激励','考核奖励','开票价','利润','经销商成本']
		    },
		    /*dataZoom: {//图例滚动条
		        show: true,
		        start : 0,
		        end : 70
		    },*/
		   /* grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },*/
		    xAxis : [
		             {
		                 type : 'category',
		                 axisLabel:{
		                 interval: 0,//标签设置为全部显示
		                 rotate:0,
		                 textStyle:{
		                	 color:'black',
		                	 fontWeight:'normal'
		                 }
		                /* formatter:function(params){
		                  var newParamsName = "";// 最终拼接成的字符串
		         var paramsNameNumber = params.length;// 实际标签的个数
		         var provideNumber = 6;// 每行能显示的字的个数
		         var rowNumber = Math.ceil(paramsNameNumber / provideNumber);// 换行的话，需要显示几行，向上取整
		         *//**
		          * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
		          *//*
		         // 条件等同于rowNumber>1
		         if (paramsNameNumber > provideNumber) {
		             *//** 循环每一行,p表示行 *//*
		             for (var p = 0; p < rowNumber; p++) {
		                 var tempStr = "";// 表示每一次截取的字符串
		                 var start = p * provideNumber;// 开始截取的位置
		                 var end = start + provideNumber;// 结束截取的位置
		                 // 此处特殊处理最后一行的索引值
		                 if (p == rowNumber - 1) {
		                     // 最后一次不换行
		                     tempStr = params.substring(start, paramsNameNumber);
		                 } else {
		                     // 每一次拼接字符串并换行
		                     tempStr = params.substring(start, end) + "\n";
		                 }
		                 newParamsName += tempStr;// 最终拼成的字符串
		             }

		         } else {
		             // 将旧标签的值赋给新标签
		             newParamsName = params;
		         }
		         //将最终的字符串返回
		         return newParamsName
		                 }*/},
		                 /*splitArea:{
		                	show:false ,
		                	areaStyle:{
		                		color:'black'
		                	}
		                 },*/
		                 
		                 data : arrObject
		             }
		         ],
		    yAxis : [
		        {
		            type : 'value',
		            max:max,
		            //min:min,
		            show: false
		        }
		    ],
		    backgroundColor:'#FFFFFF',
		    series : [
		              {
                name: "开票价",
                type:'bar',
                data: arrInvoicePrice,
                stack: "msrp",
                label:{
	                     normal:{
	                         show:true,
	                         textStyle:{
	                        	 color:'white'//数据显示的颜色
	                         },
	                         formatter: function(p){
	                        	 return formatNum(p.value);
	                         },
	                         position:'inside'//数据在柱子里面居中显示
	                     }
	                 },
	                 itemStyle:{
	                	 normal:{
	                		 color:'#00235A',//柱子的颜色
	                		 borderColor:'white',
	                		 borderWith:1
	                		 
	                	 }
	                 },
	                 barWidth:barWidth,
	                 barMaxWidth:barMaxWidth,
	                 barMinHeight:barMinHeight,
	                 barGap:barGap
            },  {
                name: "考核奖励",
                type:'bar',
                data: arrBonus,
                stack: "msrp",
                label:{
	                     normal:{
	                         show:true,
	                         textStyle:{
	                        	 color:'black'
	                         },
	                         position:'inside',
	                         formatter: function(p){
	                        	 return formatNum(p.value);
	                         }
	                     }
	                 },
	                 itemStyle:{
	                	 normal:{
	                		 color:'#8994A0',//柱子的颜色
	                		 borderColor:'white',
	                		 borderWith:1
	                	 }
	                 },
	                 barWidth:barWidth,
	                 barMaxWidth:barMaxWidth,
	                 barMinHeight:barMinHeight,
	                 barGap:barGap
            },{
                name: "用户激励",
                type:'bar',
                data: arrCustomerIncentive,
                stack: "msrp",
                label:{
	                     normal:{
	                         show:true,
	                         textStyle:{
	                        	 color:'black'
	                         },
	                         position:'inside',
	                         formatter: function(p){
	                        	 return formatNum(p.value);
	                        	 }
	                     }
	                 },
	                 itemStyle:{
	                	 normal:{
	                		 color:'#F9A700',//柱子的颜色
	                		 borderColor:'white',
	                		 borderWith:1
	                	 }
	                 },
	                 barWidth:barWidth,
	                 barMaxWidth:barMaxWidth,
	                 barMinHeight:barMinHeight,
	                 barGap:barGap
            },{
                name: "经销商支持",
                type:'bar',
                data: arrGrossSupports,
                stack: "msrp",
                label:{
	                     normal:{
	                         show:true,
	                         textStyle:{
	                        	 color:'black'
	                         },
	                         position:'inside',
	                         formatter: function(p){
	                        	 return formatNum(p.value);
	                        	 }
	                     }
	                 },
	                 itemStyle:{
	                	 normal:{
	                		 color:'#E26700',//柱子的颜色
	                		 borderColor:'white',
	                		 borderWith:1
	                	 }
	                 },
	                 barWidth:barWidth,
	                 barMaxWidth:barMaxWidth,
	                 barMinHeight:barMinHeight,
	                 barGap:barGap
            },{
		                  name: "返利",
		                  type:'bar',
		                  data: arrMargin,
		                  stack: "msrp",
		                  label:{
			                     normal:{
			                         show:true,
			                         textStyle:{
			                        	 color:'black'
			                         },
			                         position:'inside',
			                         formatter: function(p){
			                        	 return formatNum(p.value);
			                        	 }
			                     }
			                 },
			                 itemStyle:{
			                	 normal:{
			                		 color:'#62C5E2',//柱子的颜色
			                		 borderColor:'white',
			                		 borderWith:1
			                	 }
			                 },
			                 barWidth:barWidth,
			                 barMaxWidth:barMaxWidth,
			                 barMinHeight:barMinHeight,
			                 barGap:barGap
		              }, {
			             	 name:"指导价",
			             	 type:'bar',
			             	 data: arrMsrpNew ,
			             	 stack:"msrp",
			             	 label:{
			                     normal:{
			                         show:true,
			                         textStyle:{
			                        	 color:'black'
			                         },
			                         position:'insideBottom',
			                         formatter: function(p){
			                        	 return formatNum(p.value);
			                        	 }
			                     }
			                 },
			                 itemStyle:{
			                	 normal:{
			                		 color:'#FFFFFF'
			                	 }
			                 } ,
			                 barWidth:barWidth,
			                 barMaxWidth:barMaxWidth,
			                 barMinHeight:barMinHeight,
			                 barGap:barGap
			              }, {
		                  name: "经销商成本",
		                  type:'bar',
		                  data: arrGrossInvoicePrice,
		                  stack: "tp",
		                  label:{
			                     normal:{
			                         show:true,
			                         textStyle:{
			                        	 color:'white'
			                         },
			                         position:'inside',
			                         formatter: function(p){
			                        	 return formatNum(p.value);
			                        	 }
			                     }
			                 },
			                 itemStyle:{
			                	 normal:{
			                		 color:'#4682B4',//柱子的颜色
			                		 borderColor:'white',
			                		 borderWith:1
			                	 }
			                 },
			                 barWidth:barWidth,
			                 barMaxWidth:barMaxWidth,
			                 barMinHeight:barMinHeight,
			                 barGap:barGap
		              },{
		                  name: "利润",
		                  type:'bar',
		                  data: arrProfitNew,
		                  stack: "tp",
		                  label:{
			                     normal:{
			                         show:true,
			                         textStyle:{
			                        	 color:'black'
			                         },
			                         position:'inside',
			                         formatter: function(p){
			                        	 return formatNum(p.value);
			                        	 }
			                     }
			                 },
			                 itemStyle:{
			                	 normal:{
			                		 color:profitLegendColor,//柱子的颜色
			                		 borderColor:'white',
			                		 borderWith:1
			                	 }
			                 },
			                 barWidth:barWidth,
			                 barMaxWidth:barMaxWidth,
			                 barMinHeight:barMinHeight,
			                 barGap:barGap
		              },{
		                  name: "成交价",
		                  type:'bar',
		                  data: arrTp,
		                  stack: "tp",
		                  label:{
			                     normal:{
			                         show:true,
			                         textStyle:{
			                        	 color:'black'
			                         },
			                         position:'insideBottom',
			                         formatter: function(p){
			                        	 return formatNum(p.value);
			                        	 }
			                     }
			                 },
			                 itemStyle:{
			                	 normal:{
			                	 color:'#FFFFFF'//背景颜色
			                	 }
			                 },
			                 barWidth:barWidth,
			                 barMaxWidth:barMaxWidth,
			                 barMinHeight:barMinHeight,
			                 barGap:barGap
		              }
		    ]
		};
	myChart.clear();
	//myCharts.resize();
	//console.log(JSON.stringify(option));
	myChart.setOption(option);
}
/**
 * 格式化千分位
 * @param strNum
 * @returns
 */
function formatNum(strNum) {
	if(null==strNum){
		return null;
	}
	if (strNum.length <= 3) {
	return strNum;
	}
	if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(strNum)) {
	return strNum;
	}
	var a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
	var re = new RegExp();
	re.compile("(\\d)(\\d{3})(,|$)");
	while (re.test(b)) {
	b = b.replace(re, "$1,$2$3");
	}
	return a + "" + b + "" + c;
	}

/**
 * 把指导价和成交价的特殊数据格式化为0
 * @param data
 * @returns {Number}
 */
function formatData2(data){
	if(data=="-"){
		data=0;
	} else{
		data=data;
	}
	return data;
}

/**
 * 导出Excel
 * @param languageType EN:英文版;ZH:中文版
 */
function exportExcel(languageType) {
	if(!$.trim($("#gridTbody").html())) {
		alert("暂无数据导出");
		return;
	}
	var citys = $("#cityModalResultContainer input[name='selectedCity']").map(function(){return $(this).val();}).get().join(",");
	$("#ex_beginDate").val($("#startDate").val());
	$("#ex_endDate").val($("#endDate").val());
	$("#ex_maxDate").val($("#maxDate").val());
	$("#ex_objectType").val($("#objectType").val());
	$("#ex_citys").val(citys);
	//如果是全国均价，则取弹出枉里的城市ID
	var endDate_TPMix=endDate.substr(0,4)-1;
	$("#ex_citys").val(citys);
	$("#ex_mids").val($("#subModelModalResultContainer input[name='selectedSubModel']").map(function(){return "'"+$(this).val()+"'";}).get().join(","));
	$("#ex_manfs").val($("#manfModalResultContainer input[name='selectedManf']").map(function(){return "'"+$(this).val()+"'";}).get().join(","));
	$("#languageType").val(languageType);
	$("#objType").val($("#objectType").val());
	$("#analysisType").val($("#analysisDimensionType").val());
	$("#analysisType2").val($("#analysisDimensionType").val());
	$("#dataIndexType").val($("#analysisContentType").val());
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
	var inputType = $("#analysisDimensionType").val();
	
	if('1' == type) getFawvwModelPage("tabs-competingProducts",type,inputType,"2");
	else if('2' == type) getFawvwModelPage("tabs-segment",type,inputType,"2");
	else if('3' == type) getFawvwModelPage("tabs-brand",type,inputType,"2");
	else getFawvwModelPage("tabs-manf",type,inputType,"2");
};

/**
 * 展示页面
 * @param id 展示容器ID
 * @param type 子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
 * @param inputType 控件类型1：复选，2：单选;默认为1
 * @param timeType 时间类型1：时间点;2：时间段默认为2
 * 
 */
function getFawvwModelPage(id,type,inputType,timeType)
{
	//如果内容不为空则触发请求
	if(!$.trim($('#' + id).html())){
		
		//获取时间
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var hatchbackId = "";
		
		//数据分析类型如果没有默认为成交价
		var analysisContentType = $("#analysisContentType").val();
		if(!analysisContentType) analysisContentType = "3";
		
		//如果数据分析类型是销量校验
		if("4" == analysisContentType)
		{
			beginDate = $("#dateYear .dateLIContainer").eq(0).find(".startDate-container input").val().replace("-","");
			endDate = $("#dateYear .dateLIContainer").eq(0).find(".endDate-container input").val().replace("-","");
			hatchbackId = $("#model .subModelLIContainer").eq($("#getModelIndexId").val()).find("td:eq(1)").find("li div :input").map(function(){return $(this).val();}).get().join(",");
		}
		
		//传递参数
		var params = {inputType:inputType,subModelShowType:type,beginDate:beginDate,endDate:endDate,analysisContentType:analysisContentType,timeType:timeType,hatchbackId:hatchbackId};
		//触发请求
		showLoading(id);
		$('#' + id).load(ctx+"/policy/submodelProfitGlobal/getSubmodelModal",params,function(){
			
			$('#selectorResultContainerBySubModel .removeBtnByResult').each(function(){
				var subModelId = $(this).attr("subModelId");
				$(".subModelModalContainer .subModelIdInput").each(function(){
					
					if($(this).val() == subModelId) $(this).attr("checked",'true');//行全选
				});
			});
			
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
};
