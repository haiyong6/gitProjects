package com.ways.framework.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Company：Way-s   
 * @ClassName：CookieUtil   
 * @Description： Cookie处理Util类
 * @author Zhanggk
 * @date：2013-9-6 下午03:10:50   
 * @Modifier： 
 * @Modify Date：  
 * @Modify Note：   
 * @version
 */
public class CookieUtil {
   
	/**
	 * @Title: addCookie 
	 * @Description: 
	 * @param response
	 * @param name 名称
	 * @param value 值
	 * @param maxAge 最长时间 单位秒
	 * @throws
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {       
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge>0) cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
	
    /**
     * @Title: addCookie 
     * @Description: 
     * @param response
     * @param name cookie名称
     * @param path cookie存放路径
     * @param value cookie值
     * @param maxAge cookie最长时间
     * @throws
     */
    public static void addCookie(HttpServletResponse response, String name,String path, String value, int maxAge) {       
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        if (maxAge>0) cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
   

   /**
    * @Title: getCookieByName 
    * @Description: 获取cookie的值
    * @param request
    * @param name 名称
    * @return      
    * @throws
    */
    public static String getCookieByName(HttpServletRequest request, String name) {
     Map<String, Cookie> cookieMap = CookieUtil.readCookieMap(request);
        if(cookieMap.containsKey(name)){
            Cookie cookie = (Cookie)cookieMap.get(name);
            return cookie.getValue();
        }else{
            return null;
        }
    }
   
    protected static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                cookieMap.put(cookies[i].getName(), cookies[i]);
            }
        }
        return cookieMap;
    }
}