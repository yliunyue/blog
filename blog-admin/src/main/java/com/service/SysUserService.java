package com.service;

import com.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;
import com.vo.params.PageParam;
import com.vo.UserVo;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-01-20
 */
public interface SysUserService extends IService<SysUser> {

    /*
    * 用户列表
    * */
    Result userList(PageParam pageParam);

    /*
    * 根据用户id删除用户
    * */
    Result deleteUserById(Long id);

    /*
    * 启用用户
    * */
    Result notDeleteUser(Long id);

    UserVo findByAuthorId(Long authorId);

    /*
    * 根据作者名字模糊查询，返回集合
    * */
    List<Long> selectByAuthorName(String queryString);
}
