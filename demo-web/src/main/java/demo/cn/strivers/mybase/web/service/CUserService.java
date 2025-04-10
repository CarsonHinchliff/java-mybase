package demo.cn.strivers.mybase.web.service;


import demo.cn.strivers.mybase.web.entity.CUser;
import java.util.List;

public interface CUserService {
    List<CUser> findByUserId(Long id);
}
