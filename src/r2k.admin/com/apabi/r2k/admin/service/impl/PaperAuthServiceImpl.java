package com.apabi.r2k.admin.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.OrgPaperOrderDao;
import com.apabi.r2k.admin.dao.PaperAuthDao;
import com.apabi.r2k.admin.dao.RecommendDao;
import com.apabi.r2k.admin.model.OrgPaperOrder;
import com.apabi.r2k.admin.model.PaperAuth;
import com.apabi.r2k.admin.model.Recommend;
import com.apabi.r2k.admin.service.PaperAuthService;
import com.apabi.r2k.common.base.ServerModelTrandsform;
import com.apabi.r2k.common.solr.SolrResult;
import com.apabi.r2k.common.solr.SolrUtil;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.HttpUtils;
import com.apabi.r2k.paper.service.SyncMessageService;

@Service("paperAuthService")
public class PaperAuthServiceImpl implements PaperAuthService {
	
	private Logger log = LoggerFactory.getLogger(PaperAuthServiceImpl.class);
	
	@Resource(name="paperAuthDao")
	private PaperAuthDao paperAuthDao;
	@Resource(name="orgPaperOrderDao")
	private OrgPaperOrderDao orgPaperOrderDao;
	@Resource(name="recommendDao")
	private RecommendDao recommendDao;
	@Resource(name="syncMessageService")
	private SyncMessageService syncMessageService;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void addPaperAuth(List<PaperAuth> list) throws Exception{
		paperAuthDao.addPaperAuth(list);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<PaperAuth> getPaperAuth(String orgId) throws Exception{
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(orgId)){
			paramMap.put("orgId", orgId);
		}
		return paperAuthDao.getPaperAuth(paramMap);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<PaperAuth> getPaperAuthById(String orgId, String paperId ) throws Exception{
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(orgId) && StringUtils.isNotBlank(paperId)){
			paramMap.put("orgId", orgId);
			paramMap.put("paperId", paperId);
		}
		return paperAuthDao.getPaperAuthById(paramMap);
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public void deletePaperAuthById(String orgId, String paperId) throws Exception{
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(orgId) && StringUtils.isNotBlank(paperId)){
			paramMap.put("orgId", orgId);
			paramMap.put("paperId", paperId);
		}
		paperAuthDao.deletePaperAuthById(paramMap);
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public void deletePaperAuthByorderId(Map<String,Object> paramMap) throws Exception{
		paperAuthDao.deletePaperAuthByorderId(paramMap);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteOrgPaperOrder(String orgId, int orderId, String status) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("status", status);
		log.info("deleteOrgPaperOrder:[orgId#"+orgId+",orderId#"+orderId+",status#"+status+"]:将订单状态置为已删除");
		orgPaperOrderDao.deleteOrgPaperOrder(params);		//将订单状态置为已删除
		List<PaperAuth> paperAuths = getPaperAuthByorderId(orgId, orderId);
		params.put("orgId", orgId);
		deletePaperAuthByorderId(params);			//获取该订单下的所有报纸授权
		log.info("deleteOrgPaperOrder:[orgId#"+orgId+",size#"+paperAuths.size()+"]:开始删除报纸filter");
		deletePaperFilterInfo(GlobalConstant.URL_FILTER_PAPER_DELETE,paperAuths);	//删除报纸filter
	}
	
	public Page<?> orderDetailQuery(PageRequest<?> pageRequest) throws Exception {
		return paperAuthDao.orderDetailQuery("orderDetailSelect", pageRequest,"orderDetailSelectCount");
	}
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest) throws Exception {
		Page p = paperAuthDao.pageQuery(GlobalConstant.PAGE_QUERY_STATEMENT, pageRequest);
		/*
		Map param = (Map) pageRequest.getFilters();
		String orgId = (String)param.get(GlobalConstant.KEY_ORGID);
		int pageNumber = pageRequest.getPageNumber();
		int pageSize = pageRequest.getPageSize();
		
		SolrParam solrParam = SolrUtil.getSolrPageParam(pageNumber, pageSize);
		String url = GlobalConstant.URL_PAPER_GET+"&"+GlobalConstant.URL_PARAM_ORG+"="+orgId+"&"+SolrUtil.joinUrlParam(solrParam, null, null, null);
		System.out.println("url = " + url);
		
		//按报纸名称查询
		String q = (String) param.get("paperName");
		if (q!=null) {
			url = url + "&q=" + q;
			List<PaperAuth> palist1 = new ArrayList<PaperAuth>();
			Map result1 = getDataFromSolr(url, "Paper", Paper.class, "id");
	        Set set1 = result1.entrySet(); 
	        for(Iterator iter = set1.iterator(); iter.hasNext();){
	        	Map.Entry entry = (Map.Entry)iter.next();
	        	String key = (String)entry.getKey();
	        	String value = (String)entry.getValue();		         
	        	for(int i = 0, len = palist.size(); i < len; i++){
	        		String sidString = palist.get(i).getPaperId();
	        		if (sidString.trim().equals(key.trim())) {
	        			paper = new Paper();
	        			paper.setId(key);
	        			paper.setPaperName(value);		    	         
	        			palist.get(i).setPaper(paper);		    	         
	        			palist1.add(palist.get(i));
	        		} 
	        	}		         
	        }	
	        p.setResult(palist1);
			return p;
		    }
		
		List<PaperAuth> palist = p.getResult();
		//查询所有分页
		Map result = getDataFromSolr(url, "Paper", Paper.class, "id");
        Set set = result.entrySet(); 
        for(Iterator iter = set.iterator(); iter.hasNext();)
        {
         Map.Entry entry = (Map.Entry)iter.next();
         String key = (String)entry.getKey();
         String value = (String)entry.getValue();
         for(int i = 0, len = palist.size(); i < len; i++){
        	 String sidString = palist.get(i).getPaperId();
        	 if (sidString.trim().equals(key.trim())) {
    	         paper = new Paper();
    	         paper.setId(key);
    	         paper.setPaperName(value);
        		 palist.get(i).setPaper(paper);
			} 
           }
        }
		p.setResult(palist);
		*/
		return p;
	}
	public Map getDataFromSolr(String url,String alias, Class clazz,String...attributes) throws Exception {
		System.out.println(url);
		HttpEntity entity = HttpUtils.httpGet(url);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(entity.getContent());
		Element root = (Element) doc.getRootElement();
		return xmlToObject(root,alias,clazz,attributes);
	}

