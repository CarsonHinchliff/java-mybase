package cn.strivers.mybase.task.dao.impl;

import cn.strivers.mybase.task.dao.TaskDao;
import cn.strivers.mybase.task.model.TaskCron;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

/**
 * @author ：ypp
 * @date ：Created in 15:18 2019/7/1
 * @Description : 定时任务实现类
 * @Modified By：
 * @Version : 1.0$
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskDaoImpl implements TaskDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TaskCron> findByOneTenant() {
        String sql = "SELECT task_id, task_name, cron, url, param, status, remark,'0' tenant_id FROM task_cron where status = 1";
        return entityManager.createNativeQuery(sql, TaskCron.class).getResultList();
    }

    @Override
    public List<TaskCron> findByAllTenant(Map<Integer, String> tenant) {
        StringBuilder sql = new StringBuilder();
        for (Map.Entry<Integer, String> map : tenant.entrySet()) {
            sql.append(" UNION ALL SELECT task_id, task_name, cron, url, param, status, remark,'").append(map.getKey()).append("' tenant_id FROM ").append(map.getValue()).append(".task_cron WHERE status = 1");
        }
        return entityManager.createNativeQuery(sql.toString().substring(10), TaskCron.class).getResultList();
    }
}
