<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<ul style="margin:10px;padding:0px;">
	<c:forEach items="${origList}" var="orig">
		<li style="list-style: none;width:20%;float: left;padding-left:5px;">
	     	<label class="checkbox" style="font-size: 12px;color: #3675b4;">
				<input type="${inputType == 1 ?'checkbox':'radio'}" value="${orig.origId}" name="origIdInput" class="origModal" />
				${orig.origName}
			</label>
		</li>
	</c:forEach>
</ul>


		 