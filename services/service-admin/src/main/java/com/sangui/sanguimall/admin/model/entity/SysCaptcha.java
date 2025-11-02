package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 系统验证码
 * sys_captcha
 */
@Data
public class SysCaptcha implements Serializable {
    /**
     * uuid
     */
    private String uuid;

    /**
     * 验证码
     */
    private String code;

    /**
     * 过期时间
     */
    private Date expireTime;

    private static final long serialVersionUID = 1L;
}