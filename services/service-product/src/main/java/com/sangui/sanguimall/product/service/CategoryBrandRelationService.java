package com.sangui.sanguimall.product.service;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.product.model.vo.CategoryBrandRelationVo;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-12
 * @Description:
 * @Version: 1.0
 */
public interface CategoryBrandRelationService {
    PageInfo<CategoryBrandRelationVo> getRelationsById(Integer current, Long brandId);
}
