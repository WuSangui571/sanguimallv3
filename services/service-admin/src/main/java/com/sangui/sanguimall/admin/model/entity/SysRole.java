package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 角色
 * sys_role
 */
@Data
public class SysRole implements Serializable {
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}