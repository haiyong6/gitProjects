$(document).ready(function(){ 
	
	 $(".letterContainer .words").find('label[rel="word"]').live('click',function(){
		var wordText = $(this).text();
		var container =  $(this).parents('.letterContainer').find('.letterContentContainer');
	  	var scrollTo = $(this).parents('.letterContainer').find('.letterContentContainer').find('label[rel="findword"]').filter(function(){
			return $.trim($(this).text()) == $.trim(wordText);
		}).parent().parent();
		if(scrollTo && scrollTo.offset())
			container.scrollTop(scrollTo.offset().top-container.offset().top+container.scrollTop());
	});
	
	 $(".letterContainer").find('select[rel="words"]').live('change',function(){
		var wordText = $(this).find('option:selected').text();
		var container =  $(this).parents('.letterContainer').find('.letterContentContainer');
	  	var scrollTo = $(this).parents('.letterContainer').find('.letterContentContainer').find('label[rel="findword"]').filter(function(){
			/**
			 * 新增判断由于一汽大众常用型号组label中存在两个固定文本'Mix','MSRP',所以新增一个modelName属性用来存储判断的文本
			 */
			if(!$(this).attr("modelName") || !$(this).attr("modelName")) {
				return $.trim($(this).text()) == $.trim(wordText);
			}else{
				return $.trim($(this).attr("modelName")) == $.trim(wordText);
			}
			
		}).parent().parent();
		
		if(scrollTo && scrollTo.offset())
			container.scrollTop(scrollTo.offset().top-container.offset().top+container.scrollTop());
	});
	
	
	 $(".letterContainer").find('.locationSearch').live('keyup',function(){
	 	 var wordText = $.trim($(this).val().toUpperCase());
	 	 var container =  $(this).parents('.letterContainer').find('.letterContentContainer');
	 	
	 	 $(this).parents('.letterContainer').find('.letterContentContainer').find('.subModelIdLabel').each(function(){
	 		 $(this).removeClass("label label-info");
	 	 });
		 
	 	 if(wordText != ""){
		     var scrollToAll = $(this).parents('.letterContainer').find('.letterContentContainer').find('.subModelIdLabel').filter(function(){
		  	 var text = $.trim($(this).text()).toUpperCase();
		  	 var letter = $.trim($(this).attr("letter")).toUpperCase();
		  	 if(text.indexOf(wordText) != -1 || letter == wordText){
		  	     $(this).addClass("label label-info");
		  		     return true;
		  		 }else{
		  			 return false;
		  		 }			
			 });
			 if(scrollToAll.length>0){
			     var scrollTo = scrollToAll.get(0);
				 scrollTo = $(scrollTo);//.parent();
				 if(scrollTo) {
					 container.scrollTop(scrollTo.offset().top-container.offset().top+container.scrollTop());
				 }			
			 }
		 }else{
		     container.scrollTop(0);
		 }
	 });
	 
    $(".letterContainer").find('.locationSearchByAutoVersion').live('keyup',function(){
	    var wordText = $.trim($(this).val().toUpperCase());
	 	var container =  $(this).parents('.letterContainer').find('.letterContentContainer');
	 	
	 	
	 	$(this).parents('.letterContainer').find('.letterContentContainer').find('.autoVersionIdLabel').each(function(){
	 		$(this).removeClass("label label-info");
	 	});
		
	 	if(wordText != ""){
		  	var scrollToAll = $(this).parents('.letterContainer').find('.letterContentContainer').find('.autoVersionIdLabel').filter(function(){
		  		var text = $.trim($(this).text()).toUpperCase();
		  		var letter = $.trim($(this).attr("letter")).toUpperCase();
		  		if(text.indexOf(wordText) != -1 || letter == wordText){
		  			$(this).addClass("label label-info");
		  			return true;
		  		}else{
		  			return false;
		  		}			
			});
			if(scrollToAll.length>0){
				var scrollTo = scrollToAll.get(0);
				scrollTo = $(scrollTo);//.parent();
				if(scrollTo)			
					container.scrollTop(scrollTo.offset().top-container.offset().top+container.scrollTop());
			}
		}else{
			container.scrollTop(0);
		}
	});
	
		
	/**城市选择初如化------------开始*/
	$(".cityModalContainer").find('.cityModalByAll').live('click',function(){
		if($(this).attr("checked")){
			$(this).parents('.cityModalContainer').find(".modal-body input").each(function(index){
		 		$(this).attr("checked",'true');//全选
		 	});
		}else{
			$(this).parents('.cityModalContainer').find(".modal-body input").each(function(index){
		 		 $(this).prop("checked",false);//取消全选
		 	});
		} 
	});
	
	/**区域点击事件*/
	$(".cityModalContainer").find('.cityModalByArea').live('click',function(){
		if($(this).attr("checked")){
			$(this).parent().parent().parent().find(".cityModalByCity").each(function(index){
				$(this).attr("checked",'true');//行全选
			});
		}else{
			$(this).parent().parent().parent().find(".cityModalByCity").each(function(index){
				$(this).prop("checked",false);//取消行全选
			});
		}
		
		var allCityArr = $(this).parents(".cityModalContainer .selectorTable").find(".cityModalByCity");
		var allSelectedCityArr = $(this).parents(".cityModalContainer .selectorTable").find(".cityModalByCity:checked");
		if(allCityArr.length == allSelectedCityArr.length){
			$(this).parents('.cityModalContainer').find('.cityModalByAll').attr("checked",'true');//全部全选		
		}else{
			$(this).parents('.cityModalContainer').find('.cityModalByAll').prop("checked",false);//全部取消
		}
	});
	
	/**城市点击事件*/
	$(".cityModalContainer").find('.modal-body .cityModalByCity').live('click',function(){
		//行的区域是否选中
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
		
		var allCityArr = $(this).parents(".cityModalContainer .selectorTable").find(".cityModalByCity");
		var allSelectedCityArr = $(this).parents(".cityModalContainer .selectorTable").find(".cityModalByCity:checked");
		if(allCityArr.length == allSelectedCityArr.length){
			$(this).parents('.cityModalContainer').find('.cityModalByAll').attr("checked",'true');//全部全选		
		}else{
			$(this).parents('.cityModalContainer').find('.cityModalByAll').prop("checked",false);//全部取消
		}
	});
	/**点击确定生成内容*/
	$(".cityModalContainer").find('.confirm').on('click',function(){
		var containerId = $(this).parents(".cityModalContainer").attr("id");
		var relContainer = $(this).attr("relContainer");
		var relInputName = $(this).attr("relInputName");
		$("#"+relContainer).html("");
		var strHTML = "";
		strHTML += '<ul class="selectorResultContainer">';
		//如果城市全部选中，则生成全国均价
		if($("#myModalLabel :checkbox").attr("checked"))
		{
			strHTML += '<li>';
			strHTML += '<div class="removeBtn" relContainer="cityModal" value="0" style="cursor:pointer;margin-top: 5px;" title="全国均价">';
			strHTML += '<input type="hidden" value="0" name="selectedCity" />';
			strHTML += '全国均价<i class="icon-remove" style="visibility: hidden;"></i>';
			strHTML += '</div>';
			strHTML += '</li>';
		}
		else
		{
			$(this).parents(".cityModalContainer").find('.cityModalByCity:checked').each(function(){
				strHTML += '<li>';
				strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+$(this).val()+'" style="cursor:pointer" title="删除：'+$.trim($(this).parent().text())+'">';
				strHTML += '<input type="hidden" value="'+$(this).val()+'" name="'+relInputName+'" />';
				strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
				strHTML += '</div>';
				strHTML += '</li>';
			});
		}
		strHTML += '</ul>';
		$("#"+relContainer).html(strHTML);
	});
	
	/** 删除*/
	$(".selectorResultContainer .removeBtn").live('click',function(){
		
		var relContainer = $(this).attr("relContainer");
		var currVal = $(this).attr('value');
		//各模块有全选的value都给为-1
		if(currVal == -1){
			$(this).parent().parent().parent().html("");
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
	
	/**城市控件值鼠标经过事件*/
	$("#cityModalResultContainer ul div").live('mouseover',function(){
		$(this).find(".icon-remove").css({visibility:'visible'});
	});
	
	/** 城市控件值鼠标离开事件*/
	$("#cityModalResultContainer ul div").live('mouseout',function(){
		$(this).find(".icon-remove").css({visibility:'hidden'});
	});
	
	
	/**城市选择初如化------------开始*/
	
	/**子车型如化------------开始*/
	$(".subModelModalContainer .subModelIdInput").live('click',function(){
		//设置联动选择，当某个车型被选中时，其它标签相关的一样的车型也会选中。 -----开始			
		if($(this).attr("checked")){
			if($(this).attr('type') == 'radio'){
				/*$(this).parents('.subModelModalContainer').find('.subModelIdInput:checked').each(function(){
					$(this).removeAttr("checked");//取消选中
				});*/
			}else{
			var currVal = $(this).val();
			$(this).parents('.subModelModalContainer').find('.subModelIdInput').filter(function(){
				return $(this).val() == currVal;
			}).each(function(){
				$(this).attr("checked",'true');
			});
			}
		}else{
			var currVal = $(this).val();
			$(this).parents('.subModelModalContainer').find('.subModelIdInput').filter(function(){
				return $(this).val() == currVal;
			}).each(function(){
				$(this).prop("checked",false);//取消选中
			});
		}
		//设置联动选择，当某个车型被选中时，其它标签相关的一样的车型也会选中。 -----结束
		
		addResultContainerBySubModel();
		
	});
	
	$(".subModelModalContainer .resultShowContent .removeBtnByResult").live('click',function(){
		var currVal = $(this).attr("subModelId");
		$(this).parents('.subModelModalContainer').find('.subModelIdInput').filter(function(){
			return $(this).val() == currVal;
		}).each(function(){
			$(this).prop("checked",false);//取消选中
			
			var ths = $(this);
			//非本竞品页的父级也取消选中
			if($("#choseType").val() != 1){
				ths.closest("td").prev().find("input").prop("checked",false);
			}
			//车型全选框也取消选中
			$("#modelAll").prop("checked",false);
			
		});;	
		$(this).parent().remove();
	});
	
	/**子车型选择确定按扭---开始*/
	$(".subModelModalContainer").find('.confirm').bind('click',function(event){
		//event.stopPropagation();
		var containerId = $(this).parents(".subModelModalContainer").attr("id");
		var relContainer = $(this).attr("relContainer");
		var relInputName = $(this).attr("relInputName");
		/**
		 * --start常用对象组互斥
		 * 部分模块存在除车型,型号对象之外还存在常用组对象
		 * 常用组与车型,型号对象互斥,只能存在一个对象有值
		 */
		var subModelLength = $(this).parents(".subModelModalContainer").find('.resultShowContent').find('.removeBtnByResult').length;
		if(subModelLength > 0){
			var autoVersionResult = $("#autoVersionModalResultContainer");
			if(autoVersionResult.length > 0)$("#autoVersionModalResultContainer").html("");
		}
		/**--end常用对象组互斥**/
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
		  		strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+allSubModelArr[i].subModelId+'" style="padding-top: 6px;cursor:pointer" title="删除：'+allSubModelArr[i].subModelName+'">';
			  		strHTML += '<input type="hidden" letter="'+allSubModelArr[i].letter+'" pooAttributeId="'+allSubModelArr[i].pooAttributeId+'" subModelName="'+allSubModelArr[i].subModelName+'" value="'+allSubModelArr[i].subModelId+'" name="'+relInputName+'" />';
			  		strHTML += allSubModelArr[i].subModelName + '<i class="icon-remove" style="visibility: hidden;"></i>';
		  		strHTML += '</div>';
	  		strHTML += '</li>';
		 }
		strHTML += '</ul>';
		$("#"+relContainer).html(strHTML);
		//清空型号
		clearVersionByModelChange();
		 
	});
	
	/** 子车型控件值鼠标经过事件*/
	$("#subModelModalResultContainer ul div").live('mouseover',function(){
		$(this).find(".icon-remove").css({visibility:'visible'});
	});
	
	/**子车型控件值鼠标离开事件*/
	$("#subModelModalResultContainer ul div").live('mouseout',function(){
		$(this).find(".icon-remove").css({visibility:'hidden'});
	});
	/**子车型选择确定按扭---结束*/
	
	/** 产地属性-----开始*/
	$(".subModelModalContainer").find(".pooAttributeIdInput").live("click",function(event){
		
		var pooAttributeIdArr = [];
		$(".subModelModalContainer").find(".pooAttributeIdInput").each(function(){
			if($(this).prop("checked")){		
				pooAttributeIdArr[pooAttributeIdArr.length] = $(this).val();
			}
		});	

		var ob=$(this);
		//品牌
		if($("#choseType").val()==3){
			ob.parents(".subModelModalContainer").find("#tabs-brand").find(".subModelIdInput").each(function(){
				var obj=$(this);
				var flag = false;
				for(var i=0;i<pooAttributeIdArr.length;i++){
					if(pooAttributeIdArr[i] == $(this).attr("pooAttributeId")){
						flag = true;
						break;
					}
				}
				if(flag){
					obj.parent().show();
				}else{
					if(obj.prop("checked")){
						//obj.click();
						obj.prop("checked",false);
						if(obj.parent().parent().parent().prev("td").find(":checkbox").prop("checked")){
							obj.parent().parent().parent().prev("td").find(":checkbox").prop("checked",false);
						}
						
					}
					obj.parent().hide();
				}
			});
			$("#subModelModal .resultShowContainer .resultShowContent").html("");//先清空再重新放上去
			addResultContainerBySubModelByBrand();//把选中的放到车型选择下面的容器中
			
			//判断车型全选框是不是要勾选
			var num = 0;//用来统计本竞品页显示车型的数量
			$("#tabs-brand .selectorContentTd").find("input").each(function(){
				var ths = $(this);
				/*if(ths.parent().attr("style") == undefined){
					ths.parent().attr("style","");
				}*/
				if(ths.parent().css("display") != "none"){
					num++;
				}
			});
			if(num == ($("#tabs-brand .selectorContentTd input:checked").length) && (num != 0)){
				$("#modelAll").attr("checked","checked");
			} else{
				$("#modelAll").prop("checked",false);
			}
			
		}else if($("#choseType").val()==2){
			//细分市场
			ob.parents(".subModelModalContainer").find("#tabs-segment").find(".subModelIdInput").each(function(){
				var obj=$(this);
				var flag = false;
				for(var i=0;i<pooAttributeIdArr.length;i++){
					if(pooAttributeIdArr[i] == $(this).attr("pooAttributeId")){
						flag = true;
						break;
					}
				}
				
				if(flag){
					obj.parent().show();
					
				}else{
					if(obj.prop("checked")){
						//obj.click();
						obj.prop("checked",false);
						if(obj.parent().parent().parent().prev("td").find(":checkbox").prop("checked")){
							obj.parent().parent().parent().prev("td").find(":checkbox").prop("checked",false);
						}
					}
					obj.parent().hide();
				}
			});
			$("#subModelModal .resultShowContainer .resultShowContent").html("");//先清空再重新放上去
			addResultContainerBySubModelBySegment();//把选中的放到车型选择下面的容器中
			
			//判断车型全选框是不是要勾选
			var num = 0;//用来统计本竞品页显示车型的数量
			$("#tabs-segment .selectorContentTd").find("input").each(function(){
				var ths = $(this);
				/*if(ths.parent().attr("style") == undefined){
					ths.parent().attr("style","");
				}*/
				if(ths.parent().css("display") != "none"){
					num++;
				}
			});
			if(num == ($("#tabs-segment .selectorContentTd input:checked").length) && (num != 0)){
				$("#modelAll").attr("checked","checked");
			} else{
				$("#modelAll").prop("checked",false);
			}
			
		}else if($("#choseType").val()==4){
			//厂商、
			ob.parents(".subModelModalContainer").find("#tabs-manf").find(".subModelIdInput").each(function(){
				var obj=$(this);
				var flag = false;
				for(var i=0;i<pooAttributeIdArr.length;i++){
					if(pooAttributeIdArr[i] == $(this).attr("pooAttributeId")){
						flag = true;
						break;
					}
				}
				
				if(flag){
					obj.parent().show();
				}else{
					if(obj.prop("checked")){
						//obj.click();
						obj.prop("checked",false);
						if(obj.parent().parent().parent().prev("td").find(":checkbox").prop("checked")){
							obj.parent().parent().parent().prev("td").find(":checkbox").prop("checked",false);
						}
					}
					obj.parent().hide();
				}
			});
			$("#subModelModal .resultShowContainer .resultShowContent").html("");//先清空再重新放上去
			addResultContainerBySubModelByManf();//把选中的放到车型选择下面的容器中
			
			//判断车型全选框是不是要勾选
			var num = 0;//用来统计本竞品页显示车型的数量
			$("#tabs-manf .selectorContentTd").find("input").each(function(){
				var ths = $(this);
				/*if(ths.parent().attr("style") == undefined){
					ths.parent().attr("style","");
				}*/
				if(ths.parent().css("display") != "none"){
					num++;
				}
			});
			if(num == ($("#tabs-manf .selectorContentTd input:checked").length) && (num != 0)){
				$("#modelAll").attr("checked","checked");
			} else{
				$("#modelAll").prop("checked",false);
			}
			
		}else{
			//本竞品
			ob.parents(".subModelModalContainer").find("#tabs-competingProducts").find(".subModelIdInput").each(function(){
				var obj=$(this);
				var flag = false;
				for(var i=0;i<pooAttributeIdArr.length;i++){
					if(pooAttributeIdArr[i] == $(this).attr("pooAttributeId")){
						flag = true;
						break;
					}
				}
				
				if(flag){
					obj.parent().show();
				}else{
					if(obj.prop("checked")){
						
						obj.parents(".subModelModalContainer").find(".subModelIdInput").filter(function(){
							return $(this).val() == obj.val();
						}).each(function(){
							$(this).prop("checked",false);
						});
						
					}
					obj.parent().hide();
				}
			});
			$("#subModelModal .resultShowContainer .resultShowContent").html("");//先清空再重新放上去
			addResultContainerBySubModel();//把选中的放到车型选择下面的容器中
			var choseType = $("#choseType").val();
			//判断车型全选框是不是要勾选
			var num = 0;//用来统计本竞品页显示车型的数量
			$("#tabs-competingProducts").find("input").each(function(){
				var ths = $(this);
				/*if(ths.parent().attr("style") == undefined){
					ths.parent().attr("style","");
				}*/
				if(ths.parent().css("display") != "none"){
					num++;
				}
			});
			if(num == ($("#tabs-competingProducts input:checked").length) && (num != 0)){
				$("#modelAll").attr("checked","checked");
			} else{
				$("#modelAll").prop("checked",false);
			}
			
		}
		
		//中小类全选检查情况
		checkAll($("#choseType").val());
		/**中级全选检查打钩情况**/
		 checkSelectorHeadTdTrAll($("#choseType").val());
		});

	/**产地属性-----结束	*/
	
	/**型号---开始*/
	$(".versionModalContainer").find('.selectVersionAll').live('click',function(event){
		if($(this).attr("checked")){
			var currModelId = $(this).val();
			$(this).parents(".versionModalContainer").find(".versionIdInput").each(function(){
				var subModelId =  $(this).attr("subModelId");
				if(currModelId == subModelId){
					$(this).attr("checked",'true');
				}
			});
		}else{
			var currModelId = $(this).val();
			$(this).parents(".versionModalContainer").find(".versionIdInput").each(function(){
				var subModelId =  $(this).attr("subModelId");
				if(currModelId == subModelId){
					$(this).prop("checked",false);//取消选中
				}
			});
		}
	});
	
	/**型号选择确定按扭---开始*/
	$(".versionModalContainer").find('.confirm').live('click',function(event){
		event.stopPropagation();
		var containerId = $(this).parents(".versionModalContainer").attr("id");
		var relContainer = $(this).attr("relContainer");
		var relInputName = $(this).attr("relInputName");
	
		$("#"+relContainer).html("");
		var strHTML = "";
		strHTML += '<ul class="selectorResultContainer">';
		$(this).parents(".versionModalContainer").find('.versionIdInput:checked').each(function(){
			strHTML += '<li>';
		  		strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+$(this).val()+'" style="padding-top: 6px;cursor:pointer" title="删除：'+$.trim($(this).parent().text())+'">';
		  		strHTML += '<input type="hidden" value="'+$(this).val()+'" name="'+relInputName+'" />';
		  		strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
	  			strHTML += '</div>';
	  		strHTML += '</li>';
		});
		strHTML += '</ul>';
		$("#"+relContainer).html(strHTML);
	});
	
	/**型号控件值鼠标经过事件*/
	$("#versionModalResultContainer ul div").live('mouseover',function(){
		$(this).find(".icon-remove").css({visibility:'visible'});
	});
	
	/** 型号控件值鼠标离开事件*/
	$("#versionModalResultContainer ul div").live('mouseout',function(){
		$(this).find(".icon-remove").css({visibility:'hidden'});
	});
	
	/**型号选择确定按扭---结束	*/
	$(".versionModalContainer").find('.versionIdInput').live('click',function(event){
		
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
				$(this).prop("checked",false);//取消选中
			}
		});
		
	});
	/**型号---结束*/
	
	/**细分市场弹出框------------开始*/
	$(".segmentModalContainer").find('.segmentModalByAll').live('click',function(){
		if($(this).attr("checked")){
			$(this).parents('.segmentModalContainer').find(".modal-body input").each(function(index){
		 		$(this).attr("checked",'true');//全选
		 	});
		}else{
			$(this).parents('.segmentModalContainer').find(".modal-body input").each(function(index){
		 		 $(this).prop("checked",false);//取消全选
		 	});
		} 
	});
	
	/**一级细分市场点击事件*/
	$(".segmentModalContainer").find('.segmentModalByLevel1').live('click',function(){
		if($(this).attr("checked")){
			$(this).parent().parent().parent().find(".segmentModalByLevel2").each(function(index){
				$(this).attr("checked",'true');//行全选
			});
		}else{
			$(this).parent().parent().parent().find(".segmentModalByLevel2").each(function(index){
				$(this).prop("checked",false);//取消行全选
			});
		}
		
		var allCityArr = $(this).parents(".segmentModalContainer").find(".segmentModalByLevel2");
		var allSelectedCityArr = $(this).parents(".segmentModalContainer").find(".segmentModalByLevel2:checked");
		if(allCityArr.length == allSelectedCityArr.length){
			$(this).parents('.segmentModalContainer').find('.segmentModalByAll').attr("checked",'true');//全部全选		
		}else{
			$(this).parents('.segmentModalContainer').find('.segmentModalByAll').prop("checked",false);//全部取消
		}
	});
	
	/**二级细分市场点击事件*/
	$(".segmentModalContainer").find('.segmentModalByLevel2').live('click',function(){
		//行的区域是否选中
		var allArr = $(this).parent().parent().parent().find(".segmentModalByLevel2");
		var selectedArr = $(this).parent().parent().parent().find("input:checked");
		if(allArr.length == selectedArr.length){
			$(this).parent().parent().parent().parent().find('.segmentModalByLevel1').each(function(){
				$(this).attr("checked",'true');//行全选
			});
		}else{
			$(this).parent().parent().parent().parent().find('.segmentModalByLevel1').each(function(){
				$(this).prop("checked",false);//取消行全选
			});
		}
		
		var allCityArr = $(this).parents(".segmentModalContainer").find(".segmentModalByLevel2");
		var allSelectedCityArr = $(this).parents(".segmentModalContainer").find(".segmentModalByLevel2:checked");
		if(allCityArr.length == allSelectedCityArr.length){
			$(this).parents('.segmentModalContainer').find('.segmentModalByAll').attr("checked",'true');//全部全选		
		}else{
			$(this).parents('.segmentModalContainer').find('.segmentModalByAll').prop("checked",false);//全部取消
		}
	});
	/**点击确定生成内容*/
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
				if(level2RowTotalAmount == level2RowSelectedAmount){
					$(this).find(".segmentModalByLevel1").each(function() {
						strHTML += '<li>';
						strHTML += '<div class="removeBtn" relContainer="' + containerId + '" value="' + $(this).val() + '" style="cursor:pointer" title="删除：' + $.trim($(this).parent().text()) + '">';
						strHTML += '<input type="hidden" value="' + $(this).val() + '" name="' + relInputName + '" levelType="1" />';
						strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
						strHTML += '</div>';
						strHTML += '</li>';
					});
				} else {
					$(this).find(".segmentModalByLevel2:checked").each(function() {
						strHTML += '<li>';
						strHTML += '<div class="removeBtn" relContainer="' + containerId + '" value="' + $(this).val() + '" style="cursor:pointer" title="删除：' + $.trim($(this).parent().text()) + '">';
						strHTML += '<input type="hidden" value="' + $(this).val() + '" name="' + relInputName + '"  levelType="2" />';
						strHTML += $.trim($(this).parent().text()) + '<i class="icon-remove" style="visibility: hidden;"></i>';
						strHTML += '</div>';
						strHTML += '</li>';
					});
				}
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
	
	/**点击关闭时，还原选择项**/
	$(".segmentModalContainer .close").live("click", function() {
		returnSegmentType();
	});
	
	/**还原细分市场大类别勾选情况**/
	function returnSegmentType() {
		var choosedSegmentType = $("#choosedSegmentType").val().split(",");
		var notChoosed = new Array();
		var count = 0;
		$(".segmentType").each(function() {
			var flag = false;
			for(var i = 0 ; i < choosedSegmentType.length; i++) {
				var segment = $(this);
				if(segment.val() == choosedSegmentType[i]) {
					flag = true;
					if(!segment.prop("checked")) {
					    segment.click();
					    break;
					}
				}
			}
			if(!flag) {
				notChoosed[count] = segment;
				count++;
			}
		});
		
		for(var i = 0; i < notChoosed.length; i++) {
			var obj = notChoosed[i];
			if($(obj).prop("checked")) {
				$(obj).click();
			}
		}
	}
	
	/** 删除*/
	$(".selectorResultContainer .removeBtn").live('click',function(){
		var relContainer = $(this).attr("relContainer");
		var currVal = $(this).attr('value');
		$('#'+relContainer).find('.segmentModalByLevel2').each(function(){
			if($.trim($(this).val()) == $.trim(currVal)){
				$(this).removeAttr("checked");
				
				var allArr = $(this).parent().parent().parent().find(".segmentModalByLevel2");
				var selectedArr = $(this).parent().parent().parent().find("input:checked");
				if(allArr.length == selectedArr.length){
					$(this).parent().parent().parent().parent().find('.segmentModalByLevel1').each(function(){
						$(this).attr("checked",'true');//行全选
					});
				}else{
					$(this).parent().parent().parent().parent().find('.segmentModalByLevel1').each(function(){
						$(this).removeAttr("checked");//取消行全选
					});
				}
				
				var allCityArr = $(this).parents(".segmentModalContainer").find(".segmentModalByLevel2");
				var allSelectedCityArr = $(this).parents(".segmentModalContainer").find(".segmentModalByLevel2:checked");
				if(allCityArr.length == allSelectedCityArr.length){
					$(this).parents('.segmentModalContainer').find('.segmentModalByAll').attr("checked",'true');//全部全选		
				}else{
					$(this).parents('.segmentModalContainer').find('.segmentModalByAll').removeAttr("checked");//全部取消
				}
			}
		});
		
		$(this).parent().remove();
		clearVersionByModelChange();
	});
	
	/** 细分市场控件值鼠标经过事件*/
	$(".segmentModalResultContainer ul div").live('mouseover',function(){
		$(this).find(".icon-remove").css({visibility:'visible'});
	});
	
	/** 细分市场控件值鼠标离开事件*/
	$(".segmentModalResultContainer ul div").live('mouseout',function(){
		$(this).find(".icon-remove").css({visibility:'hidden'});
	});
	
	
	/**细分市场弹出框------------结束*/
	
	
	
	
	/**厂商选择确定按扭---开始*/
	$(".manfModalContainer").find('.confirm').live('click',function(event){
		//event.stopPropagation();
		var containerId = $(this).parents(".manfModalContainer").attr("id");
		var relInputName = $(this).attr("relInputName");
		
		var allArr = [];
		$(this).parents(".manfModalContainer").find('.resultShowContent').find('.removeBtnByResult').each(function(){
			var obj = {};
			obj.manfId =  $(this).attr("manfId");
			obj.manfName =  $(this).attr("manfName");
			obj.letter =  $(this).attr("letter");
			allArr[allArr.length] = obj;
		});
		var strHTML = "";
		strHTML += '<ul class="selectorResultContainer">';
		for(var i=0;i<allArr.length;i++){
			strHTML += '<li>';
		  		strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+allArr[i].subModelId+'" style="cursor:pointer" title="删除：'+allArr[i].manfName+'">';
			  		strHTML += '<input type="hidden" letter="'+allArr[i].letter+'" manfName="'+allArr[i].manfName+'" value="'+allArr[i].manfId+'" name="'+relInputName+'" />';
			  		strHTML += allArr[i].manfName + '<i class="icon-remove" style="visibility: hidden;"></i>';
		  		strHTML += '</div>';
	  		strHTML += '</li>';
		 }
		strHTML += '</ul>';
		$(currManfLI).find('.manfModalResultContainer').html(strHTML);
	});
	
	/**厂商控件值鼠标经过事件*/
	$(".manfModalResultContainer ul div").live('mouseover',function(){
		$(this).find(".icon-remove").css({visibility:'visible'});
	});
	
	/** 厂商控件值鼠标离开事件*/
	$(".manfModalResultContainer ul div").live('mouseout',function(){
		$(this).find(".icon-remove").css({visibility:'hidden'});
	});
	
	$(".manfModalContainer .resultShowContent .removeBtnByResult").live('click',function(){
		var currVal = $(this).attr("manfId");
		$(this).parents('.manfModalContainer').find('.manfIdInput').filter(function(){
			return $(this).val() == currVal;
		}).each(function(){
			$(this).removeAttr("checked");//取消选中
		});;	
		$(this).parent().remove();
	});
	
	/**厂商选择确定按扭---结束*/
	
	
	/**品牌选择确定按扭---开始*/
	$(".brandModalContainer").find('.confirm').live('click',function(event){
		var containerId = $(this).parents(".brandModalContainer").attr("id");
		var relInputName = $(this).attr("relInputName");
		var allArr = [];
		$(this).parents(".brandModalContainer").find('.resultShowContent').find('.removeBtnByResult').each(function(){
			var obj = {};
			obj.brandId =  $(this).attr("brandId");
			obj.brandName =  $(this).attr("brandName");
			obj.letter =  $(this).attr("letter");
			allArr[allArr.length] = obj;
		});
		var strHTML = '<ul class="selectorResultContainer">';
			for(var i=0;i<allArr.length;i++){
				strHTML += '<li>';
			  	strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+allArr[i].subModelId+'" style="cursor:pointer" title="删除：'+allArr[i].brandName+'">';
				strHTML += '<input type="hidden" letter="'+allArr[i].letter+'" brandName="'+allArr[i].brandName+'" value="'+allArr[i].brandId+'" name="'+relInputName+'" />';
				strHTML += allArr[i].brandName + '<i class="icon-remove" style="visibility: hidden;"></i>';
			  	strHTML += '</div>';
		  		strHTML += '</li>';
			 }
		strHTML += '</ul>';
		$(currBrandLI).find('.brandModalResultContainer').html(strHTML);
	});

	/** 品牌控件值鼠标经过事件*/
	$(".brandModalResultContainer ul div").live('mouseover',function(){
		$(this).find(".icon-remove").css({visibility:'visible'});
	});
	
	/** 品牌控件值鼠标离开事件*/
	$(".brandModalResultContainer ul div").live('mouseout',function(){
		$(this).find(".icon-remove").css({visibility:'hidden'});
	});
	
	$(".brandModalContainer .resultShowContent .removeBtnByResult").live('click',function(){
		var currVal = $(this).attr("brandId");
		$(this).parents('.brandModalContainer').find('.brandIdInput').filter(function(){
			return $(this).val() == currVal;
		}).each(function(){
			$(this).removeAttr("checked");//取消选中
		});	
		$(this).parent().remove();
	});
	
	/**品牌选择确定按扭---结束*/
	
	/**车身形式-开始*/
	$(".bodyTypeModalContainer").find('.bodyTypeModalByAll').live('click',function(){
		if($(this).attr("checked")){
			$(this).parents('.bodyTypeModalContainer').find(".modal-body input").each(function(index){
		 		$(this).attr("checked",'true');//全选
		 	});
		}else{
			$(this).parents('.bodyTypeModalContainer').find(".modal-body input").each(function(index){
		 		 $(this).removeAttr("checked");//取消全选
		 	});
		} 
	});
	
	/**车身形式点击*/
	$(".bodyTypeModalContainer").find('.bodyTypeModal').live('click',function(){
		
		var allArr = $(this).parents(".bodyTypeModalContainer").find(".bodyTypeModal");
		var allSelectedArr = $(this).parents(".bodyTypeModalContainer").find(".bodyTypeModal:checked");
		if(allArr.length == allSelectedArr.length){
			$(this).parents('.bodyTypeModalContainer').find('.bodyTypeModalByAll').attr("checked",'true');//全部全选		
		}else{
			$(this).parents('.bodyTypeModalContainer').find('.bodyTypeModalByAll').removeAttr("checked");//全部取消
		}
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
		$(currBodyTypeLI).html(strHTML);
	});
	
	/** 车身形式控件值鼠标经过事件*/
	$(".bodyTypeModalResultContainer ul div").live('mouseover',function(){
		$(this).find(".icon-remove").css({visibility:'visible'});
	});
	
	/** 车身形式控件值鼠标离开事件*/
	$(".bodyTypeModalResultContainer ul div").live('mouseout',function(){
		$(this).find(".icon-remove").css({visibility:'hidden'});
	});
	
	/** 车身形式-结束*/
	
	/**车系-开始*/
	$(".origModalContainer").find('.origModalByAll').live('click',function(){
		if($(this).attr("checked")){
			$(this).parents('.origModalContainer').find(".modal-body input").each(function(index){
		 		$(this).attr("checked",'true');//全选
		 	});
		}else{
			$(this).parents('.origModalContainer').find(".modal-body input").each(function(index){
		 		 $(this).removeAttr("checked");//取消全选
		 	});
		} 
	});
	
	/**车系点击*/
	$(".origModalContainer").find('.origModal').live('click',function(){
		var allArr = $(this).parents(".origModalContainer").find(".origModal");
		var allSelectedArr = $(this).parents(".origModalContainer").find(".origModal:checked");
		if(allArr.length == allSelectedArr.length){
			$(this).parents('.origModalContainer').find('.origModalByAll').attr("checked",'true');//全部全选		
		}else{
			$(this).parents('.origModalContainer').find('.origModalByAll').removeAttr("checked");//全部取消
		}
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
			$(currOrigLI).find("tbody td input[name='obj_Split']").attr('checked',false);
			$(currOrigLI).find("tbody td input[name='obj_Split']").attr('disabled',true);
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
			$(currOrigLI).find("tbody td input[name='obj_Split']").attr('disabled',false);
		}
		strHTML += '</ul>';
		$(currOrigLI).find(".origModalResultContainer").html(strHTML);
	});
	
	/** 车系控件值鼠标经过事件*/
	$(".origModalResultContainer ul div").live('mouseover',function(){
		$(this).find(".icon-remove").css({visibility:'visible'});
	});
	
	/** 车系控件值鼠标离开事件*/
	$(".origModalResultContainer ul div").live('mouseout',function(){
		$(this).find(".icon-remove").css({visibility:'hidden'});
	});
	
	/** 车系-结束*/
	
}) ;

