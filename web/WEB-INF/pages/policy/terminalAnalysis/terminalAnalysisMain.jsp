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
        <li class="active">终端支持分析</li>
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
						    				<label class="control-label" for="analysisDimension">频次：</label>
						    				<div class="controls">
										    	<select id="frequencyType"  >
										      		<option value="1">月</option>
										      		<option value="2">季</option>
										      		<option value="3">年</option>
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
										<div class="control-group">
						    				<label class="control-label" for="analysisDimension">对象类型：</label>
						    				<div class="controls">
										    	<select id="objectType" name ="objectType" >
										    		<option value="0" selected="selected">型号</option>
										    		<option value="1">车型</option>
										    		<option value="2">厂商品牌</option>
										    		<option value="3">品牌</option>
										    		<option value="4">系别</option>
										      		<option value="5">级别</option>
										      	</select>
						    				</div>
						  				</div>	
						  				
						  				<div id="objectContainer">
						  					<div class="control-group" name = "objectModal" style="display:block;">
							    				<label class="control-label" for="analysisDimension">对象：</label>
							    				<div class="controls">
							      					<div class="span2" style="width:90px">
								    					<a href="#subModelModal" role="button" class="btn" data-toggle="modal">选择车型</a>
								    				</div>
								    				<div class="span10" style="margin-left:0px" id="subModelModalResultContainer"></div>
							    				</div>
											</div>
						  				
						  					<div class="control-group" name = "objectModal" style="display:block;">
							    				<div class="controls">
							      					<div class="span2" style="width:90px">
								    					<a href="#versionModal" role="button" name = "versionSelector" class="btn" data-toggle="modal">选择型号</a>							    					
								    				</div>
								    				<div class="versionModalResultContainer" style="margin-left:0px" id="versionModalResultContainer"></div>
							    				</div>
											</div>
											
											<div class="control-group" name = "objectModal" style="display:block;">
							    				<div class="controls">
							      					<div class="span2" style="width:90px">
								    					<a href="#autoVersionModal" role="button" name = "autoVersionSelector" class="btn" data-toggle="modal">常用对象</a>							    					
								    				</div>
								    				<div class="autoVersionModalResultContainer" style="margin-left:0px" id="autoVersionModalResultContainer"></div>
							    				</div>
											</div>
											
											<div class="control-group" name = "objectModal" style="display: none;">
							    				<label class="control-label" for="analysisDimension">对象：</label>
							    				<div class="controls">
							      					<div class="span2" style="width:150px">
								    					<a href="#manfModal" role="button" name = "manfSelector" class="btn" data-toggle="modal">选择厂商品牌</a>
								    				</div>
								    				<div class="manfModalResultContainer" style="margin-left:0px" id="manfModalResultContainer"></div>
							    				</div>
											</div>
											
											<div class="control-group" name = "objectModal" style="display: none;">
							    				<label class="control-label" for="analysisDimension">对象：</label>
							    				<div class="controls">
							      					<div class="span2" style="width:90px">
								    					<a href="#brandModal" role="button" name = "brandSelector" class="btn" data-toggle="modal">选择品牌</a>
								    				</div>
								    				<div class="brandModalResultContainer" style="margin-left:0px" id="brandModalResultContainer"></div>
							    				</div>
											</div>
											
											<div class="control-group" name = "objectModal" style="display: none;">
							    				<label class="control-label" for="analysisDimension">对象：</label>
							    				<div class="controls">
							      					<div class="span2" style="width:90px">
								    					<a href="#origModal" role="button" name = "origSelector" class="btn" data-toggle="modal">选择系别</a>
								    				</div>
								    				<div class="origModalResultContainer" style="margin-left:0px" id="origModalResultContainer"></div>
							    				</div>
											</div>
											
							  				<div class="control-group" name = "objectModal" style="display:none;">
							    				<label class="control-label" for="analysisDimension">对象：</label>
							    				<div class="controls">
							      					<div class="span2" style="width:90px">
								    					<a href="#segmentModal" role="button" name = "segmentSelector" class="btn" data-toggle="modal">选择级别</a>							    					
								    				</div>
								    				<div class="segmentModalResultContainer" style="margin-left:0px" id="segmentModalResultContainer"></div>
							    				</div>
											</div>
											
											<div class="control-group"  name="bodyTypeModal" style="display: none;">
										  		<label class="control-label" for="analysisDimension">车身形式：</label>
					                       		<div class="controls"> 
					                       			<div class="span2" style="width:90px">
					                       				<a href="#bodyTypeModal" role="button" name = "bodyTypeSelector" class="btn" data-toggle="modal">车身形式</a> 
					                       			</div> 
					                       			<div class="bodyTypeModalResultContainer" style="margin-left:0px" id="bodyTypeModalResultContainer"></div>
					                      		</div>
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
            					<img alt="" src="${ctx }/img/outGive/terminalAnalysis.png">
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
        		<form action="${ctx }/terminal/exportExcel" method="post" id="exportFormId">
	             	<div class="isw-graph"></div>
	                 <h1>查询结果</h1>
	                 <ul class="buttons">
	                 	<li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('EN')"><span class="isw-download"></span>英文Excel</a></li>
	                    <li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('ZH')"><span class="isw-download"></span>中文Excel</a></li>
	                 </ul>
	                 <!-- 导出时需要重新查询原始数据所以必须得把查询时的参数传递过去 -->
	                 <input type="hidden" name="ex_beginDate" id="ex_beginDate" /><!-- 开始时间 -->
                 	 <input type="hidden" name="ex_endDate" id="ex_endDate" /><!-- 结束时间 -->
                 	 <input type="hidden" name="ex_mids" id="ex_mids" /><!-- 车型ID -->
                 	 <input type="hidden" name="ex_vids" id="ex_vids" /><!-- 型号ID -->
                 	 <input type="hidden" name="ex_manfIds" id="ex_manfIds" /><!-- 生产商品牌ID -->
                 	 <input type="hidden" name="ex_brandIds" id="ex_brandIds" /><!-- 品牌ID -->
                 	 <input type="hidden" name="ex_origIds" id="ex_origIds" /><!-- 系别ID -->
                 	 <input type="hidden" name="ex_segmentIds" id="ex_segmentIds" /><!-- 级别ID -->
                 	 <input type="hidden" name="ex_bodyTypeIds" id="ex_bodyTypeIds" /><!-- 车身形势ID -->
                 	 <input type="hidden" name="ex_frequencyType" id="ex_frequencyType" /><!-- 频次ID -->
                 	 <input type="hidden" name="ex_objectType" id="ex_objectType" /><!-- 对象类型 -->
                 	 <input type="hidden" name="ex_priceType" id="ex_priceType" /><!-- 导出促销细分价格类型 -->
                 	 <input type="hidden" name="languageType" id="languageType" /><!-- 导出语言类型 -->
                 </form> 
             </div>
             <div class="block-fluid">
             	<ul class="nav nav-tabs" id="otherTab">
                	<li class="active"><a href="#tabs-1" id="showChart" data-toggle="tab" value="2">图表</a></li>
                  	<li><a href="#tabs-2" id ="showTable" data-toggle="tab" value="1">数据</a></li>
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
	                     			<div id = "priceTypeDiv" class="control-group" style = "margin-left: 80px;margin-top:-50px; display: none;">
		                     			<input type="checkbox" name = "isPromotion" id = "isPromotion" style = "margin-top: -3px;width: 20px;height: 20px;"><font size="4px;">促销细分</font></input>&nbsp;&nbsp;&nbsp;
		                     			<select name = "priceType" id = "priceType" class="hide">
		                     				<option value = "1">金额</option>
		                     				<option value = "2">份额</option>
		                     			</select>
	                     			</div>
	                        		<div class="block-fluid table-sorting clearfix" id="chartId" style="height:0px;padding-top: 15px;"></div>
	                        		<div id="chartTable" style="display: block;margin-top:-50px;"></div>
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
		                                	  <th style="text-align: center;">时间</th>
		                                	  <th style="text-align: center;">型号</th>
		                                      <th style="text-align: center;">型号促销支持</th>
		                                      <th style="text-align: center;">提车支持</th>
		                                      <th style="text-align: center;">零售支持</th>
		                                      <th style="text-align: center;">人员奖励</th>
		                                      <th style="text-align: center;">金融贷款</th> 
		                                      <th style="text-align: center;">置换支持</th>    
		                                      <th style="text-align: center;">赠送保险</th>                               
		                                      <th style="text-align: center;">赠送奖品</th>
		                                  	</tr>
		                             	</thead>
		                             	<thead id="gridTheadByModel" class="hide">
		                                	<tr>
		                                	  <th style="text-align: center;">时间</th>
		                                	  <th style="text-align: center;">车型</th>
		                                      <th style="text-align: center;">车型促销支持</th>
		                                      <th style="text-align: center;">提车支持</th>
		                                      <th style="text-align: center;">零售支持</th>
		                                      <th style="text-align: center;">人员奖励</th>
		                                      <th style="text-align: center;">金融贷款</th> 
		                                      <th style="text-align: center;">置换支持</th>    
		                                      <th style="text-align: center;">赠送保险</th>                               
		                                      <th style="text-align: center;">赠送奖品</th>
		                                  	</tr>
		                             	</thead>
		                             	<thead id="gridTheadByManf" class="hide">
		                                	<tr>
		                                	  <th style="text-align: center;">时间</th>
		                                	  <th style="text-align: center;">厂商</th>
		                                      <th style="text-align: center;">厂商促销支持</th>
		                                      <th style="text-align: center;">提车支持</th>
		                                      <th style="text-align: center;">零售支持</th>
		                                      <th style="text-align: center;">人员奖励</th>
		                                      <th style="text-align: center;">金融贷款</th> 
		                                      <th style="text-align: center;">置换支持</th>    
		                                      <th style="text-align: center;">赠送保险</th>                               
		                                      <th style="text-align: center;">赠送奖品</th>
		                                  	</tr>
		                             	</thead>
		                             	<thead id="gridTheadByBrand" class="hide">
		                                	<tr>
		                                	  <th style="text-align: center;">时间</th>
		                                	  <th style="text-align: center;">品牌</th>
		                                      <th style="text-align: center;">品牌促销支持</th>
		                                      <th style="text-align: center;">提车支持</th>
		                                      <th style="text-align: center;">零售支持</th>
		                                      <th style="text-align: center;">人员奖励</th>
		                                      <th style="text-align: center;">金融贷款</th> 
		                                      <th style="text-align: center;">置换支持</th>    
		                                      <th style="text-align: center;">赠送保险</th>                               
		                                      <th style="text-align: center;">赠送奖品</th>
		                                  	</tr>
		                             	</thead>
		                             	<thead id="gridTheadByOrig" class="hide">
		                                	<tr>
		                                	  <th style="text-align: center;">时间</th>
		                                	  <th style="text-align: center;">系别</th>
		                                      <th style="text-align: center;">系别促销支持</th>
		                                      <th style="text-align: center;">提车支持</th>
		                                      <th style="text-align: center;">零售支持</th>
		                                      <th style="text-align: center;">人员奖励</th>
		                                      <th style="text-align: center;">金融贷款</th> 
		                                      <th style="text-align: center;">置换支持</th>    
		                                      <th style="text-align: center;">赠送保险</th>                               
		                                      <th style="text-align: center;">赠送奖品</th>
		                                  	</tr>
		                             	</thead>
		                             	<thead id="gridTheadBySegment" class="hide">
		                                	<tr>
		                                	  <th style="text-align: center;">时间</th>
		                                	  <th style="text-align: center;">级别</th>
		                                      <th style="text-align: center;">级别促销支持</th>
		                                      <th style="text-align: center;">提车支持</th>
		                                      <th style="text-align: center;">零售支持</th>
		                                      <th style="text-align: center;">人员奖励</th>
		                                      <th style="text-align: center;">金融贷款</th> 
		                                      <th style="text-align: center;">置换支持</th>    
		                                      <th style="text-align: center;">赠送保险</th>                               
		                                      <th style="text-align: center;">赠送奖品</th>
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
<%@ include file="/common/template/subModelModalTemplate.jsp"%>
<%@ include file="/common/template/versionModalTemplate.jsp"%>
<%@ include file="/common/template/autoVersinoModalTemplate.jsp"%>
<%@ include file="/common/template/manfModalTemplate.jsp"%>
<%@ include file="/common/template/brandModalTemplate.jsp"%>
<%@ include file="/common/template/bodyTypeModalTemplate.jsp"%>
<%@ include file="/common/template/origModalTemplate.jsp"%>
<%@ include file="/common/template/segmentModalTemplate.jsp"%>
<script type="text/javascript" src="${ctx}/js/plugins/echarts/echarts-all.js"></script>
<script type="text/javascript" src="${ctx}/js/app/policy/terminalAnalysis/terminalAnalysis.js"></script>  
</body>
</html>