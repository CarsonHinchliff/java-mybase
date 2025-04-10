package cn.strivers.mybase.db.pages;


/**
 * 分页接口
 */
public interface Paginable {
    /**
     * 获取总条数
     *
     * @return
     */
    int getTotalCount();

    /**
     * 获取页码总数
     *
     * @return
     */
    int getTotalPage();

    /**
     * 传入条数
     *
     * @return
     */
    int getPageSize();

    /**
     * 传入页码
     *
     * @return
     */
    int getPageNo();

    /**
     * 首页
     *
     * @return
     */
    boolean isFirstPage();

    /**
     * 最后一页
     *
     * @return
     */
    boolean isLastPage();

    /**
     * 下一页
     *
     * @return
     */
    int getNextPage();

    /**
     * 之前页面
     *
     * @return
     */
    int getPrePage();
}
