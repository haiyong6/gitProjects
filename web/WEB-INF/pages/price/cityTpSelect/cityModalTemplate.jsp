<%@ page language="java" errorPage="/error.jsp"  pageEncoding="utf-8"%>
<!-- Bootrstrap modal form -->
<div id="cityModal" class="modal hide cityModalContainer" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"  >
	<div class="modal-header">
		<button type="button" class="close reset" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">
          	<label style="margin-top: 3px;display: inLine; width: 20%;">
				<input type="checkbox" class="cityModalByAll">城市选择&nbsp;&nbsp;&nbsp;&nbsp;
			</label>
		</h3>
			<label style="font-size: 14px;font-weight: normal; display:inline;">
		      <input type="checkbox" id="countryAverage" class="cityModalByCity" value="-1">全国均价
			</label>
	</div>        
    <div class="modal-body" style="margin:0px" id="cityModalBody"></div>                    
    <div class="modal-footer" style="text-align:center" >
        <button class="btn btn-primary confirm" data-dismiss="modal" aria-hidden="true" relContainer="cityModalResultContainer" relInputName="selectedCity">确认</button>
	</div>
</div>    