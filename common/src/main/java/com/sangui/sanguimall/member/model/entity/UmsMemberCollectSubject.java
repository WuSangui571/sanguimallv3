package com.sangui.sanguimall.member.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 会员收藏的专题活动
 * ums_member_collect_subject
 */
@Data
public class UmsMemberCollectSubject implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * subject_id
     */
    private Long subjectId;

    /**
     * subject_name
     */
    private String subjectName;

    /**
     * subject_img
     */
    private String subjectImg;

    /**
     * 活动url
     */
    private String subjectUrll;

    private static final long serialVersionUID = 1L;
}