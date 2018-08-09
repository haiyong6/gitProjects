<%@ page language="java" errorPage="/error.jsp"  pageEncoding="utf-8"%>
<!-- Bootrstrap modal form -->
<div id="autoVersionModal" style="width:700px" class="modal hide autoVersionModalContainer" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true" ng-click="resetModelFunc()">×</button>
        <h3 id="myModalLabel">常用对象选择</h3>
    </div>       
    <div class="modal-body" style="margin:0px" id="autoVersionModalBody"></div>           
	<div class="modal-footer" style="text-align:center">
      	<button class="btn btn-primary confirm" data-dismiss="modal" aria-hidden="true"  relContainer="autoVersionModalResultContainer" relInputName="selectedAutoVersion">确认</button>
	</div>
</div>  