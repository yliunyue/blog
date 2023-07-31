package com.mapper;

import com.entity.AdminPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dragon
 * @since 2023-01-20
 */
@Mapper
public interface AdminPermissionMapper extends BaseMapper<AdminPermission> {

    /*
    * 添加ms_admin_permission关联表
    * */
    void insertByAdminPermissionList(List<AdminPermission> adminPermissionList);
}
