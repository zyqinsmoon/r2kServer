package com.apabi.r2k.security.service.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.OrgPaperOrderDao;
import com.apabi.r2k.admin.dao.PaperAuthDao;
import com.apabi.r2k.admin.model.OrgPaperOrder;
import com.apabi.r2k.admin.model.PaperAuth;
import com.apabi.r2k.admin.model.PrjEnum;
import com.apabi.r2k.admin.service.TopicService;
import com.apabi.r2k.common.base.ServerModelTrandsform;
import com.apabi.r2k.common.solr.SolrResult;
import com.apabi.r2k.common.solr.SolrUtil;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.menu.service.MenuService;
import com.apabi.r2k.security.dao.AuthOrgDao;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.model.AuthOrgRole;
import com.apabi.r2k.security.model.AuthRole;
import com.apabi.r2k.security.service.AuthOrgRoleService;
import com.apabi.r2k.security.service.AuthOrgService;
import com.apabi.r2k.security.service.AuthRoleService;
import com.apabi.r2k.security.service.AuthUserService;

@Service("authOrgService")
public class AuthOrgServiceImpl implements AuthOrgService {
	private Logger log = LoggerFactory.getLogger(AuthOrgServiceImpl.class);
	
	@Resource
	private AuthOrgDao authOrgDao;
	@Resource(name="topicServiceImpl")
	private TopicService topicService;
	@Resource(name="paperAuthDao")
	private PaperAuthDao paperAuthDao;
	@Resource(name="orgPaperOrderDao")
	private OrgPaperOrderDao orgPaperOrderDao;
	@Resource(name="authUserService")
	private AuthUserService authUserService;
	@Resource(name="authRoleService")
	private AuthRoleService authRoleService;
	@Resource(name="authOrgRoleService")
	private AuthOrgRoleService authOrgRoleService;
	@Resource(name="menuService")
	private MenuService menuService;

	public final String AUTH_STATUS_DEL = "0";		//删除权限
	public final String AUTH_STATUS_ADD = "1";		//添加权限
	
	public final String AUTH_CHANGE_ADD = "add";
	public final String AUTH_CHANGE_DEL = "del";
	
	public Page findByPageRequest(PageRequest pr) throws Exception {
		return authOrgDao.findByPageRequest(pr);
	}
	public List<AuthOrg> findAllChildOrgs(java.lang.Long orgId) throws Exception{
		return authOrgDao.findAllChildOrgs(orgId);
	}
	/*public Boolean isOrgNameExist(String orgName) {
		return authOrgDao.findBy("orgName",orgName).size()>0;
	}
	*/
	@Override
	public Boolean isOrgNameExist(String orgName) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public AuthOrg get(Long orgId) throws Exception{
		return authOrgDao.getAuthOrgById(orgId);
	}
	@Override
	public void removeById(Long orgId) {
		// TODO Auto-generated method stub
		
	}
	
