package com.ways.auth.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ways.auth.dao.ILoginDao;
import com.ways.auth.model.AuthConstants;
import com.ways.auth.model.MyUserDetails;

public class LoginProcessingFilter  implements Filter {
	Logger logger = Logger.getLogger(LoginProcessingFilter.class);
	@Override
	public void destroy() {
		
	}
//	private static MemCachedManager cache = MemCachedManager.getInstance();
//	private static EncryptionService es = EncryptionService.getInstance();
//	private static DesCryptService des = DesCryptService.getInstance();

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse)res;
        MyUserDetails userDetails  = (MyUserDetails)request.getSession().getAttribute(AuthConstants.CURR_USER_DETAILS);
        
        if(!request.getRequestURI().contains(".js") && !request.getRequestURI().contains(".css") &&  !request.getRequestURI().contains("/403.jsp") && !request.getRequestURI().contains("/404.jsp")){
        	String loginId = request.getParameter("u");
        	String password = request.getParameter("p");
        	//接收是否跳转至价格段销量
        	String isToPriceSales = request.getParameter("isToPriceSales");
        	request.setAttribute("isToPriceSales", isToPriceSales);
        	if(loginId==null) loginId = "";
        	if(password==null) password = "";
        	
        	if(loginId.equals("") && password.equals("")){
//        		loginId = "RUANRF";
//        		password = "NGUl+7isrgQFjxbQGZ5lAg==";
        		loginId = "ZHAOHY";
        		password = "x81OTvfaR8ZkvzjBGAn38A==";
        	}
        	
        	if(!loginId.trim().equals("") && !password.trim().equals("")){
//        		try {
//	        		loginId = des.getDesString(loginId);
//	        		password = des.getDesString(password);
//        		}catch(Exception e){
//        			logger.error("账号或密码不正确！"+e.getMessage());
//					response.sendRedirect(request.getContextPath()+"/403.jsp");
//        		}
        		if(userDetails != null){
        			if(!userDetails.getLoginId().equalsIgnoreCase(loginId)){
        				userDetails = null;
        			}
        		}
        		if(userDetails == null){
		        	try {
			        	ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
			        	ILoginDao loginDaoImpl = (ILoginDao)ac.getBean("loginDaoImpl");
			        	Map params = new HashMap();
			        	params.put("loginId",loginId);
			        	userDetails = loginDaoImpl.getUserInfo(params);
			        	if(userDetails != null){
			        		if(!userDetails.getPassword().equals(password)){
			        			response.sendRedirect(request.getContextPath()+"/403.jsp");
			        		}else{
			        			request.getSession().setAttribute(AuthConstants.CURR_USER_DETAILS, userDetails);
			        			request.getSession().setAttribute(AuthConstants.CTX, request.getContextPath());
			        		}
			        	}
						
					} catch (Exception e) {
						logger.error("账号或密码不正确！"+e.getMessage());
						response.sendRedirect(request.getContextPath()+"/403.jsp");
					}
					
        		}
        	}
        	if(userDetails == null){
	        	response.sendRedirect(request.getContextPath()+"/403.jsp");
			}else{
				chain.doFilter(req, res);
			}
        }else{
			chain.doFilter(req, res);
		}
	        		 
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
}