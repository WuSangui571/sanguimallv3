package com.sangui.sanguimall.product.manager;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.product.model.query.CategoryBrandRelationQuery;
import com.sangui.sanguimall.product.model.vo.CategoryBrandRelationVo;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-13
 * @Description:
 * @Version: 1.0
 */
public interface CateBrandRelationManager {
    int addRelation(CategoryBrandRelationQuery categoryBrandRelationQuery);

    PageInfo<CategoryBrandRelationVo> getRelationsById(Integer current, Long brandId);
}
