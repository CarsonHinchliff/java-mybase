package demo.cn.strivers.mybase.rule.dao;


import demo.cn.strivers.mybase.rule.entity.RuleTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RuleTreeDao extends JpaRepository<RuleTree, Integer> {

    /**
     * 根据组id查询规则树
     *
     * @param groupId
     * @return
     */
    @Query(value = "select * from rule_tree a where a.status=0 and (a.group_id=0 or a.group_id=:groupId)", nativeQuery = true)
    List<RuleTree> findRuleByGroupId(@Param("groupId") Integer groupId);

}
