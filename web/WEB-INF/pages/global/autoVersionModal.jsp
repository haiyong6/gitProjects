<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
 
<div class="letterContainer">
	<div class="form-inline form-search" style="padding: 4px;">
		<select id="autoSubModelFilter" style="width: 100px"  rel="words">
			<c:forEach items="${autoCustomGroupList}" var="autoCustomGroup">
				<option  value="${autoCustomGroup.modelID}">${autoCustomGroup.modelName}</option>
			</c:forEach>
		</select>
		&nbsp;&nbsp;
		查找：<input type="text" 	class="input-small locationSearchByAutoVersion">
		
	</div>
	<div class="letterContentContainer" style="height: 250px; overflow:auto;" id="test">
		<table cellpadding="0" cellspacing="0" border="1" width="100%" class="selectorTable" style="margin-bottom: 0px">
			<c:forEach items="${autoCustomGroupList}" var="autoCustomGroup">
				<tr>
					<td colspan="2" class="selectorHeadTd">
					<label rel="findword" modelName ="${autoCustomGroup.modelName}">${autoCustomGroup.modelName}
					<span style="float: right; margin-right: 70px;">Mix</span>
					<span style="float: right; margin-right: 70px;">MSRP</span>
					</label>
					</td>
				</tr>
				<c:forEach items="${autoCustomGroup.objectGroupList}" var="objectGroup">
					<tr>
						<td class="selectorTypeTd" style="padding-top: 0px;">${objectGroup.objectGroup}<input class="selectAutoVersionAll" type="${inputType == 1?'checkbox':'hidden'}" name = "autoGroup" style="margin-top: 0px;" value ="${objectGroup.objectGroupID}"></td>
						<td class="selectorContentTd">
							<div class="row-fluid">
								<c:forEach items="${objectGroup.versionList}" var="version">
									<label style="width:500px;padding:0px;margin-left:20px;" class="span3 checkbox autoVersionIdLabel" letter="${version.letter}">
										<input name="autoVersionIdInput" type="${inputType == 1?'checkbox':'radio'}" objectGroup ="${objectGroup.objectGroupID}" class="autoVersionIdInput" text = "${version.versionName}" value="${version.versionId}"/>${version.versionName}
										<span style="float: right;width:50px;">${version.mix}%</span>
										<span style="float: right; margin-right: 70px;"><fmt:formatNumber value="${version.msrp}" pattern="###,###"/></span>
									</label>
								</c:forEach>
							</div>
						</td>
					</tr>
				</c:forEach>
			</c:forEach>
		</table>
	</div>
</div>
		 