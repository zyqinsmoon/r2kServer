package com.apabi.r2k.admin.dao;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.PaperAuth;

public interface PaperAuthDao {
	void addPaperAuth (List<PaperAuth> list) throws Exception;
	List<PaperAuth> getPaperAuth(Map<String,Object> paramMap) throws Exception;
	List<PaperAuth> getPaperAuthById(Map<String,Object> paramMap) throws Exception;
	void deletePaperAuthById(Map<String,Object> paramMap) throws Exception;
	void deletePaperAuthByorderId(Map<String,Object> paramMap) throws Exception;
	//分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest) throws Exception;
	/**
	 * 更新报纸授权
	 * @param paperAuth
	 */
	boolean updatePaperAuth(PaperAuth paperAuth) throws Exception;
	/**
	 * 查询报纸授权对象
	 * @param orgId
	 * @param paperId
	 * @return
	 */
	public List<PaperAuth> getPaperAuthByorderId(String orgId, int orderId) throws Exception;
	
	public PaperAuth getPaperAuthObject(String orgId, String paperId) throws Exception;
	
	public List<PaperAuth> getByStatus(Map<String,Object> paramMap) throws Exception;

    public Page<?> orderDetailQuery(String statementName, PageRequest<?> pageRequest,String countStatementName) throws Exception;
    
    //更新报纸授权状态
	public int updatePaperAuthStatus(Map<String,Object> paramMap)throws Exception;
	
	//通过机构orgId更新报纸授权状态
	public int updateByOrgId(Map<String,Object> paramMap)throws Exception;
	
	public List<PaperAuth> batchSavePaperAuth(List<PaperAuth> paperAuths) throws Exception;
	
	public List<PaperAuth> batchUpdatePaperAuth(List<PaperAuth> paperAuths) throws Exception;
	
	//根据报纸id获取所有报纸
	public List<PaperAuth> getByPaperAndDate(Map<String, Object> params) throws Exception;
}