package com.sangui.sanguimall.product.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.constant.Constants;
import com.sangui.sanguimall.product.mapper.CategoryBrandRelationMapper;
import com.sangui.sanguimall.product.model.converter.CategoryBrandRelationConverter;
import com.sangui.sanguimall.product.model.entity.CategoryBrandRelationDo;
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

    @Override
    public PageInfo<CategoryBrandRelationVo> getRelationsById(Integer current, Long brandId) {
        // 1. 设置 PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        // 2. 查询
        List<CategoryBrandRelationDo> doList = categoryBrandRelationMapper.selectRelationsByBrandId(brandId);
        List<CategoryBrandRelationVo> voList = new ArrayList<>();
        for (CategoryBrandRelationDo categoryBrandRelationDo: doList){
            voList.add(categoryBrandRelationConverter.doToVo(categoryBrandRelationDo));
        }
        // 3. 封装分页数据到 PageInfo
        return new PageInfo<>(voList);
    }
}
