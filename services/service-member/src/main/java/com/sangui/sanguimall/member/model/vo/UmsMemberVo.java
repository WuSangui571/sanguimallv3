package com.sangui.sanguimall.member.model.vo;

import java.util.Date;
import lombok.Data;

@Data
public class UmsMemberVo {
    private Long id;
    private Long levelId;
    private String levelName;
    private String username;
    private String nickname;
    private String mobile;
    private String email;
    private String header;
    private Byte gender;
    private Date birth;
    private String city;
    private String job;
    private String sign;
    private Byte sourceType;
    private Integer integration;
    private Integer growth;
    private Byte status;
    private Date createTime;
    /**
     * 格式化后的注册时间（北京时间）
     */
    private String createTimeText;
}
