
CREATE TABLE `task_cron` (
  `task_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `task_name` varchar(30) DEFAULT NULL COMMENT '任务名称',
  `cron` varchar(50) DEFAULT NULL COMMENT '执行计划',
  `url` varchar(100) DEFAULT NULL COMMENT '请求地址',
  `param` varchar(100) DEFAULT NULL COMMENT '请求参数',
  `status` int(1) DEFAULT '0' COMMENT '状态：0未启用1启用',
  `remark` varchar(100) DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='定时任务配置表';

