package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.BrandDo;

public interface BrandMapper {
    int deleteByPrimaryKey(Long brandId);

    int insert(BrandDo record);

    int insertSelective(BrandDo record);

    BrandDo selectByPrimaryKey(Long brandId);

    int updateByPrimaryKeySelective(BrandDo record);

    int updateByPrimaryKey(BrandDo record);
}