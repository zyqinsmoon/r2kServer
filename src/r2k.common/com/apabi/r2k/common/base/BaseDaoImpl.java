package com.apabi.r2k.common.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

@Repository
@Scope("prototype")
public  abstract class BaseDaoImpl<T,PK extends Serializable> {

	@Autowired
	protected SqlSessionTemplate baseDao;

	public SqlSessionTemplate getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(SqlSessionTemplate baseDao) {
		this.baseDao = baseDao;
	}

	
    public  Class getEntityClass(){
    	return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

	/**
	 * 保存对象到数据库
	 */
	public int save(T entity) throws Exception {
		return baseDao.insert(entity.getClass().getName() + ".insert", entity);
	}

	//批量保存
	public void batchSave(List<T> entities) throws Exception{
		SqlSession session = null;
		try {
			SqlSessionFactory sqlSessionFactory = baseDao.getSqlSessionFactory();
			session = sqlSessionFactory.openSession(false);
			int size = entities.size();
			for(int i = 0; i < size; i++){
				T entity = entities.get(i);
				session.insert(getStatement("insert"),entity);
				if((i+1)%500 == 0){
					session.commit();
				}
			}
			session.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session!=null){
				session.close();
			}
		}
		
	}
	
	/**
	 * 从数据库中删除对象
	 */
	public int deleteBy(Object... params) throws Exception {
		return baseDao.delete(getDeleteQuery(), map(params));
	}

	/**
	 * 批量删除
	 */
	public int batchDelete(Map params) throws Exception{
		return baseDao.delete(getBatchDeleteQuery(), params);
	}
	
	public String getDeleteQuery() throws Exception {
		return getDeleteQuery(getEntityClass());
	}

	private String getDeleteQuery(Class<?> clazz) throws Exception {
		return clazz.getName() + ".delete";
	}
	
	public String getBatchDeleteQuery() throws Exception {
		return getBatchDeleteQuery(getEntityClass());
	}
	
	public String getBatchDeleteQuery(Class<?> clazz) throws Exception {
		return clazz.getName() + ".batchDelete";
	}

	/**
	 * 更新数据库中的数据
	 */
	public int update(T entity) throws Exception {
		return baseDao.update(getUpdateQuery(), entity);
	}

	private String getUpdateQuery() throws Exception {
		return getEntityClass().getName() + ".update";
	}

	public String getPageSelect(String... query) throws Exception {
		String selectId = ".pageSelect";
		if (query != null && query.length == 1) {
			selectId = "." + query[0];
		}
		return getEntityClass().getName() + selectId;
	}

	public int count(Object... params) throws Exception {
		final Map<?, ?> map = map(params);
		final Number count = (Number) baseDao.selectOne(
				getCountQuery(), map);
		return count.intValue();
	}

	/**
	 * 根据ID查询
	 */
	@SuppressWarnings("unchecked")
	public T getById(PK primaryKey) throws Exception {
		T entity = (T)baseDao.selectOne(getPrimaryKeyStatement(), primaryKey);
		return entity;
	}
	
	public String getPrimaryKeyStatement() throws Exception {
		return getEntityClass().getName()+".getById";
	}
	
	public String getCountQuery() throws Exception {
		String selectId = ".count";
		/*if (query != null) {
			selectId = "." + query;
		}*/
		System.out.println(getEntityClass().getName() + selectId);
		return getEntityClass().getName() + selectId;
	}
	
	public String getStatement(String statementName) throws Exception {
		return getEntityClass().getName() +"." +statementName;
	}
	/**
	 * 用于子类覆盖,在insert,update之前调用.
	 */
    protected void prepareObjectForSaveOrUpdate(final T o) {
    }
    public List findBy(final Object... params) throws Exception {
		return baseDao.selectList(getPageSelect(), map(params));
	}

