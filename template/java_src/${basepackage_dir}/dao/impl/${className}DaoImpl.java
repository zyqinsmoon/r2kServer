<#assign fullModelName = basepackage + ".model." + table.className>
<#assign fullDaoName = basepackage + ".dao." + table.className + "Dao">
<#assign daoName = table.className + "Dao">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao.impl;

import ${fullModelName};
import ${fullDaoName};
import java.util.List;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;

@Repository("${classNameLower}Dao")
public class ${className}DaoImpl implements ${daoName} {

	private Logger log = LoggerFactory.getLogger(${className}DaoImpl.class);
	
	private BaseDaoImpl<${className}, ${table.idColumn.javaType}> baseDao;
	
	//注入baseDao
	@Resource
	public void setBaseDao(BaseDaoImpl<${className}, ${table.idColumn.javaType}> baseDao) throws Exception {
		this.baseDao = baseDao;
		baseDao.setEntityClass(getEntityClass());
	}
	
	public Class getEntityClass() throws Exception {
		return ${className}.class;
	}
	
	//入库
	public int save(${className} ${classNameLower}) throws Exception {
		return baseDao.save(${classNameLower});
	}
	
	//更新
	public int update(${className} ${classNameLower}) throws Exception {
		return baseDao.update(${classNameLower});
	}
	
	//根据ID删除
	public int deleteById(${table.idColumn.javaType} id) throws Exception {
		return baseDao.deleteBy("${table.idColumn}",id);
	}
	
	//根据ID批量删除
	public int batchDelete(List<${table.idColumn.javaType}> ids) throws Exception {
		return baseDao.batchDelete(ids);
	}
	
	//根据ID查询
	public ${className} getById(${table.idColumn.javaType} id) throws Exception {
		return baseDao.getById(id);
	}
	
	//分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception {
		return baseDao.pageQuery(statementName, pageRequest, countQuery);
	} 
	
	/**
	 * 全部查询
	 */
	public List<${className}> queryAll() throws Exception {
		return baseDao.queryAll();
	}
}
