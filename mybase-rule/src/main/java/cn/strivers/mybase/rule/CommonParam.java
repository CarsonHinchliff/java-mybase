package cn.strivers.mybase.rule;

import lombok.Data;

import java.util.Date;

@Data
public class CommonParam {
    private static ThreadLocal<CommonParam> threadLocal = new ThreadLocal<>();

    public static CommonParam getInstance() {
        //每个线程对应一个实例
        CommonParam param = threadLocal.get();
        if (param == null) {
            param = new CommonParam();
            threadLocal.set(param);
        }
        return param;
    }

    public static void remove() {
        threadLocal.remove();
    }

    private Date beginTime;

}
