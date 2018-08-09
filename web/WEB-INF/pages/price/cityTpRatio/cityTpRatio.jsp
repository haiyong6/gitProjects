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
        <li>价格监测<span class="divider">></span></li>                
        <li class="active">成交价城市对比</li>
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
            		<table  style="width: 100%">
            			<tr>
            				<td style="width:55%">
            					<div class="span12">
			                    	<form class="form-horizontal" id="formId">
						  				<div class="control-group">
						    				<label class="control-label" for="analysisDimension">图形类型：</label>
						    				<div class="controls">
										    	<select id="chartType"  >
										      		<option value="1">雷达图</option>
										      		<option value="2">折线图</option>
										      	</select>
						    				</div>
						  				</div>
						  				
						  				<div class="control-group">
						    				<label class="control-label" for="startDate">时间：</label>
						    				<div class="controls">
						    					<div class="form-inline">
							      					<span id="startDate-container" class="input-append date">
									  					<input type="text" value="${defaultBeginDate}"  readonly="readonly" class="input-mini white" placeholder="开始日期" id="startDate"><span class="add-on"><i class="icon-th"></i></span>
													</span>
												</div>
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
        		<form action="${ctx }/exportCityTpExcel" method="post" id="exportFormId">
	             	<div class="isw-graph"></div>
	                 <h1>查询结果</h1>
	                 <ul class="buttons">
	                 	<li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('EN')"><span class="isw-download"></span>英文Excel</a></li>
	                    <li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('ZH')"><span class="isw-download"></span>中文Excel</a></li>
	                 </ul>
	                 <input type="hidden" name="languageType" id="languageType" /><!-- 导出语言类型 -->
                 </form> 
             </div>
             <div class="block-fluid">
             	<ul class="nav nav-tabs" id="otherTab">
                	<li class="active hide"><a href="#tabs-1" id="showChart" data-toggle="tab" value="2">图表</a></li>
                  	<li class="hide"><a href="#tabs-2" data-toggle="tab" value="1">数据</a></li>
                </ul>        
            	<div class="tab-content">                    
	             	<div class="tab-pane active" id="tabs-1">
	                 	<div class="row-fluid">
	                     	<div class="span12" style="background-color:#FFF;" id="chartTitleDiv">
	                     			<table style="width:100%">
	                     				<tr>
	                     					<td colspan="2" style="background-color: #003366;height:35px;display: none;" id="tdTitle">
	                     						<font style="color: #FFF;font-size: 18px;padding-left: 10px;font-family: 微软雅黑;"></font>
	                     					</td>
	                     				</tr>
	                     				<tr>
	                     					<td>
	                     						<div id="chartId" style="height:0px;padding-top:10px;"></div>
	                        					<div id="lineChartTable" style="display: none;margin-top:-25px;border: 1px solid;"></div>
	                     					</td>
	                     					<td style="width:60%;padding-top:15px;" id="radarTd">
	                     						<table cellpadding="0" cellspacing="0" class="theadtext" style="width:100%;display:none;border-collapse: collapse;" id="tSortable">
				                             		<thead>
					                                	<tr>
					                                	  <th rowspan="2">大区</th>
					                                	  <th rowspan="2">城市</th>
					                                	  <th colspan="3">当前成交价</th>
					                                	  <th colspan="4">环比上月</th>
					                                	  <th rowspan="2">指导价</th>
					                                	  <th rowspan="2">激励额</th>
					                                	  <th rowspan="2">奖励</th>
					                                	  <th rowspan="2">促销</th>
					                                  	</tr>
					                                  	<tr>
					                                  		<th>成交价</th>
					                                  		<th>离散度</th>
					                                  		<th>利润</th>
					                                  		<th>成交价.±</th>
					                                  		<th>成交价%</th>
					                                  		<th>利润.±</th>
					                                  		<th>利润率±</th>
					                                  	</tr>
					                             	</thead>
					                             	<tbody id="gridTbody">
				                              		</tbody>
				                          		</table>
	                     					</td>
	                     				</tr>
	                     			</table>
	                     			
	                        </div>
	                     </div>
	                 </div>
                    
	                 <div class="tab-pane" id="tabs-2">
	                 	<div class="row-fluid">
	                     	<div class="span12">
	                        	<div class="block-fluid table-sorting clearfix">
	                          		<!-- 弹出框校验为成交价校验 -->
	                          		<input type="hidden" id="analysisContentType" value="2" />
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
<script type="text/javascript" src="${ctx}/js/app/price/cityTpRatio/cityTpRatio.js"></script> 
</body>
</html>

