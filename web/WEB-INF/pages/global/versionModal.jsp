<%@page import="com.ways.app.price.model.Version"%>
<%@page import="com.ways.app.framework.utils.AppFrameworkUtil"%>
<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="letterContainer">
	<div class="form-inline" style="margin-bottom:5px;">
		<ul class="words" style="margin:0px;padding:0px;">
			<c:forEach items="${versionList}" var="subModel">
				<li style="width:auto;margin-left:10px;" >
					<label rel="word" class="label label-info"  title="${subModel.subModelName}">${subModel.subModelName}</label>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div style="clear:both"></div>
	<div style="height: 250px; overflow: auto;" id="test" class="letterContentContainer">
		<table cellpadding="0" cellspacing="0" border="1" width="100%" class="selectorTable">
			<thead>
				<tr>
					<th>车型</th>
					<th>MSRP</th>
					<th>Mix</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${versionList}" var="subModel">
					<tr>
						<td colspan="3" class="selectorHeadTd" style="padding-left:4px">
							<label style="height:25px;line-height:25px;" rel="findword">
								<input type="checkbox" class="selectVersionAll" value="${subModel.subModelId}"  style="margin:0px;display:${inputType==1?'':'none'}" />
								${subModel.subModelName}
							</label>
						</td>
					</tr>
					<c:forEach items="${subModel.versionList}" var="version">
						<tr>
							<td class="selectorContentTd" style="width:300px">
								<label style="line-height:20px"><input style="margin:0px;margin-right:5px" type="${inputType == 1?'checkbox':'radio'}" name="versionIdInput" class="versionIdInput" subModelId="${subModel.subModelId}" value="${version.versionId}"  />${version.versionName}</label>
							</td>
							<td class="selectorContentTd" style="width:50px">
								<div class="form-inline">
									<label class="checkbox">
									<%
										Version version =(Version)pageContext.getAttribute("version");
									
										String msrp = AppFrameworkUtil.format(version.getMsrp()); 
										out.print(msrp);
									%>
									</label>
								</div>
							</td>
							<td class="selectorContentTd" style="width:50px">
								<div class="form-inline">
									<label class="checkbox">
										${version.mix }%
									</label>
								</div>
							</td>
						</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>