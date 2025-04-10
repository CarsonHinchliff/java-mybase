package cn.strivers.mybase.rule.core;

/**
 * 规则引擎参数
 */
public class RulesEngineParameters {

    /**
     * 执行异常默认结果
     */
    private boolean resultOnException = false;

    public RulesEngineParameters() {
    }

    public RulesEngineParameters(final boolean resultOnException) {
        this.resultOnException = resultOnException;
    }

    public boolean getResultOnException() {
        return resultOnException;
    }

    public void setResultOnException(final boolean resultOnException) {
        this.resultOnException = resultOnException;
    }

}
