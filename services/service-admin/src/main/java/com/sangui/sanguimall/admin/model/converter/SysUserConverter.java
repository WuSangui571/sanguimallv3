package com.sangui.sanguimall.admin.model.converter;


import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.admin.model.vo.SysUserVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-04
 * @Description:
 * @Version: 1.0
 */
@Mapper(componentModel = "spring", uses = {SysRoleConverter.class})
public interface SysUserConverter {
    @Mapping(target = "id", source = "userId")
    @Mapping(target = "createByVo", source = "createByDo", qualifiedByName = "toSimpleVo")
    @Mapping(target = "roleVo", source = "sysRole") // 会调用 SysRoleConverter
    SysUserVo doToVo(SysUser sysUser);

    // 简化版转换，避免无限递归
    @Named("toSimpleVo")
    @Mapping(target = "id", source = "userId")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "createTime", source = "createTime")
    SysUserVo doToSimpleVo(SysUser sysUser);
}
