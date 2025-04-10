package cn.strivers.mybase.core.utils;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 雪花算法
 *
 * @author xuwenqian
 * @date 2022/12/26 4:39 PM
 */
@Slf4j
public class SnowflakeUtils {
    /**
     * 起始的时间戳(2023-01-01) 毫秒时间戳41位转化成
     */
    private static final long START_STAMP = 1672502400000L;
    /**
     * 机器标识占用的位数
     */
    private static final long MACHINE_BIT = 5L;
    /**
     * 数据标识id所占的位数
     */
    private static final long DATACENTER_BIT = 5L;
    /**
     * 每一部分最大值 支持的最大数据标识id，结果是31
     */
    private static final long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
    /**
     * 支持的最大机器id，结果是31
     */
    private static final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    /**
     * 每一部分占用的位数，就三个 序列号占用的位数
     */
    private static final long SEQUENCE_BIT = 12L;
    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    /**
     * 机器ID向左移12位
     */
    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    /**
     * 数据标识id向左移17位(12+5)
     */
    private static final long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    /**
     * 时间截向左移22位(5+5+12)
     */
    private static final long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    /**
     * 数据中心[0, 2^DATACENTER_BIT)
     */
    private static long datacenterId = 0L;
    /**
     * 机器标识[0, 2^MACHINE_BIT)
     */
    private static long machineId = 0L;
    /**
     * 毫秒内序列号(0~4095)
     */
    private static long sequence = 0L;
    /**
     * 上一次时间戳
     */
    private static long lastStamp = -1L;


    /**
     * 必须使用单例构造，否则单台机器产生的ID会产生重复
     *
     * @param datacenterId
     * @param machineId
     * @return
     */
    public static SnowflakeUtils getInstance(long datacenterId, long machineId) {
        return Singleton.get(SnowflakeUtils.class, datacenterId, machineId);
    }

    public static SnowflakeUtils getInstance() {
        long dataCenterId = getDataCenterId(MAX_DATACENTER_NUM);
        long machineId = getMachineId(dataCenterId, MAX_MACHINE_NUM);
        log.info("雪花id数据中心id：{},机器id：{}", dataCenterId, machineId);
        return Singleton.get(SnowflakeUtils.class, datacenterId, machineId);
    }

    /**
     * 构造函数(分布式每台机子每条线程都不一样参数)
     *
     * @param datacenterId 数据中心
     * @param machineId    机器id
     */
    public SnowflakeUtils(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        SnowflakeUtils.datacenterId = datacenterId;
        SnowflakeUtils.machineId = machineId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public static String nextStrId() {
        return String.valueOf(nextId());
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized static long nextId() {
        long currStamp = getNewStamp();
        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (currStamp < lastStamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStamp == lastStamp) {
            // if条件里表示当前调用和上一次调用落在了相同毫秒内，只能通过第三部分，序列号自增来判断为唯一，所以+1.
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大，只能等待下一个毫秒
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为0
            // 执行到这个分支的前提是currTimestamp > lastTimestamp，说明本次调用跟上次调用对比，已经不再同一个毫秒内了，这个时候序号可以重新回置0了。
            sequence = 0L;
        }

        lastStamp = currStamp;
        // 就是用相对毫秒数、机器ID和自增序号拼接
        return (currStamp - START_STAMP) << TIMESTAMP_LEFT // 时间戳部分
                | datacenterId << DATACENTER_LEFT      // 数据中心部分
                | machineId << MACHINE_LEFT            // 机器标识部分
                | sequence;                            // 序列号部分
    }

    /**
     * 获取下一个毫秒
     *
     * @return
     */
    private static long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    /**
     * 获取机器id
     *
     * @param datacenterId
     * @param maxWorkerId
     * @return
     */
    public static long getMachineId(long datacenterId, long maxWorkerId) {
        final StringBuilder mpid = new StringBuilder();
        mpid.append(datacenterId);
        try {
            mpid.append(RuntimeUtil.getPid());
        } catch (UtilException igonre) {
            // ignore
        }
        /*
         * MAC + PID 的 hashcode 获取16个低位
         */
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    /**
     * 获取数据中心ID
     *
     * @param maxDatacenterId
     * @return
     */
    public static long getDataCenterId(long maxDatacenterId) {
        Assert.isTrue(maxDatacenterId > 0, "maxDatacenterId must be > 0");
        if (maxDatacenterId == Long.MAX_VALUE) {
            maxDatacenterId -= 1;
        }
        long id = 1L;
        byte[] mac = null;
        try {
            mac = NetUtil.getLocalHardwareAddress();
        } catch (UtilException ignore) {
            // ignore
        }
        if (null != mac) {
            id = ((0x000000FF & (long) mac[mac.length - 2])
                    | (0x0000FF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
            id = id % (maxDatacenterId + 1);
        }

        return id;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return
     */
    private static long getNewStamp() {
        return System.currentTimeMillis();
    }


    public static void main(String[] args) {
        long id = nextId();
        System.out.println(id);
    }

}
