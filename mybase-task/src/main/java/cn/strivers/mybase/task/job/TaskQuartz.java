package cn.strivers.mybase.task.job;

import cn.hutool.http.HttpRequest;
import cn.strivers.mybase.core.utils.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * @author ：ypp
 * @date ：Created in 10:11 2019/7/2
 * @Description : 定时任务测试
 * @Modified By：
 * @Version : 1.0$
 */
@Slf4j
@Component
public class TaskQuartz implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
        String url = jobDataMap.getString("url").trim();
        String param = jobDataMap.getString("param");
        param = StrUtil.isBlank(param) ? "" : param;
        String tenantId = jobDataMap.getString("tenantId");
        if (StrUtil.isBlank(tenantId)) {
            log.error("租户id为空，任务启动失败");
            return;
        }
        log.info("tenantId:{},id:{},taskName:{},任务启动。。。", tenantId, jobDataMap.get("taskId"), jobDataMap.get("remark"));
        String clientInfo = "c5-t" + tenantId;
        String result = HttpRequest.post(url).body(param).header("clientInfo", clientInfo).execute().body();
        log.info("tenantId:{},id:{},taskName:{},执行完成,结果:{}", tenantId, jobDataMap.get("taskId"), jobDataMap.get("remark"), result);
    }
}
