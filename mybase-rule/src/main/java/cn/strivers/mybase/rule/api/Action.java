package cn.strivers.mybase.rule.api;

/**
 * 动作
 */
public interface Action {

    /**
     * 执行action
     *
     * @param facts
     */
    void execute(Facts facts);
}