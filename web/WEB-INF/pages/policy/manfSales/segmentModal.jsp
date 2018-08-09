<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<table cellpadding="0" cellspacing="0" border="1" width="100%" class="selectorTable">
	<c:forEach items="${segmentList}" var="segment">
		<tr class="selectorTR">
	    	<td class="selectorTypeTd">
	     		<label>
					<input type="hidden" value="${segment.segmentId}" class="segmentModalByLevel1" />${segment.segmentName}
				</label>
			</td>
	     	<td class="selectorContentTd">
	     		<div class="form-inline">
	     			<c:forEach items="${segment.segmentList}" var="subSegment">
		       			<label class="checkbox" style="width:100px">
							<input type="${inputType == 1?'checkbox':'radio'}" value="${subSegment.segmentId}" name="segmentIdInput" class="segmentModalByLevel2" />
							${subSegment.segmentName}
						</label>
					</c:forEach>
				</div>
			</td>
		</tr>
	</c:forEach>
</table>
<script>
 /* $(document).ready(function(){
$(".segmentModalResultContainer").find("div").each(function(){
 		var vid1 = $(this).attr("value");
 		$("#segmentModalBody").find(".segmentModalByLevel1").each(function(){
 			var vid = $(this).val();
 			if( vid1 != null && vid1 != "undefined" && $.trim(vid1)=='0' && !$(this).is(':checked')){
 				$(this).click();
 			}
 			if( vid1 != null && vid1 != "undefined" && vid1 == vid && !$(this).is(':checked')){
 				$(this).click();
 			}
 		});  
 		$("#segmentModalBody").find(".segmentModalByLevel2").each(function(){
 			var vid = $(this).val();
 			if( vid1 != null && vid1 != "undefined" && $.trim(vid1)=='0' && !$(this).is(':checked')){
 				$(this).click();
 			}
 			if( vid1 != null && vid1 != "undefined" && vid1 == vid && !$(this).is(':checked')){
 				$(this).click();
 			}
 		});  
 	});
}); */
</script>