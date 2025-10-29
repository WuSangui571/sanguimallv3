package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.CategoryDo;

import java.util.List;

/**
 * @author sangui
 */
public interface CategoryMapper {
    int deleteByPrimaryKey(Long catId);

    int insert(CategoryDo record);

    int insertSelective(CategoryDo record);

    CategoryDo selectByPrimaryKey(Long catId);

    int updateByPrimaryKeySelective(CategoryDo record);

    int updateByPrimaryKey(CategoryDo record);

    List<CategoryDo> selectAll();
}