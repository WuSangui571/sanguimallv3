package com.sangui.sanguimall.member.model.query;

import lombok.Data;

@Data
public class MemberPageQuery {
    /**
     * 当前页码（默认 1）
     */
    private Integer current;

    /**
     * 模糊查询关键词，匹配用户名/昵称/手机/邮箱
     */
    private String keyword;

    /**
     * 会员等级筛选
     */
    private Long levelId;

    /**
     * 状态筛选（0 禁用 / 1 启用）
     */
    private Integer status;
}
