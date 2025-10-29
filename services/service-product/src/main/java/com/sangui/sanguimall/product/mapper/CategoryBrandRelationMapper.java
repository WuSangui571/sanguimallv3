package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.CategoryBrandRelationDo;

public interface CategoryBrandRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CategoryBrandRelationDo record);

    int insertSelective(CategoryBrandRelationDo record);

    CategoryBrandRelationDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CategoryBrandRelationDo record);

    int updateByPrimaryKey(CategoryBrandRelationDo record);
}