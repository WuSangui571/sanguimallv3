package com.sangui.sanguimall.admin.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.admin.manager.SysUserManager;
import com.sangui.sanguimall.admin.mapper.SysMenuMapper;
import com.sangui.sanguimall.admin.mapper.SysRoleMapper;
import com.sangui.sanguimall.admin.mapper.SysUserMapper;
import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.admin.model.query.SysRoleQuery;
import com.sangui.sanguimall.admin.service.SysUserService;
import com.sangui.sanguimall.constant.Constants;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-02
 * @Description: SysUserServiceImpl
 * @Version: 1.0
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    SysUserManager sysUserManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return sysUserManager.loadUserByUsername(username);
    }

    @Override
    public PageInfo<SysUser> getSysUsersByPage(Integer current) {
        // 1. 设置 PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        // 2. 查询
        List<SysUser> list = sysUserMapper.selectSysUsersByPage();
        // 3. 封装分页数据到 PageInfo
        return new PageInfo<>(list);
    }

    @Override
    public SysUser getUserDetailById(Long id) {
        return sysUserMapper.selectByIdWithCreateUserName(id);
    }

    @Override
    public int addUser(SysRoleQuery sysRoleQuery, Authentication authentication) {
        System.out.println("sysRoleQuery=" + sysRoleQuery);
        System.out.println("authentication=" + authentication);
        return sysUserManager.addUser(sysRoleQuery, authentication);
    }
}