/**一汽大众常用型号组---开始*/
	$(".autoVersionModalContainer").find('.selectAutoVersionAll').live('click',function(event){
		if($(this).attr("checked")){
			var currObjectGroupId = $(this).val();
			$(this).parents(".autoVersionModalContainer").find(".autoVersionIdInput").each(function(){
				var objectGroup =  $(this).attr("objectGroup");
				if(currObjectGroupId == objectGroup){
					$(this).attr("checked",'true');
				}
			});
		}else{
			var currObjectGroupId = $(this).val();
			$(this).parents(".autoVersionModalContainer").find(".autoVersionIdInput").each(function(){
				var objectGroup =  $(this).attr("objectGroup");
				if(currObjectGroupId == objectGroup){
					$(this).removeAttr("checked");//取消选中
				}
			});
		}
	});
	
	/**一汽大众常用型号组选择确定按扭---开始*/
	$(".autoVersionModalContainer").find('.confirm').live('click',function(event){
		event.stopPropagation();
		
		var containerId = $(this).parents(".autoVersionModalContainer").attr("id");
		var relContainer = $(this).attr("relContainer");
		var relInputName = $(this).attr("relInputName");
		
		/**
		 * --start常用对象组互斥
		 * 部分模块存在除车型,型号对象之外还存在常用组对象
		 * 常用组与车型,型号对象互斥,只能存在一个对象有值
		 */
		var autoVersionLength = $(this).parents(".autoVersionModalContainer").find('.autoVersionIdInput:checked').length;
		if(autoVersionLength > 0){
			var subModelResult = $("#subModelModalResultContainer");
			var versionsResult = $("#versionModalResultContainer");
			var versionBody = $("#versionModalBody");
			
			if(subModelResult.length > 0)$("#subModelModalResultContainer").html("");
			if(versionsResult.length > 0)$("#versionModalResultContainer").html("");
			if(versionBody.length > 0)$("#versionModalBody").html("");
		}
		/**--end常用对象组互斥**/
	
		$("#"+relContainer).html("");
		var strHTML = "";
		strHTML += '<ul class="selectorResultContainer">';
		$(this).parents(".autoVersionModalContainer").find('.autoVersionIdInput:checked').each(function(){
			strHTML += '<li>';
		  		strHTML += '<div class="removeBtn" relContainer="'+containerId+'" value="'+$(this).val()+'" style="padding-top: 6px;cursor:pointer" title="删除：'+$.trim($(this).parent().text())+'">';
		  		strHTML += '<input type="hidden" value="'+$(this).val()+'" name="'+relInputName+'" />';
		  		strHTML += $.trim($(this).parent().find("input").attr("text")) + '<i class="icon-remove" style="visibility: hidden;"></i>';
	  			strHTML += '</div>';
	  		strHTML += '</li>';
		});
		strHTML += '</ul>';
		$("#"+relContainer).html(strHTML);
	});
	
	/**一汽大众常用型号组控件值鼠标经过事件*/
	$("#autoVersionModalResultContainer ul div").live('mouseover',function(){
		$(this).find(".icon-remove").css({visibility:'visible'});
	});
	
	/**一汽大众常用型号组控件值鼠标离开事件*/
	$("#autoVersionModalResultContainer ul div").live('mouseout',function(){
		$(this).find(".icon-remove").css({visibility:'hidden'});
	});
	
	/**一汽大众常用型号组选择确定按扭---结束	*/
	
	$(".autoVersionModalContainer").find('.autoVersionIdInput').live('click',function(event){
		
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
		
	});
	
