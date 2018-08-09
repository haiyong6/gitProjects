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
var segmentPath = "/terminal/global/getSegmentAndChildren";

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
	
	
	/**展示车型弹出框-开始*/
	$('#subModelModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		
		//加载车型数据
		showLoading("subModelModalBody");
		$('#subModelModalBody').load(ctx+"/terminal/global/getSubmodelModal",getParams(),function(){
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
		$('#versionModalBody').load(ctx+"/terminal/global/getVersionModalByCommon",getParams(),function(){
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
	
	
	/**展示常用对象弹出框-开始*/
	$('#autoVersionModal').on('show', function (e) {	
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
		if(e.relatedTarget)  return; 
		//加载子常用对象下型号数据
		showLoading("autoVersionModalBody");
		$('#autoVersionModalBody').load(ctx+"/terminal/global/getAutoCustomGroup",getParams(),function(){
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
		$('#segmentModalBody').load(ctx+"/terminal/global/getSegmentAndChildren",getParams(),function(){
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
		$('#origModalBody').load(ctx+"/terminal/global/getOrig",getParams(),function(){
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
		$('#brandModalBody').load(ctx+"/terminal/global/getBrand",getParams(),function(){
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
		$('#manfModalBody').load(ctx+"/terminal/global/getManfModal",getParams(),function(){
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
		$('#bodyTypeModalBody').load(ctx+"/terminal/global/getBodyType",getParams(),function(){
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
		//清空已选择对象,默认选择下拉框对象:级别
		$("#objectContainer").children().hide();
		$("#objectContainer").children().find(".selectorResultContainer").remove();
		$("#objectContainer").children().eq(0).show();
		$("#objectContainer").children().eq(1).show();
		$("#objectContainer").children().eq(2).show();
	});
	/**重置按钮事件-结束*/
	
	/**查询按钮事件-开始*/
	$('#queryBtn').on('click', function (e) {
		var params = getParams();
		params.submitType = "btnOk";
		$("#isPromotion").attr("checked",false);
		
		if(paramsValidate(params)) return;
		$('body').showLoading();
		//默认展示图表
		$("#showChart").click();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/terminal/loadChartAndTable",
			   data: params,
			   success: function(data){
			   	if(data){
				     //查询面板折叠
				     $(".queryConditionContainer .buttons .toggle a").click();
					 $("#chartId").height(500);
					 if(!data.chart.showType){
					 	$("#chartId").html("");
						$("#chartTable").html("");
						$("#showTable").click();
						$("#priceTypeDiv").css("display","none");
					 	$(".tab-content").css("overflow","auto");
					 }else{
					 	 $(".tab-content").css("overflow","hidden");
						 $("#priceTypeDiv").css("display","block");
						 $("#priceType").addClass("hide");
						 $("#priceType option").eq(0).attr("selected","selected");
						 
					 	 showChart(data.chart);
				 	 	 CreateChartTable(data.chart,params);
					 }
					 showObjectGrid(data.grid,params);
				   }
				   $('body').hideLoading();
			   },
			   error:function(){
				   $('body').hideLoading();
			   }
			});
	});
	/**查询按钮事件-结束*/
	
	/**促销细分点击 - 开始*/
	$('#isPromotion').on('click', function () {
		if(this.checked){
			$("#priceType").removeClass("hide");
			changPromotion();
		}else{
			$("#priceType").addClass("hide");
			changPromotion();
		}
	});
	/**促销细分点击 - 结束*/
	
	/**金额,份额切换 - 开始*/
	$('#priceType').on('change', function () {
		changPromotion();
	});
	/**金额,份额切换 - 结束*/
	
	/**图表 -开始*/
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
					return window.formatData(v3);
			}}}};
			
			obj.barGap = "0%";
			obj.barMaxWidth = 35;
		}
		var myChart = echarts.init(document.getElementById('chartId'),'default');
		var option = {
			tooltip : {
				trigger: 'axis',
				showDelay:0,
				hideDelay:0,
				axisPointer:{
					type: 'line',
			        lineStyle: {
			            color: '#2EC7C9',
			            width: 2,
			            type: 'solid'
			        }
		        },
			transitionDuration:0
			},
			legend: {
				data:json.titles
			},
			xAxis : [
				{
					splitLine:{show:false},
					type : 'category',
					show: false,
					data : json.xTitle
				}
			],
			yAxis : [
				{
					splitLine:{show:false},
					type : 'value',
					name : '百万',
					nameTextStyle :{fontSize:15}
				}
			],
			series : series
		};
	myChart.clear();
	//console.log(JSON.stringify(option))
	myChart.setOption(option);
	}
	
	function CreateChartTable(json,params){
		var d1;
		var d2;
		var budget;
		var objectType =params.objectType;
		var title = json.xTitle;
		var series = json.series;
		var showType = json.showType;
		var width = $("#chartId").css("width").replace('px','');
		width = width - 160;
		var tableHtml = "";
		tableHtml += "<Table><tr><td style ='border: 1px solid;text-align: center;width:80px;'>";
		if(showType == "0"){
			if(objectType == 0){
				tableHtml += "型号</td>";
			}else if(objectType == 1){
				tableHtml += "车型</td>";
			}else if(objectType == 2){
				tableHtml += "厂商品牌</td>";
			}else if(objectType == 3){
				tableHtml += "品牌</td>";
			}else if(objectType == 4){
				tableHtml += "系别</td>";
			}else{
				tableHtml += "级别</td>";
			}
		}else{
			tableHtml += "时间</td>";
		}
		for (var i=0; i<title.length ; i++) {
			tableHtml += "<td style ='border: 1px solid;text-align: center;width:"+(width / title.length)+"px;'>"+title[i]+"</td>";
		};
		tableHtml +="<td style ='width:80px;'></td></tr>";
		
		if(showType == '0'){
			tableHtml += "<tr><td style ='border: 1px solid;text-align: center;width:80px;'>预算增速</td>";
			for (var i=0; i<title.length ; i++) {
				tableHtml += "<td style ='border: 1px solid;text-align: center;width:"+(width / title.length)+"px;'>"
				if (series.length >= 2) {
						d1 = series[series.length - 1].data[i];
						d2 = series[series.length - 2].data[i];
						if(d1 != "-" && d2 != '-'){
							budget = (d1 - d2) / d2;
						}else{
							budget = '-';
						}
				}else{
					budget = "-";
				}
				if(Math.round(budget * 10000) / 100 > 1){
					tableHtml += Math.round(budget * 10000) / 100 +"%</td>";	
				}else if(budget == "-"){
					tableHtml += budget + "</td>";
				}else{
					tableHtml += "<font color='red'>"+ Math.round(budget * 10000) / 100 +"%</font></td>";
				}
				
			};
		}
		tableHtml +="<td style ='width:80px;'></td></tr>";
		tableHtml +="</Table>";
		
		$("#chartTable").html(tableHtml);
		$("#chartTable").css("display","block");
		$("#chartTable").css("margin-top","-50px");
	}
	
	function CreateChartTableByPromotion(json,params){
		var title = json.xTitle;
		var subxTitle = json.subxTitle; 
		var showType = json.showType;
		var width = $("#chartId").css("width").replace('px','');
		width = width - 160;
		var tableHtml = "";
		tableHtml += "<Table><tr><td style ='border: 0px;text-align: center;width:80px;'></td>";
		for (var i=0; i<subxTitle.length ; i++) {
			tableHtml += "<td style ='border: 1px solid;text-align: center;width:"+(width / subxTitle.length)+"px;'>"+subxTitle[i]+"</td>";
		};
		tableHtml +="<td style ='width:80px;'></td></tr>";
		tableHtml +="<td style ='width:80px;'></td></tr>";
		tableHtml +="</Table>";
		
		$("#chartTable").html(tableHtml);
		$("#chartTable").css("display","block");
		if(showType == 1){
			$("#chartTable").css("margin-top","-4px");
		}else{
			$("#chartTable").css("margin-top","-50px");
		}
	}
	
	
	function showObjectGrid(grid,params){
		var trShow ="";
		var tbodyHtml = "";
		var objectType =params.objectType;
		if(objectType == 0){
				$("#gridTheadByVersion").removeClass("hide");
				$("#gridTheadByModel").addClass("hide");
				$("#gridTheadByManf").addClass("hide");
				$("#gridTheadByBrand").addClass("hide");
				$("#gridTheadByOrig").addClass("hide");
				$("#gridTheadBySegment").addClass("hide");
			}else if(objectType == 1){
				$("#gridTheadByVersion").addClass("hide");
				$("#gridTheadByModel").removeClass("hide");
				$("#gridTheadByManf").addClass("hide");
				$("#gridTheadByBrand").addClass("hide");
				$("#gridTheadByOrig").addClass("hide");
				$("#gridTheadBySegment").addClass("hide");
			}else if(objectType == 2){
				$("#gridTheadByVersion").addClass("hide");
				$("#gridTheadByModel").addClass("hide");
				$("#gridTheadByManf").removeClass("hide");
				$("#gridTheadByBrand").addClass("hide");
				$("#gridTheadByOrig").addClass("hide");
				$("#gridTheadBySegment").addClass("hide");
			}else if(objectType == 3){
				$("#gridTheadByVersion").addClass("hide");
				$("#gridTheadByModel").addClass("hide");
				$("#gridTheadByManf").addClass("hide");
				$("#gridTheadByBrand").removeClass("hide");
				$("#gridTheadByOrig").addClass("hide");
				$("#gridTheadBySegment").addClass("hide");
			}else if(objectType == 4){
				$("#gridTheadByVersion").addClass("hide");
				$("#gridTheadByModel").addClass("hide");
				$("#gridTheadByManf").addClass("hide");
				$("#gridTheadByBrand").addClass("hide");
				$("#gridTheadByOrig").removeClass("hide");
				$("#gridTheadBySegment").addClass("hide");
			}else{
				$("#gridTheadByVersion").addClass("hide");
				$("#gridTheadByModel").addClass("hide");
				$("#gridTheadByManf").addClass("hide");
				$("#gridTheadByBrand").addClass("hide");
				$("#gridTheadByOrig").addClass("hide");
				$("#gridTheadBySegment").removeClass("hide");
			}
			
			for (var i=0; i< grid.length; i++) {
				var temp = grid[i].objectId+grid[i].dateTime;
				
				if(trShow.indexOf(temp) == -1 && i != 0){
					tbodyHtml += "</tr>";
				}
				
				if(trShow.indexOf(temp) == -1){
					trShow += ","+temp;
					tbodyHtml += "<tr>";
					tbodyHtml += "<td style='width:10%;text-align: center;'>" + grid[i].dateTime + "</td>";
					tbodyHtml += "<td style='width:18%;text-align: center;'>" + grid[i].objectName + "</td>";
				}
				var subsidyHtml ="<td style='width:9%;text-align: center;'>" + grid[i].subsidy + "</td>";
				if(grid[i].subsidyType == CXZE){
					tbodyHtml += subsidyHtml;
				}
				if(grid[i].subsidyType == TCZC){
					tbodyHtml += subsidyHtml;
				}
				if(grid[i].subsidyType == LSZC){
					tbodyHtml += subsidyHtml;
				}
				if(grid[i].subsidyType == RYJL){
					tbodyHtml += subsidyHtml;
				}
				if(grid[i].subsidyType == JRDK){
					tbodyHtml += subsidyHtml;
				}
				if(grid[i].subsidyType == ZHZC){
					tbodyHtml += subsidyHtml;
				}
				if(grid[i].subsidyType == ZSBX){
					tbodyHtml += subsidyHtml;
				}
				if(grid[i].subsidyType == ZSLP){
					tbodyHtml += subsidyHtml;
				}
			};
			//alert(tbodyHtml)
			$("#gridTbody").html(tbodyHtml);
	}
	/**图表 -结束*/
	
	/**根据促销细分画图-开始*/
	function changPromotion() {
		var priceType = "";
		var isPromotion = false;
		var params = getParams();
		
		if(paramsValidate(params)) return;
		if("checked" == $("#isPromotion").attr("checked")){isPromotion = true;};
		if(isPromotion){priceType = $("#priceType").val();};
		params.priceType = priceType;
		params.isPromotion = isPromotion;
		if(isPromotion){
			params.submitType = "changPromotion";
		}else{
			params.submitType = "btnOk";
		}
		
		//遮盖层
		$('body').showLoading();
		//默认展示图表
		$("#showChart").click();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/terminal/loadChartAndTable",
			   data: params,
			   success: function(data){
			   	if(data){
					 $("#chartId").height(480);
					 if(!data.chart.showType){
					 	$("#chartId").html("");
						$("#chartTable").html("");
						$("#showTable").click();
						$("#priceTypeDiv").css("display","none");
					 	$(".tab-content").css("overflow","auto");
					 }else{
					 	 $(".tab-content").css("overflow","hidden");
						 $("#priceTypeDiv").css("display","block");
						 if(isPromotion){
						 	$("#chartTable").css("display","block");
						 	showChartByType(data.chart);
							CreateChartTableByPromotion(data.chart,params);
						 }else{
						 	showChart(data.chart);
					 	 	CreateChartTable(data.chart,params);
							$("#chartTable").css("display","block");
						 }
					 }
					 if(!isPromotion){
						 showObjectGrid(data.grid,params);
					 }
				   }
				   $('body').hideLoading();
			   },
			   error:function(){
				   $('body').hideLoading();
			   }
			});
	};
	
	function showChartByType(json){
		var priceType = $("#priceType").val();
		var yformatter = '{value}';
		var yname = '百万';
		if(priceType == 2){
			yformatter = '{value}.00%';
			yname = '';
		}
		//线图颜色数组
		var colos = ['#00235A','#AED4F8','#8994A0','#62C5E2','#E26700','#F9A700','#4A6F8A','#BBC600','#920A6A','#E63110','#005D5B','#FACE00','#009C0E','#77001D'];
		if(!json) return;
		var series = json.series;
		var subxTitle = json.subxTitle;
		//循环添加数据标签展示内容
		for(var i = 0; i < series.length; i++)
		{
			var obj = series[i];
			obj.itemStyle = {normal:{color:colos[i],label:{show:true,formatter:function(v1,v2,v3){
					return window.formatData(v3);
			}}}};
			obj.stack = "促销";
			obj.barMaxWidth = '35';
		}
		var myChart = echarts.init(document.getElementById('chartId'),'default');
		var option = {
				tooltip : {
					trigger: 'axis',
					showDelay:0,
						hideDelay:0,
						axisPointer:{
							type: 'line',
							lineStyle: {
								color: '#2EC7C9',
								width: 2,
								type: 'solid'
							}
						},
						formatter: function(params) {
			            var res = '<div>';
			            res += '<strong>' + params[0].name + '</strong>'
			            for (var i = 0, l = params.length; i < l; i++) {
			                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value;
							if(priceType == '2' && params[i].value != '-'){
								res +='%';
							}
			            }
			            res += '</div>';
			            return res;
			        },
					transitionDuration:0
				},
				legend: {
					data:json.titles
				},
				xAxis : [
					{
						splitLine:{show:false},
						type : 'category',
						axisLabel:{interval:0,rotate:25},						
						data : json.xTitle
					}
				],
				yAxis : [
					{
						splitLine:{show:false},
						type : 'value',	
						name : yname,
						nameTextStyle :{fontSize:15},				
						axisLabel:{formatter: yformatter}
					}
				],
				grid:{
					y2:100
				},
				series : json.series
			};
		//console.log(JSON.stringify(option))
		myChart.clear();
		customMyChart(option,subxTitle);
		myChart.setOption(option);
	}
	
	//特殊处理图形,不同对象中间隔开
	function customMyChart(option,subxTitle){
		var series = option.series;
		var xData = option.xAxis[0].data;
		var len = xData.length/subxTitle.length;
		for (var i = 0; i < subxTitle.length; i++) {
			if(i != subxTitle.length - 1){
				xData.splice((i + 1) * len + i ,0,"");
			};
		};
		
		for (var j=0; j<series.length; j++) {
			var obj = series[j];
			for (var i = 0; i < subxTitle.length; i++) {
				if(i != subxTitle.length - 1){
					obj.data.splice((i + 1) * len + i ,0,"");
				};
			};
		};
	}
	/**根据促销细分画图-结束*/
	
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
			   url: ctx+"/terminal/checkPopBoxData",
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
		paramObj.inputType = 1;
		paramObj.frequencyType = frequencyType;
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
		if("1" == $("#objectType").val()) queryCondition += "级别";
		if("2" == $("#objectType").val()) queryCondition += "系别";
		if("3" == $("#objectType").val()) queryCondition += "品牌";
		if("4" == $("#objectType").val()) queryCondition += "厂商品牌";
		if("5" == $("#objectType").val()) queryCondition += "车型";
		
		queryCondition += "\n时间 = " + $("#startDate").val() + "至" + $("#endDate").val();
		
		queryCondition += "\n级别 = " + $("#segmentModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n系别 = " + $("#origModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n品牌 = " + $("#brandModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n厂商品牌 = " + $("#manfModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n车型 = " + $("#subModelModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		
		paramObj.queryCondition = queryCondition;
	}
	
});

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
		var hatchbackId = "";
		
		//传递参数
		var params = {
						inputType:inputType,
						subModelShowType:type,
						beginDate:beginDate,
						endDate:endDate,
					};
		//触发请求
		showLoading(id);
		$('#' + id).load(ctx+"/terminal/global/getSubmodelModal",params,function(){
			
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
	
	if(parseInt(beginDate.replace("-","")) > parseInt(endDate.replace("-","")) ){
			alert("开始时间不能大于结束时间");
			flag = true;
	}
	return flag;
};

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
	var beginDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var objectType = $("#objectType").val();
	var frequencyType = $("#frequencyType").val();
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
	$("#exportFormId").submit();
};