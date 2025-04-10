package cn.strivers.mybase.rule.core;

import cn.strivers.mybase.rule.api.Action;
import cn.strivers.mybase.rule.api.Condition;
import cn.strivers.mybase.rule.api.Facts;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认规则
 */
@Slf4j
public class DefaultRule extends AbstractBasicRule {

    protected Condition condition = Condition.TRUE;
    protected List<Action> prepares = new ArrayList<>();
    protected List<Action> actions = new ArrayList<>();

    public DefaultRule() {
    }

    public DefaultRule(String name, String description, int priority, Condition condition, List<Action> prepares, List<Action> actions) {
        super(name, description, priority);
        this.condition = condition;
        this.prepares = prepares;
        this.actions = actions;
    }

    @Override
    public void before(Facts facts) {
        for (Action action : prepares) {
            action.execute(facts);
        }
    }

    @Override
    public boolean evaluate(Facts facts) {
        return condition.evaluate(facts);
    }

    @Override
    public void execute(Facts facts) {
        for (Action action : actions) {
            action.execute(facts);
        }
    }

}
