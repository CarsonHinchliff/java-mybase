package cn.strivers.gateway.utils;

import cn.strivers.mybase.core.exception.ApplicationException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.extern.slf4j.Slf4j;

/**
 * jwt 管理
 *
 * @author xuwenqian
 * @date 2024-09-24 14:22:38
 */
@Slf4j
public class JWTUtils {
    /**
     * 密钥
     */
    private static final String SECRET = "my@2024Pkz+iUX5~KRCuA3@~DzSWUB#k8+yF1pl";

    /**
     * 由hmac生成令牌token
     * 可自行改造
     *
     * @param payloadStr 加密参数
     * @return {@link String }
     * @author xuwq
     * @date 2022-12-22 08:55:10
     */
    public static String generateToken(String payloadStr) {
        try {
            //建立JWS头，设置签名算法和类型
            JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
            //将负载信息封装到Payload中
            Payload payload = new Payload(payloadStr);
            //建立JWS对象
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            //建立HMAC签名器
            JWSSigner jwsSigner = new MACSigner(SECRET);
            //签名
            jwsObject.sign(jwsSigner);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("加密失败：{}", e.getMessage(), e);
            return "";
        }
    }


    /**
     * 验证令牌由hmac
     *
     * @param token 令牌
     * @author xuwq
     * @date 2022-12-22 08:55:05
     */
    public static String verifyToken(String token) {
        try {
            //从token中解析JWS对象
            JWSObject jwsObject = JWSObject.parse(token);
            //建立HMAC验证器
            JWSVerifier jwsVerifier = new MACVerifier(SECRET);
            if (!jwsObject.verify(jwsVerifier)) {
                throw new ApplicationException("token签名不合法！");
            }
            return jwsObject.getPayload().toString();
        } catch (Exception e) {
            log.error("解密失败：{}", e.getMessage());
            return null;
        }
    }
}
