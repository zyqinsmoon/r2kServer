package com.apabi.r2k.admin.utils;

import java.util.Comparator;

import com.apabi.r2k.admin.model.Column;

public class ColumnComparator implements Comparator<Column> {

	@Override
	public int compare(Column o1, Column o2) {
		return o1.getSort() - o2.getSort();
	}

}
