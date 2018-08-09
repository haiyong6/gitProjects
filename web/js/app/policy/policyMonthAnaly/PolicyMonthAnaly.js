
//查询后的数据
var selectData = null;
//页面显示的条数
var eachNumber = 6;
//开始条数
var startNumber = 0;
//结束条数
var endNumber = 0;
//当前页数
var pageNumber = 1;
//最大页数
var maxPageNumber = null;

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
    
    $('#startDate-container.input-append.date').on('changeDate',function(){
    	var beginDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		  checkPopBoxData();
		
    });
    
    /**
	 * 校验弹出框有效数据
	 */
	function checkPopBoxData()
	{	
		var paramsObj = getParams();
		if( !paramsObj.modelIds) return;
			
		$('.queryConditionContainer').showLoading();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/policy/checkPopBoxData",
			   data: paramsObj,
			   dataType:'json',
			   success: function(data){
				   if(data)
				   {
					  var modelObj = $("#subModelModalResultContainer ul li");
					 var flag=false;
					  for(var i = 0; i < data.length; i++)
					  {
						  var mid = data[i].subModelId;
						  //遍历车型
							  $.each(modelObj,function(i,n){
								  if(mid == $(n).find("input").val()){
									  $(n).remove();
								  } 
							  });
							  
					  }
				   }
				   $('.queryConditionContainer').hideLoading();
			   },
			   error:function(){
				   $('.queryConditionContainer').hideLoading();
			   }
			});
	}
	
	
	$(".subModelModalContainer .subModelIdInput").live('click',function(){
		//如果是单选的情况下，就只在本页取对象
		if($(this).attr('type') == 'radio'){
			addResultContainerBySubModelPolicy();
		}
	});
	
	function addResultContainerBySubModelPolicy(){
		var objName = "";
		var type = $("#choseType").val();
		if(type == 1){
			objName = "#tabs-competingProducts";
		} else if(type == 2){
			objName = "#tabs-segment"
		} else if(type == 3){
			objName = "#tabs-brand";
		} else if(type == 4){
			objName = "#tabs-manf";
		}
		//显示选中的值	——开始
		//去掉重复选项
		$("#selectorResultContainerBySubModel").html("");
		var allSubModelArr = [];
		$(".subModelModalContainer "+objName).find('.subModelIdInput:checked').each(function(){
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
	}
    
	function uniqueSubModel(array){  
	    for(var i=0;i<array.length;i++) {  
	        for(var j=i+1;j<array.length;j++) {  
	            //注意 ===  
	            if(array[i].subModelId===array[j].subModelId) {  
	                array.splice(j,1);  
	                j--;  
	            }  
	        }  
	    }  
	  		return array;  
	} 
	
    /**
     * 车型弹出框
     */
	$('#subModelModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//当是时间对比的时候隐藏车型全选框
		if($("#analysisDimensionType").val() == 2){
			$("#modelAll").css("display","none");
		} else{
			$("#modelAll").css("display","inline");
		}
		
		
		//加载子车型数据
		showLoading("subModelModalBody");
		$('#subModelModalBody').load(ctx+"/policy/global/getSubmodelModal",getParams(),function(){
			//弹出框设置默认选中项结果集		
			var strHTML = '<ul class="inline" >';
			
			$("#subModelModalResultContainer").find('input').each(function(){
					
					if($(this).val() != -1){
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
					}
				
				});
			//如果本竞品页有没有选中的车型全选框取消选中
			$(".subModelModalContainer .subModelIdInput").each(function(){
				if(!$(this).prop("checked")){
					$("#modelAll").prop("checked",false);
					return false;
				}
			});
			strHTML += '</ul>';
			$("#selectorResultContainerBySubModel").html(strHTML);
			//给变量赋值，默认本竞品
			$("#choseType").val("1");
			//判断车型全选框是不是要勾选
			var num = 0;//用来统计本竞品页显示车型的数量
			$("#tabs-competingProducts").find("input").each(function(){
				var ths = $(this);
				
				if(ths.parent().css("display") != "none"){
					num++;
				}
			});
			if(num == ($("#tabs-competingProducts input:checked").length) && (num != 0)){
				$("#modelAll").attr("checked","checked");
			} else{
				$("#modelAll").prop("checked",false);
			}
			
			
			
		});
	});
	
	//车型全选
	$("#modelAll").on("click",function(){
		if($(this).attr("checked") == "checked"){
			//本竞品
			if($("#choseType").val() == 1){
				$("#tabs-competingProducts").find("input").each(function(){
					var ths = $(this);
					var check = ($(this).attr("checked") == "checked");
					/*if(ths.parent().attr("style") == undefined){
						ths.parent().attr("style","");
					}*/
					
//					//判断是不是隐藏
					if(ths.parent().css("display") != "none"){
						//设置联动
						var currVal = $(this).val();
						$(this).parents('.subModelModalContainer').find('.subModelIdInput').filter(function(){
							return $(this).val() == currVal;
						}).each(function(){
							$(this).attr("checked",'true');
						});
					}
				})
				$("#subModelModal .resultShowContainer .resultShowContent").html("");//先清空再重新放上去
				addResultContainerBySubModel();//把选中的放到车型选择下面的容器中
			}
			//细分市场
			else if($("#choseType").val() == 2){
				$("#tabs-segment .selectorContentTd").find("input").each(function(){
					var ths = $(this);
					var check = ($(this).attr("checked") == "checked");
					//判断是不是隐藏
					if($(ths).parent().css("display") != "none"){
						//设置联动(因ie8性能低下，经需求人员确定，全选时非本竞品页不设置联动)
						var currVal = $(ths).val();
						$(ths).parents('#tabs-segment .selectorContentTd').find('input').each(function(){
							if($(this).val() == currVal){
								$(this).attr("checked",'true');
								return false;
							}
						});
					} 
				})
				$("#subModelModal .resultShowContainer .resultShowContent").html("");//先清空再重新放上去
				addResultContainerBySubModelBySegment();//把选中的放到车型选择下面的容器中
				//把父级分类也勾选一下
				$("#tabs-segment .selectorTypeTd").find("input").each(function(){
					if(!$(this).attr("checked")){
						$(this).attr("checked","true");
					}
				});
			}
			//品牌
			else if($("#choseType").val() == 3){
				$("#tabs-brand .selectorContentTd").find("input").each(function(){
					var ths = $(this);
					var check = ($(this).attr("checked") == "checked");
					
					//判断是不是隐藏
					if(ths.parent().css("display") != "none"){
						//设置联动(因ie8性能低下，经需求人员确定，全选时非本竞品页不设置联动)
						var currVal = ths.val();
						$(ths).parents('#tabs-brand .selectorContentTd').find('input').each(function(){
							if($(this).val() == currVal){
								$(this).attr("checked",'true');
								return false;
							}
						});
					}
				})
				$("#subModelModal .resultShowContainer .resultShowContent").html("");//先清空再重新放上去
				addResultContainerBySubModelByBrand();//把选中的放到车型选择下面的容器中
				//把父级分类也勾选一下
				$("#tabs-brand .selectorTypeTd").find("input").each(function(){
					if(!$(this).attr("checked")){
						$(this).attr("checked","true");
					}
				});
			}
			//厂商品牌
			else if($("#choseType").val() == 4){
				$("#tabs-manf .selectorContentTd").find("input").each(function(){
					var ths = $(this);
					var check = ($(this).attr("checked") == "checked");
					/*if(ths.parent().attr("style") == undefined){
						ths.parent().attr("style","");
					}*/
					
					//判断是不是隐藏
					if(ths.parent().css("display") != "none"){
						//设置联动(因ie8性能低下，经需求人员确定，全选时非本竞品页不设置联动)
						var currVal = ths.val();
						/*$(this).parents('.subModelModalContainer').find('.subModelIdInput').filter(function(){
							return $(this).val() == currVal;
						}).each(function(){
							$(this).attr("checked",'true');
						});*/
						$(ths).parents('#tabs-manf .selectorContentTd').find('input').each(function(){
							if($(this).val() == currVal){
								$(this).attr("checked",'true');
								return false;
							}
						});
					}
					
					
					
				})
				$("#subModelModal .resultShowContainer .resultShowContent").html("");//先清空再重新放上去
				addResultContainerBySubModelByManf();//把选中的放到车型选择下面的容器中
				//把父级分类也勾选一下
				$("#tabs-manf .selectorTypeTd").find("input").each(function(){
					if(!$(this).attr("checked")){
						$(this).attr("checked","true");
					}
				});
			}
		} else{
			//本竞品
			if($("#choseType").val() == 1){
				$("#tabs-competingProducts").find("input").each(function(){
					var ths = $(this);
					var check = ($(this).attr("checked") == "checked");
					//设置联动取消选中
					var currVal = $(this).val();
					$(this).parents('.subModelModalContainer ').find('.subModelIdInput').filter(function(){
						return $(this).val() == currVal;
					}).each(function(){
						$(this).prop("checked",false);//取消选中
					});
					
				})
				$("#subModelModal .resultShowContainer .resultShowContent").html("");//先清空再重新放上去
				addResultContainerBySubModel();//把选中的放到车型选择下面的容器中

			} 
			//细分市场
			else {
				$("#subModelModal .resultShowContainer .resultShowContent").html("");//先清空
				$(".subModelModalContainer .tab-content").find("input").each(function(){
					$(this).prop("checked",false);
				})
			}
		}
	})
	
		//点击下面的车型取消选中，车型总全选框也会取消选中
	$(".subModelModalContainer .subModelIdInput").live('click',function(){
		var ths = $(this);
		var check = ($(this).attr("checked") == "checked");
		if(!check){
			if($("#modelAll").attr("checked") == "checked"){
				$("#modelAll").prop("checked",false);
			}
			
		} else{
			//本竞品
			if($("#choseType").val() == 1){
				var flag = false;
				var num = 0;//用来统计显示车型的数量
				//把所有选择框都判断一遍，都选中则车型总全选框选中
				$("#tabs-competingProducts").find("input").each(function(){
					var ths = $(this);
					/*if(ths.parent().attr("style") == undefined || null==ths.parent().attr("style")||""==ths.parent().attr("style")){
						ths.parent().attr("style","")
					}*/
					if(ths.parent().css("display") != "none"){
						num++;
					}
				});
				if((num == $("#tabs-competingProducts input:checked").length) && (num != 0)){
					$("#modelAll").attr("checked","checked");
				} else{
					$("#modelAll").prop("checked",false);
				}
			}
			//细分市场
			else if($("#choseType").val() == 2){
				var flag = false;
				var num = 0;//用来统计显示车型的数量
				//把所有选择框都判断一遍，都选中则车型总全选框选中
				$("#tabs-segment .selectorContentTd").find("input").each(function(){
					var ts = $(this);
					/*if(ts.parent().attr("style") == undefined){
						ts.parent().attr("style","")
					}*/
					//判断是不是隐藏,只对不隐藏的起效果
					if(ts.parent().css("display") != "none"){
						num++;
					}
				});
				if(num == $("#tabs-segment .selectorContentTd input:checked").length && (num != 0)){
					$("#modelAll").attr("checked","checked");
				} else{
					$("#modelAll").prop("checked",false);
				}
						
			}
			//品牌
			else if($("#choseType").val() == 3){
				var flag = false;
				var num = 0;//用来统计显示车型的数量
				//把所有选择框都判断一遍，都选中则车型总全选框选中
				$("#tabs-brand .selectorContentTd").find("input").each(function(){
					var ts = $(this);
					/*if(ts.parent().attr("style") == undefined){
						ts.parent().attr("style","")
					}*/
					//判断是不是隐藏,只对不隐藏的起效果
					if(ts.parent().css("display") != "none"){
						num++;
					}
				});
				if(num == $("#tabs-brand .selectorContentTd input:checked").length && (num != 0)){
					$("#modelAll").attr("checked","checked");
				} else{
					$("#modelAll").prop("checked",false);
				}
						
			}
			//厂商品牌
			else if($("#choseType").val() == 4){
				var flag = false;
				var num = 0;//用来统计显示车型的数量
				//把所有选择框都判断一遍，都选中则车型总全选框选中
				$("#tabs-manf .selectorContentTd").find("input").each(function(){
					var ts = $(this);
					var checkL = ($(this).attr("checked") == "checked");
					/*if(ts.parent().attr("style") == undefined){
						ts.parent().attr("style","")
					}*/
					//判断是不是隐藏,只对不隐藏的起效果
					if(ts.parent().css("display") != "none"){
						num++;
					}
				});
				if(num == $("#tabs-manf .selectorContentTd input:checked").length && (num != 0)){
					$("#modelAll").attr("checked","checked");
				} else{
					$("#modelAll").prop("checked",false);
				}
						
			}
		}
	});
	
	// 车型确认按钮动作
	$(".subModelModalContainer").find('.confirm').bind('click',function(){
		var containerId = $(this).parents(".subModelModalContainer").attr("id");
		var relInputName = $(this).attr("relInputName");
		var allSubModelArr = [];
		var tis = $(this);
		var strHTML = "";
		//如果车型全部选中，则显示全部
		if($("#modelAll").prop("checked") && $(".subModelModalContainer .resultShowContent .removeBtnByResult").length != 0){
			
			strHTML += '<ul class="selectorResultContainer">';
				strHTML += '<li>';
			  	strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="-1" style="cursor:pointer" title="删除：'+'全部'+'">';
				strHTML += '<input type="hidden" letter="全部" pooAttributeId="全部" subModelName="全部" value="-1" name="'+relInputName+'">';
				strHTML += '全部' + '<i class="icon-remove" style="visibility: hidden;"></i>';
			  	strHTML += '</div>';
		  		strHTML += '</li>';
			strHTML += '</ul>';
			$(tis).parents(".subModelModalContainer").find('.resultShowContent').find('.removeBtnByResult').each(function(){
				var obj = {};
				obj.subModelId =  $(this).attr("subModelId");
				obj.subModelName =  $(this).attr("subModelName");
				obj.letter =  $(this).attr("letter");
				obj.pooAttributeId = $(this).attr("pooAttributeId");
				allSubModelArr[allSubModelArr.length] = obj;
			});
			strHTML += '<ul class="selectorResultContainer"  style="display:none">';
			for(var i=0;i<allSubModelArr.length;i++){
				strHTML += '<li>';
				strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+allSubModelArr[i].subModelId+'" style="cursor:pointer" title="删除：'+allSubModelArr[i].subModelName+'">';
				strHTML += '<input type="hidden" letter="'+allSubModelArr[i].letter+'" pooAttributeId="'+allSubModelArr[i].pooAttributeId+'" subModelName="'+allSubModelArr[i].subModelName+'" value="'+allSubModelArr[i].subModelId+'" name="'+relInputName+'">';
				strHTML += allSubModelArr[i].subModelName + '<i class="icon-remove" style="visibility: hidden;"></i>';
				strHTML += '</div>';
				strHTML += '</li>';
			 }
			strHTML += '</ul>';
		} else{
			$(this).parents(".subModelModalContainer").find('.resultShowContent').find('.removeBtnByResult').each(function(){
				var obj = {};
				obj.subModelId =  $(this).attr("subModelId");
				obj.subModelName =  $(this).attr("subModelName");
				obj.letter =  $(this).attr("letter");
				obj.pooAttributeId = $(this).attr("pooAttributeId");
				allSubModelArr[allSubModelArr.length] = obj;
			});
			
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
		}
		$('#subModelModalResultContainer').html(strHTML);
	});
	
	/**
	 * 确定查询
	 */
	$('#queryBtn').on('click', function (e) {
		var params = getParams();
		var beginDate = params.beginDate;
		var endDate = params.endDate;
		if($("#analysisDimensionType").val() == 1){
			var time = beginDate.replace("-","");
			var year = time.substr(0,4);
			var month = time.substr(4,5);
			if(month > 1){
				month = month-1;
			} else if(month = 1){
				year = year - 1;
				month = 12;
			}
			if(month.toString().length > 1){
				beginDate = year+"-"+month;
			} else{
				beginDate = year+"-0"+month;
			}
			
			endDate = $("#startDate").val();
		}
		params.beginDate = beginDate;
		params.endDate = endDate;
		if(paramsValidate()) return;
		$('body').showLoading();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/loadPolicyMonthData",
			   data: params,
			   success: function(data){
				   $('body').hideLoading();
				  //清空结果集
				   $("#simpleTableId").html("");
				   $("#detailTableId").html("");
				   $("#paging").html("");
				   if(data.detail != "")
				   {
					   //查询面板折叠
					   $(".queryConditionContainer .buttons .toggle a").click();
					   //时间点
					   if("1" == $("#analysisDimensionType").val())
					   { 
						   selectData = data;
						   createSimpleTable(data);//简单
						   createDetailedTable(data);//详细
					   }
					   //时间段
					   else createDetailedTimeTable(data);
					   
					   //保存当前查询条件，用于导出判断
					   $("#analysisId").val($("#analysisDimensionType").val());
					   $("#modelName").val($("#subModelModalResultContainer input[name='selectedSubModel']").attr("submodelname"));
					   $("#time").val($("#startDate").val());
					   $('body').hideLoading();
				   }else{
					   $("#simpleTableId").html("");
					   $("#simpleTableId").html("<h5>NO DATA!</h5>");
					   $('body').hideLoading();
				   }
			   },
			   error:function(){
				   $('body').hideLoading();
			   }
			});
	});
	
	/**
	 * 重置
	 */
	$('#resetBtn').on('click', function (e) {
		//车型容器
		$('#subModelModalResultContainer').html("");
		
		$('#formId :reset');	
		
		$('#dateSelectText').css('visibility','hidden');
		$('#endDate').css('visibility','hidden');
		$('#endDateButton').css('visibility','hidden');
		$("#otherTab").show();
		$("#simple a").click();
	});
	
});

