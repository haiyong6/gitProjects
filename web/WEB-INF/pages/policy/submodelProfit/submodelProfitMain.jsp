<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" errorPage="/error.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html >
<head> 
	<title>WAYS智库——汽车大数据智能平台</title>
	<meta http-equiv="Cache-Control" content="no-store"/> 
    <meta http-equiv="Pragma" content="no-cache"/>
	<style>   
	#tabs-competingProducts .form-inline .checkbox,
	#tabs-segment .form-inline .checkbox,
	#tabs-brand .form-inline .checkbox,
	#tabs-manf .form-inline .checkbox{control-group
		margin-right:10px;
	}
	.modal-backdrop.in{
		z-index:9000;
		position:absolute;
	}
	/* 设置弹出框显示层级为最高  */
	.modal{
		z-index:9001;
	}
	</style>
	<script type="text/javascript" >
		var beginDate = "${beginDate}";
		var endDate = "${endDate}";
		var defaultBeginDate = "${defaultBeginDate}";
	</script>
</head>
<body>
<div class="breadLine">
    <ul class="breadcrumb">
        <li>终端支持研究<span class="divider">></span></li>                
        <li class="active">车型利润分析</li>
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
            		<table style="width: 100%">
            			<tr>
            				<td style="width:55%">
            					<div class="span12">
			                    	<form class="form-horizontal" id="formId">
			                    	
			                    		<div class="control-group">
						    				<!-- <label class="control-label" for="analysisDimension">数据指标：</label>
						    				<div class="controls">
										    	<select id="analysisContentType"  >
										      		<option value="1">利润</option>
										      		<option value="2">折扣</option>
										      		<option value="3">成交价</option>
										      	</select>
						    				</div> -->
						  				</div>
			                    		
						  				<div class="control-group">
						    				<label class="control-label" for="analysisDimension">分析维度：</label>
						    				<div class="controls">
										    	<select id="analysisDimensionType"  >
										      		<option value="1">对象对比</option>
										      		<option value="2">城市对比</option>
										      		<option value="3">时间对比</option>
										      	</select>
						    				</div>
						  				</div>
						  				
						  			
						  				
						  				<div class="control-group">
						  					<input type="hidden" id ="maxDate" value="${endDate}">
						    				<label class="control-label" for="startDate">时间：</label>
						    				<div class="controls">
						    					<div class="form-inline">
							      					<span id="startDate-container" class="input-append date">
									  					<input type="text" value="${endDate}"  readonly="readonly" class="input-mini white" placeholder="开始日期" id="startDate"><span class="add-on"><i class="icon-th"></i></span>
													</span>
													<span class="2" style="display:none;">至</span>						
							      					<span id="endDate-container" class="input-append date" style="display:none;">
									  					<input type="text" value="${endDate}" readonly="readonly" class="input-mini white"  placeholder="结束日期" id="endDate"><span class="add-on"><i class="icon-th"></i></span>
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
						    				<label class="control-label" for="analysisDimension">对象类型：</label>
						    				<div class="controls">
										    	<select id="objectType" name ="objectType" >
										      		<option value="1">型号</option>
										      		<option value="2">车型</option>
										      		<option value="3">厂商品牌</option>
										      	</select>
						    				</div>
						  				</div>	
						  				<div class="control-group" name = "objectType1">
						    				<label class="control-label" for="analysisDimension">对象：</label>
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#subModelModal" role="button" class="btn" data-toggle="modal">选择车型</a>
							    				</div>
							    				<input type="hidden" value="1,2,3" class="selectedPooAttributeIds" >
							    				<div class="span10" style="margin-left:0px" id="subModelModalResultContainer"></div>
						    				</div>
										</div>
						  				<div class="control-group" name = "objectType1">
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#versionModal" role="button" class="btn" data-toggle="modal">选择型号</a>
							    				</div>
							    				<div class="span10" style="margin-left:0px" id="versionModalResultContainer"></div>
						    				</div>
						  				</div>
						  				<div class="control-group" name = "objectType1">
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#autoVersionModal" role="button" class="btn" data-toggle="modal">常用对象</a>
							    				</div>
							    				<div class="span10" style="margin-left:0px" id="autoVersionModalResultContainer"></div>
						    				</div>
						  				</div>
						  				
										<div class="control-group" name = "objectType3" style="display: none;">
						    				<label class="control-label" for="analysisDimension">对象：</label>
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#manfModal" role="button" name = "manfSelector" class="btn" data-toggle="modal">选择厂商</a>
							    				</div>
							    				<div class="manfModalResultContainer" style="margin-left:0px" id="manfModalResultContainer"></div>
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
            				<td>
            					 <img alt="" src="${ctx }/img/outGive/submodelProfit.png"> 
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
        		<form action="${ctx }/policy/submodelProfit/exportSubmodelProfitExcel" method="post" id="exportFormId">
	             	<div class="isw-graph"></div>
	                 <h1>查询结果</h1>
	                 <ul class="buttons">
	                 	<li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('EN')"><span class="isw-download"></span>英文Excel</a></li>
	                    <li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('ZH')"><span class="isw-download"></span>中文Excel</a></li>
	                 </ul>
	                 <!-- 导出时需要重新查询原始数据所以必须得把查询时的参数传递过去 -->
	                 <input type="hidden" name="latestWeek" id="latestWeek" value ="${latestWeek}" /><!-- 是否存在最新周数据 -->
	                 <input type="hidden" name="objectType" id="ex_objectType"/><!-- 是否存在最新周数据 -->
	                 <input type="hidden" name="beginDate" id="ex_beginDate"/><!-- 开始时间 -->
	                 <input type="hidden" name="endDate" id="ex_endDate"/><!-- 结束时间 -->
	                 <input type="hidden" name="mids" id="ex_mids"/><!-- 车型ID -->
	                 <input type="hidden" name="manfs" id="ex_manfs"/><!-- 生产商ID -->
	                 <input type="hidden" name="maxDate" id ="ex_maxDate"/><!-- 最大日期 -->
	                 <input type="hidden" name="citys" id="ex_citys"/><!-- 城市ID -->
	                 <input type="hidden" name="languageType" id="languageType" /><!-- 导出语言类型 -->
	                 <input type="hidden" name="inputType" id="analysisType" /><!-- 分析维度类型 -->
	                 <input type="hidden" name="analysisDimensionType" id="analysisType2" /><!-- 分析维度类型 -->
	                 <input type="hidden" name="analysisContentType" id="dataIndexType" /><!-- 数据指标类型 -->
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
	                     	<div class="span12" style="background-color:#FFF;overflow-x:auto;overflow-y:auto; height:0px;" id="chartTitleDiv" >
	                     			<!-- <span style="font-size:18px;color:#002A5B;font-family:微软雅黑;font-weight:bold;margin-left: 15px;padding-top: 5px;position: absolute;"></span>
	                     			<br/>
	                     			<br/>
	                     			<span style="font-size:18px;color:#002A5B;font-family:微软雅黑;margin-left: 15px;position: absolute;margin-top: -10px;"></span>
	                     			<br/>
	                        		<div class="block-fluid table-sorting clearfix" id="chartId" style="height:0px;padding-top: 15px;"></div> -->
	                        </div>
	                     </div>
	                 </div>
                    
                     
	                 <div class="tab-pane" id="tabs-2">
	                 	<div class="row-fluid">
	                     	<div class="span12">
	                        	<div class="block-fluid table-sorting clearfix">
	                         		<table cellpadding="0" cellspacing="0" width="100%" class="table dataTable" id="tSortable">
	                             		<thead id="gridTheadByVersion" class="hide">
		                                	<tr>
		                                	  
		                                	  <th>年月</th>
		                                	  <th id="versionCityTh" class="hide">城市</th>
		                                      <th>型号全称</th>
		                                      <th>返利</th>
		                                      <th>经销商支持</th>
		                                      <th>用户激励</th>
		                                      <th>奖励</th>
		                                      <th>开票价</th> 
		                                      <th>利润</th>    
		                                      <th>经销商成本</th>                               
		                                  	</tr>
		                             	</thead>
		                             	<thead id="gridTheadByModel" class="hide">
		                                	<tr>
		                                	  
		                                	  <th>年月</th>
		                                	  <th id="modelCityTh" class="hide">城市</th>
		                                      <th>车型全称</th>
		                                      <th>返利</th>
		                                      <th>经销商支持</th>
		                                      <th>用户激励</th>
		                                      <th>奖励</th>
		                                      <th>开票价</th> 
		                                      <th>利润</th>    
		                                      <th>经销商成本</th>  
		                                  	</tr>
		                             	</thead>
		                             	<thead id="gridTheadByManf" class="hide">
		                                	<tr>
		                                	 
		                                	  <th>年月</th>
		                                	  <th id="manfCityTh" class="hide">城市</th>
		                                      <th>生产商|品牌全称</th>
		                                      <th>返利</th>
		                                      <th>经销商支持</th>
		                                      <th>用户激励</th>
		                                      <th>奖励</th>
		                                      <th>开票价</th> 
		                                      <th>利润</th>    
		                                      <th>经销商成本</th>  
		                                  	</tr>
		                             	</thead>
		                             	<tbody id="gridTbody">
	                              		</tbody>
	                          		</table>
	                          		<input type="hidden" id="versionEn" />
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
<%@ include file="/common/template/manfModalTemplate.jsp"%>
<script type="text/javascript" >
		define = undefined;
	</script>
	<script type="text/javascript" src="${ctx}/js/plugins/echarts/echarts3/echarts.js"></script><!-- echarts3版本 -->
	<script type="text/javascript" >
	var myChart = echarts.init(document.getElementById("chartTitleDiv"));
	</script>
<script type="text/javascript" src="${ctx}/js/app/policy/submodelProfit/submodelProfit.js"></script> 
</body>
</html>

