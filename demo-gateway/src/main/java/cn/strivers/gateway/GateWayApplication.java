package cn.strivers.gateway;

import cn.strivers.gateway.filter.TokenGatewayFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * GateWayApplication
 *
 * @author LT
 * @date 2019/1/16 11:53
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.strivers.mybase", "cn.strivers.gateway"})
public class GateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class, args);
    }

    @Bean
    public TokenGatewayFilter getGatewayFilter() {
        return new TokenGatewayFilter();
    }
}
