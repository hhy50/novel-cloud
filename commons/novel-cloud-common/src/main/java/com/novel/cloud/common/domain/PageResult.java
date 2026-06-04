package com.novel.cloud.common.domain;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通用分页结果对象
 *
 * @param <T> 数据条目类型
 */
public class PageResult<T> {

    private final List<T> records;
    private final long total;
    private final long page;
    private final long pageSize;

    public PageResult(List<T> records, long total, long page, long pageSize) {
        this.records = records != null ? records : Collections.emptyList();
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }

    /**
     * 转换条目类型，保留分页信息
     */
    public <R> PageResult<R> map(Function<T, R> mapper) {
        List<R> mapped = this.records.stream()
                .map(mapper)
                .collect(Collectors.toList());
        return new PageResult<>(mapped, this.total, this.page, this.pageSize);
    }

    public List<T> getRecords() {
        return records;
    }

    public long getTotal() {
        return total;
    }

    public long getPage() {
        return page;
    }

    public long getPageSize() {
        return pageSize;
    }

    public long getTotalPages() {
        return pageSize > 0 ? (total + pageSize - 1) / pageSize : 0;
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }
}
