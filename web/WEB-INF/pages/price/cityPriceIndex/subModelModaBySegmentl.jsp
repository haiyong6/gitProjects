<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript">
//是否通过父按钮点击选中的标记
var flag = false;
var pooAttributeId = [];
    $(document).ready(function() {
		checkSegmentType();
    });
    
    function checkSegmentType() {
        var pooAttributeIdArr = [];
        $(".subModelModalContainer").find(".pooAttributeIdInput").each(function(){
			if($(this).attr("checked")){		
				pooAttributeIdArr[pooAttributeIdArr.length] = $(this).val();
			}
		});	
		pooAttributeId = pooAttributeIdArr;
    }
    //设置细分市场全选效果
	$(document).on("click", "#tabs-segment .selectSegmentAll", function() {
	    var modelObj = $(this);
	    var result = false;
		checkSegmentType();
		flag = true;
		$(modelObj).closest("td").next().find(".subModelIdInput").each(function(){
		    var subModelObj = $(this);
		    for(var i = 0; i < pooAttributeId.length; i++) {
		        if(pooAttributeId[i] == subModelObj.attr("pooAttributeId")) {
		            result = true;
		            break;
		        }
		    }
		    if(result) {
			    //全选时，把全部没被选中的子车型全部选中，选中的则不操作
			    if(modelObj.prop("checked")){
			       	if(!subModelObj.prop("checked")){
			       		//设置联动
			       		$(subModelObj).parents('.subModelModalContainer').find('.subModelIdInput').filter(function(){
							return $(this).val() == $(subModelObj).val();
						}).each(function(){
							$(this).prop("checked",true);
						});
			       	} 
				    //判断总全选框要不要全选：当大类全选时监测所有显示的车型车型都选中全选框才选中
			       	var flagt = false;
			       	$("#tabs-segment .subModelIdInput").each(function(){
						   var ths = $(this);
							/*if(ths.parent().attr("style") == undefined){
								ths.parent().attr("style","");
							}*/
							//判断是不是隐藏
						   if(ths.parent().css("display") != "none"){
								if(!$(this).prop("checked")){
									flagt = true;
									return false;
								}
							}
						});
					if(flagt){
							$("#modelAll").prop("checked",false);
						} else{
							$("#modelAll").prop("checked",true);
							}
			       	
			    //取消全选时，把全部被选中的子车型全部取消选中，没被选中的则不操作
			   	} else {
				   	//判斷總全選框要不要全選 ：大類不全選全選框不全選
				   	if($("#modelAll").prop("checked")){
				   		$("#modelAll").prop("checked",false);
					   	}
			        if(subModelObj.prop("checked")){
			    		//subModelObj.click();
			        	//subModelObj.prop("checked",false);
			        	//设置联动取消
			        	$(subModelObj).parents('.subModelModalContainer').find('.subModelIdInput').filter(function(){
							return $(this).val() == $(subModelObj).val();
						}).each(function(){
							$(this).prop("checked",false);
						});
			        } 
			    }
		    }
		    result = false;
		});
		$("#subModelModal .resultShowContainer .resultShowContent").html("");//先清空再重新放上去
		addResultContainerBySubModel();//把选中的放到车型选择下面的容器中
		flag = false;
		/**中级全选检查打钩情况**/
		 checkSelectorHeadTdTrAll($("#choseType").val());
    });
		
		$(document).on("click","#tabs-segment .subModelIdInput",function(e){
		    var checkBox = $(this).closest("td").find(":checkbox");
		    var checkBoxLength = 0;
		    checkSegmentType();
		    $(this).closest("td").find(":checkbox").each(function() {
		        var obj = $(this);
                var p = obj.attr("pooattributeid");
                for(var k = 0; k < pooAttributeId.length; k++) {
                    if(p == pooAttributeId[k]) {
                    	checkBoxLength++;
                    	break;
                    }
                } 
		    });
		    var checkedLength = checkBox.filter(":checked").length;
		    var modelObj = $(this).closest("td").prev().find(".selectSegmentAll");
		    if(!flag) {
				modelObj.prop("checked", checkBoxLength == checkedLength);
		    }
		});
		
</script> 
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
				<tr class="selectorHeadTdTr">
					<td colspan="2" class="selectorHeadTd">
						<label rel="findword">
							<input type="checkbox" class="selectorHeadTdInput" style="margin-bottom:5px;">
							${segment.segmentName}
						</label>
					</td>
				</tr>
				<c:forEach items="${segment.segmentList}" var="subSegment">
					<tr>
					   <td class="selectorTypeTd" style="padding-top: 0px;">
						    <input class="selectSegmentAll" type="checkbox" name="autoGroup" style="margin-top: 0px;" value="${subSegment.segmentId }">${subSegment.segmentName}
						</td>
						<td class="selectorContentTd">
							<div class="row-fluid">
								<c:forEach items="${subSegment.subModelList}" var="subModel">
									<label style="width:120px;padding:0px;margin-left:20px;" class="span3 checkbox subModelIdLabel" letter="${subModel.letter}"><input  name="subModelIdInputBySegment" type="${inputType == 1?'checkbox':'radio'}" class="subModelIdInput" pooAttributeId="${subModel.pooAttributeId}" objectGroup="${subSegment.segmentId }"  value="${subModel.subModelId}" >${subModel.subModelName}</label>
								</c:forEach>
							</div>
						</td>
					</tr>
				</c:forEach>
			</c:forEach>
			<tr class="selectorHeadTdTr">
				</tr>
		</table>
	</div>
</div>
		 