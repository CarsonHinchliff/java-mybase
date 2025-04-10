package cn.strivers.mybase.db.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页
 */
public class Pagination<T> extends SimplePage implements Serializable, Paginable {

    private static final long serialVersionUID = 1L;

    private List<T> list = new ArrayList<>();

    public Pagination() {
    }

    public Pagination(int pageSize) {
        super(pageSize);
    }

    public Pagination(int pageNo, int pageSize, int totalCount) {
        super(pageNo, pageSize, totalCount);
    }

    public Pagination(int pageNo, int pageSize, int totalCount, List<T> list) {
        super(pageNo, pageSize, totalCount);
        this.list = list;
    }

    public int getFirstResult() {
        return (this.pageNo - 1) * this.pageSize;
    }

    public List<T> list() {
        return this.list;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
