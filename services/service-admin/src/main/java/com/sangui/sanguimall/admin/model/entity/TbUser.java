package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户
 * tb_user
 */
@Data
public class TbUser implements Serializable {
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}