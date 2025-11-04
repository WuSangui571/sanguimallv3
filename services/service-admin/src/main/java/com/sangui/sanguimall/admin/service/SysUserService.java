package com.sangui.sanguimall.admin.service;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.admin.model.query.SysRoleQuery;
import com.sangui.sanguimall.admin.model.vo.SysUserVo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-02
 * @Description: SysUserService
 * @Version: 1.0
 */
public interface SysUserService extends UserDetailsService {
    PageInfo<SysUser> getSysUsersByPage(Integer current);

    SysUserVo getUserDetailById(Long id);

    int addUser(SysRoleQuery sysRoleQuery, Authentication authentication);
}
