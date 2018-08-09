/**定义厂商LI全局变量*/
var currManfLI = null;
/**定义细分市场LI全局变量*/
var currSegmentLI = null;
/**定义品牌LI全局变量*/
var currBrandLI = null;
/**定义车身形式LI全局变量*/
var currBodyTypeLI = null;
/**定义系别LI全局变量*/
var currOrigLI = null;
var segmentPath = "/promotion/global/getSegmentAndChildren";
/**车身形式的HTML*/
var  bodyTypeContainerHTML = '<div class="control-group"  name="objectType7" id="bodyTypeControlGroup"> ' 
						   + '	<label class="control-label" for="analysisDimension"> &nbsp; </label> '  
	                       + '	<div class="controls">  ' 
	                       + ' 		<div class="span2" style="width:90px">  ' 
	                       + '			<a href="#bodyTypeModal" role="button" name = "bodyTypeSelector" class="btn" data-toggle="modal">车身形式</a>	'  
	                       + '		</div>   '  
	                       + '		<div class="bodyTypeModalResultContainer" style="margin-left:0px" id="bodyTypeModalResultContainer"></div>  '  
	                       + '	</div>  '  
	                       + '</div>';

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
	 * 展示车型弹出框
	 */
	$('#subModelModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		
		//加载子车型数据
		showLoading("subModelModalBody");
		$('#subModelModalBody').load(ctx+"/promotion/global/getSubmodelModal",getParams(),function(){
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
						$(this).attr("checked","true");//行全选
					}
				});
			});
			strHTML += '</ul>';
			$("#selectorResultContainerBySubModel").html(strHTML);
			
		});
	});
	
	/* 品牌鼠标点击删除*/
	$(".brandModalResultContainer ul div").live('click',function(){
		var currVal = $(this).attr("brandId");
		$(this).parents('.brandModalContainer').find('.brandIdInput').filter(function(){
			return $(this).val() == currVal;
		}).each(function(){
			$(this).removeAttr("checked");//取消选中
		});	
		$(this).parent().remove();
	});
	
	$('#versionModal').on('show', function (e) {			
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
		if(e.relatedTarget || 0 == $("#subModelModalResultContainer input[name='selectedSubModel']").length)  return; 
		//加载子车型下型号数据
		showLoading("versionModalBody");
		$('#versionModalBody').load(ctx+"/promotion/global/getVersionModalByCommon",getParams(),function(){
			//设置默认选中项
			$('#versionModalResultContainer input').each(function(){
				var selectedId = $(this).val();
				$(".versionModalContainer .versionIdInput").each(function(){
					if($(this).val() == selectedId){
						$(this).attr("checked","true");//行全选
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
					$(this).attr("checked","true");
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
		$('#autoVersionModalBody').load(ctx+"/promotion/global/getAutoCustomGroup",getParams(),function(){
			//设置默认选中项
			$('#autoVersionModalResultContainer input').each(function(){
				var selectedId = $(this).val();
				$(".autoVersionModalContainer .autoVersionIdInput").each(function(){
					if($(this).val() == selectedId){
						$(this).attr("checked","true");//行全选
					}
				});
			});
			
			//设置全选效果---开始
			$(".autoVersionModalContainer").find(".selectAutoVersionAll").each(function(){
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
					$(this).attr("checked","true");
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
		//修复bootstrap的modal引入tabpane时,触发事件问题。 或者没有选择车型
		if(e.relatedTarget)  return; 
		//加载生产商数据
		showLoading("manfModalBody");
		$('#manfModalBody').load(ctx+"/promotion/global/getManfModal",getParams(),function(){
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
	
	/** 弹出品牌对话框*/
	$(".brandSelector").live('click',function(){
		currBrandLI = $("div[name = 'objectType4']");
	});
	
	
	$("#brandModal").on("show", function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//加载子车型数据
		showLoading("brandModalBody");
		$("#brandModalBody").load(ctx + "/promotion/global/getBrand", getParams(), function() {
			//弹出框设置默认选中项结果集		
			var strHTML = '<ul class="selectorResultContainer" >';
			$(currBrandLI).find(".brandModalResultContainer input").each(function() {
				var id = $(this).val();
				var name = $(this).attr("brandName");
				var letter = $(this).attr("letter");
				strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
			  	strHTML += '<div class="removeBtnByResult label label-info" brandId="' + id + '"  letter="' + letter + '" brandName="' + name + '" style="cursor:pointer" title="删除：' + name + '">';
				strHTML += '<i class="icon-remove icon-white"></i>' + name;
			  	strHTML += '</div>';
			 	strHTML += '</li>';
		 		$(".brandModalContainer .brandIdInput").each(function() {
					if($(this).val() == id) {
						$(this).attr("checked", "true");//行全选
					}
				});
			});
			strHTML += '</ul>';
			$("#selectorResultContainerByBrand").html();
			$("#selectorResultContainerByBrand").html(strHTML);
		});
	});
	
	$(".brandModalContainer").find('.brandIdInput').live('click',function(e){
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//显示选中的值	——开始
		//去掉重复选项
		$("#brandModalResultContainer").html("");
		var allObjArr = [];
		$(".brandModalContainer").find('.brandIdInput:checked').each(function(){
			var obj = {};
			obj.id =  $(this).val();
			obj.name =  $.trim($(this).parent().text());
			obj.letter =  $(this).attr("letter");
			allObjArr[allObjArr.length] = obj;
		});
		var strHTML = '<ul class="selectorResultContainer" >';
		for(var i=0;i<allObjArr.length;i++){
			strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
		  		strHTML += '<div class="removeBtnByResult label label-info" brandId="'+allObjArr[i].id+'"  letter="'+allObjArr[i].letter+'" brandName="'+allObjArr[i].name+'" style="cursor:pointer" title="删除：'+allObjArr[i].name+'">';
			  	strHTML += '<i class="icon-remove icon-white"></i>'+allObjArr[i].name;
		  		strHTML += '</div>';
		 		strHTML += '</li>';
		 }
		strHTML += '</ul>';
//		$("#brandModalResultContainer").html(strHTML);
		
		//显示选中的值	——结束
		$("#selectorResultContainerByBrand").html("");
		$("#selectorResultContainerByBrand").html(strHTML);
	});
	
	
	
	/** 弹出系别对话框*/
	$(".origSelector").live("click",function(){
		currOrigLI = $("div[name = 'objectType5']");
	});
	
	
	/**展示车身形式弹出框*/
	$('#origModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//去掉默认选中的效果
		$('.origModalByAll').each(function(){
			$(this).removeAttr("checked");//取消行全选
		});
		//加载子车型数据
		showLoading("origModalBody");
		$('#origModalBody').load(ctx+"/promotion/global/getOrig",getParams(),function(){
			//弹出框设置默认选中项结果集		
			$(currOrigLI).find('input').each(function(){
				var origId = $(this).val();
				if(origId == "0"){
					$('.origModalByAll').each(function(){
						$(this).attr("checked","true");//全选
					});
					$(".origModalContainer .origModal").each(function(){
						$(this).attr("checked","true");//行全选
					});
				}else{
					$(".origModalContainer .origModal").each(function(){
						if($(this).val() == origId){
							$(this).attr("checked","true");//行全选
						}
					});
				}
			});
		});
	});
	
	
	
	/**点击确定生成内容*/
	$(".origModalContainer").find('.confirm').live('click',function(){
		var containerId = $(this).parents(".origModalContainer").attr("id");
		var relInputName = $(this).attr("relInputName");
		var strHTML = "";
		strHTML += '<ul class="selectorResultContainer">';
		//如果全部选中，则生成全部
		if($(".origModalByAll").attr("checked"))
		{
			strHTML += '<li>';
			strHTML += '<div class="removeBtn" relContainer="origModal" value="0" style="cursor:pointer;" title="整体市场">';
			strHTML += '<input type="hidden" value="0" name="selectedOrig" />';
			strHTML += '整体市场<i class="icon-remove" style="visibility: hidden;"></i>';
			strHTML += '</div>';
			strHTML += '</li>';
			
			//部分模块存在拆分对象情况,当车型为全选时不允许拆分
			$("#origModalResultContainer").find("tbody td input[name='obj_Split']").attr('checked',false);
			$("#origModalResultContainer").find("tbody td input[name='obj_Split']").attr('disabled',true);
		}
		else
		{							
			$(this).parents(".origModalContainer").find('.origModal:checked').each(function(){
				strHTML += '<li>';
				strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+$(this).val()+'" style="cursor:pointer" title="删除：'+$.trim($(this).parent().text())+'">';
				strHTML += '<input type="hidden" value="'+$(this).val()+'" name="'+relInputName+'" levelType="1" />';
				strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
				strHTML += '</div>';
				strHTML += '</li>';
			});
			
			//部分模块存在拆分对象情况,当车型为全选时不允许拆分
			$("#origModalResultContainer").find("tbody td input[name='obj_Split']").attr('disabled',false);
		}
		strHTML += '</ul>';		
		$("#origModalResultContainer").html(strHTML);
	});
	
	
	
	/** 弹出细分市场(级别)对话框*/
	$(".segmentSelector").live('click',function(){		
		currSegmentLI = $("div[name = 'objectType6']");		
	});
	
	/** 展示细分市场弹出框*/
	$('#segmentModal').on('show', function (e) {		
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//去掉默认选中的效果
		$('.segmentModalByAll').each(function(){
			$(this).removeAttr("checked");//取消行全选
		});
		//加载子车型数据
		showLoading("segmentModalBody");
		$('#segmentModalBody').load(ctx+"/promotion/global/getSegmentAndChildren", getParams(), function(){
			//弹出框设置默认选中项结果集		
			$(currSegmentLI).find('input').each(function(){
				var segmentId = $(this).val();
				if(segmentId == "0"){
					$('.segmentModalByAll').each(function(){
						$(this).attr("checked",'true');//全选
					});
					$(".segmentModalContainer .segmentModalByLevel1").each(function(){
						$(this).attr("checked",'true');//行全选
					});
			 		$(".segmentModalContainer .segmentModalByLevel2").each(function(){
						$(this).attr("checked",'true');//行全选
					});
				}else{
					$(".segmentModalContainer .segmentModalByLevel1").each(function(){
						if($(this).val() == segmentId){
							$(this).attr("checked",'true');//行全选
						}
					});
			 		$(".segmentModalContainer .segmentModalByLevel2").each(function(){
						if($(this).val() == segmentId){
							$(this).attr("checked",'true');//行全选
						}
					});
				}
			});
			
			$('.segmentModalContainer').find('.selectorTR').each(function(){
				var oInput = $(this).find('.segmentModalByLevel1:checked');
				if(oInput.attr("checked")){
					$(this).find('.segmentModalByLevel2').each(function(){
						 $(this).attr("checked",'true');//行全选
					});
				}
			});
		});
	});
	
	
	/**点击确定生成内容*/
	$(".segmentModalContainer").find('.confirm').live('click',function(){
		var containerId = $(this).parents(".segmentModalContainer").attr("id");
		var relInputName = $(this).attr("relInputName");
		var strHTML = "";
		strHTML += '<ul class="selectorResultContainer">';
		//如果第一级分细市场全部选中，则生成整体市场
		if($("#myModalLabel :checkbox").attr("checked")) {
			strHTML += '<li>';
			strHTML += '<div class="removeBtn" relContainer="segmentModal" value="0" style="cursor:pointer;" title="整体市场">';
			strHTML += '<input type="hidden" value="0" name="selectedSegment" />';
			strHTML += '整体市场<i class="icon-remove" style="visibility: hidden;"></i>';
			strHTML += '</div>';
			strHTML += '</li>';
			
			//拆分选项不可选
			$('#segmentModalResultContainer').find("tbody td input[name='obj_Split']").attr('checked',false);
			$('#segmentModalResultContainer').find("tbody td input[name='obj_Split']").attr('disabled',true);
		}
		else
		{							
			$(this).parents(".segmentModalContainer").find('.selectorTR').each(function(){
				var level2RowTotalAmount = $(this).find('.segmentModalByLevel2').length;
				var level2RowSelectedAmount = $(this).find('.segmentModalByLevel2:checked').length;
//				if(level2RowTotalAmount == level2RowSelectedAmount){
//					$(this).find('.segmentModalByLevel1').each(function(){
//						strHTML += '<li>';
//						strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+$(this).val()+'" style="cursor:pointer" title="删除：'+$.trim($(this).parent().text())+'">';
//						strHTML += '<input type="hidden" value="'+$(this).val()+'" name="'+relInputName+'" levelType="1" />';
//						strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
//						strHTML += '</div>';
//						strHTML += '</li>';
//					});
//				}else{
					$(this).find('.segmentModalByLevel2:checked').each(function(){
						strHTML += '<li>';
						strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+$(this).val()+'" style="cursor:pointer" title="删除：'+$.trim($(this).parent().text())+'">';
						strHTML += '<input type="hidden" value="'+$(this).val()+'" name="'+relInputName+'"  levelType="2" />';
						strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
						strHTML += '</div>';
						strHTML += '</li>';
					});
//				}
			});
			
			//拆分选项可选
			$('#segmentModalResultContainer').find("tbody td input[name='obj_Split']").attr('disabled',false);
		}
		strHTML += '</ul>';
		
		$('#segmentModalResultContainer').html(strHTML);
	});
	
	
	
	/** 弹出细分市场(级别)对话框*/
	$(".bodyTypeSelector").live('click',function(){		
		currBodyTypeLI = $("#bodyTypeControlGroup");		
	});
	
	/**展示车身形式弹出框*/
	$('#bodyTypeModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//去掉默认选中的效果
		$('.bodyTypeModalByAll').each(function(){
			$(this).removeAttr("checked");//取消行全选
		});
		//加载子车型数据
		showLoading("bodyTypeModalBody");
		$('#bodyTypeModalBody').load(ctx+"/promotion/global/getBodyType",getParams(),function(){
			//弹出框设置默认选中项结果集		
			$(currBodyTypeLI).find('input').each(function(){
				var bodyTypeId = $(this).val();
				if(bodyTypeId == "0"){
					$('.bodyTypeModalByAll').each(function(){
						$(this).attr("checked",'true');//全选
					});
					$(".bodyTypeModalContainer .bodyTypeModal").each(function(){
						$(this).attr("checked",'true');//行全选
					});
				}else{
					$(".bodyTypeModalContainer .bodyTypeModal").each(function(){
						if($(this).val() == bodyTypeId){
							$(this).attr("checked",'true');//行全选
						}
					});
				}
			});
		});
	});
	
	
	/**点击确定生成内容*/
	$(".bodyTypeModalContainer").find('.confirm').live('click',function(){
		var containerId = $(this).parents(".bodyTypeModalContainer").attr("id");
		var relInputName = $(this).attr("relInputName");
		var strHTML = "";
		strHTML += '<ul class="selectorResultContainer">';
		//如果全部选中，则生成全部
		if($(".bodyTypeModalByAll").attr("checked"))
		{
			strHTML += '<li>';
			strHTML += '<div class="removeBtn" relContainer="bodyTypeModal" value="0" style="cursor:pointer;" title="全部">';
			strHTML += '<input type="hidden" value="0" name="selectedBodyType" />';
			strHTML += '全部<i class="icon-remove" style="visibility: hidden;"></i>';
			strHTML += '</div>';
			strHTML += '</li>';
		}
		else
		{							
			$(this).parents(".bodyTypeModalContainer").find('.bodyTypeModal:checked').each(function(){
				strHTML += '<li>';
				strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+$(this).val()+'" style="cursor:pointer" title="删除：'+$.trim($(this).parent().text())+'">';
				strHTML += '<input type="hidden" value="'+$(this).val()+'" name="'+relInputName+'" levelType="1" />';
				strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
				strHTML += '</div>';
				strHTML += '</li>';
			});
		}
		strHTML += '</ul>';
		$("#bodyTypeModalResultContainer").html(strHTML);
	});

	
	
	/**
	 * 对象类型改变(车型,型号,常用组)清空,且显示对象改变
	 */
	 $('#objectType').on('change', function () {
		 //当页面条件是非型号时显示正斜对应选择项
		 if($("select[id='objectType'] option:selected").val()!='1'){
				$("#duiyingDiv").attr("style","display:none")
			}else{
				$("#duiyingDiv").attr("style","display:none");
			}
	 	 var type =$('#objectType').val();
		 //$("#cityModalResultContainer").html("");
		 $("#subModelModalResultContainer").html("");
		 $("#versionModalResultContainer").html("");
		 $("#versionModalBody").html("");
		 $("#autoVersionModalResultContainer").html("");
		 $("#manfModalResultContainer").html("");
		 $("#brandModalResultContainer").html("");
		 $("#origModalResultContainer").html("");
		 $("#segmentModalResultContainer").html("");
		 $("#bodyTypeModalResultContainer").html("");  //清空车身形式
		 
		 $("#bodyTypeControlGroup").remove();
		 
		 //对象类型改变时弹出框跟着改变
		 $(".control-group[name^='objectType']").css("display","none");
		 if(type == 1){
		 	$(".control-group[name='objectType1']").css("display","block");
		 }else if(type == 2){
		 	$(".control-group[name='objectType1']").eq(0).css("display","block");
		 }else if(type == 3){
		 	$(".control-group[name='objectType3']").css("display","block");
		 }else if(type == 4){
		 	$(".control-group[name='objectType4']").css("display","block");
		 }else if(type == 5){
		 	$(".control-group[name='objectType5']").css("display","block");		 	
		 	$(".control-group[name='objectType5']").after(bodyTypeContainerHTML);		 	
		 }else if(type == 6){
		 	$(".control-group[name='objectType6']").css("display","block");		 	
		 	$(".control-group[name='objectType6']").after(bodyTypeContainerHTML);
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
			   url: ctx+"/promotion/resetDate",
			   data: {analysisContentType:$("#analysisContentType").val()},
			   dataType:'text',
			   success: function(data){
				   if(data)
				   {
						 //data = 默认开始时间 ，开始时间，结束时间
						 var defaultBeginDate = data.split(",")[0];
					     var beginDate = data.split(",")[1];
					     var endDate = data.split(",")[2];						 
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
		}else if(paramsObj.objectType == 4){
			if(!paramsObj.brandIds) return;
		}else if(paramsObj.objectType == 5){
			if(!paramsObj.origIds) return;
		}else if(paramsObj.objectType == 6){
			if(!paramsObj.segmentIds) return;
		}else{
			if(!paramsObj.vids && !paramsObj.mids) return;
		}
		$('.queryConditionContainer').showLoading();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/promotion/checkPopBoxData",
			   data: paramsObj,
			   dataType:'json',
			   success: function(data){
				   if(data)
				   {
					  var modelObj = $("#subModelModalResultContainer ul li");
					  var vidObj = $("#versionModalResultContainer ul li");
					  var manfObj = $("#manfModalResultContainer ul li");
					  
					  var brandObj = $("#brandModalResultContainer ul li");
					  var origObj = $("#origModalResultContainer ul li");
					  var segmentObj = $("#segmentModalResultContainer ul li");
					  
					  for(var i = 0; i < data.length; i++)
					  {
						  var mid = data[i].subModelId;
						  var vids = data[i].versionList;
						  var manf = data[i].manfId;
						  
						  //遍历级别(细分市场)
						  if(segmentObj)
						  {
							  $.each(segmentObj,function(i,n){
								  if(manf == $(n).find("div").attr("value")) $(n).remove();
							  });
						  }
						  
						  
						  //遍历系别
						  if(origObj)
						  {
							  $.each(origObj,function(i,n){
								  if(manf == $(n).find("div").attr("value")) $(n).remove();
							  });
						  }
						  
						  //遍历品牌
						  if(brandObj)
						  {
							  $.each(brandObj,function(i,n){
								  if(manf == $(n).find("div").attr("brandid")) $(n).remove();
							  });
						  }
						  
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
	window.getParams = function()
	{	
		var duiying=$(".duiying:checked").val();
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();		
		var maxDate = $("#maxDate").val();
		var objectType = $("#objectType").val();
		var inputType = $("#analysisDimensionType").val();//控件是单选还是复选
		var segmentType = $(".segmentType").map(function(){if($(this).prop("checked")) {return $(this).val();} else {return null;}}).get().join(",");//细分市场类别
		var analysisContentType = $("#analysisContentType").val();//数据指标分析类型
		//城市ID
		var citys = "";
		//$("#cityModalResultContainer input[name='selectedCity']").map(function(){return $(this).val();}).get().join(",");
		//如果是全国均价，则取弹出枉里的城市ID
		var endDate_TPMix=endDate.substr(0,4)-1;
		var autoVidLength = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").length;
		var vids = "";
		if(autoVidLength > 0){
			vids = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").map(function(){return $(this).val();}).get().join(",");
		}else{
			vids = $("#versionModalResultContainer input[name='selectedVersion']").map(function(){return $(this).val();}).get().join(",");
		}
		var mids = $("#subModelModalResultContainer input[name='selectedSubModel']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
		var manfs = $("#manfModalResultContainer input[name='selectedManf']").map(function(){  
			var value1 = $(this).val();  
//			if(value1.split("~").length > 1){
//				value1 = value1.split("~")[0];
//			} 
			return "'"+value1+"'";
		}).get().join(",");
		
		var brandIds = $(".selectorResultContainer li div input").map(function(){return $.trim($(this).attr("value"));}).get().join(",");
		var origIds =  $("#origModalResultContainer li div").map(function(){return $.trim($(this).attr("value"));}).get().join(",");
		var segmentIds =  $("#segmentModalResultContainer li div").map(function(){return $.trim($(this).attr("value"));}).get().join(",");
		/*var segmentIds =  $("#segmentModalResultContainer li div").map(function(){
			return $.trim($(this).attr("value"))+"~"+$.trim($(this).find("input").eq(0).attr("leveltype"));
		}).get().join(",");*/
		var bodyTypeIds = $("#bodyTypeModalResultContainer li div").map(function(){return $.trim($(this).attr("value"));}).get().join(",");
			
	    var paramObj = {};
		paramObj.beginDate = beginDate;
		paramObj.endDate = endDate;
		paramObj.modelIds = mids;
		paramObj.inputType = inputType;
		paramObj.citys = citys;
		paramObj.vids = vids;
		paramObj.mids = mids;
		paramObj.manfs = manfs;		
		paramObj.bodyTypeIds = bodyTypeIds;
		paramObj.brandIds = brandIds;		
		paramObj.origIds = origIds;
		paramObj.segmentIds = segmentIds;		
		paramObj.maxDate = maxDate;		
		paramObj.objectType = objectType;
		paramObj.analysisContentType = analysisContentType;
		paramObj.segmentType = segmentType;
		paramObj.timeType = "2";
		paramObj.duiying=duiying;
		getQueryConditionAndBrowser(paramObj);
		
		//console.log(  JSON.stringify( paramObj ) );
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
		if("1" == $("#analysisContentType").val()) queryCondition += "促销";
		if("2" == $("#analysisContentType").val()) queryCondition += "内促";
		if("3" == $("#analysisContentType").val()) queryCondition += "外促";
		
//		queryCondition += "\n分析维度 = ";
//		if("1" == $("#analysisDimensionType").val() ) queryCondition += "对象对比";
//		if("2" == $("#analysisDimensionType").val() ) queryCondition += "城市对比";
		
		queryCondition += "\n对象类型 = ";
		if("1" == $("#objectType").val() ) queryCondition += "型号";
		if("2" == $("#objectType").val() ) queryCondition += "车型";
		if("3" == $("#objectType").val() ) queryCondition += "厂商品牌";
		if("4" == $("#objectType").val() ) queryCondition += "品牌";
		if("5" == $("#objectType").val() ) queryCondition += "系别";
		if("6" == $("#objectType").val() ) queryCondition += "级别";
		
		queryCondition += "\n时间 = " + $("#startDate").val() + "至" + $("#endDate").val();
		
		//queryCondition += "\n城市 = " + $("#cityModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n车型 = " + $("#subModelModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n型号 = " + $("#versionModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n自定义型号组 = " + $("#autoVersionModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n生产商 = " + $("#manfModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");		
		queryCondition += "\n品牌 = " + $(".selectorResultContainer li div input").map(function(){return $.trim($(this).text());}).get().join(",");
		queryCondition += "\n系别 = " + $("#origModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		queryCondition += "\n级别 = " + $("#segmentModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		queryCondition += "\n车身形式 = " + $("#bodyTypeModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
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
		
		var objectName = "";
		var objectNameEn = "";
		
		var  acType = parseInt($("#analysisContentType").val());
		var  dataNamesCn = ["促销","内促","外促"];
		var  dataNamesEn = ["Promotion","Promotion","Promotion"];
		var dataName = dataNamesCn[acType-1];
		var dataNameEn = dataNamesEn[acType-1];
		
		if("1" == objectType){
			objectName = "型号";
			objectNameEn = " Version "; 
		}else if("2" == objectType){
			objectName = "车型";
			objectNameEn = " Model "; 
		}else if("3" == objectType){
			objectName = "生产商";
			objectNameEn = " Model "; 
		}else if("4" == objectType){
			objectName = "品牌";
			objectNameEn = " Brand "; 
		}else if("5" == objectType){
			objectName = "系别";
			objectNameEn = " Orig "; 
		}else if("6" == objectType){
			objectName = "级别";
			objectNameEn = " Segment "; 
		}else{
			objectName = "生产商";
			objectNameEn = " Manf "; 
		}
		
//		span1Text = $("#subModelModalResultContainer ul li div").text() + objectName + dataName + "走势";
//		span2Text = "Dealer "+objectNameEn + dataNameEn+" trend" + span2Text;
		
		span1Text = dataName + "走势";
		span2Text = dataNameEn+" trend" + span2Text;
		
		$("#chartTitleDiv span").eq(0).text(span1Text);
		$("#chartTitleDiv span").eq(1).text(span2Text);
	}
	
	/**
	 * 画图
	 */
	function showChart(json)
	{
		//线图颜色数组
		var colos = ['#00235A','#AED4F8','#8994A0','#62C5E2','#E26700','#F9A700','#4A6F8A','#BBC600','#920A6A','#E63110','#005D5B','#FACE00','#009C0E','#77001D'];
		if(!json) return;
		var series = json.series;
		//循环添加数据标签展示内容
		for(var i = 0; i < series.length; i++)
		{
			var obj = series[i];
			
			obj.itemStyle = {normal:{color:colos[i],label:{show:true,formatter:function(v1,v2,v3){				 
				   if(v3==null||v3==''||v3=='null'||v3=='NaN'){
					   return '';
				   }
				   
				   return window.formatData(v3);
			}}}};
			//添加数据标记
			if(0 != obj.markPointList.length)
		    {
				obj.markPoint = {data:obj.markPointList};
			}
		}
		//Y轴名称
		var yname = "促销(元)";
		
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
			        data:json.titles
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
			            type : "value",
			            max : json.boundarys[0],
			            min : json.boundarys[1],
			            splitNumber:json.boundarys[2],
			            splitArea : {show : false},
			            axisLine:{lineStyle:{color:"#BBBBBB"}},
			            splitLine : {show : false,lineStyle:{color:"#F0F0F0"}}
			        }
			    ],
			    series : series
			};
		   myChart.clear();
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
		$("#gridTheadByBrand").addClass("hide");
		$("#gridTheadByOrig").addClass("hide");
		$("#gridTheadBySegment").addClass("hide");
		
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
				tbodyHtml += "<td style='width:8%' class='"+className+"'>" + dataObj.yearMonth + "</td>";
				tbodyHtml += "<td style='width:6%' class='"+className+"'>" + dataObj.versionCode + "</td>";
				tbodyHtml += "<td style='width:10%' class='"+className+"'>" + dataObj.versionName + "</td>";
				//tbodyHtml += "<td style='width:8%;text-align: center;' class='"+className+"'>" + dataObj.versionLaunchDate + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c1) + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c2) + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c3) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c8) + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c7) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.maintenance) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c4) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c5) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c6) + "</td>";
				tbodyHtml += "</tr>";
			}
			$("#gridTbody").html(tbodyHtml);
		}
	}
	
	/**
	 * 画车型或厂商、品牌、系别、级别表格
	 */
	function showObjectGrid(grid,objectType)
	{
		$("#gridTheadByVersion").addClass("hide");
		$("#gridTheadByModel").addClass("hide");
		$("#gridTheadByManf").addClass("hide");
		$("#gridTheadByBrand").addClass("hide");
		$("#gridTheadByOrig").addClass("hide");
		$("#gridTheadBySegment").addClass("hide");
		
		if (objectType == 2) {
			$("#gridTheadByModel").removeClass("hide");			
		}else if (objectType == 3){		
			$("#gridTheadByManf").removeClass("hide");
		}else if (objectType == 4){		
			$("#gridTheadByBrand").removeClass("hide");
		}else if (objectType == 5){		
			$("#gridTheadByOrig").removeClass("hide");
		}else if (objectType == 6){		
			$("#gridTheadBySegment").removeClass("hide");
		}
		var tbodyHtml = "";
		if(grid)
		{
			for(var i = 0; i < grid.length; i++)
			{
				var dataObj = grid[i];
				var className = "";
				if(0 == i % 2) className = "odd";
				
				tbodyHtml += "<tr>";
				tbodyHtml += "<td style='width:8%' class='"+className+"'>" + dataObj.yearMonth + "</td>";
				if(objectType == 2){
					tbodyHtml += "<td style='width:10%' class='"+className+"'>" + dataObj.submodelName + "</td>";
				}else{
					tbodyHtml += "<td style='width:10%' class='"+className+"'>" + dataObj.manfName + "</td>";
				}
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c1) + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c2) + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c3) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c8) + "</td>";
				tbodyHtml += "<td style='width:7%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c7) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c6) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c4) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c5) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: center;' class='text-right "+className+"'>" + formatData(dataObj.c6) + "</td>";
				tbodyHtml += "</tr>";
			}
			$("#gridTbody").html(tbodyHtml);
		}
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
			   url: ctx+"/promotion/loadChartAndTable",
			   data: params,
			   success: function(data){
				   if(data)
				   {
					  $("#chartTitleDivNoData").hide();
					  $("#tSortableNoData").hide();
					  $("#chartTitleDiv").show();
					  $("#tSortable").show();
						 
				     //查询面板折叠
				     $(".queryConditionContainer .buttons .toggle a").click();
					 $("#chartId").height(450);
					 myChart.resize();
					 showChart(data.chart);
					 if(params.objectType == 1){
						 showVersionGrid(data.grid);
					 }else{
					 	showObjectGrid(data.grid,params.objectType);
					 }
					 showChartTitleDiv();
				   }else{//无数据提示
					   //alert("没有对应的查询结果");
					   $("#chartTitleDivNoData").show();
					   $("#tSortableNoData").show();
					   $("#chartTitleDiv").hide();
					   $("#tSortable").hide();
				   }
				   $('body').hideLoading();
			   },
			   error:function(){
				   $("#chartTitleDivNoData").show();
				   $("#tSortableNoData").show();
				   $("#chartTitleDiv").hide();
				   $("#tSortable").hide();
				   $('body').hideLoading();
			   }
			});
	});
	
	$('#resetBtn').on('click', function (e) {
		//重置时变成型号维度隐藏掉正斜对应
		$("#duiyingDiv").attr("style","display:none");
		//车型容器
		$('#subModelModalResultContainer').html("");
		//型号容器
		$('#versionModalResultContainer').html("");
		//常用选项容器
		$('#autoVersionModalResultContainer').html("");
		//生产商容器
		$('#manfModalResultContainer').html("");
		
		//品牌容器
		$('#brandModalResultContainer').html("");
		//系别容器
		$('#origModalResultContainer').html("");
		//级别容器
		$('#segmentModalResultContainer').html("");
		
		$("#bodyTypeModalResultContainer").html("");  //清空车身形式
		
		$(".control-group[name^='objectType']").css("display","none");
		$(".control-group[name='objectType1']").css("display","block");
		
		$('#formId :reset');	
	});
});

/**
 * 导出Excel
 * @param languageType EN:英文版;ZH:中文版
 */
function exportExcel(languageType)
{
	if(!$.trim($("#gridTbody").html()))
	{
		alert("暂无数据导出");
		return;
	}
	if(paramsValidate()) return;
	var citys = "";
	//$("#cityModalResultContainer input[name='selectedCity']").map(function(){return $(this).val();}).get().join(",");
	$("#ex_beginDate").val($("#startDate").val());
	$("#ex_endDate").val($("#endDate").val());
	$("#ex_maxDate").val($("#maxDate").val());
	$("#ex_objectType").val($("#objectType").val());
	$("#ex_citys").val(citys);
	//如果是全国均价，则取弹出枉里的城市ID
	var endDate_TPMix=endDate.substr(0,4)-1;
	$("#ex_mids").val($("#subModelModalResultContainer input[name='selectedSubModel']").map(function(){return "'"+$(this).val()+"'";}).get().join(","));
	
	var autoVidLength = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").length;
	var vids = "";
	if(autoVidLength > 0){
		vids = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").map(function(){return $(this).val();}).get().join(",");
	}else{
		vids = $("#versionModalResultContainer input[name='selectedVersion']").map(function(){return $(this).val();}).get().join(",");
	}
	$("#ex_vids").val(vids);
	
	var manfs = $("#manfModalResultContainer input[name='selectedManf']").map(function(){  
		var value1 = $(this).val();  
//		if(value1.split("~").length > 1){
//			value1 = value1.split("~")[0];
//		} 
		return "'"+value1+"'";
	}).get().join(",");
	
	var brandIds = $(".selectorResultContainer li div input").map(function(){return $.trim($(this).attr("value"));}).get().join(",");
	var origIds =  $("#origModalResultContainer li div").map(function(){return $.trim($(this).attr("value"));}).get().join(",");
//	var segmentIds =  $("#segmentModalResultContainer li div").map(function(){
//		return $.trim($(this).attr("value"))+"~"+$.trim($(this).find("input").eq(0).attr("leveltype"));
//	}).get().join(",");
	var segmentIds =  $("#segmentModalResultContainer li div").map(function(){return $.trim($(this).attr("value"));}).get().join(",");
	var bodyTypeIds = $("#bodyTypeModalResultContainer li div").map(function(){return $.trim($(this).attr("value"));}).get().join(",");
	var duiying=$(".duiying:checked").val();
	
	$("#ex_manfs").val(manfs);
	$("#ex_brands").val(brandIds);
	$("#ex_origs").val(origIds);
	$("#ex_segments").val(segmentIds);
	$("#ex_bodyTypes").val(bodyTypeIds);   
	$("#languageType").val(languageType);
	$("#objType").val($("#objectType").val());
	$("#analysisType").val($("#analysisDimensionType").val());
	$("#dataIndexType").val($("#analysisContentType").val());
	$("#duiying").val(duiying);
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
	
	//$("#choseType").attr("value","")
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
	
	if('1' == type) getPromotionModelPage("tabs-competingProducts",type,inputType,"2");
	else if('2' == type) getPromotionModelPage("tabs-segment",type,inputType,"2");
	else if('3' == type) getPromotionModelPage("tabs-brand",type,inputType,"2");
	else getPromotionModelPage("tabs-manf",type,inputType,"2");
};


/**
 * 参数验证
 */
function paramsValidate()
{
	var beginDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var objectType = $("#objectType").val();
	//城市ID
	var cityLength = "";
	var vidLength = $("#versionModalResultContainer input[name='selectedVersion']").length;
	var modelLength = $("#subModelModalResultContainer :input[name='selectedSubModel']").length;
	var autoVidLength = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").length;
	var manfLength = $("#manfModalResultContainer .selectorResultContainer li").length;
	var brandLength = $("#brandModalResultContainer .selectorResultContainer li").length; 
	var origLength = $("#origModalResultContainer .selectorResultContainer li").length; 
	var segmentLength = $("#segmentModalResultContainer .selectorResultContainer li").length; 
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
	}else if(objectType == 3){
		//对象类型为厂商时必须选择生产商
		if(0 == manfLength){
			alert("请选择厂商");
			return true;
		}
	}else if(objectType == 4){
		//对象类型为品牌时必须选择品牌
		if(0 == brandLength){
			alert("请选择品牌");
			return true;
		}
	}else if(objectType == 5){
		//对象类型为系别时必须选择系别
		if(0 == origLength){
			alert("请选择系别");
			return true;
		}
	}else if(objectType == 6){
		//对象类型为级别时必须选择级别
		if(0 == segmentLength){
			alert("请选择级别");
			return true;
		}
	}
	
	return false;
}


/**
 * 展示页面
 * @param id 展示容器ID
 * @param type 子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
 * @param inputType 控件类型1：复选，2：单选;默认为1
 * @param timeType 时间类型1：时间点;2：时间段默认为2
 * 
 */
function getPromotionModelPage(id,type,inputType,timeType)
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
		$('#' + id).load(ctx+"/policy/promotion/getSubmodelModal",params,function(){
			$('#selectorResultContainerBySubModel .removeBtnByResult').each(function(){
				var subModelId = $(this).attr("subModelId");
				$(".subModelModalContainer .subModelIdInput").each(function(){
					
					if($(this).val() == subModelId) $(this).attr("checked",'true');//行全选
				});
				//全选之后再次进入的时候全选选中
				$(".subModelModalContainer .subModelIdInput").each(function(){
					if( $(this).closest("td").find(":checkbox").length==$(this).closest("td").find(":checkbox:checked").length){
						$(this).closest("td").prev().find(".selectSegmentAll").attr("checked",true);
						$(this).closest("td").prev().find(".selectManfAll").attr("checked",true);
						$(this).closest("td").prev().find(".selectBrandAll").attr("checked",true);
					}
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

