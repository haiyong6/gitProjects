<%@ page language="java" errorPage="/error.jsp"  pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<ul class="navigation">            
    <li class="openable active" style="background-image: none;border: none;">
        <a href="index.html#">
            <span class="icon-tasks" style="margin-top:10px;margin-left:5px;float:left;"></span><span class="text" style="color:#000;text-shadow:none">价格监测</span>
        </a>
        <ul>
        	
            <li <c:if test="${menuId eq '001' }">class='menu-select'</c:if> >
                <a href="${ctx}/profit/profitOfMain.do">
                    <span class="icon-th"></span><span class="text" >价格利润走势yin</span>
                </a>                  
            </li>  
           
            <li <c:if test="${menuId eq '012' }">class='menu-select'</c:if> >
                <a href="${ctx}/price/cityTpSelectMain">
                    <span class="icon-th"></span><span class="text">成交价查询</span>
                </a>                  
            </li>
            
            <li <c:if test="${menuId eq '021' }">class='menu-select'</c:if> >
                <a href="${ctx}/price/msrpQueryMain">
                    <span class="icon-align-justify"></span><span class="text">指导价查询</span>
                </a>  
                     
            </li>
            
            <li <c:if test="${menuId eq '020' }">class='menu-select'</c:if> >
                <a href="${ctx}/price/subModelPriceAnalysisMain">
                    <span class="icon-signal"></span><span class="text">车型价格段分析</span>
                </a>     
                <img src="${ctx}/img/outGive/new/new.png" >                
            </li>   
           
            <li <c:if test="${menuId eq '002' }">class='menu-select'</c:if> >
                <a href="${ctx}/cityTpRatio/cityTpRatioMain">
                    <span class="icon-fire"></span><span class="text">成交价城市对比yin</span>
                </a>                  
            </li>  
           
             
            <li <c:if test="${menuId eq '003' }">class='menu-select'</c:if> >
                <a href="${ctx}/versionDiscountRatio/versionDiscountRatio">
                    <span class="icon-chevron-right"></span><span class="text">车型折扣对比</span>
                </a>                  
            </li>   
           
             
            <li <c:if test="${menuId eq '004' }">class='menu-select'</c:if> >
                <a href="${ctx}/profit/CityProfitDistributionMain.do">
                    <span class="icon-th-large"></span><span class="text">利润城市分布yin</span>
                </a>                  
            </li>   
              
            
            <li <c:if test="${menuId eq '005' }">class='menu-select'</c:if> >
                <a href="${ctx}/priceIndex/priceIndexMain">
                    <span class="icon-list"></span><span class="text">价格降幅</span>
                </a>                  
            </li>  
            
             <li <c:if test="${menuId eq '023' }">class='menu-select'</c:if> >
                <a href="${ctx}/cityPriceIndex/cityPriceIndexMain">
                    <span class="icon-road"></span><span class="text">区域价格降幅</span>
                </a>     
                <img src="${ctx}/img/outGive/new/new.png" >             
            </li>  
        </ul>   
     </li>
     
     <li class="openable active" style="background-image: none;border: none;">
     	<a href="index.html#">
            <span class="icon-tasks" style="margin-top:10px;margin-left:5px;float:left;"></span><span class="text" style="color:#000;text-shadow:none">销量查询</span>
          </a>
           <ul>
           
           <li <c:if test="${menuId eq '022' }">class='menu-select'</c:if>>
                <a href="${ctx}/salesQuerys/salesAmountFawMain">
                    <span class="icon-th-large"></span><span class="text">销量查询</span>
                </a> 
                <img src="${ctx}/img/outGive/new/new.png" >                   
            </li> 
           
            <li <c:if test="${menuId eq '019' }">class='menu-select'</c:if>>
                <a href="${ctx}/salesQuerys/modelSalesAmountFawMain">
                    <span class="icon-list-alt"></span><span class="text">销售比例查询</span>
                </a> 
                               
            </li> 
            </ul> 
        
     </li>
     
     <li class="openable active" style="background-image: none;border: none;">
	        <a href="index.html#">
	            <span class="icon-tasks" style="margin-top:10px;margin-left:5px;float:left;"></span><span class="text" style="color:#000;text-shadow:none">价量分析</span>
	        </a>
	        <ul>
	       
	            <li <c:if test="${menuId eq '008' }">class='menu-select'</c:if> >
	                <a href="${ctx}/pricesale/VolumeByPriceRangeMain.do">
	                    <span class="icon-th"></span><span class="text">价格段销量分析yin</span>
	                </a>                  
	            </li>
	            
	            <li <c:if test="${menuId eq '018' }">class='menu-select'</c:if> >
	                <a href="${ctx}/pricesale/competingProduct/competingProductMain">
	                    <span class="icon-th-list"></span><span class="text">竞品价量分析yin</span>
	                </a>                  
	                <img src="${ctx}/img/outGive/new/new.png" >   
	            </li>
	            <li <c:if test="${menuId eq '017' }">class='menu-select'</c:if> >
	                <a href="${ctx}/pricesale/ScaleBySubModelSaleMain.do">
	                    <span class="icon-certificate"></span><span class="text">车型销售比例分析(气泡图)</span>
	                </a>                  
	                <img src="${ctx}/img/outGive/new/new.png" >   
	            </li>      
	        </ul>   
      </li>
      
     <li class="openable active" style="background-image: none;border: none;">
        <a href="index.html#">
            <span class="icon-tasks" style="margin-top:10px;margin-left:5px;float:left;"></span><span class="text" style="color:#000;text-shadow:none">产品配置</span>
        </a>
        <ul>
            <li <c:if test="${menuId eq '006' }">class='menu-select'</c:if> >
                <a href="${ctx}/product/configVersionQueryMain">
                    <span class=" icon-retweet"></span><span class="text">型号配置查询</span>
                </a>                  
            </li>  
        </ul>   
     </li>
  
     <li class="openable active" style="background-image: none;border: none;">
	        <a href="index.html#">
	            <span class="icon-tasks" style="margin-top:10px;margin-left:5px;float:left;"></span><span class="text" style="color:#000;text-shadow:none">终端支持研究</span>
	        </a>
	        <ul>
	            <li <c:if test="${menuId eq '007' }">class='menu-select'</c:if> >
	                <a href="${ctx}/policy/policyMonthAnalyMain">
	                    <span class="icon-th"></span><span class="text">促销查询</span>
	                </a>
	                
	            </li>
	            
	            <li <c:if test="${menuId eq '016' }">class='menu-select'</c:if> >
	                <a href="${ctx}/policy/submodelProfit/submodelProfitMain">
	                    <span class="icon-barcode"></span><span class="text">车型利润分析yin</span>
	                </a>
	                 <img src="${ctx }/img/outGive/new/new.png"  > 
	            </li>
	           
	            <li <c:if test="${menuId eq '010' }">class='menu-select'</c:if> >
                	<a href="${ctx}/promotion/promotionMain">
                    	<span class="icon-fire"></span><span class="text">促销走势分析yin</span>
                	</a>                  
            	</li>
            	
            	<li <c:if test="${menuId eq '015' }">class='menu-select'</c:if> >
                	<a href="${ctx}/policy/salePromotionAnalysisMain">
                    	<span class="icon-align-left"></span><span class="text">销量促销分析yin</span>
                	</a>  
                	<img src="${ctx}/img/outGive/new/new.png" >                
            	</li>
            	
            	<li <c:if test="${menuId eq '011' }">class='menu-select'</c:if> >
	                <a href="${ctx}/terminal/terminalAnalysisMain">
	                    <span class="icon-barcode"></span><span class="text">终端支持分析yin</span>
	                </a>    
	                              
	            </li> 
	            
	            <li <c:if test="${menuId eq '013' }">class='menu-select'</c:if> >
	                <a href="${ctx}/promotionGroup/promotionGroupMain">
	                    <span class="icon-signal"></span><span class="text">促销分类分析yin</span>
	                </a> 
	                <img src="${ctx }/img/outGive/new/new.png"  >                 
	            </li> 
	           
	            <li <c:if test="${menuId eq '014' }">class='menu-select'</c:if> >
	                <a href="${ctx}/manfSales/manfSalesMain">
	                    <span class="icon-adjust"></span><span class="text">厂商销售支持yin</span>
	                </a> 
	                <img src="${ctx }/img/outGive/new/new.png"  >                
	            </li>
	       		<li <c:if test="${menuId eq '009' }">class='menu-select'</c:if> >
	                <a href="${ctx}/policy/saleIncentiveQueryMain">
	                    <span class="icon-th"></span><span class="text">销售激励查询</span>
	                </a>                  
	            </li>
	        </ul>   
     </li> 
     
</ul>