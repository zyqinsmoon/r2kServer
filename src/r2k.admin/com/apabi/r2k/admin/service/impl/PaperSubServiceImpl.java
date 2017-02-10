package com.apabi.r2k.admin.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.apabi.r2k.admin.dao.PaperSubDao;
import com.apabi.r2k.admin.model.Paper;
import com.apabi.r2k.admin.model.PaperAuth;
import com.apabi.r2k.admin.model.PaperSub;
import com.apabi.r2k.admin.model.Subscribe;
import com.apabi.r2k.admin.service.PaperSubService;
import com.apabi.r2k.common.base.ServerModelTrandsform;
import com.apabi.r2k.common.solr.SolrUtil;
import com.apabi.r2k.common.utils.DateUtil;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.XmlUtil;

@Service("paperSubService")
public class PaperSubServiceImpl implements PaperSubService {

	private Logger log = LoggerFactory.getLogger(PaperSubServiceImpl.class);
	@Resource(name = "paperSubDao")
	private PaperSubDao paperSubDao;

	//获取当前用户订阅的报纸
	@Override
	public String getPaperSubs(String orgId, String userId) throws Exception {
		Map params = new HashMap();
		params.put("userId", userId);
		params.put("orgId", orgId);
		params.put("sortColumns", "LAST_UPDATE desc");
		List<PaperSub> paperSubs = paperSubDao.getPaperSubs(params);
		StringBuilder ids = new StringBuilder();
		for (PaperSub paperSub : paperSubs) {
			ids.append(paperSub.getPaperId() + " OR ");
		}
		ids.append("1");
		params.clear();
		params.put("org", orgId);
		params.put("id", ids.toString());
		params.put("from", "1");
		params.put("to", "10000");
		String urlParams = SolrUtil.joinUrlParam(params);
		String url = GlobalConstant.URL_PAPER_GET + "&" + urlParams;
		log.info("subscribe:[userId#" + userId + ",orgId#" + orgId + ",url#"
				+ url + "]:查询订阅");
		InputStream inStream = SolrUtil.getSolrRequest(url);
		Document doc = XmlUtil.getDocumentFromInputStream(inStream);
		for (PaperSub paperSub : paperSubs) {
			Element node = (Element) doc.selectSingleNode("//R2k//Paper[@id='"
					+ paperSub.getPaperId() + "']");
			if (node != null) {
				Element subElement = node.addElement("Subscribe");
				Element crtElement = subElement.addElement("CrtDate");
				crtElement.setText(DateUtil.formatDate(paperSub.getCrtDate()));
			}
		}
		return doc.asXML();
	}

	//批量保存或删除用户订阅报纸
	@Override
	public int saveOrDelPaperSubs(List<Node> nodes, String orgId, String userId, String method) throws Exception {
		List subscribes = new ArrayList();
		Subscribe subscribe = (Subscribe)ServerModelTrandsform.xmlToObject(nodes.get(0));
		int result = 0;
		if(subscribe != null){
			List resources = subscribe.getResources();
			if(subscribe.getType().equals(GlobalConstant.SUBSCRIBE_TYPE_PAPER)){
				if(resources != null && resources.size() > 0){
					for(Object obj : resources){
						Paper paper = (Paper)obj;
						PaperSub paperSub = createPaperSub(userId, orgId, paper);
						subscribes.add(paperSub);
					}
				}
				if(method.toLowerCase().equals(GlobalConstant.HTTP_TYPE_PUT)){
					log.info("subscribe:[userId#"+userId+",orgId#"+orgId+",type#"+subscribe.getType()+",size#"+resources.size()+"]:批量保存用户订阅");
					result = paperSubDao.batchSavePaperSub(subscribes);
				}else if(method.toLowerCase().equals(GlobalConstant.HTTP_TYPE_DELETE)){
					log.info("subscribe:[userId#"+userId+",orgId#"+orgId+",type#"+subscribe.getType()+",size#"+resources.size()+"]:批量删除用户订阅");
					Map params = new HashMap();
					params.put("userId", userId);
					params.put("orgId", orgId);
					params.put("paperSubs", subscribes);
					result = paperSubDao.batchDeletePaperSub(params);
				}
				log.info("subscribe批量操作成功执行"+result+"条");
			}
		}
		return result;
	}

	//根据报纸id、期次出版日期获取订阅列表
	public List<PaperSub> findByPaperAndDate(String paperId, String publishDate) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("paperId", paperId);
		params.put("publishDate", publishDate);
		params.put("status", PaperAuth.DETAIL_STATUS_AUTH);
		return paperSubDao.getByPaperAndDate(params);
	}
	
	private PaperSub createPaperSub(String userId, String orgId, Paper paper) {
		PaperSub paperSub = new PaperSub();
		Date now = new Date();
		paperSub.setUserId(userId);
		paperSub.setOrgId(orgId);
		paperSub.setCrtDate(now);
		paperSub.setLastUpdate(now);
		paperSub.setPaperId(paper.getId());
		paperSub.setSort(0);
		return paperSub;
	}

	public PaperSubDao getPaperSubDao() {
		return paperSubDao;
	}

	public void setPaperSubDao(PaperSubDao paperSubDao) {
		this.paperSubDao = paperSubDao;
	}
}
