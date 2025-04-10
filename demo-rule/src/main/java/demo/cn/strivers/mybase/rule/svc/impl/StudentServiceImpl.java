package demo.cn.strivers.mybase.rule.svc.impl;

import demo.cn.strivers.mybase.rule.svc.StudentService;
import org.springframework.stereotype.Service;

/**
 * @author Carson
 * @created 2025/4/10 星期四 下午 05:22
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Override
    public void say(String name) {
        System.out.println("Hello from student service: " + name);
    }
}