	public Map<String,String> xmlToObject(Element root,String alias, Class clazz,String...attributes) throws Exception{
		Element Papers = (Element) root.elements().get(0);
		List<Element> Paper = Papers.elements();
		Map<String,String> result = new HashMap();
		for(Element e : Paper){
			List<Element> paperInfoList = e.elements();
			String id = (String) e.attribute("id").getData();
			for(Element e1: paperInfoList){
				if ((String)((Element)e1.elements().get(0)).getName() == "PaperName") {
					String name = ((Element)e1.elements().get(0)).getName();
					String text = ((Element)e1.elements().get(0)).getText();
					result.put(id, text);
				   }
				}
			}
		return result;
	}

	//更新报纸授权
	public boolean updatePaperAuth(PaperAuth paperAuth) throws Exception {
		return this.paperAuthDao.updatePaperAuth(paperAuth);
	}
	//根据订单号查询
	public List<PaperAuth> getPaperAuthByorderId(String orgId, int orderId) throws Exception {
		return this.paperAuthDao.getPaperAuthByorderId(orgId, orderId);
	}
	//查询状态为已授权的给定机构下的报纸
	@Transactional(propagation=Propagation.REQUIRED)
	public List<PaperAuth> getByStatus(String orgId, String status) throws Exception{
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(orgId) && StringUtils.isNotBlank(status)){
			paramMap.put("orgId", orgId);
			paramMap.put("status", status);
		}
		return paperAuthDao.getByStatus(paramMap);
	}
	
	//通知sorl创建报纸
	@Transactional(propagation=Propagation.REQUIRED)
	public Map createPaperFilterInfo(String url, List<PaperAuth> talist) throws Exception {
		String xml = ServerModelTrandsform.objectToXml(talist);
		InputStream in = SolrUtil.sendSolrRequest(url, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		return SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
	}
	//通知sorl删除报纸
	@Transactional(propagation=Propagation.REQUIRED)
	public Map deletePaperFilterInfo(String url, List<PaperAuth> talist) throws Exception {
		String xml = ServerModelTrandsform.objectToXml(talist);
		InputStream in = SolrUtil.sendSolrRequest(url, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		return SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
	}
	
	//通过机构orgId更新报纸授权状态
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateByOrgId(String orgId, String status)throws Exception{
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(orgId) && StringUtils.isNotBlank(status)){
			paramMap.put("orgId", orgId);
			paramMap.put("status", status);
		}
		return this.paperAuthDao.updateByOrgId(paramMap);
	}

	@Override
	public void batchSavePaperAuth(List<PaperAuth> paperAuths) throws Exception {
		List<PaperAuth> failList = null;
		if(paperAuths != null && paperAuths.size() > 0){
			failList = paperAuthDao.batchSavePaperAuth(paperAuths);
		}
		if(failList != null && failList.size() > 0){
			log.info("batchSavePaperAuth:批量创建报纸授权失败" + failList.size() +"条，再次创建");
			failList = paperAuthDao.batchSavePaperAuth(failList);
		}
		if(failList != null && failList.size() > 0){
			String orgId = null;
			PaperAuth pa = failList.get(0);
			if(pa != null){
				orgId = pa.getOrgId();
			}
			log.info("batchSavePaperAuth:[orgId#"+orgId+"]:再次创建报纸授权失败,失败的报纸id:{"+getFailImportPapers(failList)+"}");
		}
	}

	@Override
	public void batchUpdatePaperAuth(List<PaperAuth> paperAuths)
			throws Exception {
		List<PaperAuth> failList = null;
		if(paperAuths != null && paperAuths.size() > 0){
			failList = paperAuthDao.batchUpdatePaperAuth(paperAuths);
		}
		if(failList != null && failList.size() > 0){
			log.info("batchUpdatePaperAuth:批量更新报纸授权失败" + failList.size() +"条，再次更新");
			failList = paperAuthDao.batchUpdatePaperAuth(failList);
		}
		if(failList != null && failList.size() > 0){
			String orgId = null;
			PaperAuth pa = failList.get(0);
			if(pa != null){
				orgId = pa.getOrgId();
			}
			log.info("batchUpdatePaperAuth:[orgId#"+orgId+"]:再次更新报纸授权失败,失败的报纸id:{"+getFailImportPapers(failList)+"}");
		}
	}
	
	private String getFailImportPapers(List<PaperAuth> failList) throws Exception{
		StringBuilder paperIds = new StringBuilder("");
		int size = failList.size();
		for(int i = 0; i < size; i++){
			PaperAuth pa = failList.get(i);
			paperIds.append(pa.getPaperId());
			if(i < (size - 1)){
				paperIds.append(",");
			}
		}
		return paperIds.toString();
	}

	//修改状态为已生效但过期的报纸订单,并通知solr
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateAuthExpire(String now){
		//获取状态为已生效但过期的报纸订单
		List<OrgPaperOrder> orderlist = null;
		try {
			orderlist = this.getAllAuthOrder(OrgPaperOrder.ORDER_STATUS_AUTH, null, now);
		} catch (Exception e1) {
			log.error("doAuthExpire:" + now + "-获取状态为已生效但过期的报纸订单发生错误");
			e1.printStackTrace();
		}
		for (OrgPaperOrder order : orderlist) {
			if(order != null){
				int orderId = order.getOrderId();
				String orgId = order.getOrgId();
				try {
					//修改状态为已生效但过期的报纸订单
					this.updateOrderStatus(orderId, OrgPaperOrder.ORDER_STATUS_EXPIRE);
					//删除当前机构推荐表中的报纸记录
					this.recommendDao.deleteRecommendByOrgId(orgId, Recommend.RECOMMEND_TYPE_PAPER);
					
					List palist = this.paperAuthDao.getPaperAuthByorderId(orgId, orderId);
					if(palist != null && palist.size() > 0){
						//将过期的订单数据通知solr
						Map returnMsg =	deletePaperFilterInfo(GlobalConstant.URL_FILTER_PAPER_DELETE, palist);
						List<SolrResult> solrResults = (List<SolrResult>)returnMsg.get(GlobalConstant.RESULT_LIST);
						if(solrResults != null && solrResults.size() > 0){
							log.info("doAuthExpire:" + now + "-报纸订单已过期定时任务:机构("+order.getOrgId()+"),订单号("+orderId+"),返回值("+solrResults.get(0).getCode()+")");
						}
					}
				} catch (Exception e) {
					log.error("doAuthExpire:" + now + "-报纸订单已过期定时任务错误:机构("+orgId+"),订单号("+orderId+")");
					e.printStackTrace();
				}
			}
		}
	}
	
	//修改即将生效的报纸订单,并通知solr
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateUnauthOrder(String now){
		//获取状态为未生效但已经生效的报纸订单
		List<OrgPaperOrder> orderlist = null;
		try {
			orderlist = this.getAllAuthOrder(OrgPaperOrder.ORDER_STATUS_UNAUTH, now, null);
		} catch (Exception e1) {
			log.error("doAuthExpire:" + now + "-获取状态为已生效但过期的报纸订单发生错误");
			e1.printStackTrace();
		}
		for (OrgPaperOrder unauthOrder : orderlist) {
			try {
				//获取当前机构未生效订单明细
				List<PaperAuth> unauthlist = getPaperAuthByStatus(null, unauthOrder.getOrderId(), OrgPaperOrder.ORDER_STATUS_UNAUTH);
				//获取当前机构生效订单明细
				List<PaperAuth> authlist = getPaperAuthByStatus(unauthOrder.getOrgId(), null, OrgPaperOrder.ORDER_STATUS_AUTH);
				//存在已生效订单
				if(authlist != null && authlist.size() > 0){
					Map<String, PaperAuth> authMap = new HashMap<String, PaperAuth>();
					for (PaperAuth auth : authlist) {
						authMap.put(auth.getPaperId(), auth);
					}
					for (PaperAuth unauth : unauthlist) {
						if(unauth != null){
							String paperId = unauth.getPaperId();
							if(authMap.containsKey(paperId)){
								this.updatePaperStatusByPaperId(authMap.get(paperId).getOrderId(), OrgPaperOrder.ORDER_STATUS_EXPIRE, paperId);
							}
						}
					}
				}
				//更新即将生效的订单和订单明细的状态
				this.updateOrderStatus(unauthOrder.getOrderId(), OrgPaperOrder.ORDER_STATUS_AUTH);
				//生效订单通知solr
				if(unauthlist != null && unauthlist.size() > 0){
					Map updatereturnMsg = createPaperFilterInfo(GlobalConstant.URL_FILTER_PAPER_CREATE, unauthlist);
					List<SolrResult> solrResults = (List<SolrResult>)updatereturnMsg.get(GlobalConstant.RESULT_LIST);
					if(solrResults != null && solrResults.size() > 0){
						syncMessageService.distributeFilterMsg(unauthOrder.getOrgId());
						log.info("checkAuthExpire:"+now+"-报纸订单是否及时生效定时任务:订单号("+unauthOrder.getOrderId()+"),返回值("+solrResults.get(0).getCode()+")");
					}
				}
			} catch (Exception e) {
				log.error("updateUnauthOrder:即将生效的报纸订单"+unauthOrder.getOrderId()+"错误。");
				e.printStackTrace();
			}
		}//end for 未生效订单遍历
	}
	
	
	//根据订单状态查询所有订单列表(传入授权开始和结束时间用于定时任务)
	public List<OrgPaperOrder> getAllAuthOrder(String status, String startDate, String endDate) throws Exception{
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("status", status);
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		return this.orgPaperOrderDao.getAllAuthOrder(paramMap);
		
	}
	//更新过期订单的订单状态和明细状态
	public void updateOrderStatus(int orderId, String status) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("orderId", orderId);
		this.orgPaperOrderDao.updateStatus(map);
		this.paperAuthDao.updatePaperAuthStatus(map);
	}
	//根据订单状态查询订单明细
	public List<PaperAuth> getPaperAuthByStatus(String orgId, Integer orderId, String status) throws Exception{
		Map<String,Object> authMap = new HashMap<String,Object>();
		authMap.put("orgId", orgId);
		authMap.put("status", status);
		if(orderId != null){
			authMap.put("orderId", orderId);
		}
		return this.paperAuthDao.getByStatus(authMap);
	}
	//更新订单明细一条数据的状态
	public void updatePaperStatusByPaperId(int orderId, String status, String paperId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("orderId", orderId);
		map.put("paperId", paperId);
		this.paperAuthDao.updatePaperAuthStatus(map);
	}

	//根据报纸id、出版日期获取授权的机构列表
	public List<PaperAuth> findByPaperAndDate(String paperId, String publishDate) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("paperId", paperId);
		params.put("publishDate", publishDate);
		params.put("status", PaperAuth.DETAIL_STATUS_AUTH);
		return paperAuthDao.getByPaperAndDate(params);
	}
	
	// getter and setter
	
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

	public RecommendDao getRecommendDao() {
		return recommendDao;
	}

	public void setRecommendDao(RecommendDao recommendDao) {
		this.recommendDao = recommendDao;
	}

}
