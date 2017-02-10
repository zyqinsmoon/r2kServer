package com.apabi.r2k.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipFile;

/**
 * ZIP压缩/解压工具类.
 * 
 * @author minwh
 *
 */
public abstract class ZipUtils {
	
	private ZipUtils() {
		
	}
	
	/** 
     * 将文件压缩为zip文件.
     */ 
    public static void zip(final String zipPath, final String dir) throws IOException { 
        FileOutputStream fos = null; 
        ZipOutputStream zipout = null; 
        try { 
            fos = new FileOutputStream(zipPath);
            zipout = new ZipOutputStream(fos);
            final File file = new File(dir); 
            zip(zipout, file, "", Filter.TRUE_FILTER);
        } finally {
        	IOUtils.closeQuietly(zipout);
        	IOUtils.closeQuietly(fos);
        } 
    }
    
    /**
     * 将文件压缩输出到out.
     */
    public static void zip(final OutputStream out, final String dir) throws IOException {
    	ZipOutputStream zipout = null;
    	try {
    		zipout = new ZipOutputStream(out);
    		zip(zipout, new File(dir), "", Filter.TRUE_FILTER);
    	} finally {
    		IOUtils.closeQuietly(zipout);
    	}
    }
    

    /**
     * 将文件夹压缩输出到out（不输出文件夹本身）.
     * 
     * @param out 输出流
     * @param dir 文件夹
     */
    public static void zipDir(final OutputStream out, final String dir) throws IOException {
    	zipDir(out, dir, Filter.TRUE_FILTER);
    }
    
    /**
     * 过滤器，只压缩符合条件的文件.
     * 
     * @author minwh
     */
    public static interface Filter {
    	boolean needs(File file);
    	Filter TRUE_FILTER = new TrueFilter();
    }
    
    /**
     * 总是返回true的filter.
     * 
     * @author minwh
     *
     */
    public static class TrueFilter implements Filter {
    	public boolean needs(final File file) {
			return true;
		}
    }
    
    
    public static void zipDir(final OutputStream out, final String dir, final Filter filter) throws IOException {
    	final File file = new File(dir);
    	if (!file.isDirectory()) {
    		throw new IllegalArgumentException("dir参数必须指定一个文件夹");
    	}
    	ZipOutputStream zipout = null;
    	try {
    		zipout = new ZipOutputStream(out);
        	final File[] fileArr = file.listFiles(); // 递归写入目录下的所有文件 
            for (final File f : fileArr) {
            	zip(zipout, f, "", filter);
            } 
    	} finally {
    		IOUtils.closeQuietly(zipout);
    	}
    }
    
    /** 
     * 将文件 file写入到 zip输出流中.
     */ 
    private static void zip(final ZipOutputStream out, final File file, 
    		final String base, final Filter filter) throws IOException {
    	String fullBase = base;
    	if (!filter.needs(file)) {
    		return;
    	}
        if (file.isDirectory()) {
        	//注掉下面三行：不带根目录生成
//            fullBase += file.getName() + "/";             
//            final ZipEntry entry = new ZipEntry(fullBase); // 创建一个目录条目 [以 / 结尾] 
//            out.putNextEntry(entry); // 向输出流中写入下一个目录条目 
            final File[] fileArr = file.listFiles(); // 递归写入目录下的所有文件 
            for (final File f : fileArr) {
            	zip(out, f, fullBase, filter); 
            } 
        } else if (file.isFile()) { 
            fullBase += file.getName(); 
            final ZipEntry entry = new ZipEntry(fullBase); // 创建一个文件条目 
            out.putNextEntry(entry); // 向输出流中写入下一个文件条目 
            final FileInputStream in = new FileInputStream(file); // 写入文件内容 
            int data = in.read(); // 为了防止出现乱码 
            while (data != -1) { // 此处按字节流读取和写入 
            	out.write(data); 
               	data = in.read(); 
            } 
            IOUtils.closeQuietly(in);
        } 
    }
    /**
     * 将文件夹压缩输出到out（不输出文件夹本身）.
     * 
     * @param out 输出流
     * @param dir 文件夹
     */
    public static void zipIncludeDir(final OutputStream out, final String dir) throws IOException {
    	zipIncludeDir(out, dir, Filter.TRUE_FILTER);
    }
    
