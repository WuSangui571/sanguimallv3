package com.sangui.sanguimall.admin.mapper;

import com.sangui.sanguimall.admin.model.entity.SysRole;
import com.sangui.sanguimall.admin.model.entity.SysUser;

import java.util.List;

/**
 * @author sangui
 */
public interface SysRoleMapper {
    int deleteByPrimaryKey(Long roleId);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> selectByUserId(Long userId);

    List<SysRole> selectAll();

    List<SysUser> selectUsersByRoleId(Long roleId);
}