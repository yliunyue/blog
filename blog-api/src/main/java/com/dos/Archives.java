package com.dos;

import lombok.Data;
//dos里的对象：也是数据库中查询的对象，但是不需要持久化的对象。

/*
* 档案，首月的归档对象
* */
@Data
public class Archives {
    private Integer year;
    private Integer month;
    private Long count;

}
