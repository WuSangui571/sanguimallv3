package com.sangui.sanguimall.member.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 会员登录记录
 * ums_member_login_log
 */
@Data
public class UmsMemberLoginLog implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * member_id
     */
    private Long memberId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * ip
     */
    private String ip;

    /**
     * city
     */
    private String city;

    /**
     * 登录类型[1-web，2-app]
     */
    private Boolean loginType;

    private static final long serialVersionUID = 1L;
}