<%@ page language="java" errorPage="/error.jsp"  pageEncoding="utf-8"%>
<!-- Bootrstrap modal form -->
<div id="brandModal" style="width:700px" class="modal hide brandModalContainer" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">
       品牌选择
        </h3>
    </div>       
    <div class="modal-body" style="margin:0px" id="brandModalBody"></div>           
	<div class="modal-footer" style="text-align:center">
      	<button class="btn btn-primary confirm" data-dismiss="modal" aria-hidden="true"  relContainer="brandModalResultContainer" relInputName="selectedBrand">确认</button>
	</div>
</div>   