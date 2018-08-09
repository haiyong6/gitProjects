<%@ page language="java" errorPage="/error.jsp"  pageEncoding="utf-8"%>
<!-- Bootrstrap modal form -->
<div id="cityModal" class="modal hide cityModalContainer" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"  >
	<div class="modal-header">
		<button type="button" class="close reset" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">
          	<label class="checkbox">
				<input type="checkbox" class="cityModalByAll">城市选择
			</label>
		</h3>
	</div>        
    <div class="modal-body" style="margin:0px" id="cityModalBody"></div>                    
    <div class="modal-footer" style="text-align:center" >
        <button class="btn btn-primary confirm" data-dismiss="modal" aria-hidden="true" relContainer="cityModalResultContainer" relInputName="selectedCity">确认</button>
	</div>
</div>    