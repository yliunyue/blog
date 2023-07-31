package com.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.SysUser;
import com.service.SysUserService;
import com.vo.Result;
import com.vo.params.PageParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2023-01-20
 */
@RestController
@RequestMapping("/sysUser")
@Slf4j
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    /*
    * 用户列表（可查询）
    * */
    @PostMapping("/userList")
    private Result userList(@RequestBody PageParam pageParam){
        return sysUserService.userList(pageParam);
    }

    /*
    * 删除用户
    * */
    @DeleteMapping("/deleteUser/{id}")
    private Result deleteUser(@PathVariable("id") Long id){
        log.info("数据：{}",id);
        return sysUserService.deleteUserById(id);
    }

    /*
    * 启用用户
    * */
    @PostMapping("/notDeleteUser/{id}")
    private Result notDeleteUser(@PathVariable("id") Long id){
        log.info("数据：{}",id);
        return sysUserService.notDeleteUser(id);
    }
}
