package com.controller;

import com.entity.Permission;
import com.service.AdminService;
import com.service.PermissionService;
import com.vo.Result;
import com.vo.params.AddAdminParam;
import com.vo.params.PageParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dragon
 * @since 2023-01-10
 */
@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AdminService adminService;
    /*
    * 权限列表
    * */
    @PostMapping("/permission/permissionList")
    public Result listPermission(@RequestBody PageParam pageParam) {
        return permissionService.listPermission(pageParam);
    }
    /*
    * 添加权限
    * */
    @PostMapping("/permission/add")
    public Result addPermission(@RequestBody Permission permission) {
        return permissionService.addPermission(permission);
    }
    /*
    * 删除权限
    * */
    @GetMapping("/permission/delete/{id}")
    public Result deletePermission(@PathVariable("id")Long id) {
        return permissionService.deletePermission(id);
    }
    /*
    * 修改权限
    * */
    @PostMapping("/permission/update")
    public Result updatePermission(@RequestBody Permission permission) {
        return permissionService.updatePermission(permission);
    }
    /*
    * 后台用户列表
    * */
    @PostMapping("/adminList")
    public Result AdminList(@RequestBody PageParam pageParam){
        return  adminService.AdminList(pageParam);
    }

    /*
    * 添加管理员
    * */
    @PostMapping("/addAdmin")
    public Result addAdmin(@RequestBody AddAdminParam addAdminParam){
        log.info("数据：{}",addAdminParam.toString());
        return adminService.addAdmin(addAdminParam);
    }
    /*
    * 删除管理员（只允许id为1的admin用户删除其他用户）
    * */
    @DeleteMapping("/deleteAdminUser/{id}")
    public Result deleteAdminUserById(@PathVariable("id") Long id){
        //只有当前登录用户id为1的admin超级管理员才可以删除用户、
        log.info("数据：{}",id);
        return adminService.deleteAdminUserById(id);
    }

    /*
    * 修改管理员
    * */
    @PutMapping("/updateAdmin")
    public Result updateAdmin(@RequestBody AddAdminParam addAdminParam){
        log.info("数据：{}",addAdminParam.toString());
        return adminService.updateAdmin(addAdminParam);
    }
}
