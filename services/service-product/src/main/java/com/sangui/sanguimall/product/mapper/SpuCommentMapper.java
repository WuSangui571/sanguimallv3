package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.SpuCommentDo;

public interface SpuCommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SpuCommentDo record);

    int insertSelective(SpuCommentDo record);

    SpuCommentDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SpuCommentDo record);

    int updateByPrimaryKey(SpuCommentDo record);
}