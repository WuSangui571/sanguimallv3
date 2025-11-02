package com.sangui.sanguimall.admin.service.impl;


import com.sangui.sanguimall.admin.mapper.SysUserMapper;
import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.admin.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("登录账号不存在！");
        }

//        // 更新上次登录时间
//        sysUser.setLastLoginTime(new Date());
//        userMapper.updateByPrimaryKey(tUser);

        return sysUser;
    }
}
