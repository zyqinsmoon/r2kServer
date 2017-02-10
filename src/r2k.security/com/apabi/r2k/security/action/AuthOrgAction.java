package com.apabi.r2k.security.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.PrjEnum;
import com.apabi.r2k.admin.service.PrjEnumService;
import com.apabi.r2k.admin.service.TopicService;
import com.apabi.r2k.common.base.PageRequestFactory;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.model.AuthRole;
import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.service.AuthOrgService;
import com.apabi.r2k.security.service.AuthRoleService;
import com.apabi.r2k.security.service.AuthUserService;
import com.apabi.r2k.security.utils.SecurityUtil;
import com.apabi.r2k.security.utils.SessionUtils;
import com.apabi.r2k.weather.model.BaseAreacode;
import com.apabi.r2k.weather.service.BaseAreacodeService;
@Controller("authOrgAction")
@Scope("prototype")
public class AuthOrgAction {
	private Logger log = LoggerFactory.getLogger(AuthOrgAction.class);

	@Resource(name="authOrgService")
	private AuthOrgService authOrgService;
	@Resource(name="topicServiceImpl")
	private TopicService topicService;
	@Resource(name="authUserService")
	private AuthUserService authUserService;
	@Resource
	private AuthRoleService authRoleService;
	@Resource(name="prjEnumService")
	private PrjEnumService prjEnumService;
	@Resource(name="baseAreacodeService")
	private BaseAreacodeService baseAreacodeService;
	
	private String orgId;				
	private String orgName;				
	private AuthOrg org;
	private List<AuthOrg> orglist;
	private String status;				//返回状态：-1错误，0失败，1成功
	private String name_startsWith;
	private String callback;
	private Page page;
	private String queryIdOrName;		// 查询字符串
	private List<String> enumCodes;
	private List<PrjEnum> enumlist;
	
	private String oldauth;
	private String newauth;
	
	private String userpwd;	//默认用户密码
	private List<BaseAreacode> provincelist;
	private List<BaseAreacode> citylist;
	private List<BaseAreacode> districtlist;
	private int provinceId;
	private String provinceCode;
	private String cityCode;
	private String districtCode;
	
	//method
	public String checkOrgId(){
		try{
			if(orgId != null && !"".equals(orgId)){
				status = this.authOrgService.checkOrgId(orgId.toLowerCase());
			}else{
				status = "-1";
				log.info("checkOrgId:[orgId#" + orgId + "]:[status#" + status + "]:检查用户失败");
			}
		}catch(Exception e){
			log.error("checkOrgId:[orgId#" + orgId + "]:[status#" + status + "]:检查用户是否存在发生错误");
			e.printStackTrace();
			status = "-1";
		}
		return "checkOrgInfo";
	}
	public String checkOrgName(){
		try{
			if(orgName != null && !"".equals(orgName)){
				status = this.authOrgService.checkOrgName(orgName);
			}else{
				status = "-1";
				log.info("checkOrgName:[orgName#" + orgName + "]:[status#" + status + "]:检查用户失败");
			}
		}catch(Exception e){
			log.error("checkOrgName:[orgName#" + orgName + "]:[status#" + status + "]:检查用户是否存在发生错误");
			e.printStackTrace();
			status = "-1";
		}
		return "checkOrgInfo";
	}
	
	public String saveOrg(){
		String result = "save";
		HttpServletRequest req = ServletActionContext.getRequest();
		try{
			if(org != null && isAdmin(req)){
				String orgId = org.getOrgId().trim().toLowerCase();
				org.setCrtDate(new Date());
				org.setOrgId(orgId);
				org.setOrgName(org.getOrgName().trim());
				//返回默认用户密码
				setUserpwd(this.authOrgService.saveOrg(org, enumCodes));
			}else{
				result = "error";
				log.info("saveOrg:[]:[result#" + result + "]:保存用户失败");
			}
		}catch(Exception e){
			log.error("saveOrg:[]:[result#" + result + "]:保存用户失败");
			e.printStackTrace();
			result = "error";
		}
		return result;
	}
	
