package cn.strivers.mybase.redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class IdUtil {
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 获取唯一ID
     * @param bizType
     * @return
     */
    public String getId(String bizType) {
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        long serialNum = redisTemplate.opsForValue().increment("ID_" + bizType);
        String serial = String.format("%04d", serialNum % 10000);
        return timestamp+serial;
    }

    /**
     * 获取自增序列
     * @param bizType
     * @return
     */
    public long getAutoIncr(String bizType,int numLen) {
        long serial=redisTemplate.opsForValue().increment("AUTO_INCR_"+bizType);
        return serial%(numLen*10);
    }

}
