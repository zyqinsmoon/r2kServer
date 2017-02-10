package com.apabi.r2k.admin.model;

import java.util.Date;

import com.apabi.r2k.common.base.XmlDateConverter;
import com.apabi.r2k.common.base.XmlIntConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("TopicRecommend")
public class TopicAuth {
	
	@XStreamOmitField
	private int id;			//id
	@XStreamOmitField
	private Date crtDate;	//创建时间
	@XStreamOmitField
	private Date lastDate;	//最后更新时间
	@XStreamOmitField
	private String type;	//专题类型 是否推荐：0不推荐 1推荐

	@XStreamOmitField
	private String orgId;	//机构id
	@XStreamAlias("Topic")
	private String topicId;	//专题id
	@XStreamAlias("StartDate")
	@XStreamConverter(value=XmlDateConverter.class)
	private Date startDate;	//授权开始时间
	@XStreamAlias("EndDate")
	@XStreamConverter(value=XmlDateConverter.class)
	private Date endDate;	//授权结束时间
	@XStreamAlias("Position")
	@XStreamConverter(value=XmlIntConverter.class)
	private int position;	//专题顺序
	
	@XStreamOmitField
	private Topic topic;	//专题对象
	@XStreamOmitField
	private Recommend recommend;		//专题推荐
	private String topicType;			//专题类型：0:普通专题,1:高级专题
	private String status;				//专题状态（1未生效，2已生效，3已过期，4已删除）
	
	//专题类型 是否推荐：0不推荐 1推荐
	public static final String RECOMMEND_TYPE_GENERAL = "0";
	public static final String RECOMMEND_TYPE_RECOMMEND = "1";
	//专题类型：0:普通专题,1:高级专题
	public static final String TOPIC_TYPE_GENERAL = "0";
	public static final String TOPIC_TYPE_ADVANCED = "1";
	//专题状态（1未生效，2已生效，3已过期，4已删除）
	public static final String TOPIC_STATUS_UNAUTH = "1";	//专题状态：1未生效
	public static final String TOPIC_STATUS_AUTH   = "2";	//专题状态：2已生效
	public static final String TOPIC_STATUS_EXPIRE = "3";	//专题状态：3已过期
	public static final String TOPIC_STATUS_DELETE = "4";	//专题状态：4已删除
	
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
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public Recommend getRecommend() {
		return recommend;
	}
	public void setRecommend(Recommend recommend) {
		this.recommend = recommend;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getTopicType() {
		return topicType;
	}
	public void setTopicType(String topicType) {
		this.topicType = topicType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
