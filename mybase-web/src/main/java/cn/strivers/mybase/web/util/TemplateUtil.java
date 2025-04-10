package cn.strivers.mybase.web.util;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.strivers.mybase.core.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板替换
 *
 * @author mozhu
 * @date 2023/5/4 18:09
 */
@Slf4j
public class TemplateUtil {

    /**
     * sql模板替换-带拼接字符串
     *
     * @param template 模板
     * @param vars     参数
     */
    public static String replaceStrSql(String template, Map<String, Object> vars) {
        if (StringUtils.isBlank(template)) {
            throw new ApplicationException("模板字符串替换失败,模板字符串不能为空");
        }
        if (MapUtil.isEmpty(vars)) {
            throw new ApplicationException("模板字符串替换失败,map参数不能为空");
        }
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : vars.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (ObjUtil.isEmpty(value)) {
                map.put(key, handleSplice(value));
                continue;
            }
            if (value instanceof List) {
                List<String> newStr = new ArrayList<>();
                for (Object o : (List) value) {
                    newStr.add(handleSplice(o));
                }
                map.put(key, StringUtils.join(newStr, ","));
            } else {
                map.put(key, handleSplice(value));
            }
        }

        StringSubstitutor stringSubstitutor = new StringSubstitutor(map);
        stringSubstitutor.setEnableSubstitutionInVariables(true);
        String replace = stringSubstitutor.replace(template);
        return StrUtil.replace(replace, "''", "null");
    }

    /**
     * 模板替换
     *
     * @param template 模板 如：我叫${name},今年${age}岁
     * @param valueMap 参数
     * @return
     */
    public static <V> String replace(String template, Map<String, V> valueMap) {
        if (StringUtils.isBlank(template)) {
            throw new ApplicationException("模板字符串替换失败,模板字符串不能为空");
        }
        if (MapUtil.isEmpty(valueMap)) {
            throw new ApplicationException("模板字符串替换失败,map参数不能为空");
        }
        StringSubstitutor stringSubstitutor = new StringSubstitutor(valueMap);
        stringSubstitutor.setEnableSubstitutionInVariables(true);
        return stringSubstitutor.replace(template);
    }


    /**
     * 模板替换
     *
     * @param template 模板  如：我叫{name},今年{age}岁
     * @param vars     参数
     * @param prefix   替换开始 如：{
     * @param suffix   替换结束 如：}
     * @return
     */
    public static String replace(String template, Map<String, Object> vars, String prefix, String suffix) {
        if (StringUtils.isBlank(template)) {
            throw new ApplicationException("模板字符串替换失败,模板字符串不能为空");
        }
        if (MapUtil.isEmpty(vars)) {
            throw new ApplicationException("模板字符串替换失败,map参数不能为空");
        }
        StringSubstitutor stringSubstitutor = new StringSubstitutor(vars, prefix, suffix);
        return stringSubstitutor.replace(template);
    }


    private static String handleSplice(Object str) {
        if (ObjUtil.isEmpty(str)) {
            return "''";
        }
        return "'" + str + "'";
    }


    public static void main(String[] args) {
        Map<String, Object> vals = new HashMap<>();
        vals.put("name", "xiaoxu");
        vals.put("age", 36);
        String temp = "我叫${name},今年${age}岁";
        System.out.println(replace(temp, vals));
    }
}
