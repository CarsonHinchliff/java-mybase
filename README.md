# mybase

#### 介绍
基于hutool、springboot、springcloud等组件封装，结合日常开发使用进行不断完善的全架构解决方案框架

#### 框架结构及原理
1、mybase中包含组件如下：

   mybase-core：基础核心包
   
   mybase-db：基于jpa封装的数据库操作，支持schema多租户模式切换
   
   mybase-web：web微服务封装，默认包含web相关基础配置
   
   mybase-task：基于crontab对定时任务调度封装
   
   mybase-redis：基于redis对缓存相关操作封装
   
   mybase-rule：借鉴easyrule的设计思维，结合自身业务情况进行重写封装的规则引擎
   
   --------------------------------------
   
   demo-configserver: 配置中心使用样例
      
   demo-gateway: 业务网关使用样例
   
   demo-rule: 规则引擎使用样例
   
   demo-task: 定时任务使用样例
   
   demo-web: web工程使用样例
   
2、所有请求经过gateway转发至对应微服务，由gateway完成权限校验和加解密动作。




#### 使用说明

1、将工程父类至为mybase，如下：
    <parent>
        <artifactId>mybase</artifactId>
        <groupId>cn.strivers</groupId>
        <version>x.x.x</version>
    </parent>
  则工程已经默认对springboot，springcloud进行了继承和定义依赖，具体版本可参考总pom.xml
  
2、需要哪个功能，则只需要再引入对应需要的子模块即可，mybase的父pom中已经定义了子模块相关的所有依赖