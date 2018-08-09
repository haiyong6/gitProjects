package com.ways.framework.base;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ways.framework.utils.Pager;
import com.ways.framework.utils.StringUtil;

@Repository("baseDaoImpl")
public class BaseDaoImpl implements IBaseDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private SqlMapClient sqlMapClient;
	
	private static SqlMapClientTemplate sqlMapClientTemplate = null;
//	private static Session session= null;

    public Session getSession() throws HibernateException {
        Session sess = getSessionFactory().getCurrentSession();
        if (sess == null) {
            sess = getSessionFactory().openSession();
        }
        return sess;
    }
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	// 获取iBatis的模板
	@SuppressWarnings("static-access")
	public SqlMapClientTemplate getSqlMapClientTemplate() {
		if (this.sqlMapClientTemplate == null) {
			this.sqlMapClientTemplate = new SqlMapClientTemplate();
			this.sqlMapClientTemplate.setSqlMapClient(sqlMapClient);
		}
		return this.sqlMapClientTemplate;
	}
	
	@SuppressWarnings("rawtypes")
	public List findObjects(final Object object, final Pager pager,final Map queryParam) {
		Example ex = Example.create(object).ignoreCase().enableLike(MatchMode.ANYWHERE);//通过一个给定实例 构建一个条件查询
		Criteria ca = this.getSession().createCriteria(object.getClass()).add(ex);				
		pager.setTotalObjects(((Integer) (ca.setProjection(Projections.rowCount()).uniqueResult())).intValue()); //获取条件查询总数Count
		ca.setProjection(null);
		ca.setResultTransformer(Criteria.ROOT_ENTITY);
		pager.calc();
	
		ca.setFirstResult(pager.getFirstResult());
		ca.setMaxResults(pager.getPageSize());
		
		if(queryParam!=null && !StringUtil.isEmpty((String)queryParam.get("sidx"))){
			String sidxs=(String)queryParam.get("sidx");
			String sords=(String)queryParam.get("sord");
			String[] sidxArr=sidxs.split(",");
			String[] sordArr=sords.split(",");
			for(int i=0;i<sidxArr.length;i++){
				if("desc".equals(sordArr[i])){
					ca.addOrder(Order.desc(sidxArr[i]));
				}else{
					ca.addOrder(Order.asc(sidxArr[i]));
				}
			}
		}
		return ca.list();
	}
}
