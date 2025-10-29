package com.sangui.sanguimall.product.mapper;


import com.sangui.sanguimall.product.model.entity.SpuInfoDescDo;

public interface SpuInfoDescMapper {
    int deleteByPrimaryKey(Long spuId);

    int insert(SpuInfoDescDo record);

    int insertSelective(SpuInfoDescDo record);

    SpuInfoDescDo selectByPrimaryKey(Long spuId);

    int updateByPrimaryKeySelective(SpuInfoDescDo record);

    int updateByPrimaryKey(SpuInfoDescDo record);
}