<%@ page language="java" errorPage="/error.jsp"   pageEncoding="utf-8"%> 
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page import="com.ways.framework.utils.LocaleMessageUtil"%> 
<!-- 页头 -->
<header class="page-header">
    <div class="logo"></div>
    <div class="user-menu">
        <span class="welcome"><fmt:message key="common.welcome"/>，${currUserDetails.userName}(${currUserDetails.corpUnitName})</span>
        <!--<a class="mr10">更新提示</a>
        <a class="mr10">系统设置</a>
        -->
        <span class="mr10" style="color:#999999;"><fmt:message key="common.update"/></span>
        <%--<sec:authorize ifAnyGranted="BDIP_PC_SYS_CONFIG"> <span class="mr10"><a href="${ctx}/vistorLoginDetailMain.do "><fmt:message key="common.setting"/></a></span></sec:authorize>--%>
        
        <a class="mr10" href="${ctx}/common/logout.jsp"  title="<fmt:message key="common.backToHomepage"/>"><fmt:message key="common.backToHomepage"/></a>
         <%
        	String locale = LocaleMessageUtil.getCurrentLanguage();
        	String value="";
        	String text="";
        	if("zh".equals(locale)){
        		value="zh_CN";
        		text="简体中文";
        	}else{
        		value=locale;
        		text="English";
        	}
        %>
        <div class="btn-group" store="locale" id="locale" style="display:none;">
            <a class="btn dropdown-toggle"></a>
            <ul class="dropdown-menu">
                <li data-value="zh_CN"><a>简体中文</a></li>
                <li data-value="en"><a>English</a></li>
            </ul>
            <input type="hidden" name="locale" value="<%=value%>" text="<%=text%>"/>
        </div>
    </div>
</header>
