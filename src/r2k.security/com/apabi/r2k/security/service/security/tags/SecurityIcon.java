package com.apabi.r2k.security.service.security.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 权限按钮标签 
 * @usage 将需要作为权限按钮的HTML或JS代码列入该标签的标签体内。并为该标签指定按钮编码。只要拥有对应按钮编码的用户。就能看到标签体内容
 * 注意 此标签需要以页面传参的方式（其他方式也行，将参数放到request里即可）传入用户具有哪些按钮编码。传入的格式为 [按钮编码]=1 只传具备的按钮编码
 * 另外 标签的按钮编码可以为多个 以,隔开。可以通过mode指定匹配策略。all：表示用户必须具备全部的这些编码。才能显示按钮。Any标示具备任意一个即可。
 * */
public class SecurityIcon extends TagSupport {
	private String iconCode;//按钮编码 可以为多个编码
	private String mode;//匹配模式 大小写不敏感 默认为ALL
	private final int MODE_ANY=2;
	private final int MODE_ALL=1;
	private int flag;//mode的内部表现形式 目前 1=ALL 2=ANY 不识别的mode 均当做ALL处理
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		if("ALL".equalsIgnoreCase(mode)){
			this.flag = MODE_ALL;
			this.mode=mode;
		}
		else if("ANY".equalsIgnoreCase(mode)){
			this.flag=MODE_ANY;
			this.mode=mode;
		}
		else {
			this.flag=MODE_ALL;
			this.mode="ALL";
		}
		
	}

	private PageContext pc;
	public SecurityIcon() {
		this.flag=MODE_ALL;
	}
	public void setIconCode(String iconCode) {
		this.iconCode = iconCode;
	}
	@Override
	public void setPageContext(PageContext pageContext) {
		this.pc = pageContext;
	}
	
	@Override
	public int doStartTag() throws JspException {
		String[] codes=iconCode.split(",");
		int hit=0;
		for(int i=0;i<codes.length;i++){
			
			if("1".equals(pc.getRequest().getParameter(codes[i])))hit++;
		}
		if(flag==MODE_ALL){
			if(hit==codes.length)return EVAL_BODY_INCLUDE;
			else return SKIP_BODY;
		}
		else if(flag==MODE_ANY){
			if(hit>0)return EVAL_BODY_INCLUDE;
			else return SKIP_BODY;
		}
		else return SKIP_BODY;
		
	}
	
}
