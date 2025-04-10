package cn.strivers.mybase.rule.mvel;

import cn.strivers.mybase.rule.api.Action;
import cn.strivers.mybase.rule.api.Facts;
import lombok.extern.slf4j.Slf4j;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import java.io.Serializable;

/**
 * MVEL动作
 */
@Slf4j
public class MVELAction implements Action {

    /**
     * 表达式
     */
    private String expression;
    /**
     * 编译表达式
     */
    private Serializable compiledExpression;

    private ParserContext parserContext;

    public MVELAction(String expression) {
        this.expression = expression;
    }

    public MVELAction(String expression, ParserContext parserContext) {
        this.expression = expression;
        this.parserContext = parserContext;
    }

    @Override
    public void execute(Facts facts) {
        try {
            Serializable compiledExpression = MVEL.compileExpression(expression, parserContext);
            MVEL.executeExpression(compiledExpression, facts.asMap());
        } catch (Exception e) {
            log.error("Unable to evaluate expression: '" + expression + "' on facts: " + facts, e);
            throw e;
        }
    }
}
