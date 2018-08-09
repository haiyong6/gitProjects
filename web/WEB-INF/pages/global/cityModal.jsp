<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<table cellpadding="0" cellspacing="0" border="1" width="100%" class="selectorTable ">
	<c:forEach items="${areaList}" var="area">
		<tr>
	    	<td class="selectorTypeTd">
	     		<label>
					<input type="checkbox" value="${area.areaId}" class="cityModalByArea" />${area.areaName}
				</label>
			</td>
	     	<td class="selectorContentTd">
	     		<div class="form-inline" >
	     			<c:forEach items="${area.citys}" var="city">
		       			<label class="checkbox">
							<input type="checkbox" value="${city.cityId}" name="selectionCity" class="cityModalByCity" />${city.cityName}
						</label>
					</c:forEach>
				</div>
			</td>
		</tr>
	</c:forEach>
</table>