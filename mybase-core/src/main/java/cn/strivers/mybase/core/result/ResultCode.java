package cn.strivers.mybase.core.result;

import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author xuwenqian
 */
@Getter
public enum ResultCode {
    /**
     * 响应码枚举
     */
    SUCCESS("00000", "操作成功"),
    FAIL("11111", "操作失败"),
    WARN("11112", "操作警告"),
    /**
     * A开头 用户端问题
     */
    ERROR_A1001("A1001", "用户端错误"),
    ERROR_A1002("A1002", "用户签名异常，请重新登录"),
    ERROR_A1003("A1003", "用户操作异常"),
    ERROR_A1004("A1004", "请求 JSON 解析失败"),
    ERROR_A1005("A1005", "请求参数格式错误"),
    ERROR_A1007("A1007", "访问权限异常,用户无权限"),
    /**
     * 登录过期
     */
    ERROR_A1008("A1008", "用户登录已过期，请重新登录"),
    ERROR_A1009("A1009", "用户验证码错误"),
    ERROR_A1010("A1010", "用户不存在，请重新登录"),
    ERROR_A1011("A1011", "租户不存在，请联系管理员"),
    ERROR_A1012("A1012", "请勿过于频繁点击"),
    ERROR_A1013("A1013", "您的IP不在允许的IP范围内"),
    ERROR_A1014("A1014", "请求参数验证不通过"),
    /**
     * B开头 系统异常
     */
    ERROR_B0001("B0001", "系统执行出错，请稍候再试"),
    ERROR_B0002("B0002", "系统执行超时，请稍候再试"),
    ERROR_B0003("B0003", "系统限流，请求超过最大数，请稍候再试"),
    ERROR_B0004("B0004", "sql报错，请检查sql"),
    /**
     * C开头 第三方或者中间件异常
     */
    ERROR_C0001("C0001", "中间件服务出错，请稍候再试"),
    ERROR_C0002("C0002", "调用第三方服务出错，请稍候再试"),
    ERROR_C0003("C0003", "调用内部服务出错，请稍候再试"),
    ERROR_C0004("C0004", "接口不存在"),
    ERROR_C0005("C0005", "消息服务出错，请稍候再试"),
    ERROR_C0006("C0006", "缓存服务出错，请稍候再试"),
    ERROR_C0007("C0007", "数据库服务出错，请稍候再试"),
    ERROR_C0008("C0008", "服务未找到，请稍候再试"),

    ;

    public final String code;
    public final String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据状态码获取状态码描述
     *
     * @param code 状态码
     * @return String 状态码描述
     */
    public static String getMessage(String code) {
        String result = null;
        for (ResultCode r : ResultCode.values()) {
            if (r.code.equals(code)) {
                result = r.message;
            }
        }
        return result;
    }
}