/**
 * 创建时间点明细表格内容
 */
window.createDetailedTable = function(json)
{
	if(json)
	{
		var modelNames = json.submodelList;//车型信息
		var yearMonth = json.yearMonth;//年月信息
		var detail = json.detail;
		//拿取每个车型每个月的条数
		var policyListSizePerSubmodelPerMonth = new Array();
		for(var i = 0;i < modelNames.length;i++){
			policyListSizePerSubmodelPerMonth[i] = new Array();
			for(var t = 0; t < yearMonth.length;t++){
				for(var k = 0; k < detail.length;k++){
					var submodelId = detail[k].submodelId;
					var ym = detail[k].ym;
					
						if(submodelId == modelNames[i].submodelId && yearMonth[t].YM == ym ){
								var size = detail[k].policyList.length;
								policyListSizePerSubmodelPerMonth[i].push(size);
						}
				}
			}
			policyListSizePerSubmodelPerMonth.push(policyListSizePerSubmodelPerMonth[i]);
		}
		//计算每个车型里的在所有月的最大条数
		var maxPolicyNum = new Array();
		for(var i = 0; i < policyListSizePerSubmodelPerMonth.length; i++){
			var size = 0;
			for(var k = 0; k < policyListSizePerSubmodelPerMonth[i].length; k++){
				if(size >= policyListSizePerSubmodelPerMonth[i][k]){
					size = size;
				} else{
					size = policyListSizePerSubmodelPerMonth[i][k];
				}
			}
			maxPolicyNum.push(size);
		}
		
		//兼容ie设置宽度
		$("#detailTableId").attr("style","");
		
		var tableHtml = ""
			tableHtml += "<table style=';'>";
		//时间行
			tableHtml += "<thead>";
				tableHtml += "<tr>";
					tableHtml += "<td style='width:15%;height:75px;'>车型</td>";
					for(var i = 0; i < yearMonth.length; i++){
						tableHtml += "<td colspan='3'>" + yearMonth[i].YM + "</td>";
					}
					//tableHtml += "<td></td>";
				tableHtml += "</tr>";
				tableHtml += "</thead>";
				tableHtml += "<tbody>";
				
				startNumber = (pageNumber - 1) * eachNumber;
				endNumber = startNumber + eachNumber;
				maxPageNumber = Math.ceil(modelNames.length/eachNumber);//向上取整，小数部分加一取整
				if(endNumber > modelNames.length) {
					endNumber = modelNames.length;
				}
				if(startNumber < 0) {
					startNumber = 0;
				}
					//车型行
				for(var i = startNumber;i < endNumber; i++){
					var rewardTotal = new Array();
					for(var k = 0; k < maxPolicyNum[i];k++){
						tableHtml += "<tr>";
						if(k==0){
							tableHtml += "<td rowspan="+(maxPolicyNum[i]+1)+">" + modelNames[i].submodelName + "</td>";
						} 
						
						var lastMonthPolicyContent = "";//上月政策
						for(var t = 0; t < yearMonth.length; t++){
							var monthPolicyContent = "";//本月政策
							var policyName = "";
							var policyContent = "";
							var reward = "";
							var subsidyTypeId = "";
							for(var y = 0; y < detail.length; y++){
								var submodelId = detail[y].submodelId;
								var ym = detail[y].ym;
								if(modelNames[i].submodelId == submodelId && ym==yearMonth[t].YM){
									if(k >= detail[y].policyList.length){
										policyName="";
										policyContent = "";
										reward="";
										subsidyTypeId="";
										
									} else{
										policyName = detail[y].policyList[k].policyName;//政策名称
										policyContent = detail[y].policyList[k].policyContent;//政策内容
										reward = detail[y].policyList[k].reward;//促销
										subsidyTypeId = detail[y].policyList[k].subsidyTypeId;//政策类型
									}
									if(k==0){
										rewardTotal.push(detail[y].policyList[0].rewardTotal);//单车促销
									}
									
									if(t == 0){
										if(policyContent == ""){
											lastMonthPolicyContent ="";
										} else{
											lastMonthPolicyContent = policyName+": "+policyContent;
										}
									} else{
										if(lastMonthPolicyContent !="" && policyContent == ""){
											monthPolicyContent = "<font color='red' style='font-weight:bold;'>取消</font>"
										} else if(lastMonthPolicyContent == "" && policyContent == ""){
											monthPolicyContent = "";
										} else if(lastMonthPolicyContent != "" && policyContent != "" && lastMonthPolicyContent != (policyName+": "+ policyContent)){
											monthPolicyContent = "<font color='green' style='font-weight:bold;'>" + policyName+": "+policyContent + "</font>";
										}else if(lastMonthPolicyContent == "" && policyContent != ""){
											monthPolicyContent ="<font color='green' style='font-weight:bold;'>"+"新增" + policyName+": "+policyContent + "</font>";
										} else {
											monthPolicyContent = policyName+": "+policyContent;
										}
									}
								}
							}
							var sort = k+1;
							if(subsidyTypeId == 15){
								sort = "◆";
							} 
							tableHtml += "<td style='width:5%;height:50px;'>" + sort + "</td>";//排序列
							if(t == 1){
								tableHtml += "<td style='width:30%;height:50px;text-align:left;'>"  + monthPolicyContent + "</td>";//政策内容列
							} else{
								tableHtml += "<td style='width:30%;height:50px;text-align:left;'>"  + lastMonthPolicyContent + "</td>";//政策内容列
							}
							
							if(sort == "◆"){
								tableHtml += "<td style='width:7%;height:50px'>" + "-" + "</td>";//reward促销列
							} else{
								tableHtml += "<td style='width:7%;height:50px'>" + formatNum(reward) + "</td>";//reward促销列
							}
						}
						tableHtml += "</tr>";
					}
					tableHtml += "<tr>";
					//tableHtml += "<td></td>";
					for(var t = 0; t < yearMonth.length; t++){
						var value = "";
						
						for(var y = 0; y < detail.length; y++){
							var submodelId = detail[y].submodelId;
							var ym = detail[y].ym;
							if(modelNames[i].submodelId == submodelId && ym==yearMonth[t].YM){
								for(var L = 0;L < detail[y].policyList.length; L++){
									if(detail[y].policyList[L].rewardTotal != ""){
										value = detail[y].policyList[L].rewardTotal;//单车促销
										break;
									}
								}
							}
						}
						
						tableHtml += "<td></td>";
						tableHtml += "<td style='height:50px;text-align:right;'>单车促销</td>";
						if("" == value){
							tableHtml += "<td>" + "" + "</td>";
						} else{
							tableHtml += "<td>" + formatNum(Math.round(value/100)*100) + "</td>";
						}
					}
					tableHtml += "</tr>";
					//单车促销下面的空行
					/*tableHtml += "<tr>";
					for(var t = 0; t < yearMonth.length; t++){
						
						tableHtml += "<td></td>";
						tableHtml += "<td style='height:50px;'></td>";
						tableHtml += "<td></td>";
					}
					tableHtml += "</tr>";*/
				}
				
				tableHtml += "</tbody>";
				tableHtml += "</table>";
		$("#detailTableId").html(tableHtml);
	}
};


