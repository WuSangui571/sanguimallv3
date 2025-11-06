package com.sangui.sanguimall.admin.manager;


import com.sangui.sanguimall.admin.model.query.SysUserQuery;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-04
 * @Description:
 * @Version: 1.0
 */
public interface SysUserManager {
    int addUser(SysUserQuery sysUserQuery, Authentication authentication);

    UserDetails loadUserByUsername(String username);

    int editUser(SysUserQuery sysUserQuery, Authentication authentication);

    int delUserByIds(String ids);

    int delUserById(Long id);
}
