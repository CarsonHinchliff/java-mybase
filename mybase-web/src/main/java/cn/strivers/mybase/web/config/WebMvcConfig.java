package cn.strivers.mybase.web.config;

import cn.strivers.mybase.web.Interceptor;
import cn.strivers.mybase.web.log.TraceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private Interceptor interceptor;
    @Autowired
    private TraceInterceptor traceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");
        registry.addInterceptor(traceInterceptor).addPathPatterns("/**");
    }
}
