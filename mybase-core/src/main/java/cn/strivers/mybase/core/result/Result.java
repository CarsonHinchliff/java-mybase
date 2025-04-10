package cn.strivers.mybase.core.result;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一API响应结果封装
 *
 * @author xuwenqian
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 8393995553581363877L;
    /**
     * 返回code："00000"操作成功
     */
    private String code;
    /**
     * 是否请求成功： true请求成功
     */
    private Boolean success;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;


    private Result() {
    }

    private Result(String code, Boolean success, String message) {
        this.code = code;
        this.success = success;
        this.message = message;
    }

    private Result(String code, Boolean success, String message, T data) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public Boolean isSuccess() {
        return ObjectUtil.equals(this.code, ResultCode.SUCCESS.getCode()) && ObjectUtil.equals(this.success, Boolean.TRUE);
    }

    public static <T> Result<T> create() {
        return new Result<>();
    }

    public static <T> Result<T> fail() {
        return fail(ResultCode.FAIL.getCode(), true, ResultCode.FAIL.getMessage());
    }

    public static <T> Result<T> fail(String message) {
        return fail(ResultCode.FAIL.getCode(), true, message);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return fail(resultCode.getCode(), true, resultCode.getMessage());
    }

    public static <T> Result<T> fail(String code, String message) {
        return fail(code, true, message);
    }

    public static <T> Result<T> fail(String code, Boolean success, String message) {
        return fail(code, success, message, null);
    }

    public static <T> Result<T> fail(String message, T data) {
        return fail(ResultCode.FAIL.getCode(), true, message, data);
    }

    public static <T> Result<T> fail(String code, Boolean success, String message, T data) {
        return new Result<>(code, success, message, data);
    }

    public static <T> Result<T> warn(String message) {
        return warn(ResultCode.WARN.getCode(), true, message, null);
    }

    public static <T> Result<T> warn(String message, T data) {
        return warn(ResultCode.WARN.getCode(), true, message, data);
    }

    public static <T> Result<T> warn(String code, Boolean success, String message, T data) {
        return new Result<>(code, success, message, data);
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return success(ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), true, message, data);
    }

}