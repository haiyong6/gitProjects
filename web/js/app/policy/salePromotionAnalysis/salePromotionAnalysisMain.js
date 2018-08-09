/** 定义细分市场LI全局变量 */
var currSegmentLI = null;
/** 定义厂商LI全局变量 */
var currManfLI = null;
/** 定义品牌LI全局变量 */
var currBrandLI = null;
/** 定义子车型LI全局变量 */
var currSubModelLI = null;
/** 定义车身形式LI全局变量 */
var currBodyTypeLI = null;
/** 定义车系LI全局变量 */
var currOrigLI = null;
/** 细分市场请求路径 * */
var segmentPath = "/policy/salePromotionAnalysis/getSegmentAndChildren";
/** 车身形式的HTML */
var bodyTypeContainerHTML = '<div class="control-group"  name="objectType7" id="bodyTypeControlGroup"> '
		+ '	<label class="control-label" for="analysisDimension"> &nbsp; </label> '
		+ '	<div class="controls">  '
		+ ' 		<div class="span2" style="width:90px">  '
		+ '			<a href="#bodyTypeModal" role="button" name = "bodyTypeSelector" class="btn" data-toggle="modal">车身形式</a>	'
		+ '		</div>   '
		+ '		<div class="bodyTypeModalResultContainer" style="margin-left:0px" id="bodyTypeModalResultContainer"></div>  '
		+ '	</div>  ' 
		+ '</div>';

