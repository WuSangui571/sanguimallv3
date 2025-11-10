package com.sangui.sanguimall.product.model.converter;


import com.sangui.sanguimall.product.model.entity.BrandDo;
import com.sangui.sanguimall.product.model.entity.CategoryDo;
import com.sangui.sanguimall.product.model.vo.CategoryVo;
import com.sangui.sanguimall.product.model.vo.SingleBrandVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-10
 * @Description:
 * @Version: 1.0
 */
@Mapper(componentModel = "spring")
public interface BrandConverter {
    /**
     * DO → VO
     */
    @Mapping(target = "id",source = "brandId")
    SingleBrandVo doToVo(BrandDo brandDo);
}
