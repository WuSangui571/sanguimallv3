package com.sangui.sanguimall.product.manager.impl;


import com.sangui.sanguimall.product.manager.CategoryManager;
import com.sangui.sanguimall.product.mapper.CategoryBrandRelationMapper;
import com.sangui.sanguimall.product.mapper.CategoryMapper;
import com.sangui.sanguimall.product.model.entity.CategoryBrandRelationDo;
import com.sangui.sanguimall.product.model.entity.CategoryDo;
import com.sangui.sanguimall.product.model.query.CategoryQuery;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-14
 * @Description:
 * @Version: 1.0
 */
@Service
@Transactional
public class CategoryManagerImpl implements CategoryManager {
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    CategoryBrandRelationMapper categoryBrandRelationMapper;

    @Override
    public int editCategory(CategoryQuery categoryQuery) {
        Long catId = categoryQuery.getCatId();
        CategoryDo categoryDo = categoryMapper.selectByPrimaryKey(catId);
        categoryDo.setName(categoryQuery.getNewNodeLabel());

        List<CategoryBrandRelationDo> categoryBrandRelationDoList = categoryBrandRelationMapper.selectByCategoryId(categoryDo.getCatId());
        if (!categoryBrandRelationDoList.isEmpty()){
            for (CategoryBrandRelationDo categoryBrandRelationDo:categoryBrandRelationDoList){
                categoryBrandRelationDo.setCatelogName(categoryQuery.getNewNodeLabel());
                categoryBrandRelationMapper.updateByPrimaryKeySelective(categoryBrandRelationDo);
            }
        }
        return categoryMapper.updateByPrimaryKeySelective(categoryDo);
    }
}
