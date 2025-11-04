package com.sangui.sanguimall.admin.manager.impl;


import com.sangui.sanguimall.admin.manager.SysUserManager;
import com.sangui.sanguimall.admin.mapper.SysMenuMapper;
import com.sangui.sanguimall.admin.mapper.SysRoleMapper;
import com.sangui.sanguimall.admin.mapper.SysUserMapper;
import com.sangui.sanguimall.admin.mapper.SysUserRoleMapper;
import com.sangui.sanguimall.admin.model.entity.SysMenu;
import com.sangui.sanguimall.admin.model.entity.SysRole;
import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.admin.model.entity.SysUserRole;
import com.sangui.sanguimall.admin.model.query.SysRoleQuery;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-04
 * @Description:
 * @Version: 1.0
 */
@Transactional
@Service
public class SysUserManagerImpl implements SysUserManager {
    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    SysMenuMapper sysMenuMapper;

    @Resource
    SysUserRoleMapper sysUserRoleMapper;

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("登录账号不存在！");
        }

        // 查询一下当前用户的角色信息
        List<SysRole> roleList = sysRoleMapper.selectByUserId(sysUser.getUserId());
        // 字符串的角色列表
        List<String> stringRoleList = new ArrayList<>();
        roleList.forEach(role -> {
            stringRoleList.add(role.getRoleName());
        });
        // 设置用户的角色
        sysUser.setRoleList(stringRoleList);

        // 查询一下该用户有哪些菜单权限
        List<SysMenu> menuPermissionList = sysMenuMapper.selectMenuPermissionByUserId(sysUser.getUserId());
        System.out.println("menuPermissionList=" + menuPermissionList);
        sysUser.setMenuPermissionList(menuPermissionList);

        return sysUser;
    }

    @Override
    public int addUser(SysRoleQuery sysRoleQuery, Authentication authentication) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(sysRoleQuery.getUsername());
        sysUser.setPassword(passwordEncoder.encode(sysRoleQuery.getPassword()));
        sysUser.setEmail(sysRoleQuery.getEmail());
        sysUser.setMobile(sysRoleQuery.getMobile());
        sysUser.setStatus(sysRoleQuery.getStatus());
        sysUser.setCreateUserId(((SysUser) authentication.getPrincipal()).getUserId());
        sysUser.setCreateTime(new Date());

        int count1 = sysUserMapper.insert(sysUser);

        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(sysUser.getUserId());
        sysUserRole.setRoleId(sysRoleQuery.getRoleId());
        int count2 = sysUserRoleMapper.insert(sysUserRole);

        return count1 + count2;
    }
}
