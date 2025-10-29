package com.sangui.sanguimall.product.model.converter;


import com.sangui.sanguimall.product.model.entity.CategoryDo;
import com.sangui.sanguimall.product.model.vo.CategoryVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: CategoryConverter
 * @Version: 1.0
 */
@Mapper(componentModel = "spring")
public interface CategoryConverter {
    CategoryConverter INSTANCE = Mappers.getMapper(CategoryConverter.class);

    /**
     * DO → VO
     * childList 由 Service 手动填充，避免递归死循环
     */

    @Mapping(source = "catId", target = "id")
    @Mapping(source = "name", target = "label")
    @Mapping(target = "childList", ignore = true)
//    @Mapping(source = "id", target = "catId")
//    @Mapping(source = "label", target = "name")
    CategoryVo doToVo(CategoryDo categoryDo);

    //List<CategoryVo> doListToVoList(List<CategoryDo> categoryDos);
}
