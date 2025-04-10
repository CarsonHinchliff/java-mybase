package demo.cn.strivers.mybase.web.config;

import cn.strivers.mybase.db.CommonDao;
import demo.cn.strivers.mybase.web.entity.CUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class DaoConfig {
    @PersistenceContext
    EntityManager entityManager;

    @Bean
    public CommonDao<CUser, Long> getUserDao() {
        return new CommonDao<>(CUser.class, entityManager);
    }
}
