<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
.selectorByConf {
	margin-top:10px;
}
.selectorByConf li{
	list-style-type: none;
    cursor:pointer;
    line-height: 20px;
    padding-left: 5px;
}

.selectorByConf .border{
	border-right:1px solid #eee;
}
 
</style>

<script type="text/javascript">

	/**
	*根据配置大类ID获取配置信息
	*
	*/
	function getConfigInfoList(classifyId,obj)
	{
		//添加选中样式
		$("#configClassifyLi li").removeClass("active");
		$(obj).addClass("active");
		
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/product/global/getConfigInfoList",
			   data: {classifyId:classifyId},
			   success: function(data){
				   if(data)
				   {
					   var liHtml = "";
					   for(var i = 0; i < data.length; i++)
					   {
						   var conf = data[i];
						   liHtml += "<li onclick='addConfigInfo(\""+conf.configId+"\",\""+conf.configType+"\",this)'"
						   + "data-value='"+conf.configId+","+conf.configType+"'><a href='#'>"+conf.configName+"</a></li>";
					   }
					   $("#configLi").html(liHtml);
				   }
			   },
			   error:function(){
				   
			   }
			});
	}
	
	/**
	* 添加配置
	*
	*/
	function addConfigInfo(configId,configType,obj)
	{
		//添加选中样式
		$("#configLi li").removeClass("active");
		$(obj).addClass("active");
		//已选配置内容
		var selConfigUlHtml = $("#selConfigUl").html();
		
		if("B" == configType)
		{
			if(-1 != selConfigUlHtml.indexOf(configId+","+configType))
			{
					return;
			}
			var configUlHtml = "<li>";
			configUlHtml += "<div class='removeBtn' data-value='"+configId+","+configType+",1' style='padding-top: 6px; cursor: pointer'>";
			configUlHtml += "<input type='hidden' />" + $(obj).text();
			configUlHtml += "<i class='icon-remove' style='visibility: visible;'></i></div></li>";
			$("#selConfigUl").html(selConfigUlHtml + configUlHtml);
			//清空配置值栏
			$("#configValueLi").html('');
		}
		else
		{
			//发送请求
			$.ajax({
				   type: "POST",
				   url: ctx+"/product/global/getConfigValue",
				   data: {configId:configId},
				   success: function(data){
					   if(data)
					   {
						   var liHtml = "";
						   for(var i = 0; i < data.length; i++)
						   {
							   var conf = data[i];
							   liHtml += "<li onclick='addConfigToValue(this)'><a href='#'>"+conf.VALUE+"</a></li>";
						   }
						   $("#configValueLi").html(liHtml);
					   }
				   },
				   error:function(){
					   
				   }
				});
		}
	}
	
	/**
	*添加非B类型带配置值配置
	*
	*/
	function addConfigToValue(obj)
	{
		//添加选中样式
		$("#configValueLi li").removeClass("active");
		$(obj).addClass("active");
		var config = $("#configLi li.active").attr("data-value") + "," + $(obj).text();
		//已选配置内容
		var selConfigUlHtml = $("#selConfigUl").html();
		if(-1 != selConfigUlHtml.indexOf(config))
		{
			return;
		}
		
		var configUlHtml = "<li>";
		configUlHtml += "<div class='removeBtn' data-value='"+config+"' style='padding-top: 6px; cursor: pointer'>";
		configUlHtml += "<input type='hidden' />" + $("#configLi li.active").text() +  $(obj).text();
		configUlHtml += "<i class='icon-remove' style='visibility: visible;'></i></div></li>";
		$("#selConfigUl").html(selConfigUlHtml + configUlHtml);
		
	}
</script>
<div class="row-fluid selectorByConf" style="margin-top:5px;">
	<div class="span4 border" style="height:250px;">
		<div class="list-content">
			<ul class="nav nav-list config-category" id="configClassifyLi"> 
				<c:forEach items="${configClassifyList }" var="config">
					<li onclick="getConfigInfoList(${config.classifyId },this)"><a href="#">${config.classifyName }</a></li>    
				</c:forEach>
			</ul> 
		</div>
	</div>
	<div class="span4 border"  style="height:250px;overflow: auto;">
		<div class="list-content">
			<ul class="nav nav-list config-category" id="configLi"> 
			</ul> 
		</div>
	</div>
	<div class="span4"  style="height:250px;overflow: auto;">
		<div class="list-content">
			<ul class="nav nav-list config-category" id="configValueLi"> 
			</ul> 
		</div>
	</div>
	<hr style="border:1px solid #EEEEEE" />
	
	<div class="span10">
		已选配置：
		<ul class="selectorResultContainer" id="selConfigUl">
		</ul>
	</div>
</div>