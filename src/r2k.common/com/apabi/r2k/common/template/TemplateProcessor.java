package com.apabi.r2k.common.template;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.apabi.r2k.common.utils.FileUtils;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * 基于freemarker的模板处理程序. 所有模板文件应该放在classpath:/tpl下.
 * 
 *
 *
 */
public abstract class TemplateProcessor {
	private TemplateProcessor() {
		
	}
	
	private static Logger log = LoggerFactory.getLogger(TemplateProcessor.class);
	
	private static final String TPL_DIRECTORY = "/tpl";
	
	private static final Configuration CFG = new Configuration() { {
		setClassForTemplateLoading(TemplateProcessor.class, TPL_DIRECTORY);
//		try {
//			setDirectoryForTemplateLoading(new File(getClass().getResource(
//				TPL_DIRECTORY).getFile()));
//
//		} catch (final IOException e) {
//			
//			final String msg = "加载模板路径失败";
//			log.error(msg, e);
//		}
		setDefaultEncoding("UTF-8");
		setObjectWrapper(new DefaultObjectWrapper());
	} };
	
	/**
	 * 处理模板，返回结果.
	 * 
	 * @param tpl 相对于classpath:/tpl的模板文件路径
	 * @param model 模板数据
	 * @return 合成结果
	 * @throws TemplateException
	 */
	public static String process(final String tpl, final Map model) throws TemplateException {
		final Writer out = new StringWriter();
		process(tpl, model, out);
		return out.toString();
	}
	
	/**
	 * 处理模板，输出结果到文件. 如果文件不存在，将创建文件及路径上的所有目录.
	 * 
	 * @param tpl 相对于classpath:/tpl的模板文件路径
	 * @param model 模板数据
	 * @param file 目标文件，不存在是将新建
	 * @param overwrite 目标文件存在时是否覆盖
	 * @throws TemplateException
	 */
	public static void process(final String tpl, final Map model, 
			final File file, final boolean overwrite) throws TemplateException {
		if (file.exists()) {
			if (overwrite) {
				FileUtils.deleteQuietly(file);
			} else {
				log.warn("目标文件已存在");
				return;
			}
		}
		OutputStream out;
		try {
			out = FileUtils.openOutputStream(file);
		} catch (final IOException e) {
			final String msg = "处理模板失败-打开目标文件失败";
			throw new TemplateException(msg, e);
		}
		final Writer writer = new OutputStreamWriter(new BufferedOutputStream(out, 8192));
		process(tpl, model, writer);
	}
	
	/**
	 * 处理模板，输出结果到writer参数.
	 * 
	 * @param tpl  相对于classpath:/tpl的模板文件路径
	 * @param model  模板数据
	 * @param writer 输出目标
	 * @throws TemplateException
	 */
	public static void process(final String tpl, final Map model, final Writer writer) throws TemplateException {
		try {
			final Template template = CFG.getTemplate(tpl);
			template.setEncoding("UTF-8");
			template.process(model, writer);
		} catch (final Exception e) {
			final String msg = "处理模板失败";
			throw new TemplateException(msg, e);
		}
	}
	
	
	public static void generateResponse(String tpl, Map<String ,Object> model, HttpServletResponse response)  {
		 outPutXmlSetResponse(response);
		PrintWriter writer;
		try {
			writer = response.getWriter();
			TemplateProcessor.process(tpl, model, writer);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 重载生成xml方法
	public static String generateResponse(String tpl, Map<String ,Object> model){
		Writer writer;
		try {
			writer = new StringWriter(1024);
			TemplateProcessor.process(tpl, model, writer);
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//返回xml格式http头写入公用参数
	public static HttpServletResponse  outPutXmlSetResponse(HttpServletResponse response){
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache,no-store");
		return response;
	}
	
	public static void main(String[] args) throws Exception {
//		List<Menu> menus = new ArrayList<Menu>();
//		for (int i = 0; i <4; i++) {
//			Menu menu = new Menu();
//			menu.setIconUrl(i+".jpg");
//			menu.setLink("http://r2k/menu/"+i);
//			menu.setLinkType("link");
//			menu.setTitle("测试"+i);
//			menus.add(menu);
//		}
//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("menus", menus);
//		model.put("count", "4");
//        process("com.apabi.r2k.api.menu.xml", model, new File("e://1.xml"), true);
	}
}
