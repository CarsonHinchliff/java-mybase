package cn.strivers.mybase.rule.core;

import cn.strivers.mybase.rule.api.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认规则引擎
 */
@Slf4j
public final class DefaultRulesEngine extends AbstractRuleEngine {

    public DefaultRulesEngine() {
        super();
    }

    public DefaultRulesEngine(final RulesEngineParameters parameters) {
        super(parameters);
    }

    @Override
    public boolean fire(Rules rules, Facts facts) {
        triggerListenersBeforeRules(rules, facts);
        boolean result = doFire(rules, facts);
        triggerListenersAfterRules(rules, facts);
        return result;
    }

    /**
     * 执行规则
     *
     * @param rules
     * @param facts
     * @return
     */
    private boolean doFire(Rules rules, Facts facts) {
        for (Rule rule : rules) {
            try {
                if (!rule.fire(this, facts)) {
                    return false;
                }
            } catch (Exception exception) {
                triggerListenersOnException(rule, exception, facts);
                if (!parameters.getResultOnException()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 监听器：规则准备前
     *
     * @param rule
     * @param facts
     */
    public boolean triggerListenersBeforePrepare(final Rule rule, Facts facts) {
        for (RuleListener ruleListener : ruleListeners) {
            ruleListener.beforePrepare(rule, facts);
        }
        return true;
    }

    /**
     * 监听器：prepare执行成功
     * @param rule
     * @param facts
     */
    public void triggerListenersOnPrepareSuccess(final Rule rule, Facts facts) {
        for (RuleListener ruleListener : ruleListeners) {
            ruleListener.onPrepareSuccess(rule, facts);
        }
    }

    /**
     * 监听器：规则执行后
     *
     * @param rule
     * @param facts
     */
    public void triggerListenersBeforeExecute(final Rule rule, Facts facts) {
        for (RuleListener ruleListener : ruleListeners) {
            ruleListener.beforeExecute(rule, facts);
        }
    }

    /**
     * 监听器：规则执行成功
     * @param rule
     * @param facts
     */
    public void triggerListenersOnSuccess(final Rule rule, Facts facts) {
        for (RuleListener ruleListener : ruleListeners) {
            ruleListener.onSuccess(rule, facts);
        }
    }

    /**
     * 监听器：规则执行异常
     * @param rule
     * @param exception
     * @param facts
     */
    public void triggerListenersOnException(final Rule rule, final Exception exception, Facts facts) {
        for (RuleListener ruleListener : ruleListeners) {
            ruleListener.onException(rule, facts, exception);
        }
    }

    /**
     * 监听器：规则评估前
     *
     * @param rule
     * @param facts
     */
    public boolean triggerListenersBeforeEvaluate(Rule rule, Facts facts) {
        for (RuleListener ruleListener : ruleListeners) {
            ruleListener.beforeEvaluate(rule, facts);
        }
        return true;
    }

    /**
     * 监听器：规则评估后
     * @param rule
     * @param facts
     * @param evaluationResult
     */
    public void triggerListenersAfterEvaluate(Rule rule, Facts facts, boolean evaluationResult) {
        for (RuleListener ruleListener : ruleListeners) {
            ruleListener.afterEvaluate(rule, facts, evaluationResult);
        }
    }

    /**
     * 监听器：引擎执行前
     *
     * @param rule
     * @param facts
     */
    private void triggerListenersBeforeRules(Rules rule, Facts facts) {
        for (RulesEngineListener rulesEngineListener : rulesEngineListeners) {
            rulesEngineListener.beforeEvaluate(rule, facts);
        }
    }

    /**
     * 监听器：引擎执行后
     * @param rule
     * @param facts
     */
    private void triggerListenersAfterRules(Rules rule, Facts facts) {
        for (RulesEngineListener rulesEngineListener : rulesEngineListeners) {
            rulesEngineListener.afterExecute(rule, facts);
        }
    }

    public RulesEngineParameters getRulesEngineParameters() {
        return super.parameters;
    }
}
