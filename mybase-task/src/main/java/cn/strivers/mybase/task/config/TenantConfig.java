package cn.strivers.mybase.task.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "mybase.task")
public class TenantConfig {

    private Map<Integer, String> tenant;

    public Map<Integer, String> getTenant() {
        return tenant;
    }

    public void setTenant(Map<Integer, String> tenant) {
        this.tenant = tenant;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(30);
        return taskScheduler;
    }
}
