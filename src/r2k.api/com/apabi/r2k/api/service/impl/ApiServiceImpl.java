package com.apabi.r2k.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.apabi.r2k.admin.service.PaperSubService;
import com.apabi.r2k.admin.service.SuggestService;
import com.apabi.r2k.api.service.ApiService;
import com.apabi.r2k.api.utils.ApiUtils;
import com.apabi.r2k.common.base.ServerModelTrandsform;
import com.apabi.r2k.common.email.SendMailUtil;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.XmlUtil;
import com.apabi.r2k.menu.model.Menu;
import com.apabi.r2k.menu.service.MenuService;
import com.apabi.r2k.paper.service.SyncMessageService;
import com.apabi.r2k.security.dao.AuthOrgDao;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.userdetails.MyUserDetails;
import com.apabi.r2k.security.userdetails.UserDetailFactory;
import com.apabi.r2k.security.utils.AuthenticationTokenUtil;

@Service("apiService")
public class ApiServiceImpl implements ApiService {

	private Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);
	
	@Resource(name="authOrgDao")
	private AuthOrgDao authOrgDao;
	@Resource(name="syncMessageService")
	private SyncMessageService syncMessageService;
	@Resource(name="paperSubService")
	private PaperSubService paperSubService;
	@Resource(name="suggestService")
	private SuggestService suggestService;
	@Resource(name="menuService")
	private MenuService menuService;
	@Resource(name="sendMailUtil")
	private SendMailUtil mailUtil;
	@Resource(name="myUserDetailsService")
	private UserDetailFactory userDetailFactory;

	public int saveOrDelPaperSubs(List<Node> nodes, String orgId, String userId, String method) throws Exception{
		return paperSubService.saveOrDelPaperSubs(nodes, orgId, userId, method);
	}
	
	@Override
	public String getPaperSubs(String orgId, String userId) throws Exception {
		return paperSubService.getPaperSubs(orgId, userId);
	}

	@Override
	public int saveSuggest(List<Node> nodes, String orgId, String userId) throws Exception {
		return suggestService.saveSuggest(nodes, orgId, userId);
	}

	@Override
	public String fuzzySearchOrg(String name_startsWith) throws Exception {
		if(StringUtils.isBlank(name_startsWith)){
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name_startsWith", name_startsWith);
		List<AuthOrg> orgList = authOrgDao.fuzzySearchOrg(map);
		return ServerModelTrandsform.objectToXml(orgList);
	}

	/**
	 * 检查该期是否有cebx
	 */
	@Override
	public boolean checkHasCebx(String periodId) throws Exception{
		String url = GlobalConstant.URL_PERIOD_GET + "&id=" + periodId+"&from=1&to=10000";
		log.info("checkHasCebx:[url#"+url+"]:消息分发前检查");
		Document doc = XmlUtil.getDataFromSolr(url);
		int cebxNum = getCebxNum(doc);
		log.info("checkHasCebx:[期次:"+periodId+",cebx数量:"+cebxNum+"]");
		if(cebxNum > 0){
			return true;
		}
		return false;
	}
	
	//获取该期cebx数量
	private int getCebxNum(Document doc){
		List<Element> cebxs = (List<Element>)XmlUtil.getNodes(doc, "//Period//CebxFile");
		List<String> urls = new ArrayList<String>();
		for(Element cebx : cebxs){
			String url = cebx.getStringValue();
			if(url != null && !url.trim().isEmpty()){
				urls.add(url);
			}
		}
		return urls.size();
	}
	
	@Override
	public void createPaperMsg(String paperId, String periodId, String publishDate)
			throws Exception {
		syncMessageService.savePaperMsg(paperId, periodId, publishDate);
	}

	@Override
	public int getEnumId(String deviceType) throws Exception {
		DevTypeEnum devTypeEnum = DevTypeEnum.findName(deviceType);
		if(devTypeEnum == null){
			return -1;
		}
		return devTypeEnum.getId();
	}



	@Override
	public String getMenuRoot() {
		return Menu.RELATIVE_ROOT;
	}

	@Override
	public void sendMail(String emailAdress, Map<String, Object> mailContent)
			throws Exception {
		mailUtil.sendMail(emailAdress, mailContent);
	}
	
	@Override
	public boolean login(String orgId, String userName, String password, String userAgent) throws Exception {
		MyUserDetails userDetails = userDetailFactory.createNormalUserDetails(userName, password, orgId, userAgent);
		if(userDetails == null){
			return false;
		}
		return AuthenticationTokenUtil.createAndAddToContext(userDetails);
	}
	
	/**
	 * 获取文件的绝对路径
	 * @param orgid
	 * @param useragent
	 * @param deviceid
	 * @param filetype menu\info
	 * @paprm filename @filetype[menu.zip,info.html] 
	 * @return
	 * @throws Exception 
	 */
	public  String getFilePath(AuthOrg authOrg,String devicetype,String deviceid,String filetype,String filename) throws Exception{
		//
		String filePath = "";
		String orgid= authOrg.getOrgId();
		String rootPath = PropertiesUtil.getRootPath();
		String basePath =PropertiesUtil.getValue("base.r2kfile")+"/"+ApiUtils.getFileBasePath(filetype)+"/"+orgid;
		String deviceIdPath = deviceid+"/"+filename;
		//菜单设备类型路径
		String deviceTypePath = devicetype+"/"+filename;
		///菜单设备id物理路径
		String fileDeviceIdPath=rootPath+"/"+basePath+"/"+deviceIdPath;
		if(!ApiUtils.fileExists(fileDeviceIdPath)){//设备上不否存在文件
			String fileDeviceTypePath = rootPath+"/"+basePath+"/"+deviceTypePath;
			if(!ApiUtils.fileExists(fileDeviceTypePath)){//设备类型上是否存在文件
				if(filetype.equalsIgnoreCase(PropertiesUtil.get("path.menu"))){//如果是菜单类型，设备上和设备类型上都不存在，生成菜单
					menuService.initDefMenusForDeviceType(authOrg,devicetype,fileDeviceTypePath);
					filePath = basePath+"/"+deviceTypePath;
				}else if(filetype.equalsIgnoreCase(PropertiesUtil.get("path.info"))){
					//info/Android-Large/welcome.html
					filePath= PropertiesUtil.getValue("base.r2k")+"/"+PropertiesUtil.get("path.info")+"/"+devicetype+"/"+"welcome.html";
				}
			}else{
				filePath = basePath+"/"+deviceTypePath;
			}
			
			
		}else{
			filePath = basePath+"/"+deviceIdPath;
		}
		
		return filePath;
			
		
	}

}
