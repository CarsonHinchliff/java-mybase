package cn.strivers.mybase.core.utils;


import cn.hutool.core.util.StrUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * Aes加密解密工具类
 *
 * @author xuwq
 * @date 2023-02-03 17:00:04
 */
public class AesUtil {
    private static final Logger log = LogManager.getLogger(AesUtil.class);

    /**
     * 加密解密key
     */
    public static final String KEY = "1682e97c-674f-42";
    /**
     * 算法
     */
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     * aes解密
     *
     * @param encrypt 内容
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String encrypt) {
        try {
            return aesDecrypt(encrypt, KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * aes加密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public static String aesEncrypt(String content) {
        try {
            return aesEncrypt(content, KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes byte[]
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        // 这里的1代表正数
        return new BigInteger(1, bytes).toString(radix);
    }

    /**
     * base 64 encode
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    /**
     * base 64 decode
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) {
        return StrUtil.isEmpty(base64Code) ? null : Base64.decodeBase64(base64Code);
    }


    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 将byte[]转为各种进制的字符串
     * @param salt String
     * @param dataMap 对map 数据进行 加密
     * @return 转换后的字符串
     */

    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * AES参数加密
     * 1.对入参进行加密
     * 2.取第一次加密结果，第二次加密第一次的加密结果
     *
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String paramAesEncrypt(String params) {
        String result = null;
        try {
            result = AesUtil.aesEncrypt(aesEncrypt(params, KEY), KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * AES参数解密
     * 1.对入参进行解密
     * 2.二次解密
     *
     * @param params 参数
     * @return
     * @throws Exception
     */
    public static String paramAesDecrypt(String params) {
        return aesDecrypt(aesDecrypt(params));
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }


    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return StrUtil.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

}