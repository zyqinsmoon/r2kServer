package com.apabi.r2k.admin.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.apabi.r2k.admin.model.Column;
import com.apabi.r2k.common.utils.PropertiesUtil;

public class ColumnUtil {

	/**
	 * 从全部column列表中获取指定id的column及其所有的后代column
	 * @param columns
	 * @param id
	 * @return
	 */
	public static Column getColumnAndChilds(List<Column> columns, int id){
		Column column = null;
		for(Column col : columns){
			if(col.getId() == id){
				column = col;
				List<Column> childs = getChilds(columns, column.getId());
				column.setColumns(childs);
				break;
			}
		}
		return column;
	}
	
	/**
	 * 获取父id为parentId的column
	 * @param columns
	 * @param parentId
	 * @return
	 */
	public static List<Column> getChilds(List<Column> columns, int parentId){
		List<Column> childs = new ArrayList<Column>();
		for(Column col : columns){
			if(col.getParentId() == parentId){
				List<Column> newChilds = getChilds(columns, col.getId());
				col.setColumns(newChilds);
				childs.add(col);
			}
		}
		return childs;
	}
	
	/**
	 * 将column树转成普通list
	 * @return
	 */
	public static List<Column> normalizeColumn(Column column){
		List<Column> columns = new ArrayList<Column>();
		columns.add(column);
		List<Column> childs = column.getColumns();
		if(CollectionUtils.isNotEmpty(childs)){
			for(Column col :childs){
				List<Column> newChilds = normalizeColumn(col);
				columns.addAll(newChilds);
			}
		}
		return columns;
	}
	
	/**
	 * 将所有的资讯内容转成树
	 * @param columns
	 * @return
	 */
	public static List<Column> columnsToTree(List<Column> columns, int parentId){
		List<Column> tree = new ArrayList<Column>();
		for(Column col : columns){
			if(col.getParentId() == parentId){
				List<Column> childTree = columnsToTree(columns, col.getId());
				col.setColumns(childTree);
				tree.add(col);
			}
		}
		return tree;
	}
	
	/**
	 * 将column树转成map，key为顶级column id，value为column树
	 * @param columns
	 * @return
	 */
	public static Map columnTreeToMap(List<Column> columns){
		Map columnMap = new HashMap();
		for(Column col : columns){
			columnMap.put(col.getId(), col);
		}
		return columnMap;
	}
	
	public static Map allColumnToMap(List<Column> columns){
		List<Column> tree = columnsToTree(columns, 0);
		return columnTreeToMap(tree);
	}
	
	/**
	 * 将column列表中的父子合并,列表中只存在一个父节点
	 * @param columns
	 * @return
	 */
	public static Column mergeColumns(List<Column> columns, int parentId){
		Column parentCol = null;
		List<Column> childs = new ArrayList<Column>();
		for(Column col : columns){
			if(col.getId() == parentId){
				parentCol = col;
			}else{
				childs.add(col);
			}
		}
		if(parentCol != null){
			parentCol.setColumns(childs);
		}
		return parentCol;
	}
	
	//获取输出路径
	public static String getOutputPath(String orgid, String devDir, String tempType, Column column){
		String pubDir = "/" + PropertiesUtil.get("base.r2kfile") + "/" + PropertiesUtil.get("path.info.pub");
		String htmlName = tempType + ".html";
		StringBuilder outFilePath = new StringBuilder(pubDir + "/" + orgid + "/" + devDir + "/");
		if(column != null){
			int parentid = column.getParentId();
			int selfid = column.getId();
			if(parentid != 0){
				outFilePath.append(parentid+"/");
			}
			outFilePath.append(selfid+"/");
		}
		outFilePath.append(htmlName);
		return outFilePath.toString();
	}
}
