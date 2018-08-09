package com.ways.auth.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.ways.auth.dao.ILoginDao;
import com.ways.auth.model.MyUserDetails;
import com.ways.framework.base.BaseDaoImpl;

@Repository("loginDaoImpl")
public class LoginDaoImpl  extends BaseDaoImpl   implements ILoginDao {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public MyUserDetails getUserInfo(Map params) {
		MyUserDetails myUserDtails = null;
		try {
			List<MyUserDetails> list = getSqlMapClientTemplate().queryForList("login.getUserInfo", params);
			if(list != null && list.size()>0){
				myUserDtails = list.get(0); 
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return myUserDtails;
	}
}
