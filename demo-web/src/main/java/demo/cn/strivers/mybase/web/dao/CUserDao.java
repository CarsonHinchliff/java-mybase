package demo.cn.strivers.mybase.web.dao;


import demo.cn.strivers.mybase.web.entity.CUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CUserDao extends JpaRepository<CUser, Integer> {
    List<CUser> findByUserId(Long id);
}
