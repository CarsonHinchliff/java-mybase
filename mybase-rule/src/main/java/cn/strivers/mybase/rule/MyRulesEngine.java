package cn.strivers.mybase.rule;

import cn.strivers.mybase.rule.api.Facts;
import cn.strivers.mybase.rule.api.RuleListener;
import cn.strivers.mybase.rule.api.Rules;
import cn.strivers.mybase.rule.api.RulesEngine;
import cn.strivers.mybase.rule.core.DefaultRulesEngine;
import cn.strivers.mybase.rule.core.RulesEngineParameters;

public class MyRulesEngine {
    /**
     * 规则引擎
     */
    private RulesEngine rulesEngine;
    /**
     * 规则组
     */
    private Rules rules;

    public MyRulesEngine() {
        this.rulesEngine = new DefaultRulesEngine(new RulesEngineParameters());
    }

    public MyRulesEngine(RulesEngineParameters rulesEngineParameters) {
        this.rulesEngine = new DefaultRulesEngine(rulesEngineParameters);
    }

    public void loadRules(Rules rules) {
        this.rules = rules;
    }

    public boolean fire(Facts facts) {
        return this.rulesEngine.fire(rules, facts);
    }

    public void registerRuleListener(RuleListener ruleListener) {
        ((DefaultRulesEngine) this.rulesEngine).registerRuleListener(ruleListener);
    }
}
