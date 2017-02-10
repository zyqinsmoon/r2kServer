package com.apabi.r2k.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 文件工具类.
 * 
 * 
 *
 */
public abstract class FileUtils extends org.apache.commons.io.FileUtils {

	public final static String EXT_PNG = ".png";		//png文件扩展名
	public final static String EXT_JPG = ".jpg";		//jpg文件扩展名
	
	private FileUtils() {
		
	}
	
	/**
	 * 创建文件，如果文件路径上的目录不存在，创建相应目录.
	 */
	public static void createFile(final String filename) throws IOException {
		final File file = new File(filename);
		if (file.getParentFile() != null && !file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
            	throw new IOException("创建目录失败：" + file.getParentFile().getAbsolutePath());
            }
        }
		file.createNewFile();
	}
	

    private static ClassLoader getClassLoader() {
	return Thread.currentThread().getContextClassLoader();

    }

    /**
     * 将资源文件加载到输入流中
     * 
     * @param resource
     *                资源文件
     * @return
     */
    public static InputStream getStream(String resource) {
	return getClassLoader().getResourceAsStream(resource);
    }
    
    private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',    
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	public static String getMd5(File file) throws Exception {
		return getHash(file, "MD5");
	}

	/**
	 * 获取文件散列值.
	 * 
	 * @param fileName 文件名
	 * @param hashType 散列类型（MD5、SHA1等）
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getHash(File file, String hashType) throws Exception {
		InputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		MessageDigest digest = MessageDigest.getInstance(hashType);
		int numRead = 0;
		while ((numRead = fis.read(buffer)) > 0) {
			digest.update(buffer, 0, numRead);
		}
		fis.close();
		return toHexString(digest.digest());
	}

	private static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}
	
	public static boolean checkFileSize(File file,long size) throws Exception{
		long fileSize = file.length();
		if(fileSize > size)
		   return false;
		else 
		   return true;
	}
	
	public static boolean isExists(String path){
		boolean exists = false;
		File indexFile = new File(path);
		if(indexFile.exists() && indexFile.isFile()){
			exists = true;
		}
		return exists;
	}
	
	//创建目录
	public static void createDirs(String filename){
		File file = new File(filename);
		if(file.isFile()){
			file = file.getParentFile();
		}
		if(!file.exists()){
			file.mkdirs();
		}
	}
	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    
    /**
     * 文件备份	
     * @param file
     */
    public static File fileBak(File oldfile, String bakSurfix) throws Exception{
    	File bakfile = null;
    	String filename = oldfile.getCanonicalPath();
    	if(oldfile.isFile()){
    		String surfix = filename.substring(filename.lastIndexOf("."));
    		String bakname = filename.replace(surfix, bakSurfix + surfix);
    		bakfile = new File(bakname);
    		FileUtils.copyFile(oldfile, bakfile);
    	} else if(oldfile.isDirectory()){
    		bakfile = new File(filename + bakSurfix);
    		oldfile.renameTo(bakfile);
    	}
    	return bakfile;
    }
    
    // 复制文件夹
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }
    
    //获取文件扩展名,如：a.txt，结果：txt
    public static String getFileExtendsion(String fileName){
    	return fileName.substring(fileName.lastIndexOf(".")+1);
    }
    
    /**
     * 如果原文件存在则先删除再复制，否则直接复制
     */
    public static void deleteAndCopyFile(String srcPath, String destPath) throws Exception{
    	File srcFile = new File(srcPath);
    	File destFile = new File(destPath);
    	deleteAndCopyFile(srcFile, destFile);
    }
    
    /**
     * 如果原文件存在则先删除再复制，否则直接复制
     */
    public static void deleteAndCopyFile(File srcFile, File destFile) throws Exception{
    	if(destFile.exists()){
    		forceDelete(destFile);
    	}
    	copyFile(srcFile, destFile);
    }
    
    
    public static String getFileSuffix(String fileName,String suffix){
    		int last =  fileName.lastIndexOf(".");
    		 String suffixName = fileName.substring(last, fileName.length());
    		 String oldFileName = fileName.substring(0, last);
    		String newFileName = oldFileName + suffix+suffixName;
    return newFileName;
		
    }
    
    //删除文件并重新创建文件
   public static File delAndCrtFile(String filepath) {
	   File file = new File(filepath);
		FileUtils.deleteQuietly(file);
		if(!file.getParentFile().exists()){
		   file.getParentFile().mkdirs();
		   }
		   if(!file.exists()){
		      try {
				file.createNewFile();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    }
		   return file;
   }
}
