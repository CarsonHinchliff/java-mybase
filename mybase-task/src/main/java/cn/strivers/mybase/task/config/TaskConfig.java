package cn.strivers.mybase.task.config;

import cn.strivers.mybase.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfig implements CommandLineRunner {
    @Autowired
    private TaskService taskService;

    @Override
    public void run(String... args) throws Exception {
        taskService.flushTask();
    }
}
