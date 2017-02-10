package com.apabi.r2k.admin.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.PaperAuth;

public interface PaperAuthService {
	//增加授权
	public void addPaperAuth(List<PaperAuth> list) throws Exception;
	//查询授权
	public List<PaperAuth> getPaperAuth(String orgId) throws Exception;
	//通过机构id报纸id查询记录
	public List<PaperAuth> getPaperAuthById(String orgId, String id) throws Exception;
	//通过机构id报纸id删除记录
	public void deletePaperAuthById(String orgId, String id) throws Exception;
	//通过订单id和机构id删除记录
	public void deletePaperAuthByorderId(Map<String,Object> paramMap) throws Exception;
	//删除订单及相应授权
	public void deleteOrgPaperOrder(String orgId, int orderId, String status) throws Exception;
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest) throws Exception;
	
	public Page<?> orderDetailQuery(PageRequest<?> pageRequest) throws Exception;
	/**
	 * 更新报纸授权
	 * @param paperAuth
	 */
	public boolean updatePaperAuth(PaperAuth paperAuth) throws Exception;
	
	public List<PaperAuth> getPaperAuthByorderId(String orgId, int orderId) throws Exception;
	
	public List<PaperAuth> getByStatus(String orgId, String status) throws Exception;
	
	public Map createPaperFilterInfo(String url, List<PaperAuth> talist) throws Exception;
	
	public Map deletePaperFilterInfo(String url, List<PaperAuth> talist) throws Exception;
	
	//通过机构orgId更新报纸授权状态
	public int updateByOrgId(String orgId, String status)throws Exception;
	
	public void batchSavePaperAuth(List<PaperAuth> paperAuths) throws Exception;
	
	public void batchUpdatePaperAuth(List<PaperAuth> paperAuths) throws Exception;
	//查询所有过期报纸订单,并通知solr
	public void updateAuthExpire(String now);
	//更新所有即将生效报纸订单,并通知solr
	public void updateUnauthOrder(String format);
	
	//根据报纸id、出版日期获取授权的机构列表
	public List<PaperAuth> findByPaperAndDate(String paperId, String publishDate) throws Exception;
}
