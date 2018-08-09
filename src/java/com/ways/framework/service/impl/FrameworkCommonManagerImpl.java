package com.ways.framework.service.impl;

import org.springframework.stereotype.Service;

import com.ways.framework.dao.IFrameworkCommonDao;
import com.ways.framework.service.IFrameworkCommonManager;


@Service("frameworkCommonManagerImpl")
public class FrameworkCommonManagerImpl  implements IFrameworkCommonManager {
	IFrameworkCommonDao fameworkCommonDaoImpl;
	@Override
	public String getBarcode(String type) {
		return fameworkCommonDaoImpl.getBarcode(type);
	}


}