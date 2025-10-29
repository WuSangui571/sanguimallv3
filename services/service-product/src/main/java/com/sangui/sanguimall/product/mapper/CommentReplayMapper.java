package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.CommentReplayDo;


public interface CommentReplayMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CommentReplayDo record);

    int insertSelective(CommentReplayDo record);

    CommentReplayDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CommentReplayDo record);

    int updateByPrimaryKey(CommentReplayDo record);
}