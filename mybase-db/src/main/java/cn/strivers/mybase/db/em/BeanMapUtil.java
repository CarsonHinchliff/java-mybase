package cn.strivers.mybase.db.em;

import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author: xuwq
 * @date: 2020/10/20 18:05
 */
public class BeanMapUtil {
    /**
     * 将对象装换为map
     *
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean == null) {
            return map;
        }
        BeanMap beanMap = BeanMap.create(bean);
        for (Object key : beanMap.keySet()) {
            map.put(String.valueOf(key), beanMap.get(key));
        }
        return map;
    }

    /**
     * 将map装换为javabean对象
     *
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 将List<T>转换为List<Map<String, Object>>
     *
     * @param objList
     * @return
     */
    public static <T> List<Map<String, Object>> beansToMaps(List<T> objList) {
        if (objList.isEmpty()) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> list = new ArrayList<>(objList.size());
        for (T t : objList) {
            Map<String, Object> map = beanToMap(t);
            list.add(map);
        }
        return list;
    }

    /**
     * 将List<Map<String,Object>>转换为List<T>
     *
     * @param maps
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> mapsToBeans(List<Map<String, Object>> maps, Class<T> clazz) {
        if (maps.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>(maps.size());
        try {
            for (Map<String, Object> map : maps) {
                T bean = mapToBean(map, clazz.getDeclaredConstructor().newInstance());
                list.add(bean);
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException ex) {
            ex.printStackTrace();
        }
        return list;
    }

}
