package com.sangui.sanguimall.product.model.converter;


import com.sangui.sanguimall.product.model.entity.AttrDo;
import com.sangui.sanguimall.product.model.vo.AttrVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-16
 * @Description:
 * @Version: 1.0
 */
@Mapper(
        componentModel = "spring",
        // 复用 CategoryConverter
        uses = {CategoryConverter.class}
)
public interface AttrConverter {
    /**
     * DO → VO
     */
    @Mapping(target = "categoryVo", source = "categoryDo")
    AttrVo doToVo(AttrDo attrDo);
}
