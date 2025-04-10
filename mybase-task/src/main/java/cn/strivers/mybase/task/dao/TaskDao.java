package cn.strivers.mybase.task.dao;

import cn.strivers.mybase.task.model.TaskCron;

import java.util.List;
import java.util.Map;

/**
 * @author ：ypp
 * @date ：Created in 15:17 2019/7/1
 * @Description : 定时任务接口
 * @Modified By：
 * @Version : 1.0$
 */
public interface TaskDao {
    /**
     * 查询启用的定时任务
     *
     * @param
     * @return
     */
    List<TaskCron> findByOneTenant();

    /**
     * 查询所有商户的定时任务
     *
     * @param tenant
     * @return
     */
    List<TaskCron> findByAllTenant(Map<Integer, String> tenant);
}