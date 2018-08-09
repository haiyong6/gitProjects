/**定义子车型LI全局变量*/
var currSubModelLI = null;
/**定义车身形式LI全局变量*/
var currBodyTypeLI = null;

$(function() {
	//页面加载隐藏图表面板
	$(".tab-content").attr("style","display:none");
    //初始化页面
	initPage();
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
	});
	
    $('#startDate-container.input-append.date').on('changeDate',function(){
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
    //常用对象确认时清空车型结果集
    $(".confirm[relcontainer='autoVersionModalResultContainer']").click(function(){
    	$(".subModelModalResultContainer .selectorResultContainer").html("");
    });
});
	/** 时间控件初始化*/
	function initDate(type){
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
	}

	/**获取时间模板*/
	function getDateTp(type){
		var htmlStr = "";	
		htmlStr += '<li style="list-style: none;margin-bottom:10px;" class="dateLIContainer">';
	    htmlStr += '	<div class="form-inline">';
	    htmlStr += '  		<span id="startDate-container" class="input-append date">';
		htmlStr += '	  		<input type="text" value="'+defaultBeginDate+'"  readonly="readonly" class="input-mini white" placeholder="开始日期" id="startDate"/>';
		htmlStr += '	  		<span class="add-on"><i class="icon-th"></i></span>';
		htmlStr += '		</span>';
		htmlStr += '		<span class="2">至</span>';
	    htmlStr += '  		<span id="endDate-container" class="input-append date">';
		htmlStr += '	  		<input type="text" value="'+endDate+'" readonly="readonly" class="input-mini white"  placeholder="结束日期" id="endDate"/>';
		htmlStr += '	  		<span class="add-on"><i class="icon-th"></i></span>';
		htmlStr += '		</span>';
		htmlStr += '	</div>';
	 	htmlStr += '</li>';
	 	return htmlStr;
	}
	
	/**获取车身形式和车型模板*/
	function getSubModelTp(){
		var htmlStr = "";
		htmlStr += '<li style="list-style: none;margin-bottom:10px;" class="subModelLIContainer">';
		htmlStr += '	<table style="width:570px;">';
		htmlStr += '		<tr>';
		htmlStr += '			<td valign="top" nowrap="nowrap" style="width:8%">';
		htmlStr += '				<div>';
		htmlStr += '					<a href="#" role="button" class="btn bodyTypeSelector" data-toggle="modal">车身形式</a>';
	 	htmlStr += '				</div>';
		htmlStr += '			</td>';
		htmlStr += '			<td valign="top" nowrap="nowrap" style="width:29%">';
		htmlStr += '				<div  style="margin-left:0px" class="bodyTypeModalResultContainer">';
		htmlStr += '				</div>';
		htmlStr += '			</td>';
		htmlStr += '			<td valign="top" style="width:20%">	';
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
	
	/**初始化时，加载对象模板*/
	function initPage(){
		$(".dateULContainer").each(function(){ 
			$(this).append(getDateTp());
		});
		$(".subModelULContainer").each(function(){ 
			$(this).append(getSubModelTp());
		});
		initDate();
	};
	
	/** 弹出车身形式对话框*/
	$(".bodyTypeSelector").live('click',function(){
		currBodyTypeLI = $(this).parents('table').find('.bodyTypeModalResultContainer');
		$('#bodyTypeModal').modal('show');
	});
	
	/**展示车身形式弹出框*/
	$('#bodyTypeModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//去掉默认选中的效果
		$('.bodyTypeModalByAll').each(function(){
			$(this).removeAttr("checked");//取消行全选
		});
		/**打开车身形式选择框时，清空车型选择*/
		$(".subModelModalResultContainer").html("");
		//加载子车型数据
		showLoading("bodyTypeModalBody");
		$('#bodyTypeModalBody').load(ctx+"/policy/saleIncentiveQuery/getBodyType",getParams(),function(){
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
	
	
	/**展示车型弹出框-开始*/
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
		$("#subModelModalBody").load(ctx+"/policy/saleIncentiveQuery/getSubmodelModal",getParams(),function(){
			//弹出框设置默认选中项结果集		
			var strHTML = '<ul class="inline" >';
			$(currSubModelLI).find('.subModelModalResultContainer input').each(function(){
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
						$(this).attr("checked",'true');
					}
				});
			});
			strHTML += '</ul>';
			$("#selectorResultContainerBySubModel").html(strHTML);
		});
	});
	
	// 车型确认按钮动作
	$(".subModelModalContainer").find('.confirm').unbind('click').bind('click',function(e){
		var containerId = $(this).parents(".subModelModalContainer").attr("id");
		var relInputName = $(this).attr("relInputName");
		var allSubModelArr = [];
		$(this).parents(".subModelModalContainer").find('.resultShowContent').find('.removeBtnByResult').each(function(){
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
		$(currSubModelLI).find('.subModelModalResultContainer').html(strHTML);
	});
	/**展示车型弹出框-结束*/
	
	/** 弹出常用对象对话框 开始**/
	$('#autoVersionModal').on('show', function (e) {	
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
		if(e.relatedTarget)  return; 
		//加载子常用对象下型号数据
		showLoading("autoVersionModalBody");
		$('#autoVersionModalBody').load(ctx+"/policy/saleIncentiveQuery/getAutoCustomGroup",getParams(),function(){
			//设置默认选中项
			$('#autoVersionModalResultContainer input').each(function(){
				var selectedId = $(this).val();
				$(".autoVersionModalContainer .autoVersionIdInput").each(function(){
					if($(this).val() == selectedId){
						$(this).attr("checked",'true');
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
					$(this).attr("checked",'true');//同个组别全选
				}else{
					$(this).removeAttr("checked");//取消选中
				}
			});
			//设置全选效果---结束
	});
		
	});
	/** 弹出常用对象对话框 结束**/
	
	/**点击确定查询*/
	$("#queryBtn").on("click", function (e) {
		if(!paramsValidate()) {
			return;
		}
		$("body").showLoading();
		//发送请求
		$.ajax({
	        type: "POST",
			url: ctx+"/policy/saleIncentiveQuery/getSaleIncentiveQueryData",
			data: getParams(),
			success: function(data) {
				//页面加载隐藏图表面板
				$(".tab-content").attr("style","display:block;");
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
			   $('body').hideLoading();
		   },
		   error:function() {
			   $('body').hideLoading();
		   }
		});
	});
	
	/**重置**/
	$("#resetBtn").on("click", function (e) {
		//清空车型
		$(".subModelModalResultContainer").html("");
		//清空常用对象
		$("#autoVersionModalResultContainer").html("");
		//清空车身形式
		$(".bodyTypeModalResultContainer").html(""); 
	});

	/**
	 * 字体变红色
	 * */
	function redStyle(str){	
		if(''==str || ""==str || null == str||undefined==str){
			 return "-";
		}
		var tempStr = str.replace(",","");
		if(tempStr<0){
			return "<font color='red'>"+str+"</font>";
		}else{
			return str;
		}
	}
	
	/**
	 * 千位位的处理
	 * */
   function thousandthStyle(number) {

	   if(''==number || ""==number || null == number||undefined==number){
		   return "";
	   }
	   if(0==number){
		   return "0";
	   }
        var num = number + "";  
        num = num.replace(new RegExp(",","g"),"");   
        // 正负号处理   
        var symble = "";   
        if(/^([-+]).*$/.test(num)) {   
            symble = num.replace(/^([-+]).*$/,"$1");   
            num = num.replace(/^([-+])(.*)$/,"$2");   
        }   
      
        if(/^[0-9]+(\.[0-9]+)?$/.test(num)) {   
            var num = num.replace(new RegExp("^[0]+","g"),"");   
            if(/^\./.test(num)) {   
            num = "0" + num;   
            }   
      
            var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1");   
            var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1");   
      
            var re=/(\d+)(\d{3})/;  
      
            while(re.test(integer)){   
                integer = integer.replace(re,"$1,$2");  
            }   
            return symble + integer + decimal;   
      
        } else {
            return number;   
        }   
    }
	
	/**将空值置为-**/
	function changeShow(str){
//		$("#hellotest").css("color","red");
		if(null == str || "" == str || undefined == str || ''==str){
			return "-";
//		}
//		else if(0==str){
//			return "0";
		}else{
//			str.style.color="red";
			return thousandthStyle(str);
		}
	}
	
	/**
	 * 取小数点后位数
	 * v 为保留位数
	 * */
	function decimal(num,v)  
	{  
		if(null == num || "" == num || undefined == num || ''==num){
			return "";
		}
	    var vv = Math.pow(10,v);  
//	    return Math.round(num*vv)/vv;
	    var x=Math.round(num*vv)/vv;
		var s_x = x.toString();
		var pos_decimal = s_x.indexOf('.');
		if (pos_decimal < 0)
		 {
		    pos_decimal = s_x.length;
		    s_x += '.';
		 }
		 while (s_x.length <= pos_decimal + 1)
		 {
		     s_x += '0';
		 }
		 
		 return thousandthStyle(s_x)+'%';
	     
	}
	
	
	/**
	 * 百分比保留一位
	 * */
	function changeOneDecimal_f(x)
	{

	   var s_x = x.toString();
	   var pos_decimal = s_x.indexOf('.');
	   if (pos_decimal < 0)
	   {
	      pos_decimal = s_x.length;
	      s_x += '.';
	   }
	   while (s_x.length <= pos_decimal + 1)
	   {
	      s_x += '0';
	   }
	   return s_x;
	}
	
	
	/** 展示数据表格*/
	function showData(data){
		$("#tableHead").removeClass("hide");
		var tbodyHtml = "";
		if(data)
		{
			for(var i = 0; i < data.length; i++)  
			{
				var dataObj = data[i];
				tbodyHtml += "<tr>";
				tbodyHtml += "<td class='tdSubModel'>" + dataObj.ym + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + dataObj.manfname + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + dataObj.segment +"</td>";
				tbodyHtml += "<td class='tdSubModel'>" + dataObj.subsegment + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + dataObj.bodytypeEn + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + dataObj.brand + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + dataObj.model + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + dataObj.versionname + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + dataObj.code + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + dataObj.launchdate + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.MSRP)) + "</td>";
				var margin=null;
				if(dataObj.margin==""){
					margin="-";
				}else{
					margin=dataObj.margin;
				}
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(margin)) + "</td>";
				var bonus=null;
				if(dataObj.bonus==""){
					bonus="-";
				}else{
					bonus=dataObj.bonus;
				}
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(bonus)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.totaltactical)) + "</td>";
				var grosssupports=null;
				if(dataObj.STD==""&&dataObj.AAK==""){
					grosssupports="-";
				}else{
					grosssupports=dataObj.grosssupports;
				}
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.fullyPaid)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(grosssupports)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.STD)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.AAK)) + "</td>";
				var customerincentive=null;
				if(dataObj.presents==""&&dataObj.insurance==""&&dataObj.maintenance==""&&dataObj.staffreward==""&&dataObj.financialloan==""&&dataObj.tradeinsupport==""){
					customerincentive="-";
				}else{
					customerincentive=dataObj.customerincentive;
				}
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(customerincentive)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.presents)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.insurance)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.maintenance)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.staffreward)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.financialloan)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.tradeinsupport)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.invoiceprice)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.grosscost)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.TP)) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + redStyle(thousandthStyle(dataObj.profit)) + "</td>";
				var profitrate=null;
				if(dataObj.profitrate==""){
					profitrate="-";
				}else{
					profitrate=dataObj.profitrate;
				}
				if(dataObj.profitrate < 0) {
					tbodyHtml += "<td class='tdSubModel'> <font color='red'>" + decimal(dataObj.profitrate*100,1) + "</font></td>";
				} else if(profitrate=="-"){
					tbodyHtml += "<td class='tdSubModel'>" + profitrate + "</td>";
				}else{
					tbodyHtml += "<td class='tdSubModel'>" + decimal(dataObj.profitrate*100,1) + "</td>";	
				}
				tbodyHtml += "<td class='tdSubModel'>" + changeShow(dataObj.versionlastmonthsales) + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + changeShow(dataObj.versionmonthsales) + "</td>";
				tbodyHtml += "</tr>";
			}
			$("#gridTbody").html(tbodyHtml);
	    }
	}

