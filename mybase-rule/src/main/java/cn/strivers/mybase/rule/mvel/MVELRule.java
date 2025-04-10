package cn.strivers.mybase.rule.mvel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.strivers.mybase.rule.api.Action;
import cn.strivers.mybase.rule.api.Condition;
import cn.strivers.mybase.rule.api.Facts;
import cn.strivers.mybase.rule.api.Rule;
import cn.strivers.mybase.rule.core.AbstractBasicRule;
import org.mvel2.ParserContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 存放规则表达式
 */
public class MVELRule extends AbstractBasicRule {

    private Condition condition = Condition.TRUE;
    private List<Action> actions = new ArrayList<>();
    private List<Action> prepares = new ArrayList<>();

    public MVELRule() {
        super(Rule.DEFAULT_NAME, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
    }

    public MVELRule(final String name, final String description, final int priority) {
        super(name, description, priority);
    }

    public MVELRule name(String name) {
        this.name = name;
        return this;
    }

    public MVELRule description(String description) {
        this.description = description;
        return this;
    }

    public MVELRule priority(int priority) {
        this.priority = priority;
        return this;
    }

    public MVELRule when(String condition) {
        return this.when(condition, new ParserContext());
    }

    public MVELRule when(String condition, ParserContext parserContext) {
        if (StrUtil.isNotEmpty(condition)) {
            this.condition = new MVELCondition(condition, parserContext);
        }
        return this;
    }

    public MVELRule prepare(String action) {
        return this.prepare(action, new ParserContext());
    }

    public MVELRule prepare(String action, ParserContext parserContext) {
        if (!StrUtil.isEmpty(action)) {
            this.prepares.add(new MVELAction(action, parserContext));
        }
        return this;
    }

    public MVELRule then(String action) {
        return this.then(action, new ParserContext());
    }

    public MVELRule then(String action, ParserContext parserContext) {
        if (StrUtil.isNotEmpty(action)) {
            this.actions.add(new MVELAction(action, parserContext));
        }
        return this;
    }

    @Override
    public void before(Facts facts) {
        if (CollUtil.isNotEmpty(prepares)) {
            for (Action action : prepares) {
                action.execute(facts);
            }
        }
    }

    @Override
    public boolean evaluate(Facts facts) {
        return condition.evaluate(facts);
    }

    @Override
    public void execute(Facts facts) {
        if (CollUtil.isNotEmpty(actions)) {
            for (Action action : actions) {
                action.execute(facts);
            }
        }
    }

}
