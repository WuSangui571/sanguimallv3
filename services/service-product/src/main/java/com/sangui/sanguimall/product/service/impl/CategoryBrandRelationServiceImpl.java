package com.sangui.sanguimall.product.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.constant.Constants;
import com.sangui.sanguimall.product.manager.CateBrandRelationManager;
import com.sangui.sanguimall.product.mapper.CategoryBrandRelationMapper;
import com.sangui.sanguimall.product.model.converter.CategoryBrandRelationConverter;
import com.sangui.sanguimall.product.model.entity.CategoryBrandRelationDo;
import com.sangui.sanguimall.product.model.query.CategoryBrandRelationQuery;
import com.sangui.sanguimall.product.model.vo.CategoryBrandRelationVo;
import com.sangui.sanguimall.product.service.CategoryBrandRelationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-12
 * @Description:
 * @Version: 1.0
 */
@Service
public class CategoryBrandRelationServiceImpl implements CategoryBrandRelationService {
    @Resource
    CategoryBrandRelationMapper categoryBrandRelationMapper;

    @Resource
    CategoryBrandRelationConverter categoryBrandRelationConverter;

    @Resource
    CateBrandRelationManager cateBrandRelationManager;


    @Override
    public PageInfo<CategoryBrandRelationVo> getRelationsById(Integer current, Long brandId) {
        return cateBrandRelationManager.getRelationsById(current,brandId);
    }

    @Override
    public int addRelation(CategoryBrandRelationQuery categoryBrandRelationQuery) {
        return cateBrandRelationManager.addRelation(categoryBrandRelationQuery);
    }

    @Override
    public int delRelationById(Long id) {
        return categoryBrandRelationMapper.deleteByPrimaryKey(id);
    }
}
