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
	htmlStr += '	  		<input type="text" value="' + defaultBeginDate + '"  readonly="readonly" class="input-mini white" placeholder="开始日期" />';
	htmlStr += '	  		<span class="add-on"><i class="icon-th"></i></span>';
	htmlStr += '		</span>';
	htmlStr += '		<span class="2">至</span>';
    htmlStr += '  		<span class="endDate-container input-append date">';
	htmlStr += '	  		<input type="text" value="' + endDate + '" readonly="readonly" class="input-mini white"  placeholder="结束日期" />';
	htmlStr += '	  		<span class="add-on"><i class="icon-th"></i></span>';
	htmlStr += '		</span>';
	htmlStr += '		<span style="margin-left:20px;">';
	htmlStr += '			<i class="icon-plus addDateBtn" style="cursor:pointer;" title="增加"></i>';
	htmlStr += '			<i class="icon-minus removeDateBtn" style="margin-left: 20px;cursor:pointer;" title="删除"></i>';
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
	htmlStr += '						<option value="4">车型</option>';
	htmlStr += '					</select>';
	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" style="width:70px;">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn btn-mini segmentSelector" data-toggle="modal">选择级别</a>';
 	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:165px">';
	htmlStr += '				<div style="margin-left:0px" class="segmentModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:60px;">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn btn-mini bodyTypeSelector" data-toggle="modal">车身形式</a>';
 	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:150px;">';
	htmlStr += '				<div  style="margin-left:0px" class="bodyTypeModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:70px;">';
	htmlStr += '				<div class="control-group" style="margin-left:20px;margin-top: 2px;">';
  	htmlStr += '					<i class="icon-plus addSegmentBtn" style="cursor:pointer;" title="增加" ></i>';
  	htmlStr += '					<i class="icon-minus removeSegmentBtn" style="margin-left: 20px;cursor:pointer;" title="删除"></i>';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:60px;">';
	htmlStr += '				<div class="control-group" style="margin-left:10px;margin-top: 2px;">';
  	htmlStr += '					&nbsp;&nbsp;拆分<input name = "obj_Split" type="checkbox" value="checkbox">';
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
	htmlStr += '						<option value="4">车型</option>';
	htmlStr += '					</select>';
	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" style="width:70px;">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn btn-mini manfSelector" data-toggle="modal">选择厂商</a>';
 	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:165px;">';
	htmlStr += '				<div  style="margin-left:0px" class="manfModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:60px;">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn btn-mini bodyTypeSelector" data-toggle="modal">车身形式</a>';
 	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:150px;">';
	htmlStr += '				<div  style="margin-left:0px" class="bodyTypeModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:70px;">';
	htmlStr += '				<div class="control-group" style="margin-left:20px;margin-top: 2px;">';
  	htmlStr += '					<i class="icon-plus addManfBtn" style="cursor:pointer;" title="增加" ></i>';
  	htmlStr += '					<i class="icon-minus removeManfBtn" style="margin-left: 20px;cursor:pointer;" title="删除"></i>';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:60px;">';
	htmlStr += '				<div class="control-group" style="margin-left:10px;margin-top: 2px;">';
  	htmlStr += '					&nbsp;&nbsp;拆分<input name = "obj_Split" type="checkbox" value="checkbox">';
	htmlStr += '				</div>';
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
	htmlStr += '						<option value="4">车型</option>';
	htmlStr += '					</select>';
	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" style="width:70px;">	';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn btn-mini brandSelector" data-toggle="modal">选择品牌</a>';
 	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:165px;">';
	htmlStr += '				<div  style="margin-left:0px" class="brandModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:60px;">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn btn-mini bodyTypeSelector" data-toggle="modal">车身形式</a>';
 	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:150px;">';
	htmlStr += '				<div  style="margin-left:0px" class="bodyTypeModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:70px;">';
	htmlStr += '				<div class="control-group" style="margin-left:20px;margin-top: 2px;">';
  	htmlStr += '					<i class="icon-plus addBrandBtn" style="cursor:pointer;" title="增加" ></i>';
  	htmlStr += '					<i class="icon-minus removeBrandBtn" style="margin-left: 20px;cursor:pointer;" title="删除"></i>';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:60px;">';
	htmlStr += '				<div class="control-group" style="margin-left:10px;margin-top: 2px;">';
  	htmlStr += '					&nbsp;&nbsp;拆分<input name = "obj_Split" type="checkbox" value="checkbox">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '		</tr>';
	htmlStr += '	</table>';
	htmlStr += '</li>';
	return htmlStr;
};

