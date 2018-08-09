<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>
	<div class="resultShowContainer">
		<div class="resultShowContent" style="background-color:#fff;height:50px;overflow: auto;" id="selectorResultContainerByManf" >
			
		</div>
	</div>
	<div class="letterContainer">
		<div class="form-inline" style="margin-bottom:5px;">
			<ul class="words">
				<c:forEach items="${manfLetterList}" var="manfLetter">
					<li>
						<label rel="word" class="letter" title="${manfLetter.letter}">${manfLetter.letter}</label>
					</li>
				</c:forEach>
			</ul>
			查找：<input type="text" class="input-small locationSearch">
		</div>
	
		<div class="letterContentContainer" style="height: 250px; overflow:auto;" id="test">
	
			<table cellpadding="0" cellspacing="0" border="1" width="100%" class="selectorTable" style="margin-bottom: 0px">
				<c:forEach items="${manfLetterList}" var="manfLetter">
					<tr>
						<td  class="selectorHeadTd">
							<label rel="findword">${manfLetter.letter}</label>
						</td>
					</tr>
					<tr>
						<td class="selectorTypeTd" style="padding-top: 0px;padding-bottom:0px;">
							<ul style="margin:0px;padding:0px;width:600px;">
								<c:forEach items="${manfLetter.objList}" var="manf">
									<li style="float:left;list-style: none;margin-bottom:5px;margin-top:5px;">
										<label style="width:170px;padding:0px;text-align:left;" class="span3 checkbox subModelIdLabel" letter="${manf.letter}" >
											<input name="manfIdInput" type="${inputType == 1?'checkbox':'radio'}" class="manfIdInput"  value="${manf.manfId}">${manf.manfName}
										</label>
										</br>
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


		 