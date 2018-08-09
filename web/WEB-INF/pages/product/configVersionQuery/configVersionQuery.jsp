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
	#tSortable tbody td {
		border-bottom: 1px solid #DDDDDD;
	}
	</style>
</head>
<body>
<div class="breadLine">
    <ul class="breadcrumb">
        <li>产品配置<span class="divider">></span></li>                
        <li class="active">型号配置查询</li>
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
								<label class="control-label" for="analysisDimension">配置选择：</label>
							    <div class="controls">
							    	<div class="span2" style="width:90px">
								    	<a href="#configModal" role="button" class="btn" data-toggle="modal">请选择</a>
								    </div>
								    <div class="span10" style="margin-left:0px" id="configModalResultContainer">
									    	<ul class=selectorResultContainer>
									    	</ul>
									</div>
							    </div>
							</div>	
							<div class="control-group">
									<label class="control-label" for="analysisDimension">价格段：</label>
									<input type="text" id="beginPrice" style="width: 80px;margin-left: 20px;" maxlength="6" onkeydown="checkText(event,this)" /> 
									- 
									<input type="text" id="endPrice" style="width: 80px;" maxlength="6" onkeydown="checkText(event,this);" /> 万
							</div>
							<div class="control-group">
									<label class="control-label" for="analysisDimension">车身形式：</label>
									<div class="controls" style="margin-top: 3px;" id="subModelBodyType">
								    	<c:forEach items="${subModelBodyType }" var="subBody">
								      			<input type="checkbox" name="subModelBodyTypeCheckbox" value="${subBody.id }" title="${subBody.text }" style="margin-top: 0px;"/>
								      			${subBody.text }&nbsp;&nbsp;
								      	</c:forEach>
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
		<div class="span12">                    
        	<div class="head clearfix">
        		<form action="${ctx }/exportProductConfigVersion" method="post" id="exportFormId">
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
            	<div class="tab-content">                    
	             	<div class="tab-pane active" id="tabs-1">
	                    <h4 id="tableTitle" style="text-align: center"></h4>
	                 	<div class="row-fluid" style="height:500px;overflow: auto;">
	                         		<table cellpadding="0" cellspacing="0" style="width:100%;" class="table dataTable" id="tSortable">
	                             		<thead>
		                             			<tr>
		                             				<th>型号编码</th>
		                             				<th>级别</th>
		                             				<th>生产厂</th>
		                             				<th>车型</th>
		                             				<th>型号名称</th>
		                             				<th>排量</th>
		                             				<th>排档方式</th>
		                             				<th>车身形式</th>
		                             				<th>上市日期</th>
		                             				<th>年款</th>
		                             				<th>MSRP</th>
		                             				<th>Mix</th>
		                             			</tr>
			                            </thead>
			                            <tbody></tbody>
	                          		</table>
	                     </div>
	                 </div>
            	</div>
        	</div>
    	</div>
	</div><!-- row-fluid  图表、数据展示 -->
</div>
<%@ include file="/common/template/configModalTemplate.jsp"%>
<%@ include file="/common/template/subModelModalTemplate.jsp"%>
<script type="text/javascript" src="${ctx }/js/app/product/productConfigGlobal.js"></script>
<script type="text/javascript" src="${ctx }/js/app/product/configVersionQuery/configVersionQuery.js"></script>
</body>
</html>

