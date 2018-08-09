<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<ul style="margin:10px;padding:0px;">
	<c:forEach items="${origList}" var="orig">
		<li style="list-style: none;width:20%;float: left;padding-left:5px;">
	     	<label class="checkbox" style="font-size: 12px;color: #3675b4;">
				<input type="checkbox" value="${orig.origId}" name="origIdInput" class="origModal" />
				${orig.origName}
			</label>
		</li>
	</c:forEach>
</ul>
<script>
$(document).ready(function(){
 	$("#origModalResultContainer",window.parent.document).find("div").each(function(){
 		var vid1 = $(this).attr("value"); 		
 		$("#origModalBody").find(".origModal").each(function(){
 			var vid = $(this).val();
 			if( vid1 != null && vid1 != "undefined" && $.trim(vid1)=='0' && !$(this).is(':checked')){
 				$(this).click();
 			}
 			if(vid1 != null && vid1 != "undefined" && vid1 == vid && !$(this).is(':checked')){
 				$(this).click();
 			}
 		});  
 	});
});
</script>

		 