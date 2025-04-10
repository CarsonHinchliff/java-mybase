package cn.strivers.mybase.core.utils;

import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * EnumUtil
 *
 * @author LT
 * @date 2019/1/4 15:01
 */
public class EnumUtil extends cn.hutool.core.util.EnumUtil {
    /**
     * 枚举转Map
     *
     * @param enums
     * @return
     */
    public static Map<String, Object> enumToMap(Enum<?>[] enums) {
        Map<String, Object> map = new HashMap<>();
        try {
            Method kMethod = null, vMethod = null;
            for (Object o : enums) {
                if (kMethod == null) {
                    //获取枚举值的方法名
                    String kMethodName = null, vMethodName = null, methodName = null;
                    Field[] fields = o.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if (!field.getType().equals(Integer.class) && !field.getType().equals(Long.class) && !field.getType().equals(String.class)) {
                            continue;
                        }
                        methodName = field.getName();
                        methodName = "get" + StrUtil.upperFirst(methodName);
                        if (kMethodName == null) {
                            kMethodName = methodName;
                        } else if (vMethodName == null) {
                            vMethodName = methodName;
                        }
                    }
                    //根据方法名获取方法
                    kMethod = o.getClass().getMethod(kMethodName);
                    vMethod = o.getClass().getMethod(vMethodName);
                }
                map.put(String.valueOf(kMethod.invoke(o)), vMethod.invoke(o));
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException("枚举转失败");
        }
    }
}
