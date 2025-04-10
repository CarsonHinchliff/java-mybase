package cn.strivers.mybase.db;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

/**
 * 公共dao
 *
 * @param <T>
 * @param <ID>
 */
public class CommonDao<T, ID> extends SimpleJpaRepository<T, ID> {
    public CommonDao(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }
}
