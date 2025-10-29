package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.SpuImagesDo;

public interface SpuImagesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SpuImagesDo record);

    int insertSelective(SpuImagesDo record);

    SpuImagesDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SpuImagesDo record);

    int updateByPrimaryKey(SpuImagesDo record);
}