    public static void zipIncludeDir(final OutputStream out, final String dir, final Filter filter) throws IOException {
    	final File file = new File(dir);
    	if (!file.isDirectory()) {
    		throw new IllegalArgumentException("dir参数必须指定一个文件夹");
    	}
    	ZipOutputStream zipout = null;
    	try {
    		zipout = new ZipOutputStream(out);
        	final File[] fileArr = file.listFiles(); // 递归写入目录下的所有文件 
            for (final File f : fileArr) {
            	zipIncludeDir(zipout, f, "", filter);
            } 
    	} finally {
    		IOUtils.closeQuietly(zipout);
    	}
    }
    
    /** 
     * 将文件 file写入到 zip输出流中(包含目录).
     */ 
    public static void zipIncludeDir(final ZipOutputStream out, final File file, 
    		final String base, final Filter filter) throws IOException {
    	String fullBase = base;
    	if (!filter.needs(file)) {
    		return;
    	}
    	if (file.isDirectory()) {
    		//注掉下面三行：不带根目录生成
            fullBase += file.getName() + "/";             
            final ZipEntry entry = new ZipEntry(fullBase); // 创建一个目录条目 [以 / 结尾] 
            out.putNextEntry(entry); // 向输出流中写入下一个目录条目 
    		final File[] fileArr = file.listFiles(); // 递归写入目录下的所有文件 
    		for (final File f : fileArr) {
    			zip(out, f, fullBase, filter); 
    		} 
    	} else if (file.isFile()) { 
    		fullBase += file.getName(); 
    		final ZipEntry entry = new ZipEntry(fullBase); // 创建一个文件条目 
    		out.putNextEntry(entry); // 向输出流中写入下一个文件条目 
    		final FileInputStream in = new FileInputStream(file); // 写入文件内容 
    		int data = in.read(); // 为了防止出现乱码 
    		while (data != -1) { // 此处按字节流读取和写入 
    			out.write(data); 
    			data = in.read(); 
    		} 
    		IOUtils.closeQuietly(in);
    	} 
    }
    