	public String toUpdateOrg(){
		try {
			if(orgId != null && !"".equals(orgId)){
				org = this.authOrgService.getOrgObject(orgId);
			}else{
				return "error";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "toUpdateOrg";	 
	}
	
	public String updateOrg(){
		String result = "update";
		boolean flag = true;
		HttpServletRequest req = ServletActionContext.getRequest();
		try{
			if(org != null && org.getOrgId() != null && isAdmin(req)){
				org.setOrgName(org.getOrgName().trim());
				if(oldauth != null && newauth != null){
					String[] oldnode = oldauth.split(",");
					String[] newnode = newauth.split(",");
					this.authOrgService.updateOrg(org, oldnode, newnode);
				}
				result = flag ? "update" : "error";
			}else{
				result = "error";
				log.info("updateOrg:[orgId#" + orgId + "]:[status#" + status + "]:更新用户");
			}
		} catch (Exception e) {
			log.error("updateOrg:[orgId#" + orgId + "]:[status#" + status + "]:更新用户");
			e.printStackTrace();
			return "error";
		}
		return result;
	}
	
	public String deleteOrg(){
		String result = "delete";
		HttpServletRequest req = ServletActionContext.getRequest();
		try{
			if(org != null && isAdmin(req)){
				boolean flag = this.authOrgService.deleteOrg(org);
				if(flag){
					result = "delete";
					log.info("deleteOrg:[orgId#" + orgId + "]:[]:删除用户 成功");
				}else{
					log.info("deleteOrg:[orgId#" + orgId + "]:[]:删除用户失败");
					result = "error";
				}
			}
		} catch (Exception e) {
			log.error("deleteOrg:[orgId#" + orgId + "]:[]:删除用户错误");
			e.printStackTrace();
			return "error";
		}
		return result;
	}
	
	public String findOrgList(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			if(StringUtils.isBlank(orgId)){
				orgId = SessionUtils.getAuthOrgId(req);
			}
			PageRequest pageRequest= new PageRequest();
			PageRequestFactory.bindPageRequest(pageRequest,req);
			Map param = (Map) pageRequest.getFilters();
			param.put("myOrg", orgId);
			page = this.authOrgService.pageQuery(pageRequest, null);
			enumlist = this.prjEnumService.getEnumByDevtype(PrjEnum.ENUMTYPE_RES, PrjEnum.ENUMTYPE_RES_ADMIN);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "list";
	}
	
	/**
	 * 机构模糊查询
	 */
	public void suggest() {
		HttpServletResponse response = ServletActionContext.getResponse();
			try {
				List<AuthOrg> orgList = authOrgService.fuzzySearchOrg(name_startsWith);
				List<Map<String, String>> suggestList = new ArrayList<Map<String, String>>();
				for (AuthOrg org : orgList) {
					Map<String, String> suggestMap = new HashMap<String, String>();
					suggestMap.put("orgName",org.getOrgName() );
					suggestMap.put("orgId", org.getOrgId());
					suggestMap.put("id", ""+org.getId());
					suggestList.add(suggestMap);
				}
				JSONArray jsonArray = JSONArray.fromObject(suggestList);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("orgList", jsonArray);
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write(callback + "(" + jsonObject + ");");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	//查询当前机构信息
	public String getOrgInfo(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			if(StringUtils.isBlank(orgId)){
				orgId = SessionUtils.getAuthOrgId(req);
			}else{
//				SessionUtils.putOrg(orgId);
				List<AuthRole> authRoles = authRoleService.getRolesByOrg(orgId);
				AuthUser authUser = SessionUtils.getCurrentUser(req);
				authUser.setAuthRoleList(authRoles);
				SecurityUtil.setAuthRoles(authRoles);
			}
			if(orgId != null && !"".equals(orgId)){
				org = this.authOrgService.getOrgObject(orgId);
				SecurityUtil.setCurrentOrg(org);
				PageRequest pageRequest = new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest, req);
				Map param = (Map) pageRequest.getFilters();
				param.put("authOrgId", org.getOrgId());
				page = this.authUserService.findByPageRequest(pageRequest);
				enumlist = this.prjEnumService.getEnumByDevtype(PrjEnum.ENUMTYPE_RES, PrjEnum.ENUMTYPE_RES_ADMIN);
			}else{
				return "error";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "info";	 
	}
	//初始化下拉列表
	public String initSelect(){
		try {
			if("0".equals(""+provinceId)){
				provincelist = this.baseAreacodeService.findCodeListByPid(BaseAreacode.PID_TOP);
				citylist = this.baseAreacodeService.findCodeListByPid(BaseAreacode.PID_BEIJING_CITY);
				districtlist = this.baseAreacodeService.findCodeListByPid(BaseAreacode.PID_BEIJING_DISTRICT);
			} else {
				BaseAreacode codeSet = baseAreacodeService.findCodeSetByAreaCode("" + provinceId);
				BaseAreacode province = codeSet.getProvince();
				BaseAreacode city = codeSet.getCity();
				BaseAreacode district = codeSet.getDistrict();
				provincelist = this.baseAreacodeService.findCodeListByPid(BaseAreacode.PID_TOP);
				citylist = this.baseAreacodeService.findCodeListByPid(province.getId());
				districtlist = this.baseAreacodeService.findCodeListByPid(city.getId());
				provinceCode = province.getId() + "_" + province.getAreaCode();
				cityCode = city.getId() + "_" + city.getAreaCode();
				districtCode = district.getId() + "_" + district.getAreaCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "initSelect";
	}
	//触发省级下拉列表
	public String selectProvince(){
		try {
			citylist = baseAreacodeService.findCodeListByPid(provinceId);
			if(citylist != null && citylist.size() > 0){
				int pid = citylist.get(0).getId();
				districtlist = baseAreacodeService.findCodeListByPid(pid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "selectProvince";
	}
	//触发市级下拉列表
	public String selectCity(){
		try {
			districtlist = baseAreacodeService.findCodeListByPid(provinceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "selectCity";
	}
	
	/**
	 * 辅助方法
	 * 判断当前机构是否为apabi
	 * @return
	 */
	private boolean isAdmin(HttpServletRequest request){
		AuthOrg org = SessionUtils.getAuthOrg(request);
		if(org != null){
			if(org.getIsAdmin() == GlobalConstant.IS_ADMIN){
				return true;
			}
		}
//		return "apabi".equals(SessionUtils.getOrg());
		return false;
	}
	


	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public String getName_startsWith() {
		return name_startsWith;
	}
	public void setName_startsWith(String name_startsWith) {
		this.name_startsWith = name_startsWith;
	}
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getQueryIdOrName() {
		return queryIdOrName;
	}
	public void setQueryIdOrName(String queryIdOrName) {
		this.queryIdOrName = queryIdOrName;
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
	public AuthOrg getOrg() {
		return org;
	}
	public void setOrg(AuthOrg org) {
		this.org = org;
	}
	public List<AuthOrg> getOrglist() {
		return orglist;
	}
	public void setOrglist(List<AuthOrg> orglist) {
		this.orglist = orglist;
	}
	public  AuthOrgService getAuthOrgService() {
		return authOrgService;
	}
	public  void setAuthOrgService(AuthOrgService authOrgService) {
		this.authOrgService = authOrgService;
	}
	public  AuthRoleService getAuthRoleService() {
		return authRoleService;
	}
	public  void setAuthRoleService(AuthRoleService authRoleService) {
		this.authRoleService = authRoleService;
	}
	public  List<String> getEnumCodes() {
		return enumCodes;
	}
	public  void setEnumCodes(List<String> enumCodes) {
		this.enumCodes = enumCodes;
	}
	public List<PrjEnum> getEnumlist() {
		return enumlist;
	}
	public void setEnumlist(List<PrjEnum> enumlist) {
		this.enumlist = enumlist;
	}
	public PrjEnumService getPrjEnumService() {
		return prjEnumService;
	}
	public void setPrjEnumService(PrjEnumService prjEnumService) {
		this.prjEnumService = prjEnumService;
	}
	public String getOldauth() {
		return oldauth;
	}
	public void setOldauth(String oldauth) {
		this.oldauth = oldauth;
	}
	public String getNewauth() {
		return newauth;
	}
	public void setNewauth(String newauth) {
		this.newauth = newauth;
	}
	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}
	public String getUserpwd() {
		return userpwd;
	}
	public List<BaseAreacode> getProvincelist() {
		return provincelist;
	}
	public void setProvincelist(List<BaseAreacode> provincelist) {
		this.provincelist = provincelist;
	}
	public List<BaseAreacode> getCitylist() {
		return citylist;
	}
	public void setCitylist(List<BaseAreacode> citylist) {
		this.citylist = citylist;
	}
	public List<BaseAreacode> getDistrictlist() {
		return districtlist;
	}
	public void setDistrictlist(List<BaseAreacode> districtlist) {
		this.districtlist = districtlist;
	}
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
}
