package com.apabi.r2k.admin.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.apabi.r2k.admin.dao.SuggestDao;
import com.apabi.r2k.admin.model.Suggest;
import com.apabi.r2k.common.base.BaseDaoImpl;

@Repository("suggestDao")
public class SuggestDaoImpl extends BaseDaoImpl<Suggest, Serializable> implements SuggestDao {

	@Override
	public int saveSuggest(Suggest orgSuggest) throws Exception {
		return save(orgSuggest);
	}

}
