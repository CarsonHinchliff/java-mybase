package cn.strivers.mybase.rule.core;

import cn.hutool.core.util.StrUtil;
import cn.strivers.mybase.rule.api.Facts;
import cn.strivers.mybase.rule.api.Rule;

import java.util.Map;
import java.util.Objects;

/**
 * 基础规则
 */
public abstract class AbstractBasicRule implements Rule {

    /**
     * 名字
     */
    protected String name;

    /**
     * 描述
     */
    protected String description;
    /**
     * 优先级
     */
    protected int priority;

    /**
     * 规则定义
     */
    protected Map<String, Object> ruleDefine;

    public AbstractBasicRule() {
        this(Rule.DEFAULT_NAME, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
    }

    public AbstractBasicRule(final String name) {
        this(name, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
    }

    public AbstractBasicRule(final String name, final String description) {
        this(name, description, Rule.DEFAULT_PRIORITY);
    }

    public AbstractBasicRule(final String name, final String description, final int priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;
    }

    @Override
    public boolean fire(DefaultRulesEngine defaultRulesEngine, Facts facts) {
        try {
            doBefore(defaultRulesEngine, this, facts);
            if (doEvaluate(defaultRulesEngine, this, facts)) {
                doExecute(defaultRulesEngine, this, facts);
            } else {
                return false;
            }
        } catch (Exception exception) {
            defaultRulesEngine.triggerListenersOnException(this, exception, facts);
            if (!defaultRulesEngine.getRulesEngineParameters().getResultOnException()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 前置action
     *
     * @param defaultRulesEngine
     * @param rule
     * @param facts
     */
    public void doBefore(DefaultRulesEngine defaultRulesEngine, Rule rule, Facts facts) {
        defaultRulesEngine.triggerListenersBeforePrepare(rule, facts);
        this.before(facts);
        defaultRulesEngine.triggerListenersOnPrepareSuccess(rule, facts);
    }

    /**
     * 评估规则
     *
     * @param defaultRulesEngine
     * @param rule
     * @param facts
     * @return
     */
    public boolean doEvaluate(DefaultRulesEngine defaultRulesEngine, Rule rule, Facts facts) {
        defaultRulesEngine.triggerListenersBeforeEvaluate(rule, facts);
        boolean result = this.evaluate(facts);
        defaultRulesEngine.triggerListenersAfterEvaluate(rule, facts, result);
        return result;
    }

    /**
     * 后置action
     *
     * @param defaultRulesEngine
     * @param rule
     * @param facts
     */
    public void doExecute(DefaultRulesEngine defaultRulesEngine, Rule rule, Facts facts) {
        defaultRulesEngine.triggerListenersBeforeExecute(rule, facts);
        this.execute(facts);
        defaultRulesEngine.triggerListenersOnSuccess(rule, facts);
    }

    /**
     * 前置action
     *
     * @param facts
     */
    @Override
    public abstract void before(Facts facts);

    /**
     * 评估规则
     *
     * @param facts
     * @return
     */
    @Override
    public abstract boolean evaluate(Facts facts);

    /**
     * 后置action
     *
     * @param facts
     */
    @Override
    public abstract void execute(Facts facts);

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(final int priority) {
        this.priority = priority;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Map<String, Object> getRuleDefine() {
        return ruleDefine;
    }

    @Override
    public void setRuleDefine(Map<String, Object> ruleDefine) {
        this.ruleDefine = ruleDefine;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractBasicRule abstractBasicRule = (AbstractBasicRule) o;

        if (priority != abstractBasicRule.priority) {
            return false;
        }
        if (!name.equals(abstractBasicRule.name)) {
            return false;
        }
        return Objects.equals(description, abstractBasicRule.description);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (StrUtil.isNotBlank(description) ? description.hashCode() : 0);
        result = 31 * result + priority;
        return result;
    }

    @Override
    public String toString() {
        return name;
    }


    @Override
    public int compareTo(final Rule rule) {
        if (getPriority() < rule.getPriority()) {
            return -1;
        } else if (getPriority() > rule.getPriority()) {
            return 1;
        } else {
            return getName().compareTo(rule.getName());
        }
    }
}