	public List<AuthOrg> getRolesByOrg(Long orgId) throws Exception{
		return this.authOrgDao.getRolesByOrg(orgId);
	}
	//注册验证：检查orgId在数据库中是否存在，1成功(不存在)，0失败(已存在)
	public String checkOrgId(String orgId) throws Exception{
		String status = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		AuthOrg org = this.authOrgDao.getOrgById(map);
		return status = org != null ? "0" : "1";
	}
	//注册验证：检查orgId在数据库中是否存在，1成功(不存在)，0失败(已存在)
	public String checkOrgName(String orgName) throws Exception{
		String status = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgName", orgName);
		AuthOrg org = this.authOrgDao.getOrgByName(map);
		return status = org != null ? "0" : "1";
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public String saveOrg(AuthOrg org, List<String> enumCodes) throws Exception {
		String orgId = org.getOrgId();
		boolean ebookflag = false;	//读书授权标识
		boolean paperflag = false;	//报纸授权标识
		boolean topicflag = false;	//专题授权标识
		boolean publishflag = false;//资源授权标识
		boolean pictureflag = false;//图片授权标识
		//角色赋予
		Date now = new Date();
		if(enumCodes != null){
			//enumCodes.add("");	//ENUM表中的公共信息
			List<AuthRole> rolelist = this.authRoleService.queryAllByEnum(enumCodes);
			List<AuthOrgRole> authOrgRoles = new ArrayList<AuthOrgRole>();
			
			for(AuthRole role : rolelist){
				AuthOrgRole orgrole = new AuthOrgRole();
				orgrole.setCrtDate(now);
				orgrole.setLastUpdate(now);
				orgrole.setOrgId(orgId);
				orgrole.setRoleId(role.getId());
				authOrgRoles.add(orgrole);
			}
			this.authOrgRoleService.batchSave(authOrgRoles);
			//向机构表中保存授权信息
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orgId", org.getOrgId());
			map.put("orgName", org.getOrgName());
			map.put("createDate", org.getCrtDate());
			map.put("makerId", org.getMakerId());
			map.put("deviceNum", org.getDeviceNum());
			map.put("areaCode", org.getAreaCode());
			
			for (String code : enumCodes) {
				if(PrjEnum.ENUMTYPE_RES_TOPIC.equals(code)){
					topicflag = true;
				}else if(PrjEnum.ENUMTYPE_RES_EBOOK.equals(code)){
					ebookflag = true;
				}else if(PrjEnum.ENUMTYPE_RES_PUBLISH.equals(code)){
					publishflag = true;
				}else if(PrjEnum.ENUMTYPE_RES_PAPER.equals(code)){
					paperflag = true;
				}else if(PrjEnum.ENUMTYPE_RES_PICTURE.equals(code)){
					pictureflag = true;
				}
			}
			//普通专题授权
			if(topicflag){
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Map returnMsg = this.topicService.updateTopicAuthByNewOrg(orgId);
				List<SolrResult> solrResults = (List<SolrResult>)returnMsg.get(GlobalConstant.RESULT_LIST);
				if(GlobalConstant.RESULT_STATUS_SUCCESS.equals(solrResults.get(0).getCode())){
					log.info("saveOrg:"+sf.format(now)+"-机构"+orgId+"的普通专题授权已成功通知solr");
				}else{
					log.info("saveOrg:"+sf.format(now)+"-机构"+orgId+"的普通专题授权通知solr失败");
				}
				map.put("topic", AuthOrg.AUTH_STATUS_YES);
			}else{
				map.put("topic", AuthOrg.AUTH_STATUS_NO);
			}
			//读书授权
			if(ebookflag){
				map.put("ebook", AuthOrg.AUTH_STATUS_YES);
			}else{
				map.put("ebook", AuthOrg.AUTH_STATUS_NO);
			}
			//报纸授权
			if(paperflag){
				map.put("paper", AuthOrg.AUTH_STATUS_YES);
			}else{
				map.put("paper", AuthOrg.AUTH_STATUS_NO);
			}
			//资源授权
			if(publishflag){
				map.put("publish", AuthOrg.AUTH_STATUS_YES);
			}else{
				map.put("publish", AuthOrg.AUTH_STATUS_NO);
			}
			//图片授权
			if(pictureflag){
				map.put("picture", AuthOrg.AUTH_STATUS_YES);
			}else{
				map.put("picture", AuthOrg.AUTH_STATUS_NO);
			}
			this.authOrgDao.saveOrg(map);
		}
		
		//为机构添加默认用户
		return this.authUserService.saveDefaultUser(orgId);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateOrg(AuthOrg org, String[] oldauth, String[] newauth) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", org.getOrgId());
		map.put("orgName", org.getOrgName());
		map.put("makerId", org.getMakerId());
		map.put("deviceNum", org.getDeviceNum());
		map.put("lastUpdate", new Date());
		map.put("areaCode", org.getAreaCode());
		//角色修改
		if(oldauth != null && newauth != null){
			boolean isAuthChanged = false;
			for(int i = 0, len = oldauth.length; i < len; i++){
				String newnode = newauth[i];
				if(!oldauth[i].equals(newnode)){
					isAuthChanged = true;
					if(this.AUTH_STATUS_ADD.equals(newnode)){
						updateResAuth(org, i, AUTH_STATUS_ADD, map);
					}else if(this.AUTH_STATUS_DEL.equals(newnode)){
						updateResAuth(org, i, AUTH_STATUS_DEL, map);
					}
				}
				setNewOrgAuth(org, i, newnode);
			}
			//如果机构授权改变，则修改机构菜单并重新发布
			if(isAuthChanged){
				Map<String, List<String>> menuChanges = getMenuChangeByAuthArray(oldauth, newauth);
				List<String> addMenus = menuChanges.get(AUTH_CHANGE_ADD);
				List<String> delMenus = menuChanges.get(AUTH_CHANGE_DEL);
				menuService.modifyMenus(org, addMenus, delMenus);
			}
			//更新机构信息并修改机构中的授权
			this.authOrgDao.updateOrg(map);
		}
	}
	
	//设置机构新的授权
	private void setNewOrgAuth(AuthOrg org, int i, String auth){
		switch (i) {
		case 0:
			org.setEbook(auth);
			break;
		case 1:
			org.setPaper(auth);
			break;
		case 2:
			org.setTopic(auth);
			break;
		case 3:
			org.setPublish(auth);
			break;
		case 4:
			org.setPicture(auth);
			break;
		}
	}
	
	/**
	 * 根据授权数组获取修改授权后增减菜单的列表
	 * @param auths
	 * @return
	 */
	private Map<String, List<String>> getMenuChangeByAuthArray(String[] oldAuths, String[] newAuths){
		List<String> addMenus = new ArrayList<String>();
		List<String> delMenus = new ArrayList<String>();
		/*
		 * 对比授权数组，在同一索引位置，
		 * 1 如果(原授权-新授权) < 0，则为新增授权
		 * 2 如果(原授权-新授权) > 0，则为删除授权
		 * 索引位置: 0:图书授权 1:报纸授权 2:专题授权 3: 资讯授权 4:图书授权 
		 */
		int oldBookAuth = Integer.parseInt(oldAuths[0]);
		int newBookAuth = Integer.parseInt(newAuths[0]);
		if((oldBookAuth - newBookAuth) < 0){
			addMenus.add(PrjEnum.ENUM_VALUE_BOOKS);
		}else if((oldBookAuth - newBookAuth) > 0){
			delMenus.add(PrjEnum.ENUM_VALUE_BOOKS);
		}
		int oldPaperAuth = Integer.parseInt(oldAuths[1]);
		int newPaperAuth = Integer.parseInt(newAuths[1]);
		if((oldPaperAuth - newPaperAuth) < 0){
			addMenus.add(PrjEnum.ENUM_VALUE_PAPER);
		}else if((oldPaperAuth - newPaperAuth) > 0){
			delMenus.add(PrjEnum.ENUM_VALUE_PAPER);
		}
		int oldTopicAuth = Integer.parseInt(oldAuths[2]);
		int newTopicAuth = Integer.parseInt(newAuths[2]);
		if((oldTopicAuth - newTopicAuth) < 0){
			addMenus.add(PrjEnum.ENUM_VALUE_TOPIC);
		}else if((oldTopicAuth - newTopicAuth) > 0){
			delMenus.add(PrjEnum.ENUM_VALUE_TOPIC);
		}
		int oldPubAuth = Integer.parseInt(oldAuths[3]);
		int newPubAuth = Integer.parseInt(newAuths[3]);
		if((oldPubAuth - newPubAuth) < 0){
			addMenus.add(PrjEnum.ENUM_VALUE_PUBLISH);
		}else if((oldPubAuth - newPubAuth) > 0){
			delMenus.add(PrjEnum.ENUM_VALUE_PUBLISH);
		}
		int oldPicAuth = Integer.parseInt(oldAuths[4]);
		int newPicAuth = Integer.parseInt(newAuths[4]);
		if((oldPicAuth - newPicAuth) < 0){
			addMenus.add(PrjEnum.ENUM_VALUE_PICTURE);
		}else if((oldPicAuth - newPicAuth) > 0){
			delMenus.add(PrjEnum.ENUM_VALUE_PICTURE);
		}
		Map<String, List<String>> menuChanges = new HashMap<String, List<String>>();
		menuChanges.put(AUTH_CHANGE_ADD, addMenus);
		menuChanges.put(AUTH_CHANGE_DEL, delMenus);
		return menuChanges;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean deleteOrg(AuthOrg org) throws Exception {
		boolean roleflag = false, orgflag = false, userflag = false;
		if(org.getId() > 0){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orgId", org.getOrgId());
			//删除机构角色中间表数据
			int count = this.authOrgRoleService.deleteByOrgId(org.getOrgId());
			if(count > 0){
				roleflag = true;
			}
			//删除机构
			orgflag = this.authOrgDao.deleteOrg(map);
			//删除机构下用户
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("authOrgId", org.getId());
			userflag = this.authUserService.delete(param);
		}
		return (roleflag && orgflag && userflag);
	}

	public AuthOrg getOrgObject(String orgId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		AuthOrg org = this.authOrgDao.getOrgById(map);
		List<AuthRole> rolelist = new ArrayList<AuthRole>();
		rolelist = this.authRoleService.getRolesByOrg(org.getOrgId());
		org.setEnumAuthList(rolelist);
		//org.setAuthRoleList(rolelist);
		return org;
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception {
		Page p = authOrgDao.pageQuery(GlobalConstant.PAGE_QUERY_STATEMENT, pageRequest, countQuery);
		List<AuthOrg> orglist = (List<AuthOrg>)p.getResult();
		for(AuthOrg org : orglist){
//			List<AuthRole> rolelist = new ArrayList<AuthRole>();
//			rolelist = this.authRoleService.getRolesByOrg(org.getOrgId());
			org.setEnumAuthList(org.getAuthRoleList());
			//org.setAuthRoleList(rolelist);
		}
		return p;
	}
	
	@Override
	public List<AuthOrg> fuzzySearchOrg(String name_startsWith) throws Exception {
		if(StringUtils.isBlank(name_startsWith)){
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name_startsWith", name_startsWith);
		List<AuthOrg> orgList = authOrgDao.fuzzySearchOrg(map);
		return orgList;
	}
	
	//获取所有购买专题的机构列表
	public List<AuthOrg> queryAllTopciOrg() throws Exception{
		return this.authOrgDao.getAllBuyTopicOrgList();
	}	
	
	
	/**
	 * 机构权限修改
	 * @param org 修改的机构
	 * @param index	权限类型: 0读书,1报纸,2专题,3资讯
	 * @param type 0删除,1增加
	 * @param map 
	 * @throws Exception 
	 */
	private void updateResAuth(AuthOrg org, int index, String type, Map map) throws Exception {
		//报纸
		List<AuthRole> rolelist= this.authRoleService.queryByEnum(""+(index+1));
		if(index == 1){
			//添加报纸角色
			if(AUTH_STATUS_ADD.equals(type)){
				//向authorg表中添加报纸授权
				map.put("paper", AuthOrg.AUTH_STATUS_YES);
				this.addAuth(org, rolelist);
			}//删除报纸角色
			else if(AUTH_STATUS_DEL.equals(type)){
				List<Long> roleIds = new ArrayList<Long>(); 
				for (AuthRole authRole : rolelist) {
					roleIds.add(authRole.getId());
				}
				this.authOrgRoleService.deleteByEnum(org.getOrgId(), roleIds);
				deletePaperAuth(org.getOrgId());
				//向authorg表中删除报纸授权
				map.put("paper", AuthOrg.AUTH_STATUS_NO);
				//删除菜单授权
//				deleteMenuAuth(org, rolelist);
			}
		}//专题
		else if(index == 2){
			if(AUTH_STATUS_ADD.equals(type)){
				this.addAuth(org, rolelist);
				//添加普通专题授权
				Map result = this.topicService.updateTopicAuthByNewOrg(org.getOrgId());
				List<SolrResult> solrResults = (List<SolrResult>)result.get(GlobalConstant.RESULT_LIST);
				if(solrResults != null && solrResults.size() > 0){
					log.info("updateResAuth:添加专题授权solr响应码("+solrResults.get(0).getCode()+")");
				}
				//向authorg表中添加专题授权
				map.put("topic", AuthOrg.AUTH_STATUS_YES);
			}else if(AUTH_STATUS_DEL.equals(type)){
				List<Long> roleIds = new ArrayList<Long>(); 
				for (AuthRole authRole : rolelist) {
					roleIds.add(authRole.getId());
				}
				this.authOrgRoleService.deleteByEnum(org.getOrgId(), roleIds);
				//删除已授权专题
				this.topicService.deleteTopicAuthByOrgId(org.getOrgId());
				//向authorg表中删除专题授权
				map.put("topic", AuthOrg.AUTH_STATUS_NO);
				//删除菜单授权
//				deleteMenuAuth(org, rolelist);
			}
		}//读书
		else if(index == 0){
			if(AUTH_STATUS_ADD.equals(type)){
				this.addAuth(org, rolelist);
				//向authorg表中添加读书授权
				map.put("ebook", AuthOrg.AUTH_STATUS_YES);
				org.setEbook(AUTH_STATUS_ADD);
			}else if(AUTH_STATUS_DEL.equals(type)){
				List<Long> roleIds = new ArrayList<Long>(); 
				for (AuthRole authRole : rolelist) {
					roleIds.add(authRole.getId());
				}
				this.authOrgRoleService.deleteByEnum(org.getOrgId(), roleIds);
				//向authorg表中删除读书授权
				map.put("ebook", AuthOrg.AUTH_STATUS_NO);
				//删除菜单授权
//				deleteMenuAuth(org, rolelist);
			}
		}//资讯
		else if(index == 3){
			if(AUTH_STATUS_ADD.equals(type)){
				this.addAuth(org, rolelist);
				//向authorg表中添加资讯授权
				map.put("publish", AuthOrg.AUTH_STATUS_YES);
				org.setPublish(AUTH_STATUS_ADD);
			}else if(AUTH_STATUS_DEL.equals(type)){
				List<Long> roleIds = new ArrayList<Long>(); 
				for (AuthRole authRole : rolelist) {
					roleIds.add(authRole.getId());
				}
				this.authOrgRoleService.deleteByEnum(org.getOrgId(), roleIds);
				//向authorg表中删除资讯授权
				map.put("publish", AuthOrg.AUTH_STATUS_NO);
				//删除菜单授权
//				deleteMenuAuth(org, rolelist);
			}
		}else if(index == 4){
			if(AUTH_STATUS_ADD.equals(type)){
				this.addAuth(org, rolelist);
				map.put("picture", AuthOrg.AUTH_STATUS_YES);
				org.setPicture(AUTH_STATUS_ADD);
			}else if(AUTH_STATUS_DEL.equals(type)){
				List<Long> roleIds = new ArrayList<Long>(); 
				for (AuthRole authRole : rolelist) {
					roleIds.add(authRole.getId());
				}
				this.authOrgRoleService.deleteByEnum(org.getOrgId(), roleIds);
				//向authorg表中删除图片授权
				map.put("picture", AuthOrg.AUTH_STATUS_NO);
			}
		}
	}
	
	//通过角色添加授权
	private void addAuth(AuthOrg org, List<AuthRole> rolelist) throws Exception{
		if(rolelist != null && rolelist.size() > 0){
			Date now = new Date();
			List<AuthOrgRole> authOrgRoles = new ArrayList<AuthOrgRole>();
			String orgid = org.getOrgId();
			for(int i = 0, len = rolelist.size(); i < len; i++){
				AuthOrgRole orgrole = new AuthOrgRole();
				orgrole.setCrtDate(now);
				orgrole.setLastUpdate(now);
				orgrole.setOrgId(orgid);
				orgrole.setRoleId(rolelist.get(i).getId());
				authOrgRoles.add(orgrole);
			}
			this.authOrgRoleService.batchSave(authOrgRoles);
		}
	}
	//辅助方法：机构更新去掉报纸授权
	private void deletePaperAuth(String orgId) throws Exception{
		//删除报纸授权信息通知solr
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("orgId", orgId);
		List<PaperAuth> palist = this.paperAuthDao.getByStatus(paramMap);
		String xml = ServerModelTrandsform.objectToXml(palist);
		InputStream in = SolrUtil.sendSolrRequest(GlobalConstant.URL_FILTER_PAPER_DELETE, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		Map result = SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
		List<SolrResult> solrResults = (List<SolrResult>)result.get(GlobalConstant.RESULT_LIST);
		if(solrResults != null && solrResults.size() > 0 && solrResults.get(0).getCode().equals(GlobalConstant.RESULT_STATUS_SUCCESS)){
			//标记已授权报纸为已过期
			paramMap.put("status", PaperAuth.DETAIL_STATUS_EXPIRE);
			this.paperAuthDao.updateByOrgId(paramMap);
			//标记已授权报纸为已过期
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("orgId", orgId);
			param.put("status", OrgPaperOrder.ORDER_STATUS_EXPIRE);
			this.orgPaperOrderDao.updateStatusByOrgId(param);
		}
	}
	
	
	
	//getter and setter
	
	public AuthOrgDao getAuthOrgDao() {
		return authOrgDao;
	}
	public void setAuthOrgDao(AuthOrgDao authOrgDao) {
		this.authOrgDao = authOrgDao;
	}
	public TopicService getTopicService() {
		return topicService;
	}
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}
	public AuthUserService getAuthUserService() {
		return authUserService;
	}
	public void setAuthUserService(AuthUserService authUserService) {
		this.authUserService = authUserService;
	}
	public AuthRoleService getAuthRoleService() {
		return authRoleService;
	}
	public void setAuthRoleService(AuthRoleService authRoleService) {
		this.authRoleService = authRoleService;
	}
	public AuthOrgRoleService getAuthOrgRoleService() {
		return authOrgRoleService;
	}
	public void setAuthOrgRoleService(AuthOrgRoleService authOrgRoleService) {
		this.authOrgRoleService = authOrgRoleService;
	}
	public PaperAuthDao getPaperAuthDao() {
		return paperAuthDao;
	}
	public void setPaperAuthDao(PaperAuthDao paperAuthDao) {
		this.paperAuthDao = paperAuthDao;
	}
	public OrgPaperOrderDao getOrgPaperOrderDao() {
		return orgPaperOrderDao;
	}
	public void setOrgPaperOrderDao(OrgPaperOrderDao orgPaperOrderDao) {
		this.orgPaperOrderDao = orgPaperOrderDao;
	}
	public MenuService getMenuService() {
		return menuService;
	}
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}
}
