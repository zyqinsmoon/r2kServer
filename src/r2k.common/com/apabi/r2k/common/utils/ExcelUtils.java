package com.apabi.r2k.common.utils;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelUtils {
	/** 
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上 
     * @param title 表格标题名 
     * @param headers 表格属性列名数组 
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的 
     *            	javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据) 
     * @param out 与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中 
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyyy-MM-dd" 
     */  
    public void exportExcel(String title, String[] headers, String[][] dataset, OutputStream out) {  
        // 声明一个工作薄  
        HSSFWorkbook workbook = new HSSFWorkbook();  
        // 生成一个表格  
        HSSFSheet sheet = workbook.createSheet(title);  
        // 设置表格默认列宽度为15个字节  
        sheet.setDefaultColumnWidth(15);  
        // 生成一个样式  
//        HSSFCellStyle headStyle = setExcelHeadStyle(workbook);  
//        HSSFCellStyle bodyStyle = setExcelBodyStyle(workbook);  
        // 产生表格标题行  
        sheet =	createHeader(sheet, headers);  
        // 产生表格正文
        sheet = createExcelData(sheet, dataset);
        try {  
            workbook.write(out);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
    
  //产生表格正文
	public HSSFSheet createExcelData(HSSFSheet sheet, String[][] dataset) {
		HSSFRow rowCell = null;
		for(int row = 0, rlen = dataset.length; row < rlen; row++){
			rowCell = sheet.createRow(row+1);
			String[] datarow = dataset[row];
			int clen = dataset[row].length;
			for(int col = 0; col < clen; col++){
				HSSFCell cell = rowCell.createCell(col);  
//                cell.setCellStyle(bodyStyle);
                cell.setCellValue(datarow[col]);
			}
		}
		return sheet;
	}
    
    //辅助方法
    //创建Excel标题样式
    public HSSFCellStyle setExcelHeadStyle(HSSFWorkbook workbook){
    	// 生成一个样式  
        HSSFCellStyle style = workbook.createCellStyle();  
        // 设置这些样式  
//        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        // 生成一个字体  
        HSSFFont font = workbook.createFont();  
//        font.setColor(HSSFColor.VIOLET.index);  
        font.setFontHeightInPoints((short) 12);  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        // 把字体应用到当前的样式  
        style.setFont(font);  
    	return style;
    }
    //创建Excel内容样式
    public HSSFCellStyle setExcelBodyStyle(HSSFWorkbook workbook){
    	// 生成并设置另一个样式  
        HSSFCellStyle style2 = workbook.createCellStyle();  
//        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);  
//        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成另一个字体  
        HSSFFont font2 = workbook.createFont();  
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用到当前的样式  
        style2.setFont(font2);  
        return style2;
    }
    //创建Excel标题
    public HSSFSheet createHeader(HSSFSheet sheet, String[] headers){
    	HSSFRow row = sheet.createRow(0);  
        for (int i = 0; i < headers.length; i++) {  
            HSSFCell cell = row.createCell(i);  
//            cell.setCellStyle(headStyle);  
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);  
            cell.setCellValue(text);  
        }  
    	return sheet;
    }
}
