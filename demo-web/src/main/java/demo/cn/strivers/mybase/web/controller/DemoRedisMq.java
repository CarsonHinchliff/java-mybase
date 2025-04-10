package demo.cn.strivers.mybase.web.controller;

import cn.strivers.mybase.redis.config.AbstractRedisConsumersMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: xuwq
 * @date: 2020/10/13 18:43
 */
@Slf4j
@Component
public class DemoRedisMq extends AbstractRedisConsumersMsg {

    @Override
    public void consumersMsg(Object msg) {
        log.info("消息：{}", msg);
    }
}
