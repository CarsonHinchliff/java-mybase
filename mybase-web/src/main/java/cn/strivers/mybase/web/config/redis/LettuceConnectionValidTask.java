package cn.strivers.mybase.web.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每隔5分钟校验异常lettuce连接是否正常，解决长期空闲lettuce连接关闭但是netty不能及时监控到的问题
 *
 * @author mozhu
 * @date 2024/3/12 09:36
 */
@Component
@Slf4j
@EnableScheduling
public class LettuceConnectionValidTask {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void task() {
        if (redisConnectionFactory instanceof LettuceConnectionFactory) {
            LettuceConnectionFactory connectionFactory = (LettuceConnectionFactory) redisConnectionFactory;
            connectionFactory.validateConnection();
        }
    }
}
