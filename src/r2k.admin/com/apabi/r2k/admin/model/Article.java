package com.apabi.r2k.admin.model;

import com.apabi.r2k.common.base.XmlIntConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("Article")
public class Article {

	@XStreamAlias("id")
	@XStreamAsAttribute
	private String id;
	@XStreamAlias("ArticleId")
	private String articleId;
	@XStreamAlias("Topic")
	private String topic;
	@XStreamAlias("PaperName")
	private String paperName;
	@XStreamAlias("HeadLine")
	private String headLine;
	@XStreamAlias("Author")
	private String author;
	@XStreamAlias("Period")
	private String period;
	@XStreamAlias("PeriodType")
	private String periodType;
	@XStreamAlias("Position")
	@XStreamConverter(value=XmlIntConverter.class)
	private int position;
	
	public Article() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeadLine() {
		return headLine;
	}

	public void setHeadLine(String headLine) {
		this.headLine = headLine;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getPaperName() {
		return paperName;
	}

	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
}
