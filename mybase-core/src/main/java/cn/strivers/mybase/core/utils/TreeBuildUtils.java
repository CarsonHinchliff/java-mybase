package cn.strivers.mybase.core.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.strivers.mybase.core.exception.ApplicationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 扩展 hutool TreeUtil 封装系统树构建
 *
 * @author mozhu
 * @date 2023-06-13 18:45:06
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class TreeBuildUtils {

    protected static final TreeNodeConfig MY_DEFAULT_CONFIG = TreeNodeConfig.DEFAULT_CONFIG
            .setIdKey("id")
            .setParentIdKey("parentId")
            .setNameKey("nodeName")
            .setWeightKey("nodeWeight");

    /**
     * 构造树
     *
     * @param list       数据
     * @param target     目标类
     * @param nodeParser 节点解析器
     * @return {@link List }<{@link K }>
     */
    public static <T, K> List<K> build(List<T> list, Class<K> target, NodeParser<T, String> nodeParser) {
        List<Tree<String>> treeList = build(list, nodeParser);
        return BeanUtil.copyToList(treeList, target);
    }

    /**
     * 构造树
     *
     * @param list       数据
     * @param nodeParser 节点解析器
     * @param <T>
     * @return
     */
    public static <T> List<Tree<String>> build(List<T> list, NodeParser<T, String> nodeParser) {
        return build(list, nodeParser, null);
    }

    /**
     * 构造树
     *
     * @param list       数据
     * @param nodeParser 节点解析器
     * @param <T>
     * @return
     */
    public static <T> List<Tree<String>> build(List<T> list, NodeParser<T, String> nodeParser, Integer deep) {
        return build(list, "-1", nodeParser, deep);
    }


    /**
     * 构造树
     *
     * @param list       数据
     * @param target     目标
     * @param rootId     根节点
     * @param nodeParser 节点解析器
     * @return {@link List }<{@link K }>
     */
    public static <T, K> List<K> build(List<T> list, Class<K> target, String rootId, NodeParser<T, String> nodeParser) {
        List<Tree<String>> treeList = build(list, rootId, nodeParser);
        return BeanUtil.copyToList(treeList, target);
    }

    /**
     * 构造树
     *
     * @param list       数据
     * @param rootId     根节点
     * @param nodeParser 节点解析器
     * @param <T>
     * @return
     */
    public static <T> List<Tree<String>> build(List<T> list, String rootId, NodeParser<T, String> nodeParser) {
        return build(list, rootId, nodeParser, null);
    }


    /**
     * 构造树
     *
     * @param list       数据
     * @param target     目标
     * @param rootId     根节点
     * @param nodeParser 节点解析器
     * @param deep       深度
     * @return {@link List }<{@link K }>
     */
    public static <T, K> List<K> build(List<T> list, Class<K> target, String rootId, NodeParser<T, String> nodeParser, Integer deep) {
        List<Tree<String>> treeList = build(list, rootId, nodeParser, deep);
        return BeanUtil.copyToList(treeList, target);
    }


    /**
     * 构造树
     *
     * @param list       数据
     * @param rootId     根节点
     * @param nodeParser 节点解析器
     * @param deep       深度
     * @param <T>
     * @return
     */
    public static <T> List<Tree<String>> build(List<T> list, String rootId, NodeParser<T, String> nodeParser, Integer deep) {
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        if (deep != null) {
            MY_DEFAULT_CONFIG.setDeep(deep);
        } else {
            MY_DEFAULT_CONFIG.setDeep(5);
        }
        List<Tree<String>> treeList = TreeUtil.build(list, rootId, MY_DEFAULT_CONFIG, nodeParser);
        if (CollUtil.isEmpty(treeList)) {
            return null;
        }
        for (Tree<String> stringTree : treeList) {
            if (CollUtil.isEmpty(stringTree.getChildren())) {
                stringTree.setChildren(null);
            }
        }
        return treeList;
    }

    /**
     * 设置树的参数
     *
     * @param clazz 类
     * @param tree  树
     * @param <T>   参数类
     */
    public static <T> void putExtra(T clazz, Tree<String> tree) {
        try {
            Field[] fields = clazz.getClass().getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                // 排除序列化属性
                if ("serialVersionUID".equals(fieldName)) {
                    continue;
                }
                //设置允许通过反射访问私有变量
                field.setAccessible(true);
                //获取字段的值
                PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz.getClass());
                // 获取对应属性值
                Method getMethod = pd.getReadMethod();
                Object val = getMethod.invoke(clazz);
                tree.putExtra(fieldName, val);
            }
        } catch (Exception e) {
            log.error("设置参数失败：{}", e.getMessage(), e);
            throw new ApplicationException("设置树参数失败");
        }
    }
}
