package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Permission;
import com.mapper.PermissionMapper;
import com.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vo.Result;
import com.vo.params.PageParam;
import com.vo.params.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-01-10
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    /*
     * 权限列表
     * */
    @Override
    public Result listPermission(PageParam pageParam) {
        //需要的数据， 表的所有字段 Permission
        //分页查询，需要的是 total（总条数） 和 list---->PageResult
        IPage<Permission> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getQueryString())) {
            queryWrapper.like(Permission::getName, pageParam.getQueryString());
        }
        IPage<Permission> permissionIPage = permissionMapper.selectPage(page, queryWrapper);
        PageResult<Permission> pageResult = new PageResult<>();
        pageResult.setTotal(permissionIPage.getTotal());
        pageResult.setList(permissionIPage.getRecords());
        return Result.success(pageResult);
    }

    /*
     * 添加权限
     * */
    @Override
    public Result addPermission(Permission permission) {
        permissionMapper.insert(permission);
        return Result.success(null);
    }

    /*
     * 删除权限
     * */
    @Override
    public Result deletePermission(Long id) {
        permissionMapper.deleteById(id);
        return Result.success(null);
    }

    /*
     * 修改权限
     * */
    @Override
    public Result updatePermission(Permission permission) {
        permissionMapper.updateById(permission);
        return Result.success(null);
    }

    /*
     * 查找所有权限
     * */
    @Override
    public Result permissionList() {
        List<Permission> permissionList = permissionMapper.selectList(new LambdaUpdateWrapper<>());
        return Result.success(permissionList);
    }
}