/**
 * 创建时间段明细表格内容
 */
window.createDetailedTimeTable = function(json)
{
	if(json)
	{
		var modelNames = json.submodelList;//车型信息
		var yearMonth = json.yearMonth;//年月信息
		var detail = json.detail;
		//拿取每个车型每个月的条数
		var policyListSizePerSubmodelPerMonth = new Array();
		for(var i = 0;i < modelNames.length;i++){
			policyListSizePerSubmodelPerMonth[i] = new Array();
			for(var t = 0; t < yearMonth.length;t++){
				for(var k = 0; k < detail.length;k++){
					var submodelId = detail[k].submodelId;
					var ym = detail[k].ym;
					
						if(submodelId == modelNames[i].submodelId && yearMonth[t].YM == ym ){
								var size = detail[k].policyList.length;
								policyListSizePerSubmodelPerMonth[i].push(size);
						}
				}
			}
			policyListSizePerSubmodelPerMonth.push(policyListSizePerSubmodelPerMonth[i]);
		}
		//计算每个车型里的在所有月的最大条数
		var maxPolicyNum = new Array();
		for(var i = 0; i < policyListSizePerSubmodelPerMonth.length; i++){
			var size = 0;
			for(var k = 0; k < policyListSizePerSubmodelPerMonth[i].length; k++){
				if(size >= policyListSizePerSubmodelPerMonth[i][k]){
					size = size;
				} else{
					size = policyListSizePerSubmodelPerMonth[i][k];
				}
			}
			maxPolicyNum.push(size);
		}
		var width = (yearMonth.length-1)*400 + 550 + 200;
		//兼容ie设置宽度
		$("#detailTableId").attr("style","width:"+width+"px;");
		
		var tableHtml = ""
			tableHtml += "<table style='width:"+width+"px;'>";
		//时间行
		tableHtml += "<thead>";
				tableHtml += "<tr>";
					tableHtml += "<td style='width:200px;height:75px;'>车型</td>";
					tableHtml += "<td>序号</td>";
					tableHtml += "<td>奖励名称</td>";
					for(var i = 0; i < yearMonth.length; i++){
							tableHtml += "<td colspan='2'>" + yearMonth[i].YM + "</td>";
						}
				tableHtml += "</tr>";
				tableHtml += "</thead>";
				tableHtml += "<tbody>";
				//车型行
				for(var i = 0; i < modelNames.length; i++){
					var rewardTotal = new Array();
					for(var k = 0; k < maxPolicyNum[i];k++){
						tableHtml += "<tr>";
						if(k==0){
							tableHtml += "<td rowspan="+(maxPolicyNum[i]+2)+">" + modelNames[i].submodelName + "</td>";
						} 
						
						for(var t = 0; t < yearMonth.length; t++){
							var policyName = "";
							var policyContent = "";
							var reward = "";
							var subsidyTypeId = "";
							for(var y = 0; y < detail.length; y++){
								var submodelId = detail[y].submodelId;
								var ym = detail[y].ym;
								if(modelNames[i].submodelId == submodelId && ym==yearMonth[t].YM){
									if(k >= detail[y].policyList.length){
										policyName="";
										policyContent = "";
										reward="";
										subsidyTypeId="";
										
									} else{
										policyName = detail[y].policyList[k].policyName;//政策名称
										policyContent = detail[y].policyList[k].policyContent;//政策内容
										reward = detail[y].policyList[k].reward;//促销
										subsidyTypeId = detail[y].policyList[k].subsidyTypeId;//政策类型
									}
									if(k==0){
										rewardTotal.push(detail[y].policyList[0].rewardTotal);//单车促销
									}
								}
							}
							var sort = k+1;
							if(subsidyTypeId == 15){
								sort = "◆";
							} 
							if(t == 0){
								tableHtml += "<td style='width:50px;height:50px;'>" + sort + "</td>";//排序列
								tableHtml += "<td style='width:100px;height:50px;'>" + policyName + "" + "</td>";//排序列

							}
							tableHtml += "<td style='width:350px;height:50px;text-align:left;'>" +   policyContent + "</td>";//政策内容列
							
							if(sort == "◆"){
								tableHtml += "<td style='width:50px;height:50px;'>" + "-" + "</td>";//reward促销列

							} else{
								tableHtml += "<td style='width:50px;height:50px;'>" + formatNum(reward) + "</td>";//reward促销列
							}
						}
						tableHtml += "</tr>";
					}
					tableHtml += "<tr>";
					for(var t = 0; t < yearMonth.length; t++){
						var value = "";
						
						for(var y = 0; y < detail.length; y++){
							var submodelId = detail[y].submodelId;
							var ym = detail[y].ym;
							if(modelNames[i].submodelId == submodelId && ym==yearMonth[t].YM){
								for(var L = 0;L < detail[y].policyList.length; L++){
									if(detail[y].policyList[L].rewardTotal != ""){
										value = detail[y].policyList[L].rewardTotal;//单车促销
										break;
									}
								}
							}
						}
						
						if(t == 0){
							tableHtml += "<td></td>";
							tableHtml += "<td style='height:50px;'>单车促销</td>";
						}
						tableHtml += "<td></td>";
						if("" == value){
							tableHtml += "<td>" + "" + "</td>";
						} else{
							tableHtml += "<td>" + formatNum(Math.round(value/100)*100) + "</td>";
						}
					}
					tableHtml += "</tr>";
					//单车促销下面的空行
					/*tableHtml += "<tr>";
					for(var t = 0; t < yearMonth.length; t++){
						
						tableHtml += "<td></td>";
						tableHtml += "<td></td>";
						tableHtml += "<td></td>";
					}
					tableHtml += "</tr>";*/
				}
				tableHtml += "</tbody>";
				tableHtml += "</table>";
		$("#detailTableId").html(tableHtml);
	}
};

