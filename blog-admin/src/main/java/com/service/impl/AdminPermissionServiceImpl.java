package com.service.impl;

import com.entity.AdminPermission;
import com.mapper.AdminPermissionMapper;
import com.service.AdminPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-01-20
 */
@Service
public class AdminPermissionServiceImpl extends ServiceImpl<AdminPermissionMapper, AdminPermission> implements AdminPermissionService {

    @Autowired
    private AdminPermissionMapper adminPermissionMapper;
    @Override
    public void insertByAdminPermissionList(List<AdminPermission> adminPermissionList) {
        adminPermissionMapper.insertByAdminPermissionList(adminPermissionList);
    }
}
