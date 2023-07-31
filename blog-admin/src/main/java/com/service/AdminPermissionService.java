package com.service;

import com.entity.AdminPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-01-20
 */
public interface AdminPermissionService extends IService<AdminPermission> {

    /*
    * 添加ms_admin_permission关联表
    * */
    void insertByAdminPermissionList(List<AdminPermission> adminPermissionList);
}
