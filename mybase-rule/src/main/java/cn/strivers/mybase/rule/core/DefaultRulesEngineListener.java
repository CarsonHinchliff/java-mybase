package cn.strivers.mybase.rule.core;

import cn.strivers.mybase.rule.api.Facts;
import cn.strivers.mybase.rule.api.Rule;
import cn.strivers.mybase.rule.api.Rules;
import cn.strivers.mybase.rule.api.RulesEngineListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 默认引擎监听器
 */
@Slf4j
public class DefaultRulesEngineListener implements RulesEngineListener {

    private RulesEngineParameters parameters;

    public DefaultRulesEngineListener(RulesEngineParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public void beforeEvaluate(Rules rules, Facts facts) {
        if (!rules.isEmpty()) {
            logEngineParameters();
            log(rules);
            log(facts);
            log.debug("Rules evaluation started");
        } else {
            log.warn("No rules registered! Nothing to apply");
        }
    }

    @Override
    public void afterExecute(Rules rules, Facts facts) {

    }

    private void logEngineParameters() {
        log.debug(parameters.toString());
    }

    private void log(Rules rules) {
        log.debug("Registered rules:");
        for (Rule rule : rules) {
            log.debug("Rule { name = '{}', description = '{}', priority = '{}'}", rule.getName(), rule.getDescription(), rule.getPriority());
        }
    }

    private void log(Facts facts) {
        log.debug("Known facts:");
        for (Map.Entry<String, Object> fact : facts) {
            log.debug("Fact { {} : {} }", fact.getKey(), fact.getValue());
        }
    }
}
