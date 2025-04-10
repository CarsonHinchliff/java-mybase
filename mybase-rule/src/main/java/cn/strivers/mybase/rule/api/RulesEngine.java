package cn.strivers.mybase.rule.api;

import cn.strivers.mybase.rule.core.RulesEngineParameters;

import java.util.List;

/**
 * 规则引擎
 */
public interface RulesEngine {

    /**
     * 获取规则引擎参数
     *
     * @return
     */
    RulesEngineParameters getParameters();

    /**
     * 规则执行监听器
     *
     * @return
     */
    List<RuleListener> getRuleListeners();

    /**
     * 规则引擎监听器
     *
     * @return
     */
    List<RulesEngineListener> getRulesEngineListeners();

    /**
     * 执行规则
     *
     * @param rules
     * @param facts
     * @return
     */
    boolean fire(Rules rules, Facts facts);
}
