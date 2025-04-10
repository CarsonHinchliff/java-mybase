package cn.strivers.mybase.db.pages;

/**
 * 简单分页
 */
public class SimplePage implements Paginable {

    /**
     * 默认查询条数
     */
    private static final int DEF_COUNT = 15;
    /**
     * 默认总数
     */
    private static final int DEF_TOTAL_COUNT = 0;
    /**
     * 默认页数
     */
    private static final int DEF_PAGE_NO = 1;
    int pageNo = DEF_PAGE_NO;
    int pageSize = DEF_COUNT;
    private int totalCount = DEF_TOTAL_COUNT;

    public SimplePage() {
    }

    public SimplePage(int pageNo, int pageSize, int totalCount) {
        this.totalCount = Math.max(totalCount, DEF_TOTAL_COUNT);
        this.pageSize = pageSize <= 0 ? DEF_COUNT : pageSize;
        this.pageNo = pageNo <= 0 ? DEF_PAGE_NO : pageNo;
        if ((this.pageNo - 1) * this.pageSize >= totalCount) {
            this.pageNo = (totalCount / pageSize);
        }
    }

    public SimplePage(int pageSize) {
        this(DEF_PAGE_NO, pageSize, DEF_TOTAL_COUNT);
    }

    @Override
    public int getPageNo() {
        return this.pageNo;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public int getTotalCount() {
        return this.totalCount;
    }

    @Override
    public int getTotalPage() {
        int totalPage = this.totalCount / this.pageSize;
        if ((this.totalCount % this.pageSize != 0) || (totalPage == 0)) {
            totalPage++;
        }
        return totalPage;
    }

    @Override
    public boolean isFirstPage() {
        return this.pageNo <= 1;
    }

    @Override
    public boolean isLastPage() {
        return this.pageNo >= getTotalPage();
    }

    @Override
    public int getNextPage() {
        if (isLastPage()) {
            return this.pageNo;
        }
        return this.pageNo + 1;
    }

    @Override
    public int getPrePage() {
        if (isFirstPage()) {
            return this.pageNo;
        }
        return this.pageNo - 1;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
