package com.sangui.sanguimall.admin.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.admin.manager.SysRoleManager;
import com.sangui.sanguimall.admin.mapper.SysRoleMapper;
import com.sangui.sanguimall.admin.model.converter.SysRoleConverter;
import com.sangui.sanguimall.admin.model.entity.SysRole;
import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.admin.model.query.SysRoleQuery;
import com.sangui.sanguimall.admin.model.vo.SysRoleDetailVo;
import com.sangui.sanguimall.admin.model.vo.SysRoleVo;
import com.sangui.sanguimall.admin.service.SysRoleService;
import com.sangui.sanguimall.constant.Constants;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    @Resource
    SysRoleManager sysRoleManager;

    @Override
    public List<SysRoleVo> getAllRoles() {
        List<SysRole> sysRoles = sysRoleMapper.selectAll();
        List<SysRoleVo> sysRoleVoList = new ArrayList<>();
        for (SysRole sysRole: sysRoles){
            sysRoleVoList.add(sysRoleConverter.doToVo(sysRole));
        }
        return sysRoleVoList;
    }

    @Override
    public PageInfo<SysRoleDetailVo> getRolesByPage(Integer current) {
        return sysRoleManager.getRolesByPage(current);
    }

    @Override
    public SysRoleDetailVo getRoleDetailByRoleId(Long id) {
        return sysRoleManager.getRoleDetailByRoleId(id);
    }

    @Override
    public int addRole(SysRoleQuery sysRoleQuery, Authentication authentication) {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName(sysRoleQuery.getRoleName());
        sysRole.setRemark(sysRoleQuery.getRemark());
        sysRole.setCreateTime(new Date());

        // 添加创建人 id
        SysUser createByDo = (SysUser) authentication.getPrincipal();
        Long createBy = createByDo.getUserId();
        sysRole.setCreateUserId(createBy);

        return sysRoleMapper.insert(sysRole);
    }

    @Override
    public int editRole(SysRoleQuery sysRoleQuery, Authentication authentication) {
        SysRole sysRole = new SysRole();
        sysRole.setRoleId(sysRoleQuery.getId());
        sysRole.setRoleName(sysRoleQuery.getRoleName());
        sysRole.setRemark(sysRoleQuery.getRemark());

        return sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    @Override
    public int delRoleById(Long id) {
        return sysRoleManager.delRoleById(id);
    }
}
