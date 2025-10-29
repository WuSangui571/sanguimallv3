package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.SkuImagesDo;

public interface SkuImagesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SkuImagesDo record);

    int insertSelective(SkuImagesDo record);

    SkuImagesDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SkuImagesDo record);

    int updateByPrimaryKey(SkuImagesDo record);
}