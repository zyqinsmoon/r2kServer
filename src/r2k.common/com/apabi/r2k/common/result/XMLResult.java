package com.apabi.r2k.common.result;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.opensymphony.xwork2.ActionInvocation;

@SuppressWarnings("serial")
public class XMLResult extends StrutsResultSupport{
    private String contentType = "text/xml; charset=UTF-8";
    private String root = "result";
    public XMLResult() {
        super();
    }
    public XMLResult(String location) {
        super(location);
    }
    protected void doExecute(String finalLocation, ActionInvocation invocation)
            throws Exception {
        HttpServletResponse response = (HttpServletResponse) invocation
                .getInvocationContext().get(HTTP_RESPONSE);
        // String contentType = (String)
        // invocation.getStack().findValue(conditionalParse(contentType,
        // invocation));
        contentType = conditionalParse(contentType, invocation);
        response.setContentType(contentType);
        PrintWriter out = response.getWriter();
        // String result = conditionalParse(root, invocation);
        String result = (String) invocation.getStack().findValue(root);
        out.println(result);
        out.flush();
        out.close();
    }
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
    
}
