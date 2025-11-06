package com.sangui.sanguimall.admin.model.vo;


import lombok.Data;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-06
 * @Description: 详细的 Role Vo
 * @Version: 1.0
 */
@Data
public class SysRoleDetailVo {
    private Long id;
    private String roleName;
    private String remark;
    private List<UserVoForRole> userList;
    private Long userCount;
}
