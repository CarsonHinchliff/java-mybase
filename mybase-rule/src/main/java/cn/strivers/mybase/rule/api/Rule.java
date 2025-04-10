package cn.strivers.mybase.rule.api;

import cn.strivers.mybase.rule.core.DefaultRulesEngine;

import java.util.Map;

/**
 * 规则
 */
public interface Rule extends Comparable<Rule> {

    String DEFAULT_NAME = "rule";

    String DEFAULT_DESCRIPTION = "description";

    int DEFAULT_PRIORITY = Integer.MAX_VALUE - 1;

    /**
     * 获取名字
     *
     * @return
     */
    String getName();

    /**
     * 获取描述
     *
     * @return
     */
    String getDescription();

    /**
     * 获取执行优先级
     *
     * @return
     */
    int getPriority();

    /**
     * 获取规则定义
     *
     * @return
     */
    Map<String, Object> getRuleDefine();

    /**
     * set规则定义
     *
     * @param ruleDefine
     */
    void setRuleDefine(Map<String, Object> ruleDefine);

    /**
     * 执行
     *
     * @param defaultRulesEngine
     * @param facts
     * @return
     */
    boolean fire(DefaultRulesEngine defaultRulesEngine, Facts facts);

    /**
     * 前置action
     *
     * @param facts
     */
    void before(Facts facts);

    /**
     * 评估规则
     *
     * @param facts
     * @return
     */
    boolean evaluate(Facts facts);

    /**
     * 后置action
     *
     * @param facts
     */
    void execute(Facts facts);

}
