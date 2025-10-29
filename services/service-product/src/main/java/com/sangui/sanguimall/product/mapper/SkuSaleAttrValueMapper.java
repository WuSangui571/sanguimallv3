package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.SkuSaleAttrValueDo;

public interface SkuSaleAttrValueMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SkuSaleAttrValueDo record);

    int insertSelective(SkuSaleAttrValueDo record);

    SkuSaleAttrValueDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SkuSaleAttrValueDo record);

    int updateByPrimaryKey(SkuSaleAttrValueDo record);
}