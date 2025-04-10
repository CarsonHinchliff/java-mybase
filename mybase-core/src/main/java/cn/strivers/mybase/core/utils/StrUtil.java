package cn.strivers.mybase.core.utils;

/**
 * StrUtil
 *
 * @author LT
 * @date 2019/1/4 15:31
 */
public class StrUtil extends cn.hutool.core.util.StrUtil {

    /**
     * 驼峰转下划线
     *
     * @param str
     * @return
     */
    public static String humpToUnderline(String str) {
        if (cn.hutool.core.util.StrUtil.isBlank(str)) {
            return "";
        }
        int len = str.length();
        StringBuffer sb = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i != 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String underlineToHump(String str) {
        if (cn.hutool.core.util.StrUtil.isBlank(str)) {
            return "";
        }
        int len = str.length();
        StringBuffer sb = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(str.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
