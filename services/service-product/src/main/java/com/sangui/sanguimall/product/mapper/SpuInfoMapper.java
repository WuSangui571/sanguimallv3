package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.SpuInfoDo;

public interface SpuInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SpuInfoDo record);

    int insertSelective(SpuInfoDo record);

    SpuInfoDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SpuInfoDo record);

    int updateByPrimaryKey(SpuInfoDo record);
}