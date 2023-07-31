package com.service;

import com.entity.Admin;
import com.entity.Permission;
import com.utils.AdminThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
* 权限认证
* */
@Service
public class AuthService {
    @Autowired
    private AdminService adminService;

    public boolean auth(HttpServletRequest request, Authentication authentication) {
        //权限认证
        //请求路径
        String requestURI = request.getRequestURI();
        //当前登录用户
        Object principal = authentication.getPrincipal();
        if (principal == null || "anonymousUser".equals(principal)) {
            //未登录，或者是匿名用户
            return false;
        }
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Admin admin=adminService.findAdminByUserName(username);
        if (admin == null) {
            return false;
        }
        //adminID
        Long id = admin.getId();
        //将admin存入到threadlocal中
        AdminThreadLocal.put(admin);
        if (id==1){
            //直接放行，最高权限用户
            return true;
        }
        List<Permission> permissionList=this.adminService.findPermissionByAdminId(id);
        //去掉？后面的参数
        requestURI= StringUtils.split(requestURI,'?')[0];
        for (Permission permission : permissionList) {
            if (requestURI.equals(permission.getPath())){
                //有对应权限
                return true;
            }
        }
        return false;
    }
}
