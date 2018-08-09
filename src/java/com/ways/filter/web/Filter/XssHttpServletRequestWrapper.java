package com.ways.filter.web.Filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.util.HtmlUtils;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
	}

	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null)
			return null;

		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = cleanXSS(values[i]);
		}

		return encodedValues;
	}

	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null)
			return null;

		return cleanXSS(value);
	}

	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value == null)
			return null;

		return cleanXSS(value);
	}

	private String cleanXSS(String value) {
		StringBuilder buffer = new StringBuilder(value.length() + 16);
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
			case '>':
				buffer.append(">");// 转义大于号
				break;
			case '<':
				buffer.append("<");// 转义小于号
				break;
			case '\'':
				buffer.append("'");// 转义单引号
				break;
			case '\"':
				buffer.append("\""); // 转义双引号
				break;
			case '&':
				buffer.append("&");// 转义&
				break;
			default:
				buffer.append(c);
				break;
			}
		}

		return buffer.toString();
	}

	public static void main(String[] args) {
		System.out
				.println(HtmlUtils
						.htmlEscape("www.waysdata.com/fca-sales/sales/manfMarket/manfMarketNext.do?year=2017&month=4&dateAnalysisType=0&maxMonth=4&maxYear=&minDate=2015-1&refurbishDateFlag=&source=1&rankType=0&middleType=1&top=30&rankScope=1-10&datalabel=2016年累计销量,2017年累计销量,累计同比,整体市场累计同比&mainIndexList=sales,mom,yoy,salesSum,tqSalesSum,yearYoy,mix,tqMix,mixChange&manf=49&manfName=上汽通用&manf_id=93&locale=zh_CN"));
	}
}