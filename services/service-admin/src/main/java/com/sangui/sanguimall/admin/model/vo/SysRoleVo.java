package com.sangui.sanguimall.admin.model.vo;


import lombok.Data;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-04
 * @Description:
 * @Version: 1.0
 */
@Data
public class SysRoleVo {
    /**
     * 对应 SysRole 里的 role_id 属性，即 ROLE 的 主键
     */
    private int id;

    /**
     * 对应 SysRole 里的 remark 属性，即 ROLE 的 角色名称
     */
    private String typeValue;
}