/**
 * 参数验证
 */
window.paramsValidate = function()
{
	var paramObj = getParams();
	if((paramObj.aVersionId == "" || paramObj.aVersionId == null) && (paramObj.subModelId == "" || paramObj.subModelId == null))
	{
		alert("请选择车型或常用对象");
		return false;
	}
	getDateGroup(paramObj);
	var dateGroup = (paramObj.dateGroup).substr(0,paramObj.dateGroup.length - 1).split("|");
	var date = dateGroup[0].split(",");
	var beginDate = parseInt(date[0].replace("-",""));
	var endDate = parseInt(date[1].replace("-",""));
	if(beginDate > endDate)
	{
		alert("开始时间不能大于结束时间");
		return false;
	}
	return true;
};

/**
 * 获取页面参数
 */
window.getParams = function()
{
	var beginDate = $("#dateYear .dateLIContainer").eq(0).find("#startDate-container input").val();
	var endDate = $("#dateYear .dateLIContainer").eq(0).find("#endDate-container input").val();
	//数据分析类型默认为销量
	var analysisContentType = $('#moduleCode').val();
	var paramObj = {};
	var timeRange ={}; 
	var subModelId = $(".subModelModalResultContainer input").map(function(){return $(this).val();}).get().join(",");
	var aVersionId = $("#autoVersionModalResultContainer input").map(function(){return $(this).val();}).get().join(",");
	var hatchbackId = $("#model .subModelLIContainer").eq($("#getModelIndexId").val()).find("td:eq(1)").find("li div :input").map(function(){return $(this).val();}).get().join(",");
	var bodyTypeName = $("#model .subModelLIContainer").eq($("#getModelIndexId").val()).find("td:eq(1)").find("li div").map(function(){return $(this).text();}).get().join(",");
	paramObj.subModelId = subModelId;
	paramObj.aVersionId = aVersionId;
	paramObj.hatchbackId = hatchbackId;
    paramObj.bodyTypeName = bodyTypeName;
	paramObj.analysisContentType = analysisContentType;
	getDateGroup(paramObj);
	getQueryCondition(paramObj);
	timeRange = getTimeRange(paramObj.dateGroup);
	paramObj.beginDate = timeRange.beginDate;
	paramObj.endDate = timeRange.endDate;
	paramObj.browser = navigator.appVersion;
	return paramObj;
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
		dateGroup += $(n).find("#startDate-container :input").val() + "," +  $(n).find("#endDate-container :input").val() + "|";
	});
	if(dataLi.length > 1) paramObj.multiType = "1";//证明时间多选
	paramObj.dateGroup = dateGroup;
};

