package com.apabi.r2k.admin.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.apabi.r2k.common.base.ServerModelTrandsform;
import com.apabi.r2k.common.base.XmlDateConverter;
import com.apabi.r2k.common.base.XmlListConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("Column")
public class Column {
	
	@XStreamAsAttribute
	private int id;
	@XStreamAlias("Parent")
	private int parentId;	//父节点
	@XStreamAlias("Title")
	private String title; //文章标题
	@XStreamAlias("Summary")
	private String summary;	//摘要
	@XStreamAlias("Image")
	private String image;	//图片地址
	@XStreamAlias("Sort")
	private int sort;	//排序
	@XStreamAlias("Link")
	private String link;	//链接地址
	@XStreamAlias("CrtDate")
	@XStreamConverter(value=XmlDateConverter.class)
	private Date crtDate;	//创建时间
	@XStreamOmitField
	private int status;	//状态	0未发布 1发布
	@XStreamAlias("Org")
	private String orgId;	//机构ID
	@XStreamAlias("Thumbnail")
	private String thumbnail;	//缩略图
	@XStreamAsAttribute
	private String type;	//类型：0为栏目，1为文章, 2为欢迎页
	@XStreamAlias("ParentTitle")
	private String parentTitle;	//父标题名称
	@XStreamAlias("Content")
	private String content;//正文内容
	@XStreamAlias("Quote")
	private int quoteId;//引用ID
	@XStreamAlias("Device")
	private String deviceId;//设备ID
	@XStreamAlias("DeviceType")
	private String deviceType;	//设备类型
	@XStreamAlias("QuoteTitle")
	private String quoteTitle;//引用标题
	@XStreamAlias("ChildCount")
	private int childs;
	@XStreamAlias("Childs")
	@XStreamConverter(value=XmlListConverter.class)
	private List<Column> columns;
	@XStreamOmitField
	private InfoTemplate infoTemplate;
	public static final String TYPE_COLUMN = "11";
	public static final String TYPE_ARTICLE = "1";
	public static final String TYPE_WELCOME = "0";
	
	public static final String TYPE_QUOTE_COLUMN = "10";
	public static final String TYPE_QUOTE_ARTICLE = "11";
	public static final String TYPE_QUOTE_WELCOME = "12";
	
	
	public static final String TYPE_QUOTE_PREFIX = "1";
	
	public static final int STATUS_UNPUBLISH = 0;
	public static final int STATUS_PUBLISH = 1;
	
	public static final int SORT_DEFAULT = 1;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}
	public Date getCrtDate() {
		return crtDate;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setParentTitle(String parentTitle) {
		this.parentTitle = parentTitle;
	}
	public String getParentTitle() {
		return parentTitle;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getSort() {
		return sort;
	}
	public int getChilds() {
		return childs;
	}
	public void setChilds(int childs) {
		this.childs = childs;
	}
	public void setQuoteId(int quoteId) {
		this.quoteId = quoteId;
	}
	public int getQuoteId() {
		return quoteId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setQuoteTitle(String quoteTitle) {
		this.quoteTitle = quoteTitle;
	}
	public String getQuoteTitle() {
		return quoteTitle;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public InfoTemplate getInfoTemplate() {
		return infoTemplate;
	}
	public void setInfoTemplate(InfoTemplate infoTemplate) {
		this.infoTemplate = infoTemplate;
	}
}
