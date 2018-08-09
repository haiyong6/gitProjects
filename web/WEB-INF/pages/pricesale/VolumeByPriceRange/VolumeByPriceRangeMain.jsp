<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" errorPage="/error.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html >
<head> 
	<title>WAYS智库——汽车大数据智能平台</title>
	<style>   
	#tabs-competingProducts .form-inline .checkbox,
	#tabs-segment .form-inline .checkbox,
	#tabs-brand .form-inline .checkbox,
	#tabs-manf .form-inline .checkbox{
		margin-right:10px;
	}
	.tbodytext{text-align: center!important;}
	</style>
	<script type="text/javascript" >
		var beginDate = "${beginDate}";
		var endDate = "${endDate}";
		var defaultBeginDate = "${defaultBeginDate}";
	</script>
</head>
<body>
<input type="hidden" id ="moduleCode" value="${moduleCode}">
<div class="breadLine">
    <ul class="breadcrumb">
        <li>价量分析<span class="divider">></span></li>                
        <li class="active">价格段销量分析</li>
    </ul>
</div>
<div class="workplace">
	<div class="row-fluid queryConditionContainer">
    	<div class="span12">
        	<div class="head clearfix">
            	<div class="isw-zoom"></div>
                <h1>条件车型选择</h1>
                <ul class="buttons">
                	<li class="toggle"><a href="#"></a></li>
                </ul>                            
			</div>
            <div class="block">
            	<div class="row-fluid">
                	<div class="span12">
                    	<form class="form-horizontal" id="formId">
                    	<!-- 
                    		<div class="control-group">
			    				<label class="control-label" for="analysisDimension">分析维度：</label>
			    				<div class="controls">
							    	<select id="objectType"  >
							      		<option value="0">级别</option>
							      		<option value="4">系别</option>
							      		<option value="1">厂商</option>
							      		<option value="2">品牌</option>
							      		<option value="3">车型</option>
							      	</select>
			    				</div>
			  				</div>
                    		-->
			  				<div class="control-group">
			    				<label class="control-label" for="analysisDimension">价格信息：</label>
			    				<div class="controls">
							    	<select id="priceType"  >
							      		<option value="0">指导价</option>
							      		<option value="1">成交价</option>
							      	</select>
			    				</div>
			  				</div>
			  				
			  				<div class="control-group">
			    				<label class="control-label" for="analysisDimension">价格段	：</label>
			    				<div class="controls">
			    				<input type="text" name = "sprice" value = "" id = "sprice" maxlength="8" style="width: 72px;" onblur="spriceOnBulur()" onkeyup="this.value=this.value.replace(/\D/g,'')">
			    				&nbsp;&nbsp;至&nbsp;&nbsp;
			    				<input type="text" name = "eprice" value = "" id = "eprice" maxlength="8" style="width: 72px;" onblur="epriceOnBulur()" onkeyup="this.value=this.value.replace(/\D/g,'')" >
			    				&nbsp;&nbsp;万
			    				</div>
			  				</div>
			  				
			  				<div class="control-group">
			    				<label class="control-label" for="analysisDimension">价格段刻度：</label>
			    				<div class="controls">
			    					<select name="priceScale" id = "priceScale">
										<option value="1000">1000</option>
										<option value="2000">2000</option>
										<option value="5000">5000</option>
										<option value="10000" selected="selected">10000</option>
										<option value="20000">20000</option>
										<option value="40000">40000</option>
										<option value="50000">50000</option>
									</select>
			    				</div>
			  				</div>
			  				
			  				<div class="control-group">
			    				<label class="control-label" for="analysisDimension">时间：</label>
			    				<div id='dateYear' class="controls">
			    					<ul class="dateULContainer" style="margin:0px;"></ul>
			    				</div>
			  				</div>
			  				
			  				<div id="objectContainer">
			  				  <div id="objectDiv" style="display:block;">
					  			<div class="control-group">
					    		  <label class="control-label" for="analysisDimension">对象：</label>
					    			<div class="controls">
					    			</div>
					  			</div>
			  				  </div>
							</div>
			  				<div class="control-group" style="clear:both;">
					   			<div class="text-center">
     								<button type="button" class="btn btn-primary" id="queryBtn">确认</button>
     								<button type="reset" class="btn leftSpace" id="resetBtn">重置</button>
     								<!-- 弹出框校验为销量校验 -->
	                          		<input type="hidden" id="analysisContentType" value="4" />
			    				</div>
						  </div>
						</form>
              		</div>
                </div>
			</div>
		</div>

	</div><!-- row-fluid 查询条件 -->

    <div class="dr"><span></span></div><!-- 分割线 -->
	
	<div class="row-fluid">
		<div class="span12">                    
        	<div class="head clearfix">
        		<form action="${ctx}/exportVolumeByPriceRangeData" method="post" id="exportFormId">
	             	<div class="isw-graph"></div>
	                 <h1>查询结果</h1>
	                 <ul class="buttons" id = "exportButtons">
	                 	<li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('EN')"><span class="isw-download"></span>英文Excel</a></li>
	                    <li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('ZH')"><span class="isw-download"></span>中文Excel</a></li>
	                 </ul>
	                 <input type="hidden" id="languageType" name="languageType" /><!-- 导出语言标识 -->
	                 <input type="hidden" id="yMax" name="yMax" /><!-- Y轴最大值 -->
	                 <input type="hidden" id="yMin" name="yMin" /><!-- Y轴最小值 -->
	                 <input type="hidden" id="splitNumber" name="splitNumber" /><!-- 分割断数 -->
                 </form> 
             </div>
             <div class="block-fluid">
             	<!-- 保存获取车形弹出框当前车型下标 -->
                <input type="hidden" id="getModelIndexId" />
             	<ul class="nav nav-tabs hide" id="otherTab">
                	<li class="active"><a href="#tabs-1" id="showChart" data-toggle="tab" value="2">图表</a></li>
                  	<li><a href="#tabs-2" data-toggle="tab" value="1">数据</a></li>
                </ul>        
            	<div class="tab-content">                    
	             	<div class="tab-pane active" id="tabs-1">
	                 	<div class="row-fluid">
	                     	<div class="span12" style="background-color:#FFF;" id="chartTitleDiv">
	                     			<div id ="noData" style="font-size: 20px; color: red;display: none;">No Data!</div>
	                        		<div id="chartId" style="height:0px;padding-top: 15px;margin-left: 36px;"></div>
	                        		<div class="row-fluid" style="margin-top: -28px;">
				                     	<div class="span12">
				                        	<div class="block-fluid table-sorting clearfix">
				                         		<table class="table dataTable theadtext" style="border: solid 1px #DDDDDD;" id="discountLineTable">
				                          		</table>
				                    		</div>
				                     	</div>
				                	</div>
	                        </div>
	                     </div>
	                 </div>
                    
	                 <div class="tab-pane" id="tabs-2">
	                 	<div class="row-fluid">
	                     	<div class="span12">
	                        	<div class="block-fluid table-sorting clearfix">
	                         		<table cellpadding="0" cellspacing="0" width="100%" class="table dataTable" id="tSortable">
	                          		</table>
	                    		</div>
	                     	</div>
	                	</div>
					</div>                        
            	</div>
        	</div>
    	</div>                                
	</div><!-- row-fluid  图表、数据展示 -->
</div>
<%@ include file="/common/template/segmentModalTemplate.jsp"%>
<%@ include file="/common/template/origModalTemplate.jsp"%>
<%@ include file="/WEB-INF/pages/price/priceIndex/brandModalTemplate.jsp"%>
<%@ include file="/WEB-INF/pages/price/priceIndex/manfModalTemplate.jsp"%>
<%@ include file="/common/template/subModelModalTemplate.jsp"%>
<%@ include file="/common/template/bodyTypeModalTemplate.jsp"%>
<!-- 
<script type="text/javascript" src="${ctx}/js/plugins/dropdown/jq.js"></script> 
<script type="text/javascript" src="${ctx}/js/plugins/dropdown/dropdownlist.js"></script> -->
<script type="text/javascript" >
		var myChart;
		require(
		    [
				'echarts',
				'echarts/chart/line'
		    ],
		    function (ec) {
		        myChart = ec.init(document.getElementById("chartId"));
		    }
		);
	</script>
<script type="text/javascript" src="${ctx}/js/app/pricesale/VolumeByPriceRange/VolumeByPriceRangeMain.js"></script>  
</body>
</html>