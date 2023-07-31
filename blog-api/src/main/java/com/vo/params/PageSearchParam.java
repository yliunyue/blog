package com.vo.params;

import lombok.Data;

@Data
public class PageSearchParam {
    private int page = 1;

    private int pageSize = 10;

    private String queryString;
}
