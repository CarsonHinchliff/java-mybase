package cn.strivers.mybase.rule.support;

import cn.strivers.mybase.rule.api.Facts;
import cn.strivers.mybase.rule.api.Rule;
import cn.strivers.mybase.rule.api.Rules;
import cn.strivers.mybase.rule.core.DefaultRulesEngine;
import cn.strivers.mybase.rule.mvel.MVELRule;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 组合规则抽象
 */
public abstract class AbstractCompositeRule extends MVELRule {

    public Set<Rule> rules;

    private Map<Object, Rule> proxyRules;

    public AbstractCompositeRule() {
        this(Rule.DEFAULT_NAME, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
    }

    public AbstractCompositeRule(final String name) {
        this(name, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
    }

    public AbstractCompositeRule(final String name, final String description) {
        this(name, description, Rule.DEFAULT_PRIORITY);
    }

    public AbstractCompositeRule(final String name, final String description, final int priority) {
        super(name, description, priority);
        rules = new TreeSet<>();
        proxyRules = new HashMap<>();
    }

    /**
     * 执行规则
     *
     * @param myDefaultRulesEngine
     * @param facts
     * @return
     */
    @Override
    public abstract boolean fire(DefaultRulesEngine myDefaultRulesEngine, Facts facts);

    /**
     * 添加规则
     *
     * @param rule
     */
    public void addRule(final Object rule) {
        Rule proxy = Rules.asRule(rule);
        rules.add(proxy);
        proxyRules.put(rule, proxy);
    }

    /**
     * 移除规则
     *
     * @param rule
     */
    public void removeRule(final Object rule) {
        Rule proxy = proxyRules.get(rule);
        if (proxy != null) {
            rules.remove(proxy);
        }
    }
}
