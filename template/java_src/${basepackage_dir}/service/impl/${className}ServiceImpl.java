<#assign fullModelName = basepackage + ".model." + table.className>
<#assign fullDaoName = basepackage + ".dao." + table.className + "Dao">
<#assign fullServiceName = basepackage + ".service." + table.className + "Service">
<#assign serviceName = table.className + "Service">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service.impl;

import javax.annotation.Resource;
import java.util.List;
import ${fullModelName};
import ${fullDaoName};
import ${fullServiceName};

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.apabi.r2k.common.base.BaseDaoImpl;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

@Service("${classNameLower}Service")
public class ${className}ServiceImpl implements ${serviceName} {

	private Logger log = LoggerFactory.getLogger(${className}ServiceImpl.class);
	
	public static final String PAGE_QUERY_STATEMENT = ".pageSelect";
	
	@Resource(name="${classNameLower}Dao")
	private ${className}Dao ${classNameLower}Dao;
	
	public void set${className}Dao(${className}Dao ${classNameLower}Dao){
		this.${classNameLower}Dao = ${classNameLower}Dao;
	}
	
	public ${className}Dao get${className}Dao(){
		return ${classNameLower}Dao;
	}
	
	//入库
	@Transactional(propagation = Propagation.REQUIRED)
	public int save(${className} ${classNameLower}) throws Exception {
		return ${classNameLower}Dao.save(${classNameLower});
	}
	
	//更新
	@Transactional(propagation = Propagation.REQUIRED)
	public int update(${className} ${classNameLower}) throws Exception {
		return ${classNameLower}Dao.update(${classNameLower});
	}
	
	//根据ID删除
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteById(${table.idColumn.javaType} id) throws Exception {
		return ${classNameLower}Dao.deleteById(id);
	}
	
	//批量删除
	@Transactional(propagation = Propagation.REQUIRED)
	public int batchDelete(List<${table.idColumn.javaType}> ids) throws Exception {
		return ${classNameLower}Dao.batchDelete(ids);
	}
	
	//根据ID查询
	public ${className} getById(${table.idColumn.javaType} id) throws Exception {
		return ${classNameLower}Dao.getById(id);
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception {
		return ${classNameLower}Dao.pageQuery(PAGE_QUERY_STATEMENT, pageRequest, countQuery);
	} 
	
	public List<${className}> queryAll() throws Exception{
		return ${classNameLower}Dao.queryAll();
	}
}
