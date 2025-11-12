package com.sangui.sanguimall.product.model.converter;


import com.sangui.sanguimall.product.model.entity.CategoryBrandRelationDo;
import com.sangui.sanguimall.product.model.vo.CategoryBrandRelationVo;

import org.mapstruct.Mapper;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-12
 * @Description:
 * @Version: 1.0
 */
@Mapper(componentModel = "spring")
public interface CategoryBrandRelationConverter {
    /**
     * DO → VO
     */
    CategoryBrandRelationVo doToVo(CategoryBrandRelationDo categoryBrandRelationDo);
}
