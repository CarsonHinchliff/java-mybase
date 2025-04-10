package cn.strivers.mybase.web.log;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Map;

/**
 * 全局链路拦截器
 *
 * @author mozhu
 * @since 2024-10-24 16:13:47
 */
@Slf4j
@Component
public class TraceInterceptor implements HandlerInterceptor {

    private static final TransmittableThreadLocal<StopWatch> INVOKE_TIMETL = new TransmittableThreadLocal<>();

    /**
     * 访问控制器方法前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 可配置打印环境
        // String activeProfile = SpringUtil.getActiveProfile();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        log.info("开始请求 => [{}],URL[{}]", method, requestURI);
        // 打印请求参数
        if (isJsonRequest(request)) {
            String jsonParam = "";
            if (request instanceof RepeatedlyRequestWrapper) {
                BufferedReader reader = request.getReader();
                jsonParam = IoUtil.read(reader);
            }
            log.info("请求参数类型[json],参数:[{}]", jsonParam);
        } else {
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (MapUtil.isNotEmpty(parameterMap)) {
                String parameters = JSONUtil.toJsonStr(parameterMap);
                log.info("请求参数类型[param],参数:[{}]", parameters);
            }
        }
        StopWatch stopWatch = new StopWatch();
        INVOKE_TIMETL.set(stopWatch);
        stopWatch.start();
        // 可配置返回traceId
        return true;
    }

    /**
     * 访问控制器方法后执行
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    /**
     * postHandle方法执行完成后执行，一般用于释放资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        StopWatch stopWatch = INVOKE_TIMETL.get();
        stopWatch.stop();
        log.info("结束请求 =>[{}],URL[{}],耗时:[{}]毫秒", request.getMethod(), request.getRequestURI(), stopWatch.getTime());
        INVOKE_TIMETL.remove();
    }

    /**
     * 判断本次请求的数据类型是否为json
     *
     * @param request request
     * @return boolean
     */
    private boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (StringUtils.isNotBlank(contentType)) {
            return StringUtils.startsWithIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE);
        }
        return false;
    }
}
