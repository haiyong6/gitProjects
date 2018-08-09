<%@ page language="java" errorPage="/error.jsp"  pageEncoding="utf-8"%>
<!-- Bootrstrap modal form -->
<div id="versionModal" class="modal hide versionModalContainer" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true" ng-click="resetVersionFunc()">×</button>
        <h3 id="myModalLabel">型号选择</h3>
    </div>        
     <div class="modal-body" style="margin:0px;padding-top:0px;line-height: 20px" id="versionModalBody"></div>
    <div class="modal-footer" style="text-align:center">
		<button class="btn btn-primary confirm" data-dismiss="modal" aria-hidden="true"  relContainer="versionModalResultContainer" relInputName="selectedVersion">确认</button>
    </div>
</div>  