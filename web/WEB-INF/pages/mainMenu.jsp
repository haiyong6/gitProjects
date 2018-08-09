<%@page import="com.ways.auth.dao.LoginDao"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.security.core.userdetails.User"%>
<%@page import="java.security.Principal"%>
<%@page import="org.springframework.security.cas.authentication.CasAuthenticationToken"%>
<%@page import="com.ways.auth.model.AuthConstants"%>
<%@page import="com.ways.auth.model.MyUserDetails"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%
	String toMenu = request.getParameter("isToPriceSales") ;
	if(!"1".equals(toMenu)){
		response.sendRedirect(request.getContextPath()+"/profit/profitOfMain.do");
	}else{
		response.sendRedirect(request.getContextPath()+"/pricesale/VolumeByPriceRangeMain.do");
	}
 %>

 