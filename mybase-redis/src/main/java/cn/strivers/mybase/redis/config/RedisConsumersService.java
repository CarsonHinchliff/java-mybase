package cn.strivers.mybase.redis.config;

/**
 * @Author: xuwq
 * @Date: 2019-07-17 16:31
 */
public interface RedisConsumersService {
    /**
     * 发送消息
     *
     * @param key
     * @param msg
     */
    void sendMessage(Object key, Object msg);

    /**
     * 消费消息
     *
     * @param key
     * @return
     */
    void consumersKey(Object key);


    /**
     * 处理业务代码
     *
     * @param msg
     */
    void consumersMsg(Object msg);
}
