<%@ page language="java" errorPage="/error.jsp"  pageEncoding="utf-8"%>
<!-- Bootrstrap modal form -->
<div id="subModelModal" style="width:700px" class="modal hide subModelModalContainer" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true" ng-click="resetModelFunc()">×</button>
        <h3 id="myModalLabel">车型选择</h3>
        <input type="hidden" id="choseType" value="1"><!-- 默认为本竞品 -->
    </div>       
    <div class="modal-body" style="margin:0px" id="subModelModalBody"></div>           
	<div class="modal-footer" style="text-align:center">
      	<button class="btn btn-primary confirm" data-dismiss="modal" aria-hidden="true"  relContainer="subModelModalResultContainer" relInputName="selectedSubModel">确认</button>
	</div>
</div>   