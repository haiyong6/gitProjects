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
	
	<script>
	
	</script>
</head>
<body>
<div class="breadLine">
    <ul class="breadcrumb">
        <li>价格监测<span class="divider">></span></li>                
        <li class="active">利润城市分布</li>
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
            		<table style="width:100%">
            			<tr>
            				<td style="width:55%">
            					<div class="span12">
			                    	<form class="form-horizontal" id="formId">
			                    	
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
            					<img alt="" src="${ctx }/img/outGive/profitCitySpread.png">
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
        		<form action="${ctx }/exportExcelMap" method="post" id="exportFormId">
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
	                     			<div id="id1" style="float:left;margin:auto;width:22%;padding-top: 40px;padding-left: 80px;">
                                    </div>
                                    <div id="titleId" style="float:top;margin-left:35%;">
	                        		</div> 
	                        		<div id="chartMapId" style="height:400px;float:left;width:40%;"></div>
	                        		<div id="id2" style="float:right;margin-auto;width:25%;">
	                        		</div>
	                        </div>
	                     </div>
	                 </div>
                    
	                 <div class="tab-pane" id="tabs-2">
	                 	<div class="row-fluid">
	                     	<div class="span12">
	                        	<div class="block-fluid table-sorting clearfix">
	                         		<table cellpadding="0" cellspacing="0" width="100%" class="table dataTable" id="tSortable">
	                             		<thead>
		                                	<tr>
		                                	  <th>年月</th>
		                                      <th>型号编码</th>
		                                      <th>型号全称</th>
		                                      <th>上市日期</th>
		                                      <th>指导价</th>
		                                      <th>城市</th>
		                                      <th>成交价</th>
		                                      <th>经销商成本</th> 
		                                      <th>开票价</th>    
		                                      <th>激励额</th>                               
		                                      <th>考核奖励</th>
		                                      <th>促销补贴</th>
		                                      <th>车型利润</th>
		                                  	</tr>
		                             	</thead>
		                             	<tbody id="gridTbody">
	                              		</tbody>
	                          		</table>
	                          		<!-- 弹出框校验为利润校验 -->
	                          		<input type="hidden" id="analysisContentType" value="1" />
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
<script src="${ctx}/js/plugins/echarts/echarts-plain-map.js"></script>
<script type="text/javascript">
    var beginDate = "${beginDate}";
	var endDate = "${endDate}";
	var defaultBeginDate = "${defaultBeginDate}";
	var myChart = echarts.init(document.getElementById('chartMapId'));
</script>
<script type="text/javascript" src="${ctx}/js/app/price/CityProfitDistribution/CityProfitDistribution.js"></script>  
</body>
</html>

