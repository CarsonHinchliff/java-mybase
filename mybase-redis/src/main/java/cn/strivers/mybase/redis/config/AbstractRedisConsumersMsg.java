package cn.strivers.mybase.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xuwq
 * @Date: 2019-07-17 17:02
 */
@Slf4j
public abstract class AbstractRedisConsumersMsg implements RedisConsumersService {
    private ExecutorService service = new ThreadPoolExecutor(1, 10, 180L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), r -> new Thread(r, "messageRedis_thread_pool_" + r.hashCode()));


    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void sendMessage(Object key, Object msg) {
        redisTemplate.opsForList().leftPush("REDIS_MQ_" + key, msg);
    }

    @Override
    public void consumersKey(Object key) {
        Object newKey = "REDIS_MQ_" + key;
        service.submit(() -> {
            while (true) {
                TimeUnit.SECONDS.sleep(1L);
                try {
                    Boolean hasKey = redisTemplate.hasKey(newKey);
                    if (hasKey != null && hasKey) {
                        Object msg = redisTemplate.opsForList().rightPop(newKey, 10, TimeUnit.MILLISECONDS);
                        if (!ObjectUtils.isEmpty(msg)) {
                            consumersMsg(msg);
                        }
                    }
                } catch (Exception e) {
                    log.error("redis消息消费异常：{},key:{}", e.getMessage(), newKey);
                }
            }
        });
    }

    @Override
    public void consumersMsg(Object msg) {
        log.info("redis消息消费无需处理......");
    }
}
