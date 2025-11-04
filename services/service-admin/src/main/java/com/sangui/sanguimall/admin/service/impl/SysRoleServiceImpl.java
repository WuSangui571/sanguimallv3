package com.sangui.sanguimall.admin.service.impl;


import com.sangui.sanguimall.admin.mapper.SysRoleMapper;
import com.sangui.sanguimall.admin.model.converter.SysRoleConverter;
import com.sangui.sanguimall.admin.model.entity.SysRole;
import com.sangui.sanguimall.admin.model.vo.SysRoleVo;
import com.sangui.sanguimall.admin.service.SysRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-04
 * @Description:
 * @Version: 1.0
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysRoleConverter sysRoleConverter;

    @Override
    public List<SysRoleVo> getAllRoles() {
        List<SysRole> sysRoles = sysRoleMapper.selectAll();
        List<SysRoleVo> sysRoleVoList = new ArrayList<>();
        for (SysRole sysRole: sysRoles){
            sysRoleVoList.add(sysRoleConverter.doToVo(sysRole));
        }
        return sysRoleVoList;
    }
}
