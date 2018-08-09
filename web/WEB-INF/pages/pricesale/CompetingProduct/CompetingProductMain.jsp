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

 	.content .workplace .head .tab-content .buttons  li.toggle a {
		border-left: 0px;
	    border-color: #222222;
	    background-image: url("../../img/styles/default/show_arrow_up.png");
	    width: 60px;
	    height: 30px;
	    margin-right: 20px;
	}
    .content .workplace .head .tab-content .buttons  li.toggle.active a {
    	border-left: 0px;
    	border-color: #222222;
    	background-image: url("../../img/styles/default/show_arrow_down.png");
    	width: 60px;
    	height: 30px;
    	margin-right: 20px;
    } 
	#tabs-competingProducts .form-inline .checkbox,
	#tabs-segment .form-inline .checkbox,
	#tabs-brand .form-inline .checkbox,
	#tabs-manf .form-inline .checkbox{
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
	
	.tbodytext{text-align: center!important;}
	/* 图表标题  */
	.tdSubModel{text-align: center;background-color:#3B7AB2;/*  width:133px; */ height:55px;  font-size: large;  
				font-weight: bold; /* border-right:5px solid #F9F9F9; */ border-left:1px solid #3B7AB2;border-right:1px solid #F9F9F9;
				/* margin-right:10px; */
				border-bottom:1px dashed black;}
	/* 图表空格  */			
	.tdSubModelk{
				background-color:#F9F9F9;/* width:10px; */height:55px;
	}
	/* 图表内容列  */
	.tdSubModel1{
				text-align: center;  /* width:133px; */  background-color:#F9F9F9;
				border-left:1px solid black;/* border-right:5px solid #F9F9F9; */
				border-top:1px dashed black;border-bottom:1px dashed black;border-right:1px solid black;
				/* border-style:dotted solid; */
	}
	/* 图表内容列 空格  */
	.tdSubModelk1{
				background-color:#F9F9F9;/* width:10px; */
	}
	/* 图表下方表格 */
	.tdSubModel2{
				text-align: center;/* width:133px; */height:10px;background-color:#F9F9F9;/* border-top:1px solid white */
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
	    width: 80px;
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
	    width: 80px;
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
<input type="hidden" id ="moduleCode" value="${moduleCode}">
<div class="breadLine">
    <ul class="breadcrumb">
        <li>价量分析<span class="divider">></span></li>                
        <li class="active">竞品价量分析</li>
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
			    				<label class="control-label" for="analysisDimension">时间段：</label>
			    				<div id='dateYear' class="controls">
			    					<ul class="dateULContainer" style="margin:0px;"></ul>
			    				</div>
			  				</div>
                    		
			  				<div class="control-group">
			    				<label class="control-label" for="analysisDimension">价格类型：</label>
			    				<div class="controls">
							    	<select id="priceType"  >
							      		<option value="0">指导价</option>
							      		<option value="1" selected>成交价</option>
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
			  				
			  				
			  				
			  				<div id="objectContainer">
			  				  <div id="objectDiv" style="display:block;">
					  			<div class="control-group">
					    		  <label class="control-label" for="analysisDimension">对比对象：</label>
					    			<div class="controls">
					    			</div>
					  			</div>
			  				  </div>
			  				  <div id="model" style="display:block;">
					  				<div class="control-group">
					    				<label class="control-label" for="analysisDimension">对象：</label>
					    				<div class="controls">
					    					<ul style="margin:0px;" class="subModelULContainer"></ul>
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
              		</td>
            				<td>
            					 <%-- <img alt="" src="${ctx }/img/outGive/c.png">  --%>
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
        		<form action="${ctx}/exportCompetingProductData" method="post" id="exportFormId">
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
	                 <input type="hidden" id="isScale" name="isScale"><!-- 是否勾选占比 -->
                 </form> 
             </div>
             <div class="block-fluid">
             	<!-- 保存获取车形弹出框当前车型下标 -->
                <input type="hidden" id="getModelIndexId" />
             	<ul class="nav nav-tabs " id="otherTab" >
                	<li class="active"><a href="#tabs-1" id="showChart" data-toggle="tab" value="2" style="background-color:#F9F9F9">图表</a></li>
                  	<!-- <li><a href="#tabs-2" data-toggle="tab" value="1">数据</a></li> -->
                </ul>        
            	<div class="tab-content"  >   
            	
            	   <div class="span12">
                 <div class="head clearfix" style="background:url('${ctx}/img/styles/default/backgrounds/header1.jpg') left top repeat-x;border: 0px;">
                    <input type="text" value="显示设置" readOnly style="width: 90px; background-color: white;"/>
                    <ul class="buttons">
            	      <li class="toggle"><a href="#" ></a></li>
                    </ul> 
                 </div>
                    <div class="block" style="background-color: white;font-size: 10pt; font-family: 微软雅黑; margin-bottom: 0px;">
                      <table>
                        <tr>
                          <td class="subModelOrderTd" style="width: 8%;text-align: right;">
                                                                 车型顺序：
                          </td>
                          <td class="subModelOrderTd" style="width: 70%;" colspan="4">
                          <div class="position" id="sortSubModel">
                            <div class="pick" style="display:none;">
                               <ul></ul>
                               <a class="confirm">确认</a>
                            </div>
                            <div class="order">
                               <ul class="sortTable"></ul>
                               <a class="edit">增加或删除</a>
                            </div>
                          </div>
                          </td>
                          <td style="width: 5%;" rowspan="2">
                            <input type="button" value="刷新图表" id="refreshChart" onclick="refreshChart()"/>
                          </td>
                        </tr>
                      </table>
                      </div>
                 </div>
            	                 
	             	<div class="tab-pane active" id="tabs-1" style="background-color:#F9F9F9" >
	                 	<div class="row-fluid" id="chartNew" style="margin-left:10px;">
	                 	 	<div id = "priceTypeDiv" class="span12" style = "margin-left: 80px;margin-top:20px; display: none;">
		                     			<input type="checkbox" name = "isPromotion" id = "isPromotion" style = "margin-top: -3px;width: 20px;height: 20px;"><font size="4px;">占比</font></input>&nbsp;&nbsp;&nbsp;
	                     			</div>
		                     	<div  class="span12" style="background-color:#F9F9F9; width:400px; float:left;/* margin-top:-163px; */ " id="chartTitleDiv">
					                         <div class="span12" style="background-color:#F9F9F9; /* width:1128px; */ "  id="chartTitleDiv1"> 
				                        	</div>
					                         <div class="span12" style="background-color:#F9F9F9; /* width:1128px; */ "  id="chartTitleDiv3"> 
				                        	</div>
				                        	
		                        </div>
		                        
		                        <div class="span12" style="background-color:#F9F9F9;  width:600px;  float:left;" id="chartTitleDiv2">
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
		define = undefined;
	</script>
	<script type="text/javascript" src="${ctx}/js/plugins/echarts/echarts3/echarts.min.js"></script><!-- echarts3版本 -->
	<script type="text/javascript" src="${ctx}/js/plugins/jquery/jquery-ui.js"></script>
	<script type="text/javascript" >
	var myChart = echarts.init(document.getElementById("chartTitleDiv1"));
	</script>
<script type="text/javascript" src="${ctx}/js/app/pricesale/CompetingProduct/CompetingProduct.js"></script>  
</body>
</html>