package com.service;

import com.dos.SysUserMessage;
import com.pojo.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;
import com.vo.params.UpdataEmailParam;
import com.vo.params.UserVo;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-01-06
 */

public interface SysUserService extends IService<SysUser> {
    SysUser findByIdSysUser(Long id);

    /*
    * 根据账号密码查找用户
    * */
    SysUser findUser(String account, String password);

    /*
    * 根据token查询用户信息
    * */
    Result findUserByToken(String token);

    /*
    * 根据账户查找用户
    * */
    SysUser finduserByAccount(String account);

    /*
    *
    * */
    UserVo findUserVoById(Long id);

    Result findSysUserMessage();

    //验证验证码是否周期
    Result isCode(String code);

    Result updataEmail(String code,String email);

    Result updataMes(String avatar, String nickname,String token);


    // SysUserMessage findSysUserMessage();
}
