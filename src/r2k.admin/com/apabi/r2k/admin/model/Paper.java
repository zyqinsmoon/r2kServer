package com.apabi.r2k.admin.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Paper")
public class Paper {
	
	@XStreamAsAttribute
	@XStreamAlias("id")
   private String id; 
   private String paperName;
   private String desc;
   private String icon;
   private String link;
 /*  @XStreamImplicit
   private Map<String,String> metaInfo;
   @XStreamImplicit
   private Map<String, String> period;
   @XStreamImplicit
   private Map<String, Map<String, String>> periods;*/

   /*  public  Paper() {
	   id = null;
	   paperName = null;
	   desc = null;
	   icon = null;
	   link = null;
	   metaInfo = new LinkedHashMap<String, String>();//对应PaperAuth1的metaInfo节点
	   metaInfo.put("PaperName", null);
	   metaInfo.put("Desc", null);
	   metaInfo.put("Icon", null);
	   metaInfo.put("Link", null);
	   period = new LinkedHashMap<String, String>();//对应PaperAuth1的period节点
	   period.put("id", null);
	   period.put("PublishedDate", null);
	   period.put("UpdateTime", null);
	   period.put("Icon", null);
	   period.put("Link", null);
	   periods = new LinkedHashMap<String, Map<String, String>>();//对应PaperAuth1的periods节点
	   periods.put("period", null);
}*/
   public String getId() {
		return id;
	}
public String getDesc() {
	return desc;
}
public void setDesc(String desc) {
	this.desc = desc;
}
public String getIcon() {
	return icon;
}
public void setIcon(String icon) {
	this.icon = icon;
}
public String getLink() {
	return link;
}
public void setLink(String link) {
	this.link = link;
}
public void setId(String id) {
	this.id = id;
}
public String getPaperName() {
	return paperName;
}
public void setPaperName(String paperName) {
	this.paperName = paperName;
}
/*public Map<String, String> getMetaInfo() {
	return metaInfo;
}
public void setMetaInfo(Map<String, String> metaInfo) {
	this.metaInfo = metaInfo;
}
public Map<String, String> getPeriod() {
	return period;
}
public void setPeriod(Map<String, String> period) {
	this.period = period;
}
public Map<String, Map<String, String>> getPeriods() {
	return periods;
}
public void setPeriods(Map<String, Map<String, String>> periods) {
	this.periods = periods;
}*/

}
