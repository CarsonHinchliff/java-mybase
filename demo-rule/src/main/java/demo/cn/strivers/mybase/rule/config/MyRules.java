package demo.cn.strivers.mybase.rule.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import cn.strivers.mybase.core.utils.StrUtil;
import cn.strivers.mybase.rule.api.Rule;
import cn.strivers.mybase.rule.api.Rules;
import cn.strivers.mybase.rule.core.RulesEngineParameters;
import cn.strivers.mybase.rule.mvel.MVELRule;
import cn.strivers.mybase.rule.support.AbstractCompositeRule;
import cn.strivers.mybase.rule.support.ParallelRuleGroup;
import cn.strivers.mybase.rule.support.SerialRuleGroup;
import demo.cn.strivers.mybase.rule.dao.RuleDefineDao;
import demo.cn.strivers.mybase.rule.dao.RuleGroupDao;
import demo.cn.strivers.mybase.rule.dao.RuleTreeDao;
import demo.cn.strivers.mybase.rule.entity.RuleDefine;
import demo.cn.strivers.mybase.rule.entity.RuleGroup;
import demo.cn.strivers.mybase.rule.entity.RuleTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class MyRules {

    @Autowired
    private RuleDefineDao ruleDefineDao;
    @Autowired
    private RuleTreeDao ruleTreeDao;
    @Autowired
    private RuleGroupDao ruleGroupDao;

    /**
     * 加载规则
     *
     * @param groupId
     * @return
     */
    public Rules buildGroupRules(Integer groupId) {
        //获取该组所有启用规则
        List<RuleDefine> ruleDefList = ruleDefineDao.findRuleByGroupId(groupId);
        log.info("规则组：{}，找到规则{}条", groupId, ruleDefList.size());
        if (CollUtil.isEmpty(ruleDefList)) {
            return new Rules();
        }
        Map<Integer, RuleDefine> ruleDefMap = new HashMap<>();
        for (RuleDefine ruleDefine : ruleDefList) {
            ruleDefMap.put(ruleDefine.getId(), ruleDefine);
        }
        //整理组合关系
        Map<Integer, List<Integer>> ruleTreeMap = new HashMap<>();
        List<RuleTree> ruleTreeList = ruleTreeDao.findRuleByGroupId(groupId);
        for (RuleTree ruleTree : ruleTreeList) {
            int parentRuleId = ruleTree.getParentRuleId();
            List<Integer> ruleList = ruleTreeMap.get(parentRuleId);
            if (CollUtil.isEmpty(ruleList)) {
                ruleList = new ArrayList<>();
            }
            ruleList.add(ruleTree.getRuleId());
            ruleTreeMap.put(parentRuleId, ruleList);
        }
        //构建规则
        Rules rules = new Rules();
        List<Integer> rootRuleList = ruleTreeMap.get(0);
        for (Integer ruleId : rootRuleList) {
            RuleDefine ruleDef = ruleDefMap.get(ruleId);
            if (BeanUtil.isEmpty(ruleDef)) {
                continue;
            }
            Rule rule = buildRule(ruleDef, ruleDefMap, ruleTreeMap);
            if (!BeanUtil.isEmpty(ruleDef)) {
                rules.register(rule);
            }
        }
        return rules;
    }

    /**
     * 初始化引擎
     *
     * @param ruleGroup
     * @return
     */
    public RulesEngineParameters buildEngineParameters(RuleGroup ruleGroup) {
        RulesEngineParameters rulesEngineParameters = new RulesEngineParameters();
        if (ruleGroup.getResultOnException() > 0) {
            rulesEngineParameters.setResultOnException(true);
        }
        return rulesEngineParameters;
    }

    /**
     * 加载所有规则组
     *
     * @return
     */
    public List<RuleGroup> findAllGroup() {
        RuleGroup ruleGroupExample = new RuleGroup();
        ruleGroupExample.setStatus(0);
        return ruleGroupDao.findAll(Example.of(ruleGroupExample));
    }

    /**
     * 构建规则
     *
     * @param ruleDef
     * @param ruleDefMap
     * @param ruleCompositeMap
     * @return
     */
    private Rule buildRule(RuleDefine ruleDef, Map<Integer, RuleDefine> ruleDefMap, Map<Integer, List<Integer>> ruleCompositeMap) {
        String ruleName = ruleDef.getId() + "-" + ruleDef.getRuleName();
        //0.原子条件;1.串行;2.并行
        if (ruleDef.getCompositeType().equals(0)) {
            if (StrUtil.isNotBlank(ruleDef.getCond())) {
                Rule rule = new MVELRule().name(ruleName).priority(ruleDef.getPriority()).description(ruleDef.getDesc()).prepare(ruleDef.getPrepare()).when(ruleDef.getCond()).then(ruleDef.getAction());
                rule.setRuleDefine(BeanUtil.beanToMap(ruleDef));
                return rule;
            } else {
                log.info("原则规则[{}]，cond为空跳过！！！", ruleDef.getRuleName());
                return null;
            }
        } else {
            AbstractCompositeRule compositeRule = null;
            int compositeType = ruleDef.getCompositeType();
            if (compositeType == 2) {
                compositeRule = new ParallelRuleGroup(ruleName);
            } else {
                compositeRule = new SerialRuleGroup(ruleName);
            }
            compositeRule.setPriority(ruleDef.getPriority());
            compositeRule.setDescription(ruleDef.getDesc());
            compositeRule.setRuleDefine(BeanUtil.beanToMap(ruleDef));
            compositeRule.prepare(ruleDef.getPrepare());
            compositeRule.when(ruleDef.getCond());
            compositeRule.then(ruleDef.getAction());
            List<Integer> ruleList = ruleCompositeMap.get(ruleDef.getId());
            if (CollUtil.isEmpty(ruleList)) {
                log.info("组合规则[{}]，未找到定义跳过！！！", ruleDef.getRuleName());
                return null;
            }
            for (Integer ruleId : ruleList) {
                Rule rule = buildRule(ruleDefMap.get(ruleId), ruleDefMap, ruleCompositeMap);
                if (rule != null) {
                    compositeRule.addRule(rule);
                }
            }
            return compositeRule;
        }
    }

    /**
     * 获取规则json串
     */
    public String getRuleJson(int groupId) {
        log.info("获取规则组串：{}", groupId);
        Rules rules = buildGroupRules(groupId);
        StringBuilder sb = new StringBuilder();
        showTreeSet(sb, rules.rules);
        return sb.toString();
    }

    /**
     * 拼接规则json串
     *
     * @param sb
     * @param treeSet
     */
    private void showTreeSet(StringBuilder sb, Set<Rule> treeSet) {
        Rule rule;
        AbstractCompositeRule compRule;
        sb.append('[');
        Iterator<Rule> iter = treeSet.iterator();
        int n = 1;
        while (iter.hasNext()) {
            if (n != 1) {
                sb.append(',');
            }
            n++;
            rule = iter.next();
            Integer compositeType = Convert.toInt(rule.getRuleDefine().get("compositeType"));
            if (compositeType == 0) {
                sb.append("{\"def\":");
                sb.append(JSONUtil.toJsonStr(rule.getRuleDefine()));
                sb.append(",\"child\":\"\"}");
            } else {
                compRule = (AbstractCompositeRule) rule;
                sb.append("{\"def\":");
                sb.append(JSONUtil.toJsonStr(compRule.getRuleDefine()));
                sb.append(",\"child\":");
                showTreeSet(sb, compRule.rules);
                sb.append('}');
            }
        }
        sb.append(']');
    }
}
