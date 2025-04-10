package cn.strivers.mybase.task.controller;


import cn.hutool.core.collection.CollUtil;
import cn.strivers.mybase.core.result.Result;
import cn.strivers.mybase.task.job.QuartzJobManager;
import cn.strivers.mybase.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author ：ypp
 * @date ：Created in 20:13 2019/7/1
 * @Description : 定时任务刷新接口
 * @Modified By：
 * @Version : 1.0$
 */
@Slf4j
@RestController
public class FlushController {

    @Autowired
    private TaskService taskService;

    @PostMapping(value = "/flushTask")
    public Result flushTask() {
        try {
            List<Map<String, Object>> allJob = QuartzJobManager.getInstance().getAllJob();
            if (CollUtil.isEmpty(allJob)) {
                log.info("无定时任务");
                return Result.success("无定时任务！");
            }
            log.info("所有定时任务  = {}", allJob);
            for (Map<String, Object> map : allJob) {
                QuartzJobManager.getInstance().deleteJob(String.valueOf(map.get("jobName")), String.valueOf(map.get("jobGroupName")));
            }
            boolean result = taskService.flushTask();
            if (!result) {
                log.info("定时任务刷新失败！");
                return Result.fail("定时任务刷新失败！");
            }
            log.info("定时任务刷新成功！");
            return Result.success("定时任务刷新成功！");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("定时任务刷新发生异常");
            return Result.fail("定时任务刷新发生异常");
        }
    }
}
