<#assign fullModelName = basepackage + ".model." + table.className>
<#assign fullServiceName = basepackage + ".service." + table.className + "Service">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import ${fullModelName};
import ${fullServiceName};

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.apabi.r2k.common.base.PageRequestFactory;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

@Controller("${classNameLower}Action")
public class ${className}Action{
	
	private Logger log = LoggerFactory.getLogger(${className}Action.class);
	
	@Resource(name="${classNameLower}Service")
	private ${className}Service ${classNameLower}Service;	
	
	private ${className} ${classNameLower};
	private List<${table.idColumn.javaType}> ${classNameLower}Ids;

	private Page page;
	
	
	public String toSave() throws Exception {
		return "toSave";
	}
	
	//保存
	public String save() throws Exception {
		if(${classNameLower} != null) {
			log.info("save:保存开始");
			${classNameLower}Service.save(${classNameLower});
		}
		return "success";
	}
	
	public String toUpdate() throws Exception {
		${classNameLower} = ${classNameLower}Service.getById(${classNameLower}.get${table.idColumn?cap_first}());
		return "toUpdate";
	}
	//更新
	public String update() throws Exception {
		if(${classNameLower} != null) {
			log.info("update:[${table.idColumn}#"+${classNameLower}.get${table.idColumn?cap_first}()+"]:更新");
			${classNameLower}Service.update(${classNameLower});
		}
		return "success";
	}
	
	//删除
	public String delete() throws Exception {
		if(${classNameLower}Ids != null && ${classNameLower}Ids.size() > 0){
			log.info("delete:[size#"+${classNameLower}Ids.size()+"]:批量删除");
			${classNameLower}Service.batchDelete(${classNameLower}Ids);
		}
		return "success";
	}

	//分页查询
	public String pageQuery() throws Exception {
		PageRequest pageRequest= new PageRequest();
		HttpServletRequest req = ServletActionContext.getRequest();
		PageRequestFactory.bindPageRequest(pageRequest,req);
		log.info("pageQuery:[pageNumber#"+pageRequest.getPageNumber()+",pageSize#"+pageRequest.getPageSize()+"]:分页查询开始");
		page = testUserService.pageQuery(pageRequest, null);
		return "list";
	}
	
	public void set${className}Service(${className}Service ${classNameLower}Service) {
		this.${classNameLower}Service = ${classNameLower}Service;
	}
	
	public ${className}Service get${className}Service() {
		return ${classNameLower}Service;
	}

	public void set${className}(${className} ${classNameLower}) {
		this.${classNameLower} = ${classNameLower};
	}
	
	public ${className} get${className}() {
		return ${classNameLower};
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	public void set${className}Ids(List<${table.idColumn.javaType}> ${classNameLower}Ids){
		this.${classNameLower}Ids = ${classNameLower}Ids;
	}
	
	public List<${table.idColumn.javaType}> get${className}Ids(){
		return ${classNameLower}Ids;
	}
}
