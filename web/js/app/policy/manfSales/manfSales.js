/**定义细分市场LI全局变量*/
var currSegmentLI = null;
/**定义厂商LI全局变量*/
var currManfLI = null;
/**定义品牌LI全局变量*/
var currBrandLI = null;
/**定义子车型LI全局变量*/
var currSubModelLI = null;
/**定义车身形式LI全局变量*/
var currBodyTypeLI = null;
/**定义车系LI全局变量*/
var currOrigLI = null;
/**细分市场请求路径 **/
var segmentPath = "/manfSales/global/getSegmentAndChildren";

var CXZE = "促销总额";
var TCZC = "提车支持(STD支持)";
var LSZC = "零售支持(AaK支持)";
var RYJL = "人员奖励";
var JRDK = "金融贷款";
var ZHZC = "置换支持";
var ZSBX = "赠送保险";
var ZSLP = "赠送礼品(油卡、保养)";

var subsidyTypeArray = new Array(CXZE,TCZC,LSZC,RYJL,JRDK,ZHZC,ZSBX,ZSLP);



$(document).ready(function(){
	
	//隐藏图例
	$("#tabs-1").attr("style","display:none");
	
	//默认选中厂商品牌
	var objectType = $("#objectType option:selected").val();
	$("#objectContainer").children().hide();
	$("#objectContainer").children().find(".selectorResultContainer").remove();
	//对象类型为厂商品牌时展示厂商品牌对象弹出框
	$("#objectContainer").children().eq(3).show();
	
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
	$("#startDate-container.input-append.date").datetimepicker("update", endDate);
	//时间改变事件
    $('#startDate-container.input-append.date').on('changeDate',function(){
    	checkPopBoxData();
    });
	
  //默认选择季度
	var beginDateTime=$("#startDate").val();
	var endDateTimeYear=endDate.substr(0,4);
	$("#startDate-container.input-append.date").datetimepicker("remove");
		beginDateTime = endDateTimeYear;
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
	$("#startDate-container.input-append.date").datetimepicker("update", beginDateTime);
	$("#seasonType").attr("style","display:block;width:116px;");
	
	//默认隐藏季度
	var month=endDate.substr(5,7);
	if(month >= 1 && month <= 3){
		$("#seasonType").html("<option value='1'>Q1</option>");
	} else if(month >= 4 && month <= 6){
		$("#seasonType").html("<option value='1'>Q1</option><option value='2' selected='selected'>Q2</option>");
	} else if(month >= 7 && month <= 9){
		$("#seasonType").html("<option value='1'>Q1</option><option value='2'>Q2</option><option value='3' selected='selected'>Q3</option>");
	} else {
		$("#seasonType").html("<option value='1'>Q1</option><option value='2'>Q2</option><option value='3'>Q3</option><option value='4' selected='selected'>Q4</option>");
	}
	
    /**频次选择框改变事件开始*/
    $('#frequencyType').on('change',function(){
    	$("#seasonType").attr("style","display:none");
    	var frequencyType = $("#frequencyType option:selected").val();
    	var beginDateTime = $("#startDate").val();
    	var endDateTimeYear=endDate.substr(0,4);
    	var endDateTimeMonth=endDate.substr(4,7);
    	var startDateObj = $("#startDate-container.input-append.date");
    	//年
    	if(frequencyType == "3") {
    		$(startDateObj).datetimepicker("remove");
    		if(beginDateTime.indexOf("-") != -1) {
    			beginDateTime = endDateTimeYear;
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
    		$(startDateObj).datetimepicker("update", beginDateTime);
    		//季
    	} else if(frequencyType == "2"){
    		$(startDateObj).datetimepicker("remove");
    		if(beginDateTime.indexOf("-") != -1) {
    			beginDateTime = endDateTimeYear;
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
    		$(startDateObj).datetimepicker("update", beginDateTime);
    		$("#seasonType").attr("style","display:block;width:116px;");
    		//月
    	} else {
    		$(startDateObj).datetimepicker("remove");
    		if(beginDateTime.indexOf("-") == -1) {
    			beginDateTime += endDateTimeMonth;
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
    	    $(startDateObj).datetimepicker("update", beginDateTime);
    	}
    });
    /**频次选择框改变事件结束*/
    
	/**
	 * 根据所选对象类型展示相应对象弹出框
	 * 对象类型为级别与系别时展示车身形式框
	 * objectType下标
	 * 0 型号;1 车型;2 生产商品牌;3 品牌;
	 * 4 系别;5 细分市场;
	 * 
	 * objectModal
	 * eq(0):车型;eq(1):型号;eq(2):常用对象;
	 * eq(3)生产商品牌;eq(4)品牌;eq(5)系别;
	 * eq(6)细分市场;eq(7)车身形式
	 */
	$('#objectType').on('change',function(){
		 $("#chartId").html("");//清空图
		 $("#chartTable").html("");//清空图表
		var objectType = $(this).val();
		$("#objectContainer").children().hide();
		$("#objectContainer").children().find(".selectorResultContainer").remove();
		
		if(objectType == 0){
			//对象类型为型号时展示车型,型号,常用对象弹出框
			$("#objectContainer").children().eq(0).show();
			$("#objectContainer").children().eq(1).show();
			$("#objectContainer").children().eq(2).show();
		}else if(objectType == 1){
			//对象类型为车型时展示车型对象弹出框
			$("#objectContainer").children().eq(0).show();
		}else if(objectType == 2){
			//对象类型为厂商品牌时展示厂商品牌对象弹出框
			$("#objectContainer").children().eq(3).show();
		}else if(objectType == 3){
			//对象类型为品牌时展示品牌对象弹出框
			$("#objectContainer").children().eq(4).show();
		}else if(objectType == 4){
			//对象类型为系别时展示系别,车身形式对象弹出框
			$("#objectContainer").children().eq(5).show();
			$("#objectContainer").children().eq(7).show();
		}else{
			//对象类型为细分市场时展示细分市场,车身形式对象弹出框
			$("#objectContainer").children().eq(6).show();
			$("#objectContainer").children().eq(7).show();
		}
	});
	
	//季度时年份改变Q季度重置最新
	$("#startDate").on("change",function(){
		//季度维度，最新年份时
		if($("#frequencyType").val() == 2 && $("#startDate").val() == endDate.substr(0,4)){
			var month=endDate.substr(5,7);
			if(month >= 1 && month <= 3){
				$("#seasonType").html("<option value='1'>Q1</option>");
			} else if(month >= 4 && month <= 6){
				$("#seasonType").html("<option value='1'>Q1</option><option value='2' selected='selected'>Q2</option>");
			} else if(month >= 7 && month <= 9){
				$("#seasonType").html("<option value='1'>Q1</option><option value='2'>Q2</option><option value='3' selected='selected'>Q3</option>");
			} else {
				$("#seasonType").html("<option value='1'>Q1</option><option value='2'>Q2</option><option value='3'>Q3</option><option value='4' selected='selected'>Q4</option>");
			}
		} else{
			//全部显示
			$("#seasonType").html("<option value='1'>Q1</option><option value='2'>Q2</option><option value='3'>Q3</option><option value='4'>Q4</option>");
		}
		
	})
	
	/**展示车型弹出框-开始*/
	$('#subModelModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		
		//加载车型数据
		showLoading("subModelModalBody");
		$('#subModelModalBody').load(ctx+"/manfSales/global/getSubmodelModal",getParams(),function(){
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
	/**展示车型弹出框-结束*/
	
	
	/**展示型号弹出框-开始*/
	$('#versionModal').on('show', function (e) {	
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
		if(e.relatedTarget || 0 == $("#subModelModalResultContainer input[name='selectedSubModel']").length)  return; 
		//加载子车型下型号数据
		showLoading("versionModalBody");
		$('#versionModalBody').load(ctx+"/manfSales/global/getVersionModalByCommon",getParams(),function(){
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
	/**展示型号弹出框-结束*/
	
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
	})
	
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
	})
	
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
	})
	
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
	})
		
	
	
	/**展示常用对象弹出框-开始*/
	$('#autoVersionModal').on('show', function (e) {	
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
		if(e.relatedTarget)  return; 
		//加载子常用对象下型号数据
		showLoading("autoVersionModalBody");
		$('#autoVersionModalBody').load(ctx+"/manfSales/global/getAutoCustomGroup",getParams(),function(){
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
	/**展示常用对象弹出框-结束*/
	
	
	/**展示细分市场对话框-开始*/
	$("a[name ='segmentSelector']").live('click',function(){
		currSegmentLI = $("div[name = 'objectModal']").eq(6);
	});
	
	$('#segmentModal').on('show', function (e) {
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择细分市场
		if(e.relatedTarget)  return;
		
		$('.segmentModalByAll').each(function(){
			$(this).removeAttr("checked");//取消行全选
		});
		//加载细分市场数据
		showLoading("segmentModalBody");
		$('#segmentModalBody').load(ctx+"/manfSales/global/getSegmentAndChildren",getParams(),function(){
			//隐藏级别全选框
			$("#segmentModal #myModalLabel input").attr("type","hidden");
			//弹出框设置默认选中项结果集		
			var segmentId = $("#segmentModalResultContainer .selectorResultContainer input").val();
			 		$("#segmentModalBody .segmentModalByLevel2").each(function(){
						if($(this).val() == segmentId){
							$(this).attr("checked",'checked');//行全选
						}
					});
		});
	});
	/**展示细分市场对话框-结束*/
	
	
	/**展示系别弹出框-开始*/
	$("a[name ='origSelector']").live('click',function(){
		currOrigLI = $("div[name = 'objectModal']").eq(5);
		$('#origModal').modal('show');
	});
	
	/**展示系别弹出框*/
	$('#origModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//去掉默认选中的效果
		$('.origModalByAll').each(function(){
			$(this).removeAttr("checked");//取消行全选
		});
		//加载系别数据
		showLoading("origModalBody");
		$('#origModalBody').load(ctx+"/manfSales/global/getOrig",getParams(),function(){
			//把车系全选checkbox去掉
			$("#myModalLabel label input").attr("type","hidden");
			//弹出框设置默认选中项结果集
			var origLength = $('#origModalBody ul li').length;
			$(currOrigLI).find('input').each(function(){
				var origId = $(this).val();
				if(origLength == $(currOrigLI).find('input').length){
					$('.origModalByAll').each(function(){
						$(this).attr("checked",'true');//全选
					});
					$(".origModalContainer .origModal").each(function(){
						$(this).attr("checked",'true');//行全选
					});
				}else{
					$(".origModalContainer .origModal").each(function(){
						if($(this).val() == origId){
							$(this).attr("checked",'true');//行全选
						}
					});
				}
			});
		});
	});
	
	/**点击确定生成内容   级别*/
	$(".segmentModalContainer").find(".confirm").live("click",function(){
		var containerId = $(this).parents(".segmentModalContainer").attr("id");
		var relInputName = $(this).attr("relInputName");
		var strHTML = "";
		strHTML += '<ul class="selectorResultContainer">';
		//如果第一级分细市场全部选中，则生成整体市场
		if($("#myModalLabel :checkbox").attr("checked")) {
			var checkedBox = $(".modal-header #segmentType").find(":checkbox").filter(":checked");
			if(checkedBox.length < 3) {
				for(var i = 0; i < checkedBox.length; i++) {
					var segmentValue;
					var segmentName;
					var obj = $(checkedBox[i]);
					if("Volume" == obj.val()) {
						segmentValue = 1;
						segmentName = "Volume整体市场";
					} else if("Economy" == obj.val()) {
						segmentValue = 2;
						segmentName = "Economy整体市场";
					} else {
						segmentValue = 3;
						segmentName = "Premium整体市场";
					}
					strHTML += '<li>';
					strHTML += '<div class="removeBtn" relContainer="segmentModal" value="' + segmentValue + '" style="cursor:pointer;" title="' + segmentName + '">';
					strHTML += '<input type="hidden" value="' + segmentValue + '" name="selectedSegment" />';
					strHTML += segmentName + '<i class="icon-remove" style="visibility: hidden;"></i>';
					strHTML += '</div>';
					strHTML += '</li>';
				}
				//拆分选项不可选
				$(currSegmentLI).find("tbody td input[name='obj_Split']").attr('checked',false);
				$(currSegmentLI).find("tbody td input[name='obj_Split']").attr('disabled',true);
			} else {
				strHTML += '<li>';
				strHTML += '<div class="removeBtn" relContainer="segmentModal" value="0" style="cursor:pointer;" title="整体市场">';
				strHTML += '<input type="hidden" value="0" name="selectedSegment" />';
				strHTML += '整体市场'
				strHTML += '<i class="icon-remove" style="visibility: hidden;"></i>';
				strHTML += '</div>';
				strHTML += '</li>';
			}
			//拆分选项不可选
			$(currSegmentLI).find("tbody td input[name='obj_Split']").attr('checked',false);
			$(currSegmentLI).find("tbody td input[name='obj_Split']").attr('disabled',true);
		} else {
			$(this).parents(".segmentModalContainer").find(".selectorTR").each(function() {
				var level2RowTotalAmount = $(this).find(".segmentModalByLevel2").length;
				var level2RowSelectedAmount = $(this).find(".segmentModalByLevel2:checked").length;
					$(this).find(".segmentModalByLevel2:checked").each(function() {
						strHTML += '<li>';
						strHTML += '<div class="removeBtn" relContainer="' + containerId + '" value="' + $(this).val() + '" style="cursor:pointer" title="删除：' + $.trim($(this).parent().text()) + '">';
						strHTML += '<input type="hidden" value="' + $(this).val() + '" name="' + relInputName + '"  levelType="2" />';
						strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
						strHTML += '</div>';
						strHTML += '</li>';
					});
			});
			//拆分选项可选
			$(currSegmentLI).find("tbody td input[name='obj_Split']").attr('disabled',false);
		}
		strHTML += '</ul>';
		$(currSegmentLI).find('.segmentModalResultContainer').html(strHTML);
		saveSegmentType();
	});
	/**保存已勾选的细分市场大类别**/
	function saveSegmentType() {
		var choosedSegmentType = $("#choosedSegmentType");
		var value = "";
		var count = 0;
		$(".segmentType").each(function() {
			var segment = $(this);
			if(segment.prop("checked")) {
				if(count == 0) {
					value += segment.val();
				} else {
					value += "," + segment.val();
				}
				count++;
			}
		});
		choosedSegmentType.val(value);
	}
	
	/**点击确定生成内容*/
	$(".origModalContainer").find('.confirm').live('click',function(){
		var containerId = $(this).parents(".origModalContainer").attr("id");
		var relInputName = $(this).attr("relInputName");
		var strHTML = "";
		strHTML += '<ul class="selectorResultContainer">';
		//如果全部选中，则生成全部
		$(this).parents(".origModalContainer").find('.origModal:checked').each(function(){
			strHTML += '<li>';
			strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+$(this).val()+'" style="cursor:pointer" title="删除：'+$.trim($(this).parent().text())+'">';
			strHTML += '<input type="hidden" value="'+$(this).val()+'" name="'+relInputName+'" levelType="1" />';
			strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
			strHTML += '</div>';
			strHTML += '</li>';
		});
		strHTML += '</ul>';		
		$("#origModalResultContainer").html(strHTML);
	});
	/**展示系别弹出框-结束*/
	
	
	
	/**展示品牌弹出框-开始*/
	$("a[name ='brandSelector']").live('click',function(){
		currBrandLI = $("div[name = 'objectModal']").eq(4);
	});
	
	$('#brandModal').on('show', function (e) {
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择品牌
		if(e.relatedTarget)  return; 
		//加载品牌数据
		showLoading("brandModalBody");
		$('#brandModalBody').load(ctx+"/manfSales/global/getBrand",getParams(),function(){
			//弹出框设置默认选中项结果集		
			var strHTML = '<ul class="inline" >';
			$(currBrandLI).find('.brandModalResultContainer input').each(function(){
				var id = $(this).val();
				var name = $(this).attr("brandName");
				var letter = $(this).attr("letter");
				strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
			  	strHTML += '<div class="removeBtnByResult label label-info" brandId="'+id+'"  letter="'+letter+'" brandName="'+name+'" style="cursor:pointer" title="删除：'+name+'">';
				strHTML += '<i class="icon-remove icon-white"></i>'+name;
			  	strHTML += '</div>';
			 	strHTML += '</li>';
			 	$(".brandModalContainer .brandIdInput").each(function(){
					$(this).attr("checked");//行全选
				});
		 		$(".brandModalContainer .brandIdInput").each(function(){
					if($(this).val() == id){
						$(this).attr("checked",'true');//行全选
					}
				});
			});
			strHTML += '</ul>';
			$("#selectorResultContainerByBrand").html(strHTML);
		});
	});	
	
	$(".brandModalContainer").find('.brandIdInput').live('click',function(){
		//显示选中的值
		//去掉重复选项
		$("#selectorResultContainerByBrand").html();
		var allObjArr = [];
		$(".brandModalContainer").find('.brandIdInput:checked').each(function(){
			var obj = {};
			obj.id =  $(this).val();
			obj.name =  $.trim($(this).parent().text());
			obj.letter =  $(this).attr("letter");
			allObjArr[allObjArr.length] = obj;
		});
		var strHTML = '<ul class="inline" >';
		for(var i=0;i<allObjArr.length;i++){
			strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
		  		strHTML += '<div class="removeBtnByResult label label-info" brandId="'+allObjArr[i].id+'"  letter="'+allObjArr[i].letter+'" brandName="'+allObjArr[i].name+'" style="cursor:pointer" title="删除：'+allObjArr[i].name+'">';
			  		strHTML += '<i class="icon-remove icon-white"></i>'+allObjArr[i].name;
		  		strHTML += '</div>';
		 		strHTML += '</li>';
		 }
		 strHTML += '</ul>';
		$("#selectorResultContainerByBrand").html(strHTML);
		//显示选中的值
	});
	/**展示品牌弹出框-结束*/
	
	
	
	/**展示厂商品牌弹出框-开始*/
	$("a[name ='manfSelector']").live('click',function(){
		currManfLI = $("div[name = 'objectModal']").eq(3);
	});
	
	$('#manfModal').on('show', function (e) {
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
		if(e.relatedTarget)  return; 
		//加载生产商品牌数据
		showLoading("manfModalBody");
		$('#manfModalBody').load(ctx+"/manfSales/global/getManfModal",getParams(),function(){
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
		//显示选中的值
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
		//显示选中的值
	});
	/**展示厂商弹出框-结束*/
	
	
	/**展示车身弹出框-开始*/
	$("a[name ='bodyTypeSelector']").live('click',function(){
		currBodyTypeLI = $("div[name = 'bodyTypeModal']").eq(7);
		$('#bodyTypeModal').modal('show');
	});
	
	$('#bodyTypeModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//去掉默认选中的效果
		$('.bodyTypeModalByAll').each(function(){
			$(this).removeAttr("checked");//取消行全选
		});
		//加载车身形式数据
		showLoading("bodyTypeModalBody");
		$('#bodyTypeModalBody').load(ctx+"/manfSales/global/getBodyType",getParams(),function(){
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
		strHTML += '<ul class="selectorResultContainer" style = "margin-top: -13px;">';
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
	/**展示车身弹出框-结束*/
	
	/**重置按钮事件-开始*/
	$('#resetBtn').on('click', function (e) {
		//清空图表
		$("#chartId").html("");
		$("#chartTable").html("");
		
		$("#tabs-1").attr("style","display:none");
		//清空已选择对象,默认选择下拉框对象:级别
		$("#objectContainer").children().hide();
		$("#objectContainer").children().find(".selectorResultContainer").remove();
		//对象类型为厂商品牌时展示厂商品牌对象弹出框
		$("#objectContainer").children().eq(3).show();
		
		//默认选择季度
		$("#startDate-container.input-append.date").datetimepicker("remove");
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
		$("#startDate-container.input-append.date").datetimepicker("update",  endDate.substr(0,4));
		$("#seasonType").attr("style","display:block;width:116px;");
		//默认选择最新季度
		if(endDate.replace("-","")>=endDate.substr(0,4)+"10"&&endDate.replace("-","")<=endDate.substr(0,4)+"12"){
			$("#seasonType option[value='4']").attr("selected","selected");
		}else if(endDate.replace("-","")>=endDate.substr(0,4)+"01"&&endDate.replace("-","")<=endDate.substr(0,4)+"03"){
			$("#seasonType option[value='1']").attr("selected","selected");
		}else if(endDate.replace("-","")>=endDate.substr(0,4)+"04"&&endDate.replace("-","")<=endDate.substr(0,4)+"06"){
			$("#seasonType option[value='2']").attr("selected","selected");
		}else if(endDate.replace("-","")>=endDate.substr(0,4)+"07"&&endDate.replace("-","")<=endDate.substr(0,4)+"09"){
			$("#seasonType option[value='3']").attr("selected","selected");
		}
		$("#startDate").attr("value",endDate.substr(0,4));
	});
	/**重置按钮事件-结束*/
	
	/**查询按钮事件-开始*/
	$('#queryBtn').on('click', function (e) {
		
		var params = getParams();
		if(paramsValidate(params)){
			return;
		}
		$('body').showLoading();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/manfSales/loadChartAndTable",
			   data: params,
			   success: function(data){
				   $("#tabs-1").attr("style","display:block");
			   	if(data){
				     //查询面板折叠
				     $(".queryConditionContainer .buttons .toggle a").click();
					 $("#chartId").height(380);
					 	 $(".tab-content").css("overflow","hidden");
						 $("#priceTypeDiv").css("display","block");
						 $("#priceType").addClass("hide");
						 $("#priceType option").eq(0).attr("selected","selected");
						 var flag = false;
						 //当全部为空的时候就不显示图
						 for(var i=0; i<data.grid.length;i++){
							if( data.grid[i].subsidy != '-'){
								flag=true;
								break;
							}
						 }
						 if(flag){
							 showChart(data.grid);
							 createChartTable(data.grid);
						 } else{
							 //没有数据
							 $("#chartId").html("");
							 $("#chartTable").html("");
							 $("#chartId").html("<h2 Style='color:black'>NO DATA!<h2>")
						 }
				   }
				   $('body').hideLoading();
			   },
			   error:function(){
				   $('body').hideLoading();
			   }
			});
	});
	/**查询按钮事件-结束*/
	
	
	/**图表 -开始*/
	function showChart(json){
		var arr=new Array();
		for(var i=0;i<json.length;i++){
			if("-"!=json[i].subsidy){
				var value=[json[i].subsidyType,Number(json[i].subsidy)]
				arr.push(value);
			}
		}
		  var chart;
	        chart = new Highcharts.Chart({
	            chart: {
	                renderTo: 'chartId',
	                plotBackgroundColor: null,
	                plotBorderWidth: null,
	                plotShadow: false
	            },
	            credits: {
	            	enabled: false
	            },
	                colors:['#AED4F8','#00235A','#8994A0','#9370DB','#62C5E2','#FFA500','#7FFFAA','#4A6F8A','#BBC600','#920A6A','#E63110','#005D5B','#FACE00','#009C0E','#77001D'],
	            title: {
	                text: '销售支持分析'+"  "+json[0].dateTime,
	                align:'left',
	                style: {
	                    color: 'black',
	                    fontWeight: 'bold',
	                    fontSize:'18px',
	                    fontFamily:'微软雅黑'
	                }
	            },
	            tooltip: {
	            pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>',
	            percentageDecimals: 2
	            },
	            plotOptions: {
	                pie: {
	                    allowPointSelect: false,
	                    cursor: 'pointer',
	                    dataLabels: {
	                        enabled: true,
	                        color: '#000000',
	                        connectorColor: '#000000',
	                        distance: -30,
//	                        formatter: function() {
//	                           // return /*'<b>'+ this.point.name +'</b>: '+*/ this.percentage +' %';
//	                        	return this.percentage + '%';
//	                        }
	                        format: '{point.percentage:.2f} %'
	                    },
	            showInLegend: true
	                }
	            },
	            //图例
	            legend: {
	            	align: 'right',
	            	layout: 'vertical',//垂直显示
	            	verticalAlign: 'middle',
	            	x: -400,
	            	y: 50,
	            	itemMarginBottom: 20,
	            	//图例字体设置
	            	itemStyle: {
	                    //color: '#000000',
	            		color: 'black',
	                    fontWeight: 'normal'//,
	                    //fontFamily:'微软雅黑'
	                },
	                symbolRadius:1,
	                symbolWidth:5,
	                symbolHeight:5
	            },
	            series: [{
	                type: 'pie',
	                name: '促销份额',
	                data: []
	            }]
	        });
	        chart.series[0].setData(arr);
	}
	
	function createChartTable(json){
		var tbodyHtml = "";
				
				tbodyHtml += "<tr>";
				tbodyHtml += "<td class='tdSubModel'    align='center' >" + "指标" + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + "提车支持(STD支持)" + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + "零售支持(Aak支持)" +"</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + "人员奖励" + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + "金融贷款" + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center'>" + "置换支持" + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + "赠送保险" + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + "赠送礼品(油卡、保养)" + "</td>";
				tbodyHtml += "</tr>";
				tbodyHtml += "<tr>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + "金额/百万" + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + isEmpty(json[0].versionSubsidy) + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + isEmpty(json[1].versionSubsidy) +"</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + isEmpty(json[2].versionSubsidy) + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + isEmpty(json[3].versionSubsidy) + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + isEmpty(json[4].versionSubsidy) + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + isEmpty(json[5].versionSubsidy) + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + isEmpty(json[6].versionSubsidy) + "</td>";
				tbodyHtml += "</tr>";
				tbodyHtml += "<tr>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + "占比/%" + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" +format(json[0].subsidy) + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + format(json[1].subsidy) +"</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" +format(json[2].subsidy) + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + format(json[3].subsidy) + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" +format(json[4].subsidy) + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + format(json[5].subsidy) + "</td>";
				tbodyHtml += "<td class='tdSubModel' align='center' >" + format(json[6].subsidy) + "</td>";
				tbodyHtml += "</tr>";
				
			$("#chartTable").html(tbodyHtml);
			$("#chartTable ").attr("style","margin-left:10px");
			$("#chartTable").find("td").each(function(){
				$(this).attr("style","font-family:微软雅黑;border:solid; border-width:1px;line-height:25px;border-color:gray;height:25px;width:120px;");
			})
			$("#chartTable  td").eq(0).css("background-color","#00235A");
			$("#chartTable  td").eq(0).css("color","white");
	}
	//非空判断
	function isEmpty(data){
		if(""==data||null==data){
			data="-";
		}else{
			data=data;
		}
		return data;
	}
	//格式化
	function format(data){
		if("-"==data){
			data=data;
		}else{
			data=data;
		}
		return data;
	}
	/**图表 -结束*/
	
	
	
	/**
	 * 校验弹出框有效数据
	 * objectType下标
	 * 0 型号;1 车型;2 生产商品牌;3 品牌;
	 * 4 系别;5 细分市场;
	 */
	function checkPopBoxData()
	{	
		var paramsObj = getParams();
		var objectType = paramsObj.objectType;
		if(objectType == 0){
			if(!paramsObj.vids && !paramsObj.mids) return;
		}else if(objectType == 1){
			if(!paramsObj.mids) return;
		}else if(objectType == 2){
			if(!paramsObj.manfIds) return;
		}else if(objectType == 3){
			if(!paramsObj.brandIds) return;
		}else if(objectType == 4){
			if(!paramsObj.origIds) return;
		}if(objectType == 5){
			if(!paramsObj.segmentIds) return;
		}
		$('.queryConditionContainer').showLoading();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/manfSales/checkPopBoxData",
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
					  var autoVidObj = $("#autoVersionModalResultContainer ul li");
					  for(var i = 0; i < data.length; i++)
					  {
						  var mid = data[i].subModelId;
						  var vids = data[i].versionList;
						  var manf = data[i].manfId;
						  var brand = data[i].brandId;
						  var orig = data[i].origId;
						  var segment = data[i].segmentId;
						  
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
						  //自定义型号
						  if(autoVidObj && vids)
						  {
							  for(var j = 0; j < vids.length; j++)
							  {
								  var vid = vids[j].versionId;
								  $.each(autoVidObj,function(i,n){
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
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var objectType = $("#objectType").val();
		var frequencyType = $("#frequencyType").val();
		var seasonType=$("#seasonType").val();
		var mids = $("#subModelModalResultContainer input[name='selectedSubModel']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
		var autoVidLength = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").length;
		if(autoVidLength > 0){
			var vids = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").map(function(){return $(this).val();}).get().join(",");
		}else{
			var vids = $("#versionModalResultContainer input[name='selectedVersion']").map(function(){return $(this).val();}).get().join(",");
		}
		var manfIds = $("#manfModalResultContainer input[name='selectedManf']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
		var brandIds = $("#brandModalResultContainer input[name='selectedBrand']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
		var origIds = $("#origModalResultContainer input[name='selectedOrig']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
		var segmentIds = $("#segmentModalResultContainer input[name='selectedSegment']").map(function(){return $(this).val();}).get().join(",");
		var segmentType = $(".segmentType").map(function(){if($(this).prop("checked")) {return $(this).val();} else {return null;}}).get().join(",");//细分市场类别
		var bodyTypeIds = $("#bodyTypeModalResultContainer input[name='selectedBodyType']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
		
		var paramObj = {};
		paramObj.beginDate = beginDate;
		paramObj.endDate = endDate;
		paramObj.mids = mids;
		paramObj.vids = vids;
		paramObj.manfIds = manfIds;
		paramObj.brandIds = brandIds;
		paramObj.origIds = origIds;
		paramObj.segmentIds = segmentIds;
		paramObj.segmentType = segmentType;
		paramObj.bodyTypeIds = bodyTypeIds;
		paramObj.inputType = "2";
		paramObj.frequencyType = frequencyType;
		paramObj.seasonType=seasonType;
		paramObj.objectType = objectType;
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
		
		queryCondition += "频次 = ";
		if("1" == $("#frequencyType").val()) queryCondition += "月";
		if("2" == $("#frequencyType").val()) queryCondition += "季";
		if("3" == $("#frequencyType").val()) queryCondition += "年";
		
		queryCondition += "\n对象类型 = ";
		if("0" == $("#objectType").val()) queryCondition += "型号";
		if("1" == $("#objectType").val()) queryCondition += "车型";
		if("2" == $("#objectType").val()) queryCondition += "厂商品牌";
		if("3" == $("#objectType").val()) queryCondition += "品牌";
		if("4" == $("#objectType").val()) queryCondition += "系别";
		if("5" == $("#objectType").val()) queryCondition += "级别";
		
		if($("#frequencyType").val() == 1){
			queryCondition += "\n时间 = " + $("#startDate").val() ;
		} else if($("#frequencyType").val() == 2){
			queryCondition += "\n时间 = " + $("#startDate").val() + $("#seasonType option:selected").text() ;
		} else{
			queryCondition += "\n时间 = " + $("#startDate").val() ;
		}
		
		queryCondition += "\n级别 = " + $("#segmentModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n系别 = " + $("#origModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n品牌 = " + $("#brandModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n厂商品牌 = " + $("#manfModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n车型 = " + $("#subModelModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n型号 = " + $("#versionModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n自定义型号组 = " + $("#autoVersionModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n车身形式 = " + $("#bodyTypeModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		paramObj.queryCondition = queryCondition;
	}
	
});

/**
 * 车型弹出框
 * @param type
 */
function showSubModel(type)
{
	//把选中的都取消
	
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
	if('1' == type) getTerminalModelPage("tabs-competingProducts",type,"1","2");
	else if('2' == type) getTerminalModelPage("tabs-segment",type,"1","2");
	else if('3' == type) getTerminalModelPage("tabs-brand",type,"1","2");
	else getTerminalModelPage("tabs-manf",type,"1","2");
};	
	
/**
 * 展示页面
 * @param id 展示容器ID
 * @param type 子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
 * @param inputType 控件类型1：复选，2：单选;默认为1
 * @param timeType 时间类型1：时间点;2：时间段默认为2
 * 
 */
function getTerminalModelPage(id,type,inputType,timeType)
{
	//如果内容不为空则触发请求
	if(!$.trim($('#' + id).html())){
		
		//获取时间
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var frequencyType=$("#frequencyType").val();
		var seasonType=$("#seasonType").val();
		var hatchbackId = "";
		//传递参数
		var params = {
						inputType:inputType,
						subModelShowType:type,
						beginDate:beginDate,
						endDate:endDate,
						frequencyType:frequencyType,
						seasonType:seasonType
					};
		//触发请求
		showLoading(id);
		$('#' + id).load(ctx+"/manfSales/global/getSubmodelModal",params,function(){
			
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

function paramsValidate(params){
	var objectType = params.objectType;
	var beginDate = params.beginDate;
	var endDate = params.endDate;
	var mids = params.mids;
	var vids = params.vids;
	var manfIds = params.manfIds;
	var brandIds =params.brandIds;
	var origIds = params.origIds;
	var segmentIds = params.segmentIds;
	var flag = false;
	if(0 == objectType){
		if("" ==vids && "" == mids){
			alert("请选择车型");
			flag = true;
		}else if("" == vids && "" != mids){
			alert("请选择型号");
			flag = true;
		}
	}else if(1 == objectType){
		if("" == mids){
			alert("请选择车型");
			flag = true;
		}
	}else if(2 == objectType){
		if("" == manfIds){
			alert("请选择厂商品牌");
			flag = true;
		}
	}else if(3 == objectType){
		if("" == brandIds){
			alert("请选择品牌");
			flag = true;
		}
	}else if(4 == objectType){
		if("" == origIds){
			alert("请选择系别");
			flag = true;
		}
	}else if(5 == objectType){
		if("" == segmentIds){
			alert("请选择细分市场");
			flag = true;
		}
	}
	
	return flag;
};

/**
 * 导出Excel
 * @param languageType EN:英文版;ZH:中文版
 */
function exportExcel(languageType)
{
	
	if(!$.trim($("#chartTable").html()))
	{
		alert("暂无数据导出");
		return;
	}
	var beginDate = $("#startDate").val();
	//var maxDate = endDate;//最新月份
	var endDate = $("#endDate").val();
	var objectType = $("#objectType").val();
	var frequencyType = $("#frequencyType").val();
	var seasonType=$("#seasonType").val();
	var mids = $("#subModelModalResultContainer input[name='selectedSubModel']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
	var autoVidLength = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").length;
	if(autoVidLength > 0){
		var vids = $("#autoVersionModalResultContainer input[name='selectedAutoVersion']").map(function(){return $(this).val();}).get().join(",");
	}else{
		var vids = $("#versionModalResultContainer input[name='selectedVersion']").map(function(){return $(this).val();}).get().join(",");
	}
	var manfIds = $("#manfModalResultContainer input[name='selectedManf']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
	var brandIds = $("#brandModalResultContainer input[name='selectedBrand']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
	var origIds = $("#origModalResultContainer input[name='selectedOrig']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
	var segmentIds = $("#segmentModalResultContainer input[name='selectedSegment']").map(function(){return $(this).val();}).get().join(",");
	var bodyTypeIds = $("#bodyTypeModalResultContainer input[name='selectedBodyType']").map(function(){return "'"+$(this).val()+"'";}).get().join(",");
	var priceType = "1";
	if($("#priceType").attr("class") != "hide"){
		priceType = $("#priceType").val();
	}
	
	$("#ex_beginDate").val(beginDate);
	$("#ex_endDate").val(endDate);
	$("#ex_mids").val(mids);
	$("#ex_vids").val(vids);
	$("#ex_manfIds").val(manfIds);
	$("#ex_brandIds").val(brandIds);
	$("#ex_origIds").val(origIds);
	$("#ex_segmentIds").val(segmentIds);
	$("#ex_bodyTypeIds").val(bodyTypeIds);
	$("#ex_frequencyType").val(frequencyType);
	$("#ex_objectType").val(objectType);
	$("#ex_priceType").val(priceType);
	$("#languageType").val(languageType);
	$("#ex_seasonType").val(seasonType);
	$("#exportFormId").submit();
};
