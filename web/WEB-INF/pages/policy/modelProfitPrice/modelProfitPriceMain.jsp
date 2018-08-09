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
		.theadtext thead tr th{text-align: center;background-color: #A6CFF7!important;border: 1px solid!important;}
		.tbodytext{text-align: right!important;padding-right: 10px; background-color: #FFF!important;border: 1px solid!important;}
		
	</style>
	<script type="text/javascript">
		var beginDate = "${beginDate}";
		var endDate = "${endDate}";
		var defaultBeginDate = "${defaultBeginDate}";
	</script>
</head>
<body>
<div class="breadLine">
    <ul class="breadcrumb">
        <li>商务政策<span class="divider">></span></li>                
        <li class="active">车型利润分析</li>
    </ul>
<input type="hidden" name="moduleName" id="moduleName" value ="${moduleName}">    
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
            		<table  style="width: 100%">
            			<tr>
            				<td style="width:55%">
            					<div class="span12">
			                    	<form class="form-horizontal" id="formId">
						  				<div class="control-group">
						    				<label class="control-label" for="analysisDimension">分析维度：</label>
						    				<div class="controls">
										    	<select id="analysisDimensionType"  >
										      		<option value="1" selected="selected">型号对比</option>
										      		<option value="2">时间对比</option>
										      		<option value="3">城市对比</option>
										      	</select>
						    				</div>
						  				</div>
						  				
						  				<div class="control-group">
						    				<label class="control-label" for="analysisDimension">频次：</label>
						    				<div class="controls">
										    	<select id="frequency"  >
										      		<option value="1">周</option>
										      		<option value="2">半月</option>
										      		<option value="3" selected="selected">月</option>
										      		<option value="4">季</option>
										      		<option value="5">年</option>
										      	</select>
						    				</div>
						  				</div>	
						  				
						  				<div class="control-group">
						    				<label class="control-label" for="analysisDimension">时间：</label>
						    				<div id='dateYear' class="controls">
						    					<ul class="dateULContainer" style="margin:0px;"></ul>
						    				</div>
						  				</div>
						  				
										<div class="control-group">
											<label class="control-label" for="analysisDimension">城市：</label>
										    <div class="controls">
										    	<div class="span2" style="width:90px">
											    	<a href="#cityModal" role="button" class="btn" data-toggle="modal">选择城市</a>
											    </div>
											    <div class="span10" style="margin-left:0px" id="cityModalResultContainer">
											    	<ul class=selectorResultContainer>
												    	<li>
															<div style="CURSOR: pointer;margin-top: 5px;" class="removeBtn" title="全国均价" value="0" relContainer="cityModal">
																<input value="0" type="hidden" name="selectedCity" />
																全国均价<i class="icon-remove" style="visibility: hidden;"></i>
															</div>
														</li>
											    	</ul>
											    </div>
										    </div>
										</div>
										
						  				<div class="control-group">
						    				<label class="control-label" for="analysisDimension">对象：</label>
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#subModelModal" role="button" class="btn" data-toggle="modal">选择车型</a>
							    				</div>
							    				<input type="hidden" value="1,2,3" class="selectedPooAttributeIds" >
							    				<div class="span10" style="margin-left:0px" id="subModelModalResultContainer"></div>
						    				</div>
										</div>
										
						  				<div class="control-group">
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#versionModal" role="button" class="btn" data-toggle="modal">选择型号</a>
							    				</div>
							    				<div class="span10" style="margin-left:0px" id="versionModalResultContainer"></div>
						    				</div>
						  				</div>
						  				
						  				<div class="control-group">
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#autoVersionModal" role="button" class="btn" data-toggle="modal">常用对象</a>
							    				</div>
							    				<div class="span10" style="margin-left:0px" id="autoVersionModalResultContainer"></div>
						    				</div>
						  				</div>
						  				
						  				<div class="control-group">
								   			<div class="text-center">
			     								<button type="button" class="btn btn-primary" id="queryBtn">确认</button>
			     								<button type="reset" class="btn leftSpace" id="resetBtn">重置</button>
						    				</div>
									  </div>
									</form>
			              		</div>
            				</td>
            				<td style="width:45%">
            					<img alt="" src="${ctx }/img/outGive/tpRatio_Radar.png">
            				</td>
            			</tr>
            		</table>
                	
                </div>
			</div>
		</div>

	</div><!-- row-fluid 查询条件 -->

    <div class="dr"><span></span></div><!-- 分割线 -->
	
<div class="row-fluid">
		<div class="span12">                    
        	<div class="head clearfix">
        		<form action="${ctx }/exportProfitExcel" method="post" id="exportFormId">
	             	<div class="isw-graph"></div>
	                 <h1>查询结果</h1>
	                 <ul class="buttons">
	                 	<li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('EN')"><span class="isw-download"></span>英文Excel</a></li>
	                    <li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('ZH')"><span class="isw-download"></span>中文Excel</a></li>
	                 </ul>
	                 <!-- 导出时需要重新查询原始数据所以必须得把查询时的参数传递过去 -->
	                 <input type="hidden" name="languageType" id="languageType" /><!-- 导出语言类型 -->
	                 <input type="hidden" name="ymax" id="ymax" />
	                 <input type="hidden" name="ymin" id="ymin" />
                 </form> 
             </div>
             <div class="block-fluid">
             	<ul class="nav nav-tabs" id="otherTab">
                	<li class="active"><a href="#tabs-1" id="showChart" data-toggle="tab" value="2">图表</a></li>
                  	<li><a href="#tabs-2" data-toggle="tab" value="1">数据</a></li>
                </ul>        
            	<div class="tab-content">                    
	             	<div class="tab-pane active" id="tabs-1">
	                 	<div class="row-fluid">
	                     	<div class="span12" style="background-color:#FFF;" id="chartTitleDiv">
	                     			<span style="font-size:18px;color:#002A5B;font-family:微软雅黑;font-weight:bold;margin-left: 15px;padding-top: 5px;position: absolute;"></span>
	                     			<br/>
	                     			<br/>
	                     			<span style="font-size:18px;color:#002A5B;font-family:微软雅黑;margin-left: 15px;position: absolute;margin-top: -10px;"></span>
	                     			<br/>
	                        		<div class="block-fluid table-sorting clearfix" id="chartId" style="height:0px;padding-top: 15px;"></div>
	                        </div>
	                     </div>
	                 </div>
                    
	                 <div class="tab-pane" id="tabs-2">
	                 	<div class="row-fluid">
	                     	<div class="span12">
	                        	<div class="block-fluid table-sorting clearfix">
	                    		</div>
	                     	</div>
	                	</div>
					</div>                        
            	</div>
        	</div>
    	</div>                                
	</div><!-- row-fluid  图表、数据展示 -->
</div>
<%@ include file="/common/template/cityModalTemplate.jsp"%>
<%@ include file="/common/template/subModelModalTemplate.jsp"%>
<%@ include file="/common/template/versionModalTemplate.jsp"%>
<%@ include file="/common/template/autoVersinoModalTemplate.jsp"%>
	<script type="text/javascript">
		var myChart;
		require(
		    [
				'echarts',
				'echarts/chart/radar',
				'echarts/chart/line'
		    ],
		    function (ec) {
		        myChart = ec.init(document.getElementById("chartId"));
		    }
		);
	</script>
<script type="text/javascript" src="${ctx}/js/app/policy/modelProfitPrice/modelProfitPriceMain.js"></script> 
</body>
</html>