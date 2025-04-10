package cn.strivers.mybase.db.em;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.strivers.mybase.db.pages.Pagination;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SqlUtil Created by liteng.
 *
 * @author liteng
 * @date 2019/03/26 14:47
 */
public class SqlUtil {

    /**
     * 执行sql
     *
     * @param sql    要执行的语句
     * @param params 参数
     * @param clazz  返回类型
     * @param em     entityManager
     * @param <T>    泛型
     * @return 结果集
     */
    public static <T> List<T> executeSql(String sql, Map<String, Object> params, Class<T> clazz, EntityManager em) {
        Session session = em.unwrap(Session.class);
        NativeQuery query = session.createSQLQuery(sql);
        assemblyQuery(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(ResultTransformer.of(clazz));
        return query.getResultList();
    }

    /**
     * 执行sql
     *
     * @param sql    要执行的语句
     * @param params 参数
     * @param em     entityManager
     * @return 结果集
     */
    public static List<Map> executeSql(String sql, Map<String, Object> params, EntityManager em) {
        Session session = em.unwrap(Session.class);
        NativeQuery query = session.createSQLQuery(sql);
        assemblyQuery(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }


    /**
     * 执行sql分页
     *
     * @param sql
     * @param countSql
     * @param params
     * @param pagination
     * @param clazz
     * @param em
     * @param <T>
     * @return
     */
    public static <T> Pagination executeSqlPage(String sql, String countSql, Map<String, Object> params, Pagination pagination, Class<T> clazz, EntityManager em) {
        NativeQuery query = getNativeQuery(sql, countSql, params, pagination, em);
        if (query == null) {
            return pagination;
        }
        query.unwrap(NativeQueryImpl.class).setResultTransformer(ResultTransformer.of(clazz));
        pagination.setList(query.getResultList());
        return pagination;
    }

    /**
     * 执行sql分页
     *
     * @param sql
     * @param countSql
     * @param params
     * @param pagination
     * @param em
     * @return
     */
    public static Pagination executeSqlPage(String sql, String countSql, Map<String, Object> params, Pagination pagination, EntityManager em) {
        NativeQuery query = getNativeQuery(sql, countSql, params, pagination, em);
        if (query == null) {
            return pagination;
        }
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        pagination.setList(query.getResultList());
        return pagination;
    }

    /**
     * 执行分页sql
     *
     * @param sql
     * @param countSql
     * @param params
     * @param pagination
     * @param em
     * @return
     */
    private static NativeQuery getNativeQuery(String sql, String countSql, Map<String, Object> params, Pagination pagination, EntityManager em) {
        Session session = em.unwrap(Session.class);
        NativeQuery countQuery = session.createSQLQuery(countSql);
        assemblyQuery(countQuery, params);
        int count = Convert.toInt(countQuery.getSingleResult(), 0);
        if (count == 0) {
            return null;
        }
        pagination.setTotalCount(count);
        NativeQuery query = session.createSQLQuery(sql);
        assemblyQuery(query, params);
        query.setFirstResult((pagination.getPageNo() - 1) * pagination.getPageSize());
        query.setMaxResults(pagination.getPageSize());
        return query;
    }

    /**
     * 为Query填充参数
     *
     * @param query  query
     * @param params 参数
     */
    private static void assemblyQuery(Query query, Map<String, Object> params) {
        if (CollUtil.isEmpty(params)) {
            return;
        }
        Set<Parameter<?>> cols = query.getParameters();
        List<String> list = cols.stream().map(Parameter::getName).collect(Collectors.toList());
        //把params中的每一个元素放入entry
        params.forEach((k, v) -> {
            //忽略params中无效的参数
            if (!list.contains(k)) {
                return;
            }
            query.setParameter(k, v);
        });
    }


}
