package com.ways.auth.dao;

import java.util.Map;

import com.ways.auth.model.MyUserDetails;

public interface ILoginDao {
	 
	@SuppressWarnings("rawtypes")
	public MyUserDetails getUserInfo(Map params) ;
}
