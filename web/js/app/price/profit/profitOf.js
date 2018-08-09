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
    
	//时间改变事件
	$('#endDate-container.input-append.date').on('changeDate',function(){
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
		checkPopBoxData();
	});
	
    $('#startDate-container.input-append.date').on('changeDate',function(){
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
    	checkPopBoxData();
    });
	
    /**
     * 展示城市弹出框
     */
	$('#cityModal').on('show', function () {
		showLoading("cityModalBody");
		$('#cityModalBody').load(ctx+"/price/global/getCityModal",getParams(),function(){
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
	$("#subModelModal").on("show", function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		
		//加载子车型数据
		showLoading("subModelModalBody");
		$("#subModelModalBody").load(ctx+"/price/global/getSubmodelModal",getParams(),function(){
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
						$(this).attr("checked", "true");//行全选
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
		$('#versionModalBody').load(ctx+"/price/global/getVersionModalByCommon",getParams(),function(){
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
		$('#autoVersionModalBody').load(ctx+"/price/global/getAutoCustomGroup",getParams(),function(){
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
		$('#manfModalBody').load(ctx+"/price/global/getManfModal",getParams(),function(){
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
	 * 分析维度改变
	 * $('#analysisDimensionType').on('change', function () {
		 $("#cityModalResultContainer").html("");
		 //如果是城市对比则不需要校验
		 if("2" == $("#analysisDimensionType").val())
		 {
			 $("#subModelModalResultContainer").html("");
			 $("#versionModalResultContainer").html("");
			 $("#versionModalBody").html("");
			 $("#autoVersionModalResultContainer").html("");
			 return;
		 }
		 checkPopBoxData();
	});
	 */
	
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
		 
		 //数据指标为成交价,折扣时对象对比和城市对比数据有效日期变更
		 /**if("1" != $("#analysisContentType").val()){
		 	var latestWeek = $("#latestWeek").val()
		 	if("2" == $("#analysisDimensionType").val()){
				var endDate = new Date(latestWeek); 
				endDate.setMonth(endDate.getMonth()-1);
				var year = endDate.getFullYear();
				var month = endDate.getMonth() + 1;
				var endDateStr = year + "-" + month;
				$('#endDate-container').datetimepicker('setEndDate',endDateStr);
		    	$('#endDate-container').datetimepicker('update',endDateStr);
			}else{
				$('#endDate-container').datetimepicker('setEndDate',latestWeek);
		    	$('#endDate-container').datetimepicker('update',latestWeek);
			}
		 }**/
		 
	});
	
	/**
	 * 分析维度改变(城市,车型,型号,常用组)清空,且显示对象改变
	 */
	 $('#objectType').on('change', function () {
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
			   url: ctx+"/resetDate",
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
			   url: ctx+"/checkPopBoxData",
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
		if(0 == citys){
			if(endDate_TPMix>2013){
				citys = "8,17,28,3,18,7,12,15,31,2,5,11,23,1,14,16,20,32,4,13,21,22,29,19,30,41,42,48,60,61,47";
			}else{
				citys = "8,17,28,3,18,7,12,15,31,2,5,11,23,1,14,16,20,32,4,13,21,22,29,47";
			}
		}
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
		
		queryCondition += "数据指标 = ";
		if("1" == $("#analysisContentType").val()) queryCondition += "利润";
		if("2" == $("#analysisContentType").val()) queryCondition += "折扣";
		if("3" == $("#analysisContentType").val()) queryCondition += "成交价";
		
		queryCondition += "\n分析维度 = ";
		if("1" == $("#analysisDimensionType").val() ) queryCondition += "对象对比";
		if("2" == $("#analysisDimensionType").val() ) queryCondition += "城市对比";
		
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
	 * 展示图形标题
	 */
	function showChartTitleDiv()
	{
		var span1Text = "";
		var span2Text = "";
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var objectType = $("#objectType").val();
		span2Text = "(" + beginDate.substr(5) + "/" + beginDate.substr(0,4)+ "-" +  endDate.substr(5) + "/" + endDate.substr(0,4) + ")";
		//标题展示名称
		var dataName = "";
		var dataNameEn = "";
		var objectName = "";
		var objectNameEn = "";
		if("1" == $("#analysisContentType").val())
		{
			dataName = "经销商利润";
			dataNameEn = "Profit";
		}
		else if("2" == $("#analysisContentType").val()) 
		{
			dataName = "折扣";
			dataNameEn = "Distount";
		}
		else 
		{
			dataName = "成交价";
			dataNameEn = "TP";
		}
		
		if("1" == objectType){
			objectName = "型号";
			objectNameEn = " Version "; 
		}else if("2" == objectType){
			objectName = "车型";
			objectNameEn = " Model "; 
		}else{
			objectName = "厂商品牌";
			objectNameEn = " Manf Brand "; 
		}
		
		//如果是城市利润对比
		if("2" == $("#analysisDimensionType").val()) {
			var versionName = $("#versionModalResultContainer ul li div").text();
			span1Text = "RSD " + versionName + dataName + "走势";
			if("2" == $("#analysisContentType").val() || "3" == $("#analysisContentType").val()){
				span2Text =  dataNameEn +" Trend of " + $("#versionEn").val() + " RSD " + span2Text;
			} else{
				span2Text = "Dealer "+ dataNameEn +" Trend of " + $("#versionEn").val() + " RSD " + span2Text;
			}
			
		} else {
			if(1 < $("#subModelModalResultContainer ul li div input").length) {
				span1Text = objectName + dataName + "走势";
			} else {
				span1Text = $("#subModelModalResultContainer ul li div").text() + objectName + dataName + "走势";
			}
			if("2" == $("#analysisContentType").val() || "3" == $("#analysisContentType").val()){
				span2Text =  objectNameEn + dataNameEn +" Trend" + span2Text;
			} else{
				span2Text = "Dealer " + objectNameEn + dataNameEn +" Trend" + span2Text;
			}
		}
		$("#chartTitleDiv span").eq(0).text(span1Text);
		$("#chartTitleDiv span").eq(1).text(span2Text);
	}
	
	/**
	 * 画图
	 */
	function showChart(json) {
		//线图颜色数组
		var colos = ['#00235A','#AED4F8','#8994A0','#62C5E2','#E26700','#F9A700','#4A6F8A','#BBC600','#920A6A','#E63110','#005D5B','#FACE00','#009C0E','#77001D'];
		if(!json) {
			return;
		}
		var series = json.series;
		var titles = json.titles;
		for(var i = 0; i < titles.length; i++){
			var name = titles[i];
			var num = 0;
			for(var k = i+1; k < titles.length; k++){
				if(name == titles[k]){
					num++;
					for(var l = 0; l < num; l++){
						titles[k] = titles[k] + " ";
					}
				}
			}
		}
		
		//循环添加数据标签展示内容
		for(var i = 0; i < series.length; i++) {
			var obj = series[i];
			//循环每组数据，名字相同的时候加空格
			var name = obj.name;
			var num = 0;
			for(var k = i+1; k < series.length; k++){
				if(name == series[k].name){
					num++;
					for(var l = 0 ; l < num; l++){
						series[k].name = series[k].name + " ";
					}
				}
			}
			obj.itemStyle = {normal:{color:colos[i],label:{show:true,formatter:function(v1,v2,v3){
					return window.formatData(v3);
			}}}};
			
			//添加数据标记
			if(0 != obj.markPointList.length)
		    {
				obj.markPoint = {data:obj.markPointList};
			}
		}
		//Y轴名称
		var yname = "";
		if("1" == $("#analysisContentType").val()) {
			yname = "利润(元)";
		} else if("2" == $("#analysisContentType").val()) {
			yname = "折扣(元)";
		} else {
			yname = "成交价(元)";
		}
		var option = {
 			    tooltip : {
 			        trigger: 'item',
 			        show:true,
					showDelay:0,
					hideDelay:0,
					transitionDuration:0,
 			        formatter: function(params) {
			        	//返回数据标识提示框内容
 			        	var regex =/^[\u4E00-\u9FA5]+$/;
			        	if(regex.exec(params[1]) || -1 != params[1].indexOf(":")) return params[1];	
 			        	return params[0]+ "<br/>" + params[1] + ":" +  window.formatData(params[2]);
 	                }
			    },
			    legend: {
			        data:titles
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
			        	name:yname,
			        	nameTextStyle:{color:"#53A3F3"},
			            type : 'value',
			            max : json.boundarys[0],
			            min : json.boundarys[1],
			            splitNumber:json.boundarys[2],
			            splitArea : {show : false},
			            axisLine:{lineStyle:{color:'#BBBBBB'}},
			            splitLine : {show : false,lineStyle:{color:'#F0F0F0'}}
			        }
			    ],
			    series : series
			};
		   myChart.clear();
		   console.log(JSON.stringify(option));
 		   myChart.setOption(option);
 		   //保存线图刻度最大值与最小值
           $("#ymax").val(json.boundarys[0]);
  		   $("#ymin").val(json.boundarys[1]);
	}
	
	/**
	 * 画型号表格
	 */
	function showVersionGrid(grid)
	{
		$("#gridTheadByVersion").removeClass("hide");
		$("#gridTheadByModel").addClass("hide");
		$("#gridTheadByManf").addClass("hide");
		var tbodyHtml = "";
		if(grid)
		{
			for(var i = 0; i < grid.length; i++)
			{
				var dataObj = grid[i];
				var className = "";
				if(0 == i % 2) className = "odd";
				
				//如果无数据则不显示
				if(!dataObj.versionCode) continue;
				
				tbodyHtml += "<tr>";
				//如果是城市利润对比
				if("2" == $("#analysisDimensionType").val())
				{
					tbodyHtml += "<td style='width:6%' class='"+className+"'>" + dataObj.cityName + "</td>";
					$("#versionCityTh").removeClass("hide");
					//如果是城市利润时保存型号英文名称
					if(0 == i)$("#versionEn").val(dataObj.versionChartName);
				}
				else $("#versionCityTh").addClass("hide");
				
				var week = dataObj.week;
				var yearMonth = "";
				if(week != 'M'){
					yearMonth = dataObj.yearMonth+dataObj.week;
				}else{
					yearMonth = dataObj.yearMonth;
				}
				tbodyHtml += "<td style='width:8%' class='"+className+"'>" + yearMonth + "</td>";
				tbodyHtml += "<td style='width:6%' class='"+className+"'>" + dataObj.versionCode + "</td>";
				tbodyHtml += "<td style='width:10%' class='"+className+"'>" + dataObj.versionName + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='"+className+"'>" + dataObj.versionLaunchDate + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.msrp) + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.tp) + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + getDiscount(dataObj.msrp,dataObj.tp) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.sellerCost) + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.invoicePrice) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.rebatePrice) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.rewardAssessment) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.promotionalAllowance) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.modelProfit) + "</td>";
				tbodyHtml += "</tr>";
			}
			$("#gridTbody").html(tbodyHtml);
		}
	}
	
	/**
	 * 画车型或厂商表格
	 */
	function showObjectGrid(grid,objectType) {
		$("#gridTheadByVersion").addClass("hide");
		if (objectType == 2) {
			$("#gridTheadByModel").removeClass("hide");
			$("#gridTheadByManf").addClass("hide");
		} else {
			$("#gridTheadByModel").addClass("hide");
			$("#gridTheadByManf").removeClass("hide");
		}
		var tbodyHtml = "";
		if(grid) {
			for(var i = 0; i < grid.length; i++) {
				var dataObj = grid[i];
				var className = "";
				if(0 == i % 2) className = "odd";
				tbodyHtml += "<tr>";
				//如果是城市利润对比
				if ("2" == $("#analysisDimensionType").val()) {
					tbodyHtml += "<td style='width:6%' class='" + className + "'>" + dataObj.cityName + "</td>";
					if (objectType == 2) {
						$("#modelCityTh").removeClass("hide");
					} else {
						$("#manfCityTh").removeClass("hide");
					}
				//如果是城市利润时保存型号英文名称
				//if(0 == i)$("#versionEn").val(dataObj.versionChartName);
				} else {
					if (objectType == 2) {
						$("#modelCityTh").addClass("hide");
					} else {
						$("#manfCityTh").addClass("hide");
					}
				}
				
				var week = dataObj.week;
				var yearMonth = "";
				if(week != 'M'){
					yearMonth = dataObj.yearMonth+dataObj.week;
				}else{
					yearMonth = dataObj.yearMonth;
				}
				
				tbodyHtml += "<td style='width:8%' class='"+className+"'>" + yearMonth + "</td>";
				if(objectType == 2) {
					tbodyHtml += "<td style='width:10%' class='"+className+"'>" + dataObj.SubmodelName + "</td>";
				} else {
					tbodyHtml += "<td style='width:10%' class='"+className+"'>" + dataObj.manfName + "</td>";
				}
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.msrp) + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.tp) + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + getDiscount(dataObj.msrp,dataObj.tp) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.sellerCost) + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.invoicePrice) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.rebatePrice) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.rewardAssessment) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.sellerSupport) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.modelProfit) + "</td>";
				tbodyHtml += "</tr>";
			}
			$("#gridTbody").html(tbodyHtml);
		}
	}
	
	/**
	 * 获取折扣
	 */
	function getDiscount(msrp,tp)
	{
		if(msrp && tp)
		{
			if("-" != msrp && "-" != tp)
			{
				return formatData( (msrp - tp) );
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
		
		if(parseInt(beginDate.replace("-","")) > parseInt(endDate.replace("-","")) )
		{
			alert("开始时间不能大于结束时间");
			return true;
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
			   url: ctx+"/loadModelProfitChartAndTable",
			   data: params,
			   success: function(data){
				   if(data)
				   {
				     //查询面板折叠
				     $(".queryConditionContainer .buttons .toggle a").click();
					 $("#chartId").height(450);
					 myChart.resize();
					 showChart(data.chart);
					 if(params.objectType == 1) {
						 showVersionGrid(data.grid);
					 } else {
					 	showObjectGrid(data.grid,params.objectType);
					 }
					 showChartTitleDiv();
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
	});
});

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
	if(0 == citys){
		if(endDate_TPMix>2013){
			$("#ex_citys").val("8,17,28,3,18,7,12,15,31,2,5,11,23,1,14,16,20,32,4,13,21,22,29,19,30,41,42,48,60,61,47");
		}else{
			$("#ex_citys").val("8,17,28,3,18,7,12,15,31,2,5,11,23,1,14,16,20,32,4,13,21,22,29,47");
		}
	}
	$("#ex_mids").val($("#subModelModalResultContainer input[name='selectedSubModel']").map(function(){return "'"+$(this).val()+"'";}).get().join(","));
	$("#ex_manfs").val($("#manfModalResultContainer input[name='selectedManf']").map(function(){return "'"+$(this).val()+"'";}).get().join(","));
	$("#languageType").val(languageType);
	$("#objType").val($("#objectType").val());
	$("#analysisType").val($("#analysisDimensionType").val());
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

