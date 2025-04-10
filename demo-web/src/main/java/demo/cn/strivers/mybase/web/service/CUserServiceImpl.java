package demo.cn.strivers.mybase.web.service;

import demo.cn.strivers.mybase.web.dao.CUserDao;
import demo.cn.strivers.mybase.web.entity.CUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CUserServiceImpl implements CUserService {
    @Autowired
    CUserDao cUserDao;
//    @Autowired
//    CommonDao<CUser,Long> commonDao;

    @Override
    public List<CUser> findByUserId(Long id) {
        CUser cUser = new CUser();
        cUser.setUserId(id);
//        return commonDao.findAll(Example.of(cUser));
        return cUserDao.findByUserId(id);
    }
}
