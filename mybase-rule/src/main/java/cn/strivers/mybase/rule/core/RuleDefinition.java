package cn.strivers.mybase.rule.core;

import cn.strivers.mybase.rule.api.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则定义
 */
public class RuleDefinition {

    /**
     * 名字
     */
    private String name = Rule.DEFAULT_NAME;
    /**
     * 描述
     */
    private String description = Rule.DEFAULT_DESCRIPTION;
    /**
     * 优先级
     */
    private int priority = Rule.DEFAULT_PRIORITY;
    /**
     * 规则前置准备动作
     */
    private List<String> prepares = new ArrayList<>();
    /**
     * 条件
     */
    private String condition;
    /**
     * 动作组
     */
    private List<String> actions = new ArrayList<>();
    /**
     * 规则定义组
     */
    private List<RuleDefinition> composingRules = new ArrayList<>();
    /**
     * 组合类型:0.原子条件;1:串行；2:并行
     */
    private String compositeRuleType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<String> getPrepares() {
        return prepares;
    }

    public void setPrepares(List<String> actions) {
        this.prepares = actions;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public void setComposingRules(List<RuleDefinition> composingRuleDefinitions) {
        this.composingRules = composingRuleDefinitions;
    }

    public void setCompositeRuleType(String compositeRuleType) {
        this.compositeRuleType = compositeRuleType;
    }

    public String getCompositeRuleType() {
        return compositeRuleType;
    }

    public List<RuleDefinition> getComposingRules() {
        return composingRules;
    }

    public boolean isCompositeRule() {
        return !composingRules.isEmpty();
    }
}