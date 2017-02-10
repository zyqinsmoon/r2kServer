package com.apabi.r2k.admin.service;

import java.util.List;

import org.dom4j.Node;

/**
 * 意见反馈service接口
 * @author l.wen
 */
public interface SuggestService {

	public int saveSuggest(List<Node> nodes, String orgId, String userId) throws Exception;
}
