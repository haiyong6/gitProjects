<%@ page language="java" errorPage="/error.jsp"  pageEncoding="utf-8"%>
<!-- Bootrstrap modal form -->
<div id="configModal" class="modal hide configModalContainer" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"  >
	<div class="modal-header">
		<button type="button" class="close reset" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">
          	<label class="checkbox">
				配置选择
			</label>
		</h3>
	</div>        
    <div class="modal-body" style="margin:0px" id="configClassify"></div>    
    <div class="modal-footer" style="text-align:center" >
        <button class="btn btn-primary confirm" data-dismiss="modal" aria-hidden="true" btn-primary" relContainer="cityModalResultContainer" relInputName="selectedCity">确认</button>
	</div>
</div>    