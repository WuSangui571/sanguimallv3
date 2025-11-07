package com.sangui.sanguimall.admin.mapper;

import com.sangui.sanguimall.admin.model.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sangui
 */
public interface SysUserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser selectByUsername(String username);

    List<SysUser> selectSysUsersByPage();

    // 即将删除
    SysUser selectByIdWithCreateUserName(Long id);

    SysUser selectByIdWithCreateUserNameAndRole(Long id);

    int deleteByIds(String ids);

    List<SysUser> selectUsersByRoleId(Long roleId);

    List<SysUser> selectSysUsersBySearch(@Param("selectKey") String selectKey, @Param("selectValue") String selectValue);
}