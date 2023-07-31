package com.service;

import com.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.Permission;
import com.vo.Result;
import com.vo.params.AddAdminParam;
import com.vo.params.PageParam;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-01-10
 */
public interface AdminService extends IService<Admin> {

    /*
    * 根据用户名查询
    * */
    Admin findAdminByUserName(String userName);

    /*
    * 根据用户id查找用户权限
    * */
    List<Permission> findPermissionByAdminId(Long id);

    /*
    * 分页查询后台用户，可以模糊查询
    * */
    Result AdminList(PageParam pageParam);


    /*
    * 添加管理员用户
    * */
    Result addAdmin(AddAdminParam addAdminParam);

    /*
    * 超级用户根据用户id删除用户
    * */
    Result deleteAdminUserById(Long id);

    /*
    * 修改
    * */
    Result updateAdmin(AddAdminParam addAdminParam);
}
