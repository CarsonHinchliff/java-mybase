package cn.strivers.mybase.web;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.strivers.mybase.core.annotation.HttpLimit;
import cn.strivers.mybase.core.annotation.IgnoreLogin;
import cn.strivers.mybase.core.constants.MyConstants;
import cn.strivers.mybase.core.result.Result;
import cn.strivers.mybase.core.result.ResultCode;
import cn.strivers.mybase.core.utils.GsonUtil;
import cn.strivers.mybase.redis.util.RedisUtils;
import cn.strivers.mybase.web.pojo.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 拦截器
 */
@Slf4j
@Component
public class Interceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            //获取完整的url，用于限流（默认500ms），如需要获取不完整的url，使用 request.getRequestURI()
            String url = request.getRequestURL().toString();
            String ip = ServletUtil.getClientIP(request);
            // 获取此请求对应的 Method 处理函数
            Method method = ((HandlerMethod) handler).getMethod();

            if (AnnotationUtil.hasAnnotation(method, HttpLimit.class)) {
                HttpLimit httpLimit = AnnotationUtil.getAnnotation(method, HttpLimit.class);
                if (httpLimit != null) {
                    String key = MyConstants.HTTP_LIMIT + url + ":" + ip;
                    if (redisUtils.hasKey(key)) {
                        log.info("过于频繁点击");
                        return resultMessage(response, Result.fail(ResultCode.ERROR_A1012));
                    } else {
                        redisUtils.set(key, url, httpLimit.interval(), httpLimit.timeUnit());
                    }
                }
            }

            // 如果此 Method 或其所属 Class 标注了 @IgnoreLogin，则忽略掉鉴权
            if (AnnotationUtil.hasAnnotation(method, IgnoreLogin.class)) {
                log.info("Ignoring login");
                return true;
            }
        }
        // todo 登录信息 自行扩展
        LoginHelper.setLogin(new LoginDTO());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        LoginHelper.removeLogin();
    }


    /**
     * 错误信息
     *
     * @param response
     * @return
     */
    private boolean resultMessage(HttpServletResponse response, Result result) {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try (ServletOutputStream writer = response.getOutputStream()) {
            writer.write(GsonUtil.toJsonString(result).getBytes());
        } catch (IOException e) {
            log.error("response error", e);
        }
        return false;
    }
}