/**
 * 合并TD
 */
window.mergeTd = function() 
{ 
	var trObj = $("#detailTableId table tr"); 
	for(var i = 1; i < trObj.length; i++)
	{
		var tdObj = $(trObj[i]).find("td");
		var tdHtml = "";
		var colspan = 1;
		var colspanTd = "";
		for(var j = 0; j < tdObj.length; j++)
		{
			var tdValue = $.trim($(tdObj[j]).text());
			if(tdValue || j == (tdObj.length - 1) )
			{
				if(tdHtml == tdValue)
				{
					colspan ++;
					$(tdObj[j]).remove();
				}
				if( tdHtml != tdValue || j == (tdObj.length - 1) )
				{
					if(1 != colspan)
					{
						$(colspanTd).attr("colspan",colspan);
					}
					colspanTd = $(tdObj[j]);
					tdHtml = tdValue;
					colspan = 1;
				}
			}
		}
	}
};

/**
 * 创建时间点简单表格内容
 */
window.createSimpleTable = function(json)
{
	if(json)
	{
		var modelNames = json.submodelList;//车型信息
		var yearMonth = json.yearMonth;//年月信息
		var detail = json.detail;
		//拿取每个车型每个月的条数
		var policyListSizePerSubmodelPerMonth = new Array();
		for(var i = 0;i < modelNames.length;i++){
			policyListSizePerSubmodelPerMonth[i] = new Array();
			for(var t = 0; t < yearMonth.length;t++){
				for(var k = 0; k < detail.length;k++){
					var submodelId = detail[k].submodelId;
					var ym = detail[k].ym;
					
						if(submodelId == modelNames[i].submodelId && yearMonth[t].YM == ym ){
								var size = detail[k].policyList.length;
								policyListSizePerSubmodelPerMonth[i].push(size);
						}
				}
			}
			policyListSizePerSubmodelPerMonth.push(policyListSizePerSubmodelPerMonth[i]);
		}
		//计算每个车型里的在所有月的最大条数
		var maxPolicyNum = new Array();
		for(var i = 0; i < policyListSizePerSubmodelPerMonth.length; i++){
			var size = 0;
			for(var k = 0; k < policyListSizePerSubmodelPerMonth[i].length; k++){
				if(size >= policyListSizePerSubmodelPerMonth[i][k]){
					size = size;
				} else{
					size = policyListSizePerSubmodelPerMonth[i][k];
				}
			}
			maxPolicyNum.push(size);
		}
		
		//兼容ie设置宽度
		$("#detailTableId").attr("style","");
		
		var tableHtml = ""
			tableHtml += "<table style='width:96%;margin:20px;'>";
		//时间行
			tableHtml += "<thead>";
				tableHtml += "<tr>";
					tableHtml += "<td style='width:15%;height:70px;'>车型</td>";
					tableHtml += "<td width='42.6%;' colspan='1'>上月政策</td>";
					tableHtml += "<td colspan='1' width=''>" + yearMonth[1].YM.substr(4,6) + "月政策</td>";
					//tableHtml += "<td></td>";
				tableHtml += "</tr>";
			tableHtml += "<thead>";
			tableHtml += "<tbody>";
			
			startNumber = (pageNumber - 1) * eachNumber;
			endNumber = startNumber + eachNumber;
			maxPageNumber = Math.ceil(modelNames.length/eachNumber);//向上取整，小数部分加一取整
			if(endNumber > modelNames.length) {
				endNumber = modelNames.length;
			}
			if(startNumber < 0) {
				startNumber = 0;
			}
				//车型行
			for(var i = startNumber;i < endNumber; i++){
					for(var k = 0; k < maxPolicyNum[i];k++){
						tableHtml += "<tr>";
						if(k==0){
							tableHtml += "<td rowspan="+(maxPolicyNum[i]+1)+" style='font-size: 20px;font-weight: bold;text-align: center;width:100px;'>" + modelNames[i].submodelName + "</td>";
						} 
						
						var lastMonthPolicyContent = "";
						for(var t = 0; t < yearMonth.length; t++){
							var monthPolicyContent ="";
							
							var policyName = "";
							var policyContent = "";
							//var reward = "-";
							var subsidyTypeId = "-";
							
							var sort = (k+1)+". ";
							
							
							for(var y = 0; y < detail.length; y++){
								var submodelId = detail[y].submodelId;
								var ym = detail[y].ym;
								if(modelNames[i].submodelId == submodelId && ym==yearMonth[t].YM){
									if(k >= detail[y].policyList.length){
										policyName="";
										policyContent = "";
										//reward="-";
										subsidyTypeId="";
										
									} else{
										policyName = detail[y].policyList[k].policyName;//政策名称
										policyContent = detail[y].policyList[k].policyContent;//政策内容
										//reward = detail[y].policyList[k].reward;//促销
										subsidyTypeId = detail[y].policyList[k].subsidyTypeId;//政策类型
										if(subsidyTypeId == 15){
											sort = "◆  ";
										} 
									}
									if(t == 0){
											lastMonthPolicyContent = policyContent;
									} else{
										if(lastMonthPolicyContent != "" && lastMonthPolicyContent == policyContent ){
											monthPolicyContent = /*"<font color='#3AB969'>"*/"<font>"+ sort +"延续</font>";
										} else if(lastMonthPolicyContent != ""  && policyContent != ""&&lastMonthPolicyContent != policyContent){
											monthPolicyContent = "<font color='green' style='font-weight:bold;'>" + sort+""+ policyName+": " +policyContent + "</font>";
										} else if(lastMonthPolicyContent == "" && policyContent == ""){
											monthPolicyContent = sort +"";
										} else if(lastMonthPolicyContent != "" && policyContent == ""){
											monthPolicyContent = "<font color='red'style='font-weight:bold;'>"+ sort +"取消</font>";
										} else if(lastMonthPolicyContent == ""  && lastMonthPolicyContent != policyContent){
											monthPolicyContent = "<font color='green' style='font-weight:bold;'>" +sort+"新增"+ policyName +": "+ policyContent + "</font>";
										}
									}
								}
							}
							
							//tableHtml += "<td style='width:10%;height:5%'>" + sort + "</td>";//排序列
							if(t==1){
								tableHtml += "<td style='height:50px;'>"  + monthPolicyContent + "</td>";//政策内容列
							} else{
								if(policyContent == ""){
									tableHtml += "<td style='height:50px;'>" + sort+"" + "</td>";//政策内容列
								} else{
									tableHtml += "<td style='height:50px;'>" + sort+""+policyName +": " + policyContent + "</td>";//政策内容列
								}
							}
							//tableHtml += "<td style='width:15%;height:5%'>" + reward + "</td>";//reward促销列
							
						}
						tableHtml += "</tr>";
					}
					tableHtml += "<tr>";
					//tableHtml += "<td></td>";
					for(var t = 0; t < yearMonth.length; t++){
						var value = "";
						
						
						for(var y = 0; y < detail.length; y++){
							var submodelId = detail[y].submodelId;
							var ym = detail[y].ym;
							if(modelNames[i].submodelId == submodelId && ym==yearMonth[t].YM){
								for(var L = 0;L < detail[y].policyList.length; L++){
									if(detail[y].policyList[L].rewardTotal != ""){
										value = detail[y].policyList[L].rewardTotal;//单车促销
										break;
									}
								}
							
							}
						}
						
						//tableHtml += "<td></td>";
						if("" == value){
							tableHtml += "<td style='height:50px;text-align:right;'>平均单车：" + "" + "</td>";
						} else{
							tableHtml += "<td style='height:50px;text-align:right;'>平均单车：" + formatNum(Math.round(value/100)*100) + "元" + "</td>";
						}
						//tableHtml += "<td>" + value + "</td>";
					}
					//单车促销下面的空行
					tableHtml += "</tr>";
					/*for(var t = 0; t < yearMonth.length; t++){
						
						tableHtml += "<td></td>";
						tableHtml += "<td></td>";
						tableHtml += "<td></td>";
					}
					tableHtml += "</tr>";*/
				}
				tableHtml += "</tbody>";
				tableHtml += "</table>";
				$("#simpleTableId").html(tableHtml);
				
				//如果总数小于页面显示数
				if(modelNames.length > eachNumber){
					var pagingHtml = "<tr>"
					pagingHtml += "<td class='tdSubModel' style='border: 1px solid #F9F9F9;background-color:#F9F9F9;'></td>";
					pagingHtml += "<td class='tdSubModel' style='border: 1px solid #F9F9F9;background-color:#F9F9F9;'>"
						pagingHtml +="<input type='button' value='上一页' onclick='prevPage()'>                           ";
						pagingHtml +="<input type='button' value='下一页' onclick='nextPage()'>";
						pagingHtml +="                     当前第" + pageNumber +"/"+ maxPageNumber + "页                          ";
						pagingHtml +="跳转到  ";
						pagingHtml +="<input type='text' id='jump' style='width:60px;height:10px' maxlength='8' onkeyup='keyUp(this)'>";
						pagingHtml +="  页                                                        ";
						pagingHtml +="<input type='button' value='跳转' onclick='jumpPage()'>";
					pagingHtml +="</td>";
					pagingHtml += "</tr>";
				}
				
				$("#paging").html(pagingHtml)
				
	}
	
};

