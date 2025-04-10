package demo.cn.strivers.mybase.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: xuwq
 * @date: 2020/10/13 18:41
 */
@RestController
@RequestMapping("/test/redis")
public class DemoRedisController {
    @Autowired
    private DemoRedisMq demoRedisMq;

    @PostMapping("send")
    public void test() {
        demoRedisMq.sendMessage("test", 111);
        demoRedisMq.sendMessage("test", 222);
        demoRedisMq.sendMessage("test", 333);
        demoRedisMq.sendMessage("test", 444);
        demoRedisMq.sendMessage("test", 555);
    }


    @PostMapping("consumers")
    public void test1() {
        demoRedisMq.consumersKey("test");
    }


}
