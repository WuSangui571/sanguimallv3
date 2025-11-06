package com.sangui.sanguimall.admin.model.query;


import lombok.Data;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-04
 * @Description:
 * @Version: 1.0
 */
@Data
public class SysUserQuery {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String mobile;
    private Byte status;
    private Long roleId;
}
