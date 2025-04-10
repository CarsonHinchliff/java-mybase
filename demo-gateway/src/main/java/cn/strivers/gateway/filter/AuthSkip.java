package cn.strivers.gateway.filter;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Const
 *
 * @author LT
 * @date 2019/1/17 15:07
 */
@Component
@ConfigurationProperties(prefix = "auth.skip")
@Slf4j
public class AuthSkip {

    private Map wan;
    private Map lan;

    public Map getLan() {
        return lan;
    }

    public void setLan(Map lan) {
        log.info("内网跳过接口：{}", JSONUtil.toJsonStr(lan));
        this.lan = lan;
    }

    public Map getWan() {
        return wan;
    }

    public void setWan(Map wan) {
        log.info("外网跳过接口：{}", JSONUtil.toJsonStr(wan));
        this.wan = wan;
    }
}
