package com.ways.framework.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.ways.framework.base.BaseDaoImpl;
import com.ways.framework.dao.IFrameworkCommonDao;



@Repository("fameworkCommonDaoImpl")
public class FrameworkCommonDaoImpl  extends BaseDaoImpl   implements IFrameworkCommonDao {
	@Override
	public String getBarcode(String seedTypeIn) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("seedTypeIn", seedTypeIn);
		try {
			getSqlMapClientTemplate().update("framework.getBarcode", map);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return map.get("returnValue").toString();
	}
}
