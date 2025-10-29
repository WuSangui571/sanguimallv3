package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.SkuInfoDo;

public interface SkuInfoMapper {
    int deleteByPrimaryKey(Long skuId);

    int insert(SkuInfoDo record);

    int insertSelective(SkuInfoDo record);

    SkuInfoDo selectByPrimaryKey(Long skuId);

    int updateByPrimaryKeySelective(SkuInfoDo record);

    int updateByPrimaryKey(SkuInfoDo record);
}