    /** 
     * 将文件 file写入到 zip输出流中.
     */ 
    private static void zipForR2kMenu(final ZipOutputStream out, final File file, 
    		final String base, final Filter filter,String deviceType,String suffix) throws IOException {
    	String fullBase = base;
    	if (!filter.needs(file)) {
    		return;
    	}
        if (file.isDirectory()) {
        	//注掉下面三行：不带根目录生成
//            fullBase += file.getName() + "/";             
//            final ZipEntry entry = new ZipEntry(fullBase); // 创建一个目录条目 [以 / 结尾] 
//            out.putNextEntry(entry); // 向输出流中写入下一个目录条目 
            final File[] fileArr = file.listFiles(); // 递归写入目录下的所有文件 
            for (final File f : fileArr) {
            	zip(out, f, fullBase, filter); 
            } 
        } else if (file.isFile()) {
        	String fileName = file.getName(); 
    		if(deviceType.startsWith("iP")){
    			fileName = FileUtils.getFileSuffix(fileName, suffix);
    		}
            fullBase += fileName; 
            final ZipEntry entry = new ZipEntry(fullBase); // 创建一个文件条目 
            out.putNextEntry(entry); // 向输出流中写入下一个文件条目 
            final FileInputStream in = new FileInputStream(file); // 写入文件内容 
            int data = in.read(); // 为了防止出现乱码 
            while (data != -1) { // 此处按字节流读取和写入 
            	out.write(data); 
               	data = in.read(); 
            } 
            IOUtils.closeQuietly(in);
        } 
    }
    public static void zipIncludeDirByR2k(final ZipOutputStream out, final File file, 
    		final String base, final Filter filter,final String deviceType,String suffix) throws IOException {
    	String fullBase = base;
    	if (!filter.needs(file)) {
    		return;
    	}
    	if (file.isDirectory()) {
    		//注掉下面三行：不带根目录生成
            fullBase += file.getName() + "/";             
            final ZipEntry entry = new ZipEntry(fullBase); // 创建一个目录条目 [以 / 结尾] 
            out.putNextEntry(entry); // 向输出流中写入下一个目录条目 
    		final File[] fileArr = file.listFiles(); // 递归写入目录下的所有文件 
    		for (final File f : fileArr) {
    			zipForR2kMenu(out, f, fullBase, filter,deviceType,suffix); 
    		} 
    	} else if (file.isFile()) { 
    		String fileName = file.getName(); 
    		if(deviceType.startsWith("iP")){
    			fileName = FileUtils.getFileSuffix(fileName, suffix);
    		}
    		fullBase += fileName; 
    		final ZipEntry entry = new ZipEntry(fullBase); // 创建一个文件条目 
    		out.putNextEntry(entry); // 向输出流中写入下一个文件条目 
    		final FileInputStream in = new FileInputStream(file); // 写入文件内容 
    		int data = in.read(); // 为了防止出现乱码 
    		while (data != -1) { // 此处按字节流读取和写入 
    			out.write(data); 
    			data = in.read(); 
    		} 
    		IOUtils.closeQuietly(in);
    	} 
    }
    
    public static void unzip(final File zipPath, final String outdir) throws IOException {
    	unzip(zipPath.getAbsolutePath(), outdir);
    }

    /** 
     * 解压 zip 文件.
     */ 
	public static void unzip(final String zipPath, final String outdir) throws IOException { 

    	InputStream zipin = null;
    	FileOutputStream fos = null;
    	ZipFile zipFile = null;
    	org.apache.tools.zip.ZipEntry entry = null; // 文件条目
    	new File(outdir).mkdirs();
        try { 
            zipFile = new ZipFile(zipPath);
    	    Enumeration entries = zipFile.getEntries();
            
            while (entries.hasMoreElements()) { 
            	entry = (org.apache.tools.zip.ZipEntry) entries.nextElement();
                if (entry.isDirectory()) { 
                    String name = entry.getName(); 
                    name = name.substring(0, name.length() - 1); 
                    new File(outdir + GlobalConstant.SLASH + name).mkdirs();
                } else { 
                    final String name = entry.getName(); 
                    String filepath = outdir + GlobalConstant.SLASH + name;
					final File file = new File(filepath);
                    FileUtils.createFile(filepath);
                    
                    zipin = zipFile.getInputStream(entry);
                    fos = new FileOutputStream(file); 
                    int data = zipin.read(); 
                    
                    while (data != -1) { 
                        fos.write(data); 
                        data = zipin.read(); 
                    }
                    fos.flush();
                    IOUtils.closeQuietly(fos);
                } 
            }
            
        } finally { 
            IOUtils.closeQuietly(zipin);
            zipFile.close();
            zipin = null;
        	fos = null;
        } 
    }
	
	
	public static void readZip(String localPath,HttpServletResponse response) throws Exception{
		RandomAccessFile fis = new RandomAccessFile(new File(localPath), "r"); // 要上传的文件
		response.setContentLength((int)fis.length());
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition","attachment; filename=\"menu.zip\""); 
		//conn.setChunkedStreamingMode(4096);//nginx默认不支持chunk
		OutputStream os = response.getOutputStream();
		int rn = 0;
		byte[] buf = new byte[4096];
		while ((rn = fis.read(buf, 0, 4096)) > 0) {
			os.write(buf, 0, rn);
		}
		os.close();
		fis.close();
	}
	

}
