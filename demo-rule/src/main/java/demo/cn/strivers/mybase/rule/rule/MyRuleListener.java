package demo.cn.strivers.mybase.rule.rule;

import cn.strivers.mybase.rule.api.Facts;
import cn.strivers.mybase.rule.api.Rule;
import cn.strivers.mybase.rule.api.RuleListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyRuleListener implements RuleListener {
    @Override
    public boolean beforeEvaluate(Rule rule, Facts facts) {
        log.info("beforeEvaluate rule:{},facts:{}",rule.getRuleDefine().toString(),facts.toString());
        return true;
    }

    @Override
    public void afterEvaluate(Rule rule, Facts facts, boolean evaluationResult) {
        log.info("afterEvaluate result:{}",evaluationResult);
    }

    @Override
    public void beforePrepare(Rule rule, Facts facts) {
        log.info("beforePrepare rule:{},facts:{}",rule.toString(),facts.toString());
    }

    @Override
    public void onPrepareSuccess(Rule rule, Facts facts) {
        log.info("onPrepareSuccess rule:{},facts:{}",rule.toString(),facts.toString());
    }

    @Override
    public void beforeExecute(Rule rule, Facts facts) {
        log.info("beforeExecute rule:{},facts:{}",rule.toString(),facts.toString());
    }

    @Override
    public void onSuccess(Rule rule, Facts facts) {
        log.info("onSuccess rule:{},facts:{}",rule.toString(),facts.toString());
    }

    @Override
    public void onException(Rule rule, Facts facts, Exception exception) {
        log.info("onException rule:{},facts:{}",rule.toString(),facts.toString());
    }
}
