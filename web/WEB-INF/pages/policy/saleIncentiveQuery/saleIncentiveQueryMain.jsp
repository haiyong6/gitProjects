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
	.tbodytext{
	    text-align: center!important;
	}
	.tHead{
	    white-space:nowrap;
	    background-color:rgb(0, 35, 90);
	    color:white;
	    font-family:Arial;
	    border: 1px solid #000000;
	}
	.tdSubModel{
	    white-space:nowrap;
	    font-family:Arial;
	    border: 1px solid #000000;
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
        <li>终端支持研究<span class="divider">></span></li>                
        <li class="active">销售激励查询</li>
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
			  				<div class="control-group">
			    				<label class="control-label" for="analysisDimension">时间：</label>
			    				<div id='dateYear' class="controls">
			    					<ul class="dateULContainer" style="margin:0px;"></ul>
			    				</div>
			  				</div>
			  				
			  				<div id="objectContainer">
				  				<div id="model" style="display:block;">
					  				<div class="control-group">
					    				<label class="control-label" for="analysisDimension">对象：</label>
					    				<div class="controls">
					    					<ul style="margin:0px;" class="subModelULContainer"></ul>
					    				</div>
					  				</div>
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
        		<form action="${ctx}/policy/saleIncentiveQuery/exportExcelMap" method="post" id="exportFormId">
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
            <div>
              <input type="hidden" id="getModelIndexId" />
              <div class="tab-content" style="height:500px;"> 
                <table cellpadding="0" cellspacing="0" width="100%" class="table dataTable" id="tSortable">
  				    <thead class="hide" id="tableHead">
	  			     <tr>
	    			  <th class="tHead">日期</br>Date</th>
	    			  <th class="tHead">厂商</br>OEM</th>
	                  <th class="tHead">车型级别</br>Segment</th>
                      <th class="tHead">细分市场</br>Sub-Segment</th>
	                  <th class="tHead">车身形式</br>Body Type</th>
	                  <th class="tHead">品牌</br>Brand</th>
	    			  <th class="tHead">车型</br>Model</th>
	    			  <th class="tHead">型号全称</br>Version Name</th>
	    			  <th class="tHead">型号编码</br>Code</th>
	    			  <th class="tHead">上市日期</br>Code</th>
	    			  <th class="tHead">指导价</br>MSRP</th>
	    			  <th class="tHead">返利</br>Rebate</th>                               
	    			  <th class="tHead">考核奖励</br>Bonus</th>
	    			  <th class="tHead">总促销</br>Total Tactical</th>
	    			  <th class="tHead">全款购车促销</br>Fully-paid car</br> purchase </br>promotion</th> 
	    			  <th class="tHead">经销商支持</br>Gross Supports</th> 
	    			  <th class="tHead">提车支持</br>STD</th>
	   				  <th class="tHead">零售支持</br>AAK</th>
	    			  <th class="tHead">用户激励</br>Customer Incentive</th>
	    			  <th class="tHead">礼品</br>Presents</th>
	    			  <th class="tHead">保险</br>Insurance</th>
	   			  	  <th class="tHead">保养</br>Maintenance</th>
	    			  <th class="tHead">人员奖励</br>Staff Reward</th>
	    			  <th class="tHead">金融贷款</br>Financial Loan</th>
	    			  <th class="tHead">置换支持</br>Trade-in Support</th>
	    			  <th class="tHead">开票价</br>Invoice Price</th>
	    			  <th class="tHead">经销商成本</br>Gross Cost</th>    
	    			  <th class="tHead">成交价</br>TP</th>
	    			  <th class="tHead">利润</br>Profit</th>
	    			  <th class="tHead">利润率</br>Profit Rate</br>(=利润/指导价)</th>
	    			  <th class="tHead">型号上月销量</br>Version Last Month Sales</th>
	    			  <th class="tHead">型号当月销量</br>Version Month Sales</th>  
    			    </tr>
				  </thead>
	              <tbody id="gridTbody">
	              </tbody>
                </table>	                     
              </div>
        	</div>
    	</div>                                
	</div>
</div>
<%@ include file="/common/template/subModelModalTemplate.jsp"%>
<%@ include file="/common/template/bodyTypeModalTemplate.jsp"%>
<%@ include file="/common/template/autoVersinoModalTemplate.jsp"%>
<script type="text/javascript" src="${ctx}/js/app/policy/saleIncentiveQuery/saleIncentiveQuery.js"></script>  
</body>
</html>