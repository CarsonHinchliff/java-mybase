package cn.strivers.mybase.core.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 实体转换工具类
 *
 * @author xuwenqian
 * @date 2023/2/8 1:36 下午
 */
@Slf4j
public class EntityUtil {

    /**
     * 实体类转化
     * 字段相同类型不同无法转换 如需要可使用,性能较差 @see {@link BeanUtil#copyProperties(Object, Object, boolean)} (Object, Object)}
     *
     * @param source 源
     * @param clazz  目标类
     * @return {@link T }
     * @author xuwq
     * @date 2023-02-08 13:36:45
     */
    public static <T> T copy(Object source, Class<T> clazz) {
        if (ObjUtil.isEmpty(source) || clazz == null) {
            return null;
        }
        try {
            T target = clazz.newInstance();
            // 把源对象属性赋值给目标对象
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (ReflectiveOperationException e) {
            log.error("实体类转换失败：{}", e.getMessage());
            return null;
        }
    }


    /**
     * 实体类集合转化
     *
     * @param sourceList 源
     * @param clazz      目标类
     * @return {@link List }<{@link T }>
     * @author xuwq
     * @date 2023-02-22 14:22:06
     */
    public static <S, T> List<T> copyList(List<S> sourceList, Class<T> clazz) {
        if (CollUtil.isEmpty(sourceList) || clazz == null) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>(sourceList.size());
        for (S source : sourceList) {
            T target = copy(source, clazz);
            if (target != null) {
                list.add(target);
            }
        }
        return list;
    }

    /**
     * map转换为bean
     *
     * @param map       源
     * @param beanClass 目标bean
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass) {
        return BeanUtil.toBeanIgnoreError(map, beanClass);
    }

    /**
     * bean转换为bean
     *
     * @param bean 源
     * @return
     */
    public static Map<String, Object> beanToMap(Object bean) {
        return BeanUtil.beanToMap(bean);
    }

}
