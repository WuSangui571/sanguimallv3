package com.sangui.sanguimall.product.model.converter;


import com.sangui.sanguimall.product.model.entity.CategoryDo;
import com.sangui.sanguimall.product.model.vo.CategoryVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: CategoryConverter
 * @Version: 1.0
 */
@Mapper(componentModel = "spring")
public interface CategoryConverter {
    //CategoryConverter INSTANCE = Mappers.getMapper(CategoryConverter.class);

    /**
     * DO → VO
     */
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "id",source = "catId")
    @Mapping(source = "name", target = "label")
    @Mapping(source = "catLevel", target = "level")
    CategoryVo doToVo(CategoryDo categoryDo);

}
