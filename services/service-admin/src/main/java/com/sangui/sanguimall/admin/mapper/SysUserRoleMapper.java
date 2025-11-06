package com.sangui.sanguimall.admin.mapper;

import com.sangui.sanguimall.admin.model.entity.SysRole;
import com.sangui.sanguimall.admin.model.entity.SysUserRole;

import java.util.List;

/**
 * @author sangui
 */
public interface SysUserRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    SysUserRole selectByUserId(Long userId);

    List<SysUserRole> selectByUserIds(String ids);

    int deleteByIds(String ids);

    List<SysRole> selectByRoleId(Long roleId);
}