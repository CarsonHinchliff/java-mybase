package demo.cn.strivers.mybase.rule.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "rule_define")
public class RuleDefine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * 条件
     */
    private String cond;
    /**
     * 规则前置准备动作
     */
    private String prepare;
    /**
     * 动作
     */
    private String action;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 描述
     */
    private String desc;
    /**
     * 组合类型:0.原子条件;1:串行；2:并行
     */
    private Integer compositeType;
    /**
     * 状态: 0启用 1禁用 默认启用
     */
    private Integer status;
    /**
     * 组ID
     */
    private Integer groupId;
}