/**获取子车型模板*/
function getSubModelTp(){
	var htmlStr = "";
	htmlStr += '<li style="list-style: none;margin-bottom:10px;" class="subModelLIContainer">';
	htmlStr += '	<table style="width:840px;">';
	htmlStr += '		<tr>';
	htmlStr += '			<td valign="top" style="width:220px;">';
	htmlStr += '				<div>';
	htmlStr += '					<select name="objectType" onchange ="changeObjectType(this)">';
	htmlStr += '						<option value="0">级别</option>';
	htmlStr += '						<option value="1">系别</option>';
	htmlStr += '						<option value="2">品牌</option>';
	htmlStr += '						<option value="3">厂商品牌</option>';
	htmlStr += '						<option value="4" selected>车型</option>';
	htmlStr += '					</select>';
	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:70px;">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn btn-mini bodyTypeSelector" data-toggle="modal">车身形式</a>';
 	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:165px;">';
	htmlStr += '				<div  style="margin-left:0px" class="bodyTypeModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" style="width:60px;">	';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn btn-mini subModelSelector" data-toggle="modal">选择车型</a>';
 	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:150px;">';
	htmlStr += '				<div  style="margin-left:0px" class="subModelModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:70px;">';
	htmlStr += '				<div class="control-group" style="margin-left:20px;margin-top: 2px;">';
  	htmlStr += '					<i class="icon-plus addSubModelBtn" style="cursor:pointer;" title="增加" ></i>';
  	htmlStr += '					<i class="icon-minus removeSubModelBtn" style="margin-left: 20px;cursor:pointer;" title="删除"></i>';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:60px;">';
	htmlStr += '				<div class="control-group" style="margin-left:10px;margin-top: 2px;">';
  	htmlStr += '					&nbsp;&nbsp;拆分<input name = "obj_Split" type="checkbox" value="checkbox">';
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
	htmlStr += '						<option value="4">车型</option>';
	htmlStr += '					</select>';
	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" style="width:70px">	';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn btn-mini origSelector" data-toggle="modal">选择系别</a>';
 	htmlStr += '				</div>';
 	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:165px;">';
	htmlStr += '				<div  style="margin-left:0px" class="origModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:60px;">';
	htmlStr += '				<div>';
	htmlStr += '					<a href="#" role="button" class="btn btn-mini bodyTypeSelector" data-toggle="modal">车身形式</a>';
 	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:150px;">';
	htmlStr += '				<div  style="margin-left:0px" class="bodyTypeModalResultContainer">';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:70px;">';
	htmlStr += '				<div class="control-group" style="margin-left:20px;margin-top: 2px;">';
  	htmlStr += '					<i class="icon-plus addOrigBtn" style="cursor:pointer;" title="增加" ></i>';
  	htmlStr += '					<i class="icon-minus removeOrigBtn" style="margin-left: 20px;cursor:pointer;" title="删除"></i>';
	htmlStr += '				</div>';
	htmlStr += '			</td>';
	htmlStr += '			<td valign="top" nowrap="nowrap" style="width:60px;">';
	htmlStr += '				<div class="control-group" style="margin-left:10px;margin-top: 2px;">';
  	htmlStr += '					&nbsp;&nbsp;拆分<input name = "obj_Split" type="checkbox" value="checkbox">';
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

//日期-增加按扭
$(".addDateBtn").live('click',function() {
	//将拆分勾选去除
	$("#objectDiv").find("input[name='obj_Split']").removeAttr("checked");
	if(6 <= $("#dateYear .dateLIContainer").length) {
		alert("最多只能添加6个时间段");
		return;
	}
	
	var oUl = $(this).parents('.dateULContainer');
	oUl.append(getDateTp("add"));
	//重新初始化日期控制
	initDate();
    
	//删除多余对象开始
	var segmentLiList = $('.segmentULContainer').find('.segmentLIContainer');
	var origList = $('.origULContainer').find('.origLIContainer');
	var brandList = $('.brandULContainer').find('.brandLIContainer');
	var manfList = $('.manfULContainer').find('.manfLIContainer');
	var subModelList = $('.subModelULContainer').find('.subModelLIContainer');
	
	//级别对象不为空时删除其他对象
	if(segmentLiList.length != 0) {
		if(segmentLiList && segmentLiList.length > 1) {
			for(var i = 1; i < segmentLiList.length; i++) {
				$(segmentLiList[i]).remove();
			}
		}
		$(".origULContainer").remove();
		$(".brandULContainer").remove();
		$(".manfULContainer").remove();			
		$(".subModelULContainer").remove();
	} else if(origList.length != 0) {
		if(origList && origList.length > 1) {
			for(var i = 1; i < origList.length; i++) {
				$(origList[i]).remove();
			}
		}
		$(".brandULContainer").remove();
		$(".manfULContainer").remove();
		$(".subModelULContainer").remove();
	} else if(brandList.length != 0) {
		if(brandList && brandList.length > 1) {
			for(var i = 1; i < brandList.length; i++) {
				$(brandList[i]).remove();
			}
		}
		$(".manfULContainer").remove();
		$(".subModelULContainer").remove();
	} else if(manfList.length != 0) {
		if(manfList && manfList.length > 1) {
			for(var i = 1; i < manfList.length; i++) {
				$(manfList[i]).remove();
			}
		}
		$(".subModelULContainer").remove();
	} else if(subModelList.length != 0) {
		if(subModelList && subModelList.length > 1) {
			for(var i = 1; i < subModelList.length; i++) {
				$(subModelList[i]).remove();
			}
		}
	}
	//删除多余对象-结束
	
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
	defaultBeginDate = parseInt(defaultBeginDate.substr(0,4)) + 1 + "-01";
	endDate = parseInt(endDate.substr(0,4)) + 1 + "-12";
	$(oCurrLi).remove();
});


/**初始化时，加载对象模板*/
function initPage() {
	$(".dateULContainer").each(function() {
	    $(this).append(getDateTp());
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
		currBodyTypeLI = $(this).parents("table").find(".bodyTypeModalResultContainer");
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
		$("#bodyTypeModalBody").load(ctx + "/global/getBodyType", getParams(), function() {
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
		$("#segmentModalBody").load(ctx + "/global/getSegmentAndChildren", getParams(), function() {
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
//    		$("#segmentModalBody").off("segmentChange");
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
            			/*} else if(segmentId == "1" || segmentId == "2" || segmentId == "3") {
					var change = false;
					$(".segmentType").each(function() {
						for(var i = 0; i < notChoose.length; i++) {
							var obj = notChoose[i];
							var segment = $(this);
							if(segment.attr("segmentValue") == obj) {
								if(segment.prop("checked")) {
									change = true;
									$(this).trigger("click", "ok");
								}
							}
						}
					});
					if (change) {
						$(".segmentModalByAll").off("segmentChange1").on("segmentChange1", function() {
							$(".segmentModalByAll").each(function() {
								$(this).prop("checked", true);//全选
							});
							$(".segmentModalContainer .segmentModalByLevel1").each(function() {
								$(this).prop("checked", true);//行全选
							});
					 		$(".segmentModalContainer .segmentModalByLevel2").each(function() {
								$(this).prop("checked", true);//行全选
							});
					 		$(".segmentModalByAll").off("segmentChange1");
						});
					} else {
						$(".segmentModalByAll").each(function() {
							$(this).prop("checked", true);//全选
						});
						$(".segmentModalContainer .segmentModalByLevel1").each(function() {
							$(this).prop("checked", true);//行全选
						});
				 		$(".segmentModalContainer .segmentModalByLevel2").each(function() {
							$(this).prop("checked", true);//行全选
						});
					}*/
            		} else {
            			/*$(".segmentModalContainer .segmentModalByLevel1").each(function() {
						if($(this).val() == segmentId) {
							$(this).attr("checked", "checked");//行全选
						}
					});*/
            			/*(function (segmentId) {
			    		$("#segmentModalBody").on("segmentChange", function() {
			    			$(".segmentModalContainer .segmentModalByLevel2").each(function() {
			    				if($(this).val() == segmentId) {
			    					$(this).click();
			    				}
			    			});
//			    			$("#segmentModalBody").off("segmentChange");
			    		});
			    	})(segmentId);*/
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
				/*var level2RowTotalAmount = $(this).find(".segmentModalByLevel2").length;
				var level2RowSelectedAmount = $(this).find(".segmentModalByLevel2:checked").length;
				if(level2RowTotalAmount == level2RowSelectedAmount){
					$(this).find(".segmentModalByLevel1").each(function() {
						strHTML += '<li>';
						strHTML += '<div class="removeBtn" relContainer="' + containerId + '" value="' + $(this).val() + '" style="cursor:pointer" title="删除：' + $.trim($(this).parent().text()) + '">';
						strHTML += '<input type="hidden" value="' + $(this).val() + '" name="' + relInputName + '" levelType="1" />';
						strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
						strHTML += '</div>';
						strHTML += '</li>';
					});
				} else {*/
					$(this).find(".segmentModalByLevel2:checked").each(function() {
						strHTML += '<li>';
						strHTML += '<div class="removeBtn" relContainer="' + containerId + '" value="' + $(this).val() + '" style="cursor:pointer" title="删除：' + $.trim($(this).parent().text()) + '">';
						strHTML += '<input type="hidden" value="' + $(this).val() + '" name="' + relInputName + '"  levelType="2" />';
						strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
						strHTML += '</div>';
						strHTML += '</li>';
					});
//				}
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
		$("#manfModalBody").load(ctx + "/global/getManf", getParams(), function() {
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
		$("#brandModalBody").load(ctx + "/global/getBrand", getParams(), function() {
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
		$("#subModelModalBody").load(ctx + "/pricesale/VolumeByPriceRange/getSubmodelModal", getSubModelParams(bodyType), function() {
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
	
	/**展示车身形式弹出框*/
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
		$("#origModalBody").load(ctx + "/pricesale/global/getOrig", getParams(), function(){
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
			url: ctx + "/pricesale/getVolumeByPriceRangeData",
			data: getParams(),
			success: function(data) {
				         if(data) {
				             //查询面板折叠
				        	 $(".queryConditionContainer .buttons .toggle a").click();
				        	 $("#noData").css("display","none");
				        	 $("#exportButtons").css("display","block");
				        	 $("#chartId").height(450);
				        	 myChart.resize();
				        	 showChart(data);
				        	 showGrid(data);
				         }
				         $("body").hideLoading();
			   		  },
			error:function() {
			          $("body").hideLoading();
			      }
		});
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
 * 画线图
 */
window.showChart = function(json) {
	if(json) {
		var series = json.series;
		//线图颜色
		var colors = ["#1E90FF", "#333333", "#969696", "#DC143C", "#DA70D6", "#8B008B", "#8A2BE2", "#0000FF", "#008B8B", "#3CB371"];
		for(var i = 0; i < series.length; i++) {
			var obj = series[i];
			var show = true;
			if(0 == i) {
				obj.symbol = "triangle";//设置点形状 
			}
			var color = "";
			if(i < colors.length) {
				color = colors[i];
			} else {
				color = colors[colors.length - 1];
			}
			obj.itemStyle = {normal:{color: color, label: {show: show, position: "top", formatter: function(v1, v2, v3) {
				if("-0" == v3) {
					return "";
				}
				return window.formatData(v3);
			}
		    }}};
		}
		var y1Title = "销量";
		var option = {
 			    tooltip : {
 			        trigger: "item"
			    },
			    legend: {
			        data:json.legends
			    },
			    toolbox: {
			        show : false,
			        feature : {
			            mark : false,
			            dataView : {readOnly: false},
			            magicType:["line"],
			            restore : true,
			            saveAsImage : true
			        }
			    },
			    xAxis : [
			        {
						nameTextStyle:{color:"red"},
			            type : "category",
			            axisTick:{lineStyle:{color:"#BBBBBB"}},
			            splitLine : {show : false,lineStyle:{color:"#F0F0F0"}},
			            axisLine:{lineStyle:{color:"#BBBBBB"}},
			            axisLabel:{interval:0,rotate:30},
			            data : json.xTitles
			        }
			    ],
			    yAxis : [
			        {
			        	name:y1Title,
			        	nameTextStyle:{color:"#001346"},
			            type : "value",
			            max : json.boundarys[0],
			            min : json.boundarys[1],
			            splitNumber:json.boundarys[2],
			            splitArea : {show : false},
			            axisLine:{lineStyle:{color:"#BBBBBB"}},
			            splitLine : {show : false,lineStyle:{color:"#F0F0F0"}},
			            
			        }
			    ],
			    series : series
			};
		   var dataLi = $("#dateYear .dateLIContainer").length;
		   //如果只有一个时间组
		   if(1 == dataLi) {
			   option.yAxis[1] = {};
		   }
		   myChart.clear();
 		   myChart.setOption(option);
 		   $("#yMax").val(json.boundarys[0]);
 		   $("#yMin").val(json.boundarys[1]);
 		   $("#splitNumber").val(json.boundarys[2]);
		   if(series.length == 0){
		   	 $("#noData").css("display","block");
			 $("#exportButtons").css("display","none");
		   }
		   $("#chartId").height(480);
	}
};

/**
 * 画表格
 */
window.showGrid = function(json)
{
	//设置动态表格宽度
	var echartsWidth = $("#chartId").width() - 46;//echarts图形宽度
	var xLength = json.xTitles.length;//X轴长度
	var tdWidth = ((echartsWidth-120) / xLength).toFixed(0);
	var firstTdWidth = 120;
	if(xLength > 9) {
		firstTdWidth = parseInt(firstTdWidth) + parseInt(xLength);
	}
	
	var series = json.series; //图形数组
	$("#discountLineTable").width(echartsWidth);
	
	//时间多个时只展示两条数据
	var length = series.length;
	if(length > 2 && 1 < $("#dateYear .dateLIContainer").length ) {
		length = 2;
	}
	
	var tbodyHtml = "<tbody>";
	for(var i = 0; i < length; i++) {
		var obj = series[i];
		var data = obj.data;
		
		var className = "tbodytext";
		if(0 == i % 2) {
			className = "tbodytext odd";
		}
		
		var tdTitle = "MSRP";
		var x1Title = '[MSRP价格段:'+$("#sprice").val()+"~"+$("#eprice").val()+"万]";
		if("1" == $("#priceType").val()) {
			tdTitle = "TP";
			x1Title = '[TP价格段:'+$("#sprice").val()+"~"+$("#eprice").val()+"万]";
		}
		
		if("bar" == obj.type) {
			continue;
		}
		if(i == 0) {
//			tbodyHtml += '<tr><td colspan="'+(data.length+1)+'" style ="text-align:right;border:0px;">'+x1Title+'</td></tr>';
			tbodyHtml += '<tr><td colspan="'+(data.length+1)+'" style ="text-align:left;border:0px;">'+x1Title+'</td></tr>';
		}
		tbodyHtml += "<tr>";
//		tbodyHtml += "<td class='"+className.replace('tbodytext','')+"'style='width:"+firstTdWidth+"px;text-align:center;word-break:break-all;word-wrap:break-word;'><span>" + obj.name + " " + tdTitle + "</span></td>";
		tbodyHtml += "<td class='"+className.replace('tbodytext','')+"'style='width:"+firstTdWidth+"px;text-align:center;word-break:break-all;word-wrap:break-word;'><span>" + obj.name + "</span></td>";
		for(var j = 0;j < data.length; j++)
		{
			var value = data[j];
			if("-" == value) {
				value = "";
			} else {
				//千分位
				value = window.formatData(value);
			}
			
			tbodyHtml += "<td class='"+className+"' style='width:"+tdWidth+"px;' >" + value + "</td>";
		}
		tbodyHtml += "</tr>";
	}
	tbodyHtml += "</tbody>";
	$("#discountLineTable").html(tbodyHtml);
};

/**
 * 参数验证
 */
window.paramsValidate = function()
{
	var paramObj = {};
	if ($("#sprice").val() == null || $("#sprice").val() == "") {
		alert("请选择起始价格");
		return true;
	}
	if ($("#eprice").val() == null || $("#eprice").val() == "") {
		alert("请选择结束价格");
		return true;
	}
	
	getObjValueByType(paramObj);
	if(!paramObj.objId) {
		alert("请选择对象");
		return true;
	} else if(paramObj.objectType.split("|").length != paramObj.objId.split("|").length) {
		alert("对象选择不完整");
		return true;
	}
	getDateGroup(paramObj);
	var dateGroup = (paramObj.dateGroup).substr(0,paramObj.dateGroup.length - 1).split("|");
	for(var i = 0; i < dateGroup.length; i++) {
		var date = dateGroup[i].split(",");
		var beginDate = parseInt(date[0].replace("-", ""));
		var endDate = parseInt(date[1].replace("-", ""));
		if(beginDate > endDate)	{
			alert("第" + (i + 1) + "条时间段，开始时间不能大于结束时间");
			return true;
		}
		if(dateGroup.length > 1) {
			//如果是多条时间段
			if((endDate - beginDate) >= 100) {
				alert("第" + (i + 1) + "条时间段，时间不能跨年");
				return true;
			}
		}
	}
	
	var sprice = $("#sprice").val();
	var eprice = $("#eprice").val();
	var priceScale = $("select[name='priceScale']").val();
	if((eprice * 10000 - sprice * 10000) / priceScale > 256) {
		alert("尊敬的用户，请重新选择价格段范围，已超过excel所承受最大行数！");
		return true;
	}
	if((eprice - sprice) < 0) {
		alert("起始价格不能大于结束结束价格");
		return true;
	}
	return false;
};

/**
 * 获取页面参数
 */
window.getParams = function() {
	var beginDate = $("#dateYear .dateLIContainer").eq(0).find(".startDate-container input").val();
	var endDate = $("#dateYear .dateLIContainer").eq(0).find(".endDate-container input").val();
	//数据分析类型默认为销量
	var analysisContentType = $("#moduleCode").val();
	var priceType = $("#priceType").val();//页面价格类型	
	var sprice = $("#sprice").val()//页面起始价格
	var eprice = $("#eprice").val()//页面结束价格
	var priceScale = $("#priceScale").val();//价格刻度
	var segmentType = $(".segmentType").map(function(){if($(this).prop("checked")) {return $(this).val();} else {return null;}}).get().join(",");//细分市场类别
//	var segmentType = $(".segmentModalResultContainer #newSegmentType").val();
	var paramObj = {};
	var timeRange ={}; 
	paramObj.analysisContentType = analysisContentType;
	paramObj.priceType = priceType;
	paramObj.sprice = sprice;
	paramObj.eprice = eprice;
	paramObj.priceScale = priceScale;
	paramObj.segmentType = segmentType;
	getObjValueByType(paramObj);
	getDateGroup(paramObj);
	getQueryConditionAndBrowser(paramObj);
	timeRange = getTimeRange(paramObj.dateGroup);
	paramObj.beginDate = timeRange.beginDate;
	paramObj.endDate   = timeRange.endDate;
	return paramObj;
};


window.getSubModelParams = function(obj) {
	var bodyTypeId = $(obj).closest("td").prev().find("li div :input").map(function(){return $(this).val();}).get().join(",");
	var beginDate = $("#dateYear .dateLIContainer").eq(0).find(".startDate-container input").val();
	var endDate = $("#dateYear .dateLIContainer").eq(0).find(".endDate-container input").val();
	var priceType = $("#priceType").val();//页面价格类型	
	var analysisContentType = $("#moduleCode").val();
	var paramObj = {};
	var timeRange ={}; 
	paramObj.bodyTypeId = bodyTypeId;
	paramObj.priceType = priceType;
	paramObj.analysisContentType = analysisContentType;
	getDateGroup(paramObj);
	timeRange = getTimeRange(paramObj.dateGroup);
	paramObj.beginDate = timeRange.beginDate;
	paramObj.endDate   = timeRange.endDate;
	return paramObj;	
}

/**
 * 获取页头查询条件，以及浏览器
 */
window.getQueryConditionAndBrowser = function(paramObj) {
	paramObj.browser = navigator.appVersion;
	var queryCondition = "";
	
	queryCondition += "分析维度 = ";
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
	}
	
	queryCondition += "\n价格类型 = ";
	if("0" == $("#priceType").val()) {
		queryCondition += "指导价";
	} else {
		queryCondition += "成交价";
	}
	queryCondition += "\n时间= " + paramObj.dateGroup;
	queryCondition += "\n对象= " + paramObj.objName + "     车身形式= " + paramObj.bodyTypeName;
	paramObj.queryCondition = queryCondition;
};

/**
 * 获取时间组
 * paramObj JSON对象
 */
window.getDateGroup = function(paramObj)
{
	var dateGroup = "";
	var dataLi = $("#dateYear .dateLIContainer");
	$.each(dataLi,function(i,n){
		dateGroup += $(n).find(".startDate-container :input").val() + "," +  $(n).find(".endDate-container :input").val() + "|";
	});
	if(dataLi.length > 1) {
		paramObj.multiType = "1";//证明时间多选
	}
	paramObj.dateGroup = dateGroup;
};

/**
 * 获取弹出框选择值
 * objectType 页面分析维度
 * paramObj JSON对象
 */
window.getObjValueByType = function(paramObj) {
	var t_objId = "";//定义选择框所选值
	var t_objectType = "";//定义选择框对象类型
	var t_objName = "";//定义选择框所选名称
	var t_bodyTypeName = "";//定义选择框所选车身形式名称
	var t_bodyTypeId = "";//定义选择框所选车身形式ID
	var t_hatchbackId = "";//定义车型选择框车型的车身形式
	
	//选择细分市场
	var segmentObj = $("#objectDiv .segmentLIContainer");
	//选择厂商
	var manfObj = $("#objectDiv .manfLIContainer");
	//选择品牌
	var brandObj = $("#objectDiv .brandLIContainer");
	//选择车型
	var subModelObj = $("#objectDiv .subModelLIContainer");
	//选择系别
	var origObj = $("#objectDiv .origLIContainer");
	
	var objArray = new Array();
	objArray[0] = segmentObj;
	objArray[1] = origObj;
	objArray[2] = brandObj;
	objArray[3] = manfObj;
	objArray[4] = subModelObj;
	for(var k = 0; k < objArray.length; k++) {
		var objId = "";//定义选择框所选值
		var objectType = "";//定义选择框对象类型
		var objName = "";//定义选择框所选名称
		var bodyTypeName = "";//定义选择框所选车身形式名称
		var bodyTypeId = "";//定义选择框所选车身形式ID
		var hatchbackId = "";//定义车型选择框车型的车身形式
		//循环对象
		$.each(objArray[k],function(i,n) {
			//定义获取TD值锁引，如果是车型则需把锁引反过来
			var objValueIndex = 2;
			var bodyTypeIndex = 4;
			var num = 0;
			if($(n).attr("class") == "subModelLIContainer") {
				objValueIndex = 4; bodyTypeIndex = 2;
			}
			
			var obj_spilt = $(n).find("td").eq(6).find("div :checkbox").attr("checked");
			var t_map =$(n).find("td").eq(objValueIndex).find("li div :input").map(function(){return $(this).val();});
			var id = t_map.get().join(",");
			
			if(id) {
			    if(obj_spilt == "checked") {
			    	objId += (id+ "|").replace(/,/g,'|');
			    } else {
			    	objId += id + "|";
			    }
			}
			if(k == 0 && obj_spilt == "checked") {
				num = id.split(",").length;
			} else {
				num = t_map.length;
			}
			var type = $(n).find("td").eq(0).find("select").map(function(){return $(this).val();}).get().join(",");
			if(type) {
				objectType += type + "|";
			}
			//拆分对象情况
			if(obj_spilt == "checked" && num > 1) {
				for(var i = 1; i < num; i ++) {
					objectType += type + "|";
				}
			}
			var name = $(n).find("td").eq(objValueIndex).find("li div").map(function(){return $(this).text();}).get().join(",");
			if(name) {
				if(k == 0) {
					objName += name + "|";
					if(obj_spilt == "checked" && num > 1) {
						for(var i = 1; i < num; i ++) {
							objName += name + "|";
						}
					}
				} else {
					if(obj_spilt == "checked") {
					   	objName += (name+ "|").replace(/,/g,'|');
					} else {
					   	objName += name + "|";
					}
				}
			}
			var bid = $(n).find("td").eq(bodyTypeIndex).find("li div :input").map(function(){return $(this).val();}).get().join(",");
			if(bid) {
				bodyTypeId += bid + "|";
				//拆分对象情况
				if(obj_spilt == "checked" && num > 1) {
					for(var i = 1;i < num; i ++) {
						bodyTypeId += bid + "|";
					}
				} 
			} else {
				if(id) {
					bodyTypeId += "0|";
				}
				//拆分对象情况
				if(obj_spilt == "checked" && num > 1) {
					for(var i = 1; i < num; i ++) {
						bodyTypeId += "0|";
					}
				}
			}
			
			var bname = $(n).find("td").eq(bodyTypeIndex).find("li div").map(function(){return $(this).text();}).get().join(",");
			if(bname) {
				bodyTypeName += bname + "|";
				//拆分对象情况
				if(obj_spilt == "checked" && num > 1) {
					for(var i = 1; i < num; i ++) {
						bodyTypeName += bname + "|";
					}
				}	
			} else {
				if(id) {
					bodyTypeName += "全部|";
				}
				if(obj_spilt == "checked" && num > 1) {
					for(var i = 1; i < num; i ++) {
						bodyTypeName += "全部|";
					}
				}
			}
		});
		if (objArray[k].length > 0) {
			t_objId += objId;
			t_objectType += objectType;
			t_objName += objName;
			t_bodyTypeId += bodyTypeId;
			t_bodyTypeName += bodyTypeName;
			t_hatchbackId += $("#model .subModelLIContainer").eq($("#getModelIndexId").val()).find("td:eq(1)").find("li div :input").map(function(){return $(this).val();}).get().join(",");
		}
	}
	paramObj.objId = t_objId;
	paramObj.objectType = t_objectType;
	paramObj.objName = t_objName;
	paramObj.bodyTypeId = t_bodyTypeId;
	paramObj.bodyTypeName = t_bodyTypeName;
	paramObj.hatchbackId = t_hatchbackId;
}; 

/**
 * 导出Excel
 * @param languageType EN:英文版;ZH:中文版
 */
function exportExcel(languageType) {
	if(!$.trim($("#discountLineTable").html())) {
		alert("暂无数据导出");
		return;
	}
	$("#languageType").val(languageType);
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
		sprice > eprice ? $("#sprice").attr("value", "") : $("#sprice").attr("value", sprice);
	}
}

function epriceOnBulur() {
	var sprice = parseInt($("#sprice").val());
	var eprice = parseInt($("#eprice").val());
	if(!isNaN(eprice) && eprice != "") {
		sprice > eprice ? $("#eprice").attr("value", "") : $("#eprice").attr("value", eprice);
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
		var dateGroup = null;
		var priceType = null;
		getDateGroup(paramObj)
		//数据分析类型如果没有默认为成交价
		var analysisContentType = $("#moduleCode").val();
		//如果数据分析类型是销量校验
		if("salespriceanaly" == analysisContentType) {	
			dateGroup = paramObj.dateGroup;
			var timeRange = getTimeRange(dateGroup);
			beginDate = timeRange.beginDate;
			endDate   = timeRange.endDate;
			priceType = $("#priceType").val();
		}
		//传递参数
		var params = {inputType:inputType,subModelShowType:type,beginDate:beginDate,endDate:endDate,analysisContentType:analysisContentType,timeType:timeType,dateGroup:dateGroup,priceType:priceType};
		var bodyTypeId = $(bodyType).closest("td").prev().find("li div :input").map(function(){return $(this).val();}).get().join(",");
		params.bodyTypeId = bodyTypeId;
		//触发请求
		showLoading(id);
		$("#" + id).load(ctx+"/pricesale/VolumeByPriceRange/getSubmodelModal", params, function() {
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
	timeRange.endDate   = dates[maxEDateIndex].split(",")[1].replace("-", "");
	return timeRange;
}
