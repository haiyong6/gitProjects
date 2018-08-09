<%@ page language="java" errorPage="/error.jsp"  pageEncoding="utf-8"%>
<!-- Bootrstrap modal form -->
<div id="segmentModal" class="modal hide segmentModalContainer" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close reset" data-dismiss="modal" aria-hidden="true">×</button>
  		<h3>
  		<div id="segmentType">
  		   <span style="font-weight:normal;font-size:14px;">细分市场类别：</span>&nbsp;
  		   <input type="radio" name="segmentType" class="segmentType" segmentValue="1" checked="true" value="Volume"><span style="font-weight:normal;font-size:14px;">Volume</span>&nbsp;
  		   <input type="radio" name="segmentType" class="segmentType" segmentValue="2"  value="Economy"><span style="font-weight:normal;font-size:14px;">Economy</span>&nbsp;
  		   <input type="radio" name="segmentType" class="segmentType" segmentValue="3"  value="Premium"><span style="font-weight:normal;font-size:14px;">Premium</span>&nbsp;
  		   <input type="radio" name="segmentType" class="segmentType" segmentValue="4"  value="Budget"><span style="font-weight:normal;font-size:14px;">Budget</span>&nbsp;
  		   <input type="radio" name="segmentType" class="segmentType" segmentValue="5"  value="Luxury"><span style="font-weight:normal;font-size:14px;">Luxury</span>&nbsp;
  		   <input type="hidden" name="choosedSegmentType" id="choosedSegmentType" value="Volume,Economy,Premium"/>
  		</div>
  		</h3>
        <h3 id="myModalLabel">
        <label class="checkbox">
		    <input type="checkbox" class="segmentModalByAll">细分市场选择
		</label>
		</h3>
	</div>        
    <div class="modal-body" style="margin:0px" id="segmentModalBody"></div>                    
    <div class="modal-footer" style="text-align:center" >
        <button class="btn btn-primary confirm" data-dismiss="modal" aria-hidden="true" id="segmentConfirmBtn" relInputName="selectedSegment">确认</button>
	</div>
</div>
<script>
$(".segmentType").on("click", function() {
    showLoading("segmentModalBody");
    $(".segmentModalByAll").prop("checked", false);
    $("#segmentModalBody").load(ctx + segmentPath, getParams(), function() {
        $(".segmentModalByAll").trigger("segmentChange"); 
	});
});
</script>  