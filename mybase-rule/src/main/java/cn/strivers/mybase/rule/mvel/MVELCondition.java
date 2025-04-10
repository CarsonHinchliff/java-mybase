package cn.strivers.mybase.rule.mvel;

import cn.hutool.core.convert.Convert;
import cn.strivers.mybase.rule.api.Condition;
import cn.strivers.mybase.rule.api.Facts;
import lombok.extern.slf4j.Slf4j;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import java.io.Serializable;

/**
 * MVEL条件
 */
@Slf4j
public class MVELCondition implements Condition {

    /**
     * 表达式
     */
    private String expression;

    private ParserContext parserContext;

    public MVELCondition(String expression) {
        this.expression = expression;
    }

    public MVELCondition(String expression, ParserContext parserContext) {
        this.expression = expression;
        this.parserContext = parserContext;

    }

    @Override
    public boolean evaluate(Facts facts) {
        try {
            Serializable compiledExpression = MVEL.compileExpression(expression, parserContext);
            return Convert.toBool(MVEL.executeExpression(compiledExpression, facts.asMap()), false);
        } catch (Exception e) {
            log.error("Unable to evaluate expression: '" + expression + "' on facts: " + facts, e);
            return false;
        }
    }
}
