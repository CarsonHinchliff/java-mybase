package cn.strivers.mybase.core.exception;


import cn.strivers.mybase.core.result.ResultCode;

/**
 * 自定义异常
 *
 * @author mozhu
 * @date 2023-03-21 10:54:14
 */
public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = -2786284314942880035L;
    private String errorCode;
    private String errorMessage;

    public ApplicationException() {
        this.errorCode = ResultCode.FAIL.getCode();
    }

    public ApplicationException(String errorMessage) {
        super(errorMessage);
        this.errorCode = ResultCode.FAIL.getCode();
        this.errorMessage = errorMessage;
    }

    public ApplicationException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ApplicationException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = ResultCode.FAIL.getCode();
        this.errorMessage = errorMessage;
    }

    public ApplicationException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ApplicationException(Throwable cause) {
        super(cause);
        this.errorCode = ResultCode.FAIL.getCode();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
