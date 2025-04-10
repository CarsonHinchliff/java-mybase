package cn.strivers.mybase.db.em;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.map.MapUtil;
import org.hibernate.transform.AliasToBeanResultTransformer;

import java.util.Map;

/**
 * jpa 使用 EntityManager 查询时转换为实体
 * ResultTransformer Created by liteng.
 *
 * @author liteng
 * @date 2019/03/26 17:06
 */
public class ResultTransformer extends AliasToBeanResultTransformer {

    private Class resultClass;

    public static ResultTransformer of(Class resultClass) {
        return new ResultTransformer(resultClass);
    }

    public ResultTransformer(Class resultClass) {
        super(resultClass);
        this.resultClass = resultClass;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Map<String, Object> map = MapUtil.newHashMap();
        for (int i = 0; i < aliases.length; i++) {
            map.put(aliases[i], tuple[i]);
        }
        return BeanUtil.mapToBean(map, resultClass, CopyOptions.create().setIgnoreNullValue(true));
    }
}
