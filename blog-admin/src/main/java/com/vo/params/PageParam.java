package com.vo.params;

import lombok.Data;

@Data
public class PageParam {
    private Integer currentPage;

    private Integer pageSize;

    private long total;

    private String queryString;
}
