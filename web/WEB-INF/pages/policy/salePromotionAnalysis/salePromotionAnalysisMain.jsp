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
	.modal-backdrop.in{
		z-index:1000000;
		position:absolute;
	}
	/* 设置弹出框显示层级为最高  */
	.modal{
		z-index:1000001;
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
        <li class="active">销量促销分析</li>
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
										      		<option value="1">型号</option>
										      		<option value="2" selected>车型</option>
										      		<option value="3">厂商品牌</option>										      		
										      		<option value="4">品牌</option>
										      		<option value="5">系别</option>
										      		<option value="6">级别</option>
										      	</select>
										&nbsp;&nbsp;     	
						  				<h7><font color="red">提示: 时间选择跨度较大时,建议对象选择不要超过4个</font></h7>
						    				</div>
						  				</div>	
						  				<div class="control-group" name="objectType1">
						    				<label class="control-label" for="analysisDimension">对象：</label>
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#subModelModal" role="button" class="btn" data-toggle="modal">选择车型</a>
							    				</div>
							    				<input type="hidden" value="1,2,3" class="selectedPooAttributeIds" >
							    				<div class="span10" style="margin-left:0px" id="subModelModalResultContainer"></div>
						    				</div>
										</div>
						  				<div class="control-group" name="objectType1">
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#versionModal" role="button" class="btn" data-toggle="modal">选择型号</a>
							    				</div>
							    				<div class="span10" style="margin-left:0px" id="versionModalResultContainer"></div>
						    				</div>
						  				</div>
						  				<div class="control-group" name="objectType1">
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#autoVersionModal" role="button" class="btn" data-toggle="modal">常用对象</a>
							    				</div>
							    				<div class="span10" style="margin-left:0px" id="autoVersionModalResultContainer"></div>
						    				</div>
						  				</div>
						  				
										<div class="control-group" name="objectType3" style="display: none;">
						    				<label class="control-label" for="analysisDimension">对象：</label>
						    				<div class="controls">
						      					<div class="span2" style="width:150px">
							    					<a href="#manfModal" role="button" name="manfSelector" class="btn" data-toggle="modal">选择厂商品牌</a>
							    				</div>
							    				<div class="manfModalResultContainer" style="margin-left:0px" id="manfModalResultContainer"></div>
						    				</div>
										</div>
										
										<div class="control-group" name="objectType4" style="display: none;">
						    				<label class="control-label" for="analysisDimension">对象：</label>
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#brandModal" role="button" class="brandSelector btn" data-toggle="modal">选择品牌</a>
							    				</div>
							    				<div class="brandModalResultContainer" style="margin-left:0px" id="brandModalResultContainer"></div>
						    				</div>
										</div>
										
										<div class="control-group" name="objectType5" style="display: none;">
						    				<label class="control-label" for="analysisDimension">对象：</label>
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#origModal" role="button" name="origSelector" class="btn" data-toggle="modal">选择系别</a>
							    				</div>
							    				<div class="origModalResultContainer" style="margin-left:0px" id="origModalResultContainer"></div>
						    				</div>
										</div>
										
										<div class="control-group" name="objectType6" style="display: none;">
						    				<label class="control-label" for="analysisDimension">对象：</label>
						    				<div class="controls">
						      					<div class="span2" style="width:90px">
							    					<a href="#segmentModal" role="button" name="segmentSelector" class="btn" data-toggle="modal">选择级别</a>							    					
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
            				<td style="width: 45%;">
            					<img alt="" src="${ctx }/img/outGive/salePromotionAnalysis.png">
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
        		<form action="${ctx }/policy/salePromotionAnalysis/exportExcel" method="post" id="exportFormId">
	             	<div class="isw-graph"></div>
	                 <h1>查询结果</h1>
	                 <ul class="buttons">
	                 	<li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('EN')"><span class="isw-download"></span>英文Excel</a></li>
	                    <li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('ZH')"><span class="isw-download"></span>中文Excel</a></li>
	                 </ul>
	                 <input type="hidden" name="objectType" id="ex_objectType"/><!-- 对象类型 -->
	                 <input type="hidden" name="beginDate" id="ex_beginDate"/><!-- 开始时间 -->
	                 <input type="hidden" name="endDate" id="ex_endDate"/><!-- 结束时间 -->	                 
	                 <input type="hidden" name="versionIds" id="ex_versionIds"/><!-- 型号ID -->
	                 <input type="hidden" name="subModelIds" id="ex_subModelIds"/><!-- 车型ID -->
	                 <input type="hidden" name="manfIds" id="ex_manfIds"/><!-- 厂商品牌ID -->	                 
	                 <input type="hidden" name="brandIds" id="ex_brandIds"/><!-- 品牌ID -->
	                 <input type="hidden" name="origIds" id="ex_origIds"/><!-- 系别ID -->
	                 <input type="hidden" name="gradeIds" id="ex_gradeIds"/><!-- 级别ID -->
	                 <input type="hidden" name="bodyTypeIds" id="ex_bodyTypeIds"/><!-- 车身形式ID -->	                 
	                 <input type="hidden" name="languageType" id="languageType" /><!-- 导出语言类型 -->
	                 <input type="hidden" name="priceMax" id="priceMax" />
	                 <input type="hidden" name="priceMin" id="priceMin" />
	                 <input type="hidden" name="priceTick" id="priceTick" />
	                 <input type="hidden" name="saleMax" id="saleMax" />
	                 <input type="hidden" name="saleMin" id="saleMin" />
	                 <input type="hidden" name="saleTick" id="saleTick" />
	                 <input type="hidden" name="promotionMax" id="promotionMax" />
	                 <input type="hidden" name="promotionMin" id="promotionMin" />
	                 <input type="hidden" name="promotionTick" id="promotionTick" />
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
	                     	<div class="span12" style="background-color:#FFF;overflow-x:auto;overflow-y:auto;" id="chartTitleDiv" >
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
	                             		<thead id="dataThead" class="hide">
		                                	<tr>		                                	
		                                	  <th style="text-align: center;">日期</th>
		                                	  <th id="objectTypeName" style="text-align: center;"></th>
		                                      <th style="text-align: center;">促销</th>   
		                                      <th style="text-align: center;">销量</th>                               
		                                  	</tr>
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
<script type="text/javascript" src="${ctx}/js/app/policy/salePromotionAnalysis/salePromotionAnalysisMain.js"></script>  
<!-- 
<script type="text/javascript" >
	define = undefined;
</script>
<script type="text/javascript" src="${ctx}/js/plugins/echarts/echarts3/echarts.js"></script>
 -->
<script type="text/javascript" src="${ctx}/js/plugins/echarts/echarts-all.js"></script>
<script type="text/javascript" >
	var myChart = echarts.init(document.getElementById("chartTitleDiv"));
</script>
</body>
</html>