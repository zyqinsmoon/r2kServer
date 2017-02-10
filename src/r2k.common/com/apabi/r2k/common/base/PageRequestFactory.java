package com.apabi.r2k.common.base;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.util.WebUtils;

import cn.org.rapid_framework.page.PageRequest;

/**
 * 用于分页组件覆盖的类,新的分页组件覆盖此类的bindPageRequest()方法以适合不同的分页创建
 * 
 * @author badqiu
 */
public class PageRequestFactory {
	public static final int MAX_PAGE_SIZE = 1000;

	static BeanUtilsBean beanUtils = new BeanUtilsBean();
	static {
		// 用于注册日期类型的转换
		String[] datePatterns = new String[] { "yyyy-MM-dd",
				"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS", "HH:mm:ss" };
		ConvertRegisterHelper.registerConverters(beanUtils.getConvertUtils(),
				datePatterns);

		System.out.println("PageRequestFactory.MAX_PAGE_SIZE=" + MAX_PAGE_SIZE);
	}

	public static PageRequest bindPageRequest(PageRequest pageRequest,
			HttpServletRequest request) {
		return bindPageRequest(pageRequest, request, null);
	}

	public static PageRequest bindPageRequest(PageRequest pageRequest,
			HttpServletRequest request, String defaultSortColumns) {
		return bindPageRequest(pageRequest, request, defaultSortColumns,
				BaseQuery.DEFAULT_PAGE_SIZE);
	}

	/**
	 * 绑定PageRequest的属性值
	 */
	public static PageRequest bindPageRequest(PageRequest pageRequest,
			HttpServletRequest request, String defaultSortColumns,
			int defaultPageSize) {

		Map params = WebUtils.getParametersStartingWith(request, "s_");
		Iterator iterator = params.keySet().iterator();
		while(iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = (String) params.get(key);
			if(value == null || value.trim().isEmpty()){
				iterator.remove();
			}
		}
		pageRequest.setFilters(params);

		pageRequest.setPageNumber(ServletRequestUtils.getIntParameter(request,
				"pageNumber", 1));
		pageRequest.setPageSize(ServletRequestUtils.getIntParameter(request,
				"pageSize", defaultPageSize));
		pageRequest.setSortColumns(ServletRequestUtils.getStringParameter(
				request, "sortColumns", defaultSortColumns));

		if (pageRequest.getPageSize() > MAX_PAGE_SIZE) {
			pageRequest.setPageSize(MAX_PAGE_SIZE);
		}
		return pageRequest;
	}

}
