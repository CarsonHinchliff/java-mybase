package cn.strivers.gateway.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "mybase")
public class MybaseConf {
    private Map<String,String> tenant;
}