$(document).ready(function() {
	$("#startDate-container.input-append.date").datetimepicker({
		format : "yyyy-mm",
		language : "zh-CN",
		todayBtn : 0,
		autoclose : 1,
		startView : 3,
		maxView : 3,
		minView : 3,
		startDate : beginDate,
		endDate : endDate,
		showMeridian : 1
	});
	
	$("#endDate-container.input-append.date").datetimepicker({
		format : "yyyy-mm",
		language : "zh-CN",
		todayBtn : 0,
		autoclose : 1,
		startView : 3,
		maxView : 3,
		minView : 3,
		startDate : beginDate,
		endDate : endDate,
		showMeridian : 1
	});

	// 时间改变事件
	$("#endDate-container.input-append.date").on("changeDate", function() {
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

	$("#startDate-container.input-append.date").on("changeDate", function() {
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
});
	/**
	 * 展示车型弹出框
	 */
	$("#subModelModal").on("show", function(e) {
		if(e.relatedTarget) {
			return; // 修复bootstrap的modal引入tabpane时，触发事件问题。
		}	

		// 加载子车型数据
		showLoading("subModelModalBody");
		$("#subModelModalBody").load(ctx + "/policy/salePromotionAnalysis/getSubmodelModal", getParams(), function() {
			// 弹出框设置默认选中项结果集
			var strHTML = '<ul class="inline" >';
			$("#subModelModalResultContainer input").each(function() {
				var subModelId = $(this).val();
				var subModelName = $(this).attr("subModelName");
				var pooAttributeId = $(this).attr("pooAttributeId");
				var letter = $(this).attr("letter");
				strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
				strHTML += '<div class="removeBtnByResult label label-info" subModelId="' + subModelId + '"  pooAttributeId="' + pooAttributeId + '" letter="'
									  + letter + '" subModelName="' + subModelName + '" style="cursor:pointer" title="删除：' + subModelName + '">';
				strHTML += '<i class="icon-remove icon-white"></i>' + subModelName;
				strHTML += '</div>';
				strHTML += '</li>';
				$(".subModelModalContainer .subModelIdInput").each(function() {
					if($(this).val() == subModelId) {
					    $(this).attr("checked", "true");// 行全选
					}
				});
			});
			strHTML += '</ul>';
			$("#selectorResultContainerBySubModel").html(strHTML);
		});
	});

	$("#versionModal").on("show", function(e) {
		// 修复bootstrap的modal引入tabpane时，触发事件问题。
		// 或者没有选择车型
		if(e.relatedTarget || 0 == $("#subModelModalResultContainer input[name='selectedSubModel']").length) {
			return;
		}
		// 加载子车型下型号数据
		showLoading("versionModalBody");
		$("#versionModalBody").load(ctx + "/policy/salePromotionAnalysis/getVersionModalByCommon", getParams(), function() {
			// 设置默认选中项
			$("#versionModalResultContainer input").each(function() {
				var selectedId = $(this).val();
				$(".versionModalContainer .versionIdInput").each(function() {
					if($(this).val() == selectedId) {
						$(this).attr("checked", "true");// 行全选
					}
				});
		    });

			// 设置全选效果---开始
			$(".versionModalContainer").find(".selectVersionAll").each(function() {
				var selectedCount = 0;
				var totalCount = 0;
				var subModelId = $(this).val();
				$(".versionModalContainer .versionIdInput").each(function() {
				    if(subModelId == $(this).attr("subModelId")) {
						totalCount++;
						if($(this).attr("checked")) {
							selectedCount++;
						}
					}
				});
				if(selectedCount == totalCount) {
					$(this).attr("checked","true");
				} else {
					 $(this).removeAttr("checked");// 取消选中
				}
			});
				// 设置全选效果---结束
		});
	});

	//本竞品
	$("#tabs-competingProducts .subModelIdInput").live("click", function() {
		//把其他维度选中的取消
		$("#tabs-segment").find(".subModelIdInput:checked").each(function() {
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
	
    $("#autoVersionModal").on("show", function(e) {
		// 修复bootstrap的modal引入tabpane时，触发事件问题。
		// 或者没有选择车型
		if(e.relatedTarget) {
			return;
		}
		// 加载子常用对象下型号数据
		showLoading("autoVersionModalBody");
		$("#autoVersionModalBody").load(ctx + "/policy/salePromotionAnalysis/getAutoCustomGroup", getParams(), function() {
			// 设置默认选中项
			$("#autoVersionModalResultContainer input").each(function() {
				var selectedId = $(this).val();
				$(".autoVersionModalContainer .autoVersionIdInput").each(function() {
					if($(this).val() == selectedId) {
						$(this).attr("checked", "true");// 行全选
					}
				});
			});

			// 设置全选效果---开始
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
					$(this).attr("checked", "true");
				} else {
					$(this).removeAttr("checked");// 取消选中
				}
			});
		});
    });

	/** 展示厂商弹出框-开始 */
	$("a[name ='manfSelector']").live("click", function() {
		currManfLI = $("div[name = 'objectType3']");
	});

	$("#manfModal").on("show", function(e) {
		// 修复bootstrap的modal引入tabpane时,触发事件问题。
		// 或者没有选择车型
		if(e.relatedTarget) {
			return;
		}
		// 加载生产商数据
		showLoading("manfModalBody");
		$("#manfModalBody").load(ctx + "/policy/salePromotionAnalysis/getManf", getParams(), function() {
			// 弹出框设置默认选中项结果集
			var strHTML = '<ul class="inline" >';
			$(currManfLI).find(".manfModalResultContainer input").each(function() {
				var id = $(this).val();
				var name = $(this).attr("manfName");
				var letter = $(this).attr("letter");
				strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
				strHTML += '<div class="removeBtnByResult label label-info" manfId="' + id + '"  letter="' + letter
						     + '" manfName="' + name + '" style="cursor:pointer" title="删除：' + name + '">';
				strHTML += '<i class="icon-remove icon-white"></i>' + name;
				strHTML += '</div>';
				strHTML += '</li>';
				$(".manfModalContainer .manfIdInput").each(function() {
					$(this).attr("checked");// 行全选
				});
				$(".manfModalContainer .manfIdInput").each(function() {
					if($(this).val() == id) {
						$(this).attr("checked", "true");// 行全选
					}
				});
			});
			strHTML += '</ul>';
			$("#selectorResultContainerByManf").html(strHTML);
		});
	});

	$(".manfModalContainer").find(".manfIdInput").live("click", function() {
		// 显示选中的值 ——开始
		// 去掉重复选项
		$("#selectorResultContainerByManf").html();
		var allObjArr = [];
		$(".manfModalContainer").find(".manfIdInput:checked").each(function() {
			var obj = {};
			obj.id = $(this).val();
			obj.name = $.trim($(this).parent().text());
			obj.letter = $(this).attr("letter");
			allObjArr[allObjArr.length] = obj;
		});
		var strHTML = '<ul class="inline" >';
		for (var i = 0; i < allObjArr.length; i++) {
			strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
			strHTML += '<div class="removeBtnByResult label label-info" manfId="' + allObjArr[i].id + '"  letter="' + allObjArr[i].letter + '" manfName="'
					     + allObjArr[i].name + '" style="cursor:pointer" title="删除：' + allObjArr[i].name + '">';
			strHTML += '<i class="icon-remove icon-white"></i>' + allObjArr[i].name;
			strHTML += '</div>';
			strHTML += '</li>';
		}
		strHTML += '</ul>';
		$("#selectorResultContainerByManf").html(strHTML);
		// 显示选中的值 ——结束
	});
    /** 展示厂商弹出框-结束 */
	
	/** 弹出品牌对话框*/
	$(".brandSelector").live("click", function() {
		currBrandLI = $("div[name = 'objectType4']");
	});
	
	$("#brandModal").on("show", function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//加载子车型数据
		showLoading("brandModalBody");
		$("#brandModalBody").load(ctx + "/policy/salePromotionAnalysis/getBrand", getParams(), function() {
			//弹出框设置默认选中项结果集	
			var strHTML = '<ul class="selectorResultContainer" >';
			$(currBrandLI).find(".brandModalResultContainer input").each(function() {
				var id = $(this).val();
				var name = $(this).attr("brandName");
				var letter = $(this).attr("letter");
				strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
			  	strHTML += '<div class="removeBtnByResult label label-info" brandId="' + id + '"  letter="' + letter +'" brandName="' + name + '" style="cursor:pointer" title="删除：' + name + '">';
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
	
	$(".brandModalContainer").find(".brandIdInput").live("click", function(e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//显示选中的值
		//去掉重复选项
		$("#brandModalResultContainer").html("");
		var allObjArr = [];
		$(".brandModalContainer").find(".brandIdInput:checked").each(function() {
			var obj = {}; 
			obj.id =  $(this).val();
			obj.name =  $.trim($(this).parent().text());
			obj.letter =  $(this).attr("letter");
			allObjArr[allObjArr.length] = obj;
		});
		var strHTML = '<ul class="selectorResultContainer" >';
		for(var i=0;i<allObjArr.length;i++){
			strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
		  		strHTML += '<div class="removeBtnByResult label label-info" brandId="' + allObjArr[i].id + '"  letter="' + allObjArr[i].letter + '" brandName="'
		  		             + allObjArr[i].name + '" style="cursor:pointer" title="删除：' + allObjArr[i].name + '">';
			  	strHTML += '<i class="icon-remove icon-white"></i>' + allObjArr[i].name;
		  		strHTML += '</div>';
		 		strHTML += '</li>';
		 }
		strHTML += '</ul>';
		//显示选中的值	——结束
		$("#selectorResultContainerByBrand").html("");
		$("#selectorResultContainerByBrand").html(strHTML);
	});
	
	/* 品牌鼠标点击删除 */
	$(".brandModalResultContainer ul div").live("click", function() {
		var currVal = $(this).attr("brandId");
		$(this).parents(".brandModalContainer").find(".brandIdInput").filter(function() {
			return $(this).val() == currVal;
		}).each(function() {
			$(this).removeAttr("checked");// 取消选中
		});
		$(this).parent().remove();
	});
	
	/** 弹出系别对话框*/
	$(".origSelector").live("click", function() {
		currOrigLI = $("div[name = 'objectType5']");
	});
	
	/**展示系别弹出框*/
	$("#origModal").on("show", function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//去掉默认选中的效果
		$(".origModalByAll").each(function(){
			$(this).removeAttr("checked");//取消行全选
		});
		//加载子车型数据
		showLoading("origModalBody");
		$("#origModalBody").load(ctx + "/policy/salePromotionAnalysis/getOrig", getParams(), function() {
			//弹出框设置默认选中项结果集		
			$(currOrigLI).find("input").each(function() {
				var origId = $(this).val();
				if(origId == "0") {
					$(".origModalByAll").each(function() {
						$(this).attr("checked", "true");//全选
					});
					$(".origModalContainer .origModal").each(function() {
						$(this).attr("checked", "true");//行全选
					});
				} else {
					$(".origModalContainer .origModal").each(function() {
						if($(this).val() == origId) {
							$(this).attr("checked","true");//行全选
						}
					});
				}
			});
		});
	});
	
	/**点击确定生成内容*/
	$(".origModalContainer").find(".confirm").live("click", function() {
		var containerId = $(this).parents(".origModalContainer").attr("id");
		var relInputName = $(this).attr("relInputName");
		var strHTML = "";
		strHTML += '<ul class="selectorResultContainer">';
		//如果全部选中，则生成全部
		if($(".origModalByAll").attr("checked")) {
			strHTML += '<li>';
			strHTML += '<div class="removeBtn" relContainer="origModal" value="0" style="cursor:pointer;" title="整体市场">';
			strHTML += '<input type="hidden" value="0" name="selectedOrig" />';
			strHTML += '整体市场<i class="icon-remove" style="visibility: hidden;"></i>';
			strHTML += '</div>';
			strHTML += '</li>';
			
			//部分模块存在拆分对象情况,当车型为全选时不允许拆分
			$("#origModalResultContainer").find("tbody td input[name='obj_Split']").attr("checked", false);
			$("#origModalResultContainer").find("tbody td input[name='obj_Split']").attr("disabled", true);
		} else {							
			$(this).parents(".origModalContainer").find(".origModal:checked").each(function() {
				strHTML += '<li>';
				strHTML += '<div class="removeBtn" relContainer="' + containerId + '" value="' + $(this).val() + '" style="cursor:pointer" title="删除：' + 
				              $.trim($(this).parent().text()) + '">';
				strHTML += '<input type="hidden" value="' + $(this).val() + '" name="' + relInputName + '" levelType="1" />';
				strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
				strHTML += '</div>';
				strHTML += '</li>';
			});
			
			//部分模块存在拆分对象情况,当车型为全选时不允许拆分
			$("#origModalResultContainer").find("tbody td input[name='obj_Split']").attr("disabled", false);
		}
		strHTML += '</ul>';		
		$("#origModalResultContainer").html(strHTML);
	});
	
	/** 弹出细分市场(级别)对话框*/
	$(".segmentSelector").live("click", function() {	
		currSegmentLI = $("div[name = 'objectType6']");	
	});
	
	/** 展示细分市场弹出框*/
	$("#segmentModal").on("show", function (e) {		
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//去掉默认选中的效果
		$(".segmentModalByAll").each(function() {
			$(this).removeAttr("checked");//取消行全选
		});
		//加载子车型数据
		showLoading("segmentModalBody");
		$("#segmentModalBody").load(ctx + "/policy/salePromotionAnalysis/getSegmentAndChildren", getParams(), function() {
			//弹出框设置默认选中项结果集		
			$(currSegmentLI).find("input").each(function() {
				var segmentId = $(this).val();
				if(segmentId == "0") {
					$('.segmentModalByAll').each(function() {
						$(this).attr("checked", "true");//全选
					});
					$(".segmentModalContainer .segmentModalByLevel1").each(function() {
						$(this).attr("checked", "true");//行全选
					});
			 		$(".segmentModalContainer .segmentModalByLevel2").each(function() {
						$(this).attr("checked", "true");//行全选
					});
				} else {
					$(".segmentModalContainer .segmentModalByLevel1").each(function() {
						if($(this).val() == segmentId) {
							$(this).attr("checked", "true");//行全选
						}
					});
			 		$(".segmentModalContainer .segmentModalByLevel2").each(function() {
						if($(this).val() == segmentId) {
							$(this).attr("checked", "true");//行全选
						}
					});
				}
			});
		});
	});
	
	
	/**点击确定生成内容*/
	$(".segmentModalContainer").find(".confirm").live("click", function() {
		var containerId = $(this).parents(".segmentModalContainer").attr("id");
		var relInputName = $(this).attr("relInputName");
		var strHTML = '<ul class="selectorResultContainer">';
		//如果第一级分细市场全部选中，则生成整体市场
		if($("#myModalLabel :checkbox").attr("checked")) {
			strHTML += '<li>';
			strHTML += '<div class="removeBtn" relContainer="segmentModal" value="0" style="cursor:pointer;" title="整体市场">';
			strHTML += '<input type="hidden" value="0" name="selectedSegment" />';
			strHTML += '整体市场<i class="icon-remove" style="visibility: hidden;"></i>';
			strHTML += '</div>';
			strHTML += '</li>';
			
			//拆分选项不可选
			$("#segmentModalResultContainer").find("tbody td input[name='obj_Split']").attr("checked", false);
			$("#segmentModalResultContainer").find("tbody td input[name='obj_Split']").attr("disabled", true);
		} else {							
			$(this).parents(".segmentModalContainer").find(".selectorTR").each(function() {
				var level2RowTotalAmount = $(this).find(".segmentModalByLevel2").length;
				var level2RowSelectedAmount = $(this).find(".segmentModalByLevel2:checked").length;
				$(this).find(".segmentModalByLevel2:checked").each(function() {
					strHTML += '<li>';
					strHTML += '<div class="removeBtn" relContainer="' + containerId + '" value="' + $(this).val() + '" style="cursor:pointer" title="删除：'
					               + $.trim($(this).parent().text()) + '">';
					strHTML += '<input type="hidden" value="' + $(this).val() + '" name="' + relInputName + '"  levelType="2" />';
					strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
					strHTML += '</div>';
					strHTML += '</li>';
				});
			});
			
			//拆分选项可选
			$("#segmentModalResultContainer").find("tbody td input[name='obj_Split']").attr("disabled", false);
		}
		strHTML += '</ul>';
		$("#segmentModalResultContainer").html(strHTML);
	});
	
	/** 弹出车身形式(级别)对话框*/
	$(".bodyTypeSelector").live("click", function(){		
		currBodyTypeLI = $("#bodyTypeControlGroup");		
	});
	
	/**展示车身形式弹出框*/
	$("#bodyTypeModal").on("show", function (e) {
		if(e.relatedTarget) {
			return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		}
		//去掉默认选中的效果
		$(".bodyTypeModalByAll").each(function() {
			$(this).removeAttr("checked");//取消行全选
		});
		//加载子车型数据
		showLoading("bodyTypeModalBody");
		$("#bodyTypeModalBody").load(ctx + "/policy/salePromotionAnalysis/getBodyType", getParams(), function() {
			//弹出框设置默认选中项结果集		
			$(currBodyTypeLI).find("input").each(function() {
				var bodyTypeId = $(this).val();
				if(bodyTypeId == "0") {
					$(".bodyTypeModalByAll").each(function() {
						$(this).attr("checked", "true");//全选
					});
					$(".bodyTypeModalContainer .bodyTypeModal").each(function() {
						$(this).attr("checked", "true");//行全选
					});
				} else {
					$(".bodyTypeModalContainer .bodyTypeModal").each(function() {
						if($(this).val() == bodyTypeId) {
							$(this).attr("checked", "true");//行全选
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
	
	/**
	 * 对象类型改变(车型,型号,常用组)清空,且显示对象改变
	 */
	 $("#objectType").on("change", function () {
		 //当页面条件是非型号时显示正斜对应选择项
	 	 var type =$("#objectType").val();
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
		 $(".control-group[name^='objectType']").css("display", "none");
		 if(type == 1) {
		 	$(".control-group[name='objectType1']").css("display", "block");
		 } else if(type == 2) {
		 	$(".control-group[name='objectType1']").eq(0).css("display", "block");
		 } else if(type == 3) {
		 	$(".control-group[name='objectType3']").css("display", "block");
		 } else if(type == 4) {
		 	$(".control-group[name='objectType4']").css("display", "block");
		 } else if(type == 5) {
		 	$(".control-group[name='objectType5']").css("display", "block");		 	
		 	$(".control-group[name='objectType5']").after(bodyTypeContainerHTML);		 	
		 } else if(type == 6) {
		 	$(".control-group[name='objectType6']").css("display", "block");		 	
		 	$(".control-group[name='objectType6']").after(bodyTypeContainerHTML);
		 }
	});
					
	/** 重置按钮事件-开始 */
    $("#resetBtn").on("click", function (e) {
		//车型容器
		$("#subModelModalResultContainer").html("");
		//型号容器
		$("#versionModalResultContainer").html("");
		//常用选项容器
		$("#autoVersionModalResultContainer").html("");
		//生产商容器
		$("#manfModalResultContainer").html("");
		//品牌容器
		$("#brandModalResultContainer").html("");
		//系别容器
		$("#origModalResultContainer").html("");
		//级别容器
		$("#segmentModalResultContainer").html("");
		$("#bodyTypeModalResultContainer").html("");  //清空车身形式
		$("#bodyTypeControlGroup").remove();
		$(".control-group[name^='objectType']").css("display", "none");
		$(".control-group[name='objectType1']").eq(0).css("display", "block");
		$("#formId :reset");	
	});
	/** 重置按钮事件-结束 */

	/** 查询按钮事件-开始 */
	$("#queryBtn").on("click", function (e) { 
		var params = getParams(); 
  	    if(paramsValidate(params)) {
  	    	return;
  	    }
	    $("body").showLoading(); 
	    //默认展示图表
	    $("#chartTitleDiv").height(500);
	    //发送请求 
	    $.ajax({ type: "POST",
	             url: ctx + "/policy/salePromotionAnalysis/loadChartAndTable", 
	             data: params,
	             success: function(data) {
	  			     if(data) {
  					     $("#chartTitleDivNoData").hide();
  					     $("#tSortableNoData").hide();
  					     $("#chartTitleDiv").show();
  					     $("#tSortable").show();
  						 
  					     //查询面板折叠
  					     $(".queryConditionContainer .buttons .toggle a").click();
  					     $("")
  					     paint(data);
  					     saveValue(data.tickList);
  						 showObjectGrid(data); 
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

	/**保存图形各个数据刻度最大值和最小值**/
	function saveValue(tickList) {
		var saleTick = tickList[0];
		var promotionTick = tickList[1];
	    $("#saleMax").val(saleTick[0]);
	    $("#saleMin").val(saleTick[1]);
	    $("#promotionMax").val(promotionTick[0]);
	    $("#promotionMin").val(promotionTick[1]);
	}
	
	/**画图**/
	function paint(data) {
		var titles = data.xTitles;
		var tickList = data.tickList;
		var saleData = changeToNumber(data.saleData);
		var promotionData = changeToNumber(data.promotionData);
		var saleNames = data.saleNames;
		var promotionNames = data.promotionNames;
		var legend = getLegend(saleNames, promotionNames);
		var startNumber = getStartNumber(data.objNames, titles);
		//先清空图表所在的div(兼容IE8)
		var chart = $("#chartTitleDiv");
		chart.html("");
		var myChart = echarts.init(document.getElementById("chartTitleDiv"));
		var allData = getData(saleNames, promotionNames, saleData, promotionData);
		option = {
		    "grid": {y: 40, y2: 60, x: 70, x2: 80},
		    "tooltip": {
		        "trigger": "axis",
		        "axisPointer": {           
		            "type": "line"  
		        }
		    },
		    "legend": {
		    	"padding": [0, 120, 50, 120],
		        "data": legend,
		        "itemGap": 5,
		        "itemWidth": 16
		    },
		    "dataZoom": {
                "show": true,
                "realtime": false,
                "start": startNumber,
                "end": 100,
                "height": 35,
                "zoomLock": true,
                "fillerColor": "rgba(174,212,248,0.4)",
                "borderColor": "rgba(174,212,248)"
            },
		    "xAxis": [
		        {
		            "type": "category",
		            "splitLine": {
		                "show": false
		            },
		            "data": titles,
		            "axisTick": {
		                "show": false
		            },
		            "axisLine": {
		            	"show": true,
		            	"onZero": false
		            },
		            "axisLabel": {
		                "interval": 0
		            }
		        }
		    ],
		    "yAxis": [
		        {
		            "type": "value",
		            "splitNumber": parseInt(tickList[0][2]),
		            "max": parseInt(tickList[0][0]),
		            "min": parseInt(tickList[0][1]),
		            "splitLine": {
		                "show": false
		            },
		            "name": "销量/辆",
		            "nameTextStyle": {
		                "color": "#000",
		                "fontFamily": "微软雅黑",
		                "align": "right"
		            },
		            "axisLine": {
		            	"show": true,
		            	"onZero": false
		            }
		        },
		        {
		            "type": "value",
		            "splitNumber": parseInt(tickList[1][2]),
		            "max": parseInt(tickList[1][0]),
		            "min": parseInt(tickList[1][1]),
		            "splitLine": {
		                "show": false
		            },
		            "name": "促销/元",
		            "nameTextStyle": {
		                "color": "#000",
		                "fontFamily": "微软雅黑",
		                "align": "left"
		            },
		            "axisLine": {
		            	"show": true,
		            	"onZero": false
		            }
		        }
		    ],
		    "series": allData,
		    "animation": true,
		    "backgroundColor": "#ffffff"
		} 
		myChart.clear();
//		console.log(JSON.stringify(option));
		myChart.setOption(option);
	}

	/**获取要显示的数据**/
	function getData(saleNames, promotionNames, saleData, promotionData) {
		var newSeries = new Array();
		var colorGroup = ["0,35,90", "174,212,248", "187,194,197", "226,103,0", "0,156,14","74,111,138", "137,148,160", "98,197,226", "249,167,0", 
		                  "187,198,0", "146,10,106", "230,49,16", "0,93,91", "250,206,0", "119,0,29"];
		var count = 0;
		var zLevel = 1000;
        for(var i = 0; i < saleData.length; i++) {
        	var color = "";
        	if(i < colorGroup.length) {
        		color = "rgb(" + colorGroup[i] + ")";
        	} else {
        		color = "rgb(" + colorGroup[colorGroup.length - (i - colorGroup.length + 1)] + ")";
        	}
    		var obj = 
    		{
				"name": saleNames[i],
				"type": "bar",
				"barWidth": 30,
				"barMaxWidth": 40,
				"barGap": 0,
    			"z": zLevel--,
    			"itemStyle": {
    				"normal": {
    					"label": {
	    					"show": true,
	    					"position": "top",
	    					"textStyle": {
	    						"color": "#000"
	    					}
    	    			},
    					"color": color
    				}
    			},
				"yAxisIndex": 0,
				"data": saleData[i]
    		}
    		newSeries[count] = obj;
    		count++;
        }          
        for(var i = 0; i < promotionData.length; i++) {
    		var obj = 
    		{
    			"name": promotionNames[i],
    			"type": "line",
    			"symbol": "circle",
    			"itemStyle": {
    				"normal": {
    					"label": {
	    					"show": true,
	    					"position": "top",
	    					"textStyle": {
	    						"color": "#000"
	    					}
    	    			},
    					"color": "rgb(" + colorGroup[i] + ")"
    				}
    			},
    			"yAxisIndex": 1,
    			"data": promotionData[i]
    		}
    		newSeries[count] = obj;
    		count++;
        }     
		return newSeries;
	}
	
	/**把字符串数组转换成数字数组**/
	function changeToNumber(array) {
		var newArray = new Array(array.length);
		for(var i = 0; i < array.length; i++) {
			var arr = array[i];
			for(var k = 0; k < arr.length; k++) {
				if(arr[k] != "-") {
					arr[k] = parseInt(arr[k]);
				}
			}
			newArray[i] = arr;
		}
		return newArray;
	}
	
	/**获取图例组件**/
	function getLegend(saleNames, promotionNames) {
		var legend = new Array(saleNames.length + promotionNames.length);
		for(var i = 0; i < saleNames.length; i++) {
			legend[i] = saleNames[i];
			legend[i + saleNames.length] = promotionNames[i];
		}
		return legend;
	}
	
	/**获取滚动条开始比例数值**/
	function getStartNumber(objNames, titles) {
		var startNumber = 50;
		var namesSize = objNames.length - 5;
		if(namesSize % 2 == 0) {
			startNumber += namesSize * 5;
		} else {
			startNumber += (namesSize + 1) * 5;
		}
		if(startNumber >= 100) {
			startNumber = 90;
		}
		return startNumber;
	}
	
	/**
	 * 画车型或厂商、品牌、系别、级别表格
	 */
	function showObjectGrid(data) {
		var objectType = $("#objectType").val();
		$("#dataThead").removeClass("hide");
		var objectTypeName = $("#objectTypeName");
		if(objectType == 1) {
			objectTypeName.text("型号名称");	
		} else if(objectType == 2) {		
			objectTypeName.text("车型名称");	
		} else if(objectType == 3) {		
			objectTypeName.text("厂商品牌");
		} else if(objectType == 4) {		
			objectTypeName.text("品牌");
		} else if(objectType == 5) {		
			objectTypeName.text("系别");
		} else {
			objectTypeName.text("级别");
		}
		var tbodyHtml = "";
		var resultList = data.resultList;
		if(resultList) {
			for(var i = 0; i < resultList.length; i++) {
				var dataObj = resultList[i];
				var className = "";
				if(0 == i % 2) {
					className = "odd";
				}
				tbodyHtml += "<tr>";
				tbodyHtml += "<td style='width:14%;text-align: center;' class='text-right " + className + "'>" + dataObj.ym + "</td>";
				tbodyHtml += "<td style='width:16%;text-align: center;' class='text-right " + className + "'>" + dataObj.objName + "</td>";
				tbodyHtml += "<td style='width:14%;text-align: center;' class='text-right " + className + "'>" + formatData(dataObj.totalPromotion) + "</td>";
				tbodyHtml += "<td style='width:14%;text-align: center;' class='text-right " + className + "'>" + formatData(dataObj.versionSale) + "</td>";
				tbodyHtml += "</tr>";
			}
			$("#gridTbody").html(tbodyHtml);
		}
	}	
	
	/**
	 * 获取页面请求参数
	 */
	window.getParams = function() {
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var objectType = $("#objectType").val();
		var segmentType = $(".segmentType").map(function() {
			if($(this).prop("checked")) {
				return $(this).val();
			} else {
				return null;
			}
		}).get().join(",");// 细分市场类别
		var autoVidLength = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").length;
		var versionIds = "";
		var versionNames = "";
		if(autoVidLength > 0) {
			versionIds = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").map(function() {return $(this).val();}).get().join(",");
			versionNames = $("#autoVersionModalResultContainer .removeBtn").map(function() {return $(this).text();}).get().join(",");
		} else {
			versionIds = $("#versionModalResultContainer input[name='selectedVersion']").map(function() {return $(this).val();}).get().join(",");
			versionNames = $("#versionModalResultContainer .removeBtn").map(function() {return $(this).text();}).get().join(",");
		}
		var subModelIds = $("#subModelModalResultContainer input[name='selectedSubModel']").map(function() {return $(this).val();}).get().join(",");
		var manfIds = $("#manfModalResultContainer input[name='selectedManf']").map(function() {var value1 = $(this).val();return "'" + value1 + "'";}).get().join(",");
		var brandIds = $("#brandModalResultContainer li div input").map(function() {return $.trim($(this).attr("value"));}).get().join(",");
		var origIds = $("#origModalResultContainer li div").map(function() {return $.trim($(this).attr("value"));}).get().join(",");
		var gradeIds = $("#segmentModalResultContainer li div").map(function() {return $.trim($(this).attr("value"));}).get().join(",");
		var bodyTypeIds = $("#bodyTypeModalResultContainer li div").map(function() {return $.trim($(this).attr("value"));}).get().join(",");
		var paramObj = {};
		paramObj.beginDate = beginDate;
		paramObj.endDate = endDate;
		paramObj.subModelIds = subModelIds;
		paramObj.versionIds = versionIds;
		paramObj.versionNames = versionNames;
		paramObj.manfIds = manfIds;
		paramObj.bodyTypeIds = bodyTypeIds;
		paramObj.brandIds = brandIds;
		paramObj.origIds = origIds;
		paramObj.gradeIds = gradeIds;
		paramObj.objectType = objectType;
		paramObj.inputType = "1";
		paramObj.segmentType = segmentType;
		getQueryConditionAndBrowser(paramObj);
		return paramObj;
	}

	/**
	 * 获取页头查询条件，以及浏览器
	 */
	function getQueryConditionAndBrowser(paramObj) {
		paramObj.browser = navigator.appVersion;
		var queryCondition = "";
		queryCondition += "\n对象类型 = ";
		if("1" == $("#objectType").val()) {
			queryCondition += "型号";
		} else if("2" == $("#objectType").val()) {
			queryCondition += "车型";
		} else if("3" == $("#objectType").val()) {
			queryCondition += "厂商品牌";
		} else if("4" == $("#objectType").val()) {
			queryCondition += "品牌";
		} else if("5" == $("#objectType").val()) {
			queryCondition += "系别";
		} else {
			queryCondition += "级别";
		}
		queryCondition += "\n时间 = " + $("#startDate").val() + "至" + $("#endDate").val();
		queryCondition += "\n级别 = " + $("#segmentModalResultContainer li div").map(function() {return $.trim($(this).text());}).get().join(",");
		queryCondition += "\n系别 = " + $("#origModalResultContainer li div").map(function() {return $.trim($(this).text());}).get().join(",");
		queryCondition += "\n品牌 = " + $("#brandModalResultContainer li div input").map(function() {return $.trim($(this).text());}).get().join(",");
		queryCondition += "\n厂商品牌 = " + $("#manfModalResultContainer li div").map(function() {return $.trim($(this).text());}).get().join(",");
		queryCondition += "\n车型 = " + $("#subModelModalResultContainer li div").map(function() {return $.trim($(this).text());}).get().join(",");
		paramObj.queryCondition = queryCondition;
	}

	$("#subModelModalResultContainer").html("");
	$("#versionModalResultContainer").html("");
	$("#versionModalBody").html("");
	$("#autoVersionModalResultContainer").html("");
	$("#manfModalResultContainer").html("");
	$("#brandModalResultContainer").html("");
	$("#origModalResultContainer").html("");
	$("#segmentModalResultContainer").html("");
	$("#bodyTypeModalResultContainer").html("");
	$("#bodyTypeControlGroup").remove();
	$(".control-group[name^='objectType']").css("display", "none");
	$(".control-group[name='objectType1']").eq(0).css("display", "block");

	/**
	 * 车型弹出框
	 * 
	 * @param type
	 */
	function showSubModel(type) {
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
			// 获取时间
			var beginDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var hatchbackId = "";
			// 传递参数
			var params = {
				inputType : inputType,
				subModelShowType : type,
				beginDate : beginDate,
				endDate : endDate,
			};
			// 触发请求
			showLoading(id);
			$("#" + id).load(ctx + "/policy/salePromotionAnalysis/getSubmodelModal", params, function() {
				$("#selectorResultContainerBySubModel .removeBtnByResult").each(function() {
					var subModelId = $(this).attr("subModelId");
					$(".subModelModalContainer .subModelIdInput").each(function() {
						if($(this).val() == subModelId) {
							$(this).attr("checked", "true");// 行全选
						}
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
		}
	};
	
	/**校验是否已选择对象**/
	function paramsValidate(params) {
		var objectType = params.objectType;
		var beginDate = params.beginDate;
		var endDate = params.endDate;
		var subModelIds = params.subModelIds;
		var versionIds = params.versionIds;
		var manfIds = params.manfIds;
		var brandIds = params.brandIds;
		var origIds = params.origIds;
		var gradeIds = params.gradeIds;
		var flag = false;
		if(1 == objectType) {
			if("" == versionIds && "" == subModelIds) {
				alert("请选择车型");
				flag = true;
			} else if("" == versionIds && "" != subModelIds) {
				alert("请选择型号");
				flag = true;
			}
		} else if(2 == objectType) {
			if("" == subModelIds) {
				alert("请选择车型");
				flag = true;
			}
		} else if(3 == objectType) {
			if("" == manfIds) {
				alert("请选择厂商品牌");
				flag = true;
			}
		} else if(4 == objectType) {
			if("" == brandIds) {
				alert("请选择品牌");
				flag = true;
			}
		} else if(5 == objectType) {
			if("" == origIds) {
				alert("请选择系别");
				flag = true;
			}
		} else if(6 == objectType) {
			if("" == gradeIds) {
				alert("请选择级别");
				flag = true;
			}
		}
	
		if(parseInt(beginDate.replace("-", "")) > parseInt(endDate.replace("-", ""))) {
			alert("开始时间不能大于结束时间");
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
		if(!$.trim($("#gridTbody").html())) {
			alert("暂无数据导出");
			return;
		}
		var paramObj = getParams();
		$("#ex_beginDate").val(paramObj.beginDate);
		$("#ex_endDate").val(paramObj.endDate);
		$("#ex_subModelIds").val(paramObj.subModelIds);
		$("#ex_versionIds").val(paramObj.versionIds);
		$("#ex_manfIds").val(paramObj.manfIds);
		$("#ex_brandIds").val(paramObj.brandIds);
		$("#ex_origIds").val(paramObj.origIds);
		$("#ex_gradeIds").val(paramObj.gradeIds);
		$("#ex_bodyTypeIds").val(paramObj.bodyTypeIds);
		$("#ex_objectType").val(paramObj.objectType);
		$("#languageType").val(languageType);
		$("#exportFormId").submit();
	};