/**一汽大众常用型号组---结束*/

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
	
function addResultContainerBySubModel(){
	//显示选中的值	——开始
	//去掉重复选项
	$("#selectorResultContainerBySubModel").html();
	var allSubModelArr = [];
	$(".subModelModalContainer").find('.subModelIdInput:checked').each(function(){
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

function addResultContainerBySubModelByBrand(){
	//显示选中的值	——开始
	//去掉重复选项
	$("#selectorResultContainerBySubModel").html();
	var allSubModelArr = [];
	$(".subModelModalContainer #tabs-brand").find('.subModelIdInput:checked').each(function(){
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

function addResultContainerBySubModelBySegment(){
	//显示选中的值	——开始
	//去掉重复选项
	$("#selectorResultContainerBySubModel").html();
	var allSubModelArr = [];
	$(".subModelModalContainer #tabs-segment").find('.subModelIdInput:checked').each(function(){
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

function addResultContainerBySubModelByManf(){
	//显示选中的值	——开始
	//去掉重复选项
	$("#selectorResultContainerBySubModel").html();
	var allSubModelArr = [];
	$(".subModelModalContainer #tabs-manf").find('.subModelIdInput:checked').each(function(){
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

/** 改变车型时，清空不相连的型号*/
function clearVersionByModelChange()
{
	//车型名称
	var modelName = $("#subModelModalResultContainer li div").map(function(){return $(this).text();});
	//型号对象
	var versionObjs = $("#versionModalResultContainer li");
	var versionBody = $("#versionModalBody .letterContainer");
	if (0 == modelName.length) {
		$(versionObjs).remove();
		$(versionBody).remove();
	}
	else
	{
		if(0 != versionObjs.length)
		{
			$.each(versionObjs,function(i,n){
				var versionName = $(n).find("div").text();
				var delFlag = true;
				for(var j = 0; j < modelName.length; j++)
				{
					if(-1 != versionName.indexOf(modelName[j]))
					{
						delFlag = false;
						break;
					}
				}
				if(delFlag) $(n).remove();
			});
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
function getModelPage(id,type,inputType,timeType)
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
		$('#' + id).load(ctx+"/global/getSubmodelModal",params,function(){
			
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

/**
 * 展示页面
 * @param id 展示容器ID
 * @param type 子车型弹出框展示类型2：细分市场,3:品牌,4:厂商，为空则是本品与竟品
 * @param inputType 控件类型1：复选，2：单选;默认为1
 * @param timeType 时间类型1：时间点;2：时间段默认为2
 * 
 */
function getFawvwModelPage(id,type,inputType,timeType)
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
		$('#' + id).load(ctx+"/price/global/getSubmodelModal",params,function(){
			
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


/**把有全选的选项选上**/
function checkAll(type)
{
	if("2" == type){
		var allObj = $("#tabs-segment .selectorTypeTd");
		$(allObj).find(".selectSegmentAll").each(function(){
			
			var flag = true;
			var flag2 = true;//判断有没有显示的
			var modelObj = $(this);
			$(modelObj).closest("td").next().find(".subModelIdInput").each(function(){
				if($(this).parent().css("display") != "none"){
					flag2 = false;
					if(!$(this).prop("checked")){
						flag = false;
					}
				}
			});
			//如果一个细分市场下的车型被全选，则细分市场也选上
			if(flag == true){
				modelObj.prop("checked", true);
			} else{
				modelObj.prop("checked", false);
			}
			
			//全部隐藏则隐藏大类
			if(flag2 == true){
				if(modelObj.prop("checked", true)){
					modelObj.prop("checked", false);
				}
				modelObj.parent().parent().css("display","none");
			} else{
				modelObj.parent().parent().css("display","block");
			}
		});
	} else if("3" == type){
		var allObj = $("#tabs-brand .selectorTypeTd");
		$(allObj).find(".selectBrandAll").each(function(){
			var flag = true;
			var flag2 = true;//判断有没有显示的
			var modelObj = $(this);
			$(modelObj).closest("td").next().find(".subModelIdInput").each(function(){
				if($(this).parent().css("display") != "none"){
					flag2 = false;
					if(!$(this).prop("checked")){
						flag = false;
					}
				}
			});
			//如果一个品牌下的车型被全选，则品牌也选上
			if(flag == true){
				modelObj.prop("checked", true);
			} else{
				modelObj.prop("checked", false);
			}
			
			//全部隐藏则隐藏大类
			if(flag2 == true){
				if(modelObj.prop("checked", true)){
					modelObj.prop("checked", false);
				}
				modelObj.parent().parent().css("display","none")
			} else{
				modelObj.parent().parent().css("display","block");
			}
		});
	} else if("4" == type){
		var allObj = $("#tabs-manf .selectorTypeTd");
		$(allObj).find(".selectManfAll").each(function(){
			var flag = true;
			var flag2 = true;//判断有没有显示的
			var modelObj = $(this);
			$(modelObj).closest("td").next().find(".subModelIdInput").each(function(){
				if($(this).parent().css("display") != "none"){
					flag2 = false;
					if(!$(this).prop("checked")){
						flag = false;
					}
				}
			});
			//如果一个厂商下的车型被全选，则厂商也选上
			if(flag == true){
				modelObj.prop("checked", true);
			} else{
				modelObj.prop("checked", false);
			}
			
			//全部隐藏则隐藏大类
			if(flag2 == true){
				if(modelObj.prop("checked", true)){
					modelObj.prop("checked", false);
				}
				modelObj.parent().parent().css("display","none")
			} else{
				modelObj.parent().parent().css("display","block");
			}
		});
	} 
	
	
	
}

/**中级全选检查打钩情况**/
function checkSelectorHeadTdTrAll(type){

	var model = "";
	if("2" == type){
		model = "#tabs-segment";
	} else if("3" == type){
		model = "#tabs-brand";
	} else if("4" == type){
		model = "#tabs-manf";
	} else if("1" == type){
		return;
	}
	
		var allObj = $(model+" .selectorHeadTdTr");
		$(allObj).each(function(){
			var ths = $(this);
			var startTr = $(this);//点击input父级位置
			var endTr = $(this).nextAll('.selectorHeadTdTr:eq(0)').index();
			var trs = $(model+" .selectorTable tr").slice($(startTr).index()+1,endTr);//找到区间内的所有tr
			
			var showLength = $(trs).find(".selectorContentTd input").filter(function(){return $(this).parent().css("display") != "none"}).length;//显示的checkbox数量
			var showCheckedLength = $(trs).find(".selectorContentTd input").filter(function(){return $(this).prop("checked") == true }).length;//显示的打钩checkbox的数量
			if(showLength == showCheckedLength && showLength > 0){
				ths.find("input").prop("checked",true);
			} else{
				ths.find("input").prop("checked",false);
			}
		})
}

 