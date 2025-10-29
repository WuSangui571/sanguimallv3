package com.sangui.sanguimall.product.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 商品评价回复关系
 * pms_comment_replay
 */
@Data
public class CommentReplayDo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 评论id
     */
    private Long commentId;

    /**
     * 回复id
     */
    private Long replyId;

    private static final long serialVersionUID = 1L;
}