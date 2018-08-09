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
        <li>商务政策<span class="divider">></span></li>                
        <li class="active">促销走势分析</li>
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
						    				<label class="control-label" for="analysisDimension">数据指标：</label>
						    				<div class="controls">
										    	<select id="analysisContentType"  >
										      		<option value="1">促销</option>
										      		<!-- <option value="2" style="display:none">内促</option>
										      		<option value="3" style="display:none">外促</option> -->
										      	</select>
						    				</div>
						  				</div>
			                    		
						  				<div class="control-group" style="display:none;">
						    				<label class="control-label" for="analysisDimension">分析维度：</label>
						    				<div class="controls">
										    	<select id="analysisDimensionType"  >
										      		<option value="1">对象对比</option>
										      		<option value="2">城市对比</option>
										      	</select>
						    				</div>
						  				</div>
						  				
						  			
						  				
						  				<div class="control-group">
						  					<input type="hidden" id ="maxDate" value="${endDate}">
						    				<label class="control-label" for="startDate">时间：</label>
						    				<div class="controls">
						    					<div class="form-inline">
							      					<span id="startDate-container" class="input-append date">
									  					<input type="text" value="${defaultBeginDate}"  readonly="readonly" class="input-mini white" placeholder="开始日期" id="startDate"><span class="add-on"><i class="icon-th"></i></span>
													</span>
													<span class="2">至</span>						
							      					<span id="endDate-container" class="input-append date">
									  					<input type="text" value="${endDate}" readonly="readonly" class="input-mini white"  placeholder="结束日期" id="endDate"><span class="add-on"><i class="icon-th"></i></span>
													</span>
												</div>
											</div>
											
										</div>
										<div class="control-group" style="display:none;">
											<label class="control-label" for="analysisDimension">城市：</label>
										    <div class="controls">
										    	<div class="span2" style="width:90px">
											    	<a href="#cityModal" role="button" class="btn" data-toggle="modal">选择城市</a>
											    </div>
											    <div class="span10" style="margin-left:0px" id="cityModalResultContainer">
											    	<ul class="selectorResultContainer">
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
										      		<option value="4">品牌</option>
										      		<option value="5">系别</option>
										      		<option value="6">级别</option>
										      	</select>
										      	&nbsp;&nbsp;<div id="duiyingDiv" style="display:none"><input type="radio" class="duiying" name="duiying" value="1">正对应
							    					<input type="radio" class="duiying" name="duiying" checked="checked" value="0">斜对应
						    						</div>
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
						      					<div class="span2" style="width:150px">
							    					<a href="#manfModal" role="button" name = "manfSelector" class="btn" data-toggle="modal">选择厂商品牌</a>
							    				</div>
							    				<div class="manfModalResultContainer" style="margin-left:0px" id="manfModalResultContainer"></div>
						    				</div>
										</div>
										
										<div class="control-group" name = "objectType4" style="display: none;">
						    				<label class="control-label" for="analysisDimension">对象：</label>
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#brandModal" role="button" class="brandSelector btn" data-toggle="modal">选择品牌</a>
							    				</div>
							    				<div class="brandModalResultContainer" style="margin-left:0px" id="brandModalResultContainer"></div>
						    				</div>
										</div>
										
										<div class="control-group" name = "objectType5" style="display: none;">
						    				<label class="control-label" for="analysisDimension">对象：</label>
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#origModal" role="button" name = "origSelector" class="btn" data-toggle="modal">选择系别</a>
							    				</div>
							    				<div class="origModalResultContainer" style="margin-left:0px" id="origModalResultContainer"></div>
						    				</div>
										</div>
										
										<div class="control-group" name = "objectType6" style="display: none;">
						    				<label class="control-label" for="analysisDimension">对象：</label>
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#segmentModal" role="button" name = "segmentSelector" class="btn" data-toggle="modal">选择级别</a>							    					
							    				</div>
							    				<div class="segmentModalResultContainer" style="margin-left:0px" id="segmentModalResultContainer"></div>
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
            					<img alt="" src="${ctx }/img/outGive/policyPromotion.png">
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
        		<form action="${ctx }/promotion/exportExcel" method="post" id="exportFormId">
	             	<div class="isw-graph"></div>
	                 <h1>查询结果</h1>
	                 <ul class="buttons">
	                 	<li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('EN')"><span class="isw-download"></span>英文Excel</a></li>
	                    <li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('ZH')"><span class="isw-download"></span>中文Excel</a></li>
	                 </ul>
	                 <!-- 导出时需要重新查询原始数据所以必须得把查询时的参数传递过去 -->	                
	                 <input type="hidden" name="objectType" id="ex_objectType"/><!-- 对象类型 -->
	                 <input type="hidden" name="beginDate" id="ex_beginDate"/><!-- 开始时间 -->
	                 <input type="hidden" name="endDate" id="ex_endDate"/><!-- 结束时间 -->	                 
	                 <input type="hidden" name="vids" id="ex_vids"/><!-- 型号ID -->
	                 <input type="hidden" name="mids" id="ex_mids"/><!-- 车型ID -->
	                 <input type="hidden" name="manfs" id="ex_manfs"/><!-- 生产商ID -->	                 
	                 <input type="hidden" name="brandIds" id="ex_brands"/><!-- 品牌ID -->
	                 <input type="hidden" name="origIds" id="ex_origs"/><!-- 系别ID -->
	                 <input type="hidden" name="segmentIds" id="ex_segments"/><!-- 级别ID -->
	                 <input type="hidden" name="bodyTypeIds" id="ex_bodyTypes"/><!-- 车身形式ID -->	                 
	                 <input type="hidden" name="maxDate" id ="ex_maxDate"/><!-- 最大日期 -->
	                 <input type="hidden" name="citys" id="ex_citys"/><!-- 城市ID -->
	                 <input type="hidden" name="languageType" id="languageType" /><!-- 导出语言类型 -->
	                 <input type="hidden" name="inputType" id="analysisType" /><!-- 分析维度类型 -->
	                 <input type="hidden" name="analysisContentType" id="dataIndexType" /><!-- 数据指标类型 -->
	                 <input type="hidden" name="duiying" id="duiying" /><!-- 正斜对应参数，1为正对应，0为斜对应 -->
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
	             		<div class="row-fluid" style="background-color:#FFF;display:none;" id="chartTitleDivNoData">	    
	             			<div class="span12" style="background-color:#FFF;">	                 	    	                    		
	                     		<br/><br/>
	                     		<span style="font-size:18px;color:#002A5B;font-family:微软雅黑;font-weight:bold;margin-left: 15px;padding-top: 5px;position: absolute;">无数据</span>
	                     		<br/><br/><br/>
	                        </div>
	                    </div>
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
	                        		<table cellpadding="0" cellspacing="0" width="100%" class="table dataTable" id="tSortableNoData"  style="display:none;">
	                             		<thead>
		                                	<tr>		                                	
		                                	  <th>无数据</th>		                                    
		                                  	</tr>
		                             	</thead>
	                          		</table>
	                          		
	                         		<table cellpadding="0" cellspacing="0" width="100%" class="table dataTable" id="tSortable">
	                             		<thead id="gridTheadByVersion" class="hide">
		                                	<tr>		                                	
		                                	  <th>日期</th>
		                                      <th>型号编码</th>
		                                      <th>型号全称</th>
		                                      <!-- th>上市日期</th-->
		                                      <th>促销总额</th>
		                                      <th>提车支持</th>
		                                      <th>零售支持</th>
		                                      <th>礼品<!-- (油卡、保养)--></th>
		                                      <th>保险</th>
		                                       <th>保养</th>
		                                      <th>人员奖励</th> 
		                                      <th>金融贷款</th>    
		                                      <th>置换支持</th>                               
		                                      
		                                      
		                                    <!--   <th>选择促销</th> -->
		                                  	</tr>
		                             	</thead>
		                             	<thead id="gridTheadByModel" class="hide">
		                                	<tr>		                                	
		                                	  <th>日期</th>
		                                      <th>车型全称</th>
		                                      <th>促销总额</th>
		                                      <th>提车支持</th>
		                                      <th>零售支持</th>
		                                      <th>礼品<!-- (油卡、保养)--></th>
		                                      <th>保险</th>
		                                       <th>保养</th>
		                                      <th>人员奖励</th> 
		                                      <th>金融贷款</th>    
		                                      <th>置换支持</th> 
		                                  	</tr>
		                             	</thead>
		                             	<thead id="gridTheadByManf" class="hide">
		                                	<tr>		                                	
		                                	  <th>日期</th>
		                                      <th>生产商全称</th>
		                                     <th>促销总额</th>
		                                      <th>提车支持</th>
		                                      <th>零售支持</th>
		                                      <th>礼品<!-- (油卡、保养)--></th>
		                                      <th>保险</th>
		                                       <th>保养</th>
		                                      <th>人员奖励</th> 
		                                      <th>金融贷款</th>    
		                                      <th>置换支持</th> 
		                                  	</tr>
		                             	</thead>
		                             	
		                             	
		                             	<thead id="gridTheadByBrand" class="hide">
		                                	<tr>		                                	
		                                	  <th>日期</th>
		                                      <th>品牌</th>
		                                      <th>促销总额</th>
		                                      <th>提车支持</th>
		                                      <th>零售支持</th>
		                                      <th>礼品<!-- (油卡、保养)--></th>
		                                      <th>保险</th>
		                                       <th>保养</th>
		                                      <th>人员奖励</th> 
		                                      <th>金融贷款</th>    
		                                      <th>置换支持</th> 
		                                  	</tr>
		                             	</thead>
		                             	<thead id="gridTheadByOrig" class="hide">
		                                	<tr>		                                	 
		                                	  <th>日期</th>
		                                      <th>系别</th>
		                                      <th>促销总额</th>
		                                      <th>提车支持</th>
		                                      <th>零售支持</th>
		                                      <th>礼品<!-- (油卡、保养)--></th>
		                                      <th>保险</th>
		                                       <th>保养</th>
		                                      <th>人员奖励</th> 
		                                      <th>金融贷款</th>    
		                                      <th>置换支持</th> 
		                                  	</tr>
		                             	</thead>
		                             	<thead id="gridTheadBySegment" class="hide">
		                                	<tr>		                                	 
		                                	  <th>日期</th>
		                                      <th>级别</th>
		                                      <th>促销总额</th>
		                                      <th>提车支持</th>
		                                      <th>零售支持</th>
		                                      <th>礼品<!-- (油卡、保养)--></th>
		                                      <th>保险</th>
		                                       <th>保养</th>
		                                      <th>人员奖励</th> 
		                                      <th>金融贷款</th>    
		                                      <th>置换支持</th> 
		                                  	</tr>
		                             	</thead>
		                             	<tbody id="gridTbody">
	                              		</tbody>
	                          		</table>
	                          		<!-- 如果是城市利润时保存型号英文名称 -->
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
<%@ include file="/common/template/brandModalTemplate.jsp"%>
<%@ include file="/common/template/bodyTypeModalTemplate.jsp"%>
<%@ include file="/common/template/origModalTemplate.jsp"%>
<%@ include file="/common/template/segmentModalTemplate.jsp"%>
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
<script type="text/javascript" src="${ctx}/js/app/policy/promotion/promotion.js"></script>  
</body>
</html>