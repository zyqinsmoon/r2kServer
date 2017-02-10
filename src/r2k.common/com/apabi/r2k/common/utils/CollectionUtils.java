package com.apabi.r2k.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * 集合操作的Utils函数集合.
 *
 * 主要针对Web应用与Hibernate的特征而开发.
 *
 * @author calvin
 */
/**
 * 
 */
public abstract class CollectionUtils extends org.apache.commons.collections.CollectionUtils {

	private CollectionUtils() {
	}

	/**
	 * 提取集合中的对象的属性,组合成列表.
	 *
	 * @param collection 来源集合.
	 * @param propertityName 要提取的属性名.
	 */
	@SuppressWarnings("unchecked")
	public static List fetchPropertyToList(final Collection collection, final String propertyName) throws Exception {

		final List list = new ArrayList();

		for (final Object obj : collection) {
			list.add(PropertyUtils.getProperty(obj, propertyName));
		}

		return list;
	}

	/**
	 * 提取集合中的对象的属性,组合成由分割符分隔的字符串.
	 *
	 * @param collection 来源集合.
	 * @param propertityName 要提取的属性名.
	 * @param separator 分隔符.
	 */
	@SuppressWarnings("unchecked")
	public static String fetchPropertyToString(
			final Collection collection, 
			final String propertyName, 
			final String separator)
			throws Exception {
		final List list = fetchPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	/**
	 * 根据对象ID集合,整理合并集合.
	 *
	 * 整理算法为：在源集合中删除不在ID集合中的元素,创建在ID集合中的元素并对其ID属性赋值并添加到源集合中.
	 * 多用于根据http请求中的id列表，修改对象所拥有的子对象集合.
	 *
	 * @param collection 源对象集合
	 * @param retainIds  目标集合
	 * @param clazz  集合中对象的类型
	 *
	 * @see #retainById(Collection, Collection, String, Class)
	 */
	public static <T, ID> void mergeByCheckedIds(
			final Collection<T> collection, 
			final Collection<ID> checkedIds, 
			final Class<T> clazz)
			throws Exception {
		mergeByCheckedIds(collection, checkedIds, "id", clazz);
	}

	/**
	 * 根据对象ID集合,整理合并集合.
	 *
	 * http请求发送变更后的子对象id列表时，hibernate不适合删除原来子对象集合再创建一个全新的集合
	 * 需采用以下整合的算法：
	 * 在源集合中删除不在ID集合中的元素,创建在ID集合中的元素并对其ID属性赋值并添加到源集合中.
	 *
	 * @param collection 源对象集合
	 * @param retainIds  目标集合
	 * @param idName 对象中ID的属性名
	 * @param clazz  集合中对象的类型
	 */
	public static <T, ID> void mergeByCheckedIds(
			final Collection<T> collection, 
			final Collection<ID> checkedIds, 
			final String idName,
			final Class<T> clazz) throws Exception {

		if (checkedIds == null) {
			collection.clear();
			return;
		}

		final Iterator<T> it = collection.iterator();

		while (it.hasNext()) {
			final T obj = it.next();
			if (checkedIds.contains(PropertyUtils.getProperty(obj, idName))) {
				checkedIds.remove(PropertyUtils.getProperty(obj, idName));
			} else {
				it.remove();
			}
		}

		for (final ID id : checkedIds) {
			final T obj = clazz.newInstance();
			PropertyUtils.setProperty(obj, idName, id);
			collection.add(obj);
		}
	}

	/**
	 * 从集合中删除包含在remove中的所有对象.
	 *
	 * @param collection 集合
	 * @param remove 要移除的对象
	 * @return 移除的对象数量
	 */
	public static int removeAll(final Collection collection, final Object[] remove) {
		if (remove == null) {
			return 0;
		}
		int removeCount = 0;
		for (final Object obj : remove) {
			if (collection.remove(obj)) {
				removeCount++;
			}
		}
		return removeCount;
	}

    public static Collection removeAll(final Collection collection, final Collection remove) {
    	// 修正commons-collections-3.2.1的bug
        return ListUtils.removeAll(collection, remove);
    }

    /**
     * 从参数列表构建map对象.
     *
     * @param objs 参数列表, 必须为偶数个
     * @return
     */
    public static Map buildMap(final Object... objs) {
        if (objs.length == 0) {
            return new HashMap();
        }
        Assert.isTrue(objs.length % 2 == 0);
        final Map map = new HashMap();
        for (int i = 0; i < objs.length; i += 2) {
            map.put(objs[i], objs[i + 1]);
        }

        return map;
    }
}
