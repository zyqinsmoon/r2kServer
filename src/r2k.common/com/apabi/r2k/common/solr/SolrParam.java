package com.apabi.r2k.common.solr;

public class SolrParam {

	private String q;
	private String fq;
	private String sort;
	private int from;
	private int to;

	public static final int DEFAULT_FROM = 1;
	public static final int DEFAULT_TO = 20;
	
	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getFq() {
		return fq;
	}

	public void setFq(String fq) {
		this.fq = fq;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}
}
