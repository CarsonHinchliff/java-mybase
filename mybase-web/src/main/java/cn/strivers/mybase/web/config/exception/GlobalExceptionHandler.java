package cn.strivers.mybase.web.config.exception;

import cn.strivers.mybase.core.exception.ApplicationException;
import cn.strivers.mybase.core.result.Result;
import cn.strivers.mybase.core.result.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * 全局异常处理
 *
 * @author mozhu
 * @date 2023-03-15 10:08:03
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * 将Spring DataBinder配置为使用直接字段访问
     *
     * @param dataBinder
     */
    @InitBinder
    private void activateDirectFieldAccess(DataBinder dataBinder) {
        dataBinder.initDirectFieldAccess();
    }

    /**
     * 全局异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("Exception 报错接口：[{}],报错信息：{}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(ResultCode.ERROR_B0001);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public Result runExceptionHandler(HttpServletRequest request, RuntimeException e) {
        log.error("RuntimeException 报错接口：[{}],报错信息：{}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(ResultCode.ERROR_B0001);
    }


    /**
     * 统一参数校验
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        log.info("MethodArgumentNotValidException:参数校验异常:{}({})", fieldError.getDefaultMessage(), fieldError.getField());
        return Result.fail(ResultCode.ERROR_A1014.getCode(), fieldError.getDefaultMessage());
    }

    /**
     * 统一参数校验
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result bindException(BindException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError == null) {
            return Result.fail("参数校验失败");
        }
        log.info("BindException:参数校验异常:{}({})", fieldError.getDefaultMessage(), fieldError.getField());
        return Result.fail(ResultCode.ERROR_A1014.getCode(), fieldError.getDefaultMessage());
    }


    /**
     * 自定义异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ApplicationException.class)
    public Result applicationException(ApplicationException e) {
        String errorCode = e.getErrorCode();
        if (StringUtils.isBlank(errorCode)) {
            errorCode = ResultCode.FAIL.getCode();
        }
        return Result.fail(errorCode, e.getMessage());
    }

    /**
     * 主键或UNIQUE索引，数据重复异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("数据库中已存在记录'{}'", e.getMessage());
        return Result.fail(ResultCode.ERROR_B0004.getCode(), "数据库中已存在该记录，请联系管理员确认");
    }


    /**
     * sql报错 统一异常处理
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = SQLException.class)
    public Result sqlExceptionHandler(HttpServletRequest request, SQLException e) {
        log.error("SQLException 报错接口：[{}],报错信息：{}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(ResultCode.ERROR_B0004);
    }

    /**
     * 参数校验
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result missingServletRequestParameterExceptionHandler(HttpServletRequest request, MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException 报错接口：[{}],报错信息：{}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.ERROR_A1014.getCode(), e.getMessage());
    }

    /**
     * 参数校验
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = MissingServletRequestPartException.class)
    public Result missingServletRequestPartExceptionHandler(HttpServletRequest request, MissingServletRequestPartException e) {
        log.error("MissingServletRequestPartException 报错接口：[{}],报错信息：{}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.ERROR_A1014.getCode(), e.getMessage());
    }


    /**
     * 请求方法校验
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Result httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException 报错接口：[{}],报错信息：{}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(ResultCode.ERROR_A1005.getCode(), e.getMessage());
    }

    /**
     * 参数校验 断言
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result illegalArgumentExceptionHandler(HttpServletRequest request, IllegalArgumentException e) {
        log.error("IllegalArgumentException 报错接口：[{}],报错信息：{}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.ERROR_A1014.getCode(), e.getMessage());
    }
}
