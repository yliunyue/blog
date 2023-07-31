package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Admin;
import com.entity.AdminPermission;
import com.entity.Permission;
import com.mapper.AdminMapper;
import com.service.AdminPermissionService;
import com.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.AdminThreadLocal;
import com.vo.Result;
import com.vo.params.AddAdminParam;
import com.vo.AdminVo;
import com.vo.params.PageParam;
import com.vo.params.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-01-10
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminPermissionService adminPermissionService;

    @Override
    public Admin findAdminByUserName(String userName) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, userName).last("limit 1");
        Admin admin = adminMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public List<Permission> findPermissionByAdminId(Long id) {
        return adminMapper.findPermissionByAdminId(id);
    }

    /*
     * 分页查询后台用户，可以模糊查询
     * */
    @Override
    public Result AdminList(PageParam pageParam) {
        //前端展示的数据是，用户名、用户密码、用户权限（用户权限需要多表查询出来） AdminVo
        IPage<Admin> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getQueryString())) {
            queryWrapper.like(Admin::getUsername, pageParam.getQueryString());
        }
        //用户分页信息
        IPage<Admin> adminIPage = adminMapper.selectPage(page, queryWrapper);
        PageResult<AdminVo> pageResult = new PageResult<>();
        pageResult.setTotal(adminIPage.getTotal());
        //采用流的方式将AdminVo数据展示
        List<AdminVo> adminVoList = adminIPage.getRecords().stream().map(new Function<Admin, AdminVo>() {
            @Override
            public AdminVo apply(Admin admin) {
                AdminVo adminVo = new AdminVo();
                //将admin中的所有参数复制给vo对象
                BeanUtils.copyProperties(admin, adminVo);
                List<String> permissionName = new ArrayList<>();
                List<Permission> permissionList = adminMapper.findPermissionByAdminId(admin.getId());
                for (Permission permission : permissionList) {
                    permissionName.add(permission.getName() + "/");

                }
                adminVo.setPermissions(permissionName);
                return adminVo;
            }
        }).collect(Collectors.toList());
        //adminVo
        pageResult.setList(adminVoList);
        return Result.success(pageResult);
    }

    @Override
    @Transactional
    public Result addAdmin(AddAdminParam addAdminParam) {
        //前端以及做过了密码确认
        //1.将密码加密
        //spring security中的BCryptPasswordEncoder方法采用SHA-256 +随机盐+密钥对密码进行加密。
        String password = new BCryptPasswordEncoder().encode(addAdminParam.getPassword());
        addAdminParam.setPassword(password);
        //2.将其存入到ms_admin表
        Admin admin = new Admin();
        BeanUtils.copyProperties(addAdminParam, admin);
        adminMapper.insert(admin);
        //3.更新权限和用户关联表ms_admin_permission,一个用户对应着多个权限，一对多关系
        //获取用户id，刚刚添加的用户
        Long id = admin.getId();
        List<AdminPermission> adminPermissionList = new ArrayList<>();
        List<Long> permissionIds = addAdminParam.getPermissions();
        for (Long permissionId : permissionIds) {
            adminPermissionList.add(new AdminPermission(null, id, permissionId));
        }
        //批量添加
        adminPermissionService.insertByAdminPermissionList(adminPermissionList);
        return Result.success("添加成功");
    }

    @Override
    @Transactional
    public Result deleteAdminUserById(Long id) {
        //获取当前登录的管理员
        Admin admin = AdminThreadLocal.get();
        if (admin.getId() != 1) {
            return Result.success("对不起，您没有删除管理员的权限");
        }
        if (id == 1) {
            return Result.success("不能删除超级管理员");
        }
        //1.有删除权限，则先删除ms_admin_permission表中的数据,根据admin_id
        //查询表中的id，根据删除的用户admin_id查询
        LambdaQueryWrapper<AdminPermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminPermission::getAdminId,id);
        adminPermissionService.remove(queryWrapper);
        //2.删除admin表，当然不能删除id为1的用户
        adminMapper.deleteById(id);
        return Result.success(null);
    }

    @Override
    public Result updateAdmin(AddAdminParam addAdminParam) {
        //1.将密码加密，再根据id修改admin表
        //spring security中的BCryptPasswordEncoder方法采用SHA-256 +随机盐+密钥对密码进行加密。
        String password = new BCryptPasswordEncoder().encode(addAdminParam.getPassword());
        addAdminParam.setPassword(password);
        // 根据id对内容进行修改
        Admin admin = new Admin();
        BeanUtils.copyProperties(addAdminParam,admin);
        adminMapper.updateById(admin);
        //2.先删除原来的关联表中的内容，ms_admin_permission,根据admin_id
        LambdaQueryWrapper<AdminPermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminPermission::getAdminId,admin.getId());
        adminPermissionService.remove(queryWrapper);
        //3.更新权限和用户关联表ms_admin_permission,一个用户对应着多个权限，一对多关系
        //获取用户id，刚刚添加的用户
        Long id = admin.getId();
        List<AdminPermission> adminPermissionList = new ArrayList<>();
        List<Long> permissionIds = addAdminParam.getPermissions();
        for (Long permissionId : permissionIds) {
            adminPermissionList.add(new AdminPermission(null, id, permissionId));
        }
        //批量添加
        adminPermissionService.insertByAdminPermissionList(adminPermissionList);
        return Result.success("修改成功");
    }
}
