package cn.strivers.mybase.core.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * gson工具类
 *
 * @author mozhu
 * @date 2024/9/24 10:09
 */
public class GsonUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private GsonUtil() {
    }

    /**
     * 对象转成jsonString
     *
     * @param object
     * @return json
     */
    public static String toJsonString(Object object) {
        return gson.toJson(object);
    }

    /**
     * 转JsonObject
     *
     * @param json
     * @return JsonObject
     */
    public static JsonObject toJsonObject(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }

    /**
     * Json转成对象
     *
     * @param json
     * @param clazz
     * @return 对象
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }


    /**
     * 转jsonArray
     *
     * @param json
     * @return JsonArray
     */
    public static JsonArray toJsonArray(String json) {
        return JsonParser.parseString(json).getAsJsonArray();
    }

    /**
     * json转成list<T>
     *
     * @param json
     * @param clazz
     * @return list<T>
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        return gson.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * json转成list中有map的
     *
     * @param json
     * @return List<Map < String, T>>
     */
    public static <T> List<Map<String, T>> toListMaps(String json) {
        return gson.fromJson(json, new TypeToken<List<Map<String, T>>>() {
        }.getType());
    }

    /**
     * json转成map的
     *
     * @param json
     * @return Map<String, T>
     */
    public static <T> Map<String, T> toMaps(String json) {
        return gson.fromJson(json, new TypeToken<Map<String, T>>() {
        }.getType());
    }
}
