/**定义子车型LI全局变量*/
var currSubModelLI = null;
/**定义车身形式LI全局变量*/
var currBodyTypeLI = null;
//查询后的数据
var selectData = null;
//页面显示的条数
var eachNumber = 15;
//开始条数
var startNumber = 0;
//结束条数
var endNumber = 0;
//当前页数
var pageNumber = 1;
//最大页数
var maxPageNumber = null;
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
		/*htmlStr += '		<span id="checkAllPromt">';
		htmlStr += '		<h6  style="display:inline"> <font color="red">提示:车型全选时因数据量过大建议查询时间段不要超过三个月。</font></h6>';
		htmlStr += '		</span>';*/
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
		htmlStr += '			<td valign="top" style="width:15%">	';
		htmlStr += '				<div style="width:90px">';
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
			$(this).prop("checked",false);//取消行全选
		});
		/**打开车身形式选择框时，清空车型选择*/
		$(".subModelModalResultContainer").html("");
		//加载子车型数据
		showLoading("bodyTypeModalBody");
		$('#bodyTypeModalBody').load(ctx+"/salesQuery/modelSalesAmountFaw/getBodyType",getParams(),function(){
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
		$("#subModelModalBody").load(ctx+"/salesQuery/modelSalesAmountFaw/getSubmodelModal",getParams(),function(){
			//弹出框设置默认选中项结果集		
			var strHTML = '<ul class="inline" >';
			
			$(currSubModelLI).find('.subModelModalResultContainer input').each(function(){
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
	
	
	/** 删除*/
	$(".selectorResultContainer .removeBtn").live('click',function(){
		var relContainer = $(this).attr("relContainer");
		var currVal = $(this).attr('value');
		if(currVal == -1){
			$('.subModelLIContainer .subModelModalResultContainer').html("");
		}
		$('#'+relContainer).find('.cityModalByCity').each(function(){
			if($.trim($(this).val()) == $.trim(currVal)){
				$(this).prop("checked",false);
				
				var allArr = $(this).parent().parent().parent().find(".cityModalByCity");
				var selectedArr = $(this).parent().parent().parent().find("input:checked");
				if(allArr.length == selectedArr.length){
					$(this).parent().parent().parent().parent().find('.cityModalByArea').each(function(){
						$(this).attr("checked",'true');//行全选
					});
				}else{
					$(this).parent().parent().parent().parent().find('.cityModalByArea').each(function(){
						$(this).prop("checked",false);//取消行全选
					});
				}
				
				var allCityArr = $(this).parents(".cityModalContainer").find(".cityModalByCity");
				var allSelectedCityArr = $(this).parents(".cityModalContainer").find(".cityModalByCity:checked");
				if(allCityArr.length == allSelectedCityArr.length){
					$(this).parents('.cityModalContainer').find('.cityModalByAll').attr("checked",'true');//全部全选		
				}else{
					$(this).parents('.cityModalContainer').find('.cityModalByAll').prop("checked",false);//全部取消
				}
			}
		});
		
		/**	删除级别全选时,对象拆分按钮释放可选 **/
		if(relContainer =='segmentModal' && currVal == 0){
			$(this).parents('.segmentLIContainer').find("tbody td input[name='obj_Split']").attr('disabled',false);
		}
		
		/**	删除车系全选时,对象拆分按钮释放可选 **/
		if(relContainer =='origModal' && currVal == 0){
			$(this).parents('.origLIContainer').find("tbody td input[name='obj_Split']").attr('disabled',false);
		}
		
		$(this).parent().remove();
		clearVersionByModelChange();
	});
	
	// 车型确认按钮动作
	$(".subModelModalContainer").find('.confirm').unbind('click').bind('click',function(e){
		var containerId = $(this).parents(".subModelModalContainer").attr("id");
		var relInputName = $(this).attr("relInputName");
		var allSubModelArr = [];
		var ts = $(this);
		//如果车型全部选中，则显示全部
		if($("#modelAll").prop("checked") && $(".subModelModalContainer .resultShowContent .removeBtnByResult").length != 0){
			var strHTML = "";
			strHTML += '<ul class="selectorResultContainer">';
				strHTML += '<li>';
			  	strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="-1" style="cursor:pointer" title="删除：'+'全部'+'">';
				strHTML += '<input type="hidden" letter="全部" pooAttributeId="全部" subModelName="全部" value="-1" name="'+relInputName+'">';
				strHTML += '全部' + '<i class="icon-remove" style="visibility: hidden;"></i>';
			  	strHTML += '</div>';
		  		strHTML += '</li>';
			strHTML += '</ul>';
			
			$(ts).parents(".subModelModalContainer").find('.resultShowContent').find('.removeBtnByResult').each(function(){
				var obj = {};
				obj.subModelId =  $(this).attr("subModelId");
				obj.subModelName =  $(this).attr("subModelName");
				obj.letter =  $(this).attr("letter");
				obj.pooAttributeId = $(this).attr("pooAttributeId");
				allSubModelArr[allSubModelArr.length] = obj;
			});
			strHTML += '<ul class="selectorResultContainer" style="display:none;">';
			for(var i=0;i<allSubModelArr.length;i++){
				strHTML += '<li>';
			  	strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+allSubModelArr[i].subModelId+'" style="cursor:pointer" title="删除：'+allSubModelArr[i].subModelName+'">';
				strHTML += '<input type="hidden" letter="'+allSubModelArr[i].letter+'" pooAttributeId="'+allSubModelArr[i].pooAttributeId+'" subModelName="'+allSubModelArr[i].subModelName+'" value="'+allSubModelArr[i].subModelId+'" name="'+relInputName+'">';
				strHTML += allSubModelArr[i].subModelName + '<i class="icon-remove" style="visibility: hidden;"></i>';
			  	strHTML += '</div>';
		  		strHTML += '</li>';
			 }
			strHTML += '</ul>';
			//当选择全部车型时在时间段后面给出建议查询不要超三个月限制
			/*var str = "";
				str += '		<h6> <font color="red">提示:车型众多建议查询时间段不要多于三个月，以防因数据量过大造成系统崩溃。</font></h6>';

			$("#checkAllPromt").html(str);*/
		} else{
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
			//去除全选提示
			/*$("#checkAllPromt").html("");*/
		}
		
		$(currSubModelLI).find('.subModelModalResultContainer').html(strHTML);
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
	/**展示车型弹出框-结束*/
	
	/** 弹出常用对象对话框 开始**/
	$('#autoVersionModal').on('show', function (e) {	
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
		if(e.relatedTarget)  return; 
		//加载子常用对象下型号数据
		showLoading("autoVersionModalBody");
		$('#autoVersionModalBody').load(ctx+"/salesQuery/modelSalesAmountFaw/getAutoCustomGroup",getParams(),function(){
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
					$(this).prop("checked",false);//取消选中
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
			url: ctx+"/salesQuery/modelSalesAmountFaw/getModelSalesAmountFawData",
			data: getParams(),
			success: function(data) {
				//清空图表
				$("#tSortable").html("");
				$("#paging").html("");
				//页面加载隐藏图表面板
				$(".tab-content").attr("style","display:block;");
			   if(data) {
				   if(data.totalData.length==0){
					   $("#tSortable").html("<h3>NO DATA!</h3>");
				   } else{
					   pageNumber = 1;
					   //查询面板折叠
				       $(".queryConditionContainer .buttons .toggle a").click();
					   $("#noData").css("display","none");
					   $("#exportButtons").css("display","block");
					   $("#chartId").height(450);
					   selectData = data;
					   showData(data);
				   }
			   } else {
				   $("#tSortable").html("<h3>NO DATA!</h3>");
			   }
			   $('body').hideLoading();
		   },
		   error:function() {
			 //清空图表
				$("#tSortable").html("");
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
		//清空查询结果
		$("#tSortable").html("");
		$("#paging").html("");
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
		//$("#tableHead").removeClass("hide");
		var tbodyHtml = "";
		var salesType = $("#salesType").val();//0为显示销量，1为隐藏销量
		var totalType = $("#totalType").val();//0为显示总计，1为隐藏总计
		if(data)
		{	
			tbodyHtml += "<thead  id='tableHead'>";
			tbodyHtml += "<tr>";
			tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white' rowspan='2'>" + "型号编码" + "</td>";
			tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white' rowspan='2'>" + "级别" + "</td>";
			tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white' rowspan='2'>" + "生产厂商" + "</td>";
			tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white' rowspan='2'>" + "车型" + "</td>";
			tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white' rowspan='2'>" + "排量" + "</td>";
			tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white' rowspan='2'>" + "排挡方式" + "</td>";
			tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white' rowspan='2'>" + "型号标识" + "</td>";
			tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white' rowspan='2'>" + "车身形式" + "</td>";
			tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white' rowspan='2'>" + "上市日期" + "</td>";
			tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white' rowspan='2'>" + "年款" + "</td>";
			tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white' rowspan='2'>" + "厂商指导价(元)" + "</td>";
			var ym = data.monthData[0].yearMonth;
			if(salesType == 0){
				tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white;border-bottom:1px solid white;' colspan='3'>" + ym + "</td>";
			} else{
				tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white;border-bottom:1px solid white;' >" + ym + "</td>";
			}
			for(var i = 0; i<data.monthData.length; i++){
				if(data.monthData[i].yearMonth != ym){
					if(salesType == 0){
						tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white;border-bottom:1px solid white;' colspan='3'>" + data.monthData[i].yearMonth + "</td>";
					} else{
						tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white;border-bottom:1px solid white;' >" + data.monthData[i].yearMonth + "</td>";
					}
					ym = data.monthData[i].yearMonth;
				}
			}
			//总计部分
			if(totalType == 0){
				tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white;border-bottom:1px solid white;' colspan='3'>" + "总计" + "</td>";
			}
			tbodyHtml += "</tr>";
			
			tbodyHtml += "<tr>";
			if(salesType == 0){
				tbodyHtml += "<td class='tHead' style='  text-align:center; border-right:1px solid white'>" + "车型销量" + "</td>";
			}
			tbodyHtml += "<td class='tHead' style='  text-align:center; border-right:1px solid white'>" + "销售比例" + "</td>";
			if(salesType == 0){
				tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white'>" + "型号销量" + "</td>";
			}
			var ym = data.monthData[0].yearMonth;
			for(var i = 0; i<data.monthData.length; i++){
				if(data.monthData[i].yearMonth != ym){
					if(salesType == 0){
						tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white'>" + "车型销量" + "</td>";
					}
					tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white'>" + "销售比例" + "</td>";
					if(salesType == 0){
						tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white'>" + "型号销量" + "</td>";
					}
					ym = data.monthData[i].yearMonth;
				}
			}
			//总计列
			if(totalType == 0){
				if(salesType == 0){
					tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white'>" + "车型销量" + "</td>";
				}
				tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white'>" + "销售比例" + "</td>";
				if(salesType == 0){
					tbodyHtml += "<td class='tHead' style=' text-align:center; border-right:1px solid white'>" + "型号销量" + "</td>";
				}
			}
			tbodyHtml += "</tr>";

			tbodyHtml += "</thead>";
			tbodyHtml += "<tbody id='gridTbody'>";
			tbodyHtml += "</tbody>";
			
			//数据行
			
			startNumber = (pageNumber - 1) * eachNumber;
			endNumber = startNumber + eachNumber;
			maxPageNumber = Math.ceil(selectData.totalData.length/eachNumber);//向上取整，小数部分加一取整
			if(endNumber > data.totalData.length) {
				endNumber = data.totalData.length;
			}
			if(startNumber < 0) {
				startNumber = 0;
			}
			for(var i = startNumber;i < endNumber; i++){
				tbodyHtml += "<tr>";
				tbodyHtml += "<td class='tdSubModel'>" + data.totalData[i].versionCode + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + data.totalData[i].gradeName + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + data.totalData[i].manfName + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + data.totalData[i].objName +"</td>";
				tbodyHtml += "<td class='tdSubModel'>" + data.totalData[i].pl + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + data.totalData[i].pd + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + data.totalData[i].versionName + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + data.totalData[i].bodyType + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + data.totalData[i].launchDate + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + data.totalData[i].year + "</td>";
				tbodyHtml += "<td class='tdSubModel'>" + formatNum(data.totalData[i].msrp) + "</td>";
				
				var ym = data.monthData[0].yearMonth;
				for(var k = 0; k < data.monthData.length; k++){
					if(data.totalData[i].vid == data.monthData[k].vid && data.monthData[k].yearMonth == ym){
						if(salesType == 0){
							if((data.monthData[k].percent == null ||"" == data.monthData[k].percent) && (data.monthData[k].versionSale == null || "" == data.monthData[k].versionSale)){
								tbodyHtml += "<td class='tdSubModel'>" + "-" + "</td>";
							} else{
								tbodyHtml += "<td class='tdSubModel'>" + formatNum(data.monthData[k].sales) + "</td>";
							}
						}
						if(data.monthData[k].percent==null||data.monthData[k].percent==""){
							tbodyHtml += "<td class='tdSubModel'>" + "-" + "</td>";
						} else{
							tbodyHtml += "<td class='tdSubModel'>" + (data.monthData[k].percent*100).toFixed(2) + "%</td>";
						}
						if(salesType == 0){
							tbodyHtml += "<td class='tdSubModel'>" + formatNum(data.monthData[k].versionSale) + "</td>";
						}
					}
				}
				for(var k = 0; k < data.monthData.length; k++){
					if(data.totalData[i].vid == data.monthData[k].vid && data.monthData[k].yearMonth != ym){
						if(salesType == 0){
							if((data.monthData[k].percent == null ||"" == data.monthData[k].percent) && (data.monthData[k].versionSale == null || "" == data.monthData[k].versionSale)){
								tbodyHtml += "<td class='tdSubModel'>" + "-" + "</td>";
							} else{
								tbodyHtml += "<td class='tdSubModel'>" + formatNum(data.monthData[k].sales) + "</td>";
							}
						}
						if(data.monthData[k].percent==null||data.monthData[k].percent==""){
							tbodyHtml += "<td class='tdSubModel'>" + "-" + "</td>";
						} else{
							tbodyHtml += "<td class='tdSubModel'>" + (data.monthData[k].percent*100).toFixed(2) + "%</td>";
						}
						if(salesType == 0){
							tbodyHtml += "<td class='tdSubModel'>" + formatNum(data.monthData[k].versionSale) + "</td>";
						}
						ym = data.monthData[k].yearMonth;
					}
				}
				//总计
				if(totalType == 0){
					if(salesType == 0){
						tbodyHtml += "<td class='tdSubModel'>" + formatNum(data.totalData[i].totalSales) + "</td>";
					}
					if(data.totalData[i].totalPercent==null||data.totalData[i].totalPercent==""){
						tbodyHtml += "<td class='tdSubModel'>" + "-" + "</td>";
					} else{
						tbodyHtml += "<td class='tdSubModel'>" + (data.totalData[i].totalPercent*100).toFixed(2) + "%</td>";
					}
					if(salesType == 0){
						tbodyHtml += "<td class='tdSubModel'>" + formatNum(data.totalData[i].totalVersionSale) + "</td>";
					}
				}
				tbodyHtml += "</tr>";
			}
			
			
			$("#tSortable").html(tbodyHtml);
			
			//如果总数小于页面显示数
			if(data.totalData.length > eachNumber){
				var pagingHtml = "<tr>"
				pagingHtml += "<td class='tdSubModel' style='border: 1px solid #EAEAEA;'></td>";
				pagingHtml += "<td class='tdSubModel' style='border: 1px solid #EAEAEA;'>"
					pagingHtml +="<input type='button' value='上一页' onclick='prevPage()'>                           ";
					pagingHtml +="<input type='button' value='下一页' onclick='nextPage()'>";
					pagingHtml +="                     当前第" + pageNumber +"/"+ maxPageNumber + "页                          ";
					pagingHtml +="跳转到  ";
					pagingHtml +="<input type='text' id='jump' style='width:60px;height:10px' maxlength='8' onkeyup='keyUp(this)'>";
					pagingHtml +="  页                                                        ";
					pagingHtml +="<input type='button' value='跳转' onclick='jumpPage()'>";
				pagingHtml +="</td>";
				/*pagingHtml += "<td class='tdSubModel' style='border: 1px solid #EAEAEA;'>"
					
				pagingHtml +="</td>";
				pagingHtml += "<td class='tdSubModel' style='border: 1px solid #EAEAEA;'>"
					
				pagingHtml +="</td>";
				pagingHtml += "<td class='tdSubModel' style='border: 1px solid #EAEAEA;'>"
					
				pagingHtml +="</td>";*/
				pagingHtml += "</tr>";
			}
			
			
			$("#paging").html(pagingHtml)
			//设置居中对齐
			$("#tSortable .tdSubModel").css("text-align","center");
	    }
	}
	
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
	var paramObj = getParams();
	if((paramObj.aVersionId == "" || paramObj.aVersionId == null) && (paramObj.subModelId == "" || paramObj.subModelId == null))
	{
		alert("请选择车型");
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
	var salesType = $("#salesType").val();//0为显示销量，1为隐藏销量；
	var totalType = $("#totalType").val();//0为显示累计，1为隐藏累计；
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
	
	timeRange = getTimeRange(paramObj.dateGroup);
	paramObj.beginDate = timeRange.beginDate;
	paramObj.endDate = timeRange.endDate;
	paramObj.browser = navigator.appVersion;
	paramObj.salesType = salesType;
	paramObj.totalType = totalType;
	getQueryCondition(paramObj);
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
	var content = $.trim($("#tSortable").html());
	if(!content || "无数据!" == content)
	{
	    alert("暂无数据导出");
		return true;
	} else{
		$("#languageType").val(languageType);
		$("#exportFormId").submit();
	}
	
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
			$(this).prop("checked",false);
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
			   if($("#tabs-segment").html()!="" && !flag){
					$("#modelAll").prop("checked",true);
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
		if($("#tabs-brand").html()!="" && !flag){
			$("#modelAll").prop("checked",true);
		}
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
		if($("#tabs-manf").html()!="" && !flag){
			$("#modelAll").prop("checked",true);
		}
	}
	
	/*if(!flag){
		$("#modelAll").prop("checked",true);
	}*/
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
		hatchbackId = $("#model .subModelLIContainer").eq($("#getModelIndexId").val()).find("td:eq(1)").find("li div :input").map(function(){return $(this).val();}).get().join(",");
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
		}
		//传递参数
		var params = {inputType:inputType,subModelShowType:type,beginDate:beginDate,endDate:endDate,analysisContentType:analysisContentType,timeType:timeType,hatchbackId:hatchbackId,dateGroup:dateGroup,priceType:priceType};
		//触发请求
		showLoading(id);
		$("#" + id).load(ctx+"/salesQuery/modelSalesAmountFaw/getSubmodelModal",params,function(){
			var choseType = $("#choseType").val(); 
			//把结果集里选中的都打钩	
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

/**把有全选的选项选上**/
/*function checkAll(type)
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
}*/

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
 * 格式化千分位
 * @param strNum
 * @returns
 */
function formatNum(strNum) {
	if(null==strNum||""==strNum){
		return "-";
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
 * 获取页头查询条件
 */
window.getQueryCondition = function(paramObj)
{
	var subModelId = $(".subModelModalResultContainer li div").map(function(){return $(this).text();}).get().join(",");
	var queryCondition = "";
	queryCondition += "分析维度 = 车型";
	queryCondition += "\n价格类型 = 指导价";
	queryCondition += "\n时间= " + paramObj.dateGroup;
	queryCondition += "\n车型= " + subModelId;
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

