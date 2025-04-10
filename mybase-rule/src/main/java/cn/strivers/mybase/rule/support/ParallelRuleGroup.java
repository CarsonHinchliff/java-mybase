package cn.strivers.mybase.rule.support;

import cn.strivers.mybase.rule.api.Facts;
import cn.strivers.mybase.rule.api.Rule;
import cn.strivers.mybase.rule.core.DefaultRulesEngine;
import lombok.extern.slf4j.Slf4j;

/**
 * 并行组合
 */
@Slf4j
public class ParallelRuleGroup extends AbstractCompositeRule {

    public ParallelRuleGroup() {
    }

    public ParallelRuleGroup(String name) {
        super(name);
    }

    public ParallelRuleGroup(String name, String description) {
        super(name, description);
    }

    public ParallelRuleGroup(String name, String description, int priority) {
        super(name, description, priority);
    }

    @Override
    public boolean fire(DefaultRulesEngine defaultRulesEngine, Facts facts) {
        boolean result = true;
        try {
            doBefore(defaultRulesEngine, this, facts);
            if (!doEvaluate(defaultRulesEngine, this, facts)) {
                return true;
            }
            for (Rule rule : rules) {
                if (!rule.fire(defaultRulesEngine, facts)) {
                    result = false;
                }
            }
            if (result) {
                doExecute(defaultRulesEngine, this, facts);
            }
        } catch (Exception exception) {
            defaultRulesEngine.triggerListenersOnException(this, exception, facts);
            if (!defaultRulesEngine.getRulesEngineParameters().getResultOnException()) {
                return false;
            }
        }
        return result;
    }

}
