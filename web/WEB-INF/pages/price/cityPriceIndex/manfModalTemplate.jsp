<%@ page language="java" errorPage="/error.jsp"  pageEncoding="utf-8"%>
<!-- Bootrstrap modal form -->
<div id="manfModal" style="width:700px" class="modal hide manfModalContainer" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">
          <label class="checkbox">
        	<input type="checkbox" class="manfModalByAll">厂商选择
          </label>
        </h3>
    </div>       
    <div class="modal-body" style="margin:0px" id="manfModalBody"></div>           
	<div class="modal-footer" style="text-align:center">
      	<button class="btn btn-primary confirm" data-dismiss="modal" aria-hidden="true"  relContainer="manfModalResultContainer" relInputName="selectedManf">确认</button>
	</div>
</div>   