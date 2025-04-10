package demo.cn.strivers.mybase.rule.controller;

import cn.hutool.json.JSONUtil;
import cn.strivers.mybase.core.result.Result;
import cn.strivers.mybase.rule.MyRulesEngine;
import cn.strivers.mybase.rule.api.Facts;
import demo.cn.strivers.mybase.rule.config.MyRules;
import demo.cn.strivers.mybase.rule.rule.MyRuleConfig;
import demo.cn.strivers.mybase.rule.svc.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RuleController {
    @Autowired
    private MyRules myRules;

    @Autowired
    private StudentService studentService;

    @RequestMapping("/testRules")
    public Result test() {
        Facts facts = new Facts();
        facts.put("number", 3);
        //facts.put("System", System.class);
        MyRulesEngine rulesEngine = MyRuleConfig.rulesEngineMap.get(1);
        rulesEngine.fire(facts);
        return Result.success();
    }

    @RequestMapping("/testRules1")
    public Result test1(@RequestParam(name = "number") Integer number) {
        Facts facts = new Facts();
        facts.put("number", number);
        facts.put("studentSvc", studentService);
        MyRulesEngine rulesEngine = MyRuleConfig.rulesEngineMap.get(1);
        rulesEngine.fire(facts);
        return Result.success();
    }

    @GetMapping("/showRules")
    public Result show(Integer groupId) {
        if (groupId == null) {
            return Result.fail("system.unknow.groupid");
        }
        String s = myRules.getRuleJson(groupId);
        return Result.success(JSONUtil.parse(s));
    }

}
