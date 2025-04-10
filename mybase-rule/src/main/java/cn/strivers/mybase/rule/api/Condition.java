package cn.strivers.mybase.rule.api;

/**
 * 条件
 */
public interface Condition {
    /**
     * 执行条件
     *
     * @param facts
     * @return
     */
    boolean evaluate(Facts facts);

    /**
     * 规则返回值（未通过false）
     */
    Condition FALSE = facts -> false;

    /**
     * 规则返回值(通过true)
     */
    Condition TRUE = facts -> true;
}