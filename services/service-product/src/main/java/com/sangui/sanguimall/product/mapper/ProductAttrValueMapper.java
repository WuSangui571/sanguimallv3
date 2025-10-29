package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.ProductAttrValueDo;

public interface ProductAttrValueMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductAttrValueDo record);

    int insertSelective(ProductAttrValueDo record);

    ProductAttrValueDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductAttrValueDo record);

    int updateByPrimaryKey(ProductAttrValueDo record);
}