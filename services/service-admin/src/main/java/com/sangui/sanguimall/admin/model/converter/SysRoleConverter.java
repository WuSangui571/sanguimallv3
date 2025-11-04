package com.sangui.sanguimall.admin.model.converter;


import com.sangui.sanguimall.admin.model.entity.SysRole;
import com.sangui.sanguimall.admin.model.vo.SysRoleVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-04
 * @Description:
 * @Version: 1.0
 */
@Mapper(componentModel = "spring")
public interface SysRoleConverter {
    /**
     * DO → VO
     */
    @Mapping(target = "id",source = "roleId")
    @Mapping(source = "remark", target = "typeValue")
    SysRoleVo doToVo(SysRole sysRole);
}
