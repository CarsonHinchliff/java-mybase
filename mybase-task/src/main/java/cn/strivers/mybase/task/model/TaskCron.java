package cn.strivers.mybase.task.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ：ypp
 * @date ：Created in 15:13 2019/7/1
 * @Description : 定时任务实体
 * @Modified By：
 * @Version : 1.0$
 */
@Entity
@Data
@Table(name="task_cron")
public class TaskCron {

    @Id
    @GeneratedValue
    /**
     * task_id
     */
    private Integer taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 执行计划
     */
    private String cron;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求参数
     */
    private String param;

    /**
     * 状态：0未启用1启用
     */
    private Integer status;

    /**
     * 说明
     */
    private String remark;

    /**
     * 租户id
     */
    private String tenantId;

}
