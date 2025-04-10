package demo.cn.strivers.mybase.rule.svc.impl;

import demo.cn.strivers.mybase.rule.dao.RuleDefineDao;
import demo.cn.strivers.mybase.rule.entity.RuleDefine;
import demo.cn.strivers.mybase.rule.svc.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Carson
 * @created 2025/4/10 星期四 下午 05:22
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private RuleDefineDao ruleDefineDao;

    @Override
    public void say(String name) {
        System.out.println("Hello from student service: " + name);
        List<RuleDefine> rules = this.ruleDefineDao.findRuleByGroupId(1);
        rules.forEach(System.out::println);
    }
}
