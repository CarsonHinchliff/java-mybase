package demo.cn.strivers.mybase.rule.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "rule_tree")
public class RuleTree {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	/**
	 * 规则id
	 */
	private Integer ruleId;
	/**
	 * 父规则id
	 */
	private Integer parentRuleId;
	/**
	 * 组id
	 */
	private Integer groupId;
	/**
	 * 0.启用 1.禁用 默认0
	 */
	private Integer status;
}
