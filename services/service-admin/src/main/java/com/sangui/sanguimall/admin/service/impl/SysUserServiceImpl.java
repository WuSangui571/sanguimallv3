package com.sangui.sanguimall.admin.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.admin.mapper.SysMenuMapper;
import com.sangui.sanguimall.admin.mapper.SysRoleMapper;
import com.sangui.sanguimall.admin.mapper.SysUserMapper;
import com.sangui.sanguimall.admin.model.entity.SysMenu;
import com.sangui.sanguimall.admin.model.entity.SysRole;
import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.admin.service.SysUserService;
import com.sangui.sanguimall.constant.Constants;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
    SysRoleMapper sysRoleMapper;

    @Resource
    SysMenuMapper sysMenuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
        sysUser.setMenuPermissionList(menuPermissionList);

        // 查询一下该用户有哪些功能权限
        List<SysMenu> buttonPermissionList = sysMenuMapper.selectButtonPermissionByUserId(sysUser.getUserId());
        List<String> stringPermissionList = new ArrayList<>();
        buttonPermissionList.forEach(buttonPermission -> {
            // 权限标识符
            stringPermissionList.add(buttonPermission.getPerms());
        });
        // 设置用户的权限标识符
        sysUser.setButtonPermissionList(stringPermissionList);

        return sysUser;
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
}
