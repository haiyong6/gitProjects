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
		
		/**
		简单表格样式 
		*/
		#simpleTableId table tbody td{border: 3px solid #cccccc;text-align: left;padding-left: 10px;}
		#simpleTableId table thead td{border: 2px solid #cccccc; background-color: #002A5B;font-size: 
		16px;text-align: center;height: 30px;color: #fff;font-weight: bold;}
	
		/**
		明细表格样式 
		*/
		#detailTableId table tbody td{border: 1px solid #cccccc;text-align: center}
		#detailTableId table thead td{border: 1px solid #cccccc; background-color: #002A5B;font-size: 
		16px;text-align: center;height: 30px;color: #fff;font-weight: bold;}
	</style>
	
	<script type="text/javascript">
	function nextPage() {
		if(pageNumber < maxPageNumber){
			pageNumber++;
		}
		 createSimpleTable(selectData);//简单	
		 createDetailedTable(selectData);//详细
	}
	function prevPage() {
		if(pageNumber > 1){
			pageNumber--;
		}
		 createSimpleTable(selectData);//简单	
		 createDetailedTable(selectData);//详细
	}
	function jumpPage(){
		if( $.trim(($("#jump").val()) != "") && $("#jump").val() <= maxPageNumber &&  $("#jump").val() > 0) {
			pageNumber = $("#jump").val();
			 createSimpleTable(selectData);//简单	
			 createDetailedTable(selectData);//详细
			} else{
				alert("已在页码范围之外！");
				}
	}
	</script>
</head>
<body>
<div class="breadLine">
    <ul class="breadcrumb">
        <li>商务政策<span class="divider">></span></li>                
        <li class="active">促销查询</li>
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
			    				<label class="control-label" for="analysisDimension">分析维度：</label>
			    				<div class="controls">
							    	<select id="analysisDimensionType" onchange="changeDateType()" >
							      		<option value="1">车型对比</option>
							      		<option value="2">时间对比</option>
							      	</select>
			    				</div>
			  				</div>
                    	<input type="hidden" value="${defaultBeginDate}" id="hiddenSDate" >
                    	<input type="hidden" value="${endDate}" id="hiddenEDate" >
			  				<div class="control-group">
			    				<label class="control-label" for="startDate">时间：</label>
			    				<div class="controls">
			    					<div class="form-inline">
				      					<span id="startDate-container" class="input-append date">
						  					<input type="text" value="${endDate}"  readonly="readonly" class="input-mini white" placeholder="开始日期" id="startDate"><span class="add-on"><i class="icon-th"></i></span>
										</span>
										
										<span class="2" id="dateSelectText" style="visibility: hidden">至</span>						
				      					<span id="endDate-container" class="input-append date" style="visibility: hidden">
						  					<input type="text"  value="${endDate}" readonly="readonly" class="input-mini white"  placeholder="结束日期" id="endDate"><span class="add-on" id="endDateButton"><i class="icon-th"></i></span>
										</span>
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
					   			<div class="text-center">
     								<button type="button" class="btn btn-primary" id="queryBtn">确认</button>
     								<button type="reset" class="btn leftSpace" id="resetBtn">重置</button>
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
		<div class="span12" >                    
        	<div class="head clearfix">
        		<form action="${ctx }/exportPolicyExcel" method="post" id="exportFormId">
	             	<div class="isw-graph"></div>
	                 <h1>查询结果</h1>
	                 <ul class="buttons">
	                    <li style="margin-right: 10px;">
	                    	<a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('ZH')">
	                    		<span class="isw-download"></span>下载
	                    	</a>
	                    </li>
	                 </ul>
	                 <input type="hidden" name="analysisDimensionType" id="analysisId" /><!-- 分析维度 -->
	                 <input type="hidden" name="beginDate" id="time" /><!-- 分析维度 -->
	                 <input type="hidden" name="modelName" id="modelName" /><!-- 分析维度 -->
                 </form> 
             </div>
             <div class="block-fluid" >
             	<ul class="nav nav-tabs" id="otherTab">
                	<li class="active" id="simple"><a href="#tabs-1" data-toggle="tab" value="2">简单</a></li>
                  	<li id="detail"><a href="#tabs-2" data-toggle="tab" value="1">明细</a></li>
                </ul>        
            	<div class="tab-content">                    
	             	<div class="tab-pane active" id="tabs-1">
	                 	<div class="row-fluid" >
	                     	<div class="span12" style="background-color:#FFF;" id="simpleTableId">
	                        </div>
	                     </div>
	                 </div>
	                 <div class="tab-pane" id="tabs-2">
	                 	<div class="row-fluid">
		                 	<div id="detailTableId">
		                    </div>
	                 	</div>
					</div>  
				<div >
              	 <table cellpadding="0" cellspacing="0" width="100%" class="table dataTable" id="paging" style="margin-left:340px;" >
                </table>
              </div>                      
            	</div>
        	</div>
    	</div>                                
	</div><!-- row-fluid  图表、数据展示 -->
</div>


<%@ include file="/common/template/modelSalesAmountFawTemplate/subModelModalTemplate.jsp"%>
<script type="text/javascript">
    var beginDate = "${beginDate}";
	var endDate = "${endDate}";
	var defaultBeginDate = "${defaultBeginDate}";
</script>
<script type="text/javascript" src="${ctx}/js/app/policy/policyMonthAnaly/PolicyMonthAnaly.js"></script>  
</body>
</html>

