<%@ page language="java" errorPage="/error.jsp"  pageEncoding="utf-8"%>
<!-- Bootrstrap modal form -->
<div id="origModal" class="modal hide origModalContainer" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"  >
	<div class="modal-header">
		<button type="button" class="close reset" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">
          	<label class="checkbox">
				<input type="checkbox" class="origModalByAll">车系选择
			</label>
		</h3>
	</div>        
    <div class="modal-body" style="margin:0px" id="origModalBody"></div>                    
    <div class="modal-footer" style="text-align:center" >
        <button class="btn btn-primary confirm" data-dismiss="modal" aria-hidden="true" id="origConfirmBtn" relInputName="selectedOrig">确认</button>
	</div>
</div>  