package cn.strivers.mybase.task.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author ：ypp
 * @date ：Created in 9:49 2019/7/2
 * @Description : 定时任务工具类
 * @Modified By：
 * @Version : 1.0$
 */
@Slf4j
@Component
public class QuartzJobManager {
    private static QuartzJobManager jobUtil;

    @Autowired
    private Scheduler scheduler;

    public QuartzJobManager() {
        log.info("init jobUtil");
        jobUtil = this;
    }

    public static QuartzJobManager getInstance() {
        log.info("return JobCreateUtil");
        return QuartzJobManager.jobUtil;
    }


    /**
     * 创建job，可传参
     *
     * @param clazz          任务类
     * @param jobName        任务名称
     * @param jobGroupName   任务所在组名称
     * @param cronExpression cron表达式
     * @param argMap         map形式参数
     * @throws Exception
     * @author ypp
     * @date 2019/7/02.
     */
    public void addJob(Class clazz, String jobName, String jobGroupName, String cronExpression, Map<String, Object> argMap) {
        try {
            // 启动调度器
            scheduler.start();
            log.info("cron 表达式 = {}", cronExpression);

            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(((Job) clazz.newInstance()).getClass()).withIdentity(jobName, jobGroupName).build();

            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName).withSchedule(scheduleBuilder).build();

            //获得JobDataMap，写入数据
            trigger.getJobDataMap().putAll(argMap);

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            log.error("创建job失败：{}", e.getMessage(), e);
        }
    }

    /**
     * job 删除
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务所在组名称
     * @throws Exception
     * @author ypp
     * @date 2019/7/02.
     */
    public void deleteJob(String jobName, String jobGroupName) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroupName));
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            log.error("job删除失败：{}", e.getMessage(), e);
        }
    }

    /**
     * 获取所有任务列表
     *
     * @return
     * @throws SchedulerException
     */
    public List<Map<String, Object>> getAllJob() {
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            List<Map<String, Object>> jobList = new ArrayList<>();
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Map<String, Object> job = new HashMap<>();
                    job.put("jobName", jobKey.getName());
                    job.put("jobGroupName", jobKey.getGroup());
                    job.put("trigger", trigger.getKey());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    job.put("jobStatus", triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        job.put("cronExpression", cronExpression);
                    }
                    jobList.add(job);
                }
            }
            return jobList;
        } catch (SchedulerException e) {
            log.error("获取所有任务列表失败：{}", e.getMessage(), e);
            return null;
        }
    }
}
