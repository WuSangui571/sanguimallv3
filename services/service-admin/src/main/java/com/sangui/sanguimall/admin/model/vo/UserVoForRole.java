package com.sangui.sanguimall.admin.model.vo;


import lombok.Data;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-06
 * @Description: 专门给 SysRoleVo 准备的 UserVo
 * @Version: 1.0
 */
@Data
public class UserVoForRole {
    private Long userId;
    private String username;
}
