package cn.strivers.mybase.core.utils;

import cn.hutool.core.util.ObjUtil;
import cn.strivers.mybase.core.exception.ApplicationException;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 接口签名
 *
 * @author mozhu
 * @date 2024/7/1 14:21
 */
@Slf4j
public class SignUtil {

    /**
     * 加盐
     */
    private static final String sign_salt = "dXLO0T3oiP&etwFKd3q(iD@HwDudqF+";

    /**
     * 签名
     *
     * @param data      参数数据
     * @param nonce     唯一id{可使用 SnowflakeUtils.nextStrId()}
     * @param timestamp 业务时间戳-毫秒{可使用System.currentTimeMillis()}
     * @return sign签名
     */
    public static String sha256Hex(Object data, String nonce, Long timestamp) {
        checkParam(data, nonce, timestamp);
        // 当前时间戳
        long currentTimestamp = System.currentTimeMillis();
        // 2小时内有效
        long timeDifference = 2 * 60 * 60 * 1000;
        if (Math.abs(timestamp - currentTimestamp) > timeDifference) {
            log.error("签名已过期：{},{}", timestamp, currentTimestamp);
            return null;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sign_salt", sign_salt);
        jsonObject.addProperty("nonce", nonce);
        jsonObject.addProperty("data", GsonUtil.toJsonString(data));
        return DigestUtils.sha256Hex(GsonUtil.toJsonString(jsonObject));
    }

    /**
     * 验签
     *
     * @param data      数据
     * @param nonce     唯一id
     * @param timestamp 业务时间戳(毫秒)
     * @param sign      被验证签名
     * @return
     */
    public static Boolean verifySign(Object data, String nonce, Long timestamp, String sign) {
        checkParam(data, nonce, timestamp);
        if (StringUtils.isBlank(sign)) {
            throw new ApplicationException("sign不能为空");
        }
        String expectedSignature = sha256Hex(data, nonce, timestamp);
        return StringUtils.equals(sign, expectedSignature);
    }

    private static void checkParam(Object data, String nonce, Long timestamp) {
        if (ObjUtil.isNull(data)) {
            throw new ApplicationException("参数不能为空");
        }
        if (StringUtils.isBlank(nonce)) {
            throw new ApplicationException("nonce不能为空");
        }
        if (ObjUtil.isNull(timestamp)) {
            throw new ApplicationException("timestamp不能为空");
        }
    }
}
