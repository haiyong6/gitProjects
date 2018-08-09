package com.ways.app.system.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ways.auth.model.AuthConstants;
import com.ways.framework.base.BaseController;

/**
 * 配置公共controller
 * @author yinlue
 *
 */
@Controller
public class SystemGlobalController extends BaseController{
	
	protected final Log log = LogFactory.getLog(SystemGlobalController.class);
	
	/**
     * 获取配置大类
     * @return
     * @throws Exception
     */
    @RequestMapping("/reloadAction")
    public String reloadAction(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	request.getSession().setAttribute(AuthConstants.CURR_USER_DETAILS, null);
        return "success";
    }
    
	
   
}