package com.vo;

import lombok.Data;

import java.util.List;

@Data
public class AdminVo {
    private Long id;

    private String username;

    private String password;

    private List<String> permissions;

}
