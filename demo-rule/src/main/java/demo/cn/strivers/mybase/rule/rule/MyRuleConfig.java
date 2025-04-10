package demo.cn.strivers.mybase.rule.rule;

import cn.strivers.mybase.rule.MyRulesEngine;
import cn.strivers.mybase.rule.api.Rules;
import cn.strivers.mybase.rule.core.RulesEngineParameters;
import demo.cn.strivers.mybase.rule.config.MyRules;
import demo.cn.strivers.mybase.rule.entity.RuleGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class MyRuleConfig implements CommandLineRunner {
    public static Map<Integer, MyRulesEngine> rulesEngineMap = new HashMap<>();

    @Autowired
    private MyRules myRules;

    @Override
    public void run(String... args) {
        List<RuleGroup> ruleGroupList = myRules.findAllGroup();
        for (RuleGroup ruleGroup : ruleGroupList) {
            //初始化引擎
            RulesEngineParameters rulesEngineParameters = myRules.buildEngineParameters(ruleGroup);
            MyRulesEngine myRulesEngine = new MyRulesEngine(rulesEngineParameters);
            myRulesEngine.registerRuleListener(new MyRuleListener());
            //加载规则
            Rules rules = myRules.buildGroupRules(ruleGroup.getId());
            myRulesEngine.loadRules(rules);
            rulesEngineMap.put(ruleGroup.getId(), myRulesEngine);
        }
    }
}
