package com.vo.params;

import lombok.Data;

import java.util.List;

@Data
public class AddAdminParam {
    private Long id;

    private String username;

    private String password;

    private List<Long> permissions;
}
