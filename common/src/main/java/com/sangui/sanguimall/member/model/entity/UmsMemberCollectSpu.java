package com.sangui.sanguimall.member.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 会员收藏的商品
 * ums_member_collect_spu
 */
@Data
public class UmsMemberCollectSpu implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * spu_id
     */
    private Long spuId;

    /**
     * spu_name
     */
    private String spuName;

    /**
     * spu_img
     */
    private String spuImg;

    /**
     * create_time
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}