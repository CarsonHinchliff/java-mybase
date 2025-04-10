package cn.strivers.mybase.rule.support;

import cn.strivers.mybase.rule.api.Facts;
import cn.strivers.mybase.rule.api.Rule;
import cn.strivers.mybase.rule.core.DefaultRulesEngine;
import lombok.extern.slf4j.Slf4j;

/**
 * 串行规则
 */
@Slf4j
public class SerialRuleGroup extends AbstractCompositeRule {

    public SerialRuleGroup() {
    }

    public SerialRuleGroup(String name) {
        super(name);
    }

    public SerialRuleGroup(String name, String description) {
        super(name, description);
    }

    public SerialRuleGroup(String name, String description, int priority) {
        super(name, description, priority);
    }

    @Override
    public boolean fire(DefaultRulesEngine defaultRulesEngine, Facts facts) {
        try {
            doBefore(defaultRulesEngine, this, facts);
            if(!doEvaluate(defaultRulesEngine,this,facts)){
                return true;
            }
            for (Rule rule : rules) {
                if(!rule.fire(defaultRulesEngine,facts)){
                    return false;
                }
            }
            doExecute(defaultRulesEngine, this, facts);
        } catch (Exception exception) {
            defaultRulesEngine.triggerListenersOnException(this, exception, facts);
            if (!defaultRulesEngine.getRulesEngineParameters().getResultOnException()) {
                return false;
            }
        }
        return true;
    }

}
