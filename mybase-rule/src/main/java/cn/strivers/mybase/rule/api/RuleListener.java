package cn.strivers.mybase.rule.api;

/**
 * 规则执行监听器
 */
public interface RuleListener {

    /**
     * 条件评估前
     *
     * @param rule
     * @param facts
     * @return
     */
    boolean beforeEvaluate(Rule rule, Facts facts);

    /**
     * 条件评估后
     *
     * @param rule
     * @param facts
     * @param evaluationResult
     */
    void afterEvaluate(Rule rule, Facts facts, boolean evaluationResult);

    /**
     * 准备执行前
     *
     * @param rule
     * @param facts
     */
    void beforePrepare(Rule rule, Facts facts);

    /**
     * 准备执行成功
     *
     * @param rule
     * @param facts
     */
    void onPrepareSuccess(Rule rule, Facts facts);

    /**
     * action执行前
     *
     * @param rule
     * @param facts
     */
    void beforeExecute(Rule rule, Facts facts);

    /**
     * action执行成功
     *
     * @param rule
     * @param facts
     */
    void onSuccess(Rule rule, Facts facts);

    /**
     * 执行异常
     *
     * @param rule
     * @param facts
     */
    void onException(Rule rule, Facts facts, Exception exception);

}
