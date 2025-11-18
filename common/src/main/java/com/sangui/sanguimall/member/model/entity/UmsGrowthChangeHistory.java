package com.sangui.sanguimall.member.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 成长值变化历史记录
 * ums_growth_change_history
 */
@Data
public class UmsGrowthChangeHistory implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * member_id
     */
    private Long memberId;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * 改变的值（正负计数）
     */
    private Integer changeCount;

    /**
     * 备注
     */
    private String note;

    /**
     * 积分来源[0-购物，1-管理员修改]
     */
    private Byte sourceType;

    private static final long serialVersionUID = 1L;
}