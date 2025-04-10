package cn.strivers.mybase.web.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 生成折线/组装图等
 *
 * @author mozhu
 * @date 2024/3/22 11:28
 */
@Data
@Accessors(chain = true)
public class StatisticsReportBaseDTO implements Serializable {

    private static final long serialVersionUID = 3993070830049683510L;

    /**
     * 日期
     */
    private String reportDate;

    /**
     * 数量
     */
    private Integer reportNum = 0;

}
