<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>
	<div class="resultShowContainer">
		<div class="resultShowContent" style="background-color:#fff;height:50px;overflow: auto;" id="selectorResultContainerBySubModel" >
			
		</div>
	</div>
	<ul class="nav nav-tabs">
		<li  class="active">
			<a href="#tabs-competingProducts" data-toggle="tab" onclick="showSubModel('1')" value="1">本品及竞品</a>
		</li>
		<li>
			<a href="#tabs-segment" data-toggle="tab" value="2" onclick="showSubModel('2')" >细分市场</a>
		</li>
		<li>
			<a href="#tabs-brand" data-toggle="tab" value="3" onclick="showSubModel('3')" >品牌</a>
		</li>
		<li>
			<a href="#tabs-manf" data-toggle="tab" value="4" onclick="showSubModel('4')" >厂商</a>
		</li>
		
		<li class="selectorBodyType">
			<label style="margin-bottom:0px"><input type="checkbox" checked="checked" value="1" class="pooAttributeIdInput">Volume</label>
			<label style="margin-bottom:0px"><input type="checkbox" checked="checked" value="2" class="pooAttributeIdInput">Economy</label>
			<label style="margin-bottom:0px"><input type="checkbox" checked="checked" value="3"  class="pooAttributeIdInput">Premium</label>
			<label style="margin-bottom:0px"><input type="checkbox" checked="checked" value="4" class="pooAttributeIdInput">Budget</label>
			<label style="margin-bottom:0px"><input type="checkbox" checked="checked" value="5"  class="pooAttributeIdInput">Luxury</label>
		</li>
	</ul>
	<div class="tab-content">
		<!-- 本品及竞品——开始 -->
		<div class="tab-pane active " id="tabs-competingProducts" style="height:300px;overflow: auto;">
			<table cellpadding="0" cellspacing="0" border="1" width="100%" class="selectorTable">
				<c:forEach items="${bpSubModelList}" var="bpSubModel">
					<tr>
						<td class="selectorContentTd" style="width:80px;text-align:center;" > 
							<div style="margin-left: 5px;text-align: left;">
								<label>
									<input type="${inputType == 1?'checkbox':'radio'}" name="subModelIdInputByBJ" class="subModelIdInput" 
										   pooAttributeId="${bpSubModel.pooAttributeId}" value="${bpSubModel.subModelId}"  
										   style="margin-top: 0px;"
										   />
										${bpSubModel.subModelName}
								</label>
							</div>
						</td>
						<td class="selectorContentTd">
							<div class="row-fluid">
								<c:forEach items="${bpSubModel.jpSubModelList}" var="jpSubModel">
									<label style="padding:0px;margin-left:20px;margin-bottom:0px;width:110px;" class="checkbox span1">
										<input name="subModelIdInputByBJ" type="${inputType == 1?'checkbox':'radio'}" class="subModelIdInput"  pooAttributeId="${jpSubModel.pooAttributeId}" value="${jpSubModel.subModelId}" />
										${jpSubModel.subModelName}
									</label>
								</c:forEach>
							</div>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<!-- 本品及竞品——结束 -->

		<!-- 细分市场——开始 -->
		<div class="tab-pane" id="tabs-segment" style="height:300px;overflow: auto;">
			 
		</div>
		<!-- 细分市场——结束 -->


		<!-- 品牌——开始 -->
		<div class="tab-pane" id="tabs-brand" style="height:300px;overflow: auto;">
			
		</div>
		<!-- 品牌——结束 -->


		<!-- 厂商——开始 -->
		<div class="tab-pane" id="tabs-manf" style="height:300px;overflow: auto;">
			
		</div>
		<!-- 厂商——结束 -->
	</div>
</div>
<script>
$(document).ready(function(){
 	$("#subModelModalResultContainer",window.parent.document).find("div").each(function(){
 		var vid1 = $(this).attr("value");
 		$("#tabs-competingProducts").find("label").each(function(){
 			var vid = $(this).find("input").eq(0).val();
 			if(vid1 != null && vid1 != "undefined" && vid1 == vid && !$(this).is(':checked')){
 				$(this).click();
 			}
 		});  
 	});
});
</script>