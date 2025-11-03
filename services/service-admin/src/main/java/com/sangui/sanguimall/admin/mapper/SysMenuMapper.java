package com.sangui.sanguimall.admin.mapper;

import com.sangui.sanguimall.admin.model.entity.SysMenu;

import java.util.List;

public interface SysMenuMapper {
    int deleteByPrimaryKey(Long menuId);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Long menuId);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    List<SysMenu> selectButtonPermissionByUserId(Long userId);

    List<SysMenu> selectMenuPermissionByUserId(Long userId);
}