package cn.strivers.mybase.rule.api;

/**
 * 规则引擎监听器
 */
public interface RulesEngineListener {

    /**
     * 引擎执行前
     *
     * @param rules
     * @param facts
     */
    void beforeEvaluate(Rules rules, Facts facts);

    /**
     * 引擎执行后
     *
     * @param rules
     * @param facts
     */
    void afterExecute(Rules rules, Facts facts);
}