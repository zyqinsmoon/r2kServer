<#assign fullModelName = basepackage + ".model." + table.className>
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

import ${fullModelName};
import java.util.List;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

public interface ${className}Service {

	/**
	 * 入库
	 */
	public int save(${className} ${classNameLower}) throws Exception;
	
	/**
	 * 更新
	 */
	public int update(${className} ${classNameLower}) throws Exception;
	
	/**
	 * 根据ID删除
	 */
	public int deleteById(${table.idColumn.javaType} id) throws Exception;
	
	/**
	 * 批量删除
	 */
	public int batchDelete(List<${table.idColumn.javaType}> ids) throws Exception;
	
	/**
	 * 根据ID查询
	 */
	public ${className} getById(${table.idColumn.javaType} id) throws Exception;
	
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception; 
	
	/**
	 * 全部查询
	 */
	public List<${className}> queryAll() throws Exception;
}
