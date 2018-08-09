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
	.content .workplace .showConditionContainer .head .buttons > li.toggle a {
		border-left: 0px;
	    border-color: #222222;
	    background-image: url("../img/styles/default/show_arrow_up.png");
	    width: 60px;
	    height: 30px;
	    margin-right: 20px;
	}
    .content .workplace .showConditionContainer .head .buttons > li.toggle.active a {
    	border-left: 0px;
    	border-color: #222222;
    	background-image: url("../img/styles/default/show_arrow_down.png");
    	width: 60px;
    	height: 30px;
    	margin-right: 20px;
    }
    .modal-backdrop.in {
		z-index:1000000;
	}
	/* 设置弹出框显示层级为最高  */
	.modal {
		z-index:1000001;
	}
	#tabs-competingProducts .form-inline .checkbox,
	#tabs-segment .form-inline .checkbox,
	#tabs-brand .form-inline .checkbox,
	#tabs-manf .form-inline .checkbox {
		margin-right:10px;
	}
	.tbodytext {
	    text-align: center!important;
	}
	.tHead {
	    white-space:nowrap;
	    background-color:rgb(0, 35, 90);
	    color:white;
	    font-family:Arial;
	    border: 1px solid #000000;
	}
	.tdSubModel {
	    white-space:nowrap;
	    font-family:Arial;
	    border: 1px solid #000000;
	}
	.position li {
		float: left;
	    border-right: 1px solid #d6d6d6;
	    margin-right: 6px;
	    padding-right: 6px;
	    cursor: move;
	}
	.position li span {
	    display: block;
	    float: left;
	    text-align: center;
	    width: 70px;
	    height: 15px;
	    line-height: 15px;
	    border: 1px solid #d9dadd;
	    background-color: #f5f5f5;
	    color: #767676;
    }
    .position .edit,.position .confirm {
        color: #4666ad;
    	text-decoration: underline;
    	font-weight: bold;
    	cursor: pointer;
    }
    .position li.last {
        border: 0;
    }
	.position ul {
		margin: 0;
	    padding: 0;
	    border: 0;
	    list-style-type: none;
	    font-size: 100%;
	}
	.ex {
	    width: 70px;
	    height: 15px;
	    border: 1px solid #d9dadd;
	    background-color: #FFEE99;
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
        <li>价量分析<span class="divider">></span></li>                
        <li class="active">车型价格段分析</li>
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
						    	<label class="control-label" for="analysisDimension">图表类型：</label>
						    	  <div class="controls">
							    	 <select id="chartType">
							      		<option value="1">散点图</option>
							      		<option value="2">跨度图</option>
							      	 </select>
			    				  </div>
						  	</div>
						  	
			  				<div class="control-group">
			    				<label class="control-label" for="analysisDimension">价格类型：</label>
			    				<div class="controls">
							    	<select id="priceType" >
							      		<option value="1">指导价</option>
							      		<option value="2">成交价</option>
							      	</select>
			    				</div>
			  				</div>
			  				
			  				<div class="control-group">
			    				<label class="control-label" for="startDate">时间：</label>
			    				<div class="controls">
			    					<div class="form-inline">
				      					<span id="startDate-container" class="input-append date">
						  					<input type="text" value="${defaultBeginDate}"  readonly="readonly" class="input-mini white" placeholder="日期" id="startDate"><span class="add-on"><i class="icon-th"></i></span>
										</span>
									</div>
								</div>
							</div>
			  				
			  				<div class="control-group">
						    	<label class="control-label" for="analysisDimension">对象：</label>
						    	<div class="controls"> 
			                       <div class="span2" style="width:90px"> 
		  						      <a href="#bodyTypeModal" role="button" name = "bodyTypeSelector" class="btn" data-toggle="modal">车身形式</a>	
			                       </div>  
			                       <div class="bodyTypeModalResultContainer" style="margin-left:0px;" id="bodyTypeModalResultContainer"></div>  
							    </div>
							</div>
							
							<div class="control-group">
							    <label class="control-label" for="analysisDimension"></label>
							    <div class="controls"> 
					      	  	   <div class="span2" style="width:90px;">
						    	      <a href="#subModelModal" role="button" class="btn" data-toggle="modal">选择车型</a>
						    	   </div>
						    	   <div class="subModelModalResultContainer" style="margin-left:0px;" id="subModelModalResultContainer"></div>
						    	</div>
					    	</div>
					    	
			  				<div class="control-group" style="clear:both;">
					   			<div class="text-center">
     								<button type="button" class="btn btn-primary" id="queryBtn">确认</button>
     								<button type="reset" class="btn leftSpace" id="resetBtn">重置</button>
			    				</div>
						  </div>
						</form>
              		</div>
              		</td>
              		<td style="width: 45%;">
            		   <img alt="" src="${ctx }/img/outGive/subModelPriceAnalysis.png">
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
        		<form action="${ctx}/price/subModelPriceAnalysis/exportExcel" method="post" id="exportFormId">
	             	<div class="isw-graph"></div>
	                 <h1>查询结果</h1>
	                 <ul class="buttons" id = "exportButtons">
	                 	<li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('EN')"><span class="isw-download"></span>英文Excel</a></li>
	                    <li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('ZH')"><span class="isw-download"></span>中文Excel</a></li>
	                 </ul>
	                 <input type="hidden" id="languageType" name="languageType" /><!-- 导出语言标识 -->
	                 <input type="hidden" id="ex_chartType" name="chartType" />
	                 <input type="hidden" id="ex_priceType" name="priceType" />
	                 <input type="hidden" id="ex_showType" name="showType" />
	                 <input type="hidden" id="ex_sortLabel" name="sortLabel" />
                 </form> 
             </div>
             <ul class="nav nav-tabs" id="otherTab">
                	<li class="active"><a href="#tabs-1" id="showChart" data-toggle="tab" value="2">图表</a></li>
             </ul>  
             <div class="tab-content showConditionContainer"> 
               <div class="span12" id="setDiv">
                 <div class="head clearfix" style="background:url('${ctx}/img/styles/default/backgrounds/header1.jpg') left top repeat-x;border: 0px;">
                    <input type="text" value="显示设置" readOnly style="width: 90px; background-color: white;"/>
                    <ul class="buttons">
            	      <li class="toggle"><a href="#"></a></li>
                    </ul> 
                 </div>
                    <div class="block" style="background-color: white;font-size: 10pt; font-family: 微软雅黑; margin-bottom: 0px;">
                      <table>
                        <tr>
                          <td style="width: 12%;text-align: left;" class="showTd">
  		                                                手动排挡显示：
                          </td>
                           <td style="width: 12%;text-align: left;display:none" class="showTd1"></td>
                          <td style="width: 20%;" class="showTd">
                            <input type="radio" value="1" name="showType" class="showType"  checked/>&nbsp;同列显示&nbsp;&nbsp;
  		                    <input type="radio" value="2" name="showType" class="showType" />&nbsp;分开显示
                          </td>
                          <td style="width: 20%;display:none" class="showTd1"></td>
                          <td style="width: 8%;"></td>
                          <td style="width: 12%;text-align: right;" class="showTd">
        	                                       标签显示内容：
                          </td>
                           <td style="width: 12%;text-align: right;display:none" class="showTd1"></td>
                          <td style="width: 35%;" class="showTd">
                            <div class="position" id="sortLabel" style="width:300px;">
								<div class="pick" style="display: none;">
								  <ul>
								    <li><label style="padding: 0 4px;"><input type="checkbox" name="sortLabel" class="sortLabel" value="1" checked="checked" />型号名称</label></li>
									<li><label style="padding: 0 4px;"><input type="checkbox" name="sortLabel" class="sortLabel" value="2" />价格</label></li>
									<li><label style="padding: 0 4px;"><input type="checkbox" name="sortLabel" class="sortLabel" value="3" />车身形式</label></li>
								  </ul>
								  <a class="confirm">确认</a>
								</div>
								<div class="order" >
								   <ul class="sortTable">
								     <li rel="1" on="true" style="display: list-item;"><input type="hidden" value="1" class="orderLabel" /><span>型号名称</span></li>
								     <li rel="2" on="false" style="display: none;"><input type="hidden" value="2" class="orderLabel" /><span>价格</span></li>
								     <li rel="3" on="false" style="display: none;"><input type="hidden" value="3" class="orderLabel" /><span>车身形式</span></li>
								   </ul>
								   <a class="edit">修改</a>
								</div>
							 </div>
                          </td>
                          <td style="width: 35%;display:none" class="showTd1"></td>
                          <td style="width: 5%;"></td>
                          <td style="width: 5%;" rowspan="2" >
                            <input type="button" value="刷新图表" id="refreshChart" onclick="refreshChart()"/>
                          </td>
                        </tr>
                         <tr>
                          <td class="subModelOrderTd" style="width: 8%;text-align: left;">
                                                                 车型顺序：
                          </td>
                          <td class="subModelOrderTd" style="width: 70%;" colspan="4">
                          <div class="position" id="sortSubModel">
                            <div class="pick" style="display:none;">
                               <ul></ul>
                               <a class="confirm">确认</a>
                            </div>
                            <div class="order" id="orderSort" >
                               <ul class="sortTable"></ul>
                               <a class="edit">增加或删除</a>
                            </div>
                          </div>
                          </td>
                        </tr>
                      </table>
                      </div>
                 </div>  
                 <div class="span12" style="background-color:#FFF;display:none;margin-left:0px;padding-top:15px;" align="center" id="setDivName">
                 	<font size="5%;" bold="bold">Price Ladder</font>
                 </div> 
                  <div class="span12" style="background-color:#FFF;display:none;margin-left:0px;padding-top:15px;"  id="chartTitleDivNoData">
                 	<font size="5%;" bold="bold">No Data!</font>
                 </div>  
                 <br/>
	             <div class="tab-pane active" id="tabs-1">
	               	<div class="row-fluid " >
	               	<!-- <div class="span12" id="setDivName" >
	                   Price Ladder
	                    </div> -->
	                 	<div class="span12" style="background-color:#FFF;overflow-x:auto;overflow-y:auto;" id="chartTitleDiv" >
	                    </div>
	                </div>
	             </div>
	         </div>
    	</div>                                
	</div>
</div>
<%@ include file="/common/template/subModelModalTemplate.jsp"%>
<%@ include file="/common/template/bodyTypeModalTemplate.jsp"%>
<!-- 
<script type="text/javascript" src="${ctx}/js/plugins/echarts/echarts-all.js"></script>
-->
<script type="text/javascript" >
	define = undefined;
</script>
<script type="text/javascript" src="${ctx}/js/plugins/jquery/jquery-ui.js"></script>
<script type="text/javascript" src="${ctx}/js/plugins/echarts/echarts3/echarts.js"></script>
<script type="text/javascript" >
	var myChart = echarts.init(document.getElementById("chartTitleDiv"));
</script>
<script type="text/javascript" src="${ctx}/js/app/price/subModelPriceAnalysis/subModelPriceAnalysisMain.js"></script>  
</body>
</html>