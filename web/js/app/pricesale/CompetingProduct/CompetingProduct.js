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
var bodyType = null;
var segmentPath = "/global/getSegmentAndChildren";
var segmentContainer = null;
/**定义全局变量结果集*/
var resultData = null;
var subModelOrderId = null;
var subModelOrderName = null;
var orderTitles = null;
var exportSeries = null;
$(document).ready(function(){
	//$(".tab-content .buttons li a").click();
});

/**获取时间模板*/
function getDateTp(type) {
	if("add" == type) {
		defaultBeginDate = parseInt(defaultBeginDate.substr(0,4)) - 1 + "-01";
		endDate = parseInt(endDate.substr(0,4)) - 1 + "-12";
	}
	var htmlStr = "";	
	htmlStr += '<li style="list-style: none;margin-bottom:10px;" class="dateLIContainer">';
    htmlStr += '	<div class="form-inline">';
    htmlStr += '  		<span class="startDate-container input-append date">';
	htmlStr += '	  		<input id="startDate" type="text" value="' + endDate + '"  readonly="readonly" class="input-mini white" placeholder="开始日期" />';
	htmlStr += '	  		<span class="add-on"><i class="icon-th"></i></span>';
	htmlStr += '		</span>';
	htmlStr += '		<span class="2">至</span>';
    htmlStr += '  		<span class="endDate-container input-append date">';
	htmlStr += '	  		<input id="endDate" type="text" value="' + endDate + '" readonly="readonly" class="input-mini white"  placeholder="结束日期" />';
	htmlStr += '	  		<span class="add-on"><i class="icon-th"></i></span>';
	htmlStr += '		</span>';
	htmlStr += '	</div>';
 	htmlStr += '</li>';
 	return htmlStr;
}

/**获取细分市场模板*/
function getSegmentTp() {
	var htmlStr = "";
	htmlStr += '<li style="list-style: none;margin-bottom:10px;" class="segmentLIContainer">';
	htmlStr += '	<table style="width:840px;">';
	htmlStr += '		<tr>';
	htmlStr += '			<td valign="top" style="width:220px;">';
	htmlStr += '				<div>';
	htmlStr += '					<select name="objectType" onchange ="changeObjectType(this)">';
	htmlStr += '						<option value="0" selected>级别</option>';
	htmlStr += '						<option value="1">系别</option>';
	htmlStr += '						<option value="2">品牌</option>';
	htmlStr += '						<option value="3">厂商品牌</option>';
	htmlStr += '					</select>';
	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" style="width:70px;">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn  segmentSelector"  data-toggle="modal">选择级别</a>';
 	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:165px">';
	htmlStr += '				<div style="margin-left:0px" class="segmentModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:66px;">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn  bodyTypeSelector" data-toggle="modal">车身形式</a>';
 	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:150px;">';
	htmlStr += '				<div  style="margin-left:0px" class="bodyTypeModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '		</tr>';
	htmlStr += '	</table>';
	htmlStr += '</li>';
	return htmlStr;
};

/**获取厂商模板*/
function getManfTp(){
	var htmlStr = "";
	htmlStr += '<li style="list-style: none;margin-bottom:10px;" class="manfLIContainer">';
	htmlStr += '	<table style="width:840px;">';
	htmlStr += '		<tr>';
	htmlStr += '			<td valign="top" style="width:220px;">';
	htmlStr += '				<div>';
	htmlStr += '					<select name="objectType" onchange ="changeObjectType(this)">';
	htmlStr += '						<option value="0">级别</option>';
	htmlStr += '						<option value="1">系别</option>';
	htmlStr += '						<option value="2">品牌</option>';
	htmlStr += '						<option value="3" selected>厂商品牌</option>';
	htmlStr += '					</select>';
	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" style="width:70px;">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn  manfSelector" data-toggle="modal">选择厂商</a>';
 	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:165px;">';
	htmlStr += '				<div  style="margin-left:0px" class="manfModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:66px;">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn  bodyTypeSelector" data-toggle="modal">车身形式</a>';
 	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:150px;">';
	htmlStr += '				<div  style="margin-left:0px" class="bodyTypeModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			</td>';
	htmlStr += '		</tr>';
	htmlStr += '	</table>';
	htmlStr += '</li>';
	return htmlStr;
};

/**获取品牌模板*/
function getBrandTp(){
	var htmlStr = "";
	htmlStr += '<li style="list-style: none;margin-bottom:10px;" class="brandLIContainer">';
	htmlStr += '	<table style="width:840px;">';
	htmlStr += '		<tr>';
	htmlStr += '			<td valign="top" style="width:220px;">';
	htmlStr += '				<div>';
	htmlStr += '					<select name="objectType" onchange ="changeObjectType(this)">';
	htmlStr += '						<option value="0">级别</option>';
	htmlStr += '						<option value="1">系别</option>';
	htmlStr += '						<option value="2" selected>品牌</option>';
	htmlStr += '						<option value="3">厂商品牌</option>';
	htmlStr += '					</select>';
	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" style="width:70px;">	';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn  brandSelector" data-toggle="modal">选择品牌</a>';
 	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:165px;">';
	htmlStr += '				<div  style="margin-left:0px" class="brandModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:66px;">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn  bodyTypeSelector" data-toggle="modal">车身形式</a>';
 	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:150px;">';
	htmlStr += '				<div  style="margin-left:0px" class="bodyTypeModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '		</tr>';
	htmlStr += '	</table>';
	htmlStr += '</li>';
	return htmlStr;
};


/**获取车身形式和车型模板*/
function getSubModelTp(){
	var htmlStr = "";
	htmlStr += '<li style="list-style: none;margin-bottom:10px;" class="subModelLIContainer">';
	htmlStr += '	<table style="width:650px;">';
	htmlStr += '		<tr>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:8%">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn bodyTypeSelector"  data-toggle="modal">车身形式</a>';
 	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:29%">';
	htmlStr += '				<div  style="margin-left:0px" class="bodyTypeModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" style="width:15%">	';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn subModelSelector" data-toggle="modal">选择车型</a>';
 	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:42%">';
	htmlStr += '				<div style="margin-left:0px" class="subModelModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '		</tr>';
	htmlStr += '	</table>';
	htmlStr += '</li>';
	return htmlStr;
};

/**获取系别模板*/
function getOrigTp(){
	var htmlStr = "";
	htmlStr += '<li style="list-style: none;margin-bottom:10px;" class="origLIContainer">';
	htmlStr += '	<table style="width:840px;">';
	htmlStr += '		<tr>';
	htmlStr += '			<td valign="top" style="width:220px;">';
	htmlStr += '				<div>';
	htmlStr += '					<select name="objectType" onchange ="changeObjectType(this)">';
	htmlStr += '						<option value="0">级别</option>';
	htmlStr += '						<option value="1" selected>系别</option>';
	htmlStr += '						<option value="2">品牌</option>';
	htmlStr += '						<option value="3">厂商品牌</option>';
	htmlStr += '					</select>';
	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" style="width:70px">	';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn  origSelector" data-toggle="modal">选择系别</a>';
 	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:165px;">';
	htmlStr += '				<div  style="margin-left:0px" class="origModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:66px;">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn  bodyTypeSelector" data-toggle="modal">车身形式</a>';
 	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:150px;">';
	htmlStr += '				<div  style="margin-left:0px" class="bodyTypeModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '		</tr>';
	htmlStr += '	</table>';
	htmlStr += '</li>';
	return htmlStr;
};

