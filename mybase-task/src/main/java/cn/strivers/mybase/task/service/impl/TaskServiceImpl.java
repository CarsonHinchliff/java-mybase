package cn.strivers.mybase.task.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.strivers.mybase.task.config.TenantConfig;
import cn.strivers.mybase.task.dao.TaskDao;
import cn.strivers.mybase.task.job.QuartzJobManager;
import cn.strivers.mybase.task.job.TaskQuartz;
import cn.strivers.mybase.task.model.TaskCron;
import cn.strivers.mybase.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TenantConfig tenantConfig;

    @Autowired
    private TaskDao taskDao;

    @Override
    public boolean flushTask() {
        try {
            Map<Integer, String> tenantMap = tenantConfig.getTenant();
            List<TaskCron> taskCronList;
            if (MapUtil.isNotEmpty(tenantMap)) {
                taskCronList = taskDao.findByAllTenant(tenantMap);
            } else {
                taskCronList = taskDao.findByOneTenant();
            }
            taskCronList.forEach(task -> QuartzJobManager.getInstance().addJob(TaskQuartz.class, task.getTaskName(), UUID.randomUUID().toString(), task.getCron(), BeanUtil.beanToMap(task)));
            log.info("定时任务启动成功！");
            return true;
        } catch (Exception e) {
            log.error("定时任务启动失败：{}", e.getMessage(), e);
            return false;
        }
    }
}
