package com.apabi.r2k.wx.model;


public class Ebook {
	
	//唯一标识符
	private String Identifier;
	//封面地址
	private String CoverUrl;
	private String Title;
	private String Creator;
	private String Publisher;
	private String PublishDate;
	private String ISBN;
	private String BookStatus;
	private String Abstract;
	
	private String borrowUrl;
	private String htmlUrl;
	private String cssUrl;
	
	public String getBorrowUrl() {
		return borrowUrl;
	}
	public void setBorrowUrl(String borrowUrl) {
		this.borrowUrl = borrowUrl;
	}
	public String getIdentifier() {
		return Identifier;
	}
	public void setIdentifier(String identifier) {
		Identifier = identifier;
	}
	public String getCoverUrl() {
		return CoverUrl;
	}
	public void setCoverUrl(String coverUrl) {
		CoverUrl = coverUrl;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getCreator() {
		return Creator;
	}
	public void setCreator(String creator) {
		Creator = creator;
	}
	public String getPublisher() {
		return Publisher;
	}
	public void setPublisher(String publisher) {
		Publisher = publisher;
	}
	public String getPublishDate() {
		return PublishDate;
	}
	public void setPublishDate(String publishDate) {
		PublishDate = publishDate;
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public String getBookStatus() {
		return BookStatus;
	}
	public void setBookStatus(String bookStatus) {
		BookStatus = bookStatus;
	}
	public String getAbstract() {
		return Abstract;
	}
	public void setAbstract(String abstract1) {
		Abstract = abstract1;
	}
	public String getHtmlUrl() {
		return htmlUrl;
	}
	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}
	public String getCssUrl() {
		return cssUrl;
	}
	public void setCssUrl(String cssUrl) {
		this.cssUrl = cssUrl;
	}
}
