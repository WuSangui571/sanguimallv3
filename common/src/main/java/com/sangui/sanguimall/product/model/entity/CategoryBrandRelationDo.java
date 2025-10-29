package com.sangui.sanguimall.product.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 品牌分类关联
 * pms_category_brand_relation
 */
@Data
public class CategoryBrandRelationDo implements Serializable {
    private Long id;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 分类id
     */
    private Long catelogId;

    private String brandName;

    private String catelogName;

    private static final long serialVersionUID = 1L;
}