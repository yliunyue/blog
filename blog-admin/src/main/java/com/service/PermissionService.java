package com.service;

import com.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;
import com.vo.params.PageParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-01-10
 */
public interface PermissionService extends IService<Permission> {

    /*
    * 权限列表
    * */
    Result listPermission(PageParam pageParam);

    /*
    * 添加权限
    * */
    Result addPermission(Permission permission);

    /*
    * 删除权限
    * */
    Result deletePermission(Long id);

    /*
    * 修改权限
    * */
    Result updatePermission(Permission permission);


    /*
    * 查找所有权限
    * */
    Result permissionList();
}
