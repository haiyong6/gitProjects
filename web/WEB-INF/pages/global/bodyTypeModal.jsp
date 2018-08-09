<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<ul style="margin:10px;padding:0px;">
	<c:forEach items="${bodyTypeList}" var="bodyType">
		<li style="list-style: none;width:20%;float: left;padding-left:5px;">
	     	<label class="checkbox" style="font-size: 12px;color: #3675b4;">
				<input type="checkbox" value="${bodyType.bodyTypeId}" name="bodyTypeIdInput" class="bodyTypeModal" />
				${bodyType.bodyTypeName}
			</label>
		</li>
	</c:forEach>
</ul>
		 