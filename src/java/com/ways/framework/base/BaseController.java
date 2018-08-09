
/**@Title: BaseController.java
 * @Package com.ways.pfms.app.webapp.controller
 * @author Zhanggk
 * @date 2013-7-18 上午10:19:58
 *  
 */
  
package com.ways.framework.base;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;


/**@Company：Way-s   
 * @ClassName：BaseController   
 * @Description：   
 * @author Zhanggk
 * @date：2013-7-18 上午10:19:58   
 * @Modifier： 
 * @Modify Date：  
 * @Modify Note：   
 * @version 
 */

public class BaseController {

	/**
	 * @Title: getParams 
	 * @Description: 把HttpServletRequest的所有参数对象转换为JSON对象
	 * @param request
	 * @return      
	 * @throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JSONObject getParams(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		Map<String,String[]> map = request.getParameterMap();
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String[] val = map.get(key);
			obj.put(key, val.length > 0 ? val[0] : null);
		}
		return obj;
	}
	
	/**
	 * @Title: sendResult 
	 * @Description: 向客户端发送响应
	 * @param responseText
	 * @param response      
	 * @throws
	 */
	public void sendResult(HttpServletResponse response, String responseText) {
		ControllerUtils.renderText(response, responseText);
	}
	
	/**
	 * @Title: sendResult 
	 * @Description: 向客户端发送响应
	 * @param response
	 * @param jsonObj      
	 * @throws
	 */
	public void sendResult(HttpServletResponse response, JSONObject jsonObj) {
		ControllerUtils.renderJson(response, jsonObj);
	}
	
	
	public Long getLongVal(HttpServletRequest request, String param) {
    	if(request.getParameter(param) != null && !"-1".equals(request.getParameter(param)) && !"".equals(request.getParameter(param))) {
    		return Long.parseLong(request.getParameter(param));
    	}
    	return null;
    }
    
    public Long[] getLongVals(HttpServletRequest request, String param) {
    	if(request.getParameter(param) != null && !"-1".equals(request.getParameter(param)) && !"".equals(request.getParameter(param))) {
    		String val = request.getParameter(param);
    		String[] vs = val.split(",");
    		Long[] ids = new Long[vs.length];
    		for (int i = 0; i < vs.length; i++) {
				ids[i] = Long.parseLong(vs[i]);
			}
    		return ids;
    	}
    	return null;
    }
    
    public Integer getIntegerVal(HttpServletRequest request, String param) {
    	if(request.getParameter(param) != null && !"-1".equals(request.getParameter(param)) && !"".equals(request.getParameter(param))) {
    		return Integer.parseInt(request.getParameter(param));
    	}
    	return null;
    }
    
    public Float getFloatVal(HttpServletRequest request, String param) {
    	if(request.getParameter(param) != null && !"-1".equals(request.getParameter(param)) && !"".equals(request.getParameter(param)) ) {
    		return Float.parseFloat(request.getParameter(param));
    	}
    	return null;
    }
    
    public Float[] getFloatVals(HttpServletRequest request, String param) {
    	if(request.getParameter(param) != null && !"-1".equals(request.getParameter(param)) && !"".equals(request.getParameter(param)) ) {
    		String val = request.getParameter(param);
    		String[] vs = val.split(",");
    		Float[] ids = new Float[vs.length];
    		for (int i = 0; i < vs.length; i++) {
				ids[i] = Float.parseFloat(vs[i]);
			}
    		return ids;
    	}
    	return null;
    }
    
    public String getStringVal(HttpServletRequest request, String param) {
    	if(request.getParameter(param) != null && !"-1".equals(request.getParameter(param)) && !"".equals(request.getParameter(param))) {
    		return request.getParameter(param);
    	}
    	return null;
    }
    
    public String[] getStringVals(HttpServletRequest request, String param) {
    	if(request.getParameter(param) != null && !"-1".equals(request.getParameter(param)) && !"".equals(request.getParameter(param))) {
    		return request.getParameter(param).split(",");
    	}
    	return null;
    }
}