/**
 * 分页跳转内容只能写数字
 * @param data
 */
	function keyUp(data){
		data.value=data.value.replace(/\D/g,'');
	}

/**
 * 参数验证
 */
window.paramsValidate = function()
{
	var beginDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var modelLength = $("#subModelModalResultContainer :input[name='selectedSubModel']").length;
	
	if(0 == modelLength)
	{
		alert("请选择车型");
		return true;
	}
	if("2" == $("#analysisDimensionType").val())
	{
		if(parseInt(beginDate.replace("-","")) > parseInt(endDate.replace("-","")) )
		{
			alert("开始时间不能大于结束时间");
			return true;
		}
	}
	return false;
};

/**
 * 获取页面请求参数
 */
window.getParams = function()
{
	var beginDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var analysisDimensionType = $("#analysisDimensionType").val();//数据指标分析类型
	var mids = $("#subModelModalResultContainer input[name='selectedSubModel']").map(function(){return $(this).val();}).get().join(",");
	
	var paramObj = {};
	paramObj.beginDate = beginDate.replace("-","");
	paramObj.endDate = endDate.replace("-","");
	paramObj.modelIds = mids;
	paramObj.analysisDimensionType = analysisDimensionType;
	getQueryConditionAndBrowser(paramObj);
	return paramObj;
};

