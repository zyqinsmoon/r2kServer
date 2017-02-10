package com.apabi.r2k.admin.model;

import java.io.IOException;
import java.util.Date;

import com.apabi.r2k.common.base.XmlDateConverter;
import com.apabi.r2k.common.base.XmlIntConverter;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("Topic")
public class Topic {

	@XStreamAlias("id")
	@XStreamAsAttribute
	private String id;
	@XStreamAlias("TopicName")
	private String topicName;						//专题名称
	@XStreamAlias("Description")
	private String description;						//专题描述
	@XStreamAlias("Icon")
	private String icon;							//配图地址
	@XStreamAlias("Org")
	private String org;
	@XStreamAlias("AutoCreate")
	@XStreamConverter(value=XmlIntConverter.class)
	private int autoCreate;							//自动生成时间，为空是为手动生成 0手动，1自动。且为1时，condition不能为空
	@XStreamAlias("IncrCreate")
	@XStreamConverter(value=XmlIntConverter.class)
	private int incrCreate;							//增量生成，0：非增量生成，1:增量生成，默认为1
	@XStreamAlias("Results")
	@XStreamConverter(value=XmlIntConverter.class)
	private int results;							//生成结果数，默认为1000
	@XStreamAlias("CreateDate")
	@XStreamConverter(value=XmlDateConverter.class)
	private Date createDate;						//最后更新时间
	@XStreamAlias("UpdateTime")
	@XStreamConverter(value=XmlDateConverter.class)
	private Date updateTime;						//最后更新时间
	@XStreamAlias("Position")
	private String position;						//专题位置默认为0
	@XStreamAlias("Condition")
	private TopicCondition condition;				//生成条件：文章标题; 排序字段：默认为最后更新时间
	@XStreamAlias("IsPublished")
	@XStreamConverter(value=XmlIntConverter.class)
	private int isPublished;						//是否发布：0:未发布,1:已发布,2:已删除
	@XStreamAlias("TopicType")
	@XStreamConverter(value=XmlIntConverter.class)
	private int topicType;							//专题类型：0:普通专题,1:高级专题
	
	//删除状态
	public static final int STATUS_DEL = 2;
	
	//专题类型
	public static final int TOPIC_TYPE_GENERAL = 0;
	public static final int TOPIC_TYPE_ADVANCE = 1;
	
	public Topic() {
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getResults() {
		return results;
	}
	public void setResults(int results) {
		this.results = results;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public int getAutoCreate() {
		return autoCreate;
	}
	public void setAutoCreate(int autoCreate) {
		this.autoCreate = autoCreate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getIncrCreate() {
		return incrCreate;
	}
	public void setIncrCreate(int incrCreate) {
		this.incrCreate = incrCreate;
	}
	public int getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(int isPublished) {
		this.isPublished = isPublished;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}
	
	public String getIconUrl(){
		String iconUrl = "/r2kFile/upload/image/" + org + "/topic/" + icon;
		return iconUrl;
	}

	public TopicCondition getCondition() {
		return condition;
	}

	public void setCondition(TopicCondition condition) {
		this.condition = condition;
	}

	public int getTopicType() {
		return topicType;
	}

	public void setTopicType(int topicType) {
		this.topicType = topicType;
	}
}
