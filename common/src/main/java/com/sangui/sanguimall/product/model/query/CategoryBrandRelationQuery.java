package com.sangui.sanguimall.product.model.query;


import lombok.Data;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-13
 * @Description:
 * @Version: 1.0
 */
@Data
public class CategoryBrandRelationQuery {
    private Long id;
    private Long brandId;
    private Long categoryId;
}