/**
 * 获取页头查询条件，以及浏览器
 */
window.getQueryConditionAndBrowser = function(paramObj)
{
	paramObj.browser = navigator.appVersion;
	var queryCondition = "";
	
	queryCondition += "分析维度 = ";
	if("1" == $("#analysisDimensionType").val()) queryCondition += "车型对比";
	if("2" == $("#analysisDimensionType").val()) queryCondition += "时间对比";
	
	queryCondition += "\n时间 = ";
	if("1" == $("#analysisDimensionType").val()) queryCondition += $("#startDate").val();
	if("2" == $("#analysisDimensionType").val()) queryCondition += $("#startDate").val() + "至" + $("#endDate").val();
	
	queryCondition += "\n车型 = " + $("#subModelModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
	paramObj.queryCondition = queryCondition;
};

/**
 * 导出Excel
 * @param exportType 1:英文版;2:中文版
 */
function exportExcel(exportType)
{
	if(!$.trim($("#detailTableId").html()))
	{
		alert("暂无数据导出");
		return;
	}
	$("#exportFormId").submit();
}


function changeDateType(){
	
	var dateType = $('#analysisDimensionType').val();
	//时间点，隐藏结束时间控件
	if(dateType == 1)
	{
		$("#otherTab").show();
		$('#dateSelectText').css('visibility','hidden');
		$('#endDate').css('visibility','hidden');
		$('#endDateButton').css('visibility','hidden');
		//隐藏标签存了 结束时间 开始时间设置为结束时间(最新时间)
		$('#startDate').val($('#hiddenEDate').val());
	}
	//时间段，显示出开始时间和结束时间控件
	else
	{
		$("#detail a").click();
		$("#otherTab").hide();
		$('#dateSelectText').css('visibility','visible');
		$('#endDate').css('visibility','visible');
		$('#endDateButton').css('visibility','visible');
		//隐藏标签存了 开始时间  开始时间设置为往前推一年的
		$('#startDate').val($('#hiddenSDate').val());
	}
	$("#subModelModalResultContainer").html("");
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
	
	//清空本页的选项
	//把结果集里选中的都打钩	
	var choseType = $("#choseType").val();
	if(choseType ==1){
		var ty = $("#tabs-competingProducts .subModelIdInput");
		ty.each(function(){
				$(this).prop("checked",false);
		});
		$("#selectorResultContainerBySubModel .removeBtnByResult").each(function(){
			var subModelId = $(this).attr("subModelId");
			ty.each(function(){
				if($(this).val() == subModelId){
					$(this).prop("checked",true);//行全选
				} 
			});
		});
	} else if(choseType ==2){
		var ty = $("#tabs-segment .subModelIdInput");
		ty.each(function(){
			$(this).prop("checked",false);
	});
		
		$("#selectorResultContainerBySubModel .removeBtnByResult").each(function(){
			var subModelId = $(this).attr("subModelId");
			ty.each(function(){
				if($(this).val() == subModelId) {
					$(this).prop("checked",true);//行全选
					return false;
				}
			});
		});
	} else if(choseType ==3){
		var ty = $("#tabs-brand .subModelIdInput");
		ty.each(function(){
			$(this).prop("checked",false);
	});
		$("#selectorResultContainerBySubModel .removeBtnByResult").each(function(){
			var subModelId = $(this).attr("subModelId");
			ty.each(function(){
				if($(this).val() == subModelId){
					$(this).prop("checked",true);//行全选
					return false;
				} 
			});
		});
	} else if(choseType ==4){
		var ty = $("#tabs-manf .subModelIdInput");
		ty.each(function(){
			$(this).prop("checked",false);
	});
		$("#selectorResultContainerBySubModel .removeBtnByResult").each(function(){
			var subModelId = $(this).attr("subModelId");
			ty.each(function(){
				if($(this).val() == subModelId) {
					$(this).prop("checked",true);//行全选
					return false;
				}
			});
		});
	}
	
	/* 车型全选框勾选判断 结束*/
	checkAll(type);
	
	//如果整个页面显示的车型有没有选中的，车型全选框不选；
	var flag = false;
	if(choseType == 1){
		$("#tabs-competingProducts .subModelIdInput").each(function(){
			   var ths = $(this);
				//判断是不是隐藏
			   if(ths.parent().css("display") != "none"){
					if(!$(this).attr("checked")){
						$("#modelAll").prop("checked",false);
						flag = true;
					}
				}
				
			});
	} else if(choseType == 2){
		$("#tabs-segment .subModelIdInput").each(function(){
			   var ths = $(this);
				//判断是不是隐藏
			   if(ths.parent().css("display") != "none"){
					if(!$(this).attr("checked")){
						$("#modelAll").prop("checked",false);
						flag = true;
					}
				}
				
			});
		if($("#tabs-segment").html()!="" && !flag){
			$("#modelAll").prop("checked",true);
		}
	} else if(choseType == 3){
		$("#tabs-brand .subModelIdInput").each(function(){
			   var ths = $(this);
				//判断是不是隐藏
			   if(ths.parent().css("display") != "none"){
					if(!$(this).attr("checked")){
						$("#modelAll").prop("checked",false);
						flag = true;
					}
				}
				
			});
		if($("#tabs-brand").html()!="" && !flag){
			$("#modelAll").prop("checked",true);
		}
	} else if(choseType == 4){
		$("#tabs-manf .subModelIdInput").each(function(){
			   var ths = $(this);
				//判断是不是隐藏
			   if(ths.parent().css("display") != "none"){
					if(!$(this).attr("checked")){
						$("#modelAll").prop("checked",false);
						flag = true;
					}
				}
			});
		if($("#tabs-manf").html()!="" && !flag){
			$("#modelAll").prop("checked",true);
		}
	}
	
	if('1' == type) getPolicyModelPage("tabs-competingProducts",type);
	else if('2' == type) getPolicyModelPage("tabs-segment",type);
	else if('3' == type) getPolicyModelPage("tabs-brand",type);
	else getPolicyModelPage("tabs-manf",type);
};

/**
 * 展示页面
 * @param id 展示容器ID
 * @param type 子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
 * @param inputType 控件类型1：复选，2：单选;默认为1
 * @param timeType 时间类型1：时间点;2：时间段默认为2
 * 
 */
function getPolicyModelPage(id,type,timeType)
{
	//如果内容不为空则触发请求
	if(!$.trim($('#' + id).html())){
		
		var beginDate = ($("#startDate").val()).replace("-","");
		var endDate = ($("#endDate").val()).replace("-","");
		//传递参数
		var params = {subModelShowType:type,beginDate:beginDate,endDate:endDate,analysisDimensionType:$("#analysisDimensionType").val()};
		//触发请求
		showLoading(id);
		$('#' + id).load(ctx+"/policy/global/getSubmodelModal",params,function(){
			//如果是时间对比隐藏大类全选框
			if($("#analysisDimensionType").val() == 2){
				$("#subModelModalBody .selectorTypeTd input").css("display","none");
			} else{
				$("#subModelModalBody .selectorTypeTd input").css("display","inline");
			}
			
			var choseType = $("#choseType").val(); 
			//把结果集里选中的都打钩	
			if(choseType == 1){
				var ty = $("#tabs-competingProducts .subModelIdInput");
				ty.each(function(){
						$(this).prop("checked",false);
				});
				$("#selectorResultContainerBySubModel .removeBtnByResult").each(function(){
					var subModelId = $(this).attr("subModelId");
					ty.each(function(){
						if($(this).val() == subModelId){
							$(this).prop("checked",true);//行全选
						} 
					});
				});
			} else if(choseType ==2){
				var ty = $("#tabs-segment .subModelIdInput");
				ty.each(function(){
					$(this).prop("checked",false);
			});
				
				$("#selectorResultContainerBySubModel .removeBtnByResult").each(function(){
					var subModelId = $(this).attr("subModelId");
					ty.each(function(){
						if($(this).val() == subModelId) {
							$(this).prop("checked",true);//行全选
							return false;
						}
					});
				});
			} else if(choseType ==3){
				var ty = $("#tabs-brand .subModelIdInput");
				ty.each(function(){
					$(this).prop("checked",false);
			});
				$("#selectorResultContainerBySubModel .removeBtnByResult").each(function(){
					var subModelId = $(this).attr("subModelId");
					ty.each(function(){
						if($(this).val() == subModelId){
							$(this).prop("checked",true);//行全选
							return false;
						} 
					});
				});
			} else if(choseType ==4){
				var ty = $("#tabs-manf .subModelIdInput");
				ty.each(function(){
					$(this).prop("checked",false);
			});
				$("#selectorResultContainerBySubModel .removeBtnByResult").each(function(){
					var subModelId = $(this).attr("subModelId");
					ty.each(function(){
						if($(this).val() == subModelId) {
							$(this).prop("checked",true);//行全选
							return false;
						}
					});
				});
			}
			/*}*/
			
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
			checkAll(type);
			/*车型全选框勾选判断*/
			//如果整个页面显示的车型有没有选中的，车型全选框不选；
			var flag = false;
			if(choseType == 1){
				$("#tabs-competingProducts .subModelIdInput").each(function(){
					   var ths = $(this);
						/*if(ths.parent().attr("style") == undefined){
							ths.parent().attr("style","");
						}*/
						//判断是不是隐藏
					   if(ths.parent().css("display") != "none"){
							if(!$(this).attr("checked")){
								$("#modelAll").prop("checked",false);
								flag = true;
							}
						}
						
					});
			} else if(choseType == 2){
				$("#tabs-segment .subModelIdInput").each(function(){
					   var ths = $(this);
						/*if(ths.parent().attr("style") == undefined){
							ths.parent().attr("style","");
						}*/
						//判断是不是隐藏
					   if(ths.parent().css("display") != "none"){
							if(!$(this).attr("checked")){
								$("#modelAll").prop("checked",false);
								flag = true;
							}
						}
						
					});
			} else if(choseType == 3){
				$("#tabs-brand .subModelIdInput").each(function(){
					   var ths = $(this);
						/*if(ths.parent().attr("style") == undefined){
							ths.parent().attr("style","");
						}*/
						//判断是不是隐藏
					   if(ths.parent().css("display") != "none"){
							if(!$(this).attr("checked")){
								$("#modelAll").prop("checked",false);
								flag = true;
							}
						}
						
					});
			} else if(choseType == 4){
				$("#tabs-manf .subModelIdInput").each(function(){
					   var ths = $(this);
						/*if(ths.parent().attr("style") == undefined){
							ths.parent().attr("style","");
						}*/
						//判断是不是隐藏
					   if(ths.parent().css("display") != "none"){
							if(!$(this).attr("checked")){
								$("#modelAll").prop("checked",false);
								flag = true;
							}
						}
					});
			}
			if(!flag){
				$("#modelAll").prop("checked",true);
			}
			/*车型全选框勾选判断结束*/
		});
	} 
};

/**
 * 格式化千分位
 * @param strNum
 * @returns
 */
function formatNum(strNum) {
	if("-"==strNum){
		return "";
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

