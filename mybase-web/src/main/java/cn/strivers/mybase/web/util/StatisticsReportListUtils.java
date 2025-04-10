package cn.strivers.mybase.web.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.strivers.mybase.web.pojo.dto.StatisticsReportBaseDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 列表时间补0 util 用于生成折线图、柱状图等
 *
 * @author mozhu
 * @date 2024/3/22 11:22
 */
public class StatisticsReportListUtils {

    /**
     * 日期补0
     *
     * @param sourceList 源数据
     * @param startTime  时间开始
     * @param endTime    时间结束
     * @param isMonth    是否为月 -1 默认0 返回天
     * @return
     */
    public static List<StatisticsReportBaseDTO> getReportList(List<StatisticsReportBaseDTO> sourceList, Date startTime, Date endTime, Integer isMonth) {
        List<DateTime> dates = null;
        if (ObjUtil.equal(isMonth, 1)) {
            dates = DateUtil.rangeToList(startTime, endTime, DateField.MONTH);
        } else {
            dates = DateUtil.rangeToList(startTime, endTime, DateField.DAY_OF_YEAR);
        }
        Map<String, StatisticsReportBaseDTO> map = sourceList.stream().collect(Collectors.toMap(StatisticsReportBaseDTO::getReportDate, Function.identity()));
        List<StatisticsReportBaseDTO> list = new ArrayList<>();
        dates.forEach(date -> {
            String formatTime = "";
            if (ObjUtil.equal(isMonth, 1)) {
                formatTime = DateUtil.format(date, DatePattern.NORM_MONTH_PATTERN);
            } else {
                formatTime = DateUtil.format(date, DatePattern.NORM_DATE_PATTERN);
            }
            if (map.containsKey(formatTime)) {
                list.add(map.get(formatTime));
            } else {
                // 没有这一天的数据，默认补0
                list.add(new StatisticsReportBaseDTO().setReportDate(formatTime));
            }
        });
        return list;
    }

}
