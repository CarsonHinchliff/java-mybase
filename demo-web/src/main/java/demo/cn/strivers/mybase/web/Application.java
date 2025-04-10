package demo.cn.strivers.mybase.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"cn.strivers.mybase","demo.cn.strivers.mybase"})
@EnableJpaRepositories(basePackages = {"cn.strivers.mybase","demo.cn.strivers.mybase"})
@EntityScan(basePackages = {"cn.strivers.mybase","demo.cn.strivers.mybase"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
