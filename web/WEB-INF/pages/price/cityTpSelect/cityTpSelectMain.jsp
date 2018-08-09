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
	#tableHead .tHead{
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
<div class="breadLine">
    <ul class="breadcrumb">
        <li>价格监测<span class="divider">></span></li>                
        <li class="active">成交价查询</li>
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
										    	<select id="frequencyType" onchange="changeFrequencyType()">
										    	    <option value="1">周</option>
										    	    <option value="2">每半月</option>
										      		<option value="3" selected>月</option>
										      		<option value="4">季度</option>
										      		<option value="5">年</option>
										      	</select>
						    				</div>
						  				</div>
						  				<div class="control-group">
											<label class="control-label" for="analysisDimension">城市：</label>
											<input type="hidden" id="allCityNames" />
											<input type="hidden" id="allCityNameEns" />
										    <div class="controls">
										    	<div class="span2" style="width:90px">
											    	<a href="#cityModal" role="button" class="btn" data-toggle="modal">选择城市</a>
											    </div>
											    <div class="span10" style="margin-left:0px" id="cityModalResultContainer">
											    </div>
										    </div>
										</div>	
										<div class="control-group">
										    <label class="control-label">成交价类型： </label>
										     <div class="controls">
										    	<select id="tpType">
										    	    <option value="1" selected>一汽大众成交价</option>
										    	   <!-- 
										      		<option value="3">市场价</option>
										    	    <option value="2">最低参考价</option>
										    	   --> 
										      	</select>
						    				</div>
										</div>	
						  				<div class="control-group">
						  					<input type="hidden" id ="maxDate" value="${endDate}">
						    				<label class="control-label" for="startDate">时间：</label>
						    				<div id="dateYear" class="controls">
						    					<div class="form-inline">
							      					<span id="startDate-container" class="input-append date">
									  					<input type="text" value="${defaultBeginDate}"  readonly="readonly" class="input-mini white" placeholder="开始日期" id="startDate"><span class="add-on"><i class="icon-th"></i></span>
													</span>
													<span>至</span>						
							      					<span id="endDate-container" class="input-append date">
									  					<input type="text" value="${endDate}" readonly="readonly" class="input-mini white"  placeholder="结束日期" id="endDate"><span class="add-on"><i class="icon-th"></i></span>
													</span>
												</div>
											</div>
										</div>
						  				<div id="objectContainer">
						  				  <div id="model" style="display:block;">
						  					<div class="control-group" name = "objectModal" style="display:block;">
							    				<label class="control-label" for="analysisDimension">对象：</label>
							    				<div class="controls">
							      					<ul style="margin:0px;" class="subModelULContainer">
							      					   <li style="list-style: none;margin-bottom:10px;" class="subModelLIContainer">
							      					     <table style="width:570px;">
							      					       <tr>
							      					         <td valign="top" nowrap="nowrap" style="width:8%">
							      					           <div>
						                                         <a href="#" role="button" class="btn bodyTypeSelector" data-toggle="modal">车身形式</a>
					                                           </div>
					                                         </td>
			                                                 <td valign="top" nowrap="nowrap" style="width:29%">
				                                               <div  style="margin-left:0px" class="bodyTypeModalResultContainer">
				              								   </div>
			 											     </td>
			 											     <td valign="top" style="width:25%">	
				 											   <div>
															     <a href="#" role="button" class="btn subModelSelector" data-toggle="modal">选择车型</a>
	 														   </div>
	 														 </td>
															 <td valign="top" nowrap="nowrap" style="width:42%">
															   <div style="margin-left:0px" class="subModelModalResultContainer">
															   </div>
															   </td>
			 												 </tr>
														   </table>
													  </li>
							      					</ul>
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
						  				<div class="control-group">
								   			<div class="text-center">
			     								<button type="button" class="btn btn-primary" id="queryBtn">确认</button>
			     								<button type="reset" class="btn leftSpace" id="resetBtn">重置</button>
						    				</div>
									  </div>
									</form>
			              		</div>
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
        		<form action="${ctx }/price/cityTpSelect/exportExcel" method="post" id="exportFormId">
	             	<div class="isw-graph"></div>
	                 <h1>查询结果</h1>
	                 <ul class="buttons">
	                 	<li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('EN')"><span class="isw-download"></span>英文Excel</a></li>
	                    <li><a style="cursor:pointer;padding-right:5px;color:#fff;line-height: 35px" onClick="exportExcel('ZH')"><span class="isw-download"></span>中文Excel</a></li>
	                 </ul>
	                 <input type="hidden" id="languageType" name="languageType" /><!-- 导出语言标识 -->
	                 <input type="hidden" id="countryAvg" name="countryAvg" /><!-- 全国均价标识 -->
	                 <input type="hidden" id="citys" name="citys" /><!-- 选择城市数量 -->
                 </form> 
             </div>
             <div>
             <input type="hidden" id="getModelIndexId" />
             <div class="tab-content" style="height:500px;"> 
                <table cellpadding="0" cellspacing="0" width="100%" class="table dataTable" id="tSortable">
  				    <thead class="hide" id="tableHead">
  				     <tr id="headTr">
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
<%@ include file="/WEB-INF/pages/price/cityTpSelect/cityModalTemplate.jsp"%>
<%@ include file="/common/template/subModelModalTemplate.jsp"%>
<%@ include file="/common/template/bodyTypeModalTemplate.jsp"%>
<%@ include file="/common/template/autoVersinoModalTemplate.jsp"%>
<script type="text/javascript" src="${ctx}/js/app/price/cityTpSelect/cityTpSelect.js"></script>
</body>
</html>