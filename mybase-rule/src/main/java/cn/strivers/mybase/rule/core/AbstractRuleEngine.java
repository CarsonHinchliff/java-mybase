package cn.strivers.mybase.rule.core;

import cn.strivers.mybase.rule.api.RuleListener;
import cn.strivers.mybase.rule.api.RulesEngine;
import cn.strivers.mybase.rule.api.RulesEngineListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 规则引擎抽象
 */
public abstract class AbstractRuleEngine implements RulesEngine {

    RulesEngineParameters parameters;
    List<RuleListener> ruleListeners;
    List<RulesEngineListener> rulesEngineListeners;

    public AbstractRuleEngine() {
        this(new RulesEngineParameters());
    }

    public AbstractRuleEngine(final RulesEngineParameters parameters) {
        this.parameters = parameters;
        this.ruleListeners = new ArrayList<>();
        this.ruleListeners.add(new DefaultRuleListener());
        this.rulesEngineListeners = new ArrayList<>();
        this.rulesEngineListeners.add(new DefaultRulesEngineListener(parameters));
    }

    @Override
    public RulesEngineParameters getParameters() {
        return new RulesEngineParameters(parameters.getResultOnException());
    }

    @Override
    public List<RuleListener> getRuleListeners() {
        return Collections.unmodifiableList(ruleListeners);
    }

    @Override
    public List<RulesEngineListener> getRulesEngineListeners() {
        return Collections.unmodifiableList(rulesEngineListeners);
    }

    public void registerRuleListener(RuleListener ruleListener) {
        ruleListeners.add(ruleListener);
    }

    public void registerRuleListeners(List<RuleListener> ruleListeners) {
        this.ruleListeners.addAll(ruleListeners);
    }

    public void registerRulesEngineListener(RulesEngineListener rulesEngineListener) {
        rulesEngineListeners.add(rulesEngineListener);
    }

    public void registerRulesEngineListeners(List<RulesEngineListener> rulesEngineListeners) {
        this.rulesEngineListeners.addAll(rulesEngineListeners);
    }
}
