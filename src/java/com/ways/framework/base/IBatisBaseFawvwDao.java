package com.ways.framework.base;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;

public class IBatisBaseFawvwDao extends SqlMapClientDaoSupport{
	 @Resource(name="fawvwSqlMapClient")
	  private SqlMapClient sqlMapClient;

	  @PostConstruct
	  public void initSqlMapClient()
	  {
	    super.setSqlMapClient(this.sqlMapClient);
	  }

	  @SuppressWarnings("rawtypes")
	public List getListMapByParamMap(String statementName, Map<String, Object> paramMap)
	  {
	    List resultList = null;
	    if (StringUtils.isNotEmpty(statementName)) {
	      resultList = getSqlMapClientTemplate().queryForList(statementName, paramMap);
	    }
	    return resultList;
	  }
}
