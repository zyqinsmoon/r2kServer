package com.apabi.r2k.security.model;

import java.util.List;

public class AuthEntity {
	private static final long serialVersionUID = 1L;
	//alias
	public static final String TABLE_ALIAS = "AuthEntity";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_ENTITY_CODE = "entityCode";
	public static final String ALIAS_ENTITY_NAME = "entityName";
	public static final String ALIAS_ENTITY_DES = "entityDes";
	public static final String ALIAS_CRT_DATE = "crtDate";
	public static final String ALIAS_LAST_UPDATE = "lastUpdate";
	
	//columns START
	private java.lang.Long id;
	private String entityCode;
	private String entityName;
	private String entityDesc;
	private java.util.Date crtDate;
	private java.util.Date lastUpdate;
	private int order;
	private List<AuthRes> authReses;
	//columns END

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getId() {
		return this.id;
	}
	public void setEntityCode(String value) {
		this.entityCode = value;
	}
	
	public String getEntityCode() {
		return this.entityCode;
	}
	public void setEntityName(String value) {
		this.entityName = value;
	}
	
	public String getEntityName() {
		return this.entityName;
	}
	public void setCrtDate(java.util.Date value) {
		this.crtDate = value;
	}
	
	public java.util.Date getCrtDate() {
		return this.crtDate;
	}
	
	public void setLastUpdate(java.util.Date value) {
		this.lastUpdate = value;
	}
	
	public java.util.Date getLastUpdate() {
		return this.lastUpdate;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)   
        {   
            return true;   
        }   
          
    if (obj.getClass() == AuthEntity.class)   
        {   
    	AuthEntity ae = (AuthEntity)obj;   
            return ae.id.equals(this.id);  
        }   
        return false;  
	}

	public List<AuthRes> getAuthReses() {
		return authReses;
	}

	public void setAuthReses(List<AuthRes> authReses) {
		this.authReses = authReses;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getEntityDesc() {
		return entityDesc;
	}

	public void setEntityDesc(String entityDesc) {
		this.entityDesc = entityDesc;
	}
}

