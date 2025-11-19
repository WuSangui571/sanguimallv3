package com.sangui.sanguimall.member.model.vo;

import java.util.Date;
import lombok.Data;

@Data
public class GrowthChangeHistoryVo {
    private Long id;
    private Long memberId;
    private String username;
    private String nickname;
    private String mobile;
    private String email;
    private Integer changeCount;
    private String note;
    private Byte sourceType;
    private Date createTime;
    private String createTimeText;
}
