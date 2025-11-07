package com.sangui.sanguimall.admin.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.admin.manager.SysUserManager;
import com.sangui.sanguimall.admin.mapper.SysUserMapper;
import com.sangui.sanguimall.admin.model.converter.SysUserConverter;
import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.admin.model.query.SysUserQuery;
import com.sangui.sanguimall.admin.model.vo.SysUserVo;
import com.sangui.sanguimall.admin.service.SysUserService;
import com.sangui.sanguimall.constant.Constants;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Resource
    SysUserConverter sysUserConverter;

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
    public PageInfo<SysUser> getSysUsersBySearch(Integer current, String selectKey, String selectValue) {
        // 1. 设置 PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        // 2. 查询
        List<SysUser> list = sysUserMapper.selectSysUsersBySearch(selectKey,selectValue);
        // 3. 封装分页数据到 PageInfo
        return new PageInfo<>(list);
    }

    @Override
    public SysUserVo getUserDetailById(Long id) {

        SysUser sysUser = sysUserMapper.selectByIdWithCreateUserNameAndRole(id);
        System.out.println("sysUser=" + sysUser);
        return sysUserConverter.doToVo(sysUser);
    }

    @Override
    public int addUser(SysUserQuery sysUserQuery, Authentication authentication) {
        System.out.println("sysRoleQuery=" + sysUserQuery);
        System.out.println("authentication=" + authentication);
        return sysUserManager.addUser(sysUserQuery, authentication);
    }

    @Override
    public int editUser(SysUserQuery sysUserQuery, Authentication authentication) {
        System.out.println("sysUserQuery=" + sysUserQuery);
        System.out.println("authentication=" + authentication);
        return sysUserManager.editUser(sysUserQuery,authentication);
    }

    @Override
    public int delUserByIds(String ids) {
        return sysUserManager.delUserByIds(ids);
    }

    @Override
    public int delUserById(Long id) {
        return sysUserManager.delUserById(id);
    }


}
