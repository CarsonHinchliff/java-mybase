package demo.cn.strivers.mybase.rule.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "rule_group")
public class RuleGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 组名称
     */
    private String groupName;
    /**
     * 异常时默认结果：0：false 1：true
     */
    private Integer resultOnException;
    /**
     * 0.启用 1.禁用 默认0
     */
    private Integer status;
    /**
     * 描述
     */
    private String desc;
}