/** 时间控件初始化*/
function initDate(type) {
	$('.startDate-container.input-append.date').datetimepicker({
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
    $('.endDate-container.input-append.date').datetimepicker({
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
}

/**初始化时，加载对象模板*/
function initPage() {
	$(".dateULContainer").each(function() {
	    $(this).append(getDateTp());
	});
	$(".subModelULContainer").each(function(){ 
		$(this).append(getSubModelTp());
	});
	var html = "<ul style='margin:0px;' class='segmentULContainer'>"+getSegmentTp()+"</ul>";
	$("#objectDiv .controls").append(html);
	initDate();
};

$(document).ready(function() {
	
	
	/** 价格信息下拉框改变事件*/
	$("#priceType").change(function() {
		var priceType = $(this).val();
		if("0" == priceType) {
			$("#productConfig").show();		
		} else {
			$("#productConfig").hide();
		}
	});
	
	//初始化页面
	initPage();
	//时间改变事件
	$('.endDate-container.input-append.date').on('changeDate',function(){
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
		
	});
	
	$('.startDate-container.input-append.date').on('changeDate',function(){
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
    });
	/**
	 * 对象类型切换显示不同类型弹出框
	 * @param {Object} select
	 */
	window.changeObjectType=function(select) {
		var ulLength = 0;
		var objectTypeVal = $(select).val();
		if(objectTypeVal == 0) {
			ulLength = $("#objectDiv .controls .segmentULContainer").length;
			if(ulLength == 0) {
				$("#objectDiv .controls").append("<ul style='margin:0px;' class='segmentULContainer'>" + getSegmentTp() + "</ul>");
			} else {
				$("#objectDiv .controls .segmentULContainer").append(getSegmentTp());
			}
		} else if(objectTypeVal == 1) {
			ulLength = $("#objectDiv .controls .origULContainer").length;
			if(ulLength == 0) {
				$("#objectDiv .controls").append("<ul style='margin:0px;' class='origULContainer'>" + getOrigTp() + "</ul>");
			} else {
				$("#objectDiv .controls .origULContainer").append(getOrigTp());
			}
		} else if(objectTypeVal == 2) {
			ulLength = $("#objectDiv .controls .brandULContainer").length;
			if(ulLength == 0) {
				$("#objectDiv .controls").append("<ul style='margin:0px;' class='brandULContainer'>" + getBrandTp() + "</ul>");
			} else {
				$("#objectDiv .controls .brandULContainer").append(getBrandTp());
			}
		} else if(objectTypeVal == 3) {
			ulLength = $("#objectDiv .controls .manfULContainer").length;
			if(ulLength == 0){
				$("#objectDiv .controls").append("<ul style='margin:0px;' class='manfULContainer'>" + getManfTp() + "</ul>");
			} else {
				$("#objectDiv .controls .manfULContainer").append(getManfTp());
			}
		} else {
			ulLength = $("#objectDiv .controls .subModelULContainer").length;
			if(ulLength == 0) {
				$("#objectDiv .controls").append("<ul style='margin:0px;' class='subModelULContainer'>" + getSubModelTp() + "</ul>");
			} else {
				$("#objectDiv .controls .subModelULContainer").append(getSubModelTp());
			}
		}
		$(select).parents('li').remove();;
	};
	
	/**
	 * 时间对象个数不为一时,拆分时只保存一个时间对象
	 */
	$("#objectDiv").find("input[name='obj_Split']").live('click',function(){
		var dateLiList = $('.dateULContainer').find('.dateLIContainer');
		if(dateLiList.length>1 && $(this).attr("checked") == "checked"){
			for(var i=1;i<dateLiList.length;i++){
				$(dateLiList[i]).remove();
				defaultBeginDate = parseInt(defaultBeginDate.substr(0,4)) + 1 + "-01";
				endDate = parseInt(endDate.substr(0,4)) + 1 + "-12";
			}
		}
	});
	
	/** 弹出车身形式对话框*/
	$(".bodyTypeSelector").live("click", function() {
		/**打开车身形式选择框时，清空车型选择*/
		$(this).parents("td").next().next().next().find(".selectorResultContainer").html("");
		currBodyTypeLI = $(this).parents("td").next().find(".bodyTypeModalResultContainer");
		$('#bodyTypeModal').modal("show");
	});
	
	/**展示车身形式弹出框*/
	$("#bodyTypeModal").on("show", function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//去掉默认选中的效果
		$(".bodyTypeModalByAll").each(function(){
			$(this).removeAttr("checked");//取消行全选
		});
		
		//加载子车型数据
		showLoading("bodyTypeModalBody");
		$("#bodyTypeModalBody").load(ctx + "/pricesale/competingProductGlobal/getBodyType", getParams(), function() {
			//弹出框设置默认选中项结果集		
			$(currBodyTypeLI).find("input").each(function() {
				var bodyTypeId = $(this).val();
				if(bodyTypeId == "0"){
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
	
    /**细分市场开始*/
	
	/** 弹出细分市场对话框*/
	$(".segmentSelector").live("click", function() {
		currSegmentLI = $(this).parents(".segmentLIContainer");
		segmentContainer = $(this);
		$("#segmentModal").modal("show");
	});
	
	/** 展示细分市场弹出框*/
	$("#segmentModal").on("show", function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//去掉默认选中的效果
		$(".segmentModalByAll").each(function() {
			$(this).prop("checked", false);//取消行全选
		});
		//加载细分市场数据
		showLoading("segmentModalBody");
		$("#segmentModalBody").load(ctx + "/pricesale/competingProductGlobal/getSegmentAndChildren", getParams(), function() {
			//弹出框设置默认选中项结果集		
			var segmentTypeArr = $(".segmentType");
			var inputNum = $(currSegmentLI).find("#newSegmentType").map(function(index, input) {return input.value;}).get();
			var inputNumArr = new Array();
			var loadFlag = true;
			if(inputNum.length > 0) {
				inputNumArr = inputNum[0].split(",");
			}
			var notChoose = new Array();
			for(var i = 0; i < segmentTypeArr.length; i++) {
				var obj = segmentTypeArr[i];
				var segmentValue = $(obj).val();
				if(inputNumArr.length > 0 && $.inArray(segmentValue, inputNumArr) == -1) {
					notChoose.push(segmentValue);
				}
			}
			$(".segmentType").each(function() {
	    		var segment = $(this);
	    		var flag = false;
	    		if(notChoose.length > 0) {
	    			for(var i = 0; i < notChoose.length; i++) {
	    				var obj = notChoose[i];
	    				if(segment.val() == obj) {
	    					segment.removeAttr("checked");
	    					flag = true;
	    				} else {
	    					if(!flag) {
	    						if(!segment.prop("checked")) {
	    							segment.prop("checked", true);
	    						}
	    					}
	    				}
	    			}
	    		} else {
	    			if(!segment.prop("checked")) {
	    				loadFlag = false;
	    				segment.click();
	    			}
	    		}
			});
			(function () {
			    $("#segmentModalBody").on("segmentChange", function() {
    		        loadFlag = true;
			    });
    		});
            if(loadFlag) {
            	$(currSegmentLI).find(".segmentModalResultContainer input").each(function() {
            		var segmentId = $(this).val();
            		if(segmentId == "0") {
            			$(".segmentModalByAll").each(function() {
            				$(this).prop("checked", true);//行全选
            			});
            			$(".segmentModalContainer .segmentModalByLevel1").each(function() {
            				$(this).prop("checked", true);//行全选
            			});
            			$(".segmentModalContainer .segmentModalByLevel2").each(function() {
            				$(this).prop("checked", true);//行全选
            			});
            		} else {
            			$(".segmentModalContainer .segmentModalByLevel2").each(function() {
            				if($(this).val() == segmentId) {
            					$(this).click();
            				}
            			});
            		}
            	});
            }
			$(".segmentModalContainer").find(".selectorTR").each(function() {
				var oInput = $(this).find(".segmentModalByLevel1:checked");
				if(oInput.attr("checked")) {
					$(this).find(".segmentModalByLevel2").each(function() {
						 $(this).attr("checked", "true");//行全选
					});
				}
			});
		});
	});
	
	//细分市场-增加按扭
	$(".addSegmentBtn").live("click", function() {
		var oUl = $(this).parents(".segmentULContainer");
		if(oUl.length == 0) {
			var html = "<ul style='margin:0px;' class='segmentULContainer'>" + getSegmentTp() + "</ul>";
			$("#objectDiv .controls").append(html);
		} else {
			oUl.append(getSegmentTp());
		}
		
		var dateLiList = $(".dateULContainer").find(".dateLIContainer");
		if(dateLiList.length > 1) {
			for(var i = 1; i < dateLiList.length; i++) {
				$(dateLiList[i]).remove();
				defaultBeginDate = parseInt(defaultBeginDate.substr(0,4)) + 1 + "-01";
				endDate = parseInt(endDate.substr(0,4)) + 1 + "-12";
			}
		}
	});
	//细分市场-删除按扭
	$(".removeSegmentBtn").live("click", function() {
		var oCurrLi = $(this).parents(".segmentLIContainer");
		var segmentLiList = $(".segmentULContainer").find(".segmentLIContainer");
		var manfList = $(".manfULContainer").find(".manfLIContainer");
		var brandList = $(".brandULContainer").find(".brandLIContainer");
		var subModelList = $(".subModelULContainer").find(".subModelLIContainer");
		var origList = $(".origULContainer").find(".origLIContainer");
		if(manfList.length != 0 || brandList.length != 0 || subModelList.length != 0 || origList.length != 0 || segmentLiList.length > 1) {
			$(oCurrLi).remove();
		} else {
			alert("不能删除，至少保留一行！");
			return;
		}
	});
	
	/**点击确定生成内容*/
	$(".segmentModalContainer").find(".confirm").live("click",function(){
		var containerId = $(this).parents(".segmentModalContainer").attr("id");
		var relInputName = $(this).attr("relInputName");
		var strHTML = "";
		strHTML += '<ul class="selectorResultContainer">';
		strHTML += '<input type="hidden" id="newSegmentType" value="Volume,Economy,Premium"/>';
		//如果第一级分细市场全部选中，则生成整体市场
		if($("#segmentModal #myModalLabel :checkbox").prop("checked")) {
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
		var obj = segmentContainer;
		var newSegmentType = $(obj).closest("td").next().find(".segmentModalResultContainer #newSegmentType");
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
		newSegmentType.val(value);
	}
	/**细分市场结束*/
	
	/** 展示厂商弹出框-开始*/
	$(".manfSelector").live("click", function() {
		currManfLI = $(this).parents(".manfLIContainer");
		$("#manfModal").modal("show");
	});
	
	$("#manfModal").on("show", function (e) {
		if(e.relatedTarget) {
			return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		}
		//加载子车型数据
		showLoading("manfModalBody");
		$("#manfModalBody").load(ctx + "/pricesale/competingProductGlobal/getManfModal", getParams(), function() {
			//弹出框设置默认选中项结果集		
			var strHTML = '<ul class="inline" >';
			$(currManfLI).find(".manfModalResultContainer input").each(function() {
				var id = $(this).val();
				var name = $(this).attr("manfName");
				var letter = $(this).attr("letter");
				strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
			  	strHTML += '<div class="removeBtnByResult label label-info" manfId="'+id+'"  letter="'+letter+'" manfName="'+name+'" style="cursor:pointer" title="删除：'+name+'">';
				strHTML += '<i class="icon-remove icon-white"></i>'+name;
			  	strHTML += '</div>';
			 	strHTML += '</li>';
			 	$(".manfModalContainer .manfIdInput").each(function() {
					$(this).attr("checked");//行全选
				});
		 		$(".manfModalContainer .manfIdInput").each(function() {
					if($(this).val() == id){
						$(this).attr("checked","true");//行全选
					}
				});
			});
			strHTML += '</ul>';
			$("#selectorResultContainerByManf").html(strHTML);
		});
	});
	
	/** 厂商品牌全选全不选事件开始*/
	$(".manfModalByAll").live("click", function() {
		if($(this).attr("checked")) {
			$(".modal-body input").each(function(index) {
		 		$(this).attr("checked", "true");//全选
		 	});
			/*把全选厂商品牌放进结果带开始*/
			$("#selectorResultContainerByManf").html("");
			var allObjArr = [];
			$("#manfModal").find(".manfIdInput:checked").each(function() {
				var obj = {};
				obj.id =  $(this).val();
				obj.name =  $.trim($(this).parent().text());
				obj.letter =  $(this).attr("letter");
				allObjArr[allObjArr.length] = obj;
			});
			var strHTML = '<ul class="inline" >';
			for(var i = 0; i < allObjArr.length; i++) {
				strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
			  	strHTML += '<div class="removeBtnByResult label label-info" manfId="'+allObjArr[i].id+'"  letter="'+allObjArr[i].letter+'" manfName="'+allObjArr[i].name+'" style="cursor:pointer" title="删除：'+allObjArr[i].name+'">';
				strHTML += '<i class="icon-remove icon-white"></i>'+allObjArr[i].name;
			  	strHTML += '</div>';
			 	strHTML += '</li>';
			 }
			 strHTML += '</ul>';
			$("#selectorResultContainerByManf").html(strHTML);
			/*把全选厂商品牌放进结果带结束*/
		} else {
			$("#selectorResultContainerByManf").html("");
			$(".modal-body input").each(function(index) {
		 		 $(this).removeAttr("checked");//取消全选
		 	});
			
		} 
	});
	/*删除结果带里的元素时取消全选*/
	$("#manfModal .resultShowContent .removeBtnByResult").live("click", function() {
		$("#manfModal").find(".manfModalByAll").removeAttr("checked");//全选取消
	
});
	/** 厂商品牌全选全不选事件结束*/
	/*厂商checkbox点击事件开始*/
	$(".manfModalContainer").find(".manfIdInput").live("click", function() {
		//判断全选框要不要打钩
		if($("#manfModal .modal-body input:checkbox").length==$("#manfModal .modal-body").find("input:checked").length) {
			$("#manfModal").find(".manfModalByAll").attr("checked", true);
		} else {
			$("#manfModal").find(".manfModalByAll").attr("checked", false);
		}
		//显示选中的值	——开始
		//去掉重复选项
		$("#selectorResultContainerByManf").html();
		var allObjArr = [];
		$(".manfModalContainer").find(".manfIdInput:checked").each(function() {
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
	/*厂商checkbox点击事件结束*/
	//厂商-增加按扭
	$(".addManfBtn").live("click", function() {
		var oUl = $(this).parents(".manfULContainer");
		if(oUl.length == 0) {
			var html = "<ul style='margin:0px;' class='manfULContainer'>" + getManfTp() + "</ul>";
			$("#objectDiv .controls").append(html);
		} else {
			oUl.append(getManfTp());
		}
		
		var dateLiList = $(".dateULContainer").find(".dateLIContainer");
		if(dateLiList.length > 1) {
			for(var i = 1; i < dateLiList.length; i++) {
				$(dateLiList[i]).remove();
				defaultBeginDate = parseInt(defaultBeginDate.substr(0,4)) + 1 + "-01";
				endDate = parseInt(endDate.substr(0,4)) + 1 + "-12";
			}
		}
	});
	//厂商-删除按扭
	$(".removeManfBtn").live("click", function() {
		var oCurrLi = $(this).parents(".manfLIContainer");
		var segmentLiList = $(".segmentULContainer").find(".segmentLIContainer");
		var manfList = $(".manfULContainer").find(".manfLIContainer");
		var brandList = $(".brandULContainer").find(".brandLIContainer");
		var subModelList = $(".subModelULContainer").find(".subModelLIContainer");
		var origList = $(".origULContainer").find(".origLIContainer");
		if(segmentLiList.length != 0 || brandList.length != 0 || subModelList.length != 0 || origList.length != 0 || manfList.length > 1) {
			$(oCurrLi).remove();
		} else {
			alert("不能删除，至少保留一行！");
			return;
		}
	});
	/**展示厂商弹出框-结束*/
	
	/** 展示品牌弹出框-开始*/
	//弹出品牌对话框
	$(".brandSelector").live("click", function() {
		currBrandLI = $(this).parents(".brandLIContainer");
		$("#brandModal").modal("show");
	});
	
	$("#brandModal").on("show", function (e) {
		if(e.relatedTarget) {
			return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		}
		//加载子车型数据
		showLoading("brandModalBody");
		$("#brandModalBody").load(ctx + "/pricesale/competingProductGlobal/getBrand", getParams(), function() {
			//弹出框设置默认选中项结果集		
			var strHTML = '<ul class="inline" >';
			$(currBrandLI).find('.brandModalResultContainer input').each(function() {
				var id = $(this).val();
				var name = $(this).attr("brandName");
				var letter = $(this).attr("letter");
				strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
			  	strHTML += '<div class="removeBtnByResult label label-info" brandId="'+id+'"  letter="'+letter+'" brandName="'+name+'" style="cursor:pointer" title="删除：'+name+'">';
				strHTML += '<i class="icon-remove icon-white"></i>'+name;
			  	strHTML += '</div>';
			 	strHTML += '</li>';
			 	$(".brandModalContainer .brandIdInput").each(function() {
					$(this).attr("checked");//行全选
				});
		 		$(".brandModalContainer .brandIdInput").each(function() {
					if($(this).val() == id) {
						$(this).attr("checked", "true");//行全选
					}
				});
			});
			strHTML += '</ul>';
			$("#selectorResultContainerByBrand").html(strHTML);
		});
	});
	/** 品牌全选全不选事件开始*/
	$(".brandModalByAll").live("click",function(){
		if($(this).attr("checked")) {
			$(".modal-body input").each(function(index) {
				
		 		$(this).attr("checked", "true");//全选
		 	});
			/*把全选品牌放进结果带开始*/
			$("#selectorResultContainerByBrand").html("");
			var allObjArr = [];
			$(".brandModalContainer").find(".brandIdInput:checked").each(function() {
				var obj = {};
				obj.id =  $(this).val();
				obj.name =  $.trim($(this).parent().text());
				obj.letter =  $(this).attr("letter");
				allObjArr[allObjArr.length] = obj;
			});
			var strHTML = '<ul class="inline" >';
			for(var i = 0; i < allObjArr.length; i++) {
				strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
			  	strHTML += '<div class="removeBtnByResult label label-info" brandId="'+allObjArr[i].id+'"  letter="'+allObjArr[i].letter+'" brandName="'+allObjArr[i].name+'" style="cursor:pointer" title="删除：'+allObjArr[i].name+'">';
				strHTML += '<i class="icon-remove icon-white"></i>'+allObjArr[i].name;
			  	strHTML += '</div>';
			 	strHTML += '</li>';
			 }
			 strHTML += '</ul>';
			$("#selectorResultContainerByBrand").html(strHTML);
			/*把全选品牌放进结果带结束*/
		} else {
			$("#selectorResultContainerByBrand").html("");
			$(".modal-body input").each(function(index) {
		 		 $(this).removeAttr("checked");//取消全选
		 	});
			
		} 
	});
	/*删除结果带数据，全选取消*/
	$(".brandModalContainer .resultShowContent .removeBtnByResult").live("click", function() {
		$(".brandModalContainer").find(".brandModalByAll").attr("checked", false);//全选取消
	});
	/** 品牌全选全不选事件结束*/
	$(".brandModalContainer").find(".brandIdInput").live("click", function() {
		//品牌点击，判断要不要全选打钩
		var allArr = $(this).parents(".brandModalContainer").find(".brandIdInput");
		var allSelectedArr = $(this).parents(".brandModalContainer").find(".brandIdInput:checked");
		if(allArr.length == allSelectedArr.length){
			$(this).parents(".brandModalContainer").find(".brandModalByAll").attr("checked","true");//全部全选		
		}else{
			$(this).parents(".brandModalContainer").find(".brandModalByAll").removeAttr("checked");//全选取消
		}
		//显示选中的值	——开始
		//去掉重复选项
		$("#selectorResultContainerByBrand").html();
		var allObjArr = [];
		$(".brandModalContainer").find(".brandIdInput:checked").each(function(){
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
		//显示选中的值	——结束
	});
	
	//品牌-增加按扭
	$(".addBrandBtn").live("click", function() {
		var oUl = $(this).parents(".brandULContainer");
		if(oUl.length == 0){
			var html = "<ul style='margin:0px;' class='brandULContainer'>"+getBrandTp()+"</ul>";
			$("#objectDiv .controls").append(html);
		}else{
			oUl.append(getBrandTp());
		}
		
		var dateLiList = $(".dateULContainer").find(".dateLIContainer");
		if(dateLiList.length>1){
			for(var i=1;i<dateLiList.length;i++){
				$(dateLiList[i]).remove();
				defaultBeginDate = parseInt(defaultBeginDate.substr(0,4)) + 1 + "-01";
				endDate = parseInt(endDate.substr(0,4)) + 1 + "-12";
			}
		}
	});
	//品牌-删除按扭
	$(".removeBrandBtn").live("click", function() {
		var oCurrLi = $(this).parents(".brandLIContainer");
		var segmentLiList = $(".segmentULContainer").find(".segmentLIContainer");
		var manfList = $(".manfULContainer").find(".manfLIContainer");
		var brandList = $(".brandULContainer").find(".brandLIContainer");
		var subModelList = $(".subModelULContainer").find(".subModelLIContainer");
		var origList = $(".origULContainer").find(".origLIContainer");
		if(segmentLiList.length != 0 || manfList.length != 0 || subModelList.length != 0 || origList.length != 0 || brandList.length > 1){
			$(oCurrLi).remove();
		}else{
			alert("不能删除，至少保留一行！");
			return;
		}
	});
	/**展示品牌弹出框-结束*/
	
	/**展示车型弹出框-开始*/
	//弹出车型对话框
	$(".subModelSelector").live("click", function() {
		bodyType = $(this);
		currSubModelLI = $(this).parents(".subModelLIContainer");
		//保存获取车形弹出框当前车型下标
		$("#getModelIndexId").val($("#model .subModelULContainer .subModelLIContainer").index(currSubModelLI));
		$("#subModelModal").modal("show");
		$(".subModelModalContainer").find(".confirm").unbind("click").bind("click",function(e){
			//event.stopPropagation();
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
				strHTML += '<input type="hidden" letter="'+allSubModelArr[i].letter+'" pooAttributeId="'+allSubModelArr[i].pooAttributeId+'" subModelName="'+allSubModelArr[i].subModelName+'" value="'+allSubModelArr[i].subModelId+'" name="'+relInputName+'" />';
				strHTML += allSubModelArr[i].subModelName + '<i class="icon-remove" style="visibility: hidden;"></i>';
			  	strHTML += '</div>';
		  		strHTML += '</li>';
			 }
			strHTML += '</ul>';
			$(currSubModelLI).find(".subModelModalResultContainer").html(strHTML);
		});
	});
	/** 展示车型弹出框*/
	$("#subModelModal").on("show", function (e) {
		if(e.relatedTarget) {
			return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		}
		//加载子车型数据
		showLoading("subModelModalBody");
		$("#subModelModalBody").load(ctx + "/pricesale/CompetingProduct/getSubmodelModal", getSubModelParams(bodyType), function() {
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
						$(this).attr("checked","true");//行全选
					}
				});
			});
			strHTML += '</ul>';
			$("#selectorResultContainerBySubModel").html(strHTML);
		});
	});
	
	//车型-增加按扭
	$(".addSubModelBtn").live("click", function() {
		var oUl = $(this).parents(".subModelULContainer");
		if(oUl.length == 0) {
			var html = "<ul style='margin:0px;' class='subModelULContainer'>" + getSubModelTp() + "</ul>";
			$("#objectDiv .controls").append(html);
		} else {
			$(oUl).append(getSubModelTp());
		}
		
		var dateLiList = $(".dateULContainer").find(".dateLIContainer");
		if(dateLiList.length > 1) {
			for(var i = 1; i < dateLiList.length; i++) {
				$(dateLiList[i]).remove();
				defaultBeginDate = parseInt(defaultBeginDate.substr(0,4)) + 1 + "-01";
				endDate = parseInt(endDate.substr(0,4)) + 1 + "-12";
			}
		}
	});
	
	//车型-删除按扭
	$(".removeSubModelBtn").live("click", function() {
		var oCurrLi = $(this).parents(".subModelLIContainer");
		var segmentLiList = $(".segmentULContainer").find(".segmentLIContainer");
		var manfList = $(".manfULContainer").find(".manfLIContainer");
		var brandList = $(".brandULContainer").find(".brandLIContainer");
		var subModelList = $(".subModelULContainer").find(".subModelLIContainer");
		var origList = $(".origULContainer").find(".origLIContainer");
		if(segmentLiList.length != 0 || manfList.length != 0 || brandList.length != 0 || origList.length != 0 || subModelList.length > 1) {
			$(oCurrLi).remove();
		} else {
			alert("不能删除，至少保留一行！");
			return;
		}
	});
	
	/**展示车型弹出框-结束*/
	
	/** 展示系别弹出框-开始*/
	//弹出系别对话框
	$(".origSelector").live("click",function(){
		currOrigLI = $(this).parents(".origLIContainer");
		$("#origModal").modal("show");
	});
	
	/**展示系别弹出框*/
	$("#origModal").on("show", function (e) {
		if(e.relatedTarget) {
			return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		}
		//去掉默认选中的效果
		$(".origModalByAll").each(function() {
			$(this).removeAttr("checked");//取消行全选
		});
		//加载子车型数据
		showLoading("origModalBody");
		$("#origModalBody").load(ctx + "/pricesale/competingProductGlobal/getOrig", getParams(), function(){
			//弹出框设置默认选中项结果集		
			$(currOrigLI).find(".origModalResultContainer input").each(function() {
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
							$(this).attr("checked", "true");//行全选
						}
					});
				}
			});
		});
	});
	
	
	//系别-增加按扭
	$(".addOrigBtn").live("click", function() {
		var oUl = $(this).parents(".origULContainer");
		if(oUl.length == 0) {
			var html = "<ul style='margin:0px;' class='origULContainer'>"+getOrigTp()+"</ul>";
			$("#objectDiv .controls").append(html);
		} else {
			oUl.append(getOrigTp());
		}
		
		var dateLiList = $(".dateULContainer").find(".dateLIContainer");
		if(dateLiList.length > 1) {
			for(var i = 1; i < dateLiList.length; i++) {
				$(dateLiList[i]).remove();
				defaultBeginDate = parseInt(defaultBeginDate.substr(0,4)) + 1 + "-01";
				endDate = parseInt(endDate.substr(0,4)) + 1 + "-12";
			}
		}
	});
	//系别-删除按扭
	$(".removeOrigBtn").live("click", function() {
		var oCurrLi = $(this).parents(".origLIContainer");
		var segmentLiList = $(".segmentULContainer").find(".segmentLIContainer");
		var manfList = $(".manfULContainer").find(".manfLIContainer");
		var brandList = $(".brandULContainer").find(".brandLIContainer");
		var subModelList = $(".subModelULContainer").find(".subModelLIContainer");
		var origList = $(".origULContainer").find(".origLIContainer");
		if(segmentLiList.length != 0 || manfList.length != 0 || subModelList.length != 0 || brandList.length != 0 || origList.length > 1) {
			$(oCurrLi).remove();
		} else {
			alert("不能删除，至少保留一行！");
			return;
		}
	});
	/**展示系别弹出框-结束*/
	
	/**点击确定查询*/
	$("#queryBtn").on("click", function (e) {
		if(paramsValidate()) {
			return;
		}
		$("body").showLoading();
		//默认展示图表
		$("#showChart").click();
		//发送请求
		$.ajax({
	        type: "POST",
			url: ctx + "/pricesale/getCompetingProductData",
			data: getParams(),
			success: function(data) {
				         if(data) {
				        	 $("#isPromotion").prop("checked",false);//去掉占比勾选
				             //查询面板折叠
				        	 $(".queryConditionContainer .buttons .toggle a").click();
				        	 //显示设置收起
				        	 $(".tab-content .buttons li a").click();
				        	 $("#noData").css("display","none");
				        	 $("#exportButtons").css("display","block");
				        	 $("#chartTitleDiv1").html("");//清空echarts柱状图
				        	 $("#chartTitleDiv3").html("");//清空柱状图下的左图表
				        	 $("#priceTypeDiv").css("display","block");//显示占比选项
				        	 resultData = data;
				        	 showAllSubModel(data);
				        	 showGrid(data);
				        	 showChart(data);
				         }
				         $("body").hideLoading();
			   		  },
			error:function() {
			          $("body").hideLoading();
			      }
		});
	});
	
	
	/***
	 * 占比按钮点击事件
	 */
	$("#priceTypeDiv #isPromotion").click(function(){
		$("#chartTitleDiv1").html("");//清空echarts柱状图
		 $("#chartTitleDiv3").html("");//清空柱状图下的左图表
		if($(this).prop("checked")){
			showChart(resultData);
			showGrid2(resultData);
		} else{
			showChart(resultData);
			showGrid(resultData);
		}
		
	});
	
	/**
	 * 重置按钮
	 */
	$("#resetBtn").on("click", function (e) {
		$("#formId :reset");	
		$("#productConfig").show();
		window.location.reload();
	});
	
	/** 车型控件值鼠标经过事件*/
	$(".removeBtn").live("mouseover",function(){
		$(this).find(".icon-remove").css({visibility:"visible"});
	});

	/**车型控件值鼠标离开事件*/
	$(".removeBtn").live("mouseout",function(){
		$(this).find(".icon-remove").css({visibility:"hidden"});
	});
});

/**
 * 画图
 */
function showChart(data){
	var arryScale = data.arryScale;//价格段数组
	var arrySales = data.arrySales; //所有车型在价格段内的销量和
	var arryObjectSales = data.arryObjectSales;//所有对比对象在价格段内的销量和
	var submodelSingleVersionSalest = data.submodelSingleVersionSalest;//单个车型的销量和
	var submodelNames = data.submodelNames;//车型英文名字
	var arrySalesSinglePriceScales = data.arrySalesSinglePriceScales;//单个车型在价格段内的销量和
	var submodelSalesSum = data.submodelSalesSum;//所有车型的销量和
	var objectSales = data.objectSales;//所有对比对象的销量和
	var objectType = $("select[name='objectType']").val();//对比对象维度
	var objectName = null;
	if(0 == objectType){
		objectName = "对比级别";
	} else if(1 == objectType){
		objectName = "对比系别";
	} else if(2 == objectType){
		objectName = "对比品牌";
	} else{
		objectName = "对比厂商品牌";
	}
	var arrySalesNew = new Array();
	for(var i = 0;i < arrySales.length; i++){
		if(arrySales[i] < 0){
			arrySalesNew.push(null)
		} else{
			var value = {value:arrySales[i],label:{normal:{formatter:formatNum(arrySales[i])}}}
			arrySalesNew.push(value)
		}
	}
	
	var arryObjectSalesNew = new Array();
	for(var i = 0;i < arrySales.length; i++){
		if(arryObjectSales[i] < 0){
			arryObjectSalesNew.push(null)
		} else{
			var value = {value:arryObjectSales[i],label:{normal:{formatter:formatNum(arryObjectSales[i])}}}
			arryObjectSalesNew.push(value)
		}
	}
	
	var myChart = echarts.init(document.getElementById("chartTitleDiv1"));
	option = {
	    title: {
	        text: null,
	        subtext: null
	    },
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'line'
	        }
	    },
	    legend: {
	    	 selectedMode:false, //图例点击事件关闭
	        data: ['所选车型', objectName]
	    },
	   /* grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '0',
	        containLabel: true
	    },*/
	    xAxis: {
	        type: 'value',
	        splitLine:{
	            show:false//x轴分割线隐藏
	        },
	        axisLabel:{
	            show:false//x轴刻度标签隐藏
	        },
	        axisTick:{
	            show:false//x轴刻度隐藏
	        },
	        axisLine:{
	            show:false
	        },
	        nameGap:0
	        
	        
	    },
	    grid:{
	    	 left: 0,
	            top :55,
	            //left: 0,
	            right:50,
	            bottom: 0,
	            containLabel:true
	    },
	    yAxis: {
	        type: 'category',
	         splitLine:{
	            show:true
	        },
	       axisLabel:{
	    	   interval:0//强制显示所有y轴标签
	       },
	       axisLine:{
	            lineStyle:{
	            	width:1
	            }
	        },
	        data:  arryScale,
	        boundaryGap: ['23px', '23px']
	    },
	    series: [
	        {
	            name: '所选车型',
	            type: 'bar',
	            data: arrySalesNew,
	            label:{
                    normal:{
                        show:true,
                        textStyle:{
                       	 color:'black'
                        },
                        position:'right'
                    }
                },
                itemStyle:{
               	 normal:{
               		 color:'#99CCFF',//柱子的颜色
               		 //borderColor:'black',
               		 borderWith:1
               	 }
                }
	        },
	        {
	            name: objectName,
	            type: 'bar',
	            data: arryObjectSalesNew,
	            label:{
                    normal:{
                        show:true,
                        textStyle:{
                       	 color:'black'
                        },
                        position:'right'
                    }
                },
                itemStyle:{
               	 normal:{
               		 color:'#B2B2B2',//柱子的颜色
               		 //borderColor:'black',
               		 borderWith:1
               	 }
                }
	        }
	    ]
	};
	
	myChart.clear();
	//console.log(JSON.stringify(option));
	myChart.setOption(option);

}

/**
 * 占比勾选画图
 */
function showChart2(data){
	var arryScale = data.arryScale;//价格段数组
	var arrySales = data.arrySales; //所有车型在价格段内的销量和
	var arryObjectSales = data.arryObjectSales;//所有对比对象在价格段内的销量和
	var submodelSingleVersionSalest = data.submodelSingleVersionSalest;//单个车型的销量和
	var submodelNames = data.submodelNames;//车型英文名字
	var arrySalesSinglePriceScales = data.arrySalesSinglePriceScales;//单个车型在价格段内的销量和
	var submodelSalesSum = data.submodelSalesSum;//所有车型的销量和
	var objectSales = data.objectSales;//所有对比对象的销量和
	var objectType = $("select[name='objectType']").val();//对比对象维度
	var objectName = null;
	if(0 == objectType){
		objectName = "对比级别";
	} else if(1 == objectType){
		objectName = "对比系别";
	} else if(2 == objectType){
		objectName = "对比品牌";
	} else{
		objectName = "对比厂商品牌";
	}
	var arrySalesNew = new Array();
	for(var i = 0;i < arrySales.length; i++){
		if(arrySales[i] < 0){
			arrySalesNew.push(null)
		} else{
			var value = {value:arrySales[i],label:{normal:{formatter:formatNum(arrySales[i])}}}
			arrySalesNew.push(value)
		}
	}
	
	var arryObjectSalesNew = new Array();
	for(var i = 0;i < arrySales.length; i++){
		if(arryObjectSales[i] < 0){
			arryObjectSalesNew.push(null)
		} else{
			var value = {value:arryObjectSales[i],label:{normal:{formatter:formatNum(arryObjectSales[i])}}}
			arryObjectSalesNew.push(value)
		}
	}
	
	var arryObjectSalesTotal = new Array();//价格段内所有车型和所有级别的总和
	for(var i = 0;i < arrySales.length; i++){
		var valueNew = 0;
		if(arryObjectSales[i] < 0 && arrySales[i] < 0 ){
			arryObjectSalesTotal.push(null)
		} else{
			if(arryObjectSales[i] < 0 && arrySales[i] >= 0 ){
				valueNew = formatNum(arrySales[i]);
			} else if(arryObjectSales[i] >= 0 && arrySales[i] < 0){
				valueNew = formatNum(arryObjectSales[i]);
			} else if(arryObjectSales[i] >= 0 && arrySales[i] >= 0 ){
				valueNew = formatNum(arryObjectSales[i]) + formatNum(arrySales[i])
			}
			var value = {value:valueNew,label:{normal:{formatter:valueNew}}}
			arryObjectSalesTotal.push(value)
		}
	}
	
	var myChart = echarts.init(document.getElementById("chartTitleDiv1"));
	option = {
	    title: {
	        text: null,
	        subtext: null
	    },
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'line'
	        }
	    },
	    legend: {
	    	 selectedMode:false, //图例点击事件关闭
	        data: [ objectName]
	    },
	   /* grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '0',
	        containLabel: true
	    },*/
	    xAxis: {
	        type: 'value',
	        splitLine:{
	            show:false//x轴分割线隐藏
	        },
	        axisLabel:{
	            show:false//x轴刻度标签隐藏
	        },
	        axisTick:{
	            show:false//x轴刻度隐藏
	        },
	        axisLine:{
	            show:false
	        },
	        nameGap:0
	        
	        
	    },
	    grid:{
	    	 left: 0,
	            top :55,
	            //left: 0,
	            right:50,
	            bottom: 0,
	            containLabel:true
	    },
	    yAxis: {
	        type: 'category',
	         splitLine:{
	            show:true
	        },
	       axisLabel:{
	    	   interval:0//强制显示所有y轴标签
	       },
	       axisLine:{
	            lineStyle:{
	            	width:1
	            }
	        },
	        data:  arryScale,
	        boundaryGap: ['23px', '23px']
	    },
	    series: [
	        
	        {
	            name: objectName,
	            type: 'bar',
	            data: arryObjectSalesNew,
	            label:{
                    normal:{
                        show:true,
                        textStyle:{
                       	 color:'black'
                        },
                        position:'right'
                    }
                },
                itemStyle:{
               	 normal:{
               		 color:'#B2B2B2',//柱子的颜色
               		 //borderColor:'black',
               		 borderWith:1
               	 }
                }
	        }
	    ]
	};
	myChart.clear();
//	console.log(JSON.stringify(option));
	myChart.setOption(option);

}

/**
 * 画表格
 */
function showGrid(data){
	
	var arryScale = data.arryScale;//价格段数组
	var arrySales = data.arrySales; //所有车型在价格段内的销量和
	var arryObjectSales = data.arryObjectSales;//所有对比对象在价格段内的销量和
	var submodelSingleVersionSalest = data.submodelSingleVersionSalest;//单个车型的销量和
	var submodelNames = data.submodelNames;//车型英文名字
	var arrySalesSinglePriceScales = data.arrySalesSinglePriceScales;//单个车型在价格段内的销量和
	var submodelSalesSum = data.submodelSalesSum;//所有车型的销量和
	//var submodelIds = data.listOrder;//车型顺序信息
	
	var objectSales = data.objectSales;//所有对比对象的销量和
	
	var width = 133 ;//右边图表标题的宽度
	var width1 = 10 ;//右边图表标题之间的空格宽度
	if(submodelNames.length>4){
		width = (600/submodelNames.length-submodelNames.length);
		width1 = submodelNames.length;
	}
	
	var objectType = $("select[name='objectType']").val();
	var objectName = null;
	if(0==objectType){
		objectName = "对比级别";
	} else if(1==objectType){
		objectName = "对比系别";
	} else if(2==objectType){
		objectName = "对比品牌";
	} else{
		objectName = "对比厂商品牌";
	}
	//计算单个车型在价格段内的销量和百分比并按价格段倒叙排序
	var arrySalesSinglePriceScalesNew = new Array();
	for(var k = 0;k < arrySalesSinglePriceScales.length;k++){
		arrySalesSinglePriceScalesNew[k]=new Array();
		for(var i = 0; i < arrySalesSinglePriceScales[k].length;i++){
			var value = arrySalesSinglePriceScales[k][arrySalesSinglePriceScales[k].length-(i+1)];
			var value2 =null;
			if(value<0){
				value2 = null;
			} else{
				if(submodelSingleVersionSalest[k]<=0){
					value2 = null;
				} else{
					value2 = ((value/submodelSingleVersionSalest[k])*100).toFixed(1)
				}
			}
			
			arrySalesSinglePriceScalesNew[k].push(value2);
		}
		arrySalesSinglePriceScalesNew.push(arrySalesSinglePriceScalesNew[k]);
	}
		
	
	//图最下面的那个table百分比
	var submodelObjectBi = new Array();
	  
		for(var i = 0;i < submodelSingleVersionSalest.length; i++){
			if(objectSales > 0 && submodelSingleVersionSalest[i] >= 0){
				submodelObjectBi.push(((submodelSingleVersionSalest[i]/objectSales)*100).toFixed(2));
			} else{
				submodelObjectBi.push(0.00);
			}
			
		}
	
	
	var tbodyHtml = "";
	tbodyHtml += "<table>";
	tbodyHtml += "<tr>";
	for(var i = 0; i < submodelNames.length; i++){
		tbodyHtml += "<td class='tdSubModel'    align='center' style='width:"+width+"px;' ><font color='white'>" + submodelNames[i] + "</font></td>";
	}
	tbodyHtml += "</tr>";
	for(var k = 0;k < arryScale.length; k++){
		tbodyHtml += "<tr>";
		for(var i = 0; i < submodelNames.length; i++){
			var value = null;
			if(null==arrySalesSinglePriceScalesNew[i][k]){
				value = "";
			} else{
				value = arrySalesSinglePriceScalesNew[i][k] +"%";
			}
			tbodyHtml += "<td class='tdSubModel1'    align='center' style='height:"+21+"px;width:"+width+"px;font-weight:bold;line-height:21px;' >" + value + "</td>";
		}
		tbodyHtml += "</tr>";
	}
	
	//中间空行
	tbodyHtml += "<tr>";
		for(var i =0 ; i < submodelObjectBi.length; i++){
			tbodyHtml += "<td class='tdSubModel2'    align='center' style='border-top:1px solid black;height:26px; width:"+width+"px' >"+ "" + "</td>";
		}
	tbodyHtml += "</tr>";
	
	//车型下的单个车型销量综合和百分比显示
	tbodyHtml += "<tr>";
	for(var i =0 ; i < submodelSingleVersionSalest.length; i++){
		if(submodelSingleVersionSalest[i]>=0){
			tbodyHtml += "<td class='tdSubModel2'    align='center' style='height:34px; width:"+width+"px' >"+ formatNum(submodelSingleVersionSalest[i]) + "</td>";
		} else{
			tbodyHtml += "<td class='tdSubModel2'    align='center' style='height:34px; width:34px;"+width+"px' >"+ formatNum(0) + "</td>";
		}
	}
		tbodyHtml += "</tr>";
		
		tbodyHtml += "<tr>";
			for(var i =0 ; i < submodelObjectBi.length; i++){
				tbodyHtml += "<td class='tdSubModel2'    align='center' style='height:25px; width:"+width+"px' >"+ submodelObjectBi[i] + "%" + "</td>";
			}
		tbodyHtml += "</tr>";
	tbodyHtml += "</table>";
	//设置柱状图的图例高度
	$("#chartTitleDiv1").height(24*arryScale.length+76);
	$("#chartTitleDiv2").html(tbodyHtml);
	
	var tbodyHtml2 = "";
	tbodyHtml2 += "<table>";
	tbodyHtml2 += "<tr>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center' style='width:150px'>" + $("#startDate").val().substr(0,4)+"年"+$("#startDate").val().substr(5,7)+"月"+"至"+$("#endDate").val().substr(0,4)+"年"+ $("#endDate").val().substr(5,7)+"月"+ "</td>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center' style='width:20px;background-color:#B2B2B2;margin:20px; border:20px solid #F9F9F9; ' >    </td>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center' style='width:72px'>"+ objectName + "</td>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center' style='width:100px'>"+ formatNum(objectSales) + "</td>";
	tbodyHtml2 += "</tr>";
	tbodyHtml2 += "<tr>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center'  style='width:150px'>                </td>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center' style='width:20px;background-color:#99CCFF;margin:20px; border:20px solid #F9F9F9;' >    </td>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center'  style='width:50px'>"+ "对比车型" + "</td>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center'  style='width:100px'>"+ formatNum(submodelSalesSum) + "</td>";
	tbodyHtml2 += "</tr>";
	tbodyHtml2 += "</table>";
	$("#chartTitleDiv3").html(tbodyHtml2);
}

/**
 * 画表格2 增加占比选项
 */
function showGrid2(data){
	
	var arryScale = data.arryScale;//价格段数组
	var arrySales = data.arrySales; //所有车型在价格段内的销量和
	var arryObjectSales = data.arryObjectSales;//所有对比对象在价格段内的销量和
	var submodelSingleVersionSalest = data.submodelSingleVersionSalest;//单个车型的销量和
	var submodelNames = data.submodelNames;//车型英文名字
	var arrySalesSinglePriceScales = data.arrySalesSinglePriceScales;//单个车型在价格段内的销量和
	var submodelSalesSum = data.submodelSalesSum;//所有车型的销量和
	//var submodelIds = data.listOrder;//车型顺序信息
	
	var objectSales = data.objectSales;//所有对比对象的销量和
	
	var width = 133 ;//右边图表标题的宽度
	var width1 = 10 ;//右边图表标题之间的空格宽度
	if(submodelNames.length>4){
		width = (600/submodelNames.length-submodelNames.length);
		width1 = submodelNames.length;
	}
	
	var objectType = $("select[name='objectType']").val();
	var objectName = null;
	if(0==objectType){
		objectName = "对比级别";
	} else if(1==objectType){
		objectName = "对比系别";
	} else if(2==objectType){
		objectName = "对比品牌";
	} else{
		objectName = "对比厂商品牌";
	}
	
	var arrySalesDao = new Array();//所有车型在价格段内的销量和倒叙排列
	for(var i = 0; i <arrySales.length;i++){
		var value = arrySales[arrySales.length-(i+1)];
		if(value < 0){
			value = null;
		} 
		arrySalesDao.push(value);//所有车型在价格段内的销量和倒叙排列
	}
	
	var arrySingleAllSubmodelScale = new Array();//单个车型价格段内销量占所有车型价格段内销量的百分比
	//计算单个车型在价格段内的销量和百分比并按价格段倒叙排序
	//var arrySalesSinglePriceScalesNew = new Array();
	//var arrySalesSiniglePriceSalesDao = new Array();//单个车型在价格段内的销量倒叙排列 
	for(var k = 0;k < arrySalesSinglePriceScales.length;k++){
		//arrySalesSinglePriceScalesNew[k]=new Array();
		//arrySalesSiniglePriceSalesDao[k]=new Array();
		arrySingleAllSubmodelScale[k] = new Array();
		for(var i = 0; i < arrySalesSinglePriceScales[k].length;i++){
			var value = arrySalesSinglePriceScales[k][arrySalesSinglePriceScales[k].length-(i+1)];
			//var value2 =null;
			//var value3 = null;
			var value4 = null;//单个车型占所有车型在价格段内的销量比例
			if(value<0){
			} else{
				//value3 = value;
				//if(submodelSingleVersionSalest[k]<=0){
					//value2 = null;
				//} else{
					//value2 = ((value/submodelSingleVersionSalest[k])*100).toFixed(1);
				//}
				if(arrySalesDao[i] != null && arrySalesDao[i] > 0){
					value4 = ((value/arrySalesDao[i])*100).toFixed(1);
				}
			}
			arrySingleAllSubmodelScale[k].push(value4);
			//arrySalesSiniglePriceSalesDao[k].push(value3);//单个车型在价格段内的销量倒叙排列
			//arrySalesSinglePriceScalesNew[k].push(value2);
		}
		arrySingleAllSubmodelScale.push(arrySingleAllSubmodelScale[k]);
		//arrySalesSiniglePriceSalesDao.push(arrySalesSiniglePriceSalesDao[k]);//单个车型在价格段内的销量倒叙排列 
		//arrySalesSinglePriceScalesNew.push(arrySalesSinglePriceScalesNew[k]);
	}
	
	var arryObjectSalesScaleTotal = 0;//所有对比对象各价格段销量之和
	for(var i = 0; i < arryObjectSales.length; i++){
		var value = 0;
		if(arryObjectSales[i] > 0){
			value = arryObjectSales[i]
		}
		arryObjectSalesScaleTotal += arryObjectSales[i] ;
	}
	//计算100%列价格段内对比对象销量和与所有对比对象销量和的百分比
	var arryObjectSalesScale = new Array();
	for(var i = 0; i < arryObjectSales.length; i++ ){
		var value = arryObjectSales[arryObjectSales.length-(i+1)];
		var value1 = null;
			if(arryObjectSalesScaleTotal > 0 && value >= 0){
				value1 = ((value/arryObjectSalesScaleTotal)*100).toFixed(1);
			}
			arryObjectSalesScale.push(value1);
	}
	
	//图最下面的那个table百分比
	var submodelObjectBi = new Array();
	  
	for(var i = 0;i < submodelSingleVersionSalest.length; i++){
		if(objectSales > 0 && submodelSingleVersionSalest[i] >= 0){
			submodelObjectBi.push(((submodelSingleVersionSalest[i]/objectSales)*100).toFixed(2));
		} else{
			submodelObjectBi.push(0.00);
		}
		
	}
	
	
	var tbodyHtml = "";
	tbodyHtml += "<table>";
	tbodyHtml += "<tr>";
	//100%列
	tbodyHtml += "<td class='tdSubModel'    align='center' style='width:"+width+"px;' ><font color='white'>" + "100%" + "</font></td>";
	for(var i = 0; i < submodelNames.length; i++){
		tbodyHtml += "<td class='tdSubModel'    align='center' style='width:"+width+"px;' ><font color='white'>" + submodelNames[i] + "</font></td>";
	}
	tbodyHtml += "</tr>";
	for(var k = 0;k < arryScale.length; k++){
		tbodyHtml += "<tr>";
		//100%占比列
		var objectBi = "";
		if(arryObjectSalesScale[k] != null){
			objectBi = arryObjectSalesScale[k] + "%";
		}
		tbodyHtml += "<td class='tdSubModel1'    align='center' style='height:"+21+"px;width:"+width+"px;font-weight:bold;line-height:21px;' >" + objectBi + "</td>";
		for(var i = 0; i < submodelNames.length; i++){
			var value = null;
			if(null==arrySingleAllSubmodelScale[i][k]){
				value = "";
			} else{
				value = arrySingleAllSubmodelScale[i][k] +"%";
			}
			tbodyHtml += "<td class='tdSubModel1'    align='center' style='height:"+21+"px;width:"+width+"px;font-weight:bold;line-height:21px;' >" + value + "</td>";
		}
		tbodyHtml += "</tr>";
	}
	
	//中间空行
	tbodyHtml += "<tr>";
		for(var i =0 ; i < submodelObjectBi.length + 1; i++){
			tbodyHtml += "<td class='tdSubModel2'    align='center' style='border-top:1px solid black;height:26px; width:"+width+"px' >"+ "" + "</td>";
		}
	tbodyHtml += "</tr>";
	
	//车型下的单个车型销量综合和百分比显示
	tbodyHtml += "<tr>";
	//100%列下面的空行
	tbodyHtml += "<td class='tdSubModel2'    align='center' style='height:34px; width:"+width+"px' >"+ "" + "</td>";
	for(var i =0 ; i < submodelSingleVersionSalest.length; i++){
		if(submodelSingleVersionSalest[i]>=0){
			tbodyHtml += "<td class='tdSubModel2'    align='center' style='height:34px; width:"+width+"px' >"+ formatNum(submodelSingleVersionSalest[i]) + "</td>";
		} else{
			tbodyHtml += "<td class='tdSubModel2'    align='center' style='height:34px; width:34px;"+width+"px' >"+ formatNum(0) + "</td>";
		}
	}
		tbodyHtml += "</tr>";
		
		tbodyHtml += "<tr>";
		//100%列下面的空行
		tbodyHtml += "<td class='tdSubModel2'    align='center' style='height:25px; width:"+width+"px' >"+ "" + "</td>";
			for(var i =0 ; i < submodelObjectBi.length; i++){
				tbodyHtml += "<td class='tdSubModel2'    align='center' style='height:25px; width:"+width+"px' >"+ submodelObjectBi[i] + "%" + "</td>";
			}
		tbodyHtml += "</tr>";
	tbodyHtml += "</table>";
	//设置柱状图的图例高度
	$("#chartTitleDiv1").height(24*arryScale.length+76);
	$("#chartTitleDiv2").html(tbodyHtml);
	
	var tbodyHtml2 = "";
	tbodyHtml2 += "<table>";
	tbodyHtml2 += "<tr>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center' style='width:150px'>" + $("#startDate").val().substr(0,4)+"年"+$("#startDate").val().substr(5,7)+"月"+"至"+$("#endDate").val().substr(0,4)+"年"+ $("#endDate").val().substr(5,7)+"月"+ "</td>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center' style='width:20px;background-color:#B2B2B2;margin:20px; border:20px solid #F9F9F9; ' >    </td>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center' style='width:72px'>"+ objectName + "</td>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center' style='width:100px'>"+ formatNum(objectSales) + "</td>";
	tbodyHtml2 += "</tr>";
	tbodyHtml2 += "<tr>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center'  style='width:150px'>                </td>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center' style='width:20px;background-color:#99CCFF;margin:20px; border:20px solid #F9F9F9;' >    </td>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center'  style='width:50px'>"+ "对比车型" + "</td>";
	tbodyHtml2 += "<td class='tdSubModel2'    align='center'  style='width:100px'>"+ formatNum(submodelSalesSum) + "</td>";
	tbodyHtml2 += "</tr>";
	tbodyHtml2 += "</table>";
	$("#chartTitleDiv3").html(tbodyHtml2);
}
/**
 * 参数验证
 */
window.paramsValidate = function()
{
	var paramObj = {};
	if ($("#sprice").val() == null || $("#sprice").val() == "" || $("#sprice").val() =="NaN") {
			alert("请选择起始价格");
			return true;
	}
	if ($("#eprice").val() == null || $("#eprice").val() == "" || $("#eprice").val() == "NaN") {
			alert("请选择结束价格");
			return true;
	}
	
	var modelIds = $(".subModelLIContainer .subModelModalResultContainer .selectorResultContainer").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//级别id
	var segmentCnd = $(".segmentLIContainer .segmentModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//系别id
	var origIds = $(".origLIContainer .origModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//品牌id
	var brandIds = $(".brandLIContainer .brandModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//厂商品牌id
	var manfs = $(".manfLIContainer .manfModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	if(""==modelIds) {
		alert("请选择对象");
		return true;
	} 
	if((""==segmentCnd||null==segmentCnd)&&(""==origIds||null==origIds)&&(""==brandIds||null==brandIds)&&(""==manfs||null==manfs)) {
		alert("请选择对比对象");
		return true;
	} 
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		if(beginDate > endDate)
		{
			alert("开始时间不能大于结束时间");
			return true;
		}
		var sprice = $("#sprice").val();
		var eprice = $("#eprice").val();
		var priceScale = $("select[name='priceScale']").val();
		if(((eprice*10000-sprice*10000)/priceScale+2)>256){
			alert("尊敬的用户，请重新选择价格段范围，已超过excell所承受最大行数！");
			return true;
		}
		if((eprice-sprice)<0){
			alert("起始价格不能大于结束结束价格");
			return true;
		}
	return false;
};

/**
 * 获取页面参数
 */
window.getParams = function() {
	var paramsType = 1;
	var objectType = $("select[name='objectType']").val();
	//车型id
	var modelIds = $(".subModelLIContainer .subModelModalResultContainer .selectorResultContainer").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//车型下的车身形式
	var submodelBodyTypeIds = $("#model .bodyTypeModalResultContainer .selectorResultContainer").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//对比对象下的车身形式
	var objectBodyTypeIds = $("#objectDiv .bodyTypeModalResultContainer .selectorResultContainer").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//级别id
	var segmentCnd = $(".segmentLIContainer .segmentModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//系别id
	var origIds = $(".origLIContainer .origModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//品牌id
	var brandIds = $(".brandLIContainer .brandModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//厂商品牌id
	var manfs = $(".manfLIContainer .manfModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	var beginDate = $("#dateYear .dateLIContainer").eq(0).find(".startDate-container input").val();
	var endDate = $("#dateYear .dateLIContainer").eq(0).find(".endDate-container input").val();
	//数据分析类型默认为销量
	//var analysisContentType = $("#moduleCode").val();
	var priceType = $("#priceType").val();//页面价格类型	
	var sprice = $("#sprice").val()//页面起始价格
	var eprice = $("#eprice").val()//页面结束价格
	var priceScale = $("#priceScale").val();//价格刻度
	var segmentType = $(".segmentType").map(function(){if($(this).prop("checked")) {return $(this).val();} else {return null;}}).get().join(",");//细分市场类别
	var paramObj = {};
	paramObj.priceType = priceType;
	paramObj.sprice = sprice;
	paramObj.eprice = eprice;
	paramObj.priceScale = priceScale;
	paramObj.segmentType = segmentType;
	paramObj.modelIds = modelIds;
	paramObj.submodelBodyTypeIds = submodelBodyTypeIds;
	paramObj.objectBodyTypeIds = objectBodyTypeIds;
	paramObj.segmentCnd = segmentCnd;
	paramObj.origIds = origIds;
	paramObj.brandIds = brandIds;
	paramObj.manfs = manfs;
	paramObj.objectType = objectType;
	paramObj.beginDate = beginDate;
	paramObj.endDate   = endDate;
	getQueryConditionAndBrowser(paramObj);
	paramObj.paramsType = paramsType;
	return paramObj;
};


window.getSubModelParams = function(obj) {
	var bodyTypeId = $(obj).closest("td").prev().find("li div :input").map(function(){return $(this).val();}).get().join(",");
	var beginDate = $("#dateYear .dateLIContainer").eq(0).find(".startDate-container input").val();
	var endDate = $("#dateYear .dateLIContainer").eq(0).find(".endDate-container input").val();
	var priceType = $("#priceType").val();//页面价格类型	
	var paramObj = {};
	paramObj.bodyTypeId = bodyTypeId;
	paramObj.priceType = priceType;
	paramObj.beginDate = beginDate;
	paramObj.endDate   = endDate;
	return paramObj;	
}

/**
 * 获取页头查询条件，以及浏览器
 */
window.getQueryConditionAndBrowser = function(paramObj) {
	paramObj.browser = navigator.appVersion;
	var queryCondition = "";
	
/*	queryCondition += "分析维度 = ";
	if("0" == $("#objectType").val()) {
		queryCondition += "级别";
	} else if("1" == $("#objectType").val()) {
		queryCondition += "系别";
	} else if("2" == $("#objectType").val()) {
		queryCondition += "品牌";
	} else if("3" == $("#objectType").val()) {
		queryCondition += "厂商品牌";
	} else {
		queryCondition += "车型";
	}*/
	
	//车型id
	var modelNames = $(".subModelLIContainer .subModelModalResultContainer .selectorResultContainer").find("li div ").map(function(){return $(this).text();}).get().join(",");
	//车型下的车身形式
	var submodelBodyTypeNames = $("#model .bodyTypeModalResultContainer .selectorResultContainer").find("li div ").map(function(){return $(this).text();}).get().join(",");
	//对比对象下的车身形式
	var objectBodyTypeName = $("#objectDiv .bodyTypeModalResultContainer .selectorResultContainer").find("li div ").map(function(){return $(this).text();}).get().join(",");
	//级别id
	var segmentCnd = $(".segmentLIContainer .segmentModalResultContainer ").find("li div ").map(function(){return $(this).text();}).get().join(",");
	//系别id
	var origIds = $(".origLIContainer .origModalResultContainer ").find("li div ").map(function(){return $(this).text();}).get().join(",");
	//品牌id
	var brandIds = $(".brandLIContainer .brandModalResultContainer ").find("li div ").map(function(){return $(this).text();}).get().join(",");
	//厂商品牌id
	var manfs = $(".manfLIContainer .manfModalResultContainer ").find("li div ").map(function(){return $(this).text();}).get().join(",");
	
	queryCondition += "\n价格类型 = ";
	if("0" == $("#priceType").val()) {
		queryCondition += "指导价";
	} else {
		queryCondition += "成交价";
	}
	
	queryCondition += "\n价格段= " + paramObj.sprice + "万    至" + paramObj.eprice + "万";
	
	queryCondition += "\n价格段刻度= " + paramObj.priceScale ;

	queryCondition += "\n时间= " + paramObj.beginDate + "至" + paramObj.endDate;
	
	if($("select[name='objectType']").val() == 0){
		queryCondition += "\n对比对象= " + "级别:" + segmentCnd + "     车身形式= " + objectBodyTypeName;
	} else if($("select[name='objectType']").val() == 1){
		queryCondition += "\n对比对象= " + "系别:" + origIds + "     车身形式= " + objectBodyTypeName;
	} else if($("select[name='objectType']").val() == 2){
		queryCondition += "\n对比对象= " + "品牌:" + brandIds + "     车身形式= " + objectBodyTypeName;
	} else{
		queryCondition += "\n对比对象= " + "厂商品牌:" + manfs + "     车身形式= " + objectBodyTypeName;
	}
	
	queryCondition += "\n对象= " + "选择车型:" + modelNames + "     车身形式= " + submodelBodyTypeNames;

	paramObj.queryCondition = queryCondition;
};


/**
 * 导出Excel
 * @param languageType EN:英文版;ZH:中文版
 */
function exportExcel(languageType) {
	if(!$.trim($("#chartTitleDiv1 div").html())) {
		alert("暂无数据导出");
		return;
	}
	$("#languageType").val(languageType);
	$("#isScale").val($("#isPromotion").prop("checked"));
	$("#exportFormId").submit();
}

/**
 * 车型弹出框
 * @param type
 */
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
	$(".subModelModalContainer").find(".pooAttributeIdInput").each(function(){
		
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
		getPriceSaleModelPage("tabs-competingProducts", type, inputType);
	} else if("2" == type) {
		getPriceSaleModelPage("tabs-segment", type, inputType);
	} else if("3" == type) {
		getPriceSaleModelPage("tabs-brand", type, inputType);
	} else {
		getPriceSaleModelPage("tabs-manf", type, inputType);
	}
};

function spriceOnBulur() {
    var sprice = $("#sprice").val();
	var eprice = $("#eprice").val();
	if(!isNaN(eprice) && eprice != "") {
		if(sprice > eprice){
			alert("开始价格不能大于结束价格！")
		} else{
			$("#sprice").attr("value", sprice);
		}
	}
	
}

function epriceOnBulur() {
	var sprice = parseInt($("#sprice").val());
	var eprice = parseInt($("#eprice").val());
	if(!isNaN(eprice) && eprice != "") {
		if(sprice > eprice){
			alert("开始价格不能大于结束价格！")
			//$("#eprice").attr("value", 0);
		} else{
			$("#eprice").attr("value", eprice);
		}
	}
}

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
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var bodyTypeId = "";
		var paramObj = {};
		var priceType = null;
			priceType = $("#priceType").val();
		//传递参数
		var params = {inputType:inputType,subModelShowType:type,beginDate:beginDate,endDate:endDate,priceType:priceType};
		var bodyTypeId = $(bodyType).closest("td").prev().find("li div :input").map(function(){return $(this).val();}).get().join(",");
		params.bodyTypeId = bodyTypeId;
		//触发请求
		showLoading(id);
		$("#" + id).load(ctx+"/pricesale/CompetingProduct/getSubmodelModal", params, function() {
			$("#selectorResultContainerBySubModel .removeBtnByResult").each(function() {
				var subModelId = $(this).attr("subModelId");
				$(".subModelModalContainer .subModelIdInput").each(function() {
					if($(this).val() == subModelId) $(this).attr("checked", "true");//行全选
				});
			});
			
			/*根据选择条件隐藏*/
			var pooAttributeIdArr = [];
			$(".subModelModalContainer").find(".pooAttributeIdInput").each(function(){
				
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


/**获取需要展示的车型**/
function refreshSubModel(series, xTitles) {
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
function rereshTitles(series, xTitles, xTitleEns) {
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
function showAllSubModel(data) {
	var position = $("#sortSubModel");
	
		subModelOrderName = new Array(data.listOrder.length);
		subModelOrderId = new Array(data.listOrder.length);
		var html = "";
		var $pick = position.find(".pick");
		var $order = position.find(".order");
		//创建可以勾选车型的列表
		for(var i = 0; i < data.listOrder.length; i++) {
			var daTa = data.listOrder[i];
			html += '<li><label><input type="checkbox" value="' + daTa.objId + '" checked="checked">' + daTa.objNameEn + '</label></li>';
		}
		//$pick.find("ul").html("");
		$pick.find("ul").html(html);
		html = "";
		//创建可以拖动车型的列表
		for(var i = 0; i < data.listOrder.length; i++) {
			var daTa = data.listOrder[i];
			html += '<li on="true" class rel="' + daTa.objId + '" style="display: list-item;" ><input type="hidden" value="' + daTa.objNameEn + '" >';
			html += '<span>' + daTa.objNameEn + '</span></li>';
			subModelOrderName[i] = daTa.objNameEn;
			subModelOrderId[i] = daTa.objId;
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

/**刷新图表**/
function refreshChart() {
	if(resultData != null) {
		if(paramsValidate2()){
			return;
		}
		$("body").showLoading();
		//收起显示设置
		$(".tab-content .buttons li a").click();
		//发送请求
		$.ajax({
	        type: "POST",
			url: ctx + "/pricesale/getCompetingProductData",
			data: getParams2(),
			success: function(data) {
				         if(data) {
				             //查询面板折叠
				        	 //$(".queryConditionContainer .buttons .toggle a").click();
				        	 $("#noData").css("display","none");
				        	 $("#exportButtons").css("display","block");
				        	 $("#chartTitleDiv1").html("");//清空echarts柱状图
				        	 $("#chartTitleDiv3").html("");//清空柱状图下的左图表
				        	 $("#isPromotion").prop("checked",false);//占比选项不选中
				        	 resultData = data;
				        	 showGrid(data);
				        	 showChart(data);
				         }
				         $("body").hideLoading();
			   		  },
			error:function() {
			          $("body").hideLoading();
			      }
		});
	} else {
		alert("请先查询数据！");
	}
}

window.getParams2 = function() {
	var objectType = $("select[name='objectType']").val();
	var modelIdss = "";
	$(".tab-content .order ul").find("li").each(function(){
		var ths = $(this);
		if($(ths).css("display") != "none"){
			modelIdss += $(ths).attr("rel")+",";
		}
	})
	var paramsType = 2;
	//车型id
	var modelIds = modelIdss.substr(0,modelIdss.length-1);
	//车型下的车身形式
	var submodelBodyTypeIds = $("#model .bodyTypeModalResultContainer .selectorResultContainer").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//对比对象下的车身形式
	var objectBodyTypeIds = $("#objectDiv .bodyTypeModalResultContainer .selectorResultContainer").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//级别id
	var segmentCnd = $(".segmentLIContainer .segmentModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//系别id
	var origIds = $(".origLIContainer .origModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//品牌id
	var brandIds = $(".brandLIContainer .brandModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//厂商品牌id
	var manfs = $(".manfLIContainer .manfModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	var beginDate = $("#dateYear .dateLIContainer").eq(0).find(".startDate-container input").val();
	var endDate = $("#dateYear .dateLIContainer").eq(0).find(".endDate-container input").val();
	//数据分析类型默认为销量
	//var analysisContentType = $("#moduleCode").val();
	var priceType = $("#priceType").val();//页面价格类型	
	var sprice = $("#sprice").val()//页面起始价格
	var eprice = $("#eprice").val()//页面结束价格
	var priceScale = $("#priceScale").val();//价格刻度
	var segmentType = $(".segmentType").map(function(){if($(this).prop("checked")) {return $(this).val();} else {return null;}}).get().join(",");//细分市场类别
	var paramObj = {};
	paramObj.priceType = priceType;
	paramObj.sprice = sprice;
	paramObj.eprice = eprice;
	paramObj.priceScale = priceScale;
	paramObj.segmentType = segmentType;
	paramObj.modelIds = modelIds;
	paramObj.submodelBodyTypeIds = submodelBodyTypeIds;
	paramObj.objectBodyTypeIds = objectBodyTypeIds;
	paramObj.segmentCnd = segmentCnd;
	paramObj.origIds = origIds;
	paramObj.brandIds = brandIds;
	paramObj.manfs = manfs;
	paramObj.objectType = objectType;
	getQueryConditionAndBrowser(paramObj);
	paramObj.beginDate = beginDate;
	paramObj.endDate   = endDate;
	paramObj.paramsType = paramsType;
	return paramObj;
};

function paramsValidate2(){

	var paramObj = getParams2();
	if ($("#sprice").val() == null || $("#sprice").val() == "" || $("#sprice").val() =="NaN") {
			alert("请选择起始价格");
			return true;
	}
	if ($("#eprice").val() == null || $("#eprice").val() == "" || $("#eprice").val() == "NaN") {
			alert("请选择结束价格");
			return true;
	}
	
	var modelIds = paramObj.modelIds;
	//级别id
	var segmentCnd = $(".segmentLIContainer .segmentModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//系别id
	var origIds = $(".origLIContainer .origModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//品牌id
	var brandIds = $(".brandLIContainer .brandModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	//厂商品牌id
	var manfs = $(".manfLIContainer .manfModalResultContainer ").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	if(""==modelIds) {
		alert("请选择对象");
		return true;
	} 
	if((""==segmentCnd||null==segmentCnd)&&(""==origIds||null==origIds)&&(""==brandIds||null==brandIds)&&(""==manfs||null==manfs)) {
		alert("请选择对比对象");
		return true;
	} 
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		if(beginDate > endDate)
		{
			alert("开始时间不能大于结束时间");
			return true;
		}
		var sprice = $("#sprice").val();
		var eprice = $("#eprice").val();
		var priceScale = $("select[name='priceScale']").val();
		if(((eprice*10000-sprice*10000)/priceScale+2)>256){
			alert("尊敬的用户，请重新选择价格段范围，已超过excell所承受最大行数！");
			return true;
		}
		if((eprice-sprice)<0){
			alert("起始价格不能大于结束结束价格");
			return true;
		}
	return false;
}
