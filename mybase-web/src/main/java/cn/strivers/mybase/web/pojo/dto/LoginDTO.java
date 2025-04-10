package cn.strivers.mybase.web.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登陆基础信息
 *
 * @author mozhu
 * @since 2025/1/2 15:38
 */
@Data
public class LoginDTO implements Serializable {

    private static final long serialVersionUID = -6046866175560563691L;
    /**
     * 客户端信息
     */
    private String clientInfo = "";
    /**
     * 客户端类型
     */
    private Integer clientType = 0;
    /**
     * 租户id
     */
    private Integer tenantId = 0;
    /**
     * 应用id
     */
    private Integer appId = 0;
    /**
     * 版本号
     */
    private Integer version = 0;
    /**
     * 用户id
     */
    private Long userId = 0L;
    /************************************** 参数自行扩展 **************************************************/
}
