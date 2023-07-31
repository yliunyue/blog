package com.mapper;

import com.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.AdminPermission;
import com.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dragon
 * @since 2023-01-10
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    /*
    * 根据用户id查找用户权限id
    * */
    List<Permission> findPermissionByAdminId(Long id);


}