	/**
	 * 分页查询
	 */
	public Page<?> basePageQuery(String statementName, PageRequest<?> pageRequest) throws Exception {
		Number totalCount = (Number) this.baseDao.selectOne(getCountQuery(), pageRequest.getFilters());
		if(totalCount == null || totalCount.intValue() == 0){
			return new Page<T>(pageRequest,0);
		}
		Page<?> page = new Page(pageRequest, totalCount.intValue());
		// 其它分页参数
		Map<Object, Object> otherFilters = new HashMap<Object, Object>();
		otherFilters.put("offset", page.getFirstResult());
		otherFilters.put("pageSize", page.getPageSize());
		otherFilters.put("lastRows", page.getFirstResult() + page.getPageSize());
		otherFilters.put("sortColumns", pageRequest.getSortColumns());
		//Map parameterObject = PropertyUtils.describe(pageRequest);
		//混合两个filters为一个filters,MapAndObject.get()方法将在两个对象取值,Map如果取值为null,则再在Bean中取值
		//final Map<?, ?> parameterObject = new MapAndObject(otherFilters,(Map)pageRequest.getFilters());
		otherFilters.putAll((Map)pageRequest.getFilters());
		
		//otherFilters.putAll(parameterObject);
		RowBounds rowBounds = new RowBounds(page.getFirstResult(), page.getPageSize());
		List list = baseDao.selectList(statementName, otherFilters, rowBounds);
		page.setResult(list);
		return page;
	}

	/**
	 * 分页查询
	 */
	public Page<?> basePageQuery(String statementName, PageRequest<?> pageRequest,String countStatementName) throws Exception {
		Number totalCount = (Number) this.baseDao.selectOne(countStatementName, pageRequest.getFilters());
		if(totalCount == null || totalCount.intValue() == 0){
			return new Page<T>(pageRequest,0);
		}
		Page<?> page = new Page(pageRequest, totalCount.intValue());
		// 其它分页参数
		Map<Object, Object> otherFilters = new HashMap<Object, Object>();
		otherFilters.put("offset", page.getFirstResult());
		otherFilters.put("pageSize", page.getPageSize());
		otherFilters.put("lastRows", page.getFirstResult() + page.getPageSize());
		otherFilters.put("sortColumns", pageRequest.getSortColumns());
		//Map parameterObject = PropertyUtils.describe(pageRequest);
		//混合两个filters为一个filters,MapAndObject.get()方法将在两个对象取值,Map如果取值为null,则再在Bean中取值
		//final Map<?, ?> parameterObject = new MapAndObject(otherFilters,(Map)pageRequest.getFilters());
		otherFilters.putAll((Map)pageRequest.getFilters());
		
		//otherFilters.putAll(parameterObject);
		RowBounds rowBounds = new RowBounds(page.getFirstResult(), page.getPageSize());
		List list = baseDao.selectList(statementName, otherFilters, rowBounds);
		page.setResult(list);
		return page;
	}

	//全部查询
	public List<T> queryAll(String statementName) throws Exception {
		List<T> list = baseDao.selectList(statementName);
		return list;
	}
	/**
	 * 根据参数构造map，参数必须为偶数个，依次为key1，value1，key2，value2…….
	 * @param datas 参数列表
	 */
	protected Map<?, ?> map(Object... datas) throws Exception {
		Assert.isTrue(datas.length % 2 == 0, "参数必须为偶数个");
		final Map<Object, Object> map = new HashMap<Object, Object>();
		for (int i = 0; i < datas.length; i += 2) {
			map.put(datas[i], datas[i + 1]);
		}
		return map;
	}

	public List<T> batchUpdate(String statement, List<T> lists) throws Exception{
		List<T> failList = new ArrayList<T>();
		SqlSession session = null;
		try {
			session = this.baseDao.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
			int batchNumber = 500;
			int size = lists.size();
			for(int i = 0; i < size; i++){
				T t = lists.get(i);
				int result = session.update(statement, t);
				if(result == 0){
					failList.add(t);
				}
				if(i % batchNumber == 0){
					session.commit();
				}
			}
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null){
				session.close();
			}
			
		}
		return failList;
	}
	
	public List<T> batchSave(String statement, List<T> lists) throws Exception{
		SqlSession session = null; 
		List<T> failList = new ArrayList<T>();
		try {
			session =  this.baseDao.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
			int batchNumber = 500;
			int size = lists.size();
			for(int i = 0; i < size; i++){
				T t = lists.get(i);
				int result = session.insert(statement, t);
				if(result == 0){
					failList.add(t);
				}
				if(i % batchNumber == 0){
					session.commit();
				}
			}
			session.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return failList;
	}
}