/**
 * 导出Excel
 * @param languageType EN:英文版;ZH:中文版
 */
function exportExcel(languageType)
{
	var content = $.trim($("#gridTbody").html());
	if(!content || "无数据!" == content)
	{
	    alert("暂无数据导出");
		return true;
	}
	$("#languageType").val(languageType);
	$("#exportFormId").submit();
}

/**
 * 弹出框
 * @param type
 */
window.showSubModel = function(type)
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
	var inputType = "1"; //弹出框默认多选
	if('1' == type) getModelPage("tabs-competingProducts",type,inputType);
	else if('2' == type) getModelPage("tabs-segment",type,inputType);
	else if('3' == type) getModelPage("tabs-brand",type,inputType);
	else getModelPage("tabs-manf",type,inputType);
};

function spriceOnBulur()
{
		var sprice = parseInt($('#sprice').val());
		var eprice = parseInt($('#eprice').val());
		if(!isNaN(eprice) && eprice !=''){
			sprice>eprice?$('#sprice').attr('value',''):$('#sprice').attr('value',sprice);
		}
}

function epriceOnBulur()
{
		var sprice = parseInt($('#sprice').val());
		var eprice = parseInt($('#eprice').val());
		if(!isNaN(eprice) && eprice !=''){
			sprice>eprice?$('#eprice').attr('value',''):$('#eprice').attr('value',eprice);
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
function getModelPage(id,type,inputType,timeType)
{
	//如果内容不为空则触发请求
	if(!$.trim($('#' + id).html())){
		//获取时间
		var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var hatchbackId = "";
		var paramObj = {};
		var dateGroup = null;
		var priceType = null;
		getDateGroup(paramObj)
		
		//数据分析类型如果没有默认为成交价
		var analysisContentType = $('#moduleCode').val();
		//如果数据分析类型是销量校验
		if("salespriceanaly" == analysisContentType)
		{	
			dateGroup = paramObj.dateGroup;
			var timeRange = getTimeRange(dateGroup);
			beginDate = timeRange.beginDate;
			endDate   = timeRange.endDate;
			priceType = $("#priceType").val();
			hatchbackId = $("#model .subModelLIContainer").eq($("#getModelIndexId").val()).find("td:eq(1)").find("li div :input").map(function(){return $(this).val();}).get().join(",");
		}
		//传递参数
		var params = {inputType:inputType,subModelShowType:type,beginDate:beginDate,endDate:endDate,analysisContentType:analysisContentType,timeType:timeType,hatchbackId:hatchbackId,dateGroup:dateGroup,priceType:priceType};
		//触发请求
		showLoading(id);
		$("#" + id).load(ctx+"/policy/saleIncentiveQuery/getSubmodelModal",params,function(){
			$("#selectorResultContainerBySubModel .removeBtnByResult").each(function(){
				var subModelId = $(this).attr("subModelId");
				$(".subModelModalContainer .subModelIdInput").each(function(){
					if($(this).val() == subModelId) $(this).attr("checked",'true');//行全选
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

function getTimeRange(dateGroup) 
{
	if(dateGroup.substr(dateGroup.length-1,dateGroup.length)=="|") {
        dateGroup=dateGroup.substr(0,dateGroup.length-1);
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
			if(sDate<minSDate){
				minSDateIndex = i;
			}
			if(eDate>maxEDate){
				maxEDateIndex = i;
			}
	}
	timeRange.beginDate = dates[minSDateIndex].split(",")[0].replace("-", "");
	timeRange.endDate = dates[maxEDateIndex].split(",")[1].replace("-", "");
	return timeRange;
}

/**
 * 获取页头查询条件
 */
window.getQueryCondition = function(paramObj)
{
	var queryCondition = "";
	queryCondition += "分析维度 = 车型";
	queryCondition += "\n价格类型 = 指导价";
	queryCondition += "\n时间= " + paramObj.dateGroup;
	queryCondition += "\n车身形式= " + paramObj.bodyTypeName;
	paramObj.queryCondition = queryCondition;
};

/** 车型控件值鼠标经过事件*/
$(".removeBtn").live('mouseover',function(){
	$(this).find(".icon-remove").css({visibility:'visible'});
});

/**车型控件值鼠标离开事件*/
$(".removeBtn").live('mouseout',function(){
	$(this).find(".icon-remove").css({visibility:'hidden'});
});

/**删除车身形式时，清空车型选择*/
$(".bodyTypeModalResultContainer").find(".icon-remove").live("click",function(){
	$(".subModelModalResultContainer").html("");
});