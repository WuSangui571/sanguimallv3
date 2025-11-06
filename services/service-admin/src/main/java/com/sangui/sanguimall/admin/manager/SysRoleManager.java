package com.sangui.sanguimall.admin.manager;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.admin.model.vo.SysRoleDetailVo;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-06
 * @Description:
 * @Version: 1.0
 */
public interface SysRoleManager {
    PageInfo<SysRoleDetailVo> getRolesByPage(Integer current);

    SysRoleDetailVo getRoleDetailByRoleId(Long id);

    int delRoleById(Long id);
}
