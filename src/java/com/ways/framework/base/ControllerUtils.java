package com.ways.framework.base;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Action工具类.
 * 
 * 实现输出JSON，文本的简化函数.
 * @author longo
 */
public class ControllerUtils {

	//-- header 常量定义 --//
//	private static final String HEADER_ENCODING = "encoding";
//	private static final String HEADER_NOCACHE = "no-cache";
	private static final String DEFAULT_ENCODING = "UTF-8";
//	private static final boolean DEFAULT_NOCACHE = true;
	
	//-- Content Type 定义 --//
	public static final String TEXT_TYPE = "text/plain";
	public static final String JSON_TYPE = "application/json";
	public static final String XML_TYPE = "text/xml";
	public static final String HTML_TYPE = "text/html";
	public static final String JS_TYPE = "text/javascript";
	public static final String EXCEL_TYPE = "application/vnd.ms-excel";

	private static ObjectMapper mapper = new ObjectMapper();

	public static PrintWriter getWriter(HttpServletResponse response)
	{
		try
		{
			return response.getWriter();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	//-- 绕过jsp/freemaker直接输出文本的函数 --//
	/**
	 * 直接输出内容的简便函数.

	 * eg.
	 * render("text/plain", "hello", "encoding:GBK");
	 * render("text/plain", "hello", "no-cache:false");
	 * render("text/plain", "hello", "encoding:GBK", "no-cache:false");
	 * 
	 * @param headers 可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
	 */
	public static void render(HttpServletResponse response, final String contentType, final String content, final String... headers) {
		try {
			response.setCharacterEncoding(DEFAULT_ENCODING);
			response.setContentType(contentType);
			response.getWriter().write(content);
			response.getWriter().flush();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 直接输出文本.
	 * @see #render(String, String, String...)
	 */
	public static void renderText(HttpServletResponse response, final String text, final String... headers) {
		render(response, ControllerUtils.TEXT_TYPE, text, headers);
	}

	/**
	 * 直接输出文本.
	 * @see #render(String, String, String...)
	 */
	public static void renderText(HttpServletResponse response, final String text) {
		render(response, ControllerUtils.TEXT_TYPE, text, "encoding:UTF-8","no-cache:true");
	}
	
	/**
	 * 直接输出HTML.
	 * @see #render(String, String, String...)
	 */
	public static void renderHtml(HttpServletResponse response, final String html, final String... headers) {
		render(response, ControllerUtils.HTML_TYPE, html, headers);
	}

	/**
	 * 直接输出HTML.
	 * @see #render(String, String, String...)
	 */
	public static void renderHtml(HttpServletResponse response, final String html) {
		render(response, ControllerUtils.HTML_TYPE, html, "encoding:UTF-8","no-cache:true");
	}
	/**
	 * 直接输出XML.
	 * @see #render(String, String, String...)
	 */
	public static void renderXml(HttpServletResponse response, final String xml, final String... headers) {
		render(response, ControllerUtils.XML_TYPE, xml, headers);
	}
	
	/**
	 * 直接输出XML.
	 * @see #render(String, String, String...)
	 */
	public static void renderXml(HttpServletResponse response, final String xml) {
		render(response, ControllerUtils.XML_TYPE, xml, "encoding:UTF-8","no-cache:true");
	}

	/**
	 * 直接输出JSON.
	 * 
	 * @param jsonString json字符串.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson(HttpServletResponse response, final String jsonString, final String... headers) {
		render(response, ControllerUtils.JSON_TYPE, jsonString, headers);
	}

	/**
	 * 直接输出JSON.
	 * 
	 * @param jsonString json字符串.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson(HttpServletResponse response, final String jsonString) {
		render(response, ControllerUtils.JSON_TYPE, jsonString, "encoding:UTF-8","no-cache:true");
	}
	
	/**
	 * 直接输出JSON,使用Jackson转换Java对象.
	 * @param data 可以是List<POJO>, POJO[], POJO, 也可以Map名值对.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson(HttpServletResponse response, final Object data, final String... headers) {
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 直接输出JSON,使用Jackson转换Java对象.
	 * @param data 可以是List<POJO>, POJO[], POJO, 也可以Map名值对.
	 */
	public static void renderJson(HttpServletResponse response, final Object data) {
		renderJson(response, data,"encoding:UTF-8","no-cache:true");
	}

}
