package cn.strivers.mybase.redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class LockUtil {
    private static final String LOCK_PREFIX = "LOCK:";
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 加锁
     *
     * @param lockName           锁的key
     * @param acquireTimeout     获取超时时间 单位：毫秒
     * @param milliSecondTimeout 锁的超时时间 单位：毫秒
     * @return 锁标识 true锁已存在 false锁不存在，加锁成功
     */
    public Boolean lock(String lockName, Object value, long acquireTimeout, long milliSecondTimeout) {
        try {
            // 锁名，即key值
            String lockKey = LOCK_PREFIX + lockName;
            // 获取锁的超时时间，超过这个时间则放弃获取锁
            long end = System.currentTimeMillis() + acquireTimeout;
            while (System.currentTimeMillis() < end) {
                //不存在返回true
                if (redisTemplate.opsForValue().setIfAbsent(lockKey, value)) {
                    redisTemplate.expire(lockKey, milliSecondTimeout, TimeUnit.MILLISECONDS);
                    return false;
                }
                // 返回-1代表key没有设置超时时间，为key设置一个超时时间
                if (redisTemplate.getExpire(lockKey) < 0) {
                    redisTemplate.expire(lockKey, milliSecondTimeout, TimeUnit.MILLISECONDS);
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(10L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 加锁
     *
     * @param key
     * @param value
     * @param secondTimeout 单位：秒
     * @return true锁已存在 false锁不存在，加锁成功
     */
    public Boolean lock(String key, Object value, long secondTimeout) {
        return lock(key, value, secondTimeout, TimeUnit.SECONDS);
    }

    /**
     * 加锁
     *
     * @param key
     * @param value
     * @param timeout  时间
     * @param timeUnit 时间类型
     * @return true锁已存在 false锁不存在，加锁成功
     */
    public Boolean lock(String key, Object value, long timeout, TimeUnit timeUnit) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            //设置有效期
            redisTemplate.expire(key, timeout, timeUnit);
            return false;
        }
        return true;
    }

    /**
     * 释放锁
     *
     * @param lockName 锁的key
     * @return
     */
    public void unLock(String lockName) {
        String lockKey = LOCK_PREFIX + lockName;
        redisTemplate.delete(lockKey);
    }
}
