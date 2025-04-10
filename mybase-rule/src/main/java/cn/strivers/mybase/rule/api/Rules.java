package cn.strivers.mybase.rule.api;

import java.util.*;

/**
 * 规则组
 */
public class Rules implements Iterable<Rule> {

    public Set<Rule> rules = new TreeSet<>();

    public Rules(Set<Rule> rules) {
        this.rules = new TreeSet<>(rules);
    }

    public Rules(Rule... rules) {
        Collections.addAll(this.rules, rules);
    }

    public Rules(Object... rules) {
        for (Object rule : rules) {
            this.register(asRule(rule));
        }
    }

    public void register(Object rule) {
        Objects.requireNonNull(rule);
        rules.add(asRule(rule));
    }

    public void unregister(Object rule) {
        Objects.requireNonNull(rule);
        rules.remove(asRule(rule));
    }

    public void unregister(final String ruleName) {
        Objects.requireNonNull(ruleName);
        Rule rule = findRuleByName(ruleName);
        if (rule != null) {
            unregister(rule);
        }
    }

    public boolean isEmpty() {
        return rules.isEmpty();
    }

    public void clear() {
        rules.clear();
    }

    @Override
    public Iterator<Rule> iterator() {
        return rules.iterator();
    }

    private Rule findRuleByName(String ruleName) {
        for (Rule rule : rules) {
            if (rule.getName().equalsIgnoreCase(ruleName)) {
                return rule;
            }
        }
        return null;
    }

    public static Rule asRule(final Object rule) {
        Rule result;
        if (rule instanceof Rule) {
            result = (Rule) rule;
        } else {
            throw new IllegalArgumentException("Invalid rule!!!");
        }
        return result;
    }
}
