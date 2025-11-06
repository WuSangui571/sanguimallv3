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
import com.sangui.sanguimall.admin.model.query.SysUserQuery;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
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
    public int editUser(SysUserQuery sysUserQuery, Authentication authentication) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserQuery, sysUser);
        sysUser.setUserId(sysUserQuery.getId());

        Long roleId = sysUserQuery.getRoleId();

        SysUserRole sysUserRole = sysUserRoleMapper.selectByUserId(sysUser.getUserId());
        sysUserRole.setRoleId(roleId);
        sysUserRoleMapper.updateByPrimaryKey(sysUserRole);

        // 若改变密码，加密
        if (sysUser.getPassword().isEmpty()) {
            sysUser.setPassword(null);
        }else if (sysUser.getPassword().length() < 6 || sysUser.getPassword().length() > 16) {
            return 0;
        }else{
            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        }

        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public int delUserByIds(String ids) {
        // 先删除 user-role 表里对应的内容
        List<SysUserRole> userRoleList= sysUserRoleMapper.selectByUserIds(ids);

        StringBuilder newIds = new StringBuilder();
        for (SysUserRole sysUserRole:userRoleList){
            newIds.append(sysUserRole.getId()).append(",");
        }
        newIds.deleteCharAt(newIds.length() - 1);

        int count1 = sysUserRoleMapper.deleteByIds(newIds.toString());

        // 再删除 user 表对应的内容
        int count2 = sysUserMapper.deleteByIds(ids);

        return count1 + count2;
    }

    @Override
    public int delUserById(Long id) {
        // 先删除 user-role 表里对应的内容
        SysUserRole sysUserRole = sysUserRoleMapper.selectByUserId(id);
        int count1 = sysUserRoleMapper.deleteByPrimaryKey(sysUserRole.getId());

        // 再删除 user 表对应的内容
        int count2 = sysUserMapper.deleteByPrimaryKey(id);
        return count1 + count2;
    }

    @Override
    public int addUser(SysUserQuery sysUserQuery, Authentication authentication) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(sysUserQuery.getUsername());
        sysUser.setPassword(passwordEncoder.encode(sysUserQuery.getPassword()));
        sysUser.setEmail(sysUserQuery.getEmail());
        sysUser.setMobile(sysUserQuery.getMobile());
        System.out.println("sysUserQuery.getStatus()=" + sysUserQuery.getStatus());
        sysUser.setStatus(sysUserQuery.getStatus());
        sysUser.setCreateUserId(((SysUser) authentication.getPrincipal()).getUserId());
        sysUser.setCreateTime(new Date());

        int count1 = sysUserMapper.insert(sysUser);

        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(sysUser.getUserId());
        sysUserRole.setRoleId(sysUserQuery.getRoleId());
        int count2 = sysUserRoleMapper.insert(sysUserRole);

        return count1 + count2;
    }
}
