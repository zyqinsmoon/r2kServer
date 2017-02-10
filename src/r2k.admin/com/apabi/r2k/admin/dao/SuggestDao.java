package com.apabi.r2k.admin.dao;

import com.apabi.r2k.admin.model.Suggest;


public interface SuggestDao {
	
	/**
	 * 入库
	 */
	public int saveSuggest(Suggest orgSuggest) throws Exception;
}
