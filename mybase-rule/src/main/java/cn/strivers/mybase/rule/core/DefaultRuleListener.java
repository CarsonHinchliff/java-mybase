package cn.strivers.mybase.rule.core;

import cn.strivers.mybase.rule.CommonParam;
import cn.strivers.mybase.rule.api.Facts;
import cn.strivers.mybase.rule.api.Rule;
import cn.strivers.mybase.rule.api.RuleListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 默认规则执行监听器
 */
@Slf4j
public class DefaultRuleListener implements RuleListener {

    @Override
    public boolean beforeEvaluate(final Rule rule, final Facts facts) {
        return true;
    }

    @Override
    public void afterEvaluate(final Rule rule, final Facts facts, final boolean evaluationResult) {
        final String ruleName = rule.getName();
        if (evaluationResult) {
            log.debug("rule[{}] evaluated pass", ruleName);
        } else {
            log.debug("rule[{}] evaluated to false", ruleName);
        }
    }

    @Override
    public void beforePrepare(Rule rule, Facts facts) {
        CommonParam.getInstance().setBeginTime(new Date());
    }

    @Override
    public void onPrepareSuccess(Rule rule, Facts facts) {
        log.debug("rule[{}] prepare success", rule.getName());
    }

    @Override
    public void beforeExecute(final Rule rule, final Facts facts) {
    }

    @Override
    public void onSuccess(final Rule rule, final Facts facts) {
        log.debug("rule[{}] execute success", rule.getName());
        printDuration(rule);
    }

    @Override
    public void onException(final Rule rule, final Facts facts, final Exception exception) {
        log.error("rule[" + rule.getName() + "] execute with exception", exception);
        printDuration(rule);
    }

    private void printDuration(final Rule rule) {
        Date beginTime = CommonParam.getInstance().getBeginTime();
        double duration = (System.currentTimeMillis() - beginTime.getTime()) / 1000.000;
        if (duration > 3) {
            log.info("rule[{}] execute {}s", rule.getName(), duration);
        }
    }
}
