package com.apabi.r2k.admin.model;

import java.util.Date;

import com.apabi.r2k.common.base.XmlDateConverter;
import com.apabi.r2k.common.base.XmlIntConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("PaperRecommend")
public class PaperAuth {
	
	@XStreamOmitField
	private int id;
	@XStreamOmitField
	private Date crtDate;		//创建时间
	@XStreamOmitField
	private Date lastDate;		//最后更新时间
	@XStreamOmitField
	private String type;		//报纸类型 是否推荐：0不推荐 1推荐
	
	@XStreamOmitField
	private String orgId;		//机构id
	@XStreamAlias("Paper")
	private String paperId;		//报纸Id
	@XStreamAlias("Position")
	@XStreamConverter(value=XmlIntConverter.class)
	private int position;		//报纸顺序
	
	@XStreamOmitField
	private String paperName;	//报纸名称
	@XStreamOmitField
	@XStreamConverter(value=XmlIntConverter.class)
	private int orderId;	//订单id
	
	@XStreamOmitField
	private Paper paper;
	@XStreamOmitField
	private Recommend recommend;	//报纸推荐
	@XStreamOmitField
	private OrgPaperOrder order;			//订单
	
	@XStreamAlias("StartDate")
	@XStreamConverter(value=XmlDateConverter.class)
	private Date readStartDate;		//可读开始时间
	@XStreamAlias("EndDate")
	@XStreamConverter(value=XmlDateConverter.class)
	private Date readEndDate; 		//可读开始时间
	@XStreamOmitField
	private String status;			//订单状态（1未生效，2已生效，3已过期，4已删除）
	
	
	//报纸类型 是否推荐：0不推荐 1推荐
	public static final String PAPER_TYPE_GENERAL = "0";
	public static final String PAPER_TYPE_RECOMMEND = "1";
	
	public static final String DETAIL_STATUS_UNAUTH = "1";	//订单状态：1未生效
	public static final String DETAIL_STATUS_AUTH   = "2";	//订单状态：2已生效
	public static final String DETAIL_STATUS_EXPIRE = "3";	//订单状态：3已过期
	public static final String DETAIL_STATUS_DELETE = "4";	//订单状态：4已删除
	
	
	
	public Date getReadStartDate() {
		return readStartDate;
	}
	public void setReadStartDate(Date readStartDate) {
		this.readStartDate = readStartDate;
	}
	public Date getReadEndDate() {
		return readEndDate;
	}
	public void setReadEndDate(Date readEndDate) {
		this.readEndDate = readEndDate;
	}
	public Paper getPaper() {
		return paper;
	}
	public void setPaper(Paper paper) {
		this.paper = paper;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public Date getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	public String getPaperId() {
		return paperId;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public Recommend getRecommend() {
		return recommend;
	}
	public void setRecommend(Recommend recommend) {
		this.recommend = recommend;
	}
	public String getPaperName() {
		return paperName;
	}
	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public OrgPaperOrder getOrder() {
		return order;
	}
	public void setOrder(OrgPaperOrder order) {
		this.order = order;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if(obj instanceof PaperAuth){
			PaperAuth pa = (PaperAuth)obj;
			if(this.orgId.equals(pa.getOrgId()) && this.paperId.equals(pa.getPaperId())){
				return true;
			}
		}
		return false;
	}
}
