package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.CategoryBrandRelationDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryBrandRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CategoryBrandRelationDo record);

    int insertSelective(CategoryBrandRelationDo record);

    CategoryBrandRelationDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CategoryBrandRelationDo record);

    int updateByPrimaryKey(CategoryBrandRelationDo record);

    List<CategoryBrandRelationDo> selectRelationsByBrandId(Long brandId);

    CategoryBrandRelationDo selectByCategoryIdAndBrandId(@Param("brandId") Long brandId, @Param("categoryId") Long categoryId);
}