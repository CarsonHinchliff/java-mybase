package demo.cn.strivers.mybase.web.config;

import cn.strivers.mybase.core.result.Result;
import cn.strivers.mybase.web.util.LocaleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ResultMsgI18nAspect Created by liteng.
 *
 * @author liteng
 * @date 2019/04/17 11:26
 */
@Slf4j
@Aspect
@Component
public class ResultMsgI18nAspect {

    @Autowired
    private LocaleMessageUtil localeMessageUtil;


    @Pointcut(value = "execution(public cn.strivers.mybase.core.result.Result *..controller..*(..))")
    private void i18nPointcut() {
    }

    /**
     * 处理返回result
     *
     * @param joinPoint
     * @param result
     */
    @AfterReturning(value = "i18nPointcut()", returning = "result")
    public void doAfterReturningAdvice(JoinPoint joinPoint, Result result) {
        result.setMessage(localeMessageUtil.getMessage(result.getMessage()));
    }

}
