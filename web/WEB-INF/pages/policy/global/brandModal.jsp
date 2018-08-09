<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>
	<div class="resultShowContainer">
		<div class="resultShowContent" style="background-color:#fff;height:50px;overflow: auto;" id="selectorResultContainerByBrand" >
			
		</div>
	</div>
	<div class="letterContainer">
		<div class="form-inline" style="margin-bottom:5px;">
			<ul class="words">
				<c:forEach items="${brandLetterList}" var="brandLetter">
					<li>
						<label rel="word" class="letter" title="${brandLetter.letter}">${brandLetter.letter}</label>
					</li>
				</c:forEach>
			</ul>
			查找：<input type="text" class="input-small locationSearch">
		</div>
	
		<div class="letterContentContainer" style="height: 250px; overflow:auto;" id="test">
	
			<table cellpadding="0" cellspacing="0" border="1" width="100%" class="selectorTable" style="margin-bottom: 0px">
				<c:forEach items="${brandLetterList}" var="brandLetter">
					<tr>
						<td  class="selectorHeadTd">
							<label rel="findword">${brandLetter.letter}</label>
						</td>
					</tr>
					<tr>
						<td class="selectorTypeTd" style="padding-top: 0px;padding-bottom:0px;">
							<ul style="margin:0px;padding:0px;">
								<c:forEach items="${brandLetter.objList}" var="brand">
									<li style="float:left;list-style: none;margin-bottom:5px;margin-top:5px;">
										<label style="width:100px;padding:0px;text-align:left;" class="span3 checkbox subModelIdLabel" letter="${brandLetter.letter}" >
											<input name="brandIdInput" type="${inputType == 1?'checkbox':'radio'}" class="brandIdInput"  value="${brand.brandId}">${brand.brandName}
										</label>
									</li>
								</c:forEach>
							</ul>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
</div>
<script>
/* $(document).ready(function(){
 	$("#brandModalResultContainer",window.parent.document).find("div").each(function(){
 		var vid1 = $(this).attr("brandid");
 		$("#brandModalBody").find(".brandIdInput").each(function(){
 			var vid = $(this).val();
 			if(vid1 != null && vid1 != "undefined" && vid1 == vid && !$(this).is(':checked')){ 			    
 				setTimeout( $(this).click() , 1000 );
 			}
 		});  
 	});
 	
 	
 	$(".brandModalContainer").find('.confirm').live('click',function(event){
 		if(event.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题
 		
		$("#brandModalResultContainer").html("");
		var allObjArr = [];
		$(".brandModalContainer").find('.brandIdInput:checked').each(function(){
			var obj = {};
			obj.id =  $(this).val();
			obj.name =  $.trim($(this).parent().text());
			obj.letter =  $(this).attr("letter");
			allObjArr[allObjArr.length] = obj;
		});
		var strHTML = '<ul class="selectorResultContainer" >';
		for(var i=0;i<allObjArr.length;i++){
			strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
		  	strHTML += '<div class="removeBtnByResult label label-info" brandId="'+allObjArr[i].id+'"  letter="'+allObjArr[i].letter+'" brandName="'+allObjArr[i].name+'" style="cursor:pointer" title="删除：'+allObjArr[i].name+'">';
			strHTML += '<i class="icon-remove icon-white"></i>'+allObjArr[i].name;
		  	strHTML += '</div>';
		 	strHTML += '</li>';
		}
		strHTML += '</ul>';
		$("#brandModalResultContainer").html(strHTML);
		
		//显示选中的值	——结束
		$("#selectorResultContainerByBrand").html("");
		$("#selectorResultContainerByBrand").html(strHTML);
	});
}); */
</script>

		 