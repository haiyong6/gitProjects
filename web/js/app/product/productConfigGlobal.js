$(document).ready(function(){ 
	
    /**
     * 配置点击确定生成
     */
	$(".configModalContainer .confirm").live('click',function(){
		$("#configModalResultContainer ul").html($("#selConfigUl").html().replace(/visible/g,"hidden"));
	});
	
	/**
	 * 配置鼠标经过事件
	 */
	$("#configModalResultContainer ul div").live('mouseover',function(){
		$(this).find(".icon-remove").css({visibility:'visible'});
	});
	
	/**
	 * 配置鼠标离开事件
	 */
	$("#configModalResultContainer ul div").live('mouseout',function(){
		$(this).find(".icon-remove").css({visibility:'hidden'});
	});
});


