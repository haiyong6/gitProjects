$(document).ready(function(){ 
	//页面加载隐藏图表面板
	$(".tab-content").attr("style","display:none");
    /**
     * 展示配置弹出框
     */
	$('#configModal').on('show', function () {
		showLoading("configClassify");
		$('#configClassify').load(ctx+"/product/global/getConfigClassify",function(){
			$("#selConfigUl").html($("#configModalResultContainer ul").html().replace(/visibility: hidden;/g,"visibility: visible;"));
		});
	});
	
	/**
	 * 展示车型弹出框
	 */
	$('#subModelModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		//加载子车型数据
		showLoading("subModelModalBody");
		$('#subModelModalBody').load(ctx+"/product/global/getProductSubmodelModal",getParams(),function(){
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
	
	/**
	 * 获取页面请求参数
	 */
	function getParams()
	{
		var beginPrice = $("#beginPrice").val();//开始价格
		var endPrice = $("#endPrice").val();//结束价格
		var subModelBodyType = $("#subModelBodyType :checkbox:checked").map(function(){return $(this).val();}).get().join(",");//车身形式
		var mids = $("#subModelModalResultContainer input[name='selectedSubModel']").map(function(){return $(this).val();}).get().join(",");//车型ID
		var configs = $("#configModalResultContainer li div").map(function(){return $(this).attr("data-value");}).get().join("|");//配置数据
		
		var params = {};
		params.beginPrice = beginPrice;
		params.endPrice = endPrice;
		params.subModelBodyType = subModelBodyType;
		params.mids = mids;
		params.configs = configs;
		
		getQueryConditionAndBrowser(params);
		return params;
	}
	
	/**
	 * 获取页头查询条件，以及浏览器
	 */
	function getQueryConditionAndBrowser(paramObj)
	{
		paramObj.browser = navigator.appVersion;
		var queryCondition = "";
		var beginPrice = $("#beginPrice").val();//开始价格
		var endPrice = $("#endPrice").val();//结束价格
		
		queryCondition += "配置选择 = " + $("#configModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		queryCondition += "\n价格段 = " + beginPrice + "至" + endPrice + "万";
		queryCondition += "\n车身形式 = " + $("#subModelBodyType :checkbox:checked").map(function(){return $(this).attr("title");}).get().join(",");
		queryCondition += "\n车型 = " + $("#subModelModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		paramObj.queryCondition = queryCondition;
	}
	
	/**
	 * 确定查询
	 */
	$('#queryBtn').on('click', function (e) {
		
		if(0 == $("#configModalResultContainer li div").length)
		{
			alert("请选择配置");
			return;
		}
		
		$('body').showLoading();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/product/getConfigVersionResult",
			   data: getParams(),
			   success: function(data){
				   //查询面板折叠
				   $(".queryConditionContainer .buttons .toggle a").click();
				 //加载隐藏de图表面板
					$(".tab-content").attr("style","display:block;");
				   if(data) createConfigTable(data);
				   else 
				   {
					   $("#tSortable").hide();
					   $("#tableTitle").html("共记0款车装备了所选配置");
				   }
				   $('body').hideLoading();
			   },
			   error:function(){
				   $('body').hideLoading();
			   }
			});
	});
	
	/**
	 * 创建配置表格
	 */
	function createConfigTable(json)
	{
		$("#tSortable").show();
		var length = json.length;
		var tbodyHtml = "";
		for(var i = 0; i < length; i++)
		{
			var className = "";
			if(0 == i % 2) className = "odd";
			var obj = json[i];
			var mix = obj.mix;
			if("-" != mix) mix += "%";
			tbodyHtml += "<tr>";
			tbodyHtml += "<td style='width:8%' class='"+className+"'>"+obj.versionCode+"</td>";
			tbodyHtml += "<td style='width:8%' class='"+className+"'>"+obj.gradeName+"</td>";
			tbodyHtml += "<td style='width:8%' class='"+className+"'>"+obj.manfName+"</td>";
			tbodyHtml += "<td style='width:8%' class='"+className+"'>"+obj.subModelName+"</td>";
			tbodyHtml += "<td style='width:12%' class='"+className+"'>"+obj.versionName+"</td>";
			tbodyHtml += "<td style='width:8%' class='"+className+"'>"+obj.discharge+"</td>";
			tbodyHtml += "<td style='width:8%' class='"+className+"'>"+obj.gearMode+"</td>";
			tbodyHtml += "<td style='width:8%' class='"+className+"'>"+obj.bodyType+"</td>";
			tbodyHtml += "<td style='width:8%' class='"+className+"'>"+obj.versionLaunchDate+"</td>";
			tbodyHtml += "<td style='width:8%' class='"+className+"'>"+obj.modelYear+"</td>";
			tbodyHtml += "<td style='width:8%' class='"+className+"'>"+formatData(obj.msrp)+"</td>";
			tbodyHtml += "<td style='width:8%' class='"+className+"'>"+mix+"</td>";
			tbodyHtml += "</tr>";
		}
		$("#tSortable tbody").html(tbodyHtml);
		$("#tableTitle").html("共记" + length + "款车装备了所选配置");
	}
	
	/**
	 * 重置
	 */
	$('#resetBtn').on('click', function (e) {
		//城市容器
		$('#configModalResultContainer ul').html("");
		//车型容器
		$('#subModelModalResultContainer').html("");
		//型号容器
		$('#versionModalResultContainer').html("");
		
		$('#formId :reset');	
	});
});

/**
 * 校验文本框只能输入数据
 */
function checkText(event,obj)
{
	var keyCode = event.keyCode;
	if((keyCode < 48 || keyCode > 57) && (keyCode < 96 || keyCode > 105) && keyCode != 8 && keyCode != 110 && keyCode != 190)
	{
		window.event.returnValue = false;
		return;
	}
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
			$(this).parent().parent().parent().parent().show();
		}else{
			$(this).parent().parent().parent().parent().hide();
		}
	});
	/*根据选择条件隐藏结束*/
	if('1' == type) getProductModelPage("tabs-competingProducts",type);
	else if('2' == type) getProductModelPage("tabs-segment",type);
	else if('3' == type) getProductModelPage("tabs-brand",type);
	else getProductModelPage("tabs-manf",type);
};

/**
 * 展示页面
 * 
 */
function getProductModelPage(id,type)
{
	//如果内容不为空则触发请求
	if(!$.trim($('#' + id).html())){
		//传递参数
		var beginPrice = $("#beginPrice").val();//开始价格
		var endPrice = $("#endPrice").val();//结束价格
		var subModelBodyType = $("#subModelBodyType").val();//车身形式
		var params = {subModelShowType:type,beginPrice:beginPrice,endPrice:endPrice,subModelBodyType:subModelBodyType};
		//触发请求
		showLoading(id);
		$('#' + id).load(ctx+"/product/global/getProductSubmodelModal",params,function(){
			
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
					$(this).parent().parent().parent().parent().show();
				}else{
					$(this).parent().parent().parent().parent().hide();
				}
			});
			/*根据选择条件隐藏结束*/
		});
	}
};

/**
 * 导出Excel
 * @param languageType EN:英文版;ZH:中文版
 */
function exportExcel(languageType)
{
	if(!$.trim($("#tSortable tbody").html()))
	{
		alert("暂无数据导出");
		return;
	}
	$("#languageType").val(languageType);
	$("#exportFormId").submit();
}

