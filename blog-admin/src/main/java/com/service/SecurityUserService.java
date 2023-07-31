package com.service;

import com.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SecurityUserService implements UserDetailsService {
    @Autowired
    private AdminService adminService;
    /*
    * 当登录的时候，会把userName传递到这里
    * 通过username查询admin表，如果admin表存在 将密码告诉spring security
    * 如果不存在，返回null 认证失败
    * */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Admin admin=adminService.findAdminByUserName(userName);
        if (admin == null) {
            //登录失败
            return null;
        }
        //第三个：认证授权列表
        UserDetails userDetails=new User(userName,admin.getPassword(),new ArrayList<>());
        return userDetails;
    }
}
