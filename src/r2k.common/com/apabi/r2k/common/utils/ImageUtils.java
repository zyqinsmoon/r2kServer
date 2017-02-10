package com.apabi.r2k.common.utils;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * 图片处理工具类：<br>
 * 功能：缩放图像、切割图像、图像类型转换、彩色转黑白、文字水印、图片水印等
 * @author Administrator
 */
public class ImageUtils {
    /**
     * 几种常见的图片后缀
     */
    public static String IMAGE_GIF = "gif";// 图形交换格式
    public static String IMAGE_JPG = "jpg";// 联合照片专家组
    public static String IMAGE_JPEG = "jpeg";// 联合照片专家组
    public static String IMAGE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
    public static String IMAGE_PNG = "png";// 可移植网络图形
    public static String IMAGE_PSD = "psd";// Photoshop的专用格式Photoshop
    
    /**
     * 常见图片文件格式
     */
    public static String IMAGE_TYPE_JPEG = "JPEG";
    public static String IMAGE_TYPE_PNG = "PNG";
    
    //图片扩展名对应的图片类型
    public final static Map<String, String> IMAGE_TYPE_SUFFIX_MAP = CollectionUtils.buildMap(
    		IMAGE_JPG,IMAGE_TYPE_JPEG,
    		IMAGE_JPEG,IMAGE_TYPE_JPEG,
    		IMAGE_PNG, IMAGE_TYPE_PNG
    );
    /**
     * 程序入口：用于测试
     * @param args
     */
    public static void main(String[] args) {
//        // 1-缩放图像：
//        // 方法一：按比例缩放
//        ImageUtils.scale("e:/abc.jpg", "e:/abc_scale.jpg", 2, true);//测试OK
//        // 方法二：按高度和宽度缩放
//        ImageUtils.scale2("e:/abc.jpg", "e:/abc_scale2.jpg", 500, 300, true);//测试OK
//        // 3-图像类型转换：
//        ImageUtils.convert("e:/abc.jpg", "GIF", "e:/abc_convert.gif");//测试OK
//    	scale(new File("D:/Test/img/t.jpg"), new File("D:/Test/img/1.jpg"), 1/3, true);
    }
    /**
     * 缩放图像（按比例缩放）
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     * @param scale 缩放比例
     * @param flag 缩放选择:true 放大; false 缩小;
     */
    public final static void scale(String srcImageFile, String result, int scale) {
    	File srcImg = new File(srcImageFile);
    	File destImg = new File(result);
    	scale(srcImg, destImg, scale);
    }
    
    public final static void scale(File srcImg, File destImg, int scale){
    	try {
//    		BufferedImage src = ImageIO.read(srcImg); // 读入文件
//    		int width = src.getWidth(); // 得到源图宽
//    		int height = src.getHeight(); // 得到源图长
//    		if (flag) {// 放大
//    			width = (int) (width * scale);
//    			height = (int) (height * scale);
//    		} else {// 缩小
//    			width = (int) (width / scale);
//    			height = (int) (height / scale);
//    		}
//    		System.out.println("-----------"+(float)scale);
//    		OutputStream outImg = new FileOutputStream(destImg);
//    		Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName(getImageSuffix(destImg.getName()));
//    		ImageWriter imageWriter = iterator.next();
//    		ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
//    		imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//    		imageWriteParam.setCompressionQuality((float)scale);
//    		ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(outImg);
//    		imageWriter.setOutput(imageOutputStream);
//    		IIOImage iioImage = new IIOImage(src, null, null);
//    		imageWriter.write(null, iioImage, imageWriteParam);
//    		imageOutputStream.flush();
//    		imageOutputStream.close();
//    		outImg.close();
//    		Image image = src.getScaledInstance(width, height,
//    				Image.SCALE_DEFAULT);
//    		BufferedImage tag = new BufferedImage(width, height,
//    				BufferedImage.TYPE_INT_RGB);
//    		Graphics g = tag.getGraphics();
//    		g.drawImage(image, 0, 0, null); // 绘制缩小后的图
//    		g.dispose();
//    		ImageIO.write(tag, IMAGE_TYPE_SUFFIX_MAP.get(getImageSuffix(destImg.getName())), destImg);// 输出到文件流
    		FileInputStream in = new FileInputStream(srcImg);
    		ImageInputStream iin = ImageIO.createImageInputStream(in);
    		Iterator<ImageReader> iter = ImageIO.getImageReaders(iin);
    		if(!iter.hasNext()){
    			return;
    		}
    		ImageReader reader = iter.next();
    		ImageReadParam params = reader.getDefaultReadParam();
    		reader.setInput(iin, true, true);
    		params.setSourceSubsampling(scale, scale, 0, 0);
    		BufferedImage img = reader.read(0, params);
    		ImageIO.write(img, IMAGE_TYPE_SUFFIX_MAP.get(getImageSuffix(destImg.getName())), destImg);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    //获取文件扩展名
    public final static String getImageSuffix(String fileName){
    	String suffix = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
    	return suffix;
    }
    
    /**
     * 缩放图像（按高度和宽度缩放）
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
     */
    public final static void scale2(String srcImageFile, String result, int height, int width, boolean bb) {
        try {
            double ratio = 0.0; // 缩放比例
            File f = new File(srcImageFile);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue()
                            / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform
                        .getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {//补白
                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                g.dispose();
                itemp = image;
            }
            ImageIO.write((BufferedImage) itemp, "JPEG", new File(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
  
    /**
     * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
     * @param srcImageFile 源图像地址
     * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
     * @param destImageFile 目标图像地址
     */
    public final static void convert(String srcImageFile, String formatName, String destImageFile) {
        try {
            File f = new File(srcImageFile);
            f.canRead();
            f.canWrite();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
