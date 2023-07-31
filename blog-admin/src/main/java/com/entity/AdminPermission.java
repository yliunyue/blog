package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminPermission {
    private Long id;
    private Long adminId;
    private Long permissionId;
}
