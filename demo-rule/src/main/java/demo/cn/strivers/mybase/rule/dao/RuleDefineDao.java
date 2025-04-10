package demo.cn.strivers.mybase.rule.dao;


import demo.cn.strivers.mybase.rule.entity.RuleDefine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RuleDefineDao extends JpaRepository<RuleDefine, Integer> {

    /**
     * 根据组id查询规则定义
     *
     * @param groupId
     * @return
     */
    @Query(value = "select * from rule_define a where a.status=0 and (a.group_id=0 or a.group_id=:groupId)", nativeQuery = true)
    List<RuleDefine> findRuleByGroupId(@Param("groupId") Integer groupId);

}
