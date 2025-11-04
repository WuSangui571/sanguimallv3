package com.sangui.sanguimall.admin.model.vo;


import lombok.Data;

import java.util.Date;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-04
 * @Description:
 * @Version: 1.0
 */
@Data
public class SysUserVo {
    /**
     * 对应 SysUser 里的 userId 属性，即 SysUser 的 主键
     */
    private Long id;

    /**
     * 对应 SysUser 里的 username 属性，即 用户名
     */
    private String username;

    /**
     * 对应 SysUser 里的 password 属性，即 密码
     */
    private String password;

    /**
     * 对应 SysUser 里的 email 属性，即 邮箱
     */
    private String email;

    /**
     * 对应 SysUser 里的 mobile 属性，即 手机号
     */
    private String mobile;



    /**
     * 对应 SysUser 里的 status 属性，即 状态  0：禁用   1：正常
     */
    private Byte status;

    /**
     * 对应 SysUser 里的 createTime 属性，即 创建时间
     */
    private Date createTime;


    /**
     * 对应 SysUser 里的 createByDo 属性，是一个一对一关联的属性
     */
    private SysUserVo createByVo;

    /**
     * 对应 SysUser 里的 sysRole 属性，是一个一对一关联的属性
     */
    private SysRoleVo roleVo;
}
