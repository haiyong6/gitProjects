package com.ways.framework.service;

import javax.jws.WebService;

@WebService
public interface IFrameworkCommonManager  {
	public String getBarcode(String type);
	
}