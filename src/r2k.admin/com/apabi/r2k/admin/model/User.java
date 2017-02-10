package com.apabi.r2k.admin.model;

import com.apabi.r2k.security.model.AuthOrg;

public class User {

	private AuthOrg org;			//用户所属机构
	private AuthOrg currentOrg;		//用户当前操作机构
	public AuthOrg getOrg() {
		return org;
	}
	public void setOrg(AuthOrg org) {
		this.org = org;
	}
	public AuthOrg getCurrentOrg() {
		return currentOrg;
	}
	public void setCurrentOrg(AuthOrg currentOrg) {
		this.currentOrg = currentOrg;
	}

	
	
}
