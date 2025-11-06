package com.sangui.sanguimall.admin.service;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.admin.model.query.SysRoleQuery;
import com.sangui.sanguimall.admin.model.vo.SysRoleDetailVo;
import com.sangui.sanguimall.admin.model.vo.SysRoleVo;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-04
 * @Description:
 * @Version: 1.0
 */
public interface SysRoleService {

    List<SysRoleVo> getAllRoles();

    PageInfo<SysRoleDetailVo> getRolesByPage(Integer current);

    SysRoleDetailVo getRoleDetailByRoleId(Long id);

    int addRole(SysRoleQuery sysRoleQuery, Authentication authentication);

    int editRole(SysRoleQuery sysRoleQuery, Authentication authentication);

    int delRoleById(Long id);
}
