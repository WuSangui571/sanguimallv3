package com.sangui.sanguimall.product.model.converter;


import com.sangui.sanguimall.product.model.entity.CategoryBrandRelationDo;
import com.sangui.sanguimall.product.model.vo.CategoryBrandRelationVo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    @Mapping(target = "path", ignore = true)
    CategoryBrandRelationVo doToVo(CategoryBrandRelationDo categoryBrandRelationDo);
}
