<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<div class="letterContainer">
	<div class="form-inline form-search" style="padding: 4px;">
		<select id="segmentFilter" style="width: 100px"  rel="words">
			<c:forEach items="${segmentList}" var="segment">
				<option  value="${segment.segmentId}">${segment.segmentName}</option>
			</c:forEach>
		</select>
		&nbsp;&nbsp;
		查找：<input type="text" 	class="input-small locationSearch">
		
	</div>
	<div class="letterContentContainer" style="height: 250px; overflow:auto;" id="test">
		<table cellpadding="0" cellspacing="0" border="1" width="100%" class="selectorTable" style="margin-bottom: 0px">
			<c:forEach items="${segmentList}" var="segment">
				<tr>
					<td colspan="2" class="selectorHeadTd"><label rel="findword">${segment.segmentName}</label></td>
				</tr>
				<c:forEach items="${segment.segmentList}" var="subSegment">
					<tr>
						<td class="selectorTypeTd" style="padding-top: 0px;">${subSegment.segmentName}</td>
						<td class="selectorContentTd">
							<div class="row-fluid">
								<c:forEach items="${subSegment.subModelList}" var="subModel">
									<label style="width:120px;padding:0px;margin-left:20px;" class="span3 checkbox subModelIdLabel" letter="${subModel.letter}"><input  name="subModelIdInputBySegment" type="${inputType == 1?'checkbox':'radio'}" class="subModelIdInput" pooAttributeId="${subModel.pooAttributeId}"   value="${subModel.subModelId}" >${subModel.subModelName}</label>
								</c:forEach>
							</div>
						</td>
					</tr>
				</c:forEach>
			</c:forEach>
		</table>
	</div>
</div>
		 